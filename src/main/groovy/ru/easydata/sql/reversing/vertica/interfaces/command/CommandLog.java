package ru.easydata.sql.reversing.vertica.interfaces.command;

/**
 * Описание вывода лога процесса выполнения командной строки
 *
 * @author Сергей Семыкин
 * @since 05.12.2017
 */
public interface CommandLog {
	/**
	 * Добавить строчку в конец лога
	 * @param log текст лога
	 */
	void appAnd(String log);
}
