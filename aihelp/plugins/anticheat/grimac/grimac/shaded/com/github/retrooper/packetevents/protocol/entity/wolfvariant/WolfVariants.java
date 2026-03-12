package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biome;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collections;

public final class WolfVariants {
   private static final VersionedRegistry<WolfVariant> REGISTRY = new VersionedRegistry("wolf_variant");
   public static final WolfVariant PALE;
   public static final WolfVariant SPOTTED;
   public static final WolfVariant SNOWY;
   public static final WolfVariant BLACK;
   public static final WolfVariant ASHEN;
   public static final WolfVariant RUSTY;
   public static final WolfVariant WOODS;
   public static final WolfVariant CHESTNUT;
   public static final WolfVariant STRIPED;

   private WolfVariants() {
   }

   @ApiStatus.Internal
   public static WolfVariant define(String name, MappedEntitySet<Biome> biomes) {
      return define(name, "wolf_" + name, biomes);
   }

   @ApiStatus.Internal
   public static WolfVariant define(String name, String assetId, MappedEntitySet<Biome> biomes) {
      return define(name, ResourceLocation.minecraft("entity/wolf/" + assetId), ResourceLocation.minecraft("entity/wolf/" + assetId + "_tame"), ResourceLocation.minecraft("entity/wolf/" + assetId + "_angry"), biomes);
   }

   @ApiStatus.Internal
   public static WolfVariant define(String name, ResourceLocation wildTexture, ResourceLocation tameTexture, ResourceLocation angryTexture, MappedEntitySet<Biome> biomes) {
      return (WolfVariant)REGISTRY.define(name, (data) -> {
         return new StaticWolfVariant(data, wildTexture, tameTexture, angryTexture, biomes);
      });
   }

   public static VersionedRegistry<WolfVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      PALE = define("pale", "wolf", new MappedEntitySet(Collections.singletonList(Biomes.TAIGA)));
      SPOTTED = define("spotted", new MappedEntitySet(ResourceLocation.minecraft("is_savanna")));
      SNOWY = define("snowy", new MappedEntitySet(Collections.singletonList(Biomes.GROVE)));
      BLACK = define("black", new MappedEntitySet(Collections.singletonList(Biomes.OLD_GROWTH_PINE_TAIGA)));
      ASHEN = define("ashen", new MappedEntitySet(Collections.singletonList(Biomes.SNOWY_TAIGA)));
      RUSTY = define("rusty", new MappedEntitySet(ResourceLocation.minecraft("is_jungle")));
      WOODS = define("woods", new MappedEntitySet(Collections.singletonList(Biomes.FOREST)));
      CHESTNUT = define("chestnut", new MappedEntitySet(Collections.singletonList(Biomes.OLD_GROWTH_SPRUCE_TAIGA)));
      STRIPED = define("striped", new MappedEntitySet(ResourceLocation.minecraft("is_badlands")));
      REGISTRY.unloadMappings();
   }
}
