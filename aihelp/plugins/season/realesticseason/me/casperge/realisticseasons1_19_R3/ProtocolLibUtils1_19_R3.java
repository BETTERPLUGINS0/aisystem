package me.casperge.realisticseasons1_19_R3;

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
import me.casperge.weather.WeatherPlugin;
import me.casperge.weather.interfaces.BiomePrecipitation;
import me.casperge.weather.interfaces.NMSUtils;
import me.casperge.weather.register.BiomeRegister;
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
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;

public class ProtocolLibUtils1_19_R3 implements ProtocolLibUtils {
   DedicatedServer dedicatedserver;
   HashMap<Block, ProtocolLibUtils1_19_R3.Replacement> replacements;

   public ProtocolLibUtils1_19_R3() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
   }

   public void readPacket(PacketContainer var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, boolean var7, int var8, boolean var9) {
      if (this.replacements == null) {
         this.replacements = new HashMap();
         Iterator var10 = BlockReplacements.replacements.iterator();

         while(var10.hasNext()) {
            BlockReplacement var11 = (BlockReplacement)var10.next();
            ProtocolLibUtils1_19_R3.Replacement var12 = new ProtocolLibUtils1_19_R3.Replacement();
            var12.replacement = CraftMagicNumbers.getBlock(var11.getReplacent());
            var12.seasons = var11.getSeasons();
            var12.phases = var11.getPhases();
            this.replacements.put(CraftMagicNumbers.getBlock(var11.getOriginal()), var12);
         }
      }

      this.readPacketOld(var1, var2, var3, var4, var5, var6, var8, var9);
   }

   public void readPacketOld(PacketContainer var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, int var7, boolean var8) {
      ClientboundLevelChunkPacketData var9 = (ClientboundLevelChunkPacketData)var1.getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
      int var10 = (Integer)var1.getIntegers().read(0);
      int var11 = (Integer)var1.getIntegers().read(1);
      ChunkSection[] var12 = this.extractDataSafe(var9, var5, var6);
      int[] var13 = new int[var12.length * 64];
      int var14 = 0;
      ChunkSection[] var15 = var12;
      int var16 = var12.length;

      int var17;
      ChunkSection var18;
      int var19;
      int var20;
      int var21;
      int var22;
      for(var17 = 0; var17 < var16; ++var17) {
         var18 = var15[var17];

         for(var19 = 0; var19 < 4; ++var19) {
            for(var20 = 0; var20 < 4; ++var20) {
               for(var21 = 0; var21 < 4; ++var21) {
                  var22 = this.getBiomeRegistry().a((BiomeBase)var18.c(var19, var20, var21).a());
                  var13[var14] = var22;
                  ++var14;
               }
            }
         }
      }

      if (var8) {
         if (var3) {
            byte var27;
            switch(var2) {
            case 0:
               var27 = 44;
               break;
            case 3:
               var27 = 40;
               break;
            default:
               var27 = 0;
            }

            if (var27 != 0) {
               for(var16 = 0; var16 < var13.length; ++var16) {
                  var13[var16] = var27;
               }
            }
         } else {
            var13 = BiomeUtils.updateBiomes(var13, var2, var4, var10, var11);
         }
      }

      var14 = 0;
      var15 = var12;
      var16 = var12.length;

      for(var17 = 0; var17 < var16; ++var17) {
         var18 = var15[var17];
         if (var8 && !var18.c() && var18.i() != null) {
            for(var19 = 0; var19 < 16; ++var19) {
               for(var20 = 0; var20 < 16; ++var20) {
                  for(var21 = 0; var21 < 16; ++var21) {
                     if (this.replacements.containsKey(var18.a(var19, var20, var21).b())) {
                        ProtocolLibUtils1_19_R3.Replacement var32 = (ProtocolLibUtils1_19_R3.Replacement)this.replacements.get(var18.a(var19, var20, var21).b());
                        if (var32.seasons.contains(var2) && var32.phases.contains(var4)) {
                           var18.i().c(var19, var20, var21, var32.replacement.o());
                        }
                     }
                  }
               }
            }
         }

         if (var7 == -1) {
            for(var19 = 0; var19 < 4; ++var19) {
               for(var20 = 0; var20 < 4; ++var20) {
                  for(var21 = 0; var21 < 4; ++var21) {
                     var18.setBiome(var19, var20, var21, Holder.a((BiomeBase)this.getBiomeRegistry().a(var13[var14])));
                     ++var14;
                  }
               }
            }
         } else {
            NMSUtils var30 = WeatherPlugin.getInstance().getNMSUtils();
            BiomePrecipitation var31 = null;
            if (var7 == 0) {
               var31 = BiomePrecipitation.NONE;
            } else if (var7 == 1) {
               var31 = BiomePrecipitation.RAIN;
            } else if (var7 == 2) {
               var31 = BiomePrecipitation.SNOW;
            } else if (var7 == 3) {
               var31 = BiomePrecipitation.SANDSTORM;
            }

            for(var21 = 0; var21 < 4; ++var21) {
               for(var22 = 0; var22 < 4; ++var22) {
                  for(int var23 = 0; var23 < 4; ++var23) {
                     int var24 = var13[var14];
                     int var25 = BiomeRegister.getReplacement(var24, var31);
                     var18.setBiome(var21, var22, var23, Holder.a((BiomeBase)this.getBiomeRegistry().a(var25)));
                     ++var14;
                  }
               }
            }
         }
      }

      byte[] var28 = this.toByteArray(var12);

      try {
         Field var29 = var9.getClass().getDeclaredField("c");
         var29.setAccessible(true);
         var29.set(var9, var28);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var26) {
         var26.printStackTrace();
      }

   }

   public ChunkSection[] extractDataSafe(ClientboundLevelChunkPacketData var1, int var2, HeightAccessor var3) {
      PacketDataSerializer var4 = var1.a();
      ChunkSection[] var5 = new ChunkSection[var2];

      int var6;
      for(var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = new ChunkSection(var3.getSectionYFromSectionIndex(var6), this.getBiomeRegistry());
      }

      var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ChunkSection var8 = var5[var7];
         var8.a(var4);
      }

      return var5;
   }

   public byte[] toByteArray(ChunkSection[] var1) {
      int var2 = 0;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ChunkSection var5 = var1[var4];
         var2 += var5.k();
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
      return this.dedicatedserver.aX().d(Registries.an);
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
