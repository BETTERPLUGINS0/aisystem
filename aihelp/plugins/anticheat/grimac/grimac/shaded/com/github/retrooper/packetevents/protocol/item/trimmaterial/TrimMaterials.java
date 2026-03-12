package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class TrimMaterials {
   private static final VersionedRegistry<TrimMaterial> REGISTRY = new VersionedRegistry("trim_material");
   public static final TrimMaterial AMETHYST;
   public static final TrimMaterial COPPER;
   public static final TrimMaterial DIAMOND;
   public static final TrimMaterial EMERALD;
   public static final TrimMaterial GOLD;
   public static final TrimMaterial IRON;
   public static final TrimMaterial LAPIS;
   public static final TrimMaterial NETHERITE;
   public static final TrimMaterial QUARTZ;
   public static final TrimMaterial REDSTONE;
   public static final TrimMaterial RESIN;

   private TrimMaterials() {
   }

   @ApiStatus.Internal
   public static TrimMaterial define(String key, ItemType ingredient, float itemModelIndex, int color) {
      Map<ArmorMaterial, String> overrideArmorMaterials = new HashMap(2);
      String armorMaterialId = ResourceLocation.minecraft(key).toString();
      ArmorMaterial armorMaterial = ArmorMaterials.getByName(armorMaterialId);
      if (armorMaterial != null) {
         overrideArmorMaterials.put(armorMaterial, key + "_darker");
      }

      Component description = Component.translatable("trim_material.minecraft." + key, TextColor.color(color));
      return define(key, key, ingredient, itemModelIndex, overrideArmorMaterials, description);
   }

   @ApiStatus.Internal
   public static TrimMaterial define(String key, String assetName, ItemType ingredient, float itemModelIndex, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
      return (TrimMaterial)REGISTRY.define(key, (data) -> {
         return new StaticTrimMaterial(data, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
      });
   }

   public static VersionedRegistry<TrimMaterial> getRegistry() {
      return REGISTRY;
   }

   public static TrimMaterial getByName(String name) {
      return (TrimMaterial)REGISTRY.getByName(name);
   }

   public static TrimMaterial getById(ClientVersion version, int id) {
      return (TrimMaterial)REGISTRY.getById(version, id);
   }

   public static Collection<TrimMaterial> values() {
      return REGISTRY.getEntries();
   }

   static {
      AMETHYST = define("amethyst", ItemTypes.AMETHYST_SHARD, 1.0F, 10116294);
      COPPER = define("copper", ItemTypes.COPPER_INGOT, 0.5F, 11823181);
      DIAMOND = define("diamond", ItemTypes.DIAMOND, 0.8F, 7269586);
      EMERALD = define("emerald", ItemTypes.EMERALD, 0.7F, 1155126);
      GOLD = define("gold", ItemTypes.GOLD_INGOT, 0.6F, 14594349);
      IRON = define("iron", ItemTypes.IRON_INGOT, 0.2F, 15527148);
      LAPIS = define("lapis", ItemTypes.LAPIS_LAZULI, 0.9F, 4288151);
      NETHERITE = define("netherite", ItemTypes.NETHERITE_INGOT, 0.3F, 6445145);
      QUARTZ = define("quartz", ItemTypes.QUARTZ, 0.1F, 14931140);
      REDSTONE = define("redstone", ItemTypes.REDSTONE, 0.4F, 9901575);
      RESIN = define("resin", ItemTypes.RESIN_BRICK, 0.11F, 16545810);
      REGISTRY.unloadMappings();
   }
}
