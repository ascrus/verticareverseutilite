package ru.easydata.sql.reversing.vertica.objects

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
class LogModel {
	enum TYPE {
		INFO, ERROR
	}

	private Date date = new Date()
	private String text
	private TYPE type = TYPE.INFO

	LogModel(String text, TYPE type) {
		this.text = text
		this.type = type
	}

	LogModel(String text) {
		this.text = text
	}

	String getLog() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		return df.format(date) + " [${type.name()}] ${text} \n"
	}

	@Override
	String toString() {
		return getLog()
	}
}
