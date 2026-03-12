package com.lenis0012.bukkit.loginsecurity.commands;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.LoginSecurityConfig;
import com.lenis0012.bukkit.loginsecurity.hashing.Algorithm;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command.Command;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthAction;
import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionResponse;
import com.lenis0012.bukkit.loginsecurity.session.action.ChangePassAction;
import com.lenis0012.bukkit.loginsecurity.session.action.LoginAction;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandLogin extends Command {
   public CommandLogin(LoginSecurity plugin) {
      this.setMinArgs(1);
      this.setAllowConsole(false);
   }

   public void execute() {
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(this.player);
      String password = this.getArg(0);
      LoginSecurityConfig config = LoginSecurity.getConfiguration();
      int tries = MetaData.incrementAndGet(this.player, "ls_login_tries");
      if (tries > config.getMaxLoginTries()) {
         this.player.kickPlayer("[LoginSecurity] " + LoginSecurity.translate(LanguageKeys.LOGIN_TRIES_EXCEEDED).param("max", config.getMaxLoginTries()).toString());
      } else if (session.getAuthMode() != AuthMode.UNAUTHENTICATED) {
         this.reply(false, LoginSecurity.translate(LanguageKeys.GENERAL_NOT_AUTHENTICATED), new Object[0]);
      } else {
         PlayerProfile profile = session.getProfile();
         Algorithm algorithm = Algorithm.getById(profile.getHashingAlgorithm());
         if (algorithm == null) {
            this.reply(false, LoginSecurity.translate(LanguageKeys.GENERAL_UNKNOWN_HASH), new Object[0]);
         } else {
            Player player = this.player;
            Bukkit.getScheduler().runTaskAsynchronously(LoginSecurity.getInstance(), () -> {
               boolean validated = algorithm.check(password, profile.getPassword());
               if (!validated) {
                  this.reply(player, false, LoginSecurity.translate(LanguageKeys.LOGIN_FAIL), new Object[0]);
               } else {
                  AuthAction action = new LoginAction(AuthService.PLAYER, player);
                  ActionResponse response = session.performAction(action);
                  if (!response.isSuccess()) {
                     this.reply(player, false, response.getErrorMessage(), new Object[0]);
                  } else {
                     this.reply(player, true, LoginSecurity.translate(LanguageKeys.LOGIN_SUCCESS), new Object[0]);
                     if (algorithm.isDeprecated()) {
                        LoginSecurity.getInstance().getLogger().log(Level.INFO, "Migrating password for user " + player.getName());
                        ChangePassAction changePassAction = new ChangePassAction(AuthService.PLUGIN, LoginSecurity.getInstance(), password);
                        session.performActionAsync(changePassAction, (r) -> {
                           LoginSecurity.getInstance().getLogger().log(Level.INFO, "Password migration successfully finished for " + player.getName());
                        });
                     }

                  }
               }
            });
         }
      }
   }
}
