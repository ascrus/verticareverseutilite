package ru.easydata.sql.reversing.vertica.configurations

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.util.Callback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.MessageSourceResourceBundle
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class FXMLViewLoader {
	@Autowired
	private ApplicationContext app

	private Map<String, Parent> views = new HashMap<>()

	@PostConstruct
	void init() {
		this.views.clear()
		def list = ['connection','general','grants','main','pool','deploy','build','role','schema',
					'sequence','sql_function','table','user','view','list_object']
		list.each {name ->
			this.views.put(name, this.load('/fxml/' + name + '.fxml'))
		}
	}

	Parent get(String name) {
		return this.views.get(name)
	}

	Parent load(String url) {
		FXMLLoader fxmlLoader = new FXMLLoader(app.getResource(url).getURL())
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			Object call(Class<?> c) {
				return app.getBean(c)
			}
		})
		fxmlLoader.setResources(new MessageSourceResourceBundle((MessageSource) app.getBean("messageSource"), LocaleContextHolder.getLocale()))

		return fxmlLoader.load()
	}
}
