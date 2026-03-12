package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import javax.security.auth.callback.CallbackHandler;
import javax.security.jacc.PolicyContextException;
import javax.security.jacc.PolicyContextHandler;

public class CallbackHandlerPolicyContextHandler implements PolicyContextHandler {
   private static ThreadLocal<CallbackHandler> requestContext = new ThreadLocal();

   public static void setCallbackHandler(CallbackHandler bean) {
      requestContext.set(bean);
   }

   public Object getContext(String key, Object data) throws PolicyContextException {
      Object context = null;
      if (key.equalsIgnoreCase("fr.xephi.authme.libs.org.jboss.security.auth.spi.CallbackHandler")) {
         context = requestContext.get();
      }

      return context;
   }

   public String[] getKeys() throws PolicyContextException {
      String[] keys = new String[]{"fr.xephi.authme.libs.org.jboss.security.auth.spi.CallbackHandler"};
      return keys;
   }

   public boolean supports(String key) throws PolicyContextException {
      return key.equalsIgnoreCase("fr.xephi.authme.libs.org.jboss.security.auth.spi.CallbackHandler");
   }
}
