package github.nighter.smartspawner.libs.mariadb.client.result.rowdecoder;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public interface RowDecoder {
   boolean wasNull(byte[] var1, MutableInt var2, MutableInt var3);

   int setPosition(int var1, MutableInt var2, int var3, StandardReadableByteBuf var4, byte[] var5, ColumnDecoder[] var6);

   <T> T decode(Codec<T> var1, Calendar var2, StandardReadableByteBuf var3, MutableInt var4, ColumnDecoder[] var5, MutableInt var6, Context var7) throws SQLException;

   Object defaultDecode(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Context var5) throws SQLException;

   byte decodeByte(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   boolean decodeBoolean(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   Date decodeDate(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Calendar var5, Context var6) throws SQLException;

   Time decodeTime(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Calendar var5, Context var6) throws SQLException;

   Timestamp decodeTimestamp(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Calendar var5, Context var6) throws SQLException;

   short decodeShort(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   int decodeInt(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   String decodeString(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4, Context var5) throws SQLException;

   long decodeLong(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   float decodeFloat(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;

   double decodeDouble(ColumnDecoder[] var1, MutableInt var2, StandardReadableByteBuf var3, MutableInt var4) throws SQLException;
}
