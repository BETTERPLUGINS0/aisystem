package fr.xephi.authme.libs.org.mariadb.jdbc.message.server;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Column;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ServerMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.CharsetEncodingLength;
import java.util.Objects;

public class ColumnDefinitionPacket implements Column, ServerMessage {
   protected final int charset;
   protected final long columnLength;
   protected final DataType dataType;
   protected final byte decimals;
   protected final String extTypeName;
   protected final String extTypeFormat;
   private final ReadableByteBuf buf;
   private final int flags;
   private final int[] stringPos;
   private final boolean useAliasAsName;

   public ColumnDefinitionPacket(ReadableByteBuf buf, int charset, long columnLength, DataType dataType, byte decimals, int flags, int[] stringPos, String extTypeName, String extTypeFormat, boolean useAliasAsName) {
      this.buf = buf;
      this.charset = charset;
      this.columnLength = columnLength;
      this.dataType = dataType;
      this.decimals = decimals;
      this.flags = flags;
      this.stringPos = stringPos;
      this.extTypeName = extTypeName;
      this.extTypeFormat = extTypeFormat;
      this.useAliasAsName = useAliasAsName;
   }

   protected ColumnDefinitionPacket(ColumnDefinitionPacket prev, boolean useAliasAsName) {
      this.buf = prev.buf;
      this.charset = prev.charset;
      this.columnLength = prev.columnLength;
      this.dataType = prev.dataType;
      this.decimals = prev.decimals;
      this.flags = prev.flags;
      this.stringPos = prev.stringPos;
      this.extTypeName = prev.extTypeName;
      this.extTypeFormat = prev.extTypeFormat;
      this.useAliasAsName = useAliasAsName;
   }

   public String getCatalog() {
      return "def";
   }

   public String getSchema() {
      this.buf.pos(this.stringPos[0]);
      return this.buf.readString(this.buf.readIntLengthEncodedNotNull());
   }

   public String getTableAlias() {
      this.buf.pos(this.stringPos[1]);
      return this.buf.readString(this.buf.readIntLengthEncodedNotNull());
   }

   public String getTable() {
      this.buf.pos(this.stringPos[this.useAliasAsName ? 1 : 2]);
      return this.buf.readString(this.buf.readIntLengthEncodedNotNull());
   }

   public String getColumnAlias() {
      this.buf.pos(this.stringPos[3]);
      return this.buf.readString(this.buf.readIntLengthEncodedNotNull());
   }

   public String getColumnName() {
      this.buf.pos(this.stringPos[4]);
      return this.buf.readString(this.buf.readIntLengthEncodedNotNull());
   }

   public long getColumnLength() {
      return this.columnLength;
   }

   public DataType getType() {
      return this.dataType;
   }

   public byte getDecimals() {
      return this.decimals;
   }

   public boolean isSigned() {
      return (this.flags & 32) == 0;
   }

   public int getDisplaySize() {
      if (this.isBinary() || this.dataType != DataType.VARCHAR && this.dataType != DataType.JSON && this.dataType != DataType.ENUM && this.dataType != DataType.SET && this.dataType != DataType.VARSTRING && this.dataType != DataType.STRING && this.dataType != DataType.BLOB && this.dataType != DataType.TINYBLOB && this.dataType != DataType.MEDIUMBLOB && this.dataType != DataType.LONGBLOB) {
         return (int)this.columnLength;
      } else {
         Integer maxWidth = (Integer)CharsetEncodingLength.maxCharlen.get(this.charset);
         return maxWidth != null ? (int)(this.columnLength / (long)maxWidth) : (int)(this.columnLength / 4L);
      }
   }

   public boolean isPrimaryKey() {
      return (this.flags & 2) > 0;
   }

   public boolean isAutoIncrement() {
      return (this.flags & 512) > 0;
   }

   public boolean hasDefault() {
      return (this.flags & 4096) == 0;
   }

   public boolean isBinary() {
      return this.charset == 63;
   }

   public int getFlags() {
      return this.flags;
   }

   public String getExtTypeName() {
      return this.extTypeName;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ColumnDefinitionPacket that = (ColumnDefinitionPacket)o;
         return this.charset == that.charset && this.columnLength == that.columnLength && this.dataType == that.dataType && this.decimals == that.decimals && this.flags == that.flags;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.charset, this.columnLength, this.dataType, this.decimals, this.flags});
   }
}
