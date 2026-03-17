package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.config.ObjectPosition;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public abstract class FirstPersonView {
   protected final CartAttachmentSeat seat;
   protected final AttachmentViewer player;
   private FirstPersonViewMode _liveMode;
   private FirstPersonViewMode _mode;
   private FirstPersonViewLockMode _lock;
   protected ObjectPosition _eyePosition;
   public static final float BODY_LOCK_FOV_LIMIT = 70.0F;
   private static final boolean HAS_EQUIPMENT_SEND_METHOD = Common.evaluateMCVersion(">=", "1.18");

   public FirstPersonView(CartAttachmentSeat seat, AttachmentViewer player) {
      this._liveMode = FirstPersonViewMode.DEFAULT;
      this._mode = FirstPersonViewMode.DYNAMIC;
      this._lock = FirstPersonViewLockMode.MOVE;
      this._eyePosition = new ObjectPosition();
      this.seat = seat;
      this.player = player;
   }

   public ObjectPosition getEyePosition() {
      return this._eyePosition;
   }

   public AttachmentViewer getViewer() {
      return this.player;
   }

   public boolean doesViewModeChangeRequireReset(FirstPersonViewMode newViewMode) {
      return newViewMode != this.getLiveMode() || newViewMode.hasFakePlayer();
   }

   protected Matrix4x4 getEyeTransform() {
      if (!this._eyePosition.isDefault()) {
         return Matrix4x4.multiply(this.seat.getTransform(), this._eyePosition.transform);
      } else {
         Matrix4x4 eye;
         if (this.getLiveMode() == FirstPersonViewMode.THIRD_P) {
            eye = this.seat.getTransform().clone();
            eye.translate(this.seat.seated.getThirdPersonCameraOffset());
            return eye;
         } else if (this.seat.useSmoothCoasters()) {
            eye = this.seat.getTransform().clone();
            eye.translate(this.seat.seated.getFirstPersonCameraOffset());
            return eye;
         } else {
            eye = new Matrix4x4();
            eye.translate(this.seat.seated.getFirstPersonCameraOffset());
            eye.multiply(this.seat.getTransform());
            return eye;
         }
      }
   }

   public Location getPlayerEyeLocation() {
      if (this.player != null) {
         Vector pos = this.getEyeTransform().toVector();
         Location eye = this.player.getPlayer().getEyeLocation();
         eye.setX(pos.getX());
         eye.setY(pos.getY());
         eye.setZ(pos.getZ());
         return eye;
      } else {
         return null;
      }
   }

   public abstract void makeVisible(AttachmentViewer var1, boolean var2);

   public abstract void makeHidden(AttachmentViewer var1, boolean var2);

   public abstract void onTick();

   public abstract void onMove(boolean var1);

   public FirstPersonViewMode getLiveMode() {
      return this._liveMode;
   }

   public void setLiveMode(FirstPersonViewMode liveMode) {
      this._liveMode = liveMode;
   }

   public FirstPersonViewMode getMode() {
      return this._mode;
   }

   public void setMode(FirstPersonViewMode mode) {
      this._mode = mode;
   }

   public FirstPersonViewLockMode getLockMode() {
      return this._lock;
   }

   public void setLockMode(FirstPersonViewLockMode lock) {
      this._lock = lock;
   }

   protected static void setPlayerVisible(AttachmentViewer player, boolean visible) {
      DataWatcher metaTmp = new DataWatcher();
      metaTmp.set(EntityHandle.DATA_FLAGS, (Byte)EntityUtil.getDataWatcher(player.getPlayer()).get(EntityHandle.DATA_FLAGS));
      metaTmp.setFlag(EntityHandle.DATA_FLAGS, 32, !visible);
      PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(player.getEntityId(), metaTmp, true);
      player.send((PacketHandle)metaPacket);
      if (visible) {
         PlayerInventory inv = player.getPlayer().getInventory();
         sendEquipment(player, EquipmentSlot.HEAD, inv.getHelmet());
         sendEquipment(player, EquipmentSlot.CHEST, inv.getChestplate());
         sendEquipment(player, EquipmentSlot.FEET, inv.getBoots());
         sendEquipment(player, EquipmentSlot.LEGS, inv.getLeggings());
      } else {
         sendEquipment(player, EquipmentSlot.HEAD, (ItemStack)null);
         sendEquipment(player, EquipmentSlot.CHEST, (ItemStack)null);
         sendEquipment(player, EquipmentSlot.FEET, (ItemStack)null);
         sendEquipment(player, EquipmentSlot.LEGS, (ItemStack)null);
      }

   }

   protected static void sendEquipment(AttachmentViewer player, EquipmentSlot slot, ItemStack item) {
      if (HAS_EQUIPMENT_SEND_METHOD) {
         sendEquipmentUsingBukkit(player.getPlayer(), slot, item);
      } else {
         player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(player.getEntityId(), slot, item));
      }

   }

   private static void sendEquipmentUsingBukkit(Player player, EquipmentSlot slot, ItemStack item) {
      if (item == null) {
         item = ItemUtil.emptyItem();
      }

      player.sendEquipmentChange(player, slot, item);
   }

   public static final class HeadRotation {
      public final float pitch;
      public final float yaw;
      public final float roll;
      public final Vector pyr;

      private HeadRotation(float pitch, float yaw, float roll) {
         this.pitch = pitch;
         this.yaw = yaw;
         this.roll = roll;
         this.pyr = new Vector(pitch, yaw, roll);
      }

      public FirstPersonView.HeadRotation flipVertical() {
         return new FirstPersonView.HeadRotation(180.0F - this.pitch, 180.0F + this.yaw, this.roll);
      }

      public FirstPersonView.HeadRotation ensureLevel() {
         return Math.abs(this.pitch) > 90.0F ? this.flipVertical() : this;
      }

      public static FirstPersonView.HeadRotation compute(Matrix4x4 eyeTransform) {
         return compute(eyeTransform.getRotation());
      }

      public static FirstPersonView.HeadRotation compute(Quaternion eyeOrientation) {
         Vector forward = eyeOrientation.forwardVector();
         Vector up = eyeOrientation.upVector();
         if (Math.abs(forward.getY()) < 0.999D) {
            FirstPersonView.HeadRotation rot = new FirstPersonView.HeadRotation(MathUtil.getLookAtPitch(forward.getX(), forward.getY(), forward.getZ()), MathUtil.getLookAtYaw(forward) + 90.0F, (float)eyeOrientation.getRoll());
            if (up.getY() < 0.0D) {
               rot = rot.flipVertical();
            }

            return rot;
         } else {
            float pitch;
            float yaw;
            float roll;
            if (forward.getY() > 0.0D) {
               pitch = -90.0F;
               yaw = MathUtil.getLookAtYaw(up) - 90.0F;
               roll = (float)eyeOrientation.getRoll();
            } else {
               pitch = 90.0F;
               yaw = MathUtil.getLookAtYaw(up) + 90.0F;
               roll = (float)eyeOrientation.getRoll();
            }

            return new FirstPersonView.HeadRotation(pitch, yaw, roll);
         }
      }

      public static FirstPersonView.HeadRotation of(float pitch, float yaw) {
         return new FirstPersonView.HeadRotation(pitch, yaw, 0.0F);
      }
   }
}
