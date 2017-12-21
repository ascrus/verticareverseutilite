package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание раздела формы сборки пакета
 *
 * @author Сергей Семыкин
 * @since 12.12.2017
 */
public interface DeployForm extends BaseForm {
	String getScriptDirectory();
	String setScriptDirectory(String scriptDirectory);
	String getDeployDirectory();
	void setDeployDirectory(String deployDirectory);
}
