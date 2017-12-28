package ru.easydata.sql.reversing.vertica.controllers

import getl.utils.Config
import getl.vertica.ReverseEngineering
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.cell.MapValueFactory
import javafx.stage.Window
import javafx.stage.WindowEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.services.Forms

@Component
class ListObjectController {
	enum SECTION {
		POOLS,ROLES,TABLES,VIEWS,USERS,SQL_FUNCTIONS,SEQUENCES,SCHEMAS,GRANTS
	}

	@Autowired
	private MessageSource messageSource

	@Autowired
	private Forms forms

	@FXML
	private Label labelTitle

	@FXML
	private TextArea textAreaSQL

	@FXML
	private TableView tableViewResultSQL

	@FXML
	private ProgressBar progressBarWait

	@FXML
	private Button butRun

	@FXML
	private Button butSave

	@FXML
	private Button butCancel

	private def doRun
	private SECTION section
	private boolean isSave = false

	private void addColumnName() {
		TableColumn<Map, Object> name = new TableColumn<Map, Object>(
				this.messageSource.getMessage('app.table-column.name', null, LocaleContextHolder.getLocale())
		)
		name.setCellValueFactory(new MapValueFactory<Object>('name'))
		this.tableViewResultSQL.getColumns().add(name)
	}
	private void addColumnSchema() {
		TableColumn<Map, Object> schema = new TableColumn<Map, Object>(
				this.messageSource.getMessage('app.table-column.schema', null, LocaleContextHolder.getLocale())
		)
		schema.setCellValueFactory(new MapValueFactory<Object>('schema'))
		this.tableViewResultSQL.getColumns().add(schema)
	}
	private void addColumnType() {
		TableColumn<Map, Object> type = new TableColumn<Map, Object>(
				this.messageSource.getMessage('app.table-column.type', null, LocaleContextHolder.getLocale())
		)
		type.setCellValueFactory(new MapValueFactory<Object>('type'))
		this.tableViewResultSQL.getColumns().add(type)
	}

	void setSection(SECTION section) {
		this.section = section
		this.isSave = false
		this.tableViewResultSQL.getItems().clear()
		this.tableViewResultSQL.getColumns().clear()
		Locale locale = LocaleContextHolder.getLocale()

		switch (section) {
			case SECTION.POOLS:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.pool', null, locale))
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listPools())
				}
				break
			case SECTION.ROLES:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.role', null, locale))
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listRoles())
				}
				break
			case SECTION.TABLES:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.table', null, locale))
				this.addColumnSchema()
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listTables())
				}
				break
			case SECTION.VIEWS:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.view', null, locale))
				this.addColumnSchema()
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listViews())
				}
				break
			case SECTION.USERS:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.user', null, locale))
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listUsers())
				}
				break
			case SECTION.SQL_FUNCTIONS:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.sql-function', null, locale))
				this.addColumnSchema()
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listSql_functions())
				}
				break
			case SECTION.SEQUENCES:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.sequence', null, locale))
				this.addColumnSchema()
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listSequences())
				}
				break
			case SECTION.SCHEMAS:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.schema', null, locale))
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listSchemas())
				}
				break
			case SECTION.GRANTS:
				this.labelTitle.setText(this.messageSource.getMessage('fxml.menu.left.grants', null, locale))
				this.addColumnType()
				this.addColumnSchema()
				this.addColumnName()
				this.doRun = {ReverseEngineering reverse ->
					return FXCollections.observableArrayList(reverse.listGrants())
				}
				break
		}
	}

	String getSQL() {
		return this.textAreaSQL.getText()
	}

	void setSQL(String sql) {
		this.textAreaSQL.setText(sql)
	}

	boolean isSave() {
		return this.isSave
	}

	void butAction(ActionEvent actionEvent) {
		Object source = actionEvent.getSource()

		if (!source instanceof Button) {
			return
		}

		Button menuItem = (Button) source

		switch (menuItem.id) {
			case 'butRun':
				this.run()
				break
			case 'butSave':
				this.isSave = this
				this.close()
				break
			case 'butCancel':
				this.close()
				break
		}
	}

	private void run() {
		this.progressBarWait.setVisible(true)
		this.butRun.setDisable(true)
		this.butCancel.setDisable(true)
		this.butSave.setDisable(true)
		this.butRun.getScene().setCursor(Cursor.WAIT)

		Map connections = (forms.jsonConnections().connections)
		//connections.vertica.remove('driverPath')
		Config.content.connections = connections
		Config.content.create = forms.jsonCreate().create
		Map vars = forms.jsonVars().vars
		if (vars != null) {
			vars.filter[this.section.name().toLowerCase()] = this.textAreaSQL.getText()
			Config.setVars(vars)
		}
		Config.EvalConfig()

		Locale locale = LocaleContextHolder.getLocale()

		new Thread(new Runnable() {
			@Override
			void run() {
				ReverseEngineering reverse = new ReverseEngineering()
				try {
					reverse.initReverse()
					tableViewResultSQL.setItems(doRun(reverse) as ObservableList<Map>)
				} catch (Exception e) {
					Platform.runLater(new Runnable() {
						@Override
						void run() {
							Alert alert = new Alert(Alert.AlertType.ERROR)
							alert.setTitle(messageSource.getMessage('app.alert.title.error', null, locale))
							alert.setHeaderText(messageSource.getMessage('app.alert.connection.error', null, locale))
							alert.setContentText(e.message)
							alert.showAndWait()
						}
					})
				} finally {
					try {
						reverse.doneReverse()
					} catch (Exception e) {
						//
					}

					Platform.runLater(new Runnable() {
						@Override
						void run() {
							progressBarWait.setVisible(false)
							butRun.setDisable(false)
							butCancel.setDisable(false)
							butSave.setDisable(false)
							butRun.getScene().setCursor(Cursor.DEFAULT)
						}
					})
				}
			}
		}).start()
	}

	private void close() {
		Window win = this.textAreaSQL.getScene().getWindow()
		win.fireEvent(new WindowEvent(win, WindowEvent.WINDOW_CLOSE_REQUEST))
	}
}