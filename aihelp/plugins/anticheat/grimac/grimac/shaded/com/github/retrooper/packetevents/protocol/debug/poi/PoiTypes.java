package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PoiTypes {
   private static final VersionedRegistry<PoiType> REGISTRY = new VersionedRegistry("point_of_interest_type");
   public static final PoiType ARMORER = define("armorer");
   public static final PoiType BUTCHER = define("butcher");
   public static final PoiType CARTOGRAPHER = define("cartographer");
   public static final PoiType CLERIC = define("cleric");
   public static final PoiType FARMER = define("farmer");
   public static final PoiType FISHERMAN = define("fisherman");
   public static final PoiType FLETCHER = define("fletcher");
   public static final PoiType LEATHERWORKER = define("leatherworker");
   public static final PoiType LIBRARIAN = define("librarian");
   public static final PoiType MASON = define("mason");
   public static final PoiType SHEPHERD = define("shepherd");
   public static final PoiType TOOLSMITH = define("toolsmith");
   public static final PoiType WEAPONSMITH = define("weaponsmith");
   public static final PoiType HOME = define("home");
   public static final PoiType MEETING = define("meeting");
   public static final PoiType BEEHIVE = define("beehive");
   public static final PoiType BEE_NEST = define("bee_nest");
   public static final PoiType NETHER_PORTAL = define("nether_portal");
   public static final PoiType LODESTONE = define("lodestone");
   public static final PoiType TEST_INSTANCE = define("test_instance");
   public static final PoiType LIGHTNING_ROD = define("lightning_rod");

   private PoiTypes() {
   }

   public static VersionedRegistry<PoiType> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static PoiType define(String name) {
      return (PoiType)REGISTRY.define(name, StaticPoiType::new);
   }

   static {
      REGISTRY.unloadMappings();
   }
}
