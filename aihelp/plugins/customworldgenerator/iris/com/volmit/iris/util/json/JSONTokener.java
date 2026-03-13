package com.volmit.iris.util.json;

import com.volmit.iris.Iris;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
   private final Reader reader;
   private long character;
   private boolean eof;
   private long index;
   private long line;
   private char previous;
   private boolean usePrevious;

   public JSONTokener(Reader reader) {
      this.reader = (Reader)(var1.markSupported() ? var1 : new BufferedReader(var1));
      this.eof = false;
      this.usePrevious = false;
      this.previous = 0;
      this.index = 0L;
      this.character = 1L;
      this.line = 1L;
   }

   public JSONTokener(InputStream inputStream) {
      this((Reader)(new InputStreamReader(var1)));
   }

   public JSONTokener(String s) {
      this((Reader)(new StringReader(var1)));
   }

   public static int dehexchar(char c) {
      if (var0 >= '0' && var0 <= '9') {
         return var0 - 48;
      } else if (var0 >= 'A' && var0 <= 'F') {
         return var0 - 55;
      } else {
         return var0 >= 'a' && var0 <= 'f' ? var0 - 87 : -1;
      }
   }

   public void back() {
      if (!this.usePrevious && this.index > 0L) {
         --this.index;
         --this.character;
         this.usePrevious = true;
         this.eof = false;
      } else {
         throw new JSONException("Stepping back two steps is not supported");
      }
   }

   public boolean end() {
      return this.eof && !this.usePrevious;
   }

   public boolean more() {
      this.next();
      if (this.end()) {
         return false;
      } else {
         this.back();
         return true;
      }
   }

   public char next() {
      int var1;
      if (this.usePrevious) {
         this.usePrevious = false;
         var1 = this.previous;
      } else {
         try {
            var1 = this.reader.read();
         } catch (IOException var3) {
            Iris.reportError(var3);
            throw new JSONException(var3);
         }

         if (var1 <= 0) {
            this.eof = true;
            var1 = 0;
         }
      }

      ++this.index;
      if (this.previous == '\r') {
         ++this.line;
         this.character = var1 == 10 ? 0L : 1L;
      } else if (var1 == 10) {
         ++this.line;
         this.character = 0L;
      } else {
         ++this.character;
      }

      this.previous = (char)var1;
      return this.previous;
   }

   public char next(char c) {
      char var2 = this.next();
      if (var2 != var1) {
         throw this.syntaxError("Expected '" + var1 + "' and instead saw '" + var2 + "'");
      } else {
         return var2;
      }
   }

   public String next(int n) {
      if (var1 == 0) {
         return "";
      } else {
         char[] var2 = new char[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = this.next();
            if (this.end()) {
               throw this.syntaxError("Substring bounds error");
            }
         }

         return new String(var2);
      }
   }

   public char nextClean() {
      char var1;
      do {
         var1 = this.next();
      } while(var1 != 0 && var1 <= ' ');

      return var1;
   }

   public String nextString(char quote) {
      StringBuilder var3 = new StringBuilder();

      while(true) {
         char var2 = this.next();
         switch(var2) {
         case '\u0000':
         case '\n':
         case '\r':
            throw this.syntaxError("Unterminated string");
         case '\\':
            var2 = this.next();
            switch(var2) {
            case '"':
            case '\'':
            case '/':
            case '\\':
               var3.append(var2);
               continue;
            case 'b':
               var3.append('\b');
               continue;
            case 'f':
               var3.append('\f');
               continue;
            case 'n':
               var3.append('\n');
               continue;
            case 'r':
               var3.append('\r');
               continue;
            case 't':
               var3.append('\t');
               continue;
            case 'u':
               var3.append((char)Integer.parseInt(this.next((int)4), 16));
               continue;
            default:
               throw this.syntaxError("Illegal escape.");
            }
         default:
            if (var2 == var1) {
               return var3.toString();
            }

            var3.append(var2);
         }
      }
   }

   public String nextTo(char delimiter) {
      StringBuilder var2 = new StringBuilder();

      while(true) {
         char var3 = this.next();
         if (var3 == var1 || var3 == 0 || var3 == '\n' || var3 == '\r') {
            if (var3 != 0) {
               this.back();
            }

            return var2.toString().trim();
         }

         var2.append(var3);
      }
   }

   public String nextTo(String delimiters) {
      StringBuilder var3 = new StringBuilder();

      while(true) {
         char var2 = this.next();
         if (var1.indexOf(var2) >= 0 || var2 == 0 || var2 == '\n' || var2 == '\r') {
            if (var2 != 0) {
               this.back();
            }

            return var3.toString().trim();
         }

         var3.append(var2);
      }
   }

   public Object nextValue() {
      char var1 = this.nextClean();
      switch(var1) {
      case '"':
      case '\'':
         return this.nextString(var1);
      case '[':
         this.back();
         return new JSONArray(this);
      case '{':
         this.back();
         return new JSONObject(this);
      default:
         StringBuilder var3;
         for(var3 = new StringBuilder(); var1 >= ' ' && ",:]}/\\\"[{;=#".indexOf(var1) < 0; var1 = this.next()) {
            var3.append(var1);
         }

         this.back();
         String var2 = var3.toString().trim();
         if ("".equals(var2)) {
            throw this.syntaxError("Missing value");
         } else {
            return JSONObject.stringToValue(var2);
         }
      }
   }

   public char skipTo(char to) {
      char var2;
      try {
         long var3 = this.index;
         long var5 = this.character;
         long var7 = this.line;
         this.reader.mark(1000000);

         do {
            var2 = this.next();
            if (var2 == 0) {
               this.reader.reset();
               this.index = var3;
               this.character = var5;
               this.line = var7;
               return var2;
            }
         } while(var2 != var1);
      } catch (IOException var9) {
         Iris.reportError(var9);
         throw new JSONException(var9);
      }

      this.back();
      return var2;
   }

   public JSONException syntaxError(String message) {
      return new JSONException(var1 + String.valueOf(this));
   }

   public String toString() {
      return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
   }
}
