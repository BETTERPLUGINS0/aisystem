package fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt;

import fr.xephi.authme.libs.at.favre.lib.bytes.Bytes;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public interface BCryptFormatter {
   byte[] createHashMessage(BCrypt.HashData var1);

   public static final class Default implements BCryptFormatter {
      private final Radix64Encoder encoder;
      private final Charset defaultCharset;

      public Default(Radix64Encoder encoder, Charset defaultCharset) {
         this.encoder = encoder;
         this.defaultCharset = defaultCharset;
      }

      public byte[] createHashMessage(BCrypt.HashData hashData) {
         byte[] saltEncoded = this.encoder.encode(hashData.rawSalt);
         byte[] hashEncoded = this.encoder.encode(hashData.rawHash);
         byte[] costFactorBytes = String.format(Locale.US, "%02d", hashData.cost).getBytes(this.defaultCharset);

         byte[] var6;
         try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(hashData.version.versionIdentifier.length + costFactorBytes.length + 3 + saltEncoded.length + hashEncoded.length);
            byteBuffer.put((byte)36);
            byteBuffer.put(hashData.version.versionIdentifier);
            byteBuffer.put((byte)36);
            byteBuffer.put(costFactorBytes);
            byteBuffer.put((byte)36);
            byteBuffer.put(saltEncoded);
            byteBuffer.put(hashEncoded);
            var6 = byteBuffer.array();
         } finally {
            Bytes.wrapNullSafe(saltEncoded).mutable().secureWipe();
            Bytes.wrapNullSafe(hashEncoded).mutable().secureWipe();
            Bytes.wrapNullSafe(costFactorBytes).mutable().secureWipe();
         }

         return var6;
      }
   }
}
