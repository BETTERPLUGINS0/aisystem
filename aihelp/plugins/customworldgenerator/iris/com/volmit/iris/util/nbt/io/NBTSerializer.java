package com.volmit.iris.util.nbt.io;

import com.volmit.iris.engine.data.io.Serializer;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class NBTSerializer implements Serializer<NamedTag> {
   private final boolean compressed;

   public NBTSerializer() {
      this(true);
   }

   public NBTSerializer(boolean compressed) {
      this.compressed = var1;
   }

   public void toStream(NamedTag object, OutputStream out) {
      NBTOutputStream var3;
      if (this.compressed) {
         var3 = new NBTOutputStream(new GZIPOutputStream(var2, true));
      } else {
         var3 = new NBTOutputStream(var2);
      }

      var3.writeTag((NamedTag)var1, 512);
      var3.flush();
   }
}
