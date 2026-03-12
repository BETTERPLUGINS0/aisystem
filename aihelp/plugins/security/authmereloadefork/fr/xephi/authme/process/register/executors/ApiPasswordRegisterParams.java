package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class ApiPasswordRegisterParams extends PasswordRegisterParams {
   private final boolean loginAfterRegister;

   protected ApiPasswordRegisterParams(Player player, String password, boolean loginAfterRegister) {
      super(player, password, (String)null);
      this.loginAfterRegister = loginAfterRegister;
   }

   public static ApiPasswordRegisterParams of(Player player, String password, boolean loginAfterRegister) {
      return new ApiPasswordRegisterParams(player, password, loginAfterRegister);
   }

   public boolean getLoginAfterRegister() {
      return this.loginAfterRegister;
   }
}
