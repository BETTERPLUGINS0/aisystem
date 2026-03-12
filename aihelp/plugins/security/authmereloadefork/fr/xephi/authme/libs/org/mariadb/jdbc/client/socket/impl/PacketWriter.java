package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.MaxAllowedPacketException;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.LoggerHelper;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketWriter implements Writer {
   public static final int SMALL_BUFFER_SIZE = 8192;
   private static final Logger logger = Loggers.getLogger(PacketWriter.class);
   private static final byte QUOTE = 39;
   private static final byte DBL_QUOTE = 34;
   private static final byte ZERO_BYTE = 0;
   private static final byte BACKSLASH = 92;
   private static final int MEDIUM_BUFFER_SIZE = 131072;
   private static final int LARGE_BUFFER_SIZE = 1048576;
   private static final int MAX_PACKET_LENGTH = 16777219;
   protected final MutableByte sequence;
   protected final MutableByte compressSequence;
   private final int maxQuerySizeToLog;
   private final OutputStream out;
   private final int maxPacketLength = 16777219;
   private final Integer maxAllowedPacket;
   protected byte[] buf;
   protected int pos = 4;
   private long cmdLength;
   private boolean permitTrace = true;
   private String serverThreadLog = "";
   private int mark = -1;
   private boolean bufContainDataAfterMark = false;

   public PacketWriter(OutputStream out, int maxQuerySizeToLog, Integer maxAllowedPacket, MutableByte sequence, MutableByte compressSequence) {
      this.out = out;
      this.buf = new byte[8192];
      this.maxQuerySizeToLog = maxQuerySizeToLog;
      this.cmdLength = 0L;
      this.sequence = sequence;
      this.compressSequence = compressSequence;
      this.maxAllowedPacket = maxAllowedPacket;
   }

   public int pos() {
      return this.pos;
   }

   public void pos(int pos) throws IOException {
      if (pos > this.buf.length) {
         this.growBuffer(pos);
      }

      this.pos = pos;
   }

   public long getCmdLength() {
      return this.cmdLength;
   }

   public void writeByte(int value) throws IOException {
      if (this.pos >= this.buf.length) {
         if (this.pos >= 16777219 && !this.bufContainDataAfterMark) {
            this.writeSocket(false);
         } else {
            this.growBuffer(1);
         }
      }

      this.buf[this.pos++] = (byte)value;
   }

   public void writeShort(short value) throws IOException {
      if (2 > this.buf.length - this.pos) {
         this.writeBytes(new byte[]{(byte)value, (byte)(value >> 8)}, 0, 2);
      } else {
         this.buf[this.pos] = (byte)value;
         this.buf[this.pos + 1] = (byte)(value >> 8);
         this.pos += 2;
      }
   }

   public void writeInt(int value) throws IOException {
      if (4 > this.buf.length - this.pos) {
         byte[] arr = new byte[]{(byte)value, (byte)(value >> 8), (byte)(value >> 16), (byte)(value >> 24)};
         this.writeBytes(arr, 0, 4);
      } else {
         this.buf[this.pos] = (byte)value;
         this.buf[this.pos + 1] = (byte)(value >> 8);
         this.buf[this.pos + 2] = (byte)(value >> 16);
         this.buf[this.pos + 3] = (byte)(value >> 24);
         this.pos += 4;
      }
   }

   public void writeLong(long value) throws IOException {
      if (8 > this.buf.length - this.pos) {
         byte[] arr = new byte[]{(byte)((int)value), (byte)((int)(value >> 8)), (byte)((int)(value >> 16)), (byte)((int)(value >> 24)), (byte)((int)(value >> 32)), (byte)((int)(value >> 40)), (byte)((int)(value >> 48)), (byte)((int)(value >> 56))};
         this.writeBytes(arr, 0, 8);
      } else {
         this.buf[this.pos] = (byte)((int)value);
         this.buf[this.pos + 1] = (byte)((int)(value >> 8));
         this.buf[this.pos + 2] = (byte)((int)(value >> 16));
         this.buf[this.pos + 3] = (byte)((int)(value >> 24));
         this.buf[this.pos + 4] = (byte)((int)(value >> 32));
         this.buf[this.pos + 5] = (byte)((int)(value >> 40));
         this.buf[this.pos + 6] = (byte)((int)(value >> 48));
         this.buf[this.pos + 7] = (byte)((int)(value >> 56));
         this.pos += 8;
      }
   }

   public void writeDouble(double value) throws IOException {
      this.writeLong(Double.doubleToLongBits(value));
   }

   public void writeFloat(float value) throws IOException {
      this.writeInt(Float.floatToIntBits(value));
   }

   public void writeBytes(byte[] arr) throws IOException {
      this.writeBytes(arr, 0, arr.length);
   }

   public void writeBytesAtPos(byte[] arr, int pos) {
      System.arraycopy(arr, 0, this.buf, pos, arr.length);
   }

   public void writeBytes(byte[] arr, int off, int len) throws IOException {
      if (len > this.buf.length - this.pos) {
         if (this.buf.length != 16777219) {
            this.growBuffer(len);
         }

         if (len > this.buf.length - this.pos) {
            if (this.mark != -1) {
               this.growBuffer(len);
               if (this.mark != -1) {
                  this.flushBufferStopAtMark();
               }
            }

            if (len > this.buf.length - this.pos) {
               int remainingLen = len;

               while(true) {
                  int lenToFillbuf = Math.min(16777219 - this.pos, remainingLen);
                  System.arraycopy(arr, off, this.buf, this.pos, lenToFillbuf);
                  remainingLen -= lenToFillbuf;
                  off += lenToFillbuf;
                  this.pos += lenToFillbuf;
                  if (remainingLen <= 0) {
                     return;
                  }

                  this.writeSocket(false);
               }
            }
         }
      }

      System.arraycopy(arr, off, this.buf, this.pos, len);
      this.pos += len;
   }

   public void writeLength(long length) throws IOException {
      if (length < 251L) {
         this.writeByte((byte)((int)length));
      } else {
         byte[] arr;
         if (length < 65536L) {
            if (3 > this.buf.length - this.pos) {
               arr = new byte[]{-4, (byte)((int)length), (byte)((int)(length >>> 8))};
               this.writeBytes(arr, 0, 3);
            } else {
               this.buf[this.pos] = -4;
               this.buf[this.pos + 1] = (byte)((int)length);
               this.buf[this.pos + 2] = (byte)((int)(length >>> 8));
               this.pos += 3;
            }
         } else if (length < 16777216L) {
            if (4 > this.buf.length - this.pos) {
               arr = new byte[]{-3, (byte)((int)length), (byte)((int)(length >>> 8)), (byte)((int)(length >>> 16))};
               this.writeBytes(arr, 0, 4);
            } else {
               this.buf[this.pos] = -3;
               this.buf[this.pos + 1] = (byte)((int)length);
               this.buf[this.pos + 2] = (byte)((int)(length >>> 8));
               this.buf[this.pos + 3] = (byte)((int)(length >>> 16));
               this.pos += 4;
            }
         } else if (9 > this.buf.length - this.pos) {
            arr = new byte[]{-2, (byte)((int)length), (byte)((int)(length >>> 8)), (byte)((int)(length >>> 16)), (byte)((int)(length >>> 24)), (byte)((int)(length >>> 32)), (byte)((int)(length >>> 40)), (byte)((int)(length >>> 48)), (byte)((int)(length >>> 56))};
            this.writeBytes(arr, 0, 9);
         } else {
            this.buf[this.pos] = -2;
            this.buf[this.pos + 1] = (byte)((int)length);
            this.buf[this.pos + 2] = (byte)((int)(length >>> 8));
            this.buf[this.pos + 3] = (byte)((int)(length >>> 16));
            this.buf[this.pos + 4] = (byte)((int)(length >>> 24));
            this.buf[this.pos + 5] = (byte)((int)(length >>> 32));
            this.buf[this.pos + 6] = (byte)((int)(length >>> 40));
            this.buf[this.pos + 7] = (byte)((int)(length >>> 48));
            this.buf[this.pos + 8] = (byte)((int)(length >>> 56));
            this.pos += 9;
         }
      }
   }

   public void writeAscii(String str) throws IOException {
      int len = str.length();
      if (len > this.buf.length - this.pos) {
         byte[] arr = str.getBytes(StandardCharsets.US_ASCII);
         this.writeBytes(arr, 0, arr.length);
      } else {
         for(int off = 0; off < len; this.buf[this.pos++] = (byte)str.charAt(off++)) {
         }

      }
   }

   public void writeString(String str) throws IOException {
      int charsLength = str.length();
      if (charsLength * 3 >= this.buf.length - this.pos) {
         byte[] arr = str.getBytes(StandardCharsets.UTF_8);
         this.writeBytes(arr, 0, arr.length);
      } else {
         int charsOffset;
         char currChar;
         for(charsOffset = 0; charsOffset < charsLength && (currChar = str.charAt(charsOffset)) < 128; ++charsOffset) {
            this.buf[this.pos++] = (byte)currChar;
         }

         while(true) {
            while(charsOffset < charsLength) {
               currChar = str.charAt(charsOffset++);
               if (currChar < 128) {
                  this.buf[this.pos++] = (byte)currChar;
               } else if (currChar < 2048) {
                  this.buf[this.pos++] = (byte)(192 | currChar >> 6);
                  this.buf[this.pos++] = (byte)(128 | currChar & 63);
               } else if (currChar >= '\ud800' && currChar < '\ue000') {
                  if (currChar < '\udc00') {
                     if (charsOffset + 1 > charsLength) {
                        this.buf[this.pos++] = 99;
                     } else {
                        char nextChar = str.charAt(charsOffset);
                        if (nextChar >= '\udc00' && nextChar < '\ue000') {
                           int surrogatePairs = (currChar << 10) + nextChar + -56613888;
                           this.buf[this.pos++] = (byte)(240 | surrogatePairs >> 18);
                           this.buf[this.pos++] = (byte)(128 | surrogatePairs >> 12 & 63);
                           this.buf[this.pos++] = (byte)(128 | surrogatePairs >> 6 & 63);
                           this.buf[this.pos++] = (byte)(128 | surrogatePairs & 63);
                           ++charsOffset;
                        } else {
                           this.buf[this.pos++] = 63;
                        }
                     }
                  } else {
                     this.buf[this.pos++] = 63;
                  }
               } else {
                  this.buf[this.pos++] = (byte)(224 | currChar >> 12);
                  this.buf[this.pos++] = (byte)(128 | currChar >> 6 & 63);
                  this.buf[this.pos++] = (byte)(128 | currChar & 63);
               }
            }

            return;
         }
      }
   }

   public byte[] buf() {
      return this.buf;
   }

   public void writeStringEscaped(String str, boolean noBackslashEscapes) throws IOException {
      int charsLength = str.length();
      if (charsLength * 3 >= this.buf.length - this.pos) {
         byte[] arr = str.getBytes(StandardCharsets.UTF_8);
         this.writeBytesEscaped(arr, arr.length, noBackslashEscapes);
      } else {
         int charsOffset = 0;
         char currChar;
         if (noBackslashEscapes) {
            while(charsOffset < charsLength && (currChar = str.charAt(charsOffset)) < 128) {
               if (currChar == '\'') {
                  this.buf[this.pos++] = 39;
               }

               this.buf[this.pos++] = (byte)currChar;
               ++charsOffset;
            }
         } else {
            while(charsOffset < charsLength && (currChar = str.charAt(charsOffset)) < 128) {
               if (currChar == '\\' || currChar == '\'' || currChar == 0 || currChar == '"') {
                  this.buf[this.pos++] = 92;
               }

               this.buf[this.pos++] = (byte)currChar;
               ++charsOffset;
            }
         }

         while(true) {
            while(charsOffset < charsLength) {
               currChar = str.charAt(charsOffset++);
               if (currChar < 128) {
                  if (noBackslashEscapes) {
                     if (currChar == '\'') {
                        this.buf[this.pos++] = 39;
                     }
                  } else if (currChar == '\\' || currChar == '\'' || currChar == 0 || currChar == '"') {
                     this.buf[this.pos++] = 92;
                  }

                  this.buf[this.pos++] = (byte)currChar;
               } else if (currChar < 2048) {
                  this.buf[this.pos++] = (byte)(192 | currChar >> 6);
                  this.buf[this.pos++] = (byte)(128 | currChar & 63);
               } else if (currChar >= '\ud800' && currChar < '\ue000') {
                  if (currChar < '\udc00') {
                     if (charsOffset + 1 > charsLength) {
                        this.buf[this.pos++] = 99;
                     } else {
                        char nextChar = str.charAt(charsOffset);
                        if (nextChar >= '\udc00' && nextChar < '\ue000') {
                           int surrogatePairs = (currChar << 10) + nextChar + -56613888;
                           this.buf[this.pos++] = (byte)(240 | surrogatePairs >> 18);
                           this.buf[this.pos++] = (byte)(128 | surrogatePairs >> 12 & 63);
                           this.buf[this.pos++] = (byte)(128 | surrogatePairs >> 6 & 63);
                           this.buf[this.pos++] = (byte)(128 | surrogatePairs & 63);
                           ++charsOffset;
                        } else {
                           this.buf[this.pos++] = 63;
                        }
                     }
                  } else {
                     this.buf[this.pos++] = 63;
                  }
               } else {
                  this.buf[this.pos++] = (byte)(224 | currChar >> 12);
                  this.buf[this.pos++] = (byte)(128 | currChar >> 6 & 63);
                  this.buf[this.pos++] = (byte)(128 | currChar & 63);
               }
            }

            return;
         }
      }
   }

   public void writeBytesEscaped(byte[] bytes, int len, boolean noBackslashEscapes) throws IOException {
      int i;
      if (len * 2 > this.buf.length - this.pos) {
         if (this.buf.length != 16777219) {
            this.growBuffer(len * 2);
         }

         if (len * 2 > this.buf.length - this.pos) {
            if (this.mark == -1) {
               if (this.buf.length <= this.pos) {
                  this.writeSocket(false);
               }

               if (noBackslashEscapes) {
                  for(i = 0; i < len; ++i) {
                     if (39 == bytes[i]) {
                        this.buf[this.pos++] = 39;
                        if (this.buf.length <= this.pos) {
                           this.writeSocket(false);
                        }
                     }

                     this.buf[this.pos++] = bytes[i];
                     if (this.buf.length <= this.pos) {
                        this.writeSocket(false);
                     }
                  }
               } else {
                  for(i = 0; i < len; ++i) {
                     if (bytes[i] == 39 || bytes[i] == 92 || bytes[i] == 34 || bytes[i] == 0) {
                        this.buf[this.pos++] = 92;
                        if (this.buf.length <= this.pos) {
                           this.writeSocket(false);
                        }
                     }

                     this.buf[this.pos++] = bytes[i];
                     if (this.buf.length <= this.pos) {
                        this.writeSocket(false);
                     }
                  }
               }

               return;
            }

            this.growBuffer(len * 2);
            if (this.mark != -1) {
               this.flushBufferStopAtMark();
            }
         }
      }

      if (noBackslashEscapes) {
         for(i = 0; i < len; ++i) {
            if (39 == bytes[i]) {
               this.buf[this.pos++] = 39;
            }

            this.buf[this.pos++] = bytes[i];
         }
      } else {
         for(i = 0; i < len; ++i) {
            if (bytes[i] == 39 || bytes[i] == 92 || bytes[i] == 34 || bytes[i] == 0) {
               this.buf[this.pos++] = 92;
            }

            this.buf[this.pos++] = bytes[i];
         }
      }

   }

   private void growBuffer(int len) throws IOException {
      int bufLength = this.buf.length;
      int newCapacity;
      if (bufLength == 8192) {
         if (len + this.pos <= 131072) {
            newCapacity = 131072;
         } else if (len + this.pos <= 1048576) {
            newCapacity = 1048576;
         } else {
            newCapacity = 16777219;
         }
      } else if (bufLength == 131072) {
         if (len + this.pos < 1048576) {
            newCapacity = 1048576;
         } else {
            newCapacity = 16777219;
         }
      } else if (this.bufContainDataAfterMark) {
         newCapacity = Math.max(len + this.pos, 16777219);
      } else {
         newCapacity = 16777219;
      }

      if (len + this.pos > newCapacity && this.mark != -1) {
         this.flushBufferStopAtMark();
         if (len + this.pos <= bufLength) {
            return;
         }

         if (bufLength == 16777219) {
            return;
         }

         if (len + this.pos > newCapacity) {
            newCapacity = Math.min(16777219, len + this.pos);
         }
      }

      byte[] newBuf = new byte[newCapacity];
      System.arraycopy(this.buf, 0, newBuf, 0, this.pos);
      this.buf = newBuf;
   }

   public void writeEmptyPacket() throws IOException {
      this.buf[0] = 0;
      this.buf[1] = 0;
      this.buf[2] = 0;
      this.buf[3] = this.sequence.incrementAndGet();
      this.out.write(this.buf, 0, 4);
      if (logger.isTraceEnabled()) {
         logger.trace("send com : content length=0 {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.buf, 0, 4));
      }

      this.out.flush();
      this.cmdLength = 0L;
   }

   public void flush() throws IOException {
      this.writeSocket(true);
      if (this.buf.length > 8192 && this.cmdLength * 2L < (long)this.buf.length) {
         this.buf = new byte[8192];
      }

      this.pos = 4;
      this.cmdLength = 0L;
      this.mark = -1;
   }

   public void flushPipeline() throws IOException {
      this.writeSocket(false);
      if (this.buf.length > 8192 && this.cmdLength * 2L < (long)this.buf.length) {
         this.buf = new byte[8192];
      }

      this.pos = 4;
      this.cmdLength = 0L;
      this.mark = -1;
   }

   private void checkMaxAllowedLength(int length) throws MaxAllowedPacketException {
      if (this.maxAllowedPacket != null && this.cmdLength + (long)length >= (long)this.maxAllowedPacket) {
         throw new MaxAllowedPacketException("query size (" + (this.cmdLength + (long)length) + ") is >= to max_allowed_packet (" + this.maxAllowedPacket + ")", this.cmdLength != 0L);
      }
   }

   public boolean throwMaxAllowedLength(int length) {
      if (this.maxAllowedPacket != null) {
         return this.cmdLength + (long)length >= (long)this.maxAllowedPacket;
      } else {
         return false;
      }
   }

   public void permitTrace(boolean permitTrace) {
      this.permitTrace = permitTrace;
   }

   public void setServerThreadId(Long serverThreadId, HostAddress hostAddress) {
      Boolean isMaster = hostAddress != null ? hostAddress.primary : null;
      this.serverThreadLog = "conn=" + (serverThreadId == null ? "-1" : serverThreadId) + (isMaster != null ? " (" + (isMaster ? "M" : "S") + ")" : "");
   }

   public void mark() {
      this.mark = this.pos;
   }

   public boolean isMarked() {
      return this.mark != -1;
   }

   public boolean hasFlushed() {
      return this.sequence.get() != -1;
   }

   public void flushBufferStopAtMark() throws IOException {
      int end = this.pos;
      this.pos = this.mark;
      this.writeSocket(true);
      this.out.flush();
      this.initPacket();
      System.arraycopy(this.buf, this.mark, this.buf, this.pos, end - this.mark);
      this.pos += end - this.mark;
      this.mark = -1;
      this.bufContainDataAfterMark = true;
   }

   public boolean bufIsDataAfterMark() {
      return this.bufContainDataAfterMark;
   }

   public byte[] resetMark() {
      this.pos = this.mark;
      this.mark = -1;
      if (this.bufContainDataAfterMark) {
         byte[] data = Arrays.copyOfRange(this.buf, 4, this.pos);
         this.initPacket();
         this.bufContainDataAfterMark = false;
         return data;
      } else {
         return null;
      }
   }

   public void initPacket() {
      this.sequence.set((byte)-1);
      this.compressSequence.set((byte)-1);
      this.pos = 4;
      this.cmdLength = 0L;
   }

   protected void writeSocket(boolean commandEnd) throws IOException {
      if (this.pos > 4) {
         this.buf[0] = (byte)(this.pos - 4);
         this.buf[1] = (byte)(this.pos - 4 >>> 8);
         this.buf[2] = (byte)(this.pos - 4 >>> 16);
         this.buf[3] = this.sequence.incrementAndGet();
         this.checkMaxAllowedLength(this.pos - 4);
         this.out.write(this.buf, 0, this.pos);
         if (commandEnd) {
            this.out.flush();
         }

         this.cmdLength += (long)(this.pos - 4);
         if (logger.isTraceEnabled()) {
            if (this.permitTrace) {
               logger.trace("send: {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.buf, 0, this.pos, this.maxQuerySizeToLog));
            } else {
               logger.trace("send: content length={} {} com=<hidden>", this.pos - 4, this.serverThreadLog);
            }
         }

         if (commandEnd && this.pos == 16777219) {
            this.writeEmptyPacket();
         }

         this.pos = 4;
      }

   }

   public void close() throws IOException {
      this.out.close();
   }
}
