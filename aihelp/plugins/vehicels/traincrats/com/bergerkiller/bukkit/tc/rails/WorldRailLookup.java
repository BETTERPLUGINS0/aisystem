package com.bergerkiller.bukkit.tc.rails;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.global.SignControllerWorld;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCacheWorld;
import java.util.Collection;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface WorldRailLookup {
   WorldRailLookup NONE = new WorldRailLookupNone();

   World getWorld();

   OfflineWorld getOfflineWorld();

   MutexZoneCacheWorld getMutexZones();

   SignControllerWorld getSignController();

   boolean isValid();

   boolean isValidForWorld(World var1);

   RailPiece[] findAtStatePosition(RailState var1);

   RailPiece[] findAtBlockPosition(OfflineBlock var1);

   RailLookup.CachedRailPiece lookupCachedRailPieceIfCached(OfflineBlock var1, RailType var2);

   List<RailLookup.CachedRailPiece> lookupCachedRailPieces(OfflineBlock var1);

   RailLookup.CachedRailPiece lookupCachedRailPiece(OfflineBlock var1, Block var2, RailType var3);

   List<MinecartMember<?>> findMembersOnRail(IntVector3 var1);

   List<MinecartMember<?>> findMembersOnRail(OfflineBlock var1);

   void removeMemberFromAll(MinecartMember<?> var1);

   RailLookup.TrackedSign[] discoverSignsAtRailPiece(RailPiece var1);

   RailPiece discoverRailPieceFromSign(Block var1);

   void redetectSignActions();

   void storeDetectorRegions(IntVector3 var1, DetectorRegion[] var2);

   DetectorRegion[] getDetectorRegions(IntVector3 var1);

   Collection<IntVector3> getBlockIndex();

   public static class ClosedException extends IllegalStateException {
      private static final long serialVersionUID = -5457138086475585185L;

      public ClosedException() {
         super("World Rail Lookup cache is closed");
      }
   }
}
