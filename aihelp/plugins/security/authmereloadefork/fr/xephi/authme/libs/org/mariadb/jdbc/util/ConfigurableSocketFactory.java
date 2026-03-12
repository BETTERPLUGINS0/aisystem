package fr.xephi.authme.libs.org.mariadb.jdbc.util;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import javax.net.SocketFactory;

public abstract class ConfigurableSocketFactory extends SocketFactory {
   public abstract void setConfiguration(Configuration var1, String var2);
}
