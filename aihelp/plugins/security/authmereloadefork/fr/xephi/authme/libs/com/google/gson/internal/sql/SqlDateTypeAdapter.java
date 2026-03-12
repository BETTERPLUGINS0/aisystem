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
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

final class SqlDateTypeAdapter extends TypeAdapter<Date> {
   static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         return typeToken.getRawType() == Date.class ? new SqlDateTypeAdapter() : null;
      }
   };
   private final DateFormat format;

   private SqlDateTypeAdapter() {
      this.format = new SimpleDateFormat("MMM d, yyyy");
   }

   public Date read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         String s = in.nextString();

         try {
            java.util.Date utilDate;
            synchronized(this) {
               utilDate = this.format.parse(s);
            }

            return new Date(utilDate.getTime());
         } catch (ParseException var7) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as SQL Date; at path " + in.getPreviousPath(), var7);
         }
      }
   }

   public void write(JsonWriter out, Date value) throws IOException {
      if (value == null) {
         out.nullValue();
      } else {
         String dateString;
         synchronized(this) {
            dateString = this.format.format(value);
         }

         out.value(dateString);
      }
   }

   // $FF: synthetic method
   SqlDateTypeAdapter(Object x0) {
      this();
   }
}
