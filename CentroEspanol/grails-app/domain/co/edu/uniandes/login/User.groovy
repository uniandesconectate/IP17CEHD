package co.edu.uniandes.login

import co.edu.uniandes.centroespanol.CentroEspanol

class User {

	transient springSecurityService

	String email
	String username
	String password
	
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static belongsTo = [centroEspanol: CentroEspanol]

	static transients = ['springSecurityService']

	static constraints = {
		email blank: false, unique: true
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
	
	String toString() {
		return username
	}
}
