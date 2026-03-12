package fr.xephi.authme.libs.org.postgresql.ssl;

import java.util.Properties;
import javax.net.ssl.SSLSocketFactory;

public class DefaultJavaSSLFactory extends WrappedFactory {
   public DefaultJavaSSLFactory(Properties info) {
      this.factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
   }
}
