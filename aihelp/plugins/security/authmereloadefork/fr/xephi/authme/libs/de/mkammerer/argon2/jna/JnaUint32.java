package fr.xephi.authme.libs.de.mkammerer.argon2.jna;

import com.sun.jna.IntegerType;

public class JnaUint32 extends IntegerType {
   public JnaUint32() {
      this(0);
   }

   public JnaUint32(int value) {
      super(4, (long)value, true);
   }
}
