package fr.xephi.authme.message;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.FileUtils;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class HelpMessagesFileHandler extends AbstractMessageFileHandler {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(HelpMessagesFileHandler.class);
   private FileConfiguration defaultConfiguration;

   @Inject
   HelpMessagesFileHandler() {
   }

   public String getMessage(String key) {
      String message = this.getMessageIfExists(key);
      if (message == null) {
         this.logger.warning("Error getting message with key '" + key + "'. Please update your config file '" + this.getFilename() + "' or run /authme messages help");
         return this.getDefault(key);
      } else {
         return message;
      }
   }

   private String getDefault(String key) {
      if (this.defaultConfiguration == null) {
         InputStream stream = FileUtils.getResourceFromJar(this.createFilePath("en"));
         this.defaultConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
      }

      String message = this.defaultConfiguration.getString(key);
      return message == null ? "Error retrieving message '" + key + "'" : message;
   }

   protected String createFilePath(String language) {
      return MessagePathHelper.createHelpMessageFilePath(language);
   }
}
