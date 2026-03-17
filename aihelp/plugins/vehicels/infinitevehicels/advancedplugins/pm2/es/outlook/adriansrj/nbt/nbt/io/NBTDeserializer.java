package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.Deserializer;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class NBTDeserializer implements Deserializer<NamedTag> {
   private boolean compressed;
   private boolean littleEndian;

   public NBTDeserializer() {
      this(true);
   }

   public NBTDeserializer(boolean var1) {
      this.compressed = var1;
   }

   public NBTDeserializer(boolean var1, boolean var2) {
      this.compressed = var1;
      this.littleEndian = var2;
   }

   public NamedTag fromStream(InputStream var1) {
      Object var3;
      if (this.compressed) {
         var3 = new GZIPInputStream(var1);
      } else {
         var3 = var1;
      }

      Object var2;
      if (this.littleEndian) {
         var2 = new LittleEndianNBTInputStream((InputStream)var3);
      } else {
         var2 = new NBTInputStream((InputStream)var3);
      }

      return ((NBTInput)var2).readTag(512);
   }
}
