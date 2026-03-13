package org.terraform.v1_21_R1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import net.minecraft.world.level.block.entity.TileEntityBeehive.c;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.craftbukkit.v1_21_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_21_R1.generator.CraftLimitedRegion;
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

public class NMSInjector extends NMSInjectorAbstract {
   @Nullable
   private static Method getTileEntity = null;

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
         WorldServer ws = cw.getHandle();
         TerraformWorld.get(world).minY = -64;
         TerraformWorld.get(world).maxY = 320;
         ChunkGenerator delegate = ws.l().g();
         TerraformGeneratorPlugin.logger.info("NMSChunkGenerator Delegate is of type " + delegate.getClass().getSimpleName());
         NMSChunkGenerator bpg = new NMSChunkGenerator(world.getName(), (long)((int)world.getSeed()), delegate);
         PlayerChunkMap pcm = ws.l().a;
         Field wgc = pcm.getClass().getDeclaredField("P");
         wgc.setAccessible(true);
         WorldGenContext worldGenContext = (WorldGenContext)wgc.get(pcm);
         wgc.set(pcm, new WorldGenContext(worldGenContext.a(), bpg, worldGenContext.c(), worldGenContext.d(), worldGenContext.e()));
         TerraformGeneratorPlugin.logger.info("Post injection: getChunkSource().getChunkGenerator() is of type " + ws.l().g().getClass().getSimpleName());
         return true;
      } catch (Throwable var9) {
         TerraformGeneratorPlugin.logger.stackTrace(var9);
         return false;
      }
   }

   @NotNull
   public PopulatorDataICAAbstract getICAData(@NotNull Chunk chunk) {
      IChunkAccess ica = ((CraftChunk)chunk).getHandle(ChunkStatus.n);
      CraftWorld cw = (CraftWorld)chunk.getWorld();
      WorldServer ws = cw.getHandle();
      TerraformWorld tw = TerraformWorld.get(chunk.getWorld());
      return new PopulatorDataICA(new PopulatorDataPostGen(chunk), tw, ws, ica, chunk.getX(), chunk.getZ());
   }

   public PopulatorDataICAAbstract getICAData(PopulatorDataAbstract data) {
      if (data instanceof PopulatorDataSpigotAPI) {
         PopulatorDataSpigotAPI pdata = (PopulatorDataSpigotAPI)data;
         GeneratorAccessSeed gas = ((CraftLimitedRegion)pdata.lr).getHandle();
         WorldServer ws = gas.getMinecraftWorld();
         TerraformWorld tw = TerraformWorld.get(ws.getWorld().getName(), ws.C());
         return new PopulatorDataICA(data, tw, ws, gas.a(data.getChunkX(), data.getChunkZ()), data.getChunkX(), data.getChunkZ());
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
            getTileEntity = CraftBlockEntityState.class.getDeclaredMethod("getTileEntity");
            getTileEntity.setAccessible(true);
         }

         TileEntityBeehive teb = (TileEntityBeehive)getTileEntity.invoke(hive);
         teb.a(c.a(GenUtils.RANDOMIZER.nextInt(599)));
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
