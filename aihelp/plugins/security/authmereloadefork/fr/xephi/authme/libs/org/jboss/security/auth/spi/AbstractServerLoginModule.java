package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.NestableGroup;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import org.jboss.logging.Logger;

public abstract class AbstractServerLoginModule implements LoginModule {
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String USE_FIRST_PASSWORD = "useFirstPass";
   private static final String PRINCIPAL_CLASS = "principalClass";
   private static final String UNAUTHENTICATED_IDENTITY = "unauthenticatedIdentity";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"password-stacking", "principalClass", "unauthenticatedIdentity", "jboss.security.security_domain"};
   private HashSet<String> validOptions;
   protected Subject subject;
   protected CallbackHandler callbackHandler;
   protected Map sharedState;
   protected Map options;
   protected boolean useFirstPass;
   protected boolean loginOk;
   protected String principalClassName;
   protected Principal unauthenticatedIdentity;
   protected Logger log = Logger.getLogger(AbstractServerLoginModule.class);

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.sharedState = sharedState;
      this.options = options;
      PicketBoxLogger.LOGGER.traceBeginInitialize();
      if (this.validOptions != null) {
         this.addValidOptions(ALL_VALID_OPTIONS);
         this.checkOptions();
      }

      String passwordStacking = (String)options.get("password-stacking");
      if (passwordStacking != null && passwordStacking.equalsIgnoreCase("useFirstPass")) {
         this.useFirstPass = true;
      }

      this.principalClassName = (String)options.get("principalClass");
      String name = (String)options.get("unauthenticatedIdentity");
      if (name != null) {
         try {
            this.unauthenticatedIdentity = this.createIdentity(name);
            PicketBoxLogger.LOGGER.traceUnauthenticatedIdentity(name);
         } catch (Exception var8) {
            PicketBoxLogger.LOGGER.warnFailureToCreateUnauthIdentity(var8);
         }
      }

   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      this.loginOk = false;
      if (this.useFirstPass) {
         try {
            Object identity = this.sharedState.get("javax.security.auth.login.name");
            Object credential = this.sharedState.get("javax.security.auth.login.password");
            if (identity != null && credential != null) {
               this.loginOk = true;
               return true;
            }
         } catch (Exception var3) {
            PicketBoxLogger.LOGGER.debugFailedLogin(var3);
         }
      }

      return false;
   }

   public boolean commit() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginCommit(this.loginOk);
      if (!this.loginOk) {
         return false;
      } else {
         Set<Principal> principals = this.subject.getPrincipals();
         Principal identity = this.getIdentity();
         principals.add(identity);
         Group[] roleSets = this.getRoleSets();

         for(int g = 0; g < roleSets.length; ++g) {
            Group group = roleSets[g];
            String name = group.getName();
            Group subjectGroup = this.createGroup(name, principals);
            if (subjectGroup instanceof NestableGroup) {
               SimpleGroup tmp = new SimpleGroup("Roles");
               ((Group)subjectGroup).addMember(tmp);
               subjectGroup = tmp;
            }

            Enumeration members = group.members();

            while(members.hasMoreElements()) {
               Principal role = (Principal)members.nextElement();
               ((Group)subjectGroup).addMember(role);
            }
         }

         Group callerGroup = this.getCallerPrincipalGroup(principals);
         if (callerGroup == null) {
            Group callerGroup = new SimpleGroup("CallerPrincipal");
            callerGroup.addMember(identity);
            principals.add(callerGroup);
         }

         return true;
      }
   }

   public boolean abort() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginAbort();
      return true;
   }

   public boolean logout() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogout();
      Principal identity = this.getIdentity();
      Set<Principal> principals = this.subject.getPrincipals();
      principals.remove(identity);
      Group callerGroup = this.getCallerPrincipalGroup(principals);
      if (callerGroup != null) {
         principals.remove(callerGroup);
      }

      return true;
   }

   protected abstract Principal getIdentity();

   protected abstract Group[] getRoleSets() throws LoginException;

   protected boolean getUseFirstPass() {
      return this.useFirstPass;
   }

   protected Principal getUnauthenticatedIdentity() {
      return this.unauthenticatedIdentity;
   }

   protected Group createGroup(String name, Set<Principal> principals) {
      Group roles = null;
      Iterator iter = principals.iterator();

      while(iter.hasNext()) {
         Object next = iter.next();
         if (next instanceof Group) {
            Group grp = (Group)next;
            if (grp.getName().equals(name)) {
               roles = grp;
               break;
            }
         }
      }

      if (roles == null) {
         roles = new SimpleGroup(name);
         principals.add(roles);
      }

      return (Group)roles;
   }

   protected Principal createIdentity(String username) throws Exception {
      Principal p = null;
      if (this.principalClassName == null) {
         p = new SimplePrincipal(username);
      } else {
         ClassLoader loader = SecurityActions.getContextClassLoader();
         Class clazz = loader.loadClass(this.principalClassName);
         Class[] ctorSig = new Class[]{String.class};
         Constructor ctor = clazz.getConstructor(ctorSig);
         Object[] ctorArgs = new Object[]{username};
         p = (Principal)ctor.newInstance(ctorArgs);
      }

      return (Principal)p;
   }

   protected Group getCallerPrincipalGroup(Set<Principal> principals) {
      Group callerGroup = null;
      Iterator i$ = principals.iterator();

      while(i$.hasNext()) {
         Principal principal = (Principal)i$.next();
         if (principal instanceof Group) {
            Group group = (Group)Group.class.cast(principal);
            if (group.getName().equals("CallerPrincipal")) {
               callerGroup = group;
               break;
            }
         }
      }

      return callerGroup;
   }

   protected void addValidOptions(String[] moduleValidOptions) {
      if (this.validOptions == null) {
         this.validOptions = new HashSet(moduleValidOptions.length);
      }

      this.validOptions.addAll(Arrays.asList(moduleValidOptions));
   }

   protected void checkOptions() {
      Iterator i$ = this.options.keySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         if (!this.validOptions.contains(key)) {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }

   }
}
