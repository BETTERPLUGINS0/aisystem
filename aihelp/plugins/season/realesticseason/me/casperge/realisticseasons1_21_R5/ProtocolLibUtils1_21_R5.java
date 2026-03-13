package me.casperge.realisticseasons1_21_R5;

import com.comphenix.protocol.events.PacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_21_R5.CraftServer;
import org.bukkit.craftbukkit.v1_21_R5.block.CraftBiome;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftMagicNumbers;

public class ProtocolLibUtils1_21_R5 implements ProtocolLibUtils {
   DedicatedServer dedicatedserver;
   HashMap<Block, ProtocolLibUtils1_21_R5.Replacement> replacements;
   int winterBiomeID;
   int fallBiomeID;
   Random r;

   public ProtocolLibUtils1_21_R5() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
      this.winterBiomeID = this.getBiomeRegistry().a(CraftBiome.bukkitToMinecraft(Biome.SNOWY_PLAINS));
      this.fallBiomeID = this.getBiomeRegistry().a(CraftBiome.bukkitToMinecraft(Biome.SAVANNA));
      this.r = new Random();
   }

   public void readPacket(PacketContainer var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, boolean var7, int var8, boolean var9) {
      if (this.replacements == null) {
         this.replacements = new HashMap();
         Iterator var10 = BlockReplacements.replacements.iterator();

         while(var10.hasNext()) {
            BlockReplacement var11 = (BlockReplacement)var10.next();
            ProtocolLibUtils1_21_R5.Replacement var12 = new ProtocolLibUtils1_21_R5.Replacement();
            var12.replacement = CraftMagicNumbers.getBlock(var11.getReplacent());
            var12.seasons = var11.getSeasons();
            var12.phases = var11.getPhases();
            this.replacements.put(CraftMagicNumbers.getBlock(var11.getOriginal()), var12);
         }
      }

      this.readPacketOld(var1, var2, var3, var4, var5, var7);
   }

   public void readPacketOld(PacketContainer var1, int var2, boolean var3, int var4, int var5, boolean var6) {
      ClientboundLevelChunkPacketData var7 = (ClientboundLevelChunkPacketData)var1.getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
      int var8 = (Integer)var1.getIntegers().read(0);
      int var9 = (Integer)var1.getIntegers().read(1);
      ChunkSection[] var10 = this.extractDataSafe(var7, var5);
      int[] var11 = new int[var10.length * 64];
      int var12 = 0;
      ChunkSection[] var13 = var10;
      int var14 = var10.length;

      int var15;
      ChunkSection var16;
      int var17;
      int var18;
      int var19;
      int var20;
      for(var15 = 0; var15 < var14; ++var15) {
         var16 = var13[var15];

         for(var17 = 0; var17 < 4; ++var17) {
            for(var18 = 0; var18 < 4; ++var18) {
               for(var19 = 0; var19 < 4; ++var19) {
                  var20 = this.getBiomeRegistry().a((BiomeBase)var16.c(var17, var18, var19).a());
                  var11[var12] = var20;
                  ++var12;
               }
            }
         }
      }

      if (var3) {
         int var27;
         switch(var2) {
         case 0:
            var27 = this.winterBiomeID;
            break;
         case 3:
            var27 = this.fallBiomeID;
            break;
         default:
            var27 = 0;
         }

         if (var27 != 0) {
            for(var14 = 0; var14 < var11.length; ++var14) {
               var11[var14] = var27;
            }
         }
      } else {
         var11 = BiomeUtils.updateBiomes(var11, var2, var4, var8, var9);
      }

      var12 = 0;
      var13 = var10;
      var14 = var10.length;

      for(var15 = 0; var15 < var14; ++var15) {
         var16 = var13[var15];
         if (!var16.c() && var16.h() != null) {
            ArrayList var30 = new ArrayList();

            for(var18 = 0; var18 < 16; ++var18) {
               for(var19 = 0; var19 < 16; ++var19) {
                  for(var20 = 0; var20 < 16; ++var20) {
                     if (this.replacements.containsKey(var16.a(var18, var19, var20).b())) {
                        ProtocolLibUtils1_21_R5.Replacement var21 = (ProtocolLibUtils1_21_R5.Replacement)this.replacements.get(var16.a(var18, var19, var20).b());
                        if (var21.seasons.contains(var2) && var21.phases.contains(var4)) {
                           var16.h().c(var18, var19, var20, var21.replacement.m());
                        }
                     }

                     if (var6 && var16.a(var18, var19, var20).b().equals(CraftMagicNumbers.getBlock(Material.SPRUCE_LEAVES))) {
                        Double var31 = this.r.nextDouble();
                        if (var31 < 0.2D) {
                           int var22 = 0;
                           if (var18 < 15 && !var16.a(var18 + 1, var19, var20).b().equals(CraftMagicNumbers.getBlock(Material.AIR))) {
                              ++var22;
                           }

                           if (var18 > 0 && !var16.a(var18 - 1, var19, var20).b().equals(CraftMagicNumbers.getBlock(Material.AIR))) {
                              ++var22;
                           }

                           if (var20 > 0 && !var16.a(var18, var19, var20 - 1).b().equals(CraftMagicNumbers.getBlock(Material.AIR))) {
                              ++var22;
                           }

                           if (var20 < 15 && !var16.a(var18, var19, var20 + 1).b().equals(CraftMagicNumbers.getBlock(Material.AIR))) {
                              ++var22;
                           }

                           boolean var23 = false;
                           Iterator var24 = var30.iterator();

                           while(var24.hasNext()) {
                              ProtocolLibUtils1_21_R5.Vector3D var25 = (ProtocolLibUtils1_21_R5.Vector3D)var24.next();
                              if (Math.abs(var25.x - var18) <= 1 && Math.abs(var25.y - var19) <= 1 && Math.abs(var25.z - var20) <= 1) {
                                 var23 = true;
                              }
                           }

                           if (var22 > 1 && var22 < 4 && !var23) {
                              if (var31 < 0.05D) {
                                 var16.h().c(var18, var19, var20, CraftMagicNumbers.getBlock(Material.RED_TERRACOTTA).m());
                              } else if (var31 < 0.1D) {
                                 var16.h().c(var18, var19, var20, CraftMagicNumbers.getBlock(Material.YELLOW_TERRACOTTA).m());
                              } else if (var31 < 0.15D) {
                                 var16.h().c(var18, var19, var20, CraftMagicNumbers.getBlock(Material.LIGHT_BLUE_TERRACOTTA).m());
                              } else {
                                 var16.h().c(var18, var19, var20, CraftMagicNumbers.getBlock(Material.LIME_TERRACOTTA).m());
                              }

                              var30.add(new ProtocolLibUtils1_21_R5.Vector3D(var18, var19, var20));
                           }
                        }
                     }
                  }
               }
            }
         }

         for(var17 = 0; var17 < 4; ++var17) {
            for(var18 = 0; var18 < 4; ++var18) {
               for(var19 = 0; var19 < 4; ++var19) {
                  var16.setBiome(var17, var18, var19, Holder.a((BiomeBase)this.getBiomeRegistry().a(var11[var12])));
                  ++var12;
               }
            }
         }
      }

      byte[] var28 = this.toByteArray(var10);

      try {
         Field var29 = var7.getClass().getDeclaredField("d");
         var29.setAccessible(true);
         var29.set(var7, var28);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var26) {
         var26.printStackTrace();
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
      return this.dedicatedserver.ba().f(Registries.aK);
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

   private class Vector3D {
      int x;
      int y;
      int z;

      public Vector3D(int param2, int param3, int param4) {
         this.x = var2;
         this.y = var3;
         this.z = var4;
      }
   }
}
