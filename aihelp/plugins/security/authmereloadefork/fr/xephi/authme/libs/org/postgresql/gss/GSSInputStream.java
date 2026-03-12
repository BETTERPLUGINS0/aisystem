package fr.xephi.authme.libs.org.postgresql.gss;

import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.io.InputStream;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

public class GSSInputStream extends InputStream {
   private final GSSContext gssContext;
   private final MessageProp messageProp;
   private final InputStream wrapped;
   @Nullable
   byte[] unencrypted;
   int unencryptedPos;
   int unencryptedLength;

   public GSSInputStream(InputStream wrapped, GSSContext gssContext, MessageProp messageProp) {
      this.wrapped = wrapped;
      this.gssContext = gssContext;
      this.messageProp = messageProp;
   }

   public int read() throws IOException {
      return 0;
   }

   public int read(byte[] buffer, int pos, int len) throws IOException {
      byte[] int4Buf = new byte[4];
      int copyLength = 0;
      if (this.unencryptedLength > 0) {
         copyLength = Math.min(len, this.unencryptedLength);
         System.arraycopy(Nullness.castNonNull(this.unencrypted), this.unencryptedPos, buffer, pos, copyLength);
         this.unencryptedLength -= copyLength;
         this.unencryptedPos += copyLength;
      } else if (this.wrapped.read(int4Buf, 0, 4) == 4) {
         int encryptedLength = (int4Buf[0] & 255) << 24 | (int4Buf[1] & 255) << 16 | (int4Buf[2] & 255) << 8 | int4Buf[3] & 255;
         byte[] encryptedBuffer = new byte[encryptedLength];
         this.wrapped.read(encryptedBuffer, 0, encryptedLength);

         try {
            byte[] unencrypted = this.gssContext.unwrap(encryptedBuffer, 0, encryptedLength, this.messageProp);
            this.unencrypted = unencrypted;
            this.unencryptedLength = unencrypted.length;
            this.unencryptedPos = 0;
            copyLength = Math.min(len, unencrypted.length);
            System.arraycopy(unencrypted, this.unencryptedPos, buffer, pos, copyLength);
            this.unencryptedLength -= copyLength;
            this.unencryptedPos += copyLength;
            return copyLength;
         } catch (GSSException var9) {
            throw new IOException(var9);
         }
      }

      return copyLength;
   }
}
