package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.util.StringPropertyReplacer;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class RoleMappingLoginModule extends AbstractServerLoginModule {
   private static final String REPLACE_ROLE_OPT = "replaceRole";
   private static final String ROLES_PROPERTIES = "rolesProperties";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"replaceRole", "rolesProperties"};
   protected boolean REPLACE_ROLE = false;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
   }

   public boolean login() throws LoginException {
      if (super.login()) {
         return true;
      } else {
         super.loginOk = true;
         return true;
      }
   }

   protected Principal getIdentity() {
      Iterator iter = this.subject.getPrincipals().iterator();

      Principal p;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         p = (Principal)iter.next();
      } while(p instanceof Group);

      return p;
   }

   protected Group[] getRoleSets() throws LoginException {
      String rep = (String)this.options.get("replaceRole");
      if ("true".equalsIgnoreCase(rep)) {
         this.REPLACE_ROLE = true;
      }

      String propFileName = (String)this.options.get("rolesProperties");
      if (propFileName == null) {
         throw new LoginException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("rolesProperties"));
      } else {
         propFileName = StringPropertyReplacer.replaceProperties(propFileName);
         Group group = this.getExistingRolesFromSubject();
         if (propFileName != null) {
            Properties props = new Properties();

            try {
               props = Util.loadProperties(propFileName);
            } catch (Exception var6) {
               PicketBoxLogger.LOGGER.debugFailureToLoadPropertiesFile(propFileName, var6);
            }

            if (props != null) {
               this.processRoles(group, props);
            }
         }

         return new Group[]{group};
      }
   }

   private Group getExistingRolesFromSubject() {
      Iterator iter = this.subject.getPrincipals().iterator();

      while(iter.hasNext()) {
         Principal p = (Principal)iter.next();
         if (p instanceof Group) {
            Group g = (Group)p;
            if ("Roles".equals(g.getName())) {
               return g;
            }
         }
      }

      return null;
   }

   private void processRoles(Group group, Properties props) {
      Enumeration enumer = props.propertyNames();

      while(enumer.hasMoreElements()) {
         String roleKey = (String)enumer.nextElement();
         String comma_separated_roles = props.getProperty(roleKey);

         try {
            Principal pIdentity = this.createIdentity(roleKey);
            if (group != null) {
               if (group.isMember(pIdentity)) {
                  Util.parseGroupMembers(group, comma_separated_roles, this);
               }

               if (this.REPLACE_ROLE) {
                  group.removeMember(pIdentity);
               }
            }
         } catch (Exception var7) {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(roleKey, var7);
         }
      }

   }
}
