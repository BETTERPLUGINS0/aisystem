package fr.xephi.authme.libs.org.jboss.security;

public interface SecurityConstants {
   String DEFAULT_APPLICATION_POLICY = "other";
   String JAAS_CONTEXT_ROOT = "java:jboss/jaas/";
   String JASPI_CONTEXT_ROOT = "java:jboss/jbsx/";
   String SECURITY_DOMAIN_OPTION = "jboss.security.security_domain";
   String DISABLE_SECDOMAIN_OPTION = "jboss.security.disable.secdomain.option";
   String AUTHORIZATION_PATH = "java:/authorizationMgr";
   String JASPI_DELEGATING_MODULE = "fr.xephi.authme.libs.org.jboss.security.auth.container.modules.DelegatingServerAuthModule";
   String JASPI_AUTHENTICATION_MANAGER = "fr.xephi.authme.libs.org.jboss.security.plugins.JASPISecurityManager";
   String DEFAULT_AUTHORIZATION_CLASS = "fr.xephi.authme.libs.org.jboss.security.plugins.JBossAuthorizationManager";
   String SERVLET_LAYER = "HttpServlet";
   String CONTEXT_ID = "jboss.contextid";
   String DEFAULT_WEB_APPLICATION_POLICY = "jboss-web-policy";
   String DEFAULT_EJB_APPLICATION_POLICY = "jboss-ejb-policy";
   String SUBJECT_CONTEXT_KEY = "javax.security.auth.Subject.container";
   String WEB_REQUEST_KEY = "javax.servlet.http.HttpServletRequest";
   String CALLBACK_HANDLER_KEY = "fr.xephi.authme.libs.org.jboss.security.auth.spi.CallbackHandler";
   String RUNAS_IDENTITY_IDENTIFIER = "RunAsIdentity";
   String CALLER_RAI_IDENTIFIER = "CallerRunAsIdentity";
   String ROLES_IDENTIFIER = "Roles";
   String PRINCIPAL_IDENTIFIER = "Principal";
   String PRINCIPALS_SET_IDENTIFIER = "PrincipalsSet";
   String DEPLOYMENT_PRINCIPAL_ROLES_MAP = "deploymentPrincipalRolesMap";
   String SECURITY_CONTEXT = "SecurityContext";
   String CREDENTIAL = "Credential";
   String SUBJECT = "Subject";
   String JAVAEE = "JavaEE";
   String CALLER_PRINCIPAL_GROUP = "CallerPrincipal";
}
