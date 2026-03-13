package com.volmit.iris.util.nbt.io;

import com.volmit.iris.engine.data.io.StringSerializer;
import com.volmit.iris.util.nbt.tag.Tag;
import java.io.Writer;

public class SNBTSerializer implements StringSerializer<Tag<?>> {
   public void toWriter(Tag<?> tag, Writer writer) {
      SNBTWriter.write(var1, var2);
   }

   public void toWriter(Tag<?> tag, Writer writer, int maxDepth) {
      SNBTWriter.write(var1, var2, var3);
   }
}
