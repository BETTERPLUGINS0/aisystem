package github.nighter.smartspawner.libs.mariadb.util;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import javax.net.SocketFactory;

public abstract class ConfigurableSocketFactory extends SocketFactory {
   public abstract void setConfiguration(Configuration var1, String var2);
}
