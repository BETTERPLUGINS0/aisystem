package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.config.RequestConfig;
import java.util.Collection;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ProxyAuthenticationStrategy extends AuthenticationStrategyImpl {
   public static final ProxyAuthenticationStrategy INSTANCE = new ProxyAuthenticationStrategy();

   public ProxyAuthenticationStrategy() {
      super(407, "Proxy-Authenticate");
   }

   Collection<String> getPreferredAuthSchemes(RequestConfig config) {
      return config.getProxyPreferredAuthSchemes();
   }
}
