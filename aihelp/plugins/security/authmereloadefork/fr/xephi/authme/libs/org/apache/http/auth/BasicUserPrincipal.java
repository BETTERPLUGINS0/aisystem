package fr.xephi.authme.libs.org.apache.http.auth;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.LangUtils;
import java.io.Serializable;
import java.security.Principal;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public final class BasicUserPrincipal implements Principal, Serializable {
   private static final long serialVersionUID = -2266305184969850467L;
   private final String username;

   public BasicUserPrincipal(String username) {
      Args.notNull(username, "User name");
      this.username = username;
   }

   public String getName() {
      return this.username;
   }

   public int hashCode() {
      int hash = 17;
      int hash = LangUtils.hashCode(hash, this.username);
      return hash;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         if (o instanceof BasicUserPrincipal) {
            BasicUserPrincipal that = (BasicUserPrincipal)o;
            if (LangUtils.equals((Object)this.username, (Object)that.username)) {
               return true;
            }
         }

         return false;
      }
   }

   public String toString() {
      StringBuilder buffer = new StringBuilder();
      buffer.append("[principal: ");
      buffer.append(this.username);
      buffer.append("]");
      return buffer.toString();
   }
}
