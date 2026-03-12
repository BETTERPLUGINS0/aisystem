package fr.xephi.authme.libs.org.jboss.security.config;

public interface ApplicationPolicyRegistration {
   void addApplicationPolicy(String var1, ApplicationPolicy var2);

   ApplicationPolicy getApplicationPolicy(String var1);

   boolean removeApplicationPolicy(String var1);
}
