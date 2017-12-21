package ru.easydata.sql.reversing.vertica.objects

import ru.easydata.sql.reversing.vertica.interfaces.command.Command

/**
 * Параметры для запуска GETL
 *
 * @author Сергей Семыкин
 * @since 05.12.2017
 */
class VerticaCommand implements Command {
	private File projectFile
	private File scriptDirectory
	private File directory = new File('bin')

	VerticaCommand(File projectFile, File scriptDirectory) {
		this.projectFile = projectFile
		this.scriptDirectory = scriptDirectory
	}

	@Override
	List<String> getCommand() {
		List<String> command = new ArrayList<>()
		command.add('java')
		command.add('-cp')
		command.add('libs/*')
		command.add('getl.vertica.ReverseEngineering')
		command.add('config.filename=' + projectFile.getAbsolutePath())
		command.add('script_path=' + scriptDirectory.getAbsolutePath())

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
