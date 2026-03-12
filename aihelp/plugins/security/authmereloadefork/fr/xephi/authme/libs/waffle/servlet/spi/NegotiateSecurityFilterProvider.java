package fr.xephi.authme.libs.waffle.servlet.spi;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.util.AuthorizationHeader;
import fr.xephi.authme.libs.waffle.util.NtlmServletRequest;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsSecurityContext;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NegotiateSecurityFilterProvider implements SecurityFilterProvider {
   private static final Logger LOGGER = LoggerFactory.getLogger(NegotiateSecurityFilterProvider.class);
   private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
   private static final String PROTOCOLS = "protocols";
   private static final String NEGOTIATE = "Negotiate";
   private static final String NTLM = "NTLM";
   private List<String> protocolsList = new ArrayList();
   private final IWindowsAuthProvider auth;

   public NegotiateSecurityFilterProvider(IWindowsAuthProvider newAuthProvider) {
      this.auth = newAuthProvider;
      this.protocolsList.add("Negotiate");
      this.protocolsList.add("NTLM");
   }

   public List<String> getProtocols() {
      return this.protocolsList;
   }

   public void setProtocols(List<String> values) {
      this.protocolsList = values;
   }

   public void sendUnauthorized(HttpServletResponse response) {
      Iterator var2 = this.protocolsList.iterator();

      while(var2.hasNext()) {
         String protocol = (String)var2.next();
         response.addHeader("WWW-Authenticate", protocol);
      }

   }

   public boolean isPrincipalException(HttpServletRequest request) {
      AuthorizationHeader authorizationHeader = new AuthorizationHeader(request);
      boolean ntlmPost = authorizationHeader.isNtlmType1PostAuthorizationHeader();
      LOGGER.debug((String)"authorization: {}, ntlm post: {}", (Object)authorizationHeader, (Object)ntlmPost);
      return ntlmPost;
   }

   public IWindowsIdentity doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
      AuthorizationHeader authorizationHeader = new AuthorizationHeader(request);
      boolean ntlmPost = authorizationHeader.isNtlmType1PostAuthorizationHeader();
      String connectionId = NtlmServletRequest.getConnectionId(request);
      String securityPackage = authorizationHeader.getSecurityPackage();
      LOGGER.debug((String)"security package: {}, connection id: {}", (Object)securityPackage, (Object)connectionId);
      if (ntlmPost) {
         this.auth.resetSecurityToken(connectionId);
      }

      byte[] tokenBuffer = authorizationHeader.getTokenBytes();
      LOGGER.debug((String)"token buffer: {} byte(s)", (Object)tokenBuffer.length);
      IWindowsSecurityContext securityContext = this.auth.acceptSecurityToken(connectionId, tokenBuffer, securityPackage);
      byte[] continueTokenBytes = securityContext.getToken();
      if (continueTokenBytes != null && continueTokenBytes.length > 0) {
         String continueToken = Base64.getEncoder().encodeToString(continueTokenBytes);
         LOGGER.debug((String)"continue token: {}", (Object)continueToken);
         response.addHeader("WWW-Authenticate", securityPackage + " " + continueToken);
      }

      LOGGER.debug((String)"continue required: {}", (Object)securityContext.isContinue());
      if (securityContext.isContinue()) {
         response.setHeader("Connection", "keep-alive");
         response.setStatus(401);
         response.flushBuffer();
         return null;
      } else {
         IWindowsIdentity identity = securityContext.getIdentity();
         securityContext.dispose();
         return identity;
      }
   }

   public boolean isSecurityPackageSupported(String securityPackage) {
      Iterator var2 = this.protocolsList.iterator();

      String protocol;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         protocol = (String)var2.next();
      } while(!protocol.equalsIgnoreCase(securityPackage));

      return true;
   }

   public void initParameter(String parameterName, String parameterValue) {
      if ("protocols".equals(parameterName)) {
         this.protocolsList = new ArrayList();
         String[] protocolNames = parameterValue.split("\\s+", -1);
         String[] var4 = protocolNames;
         int var5 = protocolNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String protocolName = var4[var6];
            protocolName = protocolName.trim();
            if (protocolName.length() > 0) {
               LOGGER.debug((String)"init protocol: {}", (Object)protocolName);
               if (!"Negotiate".equals(protocolName) && !"NTLM".equals(protocolName)) {
                  LOGGER.error((String)"unsupported protocol: {}", (Object)protocolName);
                  throw new RuntimeException("Unsupported protocol: " + protocolName);
               }

               this.protocolsList.add(protocolName);
            }
         }

      } else {
         throw new InvalidParameterException(parameterName);
      }
   }
}
