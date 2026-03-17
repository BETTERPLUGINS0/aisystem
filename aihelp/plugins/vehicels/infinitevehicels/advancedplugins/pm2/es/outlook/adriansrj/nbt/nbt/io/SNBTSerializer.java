package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.StringSerializer;
import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.Writer;

public class SNBTSerializer implements StringSerializer<Tag<?>> {
   public void toWriter(Tag<?> var1, Writer var2) {
      SNBTWriter.write(var1, var2);
   }

   public void toWriter(Tag<?> var1, Writer var2, int var3) {
      SNBTWriter.write(var1, var2, var3);
   }
}
