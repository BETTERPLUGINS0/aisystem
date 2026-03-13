package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.MatterStructurePOI;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class StructurePOIMatter extends RawMatter<MatterStructurePOI> {
   public StructurePOIMatter() {
      super(1, 1, 1, MatterStructurePOI.class);
   }

   public Palette<MatterStructurePOI> getGlobalPalette() {
      return null;
   }

   public void writeNode(MatterStructurePOI b, DataOutputStream dos) {
      var2.writeUTF(var1.getType());
   }

   public MatterStructurePOI readNode(DataInputStream din) {
      return MatterStructurePOI.get(var1.readUTF());
   }
}
