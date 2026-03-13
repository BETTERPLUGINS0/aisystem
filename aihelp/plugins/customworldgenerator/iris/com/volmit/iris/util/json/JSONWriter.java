package com.volmit.iris.util.json;

import com.volmit.iris.Iris;
import java.io.IOException;
import java.io.Writer;

public class JSONWriter {
   private static final int maxdepth = 200;
   protected final Writer writer;
   private final JSONObject[] stack = new JSONObject[200];
   protected char mode = 'i';
   private boolean comma = false;
   private int top = 0;

   public JSONWriter(Writer w) {
      this.writer = var1;
   }

   private JSONWriter append(String string) {
      if (var1 == null) {
         throw new JSONException("Null pointer");
      } else if (this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Value out of sequence.");
      } else {
         try {
            if (this.comma && this.mode == 'a') {
               this.writer.write(44);
            }

            this.writer.write(var1);
         } catch (IOException var3) {
            Iris.reportError(var3);
            throw new JSONException(var3);
         }

         if (this.mode == 'o') {
            this.mode = 'k';
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter array() {
      if (this.mode != 'i' && this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Misplaced array.");
      } else {
         this.push((JSONObject)null);
         this.append("[");
         this.comma = false;
         return this;
      }
   }

   private JSONWriter end(char mode, char c) {
      if (this.mode != var1) {
         throw new JSONException(var1 == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
      } else {
         this.pop(var1);

         try {
            this.writer.write(var2);
         } catch (IOException var4) {
            Iris.reportError(var4);
            throw new JSONException(var4);
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter endArray() {
      return this.end('a', ']');
   }

   public JSONWriter endObject() {
      return this.end('k', '}');
   }

   public JSONWriter key(String string) {
      if (var1 == null) {
         throw new JSONException("Null key.");
      } else if (this.mode == 'k') {
         try {
            this.stack[this.top - 1].putOnce(var1, Boolean.TRUE);
            if (this.comma) {
               this.writer.write(44);
            }

            this.writer.write(JSONObject.quote(var1));
            this.writer.write(58);
            this.comma = false;
            this.mode = 'o';
            return this;
         } catch (IOException var3) {
            Iris.reportError(var3);
            throw new JSONException(var3);
         }
      } else {
         throw new JSONException("Misplaced key.");
      }
   }

   public JSONWriter object() {
      if (this.mode == 'i') {
         this.mode = 'o';
      }

      if (this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Misplaced object.");
      } else {
         this.append("{");
         this.push(new JSONObject());
         this.comma = false;
         return this;
      }
   }

   private void pop(char c) {
      if (this.top <= 0) {
         throw new JSONException("Nesting error.");
      } else {
         int var2 = this.stack[this.top - 1] == null ? 97 : 107;
         if (var2 != var1) {
            throw new JSONException("Nesting error.");
         } else {
            --this.top;
            this.mode = (char)(this.top == 0 ? 100 : (this.stack[this.top - 1] == null ? 97 : 107));
         }
      }
   }

   private void push(JSONObject jo) {
      if (this.top >= 200) {
         throw new JSONException("Nesting too deep.");
      } else {
         this.stack[this.top] = var1;
         this.mode = (char)(var1 == null ? 97 : 107);
         ++this.top;
      }
   }

   public JSONWriter value(boolean b) {
      return this.append(var1 ? "true" : "false");
   }

   public JSONWriter value(double d) {
      return this.value(var1);
   }

   public JSONWriter value(long l) {
      return this.append(Long.toString(var1));
   }

   public JSONWriter value(Object object) {
      return this.append(JSONObject.valueToString(var1));
   }
}
