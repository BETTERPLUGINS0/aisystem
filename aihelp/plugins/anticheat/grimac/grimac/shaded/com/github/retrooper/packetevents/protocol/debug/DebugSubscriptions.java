package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugBeeInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugBrainDump;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugBreezeInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugEntityBlockIntersection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugGameEventInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugGameEventListenerInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugGoalInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugHiveInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugNeighborUpdateInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugPathInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugPoiInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugRaidsInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugStructureInfos;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.DebugVillageSections;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct.RedstoneOrientation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DebugSubscriptions {
   private static final VersionedRegistry<DebugSubscription<?>> REGISTRY = new VersionedRegistry("debug_subscription");
   public static final DebugSubscription<?> DEDICATED_SERVER_TICK_TIME = define("dedicated_server_tick_time");
   public static final DebugSubscription<DebugBeeInfo> BEES = define("bees", DebugBeeInfo::read, DebugBeeInfo::write);
   public static final DebugSubscription<DebugBrainDump> BRAINS = define("brains", DebugBrainDump::read, DebugBrainDump::write);
   public static final DebugSubscription<DebugBreezeInfo> BREEZES = define("breezes", DebugBreezeInfo::read, DebugBreezeInfo::write);
   public static final DebugSubscription<DebugGoalInfo> GOAL_SELECTORS = define("goal_selectors", DebugGoalInfo::read, DebugGoalInfo::write);
   public static final DebugSubscription<DebugPathInfo> ENTITY_PATHS = define("entity_paths", DebugPathInfo::read, DebugPathInfo::write);
   public static final DebugSubscription<DebugEntityBlockIntersection> ENTITY_BLOCK_INTERSECTIONS = define("entity_block_intersections", DebugEntityBlockIntersection::read, DebugEntityBlockIntersection::write);
   public static final DebugSubscription<DebugHiveInfo> BEE_HIVES = define("bee_hives", DebugHiveInfo::read, DebugHiveInfo::write);
   public static final DebugSubscription<DebugPoiInfo> POIS = define("pois", DebugPoiInfo::read, DebugPoiInfo::write);
   public static final DebugSubscription<RedstoneOrientation> REDSTONE_WIRE_ORIENTATIONS = define("redstone_wire_orientations", RedstoneOrientation::read, RedstoneOrientation::write);
   public static final DebugSubscription<DebugVillageSections> VILLAGE_SECTIONS = define("village_sections", DebugVillageSections::read, DebugVillageSections::write);
   public static final DebugSubscription<DebugRaidsInfo> RAIDS = define("raids", DebugRaidsInfo::read, DebugRaidsInfo::write);
   public static final DebugSubscription<DebugStructureInfos> STRUCTURES = define("structures", DebugStructureInfos::read, DebugStructureInfos::write);
   public static final DebugSubscription<DebugGameEventListenerInfo> GAME_EVENT_LISTENERS = define("game_event_listeners", DebugGameEventListenerInfo::read, DebugGameEventListenerInfo::write);
   public static final DebugSubscription<DebugNeighborUpdateInfo> NEIGHBOR_UPDATES = define("neighbor_updates", DebugNeighborUpdateInfo::read, DebugNeighborUpdateInfo::write);
   public static final DebugSubscription<DebugGameEventInfo> GAME_EVENTS = define("game_events", DebugGameEventInfo::read, DebugGameEventInfo::write);

   private DebugSubscriptions() {
   }

   public static VersionedRegistry<DebugSubscription<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static <T> DebugSubscription<T> define(String name) {
      return define(name, (PacketWrapper.Reader)null, (PacketWrapper.Writer)null);
   }

   @ApiStatus.Internal
   public static <T> DebugSubscription<T> define(String name, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer) {
      return (DebugSubscription)REGISTRY.define(name, (data) -> {
         return new StaticDebugSubscription(data, reader, writer);
      });
   }

   static {
      REGISTRY.unloadMappings();
   }
}
