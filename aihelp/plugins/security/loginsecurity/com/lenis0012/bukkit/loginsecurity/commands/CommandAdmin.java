package com.lenis0012.bukkit.loginsecurity.commands;

import com.google.common.collect.Maps;
import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command.Command;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.modules.storage.StorageImport;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.ChangePassAction;
import com.lenis0012.bukkit.loginsecurity.session.action.RemovePassAction;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdmin extends Command {
   private final Map<String, Method> methods = Maps.newLinkedHashMap();
   private final LoginSecurity plugin;

   public CommandAdmin(LoginSecurity plugin) {
      this.plugin = plugin;
      this.setAllowConsole(true);
      this.setPermission("loginsecurity.admin");
      this.setUsage("/lac");
      Method[] var2 = this.getClass().getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         SubCommand subCommand = (SubCommand)method.getAnnotation(SubCommand.class);
         if (subCommand != null) {
            this.methods.put(subCommand.name().isEmpty() ? method.getName() : subCommand.name(), method);
         }
      }

   }

   public void execute() {
      String subCommand = this.getArgLength() > 0 ? this.getArg(0) : "help";
      Method method = (Method)this.methods.get(subCommand.toLowerCase());
      if (method == null) {
         this.reply(false, LoginSecurity.translate(LanguageKeys.COMMAND_UNKNOWN).param("cmd", "/lac"), new Object[0]);
      } else {
         SubCommand info = (SubCommand)method.getAnnotation(SubCommand.class);
         if (this.getArgLength() < info.minArgs() + 1) {
            this.reply(false, LoginSecurity.translate(LanguageKeys.COMMAND_NOT_ENOUGH_ARGS).param("cmd", "/lac"), new Object[0]);
         } else {
            try {
               method.invoke(this);
            } catch (Exception var5) {
               this.plugin.getLogger().log(Level.SEVERE, "Error while executing command", var5);
               this.reply(false, LoginSecurity.translate(LanguageKeys.COMMAND_ERROR).param("error", var5.getMessage() != null ? var5.getMessage() : ""), new Object[0]);
            }

         }
      }
   }

   @SubCommand(
      description = "lacHelp",
      minArgs = -1
   )
   public void help() {
      this.reply("&3&lL&b&loginSecurity &3&lA&b&ldmin &3&lC&b&lommand:", new Object[0]);
      Iterator var1 = this.methods.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<String, Method> entry = (Entry)var1.next();
         String name = (String)entry.getKey();
         SubCommand info = (SubCommand)((Method)entry.getValue()).getAnnotation(SubCommand.class);
         String usage = info.usage().isEmpty() ? "" : " " + (info.usage().startsWith("NoTrans:") ? info.usage().substring("NoTrans:".length()) : LoginSecurity.translate(info.usage()).toString());
         String desc = info.description().startsWith("NoTrans:") ? info.description().substring("NoTrans:".length()) : LoginSecurity.translate(info.description()).toString();
         this.reply("&b/lac " + name + usage + " &7- &f" + desc, new Object[0]);
      }

   }

   @SubCommand(
      description = "lacReload"
   )
   public void reload() {
      LoginSecurity.getConfiguration().reload();
      this.reply(true, LoginSecurity.translate(LanguageKeys.LAC_RELOAD_SUCCESS), new Object[0]);
   }

   @SubCommand(
      description = "lacRmpass",
      usage = "lacRmpassArgs",
      minArgs = 1
   )
   public void rmpass() {
      String name = this.getArg(1);
      Player target = Bukkit.getPlayer(name);
      PlayerSession session = target != null ? LoginSecurity.getSessionManager().getPlayerSession(target) : LoginSecurity.getSessionManager().getOfflineSession(name);
      if (!session.isRegistered()) {
         this.reply(false, LoginSecurity.translate(LanguageKeys.LAC_NOT_REGISTERED), new Object[0]);
      } else {
         CommandSender admin = this.sender;
         session.performActionAsync(new RemovePassAction(AuthService.ADMIN, admin), (response) -> {
            this.reply(admin, true, LoginSecurity.translate(LanguageKeys.LAC_RESET_PLAYER), new Object[0]);
         });
      }
   }

   @SubCommand(
      description = "lacChangepass",
      usage = "lacChangepassArgs",
      minArgs = 2
   )
   public void changepass() {
      String name = this.getArg(1);
      Player target = Bukkit.getPlayer(name);
      PlayerSession session = target != null ? LoginSecurity.getSessionManager().getPlayerSession(target) : LoginSecurity.getSessionManager().getOfflineSession(name);
      if (!session.isRegistered()) {
         this.reply(false, LoginSecurity.translate(LanguageKeys.LAC_NOT_REGISTERED), new Object[0]);
      } else {
         CommandSender admin = this.sender;
         session.performActionAsync(new ChangePassAction(AuthService.ADMIN, admin, this.getArg(2)), (response) -> {
            this.reply(admin, true, LoginSecurity.translate(LanguageKeys.LAC_CHANGED_PASSWORD), new Object[0]);
         });
      }
   }

   @SubCommand(
      name = "import",
      description = "NoTrans:Import profiles into LoginSecurity",
      usage = "NoTrans:loginsecurity",
      minArgs = 1
   )
   public void importFrom() {
      String source = this.getArg(1);
      StorageImport storageImport = StorageImport.fromSourceName(source, this.sender);
      if (storageImport == null) {
         this.reply(false, "Unknown import source: " + source, new Object[0]);
      } else {
         this.reply(true, "Importing profiles from " + source, new Object[0]);
         Bukkit.getScheduler().runTaskAsynchronously(this.plugin, storageImport);
      }
   }
}
