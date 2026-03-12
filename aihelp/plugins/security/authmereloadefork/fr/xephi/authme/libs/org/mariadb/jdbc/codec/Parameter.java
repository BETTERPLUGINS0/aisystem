package fr.xephi.authme.libs.org.mariadb.jdbc.codec;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.PacketWriter;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Calendar;

public class Parameter<T> implements fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameter {
   public static final Parameter<?> NULL_PARAMETER = new Parameter((Codec)null, (Object)null) {
      public int getBinaryEncodeType() {
         return DataType.VARCHAR.get();
      }

      public boolean isNull() {
         return true;
      }
   };
   protected final Codec<T> codec;
   protected final T value;
   protected final Long length;

   public Parameter(Codec<T> codec, T value) {
      this.codec = codec;
      this.value = value;
      this.length = null;
   }

   public Parameter(Codec<T> codec, T value, Long length) {
      this.codec = codec;
      this.value = value;
      this.length = length;
   }

   public void encodeText(Writer encoder, Context context) throws IOException, SQLException {
      if (this.value == null) {
         encoder.writeAscii("null");
      } else {
         this.codec.encodeText(encoder, context, this.value, (Calendar)null, this.length);
      }

   }

   public void encodeBinary(Writer encoder) throws IOException, SQLException {
      this.codec.encodeBinary(encoder, this.value, (Calendar)null, this.length);
   }

   public void encodeLongData(Writer encoder) throws IOException, SQLException {
      this.codec.encodeLongData(encoder, this.value, this.length);
   }

   public byte[] encodeData() throws IOException, SQLException {
      return this.codec.encodeData(this.value, this.length);
   }

   public boolean canEncodeLongData() {
      return this.codec.canEncodeLongData();
   }

   public int getBinaryEncodeType() {
      return this.codec.getBinaryEncodeType();
   }

   public boolean isNull() {
      return this.value == null;
   }

   public String bestEffortStringValue(Context context) {
      if (this.isNull()) {
         return "null";
      } else if (this.codec.canEncodeLongData()) {
         Type it = this.codec.getClass().getGenericInterfaces()[0];
         ParameterizedType parameterizedType = (ParameterizedType)it;
         Type typeParameter = parameterizedType.getActualTypeArguments()[0];
         return "<" + typeParameter + ">";
      } else {
         try {
            PacketWriter writer = new PacketWriter((OutputStream)null, 0, 16777215, (MutableByte)null, (MutableByte)null);
            this.codec.encodeText(writer, context, this.value, (Calendar)null, this.length);
            return new String(writer.buf(), 4, writer.pos() - 4, StandardCharsets.UTF_8);
         } catch (Throwable var5) {
            return null;
         }
      }
   }
}
