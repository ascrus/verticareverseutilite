package ru.easydata.sql.reversing.vertica

import getl.utils.Logs
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

import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.SplashScreen

/**
 * @author Сергей Семыкин
 * @since 09.11.2017
 */
@SpringBootApplication
class ReversingVerticaApp extends Application {
	private final static SplashScreen splash = SplashScreen.getSplashScreen()
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

		userProperties.loadWindowPosition('main', primaryStage)

		primaryStage.setOnCloseRequest({event ->

			userProperties.setWindowPosition('main', primaryStage)

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

		if (splash != null && splash.isVisible()) {
			splash.close()
		}

		Logs.printStackTraceError = false
	}

	static void main(String[] args) {
		/*if (splash != null) {
			Graphics2D g = splash.createGraphics()
			if (g != null) {
				g.setComposite(AlphaComposite.Clear)
				g.fillRect(120,140,200,40)
				g.setPaintMode()
				g.setColor(Color.WHITE)
				g.setFont(new Font("TimesRoman", 0, 18))
				g.drawString("0.0.3-SNAPSHOT", 428, 90)
				splash.update()
			}
		}*/
		savedArgs = args
		launch(ReversingVerticaApp.class, args)
	}
}
