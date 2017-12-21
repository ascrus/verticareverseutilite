package ru.easydata.sql.reversing.vertica.objects

import java.nio.file.Path

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
class FileTree {
	Path path
	String name
	File file

	FileTree() {}

	FileTree(Path path) {
		this.path = path
		this.name = path.fileName
		this.file = path.toFile()
	}

	@Override
	String toString() {
		return name
	}
}
