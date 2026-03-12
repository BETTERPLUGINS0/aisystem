package fr.xephi.authme.libs.de.mkammerer.argon2.jna;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class Size_t extends IntegerType {
   public Size_t() {
      this(0L);
   }

   public Size_t(long value) {
      super(Native.SIZE_T_SIZE, value);
   }
}
