package co.edu.uniandes.login
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class UserRoleController {

    static scaffold = true
}
