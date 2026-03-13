package org.terraform.v1_21_R6;

import java.lang.reflect.InvocationTargetException;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity.Occupant;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.generator.CraftLimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.BlockDataFixerAbstract;
import org.terraform.coregen.NMSInjectorAbstract;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.coregen.populatordata.PopulatorDataSpigotAPI;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.TerraformFieldHandler;
import org.terraform.utils.version.TerraformMethodHandler;

public class NMSInjector extends NMSInjectorAbstract {
   @Nullable
   private static TerraformMethodHandler getTileEntity = null;

   public void startupTasks() {
      CustomBiomeHandler.init();
   }

   @NotNull
   public BlockDataFixerAbstract getBlockDataFixer() {
      return new BlockDataFixer();
   }

   public boolean attemptInject(@NotNull World world) {
      try {
         CraftWorld cw = (CraftWorld)world;
         ServerLevel ws = cw.getHandle();
         TerraformWorld.get(world).minY = -64;
         TerraformWorld.get(world).maxY = 320;
         ChunkGenerator delegate = ws.getChunkSource().getGenerator();
         TerraformGeneratorPlugin.logger.info("NMSChunkGenerator Delegate is of type " + delegate.getClass().getSimpleName());
         NMSChunkGenerator bpg = new NMSChunkGenerator(world.getName(), (long)((int)world.getSeed()), delegate);
         ChunkMap pcm = ws.getChunkSource().chunkMap;
         TerraformFieldHandler wgc = new TerraformFieldHandler(pcm.getClass(), new String[]{"worldGenContext", "Q"});
         WorldGenContext worldGenContext = (WorldGenContext)wgc.field.get(pcm);
         wgc.field.set(pcm, new WorldGenContext(worldGenContext.level(), bpg, worldGenContext.structureManager(), worldGenContext.lightEngine(), worldGenContext.mainThreadExecutor(), worldGenContext.unsavedListener()));
         TerraformGeneratorPlugin.logger.info("Post injection: getChunkSource().getChunkGenerator() is of type " + ws.getChunkSource().getGenerator().getClass().getSimpleName());
         return true;
      } catch (Throwable var9) {
         TerraformGeneratorPlugin.logger.stackTrace(var9);
         return false;
      }
   }

   @NotNull
   public PopulatorDataICAAbstract getICAData(@NotNull Chunk chunk) {
      ChunkAccess ica = ((CraftChunk)chunk).getHandle(ChunkStatus.FULL);
      CraftWorld cw = (CraftWorld)chunk.getWorld();
      ServerLevel ws = cw.getHandle();
      TerraformWorld tw = TerraformWorld.get(chunk.getWorld());
      return new PopulatorDataICA(new PopulatorDataPostGen(chunk), tw, ws, ica, chunk.getX(), chunk.getZ());
   }

   public PopulatorDataICAAbstract getICAData(PopulatorDataAbstract data) {
      if (data instanceof PopulatorDataSpigotAPI) {
         PopulatorDataSpigotAPI pdata = (PopulatorDataSpigotAPI)data;
         WorldGenLevel gas = ((CraftLimitedRegion)pdata.lr).getHandle();
         ServerLevel ws = gas.getMinecraftWorld();
         TerraformWorld tw = TerraformWorld.get(ws.getWorld().getName(), ws.getSeed());
         return new PopulatorDataICA(data, tw, ws, gas.getChunk(data.getChunkX(), data.getChunkZ()), data.getChunkX(), data.getChunkZ());
      } else if (data instanceof PopulatorDataPostGen) {
         PopulatorDataPostGen gdata = (PopulatorDataPostGen)data;
         return this.getICAData(gdata.getChunk());
      } else {
         return null;
      }
   }

   public void storeBee(Beehive hive) {
      try {
         if (getTileEntity == null) {
            getTileEntity = new TerraformMethodHandler(CraftBlockEntityState.class, new String[]{"getTileEntity", "getBlockEntity"}, new Class[0]);
         }

         BeehiveBlockEntity teb = (BeehiveBlockEntity)getTileEntity.method.invoke(hive);
         teb.storeBee(Occupant.create(GenUtils.RANDOMIZER.nextInt(599)));
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var3) {
         throw new RuntimeException(var3);
      }
   }

   public int getMinY() {
      return -64;
   }

   public int getMaxY() {
      return 320;
   }
}
