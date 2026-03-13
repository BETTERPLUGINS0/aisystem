package org.apache.commons.io.input;

import java.io.InputStream;
import java.security.MessageDigest;

public class MessageDigestCalculatingInputStream extends ObservableInputStream {
   private final MessageDigest messageDigest;

   public MessageDigestCalculatingInputStream(InputStream var1, MessageDigest var2) {
      super(var1, new MessageDigestCalculatingInputStream.MessageDigestMaintainingObserver(var2));
      this.messageDigest = var2;
   }

   public MessageDigestCalculatingInputStream(InputStream var1, String var2) {
      this(var1, MessageDigest.getInstance(var2));
   }

   public MessageDigestCalculatingInputStream(InputStream var1) {
      this(var1, MessageDigest.getInstance("MD5"));
   }

   public MessageDigest getMessageDigest() {
      return this.messageDigest;
   }

   public static class MessageDigestMaintainingObserver extends ObservableInputStream.Observer {
      private final MessageDigest messageDigest;

      public MessageDigestMaintainingObserver(MessageDigest var1) {
         this.messageDigest = var1;
      }

      public void data(int var1) {
         this.messageDigest.update((byte)var1);
      }

      public void data(byte[] var1, int var2, int var3) {
         this.messageDigest.update(var1, var2, var3);
      }
   }
}
