package ru.easydata.sql.reversing.vertica.interfaces.forms;

import java.util.Map;

/**
 * Базовое описание всех форм
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface BaseForm {
	/**
	 * Очистить содержимое формы
	 */
	void clear();

	/**
	 * Загрузка данных формы, из JSON
	 * @param json JSON структура в виде мапа
	 */
	void loadJson(Map json);
}
