package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.PermissionEnum;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignBuildEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.pathfinding.PathPredictEvent;
import com.bergerkiller.bukkit.tc.pathfinding.SignRoutingEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.util.SignActionLookupMap;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.generated.org.bukkit.block.SignHandle;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public abstract class SignAction {
   private static SignActionLookupMap lookup;
   private final boolean _hasPathPrediction = CommonUtil.isMethodOverrided(SignAction.class, this.getClass(), "predictPathFinding", new Class[]{SignActionEvent.class, PathPredictEvent.class});

   public static void init() {
      lookup = SignActionLookupMap.create();
      register(new SignActionStation());
      register(new SignActionLauncher());
      register(new SignActionSwitcher());
      register(new SignActionSpawn());
      register(new SignActionBlockChanger());
      register(new SignActionProperties());
      register(new SignActionTrigger());
      register(new SignActionTeleport());
      register(new SignActionJumper());
      register(new SignActionEject());
      register(new SignActionEnter());
      register(new SignActionDestroy());
      register(new SignActionTransfer());
      register(new SignActionFuel());
      register(new SignActionCraft());
      register(SignActionDetector.INSTANCE);
      register(new SignActionDestination());
      register(new SignActionBlocker());
      register(new SignActionWait());
      register(SignActionElevator.INSTANCE);
      register(new SignActionTicket());
      register(new SignActionAnnounce());
      register(new SignActionEffect());
      register(new SignActionBukkitEffect());
      register(new SignActionSound());
      register(new SignActionSkip());
      register(new SignActionMutex());
      register(new SignActionPathingMutex());
      register(new SignActionFlip());
      register(new SignActionAnimate());
      if (Common.evaluateMCVersion(">=", "1.11")) {
         register(new SignActionTitle());
      }

   }

   public static void deinit() {
      lookup = SignActionLookupMap.DISABLED;
   }

   public static SignActionLookupMap getLookup() {
      return lookup;
   }

   public static SignAction getSignAction(SignActionEvent event) {
      return (SignAction)lookup.lookup(event).map(SignActionLookupMap.Entry::action).orElse((Object)null);
   }

   public static <T extends SignAction> T register(T action) {
      return register(action, false);
   }

   public static <T extends SignAction> T register(T action, boolean priority) {
      return lookup.register(action, priority);
   }

   public static void unregister(SignAction action) {
      lookup.unregister(action);
   }

   public static void handleLoadChange(Sign sign, boolean frontText, boolean loaded) {
      RailLookup.TrackedSign trackedSign = RailLookup.TrackedSign.forRealSign(sign, frontText, RailPiece.NONE);
      trackedSign.rail = null;
      handleLoadChange(trackedSign, loaded);
   }

   public static void handleLoadChange(RailLookup.TrackedSign trackedSign, boolean loaded) {
      SignActionEvent info = new SignActionEvent(trackedSign);
      lookup.lookup(info, SignActionLookupMap.LookupMode.WITH_LOADED_CHANGED_HANDLER).map(SignActionLookupMap.Entry::action).ifPresent((e) -> {
         e.loadedChanged(info, loaded);
      });
   }

   public static boolean handleClick(Block clickedSign, Player player) {
      Sign bsign = BlockUtil.getSign(clickedSign);
      if (bsign == null) {
         return false;
      } else {
         SignHandle bsignhandle = SignHandle.createHandle(bsign);
         if (!bsignhandle.getFrontLine(0).isEmpty() && handleClick(RailLookup.TrackedSign.forRealSign(bsign, clickedSign, true, (RailPiece)null), player)) {
            return true;
         } else {
            return !bsignhandle.getBackLine(0).isEmpty() && handleClick(RailLookup.TrackedSign.forRealSign(bsign, clickedSign, false, (RailPiece)null), player);
         }
      }
   }

   private static boolean handleClick(RailLookup.TrackedSign clickedSign, Player player) {
      SignActionEvent info = new SignActionEvent(clickedSign);
      SignAction action = getSignAction(info);
      return action != null && action.click(info, player);
   }

   /** @deprecated */
   @Deprecated
   public static boolean handleBuild(SignChangeActionEvent event, PermissionEnum permission, String signname) {
      return handleBuild(event, permission, signname, (String)null);
   }

   /** @deprecated */
   @Deprecated
   public static boolean handleBuild(SignChangeActionEvent event, PermissionEnum permission, String signname, String signdescription) {
      return SignBuildOptions.create().setPermission(permission).setName(signname).setDescription(signdescription).handle(event.getPlayer());
   }

   /** @deprecated */
   @Deprecated
   public static void handleBuild(SignChangeActionEvent info) {
      handleBuild(new SignBuildEvent(info));
   }

   /** @deprecated */
   @Deprecated
   public static void handleBuild(SignChangeEvent event) {
      handleBuild(new SignBuildEvent(event, true));
   }

   public static void handleBuild(SignBuildEvent info) {
      CommonUtil.callEvent(info);
      if (!info.isCancelled()) {
         if (info.hasRegisteredAction()) {
            SignAction action = info.getRegisteredAction();
            if (!info.getTrackedSign().isRealSign() && !action.canSupportFakeSign(info)) {
               info.getPlayer().sendMessage(ChatColor.RED + "A real sign is required for this type of action");
               info.setCancelled(true);
               return;
            }

            if (!action.build(info)) {
               info.setCancelled(true);
               return;
            }

            if (action.canSupportRC()) {
               if (info.isRCSign() && !Permission.BUILD_REMOTE_CONTROL.has(info.getPlayer())) {
                  Localization.SIGN_NO_RC_PERMISSION.message(info.getPlayer(), new String[0]);
                  info.getHeader().setMode(SignActionMode.TRAIN);
                  info.setLine(0, info.getHeader().toString());
               }
            } else if (info.isRCSign()) {
               info.getPlayer().sendMessage(ChatColor.RED + "This sign does not support remote control!");
               info.getHeader().setMode(SignActionMode.TRAIN);
               info.setLine(0, info.getHeader().toString());
            }

            String destinationName = action.getRailDestinationName(info);
            if (destinationName != null) {
               PathNode node = info.getTrainCarts().getPathProvider().getWorld(info.getWorld()).getNodeByName(destinationName);
               if (node != null) {
                  Player p = info.getPlayer();
                  p.sendMessage(ChatColor.RED + "Another destination with the same name already exists!");
                  p.sendMessage(ChatColor.RED + "Please remove either sign and use /train reroute to fix");
                  BlockLocation loc = node.location;
                  StringBuilder locMsg = new StringBuilder(100);
                  locMsg.append(ChatColor.RED).append("Other destination '" + destinationName + "' is ");
                  if (loc.getWorld() != info.getPlayer().getWorld()) {
                     locMsg.append("on world ").append(ChatColor.WHITE).append(node.location.world);
                     locMsg.append(' ').append(ChatColor.RED);
                  }

                  locMsg.append("at ").append(ChatColor.WHITE);
                  locMsg.append('[').append(loc.x).append('/').append(loc.y);
                  locMsg.append('/').append(loc.z).append(']');
                  p.sendMessage(locMsg.toString());
               }
            }

            if (info.hasRails()) {
               Iterator var9 = info.getRailPiece().members().iterator();

               while(var9.hasNext()) {
                  MinecartMember<?> member = (MinecartMember)var9.next();
                  if (!member.isUnloaded() && !((CommonMinecart)member.getEntity()).isRemoved()) {
                     member.getGroup().getSignTracker().updatePosition();
                  }
               }
            }

            action.loadedChanged(info, true);
         }

         if (info.getMode() != SignActionMode.NONE && info.getTrackedSign().isRealSign()) {
            BlockData data = WorldUtil.getBlockData(info.getBlock());
            if (MaterialUtil.ISSIGN.get(data) && FaceUtil.isVertical(data.getAttachedFace())) {
               BlockFace oldFacing = data.getFacingDirection();
               BlockFace newFacing = Util.snapFace(oldFacing);
               if (oldFacing != newFacing) {
                  BlockUtil.setFacing(info.getBlock(), newFacing);
               }
            }
         }

      }
   }

   public static void handleDestroy(SignActionEvent info) {
      if (info != null && info.getSign() != null) {
         SignAction action = getSignAction(info);
         if (action != null) {
            Iterator var2 = MinecartGroup.getGroups().cloneAsIterable().iterator();

            while(var2.hasNext()) {
               MinecartGroup group = (MinecartGroup)var2.next();
               group.getSignTracker().removeSign(info.getTrackedSign());
            }

            boolean switchable = action.isRailSwitcher(info);
            String destinationName = action.getRailDestinationName(info);
            action.destroy(info);
            if (destinationName != null) {
               PathNode node = info.getTrainCarts().getPathProvider().getWorld(info.getWorld()).getNodeByName(destinationName);
               if (node != null) {
                  node.removeName(destinationName);
               }
            }

            if (switchable) {
               Block rails = info.getRails();
               if (rails != null) {
                  PathNode node = PathNode.get(rails);
                  if (node != null) {
                     node.remove();
                  }
               }
            }

            action.loadedChanged(info, false);
         }

      }
   }

   public static void executeAll(SignActionEvent info, SignActionType actiontype) {
      info.setAction(actiontype);
      executeAll(info);
   }

   public static void executeAll(SignActionEvent info) {
      if (info != null && info.getSign() != null) {
         info.setCancelled(false);
         if (!((SignActionEvent)CommonUtil.callEvent(info)).isCancelled()) {
            executeOneImpl(getSignAction(info), info);
         }
      }
   }

   public static void executeOne(SignAction action, SignActionEvent info) {
      if (info != null && info.getSign() != null) {
         info.setCancelled(false);
         if (!((SignActionEvent)CommonUtil.callEvent(info)).isCancelled()) {
            executeOneImpl(action, info);
         }
      }
   }

   private static void executeOneImpl(SignAction action, SignActionEvent info) {
      if (action != null) {
         if (!info.isAction(SignActionType.MEMBER_MOVE) || action.isMemberMoveHandled(info)) {
            if (info.getTrackedSign().isRealSign() || action.canSupportFakeSign(info)) {
               if (action.overrideFacing() || !info.getAction().isMovement() || info.isFacing()) {
                  try {
                     action.execute(info);
                  } catch (Throwable var5) {
                     String signInfo;
                     Block railBlock;
                     if (info.getTrackedSign().isRealSign()) {
                        railBlock = info.getBlock();
                        signInfo = railBlock.getWorld().getName() + " x=" + railBlock.getX() + " y=" + railBlock.getY() + " z=" + railBlock.getZ();
                     } else if (info.hasRails()) {
                        railBlock = info.getRails();
                        signInfo = info.getTrackedSign().getClass().getSimpleName() + " rail " + railBlock.getWorld().getName() + " x=" + railBlock.getX() + " y=" + railBlock.getY() + " z=" + railBlock.getZ();
                     } else {
                        signInfo = info.getTrackedSign().getClass().getSimpleName() + " key " + info.getTrackedSign().getUniqueKey();
                     }

                     info.getTrainCarts().getLogger().log(Level.SEVERE, "Failed to execute " + info.getAction().toString() + " for " + action.getClass().getSimpleName() + " at {" + signInfo + "}:", CommonUtil.filterStackTrace(var5));
                  }

               }
            }
         }
      }
   }

   public boolean verify(SignActionEvent info) {
      if (!info.getHeader().isValid()) {
         return false;
      } else {
         return !info.getHeader().isActionFiltered(info.getAction());
      }
   }

   public abstract boolean match(SignActionEvent var1);

   public abstract void execute(SignActionEvent var1);

   public abstract boolean build(SignChangeActionEvent var1);

   public void destroy(SignActionEvent info) {
   }

   public void loadedChanged(SignActionEvent info, boolean loaded) {
   }

   public boolean canSupportRC() {
      return false;
   }

   public boolean canSupportFakeSign(SignActionEvent info) {
      return true;
   }

   public boolean overrideFacing() {
      return false;
   }

   public boolean isMemberMoveHandled(SignActionEvent info) {
      return false;
   }

   public void route(SignRoutingEvent event) {
      if (this.isPathFindingBlocked(event, event.getCartEnterState())) {
         event.setBlocked();
      } else {
         if (this.isRailSwitcher(event)) {
            event.setRouteSwitchable(true);
         }

         String destinationName = this.getRailDestinationName(event);
         if (destinationName != null) {
            event.addDestinationName(destinationName);
         }

      }
   }

   public boolean isRailSwitcher(SignActionEvent info) {
      return false;
   }

   public String getRailDestinationName(SignActionEvent info) {
      return null;
   }

   public boolean isPathFindingBlocked(SignActionEvent info, RailState state) {
      return false;
   }

   public void predictPathFinding(SignActionEvent info, PathPredictEvent prediction) {
   }

   public final boolean hasPathFindingPrediction() {
      return this._hasPathPrediction;
   }

   public boolean click(SignActionEvent info, Player player) {
      return false;
   }

   public boolean signTextChanged(SignActionEvent event) {
      return true;
   }

   public String getDescriptiveOutputName(SignActionEvent event) {
      return null;
   }

   static {
      lookup = SignActionLookupMap.DISABLED;
   }
}
