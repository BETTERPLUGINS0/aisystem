package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.StandardLineSeparator;

public class ReversedLinesFileReader implements Closeable {
   private static final String EMPTY_STRING = "";
   private static final int DEFAULT_BLOCK_SIZE = 8192;
   private final int blockSize;
   private final Charset charset;
   private final SeekableByteChannel channel;
   private final long totalByteLength;
   private final long totalBlockCount;
   private final byte[][] newLineSequences;
   private final int avoidNewlineSplitBufferSize;
   private final int byteDecrement;
   private ReversedLinesFileReader.FilePart currentFilePart;
   private boolean trailingNewlineOfFileSkipped;

   /** @deprecated */
   @Deprecated
   public ReversedLinesFileReader(File var1) {
      this((File)var1, 8192, (Charset)Charset.defaultCharset());
   }

   public ReversedLinesFileReader(File var1, Charset var2) {
      this(var1.toPath(), var2);
   }

   public ReversedLinesFileReader(File var1, int var2, Charset var3) {
      this(var1.toPath(), var2, var3);
   }

   public ReversedLinesFileReader(File var1, int var2, String var3) {
      this(var1.toPath(), var2, var3);
   }

   public ReversedLinesFileReader(Path var1, Charset var2) {
      this((Path)var1, 8192, (Charset)var2);
   }

   public ReversedLinesFileReader(Path var1, int var2, Charset var3) {
      this.blockSize = var2;
      this.charset = Charsets.toCharset(var3);
      CharsetEncoder var4 = this.charset.newEncoder();
      float var5 = var4.maxBytesPerChar();
      if (var5 == 1.0F) {
         this.byteDecrement = 1;
      } else if (this.charset == StandardCharsets.UTF_8) {
         this.byteDecrement = 1;
      } else if (this.charset != Charset.forName("Shift_JIS") && this.charset != Charset.forName("windows-31j") && this.charset != Charset.forName("x-windows-949") && this.charset != Charset.forName("gbk") && this.charset != Charset.forName("x-windows-950")) {
         if (this.charset != StandardCharsets.UTF_16BE && this.charset != StandardCharsets.UTF_16LE) {
            if (this.charset == StandardCharsets.UTF_16) {
               throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
            }

            throw new UnsupportedEncodingException("Encoding " + var3 + " is not supported yet (feel free to submit a patch)");
         }

         this.byteDecrement = 2;
      } else {
         this.byteDecrement = 1;
      }

      this.newLineSequences = new byte[][]{StandardLineSeparator.CRLF.getBytes(this.charset), StandardLineSeparator.LF.getBytes(this.charset), StandardLineSeparator.CR.getBytes(this.charset)};
      this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
      this.channel = Files.newByteChannel(var1, StandardOpenOption.READ);
      this.totalByteLength = this.channel.size();
      int var6 = (int)(this.totalByteLength % (long)var2);
      if (var6 > 0) {
         this.totalBlockCount = this.totalByteLength / (long)var2 + 1L;
      } else {
         this.totalBlockCount = this.totalByteLength / (long)var2;
         if (this.totalByteLength > 0L) {
            var6 = var2;
         }
      }

      this.currentFilePart = new ReversedLinesFileReader.FilePart(this.totalBlockCount, var6, (byte[])null);
   }

   public ReversedLinesFileReader(Path var1, int var2, String var3) {
      this(var1, var2, Charsets.toCharset(var3));
   }

   public void close() {
      this.channel.close();
   }

   public String readLine() {
      String var1;
      for(var1 = this.currentFilePart.readLine(); var1 == null; var1 = this.currentFilePart.readLine()) {
         this.currentFilePart = this.currentFilePart.rollOver();
         if (this.currentFilePart == null) {
            break;
         }
      }

      if ("".equals(var1) && !this.trailingNewlineOfFileSkipped) {
         this.trailingNewlineOfFileSkipped = true;
         var1 = this.readLine();
      }

      return var1;
   }

