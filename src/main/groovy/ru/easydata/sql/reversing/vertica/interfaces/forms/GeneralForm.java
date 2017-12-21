package ru.easydata.sql.reversing.vertica.interfaces.forms;

import ru.easydata.sql.reversing.vertica.objects.VarModel;
import java.util.List;

/**
 * Описание формы основных настроек и списка параметров
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface GeneralForm extends BaseForm {
	/**
	 * Получить путь к лог файлу утилиты getl
	 */
	String getLogFile();

	/**
	 * Задать путь к лог файлу утилиты getl
	 * @param logFile путь к лог файлу
	 */
	void setLogFile(String logFile);

	/**
	 * Получить список параметров (из раздела vars)
	 */
	List<VarModel> getVarList();

	/**
	 * Задать список параметров (из раздела vars)
	 * @param vars список параметров
	 */
	void setVarList(List<VarModel> vars);
}
