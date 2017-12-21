package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание раздела формы удаления объектов
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface DropObject extends GenerationObject {
	/**
	 * Признак удаления объекта
	 * @return true если объект нужно удалить
	 */
	boolean isDrop();

	/**
	 * Задать признак удаления объекта
	 * @param isDrop true если объект нужно удалить
	 */
	void setDrop(boolean isDrop);
}
