package fr.xephi.authme.datasource.converter;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;

public abstract class AbstractDataSourceConverter<S extends DataSource> implements Converter {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(MySqlToSqlite.class);
   private final DataSource destination;
   private final DataSourceType destinationType;

   public AbstractDataSourceConverter(DataSource destination, DataSourceType destinationType) {
      this.destination = destination;
      this.destinationType = destinationType;
   }

   public void execute(CommandSender sender) {
      if (this.destinationType != this.destination.getType()) {
         if (sender != null) {
            sender.sendMessage("Please configure your connection to " + this.destinationType + " and re-run this command");
         }

      } else {
         DataSource source;
         try {
            source = this.getSource();
         } catch (Exception var6) {
            Utils.logAndSendMessage(sender, "The data source to convert from could not be initialized");
            this.logger.logException("Could not initialize source:", var6);
            return;
         }

         List<String> skippedPlayers = new ArrayList();
         Iterator var4 = source.getAllAuths().iterator();

         while(var4.hasNext()) {
            PlayerAuth auth = (PlayerAuth)var4.next();
            if (this.destination.isAuthAvailable(auth.getNickname())) {
               skippedPlayers.add(auth.getNickname());
            } else {
               this.destination.saveAuth(auth);
               this.destination.updateSession(auth);
               this.destination.updateQuitLoc(auth);
            }
         }

         if (!skippedPlayers.isEmpty()) {
            Utils.logAndSendMessage(sender, "Skipped conversion for players which were already in " + this.destinationType + ": " + String.join(", ", skippedPlayers));
         }

         Utils.logAndSendMessage(sender, "Database successfully converted from " + source.getType() + " to " + this.destinationType);
      }
   }

   protected abstract S getSource() throws Exception;
}