   public List<String> readLines(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("lineCount < 0");
      } else {
         ArrayList var2 = new ArrayList(var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            String var4 = this.readLine();
            if (var4 == null) {
               return var2;
            }

            var2.add(var4);
         }

         return var2;
      }
   }

   public String toString(int var1) {
      List var2 = this.readLines(var1);
      Collections.reverse(var2);
      return var2.isEmpty() ? "" : String.join(System.lineSeparator(), var2) + System.lineSeparator();
   }

   private class FilePart {
      private final long no;
      private final byte[] data;
      private byte[] leftOver;
      private int currentLastBytePos;

      private FilePart(final long param2, final int param4, final byte[] param5) {
         this.no = var2;
         int var6 = var4 + (var5 != null ? var5.length : 0);
         this.data = new byte[var6];
         long var7 = (var2 - 1L) * (long)ReversedLinesFileReader.this.blockSize;
         if (var2 > 0L) {
            ReversedLinesFileReader.this.channel.position(var7);
            int var9 = ReversedLinesFileReader.this.channel.read(ByteBuffer.wrap(this.data, 0, var4));
            if (var9 != var4) {
               throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
            }
         }

         if (var5 != null) {
            System.arraycopy(var5, 0, this.data, var4, var5.length);
         }

         this.currentLastBytePos = this.data.length - 1;
         this.leftOver = null;
      }

      private void createLeftOver() {
         int var1 = this.currentLastBytePos + 1;
         if (var1 > 0) {
            this.leftOver = IOUtils.byteArray(var1);
            System.arraycopy(this.data, 0, this.leftOver, 0, var1);
         } else {
            this.leftOver = null;
         }

         this.currentLastBytePos = -1;
      }

      private int getNewLineMatchByteCount(byte[] var1, int var2) {
         byte[][] var3 = ReversedLinesFileReader.this.newLineSequences;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte[] var6 = var3[var5];
            boolean var7 = true;

            for(int var8 = var6.length - 1; var8 >= 0; --var8) {
               int var9 = var2 + var8 - (var6.length - 1);
               var7 &= var9 >= 0 && var1[var9] == var6[var8];
            }

            if (var7) {
               return var6.length;
            }
         }

         return 0;
      }

      private String readLine() {
         String var1 = null;
         boolean var3 = this.no == 1L;
         int var4 = this.currentLastBytePos;

         while(var4 > -1) {
            if (!var3 && var4 < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
               this.createLeftOver();
               break;
            }

            int var2;
            if ((var2 = this.getNewLineMatchByteCount(this.data, var4)) > 0) {
               int var5 = var4 + 1;
               int var6 = this.currentLastBytePos - var5 + 1;
               if (var6 < 0) {
                  throw new IllegalStateException("Unexpected negative line length=" + var6);
               }

               byte[] var7 = IOUtils.byteArray(var6);
               System.arraycopy(this.data, var5, var7, 0, var6);
               var1 = new String(var7, ReversedLinesFileReader.this.charset);
               this.currentLastBytePos = var4 - var2;
               break;
            }

            var4 -= ReversedLinesFileReader.this.byteDecrement;
            if (var4 < 0) {
               this.createLeftOver();
               break;
            }
         }

         if (var3 && this.leftOver != null) {
            var1 = new String(this.leftOver, ReversedLinesFileReader.this.charset);
            this.leftOver = null;
         }

         return var1;
      }

      private ReversedLinesFileReader.FilePart rollOver() {
         if (this.currentLastBytePos > -1) {
            throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
         } else if (this.no > 1L) {
            return ReversedLinesFileReader.this.new FilePart(this.no - 1L, ReversedLinesFileReader.this.blockSize, this.leftOver);
         } else if (this.leftOver != null) {
            throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.charset));
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      FilePart(long var2, int var4, byte[] var5, Object var6) {
         this(var2, var4, var5);
      }
   }
}
