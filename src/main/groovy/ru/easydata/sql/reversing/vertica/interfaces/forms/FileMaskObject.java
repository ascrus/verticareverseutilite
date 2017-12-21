package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание раздела формы для указания имени файла генерируемого объекта
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface FileMaskObject {
	/**
	 * Получить имя файла генерируемого объекта
	 */
	String getFileMask();

	/**
	 * Задать имя файла генерируемого объекта
	 * @param fileMask имя файла
	 */
	void setFileMask(String fileMask);
}
