package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание раздела формы сборки SQL файлов
 *
 * @author Сергей Семыкин
 * @since 12.12.2017
 */
public interface BuildForm extends BaseForm {
	String getScriptDirectory();
	void setScriptDirectory(String scriptDirectory);
}
