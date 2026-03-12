package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class TwoFactorRegisterParams extends AbstractPasswordRegisterParams {
   protected TwoFactorRegisterParams(Player player) {
      super(player);
   }

   public static TwoFactorRegisterParams of(Player player) {
      return new TwoFactorRegisterParams(player);
   }
}
