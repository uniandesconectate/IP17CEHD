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
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ldap.core.GrailsLdapRoleMapper
import grails.plugin.springsecurity.ldap.core.GrailsSimpleDirContextAuthenticationStrategy
import grails.plugin.springsecurity.ldap.core.SimpleAuthenticationSource
import grails.plugin.springsecurity.ldap.userdetails.DatabaseOnlyLdapAuthoritiesPopulator
import grails.plugin.springsecurity.ldap.userdetails.GrailsLdapAuthoritiesPopulator
import grails.plugin.springsecurity.ldap.userdetails.GrailsLdapUserDetailsManager
import grails.plugin.springsecurity.userdetails.GormUserDetailsService

import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.security.ldap.DefaultLdapUsernameToDnMapper
import org.springframework.security.ldap.DefaultSpringSecurityContextSource
import org.springframework.security.ldap.authentication.BindAuthenticator
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper
import org.springframework.security.ldap.userdetails.PersonContextMapper

class SpringSecurityLdapGrailsPlugin {

	String version = '2.0-RC4'
	String grailsVersion = '2.3.0 > *'
	List pluginExcludes = [
		'docs/**',
		'src/docs/**'
	]
	List loadAfter = ['springSecurityCore']
	String author = 'Burt Beckwith'
	String authorEmail = 'burt@burtbeckwith.com'
	String title = 'LDAP authentication support for the Spring Security plugin.'
	String description = 'LDAP authentication support for the Spring Security plugin.'
	String documentation = 'http://grails-plugins.github.io/grails-spring-security-ldap/'
	String license = 'APACHE'
	def organization = [name: 'SpringSource', url: 'http://www.springsource.org/']
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPSPRINGSECURITYLDAP']
	def scm = [url: 'https://github.com/grails-plugins/grails-spring-security-ldap']

