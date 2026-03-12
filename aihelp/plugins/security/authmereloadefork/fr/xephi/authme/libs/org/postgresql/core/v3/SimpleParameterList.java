package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Utils;
import fr.xephi.authme.libs.org.postgresql.geometric.PGbox;
import fr.xephi.authme.libs.org.postgresql.geometric.PGpoint;
import fr.xephi.authme.libs.org.postgresql.jdbc.UUIDArrayAssistant;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.StreamWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;

class SimpleParameterList implements V3ParameterList {
   private static final byte IN = 1;
   private static final byte OUT = 2;
   private static final byte INOUT = 3;
   private static final byte TEXT = 0;
   private static final byte BINARY = 4;
   private final Object[] paramValues;
   private final int[] paramTypes;
   private final byte[] flags;
   private final byte[][] encoded;
   @Nullable
   private final TypeTransferModeRegistry transferModeRegistry;
   private static final Object NULL_OBJECT = new Object();
   private int pos;

   SimpleParameterList(int paramCount, @Nullable TypeTransferModeRegistry transferModeRegistry) {
      this.paramValues = new Object[paramCount];
      this.paramTypes = new int[paramCount];
      this.encoded = new byte[paramCount][];
      this.flags = new byte[paramCount];
      this.transferModeRegistry = transferModeRegistry;
   }

