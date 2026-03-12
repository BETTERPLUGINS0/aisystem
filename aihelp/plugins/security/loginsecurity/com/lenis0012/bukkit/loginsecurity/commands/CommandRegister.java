package com.lenis0012.bukkit.loginsecurity.commands;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.LoginSecurityConfig;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command.Command;
import com.lenis0012.bukkit.loginsecurity.modules.captcha.CaptchaManager;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthAction;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionCallback;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionResponse;
import com.lenis0012.bukkit.loginsecurity.session.action.RegisterAction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandRegister extends Command {
   private final LoginSecurity plugin;

   public CommandRegister(LoginSecurity plugin) {
      this.plugin = plugin;
      this.setAllowConsole(false);
      if (plugin.config().isRegisterConfirmPassword()) {
         this.setMinArgs(2);
      } else {
         this.setMinArgs(1);
      }

   }

   public void execute() {
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(this.player);
      String password = this.getArg(0);
      LoginSecurityConfig config = LoginSecurity.getConfiguration();
      if (!config.isRegistrationEnabled()) {
         this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getRegistrationDisabledMessage()));
      } else if (password.length() >= config.getPasswordMinLength() && password.length() <= config.getPasswordMaxLength()) {
         if (session.isRegistered()) {
            this.reply(false, LoginSecurity.translate(LanguageKeys.REGISTER_ALREADY), new Object[0]);
         } else if (config.isRegisterConfirmPassword() && !password.equals(this.getArg(1))) {
            this.reply(false, LoginSecurity.translate(LanguageKeys.ERROR_MATCH_PASSWORD), new Object[0]);
         } else {
            if (config.isRegisterCaptcha()) {
               CaptchaManager captcha = (CaptchaManager)this.plugin.getModule(CaptchaManager.class);
               captcha.giveMapItem(this.player, () -> {
                  AuthAction action = new RegisterAction(AuthService.PLAYER, this.player, password);
                  session.performActionAsync(action, new CommandRegister.RegisterCallback(this, this.player));
               });
               this.reply(true, LoginSecurity.translate(LanguageKeys.REGISTER_CAPTCHA), new Object[0]);
            } else {
               AuthAction action = new RegisterAction(AuthService.PLAYER, this.player, password);
               session.performActionAsync(action, new CommandRegister.RegisterCallback(this, this.player));
            }

         }
      } else {
         this.reply(false, LoginSecurity.translate(LanguageKeys.GENERAL_PASSWORD_LENGTH).param("min", config.getPasswordMinLength()).param("max", config.getPasswordMaxLength()), new Object[0]);
      }
   }

   private static final class RegisterCallback implements ActionCallback {
      private final CommandRegister command;
      private final Player player;

      private RegisterCallback(CommandRegister command, Player player) {
         this.command = command;
         this.player = player;
      }

      public void call(ActionResponse response) {
         if (!response.isSuccess()) {
            this.command.reply(this.player, false, response.getErrorMessage(), new Object[0]);
         } else {
            this.command.reply(this.player, true, LoginSecurity.translate(LanguageKeys.REGISTER_SUCCESS), new Object[0]);
         }
      }

      // $FF: synthetic method
      RegisterCallback(CommandRegister x0, Player x1, Object x2) {
         this(x0, x1);
      }
   }
}
