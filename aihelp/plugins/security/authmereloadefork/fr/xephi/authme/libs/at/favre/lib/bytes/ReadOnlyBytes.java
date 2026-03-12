package fr.xephi.authme.libs.at.favre.lib.bytes;

import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

public final class ReadOnlyBytes extends Bytes {
   ReadOnlyBytes(byte[] byteArray, ByteOrder byteOrder) {
      super(byteArray, byteOrder, new ReadOnlyBytes.Factory());
   }

   public boolean isReadOnly() {
      return true;
   }

   public byte[] array() {
      throw new ReadOnlyBufferException();
   }

   private static class Factory implements BytesFactory {
      private Factory() {
      }

      public Bytes wrap(byte[] array, ByteOrder byteOrder) {
         return new ReadOnlyBytes(array, byteOrder);
      }

      // $FF: synthetic method
      Factory(Object x0) {
         this();
      }
   }
}
