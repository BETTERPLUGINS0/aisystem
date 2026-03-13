package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import com.volmit.iris.util.matter.slices.container.JigsawStructuresContainer;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class JigsawStructuresMatter extends RawMatter<JigsawStructuresContainer> {
   public JigsawStructuresMatter() {
      this(1, 1, 1);
   }

   public JigsawStructuresMatter(int width, int height, int depth) {
      super(var1, var2, var3, JigsawStructuresContainer.class);
   }

   public Palette<JigsawStructuresContainer> getGlobalPalette() {
      return null;
   }

   public void writeNode(JigsawStructuresContainer b, DataOutputStream dos) {
      var1.write(var2);
   }

   public JigsawStructuresContainer readNode(DataInputStream din) {
      return new JigsawStructuresContainer(var1);
   }
}
