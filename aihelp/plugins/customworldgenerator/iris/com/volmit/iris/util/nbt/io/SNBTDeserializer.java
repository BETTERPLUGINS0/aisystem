package com.volmit.iris.util.nbt.io;

import com.volmit.iris.engine.data.io.StringDeserializer;
import com.volmit.iris.util.nbt.tag.Tag;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Collectors;

public class SNBTDeserializer implements StringDeserializer<Tag<?>> {
   public Tag<?> fromReader(Reader reader) {
      return this.fromReader(var1, 512);
   }

   public Tag<?> fromReader(Reader reader, int maxDepth) {
      BufferedReader var3;
      if (var1 instanceof BufferedReader) {
         var3 = (BufferedReader)var1;
      } else {
         var3 = new BufferedReader(var1);
      }

      return SNBTParser.parse((String)var3.lines().collect(Collectors.joining()), var2);
   }
}
