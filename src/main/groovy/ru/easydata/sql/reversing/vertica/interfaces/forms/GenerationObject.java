package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание раздела формы генерации объектов
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface GenerationObject extends BaseForm {
	/**
	 * Признак генерации объекта
	 * @return true если объект нужно сгенерировать
	 */
	boolean isGeneration();

	/**
	 * Задать признак генерации объекта
	 * @param isGeneration true если объект нужно сгенерировать
	 */
	void setGeneration(boolean isGeneration);

	/**
	 * Получить фильтр генерируемых объектов в виде SQL выражения для WHERE к служебной таблице Vertica
	 * @return SQL выражения
	 */
	String getFilter();

	/**
	 * Задать фильтр генерируемых объектов в виде SQL выражения для WHERE к служебной таблице Vertica
	 * @param filter SQL выражения
	 */
	void setFilter(String filter);
}
