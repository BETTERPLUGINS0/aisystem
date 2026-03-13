package com.volmit.iris.util.mantle.flag;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class MantleFlagAdapter extends TypeAdapter<MantleFlag> {
   private static final String CUSTOM = "CUSTOM:";
   private static final int CUSTOM_LENGTH = "CUSTOM:".length();

   public void write(JsonWriter out, MantleFlag value) {
      if (var2 == null) {
         var1.nullValue();
      } else {
         var1.value(var2.toString());
      }

   }

   public MantleFlag read(JsonReader in) {
      if (var1.peek() == JsonToken.NULL) {
         var1.nextNull();
         return null;
      } else {
         String var2 = var1.nextString();
         return (MantleFlag)(var2.startsWith("CUSTOM:") && var2.length() > CUSTOM_LENGTH ? MantleFlag.of(Integer.parseInt(var2.substring(CUSTOM_LENGTH))) : ReservedFlag.valueOf(var2));
      }
   }
}
