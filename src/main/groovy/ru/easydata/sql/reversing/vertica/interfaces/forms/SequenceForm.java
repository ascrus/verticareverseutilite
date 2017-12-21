package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание формы счетчиков последовательностей
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface SequenceForm extends GenerationObject, DropObject, FileMaskObject {
	/**
	 * Признак продолжения нумерации
	 * @return true счетчик продолжит нумерацию с текущего сохраненного значения, false счетчик начнется сначала
	 */
	boolean isSequencesCurrent();

	/**
	 * Задать празнак продолжения нумерации
	 * @param isSequencesCurrent true счетчик продолжит нумерацию с текущего сохраненного значения, false счетчик начнется сначала
	 */
	void setSequencesCurrent(boolean isSequencesCurrent);
}
