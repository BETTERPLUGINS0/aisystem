package fr.xephi.authme.libs.waffle.servlet;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class NegotiateRequestWrapper extends HttpServletRequestWrapper {
   private final WindowsPrincipal principal;

   public NegotiateRequestWrapper(HttpServletRequest newRequest, WindowsPrincipal newPrincipal) {
      super(newRequest);
      this.principal = newPrincipal;
   }

   public Principal getUserPrincipal() {
      return this.principal;
   }

   public String getAuthType() {
      return "NEGOTIATE";
   }

   public String getRemoteUser() {
      return this.principal.getName();
   }

   public boolean isUserInRole(String role) {
      return this.principal.hasRole(role);
   }
}