   public void registerOutParameter(int index, int sqlType) throws SQLException {
      if (index >= 1 && index <= this.paramValues.length) {
         byte[] var10000 = this.flags;
         var10000[index - 1] = (byte)(var10000[index - 1] | 2);
      } else {
         throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", index, this.paramValues.length), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   private void bind(int index, Object value, int oid, byte binary) throws SQLException {
      if (index >= 1 && index <= this.paramValues.length) {
         --index;
         this.encoded[index] = null;
         this.paramValues[index] = value;
         this.flags[index] = (byte)(this.direction(index) | 1 | binary);
         if (oid != 0 || this.paramTypes[index] == 0 || value != NULL_OBJECT) {
            this.paramTypes[index] = oid;
            this.pos = index + 1;
         }
      } else {
         throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", index, this.paramValues.length), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   @NonNegative
   public int getParameterCount() {
      return this.paramValues.length;
   }

   @NonNegative
   public int getOutParameterCount() {
      int count = 0;

      for(int i = 0; i < this.paramTypes.length; ++i) {
         if ((this.direction(i) & 2) == 2) {
            ++count;
         }
      }

      if (count == 0) {
         count = 1;
      }

      return count;
   }

   @NonNegative
   public int getInParameterCount() {
      int count = 0;

      for(int i = 0; i < this.paramTypes.length; ++i) {
         if (this.direction(i) != 2) {
            ++count;
         }
      }

      return count;
   }

   public void setIntParameter(@Positive int index, int value) throws SQLException {
      byte[] data = new byte[4];
      ByteConverter.int4(data, 0, value);
      this.bind(index, data, 23, (byte)4);
   }

   public void setLiteralParameter(@Positive int index, String value, int oid) throws SQLException {
      this.bind(index, value, oid, (byte)0);
   }

   public void setStringParameter(@Positive int index, String value, int oid) throws SQLException {
      this.bind(index, value, oid, (byte)0);
   }

   public void setBinaryParameter(@Positive int index, byte[] value, int oid) throws SQLException {
      this.bind(index, value, oid, (byte)4);
   }

   public void setBytea(@Positive int index, byte[] data, int offset, @NonNegative int length) throws SQLException {
      this.bind(index, new StreamWrapper(data, offset, length), 17, (byte)4);
   }

   public void setBytea(@Positive int index, InputStream stream, @NonNegative int length) throws SQLException {
      this.bind(index, new StreamWrapper(stream, length), 17, (byte)4);
   }

   public void setBytea(@Positive int index, InputStream stream) throws SQLException {
      this.bind(index, new StreamWrapper(stream), 17, (byte)4);
   }

   public void setBytea(@Positive int index, ByteStreamWriter writer) throws SQLException {
      this.bind(index, writer, 17, (byte)4);
   }

   public void setText(@Positive int index, InputStream stream) throws SQLException {
      this.bind(index, new StreamWrapper(stream), 25, (byte)0);
   }

   public void setNull(@Positive int index, int oid) throws SQLException {
      byte binaryTransfer = 0;
      if (this.transferModeRegistry != null && this.transferModeRegistry.useBinaryForReceive(oid)) {
         binaryTransfer = 4;
      }

      this.bind(index, NULL_OBJECT, oid, binaryTransfer);
   }

   private static String quoteAndCast(String text, @Nullable String type, boolean standardConformingStrings) {
      StringBuilder sb = new StringBuilder((text.length() + 10) / 10 * 11);
      sb.append("('");

      try {
         Utils.escapeLiteral(sb, text, standardConformingStrings);
      } catch (SQLException var5) {
         sb.append('\u0000');
      }

      sb.append("'");
      if (type != null) {
         sb.append("::");
         sb.append(type);
      }

      sb.append(")");
      return sb.toString();
   }

   public String toString(@Positive int index, boolean standardConformingStrings) {
      --index;
      Object paramValue = this.paramValues[index];
      if (paramValue == null) {
         return "?";
      } else if (paramValue == NULL_OBJECT) {
         return "(NULL)";
      } else {
         String textValue;
         String type;
         if ((this.flags[index] & 4) == 4) {
            switch(this.paramTypes[index]) {
            case 20:
               long l = ByteConverter.int8((byte[])paramValue, 0);
               textValue = Long.toString(l);
               type = "int8";
               break;
            case 21:
               short s = ByteConverter.int2((byte[])paramValue, 0);
               textValue = Short.toString(s);
               type = "int2";
               break;
            case 23:
               int i = ByteConverter.int4((byte[])paramValue, 0);
               textValue = Integer.toString(i);
               type = "int4";
               break;
            case 600:
               PGpoint pgPoint = new PGpoint();
               pgPoint.setByteValue((byte[])paramValue, 0);
               textValue = pgPoint.toString();
               type = "point";
               break;
            case 603:
               PGbox pgBox = new PGbox();
               pgBox.setByteValue((byte[])paramValue, 0);
               textValue = pgBox.toString();
               type = "box";
               break;
            case 700:
               float f = ByteConverter.float4((byte[])paramValue, 0);
               if (Float.isNaN(f)) {
                  return "('NaN'::real)";
               }

               textValue = Float.toString(f);
               type = "real";
               break;
            case 701:
               double d = ByteConverter.float8((byte[])paramValue, 0);
               if (Double.isNaN(d)) {
                  return "('NaN'::double precision)";
               }

               textValue = Double.toString(d);
               type = "double precision";
               break;
            case 1700:
               Number n = ByteConverter.numeric((byte[])paramValue);
               if (n instanceof Double) {
                  assert ((Double)n).isNaN();

                  return "('NaN'::numeric)";
               }

               textValue = n.toString();
               type = "numeric";
               break;
            case 2950:
               textValue = (new UUIDArrayAssistant()).buildElement((byte[])paramValue, 0, 16).toString();
               type = "uuid";
               break;
            default:
               return "?";
            }
         } else {
            textValue = paramValue.toString();
            switch(this.paramTypes[index]) {
            case 16:
               type = "boolean";
               break;
            case 20:
               type = "int8";
               break;
            case 21:
               type = "int2";
               break;
            case 23:
               type = "int4";
               break;
            case 600:
               type = "point";
               break;
            case 603:
               type = "box";
               break;
            case 700:
               type = "real";
               break;
            case 701:
               type = "double precision";
               break;
            case 1082:
               type = "date";
               break;
            case 1083:
               type = "time";
               break;
            case 1114:
               type = "timestamp";
               break;
            case 1184:
               type = "timestamp with time zone";
               break;
            case 1186:
               type = "interval";
               break;
            case 1266:
               type = "time with time zone";
               break;
            case 1700:
               type = "numeric";
               break;
            case 2950:
               type = "uuid";
               break;
            default:
               type = null;
            }
         }

         return quoteAndCast(textValue, type, standardConformingStrings);
      }
   }

   public void checkAllParametersSet() throws SQLException {
      for(int i = 0; i < this.paramTypes.length; ++i) {
         if (this.direction(i) != 2 && this.paramValues[i] == null) {
            throw new PSQLException(GT.tr("No value specified for parameter {0}.", i + 1), PSQLState.INVALID_PARAMETER_VALUE);
         }
      }

   }

   public void convertFunctionOutParameters() {
      for(int i = 0; i < this.paramTypes.length; ++i) {
         if (this.direction(i) == 2) {
            this.paramTypes[i] = 2278;
            this.paramValues[i] = NULL_OBJECT;
         }
      }

   }

   private static void streamBytea(PGStream pgStream, StreamWrapper wrapper) throws IOException {
      byte[] rawData = wrapper.getBytes();
      if (rawData != null) {
         pgStream.send(rawData, wrapper.getOffset(), wrapper.getLength());
      } else {
         pgStream.sendStream(wrapper.getStream(), wrapper.getLength());
      }
   }

   private static void streamBytea(PGStream pgStream, ByteStreamWriter writer) throws IOException {
      pgStream.send(writer);
   }

   public int[] getTypeOIDs() {
      return this.paramTypes;
   }

   int getTypeOID(@Positive int index) {
      return this.paramTypes[index - 1];
   }

   boolean hasUnresolvedTypes() {
      int[] var1 = this.paramTypes;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int paramType = var1[var3];
         if (paramType == 0) {
            return true;
         }
      }

      return false;
   }

   void setResolvedType(@Positive int index, int oid) {
      if (this.paramTypes[index - 1] != 0 && this.paramTypes[index - 1] != 2278) {
         if (this.paramTypes[index - 1] != oid) {
            throw new IllegalArgumentException("Can't change resolved type for param: " + index + " from " + this.paramTypes[index - 1] + " to " + oid);
         }
      } else {
         this.paramTypes[index - 1] = oid;
      }

   }

   boolean isNull(@Positive int index) {
      return this.paramValues[index - 1] == NULL_OBJECT;
   }

   boolean isBinary(@Positive int index) {
      return (this.flags[index - 1] & 4) != 0;
   }

   private byte direction(@Positive int index) {
      return (byte)(this.flags[index] & 3);
   }

   int getV3Length(@Positive int index) {
      --index;
      Object value = this.paramValues[index];
      if (value != null && value != NULL_OBJECT) {
         if (value instanceof byte[]) {
            return ((byte[])value).length;
         } else if (value instanceof StreamWrapper) {
            return ((StreamWrapper)value).getLength();
         } else if (value instanceof ByteStreamWriter) {
            return ((ByteStreamWriter)value).getLength();
         } else {
            byte[] encoded = this.encoded[index];
            if (encoded == null) {
               this.encoded[index] = encoded = value.toString().getBytes(StandardCharsets.UTF_8);
            }

            return encoded.length;
         }
      } else {
         throw new IllegalArgumentException("can't getV3Length() on a null parameter");
      }
   }

   void writeV3Value(@Positive int index, PGStream pgStream) throws IOException {
      --index;
      Object paramValue = this.paramValues[index];
      if (paramValue != null && paramValue != NULL_OBJECT) {
         if (paramValue instanceof byte[]) {
            pgStream.send((byte[])paramValue);
         } else if (paramValue instanceof StreamWrapper) {
            StreamWrapper streamWrapper = (StreamWrapper)paramValue;

            try {
               streamBytea(pgStream, streamWrapper);
            } catch (Throwable var8) {
               if (streamWrapper != null) {
                  try {
                     streamWrapper.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (streamWrapper != null) {
               streamWrapper.close();
            }

         } else if (paramValue instanceof ByteStreamWriter) {
            streamBytea(pgStream, (ByteStreamWriter)paramValue);
         } else {
            if (this.encoded[index] == null) {
               this.encoded[index] = ((String)paramValue).getBytes(StandardCharsets.UTF_8);
            }

            pgStream.send(this.encoded[index]);
         }
      } else {
         throw new IllegalArgumentException("can't writeV3Value() on a null parameter");
      }
   }

   public ParameterList copy() {
      SimpleParameterList newCopy = new SimpleParameterList(this.paramValues.length, this.transferModeRegistry);
      System.arraycopy(this.paramValues, 0, newCopy.paramValues, 0, this.paramValues.length);
      System.arraycopy(this.paramTypes, 0, newCopy.paramTypes, 0, this.paramTypes.length);
      System.arraycopy(this.flags, 0, newCopy.flags, 0, this.flags.length);
      newCopy.pos = this.pos;
      return newCopy;
   }

   public void clear() {
      Arrays.fill(this.paramValues, (Object)null);
      Arrays.fill(this.paramTypes, 0);
      Arrays.fill(this.encoded, (Object)null);
      Arrays.fill(this.flags, (byte)0);
      this.pos = 0;
   }

   @Nullable
   public SimpleParameterList[] getSubparams() {
      return null;
   }

   public Object[] getValues() {
      return this.paramValues;
   }

   public int[] getParamTypes() {
      return this.paramTypes;
   }

   public byte[] getFlags() {
      return this.flags;
   }

   public byte[][] getEncoding() {
      return this.encoded;
   }

   public void appendAll(ParameterList list) throws SQLException {
      if (list instanceof SimpleParameterList) {
         SimpleParameterList spl = (SimpleParameterList)list;
         int inParamCount = spl.getInParameterCount();
         if (this.pos + inParamCount > this.paramValues.length) {
            throw new PSQLException(GT.tr("Added parameters index out of range: {0}, number of columns: {1}.", this.pos + inParamCount, this.paramValues.length), PSQLState.INVALID_PARAMETER_VALUE);
         }

         System.arraycopy(spl.getValues(), 0, this.paramValues, this.pos, inParamCount);
         System.arraycopy(spl.getParamTypes(), 0, this.paramTypes, this.pos, inParamCount);
         System.arraycopy(spl.getFlags(), 0, this.flags, this.pos, inParamCount);
         System.arraycopy(spl.getEncoding(), 0, this.encoded, this.pos, inParamCount);
         this.pos += inParamCount;
      }

   }

   public String toString() {
      StringBuilder ts = new StringBuilder("<[");
      if (this.paramValues.length > 0) {
         ts.append(this.toString(1, true));

         for(int c = 2; c <= this.paramValues.length; ++c) {
            ts.append(" ,").append(this.toString(c, true));
         }
      }

      ts.append("]>");
      return ts.toString();
   }
}
