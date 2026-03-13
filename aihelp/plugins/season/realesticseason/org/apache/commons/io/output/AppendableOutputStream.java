package org.apache.commons.io.output;

import java.io.OutputStream;

public class AppendableOutputStream<T extends Appendable> extends OutputStream {
   private final T appendable;

   public AppendableOutputStream(T var1) {
      this.appendable = var1;
   }

   public void write(int var1) {
      this.appendable.append((char)var1);
   }

   public T getAppendable() {
      return this.appendable;
   }
}
