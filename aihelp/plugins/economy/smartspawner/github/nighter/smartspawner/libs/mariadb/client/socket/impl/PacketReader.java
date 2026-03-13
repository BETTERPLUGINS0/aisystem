package github.nighter.smartspawner.libs.mariadb.client.socket.impl;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableByte;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.LoggerHelper;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PacketReader implements Reader {
   private static final int REUSABLE_BUFFER_LENGTH = 8192;
   private static final int MAX_PACKET_SIZE = 16777215;
   private static final Logger logger = Loggers.getLogger(PacketReader.class);
   private final byte[] header = new byte[4];
   private final byte[] reusableArray = new byte[8192];
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
      int lastPacketLength = this.readHeader();
      this.sequence.set(this.header[3]);
      byte[] rawBytes;
      if (lastPacketLength < 8192) {
         rawBytes = this.reusableArray;
      } else {
         rawBytes = new byte[lastPacketLength];
      }

      int remaining = lastPacketLength;
      int off = 0;

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
      int packetLength = this.readHeader();
      byte[] rawBytes = new byte[packetLength];
      int remaining = packetLength;
      int off = 0;

      int currentLength;
      do {
         currentLength = this.inputStream.read(rawBytes, off, remaining);
         if (currentLength < 0) {
            throw new EOFException("unexpected end of stream, read " + (packetLength - remaining) + " bytes from " + packetLength + " (socket was closed by server)");
         }

         remaining -= currentLength;
         off += currentLength;
      } while(remaining > 0);

      if (traceEnable) {
         logger.trace("read: {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.header, rawBytes, 0, packetLength, this.maxQuerySizeToLog));
      }

      if (packetLength == 16777215) {
         do {
            packetLength = this.readHeader();
            currentLength = rawBytes.length;
            byte[] newRawBytes = new byte[currentLength + packetLength];
            System.arraycopy(rawBytes, 0, newRawBytes, 0, currentLength);
            rawBytes = newRawBytes;
            remaining = packetLength;
            off = currentLength;

            do {
               int count = this.inputStream.read(rawBytes, off, remaining);
               if (count < 0) {
                  throw new EOFException("unexpected end of stream, read " + (packetLength - remaining) + " bytes from " + packetLength);
               }

               remaining -= count;
               off += count;
            } while(remaining > 0);

            if (traceEnable) {
               logger.trace("read: {}\n{}", this.serverThreadLog, LoggerHelper.hex(this.header, rawBytes, currentLength, packetLength, this.maxQuerySizeToLog));
            }
         } while(packetLength == 16777215);
      }

      return rawBytes;
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

   private int readHeader() throws IOException {
      int remaining = 4;
      int off = 0;

      do {
         int count = this.inputStream.read(this.header, off, remaining);
         if (count < 0) {
            throw new EOFException("unexpected end of stream, read " + off + " bytes from 4 (socket was closed by server)");
         }

         remaining -= count;
         off += count;
      } while(remaining > 0);

      return (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
   }
}
