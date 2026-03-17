package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.actions.MemberActionLaunchDirection;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.ActionTrackerGroup;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.utils.LauncherConfig;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;
import org.bukkit.util.Vector;

public class Station {
   private final SignActionEvent info;
   private final LauncherConfig launchConfig;
   private final double launchForce;
   private final long delay;
   private final BlockFace instruction;
   private final Direction nextDirection;
   private final double centerOffset;
   private boolean wasCentered;
   private boolean autoRoute;

   public Station(SignActionEvent info) {
      this(info, Station.StationConfig.fromSign(info));
   }

   public Station(SignActionEvent info, Station.StationConfig config) {
      this.wasCentered = false;
      this.autoRoute = false;
      this.info = info;
      this.delay = config.getDelay();
      this.instruction = config.getInstruction();
      this.launchForce = config.getLaunchSpeed();
      this.launchConfig = config.getLaunchConfig();
      this.centerOffset = config.getOffsetFromCenter();
      this.nextDirection = config.getNextDirection();
      this.autoRoute = config.isAutoRouting();
   }

   public SignActionEvent getSignInfo() {
      return this.info;
   }

   public String getTag() {
      return StringUtil.blockToString(this.info.getBlock());
   }

   public boolean hasDelay() {
      return this.delay > 0L;
   }

   public long getDelay() {
      return this.delay;
   }

   public LauncherConfig getLaunchConfig() {
      return this.launchConfig;
   }

   public BlockFace getInstruction() {
      return this.instruction;
   }

   public BlockFace getNextDirectionFace() {
      return this.getNextDirection().getDirectionLegacy(this.info.getFacing(), this.info.getMember().getDirection());
   }

   public Direction getNextDirection() {
      return this.nextDirection;
   }

   public boolean isAutoRouting() {
      return this.autoRoute;
   }

   public MinecartGroup getGroup() {
      return this.info.getGroup();
   }

