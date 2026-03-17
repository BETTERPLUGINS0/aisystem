package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.IOException;

public interface NBTInput {
   NamedTag readTag(int var1) throws IOException;

   Tag<?> readRawTag(int var1) throws IOException;
}
