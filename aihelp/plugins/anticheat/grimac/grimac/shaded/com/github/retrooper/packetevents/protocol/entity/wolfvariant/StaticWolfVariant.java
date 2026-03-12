package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biome;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticWolfVariant extends AbstractMappedEntity implements WolfVariant {
   private final ResourceLocation wildTexture;
   private final ResourceLocation tameTexture;
   private final ResourceLocation angryTexture;
   private final MappedEntitySet<Biome> biomes;

   @ApiStatus.Obsolete
   public StaticWolfVariant(ResourceLocation wildTexture, ResourceLocation tameTexture, ResourceLocation angryTexture, MappedEntitySet<Biome> biomes) {
      this((TypesBuilderData)null, wildTexture, tameTexture, angryTexture, biomes);
   }

   public StaticWolfVariant(ResourceLocation angryTexture, ResourceLocation tameTexture, ResourceLocation wildTexture) {
      this((TypesBuilderData)null, angryTexture, tameTexture, wildTexture, MappedEntitySet.createEmpty());
   }

   @ApiStatus.Internal
   public StaticWolfVariant(@Nullable TypesBuilderData data, ResourceLocation wildTexture, ResourceLocation tameTexture, ResourceLocation angryTexture, MappedEntitySet<Biome> biomes) {
      super(data);
      this.wildTexture = wildTexture;
      this.tameTexture = tameTexture;
      this.angryTexture = angryTexture;
      this.biomes = biomes;
   }

   public WolfVariant copy(@Nullable TypesBuilderData newData) {
      return new StaticWolfVariant(newData, this.wildTexture, this.tameTexture, this.angryTexture, this.biomes);
   }

   public ResourceLocation getWildTexture() {
      return this.wildTexture;
   }

   public ResourceLocation getTameTexture() {
      return this.tameTexture;
   }

   public ResourceLocation getAngryTexture() {
      return this.angryTexture;
   }

   public MappedEntitySet<Biome> getBiomes() {
      return this.biomes;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticWolfVariant)) {
         return false;
      } else {
         StaticWolfVariant that = (StaticWolfVariant)obj;
         if (!this.wildTexture.equals(that.wildTexture)) {
            return false;
         } else if (!this.tameTexture.equals(that.tameTexture)) {
            return false;
         } else {
            return !this.angryTexture.equals(that.angryTexture) ? false : this.biomes.equals(that.biomes);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.wildTexture, this.tameTexture, this.angryTexture, this.biomes});
   }

   public String toString() {
      return "StaticWolfVariant{wildTexture=" + this.wildTexture + ", tameTexture=" + this.tameTexture + ", angryTexture=" + this.angryTexture + ", biomes=" + this.biomes + '}';
   }
}
