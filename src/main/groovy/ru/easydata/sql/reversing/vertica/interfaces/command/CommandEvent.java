package ru.easydata.sql.reversing.vertica.interfaces.command;

/**
 * Описания событий, возникающих во время выполнения командной строки
 *
 * @author Сергей Семыкин
 * @since 05.12.2017
 */
public interface CommandEvent {
	/**
	 * Событие, возникающее перед запуском процесса
	 * @param command параметры для запуска командной строки
	 */
	void onStart(Command command);

	/**
	 * Событие, возникающее после удачного завершения процесса
	 * @param command параметры для запуска командной строки
	 */
	void onEnd(Command command);

	/**
	 * Событие, возникающее после остановки процесса
	 * @param command параметры для запуска командной строки
	 */
	void onStop(Command command);

	/**
	 * Признак остановки процесса
	 * @return true если нужно остановить выполнение процесса
	 */
	boolean isStop();
}
