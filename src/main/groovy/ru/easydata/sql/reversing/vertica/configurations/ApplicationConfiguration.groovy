package ru.easydata.sql.reversing.vertica.configurations

import groovy.transform.CompileStatic
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Configuration
@CompileStatic
class ApplicationConfiguration {
	@Bean
	File localesMessagesPath() {
		return new File('locales')
	}

	@Bean
	UserProperties userProperties() {
		return new UserProperties()
	}

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource()
		messageSource.setBasenames("file:locales/messages")
		messageSource.setDefaultEncoding("UTF-8")
		return messageSource
	}

}
