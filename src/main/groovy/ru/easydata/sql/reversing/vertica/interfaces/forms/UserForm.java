package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание формы пользователей
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface UserForm extends GenerationObject, DropObject, FileMaskObject {
	/**
	 * Признак создания пользователей без привязки к их текущим характеристикам
	 * @return true если пользователь создается без привязки к его текущим характеристикам
	 */
	boolean isEmpty();

	/**
	 * Задать признак создания пользователей без привязки к их текущим характеристикам
	 * @param isEmpty true если пользователь создается без привязки к его текущим характеристикам
	 */
	void setEmpty(boolean isEmpty);
}
