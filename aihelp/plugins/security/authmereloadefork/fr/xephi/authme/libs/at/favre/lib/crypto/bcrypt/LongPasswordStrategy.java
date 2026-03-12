package fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt;

import fr.xephi.authme.libs.at.favre.lib.bytes.Bytes;
import fr.xephi.authme.libs.at.favre.lib.bytes.BytesTransformer;

public interface LongPasswordStrategy {
   byte[] derive(byte[] var1);

   public static final class PassThroughStrategy implements LongPasswordStrategy {
      public byte[] derive(byte[] rawPassword) {
         return rawPassword;
      }
   }

   public static final class TruncateStrategy extends LongPasswordStrategy.BaseLongPasswordStrategy {
      TruncateStrategy(int maxLength) {
         super(maxLength, null);
      }

      public byte[] innerDerive(byte[] rawPassword) {
         return Bytes.wrap(rawPassword).resize(this.maxLength, BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_ZERO_INDEX).array();
      }
   }

   public static final class Sha512DerivationStrategy extends LongPasswordStrategy.BaseLongPasswordStrategy {
      Sha512DerivationStrategy(int maxLength) {
         super(maxLength, null);
      }

      public byte[] innerDerive(byte[] rawPassword) {
         return Bytes.wrap(rawPassword).hash("SHA-512").array();
      }
   }

   public static final class StrictMaxPasswordLengthStrategy extends LongPasswordStrategy.BaseLongPasswordStrategy {
      StrictMaxPasswordLengthStrategy(int maxLength) {
         super(maxLength, null);
      }

      public byte[] innerDerive(byte[] rawPassword) {
         throw new IllegalArgumentException("password must not be longer than " + this.maxLength + " bytes plus null terminator encoded in utf-8, was " + rawPassword.length);
      }
   }

   public abstract static class BaseLongPasswordStrategy implements LongPasswordStrategy {
      final int maxLength;

      private BaseLongPasswordStrategy(int maxLength) {
         this.maxLength = maxLength;
      }

      abstract byte[] innerDerive(byte[] var1);

      public byte[] derive(byte[] rawPassword) {
         return rawPassword.length >= this.maxLength ? this.innerDerive(rawPassword) : rawPassword;
      }

      // $FF: synthetic method
      BaseLongPasswordStrategy(int x0, Object x1) {
         this(x0);
      }
   }
}
