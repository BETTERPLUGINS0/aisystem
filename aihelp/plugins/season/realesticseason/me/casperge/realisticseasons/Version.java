package me.casperge.realisticseasons;

import me.casperge.interfaces.CustomBiome;
import me.casperge.interfaces.FakeArmorStand;
import me.casperge.interfaces.GameRuleGetter;
import me.casperge.interfaces.NmsCode;
import me.casperge.realisticseasons.event.ChunkPacketEventProtocolLib;
import me.casperge.realisticseasons.event.ChunkPacketEventProtocolLib1_18;
import me.casperge.realisticseasons.event.ChunkPacketEventProtocolLib1_18_R2;
import me.casperge.realisticseasons.event.ChunkPacketEventProtocolLib1_19_R1;
import me.casperge.realisticseasons.event.ChunkPacketEventProtocolLib1_19_R2;
import me.casperge.realisticseasons.event.ChunkPacketEventProtocolLibAsync;
import me.casperge.realisticseasons.event.ChunkPacketProcessor;
import me.casperge.realisticseasons1_16_R2.CustomBiome1_16_R2;
import me.casperge.realisticseasons1_16_R2.FakeArmorStand_1_16_R2;
import me.casperge.realisticseasons1_16_R2.NmsCode_16_R2;
import me.casperge.realisticseasons1_16_R3.CustomBiome1_16_R3;
import me.casperge.realisticseasons1_16_R3.FakeArmorStand_1_16_R3;
import me.casperge.realisticseasons1_16_R3.NmsCode_16_R3;
import me.casperge.realisticseasons1_17_R1.CustomBiome1_17_R1;
import me.casperge.realisticseasons1_17_R1.FakeArmorStand_1_17_R1;
import me.casperge.realisticseasons1_17_R1.NmsCode_17_R1;
import me.casperge.realisticseasons1_18_R1.CustomBiome1_18_R1;
import me.casperge.realisticseasons1_18_R1.FakeArmorStand_1_18_R1;
import me.casperge.realisticseasons1_18_R1.NmsCode_18_R1;
import me.casperge.realisticseasons1_18_R2.CustomBiome1_18_R2;
import me.casperge.realisticseasons1_18_R2.FakeArmorStand_1_18_R2;
import me.casperge.realisticseasons1_18_R2.NmsCode_18_R2;
import me.casperge.realisticseasons1_19_R1.CustomBiome1_19_R1;
import me.casperge.realisticseasons1_19_R1.FakeArmorStand_1_19_R1;
import me.casperge.realisticseasons1_19_R1.NmsCode_19_R1;
import me.casperge.realisticseasons1_19_R2.CustomBiome1_19_R2;
import me.casperge.realisticseasons1_19_R2.FakeArmorStand_1_19_R2;
import me.casperge.realisticseasons1_19_R2.NmsCode_19_R2;
import me.casperge.realisticseasons1_19_R3.CustomBiome1_19_R3;
import me.casperge.realisticseasons1_19_R3.FakeArmorStand_1_19_R3;
import me.casperge.realisticseasons1_19_R3.NmsCode_19_R3;
import me.casperge.realisticseasons1_19_R3.ProtocolLibUtils1_19_R3;
import me.casperge.realisticseasons1_20_R1.CustomBiome1_20_R1;
import me.casperge.realisticseasons1_20_R1.FakeArmorStand_1_20_R1;
import me.casperge.realisticseasons1_20_R1.NmsCode_20_R1;
import me.casperge.realisticseasons1_20_R1.ProtocolLibUtils1_20_R1;
import me.casperge.realisticseasons1_20_R2.CustomBiome1_20_R2;
import me.casperge.realisticseasons1_20_R2.FakeArmorStand_1_20_R2;
import me.casperge.realisticseasons1_20_R2.NmsCode_20_R2;
import me.casperge.realisticseasons1_20_R2.ProtocolLibUtils1_20_R2;
import me.casperge.realisticseasons1_20_R3.CustomBiome1_20_R3;
import me.casperge.realisticseasons1_20_R3.FakeArmorStand_1_20_R3;
import me.casperge.realisticseasons1_20_R3.NmsCode_20_R3;
import me.casperge.realisticseasons1_20_R3.ProtocolLibUtils1_20_R3;
import me.casperge.realisticseasons1_20_R4.CustomBiome1_20_R4;
import me.casperge.realisticseasons1_20_R4.FakeArmorStand_1_20_R4;
import me.casperge.realisticseasons1_20_R4.NmsCode_20_R4;
import me.casperge.realisticseasons1_20_R4.ProtocolLibUtils1_20_R4;
import me.casperge.realisticseasons1_21_R1.CustomBiome1_21_R1;
import me.casperge.realisticseasons1_21_R1.FakeArmorStand_1_21_R1;
import me.casperge.realisticseasons1_21_R1.NmsCode_21_R1;
import me.casperge.realisticseasons1_21_R1.ProtocolLibUtils1_21_R1;
import me.casperge.realisticseasons1_21_R2.CustomBiome1_21_R2;
import me.casperge.realisticseasons1_21_R2.FakeArmorStand_1_21_R2;
import me.casperge.realisticseasons1_21_R2.NmsCode_21_R2;
import me.casperge.realisticseasons1_21_R2.ProtocolLibUtils1_21_R2;
import me.casperge.realisticseasons1_21_R3.CustomBiome1_21_R3;
import me.casperge.realisticseasons1_21_R3.FakeArmorStand_1_21_R3;
import me.casperge.realisticseasons1_21_R3.NmsCode_21_R3;
import me.casperge.realisticseasons1_21_R3.ProtocolLibUtils1_21_R3;
import me.casperge.realisticseasons1_21_R4.CustomBiome1_21_R4;
import me.casperge.realisticseasons1_21_R4.FakeArmorStand_1_21_R4;
import me.casperge.realisticseasons1_21_R4.NmsCode_21_R4;
import me.casperge.realisticseasons1_21_R4.ProtocolLibUtils1_21_R4;
import me.casperge.realisticseasons1_21_R5.CustomBiome1_21_R5;
import me.casperge.realisticseasons1_21_R5.FakeArmorStand_1_21_R5;
import me.casperge.realisticseasons1_21_R5.NmsCode_21_R5;
import me.casperge.realisticseasons1_21_R5.ProtocolLibUtils1_21_R5;
import me.casperge.realisticseasons1_21_R6.CustomBiome1_21_R6;
import me.casperge.realisticseasons1_21_R6.FakeArmorStand_1_21_R6;
import me.casperge.realisticseasons1_21_R6.GameRuleGetter1_21_R6;
import me.casperge.realisticseasons1_21_R6.NmsCode_21_R6;
import me.casperge.realisticseasons1_21_R6.ProtocolLibUtils1_21_R6;
import me.casperge.realisticseasons1_21_R7.CustomBiome1_21_R7;
import me.casperge.realisticseasons1_21_R7.FakeArmorStand_1_21_R7;
import me.casperge.realisticseasons1_21_R7.GameRuleGetter1_21_R7;
import me.casperge.realisticseasons1_21_R7.NmsCode_21_R7;
import me.casperge.realisticseasons1_21_R7.ProtocolLibUtils1_21_R7;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Version {
   public static String version;

   public static GameRuleGetter setupGameRuleGetter() {
      return (GameRuleGetter)(is_1_21_11_or_up() ? new GameRuleGetter1_21_R7() : new GameRuleGetter1_21_R6());
   }

   public static NmsCode setupBiomeRegister() {
      if (version.equals("v1_16_R3")) {
         return new NmsCode_16_R3();
      } else if (version.equals("v1_16_R2")) {
         return new NmsCode_16_R2();
      } else if (version.equals("v1_17_R1")) {
         return new NmsCode_17_R1();
      } else if (version.equals("v1_18_R1")) {
         return new NmsCode_18_R1();
      } else if (version.equals("v1_18_R2")) {
         return new NmsCode_18_R2();
      } else if (version.equals("v1_19_R1")) {
         return new NmsCode_19_R1();
      } else if (version.equals("v1_19_R2")) {
         return new NmsCode_19_R2();
      } else if (version.equals("v1_19_R3")) {
         return new NmsCode_19_R3();
      } else if (version.equals("v1_20_R1")) {
         return new NmsCode_20_R1();
      } else if (version.equals("v1_20_R2")) {
         return new NmsCode_20_R2();
      } else if (version.equals("v1_20_R3")) {
         return new NmsCode_20_R3();
      } else if (version.equals("v1_20_R4")) {
         return new NmsCode_20_R4();
      } else if (version.equals("v1_21_R1")) {
         return new NmsCode_21_R1();
      } else if (version.equals("v1_21_R2")) {
         return new NmsCode_21_R2();
      } else if (version.equals("v1_21_R3")) {
         return new NmsCode_21_R3();
      } else if (version.equals("v1_21_R4")) {
         return new NmsCode_21_R4();
      } else if (version.equals("v1_21_R5")) {
         return new NmsCode_21_R5();
      } else if (version.equals("v1_21_R6")) {
         return new NmsCode_21_R6();
      } else {
         return version.equals("v1_21_R7") ? new NmsCode_21_R7() : null;
      }
   }

   public static boolean is_1_17_or_up() {
      return version.equals("v1_17_R1") || version.equals("v1_18_R1") || version.equals("v1_18_R2") || version.equals("v1_19_R1") || version.equals("v1_19_R2") || version.equals("v1_19_R3") || version.equals("v1_20_R1") || version.equals("v1_20_R2") || version.equals("v1_20_R3") || version.equals("v1_20_R4") || version.equals("v1_21_R1") || version.equals("v1_21_R2") || version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_19_or_up() {
      return version.equals("v1_19_R1") || version.equals("v1_19_R2") || version.equals("v1_19_R3") || version.equals("v1_20_R1") || version.equals("v1_20_R2") || version.equals("v1_20_R3") || version.equals("v1_20_R4") || version.equals("v1_21_R1") || version.equals("v1_21_R2") || version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_19_3_or_up() {
      return version.equals("v1_19_R2") || version.equals("v1_19_R3") || version.equals("v1_20_R1") || version.equals("v1_20_R2") || version.equals("v1_20_R3") || version.equals("v1_20_R4") || version.equals("v1_21_R1") || version.equals("v1_21_R2") || version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_19_4_or_up() {
      return version.equals("v1_19_R3") || version.equals("v1_20_R1") || version.equals("v1_20_R2") || version.equals("v1_20_R3") || version.equals("v1_20_R4") || version.equals("v1_21_R1") || version.equals("v1_21_R2") || version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_20_or_up() {
      return version.equals("v1_20_R1") || version.equals("v1_20_R2") || version.equals("v1_20_R3") || version.equals("v1_20_R4") || version.equals("v1_21_R1") || version.equals("v1_21_R2") || version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_21_2_or_up() {
      return version.equals("v1_21_R2") || version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_21_4_or_up() {
      return version.equals("v1_21_R3") || version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_21_5_or_up() {
      return version.equals("v1_21_R4") || version.equals("v1_21_R5") || version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_21_9_or_up() {
      return version.equals("v1_21_R6") || version.equals("v1_21_R7");
   }

   public static boolean is_1_21_11_or_up() {
      return version.equals("v1_21_R7");
   }

   public static void setupChunkPacketEvent(RealisticSeasons var0) {
      if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
         if (version.equals("v1_18_R1")) {
            new ChunkPacketEventProtocolLib1_18(var0);
         } else if (version.equals("v1_18_R2")) {
            new ChunkPacketEventProtocolLib1_18_R2(var0);
         } else if (version.equals("v1_19_R1")) {
            new ChunkPacketEventProtocolLib1_19_R1(var0);
         } else if (version.equals("v1_19_R2")) {
            new ChunkPacketEventProtocolLib1_19_R2(var0);
         } else {
            ChunkPacketProcessor var2;
            Thread var3;
            if (version.equals("v1_19_R3")) {
               ProtocolLibUtils1_19_R3 var1 = new ProtocolLibUtils1_19_R3();
               var2 = new ChunkPacketProcessor(var1);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_20_R1")) {
               ProtocolLibUtils1_20_R1 var4 = new ProtocolLibUtils1_20_R1();
               var2 = new ChunkPacketProcessor(var4);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_20_R2")) {
               ProtocolLibUtils1_20_R2 var5 = new ProtocolLibUtils1_20_R2();
               var2 = new ChunkPacketProcessor(var5);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_20_R3")) {
               ProtocolLibUtils1_20_R3 var6 = new ProtocolLibUtils1_20_R3();
               var2 = new ChunkPacketProcessor(var6);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_20_R4")) {
               ProtocolLibUtils1_20_R4 var7 = new ProtocolLibUtils1_20_R4();
               var2 = new ChunkPacketProcessor(var7);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R1")) {
               ProtocolLibUtils1_21_R1 var8 = new ProtocolLibUtils1_21_R1();
               var2 = new ChunkPacketProcessor(var8);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R2")) {
               ProtocolLibUtils1_21_R2 var9 = new ProtocolLibUtils1_21_R2();
               var2 = new ChunkPacketProcessor(var9);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R3")) {
               ProtocolLibUtils1_21_R3 var10 = new ProtocolLibUtils1_21_R3();
               var2 = new ChunkPacketProcessor(var10);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R4")) {
               ProtocolLibUtils1_21_R4 var11 = new ProtocolLibUtils1_21_R4();
               var2 = new ChunkPacketProcessor(var11);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R5")) {
               ProtocolLibUtils1_21_R5 var12 = new ProtocolLibUtils1_21_R5();
               var2 = new ChunkPacketProcessor(var12);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R6")) {
               ProtocolLibUtils1_21_R6 var13 = new ProtocolLibUtils1_21_R6();
               var2 = new ChunkPacketProcessor(var13);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else if (version.equals("v1_21_R7")) {
               ProtocolLibUtils1_21_R7 var14 = new ProtocolLibUtils1_21_R7();
               var2 = new ChunkPacketProcessor(var14);
               var3 = new Thread(var2);
               var3.setDaemon(true);
               var3.setName("RealisticSeasons-ChunkPacketProcessor");
               var3.start();
               new ChunkPacketEventProtocolLibAsync(var0, var2);
            } else {
               new ChunkPacketEventProtocolLib(var0);
            }
         }

      } else {
         Bukkit.getLogger().info("ProtocolLib is required to run RealisticSeasons. Please install it and restart the server.");
         Bukkit.getPluginManager().disablePlugin(var0);
      }
   }

   public static CustomBiome createCustomBiome(String var0, String var1, String var2) {
      if (version.equals("v1_16_R3")) {
         return new CustomBiome1_16_R3(var0, var1, var2);
      } else if (version.equals("v1_16_R2")) {
         return new CustomBiome1_16_R2(var0, var1, var2);
      } else if (version.equals("v1_17_R1")) {
         return new CustomBiome1_17_R1(var0, var1, var2);
      } else if (version.equals("v1_18_R1")) {
         return new CustomBiome1_18_R1(var0, var1, var2);
      } else if (version.equals("v1_18_R2")) {
         return new CustomBiome1_18_R2(var0, var1, var2);
      } else if (version.equals("v1_19_R1")) {
         return new CustomBiome1_19_R1(var0, var1, var2);
      } else if (version.equals("v1_19_R2")) {
         return new CustomBiome1_19_R2(var0, var1, var2);
      } else if (version.equals("v1_19_R3")) {
         return new CustomBiome1_19_R3(var0, var1, var2);
      } else if (version.equals("v1_20_R1")) {
         return new CustomBiome1_20_R1(var0, var1, var2);
      } else if (version.equals("v1_20_R2")) {
         return new CustomBiome1_20_R2(var0, var1, var2);
      } else if (version.equals("v1_20_R3")) {
         return new CustomBiome1_20_R3(var0, var1, var2);
      } else if (version.equals("v1_20_R4")) {
         return new CustomBiome1_20_R4(var0, var1, var2);
      } else if (version.equals("v1_21_R1")) {
         return new CustomBiome1_21_R1(var0, var1, var2);
      } else if (version.equals("v1_21_R2")) {
         return new CustomBiome1_21_R2(var0, var1, var2);
      } else if (version.equals("v1_21_R3")) {
         return new CustomBiome1_21_R3(var0, var1, var2);
      } else if (version.equals("v1_21_R4")) {
         return new CustomBiome1_21_R4(var0, var1, var2);
      } else if (version.equals("v1_21_R5")) {
         return new CustomBiome1_21_R5(var0, var1, var2);
      } else if (version.equals("v1_21_R6")) {
         return new CustomBiome1_21_R6(var0, var1, var2);
      } else {
         return version.equals("v1_21_R7") ? new CustomBiome1_21_R7(var0, var1, var2) : null;
      }
   }

   public static FakeArmorStand createFakeArmorStand(World var0, double var1, double var3, double var5, double var7, boolean var9, boolean var10, boolean var11, boolean var12) {
      if (version.equals("v1_16_R3")) {
         return new FakeArmorStand_1_16_R3(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_16_R2")) {
         return new FakeArmorStand_1_16_R2(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_17_R1")) {
         return new FakeArmorStand_1_17_R1(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_18_R1")) {
         return new FakeArmorStand_1_18_R1(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_18_R2")) {
         return new FakeArmorStand_1_18_R2(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_19_R1")) {
         return new FakeArmorStand_1_19_R1(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_19_R2")) {
         return new FakeArmorStand_1_19_R2(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_19_R3")) {
         return new FakeArmorStand_1_19_R3(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_20_R1")) {
         return new FakeArmorStand_1_20_R1(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_20_R2")) {
         return new FakeArmorStand_1_20_R2(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_20_R3")) {
         return new FakeArmorStand_1_20_R3(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_20_R4")) {
         return new FakeArmorStand_1_20_R4(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_21_R1")) {
         return new FakeArmorStand_1_21_R1(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_21_R2")) {
         return new FakeArmorStand_1_21_R2(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_21_R3")) {
         return new FakeArmorStand_1_21_R3(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_21_R4")) {
         return new FakeArmorStand_1_21_R4(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_21_R5")) {
         return new FakeArmorStand_1_21_R5(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else if (version.equals("v1_21_R6")) {
         return new FakeArmorStand_1_21_R6(var0, var1, var3, var5, var7, var9, var10, var11, var12);
      } else {
         return version.equals("v1_21_R7") ? new FakeArmorStand_1_21_R7(var0, var1, var3, var5, var7, var9, var10, var11, var12) : null;
      }
   }

   public static int getSnowID() {
      if (is_1_21_9_or_up()) {
         return 6718;
      } else if (is_1_21_5_or_up()) {
         return 5950;
      } else if (is_1_21_4_or_up()) {
         return 5941;
      } else if (is_1_21_2_or_up()) {
         return 5938;
      } else if (is_1_20_or_up()) {
         return 5772;
      } else if (is_1_19_4_or_up()) {
         return 5768;
      } else if (is_1_19_3_or_up()) {
         return 5606;
      } else if (is_1_19_or_up()) {
         return 4230;
      } else {
         return is_1_17_or_up() ? 3990 : 3921;
      }
   }

   public static int getIceID() {
      if (is_1_21_9_or_up()) {
         return 6726;
      } else if (is_1_21_5_or_up()) {
         return 5958;
      } else if (is_1_21_4_or_up()) {
         return 5949;
      } else if (is_1_21_2_or_up()) {
         return 5946;
      } else if (is_1_20_or_up()) {
         return 5780;
      } else if (is_1_19_4_or_up()) {
         return 5776;
      } else if (is_1_19_3_or_up()) {
         return 5614;
      } else if (is_1_19_or_up()) {
         return 4238;
      } else {
         return is_1_17_or_up() ? 3998 : 3929;
      }
   }

   public static int getWaterID() {
      if (is_1_21_2_or_up()) {
         return 86;
      } else if (is_1_19_4_or_up()) {
         return 80;
      } else if (is_1_19_3_or_up()) {
         return 77;
      } else {
         return is_1_19_or_up() ? 75 : 34;
      }
   }

   public static String craftBukkitVersionFromMinecraftRelease(String var0) {
      if (!var0.equals("1.20.5") && !var0.equals("1.20.6")) {
         if (!var0.equals("1.20.4") && !var0.equals("1.20.3")) {
            if (var0.equals("1.20.2")) {
               return "v1_20_R2";
            } else if (!var0.equals("1.20.1") && !var0.equals("1.20")) {
               if (var0.equals("1.19.4")) {
                  return "v1_19_R3";
               } else if (var0.equals("1.19.3")) {
                  return "v1_19_R2";
               } else if (!var0.equals("1.19.2") && !var0.equals("1.19.1") && !var0.equals("1.19")) {
                  if (var0.equals("1.18.2")) {
                     return "v1_18_R2";
                  } else if (!var0.equals("1.18.1") && !var0.equals("1.18")) {
                     if (!var0.equals("1.17.1") && !var0.equals("1.17")) {
                        if (!var0.equals("1.16.5") && !var0.equals("1.16.4")) {
                           if (!var0.equals("1.16.3") && !var0.equals("1.16.2")) {
                              if (!var0.equals("1.21") && !var0.equals("1.21.1")) {
                                 if (!var0.equals("1.21.2") && !var0.equals("1.21.3")) {
                                    if (var0.equals("1.21.4")) {
                                       return "v1_21_R3";
                                    } else if (var0.equals("1.21.5")) {
                                       return "v1_21_R4";
                                    } else if (!var0.equals("1.21.6") && !var0.equals("1.21.7") && !var0.equals("1.21.8")) {
                                       if (!var0.equals("1.21.9") && !var0.equals("1.21.10")) {
                                          if (var0.equals("1.21.11")) {
                                             return "v1_21_R7";
                                          } else {
                                             Bukkit.getLogger().severe("[RealisticSeasons] Could not load unknown minecraft version " + var0);
                                             return null;
                                          }
                                       } else {
                                          return "v1_21_R6";
                                       }
                                    } else {
                                       return "v1_21_R5";
                                    }
                                 } else {
                                    return "v1_21_R2";
                                 }
                              } else {
                                 return "v1_21_R1";
                              }
                           } else {
                              return "v1_16_R2";
                           }
                        } else {
                           return "v1_16_R3";
                        }
                     } else {
                        return "v1_17_R1";
                     }
                  } else {
                     return "v1_18_R1";
                  }
               } else {
                  return "v1_19_R1";
               }
            } else {
               return "v1_20_R1";
            }
         } else {
            return "v1_20_R3";
         }
      } else {
         return "v1_20_R4";
      }
   }
}
