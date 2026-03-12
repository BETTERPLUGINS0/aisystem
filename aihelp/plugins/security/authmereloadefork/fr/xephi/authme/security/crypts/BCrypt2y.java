package fr.xephi.authme.security.crypts;

import fr.xephi.authme.libs.javax.inject.Inject;

public class BCrypt2y extends BCryptBasedHash {
   @Inject
   public BCrypt2y() {
      this(10);
   }

   public BCrypt2y(int cost) {
      super(new BCryptHasher(fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2Y, cost));
   }
}
