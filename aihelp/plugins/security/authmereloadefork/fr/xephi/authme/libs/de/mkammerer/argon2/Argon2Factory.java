package fr.xephi.authme.libs.de.mkammerer.argon2;

import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Argon2_type;

public final class Argon2Factory {
   private Argon2Factory() {
   }

   public static Argon2 create() {
      return createInternal(Argon2Factory.Argon2Types.ARGON2i, 16, 32);
   }

   public static Argon2Advanced createAdvanced() {
      return createInternal(Argon2Factory.Argon2Types.ARGON2i, 16, 32);
   }

   public static Argon2 create(int defaultSaltLength, int defaultHashLength) {
      return createInternal(Argon2Factory.Argon2Types.ARGON2i, defaultSaltLength, defaultHashLength);
   }

   public static Argon2Advanced createAdvanced(int defaultSaltLength, int defaultHashLength) {
      return createInternal(Argon2Factory.Argon2Types.ARGON2i, defaultSaltLength, defaultHashLength);
   }

   public static Argon2 create(Argon2Factory.Argon2Types type) {
      return createInternal(type, 16, 32);
   }

   public static Argon2Advanced createAdvanced(Argon2Factory.Argon2Types type) {
      return createInternal(type, 16, 32);
   }

   public static Argon2Advanced createAdvanced(Argon2Factory.Argon2Types type, int defaultSaltLength, int defaultHashLength) {
      return createInternal(type, defaultSaltLength, defaultHashLength);
   }

   public static Argon2 create(Argon2Factory.Argon2Types type, int defaultSaltLength, int defaultHashLength) {
      return createInternal(type, defaultSaltLength, defaultHashLength);
   }

   private static Argon2Advanced createInternal(Argon2Factory.Argon2Types type, int defaultSaltLength, int defaultHashLength) {
      switch(type) {
      case ARGON2i:
         return new Argon2i(defaultSaltLength, defaultHashLength);
      case ARGON2d:
         return new Argon2d(defaultSaltLength, defaultHashLength);
      case ARGON2id:
         return new Argon2id(defaultSaltLength, defaultHashLength);
      default:
         throw new IllegalArgumentException("Invalid argon2 type");
      }
   }

   public static enum Argon2Types {
      ARGON2i(1),
      ARGON2d(0),
      ARGON2id(2);

      private final Argon2_type jnaType;

      private Argon2Types(int idx) {
         this.jnaType = new Argon2_type((long)idx);
      }

      public Argon2_type getJnaType() {
         return this.jnaType;
      }
   }
}
