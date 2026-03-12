package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;

public class JBossAuthConfigProvider implements AuthConfigProvider {
   private Map<String, Object> contextProperties = null;
   private String cbhProperty = "authconfigprovider.client.callbackhandler";

   public JBossAuthConfigProvider(Map<String, Object> props, AuthConfigFactory factory) {
      this.contextProperties = props;
      if (factory != null) {
         factory.registerConfigProvider(this, (String)null, (String)null, "JBossAuthConfigProvider Self Registration");
      }

   }

   public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException {
      if (handler == null) {
         try {
            handler = this.instantiateCallbackHandler();
         } catch (Exception var5) {
            throw new AuthException(var5.getLocalizedMessage());
         }
      }

      return new JBossClientAuthConfig(layer, appContext, handler, this.contextProperties);
   }

   public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException {
      if (handler == null) {
         try {
            handler = this.instantiateCallbackHandler();
         } catch (Exception var5) {
            throw new AuthException(var5.getLocalizedMessage());
         }
      }

      return new JBossServerAuthConfig(layer, appContext, handler, this.contextProperties);
   }

   public void refresh() {
   }

   private CallbackHandler instantiateCallbackHandler() throws Exception {
      String cbhClass = System.getProperty(this.cbhProperty);
      if (cbhClass == null) {
         throw PicketBoxMessages.MESSAGES.callbackHandlerSysPropertyNotSet(this.cbhProperty);
      } else {
         ClassLoader cl = SecurityActions.getContextClassLoader();
         Class<?> cls = cl.loadClass(cbhClass);
         return (CallbackHandler)cls.newInstance();
      }
   }
}
