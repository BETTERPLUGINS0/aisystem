package fr.xephi.authme.libs.de.mkammerer.argon2.jna;

import com.sun.jna.NativeLong;

public class Argon2_type extends NativeLong {
   public Argon2_type() {
      this(0L);
   }

   public Argon2_type(long value) {
      super(value);
   }
}
