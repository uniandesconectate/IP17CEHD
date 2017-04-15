/* Copyright 2006-2015 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Creates test applications for functional tests.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */

includeTargets << new File(springSecurityCorePluginDir, "scripts/_S2Common.groovy")

functionalTestPluginVersion = '1.2.7'
projectfiles = new File(basedir, 'webtest/projectFiles')
appName = null
grailsHome = null
dotGrails = null
grailsVersion = null
projectDir = null
pluginVersion = null
testprojectRoot = null
deleteAll = false

target(createLdapTestApps: 'Creates LDAP test apps') {

	def configFile = new File(basedir, 'testapps.config.groovy')
	if (!configFile.exists()) {
		error "$configFile.path not found"
	}

	new ConfigSlurper().parse(configFile.text).each { name, config ->
		printMessage "\nCreating app based on configuration $name: ${config.flatten()}\n"
		init name, config
		createApp()
		installPlugins()
		runQuickstart()
		createProjectFiles()
	}
}

private void init(String name, config) {

	pluginVersion = config.pluginVersion
	if (!pluginVersion) {
		error "pluginVersion wasn't specified for config '$name'"
	}

	def pluginZip = new File(basedir, "grails-spring-security-ldap-${pluginVersion}.zip")
	if (!pluginZip.exists()) {
		error "plugin $pluginZip.absolutePath not found"
	}

	grailsHome = config.grailsHome
	if (!new File(grailsHome).exists()) {
		error "Grails home $grailsHome not found"
	}

	projectDir = config.projectDir
	appName = 'spring-security-ldap-test-' + name
	testprojectRoot = "$projectDir/$appName"
	grailsVersion = config.grailsVersion
	dotGrails = config.dotGrails + '/' + grailsVersion
}

private void createApp() {

	ant.mkdir dir: projectDir

	deleteDir testprojectRoot
	deleteDir "$dotGrails/projects/$appName"

	callGrails grailsHome, projectDir, 'dev', 'create-app', [appName]
}

private void installPlugins() {

	File buildConfig = new File(testprojectRoot, 'grails-app/conf/BuildConfig.groovy')
	String contents = buildConfig.text

	contents = contents.replace('grails.project.class.dir = "target/classes"', "grails.project.work.dir = 'target'")
	contents = contents.replace('grails.project.test.class.dir = "target/test-classes"', '')
	contents = contents.replace('grails.project.test.reports.dir = "target/test-reports"', '')

	contents = contents.replace('//mavenLocal()', 'mavenLocal()')

	contents = contents.replace('grails.project.fork', 'grails.project.forkDISABLED')

	contents = contents.replace('plugins {', """plugins {
test ":functional-test:$functionalTestPluginVersion"
compile ":ldap-server:0.1.8"
compile ":spring-security-ldap:$pluginVersion"
""")

	// configure the functional tests to run in order
	contents += '''
'grails.testing.patterns = ["Person1Functional", "Person2Functional", "Person3Functional"]'
'''

	buildConfig.withWriter { it.writeLine contents }

	callGrails grailsHome, testprojectRoot, 'dev', 'compile', null, true // can fail when installing the functional-test plugin
	callGrails grailsHome, testprojectRoot, 'dev', 'compile'
}

private void runQuickstart() {
	callGrails grailsHome, testprojectRoot, 'dev', 's2-quickstart', ['com.testldap', 'User', 'Role']
	callGrails grailsHome, testprojectRoot, 'dev', 's2-create-persistent-token', ['com.testldap.PersistentLogin']
}

