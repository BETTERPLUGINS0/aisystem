package fr.xephi.authme.libs.org.jboss.security;

public interface IAppPolicyStore {
   AppPolicy getAppPolicy(String var1);

   void refresh();
}
