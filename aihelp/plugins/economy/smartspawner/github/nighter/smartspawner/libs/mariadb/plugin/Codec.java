package github.nighter.smartspawner.libs.mariadb.plugin;

import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Calendar;

public interface Codec<T> {
   String className();

   boolean canDecode(ColumnDecoder var1, Class<?> var2);

   boolean canEncode(Object var1);

   T decodeText(ReadableByteBuf var1, MutableInt var2, ColumnDecoder var3, Calendar var4, Context var5) throws SQLDataException;

   T decodeBinary(ReadableByteBuf var1, MutableInt var2, ColumnDecoder var3, Calendar var4, Context var5) throws SQLDataException;

   void encodeText(Writer var1, Context var2, Object var3, Calendar var4, Long var5) throws IOException, SQLException;

   int getApproximateTextProtocolLength(Object var1, Long var2);

   void encodeBinary(Writer var1, Context var2, Object var3, Calendar var4, Long var5) throws IOException, SQLException;

   default boolean canEncodeLongData() {
      return false;
   }

   default void encodeLongData(Writer encoder, T value, Long length) throws IOException, SQLException {
      throw new SQLException("Data is not supposed to be send in COM_STMT_LONG_DATA");
   }

   default byte[] encodeData(T value, Long length) throws IOException, SQLException {
      throw new SQLException("Data is not supposed to be send in COM_STMT_LONG_DATA");
   }

   int getBinaryEncodeType();
}
