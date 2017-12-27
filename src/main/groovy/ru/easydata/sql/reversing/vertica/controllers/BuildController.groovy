package ru.easydata.sql.reversing.vertica.controllers

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.WindowEvent
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.Reversing
import ru.easydata.sql.reversing.vertica.interfaces.command.Command
import ru.easydata.sql.reversing.vertica.interfaces.command.CommandEvent
import ru.easydata.sql.reversing.vertica.interfaces.command.CommandLog
import ru.easydata.sql.reversing.vertica.interfaces.forms.BuildForm
import ru.easydata.sql.reversing.vertica.objects.FileTree
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Function

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class BuildController implements CommandEvent, CommandLog, BuildForm {
	@Autowired
	private Dialog dialog

	@Autowired
	private Reversing reversing

	@FXML
	private TextField textFieldScriptDirectory

	@FXML
	private TextArea textAreaLog

	@FXML
	private Tab tabMain

	@FXML
	private Tab tabFileList

	@FXML
	private TreeView treeViewFiles

	@FXML
	private TextArea textAreaFileView

	@FXML
	private Button butStop

	@FXML
	private Button butStart

	private boolean isStop = false
	private boolean isBuild = false

	@FXML
	void initialize() {
		this.butStop.setDisable(true)
		this.tabFileList.setDisable(true)

		// Обработка событий дерева SQL файлов, при нажатии на файл отображаем его содержимое
		this.treeViewFiles.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
			@Override
			void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if (newValue != null) {
					FileTree fileTree = treeViewFiles.getTreeItem(newValue as Integer).getValue() as FileTree
					if (fileTree.file.isFile()) {
						String extension = FilenameUtils.getExtension(fileTree.name)
						// Для просмотра подходят только файлы с расширение SQL
						if (extension == 'sql') {
							textAreaFileView.setText(fileTree.file.text)
						} else {
							textAreaFileView.clear()
						}
					}
				}
			}
		})
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
			case 'butStart':
				this.start()
				break
			case 'butStop':
				this.stop()
				break
			case 'butScriptDirectoryBrowser':
				File dir = this.dialog.chooserDirectory()
				if (dir != null) {
					this.textFieldScriptDirectory.setText(dir.getAbsolutePath())
				}
				break
		}
	}

	void onClose(WindowEvent windowEvent) {
		if (isBuild) {
			switch (this.dialog.alertClose()) {
				case ButtonBar.ButtonData.YES:
					this.stop()
					break
				default:
					break
			}
			windowEvent.consume()
		}
	}

	void onShown(WindowEvent windowEvent) {
		//tabFileList.setDisable(true)
	}

	private void stop() {
		isStop = true
	}

	private void start() {
		String scriptPath = this.textFieldScriptDirectory.getText() ?: ''
		File scriptDirectory

		// Если путь к SQL скриптам не указан, то показываем диалоговое окно с выбором директории
		if (scriptPath.isEmpty()) {
			scriptDirectory = this.dialog.chooserDirectory()
			if (scriptDirectory != null) {
				this.textFieldScriptDirectory.setText(scriptDirectory.getAbsolutePath())
			} else {
				return
			}
		} else {
			scriptDirectory = new File(scriptPath)
		}

		boolean isClear = false

		// Если директория содержит файлы, то отображаем диалоговое окно с подтверждением об очистки директории
		if (scriptDirectory.exists() && scriptDirectory.listFiles().size() > 0) {
			switch (this.dialog.alertClear()) {
				case ButtonBar.ButtonData.YES:
					isClear =  true
					break
				case ButtonBar.ButtonData.NO:
					isClear =  false
					break
				default:
					return
			}
		}
		// Запуск
		this.reversing.build(scriptDirectory, isClear)

		this.textAreaLog.clear()
		this.butStart.setDisable(true)
		this.butStop.setDisable(false)
		this.tabFileList.setDisable(true)
		isStop = false
	}

	@Override
	void onStart(Command command) {
		Platform.runLater(new Runnable() {
			@Override
			void run() {
				isBuild = true
			}
		})
	}

	@Override
	void onEnd(Command command) {
		Platform.runLater(new Runnable() {
			@Override
			void run() {
				treeViewFiles.setRoot(loadTree(Paths.get(command.scriptDirectory.getAbsolutePath())))
				tabFileList.setDisable(false)
				butStart.setDisable(false)
				butStop.setDisable(true)
				isBuild = false
			}
		})
	}

	@Override
	void onStop(Command command) {
		Platform.runLater(new Runnable() {
			@Override
			void run() {
				butStart.setDisable(false)
				butStop.setDisable(true)
				isBuild = false
			}
		})
	}

	@Override
	boolean isStop() {
		return isStop
	}

	@Override
	void appAnd(String log) {
		Platform.runLater(new Runnable() {
			@Override
			void run() {
				textAreaLog.appendText(log)
			}
		})
	}

	@Override
	String getScriptDirectory() {
		return this.textFieldScriptDirectory.getText()
	}

	@Override
	void setScriptDirectory(String scriptDirectory) {
		this.textFieldScriptDirectory.setText(scriptDirectory)
	}

	@Override
	void clear() {
		treeViewFiles.setRoot(null)

		tabFileList.setDisable(false)
		tabMain.getTabPane().getSelectionModel().select(tabMain)
		textAreaLog.clear()

		butStart.setDisable(false)
		butStop.setDisable(true)

		isBuild = false
		isStop = false
	}

	@Override
	void loadJson(Map json) {
		this.scriptDirectory = json?.vars?._settings?.buildScriptDirectory
	}

	private static TreeItem<FileTree> loadTree(Path path) {
		TreeItem<FileTree> treeItem = new TreeItem<>(new FileTree([name: 'root', path: path]))
		treeItem.setExpanded(true)

		createTree(treeItem)

		treeItem.getChildren().sort(Comparator.comparing(new Function<TreeItem<Path>, String>() {
			@Override
			String apply(TreeItem<Path> t) {
				return t.getValue().toString().toLowerCase()
			}
		}))

		return treeItem
	}

	private static Image iconFolder = new Image(getClass().getResourceAsStream("/icon/folderhorizontal.png"))
	private static Image iconFile = new Image(getClass().getResourceAsStream("/icon/document.png"))

	private static void createTree(TreeItem<FileTree> rootItem) {
		Files.newDirectoryStream(rootItem.getValue().path).withCloseable { DirectoryStream<Path> directoryStream ->
			for (Path path : directoryStream) {
				ImageView icon = (Files.isDirectory(path)) ? new ImageView(iconFolder) : new ImageView(iconFile)

				TreeItem<FileTree> newItem = new TreeItem<FileTree>(new FileTree(path), icon)
				rootItem.getChildren().add(newItem)

				if (Files.isDirectory(path)) {
					createTree(newItem)
				}
			}
		}
	}
}
