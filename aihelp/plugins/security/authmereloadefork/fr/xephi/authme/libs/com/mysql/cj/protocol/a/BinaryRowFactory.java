package fr.xephi.authme.libs.com.mysql.cj.protocol.a;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ColumnDefinition;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Resultset;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultsetRow;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.result.BinaryBufferRow;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.result.ByteArrayRow;
import fr.xephi.authme.libs.com.mysql.cj.result.Field;

public class BinaryRowFactory extends AbstractRowFactory implements ProtocolEntityFactory<ResultsetRow, NativePacketPayload> {
   public BinaryRowFactory(NativeProtocol protocol, ColumnDefinition columnDefinition, Resultset.Concurrency resultSetConcurrency, boolean canReuseRowPacketForBufferRow) {
      this.columnDefinition = columnDefinition;
      this.resultSetConcurrency = resultSetConcurrency;
      this.canReuseRowPacketForBufferRow = canReuseRowPacketForBufferRow;
      this.useBufferRowSizeThreshold = protocol.getPropertySet().getMemorySizeProperty(PropertyKey.largeRowSizeThreshold);
      this.exceptionInterceptor = protocol.getExceptionInterceptor();
      this.valueDecoder = new MysqlBinaryValueDecoder();
   }

   public ResultsetRow createFromMessage(NativePacketPayload rowPacket) {
      boolean useBufferRow = this.canReuseRowPacketForBufferRow || this.columnDefinition.hasLargeFields() || rowPacket.getPayloadLength() >= (Integer)this.useBufferRowSizeThreshold.getValue();
      rowPacket.setPosition(rowPacket.getPosition() + 1);
      return (ResultsetRow)(this.resultSetConcurrency != Resultset.Concurrency.UPDATABLE && useBufferRow ? new BinaryBufferRow(rowPacket, this.columnDefinition, this.exceptionInterceptor, this.valueDecoder) : this.unpackBinaryResultSetRow(this.columnDefinition.getFields(), rowPacket));
   }

   public boolean canReuseRowPacketForBufferRow() {
      return this.canReuseRowPacketForBufferRow;
   }

   private final ResultsetRow unpackBinaryResultSetRow(Field[] fields, NativePacketPayload binaryData) {
      int numFields = fields.length;
      byte[][] unpackedRowBytes = new byte[numFields][];
      int nullCount = (numFields + 9) / 8;
      int nullMaskPos = binaryData.getPosition();
      binaryData.setPosition(nullMaskPos + nullCount);
      int bit = 4;
      byte[] buf = binaryData.getByteBuffer();

      for(int i = 0; i < numFields; ++i) {
         if ((buf[nullMaskPos] & bit) != 0) {
            unpackedRowBytes[i] = null;
         } else {
            this.extractNativeEncodedColumn(binaryData, fields, i, unpackedRowBytes);
         }

         if (((bit <<= 1) & 255) == 0) {
            bit = 1;
            ++nullMaskPos;
         }
      }

      return new ByteArrayRow(unpackedRowBytes, this.exceptionInterceptor, new MysqlBinaryValueDecoder());
   }

   private final void extractNativeEncodedColumn(NativePacketPayload binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) {
      int type = fields[columnIndex].getMysqlTypeId();
      int len = NativeUtils.getBinaryEncodedLength(type);
      if (type != 6) {
         if (len == 0) {
            unpackedRowData[columnIndex] = binaryData.readBytes(NativeConstants.StringSelfDataType.STRING_LENENC);
         } else {
            if (len <= 0) {
               throw ExceptionFactory.createException(Messages.getString("MysqlIO.97", new Object[]{type, columnIndex, fields.length}));
            }

            unpackedRowData[columnIndex] = binaryData.readBytes(NativeConstants.StringLengthDataType.STRING_FIXED, len);
         }
      }

   }
}
