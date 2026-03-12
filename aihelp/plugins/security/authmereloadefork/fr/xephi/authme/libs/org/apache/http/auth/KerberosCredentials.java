package fr.xephi.authme.libs.org.apache.http.auth;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import java.io.Serializable;
import java.security.Principal;
import org.ietf.jgss.GSSCredential;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class KerberosCredentials implements Credentials, Serializable {
   private static final long serialVersionUID = 487421613855550713L;
   private final GSSCredential gssCredential;

   public KerberosCredentials(GSSCredential gssCredential) {
      this.gssCredential = gssCredential;
   }

   public GSSCredential getGSSCredential() {
      return this.gssCredential;
   }

   public Principal getUserPrincipal() {
      return null;
   }

   public String getPassword() {
      return null;
   }
}
