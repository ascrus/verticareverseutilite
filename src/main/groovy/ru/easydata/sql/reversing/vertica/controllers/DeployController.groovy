package ru.easydata.sql.reversing.vertica.controllers

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.WindowEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.Reversing
import ru.easydata.sql.reversing.vertica.interfaces.forms.DeployForm
import ru.easydata.sql.reversing.vertica.objects.LogModel

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class DeployController implements DeployForm {
	@Autowired
	private Dialog dialog

	@Autowired
	protected Reversing reversing

	@FXML
	private TextArea textAreaLog

	@FXML
	private TextField textFieldDeployDirectory

	@FXML
	private TextField textFieldScriptDirectory

	@FXML
	private Button butStart

	@FXML
	void initialize() {

	}

	@FXML
	void butAction(ActionEvent actionEvent) {
		def source = actionEvent.getSource()
		if (!source instanceof Button) {
			return
		}

		Button button = source as Button

		switch (button.id) {
			case 'butStart':
				this.deploy()
				break
			case 'butDeployDirectoryBrowser':
				File dir = this.dialog.chooserDirectory()
				if (dir != null) {
					this.textFieldDeployDirectory.setText(dir.getAbsolutePath())
				}
				break
		}
	}

	void onClose(WindowEvent windowEvent) {

	}

	void onShown(WindowEvent windowEvent) {

	}

	private void deploy() {
		String scriptDirectory = this.getScriptDirectory()
		String filePath = this.textFieldDeployDirectory.getText() ?: ''
		File deployDirectory

		if (filePath.isEmpty()) {
			deployDirectory = this.dialog.chooserDirectory()
			if (deployDirectory != null) {
				this.textFieldDeployDirectory.setText(deployDirectory.getAbsolutePath())
			} else {
				return
			}
		} else {
			deployDirectory = new File(filePath)
		}

		this.textAreaLog.clear()
		this.textAreaLog.appendText(new LogModel('Start job').toString())

		try {
			this.reversing.deploy(deployDirectory, scriptDirectory)
			this.textAreaLog.appendText(new LogModel('Deploy in - ' + deployDirectory.getAbsolutePath()).toString())
		} catch (Exception e) {
			this.textAreaLog.appendText(new LogModel(e.getMessage(), LogModel.TYPE.ERROR).getLog())
		}

		this.textAreaLog.appendText(new LogModel('Stop job').toString())
	}

	@Override
	String getScriptDirectory() {
		return this.textFieldScriptDirectory.getText()
	}

	@Override
	String setScriptDirectory(String scriptDirectory) {
		return this.textFieldScriptDirectory.setText(scriptDirectory)
	}

	@Override
	String getDeployDirectory() {
		return this.textFieldDeployDirectory.getText()
	}

	@Override
	void setDeployDirectory(String deployDirectory) {
		this.textFieldDeployDirectory.setText(deployDirectory)
	}

	@Override
	void clear() {
		this.textAreaLog.clear()
		this.textFieldDeployDirectory.clear()
		this.textFieldScriptDirectory.clear()
	}

	@Override
	void loadJson(Map json) {
		Map s = json?.vars?._settings
		if (s == null) {
			return
		}

		this.scriptDirectory = s.deployScriptDirectory
		this.deployDirectory = s.deployDirectory
	}
}
