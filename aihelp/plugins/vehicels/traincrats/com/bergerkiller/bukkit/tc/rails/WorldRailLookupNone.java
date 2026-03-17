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
import java.util.Collections;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;

final class WorldRailLookupNone implements WorldRailLookup {
   public World getWorld() {
      return null;
   }

   public OfflineWorld getOfflineWorld() {
      return OfflineWorld.NONE;
   }

   public MutexZoneCacheWorld getMutexZones() {
      throw new UnsupportedOperationException("World Rail Lookup cache is closed");
   }

   public SignControllerWorld getSignController() {
      throw new UnsupportedOperationException("World Rail Lookup cache is closed");
   }

   public boolean isValid() {
      return false;
   }

   public boolean isValidForWorld(World world) {
      return false;
   }

   public RailPiece[] findAtStatePosition(RailState state) {
      throw new WorldRailLookup.ClosedException();
   }

   public RailPiece[] findAtBlockPosition(OfflineBlock positionBlock) {
      throw new WorldRailLookup.ClosedException();
   }

   public RailLookup.CachedRailPiece lookupCachedRailPieceIfCached(OfflineBlock railOfflineBlock, RailType railType) {
      return RailLookup.CachedRailPiece.NONE;
   }

   public List<RailLookup.CachedRailPiece> lookupCachedRailPieces(OfflineBlock railOfflineBlock) {
      return Collections.emptyList();
   }

   public RailLookup.CachedRailPiece lookupCachedRailPiece(OfflineBlock railOfflineBlock, Block railBlock, RailType railType) {
      throw new WorldRailLookup.ClosedException();
   }

   public List<MinecartMember<?>> findMembersOnRail(IntVector3 railCoordinates) {
      return Collections.emptyList();
   }

   public List<MinecartMember<?>> findMembersOnRail(OfflineBlock railOfflineBlock) {
      return Collections.emptyList();
   }

   public void removeMemberFromAll(MinecartMember<?> member) {
      throw new WorldRailLookup.ClosedException();
   }

   public RailLookup.TrackedSign[] discoverSignsAtRailPiece(RailPiece rail) {
      throw new WorldRailLookup.ClosedException();
   }

   public RailPiece discoverRailPieceFromSign(Block signblock) {
      throw new WorldRailLookup.ClosedException();
   }

   public void redetectSignActions() {
   }

   public void storeDetectorRegions(IntVector3 coordinates, DetectorRegion[] regions) {
      throw new WorldRailLookup.ClosedException();
   }

   public DetectorRegion[] getDetectorRegions(IntVector3 coordinates) {
      throw new WorldRailLookup.ClosedException();
   }

   public Collection<IntVector3> getBlockIndex() {
      return Collections.emptySet();
   }
}
