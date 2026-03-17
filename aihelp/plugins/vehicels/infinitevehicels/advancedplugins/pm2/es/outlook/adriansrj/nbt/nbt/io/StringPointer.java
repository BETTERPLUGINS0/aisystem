package es.outlook.adriansrj.nbt.nbt.io;

public class StringPointer {
   private String value;
   private int index;

   public StringPointer(String var1) {
      this.value = var1;
   }

   public int getIndex() {
      return this.index;
   }

   public int size() {
      return this.value.length();
   }

   public String parseSimpleString() {
      int var1;
      for(var1 = this.index; this.hasNext() && isSimpleChar(this.currentChar()); ++this.index) {
      }

      return this.value.substring(var1, this.index);
   }

   public String parseQuotedString() {
      int var1 = ++this.index;
      StringBuilder var2 = null;
      boolean var3 = false;

      while(true) {
         while(this.hasNext()) {
            char var4 = this.next();
            if (var3) {
               if (var4 != '\\' && var4 != '"') {
                  throw this.parseException("invalid escape of '" + var4 + "'");
               }

               var3 = false;
            } else {
               if (var4 == '\\') {
                  var3 = true;
                  if (var2 == null) {
                     var2 = new StringBuilder(this.value.substring(var1, this.index - 1));
                  }
                  continue;
               }

               if (var4 == '"') {
                  return var2 == null ? this.value.substring(var1, this.index - 1) : var2.toString();
               }
            }

            if (var2 != null) {
               var2.append(var4);
            }
         }

         throw this.parseException("missing end quote");
      }
   }

   public boolean nextArrayElement() {
      this.skipWhitespace();
      if (this.hasNext() && this.currentChar() == ',') {
         ++this.index;
         this.skipWhitespace();
         return true;
      } else {
         return false;
      }
   }

   public void expectChar(char var1) {
      this.skipWhitespace();
      boolean var2 = this.hasNext();
      if (var2 && this.currentChar() == var1) {
         ++this.index;
      } else {
         throw this.parseException("expected '" + var1 + "' but got " + (var2 ? "'" + this.currentChar() + "'" : "EOF"));
      }
   }

   public void skipWhitespace() {
      while(this.hasNext() && Character.isWhitespace(this.currentChar())) {
         ++this.index;
      }

   }

   public boolean hasNext() {
      return this.index < this.value.length();
   }

   public boolean hasCharsLeft(int var1) {
      return this.index + var1 < this.value.length();
   }

   public char currentChar() {
      return this.value.charAt(this.index);
   }

   public char next() {
      return this.value.charAt(this.index++);
   }

   public void skip(int var1) {
      this.index += var1;
   }

   public char lookAhead(int var1) {
      return this.value.charAt(this.index + var1);
   }

   private static boolean isSimpleChar(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z' || var0 >= '0' && var0 <= '9' || var0 == '-' || var0 == '+' || var0 == '.' || var0 == '_';
   }

   public ParseException parseException(String var1) {
      return new ParseException(var1, this.value, this.index);
   }
}
