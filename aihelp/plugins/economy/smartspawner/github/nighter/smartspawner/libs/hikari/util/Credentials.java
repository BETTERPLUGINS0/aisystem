package github.nighter.smartspawner.libs.hikari.util;

import javax.management.ConstructorParameters;

public final class Credentials {
   private final String username;
   private final String password;

   public static Credentials of(String username, String password) {
      return new Credentials(username, password);
   }

   @ConstructorParameters({"username", "password"})
   public Credentials(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }
}
