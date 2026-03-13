package github.nighter.smartspawner.libs.mariadb.client;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.column.UuidColumn;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLDataException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public interface ColumnDecoder extends Column {
   static ColumnDecoder decodeStd(ReadableByteBuf buf) {
      int[] stringPos = new int[]{buf.skipIdentifier(), buf.skipIdentifier(), buf.skipIdentifier(), buf.skipIdentifier(), buf.skipIdentifier()};
      buf.skipIdentifier();
      buf.skip();
      short charset = buf.readShort();
      int length = buf.readInt();
      DataType dataType = DataType.of(buf.readUnsignedByte());
      int flags = buf.readUnsignedShort();
      byte decimals = buf.readByte();
      DataType.ColumnConstructor constructor = (flags & 32) == 0 ? dataType.getColumnConstructor() : dataType.getUnsignedColumnConstructor();
      return constructor.create(buf, charset, (long)length, dataType, decimals, flags, stringPos, (String)null, (String)null);
   }

   static ColumnDecoder decode(ReadableByteBuf buf) {
      int[] stringPos = new int[]{buf.skipIdentifier(), buf.skipIdentifier(), buf.skipIdentifier(), buf.skipIdentifier(), buf.skipIdentifier()};
      buf.skipIdentifier();
      String extTypeName = null;
      String extTypeFormat = null;
      if (buf.readByte() != 0) {
         buf.pos(buf.pos() - 1);
         ReadableByteBuf subPacket = buf.readLengthBuffer();

         while(subPacket.readableBytes() > 0) {
            switch(subPacket.readByte()) {
            case 0:
               extTypeName = subPacket.readAscii(subPacket.readLength());
               break;
            case 1:
               extTypeFormat = subPacket.readAscii(subPacket.readLength());
               break;
            default:
               subPacket.skip(subPacket.readLength());
            }
         }
      }

      buf.skip();
      short charset = buf.readShort();
      int length = buf.readInt();
      DataType dataType = DataType.of(buf.readUnsignedByte());
      int flags = buf.readUnsignedShort();
      byte decimals = buf.readByte();
      DataType.ColumnConstructor constructor = extTypeName != null && extTypeName.equals("uuid") ? UuidColumn::new : ((flags & 32) == 0 ? dataType.getColumnConstructor() : dataType.getUnsignedColumnConstructor());
      return constructor.create(buf, charset, (long)length, dataType, decimals, flags, stringPos, extTypeName, extTypeFormat);
   }

   static ColumnDecoder create(String database, String name, DataType type, int flags) {
      byte[] databaseBytes = (database == null ? "def" : database).getBytes(StandardCharsets.UTF_8);
      byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
      byte[] arr = new byte[databaseBytes.length + 6 + 2 * nameBytes.length];
      arr[0] = (byte)databaseBytes.length;
      System.arraycopy(databaseBytes, 0, arr, 1, databaseBytes.length);
      int[] stringPos = new int[]{databaseBytes.length + 1, databaseBytes.length + 2, databaseBytes.length + 3, 0, 0};
      int pos = databaseBytes.length + 4;

      for(int i = 0; i < 2; ++i) {
         stringPos[i + 3] = pos;
         arr[pos++] = (byte)nameBytes.length;
         System.arraycopy(nameBytes, 0, arr, pos, nameBytes.length);
         pos += nameBytes.length;
      }

      short len;
      switch(type) {
      case VARCHAR:
      case VARSTRING:
         len = 192;
         break;
      case SMALLINT:
         len = 5;
         break;
      case NULL:
         len = 0;
         break;
      default:
         len = 1;
      }

      DataType.ColumnConstructor constructor = (flags & 32) == 0 ? type.getColumnConstructor() : type.getUnsignedColumnConstructor();
      return constructor.create(new StandardReadableByteBuf(arr, arr.length), 33, (long)len, type, (byte)0, flags, stringPos, (String)null, (String)null);
   }

   String defaultClassname(Configuration var1);

   int getColumnType(Configuration var1);

   String getColumnTypeName(Configuration var1);

   default int getPrecision() {
      return (int)this.getColumnLength();
   }

   Object getDefaultText(ReadableByteBuf var1, MutableInt var2, Context var3) throws SQLDataException;

   Object getDefaultBinary(ReadableByteBuf var1, MutableInt var2, Context var3) throws SQLDataException;

   String decodeStringText(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   String decodeStringBinary(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   byte decodeByteText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   byte decodeByteBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   Date decodeDateText(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   Date decodeDateBinary(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   Time decodeTimeText(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   Time decodeTimeBinary(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   Timestamp decodeTimestampText(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   Timestamp decodeTimestampBinary(ReadableByteBuf var1, MutableInt var2, Calendar var3, Context var4) throws SQLDataException;

   boolean decodeBooleanText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   boolean decodeBooleanBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   short decodeShortText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   short decodeShortBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   int decodeIntText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   int decodeIntBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   long decodeLongText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   long decodeLongBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   float decodeFloatText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   float decodeFloatBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   double decodeDoubleText(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   double decodeDoubleBinary(ReadableByteBuf var1, MutableInt var2) throws SQLDataException;

   ColumnDecoder useAliasAsName();
}
