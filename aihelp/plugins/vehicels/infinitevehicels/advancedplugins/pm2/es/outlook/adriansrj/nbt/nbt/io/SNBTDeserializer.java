package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.StringDeserializer;
import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Collectors;

public class SNBTDeserializer implements StringDeserializer<Tag<?>> {
   public Tag<?> fromReader(Reader var1) {
      return this.fromReader(var1, 512);
   }

   public Tag<?> fromReader(Reader var1, int var2) {
      BufferedReader var3;
      if (var1 instanceof BufferedReader) {
         var3 = (BufferedReader)var1;
      } else {
         var3 = new BufferedReader(var1);
      }

      return (new SNBTParser((String)var3.lines().collect(Collectors.joining()))).parse(var2);
   }
}
