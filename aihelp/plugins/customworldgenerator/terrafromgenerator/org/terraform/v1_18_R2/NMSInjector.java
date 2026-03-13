package org.terraform.v1_18_R2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_18_R2.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.v1_18_R2.generator.CustomChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.BlockDataFixerAbstract;
import org.terraform.coregen.NMSInjectorAbstract;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.coregen.populatordata.PopulatorDataSpigotAPI;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;

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
      CraftWorld cw = (CraftWorld)world;
      WorldServer ws = cw.getHandle();
      TerraformWorld.get(world).minY = -64;
      TerraformWorld.get(world).maxY = 320;
      NMSChunkGenerator bpg = new NMSChunkGenerator(world.getName(), (long)((int)world.getSeed()), ws.k().g());
      TerraformGeneratorPlugin.logger.info("NMSChunkGenerator Delegate is of type " + ws.k().g().getClass().getSimpleName());
      if (ws.k().g() instanceof CustomChunkGenerator) {
         try {
            Field f = CustomChunkGenerator.class.getDeclaredField("delegate");
            f.setAccessible(true);
            ChunkGenerator delegate = (ChunkGenerator)f.get(ws.k().g());
            TerraformGeneratorPlugin.logger.info("CustomChunkGenerator Delegate is of type " + delegate.getClass().getSimpleName());
         } catch (Exception var8) {
            TerraformGeneratorPlugin.logger.stackTrace(var8);
         }
      }

      TLogger var10000 = TerraformGeneratorPlugin.logger;
      int var10001 = ws.q_().k();
      var10000.info("- minY " + var10001 + "   " + world.getMinHeight());
      var10000 = TerraformGeneratorPlugin.logger;
      var10001 = ws.q_().l();
      var10000.info("- Height " + var10001 + "   " + world.getMaxHeight());
      var10000 = TerraformGeneratorPlugin.logger;
      boolean var10 = ws.q_().m();
      var10000.info("- LogicalHeight " + var10 + "   " + world.getLogicalHeight());
      PlayerChunkMap pcm = ws.k().a;

      try {
         TerraformGeneratorPlugin.privateFieldHandler.injectField(pcm, (String)"u", bpg);
         return true;
      } catch (Throwable var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
         return false;
      }
   }

   @NotNull
   public PopulatorDataICAAbstract getICAData(@NotNull Chunk chunk) {
      IChunkAccess ica = ((CraftChunk)chunk).getHandle();
      CraftWorld cw = (CraftWorld)chunk.getWorld();
      WorldServer ws = cw.getHandle();
      TerraformWorld tw = TerraformWorld.get(chunk.getWorld());
      return new PopulatorDataICA(new PopulatorDataPostGen(chunk), tw, ws, ica, chunk.getX(), chunk.getZ());
   }

   @Nullable
   public PopulatorDataICAAbstract getICAData(PopulatorDataAbstract data) {
      if (data instanceof PopulatorDataSpigotAPI) {
         PopulatorDataSpigotAPI pdata = (PopulatorDataSpigotAPI)data;
         GeneratorAccessSeed gas = ((CraftLimitedRegion)pdata.lr).getHandle();
         WorldServer ws = gas.getMinecraftWorld();
         TerraformWorld tw = TerraformWorld.get(ws.getWorld().getName(), ws.D());
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
         NBTTagCompound nbttagcompound = new NBTTagCompound();
         nbttagcompound.a("id", "minecraft:bee");
         teb.a(nbttagcompound, 0, false);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
         throw new RuntimeException(var4);
      }
   }

   public int getMinY() {
      return -64;
   }

   public int getMaxY() {
      return 320;
   }
}
