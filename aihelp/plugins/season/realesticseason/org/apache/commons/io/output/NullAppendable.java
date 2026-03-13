package org.apache.commons.io.output;

public class NullAppendable implements Appendable {
   public static final NullAppendable INSTANCE = new NullAppendable();

   private NullAppendable() {
   }

   public Appendable append(char var1) {
      return this;
   }

   public Appendable append(CharSequence var1) {
      return this;
   }

   public Appendable append(CharSequence var1, int var2, int var3) {
      return this;
   }
}
