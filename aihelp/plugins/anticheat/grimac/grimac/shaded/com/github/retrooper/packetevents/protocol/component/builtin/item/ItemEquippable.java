package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

public class ItemEquippable {
   private EquipmentSlot slot;
   private Sound equipSound;
   @Nullable
   private ResourceLocation assetId;
   @Nullable
   private ResourceLocation cameraOverlay;
   @Nullable
   private MappedEntitySet<EntityType> allowedEntities;
   private boolean dispensable;
   private boolean swappable;
   private boolean damageOnHurt;
   private boolean equipOnInteract;
   private boolean canBeSheared;
   private Sound shearingSound;

   @ApiStatus.Obsolete
   public ItemEquippable(EquipmentSlot slot, Sound equipSound, @Nullable ResourceLocation assetId, @Nullable ResourceLocation cameraOverlay, @Nullable MappedEntitySet<EntityType> allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt) {
      this(slot, equipSound, assetId, cameraOverlay, allowedEntities, dispensable, swappable, damageOnHurt, false);
   }

   @ApiStatus.Obsolete
   public ItemEquippable(EquipmentSlot slot, Sound equipSound, @Nullable ResourceLocation assetId, @Nullable ResourceLocation cameraOverlay, @Nullable MappedEntitySet<EntityType> allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt, boolean equipOnInteract) {
      this(slot, equipSound, assetId, cameraOverlay, allowedEntities, dispensable, swappable, damageOnHurt, equipOnInteract, false, Sounds.ITEM_SHEARS_SNIP);
   }

   public ItemEquippable(EquipmentSlot slot, Sound equipSound, @Nullable ResourceLocation assetId, @Nullable ResourceLocation cameraOverlay, @Nullable MappedEntitySet<EntityType> allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt, boolean equipOnInteract, boolean canBeSheared, Sound shearingSound) {
      this.slot = slot;
      this.equipSound = equipSound;
      this.assetId = assetId;
      this.cameraOverlay = cameraOverlay;
      this.allowedEntities = allowedEntities;
      this.dispensable = dispensable;
      this.swappable = swappable;
      this.damageOnHurt = damageOnHurt;
      this.equipOnInteract = equipOnInteract;
      this.canBeSheared = canBeSheared;
      this.shearingSound = shearingSound;
   }

