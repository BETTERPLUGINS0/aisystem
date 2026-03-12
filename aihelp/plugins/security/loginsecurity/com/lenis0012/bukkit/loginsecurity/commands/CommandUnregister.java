package com.lenis0012.bukkit.loginsecurity.commands;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.hashing.Algorithm;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command.Command;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionResponse;
import com.lenis0012.bukkit.loginsecurity.session.action.RemovePassAction;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandUnregister extends Command {
   private final LoginSecurity plugin;

   public CommandUnregister(LoginSecurity plugin) {
      this.plugin = plugin;
      this.setMinArgs(1);
      this.setAllowConsole(false);
   }

   public void execute() {
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(this.player);
      String password = this.getArg(0);
      if (!session.isLoggedIn()) {
         this.reply(false, LoginSecurity.translate(LanguageKeys.GENERAL_NOT_LOGGED_IN), new Object[0]);
      } else if (!LoginSecurity.getConfiguration().isPasswordRequired() || this.player.isPermissionSet("ls.bypass") && this.player.hasPermission("ls.bypass")) {
         PlayerProfile profile = session.getProfile();
         Algorithm algorithm = Algorithm.getById(profile.getHashingAlgorithm());
         if (algorithm == null) {
            this.reply(false, LoginSecurity.translate(LanguageKeys.GENERAL_UNKNOWN_HASH), new Object[0]);
         } else {
            Player player = this.player;
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
               boolean validated = algorithm.check(password, profile.getPassword());
               if (!validated) {
                  this.reply(player, false, LoginSecurity.translate(LanguageKeys.UNREGISTER_FAIL), new Object[0]);
               } else {
                  RemovePassAction action = new RemovePassAction(AuthService.PLAYER, player);
                  ActionResponse response = session.performAction(action);
                  if (!response.isSuccess()) {
                     this.reply(player, false, response.getErrorMessage(), new Object[0]);
                  } else {
                     this.reply(player, true, LoginSecurity.translate(LanguageKeys.UNREGISTER_SUCCESS), new Object[0]);
                  }
               }
            });
         }
      } else {
         this.reply(false, LoginSecurity.translate(LanguageKeys.UNREGISTER_NOT_POSSIBLE), new Object[0]);
      }
   }
}
