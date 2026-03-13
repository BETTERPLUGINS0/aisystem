package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class JigsawPieceHandler extends RegistrantHandler<IrisJigsawPiece> {
   public JigsawPieceHandler() {
      super(IrisJigsawPiece.class, true);
   }

   public String getRandomDefault() {
      return "jigsaw-piece";
   }
}
