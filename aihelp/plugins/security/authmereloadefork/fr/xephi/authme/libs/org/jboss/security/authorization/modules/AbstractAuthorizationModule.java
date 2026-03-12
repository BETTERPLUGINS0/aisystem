package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationModule;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public abstract class AbstractAuthorizationModule implements AuthorizationModule {
   protected Subject subject = null;
   protected CallbackHandler handler = null;
   protected Map<String, Object> sharedState = null;
   protected Map<String, Object> options = null;
   protected RoleGroup role = null;
   protected Map<ResourceType, String> delegateMap = new HashMap();
   protected static Map<String, Class<?>> clazzMap = new ConcurrentHashMap();

   public abstract int authorize(Resource var1);

   public boolean abort() throws AuthorizationException {
      return true;
   }

   public boolean commit() throws AuthorizationException {
      return true;
   }

   public boolean destroy() {
      this.subject = null;
      this.handler = null;
      this.sharedState = null;
      this.options = null;
      return true;
   }

   public void initialize(Subject subject, CallbackHandler handler, Map<String, Object> sharedState, Map<String, Object> options, RoleGroup subjectRole) {
      this.subject = subject;
      this.handler = handler;
      this.sharedState = sharedState;
      this.options = options;
      if (options != null) {
         String commaSeparatedDelegates = (String)options.get("delegateMap");
         if (commaSeparatedDelegates != null && commaSeparatedDelegates.length() > 0) {
            this.populateDelegateMap(commaSeparatedDelegates);
         }
      }

      this.role = subjectRole;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("Name=" + this.getClass().getName());
      buf.append(":subject=" + this.subject);
      buf.append(":role=" + this.role);
      return buf.toString();
   }

   protected int invokeDelegate(Resource resource) {
      int authorizationDecision = true;
      ResourceType layer = resource.getLayer();
      String delegateStr = (String)this.delegateMap.get(layer);
      if (delegateStr == null) {
         throw PicketBoxMessages.MESSAGES.missingDelegateForLayer(layer != null ? layer.toString() : null);
      } else {
         AuthorizationModuleDelegate delegate = null;

         try {
            delegate = this.getDelegate(delegateStr);
            int authorizationDecision = delegate.authorize(resource, this.subject, this.role);
            return authorizationDecision;
         } catch (Exception var8) {
            IllegalStateException ise = new IllegalStateException(var8.getLocalizedMessage());
            ise.initCause(var8);
            throw ise;
         }
      }
   }

   protected AuthorizationModuleDelegate getDelegate(String delegateStr) throws Exception {
      Class<?> clazz = (Class)clazzMap.get(delegateStr);
      if (clazz == null) {
         try {
            clazz = this.getClass().getClassLoader().loadClass(delegateStr);
         } catch (Exception var5) {
            ClassLoader tcl = SecurityActions.getContextClassLoader();
            clazz = tcl.loadClass(delegateStr);
         }

         clazzMap.put(delegateStr, clazz);
      }

      return (AuthorizationModuleDelegate)clazz.newInstance();
   }

   protected void populateDelegateMap(String commaSeparatedDelegates) {
      StringTokenizer st = new StringTokenizer(commaSeparatedDelegates, ",");

      while(st.hasMoreTokens()) {
         String keyPair = st.nextToken();
         StringTokenizer keyst = new StringTokenizer(keyPair, "=");
         if (keyst.countTokens() != 2) {
            throw PicketBoxMessages.MESSAGES.invalidDelegateMapEntry(keyPair);
         }

         String key = keyst.nextToken();
         String value = keyst.nextToken();
         this.delegateMap.put(ResourceType.valueOf(key), value);
      }

   }
}
