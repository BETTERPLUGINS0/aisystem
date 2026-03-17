package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.common.wrappers.PlayerAbilities;
import com.bergerkiller.bukkit.common.wrappers.RelativeFlags;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInAbilitiesHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInFlyingHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInSteerVehicleHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutAbilitiesHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityVelocityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutPositionHandle;
import com.bergerkiller.generated.net.minecraft.server.level.EntityPlayerHandle;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerVelocityController {
   private static final double INPUT_MOTION = 0.49000000953674316D;
   private static final double INPUT_MOTION_DIAG = 0.3535533905932738D;
   private static final double MIN_MOTION = 0.003D;
   private static final PlayerVelocityController.MovementFrictionUpdate FRICTION_UPDATE = new PlayerVelocityController.MovementFrictionUpdate();
   private final Player player;
   private PlayerVelocityController.PositionTracker tracker = null;
   private final PlayerVelocityController.SentPositionChain sentPositions = new PlayerVelocityController.SentPositionChain();
   private boolean isSynchronized = false;
   private boolean syncAsArmorstand = true;
   private boolean isFlightForced = false;
   private Vector lastSyncPos = null;
   private volatile boolean translateVehicleSteer = false;

   public PlayerVelocityController(Player player) {
      this.player = player;
   }

   public void setSyncAsArmorstand(boolean sync) {
      this.syncAsArmorstand = sync;
      if (!sync) {
         this.lastSyncPos = null;
      }

   }

   public void translateVehicleSteer(boolean translate) {
      this.translateVehicleSteer = translate;
   }

   public synchronized PlayerVelocityController.HorizontalPlayerInput horizontalInput() {
      PlayerVelocityController.PositionTracker tracker = this.tracker;
      return tracker == null ? PlayerVelocityController.HorizontalPlayerInput.NONE : tracker.input.horizontalInput;
   }

   public synchronized PlayerVelocityController.VerticalPlayerInput verticalInput() {
      PlayerVelocityController.PositionTracker tracker = this.tracker;
      return tracker == null ? PlayerVelocityController.VerticalPlayerInput.NONE : tracker.input.verticalInput;
   }

   public synchronized void stop() {
      if (this.tracker != null) {
         TrainCarts.plugin.unregister(this.tracker);
      }

      if (this.isFlightForced) {
         this.player.setAllowFlight(false);
         this.isFlightForced = false;
      }

   }

   public synchronized void setPosition(Vector position) {
      if (this.syncAsArmorstand) {
         if (this.lastSyncPos == null) {
            this.lastSyncPos = position.clone();
         } else {
            this.lastSyncPos.add(position.clone().subtract(this.lastSyncPos).multiply(0.3333333333333333D));
         }

         position = this.lastSyncPos;
      }

      if (this.tracker == null) {
         this.tracker = new PlayerVelocityController.PositionTracker(this.player);
         TrainCarts.plugin.register(this.tracker, new PacketType[]{PacketType.IN_POSITION, PacketType.IN_POSITION_LOOK, PacketType.IN_ABILITIES});
      }

      if (!this.player.isFlying()) {
         if (!this.player.getAllowFlight()) {
            this.isFlightForced = true;
            this.player.setAllowFlight(true);
         }

         this.player.setFlying(true);
      }

      if (this.sentPositions.size() > 40) {
         this.sentPositions.clear();
         this.isSynchronized = false;
      }

      if (this.isSynchronized) {
         Vector diff = position.clone().subtract(this.sentPositions.getCurrentPosition());
         if (Math.abs(diff.getX()) < 0.003D) {
            diff.setX(0.0D);
         }

         if (Math.abs(diff.getY()) < 0.003D) {
            diff.setY(0.0D);
         }

         if (Math.abs(diff.getZ()) < 0.003D) {
            diff.setZ(0.0D);
         }

         PacketPlayOutEntityVelocityHandle p = PacketPlayOutEntityVelocityHandle.createNew(this.player.getEntityId(), diff.getX(), diff.getY(), diff.getZ());
         PacketUtil.sendPacket(this.player, p);
         this.sentPositions.add(new PlayerVelocityController.SentMotionUpdate(new Vector(p.getMotX(), p.getMotY(), p.getMotZ())));
      } else {
         PacketPlayOutEntityVelocityHandle p2 = PacketPlayOutEntityVelocityHandle.createNew(this.player.getEntityId(), 0.0D, 0.0D, 0.0D);
         PacketUtil.sendPacket(this.player, p2);
         PacketUtil.sendPacket(this.player, PacketPlayOutPositionHandle.createNew(position.getX(), position.getY(), position.getZ(), 0.0F, 0.0F, RelativeFlags.ABSOLUTE_POSITION.withRelativeRotation()));
         this.sentPositions.add(new PlayerVelocityController.SentAbsoluteUpdate(position.clone()));
      }

   }

   private synchronized void receiveInput(PlayerVelocityController.PlayerPositionInput input) {
      PlayerVelocityController.HorizontalPlayerInput[] var2 = input.horizontalInput.getNextLikelyInputs();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PlayerVelocityController.HorizontalPlayerInput hor = var2[var4];
         PlayerVelocityController.ConsumeResult result = this.sentPositions.tryConsumeHorizontalInput(input, hor);
         if (result != PlayerVelocityController.ConsumeResult.FAILED) {
            this.isSynchronized = result.isSynchronized();
            return;
         }
      }

      input.setLastMotionUsingPositionChanges();
      if (this.isSynchronized) {
         this.sentPositions.clear();
      }

      this.isSynchronized = false;
   }

   private static void log(String msg) {
      TrainCarts.plugin.getLogger().log(Level.INFO, msg);
   }

   private static boolean isVectorExactlyEqual(Vector v0, Vector v1) {
      return v0.getX() == v1.getX() && v0.getY() == v1.getY() && v0.getZ() == v1.getZ();
   }

   private static String strVec(Vector v) {
      return v.getX() + " // " + v.getY() + " // " + v.getZ();
   }

   private static final class MovementFrictionUpdate extends PlayerVelocityController.SentPositionUpdate {
      private MovementFrictionUpdate() {
         super(null);
      }

      public void apply(Vector position, Vector additionalMotion, Vector lastMotion, Vector outMotion) {
         outMotion.setX(lastMotion.getX() * 0.9100000262260437D);
         outMotion.setY(lastMotion.getY() * 0.6D);
         outMotion.setZ(lastMotion.getZ() * 0.9100000262260437D);
         if (Math.abs(outMotion.getX()) < 0.003D) {
            outMotion.setX(0.0D);
         }

         if (Math.abs(outMotion.getZ()) < 0.003D) {
            outMotion.setZ(0.0D);
         }

         outMotion.add(additionalMotion);
         if (Math.abs(outMotion.getY()) < 0.003D) {
            outMotion.setY(0.0D);
         }

         position.add(outMotion);
      }

      public Vector getMotion(Vector previousPosition) {
         return new Vector();
      }

      // $FF: synthetic method
      MovementFrictionUpdate(Object x0) {
         this();
      }
   }

   private static final class PlayerPositionInput {
      public final Player player;
      public final Vector lastPosition;
      public final Vector lastMotion;
      public final Vector currPosition;
      public PlayerVelocityController.ForwardMotion currForward;
      public float currSpeed;
      public PlayerVelocityController.HorizontalPlayerInput horizontalInput;
      public PlayerVelocityController.VerticalPlayerInput verticalInput;

      public PlayerPositionInput(Player player) {
         this.player = player;
         this.lastPosition = player.getLocation().toVector();
         this.lastMotion = player.getVelocity();
         this.currPosition = player.getLocation().toVector();
         this.currForward = PlayerVelocityController.ForwardMotion.get(player.getEyeLocation().getYaw());
         this.currSpeed = 0.1F;
         this.horizontalInput = PlayerVelocityController.HorizontalPlayerInput.NONE;
         this.verticalInput = PlayerVelocityController.VerticalPlayerInput.NONE;
      }

      public void setLastMotionUsingPositionChanges() {
         this.lastMotion.setX(this.currPosition.getX() - this.lastPosition.getX());
         this.lastMotion.setY(this.currPosition.getY() - this.lastPosition.getY());
         this.lastMotion.setZ(this.currPosition.getZ() - this.lastPosition.getZ());
      }

      public void updateLast() {
         MathUtil.setVector(this.lastPosition, this.currPosition);
      }
   }

   private class PositionTracker implements PacketListener {
      private final PlayerVelocityController.PlayerPositionInput input;
      private boolean lastPositionWasLook = false;

      public PositionTracker(Player player) {
         this.input = new PlayerVelocityController.PlayerPositionInput(player);
      }

      public void onPacketReceive(PacketReceiveEvent event) {
         if (event.getPlayer() == PlayerVelocityController.this.player) {
            if (event.getType() != PacketType.IN_POSITION && event.getType() != PacketType.IN_POSITION_LOOK) {
               if (event.getType() == PacketType.IN_ABILITIES) {
                  PacketPlayInAbilitiesHandle px = PacketPlayInAbilitiesHandle.createHandle(event.getPacket().getHandle());
                  if (!px.isFlying()) {
                     event.setCancelled(true);
                     PlayerAbilities pa = EntityPlayerHandle.fromBukkit(event.getPlayer()).getAbilities();
                     PacketPlayOutAbilitiesHandle pp = PacketPlayOutAbilitiesHandle.createNew(pa);
                     PacketUtil.queuePacket(event.getPlayer(), pp);
                  }
               }
            } else {
               PacketPlayInFlyingHandle p = PacketPlayInFlyingHandle.createHandle(event.getPacket().getHandle());
               synchronized(PlayerVelocityController.this) {
                  PlayerVelocityController.PlayerPositionInput input = this.input;
                  MathUtil.setVector(input.currPosition, p.getX(), p.getY(), p.getZ());
                  if (event.getType() == PacketType.IN_POSITION_LOOK) {
                     input.currForward = PlayerVelocityController.ForwardMotion.get(p.getYaw());
                     this.lastPositionWasLook = true;
                  } else if (this.lastPositionWasLook) {
                     this.lastPositionWasLook = false;
                     if (PlayerVelocityController.isVectorExactlyEqual(input.lastPosition, input.currPosition)) {
                        return;
                     }
                  }

                  PlayerVelocityController.this.receiveInput(input);
                  input.updateLast();
                  if (PlayerVelocityController.this.translateVehicleSteer) {
                     PacketPlayInSteerVehicleHandle steer = PacketPlayInSteerVehicleHandle.createNew(input.horizontalInput.left(), input.horizontalInput.right(), input.horizontalInput.forwards(), input.horizontalInput.backwards(), input.verticalInput == PlayerVelocityController.VerticalPlayerInput.JUMP, input.verticalInput == PlayerVelocityController.VerticalPlayerInput.SNEAK, false);
                     PacketUtil.receivePacket(PlayerVelocityController.this.player, steer);
                  }
               }
            }

         }
      }

      public void onPacketSend(PacketSendEvent event) {
      }
   }

   private static final class SentPositionChain extends PlayerVelocityController.SentPositionUpdate {
      private final Vector currentPosition;
      private PlayerVelocityController.SentPositionUpdate last;
      private int count;

      private SentPositionChain() {
         super(null);
         this.currentPosition = new Vector();
         this.last = this;
         this.count = 0;
      }

      public void calcCurrentPosition(Vector startPosition, Vector additionalMotion) {
         MathUtil.setVector(this.currentPosition, startPosition);
         this.apply(this.currentPosition, additionalMotion, new Vector(), new Vector());
      }

      public Vector getCurrentPosition() {
         return this.currentPosition;
      }

      public PlayerVelocityController.SentPositionUpdate getLast() {
         return this.last;
      }

      public int size() {
         return this.count;
      }

      public void apply(Vector position, Vector additionalMotion, Vector lastMotion, Vector outMotion) {
         for(PlayerVelocityController.SentPositionUpdate u = this.next; u != null; u = u.next) {
            u.apply(position, additionalMotion, lastMotion, outMotion);
         }

      }

      public Vector getMotion(Vector previousPosition) {
         return new Vector();
      }

      public PlayerVelocityController.ConsumeResult tryConsumeHorizontalInput(PlayerVelocityController.PlayerPositionInput input, PlayerVelocityController.HorizontalPlayerInput horizontalInput) {
         Vector additionalMotion = horizontalInput.getMotion(input.currForward, input.currSpeed);
         Vector outMotion = new Vector();
         Vector tmp = new Vector();
         PlayerVelocityController.SentPositionUpdate curr = this.next;
         if (curr != null) {
            if (curr.detectAsInput(input, horizontalInput, additionalMotion, outMotion, tmp)) {
               this.setStart(curr.next);
               if (additionalMotion.getX() != 0.0D || additionalMotion.getY() != 0.0D || additionalMotion.getZ() != 0.0D) {
                  this.calcCurrentPosition(input.currPosition, additionalMotion);
               }

               return PlayerVelocityController.ConsumeResult.OK;
            }

            for(int n = 1; curr.next != null; ++n) {
               curr = curr.next;
               if (curr.detectAsInput(input, horizontalInput, additionalMotion, outMotion, tmp)) {
                  this.setStart(curr.next);
                  this.calcCurrentPosition(input.currPosition, additionalMotion);
                  return n > 5 ? PlayerVelocityController.ConsumeResult.LARGE_PACKET_DROP : PlayerVelocityController.ConsumeResult.OK;
               }
            }
         }

         if (PlayerVelocityController.FRICTION_UPDATE.detectAsInput(input, horizontalInput, additionalMotion, outMotion, tmp)) {
            this.calcCurrentPosition(input.currPosition, additionalMotion);
            return PlayerVelocityController.ConsumeResult.OK;
         } else {
            return PlayerVelocityController.ConsumeResult.FAILED;
         }
      }

      public void setStart(PlayerVelocityController.SentPositionUpdate update) {
         this.next = update;
         if (update == null) {
            this.last = this;
            this.count = 0;
         } else {
            int new_count = 1;

            PlayerVelocityController.SentPositionUpdate new_last;
            for(new_last = update; new_last.next != null; ++new_count) {
               new_last = new_last.next;
            }

            this.last = new_last;
            this.count = new_count;
         }

      }

      public void clear() {
         this.next = null;
         this.last = this;
         this.count = 0;
      }

      public void add(PlayerVelocityController.SentPositionUpdate update) {
         this.last.next = update;
         this.last = update;
         ++this.count;
         update.apply(this.currentPosition, new Vector(), new Vector(), new Vector());
      }

      // $FF: synthetic method
      SentPositionChain(Object x0) {
         this();
      }
   }

   public static enum HorizontalPlayerInput {
      NONE(0.0D, 0.0D),
      FORWARDS(0.0D, 0.49000000953674316D),
      BACKWARDS(0.0D, -0.49000000953674316D),
      LEFT(0.49000000953674316D, 0.0D),
      RIGHT(-0.49000000953674316D, 0.0D),
      FORWARDS_LEFT(0.3535533905932738D, 0.3535533905932738D),
      FORWARDS_RIGHT(-0.3535533905932738D, 0.3535533905932738D),
      BACKWARDS_LEFT(0.3535533905932738D, -0.3535533905932738D),
      BACKWARDS_RIGHT(-0.3535533905932738D, -0.3535533905932738D);

      private final double xxa;
      private final double zza;
      private PlayerVelocityController.HorizontalPlayerInput[] next;

      private HorizontalPlayerInput(double xxa, double zza) {
         this.xxa = xxa;
         this.zza = zza;
      }

      public float forwardsSteerInput() {
         return (float)this.zza;
      }

      public boolean forwards() {
         return this.zza > 0.0D;
      }

      public boolean backwards() {
         return this.zza < 0.0D;
      }

      public float sidewaysSteerInput() {
         return (float)this.xxa;
      }

      public boolean left() {
         return this.xxa > 0.0D;
      }

      public boolean right() {
         return this.xxa < 0.0D;
      }

      private void setNext(PlayerVelocityController.HorizontalPlayerInput... next) {
         this.next = next;
      }

      public PlayerVelocityController.HorizontalPlayerInput[] getNextLikelyInputs() {
         return this.next;
      }

      public Vector getMotion(PlayerVelocityController.ForwardMotion forward, float speed) {
         double speedDbl = (double)speed;
         double xxa = this.xxa * speedDbl;
         double zza = this.zza * speedDbl;
         return new Vector(zza * forward.dx + xxa * forward.dz, 0.0D, zza * forward.dz - xxa * forward.dx);
      }

      // $FF: synthetic method
      private static PlayerVelocityController.HorizontalPlayerInput[] $values() {
         return new PlayerVelocityController.HorizontalPlayerInput[]{NONE, FORWARDS, BACKWARDS, LEFT, RIGHT, FORWARDS_LEFT, FORWARDS_RIGHT, BACKWARDS_LEFT, BACKWARDS_RIGHT};
      }

      static {
         NONE.setNext(NONE, FORWARDS, LEFT, RIGHT, BACKWARDS, FORWARDS_LEFT, FORWARDS_RIGHT, BACKWARDS_LEFT, BACKWARDS_RIGHT);
         FORWARDS.setNext(FORWARDS, NONE, FORWARDS_LEFT, FORWARDS_RIGHT, BACKWARDS, LEFT, RIGHT, BACKWARDS_LEFT, BACKWARDS_RIGHT);
         BACKWARDS.setNext(BACKWARDS, NONE, BACKWARDS_LEFT, BACKWARDS_RIGHT, FORWARDS, LEFT, RIGHT, FORWARDS_LEFT, FORWARDS_RIGHT);
         LEFT.setNext(LEFT, NONE, FORWARDS_LEFT, BACKWARDS_LEFT, RIGHT, FORWARDS, BACKWARDS, FORWARDS_RIGHT, BACKWARDS_RIGHT);
         RIGHT.setNext(RIGHT, NONE, FORWARDS_RIGHT, BACKWARDS_RIGHT, LEFT, FORWARDS, BACKWARDS, FORWARDS_LEFT, BACKWARDS_LEFT);
         FORWARDS_LEFT.setNext(FORWARDS_LEFT, FORWARDS, LEFT, NONE, FORWARDS_RIGHT, RIGHT, BACKWARDS, BACKWARDS_LEFT, BACKWARDS_RIGHT);
         FORWARDS_RIGHT.setNext(FORWARDS_RIGHT, FORWARDS, RIGHT, NONE, FORWARDS_LEFT, LEFT, BACKWARDS, BACKWARDS_RIGHT, BACKWARDS_LEFT);
         BACKWARDS_LEFT.setNext(BACKWARDS_LEFT, BACKWARDS, LEFT, NONE, BACKWARDS_RIGHT, FORWARDS, RIGHT, FORWARDS_LEFT, FORWARDS_RIGHT);
         BACKWARDS_RIGHT.setNext(BACKWARDS_RIGHT, BACKWARDS, RIGHT, NONE, BACKWARDS_LEFT, FORWARDS, LEFT, FORWARDS_RIGHT, FORWARDS_LEFT);
      }
   }

   public static enum VerticalPlayerInput {
      NONE(0.0F),
      SNEAK(-3.0F),
      JUMP(3.0F);

      private final float yya;

      private VerticalPlayerInput(float yya) {
         this.yya = yya;
      }

      public double getMotion(float speed) {
         return 0.5D * (double)(speed * this.yya);
      }

      // $FF: synthetic method
      private static PlayerVelocityController.VerticalPlayerInput[] $values() {
         return new PlayerVelocityController.VerticalPlayerInput[]{NONE, SNEAK, JUMP};
      }
   }

   private static final class SentMotionUpdate extends PlayerVelocityController.SentPositionUpdate {
      private final Vector motion;

      public SentMotionUpdate(Vector motion) {
         super(null);
         this.motion = motion;
      }

      public void apply(Vector position, Vector additionalMotion, Vector lastMotion, Vector outMotion) {
         MathUtil.setVector(outMotion, this.motion);
         outMotion.add(additionalMotion);
         if (Math.abs(outMotion.getY()) < 0.003D) {
            outMotion.setY(0.0D);
         }

         position.add(outMotion);
      }

      public Vector getMotion(Vector previousPosition) {
         return this.motion.clone();
      }
   }

   private abstract static class SentPositionUpdate {
      protected PlayerVelocityController.SentPositionUpdate next;

      private SentPositionUpdate() {
         this.next = null;
      }

      public abstract void apply(Vector var1, Vector var2, Vector var3, Vector var4);

      public abstract Vector getMotion(Vector var1);

      public String debugPrediction(PlayerVelocityController.PlayerPositionInput input) {
         Vector newPosition = input.lastPosition.clone();
         Vector additionalMotion = input.horizontalInput.getMotion(input.currForward, input.currSpeed);
         additionalMotion.setY(input.verticalInput.getMotion(input.currSpeed));
         Vector outMotion = new Vector();
         this.apply(newPosition, additionalMotion, input.lastMotion, outMotion);
         return "    " + this.getClass().getSimpleName() + ": " + this.getMotion(input.lastPosition) + "\n         Actual " + PlayerVelocityController.strVec(input.currPosition) + "\n         Predicted " + PlayerVelocityController.strVec(newPosition);
      }

      public boolean detectAsInput(PlayerVelocityController.PlayerPositionInput input, PlayerVelocityController.HorizontalPlayerInput horizontalInput, Vector additionalMotion, Vector outMotion, Vector tmp) {
         additionalMotion.setY(input.verticalInput.getMotion(input.currSpeed));
         MathUtil.setVector(tmp, input.lastPosition);
         this.apply(tmp, additionalMotion, input.lastMotion, outMotion);
         Vector currPos = input.currPosition;
         if (tmp.getX() == currPos.getX() && tmp.getZ() == currPos.getZ()) {
            if (tmp.getY() != currPos.getY()) {
               boolean foundMatchingVerticalInput = false;
               PlayerVelocityController.VerticalPlayerInput[] var8 = PlayerVelocityController.VerticalPlayerInput.values();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  PlayerVelocityController.VerticalPlayerInput vertInput = var8[var10];
                  if (vertInput != input.verticalInput) {
                     additionalMotion.setY(vertInput.getMotion(input.currSpeed));
                     MathUtil.setVector(tmp, input.lastPosition);
                     this.apply(tmp, additionalMotion, input.lastMotion, outMotion);
                     if (tmp.getY() == currPos.getY()) {
                        input.verticalInput = vertInput;
                        foundMatchingVerticalInput = true;
                        break;
                     }
                  }
               }

               if (!foundMatchingVerticalInput) {
                  return false;
               }
            }

            input.horizontalInput = horizontalInput;
            MathUtil.setVector(input.lastMotion, outMotion);
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      SentPositionUpdate(Object x0) {
         this();
      }
   }

   private static final class SentAbsoluteUpdate extends PlayerVelocityController.SentPositionUpdate {
      private final Vector position;

      public SentAbsoluteUpdate(Vector position) {
         super(null);
         this.position = position;
      }

      public void apply(Vector position, Vector additionalMotion, Vector lastMotion, Vector outMotion) {
         MathUtil.setVector(outMotion, additionalMotion);
         MathUtil.setVector(position, this.position);
         position.add(additionalMotion);
      }

      public Vector getMotion(Vector previousPosition) {
         return this.position.clone().subtract(previousPosition);
      }
   }

   private static enum ConsumeResult {
      FAILED(false),
      OK(true),
      LARGE_PACKET_DROP(false);

      private final boolean isSynchronized;

      private ConsumeResult(boolean isSynchronized) {
         this.isSynchronized = isSynchronized;
      }

      public boolean isSynchronized() {
         return this.isSynchronized;
      }

      // $FF: synthetic method
      private static PlayerVelocityController.ConsumeResult[] $values() {
         return new PlayerVelocityController.ConsumeResult[]{FAILED, OK, LARGE_PACKET_DROP};
      }
   }

   public static final class ForwardMotion {
      private static final float[] SIN_TABLE = new float[65536];
      public static final float DEG_TO_RAD = 0.017453292F;
      private static final PlayerVelocityController.ForwardMotion[] BY_YAW;
      public final double dx;
      public final double dz;

      public ForwardMotion(float dx, float dz) {
         this.dx = (double)dx;
         this.dz = (double)dz;
      }

      public static PlayerVelocityController.ForwardMotion get(float yaw) {
         float yaw_idx = yaw * 0.017453292F * 10430.378F;
         int idx_sin = (int)yaw_idx & '\uffff';
         int idx_cos = (int)(yaw_idx + 16384.0F) & '\uffff';
         return (idx_sin + 16384 & '\uffff') == idx_cos ? BY_YAW[idx_sin] : new PlayerVelocityController.ForwardMotion(-SIN_TABLE[idx_sin], SIN_TABLE[idx_cos]);
      }

      public String toString() {
         return "{dx=" + this.dx + ", dz=" + this.dz + "}";
      }

      static {
         int i;
         for(i = 0; i < SIN_TABLE.length; ++i) {
            SIN_TABLE[i] = (float)Math.sin((double)i * 3.141592653589793D * 2.0D / 65536.0D);
         }

         BY_YAW = new PlayerVelocityController.ForwardMotion[SIN_TABLE.length];

         for(i = 0; i < BY_YAW.length; ++i) {
            BY_YAW[i] = new PlayerVelocityController.ForwardMotion(-SIN_TABLE[i], SIN_TABLE[i + 16384 & '\uffff']);
         }

      }
   }
}
