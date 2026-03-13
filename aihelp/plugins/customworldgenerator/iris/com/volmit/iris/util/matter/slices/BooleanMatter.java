package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class BooleanMatter extends RawMatter<Boolean> {
   public BooleanMatter() {
      this(1, 1, 1);
   }

   public BooleanMatter(int width, int height, int depth) {
      super(var1, var2, var3, Boolean.class);
   }

   public Palette<Boolean> getGlobalPalette() {
      return null;
   }

   public void writeNode(Boolean b, DataOutputStream dos) {
      var2.writeBoolean(var1);
   }

   public Boolean readNode(DataInputStream din) {
      return var1.readBoolean();
   }
}
