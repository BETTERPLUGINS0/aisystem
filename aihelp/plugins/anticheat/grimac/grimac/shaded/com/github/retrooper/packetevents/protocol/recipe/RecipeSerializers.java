package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.CookedRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.ShapedRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.ShapelessRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.SimpleRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.SmithingRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.SmithingTrimRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.StoneCuttingRecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;

@ApiStatus.Obsolete
public final class RecipeSerializers {
   private static final VersionedRegistry<RecipeSerializer<?>> REGISTRY = new VersionedRegistry("legacy_recipe_serializer");
   public static final RecipeSerializer<ShapedRecipeData> CRAFTING_SHAPED;
   public static final RecipeSerializer<ShapelessRecipeData> CRAFTING_SHAPELESS;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_ARMORDYE;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_BOOKCLONING;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_MAPCLONING;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_MAPEXTENDING;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_FIREWORK_ROCKET;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_FIREWORK_STAR;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_FIREWORK_STAR_FADE;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_TIPPEDARROW;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_BANNERDUPLICATE;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_SHIELDDECORATION;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_SHULKERBOXCOLORING;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_SUSPICIOUSSTEW;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_REPAIRITEM;
   public static final RecipeSerializer<CookedRecipeData> SMELTING;
   public static final RecipeSerializer<CookedRecipeData> BLASTING;
   public static final RecipeSerializer<CookedRecipeData> SMOKING;
   public static final RecipeSerializer<CookedRecipeData> CAMPFIRE_COOKING;
   public static final RecipeSerializer<StoneCuttingRecipeData> STONECUTTING;
   @ApiStatus.Obsolete
   public static final RecipeSerializer<SmithingRecipeData> SMITHING;
   public static final RecipeSerializer<SmithingRecipeData> SMITHING_TRANSFORM;
   public static final RecipeSerializer<SmithingTrimRecipeData> SMITHING_TRIM;
   public static final RecipeSerializer<SimpleRecipeData> CRAFTING_DECORATED_POT;

   private RecipeSerializers() {
   }

   public static VersionedRegistry<RecipeSerializer<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static <T extends RecipeData> RecipeSerializer<T> define(String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
      return define(name, reader, writer, (RecipeType)null);
   }

   @ApiStatus.Internal
   public static <T extends RecipeData> RecipeSerializer<T> define(String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer, @Nullable RecipeType legacyType) {
      return (RecipeSerializer)REGISTRY.define(name, (data) -> {
         return new StaticRecipeSerializer(data, reader, writer, legacyType);
      });
   }

   public static RecipeSerializer<?> getByName(String name) {
      return (RecipeSerializer)REGISTRY.getByName(name);
   }

   public static RecipeSerializer<?> getById(ClientVersion version, int id) {
      return (RecipeSerializer)REGISTRY.getById(version, id);
   }

   public static Collection<RecipeSerializer<?>> values() {
      return REGISTRY.getEntries();
   }

   static {
      CRAFTING_SHAPED = define("crafting_shaped", ShapedRecipeData::read, ShapedRecipeData::write, RecipeType.CRAFTING_SHAPED);
      CRAFTING_SHAPELESS = define("crafting_shapeless", ShapelessRecipeData::read, ShapelessRecipeData::write, RecipeType.CRAFTING_SHAPELESS);
      CRAFTING_SPECIAL_ARMORDYE = define("crafting_special_armordye", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_ARMORDYE);
      CRAFTING_SPECIAL_BOOKCLONING = define("crafting_special_bookcloning", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_BOOKCLONING);
      CRAFTING_SPECIAL_MAPCLONING = define("crafting_special_mapcloning", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_MAPCLONING);
      CRAFTING_SPECIAL_MAPEXTENDING = define("crafting_special_mapextending", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_MAPEXTENDING);
      CRAFTING_SPECIAL_FIREWORK_ROCKET = define("crafting_special_firework_rocket", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_FIREWORK_ROCKET);
      CRAFTING_SPECIAL_FIREWORK_STAR = define("crafting_special_firework_star", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_FIREWORK_STAR);
      CRAFTING_SPECIAL_FIREWORK_STAR_FADE = define("crafting_special_firework_star_fade", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_FIREWORK_STAR_FADE);
      CRAFTING_SPECIAL_TIPPEDARROW = define("crafting_special_tippedarrow", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_TIPPEDARROW);
      CRAFTING_SPECIAL_BANNERDUPLICATE = define("crafting_special_bannerduplicate", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_BANNERDUPLICATE);
      CRAFTING_SPECIAL_SHIELDDECORATION = define("crafting_special_shielddecoration", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_SHIELDDECORATION);
      CRAFTING_SPECIAL_SHULKERBOXCOLORING = define("crafting_special_shulkerboxcoloring", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_SHULKERBOXCOLORING);
      CRAFTING_SPECIAL_SUSPICIOUSSTEW = define("crafting_special_suspiciousstew", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_SUSPICIOUSSTEW);
      CRAFTING_SPECIAL_REPAIRITEM = define("crafting_special_repairitem", SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_REPAIRITEM);
      SMELTING = define("smelting", CookedRecipeData::read, CookedRecipeData::write, RecipeType.SMELTING);
      BLASTING = define("blasting", CookedRecipeData::read, CookedRecipeData::write, RecipeType.BLASTING);
      SMOKING = define("smoking", CookedRecipeData::read, CookedRecipeData::write, RecipeType.SMOKING);
      CAMPFIRE_COOKING = define("campfire_cooking", CookedRecipeData::read, CookedRecipeData::write, RecipeType.CAMPFIRE_COOKING);
      STONECUTTING = define("stonecutting", StoneCuttingRecipeData::read, StoneCuttingRecipeData::write, RecipeType.STONECUTTING);
      SMITHING = define("smithing", (ew) -> {
         return SmithingRecipeData.read(ew, true);
      }, (ew, data) -> {
         SmithingRecipeData.write(ew, data, true);
      }, RecipeType.SMITHING);
      SMITHING_TRANSFORM = define("smithing_transform", SmithingRecipeData::read, SmithingRecipeData::write, RecipeType.SMITHING);
      SMITHING_TRIM = define("smithing_trim", SmithingTrimRecipeData::read, SmithingTrimRecipeData::write);
      CRAFTING_DECORATED_POT = define("crafting_decorated_pot", SimpleRecipeData::read, SimpleRecipeData::write);
      REGISTRY.unloadMappings();
   }
}
