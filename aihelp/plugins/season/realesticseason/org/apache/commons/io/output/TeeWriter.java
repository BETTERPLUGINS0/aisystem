package org.apache.commons.io.output;

import java.io.Writer;
import java.util.Collection;

public class TeeWriter extends ProxyCollectionWriter {
   public TeeWriter(Collection<Writer> var1) {
      super(var1);
   }

   public TeeWriter(Writer... var1) {
      super(var1);
   }
}
