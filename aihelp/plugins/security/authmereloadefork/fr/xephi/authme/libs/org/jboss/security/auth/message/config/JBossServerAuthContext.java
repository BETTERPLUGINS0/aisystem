package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

public class JBossServerAuthContext implements ServerAuthContext {
   private List<ServerAuthModule> modules = new ArrayList();
   private Map<String, Map> moduleOptionsByName = new HashMap();
   protected List<ControlFlag> controlFlags = new ArrayList();

   public JBossServerAuthContext(List<ServerAuthModule> modules, Map<String, Map> moduleNameToOptions, CallbackHandler cbh) throws AuthException {
      this.modules = modules;
      this.moduleOptionsByName = moduleNameToOptions;
      Iterator i$ = modules.iterator();

      while(i$.hasNext()) {
         ServerAuthModule sam = (ServerAuthModule)i$.next();
         sam.initialize((MessagePolicy)null, (MessagePolicy)null, cbh, (Map)this.moduleOptionsByName.get(sam.getClass().getName()));
      }

   }

   public void setControlFlags(List<ControlFlag> controlFlags) {
      this.controlFlags = controlFlags;
   }

   public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
      Iterator i$ = this.modules.iterator();

      while(i$.hasNext()) {
         ServerAuthModule sam = (ServerAuthModule)i$.next();
         sam.cleanSubject(messageInfo, subject);
      }

   }

   public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
      AuthStatus status = null;

      ServerAuthModule sam;
      for(Iterator i$ = this.modules.iterator(); i$.hasNext(); status = sam.secureResponse(messageInfo, serviceSubject)) {
         sam = (ServerAuthModule)i$.next();
      }

      return status;
   }

   public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
      List<ServerAuthModule> supportingModules = new ArrayList();
      Class requestType = messageInfo.getRequestMessage().getClass();
      Class[] requestInterfaces = requestType.getInterfaces();
      List<Class> intfaee = Arrays.asList(requestInterfaces);
      Iterator i$ = this.modules.iterator();

      while(true) {
         ServerAuthModule sam;
         List supportedTypes;
         do {
            if (!i$.hasNext()) {
               if (supportingModules.size() == 0) {
                  throw PicketBoxMessages.MESSAGES.noServerAuthModuleForRequestType(requestType);
               }

               AuthStatus authStatus = this.invokeModules(messageInfo, clientSubject, serviceSubject);
               return authStatus;
            }

            sam = (ServerAuthModule)i$.next();
            supportedTypes = Arrays.asList(sam.getSupportedMessageTypes());
            Iterator i$ = intfaee.iterator();

            while(i$.hasNext()) {
               Class clazz = (Class)i$.next();
               if (supportedTypes.contains(clazz) && !supportingModules.contains(sam)) {
                  supportingModules.add(sam);
               }
            }
         } while(!supportedTypes.contains(Object.class) && !supportedTypes.contains(requestType));

         if (!supportingModules.contains(sam)) {
            supportingModules.add(sam);
         }
      }
   }

   private AuthStatus invokeModules(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
      boolean encounteredRequiredError = false;
      boolean encounteredOptionalError = false;
      AuthException moduleException = null;
      AuthStatus overallDecision = AuthStatus.FAILURE;
      int length = this.modules.size();

      for(int i = 0; i < length; ++i) {
         ServerAuthModule module = (ServerAuthModule)this.modules.get(i);
         ControlFlag flag = (ControlFlag)this.controlFlags.get(i);
         AuthStatus decision = AuthStatus.FAILURE;

         try {
            decision = module.validateRequest(messageInfo, clientSubject, serviceSubject);
         } catch (Exception var14) {
            decision = AuthStatus.FAILURE;
            if (moduleException == null) {
               moduleException = new AuthException(var14.getMessage());
            }
         }

         if (decision == AuthStatus.SUCCESS) {
            overallDecision = AuthStatus.SUCCESS;
            if (flag == ControlFlag.SUFFICIENT && !encounteredRequiredError) {
               return AuthStatus.SUCCESS;
            }
         } else {
            if (flag == ControlFlag.REQUISITE) {
               if (moduleException != null) {
                  throw moduleException;
               }

               moduleException = new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
            }

            if (flag == ControlFlag.REQUIRED && !encounteredRequiredError) {
               encounteredRequiredError = true;
            }

            if (flag == ControlFlag.OPTIONAL) {
               encounteredOptionalError = true;
            }
         }
      }

      String msg = this.getAdditionalErrorMessage(moduleException);
      if (encounteredRequiredError) {
         throw new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage() + msg);
      } else if (overallDecision == AuthStatus.FAILURE && encounteredOptionalError) {
         throw new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage() + msg);
      } else if (overallDecision == AuthStatus.FAILURE) {
         throw new AuthException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
      } else {
         return AuthStatus.SUCCESS;
      }
   }

   private String getAdditionalErrorMessage(Exception e) {
      StringBuilder msg = new StringBuilder(" ");
      if (e != null) {
         msg.append(e.getLocalizedMessage());
      }

      return msg.toString();
   }
}
