package ru.easydata.sql.reversing.vertica.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Set;

/**
 * Управление приложением SQL reversing for Vertica
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface Reversing {
	/**
	 * Создание нового файла конфигурации
	 */
	void newFile();

	/**
	 * Открыть файла конфигурации
	 * @param file путь к файлу конфигурации
	 */
	void openFile(File file);

	/**
	 * Сознанить файл конфигурации
	 * @throws FileNotFoundException возникает если файл не указан
	 */
	void saveFile() throws FileNotFoundException;

	/**
	 * Сознанить файл конфигурации
	 * @param file путь к файлу конфигурации
	 */
	void saveAsFile(File file);

	/**
	 * Запуск утилиты GETL и генерация SQL файлов в директорию указанную в настройках приложения
	 * @param scriptDirectory директория с SQL файлами
	 * @param isClear признак очистки директории с SQL файлами
	 */
	void build(File scriptDirectory, boolean isClear);

	/**
	 * Собрать в указанную директорию дистрибутив утилиты GETL и файлов запускающий ее
	 * @param deployDirectory директория для сборки
	 * @param scriptDirectory директория с SQL файлами
	 */
	void deploy(File deployDirectory, String scriptDirectory) throws Exception;

	/**
	 * Признак редактирования конфигурации
	 * @return true если были изменения в конфигурации
	 */
	boolean isEdit();

	/**
	 * Задать локаль
	 */
	void setLocale(Locale locale);

	/**
	 * Загрыть приложение
	 */
	void close();

	/**
	 * Добавить в заголовок приложения назвиние файла конфигурации
	 * @param fileName название файла
	 */
	void setTitle(String fileName);
}
