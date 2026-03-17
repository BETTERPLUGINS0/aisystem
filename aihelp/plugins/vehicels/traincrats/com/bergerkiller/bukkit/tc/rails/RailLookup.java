package com.bergerkiller.bukkit.tc.rails;

import com.bergerkiller.bukkit.common.block.SignChangeTracker;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.PowerState;
import com.bergerkiller.bukkit.tc.SignActionHeader;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.global.SignController;
import com.bergerkiller.bukkit.tc.controller.global.SignControllerWorld;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.FakeSign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public final class RailLookup {
   static final int LIFE_TIMER_DELETED = 0;
   static final int LIFE_TIMER_START = 1;
   static int lifeTimer = 1;
   static int lifeTimerAtPosition = 1;
   static int verifyTimer = 1;
   static final DetectorRegion[] NO_DETECTOR_REGIONS = new DetectorRegion[0];
   static final RailLookup.TrackedSign[] NO_SIGNS = new RailLookup.TrackedSign[0];
   static final RailLookup.TrackedSign[] MISSING_RAILS_NO_SIGNS = new RailLookup.TrackedSign[0];
   static final List<MinecartMember<?>> DEFAULT_MEMBER_LIST = Collections.emptyList();
   private static final IdentityHashMap<World, WorldRailLookupImpl> byWorld = new IdentityHashMap();

   public static WorldRailLookup forWorld(World world) {
      WorldRailLookupImpl lookup = (WorldRailLookupImpl)byWorld.get(world);
      if (lookup == null) {
         if (world == null) {
            return WorldRailLookup.NONE;
         }

         lookup = new WorldRailLookupImpl(TrainCarts.plugin, world);
         byWorld.put(world, lookup);
         lookup.initialize();
      }

      return lookup;
   }

   public static WorldRailLookup forWorldIfInitialized(World world) {
      IdentityHashMap<World, WorldRailLookup> byWorldCast = (IdentityHashMap)CommonUtil.unsafeCast(byWorld);
      return (WorldRailLookup)byWorldCast.getOrDefault(world, WorldRailLookup.NONE);
   }

   public static RailPiece[] findAtStatePosition(RailState state) {
      return state.railLookup().findAtStatePosition(state);
   }

   public static RailPiece[] findAtBlockPosition(OfflineBlock positionBlock) {
      return forWorld(positionBlock.getLoadedWorld()).findAtBlockPosition(positionBlock);
   }

   public static List<MinecartMember<?>> findMembersOnRail(OfflineBlock railOfflineBlock) {
      return forWorldIfInitialized(railOfflineBlock.getLoadedWorld()).findMembersOnRail(railOfflineBlock);
   }

   public static RailLookup.CachedRailPiece lookupCachedRailPieceIfCached(OfflineBlock railOfflineBlock, RailType railType) {
      return forWorldIfInitialized(railOfflineBlock.getLoadedWorld()).lookupCachedRailPieceIfCached(railOfflineBlock, railType);
   }

   public static void clear() {
      byWorld.values().forEach(WorldRailLookupImpl::close);
      byWorld.clear();
   }

   public static void redetectSignActions() {
      Iterator var0 = byWorld.values().iterator();

      while(var0.hasNext()) {
         WorldRailLookup world = (WorldRailLookup)var0.next();
         world.redetectSignActions();
      }

   }

   public static void forceUnloadRail(RailType type) {
      forceRecalculation();
      byWorld.values().forEach((lookup) -> {
         lookup.unloadRailType(type);
      });
   }

   public static void forceRecalculation() {
      byWorld.values().forEach(WorldRailLookupImpl::refreshAllBuckets);
      lifeTimer = 1 + TCConfig.cacheExpireTicks + TCConfig.cacheVerificationTicks;
      lifeTimerAtPosition = 1;
      verifyTimer = ++lifeTimer + TCConfig.cacheVerificationTicks;
   }

   public static void removeMemberFromAll(MinecartMember<?> member) {
      Iterator var1 = byWorld.values().iterator();

      while(var1.hasNext()) {
         WorldRailLookup lookup = (WorldRailLookup)var1.next();
         lookup.removeMemberFromAll(member);
      }

   }

   public static void update() {
      int deadTimeout = lifeTimer - TCConfig.cacheExpireTicks - TCConfig.cacheVerificationTicks;
      Iterator iter = byWorld.values().iterator();

      while(iter.hasNext()) {
         WorldRailLookupImpl lookup = (WorldRailLookupImpl)iter.next();
         if (lookup.checkCanBeRemoved()) {
            lookup.close();
            iter.remove();
         } else {
            lookup.update(deadTimeout);
         }
      }

      ++lifeTimerAtPosition;
      verifyTimer = ++lifeTimer + TCConfig.cacheVerificationTicks;
   }

   public static RailPiece discoverRailPieceFromSign(Block signblock) {
      return forWorld(signblock.getWorld()).discoverRailPieceFromSign(signblock);
   }

   public static RailLookup.TrackedSign[] discoverSignsAtRailPiece(RailPiece rail) {
      return rail.railLookup().discoverSignsAtRailPiece(rail);
   }

   public abstract static class CachedRailPiece extends RailPiece {
      protected List<MinecartMember<?>> members;
      protected RailLookup.TrackedSign[] signs;
      protected DetectorRegion[] detectorRegions;
      public static final RailLookup.CachedRailPiece NONE = new RailLookup.CachedRailPiece() {
         public boolean verify() {
            return false;
         }

         public boolean verifyExists() {
            return false;
         }

         public void forceCacheVerification() {
         }
      };

      private CachedRailPiece() {
         this.members = Collections.unmodifiableList(Collections.emptyList());
         this.signs = RailLookup.NO_SIGNS;
         this.detectorRegions = RailLookup.NO_DETECTOR_REGIONS;
      }

      protected CachedRailPiece(WorldRailLookup railLookup, OfflineBlock offlineBlock, Block block, RailType type) {
         super(railLookup, offlineBlock, block, type);
         this.cached = this;
         this.members = RailLookup.DEFAULT_MEMBER_LIST;
         this.signs = RailLookup.NO_SIGNS;
         this.detectorRegions = RailLookup.NO_DETECTOR_REGIONS;
      }

      public abstract boolean verify();

      public abstract boolean verifyExists();

      public abstract void forceCacheVerification();

      public final List<MinecartMember<?>> cachedMembers() {
         return this.members;
      }

      public final List<MinecartMember<?>> cachedMutableMembers() {
         List<MinecartMember<?>> result = this.members;
         if (result == RailLookup.DEFAULT_MEMBER_LIST) {
            this.members = (List)(result = new ArrayList(2));
         }

         return (List)result;
      }

      public final RailLookup.TrackedSign[] cachedSigns() {
         return this.signs;
      }

      public final void redetectSignActions() {
         RailLookup.TrackedSign[] var1 = this.signs;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            RailLookup.TrackedSign sign = var1[var3];
            sign.redetectSignAction();
         }

         this.forceCacheVerification();
      }

      public final DetectorRegion[] cachedDetectorRegions() {
         return this.detectorRegions;
      }

      // $FF: synthetic method
      CachedRailPiece(Object x0) {
         this();
      }
   }

   public abstract static class TrackedSign {
      public final Sign sign;
      public final Block signBlock;
      /** @deprecated */
      @Deprecated
      public RailPiece rail;
      /** @deprecated */
      @Deprecated
      public RailType railType;
      /** @deprecated */
      @Deprecated
      public Block railBlock;
      private final int signBlockHashCode;
      private SignActionHeader cachedHeader;
      private boolean cachedActionSet;
      private SignAction cachedAction;

      TrackedSign(Sign sign, Block signBlock, RailPiece rail) {
         this.cachedHeader = null;
         this.cachedActionSet = false;
         this.cachedAction = null;
         if (sign == null) {
            throw new IllegalArgumentException("There is no sign at " + signBlock);
         } else {
            this.sign = sign;
            this.signBlock = signBlock;
            this.signBlockHashCode = signBlock.hashCode();
            this.rail = rail;
            this.railType = rail.type();
            this.railBlock = rail.block();
         }
      }

      private TrackedSign() {
         this.cachedHeader = null;
         this.cachedActionSet = false;
         this.cachedAction = null;
         this.sign = null;
         this.signBlock = null;
         this.signBlockHashCode = 0;
         this.rail = null;
         this.railType = RailType.NONE;
         this.railBlock = null;
      }

      public abstract boolean verify();

      public abstract boolean isRemoved();

      public abstract BlockFace getFacing();

      public abstract Block getAttachedBlock();

      public void setOutput(boolean output) {
         Block attachedBlock = this.getAttachedBlock();
         if (attachedBlock != null) {
            BlockUtil.setLeversAroundBlock(attachedBlock, output);
         }

      }

      public abstract String[] getExtraLines();

      public abstract PowerState getPower(BlockFace var1);

      public abstract boolean isRealSign();

      public abstract String getLine(int var1) throws IndexOutOfBoundsException;

      public abstract void setLine(int var1, String var2) throws IndexOutOfBoundsException;

      public Runnable showDebugHighlight(AttachmentViewer viewer, RailLookup.TrackedSign.DebugDisplayOptions options) {
         return () -> {
         };
      }

      public SignActionHeader getHeader() {
         SignActionHeader header = this.cachedHeader;
         if (header == null) {
            this.cachedHeader = header = SignActionHeader.parse(Util.cleanSignLine(this.getLine(0)));
         }

         return header;
      }

      public void setCachedHeader(SignActionHeader header) {
         this.cachedHeader = header;
      }

      public SignAction getAction() {
         if (this.cachedActionSet) {
            return this.cachedAction;
         } else {
            this.cachedActionSet = true;
            return this.cachedAction = SignAction.getSignAction(this.createEvent(SignActionType.NONE));
         }
      }

      public void redetectSignAction() {
         this.cachedActionSet = false;
         this.cachedAction = null;
      }

      public RailPiece getRail() {
         RailPiece rail = this.rail;
         if (rail == null) {
            this.rail = rail = RailLookup.discoverRailPieceFromSign(this.signBlock);
            this.railBlock = rail.block();
            this.railType = rail.type();
         }

         return rail;
      }

      public final SignActionEvent createEvent(SignActionType action) {
         return (new SignActionEvent(this)).setAction(action);
      }

      private final boolean canFireEvents() {
         return !this.isRemoved() && (this.rail == null || this.rail.type().isRegistered());
      }

      public void executeEventForMember(SignActionType action, MinecartMember<?> member) {
         this.executeEventForMember(action, member, (RailState)null);
      }

      public void executeEventForMember(SignActionType action, MinecartMember<?> member, RailState enterState) {
         if (this.canFireEvents() && member.isInteractable()) {
            SignActionEvent event = this.createEvent(action);
            event.setMember(member);
            event.overrideCartEnterState(enterState);
            SignAction.executeOne(this.getAction(), event);
         }

      }

      public void executeEventForGroup(SignActionType action, MinecartGroup group) {
         this.executeEventForGroup(action, group, (RailState)null);
      }

      public void executeEventForGroup(SignActionType action, MinecartGroup group, RailState enterState) {
         if (this.canFireEvents()) {
            SignActionEvent event = this.createEvent(action);
            event.setGroup(group);
            event.overrideCartEnterState(enterState);
            SignAction.executeOne(this.getAction(), event);
         }

      }

      public boolean hasIdenticalText(RailLookup.TrackedSign other) {
         for(int i = 0; i < 4; ++i) {
            if (!this.getLine(i).equals(other.getLine(i))) {
               return false;
            }
         }

         return true;
      }

      public int hashCode() {
         return this.signBlockHashCode;
      }

      public abstract Object getUniqueKey();

      public boolean equals(Object o) {
         return this == o;
      }

      /** @deprecated */
      @Deprecated
      public static RailLookup.TrackedSign forRealSign(SignChangeTracker signTracker, RailPiece rail) {
         return forRealSign(signTracker, true, rail);
      }

      /** @deprecated */
      @Deprecated
      public static RailLookup.TrackedSign forRealSign(Block signBlock, RailPiece rail) {
         return forRealSign(signBlock, true, rail);
      }

      /** @deprecated */
      @Deprecated
      public static RailLookup.TrackedSign forRealSign(Sign sign, RailPiece rail) {
         return forRealSign(sign, true, rail);
      }

      /** @deprecated */
      @Deprecated
      public static RailLookup.TrackedSign forRealSign(Sign sign, Block signBlock, RailPiece rail) {
         return forRealSign(sign, signBlock, true, rail);
      }

      public static RailLookup.TrackedSign forRealSign(SignChangeTracker signTracker, boolean frontText, RailPiece rail) {
         if (signTracker.isRemoved()) {
            throw new IllegalArgumentException("Sign does not exist at sign block " + signTracker.getBlock());
         } else {
            if (rail == null) {
               rail = RailLookup.discoverRailPieceFromSign(signTracker.getBlock());
            }

            return (RailLookup.TrackedSign)(frontText ? new RailLookup.TrackedRealSignFront(TrainCarts.plugin, signTracker, rail) : new RailLookup.TrackedRealSignBack(TrainCarts.plugin, signTracker, rail));
         }
      }

      public static RailLookup.TrackedSign forRealSign(Block signBlock, boolean frontText, RailPiece rail) {
         if (signBlock == null) {
            throw new IllegalArgumentException("Sign block is null");
         } else {
            return forRealSign(SignChangeTracker.track(signBlock), frontText, rail);
         }
      }

      public static RailLookup.TrackedSign forRealSign(Sign sign, boolean frontText, RailPiece rail) {
         if (sign == null) {
            throw new IllegalArgumentException("Sign is null");
         } else {
            return forRealSign(SignChangeTracker.track(sign), frontText, rail);
         }
      }

      public static RailLookup.TrackedSign forRealSign(Sign sign, Block signBlock, boolean frontText, RailPiece rail) {
         if (sign != null) {
            return forRealSign(SignChangeTracker.track(sign), frontText, rail);
         } else if (signBlock != null) {
            return forRealSign(SignChangeTracker.track(signBlock), frontText, rail);
         } else {
            throw new IllegalArgumentException("No sign or sign block specified (null)");
         }
      }

      // $FF: synthetic method
      TrackedSign(Object x0) {
         this();
      }

      public interface DebugDisplayOptions extends TrainCarts.Provider {
         ChatColor getTeamColor();
      }
   }

   public static final class UnitTestTrackedSign extends RailLookup.TrackedSign {
      private final String[] lines;

      public static RailLookup.UnitTestTrackedSign of(String... lines) {
         return new RailLookup.UnitTestTrackedSign(lines);
      }

      private UnitTestTrackedSign(String[] lines) {
         super(null);
         this.lines = lines;
      }

      public boolean verify() {
         return true;
      }

      public boolean isRemoved() {
         return false;
      }

      public BlockFace getFacing() {
         return BlockFace.NORTH;
      }

      public Block getAttachedBlock() {
         return null;
      }

      public String[] getExtraLines() {
         return new String[0];
      }

      public PowerState getPower(BlockFace from) {
         return PowerState.NONE;
      }

      public boolean isRealSign() {
         return false;
      }

      public String getLine(int index) throws IndexOutOfBoundsException {
         return this.lines[index];
      }

      public void setLine(int index, String line) throws IndexOutOfBoundsException {
         throw new UnsupportedOperationException("Not supported for unit test tracked signs");
      }

      public Object getUniqueKey() {
         return this;
      }
   }

   private static class TrackedRealSignBack extends RailLookup.TrackedRealSignBase {
      private TrackedRealSignBack(TrainCarts plugin, SignChangeTracker tracker, RailPiece rail) {
         super(plugin, tracker, rail, false, null);
      }

      public BlockFace getFacing() {
         return this.facing.getOppositeFace();
      }

      public String getLine(int index) throws IndexOutOfBoundsException {
         return this.tracker.getBackLine(index);
      }

      public void setLine(int index, String line) throws IndexOutOfBoundsException {
         this.tracker.setBackLine(index, line);
      }

      // $FF: synthetic method
      TrackedRealSignBack(TrainCarts x0, SignChangeTracker x1, RailPiece x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   private static class TrackedRealSignFront extends RailLookup.TrackedRealSignBase {
      private TrackedRealSignFront(TrainCarts plugin, SignChangeTracker tracker, RailPiece rail) {
         super(plugin, tracker, rail, true, null);
      }

      public BlockFace getFacing() {
         return this.facing;
      }

      public String getLine(int index) throws IndexOutOfBoundsException {
         return this.tracker.getFrontLine(index);
      }

      public void setLine(int index, String line) throws IndexOutOfBoundsException {
         this.tracker.setFrontLine(index, line);
      }

      // $FF: synthetic method
      TrackedRealSignFront(TrainCarts x0, SignChangeTracker x1, RailPiece x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   private abstract static class TrackedRealSignBase extends RailLookup.TrackedRealSign {
      protected final TrainCarts plugin;
      protected final SignChangeTracker tracker;
      protected final BlockFace facing;
      private final TrackedSignLookup.RealSignKey key;

      private TrackedRealSignBase(TrainCarts plugin, SignChangeTracker tracker, RailPiece rail, boolean front) {
         super(tracker.getSign(), tracker.getBlock(), rail);
         this.plugin = plugin;
         this.facing = tracker.getFacing();
         this.tracker = tracker;
         this.key = new TrackedSignLookup.RealSignKey(OfflineBlock.of(this.signBlock), front);
      }

      public Object getUniqueKey() {
         return this.key;
      }

      public boolean verify() {
         if (this.tracker.update()) {
            this.plugin.getSignController().notifySignChanged(this.tracker);
         }

         return !this.tracker.isRemoved() && this.tracker.getFacing() == this.facing && this.tracker.getSign() == this.sign;
      }

      public boolean isRemoved() {
         return this.tracker.isRemoved();
      }

      public boolean isFrontText() {
         return this.key.front;
      }

      public String[] getExtraLines() {
         RailPiece rail = this.getRail();
         if (rail.isNone()) {
            return StringUtil.EMPTY_ARRAY;
         } else {
            List<String> lines = new ArrayList();
            Block signBlock = this.signBlock.getRelative(BlockFace.DOWN);
            SignControllerWorld signController = this.plugin.getSignController().forWorld(rail.world());

            while(true) {
               SignController.Entry entry = signController.findForSign(signBlock, false);
               if (entry == null || entry.sign.getFacing() != this.facing) {
                  break;
               }

               RailLookup.TrackedSign sign = this.isFrontText() ? entry.createFrontTrackedSign(rail) : entry.createBackTrackedSign(rail);
               if (sign.getAction() != null) {
                  break;
               }

               for(int i = 0; i < 4; ++i) {
                  lines.add(sign.getLine(i));
               }

               signBlock = signBlock.getRelative(BlockFace.DOWN);
            }

            return (String[])lines.toArray(new String[0]);
         }
      }

      public Runnable showDebugHighlight(AttachmentViewer viewer, RailLookup.TrackedSign.DebugDisplayOptions options) {
         return SignController.spawnDebugHighlight(viewer, this.tracker, options);
      }

      public Block getAttachedBlock() {
         return this.signBlock.getRelative(this.tracker.getAttachedFace());
      }

      public PowerState getPower(BlockFace from) {
         return PowerState.get(this.signBlock, from, this.getAction() != null ? PowerState.Options.SIGN_CONNECT_WIRE : PowerState.Options.SIGN);
      }

      public boolean equals(Object o) {
         return o instanceof RailLookup.TrackedRealSignBase ? ((RailLookup.TrackedRealSignBase)o).key.equals(this.key) : false;
      }

      // $FF: synthetic method
      TrackedRealSignBase(TrainCarts x0, SignChangeTracker x1, RailPiece x2, boolean x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }

   public abstract static class TrackedRealSign extends RailLookup.TrackedSign {
      protected TrackedRealSign(Sign sign, Block signBlock, RailPiece rail) {
         super(sign, signBlock, rail);
      }

      public final boolean isRealSign() {
         return true;
      }

      public abstract boolean isFrontText();

      public String toString() {
         StringBuilder str = new StringBuilder();
         str.append(this.getClass().getSimpleName()).append('{');
         str.append("world=").append(this.signBlock.getWorld().getName());
         str.append(", x=").append(this.signBlock.getX());
         str.append(", y=").append(this.signBlock.getY());
         str.append(", z=").append(this.signBlock.getZ());
         str.append(", side=").append(this.isFrontText() ? "front" : "back");
         str.append(", lines=[");

         for(int i = 0; i < 4; ++i) {
            if (i > 0) {
               str.append(" | ");
            }

            str.append(this.getLine(i));
         }

         str.append("]}");
         return str.toString();
      }
   }

   public abstract static class TrackedFakeSign extends RailLookup.TrackedSign {
      public TrackedFakeSign(RailPiece rail) {
         this(rail.block(), rail);
      }

      public TrackedFakeSign(Block signBlock, RailPiece rail) {
         super(FakeSign.create(signBlock), signBlock, rail);
         ((FakeSign)this.sign).setHandler(new FakeSign.Handler() {
            public String getFrontLine(int index) {
               return TrackedFakeSign.this.getLine(index);
            }

            public void setFrontLine(int index, String text) {
               TrackedFakeSign.this.setLine(index, text);
            }

            public String getBackLine(int index) {
               return "";
            }

            public void setBackLine(int index, String text) {
            }
         });
      }

      public abstract String getLine(int var1) throws IndexOutOfBoundsException;

      public abstract void setLine(int var1, String var2) throws IndexOutOfBoundsException;

      public boolean isRealSign() {
         return false;
      }

      public Object getUniqueKey() {
         return this;
      }
   }

   public static final class RailTypeNotRegisteredException extends IllegalArgumentException {
      private static final long serialVersionUID = -3651967639525705930L;

      public RailTypeNotRegisteredException(RailType type) {
         super("Rail type " + type + " is not registered");
      }
   }
}
