package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.Serializer;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class NBTSerializer implements Serializer<NamedTag> {
   private boolean compressed;
   private boolean littleEndian;

   public NBTSerializer() {
      this(true);
   }

   public NBTSerializer(boolean var1) {
      this.compressed = var1;
   }

   public NBTSerializer(boolean var1, boolean var2) {
      this.compressed = var1;
      this.littleEndian = var2;
   }

   public void toStream(NamedTag var1, OutputStream var2) {
      Object var4;
      if (this.compressed) {
         var4 = new GZIPOutputStream(var2, true);
      } else {
         var4 = var2;
      }

      Object var3;
      if (this.littleEndian) {
         var3 = new LittleEndianNBTOutputStream((OutputStream)var4);
      } else {
         var3 = new NBTOutputStream((OutputStream)var4);
      }

      ((NBTOutput)var3).writeTag((NamedTag)var1, 512);
      ((NBTOutput)var3).flush();
   }
}
