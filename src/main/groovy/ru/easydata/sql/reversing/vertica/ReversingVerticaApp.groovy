package ru.easydata.sql.reversing.vertica

import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ButtonBar
import javafx.stage.Stage
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import ru.easydata.sql.reversing.vertica.configurations.FXMLViewLoader
import ru.easydata.sql.reversing.vertica.configurations.UserProperties
import ru.easydata.sql.reversing.vertica.controllers.Dialog
import ru.easydata.sql.reversing.vertica.interfaces.Reversing
import ru.easydata.sql.reversing.vertica.services.ReversingImpl

/**
 * @author Сергей Семыкин
 * @since 09.11.2017
 */
@SpringBootApplication
class ReversingVerticaApp extends Application {

	private static String[] savedArgs
	private static ConfigurableApplicationContext springContext
	private static Parent rootNode

	@Override
	void init() throws Exception {
		springContext = SpringApplication.run(ReversingVerticaApp.class, savedArgs)

		UserProperties properties = springContext.getBean('userProperties')

		// Set locale from property file
		LocaleContextHolder.setLocale(new Locale(properties.get("locale", "en")))

		FXMLViewLoader loader = springContext.getBean(FXMLViewLoader)
		loader.init()

		rootNode = loader.get('main')
	}

	@Override
	void stop() throws Exception {
		springContext.stop()
	}

	@Override
	void start(Stage primaryStage) throws Exception {
		ReversingImpl.primaryStage = primaryStage

		Scene scene = new Scene(rootNode)

		primaryStage.setScene(scene)
		primaryStage.setMinWidth(870)
		primaryStage.setMinHeight(600)
		//primaryStage.setMaximized(true)

		primaryStage.show()

		Reversing reversing = springContext.getBean(Reversing.class)
		Dialog dialog = springContext.getBean(Dialog.class)
		UserProperties userProperties = springContext.getBean(UserProperties.class)

		primaryStage.setOnCloseRequest({event ->

			userProperties.save()

			if (reversing.isEdit()) {
				switch (dialog.alertSave()) {
					case ButtonBar.ButtonData.YES:
						try {
							reversing.saveFile()
						} catch (FileNotFoundException e) {
							reversing.saveAsFile(dialog.showSaveDialog())
						}
						break
					case ButtonBar.ButtonData.NO:
						break
					default:
						event.consume()
						break
				}
			}
		})

		reversing.newFile()
	}

	static void main(String[] args) {
		savedArgs = args
		launch(ReversingVerticaApp.class, args)
	}
}
