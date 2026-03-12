package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.LoggerHelper;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PacketReader implements Reader {
   private static final int REUSABLE_BUFFER_LENGTH = 1024;
   private static final int MAX_PACKET_SIZE = 16777215;
   private static final Logger logger = Loggers.getLogger(PacketReader.class);
   private final byte[] header = new byte[4];
   private final byte[] reusableArray = new byte[1024];
   private final InputStream inputStream;
   private final int maxQuerySizeToLog;
   private final MutableByte sequence;
   private final StandardReadableByteBuf readBuf = new StandardReadableByteBuf((byte[])null, 0);
   private String serverThreadLog = "";

   public PacketReader(InputStream in, Configuration conf, MutableByte sequence) {
      this.inputStream = in;
      this.maxQuerySizeToLog = conf.maxQuerySizeToLog();
      this.sequence = sequence;
   }

   public ReadableByteBuf readableBufFromArray(byte[] buf) {
      this.readBuf.buf(buf, buf.length, 0);
      return this.readBuf;
   }

   public ReadableByteBuf readReusablePacket() throws IOException {
      return this.readReusablePacket(logger.isTraceEnabled());
   }

   public ReadableByteBuf readReusablePacket(boolean traceEnable) throws IOException {
      int remaining = 4;
      int off = 0;

      int lastPacketLength;
      do {
         lastPacketLength = this.inputStream.read(this.header, off, remaining);
         if (lastPacketLength < 0) {
            throw new EOFException("unexpected end of stream, read " + off + " bytes from 4 (socket was closed by server)");
         }

         remaining -= lastPacketLength;
         off += lastPacketLength;
      } while(remaining > 0);

      lastPacketLength = (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
      this.sequence.set(this.header[3]);
      byte[] rawBytes;
      if (lastPacketLength < 1024) {
         rawBytes = this.reusableArray;
      } else {
         rawBytes = new byte[lastPacketLength];
      }

      remaining = lastPacketLength;
      off = 0;

      do {
         int count = this.inputStream.read(rawBytes, off, remaining);
         if (count < 0) {
            throw new EOFException("unexpected end of stream, read " + (lastPacketLength - remaining) + " bytes from " + lastPacketLength + " (socket was closed by server)");
         }

         remaining -= count;
         off += count;
      } while(remaining > 0);

      if (traceEnable) {
         logger.trace("read: {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.header, rawBytes, 0, lastPacketLength, this.maxQuerySizeToLog));
      }

      this.readBuf.buf(rawBytes, lastPacketLength, 0);
      return this.readBuf;
   }

   public byte[] readPacket(boolean traceEnable) throws IOException {
      int remaining = 4;
      int off = 0;

      int lastPacketLength;
      do {
         lastPacketLength = this.inputStream.read(this.header, off, remaining);
         if (lastPacketLength < 0) {
            throw new EOFException("unexpected end of stream, read " + off + " bytes from 4 (socket was closed by server)");
         }

         remaining -= lastPacketLength;
         off += lastPacketLength;
      } while(remaining > 0);

      lastPacketLength = (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
      byte[] rawBytes = new byte[lastPacketLength];
      remaining = lastPacketLength;
      off = 0;

      int packetLength;
      do {
         packetLength = this.inputStream.read(rawBytes, off, remaining);
         if (packetLength < 0) {
            throw new EOFException("unexpected end of stream, read " + (lastPacketLength - remaining) + " bytes from " + lastPacketLength + " (socket was closed by server)");
         }

         remaining -= packetLength;
         off += packetLength;
      } while(remaining > 0);

      if (traceEnable) {
         logger.trace("read: {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.header, rawBytes, 0, lastPacketLength, this.maxQuerySizeToLog));
      }

      if (lastPacketLength == 16777215) {
         do {
            remaining = 4;
            off = 0;

            int currentbufLength;
            do {
               currentbufLength = this.inputStream.read(this.header, off, remaining);
               if (currentbufLength < 0) {
                  throw new EOFException("unexpected end of stream, read " + off + " bytes from 4");
               }

               remaining -= currentbufLength;
               off += currentbufLength;
            } while(remaining > 0);

            packetLength = (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
            currentbufLength = rawBytes.length;
            byte[] newRawBytes = new byte[currentbufLength + packetLength];
            System.arraycopy(rawBytes, 0, newRawBytes, 0, currentbufLength);
            rawBytes = newRawBytes;
            remaining = packetLength;
            off = currentbufLength;

            do {
               int count = this.inputStream.read(rawBytes, off, remaining);
               if (count < 0) {
                  throw new EOFException("unexpected end of stream, read " + (packetLength - remaining) + " bytes from " + packetLength);
               }

               remaining -= count;
               off += count;
            } while(remaining > 0);

            if (traceEnable) {
               logger.trace("read: {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.header, rawBytes, currentbufLength, packetLength, this.maxQuerySizeToLog));
            }

            lastPacketLength += packetLength;
         } while(packetLength == 16777215);
      }

      return rawBytes;
   }

   public void skipPacket() throws IOException {
      if (logger.isTraceEnabled()) {
         this.readReusablePacket(logger.isTraceEnabled());
      } else {
         int remaining = 4;
         int off = 0;

         int lastPacketLength;
         do {
            lastPacketLength = this.inputStream.read(this.header, off, remaining);
            if (lastPacketLength < 0) {
               throw new EOFException("unexpected end of stream, read " + off + " bytes from 4 (socket was closed by server)");
            }

            remaining -= lastPacketLength;
            off += lastPacketLength;
         } while(remaining > 0);

         lastPacketLength = (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
         remaining = lastPacketLength;

         do {
            remaining -= (int)this.inputStream.skip((long)remaining);
         } while(remaining > 0);

         int packetLength;
         if (lastPacketLength == 16777215) {
            do {
               remaining = 4;
               off = 0;

               do {
                  int count = this.inputStream.read(this.header, off, remaining);
                  if (count < 0) {
                     throw new EOFException("unexpected end of stream, read " + off + " bytes from 4");
                  }

                  remaining -= count;
                  off += count;
               } while(remaining > 0);

               packetLength = (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
               remaining = packetLength;

               do {
                  remaining -= (int)this.inputStream.skip((long)remaining);
               } while(remaining > 0);

               lastPacketLength += packetLength;
            } while(packetLength == 16777215);
         }

      }
   }

   public MutableByte getSequence() {
      return this.sequence;
   }

   public void close() throws IOException {
      this.inputStream.close();
   }

   public void setServerThreadId(Long serverThreadId, HostAddress hostAddress) {
      Boolean isMaster = hostAddress != null ? hostAddress.primary : null;
      this.serverThreadLog = "conn=" + (serverThreadId == null ? "-1" : serverThreadId) + (isMaster != null ? " (" + (isMaster ? "M" : "S") + ")" : "");
   }
}
