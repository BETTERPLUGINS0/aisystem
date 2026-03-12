package com.lenis0012.bukkit.loginsecurity.commands;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command.Command;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthAction;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.LogoutAction;
import org.bukkit.entity.Player;

public class CommandLogout extends Command {
   private final LoginSecurity plugin;

   public CommandLogout(LoginSecurity plugin) {
      this.plugin = plugin;
      this.setAllowConsole(false);
   }

   public void execute() {
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(this.player);
      if (!session.isLoggedIn()) {
         this.reply(false, LoginSecurity.translate(LanguageKeys.GENERAL_NOT_LOGGED_IN), new Object[0]);
      } else {
         Player player = this.player;
         AuthAction action = new LogoutAction(AuthService.PLAYER, player);
         session.performActionAsync(action, (response) -> {
            if (!response.isSuccess()) {
               this.reply(player, false, LoginSecurity.translate(LanguageKeys.LOGOUT_FAIL).param("error", response.getErrorMessage()), new Object[0]);
            } else {
               this.reply(player, true, LoginSecurity.translate(LanguageKeys.LOGOUT_SUCCESS), new Object[0]);
            }
         });
      }
   }
}
