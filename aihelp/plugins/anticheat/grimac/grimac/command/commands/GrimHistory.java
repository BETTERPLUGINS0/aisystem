package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.manager.violationdatabase.Violation;
import ac.grim.grimac.manager.violationdatabase.ViolationDatabaseManager;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.IntegerParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringParser;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrimHistory implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("history", "hist").permission("grim.help").required("target", StringParser.stringParser(), adapter.onlinePlayerSuggestions()).optional("page", IntegerParser.integerParser()).permission("grim.history").handler(this::handleHistory));
   }

   private void handleHistory(CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      String target = (String)context.get("target");
      Integer page = (Integer)context.getOrDefault("page", 1);
      String msg;
      if (!GrimAPI.INSTANCE.getViolationDatabaseManager().isEnabled()) {
         msg = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("grim-history-disabled", "%prefix% &cHistory subsystem is disabled!");
         sender.sendMessage(MessageUtil.miniMessage(msg));
      } else if (!GrimAPI.INSTANCE.getViolationDatabaseManager().isLoaded()) {
         msg = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("grim-history-load-failure", "%prefix% &cHistory subsystem failed to load! Check server console for errors.");
         sender.sendMessage(MessageUtil.miniMessage(msg));
      } else {
         GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
            int entriesPerPage = GrimAPI.INSTANCE.getConfigManager().getConfig().getIntElse("history.entries-per-page", 15);
            String header = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("grim-history-header", "%prefix% &bShowing logs for &f%player% (&f%page%&b/&f%maxPages%&f)");
            String logFormat = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("grim-history-entry", "%prefix% &8[&f%server%&8] &bFailed &f%check% (x&c%vl%&f) &7%verbose% (&b%timeago% ago&7)");
            OfflinePlatformPlayer targetPlayer = GrimAPI.INSTANCE.getPlatformPlayerFactory().getOfflineFromName(target);
            ViolationDatabaseManager violations = GrimAPI.INSTANCE.getViolationDatabaseManager();
            int logCount = violations.getLogCount(targetPlayer.getUniqueId());
            List<Violation> logs = violations.getViolations(targetPlayer.getUniqueId(), page, entriesPerPage);
            int maxPages = (int)Math.ceil((double)((float)logCount / (float)entriesPerPage));
            sender.sendMessage(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(sender, header.replace("%player%", targetPlayer.getName()).replace("%page%", String.valueOf(page)).replace("%maxPages%", String.valueOf(maxPages)))));

            for(int i = logs.size() - 1; i >= 0; --i) {
               Violation log = (Violation)logs.get(i);
               sender.sendMessage(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(sender, logFormat.replace("%player%", targetPlayer.getName()).replace("%grim_version%", log.grimVersion()).replace("%client_brand%", log.clientBrand()).replace("%client_version%", log.clientVersion()).replace("%server_version%", log.serverVersion()).replace("%check%", log.checkName()).replace("%verbose%", log.verbose()).replace("%vl%", String.valueOf(log.vl())).replace("%timeago%", this.getTimeAgo(log.createdAt())).replace("%server%", log.server()))));
            }

         });
      }
   }

   private String getTimeAgo(long timestamp) {
      long durationMillis = System.currentTimeMillis() - timestamp;
      if (durationMillis < 0L) {
         return "0s";
      } else {
         long days = TimeUnit.MILLISECONDS.toDays(durationMillis);
         durationMillis -= TimeUnit.DAYS.toMillis(days);
         long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
         durationMillis -= TimeUnit.HOURS.toMillis(hours);
         long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
         durationMillis -= TimeUnit.MINUTES.toMillis(minutes);
         long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
         StringBuilder result = new StringBuilder();
         if (days > 0L) {
            result.append(days).append("d ");
         }

         if (hours > 0L) {
            result.append(hours).append("h ");
         }

         if (minutes > 0L) {
            result.append(minutes).append("m ");
         }

         if (seconds > 0L || result.isEmpty()) {
            result.append(seconds).append("s");
         }

         return result.toString().trim();
      }
   }
}
