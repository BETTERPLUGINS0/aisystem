package ac.grim.grimac.utils.data.tags;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTags;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class SyncedTags {
   public static final ResourceLocation CLIMBABLE = ResourceLocation.minecraft("climbable");
   public static final ResourceLocation MINEABLE_AXE = ResourceLocation.minecraft("mineable/axe");
   public static final ResourceLocation MINEABLE_PICKAXE = ResourceLocation.minecraft("mineable/pickaxe");
   public static final ResourceLocation MINEABLE_SHOVEL = ResourceLocation.minecraft("mineable/shovel");
   public static final ResourceLocation MINEABLE_HOE = ResourceLocation.minecraft("mineable/hoe");
   public static final ResourceLocation NEEDS_DIAMOND_TOOL = ResourceLocation.minecraft("needs_diamond_tool");
   public static final ResourceLocation NEEDS_IRON_TOOL = ResourceLocation.minecraft("needs_iron_tool");
   public static final ResourceLocation NEEDS_STONE_TOOL = ResourceLocation.minecraft("needs_stone_tool");
   public static final ResourceLocation SWORD_EFFICIENT = ResourceLocation.minecraft("sword_efficient");
   private static final ServerVersion VERSION = PacketEvents.getAPI().getServerManager().getVersion();
   private static final ResourceLocation BLOCK;
   private final GrimPlayer player;
   private final Map<ResourceLocation, Map<ResourceLocation, SyncedTag<?>>> synced;

   public SyncedTags(GrimPlayer player) {
      this.player = player;
      this.synced = new HashMap();
      ClientVersion version = player.getClientVersion();
      this.trackTags(BLOCK, (id) -> {
         return StateTypes.getById(VERSION.toClientVersion(), id);
      }, SyncedTag.builder(CLIMBABLE).defaults(BlockTags.CLIMBABLE.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_16)), SyncedTag.builder(MINEABLE_AXE).defaults(BlockTags.MINEABLE_AXE.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(MINEABLE_PICKAXE).defaults(BlockTags.MINEABLE_PICKAXE.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(MINEABLE_SHOVEL).defaults(BlockTags.MINEABLE_SHOVEL.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(MINEABLE_HOE).defaults(BlockTags.MINEABLE_HOE.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(NEEDS_DIAMOND_TOOL).defaults(BlockTags.NEEDS_DIAMOND_TOOL.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(NEEDS_IRON_TOOL).defaults(BlockTags.NEEDS_IRON_TOOL.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(NEEDS_STONE_TOOL).defaults(BlockTags.NEEDS_STONE_TOOL.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_17)), SyncedTag.builder(SWORD_EFFICIENT).defaults(BlockTags.SWORD_EFFICIENT.getStates()).supported(version.isNewerThanOrEquals(ClientVersion.V_1_20)));
   }

   @SafeVarargs
   private <T> void trackTags(ResourceLocation location, Function<Integer, T> remapper, SyncedTag.Builder<T>... syncedTags) {
      Map<ResourceLocation, SyncedTag<?>> tags = new HashMap(syncedTags.length);
      SyncedTag.Builder[] var5 = syncedTags;
      int var6 = syncedTags.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SyncedTag.Builder<T> syncedTag = var5[var7];
         syncedTag.remapper(remapper);
         SyncedTag<T> built = syncedTag.build();
         tags.put(built.location(), built);
      }

      this.synced.put(location, tags);
   }

   public SyncedTag<StateType> block(ResourceLocation tag) {
      Map<ResourceLocation, SyncedTag<?>> blockTags = (Map)this.synced.get(BLOCK);
      return (SyncedTag)blockTags.get(tag);
   }

   public void handleTagSync(WrapperPlayServerTags tags) {
      if (!this.player.getClientVersion().isOlderThan(ClientVersion.V_1_13)) {
         tags.getTagMap().forEach((location, tagList) -> {
            if (this.synced.containsKey(location)) {
               Map<ResourceLocation, SyncedTag<?>> syncedTags = (Map)this.synced.get(location);
               tagList.forEach((tag) -> {
                  if (syncedTags.containsKey(tag.getKey())) {
                     ((SyncedTag)syncedTags.get(tag.getKey())).readTagValues(tag);
                  }
               });
            }
         });
      }
   }

   static {
      BLOCK = VERSION.isNewerThanOrEquals(ServerVersion.V_1_21) ? ResourceLocation.minecraft("block") : ResourceLocation.minecraft("blocks");
   }
}
