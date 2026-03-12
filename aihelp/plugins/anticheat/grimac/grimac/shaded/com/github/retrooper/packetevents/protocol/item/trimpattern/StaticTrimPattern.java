package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticTrimPattern extends AbstractMappedEntity implements TrimPattern {
   private final ResourceLocation assetId;
   @Nullable
   private final ItemType templateItem;
   private final Component description;
   private final boolean decal;

   public StaticTrimPattern(ResourceLocation assetId, @Nullable ItemType templateItem, Component description, boolean decal) {
      this((TypesBuilderData)null, assetId, templateItem, description, decal);
   }

   @ApiStatus.Internal
   public StaticTrimPattern(@Nullable TypesBuilderData data, ResourceLocation assetId, @Nullable ItemType templateItem, Component description, boolean decal) {
      super(data);
      this.assetId = assetId;
      this.templateItem = templateItem;
      this.description = description;
      this.decal = decal;
   }

   public TrimPattern copy(@Nullable TypesBuilderData newData) {
      return new StaticTrimPattern(newData, this.assetId, this.templateItem, this.description, this.decal);
   }

   public ResourceLocation getAssetId() {
      return this.assetId;
   }

   @ApiStatus.Obsolete
   public ItemType getTemplateItem() {
      return this.templateItem != null ? this.templateItem : ItemTypes.AIR;
   }

   public Component getDescription() {
      return this.description;
   }

   public boolean isDecal() {
      return this.decal;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticTrimPattern)) {
         return false;
      } else {
         StaticTrimPattern that = (StaticTrimPattern)obj;
         if (this.decal != that.decal) {
            return false;
         } else if (!this.assetId.equals(that.assetId)) {
            return false;
         } else {
            return !Objects.equals(this.templateItem, that.templateItem) ? false : this.description.equals(that.description);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.assetId, this.templateItem, this.description, this.decal});
   }

   public String toString() {
      return "StaticTrimPattern{assetId=" + this.assetId + ", templateItem=" + this.templateItem + ", description=" + this.description + ", decal=" + this.decal + '}';
   }
}
