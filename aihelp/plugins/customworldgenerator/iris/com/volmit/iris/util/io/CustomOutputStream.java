package com.volmit.iris.util.io;

import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class CustomOutputStream extends GZIPOutputStream {
   public CustomOutputStream(OutputStream out, int level) {
      super(var1);
      this.def.setLevel(var2);
   }
}
