package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import com.volmit.iris.util.matter.slices.container.JigsawPieceContainer;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class JigsawPieceMatter extends RawMatter<JigsawPieceContainer> {
   public JigsawPieceMatter() {
      this(1, 1, 1);
   }

   public JigsawPieceMatter(int width, int height, int depth) {
      super(var1, var2, var3, JigsawPieceContainer.class);
   }

   public Palette<JigsawPieceContainer> getGlobalPalette() {
      return null;
   }

   public void writeNode(JigsawPieceContainer b, DataOutputStream dos) {
      var2.writeUTF(var1.getLoadKey());
   }

   public JigsawPieceContainer readNode(DataInputStream din) {
      return new JigsawPieceContainer(var1.readUTF());
   }
}
