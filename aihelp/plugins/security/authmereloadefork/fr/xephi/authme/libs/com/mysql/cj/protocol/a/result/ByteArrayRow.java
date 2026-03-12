package fr.xephi.authme.libs.com.mysql.cj.protocol.a.result;

import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ValueDecoder;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.MysqlTextValueDecoder;
import fr.xephi.authme.libs.com.mysql.cj.protocol.result.AbstractResultsetRow;
import fr.xephi.authme.libs.com.mysql.cj.result.ValueFactory;

public class ByteArrayRow extends AbstractResultsetRow {
   byte[][] internalRowData;

   public ByteArrayRow(byte[][] internalRowData, ExceptionInterceptor exceptionInterceptor, ValueDecoder valueDecoder) {
      super(exceptionInterceptor);
      this.internalRowData = internalRowData;
      this.valueDecoder = valueDecoder;
   }

   public ByteArrayRow(byte[][] internalRowData, ExceptionInterceptor exceptionInterceptor) {
      super(exceptionInterceptor);
      this.internalRowData = internalRowData;
      this.valueDecoder = new MysqlTextValueDecoder();
   }

   public boolean isBinaryEncoded() {
      return this.valueDecoder instanceof MysqlBinaryValueDecoder;
   }

   public byte[] getBytes(int index) {
      return this.getNull(index) ? null : this.internalRowData[index];
   }

   public void setBytes(int index, byte[] value) {
      this.internalRowData[index] = value;
   }

   public boolean getNull(int columnIndex) {
      this.wasNull = this.internalRowData[columnIndex] == null;
      return this.wasNull;
   }

   public <T> T getValue(int columnIndex, ValueFactory<T> vf) {
      byte[] columnData = this.internalRowData[columnIndex];
      int length = columnData == null ? 0 : columnData.length;
      return this.getValueFromBytes(columnIndex, columnData, 0, length, vf);
   }
}
