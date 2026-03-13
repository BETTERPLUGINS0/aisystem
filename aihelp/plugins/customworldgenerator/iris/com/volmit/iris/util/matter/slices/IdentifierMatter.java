package com.volmit.iris.util.matter.slices;

import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class IdentifierMatter extends RawMatter<Identifier> {
   public IdentifierMatter() {
      this(1, 1, 1);
   }

   public IdentifierMatter(int width, int height, int depth) {
      super(var1, var2, var3, Identifier.class);
   }

   public Palette<Identifier> getGlobalPalette() {
      return null;
   }

   public void writeNode(Identifier b, DataOutputStream dos) {
      var2.writeUTF(var1.toString());
   }

   public Identifier readNode(DataInputStream din) {
      return Identifier.fromString(var1.readUTF());
   }
}
