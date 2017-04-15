class MysqlConnectorjGrailsPlugin {
    def version = "5.1.22.1"
    def grailsVersion = "1.2.0 > *"
    def dependsOn = [:]
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def author = "Jaroslav Barton"
    def authorEmail = "djaara83@gmail.com"
    def title = "MySQL Connector/J"
    def description = "MySQL Connector/J"
    def documentation = "http://grails.org/plugin/mysql-connectorj"
    def scm = [ url: "https://github.com/djaara/mysql-connectorj" ]
}
