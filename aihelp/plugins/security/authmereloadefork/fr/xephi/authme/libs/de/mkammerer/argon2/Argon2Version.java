package fr.xephi.authme.libs.de.mkammerer.argon2;

import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Argon2_version;

public enum Argon2Version {
   V10(16),
   V13(19),
   DEFAULT_VERSION(V13.version);

   private final int version;
   private final Argon2_version jnaType;

   private Argon2Version(int version) {
      this.version = version;
      this.jnaType = new Argon2_version(version);
   }

   public Argon2_version getJnaType() {
      return this.jnaType;
   }

   public int getVersion() {
      return this.version;
   }
}
