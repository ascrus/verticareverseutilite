package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание формы ресурсных пулов
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface PoolForm extends GenerationObject, DropObject, FileMaskObject {
	/**
	 * Признак создания ресурсных пулов без атрибутов
	 * @return true создать ресурсные пуулы без атрибутов
	 */
	boolean isEmpty();

	/**
	 * Задать признак создания ресурсных пулов без атрибутов
	 * @param isEmpty true создать ресурсные пуулы без атрибутов
	 */
	void setEmpty(boolean isEmpty);
}
