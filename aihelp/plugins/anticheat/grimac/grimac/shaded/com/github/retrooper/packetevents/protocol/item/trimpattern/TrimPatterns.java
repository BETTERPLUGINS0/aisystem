package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public final class TrimPatterns {
   private static final VersionedRegistry<TrimPattern> REGISTRY = new VersionedRegistry("trim_pattern");
   public static final TrimPattern COAST = define("coast");
   public static final TrimPattern DUNE = define("dune");
   public static final TrimPattern EYE = define("eye");
   public static final TrimPattern RIB = define("rib");
   public static final TrimPattern SENTRY = define("sentry");
   public static final TrimPattern SNOUT = define("snout");
   public static final TrimPattern SPIRE = define("spire");
   public static final TrimPattern TIDE = define("tide");
   public static final TrimPattern VEX = define("vex");
   public static final TrimPattern WARD = define("ward");
   public static final TrimPattern WILD = define("wild");
   public static final TrimPattern RAISER = define("raiser");
   public static final TrimPattern HOST = define("host");
   public static final TrimPattern SILENCE = define("silence");
   public static final TrimPattern SHAPER = define("shaper");
   public static final TrimPattern WAYFINDER = define("wayfinder");
   public static final TrimPattern BOLT = define("bolt");
   public static final TrimPattern FLOW = define("flow");

   private TrimPatterns() {
   }

   @ApiStatus.Internal
   public static TrimPattern define(String name) {
      ResourceLocation assetId = ResourceLocation.minecraft(name);
      ItemType templateItem = ItemTypes.getByName(assetId + "_armor_trim_smithing_template");
      Component description = Component.translatable("trim_pattern.minecraft." + name);
      boolean decal = false;
      return define(name, assetId, templateItem, description, decal);
   }

   @ApiStatus.Internal
   public static TrimPattern define(String name, ResourceLocation assetId, @Nullable ItemType templateItem, Component description, boolean decal) {
      return (TrimPattern)REGISTRY.define(name, (data) -> {
         return new StaticTrimPattern(data, assetId, templateItem, description, decal);
      });
   }

   public static VersionedRegistry<TrimPattern> getRegistry() {
      return REGISTRY;
   }

   public static TrimPattern getByName(String name) {
      return (TrimPattern)REGISTRY.getByName(name);
   }

   public static TrimPattern getById(ClientVersion version, int id) {
      return (TrimPattern)REGISTRY.getById(version, id);
   }

   static {
      REGISTRY.unloadMappings();
   }
}
