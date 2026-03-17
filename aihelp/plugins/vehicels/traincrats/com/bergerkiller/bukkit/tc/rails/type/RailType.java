package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailAABB;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.global.SignControllerWorld;
import com.bergerkiller.bukkit.tc.editor.RailsTexture;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicAir;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;

public abstract class RailType {
   public static final RailTypeVertical VERTICAL = new RailTypeVertical();
   public static final RailTypeActivator ACTIVATOR_ON = new RailTypeActivator(true);
   public static final RailTypeActivator ACTIVATOR_OFF = new RailTypeActivator(false);
   public static final RailTypeCrossing CROSSING = new RailTypeCrossing();
   public static final RailTypeRegular REGULAR = new RailTypeRegular();
   public static final RailTypeDetector DETECTOR = new RailTypeDetector();
   public static final RailTypePowered BRAKE = new RailTypePowered(false);
   public static final RailTypePowered BOOST = new RailTypePowered(true);
   public static final RailTypeNone NONE = new RailTypeNone();
   private static List<RailType> values = new ArrayList();
   private final boolean _isComplexRailBlock;
   private final boolean _isHandlingPhysics;
   private boolean _registered = false;

   public static void handleCriticalError(RailType railType, Throwable reason) {
      if (values.contains(railType)) {
         TrainCarts traincarts = TrainCarts.plugin;
         Plugin plugin = CommonUtil.getPluginByClass(railType.getClass());
         Logger logger = traincarts.getLogger();
         if (plugin == traincarts) {
            logger.log(Level.SEVERE, "An error occurred in RailType '" + railType.getClass().getSimpleName() + "'", reason);
         } else if (plugin != null) {
            logger.log(Level.SEVERE, "An error occurred in RailType '" + railType.getClass().getSimpleName() + "' from plugin " + plugin.getName() + ". The rail type has been disabled.", reason);
            unregister(railType);
         } else {
            logger.log(Level.SEVERE, "An error occurred in RailType '" + railType.getClass().getSimpleName() + "' from an unknown plugin. The rail type has been disabled.", reason);
            unregister(railType);
         }

      }
   }

   public static void unregister(RailType type) {
      ArrayList<RailType> newValues = new ArrayList(values);
      if (newValues.remove(type)) {
         values = newValues;
         type._registered = false;
         RailLookup.forceUnloadRail(type);
      }

   }

   public static void register(RailType type, boolean withPriority) {
      ArrayList<RailType> newValues = new ArrayList(values);
      if (withPriority) {
         newValues.add(0, type);
      } else {
         newValues.add(type);
      }

      values = newValues;
      type._registered = true;
      RailLookup.forceRecalculation();
   }

   public static Collection<RailType> values() {
      return values;
   }

   public static RailType getType(Block railsBlock) {
      return (RailType)(railsBlock != null ? getType(railsBlock, WorldUtil.getBlockData(railsBlock)) : NONE);
   }

   public static RailType getType(Block railsBlock, BlockData railsBlockData) {
      Iterator var2 = values().iterator();

      RailType type;
      do {
         if (!var2.hasNext()) {
            return NONE;
         }

         type = (RailType)var2.next();
      } while(!checkRailTypeIsAt(type, railsBlock, railsBlockData));

      return type;
   }

   public static boolean checkRailTypeIsAt(RailType type, Block railsBlock, BlockData railsBlockData) {
      try {
         return type.isComplexRailBlock() ? type.isRail(railsBlock) : type.isRail(railsBlockData);
      } catch (Throwable var4) {
         handleCriticalError(type, var4);
         return false;
      }
   }

