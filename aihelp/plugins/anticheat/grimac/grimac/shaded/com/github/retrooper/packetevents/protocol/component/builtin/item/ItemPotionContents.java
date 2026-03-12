package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.Potion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.Potions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemPotionContents {
   @Nullable
   private Potion potion;
   @Nullable
   private Integer customColor;
   private List<PotionEffect> customEffects;
   @Nullable
   private String customName;

   public ItemPotionContents(@Nullable Potion potion, @Nullable Integer customColor, List<PotionEffect> customEffects) {
      this(potion, customColor, customEffects, (String)null);
   }

   public ItemPotionContents(@Nullable Potion potion, @Nullable Integer customColor, List<PotionEffect> customEffects, @Nullable String customName) {
      this.potion = potion;
      this.customColor = customColor;
      this.customEffects = customEffects;
      this.customName = customName;
   }

   public static ItemPotionContents read(PacketWrapper<?> wrapper) {
      Potion potionId = (Potion)wrapper.readOptional((ew) -> {
         return (Potion)ew.readMappedEntity(Potions::getById);
      });
      Integer customColor = (Integer)wrapper.readOptional(PacketWrapper::readInt);
      List<PotionEffect> customEffects = wrapper.readList(PotionEffect::read);
      String customName = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2) ? (String)wrapper.readOptional(PacketWrapper::readString) : null;
      return new ItemPotionContents(potionId, customColor, customEffects, customName);
   }

   public static void write(PacketWrapper<?> wrapper, ItemPotionContents contents) {
      wrapper.writeOptional(contents.potion, PacketWrapper::writeMappedEntity);
      wrapper.writeOptional(contents.customColor, PacketWrapper::writeInt);
      wrapper.writeList(contents.customEffects, PotionEffect::write);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         wrapper.writeOptional(contents.customName, PacketWrapper::writeString);
      }

   }

   @Nullable
   public Potion getPotion() {
      return this.potion;
   }

   public void setPotion(@Nullable Potion potion) {
      this.potion = potion;
   }

   @Nullable
   public Integer getCustomColor() {
      return this.customColor;
   }

   public void setCustomColor(@Nullable Integer customColor) {
      this.customColor = customColor;
   }

   private void addCustomEffect(PotionEffect effect) {
      this.customEffects.add(effect);
   }

   public List<PotionEffect> getCustomEffects() {
      return this.customEffects;
   }

   public void setCustomEffects(List<PotionEffect> customEffects) {
      this.customEffects = customEffects;
   }

   @Nullable
   public String getCustomName() {
      return this.customName;
   }

   public void setCustomName(@Nullable String customName) {
      this.customName = customName;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemPotionContents)) {
         return false;
      } else {
         ItemPotionContents that = (ItemPotionContents)obj;
         if (!Objects.equals(this.potion, that.potion)) {
            return false;
         } else if (!Objects.equals(this.customColor, that.customColor)) {
            return false;
         } else {
            return !this.customEffects.equals(that.customEffects) ? false : Objects.equals(this.customName, that.customName);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.potion, this.customColor, this.customEffects, this.customName});
   }
}
