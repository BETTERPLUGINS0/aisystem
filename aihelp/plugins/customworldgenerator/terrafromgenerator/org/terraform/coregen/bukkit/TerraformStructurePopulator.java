package org.terraform.coregen.bukkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.coregen.populatordata.PopulatorDataSpigotAPI;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.event.TerraformStructureSpawnEvent;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.JigsawState;
import org.terraform.structure.JigsawStructurePopulator;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.StructureRegistry;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.structure.room.path.PathState;
import org.terraform.structure.stronghold.StrongholdPopulator;
import org.terraform.utils.datastructs.ConcurrentLRUCache;

public class TerraformStructurePopulator extends BlockPopulator {
   private final ConcurrentLRUCache<MegaChunk, JigsawState> jigsawCache;
   private final TerraformWorld tw;

   public TerraformStructurePopulator(TerraformWorld tw) {
      this.tw = tw;
      this.jigsawCache = new ConcurrentLRUCache("jigsawCache", 20, (mc) -> {
         BiomeBank biome = mc.getCenterBiomeSection(tw).getBiomeBank();
         SingleMegaChunkStructurePopulator spop = this.getMegachunkStructure(mc, tw, biome);
         if (spop == null) {
            return null;
         } else if (spop instanceof JigsawStructurePopulator) {
            JigsawStructurePopulator jsp = (JigsawStructurePopulator)spop;
            return jsp.calculateRoomPopulators(tw, mc);
         } else {
            return null;
         }
      });
   }

   public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion lr) {
      MegaChunk mc = new MegaChunk(chunkX, chunkZ);
      JigsawState state = (JigsawState)this.jigsawCache.get(mc);
      if (state != null) {
         if (state.isInRange(chunkX, chunkZ)) {
            PopulatorDataAbstract data = new PopulatorDataSpigotAPI(lr, this.tw, chunkX, chunkZ);
            ArrayList<HashSet<PathState.PathNode>> seenNodes = new ArrayList();

            for(int i = 0; i < state.roomPopulatorStates.size(); ++i) {
               RoomLayoutGenerator roomLayoutGenerator = (RoomLayoutGenerator)state.roomPopulatorStates.get(i);
               HashSet<PathState.PathNode> nodes = new HashSet();
               PathState pathState = roomLayoutGenerator.getOrCalculatePathState(this.tw);
               pathState.nodes.stream().filter((node) -> {
                  return node.center.getX() >> 4 == chunkX && node.center.getZ() >> 4 == chunkZ;
               }).forEach((node) -> {
                  pathState.writer.apply(data, this.tw, node);
                  nodes.add(node);
               });
               seenNodes.add(nodes);
            }

            ArrayList<CubeRoom> seenRooms = new ArrayList();
            state.roomPopulatorStates.forEach((roomLayoutGeneratorx) -> {
               roomLayoutGeneratorx.getRooms().stream().filter((room) -> {
                  return room.canLRCarve(chunkX, chunkZ, lr);
               }).forEach((room) -> {
                  seenRooms.add(room);
                  if (roomLayoutGeneratorx.roomCarver != null) {
                     roomLayoutGeneratorx.roomCarver.carveRoom(data, room, roomLayoutGeneratorx.wallMaterials);
                  }

               });
            });
            seenNodes.forEach((nodesx) -> {
               nodesx.forEach((node) -> {
                  if (node.populator != null) {
                     node.populator.populate(new PathPopulatorData(new Wall(new SimpleBlock(data, node.center), node.connected.size() == 1 ? (BlockFace)node.connected.stream().findAny().get() : BlockFace.UP), node.pathRadius));
                  }

               });
            });
            seenRooms.stream().filter((room) -> {
               return room.getPop() != null;
            }).forEach((room) -> {
               room.getPop().populate(data, room);
            });
         }
      }
   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
      TerraformGeneratorPlugin.watchdogSuppressant.tickWatchdog();
      if (TerraformGeneratorPlugin.INJECTED_WORLDS.contains(world.getName())) {
         PopulatorDataPostGen data = new PopulatorDataPostGen(chunk);
         MegaChunk mc = new MegaChunk(chunk.getX(), chunk.getZ());
         BiomeBank biome = mc.getCenterBiomeSection(this.tw).getBiomeBank();
         TLogger var10000;
         if (TConfig.areStructuresEnabled() && (new StrongholdPopulator()).canSpawn(this.tw, data.getChunkX(), data.getChunkZ(), biome)) {
            var10000 = TerraformGeneratorPlugin.logger;
            int var10001 = data.getChunkX();
            var10000.info("Generating Stronghold at chunk: " + var10001 + "," + data.getChunkZ());
            (new StrongholdPopulator()).populate(this.tw, data);
         }

         int[] chunkCoords = mc.getCenterBiomeSectionChunkCoords();
         if (chunkCoords[0] == data.getChunkX() && chunkCoords[1] == data.getChunkZ()) {
            int[] blockCoords = mc.getCenterBiomeSectionBlockCoords();
            SingleMegaChunkStructurePopulator[] var9 = StructureRegistry.getLargeStructureForMegaChunk(this.tw, mc);
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               SingleMegaChunkStructurePopulator spop = var9[var11];
               if (spop != null && spop.isEnabled() && !(spop instanceof StrongholdPopulator) && TConfig.areStructuresEnabled() && spop.canSpawn(this.tw, data.getChunkX(), data.getChunkZ(), biome)) {
                  var10000 = TerraformGeneratorPlugin.logger;
                  String var13 = spop.getClass().getName();
                  var10000.info("Generating " + var13 + " at chunk: " + data.getChunkX() + "," + data.getChunkZ());
                  Bukkit.getPluginManager().callEvent(new TerraformStructureSpawnEvent(blockCoords[0], blockCoords[1], spop.getClass().getName()));
                  spop.populate(this.tw, data);
                  break;
               }
            }
         }

      }
   }

   @Nullable
   public SingleMegaChunkStructurePopulator getMegachunkStructure(@NotNull MegaChunk mc, @NotNull TerraformWorld tw, BiomeBank biome) {
      int[] chunkCoords = mc.getCenterBiomeSectionChunkCoords();
      SingleMegaChunkStructurePopulator[] var5 = StructureRegistry.getLargeStructureForMegaChunk(tw, mc);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SingleMegaChunkStructurePopulator spop = var5[var7];
         if (spop != null && spop.isEnabled() && !(spop instanceof StrongholdPopulator) && TConfig.areStructuresEnabled() && spop.canSpawn(tw, chunkCoords[0], chunkCoords[1], biome)) {
            return spop;
         }
      }

      return null;
   }
}