   public static boolean loadRailInformation(RailState state) {
      state.initEnterDirection();
      state.position().assertAbsolute();
      RailPiece[] cachedPieces = state.railLookup().findAtStatePosition(state);
      if (cachedPieces.length == 0) {
         state.setRailPiece(RailPiece.create(NONE, state.positionBlock(), state.railLookup()));
         return false;
      } else {
         RailPiece resultPiece = cachedPieces[0];
         if (cachedPieces.length >= 2) {
            RailPath.ProximityInfo nearest = null;
            RailPiece[] var4 = cachedPieces;
            int var5 = cachedPieces.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               RailPiece piece = var4[var6];
               state.setRailPiece(piece);
               RailLogic logic = state.loadRailLogic();
               RailPath path = logic.getPath();
               RailPath.ProximityInfo near = path.getProximityInfo(state.railPosition(), state.motionVector());
               if (nearest == null || near.compareTo(nearest) < 0) {
                  nearest = near;
                  resultPiece = piece;
               }
            }
         }

         state.setRailPiece(resultPiece);
         return true;
      }
   }

   /** @deprecated */
   @Deprecated
   public static RailPiece findRailPiece(Block blockPosition) {
      RailState state = new RailState();
      state.position().setLocationMidOf(blockPosition);
      state.setRailPiece(RailPiece.createWorldPlaceholder(blockPosition.getWorld()));
      return loadRailInformation(state) ? state.railPiece() : null;
   }

   public static RailPiece findRailPiece(Location position) {
      RailState state = new RailState();
      state.position().setLocation(position);
      state.setRailPiece(RailPiece.createWorldPlaceholder(position.getWorld()));
      return loadRailInformation(state) ? state.railPiece() : null;
   }

   public RailType() {
      this._isComplexRailBlock = CommonUtil.isMethodOverrided(RailType.class, this.getClass(), "isRail", new Class[]{World.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
      this._isHandlingPhysics = CommonUtil.isMethodOverrided(RailType.class, this.getClass(), "onBlockPhysics", new Class[]{BlockPhysicsEvent.class}) || CommonUtil.isMethodOverrided(RailType.class, this.getClass(), "isRailsSupported", new Class[]{Block.class});
   }

   public abstract boolean isRail(BlockData var1);

   public boolean isRail(World world, int x, int y, int z) {
      return this.isRail(WorldUtil.getBlockData(world, x, y, z));
   }

   public final boolean isRail(Block block, BlockFace offset) {
      return this.isRail(block.getWorld(), block.getX() + offset.getModX(), block.getY() + offset.getModY(), block.getZ() + offset.getModZ());
   }

   public final boolean isRail(Block block) {
      return this.isRail(block.getWorld(), block.getX(), block.getY(), block.getZ());
   }

   public final boolean isRegistered() {
      return this._registered;
   }

   public RailAABB getBoundingBox(RailState state) {
      return RailAABB.BLOCK;
   }

   public final boolean isComplexRailBlock() {
      return this._isComplexRailBlock;
   }

   public final boolean isHandlingPhysics() {
      return this._isHandlingPhysics;
   }

   public boolean isUpsideDown(Block railsBlock) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public Block findRail(Block pos) {
      throw new UnsupportedOperationException("Not implemented");
   }

   public List<Block> findRails(Block positionBlock) {
      Block rail = this.findRail(positionBlock);
      return rail == null ? Collections.emptyList() : Collections.singletonList(rail);
   }

   /** @deprecated */
   @Deprecated
   public Block findMinecartPos(Block trackBlock) {
      return trackBlock;
   }

   /** @deprecated */
   @Deprecated
   public abstract BlockFace[] getPossibleDirections(Block var1);

   public List<RailJunction> getJunctions(Block railBlock) {
      RailState state = new RailState();
      state.setRailPiece(RailPiece.create(this, railBlock));
      state.position().setLocation(this.getSpawnLocation(railBlock, BlockFace.DOWN));
      state.position().setMotion(BlockFace.DOWN);
      state.initEnterDirection();
      RailPath path = this.getLogic(state).getPath();
      return path.isEmpty() ? Collections.emptyList() : Arrays.asList(new RailJunction("1", path.getStartPosition()), new RailJunction("2", path.getEndPosition()));
   }

   public RailState takeJunction(Block railBlock, RailJunction junction) {
      RailState state = new RailState();
      state.setRailPiece(RailPiece.create(this, railBlock));
      junction.position().copyTo(state.position());
      state.position().makeAbsolute(railBlock);
      state.position().smallAdvance();
      if (!loadRailInformation(state)) {
         return null;
      } else {
         return state.railType() == this && state.railBlock().equals(railBlock) ? null : state;
      }
   }

   public void switchJunction(Block railBlock, RailJunction from, RailJunction to) {
   }

   /** @deprecated */
   @Deprecated
   public BlockFace getDirection(Block railsBlock) {
      RailState state = new RailState();
      state.setRailPiece(RailPiece.create(this, railsBlock));
      state.setPosition(RailPath.Position.fromLocation(this.getSpawnLocation(railsBlock, BlockFace.SELF)));
      state.initEnterDirection();
      return state.enterFace();
   }

   public abstract BlockFace getSignColumnDirection(Block var1);

   public BlockFace[] getSignTriggerDirections(Block railBlock, Block signBlock, BlockFace signFacing) {
      return FaceUtil.BLOCK_SIDES;
   }

   public Block getSignColumnStart(Block railsBlock) {
      return railsBlock;
   }

   public void discoverSigns(RailPiece railPiece, SignControllerWorld signController, List<RailLookup.TrackedSign> result) {
      Block columnStart = this.getSignColumnStart(railPiece.block());
      if (columnStart != null) {
         BlockFace direction = this.getSignColumnDirection(railPiece.block());
         if (direction != null && direction != BlockFace.SELF) {
            signController.forEachSignInColumn(columnStart, direction, true, (tracker) -> {
               result.add(RailLookup.TrackedSign.forRealSign(tracker, true, railPiece));
               if (CommonCapabilities.HAS_SIGN_BACK_TEXT) {
                  result.add(RailLookup.TrackedSign.forRealSign(tracker, false, railPiece));
               }

            });
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public RailLogic getLogic(MinecartMember<?> member, Block railsBlock, BlockFace direction) {
      return RailLogicAir.INSTANCE;
   }

   public RailLogic getLogic(RailState state) {
      return this.getLogic(state.member(), state.railBlock(), state.enterFace());
   }

   public void onBlockPlaced(Block railsBlock) {
   }

   public void onBlockPhysics(BlockPhysicsEvent event) {
   }

   public boolean isRailsSupported(Block railsBlock) {
      return true;
   }

   public void onPreMove(MinecartMember<?> member) {
   }

   public void onPostMove(MinecartMember<?> member) {
   }

   public boolean onCollide(MinecartMember<?> with, Block block, BlockFace hitFace) {
      return true;
   }

   public boolean hasBlockActivation(Block railBlock) {
      return false;
   }

   public boolean onBlockCollision(MinecartMember<?> member, Block railsBlock, Block hitBlock, BlockFace hitFace) {
      return true;
   }

   public boolean isHeadOnCollision(MinecartMember<?> member, Block railsBlock, Block hitBlock) {
      return false;
   }

   public abstract Location getSpawnLocation(Block var1, BlockFace var2);

   public RailsTexture getRailsTexture(Block railsBlock) {
      return new RailsTexture();
   }

   static {
      RailType[] var0 = (RailType[])CommonUtil.getClassConstants(RailType.class);
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         RailType type = var0[var2];
         type._registered = true;
         if (type != NONE) {
            values.add(type);
         }
      }

   }
}
