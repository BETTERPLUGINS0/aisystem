package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class TropicalFishPatterns {
   private static final VersionedRegistry<TropicalFishPattern> REGISTRY = new VersionedRegistry("tropical_fish_pattern");
   public static final TropicalFishPattern KOB;
   public static final TropicalFishPattern SUNSTREAK;
   public static final TropicalFishPattern SNOOPER;
   public static final TropicalFishPattern DASHER;
   public static final TropicalFishPattern BRINELY;
   public static final TropicalFishPattern SPOTTY;
   public static final TropicalFishPattern FLOPPER;
   public static final TropicalFishPattern STRIPEY;
   public static final TropicalFishPattern GLITTER;
   public static final TropicalFishPattern BLOCKFISH;
   public static final TropicalFishPattern BETTY;
   public static final TropicalFishPattern CLAYFISH;

   private TropicalFishPatterns() {
   }

   @ApiStatus.Internal
   public static TropicalFishPattern define(String name, TropicalFishPattern.Base base) {
      return (TropicalFishPattern)REGISTRY.define(name, (typesBuilderData) -> {
         return new StaticTropicalFishPattern(typesBuilderData, base);
      });
   }

   public static VersionedRegistry<TropicalFishPattern> getRegistry() {
      return REGISTRY;
   }

   public static Collection<TropicalFishPattern> values() {
      return REGISTRY.getEntries();
   }

   static {
      KOB = define("kob", TropicalFishPattern.Base.SMALL);
      SUNSTREAK = define("sunstreak", TropicalFishPattern.Base.SMALL);
      SNOOPER = define("snooper", TropicalFishPattern.Base.SMALL);
      DASHER = define("dasher", TropicalFishPattern.Base.SMALL);
      BRINELY = define("brinely", TropicalFishPattern.Base.SMALL);
      SPOTTY = define("spotty", TropicalFishPattern.Base.SMALL);
      FLOPPER = define("flopper", TropicalFishPattern.Base.LARGE);
      STRIPEY = define("stripey", TropicalFishPattern.Base.LARGE);
      GLITTER = define("glitter", TropicalFishPattern.Base.LARGE);
      BLOCKFISH = define("blockfish", TropicalFishPattern.Base.LARGE);
      BETTY = define("betty", TropicalFishPattern.Base.LARGE);
      CLAYFISH = define("clayfish", TropicalFishPattern.Base.LARGE);
      REGISTRY.unloadMappings();
   }
}