   /** @deprecated */
   @Deprecated
   public MinecartMember<?> getCenterCart(int offset) {
      MinecartGroup group = this.getGroup();
      int size = group.size();
      if (this.info.isCartSign()) {
         return this.info.getMember();
      } else {
         int index;
         if ((size & 1) == 1) {
            index = (int)Math.floor((double)size / 2.0D);
            if (offset != 0 && size >= 3) {
               Location s = this.info.getCenterLocation();
               double d1 = ((CommonMinecart)((MinecartMember)group.get(index - 1)).getEntity()).loc.distance(s);
               double d2 = ((CommonMinecart)((MinecartMember)group.get(index + 1)).getEntity()).loc.distance(s);
               if (d1 < d2) {
                  index += offset;
               } else {
                  index -= offset;
               }
            }

            return (MinecartMember)group.get(index);
         } else {
            index = (int)Math.ceil((double)size / 2.0D) - 1;
            int mIdx2 = index + 1;
            Location s = this.info.getCenterLocation();
            double d1 = ((CommonMinecart)((MinecartMember)group.get(index)).getEntity()).loc.distance(s);
            double d2 = ((CommonMinecart)((MinecartMember)group.get(mIdx2)).getEntity()).loc.distance(s);
            return d1 > d2 ? (MinecartMember)group.get(index + offset) : (MinecartMember)group.get(mIdx2 - offset);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public MinecartMember<?> getCenterCart() {
      return this.getCenterCart(0);
   }

   public MinecartMember<?> getCenterPositionCart() {
      MinecartGroup group = this.getGroup();
      if (group.size() == 1) {
         return (MinecartMember)group.get(0);
      } else if (this.info.isCartSign()) {
         return this.info.getMember();
      } else {
         double total_size = 0.5D * (double)((CommonMinecart)group.head().getEntity()).getWidth();

         for(int i = 1; i < group.size(); ++i) {
            total_size += ((CommonMinecart)((MinecartMember)group.get(i)).getEntity()).loc.distance(((CommonMinecart)((MinecartMember)group.get(i - 1)).getEntity()).loc);
         }

         total_size += 0.5D * (double)((CommonMinecart)group.tail().getEntity()).getWidth();
         double half_size = total_size * 0.5D;
         double accum_size = 0.5D * (double)((CommonMinecart)group.head().getEntity()).getWidth();
         if (accum_size > half_size) {
            return group.head();
         } else {
            for(int i = 1; i < group.size(); ++i) {
               double new_accum_size = accum_size + ((CommonMinecart)((MinecartMember)group.get(i)).getEntity()).loc.distance(((CommonMinecart)((MinecartMember)group.get(i - 1)).getEntity()).loc);
               if (new_accum_size > half_size) {
                  double d_prev = half_size - accum_size;
                  double d_curr = new_accum_size - half_size;
                  if (d_prev < d_curr) {
                     return (MinecartMember)group.get(i - 1);
                  }

                  return (MinecartMember)group.get(i);
               }

               accum_size = new_accum_size;
            }

            return group.tail();
         }
      }
   }

   public void waitTrain(long delay) {
      this.waitTrainKeepLeversDown(delay);
      if (delay > 0L) {
         this.setLevers(false);
      }

   }

   public void waitTrainKeepLeversDown(long delay) {
      ActionTrackerGroup actions = this.info.getGroup().getActions();
      if (TCConfig.playHissWhenStopAtStation) {
         actions.addActionSizzle().addTag(this.getTag());
      }

      if (TCConfig.refillAtStations) {
         actions.addActionRefill().addTag(this.getTag());
      }

      this.setLevers(true);
      if (delay == Long.MAX_VALUE) {
         actions.addActionWaitForever().addTag(this.getTag());
      } else if (delay > 0L) {
         actions.addActionWait(delay).addTag(this.getTag());
      }

   }

   public void setLevers(boolean down) {
      this.info.getGroup().getActions().addActionSetSignOutput(this.info.getTrackedSign(), down).addTag(this.getTag());
   }

   public void centerTrain() {
      Station.CartToStationInfo stationInfo = this.getCartToStationInfo();
      if (!this.info.getGroup().getActions().hasAction() && stationInfo.distance <= 0.01D) {
         this.info.getGroup().stop();
      } else if (stationInfo.cartDir != null) {
         stationInfo.cart.getActions().addActionLaunch(stationInfo.cartDir, stationInfo.distance, 0.0D).addTag(this.getTag());
      } else {
         stationInfo.cart.getActions().addActionLaunch(stationInfo.centerLocation, 0.0D).addTag(this.getTag());
      }

      this.wasCentered = true;
   }

   public void launchTo(BlockFace direction) {
      if (!this.wasCentered) {
         Station.CartToStationInfo stationInfo = this.getCartToStationInfo();
         if (stationInfo.cartDir == direction && this.launchConfig.hasDistance()) {
            this.launchConfig.setDistance(this.launchConfig.getDistance() + stationInfo.distance);
         }
      }

      this.setLevers(false);
      MemberActionLaunchDirection action = this.getCenterPositionCart().getActions().addActionLaunch(direction, this.launchConfig, this.launchForce);
      action.addTag(this.getTag());
      this.wasCentered = false;
   }

   private Station.CartToStationInfo getCartToStationInfo() {
      Station.CartToStationInfo info = new Station.CartToStationInfo();
      info.cart = this.getCenterPositionCart();
      info.centerLocation = this.info.getCenterLocation();
      RailState centercart_state = info.cart.getRailTracker().getState();
      RailState centercart_state_inv = centercart_state.clone();
      centercart_state_inv.position().invertMotion();
      centercart_state_inv.initEnterDirection();
      info.distance = centercart_state.position().distance(info.centerLocation);
      info.cartDir = Util.vecToFace(info.cart.getRailTracker().getMotionVector(), false);
      info.centerMoveDir = info.cart.getRailTracker().getMotionVector();
      double maxDistance = 2.0D * info.distance;
      TrackWalkingPoint p = new TrackWalkingPoint(centercart_state);
      TrackWalkingPoint p_inv = new TrackWalkingPoint(centercart_state_inv);
      if (p.moveFindRail(this.info.getRails(), maxDistance)) {
         maxDistance = p.movedTotal;
         info.distance = p.movedTotal;
         info.centerMoveDir = p.state.motionVector();
      }

      if (p_inv.moveFindRail(this.info.getRails(), maxDistance)) {
         maxDistance = p_inv.movedTotal;
         info.distance = p_inv.movedTotal;
         info.centerMoveDir = p_inv.state.motionVector();
         info.cartDir = info.cartDir.getOppositeFace();
      }

      MinecartGroup group = this.getGroup();
      if (group.size() > 1 && !this.info.isCartSign()) {
         double center_size = 0.5D * (double)((CommonMinecart)((MinecartMember)group.get(0)).getEntity()).getWidth();
         double total_size = center_size;

         for(int i = 1; i < group.size(); ++i) {
            MinecartMember<?> m = (MinecartMember)group.get(i);
            total_size += 0.5D * (double)((CommonMinecart)m.getEntity()).getWidth();
            total_size += 0.5D * (double)((CommonMinecart)((MinecartMember)group.get(i - 1)).getEntity()).getWidth();
            total_size += m.getCartCouplerLength() + ((MinecartMember)group.get(i - 1)).getCartCouplerLength();
            if (m == info.cart) {
               center_size = total_size;
            }
         }

         total_size += 0.5D * (double)((CommonMinecart)group.tail().getEntity()).getWidth();
         info.distance += 0.5D * total_size - center_size;
      }

      if (this.centerOffset != 0.0D) {
         Vector stationMoveDir = info.centerMoveDir.clone();
         if (stationMoveDir.getX() + stationMoveDir.getY() + stationMoveDir.getZ() < 0.0D) {
            stationMoveDir.multiply(-1.0D);
         }

         Vector facingVec = FaceUtil.faceToVector(this.info.getFacing());
         facingVec = new Vector(facingVec.getZ(), facingVec.getY(), facingVec.getX());
         if (stationMoveDir.dot(facingVec) < 0.0D) {
            stationMoveDir.multiply(-1.0D);
         }

         if (stationMoveDir.dot(info.centerMoveDir) < 0.0D) {
            info.distance += this.centerOffset;
         } else {
            info.distance -= this.centerOffset;
         }
      }

      return info;
   }

   public static class StationConfig {
      private double _offsetFromCenter = 0.0D;
      private Direction _nextDirection;
      private double _launchSpeed;
      private LauncherConfig _launchConfig;
      private BlockFace _instruction;
      private long _delay;
      private boolean _autoRoute;
      private static final Pattern STATION_OFFSET_PATTERN = Pattern.compile("(?:^|\\s|[a-zA-Z])((?:\\-)?[\\d.,]+)m(?:$|\\s|[0-9\\-])");

      public StationConfig() {
         this._nextDirection = Direction.NONE;
         this._launchSpeed = TCConfig.launchForce;
         this._launchConfig = LauncherConfig.createDefault();
         this._instruction = null;
         this._delay = 0L;
         this._autoRoute = false;
      }

      public double getOffsetFromCenter() {
         return this._offsetFromCenter;
      }

      public void setOffsetFromCenter(double offset) {
         this._offsetFromCenter = offset;
      }

      public Direction getNextDirection() {
         return this._nextDirection;
      }

      public void setNextDirection(Direction nextDirection) {
         this._nextDirection = nextDirection;
      }

      public double getLaunchSpeed() {
         return this._launchSpeed;
      }

      public void setLaunchSpeed(double speed) {
         this._launchSpeed = speed;
      }

      public LauncherConfig getLaunchConfig() {
         return this._launchConfig;
      }

      public void setLaunchConfig(LauncherConfig config) {
         this._launchConfig = config;
      }

      public long getDelay() {
         return this._delay;
      }

      public void setDelay(long delay) {
         this._delay = delay;
      }

      public boolean isAutoRouting() {
         return this._autoRoute;
      }

      public void setAutoRouting(boolean autoRoute) {
         this._autoRoute = autoRoute;
      }

      public BlockFace getInstruction() {
         return this._instruction;
      }

      public void setInstruction(BlockFace instruction) {
         this._instruction = instruction;
      }

      public void setAutoModeUsingSign(SignActionEvent info) {
         String[] var2 = info.getLine(3).split(" ");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String part = var2[var4];
            if (part.equalsIgnoreCase("route")) {
               this.setAutoRouting(true);
            } else {
               Direction direction = Direction.parse(part);
               if (direction != Direction.NONE) {
                  this.setNextDirection(direction);
               } else {
                  this.setLaunchSpeed(parseLaunchForce(part, info));
               }
            }
         }

      }

      public void setInstructionUsingSign(SignActionEvent info) {
         boolean west;
         if (info.isRailsVertical()) {
            boolean up = info.isPowered(BlockFace.UP);
            west = info.isPowered(BlockFace.DOWN);
            if (up && !west) {
               this.setInstruction(BlockFace.UP);
            } else if (!up && west) {
               this.setInstruction(BlockFace.DOWN);
            } else if (info.isPowered()) {
               this.setInstruction(BlockFace.SELF);
            } else {
               this.setInstruction((BlockFace)null);
            }
         } else {
            Vector railDirection = info.getCartEnterDirection();
            if (Util.isDiagonal(railDirection)) {
               Sign sign_material = (Sign)BlockUtil.getData(info.getBlock(), Sign.class);
               BlockFace face_x;
               boolean south;
               if (info.getTrackedSign().isRealSign() && sign_material != null && !sign_material.isWallSign()) {
                  face_x = railDirection.getX() > 0.0D ? BlockFace.EAST : BlockFace.WEST;
                  BlockFace face_z = railDirection.getZ() > 0.0D ? BlockFace.SOUTH : BlockFace.NORTH;
                  south = info.isPowered(face_x) || info.isPowered(face_z);
                  boolean pow2 = info.isPowered(face_x.getOppositeFace()) || info.isPowered(face_z.getOppositeFace());
                  if (south && !pow2) {
                     this.setInstruction(FaceUtil.combine(face_x, face_z));
                  } else if (!south && pow2) {
                     this.setInstruction(FaceUtil.combine(face_x.getOppositeFace(), face_z.getOppositeFace()));
                  } else if (info.isPowered()) {
                     this.setInstruction(BlockFace.SELF);
                  } else {
                     this.setInstruction((BlockFace)null);
                  }
               } else {
                  face_x = info.getFacing();
                  boolean north;
                  if (FaceUtil.isAlongX(face_x)) {
                     north = info.isPowered(BlockFace.NORTH);
                     south = info.isPowered(BlockFace.SOUTH);
                     if (north && !south) {
                        this.setInstruction(BlockFace.NORTH);
                     } else if (south && !north) {
                        this.setInstruction(BlockFace.SOUTH);
                     } else if (info.isPowered()) {
                        this.setInstruction(BlockFace.SELF);
                     } else {
                        this.setInstruction((BlockFace)null);
                     }
                  } else {
                     north = info.isPowered(BlockFace.WEST);
                     south = info.isPowered(BlockFace.EAST);
                     if (north && !south) {
                        this.setInstruction(BlockFace.WEST);
                     } else if (south && !north) {
                        this.setInstruction(BlockFace.EAST);
                     } else if (info.isPowered()) {
                        this.setInstruction(BlockFace.SELF);
                     } else {
                        this.setInstruction((BlockFace)null);
                     }
                  }
               }
            } else {
               boolean east;
               if (Math.abs(railDirection.getX()) > Math.abs(railDirection.getZ())) {
                  west = info.isPowered(BlockFace.WEST);
                  east = info.isPowered(BlockFace.EAST);
                  if (west && !east) {
                     this.setInstruction(BlockFace.WEST);
                  } else if (east && !west) {
                     this.setInstruction(BlockFace.EAST);
                  } else if (info.isPowered()) {
                     this.setInstruction(BlockFace.SELF);
                  } else {
                     this.setInstruction((BlockFace)null);
                  }
               } else {
                  west = info.isPowered(BlockFace.NORTH);
                  east = info.isPowered(BlockFace.SOUTH);
                  if (west && !east) {
                     this.setInstruction(BlockFace.NORTH);
                  } else if (east && !west) {
                     this.setInstruction(BlockFace.SOUTH);
                  } else if (info.isPowered()) {
                     this.setInstruction(BlockFace.SELF);
                  } else {
                     this.setInstruction((BlockFace)null);
                  }
               }
            }
         }

      }

      public static Station.StationConfig fromSign(SignActionEvent info) {
         Station.StationConfig config = new Station.StationConfig();
         config.setDelay(ParseUtil.parseTime(info.getLine(2)));
         config.setAutoModeUsingSign(info);
         config.setInstructionUsingSign(info);
         String launchConfigStr = info.getLine(1).trim();
         int i = 0;

         while(true) {
            if (i == launchConfigStr.length()) {
               launchConfigStr = "";
               break;
            }

            char c = launchConfigStr.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
               launchConfigStr = launchConfigStr.substring(i);
               break;
            }

            ++i;
         }

         Matcher matcher = STATION_OFFSET_PATTERN.matcher(launchConfigStr);
         if (matcher.find()) {
            config.setOffsetFromCenter(ParseUtil.parseDouble(matcher.group(1), 0.0D));

            for(launchConfigStr = launchConfigStr.substring(0, matcher.start(1)) + " " + launchConfigStr.substring(matcher.end(1) + 1); launchConfigStr.startsWith(" "); launchConfigStr = launchConfigStr.substring(1)) {
            }
         }

         config.setLaunchConfig(LauncherConfig.parse(launchConfigStr));
         if (!config.getLaunchConfig().hasDuration() && !config.getLaunchConfig().hasDistance() && !config.getLaunchConfig().hasAcceleration() && config.getInstruction() != null) {
            BlockFace launchDir = config.getInstruction();
            if (launchDir == BlockFace.SELF) {
               launchDir = config.getNextDirection().getDirectionLegacy(info.getFacing(), info.getMember().getDirection());
            }

            double length = Util.calculateStraightLength(info.getRails(), launchDir);
            if (length == 0.0D) {
               ++length;
            }

            config.getLaunchConfig().setDistance(length);
         }

         return config;
      }

      private static double parseLaunchForce(String text, SignActionEvent info) {
         return text.equalsIgnoreCase("max") && info.hasGroup() ? info.getGroup().getProperties().getSpeedLimit() : Util.parseVelocity(text, TCConfig.launchForce);
      }
   }

   private static class CartToStationInfo {
      public MinecartMember<?> cart;
      public BlockFace cartDir;
      public Vector centerMoveDir;
      public double distance;
      public Location centerLocation;

      private CartToStationInfo() {
      }

      // $FF: synthetic method
      CartToStationInfo(Object x0) {
         this();
      }
   }
}
