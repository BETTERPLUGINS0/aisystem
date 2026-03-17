package com.bergerkiller.bukkit.tc.cache;

import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import org.bukkit.block.Block;

/** @deprecated */
@Deprecated
public class RailSignCache {
   public static RailLookup.TrackedSign[] getSigns(RailType railType, Block railBlock) {
      return getSigns(RailPiece.create(railType, railBlock));
   }

   public static RailLookup.TrackedSign[] getSigns(RailPiece rail) {
      return rail.signs();
   }

   public static RailLookup.TrackedSign[] discoverSigns(RailType railType, Block railBlock) {
      return discoverSigns(RailPiece.create(railType, railBlock));
   }

   public static RailLookup.TrackedSign[] discoverSigns(RailPiece rail) {
      return RailLookup.discoverSignsAtRailPiece(rail);
   }

   public static RailPiece getRailsFromSign(Block signblock) {
      return RailLookup.discoverRailPieceFromSign(signblock);
   }
}
