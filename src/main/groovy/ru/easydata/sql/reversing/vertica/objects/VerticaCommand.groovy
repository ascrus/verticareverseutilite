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
	private Boolean isClear = false

	VerticaCommand(File projectFile, File scriptDirectory) {
		this.projectFile = projectFile
		this.scriptDirectory = scriptDirectory
	}

	VerticaCommand(File projectFile, File scriptDirectory, boolean isClear) {
		this.projectFile = projectFile
		this.scriptDirectory = scriptDirectory
		this.isClear = isClear
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

		if (this.isClear) {
			command.add('clear=true')
		}

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
