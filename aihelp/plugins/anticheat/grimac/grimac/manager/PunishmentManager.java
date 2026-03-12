package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.config.ConfigReloadable;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.events.packets.ProxyAlertMessenger;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PunishmentManager implements ConfigReloadable {
   private final GrimPlayer player;
   private final List<PunishGroup> groups = new ArrayList();
   private String experimentalSymbol = "*";
   private String alertString;
   private boolean testMode;
   private String proxyAlertString = "";

   public PunishmentManager(GrimPlayer player) {
      this.player = player;
   }

   public void reload(ConfigManager config) {
      List<String> punish = config.getStringListElse("Punishments", new ArrayList());
      this.experimentalSymbol = config.getStringElse("experimental-symbol", "*");
      this.alertString = config.getStringElse("alerts-format", "%prefix% &f%player% &bfailed &f%check_name% &f(x&c%vl%&f) &7%verbose%");
      this.testMode = config.getBooleanElse("test-mode", false);
      this.proxyAlertString = config.getStringElse("alerts-format-proxy", "%prefix% &f[&cproxy&f] &f%player% &bfailed &f%check_name% &f(x&c%vl%&f) &7%verbose%");

      try {
         this.groups.clear();
         Iterator var3 = this.player.checkManager.allChecks.values().iterator();

         while(var3.hasNext()) {
            AbstractCheck check = (AbstractCheck)var3.next();
            check.setEnabled(false);
         }

         var3 = punish.iterator();

         while(var3.hasNext()) {
            Object s = var3.next();
            LinkedHashMap<String, Object> map = (LinkedHashMap)s;
            List<String> checks = (List)map.getOrDefault("checks", new ArrayList());
            List<String> commands = (List)map.getOrDefault("commands", new ArrayList());
            int removeViolationsAfter = (Integer)map.getOrDefault("remove-violations-after", 300);
            List<ParsedCommand> parsed = new ArrayList();
            List<AbstractCheck> checksList = new ArrayList();
            List<AbstractCheck> excluded = new ArrayList();
            Iterator var12 = checks.iterator();

            String command;
            label66:
            while(var12.hasNext()) {
               command = (String)var12.next();
               command = command.toLowerCase(Locale.ROOT);
               boolean exclude = false;
               if (command.startsWith("!")) {
                  exclude = true;
                  command = command.substring(1);
               }

               Iterator var15 = this.player.checkManager.allChecks.values().iterator();

               while(true) {
                  AbstractCheck check;
                  do {
                     do {
                        if (!var15.hasNext()) {
                           var15 = excluded.iterator();

                           while(var15.hasNext()) {
                              check = (AbstractCheck)var15.next();
                              checksList.remove(check);
                           }
                           continue label66;
                        }

                        check = (AbstractCheck)var15.next();
                     } while(check.getCheckName() == null);
                  } while(!check.getCheckName().toLowerCase(Locale.ROOT).contains(command) && !check.getAlternativeName().toLowerCase(Locale.ROOT).contains(command));

                  if (exclude) {
                     excluded.add(check);
                  } else {
                     checksList.add(check);
                     check.setEnabled(true);
                  }
               }
            }

            var12 = commands.iterator();

            while(var12.hasNext()) {
               command = (String)var12.next();
               String firstNum = command.substring(0, command.indexOf(":"));
               String secondNum = command.substring(command.indexOf(":"), command.indexOf(" "));
               int threshold = Integer.parseInt(firstNum);
               int interval = Integer.parseInt(secondNum.substring(1));
               String commandString = command.substring(command.indexOf(" ") + 1);
               parsed.add(new ParsedCommand(threshold, interval, commandString));
            }

            this.groups.add(new PunishGroup(checksList, parsed, removeViolationsAfter * 1000));
         }
      } catch (Exception var19) {
         LogUtil.error("Error while loading punishments.yml! This is likely your fault!", var19);
      }

   }

   private String replaceAlertPlaceholders(String original, int vl, Check check, String verbose) {
      return MessageUtil.replacePlaceholders(this.player, original.replace("[alert]", this.alertString).replace("[proxy]", this.proxyAlertString).replace("%check_name%", check.getDisplayName()).replace("%experimental%", check.isExperimental() ? this.experimentalSymbol : "").replace("%vl%", Integer.toString(vl)).replace("%description%", check.getDescription())).replace("%verbose%", MiniMessage.miniMessage().escapeTags(verbose));
   }

   public boolean handleAlert(GrimPlayer player, String verbose, Check check) {
      boolean sentDebug = false;
      Iterator var5 = this.groups.iterator();

      label90:
      while(true) {
         PunishGroup group;
         do {
            if (!var5.hasNext()) {
               return sentDebug;
            }

            group = (PunishGroup)var5.next();
         } while(!group.checks.contains(check));

         int vl = this.getViolations(group, check);
         int violationCount = group.violations.size();
         Iterator var9 = group.commands.iterator();

         while(true) {
            ParsedCommand command;
            label86:
            while(true) {
               String cmd;
               Set verboseListeners;
               do {
                  if (!var9.hasNext()) {
                     continue label90;
                  }

                  command = (ParsedCommand)var9.next();
                  cmd = this.replaceAlertPlaceholders(command.command, vl, check, verbose);
                  verboseListeners = null;
                  if (GrimAPI.INSTANCE.getAlertManager().hasVerboseListeners() && command.command.equals("[alert]")) {
                     sentDebug = true;
                     Component component = MessageUtil.miniMessage(cmd);
                     verboseListeners = GrimAPI.INSTANCE.getAlertManager().sendVerbose(component, (Set)null);
                  }
               } while(violationCount < command.threshold);

               boolean inInterval = command.interval == 0 ? command.executeCount == 0 : violationCount % command.interval == 0;
               if (!inInterval) {
                  break;
               }

               CommandExecuteEvent executeEvent = new CommandExecuteEvent(player, check, verbose, cmd);
               GrimAPI.INSTANCE.getEventBus().post(executeEvent);
               if (!executeEvent.isCancelled()) {
                  String var15 = command.command;
                  byte var16 = -1;
                  switch(var15.hashCode()) {
                  case -1979888095:
                     if (var15.equals("[webhook]")) {
                        var16 = 0;
                     }
                     break;
                  case -1821338678:
                     if (var15.equals("[proxy]")) {
                        var16 = 2;
                     }
                     break;
                  case 87367796:
                     if (var15.equals("[log]")) {
                        var16 = 1;
                     }
                     break;
                  case 2038346396:
                     if (var15.equals("[alert]")) {
                        var16 = 3;
                     }
                  }

                  switch(var16) {
                  case 0:
                     GrimAPI.INSTANCE.getDiscordManager().sendAlert(player, verbose, check.getDisplayName(), vl);
                     break label86;
                  case 1:
                     int vls = (int)group.violations.values().stream().filter((e) -> {
                        return e == check;
                     }).count();
                     String verboseWithoutGl = verbose.replaceAll(" /gl .*", "");
                     GrimAPI.INSTANCE.getViolationDatabaseManager().logAlert(player, verboseWithoutGl, check.getDisplayName(), vls);
                     break label86;
                  case 2:
                     ProxyAlertMessenger.sendPluginMessage(cmd);
                     break label86;
                  case 3:
                     sentDebug = true;
                     Component message = MessageUtil.miniMessage(cmd);
                     if (this.testMode) {
                        if (verboseListeners == null || verboseListeners.contains(player.platformPlayer)) {
                           player.sendMessage(message);
                        }
                        break label86;
                     }

                     GrimAPI.INSTANCE.getAlertManager().sendAlert(message, verboseListeners);
                     break label86;
                  default:
                     GrimAPI.INSTANCE.getScheduler().getGlobalRegionScheduler().run(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
                        GrimAPI.INSTANCE.getPlatformServer().dispatchCommand(GrimAPI.INSTANCE.getPlatformServer().getConsoleSender(), cmd);
                     });
                     break label86;
                  }
               }
            }

            ++command.executeCount;
         }
      }
   }

   public void handleViolation(Check check) {
      Iterator var2 = this.groups.iterator();

      while(var2.hasNext()) {
         PunishGroup group = (PunishGroup)var2.next();
         if (group.checks.contains(check)) {
            long currentTime = System.currentTimeMillis();
            group.violations.put(currentTime, check);
            group.violations.long2ObjectEntrySet().removeIf((time) -> {
               return currentTime - time.getLongKey() > (long)group.removeViolationsAfter;
            });
         }
      }

   }

   private int getViolations(PunishGroup group, Check check) {
      int vl = 0;
      ObjectIterator var4 = group.violations.values().iterator();

      while(var4.hasNext()) {
         Check value = (Check)var4.next();
         if (value == check) {
            ++vl;
         }
      }

      return vl;
   }
}
