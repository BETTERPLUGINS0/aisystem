package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class VillagerProfessions {
   private static final VersionedRegistry<VillagerProfession> REGISTRY = new VersionedRegistry("villager_profession");
   public static final VillagerProfession NONE = define("none");
   public static final VillagerProfession ARMORER = define("armorer");
   public static final VillagerProfession BUTCHER = define("butcher");
   public static final VillagerProfession CARTOGRAPHER = define("cartographer");
   public static final VillagerProfession CLERIC = define("cleric");
   public static final VillagerProfession FARMER = define("farmer");
   public static final VillagerProfession FISHERMAN = define("fisherman");
   public static final VillagerProfession FLETCHER = define("fletcher");
   public static final VillagerProfession LEATHERWORKER = define("leatherworker");
   public static final VillagerProfession LIBRARIAN = define("librarian");
   public static final VillagerProfession MASON = define("mason");
   public static final VillagerProfession NITWIT = define("nitwit");
   public static final VillagerProfession SHEPHERD = define("shepherd");
   public static final VillagerProfession TOOLSMITH = define("toolsmith");
   public static final VillagerProfession WEAPONSMITH = define("weaponsmith");

   private VillagerProfessions() {
   }

   public static VersionedRegistry<VillagerProfession> getRegistry() {
      return REGISTRY;
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.Internal
   public static VillagerProfession define(int id, String name) {
      return define(name);
   }

   @ApiStatus.Internal
   public static VillagerProfession define(String name) {
      return (VillagerProfession)REGISTRY.define(name, StaticVillagerProfession::new);
   }

   /** @deprecated */
   @Deprecated
   public static VillagerProfession getById(int id) {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      return getById(version.toClientVersion(), id);
   }

   public static VillagerProfession getById(ClientVersion version, int id) {
      return (VillagerProfession)REGISTRY.getById(version, id);
   }

   public static VillagerProfession getByName(String name) {
      return (VillagerProfession)REGISTRY.getByName(name);
   }

   static {
      REGISTRY.unloadMappings();
   }
}
