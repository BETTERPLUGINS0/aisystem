package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.converter.Converter;
import fr.xephi.authme.datasource.converter.CrazyLoginConverter;
import fr.xephi.authme.datasource.converter.H2ToSqlite;
import fr.xephi.authme.datasource.converter.LoginSecurityConverter;
import fr.xephi.authme.datasource.converter.MySqlToSqlite;
import fr.xephi.authme.datasource.converter.RoyalAuthConverter;
import fr.xephi.authme.datasource.converter.SqliteToH2;
import fr.xephi.authme.datasource.converter.SqliteToSql;
import fr.xephi.authme.datasource.converter.XAuthConverter;
import fr.xephi.authme.libs.ch.jalu.injector.factory.Factory;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSortedMap;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.command.CommandSender;

public class ConverterCommand implements ExecutableCommand {
   @VisibleForTesting
   static final Map<String, Class<? extends Converter>> CONVERTERS = getConverters();
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ConverterCommand.class);
   @Inject
   private CommonService commonService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Factory<Converter> converterFactory;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      Class<? extends Converter> converterClass = getConverterClassFromArgs(arguments);
      if (converterClass == null) {
         sender.sendMessage("Converters: " + String.join(", ", CONVERTERS.keySet()));
      } else {
         Converter converter = (Converter)this.converterFactory.newInstance(converterClass);
         this.bukkitService.runTaskAsynchronously(() -> {
            try {
               converter.execute(sender);
            } catch (Exception var4) {
               this.commonService.send(sender, MessageKey.ERROR);
               this.logger.logException("Error during conversion:", var4);
            }

         });
         sender.sendMessage("[AuthMe] Successfully started " + (String)arguments.get(0));
      }
   }

   private static Class<? extends Converter> getConverterClassFromArgs(List<String> arguments) {
      return arguments.isEmpty() ? null : (Class)CONVERTERS.get(((String)arguments.get(0)).toLowerCase(Locale.ROOT));
   }

   private static Map<String, Class<? extends Converter>> getConverters() {
      return ImmutableSortedMap.naturalOrder().put("xauth", XAuthConverter.class).put("crazylogin", CrazyLoginConverter.class).put("royalauth", RoyalAuthConverter.class).put("sqlitetosql", SqliteToSql.class).put("mysqltosqlite", MySqlToSqlite.class).put("sqlitetoh2", SqliteToH2.class).put("h2tosqlite", H2ToSqlite.class).put("loginsecurity", LoginSecurityConverter.class).build();
   }
}
