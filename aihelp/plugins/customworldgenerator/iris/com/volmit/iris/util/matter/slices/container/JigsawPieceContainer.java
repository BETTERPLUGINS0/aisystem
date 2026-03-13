package com.volmit.iris.util.matter.slices.container;

import com.volmit.iris.engine.object.IrisJigsawPiece;

public class JigsawPieceContainer extends RegistrantContainer<IrisJigsawPiece> {
   public JigsawPieceContainer(String loadKey) {
      super(IrisJigsawPiece.class, var1);
   }

   public static JigsawPieceContainer toContainer(IrisJigsawPiece piece) {
      return new JigsawPieceContainer(var0.getLoadKey());
   }
}
