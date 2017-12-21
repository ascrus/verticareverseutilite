package ru.easydata.sql.reversing.vertica.services

import ru.easydata.sql.reversing.vertica.interfaces.command.Command
import ru.easydata.sql.reversing.vertica.interfaces.command.CommandEvent
import ru.easydata.sql.reversing.vertica.interfaces.command.CommandLog

/**
 * Выполнение командной строки
 *
 * @author Сергей Семыкин
 * @since 05.12.2017
 */
class CommandExecute extends Thread {
	private Command command
	private Process process
	private CommandEvent event
	private CommandLog log

	CommandExecute(Command command) {
		this.command = command
	}

	void setEvent(CommandEvent event) {
		this.event = event
	}

	void setLog(CommandLog log) {
		this.log = log
	}

	@Override
	void run() {
		if (this.event != null)
			this.event.onStart(this.command)

		ProcessBuilder pb = new ProcessBuilder(this.command.command)
		pb.redirectErrorStream(true)
		pb.directory(this.command.directory)

		this.process = pb.start()

		if (this.log != null)
			new StreamHandler(this.process.getInputStream(), this.log).start()

		// Ожидание завершения выполнения процесса
		boolean isInterrupted = false
		while (!isInterrupted) {
			try {
				this.process.exitValue()
				if (this.event != null)
					this.event.onEnd(this.command)
				isInterrupted = true
			} catch (IllegalThreadStateException e) {
				// Проверка признака остановки задачи
				if (this.event != null && this.event.isStop()) {
					this.process.destroy()
					this.event.onStop(this.command)
					return
				}
				sleep(1000)
			}
		}
	}

	/**
	 * Перенаправление входящего потока в лог
	 */
	class StreamHandler extends Thread {
		private InputStream source
		private CommandLog log

		StreamHandler(InputStream source, CommandLog log) {
			this.source = source
			this.log = log
		}

		@Override
		void run() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.source))
			String line
			while ((line = reader.readLine()) != null) {
				log.appAnd(line + System.getProperty('line.separator'))
			}
		}
	}
}
