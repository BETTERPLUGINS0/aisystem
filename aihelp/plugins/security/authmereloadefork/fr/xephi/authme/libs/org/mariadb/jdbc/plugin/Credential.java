package fr.xephi.authme.libs.org.mariadb.jdbc.plugin;

public class Credential {
   private final String password;
   private final String user;

   public Credential(String user, String password) {
      this.user = user;
      this.password = password;
   }

   public String getUser() {
      return this.user;
   }

   public String getPassword() {
      return this.password;
   }
}
