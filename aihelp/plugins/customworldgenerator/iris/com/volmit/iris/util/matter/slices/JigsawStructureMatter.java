package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import com.volmit.iris.util.matter.slices.container.JigsawStructureContainer;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class JigsawStructureMatter extends RawMatter<JigsawStructureContainer> {
   public JigsawStructureMatter() {
      this(1, 1, 1);
   }

   public JigsawStructureMatter(int width, int height, int depth) {
      super(var1, var2, var3, JigsawStructureContainer.class);
   }

   public Palette<JigsawStructureContainer> getGlobalPalette() {
      return null;
   }

   public void writeNode(JigsawStructureContainer b, DataOutputStream dos) {
      var2.writeUTF(var1.getLoadKey());
   }

   public JigsawStructureContainer readNode(DataInputStream din) {
      return new JigsawStructureContainer(var1.readUTF());
   }
}
