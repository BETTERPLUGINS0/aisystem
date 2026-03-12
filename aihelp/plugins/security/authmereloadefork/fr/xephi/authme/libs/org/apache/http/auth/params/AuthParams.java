package fr.xephi.authme.libs.org.apache.http.auth.params;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HTTP;
import fr.xephi.authme.libs.org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public final class AuthParams {
   private AuthParams() {
   }

   public static String getCredentialCharset(HttpParams params) {
      Args.notNull(params, "HTTP parameters");
      String charset = (String)params.getParameter("http.auth.credential-charset");
      if (charset == null) {
         charset = HTTP.DEF_PROTOCOL_CHARSET.name();
      }

      return charset;
   }

   public static void setCredentialCharset(HttpParams params, String charset) {
      Args.notNull(params, "HTTP parameters");
      params.setParameter("http.auth.credential-charset", charset);
   }
}
