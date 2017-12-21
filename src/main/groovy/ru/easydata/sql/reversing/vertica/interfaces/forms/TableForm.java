package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание формы таблиц
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface TableForm extends GenerationObject, DropObject, FileMaskObject {
	/**
	 * Признак генерации констрейнтов
	 * @return true если констрейнты будут генериться
	 */
	boolean isConstraints();

	/**
	 * Задать признак генерации констрейнтов
	 * @param isConstraints true если констрейнты будут генериться
	 */
	void setConstraints(boolean isConstraints);

	/**
	 * Признак генерации проекций
	 * @return true если проекции будут генериться
	 */
	boolean isProjection();

	/**
	 * Задать признак генерации проекций
	 * @param isProjection true если проекции будут генериться
	 */
	void setProjection(boolean isProjection);

	/**
	 * Получить ksafe проекции
	 */
	Integer getProjectionKsafe();

	/**
	 * Задать ksafe проекции
	 * @param projectionKsafe не может быть меньше 0 и больше 2
	 */
	void setProjectionKsafe(Integer projectionKsafe);
}
