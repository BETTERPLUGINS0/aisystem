package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class IntMatter extends RawMatter<Integer> {
   public IntMatter() {
      this(1, 1, 1);
   }

   public IntMatter(int width, int height, int depth) {
      super(var1, var2, var3, Integer.class);
   }

   public Palette<Integer> getGlobalPalette() {
      return null;
   }

   public void writeNode(Integer b, DataOutputStream dos) {
      Varint.writeSignedVarInt(var1, var2);
   }

   public Integer readNode(DataInputStream din) {
      return Varint.readSignedVarInt((DataInput)var1);
   }
}
