package com.bergerkiller.bukkit.tc.events;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.PowerState;
import com.bergerkiller.bukkit.tc.SignActionHeader;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.components.RailTracker;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.direction.RailEnterDirection;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.rails.type.RailTypeRegular;
import com.bergerkiller.bukkit.tc.signactions.SignActionMode;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class SignActionEvent extends Event implements Cancellable, TrainCarts.Provider {
   private static final HandlerList handlers = new HandlerList();
   private final RailLookup.TrackedSign sign;
   private final String lowerSecondCleanedLine;
   private RailEnterDirection[] enterDirections;
   private SignActionType actionType;
   private BlockFace raildirection;
   private MinecartMember<?> member;
   private MinecartGroup group;
   private RailState overrideMemberEnterState;
   private boolean memberchecked;
   private boolean cancelled;

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signblock, MinecartMember<?> member) {
      this(signblock);
      this.member = member;
      this.memberchecked = true;
   }

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signblock, RailPiece rail, MinecartMember<?> member) {
      this(signblock, rail);
      this.member = member;
      this.memberchecked = true;
   }

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signblock, MinecartGroup group) {
      this(signblock);
      this.group = group;
      this.memberchecked = true;
   }

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signblock, RailPiece rail, MinecartGroup group) {
      this(signblock, rail);
      this.group = group;
      this.memberchecked = true;
   }

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signBlock) {
      this(RailLookup.TrackedSign.forRealSign((Block)signBlock, (RailPiece)null));
   }

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signblock, RailPiece rail) {
      this(RailLookup.TrackedSign.forRealSign(signblock, rail));
   }

   /** @deprecated */
   @Deprecated
   public SignActionEvent(Block signblock, Sign sign, RailPiece rail) {
      this(RailLookup.TrackedSign.forRealSign(sign, signblock, rail));
   }

   public SignActionEvent(RailLookup.TrackedSign trackedSign, MinecartMember<?> member) {
      this(trackedSign);
      this.member = member;
      this.memberchecked = true;
   }

   public SignActionEvent(RailLookup.TrackedSign trackedSign, MinecartGroup group) {
      this(trackedSign);
      this.group = group;
      this.memberchecked = true;
   }

   public SignActionEvent(RailLookup.TrackedSign sign) {
      this.raildirection = null;
      this.member = null;
      this.group = null;
      this.overrideMemberEnterState = null;
      this.memberchecked = false;
      this.cancelled = false;
      if (sign == null) {
         throw new IllegalArgumentException("Tracked sign is null");
      } else {
         this.sign = sign;
         this.actionType = SignActionType.NONE;
         this.lowerSecondCleanedLine = Util.cleanSignLine(sign.getLine(1)).toLowerCase(Locale.ENGLISH);
         if (this.sign.getHeader().isLegacyConverted() && this.sign.getHeader().isValid()) {
            this.setLine(0, this.sign.getHeader().toString());
         }

         this.enterDirections = null;
      }
   }

   public TrainCarts getTrainCarts() {
      return TrainCarts.plugin;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public void setLevers(boolean down) {
      this.getTrackedSign().setOutput(down);
   }

   public boolean isRailsVertical() {
      if (!this.hasRails()) {
         return false;
      } else {
         BlockFace signDirection = this.getFacing().getOppositeFace();
         RailState state = new RailState();
         state.setRailPiece(this.getRailPiece());
         state.position().setLocation(state.railType().getSpawnLocation(state.railBlock(), signDirection));
         state.position().setMotion(signDirection);
         state.initEnterDirection();
         state.loadRailLogic().getPath().snap(state.position(), state.railBlock());
         return FaceUtil.isVertical(Util.vecToFace(state.position().getMotion(), false));
      }
   }

   public void overrideCartEnterState(RailState enterState) {
      this.overrideMemberEnterState = enterState;
   }

   public Vector getCartEnterDirection() {
      RailState state = this.getCartEnterState();
      if (state != null) {
         return state.enterDirection();
      } else {
         BlockFace signDirection;
         if (this.getWatchedDirections().length > 0) {
            signDirection = this.getWatchedDirections()[0];
         } else {
            signDirection = this.getFacing().getOppositeFace();
         }

         if (this.hasRails()) {
            RailState state = new RailState();
            state.setRailPiece(this.getRailPiece());
            state.position().setLocation(state.railType().getSpawnLocation(state.railBlock(), signDirection));
            state.position().setMotion(signDirection);
            state.initEnterDirection();
            state.loadRailLogic().getPath().snap(state.position(), state.railBlock());
            return state.position().getMotion();
         } else {
            return FaceUtil.faceToVector(signDirection);
         }
      }
   }

   public BlockFace getCartEnterFace() {
      RailState state = this.getCartEnterState();
      if (state != null) {
         return state.enterFace();
      } else {
         BlockFace signDirection;
         if (this.getWatchedDirections().length > 0) {
            signDirection = this.getWatchedDirections()[0];
         } else {
            signDirection = this.getFacing().getOppositeFace();
         }

         if (this.hasRails()) {
            RailState state = new RailState();
            state.setRailPiece(this.getRailPiece());
            state.position().setLocation(state.railType().getSpawnLocation(state.railBlock(), signDirection));
            state.position().setMotion(signDirection);
            state.initEnterDirection();
            state.loadRailLogic().getPath().snap(state.position(), state.railBlock());
            state.initEnterDirection();
            return state.enterFace();
         } else {
            return signDirection;
         }
      }
   }

   public RailState getCartEnterState() {
      RailState state;
      if ((state = this.overrideMemberEnterState) != null) {
         return state;
      } else {
         if (this.hasRails() && this.hasMember()) {
            RailPiece railPiece = this.getRailPiece();
            Iterator var2 = this.member.getGroup().getRailTracker().getRailInformation().iterator();

            while(var2.hasNext()) {
               RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var2.next();
               if (rail.member == this.member && rail.state.railPiece().equals(railPiece)) {
                  return rail.state;
               }
            }
         }

         return null;
      }
   }

   /** @deprecated */
   @Deprecated
   public BlockFace getCartDirection() {
      return this.getCartEnterFace();
   }

   /** @deprecated */
   @Deprecated
   public void setRailsFromTo(BlockFace from, BlockFace to) {
      this.setRailsFromTo(this.findJunction(from), this.findJunction(to));
   }

   /** @deprecated */
   @Deprecated
   public void setRailsTo(BlockFace to) {
      this.setRailsTo(this.findJunction(to));
   }

   /** @deprecated */
   @Deprecated
   public void setRailsTo(Direction direction) {
      this.setRailsTo(this.findJunction(direction));
   }

   public List<RailJunction> getJunctions() {
      RailPiece piece = this.getRailPiece();
      return piece.isNone() ? Collections.emptyList() : piece.type().getJunctions(piece.block());
   }

   public RailJunction findJunction(String junctionName) {
      Iterator var2 = this.getJunctions().iterator();

      RailJunction junc;
      do {
         if (!var2.hasNext()) {
            String dirText = junctionName.toLowerCase(Locale.ENGLISH);
            if (LogicUtil.contains(dirText, new String[]{"c", "continue"})) {
               return this.findJunction(Direction.fromFace(this.getCartEnterFace()));
            }

            if (LogicUtil.contains(dirText, new String[]{"i", "rev", "reverse", "inverse"})) {
               return this.findJunction(Direction.fromFace(this.getCartEnterFace().getOppositeFace()));
            }

            return this.findJunction(Direction.parse(dirText));
         }

         junc = (RailJunction)var2.next();
      } while(!junc.name().equals(junctionName));

      return junc;
   }

   public RailJunction findJunction(BlockFace face) {
      return (RailJunction)RailJunction.findBest(this.getJunctions(), FaceUtil.faceToVector(face)).orElse((Object)null);
   }

   public RailJunction findJunction(Direction direction) {
      if (direction != Direction.NONE && direction != null) {
         BlockFace to = direction.getDirection(this.getFacing());
         if ((direction == Direction.IMPLICIT_LEFT || direction == Direction.IMPLICIT_RIGHT) && this.getRailType() instanceof RailTypeRegular && !this.isConnectedRails(to)) {
            to = Direction.FORWARD.getDirection(this.getFacing());
         }

         return this.findJunction(to);
      } else {
         return null;
      }
   }

   public RailJunction getEnterJunction() {
      if (!this.hasMember()) {
         return null;
      } else {
         RailTracker.TrackedRail memberRail = null;
         if (this.hasRails()) {
            Block rails = this.getRails();
            Iterator var3 = this.member.getGroup().getRailTracker().getRailInformation().iterator();

            while(var3.hasNext()) {
               RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var3.next();
               if (rail.member == this.member && rail.state.railBlock().equals(rails)) {
                  memberRail = rail;
                  break;
               }
            }
         }

         if (memberRail == null) {
            memberRail = this.member.getRailTracker().getRail();
         }

         RailState tmp = memberRail.state.cloneAndInvertMotion();
         memberRail.getPath().move(tmp, Double.MAX_VALUE);
         RailPath.Position pos = tmp.position();
         double min_dist = Double.MAX_VALUE;
         RailJunction best_junc = null;
         Iterator var6 = memberRail.state.railType().getJunctions(memberRail.state.railBlock()).iterator();

         while(var6.hasNext()) {
            RailJunction junc = (RailJunction)var6.next();
            if (junc.position().relative) {
               pos.makeRelative(memberRail.state.railBlock());
            } else {
               pos.makeAbsolute(memberRail.state.railBlock());
            }

            double dist_sq = junc.position().distanceSquared(pos);
            if (dist_sq < min_dist) {
               min_dist = dist_sq;
               best_junc = junc;
            }
         }

         return best_junc;
      }
   }

   public void setRailsTo(String toJunctionName) {
      this.setRailsFromTo(this.getEnterJunction(), this.findJunction(toJunctionName));
   }

   public void setRailsTo(RailJunction toJunction) {
      this.setRailsFromTo(this.getEnterJunction(), toJunction);
   }

   public void setRailsFromTo(String fromJunctionName, String toJunctionName) {
      this.setRailsFromTo(this.findJunction(fromJunctionName), this.findJunction(toJunctionName));
   }

   public void setRailsFromTo(RailJunction fromJunction, String toJunctionName) {
      this.setRailsFromTo(fromJunction, this.findJunction(toJunctionName));
   }

   public void setRailsFromTo(RailJunction fromJunction, RailJunction toJunction) {
      if (this.hasRails() && fromJunction != null && toJunction != null) {
         RailPiece rail = this.sign.getRail();
         Predicate membersToTeleport;
         if (this.isAction(SignActionType.GROUP_ENTER)) {
            membersToTeleport = (m) -> {
               return m.getGroup() == this.getGroup();
            };
         } else if (this.isAction(SignActionType.MEMBER_ENTER)) {
            membersToTeleport = (m) -> {
               return m == this.getMember();
            };
         } else {
            membersToTeleport = LogicUtil.alwaysTruePredicate();
         }

         if (!fromJunction.name().equals(toJunction.name())) {
            rail.switchJunction(fromJunction, toJunction, membersToTeleport);
         } else {
            RailState state = RailState.getSpawnState(rail);
            RailPath path = state.loadRailLogic().getPath();
            if (path.isEmpty()) {
               Iterator var7 = rail.getJunctions().iterator();

               while(var7.hasNext()) {
                  RailJunction junc = (RailJunction)var7.next();
                  if (!junc.name().equals(fromJunction.name())) {
                     fromJunction = junc;
                     break;
                  }
               }
            } else {
               RailPath.Position p0 = path.getStartPosition();
               RailPath.Position p1 = path.getEndPosition();
               double min_dist = Double.MAX_VALUE;
               Iterator var11 = rail.getJunctions().iterator();

               while(var11.hasNext()) {
                  RailJunction junc = (RailJunction)var11.next();
                  if (!junc.name().equals(fromJunction.name())) {
                     if (junc.position().relative) {
                        p0.makeRelative(rail.block());
                        p1.makeRelative(rail.block());
                     } else {
                        p0.makeAbsolute(rail.block());
                        p1.makeAbsolute(rail.block());
                     }

                     double dist_sq = Math.min(p0.distanceSquared(junc.position()), p1.distanceSquared(junc.position()));
                     if (dist_sq < min_dist) {
                        min_dist = dist_sq;
                        fromJunction = junc;
                     }
                  }
               }
            }

            rail.switchJunction(fromJunction, toJunction, membersToTeleport);
            if (this.hasMember()) {
               MinecartGroup group = this.member.getGroup();
               if (group != null) {
                  group.getActions().clear();
                  group.split(this.member.getIndex());
               }

               group = this.member.getGroup();
               if (group != null) {
                  group.reverse();
               }
            }

         }
      }
   }

   public SignActionType getAction() {
      return this.actionType;
   }

   public SignActionEvent setAction(SignActionType type) {
      this.actionType = type;
      return this;
   }

   public boolean isAction(SignActionType... types) {
      return LogicUtil.contains(this.actionType, types);
   }

   public boolean hasRailedMember() {
      return this.hasRails() && this.hasMember();
   }

   public SignActionHeader getHeader() {
      return this.sign.getHeader();
   }

   /** @deprecated */
   @Deprecated
   public boolean isPowerInverted() {
      return this.getHeader().isInverted();
   }

   /** @deprecated */
   @Deprecated
   public boolean isPowerAlwaysOn() {
      return this.getHeader().isAlwaysOn();
   }

   public PowerState getPower(BlockFace from) {
      return this.sign.getPower(from);
   }

   public boolean isPowered(BlockFace from) {
      if (this.sign.getHeader().isAlwaysOff()) {
         return false;
      } else {
         return this.sign.getHeader().isAlwaysOn() || this.sign.getHeader().isInverted() != this.getPower(from).hasPower();
      }
   }

   public boolean isPowered() {
      SignActionHeader header = this.sign.getHeader();
      if (header.isAlwaysOff()) {
         return false;
      } else if (this.actionType == SignActionType.REDSTONE_ON) {
         return true;
      } else if (!header.onPowerRising() && !header.onPowerFalling()) {
         if (this.actionType == SignActionType.REDSTONE_OFF) {
            return false;
         } else {
            return header.isAlwaysOn() || this.isPoweredRaw(header.isInverted());
         }
      } else {
         return false;
      }
   }

   public boolean isPoweredRaw(boolean invert) {
      boolean result;
      BlockFace[] var3;
      int var4;
      int var5;
      BlockFace face;
      if (invert) {
         result = true;
         var3 = FaceUtil.BLOCK_SIDES;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            face = var3[var5];
            result &= this.sign.getPower(face) != PowerState.ON;
         }

         return result;
      } else {
         result = false;
         var3 = FaceUtil.BLOCK_SIDES;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            face = var3[var5];
            result |= this.sign.getPower(face).hasPower();
         }

         return result;
      }
   }

   public boolean isPoweredFacing() {
      return this.actionType == SignActionType.REDSTONE_ON || this.isFacing() && this.isPowered();
   }

   public RailLookup.TrackedSign getTrackedSign() {
      return this.sign;
   }

   public Block getBlock() {
      return this.sign.signBlock;
   }

   public Block getAttachedBlock() {
      return this.sign.getAttachedBlock();
   }

   public RailPiece getRailPiece() {
      return this.sign.getRail();
   }

   public RailType getRailType() {
      return this.getRailPiece().type();
   }

   public Block getRails() {
      return this.getRailPiece().block();
   }

   public World getWorld() {
      return this.sign.signBlock.getWorld();
   }

   public boolean hasRails() {
      return !this.getRailPiece().isNone();
   }

   /** @deprecated */
   @Deprecated
   public BlockFace getRailDirection() {
      RailPiece rail = this.getRailPiece();
      if (rail.isNone()) {
         return null;
      } else {
         if (this.raildirection == null) {
            this.raildirection = rail.type().getDirection(rail.block());
         }

         return this.raildirection;
      }
   }

   public Location getCenterLocation() {
      RailPiece railPiece = this.getRailPiece();
      return railPiece.isNone() ? null : railPiece.type().getSpawnLocation(railPiece.block(), this.getFacing());
   }

   public Location getRailLocation() {
      RailPiece rail = this.sign.getRail();
      return rail.isNone() ? null : rail.block().getLocation().add(0.5D, 0.0D, 0.5D);
   }

   public Location getLocation() {
      return this.sign.signBlock.getLocation();
   }

   public BlockFace getFacing() {
      return this.sign.getFacing();
   }

   public boolean isFacing() {
      MinecartMember<?> member = this.getMember();
      if (member == null) {
         return false;
      } else {
         return !member.isMoving() ? true : this.isEnterActivated();
      }
   }

   public Sign getSign() {
      return this.sign.sign;
   }

   public String[] getExtraLinesBelow() {
      return this.sign.getExtraLines();
   }

   public boolean isConnectedRails(BlockFace direction) {
      return Util.isConnectedRails(this.getRailPiece(), direction);
   }

   public Collection<MinecartGroup> getRCTrainGroups() {
      return MinecartGroup.matchAll(this.getRCName());
   }

   public Collection<TrainProperties> getRCTrainProperties() {
      return TrainProperties.matchAll(this.getRCName());
   }

   public String getRCName() {
      return this.isRCSign() ? this.sign.getHeader().getRemoteName() : null;
   }

   public MinecartMember<?> getMember() {
      if (this.member == null) {
         if (!this.memberchecked) {
            this.member = this.hasRails() ? MinecartMemberStore.getAt(this.getRailPiece().block()) : null;
            this.memberchecked = true;
         }

         if (this.member == null && this.group != null && !this.group.isEmpty()) {
            if (this.actionType == SignActionType.GROUP_LEAVE) {
               this.member = this.group.tail();
            } else {
               Iterator var1 = this.group.iterator();

               while(var1.hasNext()) {
                  MinecartMember<?> member = (MinecartMember)var1.next();
                  if (member.getSignTracker().containsSign(this.sign)) {
                     this.member = member;
                     break;
                  }
               }

               if (this.member == null) {
                  this.member = this.group.head();
               }
            }
         }
      }

      return this.member != null && this.member.isInteractable() ? this.member : null;
   }

   public void setMember(MinecartMember<?> member) {
      this.member = member;
      this.memberchecked = true;
      this.group = member.getGroup();
   }

   public void setGroup(MinecartGroup group) {
      this.member = null;
      this.memberchecked = true;
      this.group = group;
   }

   public boolean hasMember() {
      return this.getMember() != null;
   }

   public boolean isWatchedDirectionsDefined() {
      return this.getHeader().hasEnterDirections();
   }

   public BlockFace[] getWatchedDirections() {
      return RailEnterDirection.toFacesOnly(this.getEnterDirections());
   }

   public RailEnterDirection[] getEnterDirections() {
      if (this.enterDirections == null) {
         if (this.sign.getHeader().hasEnterDirections()) {
            this.enterDirections = this.sign.getHeader().getEnterDirections(this.getRailPiece(), this.getFacing().getOppositeFace());
         } else if (TCConfig.trainsCheckSignFacing) {
            BlockFace[] faces = this.getRailPiece().type().getSignTriggerDirections(this.getRailPiece().block(), this.getBlock(), this.getFacing());
            this.enterDirections = new RailEnterDirection[faces.length];

            for(int i = 0; i < faces.length; ++i) {
               this.enterDirections[i] = RailEnterDirection.toFace(faces[i]);
            }
         } else {
            this.enterDirections = RailEnterDirection.ALL;
         }
      }

      return this.enterDirections;
   }

   public boolean isEnterActivated(RailState state) {
      RailEnterDirection[] var2 = this.getEnterDirections();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RailEnterDirection dir = var2[var4];
         if (dir.match(state)) {
            return true;
         }
      }

      return false;
   }

   public boolean isEnterActivated() {
      RailState state = this.getCartEnterState();
      return state != null && this.isEnterActivated(state);
   }

   public BlockFace[] getSpawnDirections() {
      BlockFace[] watched = this.getWatchedDirections();
      BlockFace[] spawndirs = new BlockFace[watched.length];

      for(int i = 0; i < spawndirs.length; ++i) {
         spawndirs[i] = watched[i].getOppositeFace();
      }

      return spawndirs;
   }

   public boolean isWatchedDirection(BlockFace direction) {
      return LogicUtil.contains(RailEnterDirection.toFace(direction), this.getEnterDirections());
   }

   public boolean isWatchedDirection(Vector direction) {
      RailEnterDirection[] var2 = this.getEnterDirections();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RailEnterDirection dir = var2[var4];
         if (dir.motionDot(direction) > 0.0D) {
            return true;
         }
      }

      return false;
   }

   public MinecartGroup getGroup() {
      if (this.group != null) {
         return this.group;
      } else {
         MinecartMember<?> mm = this.getMember();
         return mm == null ? null : mm.getGroup();
      }
   }

   public boolean hasGroup() {
      return this.getGroup() != null;
   }

   public Collection<MinecartMember<?>> getMembers() {
      if (this.isTrainSign()) {
         return (Collection)(this.hasGroup() ? this.getGroup() : Collections.EMPTY_LIST);
      } else if (this.isCartSign()) {
         return this.hasMember() ? Collections.singletonList(this.getMember()) : Collections.EMPTY_LIST;
      } else if (!this.isRCSign()) {
         return Collections.EMPTY_LIST;
      } else {
         ArrayList<MinecartMember<?>> members = new ArrayList();
         Iterator var2 = this.getRCTrainGroups().iterator();

         while(var2.hasNext()) {
            MinecartGroup group = (MinecartGroup)var2.next();
            members.addAll(group);
         }

         return members;
      }
   }

   public String getLine(int index) {
      return Util.cleanSignLine(this.sign.getLine(index));
   }

   public String[] getLines() {
      String[] lines = new String[4];

      for(int i = 0; i < 4; ++i) {
         lines[i] = Util.cleanSignLine(this.sign.getLine(i));
      }

      return lines;
   }

   public void setLine(int index, String line) {
      this.sign.setLine(index, line);
   }

   public SignActionMode getMode() {
      return this.getHeader().getMode();
   }

   public boolean isCartSign() {
      return this.getHeader().isCart();
   }

   public boolean isTrainSign() {
      return this.getHeader().isTrain();
   }

   public boolean isRCSign() {
      return this.getHeader().isRC();
   }

   public boolean isLine(int line, String... texttypes) {
      String linetext = this.getLine(line).toLowerCase(Locale.ENGLISH);
      String[] var4 = texttypes;
      int var5 = texttypes.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String type = var4[var6];
         if (linetext.startsWith(type)) {
            return true;
         }
      }

      return false;
   }

   public boolean isType(String... signtypes) {
      if (this.getHeader().isValid()) {
         String s = this.lowerSecondCleanedLine;
         String[] var3 = signtypes;
         int var4 = signtypes.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String type = var3[var5];
            if (s.startsWith(type)) {
               return true;
            }
         }
      }

      return false;
   }

   public String getLowerCaseSecondCleanedLine() {
      return this.lowerSecondCleanedLine;
   }

   public String toString() {
      Block signBlock = this.sign.signBlock;
      String text = "{ block=[" + signBlock.getX() + "," + signBlock.getY() + "," + signBlock.getZ() + "]";
      text = text + ", action=" + this.actionType;
      text = text + ", watched=[";

      for(int i = 0; i < this.getWatchedDirections().length; ++i) {
         if (i > 0) {
            text = text + ",";
         }

         text = text + this.getWatchedDirections()[i].name();
      }

      text = text + "]";
      if (this.sign == null) {
         text = text + " }";
      } else {
         text = text + ", lines=";
         String[] lines = this.getLines();

         for(int i = 0; i < lines.length; ++i) {
            if (i > 0 && lines[i].length() > 0) {
               text = text + " ";
            }

            text = text + lines[i];
         }

         text = text + " }";
      }

      return text;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancel) {
      this.cancelled = cancel;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
