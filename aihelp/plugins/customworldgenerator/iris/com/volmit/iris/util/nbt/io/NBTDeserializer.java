package com.volmit.iris.util.nbt.io;

import com.volmit.iris.engine.data.io.Deserializer;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class NBTDeserializer implements Deserializer<NamedTag> {
   private final boolean compressed;

   public NBTDeserializer() {
      this(true);
   }

   public NBTDeserializer(boolean compressed) {
      this.compressed = var1;
   }

   public NamedTag fromStream(InputStream stream) {
      NBTInputStream var2;
      if (this.compressed) {
         var2 = new NBTInputStream(new GZIPInputStream(var1));
      } else {
         var2 = new NBTInputStream(var1);
      }

      return var2.readTag(512);
   }
}
