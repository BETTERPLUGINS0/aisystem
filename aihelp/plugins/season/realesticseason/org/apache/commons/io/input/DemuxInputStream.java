package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class DemuxInputStream extends InputStream {
   private final InheritableThreadLocal<InputStream> inputStreamLocal = new InheritableThreadLocal();

   public InputStream bindStream(InputStream var1) {
      InputStream var2 = (InputStream)this.inputStreamLocal.get();
      this.inputStreamLocal.set(var1);
      return var2;
   }

   public void close() {
      IOUtils.close((Closeable)this.inputStreamLocal.get());
   }

   public int read() {
      InputStream var1 = (InputStream)this.inputStreamLocal.get();
      return null != var1 ? var1.read() : -1;
   }
}
