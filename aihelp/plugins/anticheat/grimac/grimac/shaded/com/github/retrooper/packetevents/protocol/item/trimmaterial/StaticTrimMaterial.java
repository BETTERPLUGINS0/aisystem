package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticTrimMaterial extends AbstractMappedEntity implements TrimMaterial {
   private final String assetName;
   @Nullable
   private final ItemType ingredient;
   private final float itemModelIndex;
   private final Map<ArmorMaterial, String> overrideArmorMaterials;
   private final Component description;

   public StaticTrimMaterial(String assetName, @Nullable ItemType ingredient, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
      this((TypesBuilderData)null, assetName, ingredient, 0.0F, overrideArmorMaterials, description);
   }

   @ApiStatus.Internal
   public StaticTrimMaterial(@Nullable TypesBuilderData data, String assetName, @Nullable ItemType ingredient, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
      this(data, assetName, ingredient, 0.0F, overrideArmorMaterials, description);
   }

   public StaticTrimMaterial(String assetName, @Nullable ItemType ingredient, float itemModelIndex, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
      this((TypesBuilderData)null, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
   }

   @ApiStatus.Internal
   public StaticTrimMaterial(@Nullable TypesBuilderData data, String assetName, @Nullable ItemType ingredient, float itemModelIndex, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
      super(data);
      this.assetName = assetName;
      this.ingredient = ingredient;
      this.itemModelIndex = itemModelIndex;
      this.overrideArmorMaterials = overrideArmorMaterials;
      this.description = description;
   }

   public TrimMaterial copy(@Nullable TypesBuilderData newData) {
      return new StaticTrimMaterial(newData, this.assetName, this.ingredient, this.itemModelIndex, this.overrideArmorMaterials, this.description);
   }

   public String getAssetName() {
      return this.assetName;
   }

   @ApiStatus.Obsolete
   public ItemType getIngredient() {
      return this.ingredient != null ? this.ingredient : ItemTypes.AIR;
   }

   public float getItemModelIndex() {
      return this.itemModelIndex;
   }

   public Map<ArmorMaterial, String> getOverrideArmorMaterials() {
      return this.overrideArmorMaterials;
   }

   public Component getDescription() {
      return this.description;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticTrimMaterial)) {
         return false;
      } else {
         StaticTrimMaterial that = (StaticTrimMaterial)obj;
         if (Float.compare(that.itemModelIndex, this.itemModelIndex) != 0) {
            return false;
         } else if (!this.assetName.equals(that.assetName)) {
            return false;
         } else if (!Objects.equals(this.ingredient, that.ingredient)) {
            return false;
         } else {
            return !this.overrideArmorMaterials.equals(that.overrideArmorMaterials) ? false : this.description.equals(that.description);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.assetName, this.ingredient, this.itemModelIndex, this.overrideArmorMaterials, this.description});
   }

   public String toString() {
      return "StaticTrimMaterial{assetName='" + this.assetName + '\'' + ", ingredient=" + this.ingredient + ", itemModelIndex=" + this.itemModelIndex + ", overrideArmorMaterials=" + this.overrideArmorMaterials + ", description=" + this.description + '}';
   }
}
