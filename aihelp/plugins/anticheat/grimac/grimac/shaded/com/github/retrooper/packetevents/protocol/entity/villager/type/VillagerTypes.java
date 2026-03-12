package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class VillagerTypes {
   private static final VersionedRegistry<VillagerType> REGISTRY = new VersionedRegistry("villager_type");
   public static final VillagerType DESERT = define("desert");
   public static final VillagerType JUNGLE = define("jungle");
   public static final VillagerType PLAINS = define("plains");
   public static final VillagerType SAVANNA = define("savanna");
   public static final VillagerType SNOW = define("snow");
   public static final VillagerType SWAMP = define("swamp");
   public static final VillagerType TAIGA = define("taiga");

   private VillagerTypes() {
   }

   public static VersionedRegistry<VillagerType> getRegistry() {
      return REGISTRY;
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.Internal
   public static VillagerType define(int id, String name) {
      return define(name);
   }

   @ApiStatus.Internal
   public static VillagerType define(String name) {
      return (VillagerType)REGISTRY.define(name, StaticVillagerType::new);
   }

   /** @deprecated */
   @Deprecated
   public static VillagerType getById(int id) {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      return getById(version.toClientVersion(), id);
   }

   public static VillagerType getById(ClientVersion version, int id) {
      return (VillagerType)REGISTRY.getById(version, id);
   }

   public static VillagerType getByName(String name) {
      return (VillagerType)REGISTRY.getByName(name);
   }

   public static Collection<VillagerType> values() {
      return REGISTRY.getEntries();
   }

   static {
      REGISTRY.unloadMappings();
   }
}
