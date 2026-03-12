package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.BindValue;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJOperationNotSupportedException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamValueEncoder extends AbstractValueEncoder {
   private byte[] streamConvertBuf = null;

   public byte[] getBytes(BindValue binding) {
      return this.streamToBytes((InputStream)binding.getValue(), binding.getScaleOrLength(), (NativePacketPayload)null);
   }

   public String getString(BindValue binding) {
      return "'** STREAM DATA **'";
   }

   public void encodeAsText(Message msg, BindValue binding) {
      NativePacketPayload intoPacket = (NativePacketPayload)msg;
      this.streamToBytes((InputStream)binding.getValue(), binding.getScaleOrLength(), intoPacket);
   }

   public void encodeAsBinary(Message msg, BindValue binding) {
      throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
   }

   protected byte[] streamToBytes(InputStream in, long length, NativePacketPayload packet) {
      boolean useLength = length == -1L ? false : (Boolean)this.propertySet.getBooleanProperty(PropertyKey.useStreamLengthsInPrepStmts).getValue();
      in.mark(Integer.MAX_VALUE);

      byte[] var10;
      try {
         if (this.streamConvertBuf == null) {
            this.streamConvertBuf = new byte[4096];
         }

         int bcnt = useLength ? Util.readBlock(in, this.streamConvertBuf, (int)length, this.exceptionInterceptor) : Util.readBlock(in, this.streamConvertBuf, this.exceptionInterceptor);
         int lengthLeftToRead = (int)(length - (long)bcnt);
         ByteArrayOutputStream bytesOut = null;
         boolean hexEscape = false;
         if (packet == null) {
            bytesOut = new ByteArrayOutputStream();
         } else {
            hexEscape = this.serverSession.isNoBackslashEscapesSet();
            packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, StringUtils.getBytes(hexEscape ? "x" : "_binary"));
            packet.writeInteger(NativeConstants.IntegerDataType.INT1, 39L);
         }

         while(bcnt > 0) {
            if (packet == null) {
               bytesOut.write(this.streamConvertBuf, 0, bcnt);
            } else if (hexEscape) {
               StringUtils.hexEscapeBlock(this.streamConvertBuf, bcnt, (lowBits, highBits) -> {
                  packet.writeInteger(NativeConstants.IntegerDataType.INT1, (long)lowBits);
                  packet.writeInteger(NativeConstants.IntegerDataType.INT1, (long)highBits);
               });
            } else {
               this.escapeblockFast(this.streamConvertBuf, packet, bcnt);
            }

            if (useLength) {
               bcnt = Util.readBlock(in, this.streamConvertBuf, lengthLeftToRead, this.exceptionInterceptor);
               if (bcnt > 0) {
                  lengthLeftToRead -= bcnt;
               }
            } else {
               bcnt = Util.readBlock(in, this.streamConvertBuf, this.exceptionInterceptor);
            }
         }

         if (packet != null) {
            packet.writeInteger(NativeConstants.IntegerDataType.INT1, 39L);
            Object var24 = null;
            return (byte[])var24;
         }

         var10 = bytesOut.toByteArray();
      } finally {
         try {
            in.reset();
         } catch (IOException var22) {
         }

         if ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.autoClosePStmtStreams).getValue()) {
            try {
               in.close();
            } catch (IOException var21) {
            }

            in = null;
         }

      }

      return var10;
   }

   private final void escapeblockFast(byte[] buf, NativePacketPayload packet, int size) {
      int lastwritten = 0;

      for(int i = 0; i < size; ++i) {
         byte b = buf[i];
         if (b == 0) {
            if (i > lastwritten) {
               packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, lastwritten, i - lastwritten);
            }

            packet.writeInteger(NativeConstants.IntegerDataType.INT1, 92L);
            packet.writeInteger(NativeConstants.IntegerDataType.INT1, 48L);
            lastwritten = i + 1;
         } else if (b == 92 || b == 39) {
            if (i > lastwritten) {
               packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, lastwritten, i - lastwritten);
            }

            packet.writeInteger(NativeConstants.IntegerDataType.INT1, (long)b);
            lastwritten = i;
         }
      }

      if (lastwritten < size) {
         packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, lastwritten, size - lastwritten);
      }

   }
}
