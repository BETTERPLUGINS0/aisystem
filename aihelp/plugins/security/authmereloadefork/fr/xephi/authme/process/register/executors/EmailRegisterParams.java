package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public class EmailRegisterParams extends RegistrationParameters {
   private final String email;
   private String password;

   protected EmailRegisterParams(Player player, String email) {
      super(player);
      this.email = email;
   }

   public static EmailRegisterParams of(Player player, String email) {
      return new EmailRegisterParams(player, email);
   }

   public String getEmail() {
      return this.email;
   }

   void setPassword(String password) {
      this.password = password;
   }

   String getPassword() {
      return this.password;
   }
}
