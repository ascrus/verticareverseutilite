package ru.easydata.sql.reversing.vertica.services

import groovy.json.JsonBuilder
import groovy.json.JsonGenerator
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.BaseForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.BuildForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.ConnectionForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.DeployForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.DropObject
import ru.easydata.sql.reversing.vertica.interfaces.forms.FileMaskObject
import ru.easydata.sql.reversing.vertica.interfaces.forms.GeneralForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.GenerationObject
import ru.easydata.sql.reversing.vertica.interfaces.forms.GrantsForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.PoolForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.RoleForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.SchemaForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.SequenceForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.SqlFunctionForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.TableForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.UserForm
import ru.easydata.sql.reversing.vertica.interfaces.forms.ViewForm
import javax.annotation.PostConstruct

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class Forms {
	@Autowired
	private ConnectionForm connectionForm

	@Autowired
	private PoolForm poolForm

	@Autowired
	private GeneralForm generalForm

	@Autowired
	private RoleForm roleForm

	@Autowired
	private UserForm userForm

	@Autowired
	private SchemaForm schemaForm

	@Autowired
	private SequenceForm sequenceForm

	@Autowired
	private TableForm tableForm

	@Autowired
	private ViewForm viewForm

	@Autowired
	private GrantsForm grantsForm

	@Autowired
	private DeployForm deployForm

	@Autowired
	private BuildForm buildForm

	@Autowired
	private SqlFunctionForm sqlFunctionForm

	private Map<String, BaseForm> forms = new HashMap<>()

	@PostConstruct
	private void init() {
		this.forms.put('connections', connectionForm)
		this.forms.put('pools', poolForm)
		this.forms.put('general', generalForm)
		this.forms.put('roles', roleForm)
		this.forms.put('users', userForm)
		this.forms.put('schemas', schemaForm)
		this.forms.put('sequences', sequenceForm)
		this.forms.put('tables', tableForm)
		this.forms.put('views', viewForm)
		this.forms.put('grants', grantsForm)
		this.forms.put('sql_functions', sqlFunctionForm)
		this.forms.put('build', buildForm)
		this.forms.put('deploy', deployForm)
	}

	Map<String, BaseForm> list() {
		return forms
	}

	void clear() {
		this.forms.each {String name, BaseForm form ->
			form.clear()
		}
	}

	void load(File file) {
		this.load(file.text)
	}

	void load(String json) {
		this.clear()

		JsonSlurper jsonSlurper = new JsonSlurper()
		Map object = jsonSlurper.parseText(json) as Map

		this.forms.each {key, forms ->
			forms.loadJson(object)
		}
	}

	JsonBuilder toJson() {
		def m = [:]
		m.putAll(this.jsonConnections())
		m.putAll(this.jsonLog())
		m.putAll(this.jsonDrop())
		m.putAll(this.jsonCreate())
		m.putAll(this.jsonFileName())
		m.putAll(this.jsonVars())

		def generator = new JsonGenerator.Options()
				.excludeNulls()
				.build()
		return new JsonBuilder(m, generator)
	}

	Map jsonConnections() {
		ConnectionForm form = this.connectionForm
		String connectHost = ''
		def connectProperty = null

		if (!form.host.isEmpty()) {
			List<String> hostValue = form.host.split("\n")
			connectHost = "${hostValue.first()}:${form.port}"

			if (hostValue.size() > 1) {
				hostValue.remove(0)
				connectProperty = [connectProperty: [
						connectionLoadBalance: true,
						backupServerNode: hostValue.join(",")
				]]
			}
		}

		return [connections: [vertica: [
				driverPath: form.driver,
				connectHost: connectHost,
				connectDatabase: form.dataBase,
				login: form.login,
				password: form.password,
				connectProperty: connectProperty
		]]]
	}

	Map jsonLog() {
		GeneralForm form = this.generalForm
		Map log = null

		if (form.logFile != null && !form.logFile.isEmpty()) {
			log = [file: form.logFile]
		}

		return [log: log]
	}

	Map jsonDrop() {
		def drop = [:]

		this.forms.each {k, v ->
			if (v instanceof DropObject) {
				drop.put((k), v.drop)
			}
		}

		return [drop: drop]
	}

	Map jsonCreate() {
		def create = [:]

		create.pool_empty = this.poolForm.empty
		create.user_empty = this.userForm.empty
		create.table_constraints = this.tableForm.constraints
		create.projection_tables = this.tableForm.projection
		create.projection_ksafe = this.tableForm.projectionKsafe

		this.forms.each {k, v ->
			if (v instanceof GenerationObject) {
				create.put((k), getCreateValue(v, '${filter.' + k + '}'))
			}
		}

		return [create: create]
	}

	Map jsonFileName() {
		def filename = [:]

		this.forms.each {k, v ->
			if (v instanceof FileMaskObject && v.fileMask != null && !v.fileMask.isEmpty()) {
				filename.put(k, v.fileMask)
			}
		}

		return (filename.size() >= 1) ? [filename: filename] : [filename: null]
	}

	Map jsonVars() {
		def vars = [:]
		def filter = [:]
		def settings = [:]

		this.generalForm.varList.each {v ->
			vars.put(v.name, v.value)
		}

		this.forms.each {k, v ->
			if (v instanceof GenerationObject && v.filter != null && !v.filter.isEmpty()) {
				filter.put(k, v.filter)
			}
		}

		if (this.buildForm.scriptDirectory != null && !this.buildForm.scriptDirectory.isEmpty()) {
			settings.put('buildScriptDirectory', this.buildForm.scriptDirectory)
		}
		if (this.deployForm.scriptDirectory != null && !this.deployForm.scriptDirectory.isEmpty()) {
			settings.put('deployScriptDirectory', this.deployForm.scriptDirectory)
		}
		if (this.deployForm.deployDirectory != null && !this.deployForm.deployDirectory.isEmpty()) {
			settings.put('deployDirectory', this.deployForm.deployDirectory)
		}

		if (filter.size() >= 1) {
			vars.put('filter', filter)
		}

		if (settings.size() >= 1) {
			vars.put('_settings', settings)
		}

		return (vars.size() >= 1) ? [vars: vars] : [vars: null]
	}

	private static def getCreateValue(GenerationObject form, String filter) {
		if (form.filter == null || form.filter.isEmpty()) {
			return form.generation
		} else if (!form.filter.isEmpty() && form.generation) {
			return filter
		} else {
			return false
		}
	}

	ConnectionForm getConnectionForm() {
		return connectionForm
	}

	PoolForm getPoolForm() {
		return poolForm
	}

	GeneralForm getGeneralForm() {
		return generalForm
	}

	RoleForm getRoleForm() {
		return roleForm
	}

	UserForm getUserForm() {
		return userForm
	}

	SchemaForm getSchemaForm() {
		return schemaForm
	}

	SequenceForm getSequenceForm() {
		return sequenceForm
	}

	TableForm getTableForm() {
		return tableForm
	}

	ViewForm getViewForm() {
		return viewForm
	}

	SqlFunctionForm getSqlFunctionForm() {
		return sqlFunctionForm
	}

	GrantsForm getGrantsForm() {
		return grantsForm
	}

	DeployForm getDeployForm() {
		return deployForm
	}

	BuildForm getBuildForm() {
		return buildForm
	}
}
