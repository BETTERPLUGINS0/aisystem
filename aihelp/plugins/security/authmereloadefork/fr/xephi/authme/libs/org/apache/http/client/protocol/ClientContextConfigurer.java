package fr.xephi.authme.libs.org.apache.http.client.protocol;

import fr.xephi.authme.libs.org.apache.http.auth.AuthSchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.client.CookieStore;
import fr.xephi.authme.libs.org.apache.http.client.CredentialsProvider;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecRegistry;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public class ClientContextConfigurer implements ClientContext {
   private final HttpContext context;

   public ClientContextConfigurer(HttpContext context) {
      Args.notNull(context, "HTTP context");
      this.context = context;
   }

   public void setCookieSpecRegistry(CookieSpecRegistry registry) {
      this.context.setAttribute("http.cookiespec-registry", registry);
   }

   public void setAuthSchemeRegistry(AuthSchemeRegistry registry) {
      this.context.setAttribute("http.authscheme-registry", registry);
   }

   public void setCookieStore(CookieStore store) {
      this.context.setAttribute("http.cookie-store", store);
   }

   public void setCredentialsProvider(CredentialsProvider provider) {
      this.context.setAttribute("http.auth.credentials-provider", provider);
   }
}
