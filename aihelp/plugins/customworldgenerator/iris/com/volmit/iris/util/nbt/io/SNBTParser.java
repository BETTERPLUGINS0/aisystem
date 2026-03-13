package com.volmit.iris.util.nbt.io;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.io.MaxDepthIO;
import com.volmit.iris.util.nbt.tag.ArrayTag;
import com.volmit.iris.util.nbt.tag.ByteArrayTag;
import com.volmit.iris.util.nbt.tag.ByteTag;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.DoubleTag;
import com.volmit.iris.util.nbt.tag.EndTag;
import com.volmit.iris.util.nbt.tag.FloatTag;
import com.volmit.iris.util.nbt.tag.IntArrayTag;
import com.volmit.iris.util.nbt.tag.IntTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import com.volmit.iris.util.nbt.tag.LongArrayTag;
import com.volmit.iris.util.nbt.tag.LongTag;
import com.volmit.iris.util.nbt.tag.ShortTag;
import com.volmit.iris.util.nbt.tag.StringTag;
import com.volmit.iris.util.nbt.tag.Tag;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class SNBTParser implements MaxDepthIO {
   private static final Pattern FLOAT_LITERAL_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?f$", 2);
   private static final Pattern DOUBLE_LITERAL_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?d$", 2);
   private static final Pattern DOUBLE_LITERAL_NO_SUFFIX_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.|\\d*\\.\\d+)(?:e[-+]?\\d+)?$", 2);
   private static final Pattern BYTE_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+b$", 2);
   private static final Pattern SHORT_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+s$", 2);
   private static final Pattern INT_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+$", 2);
   private static final Pattern LONG_LITERAL_PATTERN = Pattern.compile("^[-+]?\\d+l$", 2);
   private static final Pattern NUMBER_PATTERN = Pattern.compile("^[-+]?\\d+$");
   private final StringPointer ptr;

   private SNBTParser(String string) {
      this.ptr = new StringPointer(var1);
   }

   public static Tag<?> parse(String string, int maxDepth) {
      SNBTParser var2 = new SNBTParser(var0);
      Tag var3 = var2.parseAnything(var1);
      var2.ptr.skipWhitespace();
      if (var2.ptr.hasNext()) {
         throw var2.ptr.parseException("invalid characters after end of snbt");
      } else {
         return var3;
      }
   }

   public static Tag<?> parse(String string) {
      return parse(var0, 512);
   }

   private Tag<?> parseAnything(int maxDepth) {
      this.ptr.skipWhitespace();
      switch(this.ptr.currentChar()) {
      case '[':
         if (this.ptr.hasCharsLeft(2) && this.ptr.lookAhead(1) != '"' && this.ptr.lookAhead(2) == ';') {
            return this.parseNumArray();
         }

         return this.parseListTag(var1);
      case '{':
         return this.parseCompoundTag(var1);
      default:
         return this.parseStringOrLiteral();
      }
   }

   private Tag<?> parseStringOrLiteral() {
      this.ptr.skipWhitespace();
      if (this.ptr.currentChar() == '"') {
         return new StringTag(this.ptr.parseQuotedString());
      } else {
         String var1 = this.ptr.parseSimpleString();
         if (var1.isEmpty()) {
            throw new ParseException("expected non empty value");
         } else if (FLOAT_LITERAL_PATTERN.matcher(var1).matches()) {
            return new FloatTag(Float.parseFloat(var1.substring(0, var1.length() - 1)));
         } else {
            StringPointer var10000;
            String var10001;
            if (BYTE_LITERAL_PATTERN.matcher(var1).matches()) {
               try {
                  return new ByteTag(Byte.parseByte(var1.substring(0, var1.length() - 1)));
               } catch (NumberFormatException var3) {
                  Iris.reportError(var3);
                  var10000 = this.ptr;
                  var10001 = var1.substring(0, var1.length() - 1);
                  throw var10000.parseException("byte not in range: \"" + var10001 + "\"");
               }
            } else if (SHORT_LITERAL_PATTERN.matcher(var1).matches()) {
               try {
                  return new ShortTag(Short.parseShort(var1.substring(0, var1.length() - 1)));
               } catch (NumberFormatException var4) {
                  Iris.reportError(var4);
                  var10000 = this.ptr;
                  var10001 = var1.substring(0, var1.length() - 1);
                  throw var10000.parseException("short not in range: \"" + var10001 + "\"");
               }
            } else if (LONG_LITERAL_PATTERN.matcher(var1).matches()) {
               try {
                  return new LongTag(Long.parseLong(var1.substring(0, var1.length() - 1)));
               } catch (NumberFormatException var5) {
                  Iris.reportError(var5);
                  var10000 = this.ptr;
                  var10001 = var1.substring(0, var1.length() - 1);
                  throw var10000.parseException("long not in range: \"" + var10001 + "\"");
               }
            } else if (INT_LITERAL_PATTERN.matcher(var1).matches()) {
               try {
                  return new IntTag(Integer.parseInt(var1));
               } catch (NumberFormatException var6) {
                  Iris.reportError(var6);
                  var10000 = this.ptr;
                  var10001 = var1.substring(0, var1.length() - 1);
                  throw var10000.parseException("int not in range: \"" + var10001 + "\"");
               }
            } else if (DOUBLE_LITERAL_PATTERN.matcher(var1).matches()) {
               return new DoubleTag(Double.parseDouble(var1.substring(0, var1.length() - 1)));
            } else if (DOUBLE_LITERAL_NO_SUFFIX_PATTERN.matcher(var1).matches()) {
               return new DoubleTag(Double.parseDouble(var1));
            } else if ("true".equalsIgnoreCase(var1)) {
               return new ByteTag(true);
            } else {
               return (Tag)("false".equalsIgnoreCase(var1) ? new ByteTag(false) : new StringTag(var1));
            }
         }
      }
   }

   private CompoundTag parseCompoundTag(int maxDepth) {
      this.ptr.expectChar('{');
      CompoundTag var2 = new CompoundTag();
      this.ptr.skipWhitespace();

      while(this.ptr.hasNext() && this.ptr.currentChar() != '}') {
         this.ptr.skipWhitespace();
         String var3 = this.ptr.currentChar() == '"' ? this.ptr.parseQuotedString() : this.ptr.parseSimpleString();
         if (var3.isEmpty()) {
            throw new ParseException("empty keys are not allowed");
         }

         this.ptr.expectChar(':');
         var2.put(var3, this.parseAnything(this.decrementMaxDepth(var1)));
         if (!this.ptr.nextArrayElement()) {
            break;
         }
      }

      this.ptr.expectChar('}');
      return var2;
   }

   private ListTag<?> parseListTag(int maxDepth) {
      this.ptr.expectChar('[');
      this.ptr.skipWhitespace();
      ListTag var2 = ListTag.createUnchecked(EndTag.class);

      while(this.ptr.currentChar() != ']') {
         Tag var3 = this.parseAnything(this.decrementMaxDepth(var1));

         try {
            var2.addUnchecked(var3);
         } catch (IllegalArgumentException var5) {
            Iris.reportError(var5);
            throw this.ptr.parseException(var5.getMessage());
         }

         if (!this.ptr.nextArrayElement()) {
            break;
         }
      }

      this.ptr.expectChar(']');
      return var2;
   }

   private ArrayTag<?> parseNumArray() {
      this.ptr.expectChar('[');
      char var1 = this.ptr.next();
      this.ptr.expectChar(';');
      this.ptr.skipWhitespace();
      switch(var1) {
      case 'B':
         return this.parseByteArrayTag();
      case 'I':
         return this.parseIntArrayTag();
      case 'L':
         return this.parseLongArrayTag();
      default:
         throw new ParseException("invalid array type '" + var1 + "'");
      }
   }

   private ByteArrayTag parseByteArrayTag() {
      ArrayList var1 = new ArrayList();

      while(true) {
         if (this.ptr.currentChar() != ']') {
            String var2 = this.ptr.parseSimpleString();
            this.ptr.skipWhitespace();
            if (!NUMBER_PATTERN.matcher(var2).matches()) {
               throw this.ptr.parseException("invalid byte in ByteArrayTag: \"" + var2 + "\"");
            }

            try {
               var1.add(Byte.parseByte(var2));
            } catch (NumberFormatException var4) {
               Iris.reportError(var4);
               throw this.ptr.parseException("byte not in range: \"" + var2 + "\"");
            }

            if (this.ptr.nextArrayElement()) {
               continue;
            }
         }

         this.ptr.expectChar(']');
         byte[] var5 = new byte[var1.size()];

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            var5[var3] = (Byte)var1.get(var3);
         }

         return new ByteArrayTag(var5);
      }
   }

   private IntArrayTag parseIntArrayTag() {
      ArrayList var1 = new ArrayList();

      while(true) {
         if (this.ptr.currentChar() != ']') {
            String var2 = this.ptr.parseSimpleString();
            this.ptr.skipWhitespace();
            if (!NUMBER_PATTERN.matcher(var2).matches()) {
               throw this.ptr.parseException("invalid int in IntArrayTag: \"" + var2 + "\"");
            }

            try {
               var1.add(Integer.parseInt(var2));
            } catch (NumberFormatException var4) {
               Iris.reportError(var4);
               throw this.ptr.parseException("int not in range: \"" + var2 + "\"");
            }

            if (this.ptr.nextArrayElement()) {
               continue;
            }
         }

         this.ptr.expectChar(']');
         return new IntArrayTag(var1.stream().mapToInt((var0) -> {
            return var0;
         }).toArray());
      }
   }

   private LongArrayTag parseLongArrayTag() {
      ArrayList var1 = new ArrayList();

      while(true) {
         if (this.ptr.currentChar() != ']') {
            String var2 = this.ptr.parseSimpleString();
            this.ptr.skipWhitespace();
            if (!NUMBER_PATTERN.matcher(var2).matches()) {
               throw this.ptr.parseException("invalid long in LongArrayTag: \"" + var2 + "\"");
            }

            try {
               var1.add(Long.parseLong(var2));
            } catch (NumberFormatException var4) {
               Iris.reportError(var4);
               throw this.ptr.parseException("long not in range: \"" + var2 + "\"");
            }

            if (this.ptr.nextArrayElement()) {
               continue;
            }
         }

         this.ptr.expectChar(']');
         return new LongArrayTag(var1.stream().mapToLong((var0) -> {
            return var0;
         }).toArray());
      }
   }
}
