package fr.xephi.authme.libs.de.mkammerer.argon2.jna;

import fr.xephi.authme.libs.de.mkammerer.argon2.Argon2Version;

public class Argon2_version extends JnaUint32 {
   public Argon2_version() {
      this(Argon2Version.DEFAULT_VERSION.getVersion());
   }

   public Argon2_version(int version) {
      super(version);
   }
}
