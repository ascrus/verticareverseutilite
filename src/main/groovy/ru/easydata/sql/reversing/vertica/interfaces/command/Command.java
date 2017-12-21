package ru.easydata.sql.reversing.vertica.interfaces.command;

import java.io.File;
import java.util.List;

/**
 * Описание параметров для запуска командной строки
 *
 * @author Сергей Семыкин
 * @since 05.12.2017
 */
public interface Command {
	/**
	 * Получение строки выполнения
	 */
	List<String> getCommand();

	/**
	 * Получение рабочей директории
	 */
	File getDirectory();

	/**
	 * Получение директории с sql скриптами, сгенерированными утилитой GETL
	 */
	File getScriptDirectory();
}
