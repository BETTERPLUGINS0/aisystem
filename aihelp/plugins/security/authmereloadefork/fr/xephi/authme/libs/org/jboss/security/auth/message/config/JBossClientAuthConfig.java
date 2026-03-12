package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;

public class JBossClientAuthConfig implements ClientAuthConfig {
   private String layer = null;
   private String contextId = null;
   private CallbackHandler callbackHandler = null;
   private List modules = new ArrayList();
   private Map contextProperties;

   public JBossClientAuthConfig(String layer, String appContext, CallbackHandler handler, Map properties) {
      this.layer = layer;
      this.contextId = appContext;
      this.callbackHandler = handler;
      this.contextProperties = properties;
   }

   public ClientAuthContext getAuthContext(String authContextID, Subject clientSubject, Map properties) throws AuthException {
      return new JBossClientAuthContext(this);
   }

   public String getMessageLayer() {
      return this.layer;
   }

   public void refresh() {
   }

   public List getClientAuthModules() {
      return this.modules;
   }

   public String getAppContext() {
      return this.contextId;
   }

   public String getAuthContextID(MessageInfo messageInfo) {
      throw new UnsupportedOperationException();
   }

   public boolean isProtected() {
      throw new UnsupportedOperationException();
   }
}
