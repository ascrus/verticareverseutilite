package ru.easydata.sql.reversing.vertica.objects

import ru.easydata.sql.reversing.vertica.interfaces.command.Command

/**
 * Параметры для запуска теста
 *
 * @author Сергей Семыкин
 * @since 05.12.2017
 */
class TestCommand implements Command {
	private File scriptDirectory
	private File directory = new File('test')

	TestCommand(File scriptDirectory) {
		this.scriptDirectory = scriptDirectory
	}

	@Override
	List<String> getCommand() {
		List<String> command = new ArrayList<>()
		command.add('java')
		command.add('-jar')
		command.add('test/print-10.jar')

		return command
	}

	@Override
	File getDirectory() {
		return directory
	}

	@Override
	File getScriptDirectory() {
		return scriptDirectory
	}
}
