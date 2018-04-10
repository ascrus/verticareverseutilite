package ru.easydata.sql.reversing.vertica.controllers

import getl.utils.Config
import getl.vertica.VerticaConnection
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.ConnectionForm
import ru.easydata.sql.reversing.vertica.services.Forms

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class ConnectionController implements ConnectionForm {
	@Autowired
	private MessageSource messageSource

	@Autowired
	private Dialog dialog

	@Autowired
	private Forms forms

	@FXML
	private TextField textFieldDriver

	@FXML
	private TextArea textAreaHost

	@FXML
	private TextField textFieldPort

	@FXML
	private TextField textFieldDatabase

	@FXML
	private TextField textFieldLogin

	@FXML
	private TextField textFieldPassword

	@FXML
	private ProgressBar progressBarWaitConnect

	@FXML
	private Button butTestConnection

	@FXML
	private void initialize() {
		this.clear()
	}

	/**
	 * Обработчик событий при нажатии на любую кнопку
	 * @param actionEvent
	 */
	@FXML
	void butAction(ActionEvent actionEvent) {
		Object source = actionEvent.getSource()

		if (!source instanceof Button) {
			return
		}

		Button button = (Button) source

		switch (button.id) {
			case 'butDriverBrowser':
				File dir = this.dialog.chooserFile()
				if (dir != null) {
					this.textFieldDriver.setText(dir.getAbsolutePath())
				}
				break
			case 'butTestConnection':
				this.testConnection()
				break
		}
	}

	private void testConnection() {
		this.progressBarWaitConnect.setVisible(true)
		this.butTestConnection.setDisable(true)
		this.butTestConnection.getScene().setCursor(Cursor.WAIT)

		try {
			Map connections = (forms.jsonConnections().connections)
			//connections.vertica.remove('driverPath')
			Config.content.connections = connections
			Config.setVars(forms.jsonVars().vars)
			Config.EvalConfig()
		} catch (ignore) {

		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION)

		new Thread(new Runnable() {
			@Override
			void run() {

				Locale locale = LocaleContextHolder.getLocale()
				def conn = new VerticaConnection(config: 'vertica')

				try {
					conn.connected = true
					alert.setTitle(messageSource.getMessage('app.alert.title.information', null, locale))
					alert.setHeaderText(messageSource.getMessage('app.alert.connection.ok', null, locale))
				} catch (Exception e) {
					alert.setAlertType(Alert.AlertType.ERROR)
					alert.setTitle(messageSource.getMessage('app.alert.title.error', null, locale))
					alert.setHeaderText(messageSource.getMessage('app.alert.connection.error', null, locale))
					alert.setContentText(e.message)
				} finally {
					conn.connected = false

					Platform.runLater(new Runnable() {
						@Override
						void run() {
							progressBarWaitConnect.setVisible(false)
							butTestConnection.setDisable(false)
							butTestConnection.getScene().setCursor(Cursor.DEFAULT)

							alert.showAndWait()
						}
					})
				}
			}
		}).start()
	}

	@Override
	void clear() {
		this.textFieldDriver.clear()
		this.textAreaHost.clear()
		this.textFieldPort.setText('5433')
		this.textFieldDatabase.clear()
		this.textFieldLogin.clear()
		this.textFieldPassword.clear()
	}

	@Override
	void loadJson(Map json) {
		Map c = json?.connections?.vertica
		if (c == null) {
			return
		}

		String _connectHost = c.connectHost ?: ''
		if (!_connectHost.isEmpty()) {
			def t = _connectHost.split(":")
			if (t.size() == 2) {
				this.host = t[0] as String
				this.port = t[1] as Integer
			} else {
				this.host = _connectHost
			}
		}

		String connectProperty = c.connectProperty?.backupServerNode ?: ''
		if (!connectProperty.isEmpty()) {
			connectProperty.split(',').each {h ->
				this.textAreaHost.appendText(h)
			}
		}

		this.driver = c.driverPath
		this.dataBase = c.connectDatabase
		this.login = c.login
		this.password = c.password

	}

	@Override
	String getDriver() {
		return this.textFieldDriver.getText()
	}

	@Override
	void setDriver(String driver) {
		this.textFieldDriver.setText(driver)
	}

	@Override
	String getHost() {
		return this.textAreaHost.getText()
	}

	@Override
	void setHost(String host) {
		this.textAreaHost.setText(host)
	}

	@Override
	Integer getPort() {
		return this.textFieldPort.getText() as Integer
	}

	@Override
	void setPort(int port) {
		this.textFieldPort.setText(port as String)
	}

	@Override
	String getDataBase() {
		return this.textFieldDatabase.getText()
	}

	@Override
	void setDataBase(String dataBase) {
		this.textFieldDatabase.setText(dataBase)
	}

	@Override
	String getLogin() {
		return this.textFieldLogin.getText()
	}

	@Override
	void setLogin(String login) {
		this.textFieldLogin.setText(login)
	}

	@Override
	String getPassword() {
		return this.textFieldPassword.getText()
	}

	@Override
	void setPassword(String password) {
		this.textFieldPassword.setText(password)
	}
}
