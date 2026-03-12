package fr.xephi.authme.libs.org.mariadb.jdbc.client.result.rowdecoder;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableInt;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public interface RowDecoder {
   boolean wasNull(byte[] var1, MutableInt var2, MutableInt var3);

   int setPosition(int var1, MutableInt var2, int var3, StandardReadableByteBuf var4, byte[] var5, ColumnDecoder[] var6);

   <T> T decode(Codec<T> var1, Calendar var2, StandardReadableByteBuf var3, MutableInt var4, ColumnDecoder[] var5, MutableInt var6) throws SQLException;

   Object defaultDecode(Configuration var1, ColumnDecoder[] var2, MutableInt var3, StandardReadableByteBuf var4, MutableInt var5) throws SQLException;

   byte decodeByte(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   boolean decodeBoolean(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   Date decodeDate(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Calendar var5) throws SQLException;

   Time decodeTime(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Calendar var5) throws SQLException;

   Timestamp decodeTimestamp(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Calendar var5) throws SQLException;

   short decodeShort(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   int decodeInt(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   String decodeString(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   long decodeLong(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   float decodeFloat(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   double decodeDouble(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;
}
