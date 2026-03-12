package fr.xephi.authme.libs.com.google.gson.internal.sql;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonSyntaxException;
import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.TypeAdapterFactory;
import fr.xephi.authme.libs.com.google.gson.reflect.TypeToken;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonToken;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class SqlTimeTypeAdapter extends TypeAdapter<Time> {
   static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         return typeToken.getRawType() == Time.class ? new SqlTimeTypeAdapter() : null;
      }
   };
   private final DateFormat format;

   private SqlTimeTypeAdapter() {
      this.format = new SimpleDateFormat("hh:mm:ss a");
   }

   public Time read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         String s = in.nextString();

         try {
            synchronized(this) {
               Date date = this.format.parse(s);
               return new Time(date.getTime());
            }
         } catch (ParseException var7) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as SQL Time; at path " + in.getPreviousPath(), var7);
         }
      }
   }

   public void write(JsonWriter out, Time value) throws IOException {
      if (value == null) {
         out.nullValue();
      } else {
         String timeString;
         synchronized(this) {
            timeString = this.format.format(value);
         }

         out.value(timeString);
      }
   }

   // $FF: synthetic method
   SqlTimeTypeAdapter(Object x0) {
      this();
   }
}
