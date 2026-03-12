package com.lenis0012.bukkit.loginsecurity.session;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AuthService<T> {
   public static AuthService<Player> PLAYER = new AuthService(Player.class);
   public static AuthService<CommandSender> ADMIN = new AuthService(CommandSender.class);
   public static AuthService<LoginSecurity> SESSION = new AuthService(LoginSecurity.class);
   public static AuthService<String> CHANNEL_API = new AuthService(String.class);
   public static AuthService<Plugin> PLUGIN = new AuthService(Plugin.class);
   private final Class<T> type;

   private AuthService(Class<T> type) {
      this.type = type;
   }

   public T getProvider(AuthAction action) {
      return this.type.cast(action.getServiceProvider());
   }

   public String format(T provider) {
      if (this == PLAYER) {
         return ((Player)provider).getName();
      } else if (this == ADMIN) {
         return ((CommandSender)provider).getName();
      } else if (this == SESSION) {
         return "LoginSecurity";
      } else if (this == CHANNEL_API) {
         return (String)provider;
      } else {
         return this == PLUGIN ? ((Plugin)provider).getName() : provider.toString();
      }
   }
}
