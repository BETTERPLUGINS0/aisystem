package fr.xephi.authme.libs.org.postgresql.fastpath;

import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

/** @deprecated */
@Deprecated
public class FastpathArg {
   @Nullable
   private final byte[] bytes;
   private final int bytesStart;
   private final int bytesLength;

   public FastpathArg(int value) {
      this.bytes = new byte[4];
      this.bytes[3] = (byte)value;
      this.bytes[2] = (byte)(value >> 8);
      this.bytes[1] = (byte)(value >> 16);
      this.bytes[0] = (byte)(value >> 24);
      this.bytesStart = 0;
      this.bytesLength = 4;
   }

   public FastpathArg(long value) {
      this.bytes = new byte[8];
      this.bytes[7] = (byte)((int)value);
      this.bytes[6] = (byte)((int)(value >> 8));
      this.bytes[5] = (byte)((int)(value >> 16));
      this.bytes[4] = (byte)((int)(value >> 24));
      this.bytes[3] = (byte)((int)(value >> 32));
      this.bytes[2] = (byte)((int)(value >> 40));
      this.bytes[1] = (byte)((int)(value >> 48));
      this.bytes[0] = (byte)((int)(value >> 56));
      this.bytesStart = 0;
      this.bytesLength = 8;
   }

   public FastpathArg(byte[] bytes) {
      this(bytes, 0, bytes.length);
   }

   public FastpathArg(@Nullable byte[] buf, int off, int len) {
      this.bytes = buf;
      this.bytesStart = off;
      this.bytesLength = len;
   }

   public FastpathArg(String s) {
      this(s.getBytes());
   }

   public static FastpathArg of(ByteStreamWriter writer) {
      return new FastpathArg.ByteStreamWriterFastpathArg(writer);
   }

   void populateParameter(ParameterList params, int index) throws SQLException {
      if (this.bytes == null) {
         params.setNull(index, 0);
      } else {
         params.setBytea(index, this.bytes, this.bytesStart, this.bytesLength);
      }

   }

   static class ByteStreamWriterFastpathArg extends FastpathArg {
      private final ByteStreamWriter writer;

      ByteStreamWriterFastpathArg(ByteStreamWriter writer) {
         super((byte[])null, 0, 0);
         this.writer = writer;
      }

      void populateParameter(ParameterList params, int index) throws SQLException {
         params.setBytea(index, this.writer);
      }
   }
}
