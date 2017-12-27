package ru.easydata.sql.reversing.vertica.configurations

import javafx.stage.Stage
import org.apache.commons.io.FileUtils

/**
 * @author Сергей Семыкин
 * @since 12.12.2017
 */
class UserProperties {
	private File appHome = new File(System.getProperty("user.home"), '.SQLReversingForVertica')
	private File propertiesFile = new File(appHome, 'application.properties')
	private File opensFile = new File(appHome, 'opens')
	private List<String> openList = new ArrayList<>()
	private Properties properties = new Properties()

	private static int MAX_SIZE_LIST = 10

	UserProperties() {
		if (!this.appHome.exists()) {
			this.appHome.mkdir()
		}

		if (!this.propertiesFile.exists()) {
			this.newProperties()
		}

		def is = this.propertiesFile.newInputStream()
		properties.load(is)
		is.close()

		if (this.opensFile.exists()) {
			this.openList = this.opensFile as String[]
		}
	}

	void setWindowPosition(String name, Stage state) {
		this.properties.put('window.position.' + name + '.x', state.getX() as String)
		this.properties.put('window.position.' + name + '.y', state.getY() as String)
		this.properties.put('window.position.' + name + '.width', state.getWidth() as String)
		this.properties.put('window.position.' + name + '.height', state.getHeight() as String)
	}

	void loadWindowPosition(String name, Stage state) {
		if (this.properties.get('window.position.' + name + '.x') != null) {
			state.setX(this.properties.get('window.position.' + name + '.x', 0) as Double)
			state.setY(this.properties.get('window.position.' + name + '.y', 0) as Double)
			state.setWidth(this.properties.get('window.position.' + name + '.width', 600) as Double)
			state.setHeight(this.properties.get('window.position.' + name + '.height', 400) as Double)
		}
	}

	Set<String> getOpenList() {
		return new LinkedHashSet<String>(this.openList.take(MAX_SIZE_LIST))
	}

	void addOpenFile(File file) {
		this.openList.push(file.getAbsolutePath())
	}

	String get(String key) {
		return this.properties.get(key)
	}

	String get(String key, String defaultValue) {
		return this.properties.get(key, defaultValue)
	}

	void set(String key, String value) {
		this.properties.put(key, value)
	}

	void save() {
		def os = this.propertiesFile.newOutputStream()
		properties.store(os, null)
		os.close()

		FileUtils.writeLines(new File(appHome, 'opens'), 'UTF-8', this.getOpenList())
	}

	private void newProperties() {
		StringBuilder data = new StringBuilder()
		data.append('locale=en')

		this.propertiesFile.setText(data.toString())
	}
}