	def doWithSpring = {

		def conf = SpringSecurityUtils.securityConfig
		if (!conf || !conf.active) {
			return
		}

		SpringSecurityUtils.loadSecondaryConfig 'DefaultLdapSecurityConfig'
		// have to get again after overlaying DefaultLdapSecurityConfig
		conf = SpringSecurityUtils.securityConfig

		if (!conf.ldap.active) {
			return
		}

		boolean printStatusMessages = (conf.printStatusMessages instanceof Boolean) ? conf.printStatusMessages : true

		if (printStatusMessages) {
			println '\nConfiguring Spring Security LDAP ...'
		}

		SpringSecurityUtils.registerProvider 'ldapAuthProvider'

		Class<?> contextFactoryClass = classForName(conf.ldap.context.contextFactoryClassName) // com.sun.jndi.ldap.LdapCtxFactory
		Class<?> dirObjectFactoryClass = classForName(conf.ldap.context.dirObjectFactoryClassName) // org.springframework.ldap.core.support.DefaultDirObjectFactory

		authenticationStrategy(GrailsSimpleDirContextAuthenticationStrategy) {
			userDn = conf.ldap.context.managerDn // 'cn=admin,dc=example,dc=com'
		}

		contextSource(DefaultSpringSecurityContextSource, conf.ldap.context.server) { // 'ldap://localhost:389'
			authenticationSource = ref('ldapAuthenticationSource')
			authenticationStrategy = ref('authenticationStrategy')
			userDn = conf.ldap.context.managerDn // 'cn=admin,dc=example,dc=com'
			password = conf.ldap.context.managerPassword // 'secret'
			contextFactory = contextFactoryClass
			dirObjectFactory = dirObjectFactoryClass
			baseEnvironmentProperties = conf.ldap.context.baseEnvironmentProperties // none
			cacheEnvironmentProperties = conf.ldap.context.cacheEnvironmentProperties // true
			anonymousReadOnly = conf.ldap.context.anonymousReadOnly // false
			referral = conf.ldap.context.referral // null
		}

		ldapAuthenticationSource(SimpleAuthenticationSource) {
			principal = conf.ldap.context.managerDn // 'cn=admin,dc=example,dc=com'
			credentials = conf.ldap.context.managerPassword // 'secret'
		}

		String[] searchAttributesToReturn = toStringArray(conf.ldap.search.attributesToReturn) // null - all
		ldapUserSearch(FilterBasedLdapUserSearch, conf.ldap.search.base, conf.ldap.search.filter, contextSource) {
			searchSubtree = conf.ldap.search.searchSubtree // true
			derefLinkFlag = conf.ldap.search.derefLink // false
			searchTimeLimit = conf.ldap.search.timeLimit // 0 (unlimited)
			returningAttributes = searchAttributesToReturn
		}

		String[] attributesToReturn = toStringArray(conf.ldap.authenticator.attributesToReturn) // null - all
		String[] dnPatterns = toStringArray(conf.ldap.authenticator.dnPatterns) // null
		if (conf.ldap.authenticator.useBind) {
			ldapAuthenticator(BindAuthenticator, contextSource) {
				userSearch = ldapUserSearch
				if (attributesToReturn != null) {
					userAttributes = attributesToReturn
				}
				if (dnPatterns) {
					userDnPatterns = dnPatterns
				}
			}
		}
		else {
			ldapAuthenticator(PasswordComparisonAuthenticator, contextSource) {
				userSearch = ldapUserSearch
				if (attributesToReturn != null) {
					userAttributes = attributesToReturn
				}
				if (dnPatterns) {
					userDnPatterns = dnPatterns
				}
				passwordAttributeName = conf.ldap.authenticator.passwordAttributeName
				passwordEncoder = ref('passwordEncoder')
			}
		}

		if (conf.ldap.mapper.userDetailsClass == 'person') {
			ldapUserDetailsMapper(PersonContextMapper)
		}
		else if (conf.ldap.mapper.userDetailsClass == 'inetOrgPerson') {
			ldapUserDetailsMapper(InetOrgPersonContextMapper)
		}
		else {
			ldapUserDetailsMapper(LdapUserDetailsMapper) {
				convertToUpperCase = conf.ldap.mapper.convertToUpperCase // true
				passwordAttributeName = conf.ldap.mapper.passwordAttributeName // 'userPassword'
				if (conf.ldap.mapper.roleAttributes) {
					roleAttributes = conf.ldap.mapper.roleAttributes
				}
			}
		}

		if (conf.ldap.authorities.retrieveGroupRoles) {
			ldapAuthoritiesPopulator(GrailsLdapAuthoritiesPopulator, contextSource, conf.ldap.authorities.groupSearchBase) {
				groupRoleAttribute = conf.ldap.authorities.groupRoleAttribute // 'cn'
				groupSearchFilter = conf.ldap.authorities.groupSearchFilter // 'uniquemember={0}'
				searchSubtree = conf.ldap.authorities.searchSubtree // true
				if (conf.ldap.authorities.defaultRole) {
					defaultRole = conf.ldap.authorities.defaultRole
				}
				ignorePartialResultException = conf.ldap.authorities.ignorePartialResultException // false
				if (conf.ldap.useRememberMe && conf.ldap.authorities.retrieveDatabaseRoles) { // false
					userDetailsService = ref('ldapRememberMeUserDetailsService')
				}
				else {
					userDetailsService = ref('userDetailsService')
				}
				retrieveDatabaseRoles = conf.ldap.authorities.retrieveDatabaseRoles // false
				// Use to cleanup LDAP (Active Directory) Group names
				// Spaces are automatically converted to underscores
				rolePrefix = conf.ldap.authorities.prefix // 'ROLE_'
				roleStripPrefix = conf.ldap.authorities.clean.prefix // null
				roleStripSuffix = conf.ldap.authorities.clean.suffix // null
				roleConvertDashes = conf.ldap.authorities.clean.dashes // false
				roleToUpperCase = conf.ldap.authorities.clean.uppercase // false
			}
		}
		else if (conf.ldap.authorities.retrieveDatabaseRoles) {
			ldapAuthoritiesPopulator(DatabaseOnlyLdapAuthoritiesPopulator) {
				if (conf.ldap.authorities.defaultRole) {
					defaultRole = conf.ldap.authorities.defaultRole
				}
				if (conf.ldap.useRememberMe) {
					userDetailsService = ref('ldapRememberMeUserDetailsService')
				}
				else {
					userDetailsService = ref('userDetailsService')
				}
			}
		}
		else {
			ldapAuthoritiesPopulator(NullLdapAuthoritiesPopulator)
		}

		ldapAuthoritiesMapper(NullAuthoritiesMapper)

		ldapAuthProvider(LdapAuthenticationProvider, ldapAuthenticator, ldapAuthoritiesPopulator) {
			userDetailsContextMapper = ref('ldapUserDetailsMapper')
			hideUserNotFoundExceptions = conf.ldap.auth.hideUserNotFoundExceptions // true
			useAuthenticationRequestCredentials = conf.ldap.auth.useAuthPassword // true
			authoritiesMapper = ref('ldapAuthoritiesMapper')
		}

		if (conf.ldap.useRememberMe) {
			if (!conf.rememberMe.persistent) {
				println "\n\nERROR: LDAP remember-me requires persistent remember-me; run the s2-create-persistent-token script to configure this\n\n"
				System.exit 1
			}

			// needed just for database role lookups
			if (conf.ldap.authorities.retrieveGroupRoles) {
				ldapRememberMeUserDetailsService(GormUserDetailsService) {
					grailsApplication = ref('grailsApplication')
				}
			}

			String[] detailsAttributesToRetrieve = toStringArray(conf.ldap.rememberMe.detailsManager.attributesToRetrieve) // null - all
			userDetailsService(GrailsLdapUserDetailsManager, ref('contextSource')) {
				usernameMapper = ref('ldapUsernameMapper')
				userDetailsMapper = ref('ldapUserDetailsMapper')
				roleMapper = ref('ldapRoleMapper')
				passwordAttributeName = conf.ldap.rememberMe.detailsManager.passwordAttributeName // 'userPassword'
				groupSearchBase = conf.ldap.rememberMe.detailsManager.groupSearchBase // 'cn=groups'
				groupRoleAttributeName = conf.ldap.rememberMe.detailsManager.groupRoleAttributeName // 'cn'
				groupMemberAttributeName = conf.ldap.rememberMe.detailsManager.groupMemberAttributeName // 'uniquemember'
				if (detailsAttributesToRetrieve != null) {
					attributesToRetrieve = detailsAttributesToRetrieve
				}
			}

			ldapRoleMapper(GrailsLdapRoleMapper) {
				rolePrefix = conf.ldap.authorities.prefix // 'ROLE_'
				groupRoleAttributeName = conf.ldap.rememberMe.detailsManager.groupRoleAttributeName // 'cn'
			}

			ldapUsernameMapper(DefaultLdapUsernameToDnMapper,
				conf.ldap.rememberMe.usernameMapper.userDnBase,
				conf.ldap.rememberMe.usernameMapper.usernameAttribute)
		}

		if (printStatusMessages) {
			println '... finished configuring Spring Security LDAP\n'
		}
	}

	private String[] toStringArray(value) {
		if (value == null) {
			return null
		}
		if (value instanceof String) {
			value = [value]
		}
		value as String[]
	}

	private Class<?> classForName(String name) {
		Class.forName name, true, Thread.currentThread().contextClassLoader
	}
}
