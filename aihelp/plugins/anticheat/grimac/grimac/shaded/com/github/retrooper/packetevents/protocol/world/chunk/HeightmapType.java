package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;

public enum HeightmapType {
   WORLD_SURFACE_WG("WORLD_SURFACE_WG", false),
   WORLD_SURFACE("WORLD_SURFACE", true),
   OCEAN_FLOOR_WG("OCEAN_FLOOR_WG", false),
   OCEAN_FLOOR("OCEAN_FLOOR", false),
   MOTION_BLOCKING("MOTION_BLOCKING", true),
   MOTION_BLOCKING_NO_LEAVES("MOTION_BLOCKING_NO_LEAVES", true);

   public static final Index<String, HeightmapType> SERIALIZATION_KEY_INDEX = Index.create(HeightmapType.class, HeightmapType::getSerializationKey);
   private final String serializationKey;
   private final boolean client;

   private HeightmapType(String serializationKey, boolean client) {
      this.serializationKey = serializationKey;
      this.client = client;
   }

   @Nullable
   public static HeightmapType getHeightmapType(String serializationKey) {
      return (HeightmapType)SERIALIZATION_KEY_INDEX.value(serializationKey);
   }

   public static HeightmapType read(PacketWrapper<?> wrapper) {
      return (HeightmapType)wrapper.readEnum(HeightmapType.class);
   }

   public static void write(PacketWrapper<?> wrapper, HeightmapType type) {
      wrapper.writeEnum(type);
   }

   public String getSerializationKey() {
      return this.serializationKey;
   }

   public boolean isClient() {
      return this.client;
   }

   // $FF: synthetic method
   private static HeightmapType[] $values() {
      return new HeightmapType[]{WORLD_SURFACE_WG, WORLD_SURFACE, OCEAN_FLOOR_WG, OCEAN_FLOOR, MOTION_BLOCKING, MOTION_BLOCKING_NO_LEAVES};
   }
}
