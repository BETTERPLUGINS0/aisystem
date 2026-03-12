package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.Parser;
import fr.xephi.authme.libs.org.postgresql.jdbc2.ArrayAssistant;
import fr.xephi.authme.libs.org.postgresql.jdbc2.ArrayAssistantRegistry;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGbytea;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.index.qual.NonNegative;

final class ArrayDecoding {
   private static final ArrayDecoding.ArrayDecoder<Long[]> LONG_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Long[]>(Long.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return bytes.getLong();
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toLong(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Long[]> INT4_UNSIGNED_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Long[]>(Long.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return (long)bytes.getInt() & 4294967295L;
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toLong(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Integer[]> INTEGER_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Integer[]>(Integer.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return bytes.getInt();
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toInt(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Short[]> SHORT_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Short[]>(Short.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return bytes.getShort();
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toShort(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Double[]> DOUBLE_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Double[]>(Double.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return bytes.getDouble();
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toDouble(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Float[]> FLOAT_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Float[]>(Float.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return bytes.getFloat();
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toFloat(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Boolean[]> BOOLEAN_OBJ_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<Boolean[]>(Boolean.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) {
         return bytes.get() == 1;
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return BooleanTypeUtil.fromString(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<String[]> STRING_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<String[]>(String.class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) throws SQLException {
         assert bytes.hasArray();

         byte[] byteArray = bytes.array();
         int offset = bytes.arrayOffset() + bytes.position();

         String val;
         try {
            val = connection.getEncoding().decode(byteArray, offset, length);
         } catch (IOException var8) {
            throw new PSQLException(GT.tr("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database."), PSQLState.DATA_ERROR, var8);
         }

         bytes.position(bytes.position() + length);
         return val;
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return stringVal;
      }
   };
   private static final ArrayDecoding.ArrayDecoder<byte[][]> BYTE_ARRAY_ARRAY = new ArrayDecoding.AbstractObjectArrayDecoder<byte[][]>(byte[].class) {
      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) throws SQLException {
         byte[] array = new byte[length];
         bytes.get(array);
         return array;
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PGbytea.toBytes(stringVal.getBytes(StandardCharsets.US_ASCII));
      }
   };
   private static final ArrayDecoding.ArrayDecoder<BigDecimal[]> BIG_DECIMAL_STRING_DECODER = new ArrayDecoding.AbstractObjectStringArrayDecoder<BigDecimal[]>(BigDecimal.class) {
      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return PgResultSet.toBigDecimal(stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<String[]> STRING_ONLY_DECODER = new ArrayDecoding.AbstractObjectStringArrayDecoder<String[]>(String.class) {
      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return stringVal;
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Date[]> DATE_DECODER = new ArrayDecoding.AbstractObjectStringArrayDecoder<Date[]>(Date.class) {
      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return connection.getTimestampUtils().toDate((Calendar)null, stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Time[]> TIME_DECODER = new ArrayDecoding.AbstractObjectStringArrayDecoder<Time[]>(Time.class) {
      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return connection.getTimestampUtils().toTime((Calendar)null, stringVal);
      }
   };
   private static final ArrayDecoding.ArrayDecoder<Timestamp[]> TIMESTAMP_DECODER = new ArrayDecoding.AbstractObjectStringArrayDecoder<Timestamp[]>(Timestamp.class) {
      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return connection.getTimestampUtils().toTimestamp((Calendar)null, stringVal);
      }
   };
   private static final Map<Integer, ArrayDecoding.ArrayDecoder> OID_TO_DECODER = new HashMap(29);

   private static <A> ArrayDecoding.ArrayDecoder<A> getDecoder(int oid, BaseConnection connection) throws SQLException {
      Integer key = oid;
      ArrayDecoding.ArrayDecoder decoder = (ArrayDecoding.ArrayDecoder)OID_TO_DECODER.get(key);
      if (decoder != null) {
         return decoder;
      } else {
         ArrayAssistant assistant = ArrayAssistantRegistry.getAssistant(oid);
         if (assistant != null) {
            return new ArrayDecoding.ArrayAssistantObjectArrayDecoder(assistant);
         } else {
            String typeName = connection.getTypeInfo().getPGType(oid);
            if (typeName == null) {
               throw Driver.notImplemented(PgArray.class, "readArray(data,oid)");
            } else {
               int type = connection.getTypeInfo().getSQLType(typeName);
               return (ArrayDecoding.ArrayDecoder)(type != 1 && type != 12 ? new ArrayDecoding.MappedTypeObjectArrayDecoder(typeName) : STRING_ONLY_DECODER);
            }
         }
      }
   }

   public static Object readBinaryArray(int index, int count, byte[] bytes, BaseConnection connection) throws SQLException {
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      buffer.order(ByteOrder.BIG_ENDIAN);
      int dimensions = buffer.getInt();
      boolean hasNulls = buffer.getInt() != 0;
      int elementOid = buffer.getInt();
      ArrayDecoding.ArrayDecoder decoder = getDecoder(elementOid, connection);
      if (!decoder.supportBinary()) {
         throw Driver.notImplemented(PgArray.class, "readBinaryArray(data,oid)");
      } else if (dimensions == 0) {
         return decoder.createArray(0);
      } else {
         int adjustedSkipIndex = index > 0 ? index - 1 : 0;
         if (dimensions == 1) {
            int length = buffer.getInt();
            buffer.position(buffer.position() + 4);
            if (count > 0) {
               length = Math.min(length, count);
            }

            Object array = decoder.createArray(length);
            decoder.populateFromBinary(array, adjustedSkipIndex, length, buffer, connection);
            return array;
         } else {
            int[] dimensionLengths = new int[dimensions];

            for(int i = 0; i < dimensions; ++i) {
               dimensionLengths[i] = buffer.getInt();
               buffer.position(buffer.position() + 4);
            }

            if (count > 0) {
               dimensionLengths[0] = Math.min(count, dimensionLengths[0]);
            }

            Object[] array = decoder.createMultiDimensionalArray(dimensionLengths);
            storeValues(array, decoder, buffer, adjustedSkipIndex, dimensionLengths, 0, connection);
            return array;
         }
      }
   }

   private static <A> void storeValues(A[] array, ArrayDecoding.ArrayDecoder<A> decoder, ByteBuffer bytes, int skip, int[] dimensionLengths, int dim, BaseConnection connection) throws SQLException {
      assert dim <= dimensionLengths.length - 2;

      int i;
      for(i = 0; i < skip; ++i) {
         if (dim == dimensionLengths.length - 2) {
            decoder.populateFromBinary(array[0], 0, dimensionLengths[dim + 1], bytes, connection);
         } else {
            storeValues((Object[])array[0], decoder, bytes, 0, dimensionLengths, dim + 1, connection);
         }
      }

      for(i = 0; i < dimensionLengths[dim]; ++i) {
         if (dim == dimensionLengths.length - 2) {
            decoder.populateFromBinary(array[i], 0, dimensionLengths[dim + 1], bytes, connection);
         } else {
            storeValues((Object[])array[i], decoder, bytes, 0, dimensionLengths, dim + 1, connection);
         }
      }

   }

   static ArrayDecoding.PgArrayList buildArrayList(String fieldString, char delim) {
      ArrayDecoding.PgArrayList arrayList = new ArrayDecoding.PgArrayList();
      if (fieldString == null) {
         return arrayList;
      } else {
         char[] chars = fieldString.toCharArray();
         StringBuilder buffer = null;
         boolean insideString = false;
         boolean wasInsideString = false;
         List<ArrayDecoding.PgArrayList> dims = new ArrayList();
         ArrayDecoding.PgArrayList curArray = arrayList;
         int startOffset = 0;
         if (chars[0] == '[') {
            while(chars[startOffset] != '=') {
               ++startOffset;
            }

            ++startOffset;
         }

         for(int i = startOffset; i < chars.length; ++i) {
            if (chars[i] == '\\') {
               ++i;
            } else {
               if (!insideString && chars[i] == '{') {
                  if (dims.isEmpty()) {
                     dims.add(arrayList);
                  } else {
                     ArrayDecoding.PgArrayList a = new ArrayDecoding.PgArrayList();
                     ArrayDecoding.PgArrayList p = (ArrayDecoding.PgArrayList)dims.get(dims.size() - 1);
                     p.add(a);
                     dims.add(a);
                  }

                  curArray = (ArrayDecoding.PgArrayList)dims.get(dims.size() - 1);

                  for(int t = i + 1; t < chars.length; ++t) {
                     if (!Character.isWhitespace(chars[t])) {
                        if (chars[t] != '{') {
                           break;
                        }

                        ++curArray.dimensionsCount;
                     }
                  }

                  buffer = new StringBuilder();
                  continue;
               }

               if (chars[i] == '"') {
                  insideString = !insideString;
                  wasInsideString = true;
                  continue;
               }

               if (!insideString && Parser.isArrayWhiteSpace(chars[i])) {
                  continue;
               }

               if (!insideString && (chars[i] == delim || chars[i] == '}') || i == chars.length - 1) {
                  if (chars[i] != '"' && chars[i] != '}' && chars[i] != delim && buffer != null) {
                     buffer.append(chars[i]);
                  }

                  String b = buffer == null ? null : buffer.toString();
                  if (b != null && (!b.isEmpty() || wasInsideString)) {
                     curArray.add(!wasInsideString && "NULL".equals(b) ? null : b);
                  }

                  wasInsideString = false;
                  buffer = new StringBuilder();
                  if (chars[i] == '}') {
                     dims.remove(dims.size() - 1);
                     if (!dims.isEmpty()) {
                        curArray = (ArrayDecoding.PgArrayList)dims.get(dims.size() - 1);
                     }

                     buffer = null;
                  }
                  continue;
               }
            }

            if (buffer != null) {
               buffer.append(chars[i]);
            }
         }

         return arrayList;
      }
   }

   public static Object readStringArray(int index, int count, int oid, ArrayDecoding.PgArrayList list, BaseConnection connection) throws SQLException {
      ArrayDecoding.ArrayDecoder decoder = getDecoder(oid, connection);
      int dims = list.dimensionsCount;
      if (dims == 0) {
         return decoder.createArray(0);
      } else {
         boolean sublist = false;
         int adjustedSkipIndex = 0;
         if (index > 1) {
            sublist = true;
            adjustedSkipIndex = index - 1;
         }

         int adjustedCount = list.size();
         if (count > 0 && count != adjustedCount) {
            sublist = true;
            adjustedCount = Math.min(adjustedCount, count);
         }

         List adjustedList = sublist ? list.subList(adjustedSkipIndex, adjustedSkipIndex + adjustedCount) : list;
         if (dims == 1) {
            int length = ((List)adjustedList).size();
            if (count > 0) {
               length = Math.min(length, count);
            }

            Object array = decoder.createArray(length);
            decoder.populateFromString(array, (List)adjustedList, connection);
            return array;
         } else {
            int[] dimensionLengths = new int[dims];
            dimensionLengths[0] = adjustedCount;
            List tmpList = (List)((List)adjustedList).get(0);

            for(int i = 1; i < dims; ++i) {
               dimensionLengths[i] = ((List)Nullness.castNonNull(tmpList, "first element of adjustedList is null")).size();
               if (i != dims - 1) {
                  tmpList = (List)tmpList.get(0);
               }
            }

            Object[] array = decoder.createMultiDimensionalArray(dimensionLengths);
            storeStringValues(array, decoder, (List)adjustedList, dimensionLengths, 0, connection);
            return array;
         }
      }
   }

   private static <A> void storeStringValues(A[] array, ArrayDecoding.ArrayDecoder<A> decoder, List list, int[] dimensionLengths, int dim, BaseConnection connection) throws SQLException {
      assert dim <= dimensionLengths.length - 2;

      for(int i = 0; i < dimensionLengths[dim]; ++i) {
         Object element = Nullness.castNonNull(list.get(i), "list.get(i)");
         if (dim == dimensionLengths.length - 2) {
            decoder.populateFromString(array[i], (List)element, connection);
         } else {
            storeStringValues((Object[])array[i], decoder, (List)element, dimensionLengths, dim + 1, connection);
         }
      }

   }

   static {
      OID_TO_DECODER.put(26, INT4_UNSIGNED_OBJ_ARRAY);
      OID_TO_DECODER.put(20, LONG_OBJ_ARRAY);
      OID_TO_DECODER.put(23, INTEGER_OBJ_ARRAY);
      OID_TO_DECODER.put(21, SHORT_OBJ_ARRAY);
      OID_TO_DECODER.put(790, DOUBLE_OBJ_ARRAY);
      OID_TO_DECODER.put(701, DOUBLE_OBJ_ARRAY);
      OID_TO_DECODER.put(700, FLOAT_OBJ_ARRAY);
      OID_TO_DECODER.put(25, STRING_ARRAY);
      OID_TO_DECODER.put(1043, STRING_ARRAY);
      OID_TO_DECODER.put(3802, STRING_ONLY_DECODER);
      OID_TO_DECODER.put(1560, BOOLEAN_OBJ_ARRAY);
      OID_TO_DECODER.put(16, BOOLEAN_OBJ_ARRAY);
      OID_TO_DECODER.put(17, BYTE_ARRAY_ARRAY);
      OID_TO_DECODER.put(1700, BIG_DECIMAL_STRING_DECODER);
      OID_TO_DECODER.put(1042, STRING_ONLY_DECODER);
      OID_TO_DECODER.put(18, STRING_ONLY_DECODER);
      OID_TO_DECODER.put(114, STRING_ONLY_DECODER);
      OID_TO_DECODER.put(1082, DATE_DECODER);
      OID_TO_DECODER.put(1083, TIME_DECODER);
      OID_TO_DECODER.put(1266, TIME_DECODER);
      OID_TO_DECODER.put(1114, TIMESTAMP_DECODER);
      OID_TO_DECODER.put(1184, TIMESTAMP_DECODER);
   }

   private interface ArrayDecoder<A> {
      A createArray(@NonNegative int var1);

      Object[] createMultiDimensionalArray(int[] var1);

      boolean supportBinary();

      void populateFromBinary(A var1, @NonNegative int var2, @NonNegative int var3, ByteBuffer var4, BaseConnection var5) throws SQLException;

      void populateFromString(A var1, List<String> var2, BaseConnection var3) throws SQLException;
   }

   private static final class ArrayAssistantObjectArrayDecoder extends ArrayDecoding.AbstractObjectArrayDecoder {
      private final ArrayAssistant arrayAssistant;

      ArrayAssistantObjectArrayDecoder(ArrayAssistant arrayAssistant) {
         super(arrayAssistant.baseType());
         this.arrayAssistant = arrayAssistant;
      }

      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) throws SQLException {
         assert bytes.hasArray();

         byte[] byteArray = bytes.array();
         int offset = bytes.arrayOffset() + bytes.position();
         Object val = this.arrayAssistant.buildElement(byteArray, offset, length);
         bytes.position(bytes.position() + length);
         return val;
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return this.arrayAssistant.buildElement(stringVal);
      }
   }

   private static final class MappedTypeObjectArrayDecoder extends ArrayDecoding.AbstractObjectArrayDecoder<Object[]> {
      private final String typeName;

      MappedTypeObjectArrayDecoder(String baseTypeName) {
         super(Object.class);
         this.typeName = baseTypeName;
      }

      Object parseValue(int length, ByteBuffer bytes, BaseConnection connection) throws SQLException {
         byte[] copy = new byte[length];
         bytes.get(copy);
         return connection.getObject(this.typeName, (String)null, copy);
      }

      Object parseValue(String stringVal, BaseConnection connection) throws SQLException {
         return connection.getObject(this.typeName, stringVal, (byte[])null);
      }
   }

   static final class PgArrayList extends ArrayList<Object> {
      private static final long serialVersionUID = 1L;
      int dimensionsCount = 1;
   }

   private abstract static class AbstractObjectArrayDecoder<A> extends ArrayDecoding.AbstractObjectStringArrayDecoder<A> {
      AbstractObjectArrayDecoder(Class<?> baseClazz) {
         super(baseClazz);
      }

      public boolean supportBinary() {
         return true;
      }

      public void populateFromBinary(A arr, int index, int count, ByteBuffer bytes, BaseConnection connection) throws SQLException {
         Object[] array = (Object[])arr;

         int i;
         int length;
         for(i = 0; i < index; ++i) {
            length = bytes.getInt();
            if (length > 0) {
               bytes.position(bytes.position() + length);
            }
         }

         for(i = 0; i < count; ++i) {
            length = bytes.getInt();
            if (length != -1) {
               array[i] = this.parseValue(length, bytes, connection);
            } else {
               array[i] = null;
            }
         }

      }

      abstract Object parseValue(int var1, ByteBuffer var2, BaseConnection var3) throws SQLException;
   }

   private abstract static class AbstractObjectStringArrayDecoder<A> implements ArrayDecoding.ArrayDecoder<A> {
      final Class<?> baseClazz;

      AbstractObjectStringArrayDecoder(Class<?> baseClazz) {
         this.baseClazz = baseClazz;
      }

      public boolean supportBinary() {
         return false;
      }

      public A createArray(int size) {
         return Array.newInstance(this.baseClazz, size);
      }

      public Object[] createMultiDimensionalArray(int[] sizes) {
         return (Object[])Array.newInstance(this.baseClazz, sizes);
      }

      public void populateFromBinary(A arr, int index, int count, ByteBuffer bytes, BaseConnection connection) throws SQLException {
         throw new SQLFeatureNotSupportedException();
      }

      public void populateFromString(A arr, List<String> strings, BaseConnection connection) throws SQLException {
         Object[] array = (Object[])arr;
         int i = 0;

         for(int j = strings.size(); i < j; ++i) {
            String stringVal = (String)strings.get(i);
            array[i] = stringVal != null ? this.parseValue(stringVal, connection) : null;
         }

      }

      abstract Object parseValue(String var1, BaseConnection var2) throws SQLException;
   }
}