private void createProjectFiles() {
	String source = "$basedir/webtest/projectfiles"

	ant.copy file: "$source/classpath", tofile: "$testprojectRoot/.classpath", overwrite: true

	ant.copy file: "$source/SecureController.groovy",
	         todir: "$testprojectRoot/grails-app/controllers", overwrite: true

	ant.copy file: "$source/BootStrap.groovy",
	         todir: "$testprojectRoot/grails-app/conf", overwrite: true

	ant.copy(todir: "$testprojectRoot/test/functional", overwrite: true) {
		fileset dir: "$basedir/webtest", includes: "*Test*.groovy"
	}

	ant.mkdir dir: "$testprojectRoot/grails-app/ldap-servers/d1/data"
	ant.copy file: "$source/users.ldif", todir: "$testprojectRoot/grails-app/ldap-servers/d1/data", overwrite: true

	new File("$testprojectRoot/grails-app/conf/Config.groovy").withWriterAppend {
		it.writeLine """
grails.plugin.springsecurity.ldap.context.managerDn = 'uid=admin,ou=system'
grails.plugin.springsecurity.ldap.context.managerPassword = 'secret'
grails.plugin.springsecurity.ldap.context.server = 'ldap://localhost:10389'
grails.plugin.springsecurity.ldap.authorities.groupSearchFilter = 'uniquemember={0}'
grails.plugin.springsecurity.ldap.authorities.groupSearchBase = 'ou=groups,dc=d1,dc=example,dc=com'
grails.plugin.springsecurity.ldap.authorities.retrieveDatabaseRoles = true
grails.plugin.springsecurity.ldap.search.base = 'dc=d1,dc=example,dc=com'
grails.plugin.springsecurity.ldap.search.filter = '(uid={0})'
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
ldapServers {
   d1 {
      base = 'dc=d1,dc=example,dc=com'
      port = 10389
      indexed = ['objectClass', 'uid', 'mail', 'userPassword', 'description']
   }
}
grails.plugin.springsecurity.ldap.useRememberMe = true
grails.plugin.springsecurity.ldap.rememberMe.detailsManager.groupSearchBase = 'ou=groups,dc=d1,dc=example,dc=com'
grails.plugin.springsecurity.ldap.rememberMe.detailsManager.groupRoleAttributeName = 'cn'
grails.plugin.springsecurity.ldap.rememberMe.usernameMapper.userDnBase = 'dc=d1,dc=example,dc=com'
grails.plugin.springsecurity.ldap.rememberMe.usernameMapper.usernameAttribute = 'cn'

grails.plugin.springsecurity.fii.rejectPublicInvocations = true
grails.plugin.springsecurity.rejectIfNoRule = false
"""
	}
}

private void deleteDir(String path) {
	if (new File(path).exists() && !deleteAll) {
		String code = "confirm.delete.$path"
		ant.input message: "$path exists, ok to delete?", addproperty: code, validargs: 'y,n,a'
		def result = ant.antProject.properties[code]
		if ('a'.equalsIgnoreCase(result)) {
			deleteAll = true
		}
		else if (!'y'.equalsIgnoreCase(result)) {
			printMessage "\nNot deleting $path"
			exit 1
		}
	}

	ant.delete dir: path
}

private void error(String message) {
	errorMessage "\nERROR: $message"
	exit 1
}

private void callGrails(String grailsHome, String dir, String env, String action, List extraArgs = null, boolean ignoreFailure = false) {

	String resultproperty = 'exitCode' + System.currentTimeMillis()
	String outputproperty = 'execOutput' + System.currentTimeMillis()

	println "Running 'grails $env $action ${extraArgs?.join(' ') ?: ''}'"

	ant.exec(executable: "${grailsHome}/bin/grails", dir: dir, failonerror: false,
				resultproperty: resultproperty, outputproperty: outputproperty) {
		ant.env key: 'GRAILS_HOME', value: grailsHome
		ant.arg value: env
		ant.arg value: action
		extraArgs.each { ant.arg value: it }
		ant.arg value: '--stacktrace'
	}

	println ant.project.getProperty(outputproperty)

	int exitCode = ant.project.getProperty(resultproperty) as Integer
	if (exitCode && !ignoreFailure) {
		exit exitCode
	}
}

setDefaultTarget 'createLdapTestApps'
