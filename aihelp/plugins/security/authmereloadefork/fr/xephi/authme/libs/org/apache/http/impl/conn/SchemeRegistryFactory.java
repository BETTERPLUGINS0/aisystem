package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.PlainSocketFactory;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.Scheme;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.SchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.conn.ssl.SSLSocketFactory;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE
)
public final class SchemeRegistryFactory {
   public static SchemeRegistry createDefault() {
      SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
      registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
      return registry;
   }

   public static SchemeRegistry createSystemDefault() {
      SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
      registry.register(new Scheme("https", 443, SSLSocketFactory.getSystemSocketFactory()));
      return registry;
   }
}