   public static ItemEquippable read(PacketWrapper<?> wrapper) {
      EquipmentSlot slot = (EquipmentSlot)wrapper.readEnum((Enum[])EquipmentSlot.values());
      Sound equipSound = Sound.read(wrapper);
      ResourceLocation assetId = (ResourceLocation)wrapper.readOptional(PacketWrapper::readIdentifier);
      ResourceLocation cameraOverlay = (ResourceLocation)wrapper.readOptional(PacketWrapper::readIdentifier);
      MappedEntitySet<EntityType> allowedEntities = (MappedEntitySet)wrapper.readOptional((ew) -> {
         return MappedEntitySet.read(ew, EntityTypes::getById);
      });
      boolean dispensable = wrapper.readBoolean();
      boolean swappable = wrapper.readBoolean();
      boolean damageOnHurt = wrapper.readBoolean();
      boolean equipOnInteract = false;
      boolean canBeSheared = false;
      Sound shearingSound = Sounds.ITEM_SHEARS_SNIP;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         equipOnInteract = wrapper.readBoolean();
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
            canBeSheared = wrapper.readBoolean();
            shearingSound = Sound.read(wrapper);
         }
      }

      return new ItemEquippable(slot, equipSound, assetId, cameraOverlay, allowedEntities, dispensable, swappable, damageOnHurt, equipOnInteract, canBeSheared, shearingSound);
   }

   public static void write(PacketWrapper<?> wrapper, ItemEquippable equippable) {
      wrapper.writeEnum(equippable.slot);
      Sound.write(wrapper, equippable.equipSound);
      wrapper.writeOptional(equippable.assetId, PacketWrapper::writeIdentifier);
      wrapper.writeOptional(equippable.cameraOverlay, PacketWrapper::writeIdentifier);
      wrapper.writeOptional(equippable.allowedEntities, MappedEntitySet::write);
      wrapper.writeBoolean(equippable.dispensable);
      wrapper.writeBoolean(equippable.swappable);
      wrapper.writeBoolean(equippable.damageOnHurt);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(equippable.equipOnInteract);
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
            wrapper.writeBoolean(equippable.canBeSheared);
            Sound.write(wrapper, equippable.shearingSound);
         }
      }

   }

   public EquipmentSlot getSlot() {
      return this.slot;
   }

   public void setSlot(EquipmentSlot slot) {
      this.slot = slot;
   }

   public Sound getEquipSound() {
      return this.equipSound;
   }

   public void setEquipSound(Sound equipSound) {
      this.equipSound = equipSound;
   }

   @Nullable
   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   public void setAssetId(@Nullable ResourceLocation assetId) {
      this.assetId = assetId;
   }

   @Nullable
   public ResourceLocation getCameraOverlay() {
      return this.cameraOverlay;
   }

   public void setCameraOverlay(@Nullable ResourceLocation cameraOverlay) {
      this.cameraOverlay = cameraOverlay;
   }

   @Nullable
   public MappedEntitySet<EntityType> getAllowedEntities() {
      return this.allowedEntities;
   }

   public void setAllowedEntities(@Nullable MappedEntitySet<EntityType> allowedEntities) {
      this.allowedEntities = allowedEntities;
   }

   public boolean isDispensable() {
      return this.dispensable;
   }

   public void setDispensable(boolean dispensable) {
      this.dispensable = dispensable;
   }

   public boolean isSwappable() {
      return this.swappable;
   }

   public void setSwappable(boolean swappable) {
      this.swappable = swappable;
   }

   public boolean isDamageOnHurt() {
      return this.damageOnHurt;
   }

   public void setDamageOnHurt(boolean damageOnHurt) {
      this.damageOnHurt = damageOnHurt;
   }

   public boolean isEquipOnInteract() {
      return this.equipOnInteract;
   }

   public void setEquipOnInteract(boolean equipOnInteract) {
      this.equipOnInteract = equipOnInteract;
   }

   public boolean isCanBeSheared() {
      return this.canBeSheared;
   }

   public void setCanBeSheared(boolean canBeSheared) {
      this.canBeSheared = canBeSheared;
   }

   public Sound getShearingSound() {
      return this.shearingSound;
   }

   public void setShearingSound(Sound shearingSound) {
      this.shearingSound = shearingSound;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public ResourceLocation getModel() {
      return this.assetId;
   }

   /** @deprecated */
   @Deprecated
   public void setModel(@Nullable ResourceLocation assetId) {
      this.assetId = assetId;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemEquippable)) {
         return false;
      } else {
         ItemEquippable that = (ItemEquippable)obj;
         if (this.dispensable != that.dispensable) {
            return false;
         } else if (this.swappable != that.swappable) {
            return false;
         } else if (this.damageOnHurt != that.damageOnHurt) {
            return false;
         } else if (this.equipOnInteract != that.equipOnInteract) {
            return false;
         } else if (this.canBeSheared != that.canBeSheared) {
            return false;
         } else if (this.slot != that.slot) {
            return false;
         } else if (!this.equipSound.equals(that.equipSound)) {
            return false;
         } else if (!Objects.equals(this.assetId, that.assetId)) {
            return false;
         } else if (!Objects.equals(this.cameraOverlay, that.cameraOverlay)) {
            return false;
         } else {
            return !Objects.equals(this.allowedEntities, that.allowedEntities) ? false : this.shearingSound.equals(that.shearingSound);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound});
   }

   public String toString() {
      return "ItemEquippable{slot=" + this.slot + ", equipSound=" + this.equipSound + ", assetId=" + this.assetId + ", cameraOverlay=" + this.cameraOverlay + ", allowedEntities=" + this.allowedEntities + ", dispensable=" + this.dispensable + ", swappable=" + this.swappable + ", damageOnHurt=" + this.damageOnHurt + ", equipOnInteract=" + this.equipOnInteract + ", canBeSheared=" + this.canBeSheared + ", shearingSound=" + this.shearingSound + '}';
   }
}
