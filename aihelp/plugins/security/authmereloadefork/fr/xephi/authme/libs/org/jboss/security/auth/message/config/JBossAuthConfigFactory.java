package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;
import javax.security.auth.message.config.AuthConfigFactory.RegistrationContext;

public class JBossAuthConfigFactory extends AuthConfigFactory {
   private Map<String, AuthConfigProvider> keyToAuthConfigProviderMap = new HashMap();
   private Map<String, RegistrationListener> keyToRegistrationListenerMap = new HashMap();
   private Map<String, RegistrationContext> keyToRegistrationContextMap = new HashMap();

   public JBossAuthConfigFactory() {
      Map<String, Object> props = new HashMap();
      JBossAuthConfigProvider provider = new JBossAuthConfigProvider(props, (AuthConfigFactory)null);
      this.registerConfigProvider(provider, "HTTP", (String)null, "Default Provider");
      this.registerConfigProvider(provider, "HttpServlet", (String)null, "Default Provider");
   }

   public String[] detachListener(RegistrationListener listener, String layer, String appContext) {
      if (listener == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("listener");
      } else {
         String[] arr = new String[0];
         String input = layer + appContext;
         String allLayer = "null" + appContext;
         String allContext = layer + "null";
         String general = "nullnull";
         RegistrationListener origListener = null;
         String key = null;

         for(int i = 0; i < 4 && origListener == null; ++i) {
            if (i == 0) {
               key = input;
            }

            if (i == 1) {
               key = allLayer;
            }

            if (i == 2) {
               key = allContext;
            }

            if (i == 3) {
               key = general;
            }

            origListener = (RegistrationListener)this.keyToRegistrationListenerMap.get(key);
         }

         if (origListener == listener) {
            this.keyToRegistrationListenerMap.remove(key);
         }

         return arr;
      }
   }

   public AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener) {
      String input = layer + appContext;
      String allLayer = "null" + appContext;
      String allContext = layer + "null";
      String general = "nullnull";
      AuthConfigProvider acp = null;
      String key = null;

      for(int i = 0; i < 4; ++i) {
         if (i == 0) {
            key = input;
         }

         if (i == 1) {
            key = allLayer;
         }

         if (i == 2) {
            key = allContext;
         }

         if (i == 3) {
            key = general;
         }

         if (this.keyToAuthConfigProviderMap.containsKey(key)) {
            acp = (AuthConfigProvider)this.keyToAuthConfigProviderMap.get(key);
            break;
         }
      }

      if (listener != null) {
         this.keyToRegistrationListenerMap.put(input, listener);
      }

      return acp;
   }

   public RegistrationContext getRegistrationContext(String registrationID) {
      return (RegistrationContext)this.keyToRegistrationContextMap.get(registrationID);
   }

   public String[] getRegistrationIDs(AuthConfigProvider provider) {
      List<String> al = new ArrayList();
      if (provider == null) {
         al.addAll(this.keyToAuthConfigProviderMap.keySet());
      } else {
         Iterator i$ = this.keyToAuthConfigProviderMap.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, AuthConfigProvider> entry = (Entry)i$.next();
            if (((AuthConfigProvider)entry.getValue()).equals(provider)) {
               al.add(entry.getKey());
            }
         }
      }

      String[] sarr = new String[al.size()];
      al.toArray(sarr);
      return sarr;
   }

   public void refresh() {
   }

   public String registerConfigProvider(String className, Map properties, String layer, String appContext, String description) {
      AuthConfigProvider acp = null;
      if (className != null) {
         try {
            Class<?> provClass = SecurityActions.getContextClassLoader().loadClass(className);
            Constructor<?> ctr = provClass.getConstructor(Map.class, AuthConfigFactory.class);
            acp = (AuthConfigProvider)ctr.newInstance(properties, null);
         } catch (Exception var11) {
            throw PicketBoxMessages.MESSAGES.failedToRegisterAuthConfigProvider(className, var11);
         }
      }

      String registrationID = layer + appContext;
      AuthConfigProvider oldProvider = (AuthConfigProvider)this.keyToAuthConfigProviderMap.put(registrationID, acp);
      JBossAuthConfigFactory.JBossRegistrationContext context;
      if (oldProvider != null) {
         context = (JBossAuthConfigFactory.JBossRegistrationContext)this.keyToRegistrationContextMap.get(registrationID);
         context.setDescription(description);
         context.setIsPersistent(true);
         RegistrationListener listener = (RegistrationListener)this.keyToRegistrationListenerMap.get(registrationID);
         if (listener != null) {
            listener.notify(layer, appContext);
         }
      } else {
         context = new JBossAuthConfigFactory.JBossRegistrationContext(layer, appContext, description, true);
         this.keyToRegistrationContextMap.put(registrationID, context);
      }

      return registrationID;
   }

   public String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext, String description) {
      String registrationID = layer + appContext;
      AuthConfigProvider oldProvider = (AuthConfigProvider)this.keyToAuthConfigProviderMap.put(registrationID, provider);
      JBossAuthConfigFactory.JBossRegistrationContext context;
      if (oldProvider != null) {
         context = (JBossAuthConfigFactory.JBossRegistrationContext)this.keyToRegistrationContextMap.get(registrationID);
         context.setDescription(description);
         context.setIsPersistent(false);
         RegistrationListener listener = (RegistrationListener)this.keyToRegistrationListenerMap.get(registrationID);
         if (listener != null) {
            listener.notify(layer, appContext);
         }
      } else {
         context = new JBossAuthConfigFactory.JBossRegistrationContext(layer, appContext, description, false);
         this.keyToRegistrationContextMap.put(registrationID, context);
      }

      return registrationID;
   }

   public boolean removeRegistration(String registrationID) {
      if (registrationID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("registrationID");
      } else {
         RegistrationListener listener = (RegistrationListener)this.keyToRegistrationListenerMap.get(registrationID);
         RegistrationContext rc = (RegistrationContext)this.keyToRegistrationContextMap.get(registrationID);
         boolean removed = this.keyToAuthConfigProviderMap.containsKey(registrationID);
         this.keyToAuthConfigProviderMap.remove(registrationID);
         if (removed && listener != null) {
            listener.notify(rc.getMessageLayer(), rc.getAppContext());
         }

         this.keyToRegistrationContextMap.remove(registrationID);
         return removed;
      }
   }

   static class JBossRegistrationContext implements RegistrationContext {
      private String messageLayer;
      private String appContext;
      private String description;
      private boolean isPersistent;

      JBossRegistrationContext(String layer, String appContext, String description, boolean isPersistent) {
         this.messageLayer = layer;
         this.appContext = appContext;
         this.description = description;
         this.isPersistent = isPersistent;
      }

      public String getAppContext() {
         return this.appContext;
      }

      public void setAppContext(String appContext) {
         this.appContext = appContext;
      }

      public String getDescription() {
         return this.description;
      }

      public void setDescription(String description) {
         this.description = description;
      }

      public String getMessageLayer() {
         return this.messageLayer;
      }

      public void setMessageLayer(String messageLayer) {
         this.messageLayer = messageLayer;
      }

      public boolean isPersistent() {
         return this.isPersistent;
      }

      public void setIsPersistent(boolean isPersistent) {
         this.isPersistent = isPersistent;
      }
   }
}
