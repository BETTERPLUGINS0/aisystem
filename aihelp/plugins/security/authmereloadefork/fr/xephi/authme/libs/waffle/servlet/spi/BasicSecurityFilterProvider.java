package fr.xephi.authme.libs.waffle.servlet.spi;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.util.AuthorizationHeader;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicSecurityFilterProvider implements SecurityFilterProvider {
   private static final Logger LOGGER = LoggerFactory.getLogger(BasicSecurityFilterProvider.class);
   private String realm = "BasicSecurityFilterProvider";
   private final IWindowsAuthProvider auth;

   public BasicSecurityFilterProvider(IWindowsAuthProvider newAuthProvider) {
      this.auth = newAuthProvider;
   }

   public IWindowsIdentity doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
      AuthorizationHeader authorizationHeader = new AuthorizationHeader(request);
      String usernamePassword = new String(authorizationHeader.getTokenBytes(), StandardCharsets.UTF_8);
      String[] usernamePasswordArray = usernamePassword.split(":", 2);
      if (usernamePasswordArray.length != 2) {
         throw new RuntimeException("Invalid username:password in Authorization header.");
      } else {
         LOGGER.debug((String)"logging in user: {}", (Object)usernamePasswordArray[0]);
         return this.auth.logonUser(usernamePasswordArray[0], usernamePasswordArray[1]);
      }
   }

   public boolean isPrincipalException(HttpServletRequest request) {
      return false;
   }

   public boolean isSecurityPackageSupported(String securityPackage) {
      return "Basic".equalsIgnoreCase(securityPackage);
   }

   public void sendUnauthorized(HttpServletResponse response) {
      response.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
   }

   public String getRealm() {
      return this.realm;
   }

   public void setRealm(String value) {
      this.realm = value;
   }

   public void initParameter(String parameterName, String parameterValue) {
      if ("realm".equals(parameterName)) {
         this.setRealm(parameterValue);
      } else {
         throw new InvalidParameterException(parameterName);
      }
   }
}
