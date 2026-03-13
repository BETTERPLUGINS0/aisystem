package me.casperge.realisticseasons1_20_R2;

import com.comphenix.protocol.events.PacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.casperge.interfaces.ProtocolLibUtils;
import me.casperge.realisticseasons.biome.BiomeUtils;
import me.casperge.realisticseasons.biome.BlockReplacement;
import me.casperge.realisticseasons.biome.BlockReplacements;
import me.casperge.realisticseasons.biome.HeightAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftBiome;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;

public class ProtocolLibUtils1_20_R2 implements ProtocolLibUtils {
   DedicatedServer dedicatedserver;
   HashMap<Block, ProtocolLibUtils1_20_R2.Replacement> replacements;
   int winterBiomeID;
   int fallBiomeID;

   public ProtocolLibUtils1_20_R2() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
      this.winterBiomeID = this.getBiomeRegistry().a(CraftBiome.bukkitToMinecraft(Biome.SNOWY_PLAINS));
      this.fallBiomeID = this.getBiomeRegistry().a(CraftBiome.bukkitToMinecraft(Biome.SAVANNA));
   }

   public void readPacket(PacketContainer var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, boolean var7, int var8, boolean var9) {
      if (this.replacements == null) {
         this.replacements = new HashMap();
         Iterator var10 = BlockReplacements.replacements.iterator();

         while(var10.hasNext()) {
            BlockReplacement var11 = (BlockReplacement)var10.next();
            ProtocolLibUtils1_20_R2.Replacement var12 = new ProtocolLibUtils1_20_R2.Replacement();
            var12.replacement = CraftMagicNumbers.getBlock(var11.getReplacent());
            var12.seasons = var11.getSeasons();
            var12.phases = var11.getPhases();
            this.replacements.put(CraftMagicNumbers.getBlock(var11.getOriginal()), var12);
         }
      }

      this.readPacketOld(var1, var2, var3, var4, var5);
   }

   public void readPacketOld(PacketContainer var1, int var2, boolean var3, int var4, int var5) {
      ClientboundLevelChunkPacketData var6 = (ClientboundLevelChunkPacketData)var1.getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
      int var7 = (Integer)var1.getIntegers().read(0);
      int var8 = (Integer)var1.getIntegers().read(1);
      ChunkSection[] var9 = this.extractDataSafe(var6, var5);
      int[] var10 = new int[var9.length * 64];
      int var11 = 0;
      ChunkSection[] var12 = var9;
      int var13 = var9.length;

      int var14;
      ChunkSection var15;
      int var16;
      int var17;
      int var18;
      for(var14 = 0; var14 < var13; ++var14) {
         var15 = var12[var14];

         for(var16 = 0; var16 < 4; ++var16) {
            for(var17 = 0; var17 < 4; ++var17) {
               for(var18 = 0; var18 < 4; ++var18) {
                  int var19 = this.getBiomeRegistry().a((BiomeBase)var15.c(var16, var17, var18).a());
                  var10[var11] = var19;
                  ++var11;
               }
            }
         }
      }

      if (var3) {
         int var21;
         switch(var2) {
         case 0:
            var21 = this.winterBiomeID;
            break;
         case 3:
            var21 = this.fallBiomeID;
            break;
         default:
            var21 = 0;
         }

         if (var21 != 0) {
            for(var13 = 0; var13 < var10.length; ++var13) {
               var10[var13] = var21;
            }
         }
      } else {
         var10 = BiomeUtils.updateBiomes(var10, var2, var4, var7, var8);
      }

      var11 = 0;
      var12 = var9;
      var13 = var9.length;

      for(var14 = 0; var14 < var13; ++var14) {
         var15 = var12[var14];
         if (!var15.c() && var15.h() != null) {
            for(var16 = 0; var16 < 16; ++var16) {
               for(var17 = 0; var17 < 16; ++var17) {
                  for(var18 = 0; var18 < 16; ++var18) {
                     if (this.replacements.containsKey(var15.a(var16, var17, var18).b())) {
                        ProtocolLibUtils1_20_R2.Replacement var24 = (ProtocolLibUtils1_20_R2.Replacement)this.replacements.get(var15.a(var16, var17, var18).b());
                        if (var24.seasons.contains(var2) && var24.phases.contains(var4)) {
                           var15.h().c(var16, var17, var18, var24.replacement.n());
                        }
                     }
                  }
               }
            }
         }

         for(var16 = 0; var16 < 4; ++var16) {
            for(var17 = 0; var17 < 4; ++var17) {
               for(var18 = 0; var18 < 4; ++var18) {
                  var15.setBiome(var16, var17, var18, Holder.a((BiomeBase)this.getBiomeRegistry().a(var10[var11])));
                  ++var11;
               }
            }
         }
      }

      byte[] var22 = this.toByteArray(var9);

      try {
         Field var23 = var6.getClass().getDeclaredField("c");
         var23.setAccessible(true);
         var23.set(var6, var22);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var20) {
         var20.printStackTrace();
      }

   }

   public ChunkSection[] extractDataSafe(ClientboundLevelChunkPacketData var1, int var2) {
      PacketDataSerializer var3 = var1.a();
      ChunkSection[] var4 = new ChunkSection[var2];
      String var5 = "";

      int var6;
      for(var6 = 0; var6 < var4.length; ++var6) {
         var4[var6] = new ChunkSection(this.getBiomeRegistry());
      }

      var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ChunkSection var8 = var4[var7];

         try {
            var8.a(var3);
         } catch (Exception var10) {
            System.out.println("Exception occured on section number " + String.valueOf(var7) + "/" + var6 + " dimension: " + var5);
            var10.printStackTrace();
         }
      }

      return var4;
   }

   public byte[] toByteArray(ChunkSection[] var1) {
      int var2 = 0;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ChunkSection var5 = var1[var4];
         var2 += var5.j();
      }

      byte[] var9 = new byte[var2];
      ByteBuf var10 = Unpooled.wrappedBuffer(var9);
      var10.writerIndex(0);
      PacketDataSerializer var6 = new PacketDataSerializer(var10);

      for(int var7 = 0; var7 < var3; ++var7) {
         ChunkSection var8 = var1[var7];
         var8.c(var6);
      }

      return var9;
   }

   private IRegistry<BiomeBase> getBiomeRegistry() {
      return this.dedicatedserver.aU().d(Registries.ap);
   }

   private class Replacement {
      List<Integer> seasons;
      List<Integer> phases;
      Block replacement;

      private Replacement() {
      }

      // $FF: synthetic method
      Replacement(Object var2) {
         this();
      }
   }
}
