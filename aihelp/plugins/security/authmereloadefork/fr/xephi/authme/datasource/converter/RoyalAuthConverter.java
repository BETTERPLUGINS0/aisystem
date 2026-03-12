package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.util.Locale;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RoyalAuthConverter implements Converter {
   private static final String LAST_LOGIN_PATH = "timestamps.quit";
   private static final String PASSWORD_PATH = "login.password";
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(RoyalAuthConverter.class);
   private final AuthMe plugin;
   private final DataSource dataSource;

   @Inject
   RoyalAuthConverter(AuthMe plugin, DataSource dataSource) {
      this.plugin = plugin;
      this.dataSource = dataSource;
   }

   public void execute(CommandSender sender) {
      OfflinePlayer[] var2 = this.plugin.getServer().getOfflinePlayers();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         OfflinePlayer player = var2[var4];

         try {
            String name = player.getName().toLowerCase(Locale.ROOT);
            File file = new File(FileUtils.makePath(".", "plugins", "RoyalAuth", "userdata", name + ".yml"));
            if (!this.dataSource.isAuthAvailable(name) && file.exists()) {
               FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
               PlayerAuth auth = PlayerAuth.builder().name(name).password(configuration.getString("login.password"), (String)null).lastLogin(configuration.getLong("timestamps.quit")).realName(player.getName()).build();
               this.dataSource.saveAuth(auth);
               this.dataSource.updateSession(auth);
            }
         } catch (Exception var10) {
            this.logger.logException("Error while trying to import " + player.getName() + " RoyalAuth data", var10);
         }
      }

   }
}
