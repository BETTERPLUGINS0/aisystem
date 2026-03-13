package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class WriterOutputStream extends OutputStream {
   private static final int BUFFER_SIZE = 1024;
   private final Writer writer;
   private final CharsetDecoder decoder;
   private final boolean writeImmediately;
   private final ByteBuffer decoderIn;
   private final CharBuffer decoderOut;

   public WriterOutputStream(Writer var1, CharsetDecoder var2) {
      this(var1, (CharsetDecoder)var2, 1024, false);
   }

   public WriterOutputStream(Writer var1, CharsetDecoder var2, int var3, boolean var4) {
      this.decoderIn = ByteBuffer.allocate(128);
      checkIbmJdkWithBrokenUTF16(var2.charset());
      this.writer = var1;
      this.decoder = var2;
      this.writeImmediately = var4;
      this.decoderOut = CharBuffer.allocate(var3);
   }

   public WriterOutputStream(Writer var1, Charset var2, int var3, boolean var4) {
      this(var1, var2.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), var3, var4);
   }

   public WriterOutputStream(Writer var1, Charset var2) {
      this(var1, (Charset)var2, 1024, false);
   }

   public WriterOutputStream(Writer var1, String var2, int var3, boolean var4) {
      this(var1, Charset.forName(var2), var3, var4);
   }

   public WriterOutputStream(Writer var1, String var2) {
      this(var1, (String)var2, 1024, false);
   }

   /** @deprecated */
   @Deprecated
   public WriterOutputStream(Writer var1) {
      this(var1, (Charset)Charset.defaultCharset(), 1024, false);
   }

   public void write(byte[] var1, int var2, int var3) {
      while(var3 > 0) {
         int var4 = Math.min(var3, this.decoderIn.remaining());
         this.decoderIn.put(var1, var2, var4);
         this.processInput(false);
         var3 -= var4;
         var2 += var4;
      }

      if (this.writeImmediately) {
         this.flushOutput();
      }

   }

   public void write(byte[] var1) {
      this.write(var1, 0, var1.length);
   }

   public void write(int var1) {
      this.write(new byte[]{(byte)var1}, 0, 1);
   }

   public void flush() {
      this.flushOutput();
      this.writer.flush();
   }

   public void close() {
      this.processInput(true);
      this.flushOutput();
      this.writer.close();
   }

   private void processInput(boolean var1) {
      this.decoderIn.flip();

      while(true) {
         CoderResult var2 = this.decoder.decode(this.decoderIn, this.decoderOut, var1);
         if (!var2.isOverflow()) {
            if (var2.isUnderflow()) {
               this.decoderIn.compact();
               return;
            } else {
               throw new IOException("Unexpected coder result");
            }
         }

         this.flushOutput();
      }
   }

   private void flushOutput() {
      if (this.decoderOut.position() > 0) {
         this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
         this.decoderOut.rewind();
      }

   }

   private static void checkIbmJdkWithBrokenUTF16(Charset var0) {
      if ("UTF-16".equals(var0.name())) {
         String var1 = "vés";
         byte[] var2 = "vés".getBytes(var0);
         CharsetDecoder var3 = var0.newDecoder();
         ByteBuffer var4 = ByteBuffer.allocate(16);
         CharBuffer var5 = CharBuffer.allocate("vés".length());
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            var4.put(var2[var7]);
            var4.flip();

            try {
               var3.decode(var4, var5, var7 == var6 - 1);
            } catch (IllegalArgumentException var9) {
               throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream");
            }

            var4.compact();
         }

         var5.rewind();
         if (!"vés".equals(var5.toString())) {
            throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream");
         }
      }
   }
}
