package ru.easydata.sql.reversing.vertica.services

import groovy.json.JsonBuilder
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import ru.easydata.sql.reversing.vertica.configurations.FXMLViewLoader
import ru.easydata.sql.reversing.vertica.configurations.UserProperties
import ru.easydata.sql.reversing.vertica.controllers.BuildController
import ru.easydata.sql.reversing.vertica.interfaces.Reversing
import ru.easydata.sql.reversing.vertica.objects.VerticaCommand
import javax.annotation.PostConstruct

@Service
class ReversingImpl implements Reversing {
	@Autowired
	private File localesMessagesPath

	@Autowired
	private FXMLViewLoader fxmlViewLoader

	@Autowired
	private Forms forms

	@Autowired
	private MessageSource messageSource

	@Autowired
	private BuildController buildController

	@Autowired
	private UserProperties userProperties

	private Set<Locale> listLocale = new HashSet<>()
	private File file
	private JsonBuilder jsonBuilder
	static Stage primaryStage

	@PostConstruct
	private void init() {
		this.localesMessagesPath.eachFile() {file ->
			Properties p = new Properties()
			p.load(new FileInputStream(file))
			String locale = p.get('locale')
			if (!locale.isEmpty()) {
				this.listLocale.add(new Locale(locale))
			}
		}
	}

	@Override
	@Deprecated
	void newFile() {
		this.file = null
		this.setTitle(null)

		this.forms.clear()

		this.jsonBuilder = this.forms.toJson()
	}

	@Override
	void openFile(File file) {
		if (file == null) {
			return
		}

		this.userProperties.addOpenFile(file)

		this.file = file
		this.setTitle(file.name)

		this.forms.load(file)
		this.jsonBuilder = this.forms.toJson()
	}

	@Override
	void saveFile() throws FileNotFoundException {
		if (this.file == null) {
			throw new FileNotFoundException()
		}
		save()
	}

	@Override
	void saveAsFile(File file) {
		if (file == null) {
			return
		}
		this.file = file
		this.setTitle(file.name)

		save()
	}

	private void save() {
		this.jsonBuilder = this.forms.toJson()
		this.file.write(this.jsonBuilder.toPrettyString(), 'UTF-8')
	}

	@Override
	void build(File directory, boolean isClear) {
		File file = File.createTempFile('rev', '.conf')
		file.deleteOnExit()

		file.write(this.forms.toJson().toPrettyString(), 'UTF-8')

		CommandExecute commandExecute = new CommandExecute(new VerticaCommand(file, directory, isClear))
		commandExecute.setEvent(this.buildController)
		commandExecute.setLog(this.buildController)
		commandExecute.start()
	}

	@Override
	void deploy(File deployDirectory, String scriptDirectory) throws Exception {
		Locale locale = LocaleContextHolder.getLocale()

		if (scriptDirectory == null || scriptDirectory.isEmpty()) {
			throw new Exception(this.messageSource.getMessage('app.exception.script-directory-not-set', null, locale))
		}

		if (this.forms.connectionForm.driver == null || this.forms.connectionForm.driver.isEmpty()) {
			throw new Exception(this.messageSource.getMessage('app.exception.driver-not-set', null, locale))
		}

		File libs = new File('bin', 'libs')
		File driver = new File(this.forms.connectionForm.driver)

		if (!deployDirectory.exists()) {
			deployDirectory.mkdirs()
		}

		File deployLibs = new File(deployDirectory, 'libs')
		if (!deployLibs.exists()) {
			deployLibs.mkdir()
		} else {
			FileUtils.cleanDirectory(deployLibs)
		}

		FileUtils.copyDirectory(libs, deployLibs)

		FileUtils.copyFile(driver, new File(deployLibs, driver.name))

		String fileName = (this.file == null) ? 'reverse' : this.file.name.replace('.conf', '')

		File conf = new File(deployDirectory, fileName + '.conf')
		File sh = new File(deployDirectory, fileName + '.sh')
		File bat = new File(deployDirectory, fileName + '.bat')

		conf.write(this.forms.toJson().toPrettyString(), 'UTF-8')
		bat.write("@ECHO OFF\r\n" +
				"chcp 65001\r\n" +
				"java -cp libs\\* getl.vertica.ReverseEngineering config.filename=${conf.name} script_path=\"${scriptDirectory}\" %*", 'UTF-8')
		sh.write("#!/bin/sh\n\r" +
				"java -cp libs/* getl.vertica.ReverseEngineering config.filename=${conf.name} script_path=\"${scriptDirectory}\" \$@", 'UTF-8')
	}

	@Override
	boolean isEdit() {
		if (this.jsonBuilder.toString().hashCode() == this.forms.toJson().toString().hashCode()) {
			return false
		}

		return true
	}

	@Override
	Set<Locale> locales() {
		return this.listLocale
	}

	@Override
	void setLocale(Locale locale) {
		LocaleContextHolder.setLocale(locale)
		this.fxmlViewLoader.init()
		primaryStage.hide()
		primaryStage.getScene().setRoot(fxmlViewLoader.get('main'))
		primaryStage.show()
	}

	@Override
	void close() {
		primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST))
	}

	@Override
	void setTitle(String fileName) {
		String title = 'SQL reversing for Vertica'
		if (fileName == null) {
			primaryStage.setTitle(title)
		} else {
			primaryStage.setTitle(title + ' - ' + fileName)
		}
	}
}
