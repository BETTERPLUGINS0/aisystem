package fr.xephi.authme.libs.org.jboss.security.plugins.auth;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import fr.xephi.authme.libs.org.jboss.security.ServerAuthenticationManager;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.jacc.PolicyContext;

public class JASPIServerAuthenticationManager extends JaasSecurityManagerBase implements ServerAuthenticationManager {
   public JASPIServerAuthenticationManager() {
   }

   public JASPIServerAuthenticationManager(String securityDomain, CallbackHandler handler) {
      super(securityDomain, handler);
   }

   public boolean isValid(MessageInfo requestMessage, Subject clientSubject, String layer, CallbackHandler handler) {
      return this.isValid(requestMessage, clientSubject, layer, PolicyContext.getContextID(), handler);
   }

   public boolean isValid(MessageInfo messageInfo, Subject clientSubject, String layer, String appContext, CallbackHandler callbackHandler) {
      AuthConfigFactory factory = AuthConfigFactory.getFactory();
      AuthConfigProvider provider = factory.getConfigProvider(layer, appContext, (RegistrationListener)null);
      if (provider == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullAuthConfigProviderForLayer(layer, appContext);
      } else {
         ServerAuthConfig serverConfig = null;

         try {
            serverConfig = provider.getServerAuthConfig(layer, appContext, callbackHandler);
         } catch (AuthException var17) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var17);
            PicketBoxLogger.LOGGER.errorGettingServerAuthConfig(layer, appContext, var17);
            return false;
         }

         String authContextId = serverConfig.getAuthContextID(messageInfo);
         Properties properties = new Properties();
         properties.setProperty("security-domain", super.getSecurityDomain());
         ServerAuthContext sctx = null;

         try {
            sctx = serverConfig.getAuthContext(authContextId, new Subject(), properties);
         } catch (AuthException var16) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var16);
            PicketBoxLogger.LOGGER.errorGettingServerAuthContext(authContextId, super.getSecurityDomain(), var16);
            return false;
         }

         if (clientSubject == null) {
            clientSubject = new Subject();
         }

         Subject serviceSubject = new Subject();
         AuthStatus status = AuthStatus.FAILURE;

         try {
            status = sctx.validateRequest(messageInfo, clientSubject, serviceSubject);
         } catch (AuthException var15) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var15);
            PicketBoxLogger.LOGGER.debugIgnoredException(var15);
         }

         return AuthStatus.SUCCESS == status;
      }
   }

   public void secureResponse(MessageInfo messageInfo, Subject serviceSubject, String layer, String appContext, CallbackHandler handler) {
      AuthConfigFactory factory = AuthConfigFactory.getFactory();
      AuthConfigProvider provider = factory.getConfigProvider(layer, appContext, (RegistrationListener)null);
      if (provider == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullAuthConfigProviderForLayer(layer, appContext);
      } else {
         ServerAuthConfig serverConfig = null;

         try {
            serverConfig = provider.getServerAuthConfig(layer, appContext, handler);
         } catch (AuthException var15) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var15);
            PicketBoxLogger.LOGGER.errorGettingServerAuthConfig(layer, appContext, var15);
            return;
         }

         String authContextId = serverConfig.getAuthContextID(messageInfo);
         Properties properties = new Properties();
         properties.setProperty("security-domain", super.getSecurityDomain());
         if (serviceSubject == null) {
            serviceSubject = new Subject();
         }

         ServerAuthContext sctx = null;

         try {
            sctx = serverConfig.getAuthContext(authContextId, serviceSubject, properties);
         } catch (AuthException var14) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var14);
            PicketBoxLogger.LOGGER.errorGettingServerAuthContext(authContextId, super.getSecurityDomain(), var14);
            return;
         }

         try {
            sctx.secureResponse(messageInfo, serviceSubject);
         } catch (AuthException var13) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var13);
            PicketBoxLogger.LOGGER.debugIgnoredException(var13);
         }

      }
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject, String layer, String appContext, CallbackHandler handler) {
      AuthConfigFactory factory = AuthConfigFactory.getFactory();
      AuthConfigProvider provider = factory.getConfigProvider(layer, appContext, (RegistrationListener)null);
      if (provider == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullAuthConfigProviderForLayer(layer, appContext);
      } else {
         ServerAuthConfig serverConfig = null;

         try {
            serverConfig = provider.getServerAuthConfig(layer, appContext, handler);
         } catch (AuthException var16) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var16);
            PicketBoxLogger.LOGGER.errorGettingServerAuthConfig(layer, appContext, var16);
            return;
         }

         String authContextId = serverConfig.getAuthContextID(messageInfo);
         Properties properties = new Properties();
         properties.setProperty("security-domain", super.getSecurityDomain());
         Subject serviceSubject = new Subject();
         ServerAuthContext sctx = null;

         try {
            sctx = serverConfig.getAuthContext(authContextId, serviceSubject, properties);
         } catch (AuthException var15) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var15);
            PicketBoxLogger.LOGGER.errorGettingServerAuthContext(authContextId, super.getSecurityDomain(), var15);
            return;
         }

         try {
            sctx.cleanSubject(messageInfo, subject);
         } catch (AuthException var14) {
            SecurityContextAssociation.getSecurityContext().getData().put(AuthException.class.getName(), var14);
            PicketBoxLogger.LOGGER.debugIgnoredException(var14);
         }

      }
   }
}
