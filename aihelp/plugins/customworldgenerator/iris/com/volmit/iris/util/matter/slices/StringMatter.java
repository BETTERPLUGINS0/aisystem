package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class StringMatter extends RawMatter<String> {
   public StringMatter() {
      this(1, 1, 1);
   }

   public StringMatter(int width, int height, int depth) {
      super(var1, var2, var3, String.class);
   }

   public Palette<String> getGlobalPalette() {
      return null;
   }

   public void writeNode(String b, DataOutputStream dos) {
      var2.writeUTF(var1);
   }

   public String readNode(DataInputStream din) {
      return var1.readUTF();
   }
}
