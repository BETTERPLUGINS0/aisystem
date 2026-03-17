package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.IOException;

public interface NBTOutput {
   void writeTag(NamedTag var1, int var2) throws IOException;

   void writeTag(Tag<?> var1, int var2) throws IOException;

   void flush() throws IOException;
}
