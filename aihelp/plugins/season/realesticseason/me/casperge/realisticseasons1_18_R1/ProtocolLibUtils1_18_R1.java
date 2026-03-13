package me.casperge.realisticseasons1_18_R1;

import com.comphenix.protocol.events.PacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import me.casperge.realisticseasons.biome.BiomeUtils;
import net.minecraft.core.IRegistry;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;

public class ProtocolLibUtils1_18_R1 {
   DedicatedServer dedicatedserver;

   public ProtocolLibUtils1_18_R1() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
   }

   public void readPacket(PacketContainer var1, World var2, int var3, boolean var4, int var5) {
      ClientboundLevelChunkPacketData var6 = (ClientboundLevelChunkPacketData)var1.getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
      int var7 = (Integer)var1.getIntegers().read(0);
      int var8 = (Integer)var1.getIntegers().read(1);
      ChunkSection[] var9 = this.extractData(var6, var2);
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
                  int var19 = this.dedicatedserver.aV().d(IRegistry.aR).a(var15.c(var16, var17, var18));
                  var10[var11] = var19;
                  ++var11;
               }
            }
         }
      }

      if (var4) {
         byte var21;
         switch(var3) {
         case 0:
            var21 = 3;
            break;
         case 3:
            var21 = 16;
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
         var10 = BiomeUtils.updateBiomes(var10, var3, var5, var7, var8);
      }

      var11 = 0;
      var12 = var9;
      var13 = var9.length;

      for(var14 = 0; var14 < var13; ++var14) {
         var15 = var12[var14];

         for(var16 = 0; var16 < 4; ++var16) {
            for(var17 = 0; var17 < 4; ++var17) {
               for(var18 = 0; var18 < 4; ++var18) {
                  var15.setBiome(var16, var17, var18, (BiomeBase)this.dedicatedserver.aV().d(IRegistry.aR).a(var10[var11]));
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

   public ChunkSection[] extractData(ClientboundLevelChunkPacketData var1, World var2) {
      PacketDataSerializer var3 = var1.a();
      WorldServer var4 = ((CraftWorld)var2).getHandle();
      ChunkSection[] var5 = new ChunkSection[var4.ah()];

      int var6;
      for(var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = new ChunkSection(var4.g(var6), this.dedicatedserver.aV().d(IRegistry.aR));
      }

      var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ChunkSection var8 = var5[var7];
         var8.a(var3);
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
         var8.b(var6);
      }

      return var9;
   }
}
