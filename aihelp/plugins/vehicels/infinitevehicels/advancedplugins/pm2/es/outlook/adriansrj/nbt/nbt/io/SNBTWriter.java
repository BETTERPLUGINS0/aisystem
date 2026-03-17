package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.MaxDepthIO;
import es.outlook.adriansrj.nbt.nbt.tag.ByteArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.ByteTag;
import es.outlook.adriansrj.nbt.nbt.tag.CompoundTag;
import es.outlook.adriansrj.nbt.nbt.tag.DoubleTag;
import es.outlook.adriansrj.nbt.nbt.tag.FloatTag;
import es.outlook.adriansrj.nbt.nbt.tag.IntArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.IntTag;
import es.outlook.adriansrj.nbt.nbt.tag.ListTag;
import es.outlook.adriansrj.nbt.nbt.tag.LongArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.LongTag;
import es.outlook.adriansrj.nbt.nbt.tag.ShortTag;
import es.outlook.adriansrj.nbt.nbt.tag.StringTag;
import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class SNBTWriter implements MaxDepthIO {
   private static final Pattern NON_QUOTE_PATTERN = Pattern.compile("[a-zA-Z_.+\\-]+");
   private Writer writer;

   private SNBTWriter(Writer var1) {
      this.writer = var1;
   }

   public static void write(Tag<?> var0, Writer var1, int var2) {
      (new SNBTWriter(var1)).writeAnything(var0, var2);
   }

   public static void write(Tag<?> var0, Writer var1) {
      write(var0, var1, 512);
   }

   private void writeAnything(Tag<?> var1, int var2) {
      switch(var1.getID()) {
      case 0:
         break;
      case 1:
         this.writer.append(Byte.toString(((ByteTag)var1).asByte())).write(98);
         break;
      case 2:
         this.writer.append(Short.toString(((ShortTag)var1).asShort())).write(115);
         break;
      case 3:
         this.writer.write(Integer.toString(((IntTag)var1).asInt()));
         break;
      case 4:
         this.writer.append(Long.toString(((LongTag)var1).asLong())).write(108);
         break;
      case 5:
         this.writer.append(Float.toString(((FloatTag)var1).asFloat())).write(102);
         break;
      case 6:
         this.writer.append(Double.toString(((DoubleTag)var1).asDouble())).write(100);
         break;
      case 7:
         this.writeArray(((ByteArrayTag)var1).getValue(), ((ByteArrayTag)var1).length(), "B");
         break;
      case 8:
         this.writer.write(escapeString(((StringTag)var1).getValue()));
         break;
      case 9:
         this.writer.write(91);

         for(int var6 = 0; var6 < ((ListTag)var1).size(); ++var6) {
            this.writer.write(var6 == 0 ? "" : ",");
            this.writeAnything(((ListTag)var1).get(var6), this.decrementMaxDepth(var2));
         }

         this.writer.write(93);
         break;
      case 10:
         this.writer.write(123);
         boolean var3 = true;

         for(Iterator var4 = ((CompoundTag)var1).iterator(); var4.hasNext(); var3 = false) {
            Entry var5 = (Entry)var4.next();
            this.writer.write(var3 ? "" : ",");
            this.writer.append(escapeString((String)var5.getKey())).write(58);
            this.writeAnything((Tag)var5.getValue(), this.decrementMaxDepth(var2));
         }

         this.writer.write(125);
         break;
      case 11:
         this.writeArray(((IntArrayTag)var1).getValue(), ((IntArrayTag)var1).length(), "I");
         break;
      case 12:
         this.writeArray(((LongArrayTag)var1).getValue(), ((LongArrayTag)var1).length(), "L");
         break;
      default:
         throw new IOException("unknown tag with id \"" + var1.getID() + "\"");
      }

   }

   private void writeArray(Object var1, int var2, String var3) {
      this.writer.append('[').append(var3).write(59);

      for(int var4 = 0; var4 < var2; ++var4) {
         this.writer.append(var4 == 0 ? "" : ",").write(Array.get(var1, var4).toString());
      }

      this.writer.write(93);
   }

   public static String escapeString(String var0) {
      if (NON_QUOTE_PATTERN.matcher(var0).matches()) {
         return var0;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append('"');

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var3 = var0.charAt(var2);
            if (var3 == '\\' || var3 == '"') {
               var1.append('\\');
            }

            var1.append(var3);
         }

         var1.append('"');
         return var1.toString();
      }
   }
}
