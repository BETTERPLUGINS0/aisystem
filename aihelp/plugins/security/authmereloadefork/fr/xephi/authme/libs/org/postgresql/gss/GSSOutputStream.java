package fr.xephi.authme.libs.org.postgresql.gss;

import java.io.IOException;
import java.io.OutputStream;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

public class GSSOutputStream extends OutputStream {
   private final GSSContext gssContext;
   private final MessageProp messageProp;
   private final byte[] buffer;
   private final byte[] int4Buf = new byte[4];
   private int index;
   private final OutputStream wrapped;

   public GSSOutputStream(OutputStream out, GSSContext gssContext, MessageProp messageProp, int bufferSize) {
      this.wrapped = out;
      this.gssContext = gssContext;
      this.messageProp = messageProp;
      this.buffer = new byte[bufferSize];
   }

   public void write(int b) throws IOException {
      this.buffer[this.index++] = (byte)b;
      if (this.index >= this.buffer.length) {
         this.flush();
      }

   }

   public void write(byte[] buf) throws IOException {
      this.write(buf, 0, buf.length);
   }

   public void write(byte[] b, int pos, int len) throws IOException {
      while(len > 0) {
         int roomToWrite = this.buffer.length - this.index;
         if (len < roomToWrite) {
            System.arraycopy(b, pos, this.buffer, this.index, len);
            this.index += len;
            len -= roomToWrite;
         } else {
            System.arraycopy(b, pos, this.buffer, this.index, roomToWrite);
            this.index += roomToWrite;
            len -= roomToWrite;
         }

         if (roomToWrite == 0) {
            this.flush();
         }
      }

   }

   public void flush() throws IOException {
      try {
         byte[] token = this.gssContext.wrap(this.buffer, 0, this.index, this.messageProp);
         this.sendInteger4Raw(token.length);
         this.wrapped.write(token, 0, token.length);
         this.index = 0;
      } catch (GSSException var2) {
         throw new IOException(var2);
      }

      this.wrapped.flush();
   }

   private void sendInteger4Raw(int val) throws IOException {
      this.int4Buf[0] = (byte)(val >>> 24);
      this.int4Buf[1] = (byte)(val >>> 16);
      this.int4Buf[2] = (byte)(val >>> 8);
      this.int4Buf[3] = (byte)val;
      this.wrapped.write(this.int4Buf);
   }
}
