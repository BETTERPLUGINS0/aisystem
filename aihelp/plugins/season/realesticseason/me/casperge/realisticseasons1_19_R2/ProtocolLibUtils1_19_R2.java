package me.casperge.realisticseasons1_19_R2;

import com.comphenix.protocol.events.PacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import me.casperge.realisticseasons.biome.BiomeUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.ThreadingDetector;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.DataPaletteBlock;
import net.minecraft.world.level.chunk.DataPaletteBlock.d;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

public class ProtocolLibUtils1_19_R2 {
   boolean hasGlitched = true;
   DedicatedServer dedicatedserver;
   private final ThreadingDetector threadingDetector = new ThreadingDetector("PalettedContainer");

   public ProtocolLibUtils1_19_R2() {
      Server var1 = Bukkit.getServer();
      CraftServer var2 = (CraftServer)var1;
      this.dedicatedserver = var2.getServer();
   }

   public void readPacket(PacketContainer var1, World var2, int var3, boolean var4, int var5) {
      if (this.hasGlitched) {
         this.readPacketOld(var1, var2, var3, var4, var5);
      } else {
         ClientboundLevelChunkPacketData var6 = (ClientboundLevelChunkPacketData)var1.getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
         int var7 = (Integer)var1.getIntegers().read(0);
         int var8 = (Integer)var1.getIntegers().read(1);

         try {
            ProtocolLibUtils1_19_R2.CustomChunkSection[] var9 = this.extractData(var6, var2);
            int[] var10 = new int[var9.length * 64];
            int var11 = 0;
            ProtocolLibUtils1_19_R2.CustomChunkSection[] var12 = var9;
            int var13 = var9.length;

            int var14;
            ProtocolLibUtils1_19_R2.CustomChunkSection var15;
            int var16;
            int var17;
            int var18;
            for(var14 = 0; var14 < var13; ++var14) {
               var15 = var12[var14];

               for(var16 = 0; var16 < 4; ++var16) {
                  for(var17 = 0; var17 < 4; ++var17) {
                     for(var18 = 0; var18 < 4; ++var18) {
                        int var19 = this.dedicatedserver.aW().d(Registries.al).a((BiomeBase)((Holder)var15.getDataPaletteBiome().a(var16, var17, var18)).a());
                        var10[var11] = var19;
                        ++var11;
                     }
                  }
               }
            }

            if (var4) {
               byte var22;
               switch(var3) {
               case 0:
                  var22 = 44;
                  break;
               case 3:
                  var22 = 40;
                  break;
               default:
                  var22 = 0;
               }

               if (var22 != 0) {
                  for(var13 = 0; var13 < var10.length; ++var13) {
                     var10[var13] = var22;
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
                        var15.getDataPaletteBiome().c(var16, var17, var18, Holder.a((BiomeBase)this.dedicatedserver.aW().d(Registries.al).a(var10[var11])));
                        ++var11;
                     }
                  }
               }
            }

            byte[] var23 = this.toByteArray(var9);

            try {
               Field var24 = var6.getClass().getDeclaredField("c");
               var24.setAccessible(true);
               var24.set(var6, var23);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var20) {
               var20.printStackTrace();
            }

         } catch (Exception var21) {
            this.hasGlitched = true;
         }
      }
   }

   public ProtocolLibUtils1_19_R2.CustomChunkSection[] extractData(ClientboundLevelChunkPacketData var1, World var2) {
      PacketDataSerializer var3 = var1.a();
      WorldServer var4 = ((CraftWorld)var2).getHandle();
      ProtocolLibUtils1_19_R2.CustomChunkSection[] var5 = new ProtocolLibUtils1_19_R2.CustomChunkSection[var4.aj()];

      int var6;
      for(var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = new ProtocolLibUtils1_19_R2.CustomChunkSection(this.dedicatedserver.aW().d(Registries.al));
      }

      var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ProtocolLibUtils1_19_R2.CustomChunkSection var8 = var5[var7];
         var8.setNonEmptyBlockCount(var3.readShort());
         int var9 = var3.readerIndex();
         this.threadingDetector.a();

         try {
            byte var10 = var3.readByte();
            boolean var11 = true;
            int var19;
            if (var10 == 0) {
               var19 = 3;
            } else if (var10 > 8) {
               DataPaletteBlock var20 = new DataPaletteBlock(Block.o, Blocks.a.n(), d.d);
               var3.readerIndex(var9);
               var20.a(var3);
               var19 = var3.readerIndex() - var9;
            } else {
               int var12 = var3.readerIndex();
               int var13 = this.readVarInt(var3);

               int var14;
               for(var14 = 0; var14 < var13; ++var14) {
                  this.readVarInt(var3);
               }

               var19 = var3.readerIndex() - var12;
               var14 = this.readVarInt(var3);
               var19 = var19 + var14 * 8 + 3;
            }

            var8.setStatesArray(var3.copy(var9, var19).array());
            var3.readerIndex(var19 + var9);
            var8.setStatesSize(var19);
         } finally {
            this.threadingDetector.b();
         }

         DataPaletteBlock var18 = var8.getDataPaletteBiome();
         var18.a(var3);
         var8.setDataPaletteBiome(var18);
      }

      return var5;
   }

   public byte[] toByteArray(ProtocolLibUtils1_19_R2.CustomChunkSection[] var1) {
      int var2 = 0;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ProtocolLibUtils1_19_R2.CustomChunkSection var5 = var1[var4];
         var2 += var5.getByteBufSize();
      }

      byte[] var9 = new byte[var2];
      ByteBuf var10 = Unpooled.wrappedBuffer(var9);
      var10.writerIndex(0);
      PacketDataSerializer var6 = new PacketDataSerializer(var10);

      for(int var7 = 0; var7 < var3; ++var7) {
         ProtocolLibUtils1_19_R2.CustomChunkSection var8 = var1[var7];
         var8.write(var6);
      }

      return var9;
   }

   public int readVarInt(PacketDataSerializer var1) {
      int var3 = 0;
      int var4 = 0;

      byte var2;
      do {
         var2 = var1.readByte();
         var3 |= (var2 & 127) << var4++ * 7;
         if (var4 > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while((var2 & 128) == 128);

      return var3;
   }

   public long readVarLong(PacketDataSerializer var1) {
      long var3 = 0L;
      int var5 = 0;

      byte var2;
      do {
         var2 = var1.readByte();
         var3 |= (long)((var2 & 127) << var5++ * 7);
         if (var5 > 10) {
            throw new RuntimeException("VarLong too big");
         }
      } while((var2 & 128) == 128);

      return var3;
   }

   public void readPacketOld(PacketContainer var1, World var2, int var3, boolean var4, int var5) {
      ClientboundLevelChunkPacketData var6 = (ClientboundLevelChunkPacketData)var1.getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
      int var7 = (Integer)var1.getIntegers().read(0);
      int var8 = (Integer)var1.getIntegers().read(1);
      ChunkSection[] var9 = this.extractDataSafe(var6, var2);
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
                  int var19 = this.dedicatedserver.aW().d(Registries.al).a((BiomeBase)var15.c(var16, var17, var18).a());
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
            var21 = 44;
            break;
         case 3:
            var21 = 40;
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
                  var15.setBiome(var16, var17, var18, Holder.a((BiomeBase)this.dedicatedserver.aW().d(Registries.al).a(var10[var11])));
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

   public ChunkSection[] extractDataSafe(ClientboundLevelChunkPacketData var1, World var2) {
      PacketDataSerializer var3 = var1.a();
      WorldServer var4 = ((CraftWorld)var2).getHandle();
      ChunkSection[] var5 = new ChunkSection[var4.aj()];

      int var6;
      for(var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = new ChunkSection(var4.g(var6), this.dedicatedserver.aW().d(Registries.al));
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

   class CustomChunkSection {
      private short nonEmptyBlockCount;
      private DataPaletteBlock<Holder<BiomeBase>> dataPaletteBiome;
      private byte[] statesArray;
      private int statesSize;

      public CustomChunkSection(IRegistry<BiomeBase> param2) {
         this.dataPaletteBiome = new DataPaletteBlock(var2.t(), var2.f(Biomes.b), d.e);
      }

      public DataPaletteBlock<Holder<BiomeBase>> getDataPaletteBiome() {
         return this.dataPaletteBiome;
      }

      public short getNonEmptyBlockCount() {
         return this.nonEmptyBlockCount;
      }

      public void setNonEmptyBlockCount(short var1) {
         this.nonEmptyBlockCount = var1;
      }

      public void setDataPaletteBiome(DataPaletteBlock<Holder<BiomeBase>> var1) {
         this.dataPaletteBiome = var1;
      }

      public int getByteBufSize() {
         return 2 + this.statesSize + this.dataPaletteBiome.c();
      }

      public void write(PacketDataSerializer var1) {
         var1.writeShort(this.nonEmptyBlockCount);
         var1.writeBytes(this.statesArray);
         this.dataPaletteBiome.b(var1);
      }

      public void setStatesArray(byte[] var1) {
         this.statesArray = var1;
      }

      public int getStatesSize() {
         return this.statesSize;
      }

      public void setStatesSize(int var1) {
         this.statesSize = var1;
      }
   }
}
