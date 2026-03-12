package fr.xephi.authme.libs.waffle.util;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import java.util.Base64;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationHeader {
   private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationHeader.class);
   private final HttpServletRequest request;

   public AuthorizationHeader(HttpServletRequest httpServletRequest) {
      this.request = httpServletRequest;
   }

   public String getHeader() {
      return this.request.getHeader("Authorization");
   }

   public boolean isNull() {
      return this.getHeader() == null || this.getHeader().length() == 0;
   }

   public String getSecurityPackage() {
      String header = this.getHeader();
      if (header == null) {
         throw new RuntimeException("Missing Authorization: header");
      } else {
         int space = header.indexOf(32);
         if (space > 0) {
            return header.substring(0, space);
         } else {
            throw new RuntimeException("Invalid Authorization header: " + header);
         }
      }
   }

   public String toString() {
      return this.isNull() ? "<none>" : this.getHeader();
   }

   public String getToken() {
      return this.getHeader().substring(this.getSecurityPackage().length() + 1);
   }

   public byte[] getTokenBytes() {
      try {
         return Base64.getDecoder().decode(this.getToken());
      } catch (IllegalArgumentException var2) {
         LOGGER.debug((String)"", (Throwable)var2);
         throw new RuntimeException("Invalid authorization header.");
      }
   }

   public boolean isNtlmType1Message() {
      if (this.isNull()) {
         return false;
      } else {
         byte[] tokenBytes = this.getTokenBytes();
         if (!NtlmMessage.isNtlmMessage(tokenBytes)) {
            return false;
         } else {
            return 1 == NtlmMessage.getMessageType(tokenBytes);
         }
      }
   }

   public boolean isSPNegTokenInitMessage() {
      if (this.isNull()) {
         return false;
      } else {
         byte[] tokenBytes = this.getTokenBytes();
         return SPNegoMessage.isNegTokenInit(tokenBytes);
      }
   }

   public boolean isNtlmType1PostAuthorizationHeader() {
      if (!"POST".equals(this.request.getMethod()) && !"PUT".equals(this.request.getMethod()) && !"DELETE".equals(this.request.getMethod())) {
         return false;
      } else if (this.request.getContentLength() != 0) {
         return false;
      } else {
         return this.isNtlmType1Message() || this.isSPNegTokenInitMessage();
      }
   }

   public boolean isBearerAuthorizationHeader() {
      return this.isNull() ? false : this.getSecurityPackage().toUpperCase(Locale.ENGLISH).equalsIgnoreCase("BEARER");
   }
}
