package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ConverterSettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import org.bukkit.command.CommandSender;

public class CrazyLoginConverter implements Converter {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(CrazyLoginConverter.class);
   private final DataSource database;
   private final Settings settings;
   private final File dataFolder;

   @Inject
   CrazyLoginConverter(@DataFolder File dataFolder, DataSource dataSource, Settings settings) {
      this.dataFolder = dataFolder;
      this.database = dataSource;
      this.settings = settings;
   }

   public void execute(CommandSender sender) {
      String fileName = (String)this.settings.getProperty(ConverterSettings.CRAZYLOGIN_FILE_NAME);
      File source = new File(this.dataFolder, fileName);
      if (!source.exists()) {
         sender.sendMessage("CrazyLogin file not found, please put " + fileName + " in AuthMe folder!");
      } else {
         try {
            BufferedReader users = new BufferedReader(new FileReader(source));

            try {
               String line;
               while((line = users.readLine()) != null) {
                  if (line.contains("|")) {
                     this.migrateAccount(line);
                  }
               }

               this.logger.info("CrazyLogin database has been imported correctly");
            } catch (Throwable var9) {
               try {
                  users.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            users.close();
         } catch (IOException var10) {
            this.logger.warning("Can't open the crazylogin database file! Does it exist?");
            this.logger.logException("Encountered", var10);
         }

      }
   }

   private void migrateAccount(String line) {
      String[] args = line.split("\\|");
      if (args.length >= 2 && !"name".equalsIgnoreCase(args[0])) {
         String playerName = args[0];
         String password = args[1];
         if (password != null) {
            PlayerAuth auth = PlayerAuth.builder().name(playerName.toLowerCase(Locale.ROOT)).realName(playerName).password(password, (String)null).build();
            this.database.saveAuth(auth);
         }

      }
   }
}
