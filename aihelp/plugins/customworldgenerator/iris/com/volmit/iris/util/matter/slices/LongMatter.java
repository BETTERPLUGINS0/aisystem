package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class LongMatter extends RawMatter<Long> {
   public LongMatter() {
      this(1, 1, 1);
   }

   public LongMatter(int width, int height, int depth) {
      super(var1, var2, var3, Long.class);
   }

   public Palette<Long> getGlobalPalette() {
      return null;
   }

   public void writeNode(Long b, DataOutputStream dos) {
      Varint.writeSignedVarLong(var1, var2);
   }

   public Long readNode(DataInputStream din) {
      return Varint.readSignedVarLong(var1);
   }
}
