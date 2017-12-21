package ru.easydata.sql.reversing.vertica.interfaces.forms;

/**
 * Описание формы подключения к серверу DB Vertica
 *
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
public interface ConnectionForm extends BaseForm {
	String getDriver();
	void setDriver(String driver);
	String getHost();
	void setHost(String host);
	Integer getPort();
	void setPort(int port);
	String getDataBase();
	void setDataBase(String dataBase);
	String getLogin();
	void setLogin(String login);
	String getPassword();
	void setPassword(String password);
}
