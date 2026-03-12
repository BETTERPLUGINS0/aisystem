package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FoodProperties {
   private int nutrition;
   private float saturation;
   private boolean canAlwaysEat;
   private float eatSeconds;
   private List<FoodProperties.PossibleEffect> effects;
   @Nullable
   private ItemStack usingConvertsTo;

   public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat) {
      this(nutrition, saturation, canAlwaysEat, 1.6F, Collections.emptyList());
   }

   @ApiStatus.Obsolete
   public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat, float eatSeconds, List<FoodProperties.PossibleEffect> effects) {
      this(nutrition, saturation, canAlwaysEat, eatSeconds, effects, (ItemStack)null);
   }

   @ApiStatus.Obsolete
   public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat, float eatSeconds, List<FoodProperties.PossibleEffect> effects, @Nullable ItemStack usingConvertsTo) {
      this.nutrition = nutrition;
      this.saturation = saturation;
      this.canAlwaysEat = canAlwaysEat;
      this.eatSeconds = eatSeconds;
      this.effects = effects;
      this.usingConvertsTo = usingConvertsTo;
   }

   public static FoodProperties read(PacketWrapper<?> wrapper) {
      int nutrition = wrapper.readVarInt();
      float saturation = wrapper.readFloat();
      boolean canAlwaysEat = wrapper.readBoolean();
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         return new FoodProperties(nutrition, saturation, canAlwaysEat);
      } else {
         float eatSeconds = wrapper.readFloat();
         ItemStack usingConvertsTo = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21) ? (ItemStack)wrapper.readOptional(PacketWrapper::readItemStack) : null;
         List<FoodProperties.PossibleEffect> effects = wrapper.readList(FoodProperties.PossibleEffect::read);
         return new FoodProperties(nutrition, saturation, canAlwaysEat, eatSeconds, effects, usingConvertsTo);
      }
   }

   public static void write(PacketWrapper<?> wrapper, FoodProperties props) {
      wrapper.writeVarInt(props.nutrition);
      wrapper.writeFloat(props.saturation);
      wrapper.writeBoolean(props.canAlwaysEat);
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_2)) {
         wrapper.writeFloat(props.eatSeconds);
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            wrapper.writeOptional(props.usingConvertsTo, PacketWrapper::writeItemStack);
         }

         wrapper.writeList(props.effects, FoodProperties.PossibleEffect::write);
      }

   }

   public int getNutrition() {
      return this.nutrition;
   }

   public void setNutrition(int nutrition) {
      this.nutrition = nutrition;
   }

   public float getSaturation() {
      return this.saturation;
   }

   public void setSaturation(float saturation) {
      this.saturation = saturation;
   }

   public boolean isCanAlwaysEat() {
      return this.canAlwaysEat;
   }

   public void setCanAlwaysEat(boolean canAlwaysEat) {
      this.canAlwaysEat = canAlwaysEat;
   }

   @ApiStatus.Obsolete
   public float getEatSeconds() {
      return this.eatSeconds;
   }

   @ApiStatus.Obsolete
   public void setEatSeconds(float eatSeconds) {
      this.eatSeconds = eatSeconds;
   }

   @ApiStatus.Obsolete
   public void addEffect(FoodProperties.PossibleEffect effect) {
      this.effects.add(effect);
   }

   @ApiStatus.Obsolete
   public List<FoodProperties.PossibleEffect> getEffects() {
      return this.effects;
   }

   @ApiStatus.Obsolete
   public void setEffects(List<FoodProperties.PossibleEffect> effects) {
      this.effects = effects;
   }

   @ApiStatus.Obsolete
   @Nullable
   public ItemStack getUsingConvertsTo() {
      return this.usingConvertsTo;
   }

   @ApiStatus.Obsolete
   public void setUsingConvertsTo(@Nullable ItemStack usingConvertsTo) {
      this.usingConvertsTo = usingConvertsTo;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof FoodProperties)) {
         return false;
      } else {
         FoodProperties that = (FoodProperties)obj;
         if (this.nutrition != that.nutrition) {
            return false;
         } else if (Float.compare(that.saturation, this.saturation) != 0) {
            return false;
         } else if (this.canAlwaysEat != that.canAlwaysEat) {
            return false;
         } else if (Float.compare(that.eatSeconds, this.eatSeconds) != 0) {
            return false;
         } else {
            return !this.effects.equals(that.effects) ? false : Objects.equals(this.usingConvertsTo, that.usingConvertsTo);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.nutrition, this.saturation, this.canAlwaysEat, this.eatSeconds, this.effects, this.usingConvertsTo});
   }

   @ApiStatus.Obsolete
   public static class PossibleEffect {
      private PotionEffect effect;
      private float probability;

      public PossibleEffect(PotionEffect effect, float probability) {
         this.effect = effect;
         this.probability = probability;
      }

      public static FoodProperties.PossibleEffect read(PacketWrapper<?> wrapper) {
         PotionEffect effect = PotionEffect.read(wrapper);
         float probability = wrapper.readFloat();
         return new FoodProperties.PossibleEffect(effect, probability);
      }

      public static void write(PacketWrapper<?> wrapper, FoodProperties.PossibleEffect effect) {
         PotionEffect.write(wrapper, effect.effect);
         wrapper.writeFloat(effect.probability);
      }

      public PotionEffect getEffect() {
         return this.effect;
      }

      public void setEffect(PotionEffect effect) {
         this.effect = effect;
      }

      public float getProbability() {
         return this.probability;
      }

      public void setProbability(float probability) {
         this.probability = probability;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof FoodProperties.PossibleEffect)) {
            return false;
         } else {
            FoodProperties.PossibleEffect that = (FoodProperties.PossibleEffect)obj;
            return Float.compare(that.probability, this.probability) != 0 ? false : this.effect.equals(that.effect);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.effect, this.probability});
      }
   }
}
