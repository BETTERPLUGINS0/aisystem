package me.PM2.infinitevehicles.xseries.reflection.minecraft;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.DynamicClassHandle;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public final class NMSExtras {
   public static final Class<?> EntityLiving;
   private static final MethodHandle GET_ENTITY_HANDLE;
   public static final MethodHandle EXP_PACKET;
   public static final MethodHandle ENTITY_PACKET;
   public static final MethodHandle WORLD_HANDLE;
   public static final MethodHandle ENTITY_HANDLE;
   public static final MethodHandle LIGHTNING_ENTITY;
   public static final MethodHandle VEC3D;
   public static final MethodHandle GET_DATA_WATCHER;
   public static final MethodHandle DATA_WATCHER_GET_ITEM;
   public static final MethodHandle DATA_WATCHER_SET_ITEM;
   public static final MethodHandle PACKET_PLAY_OUT_OPEN_SIGN_EDITOR;
   public static final MethodHandle PACKET_PLAY_OUT_BLOCK_CHANGE;
   public static final MethodHandle ANIMATION_PACKET;
   public static final MethodHandle ANIMATION_TYPE;
   public static final MethodHandle ANIMATION_ENTITY_ID;
   public static final MethodHandle PLAY_OUT_MULTI_BLOCK_CHANGE_PACKET;
   public static final MethodHandle MULTI_BLOCK_CHANGE_INFO;
   public static final MethodHandle CHUNK_WRAPPER_SET;
   public static final MethodHandle CHUNK_WRAPPER;
   public static final MethodHandle SHORTS_OR_INFO;
   public static final MethodHandle SET_BlockState;
   public static final MethodHandle BLOCK_POSITION;
   public static final MethodHandle PLAY_BLOCK_ACTION;
   public static final MethodHandle GET_BUKKIT_ENTITY;
   public static final MethodHandle GET_BLOCK_TYPE;
   public static final MethodHandle GET_BLOCK;
   public static final MethodHandle GET_IBlockState;
   public static final MethodHandle SANITIZE_LINES;
   public static final MethodHandle TILE_ENTITY_SIGN;
   public static final MethodHandle TILE_ENTITY_SIGN__GET_UPDATE_PACKET;
   public static final MethodHandle TILE_ENTITY_SIGN__SET_LINE;
   public static final MethodHandle SIGN_TEXT;
   public static final Class<?> BlockState;
   public static final Class<?> MULTI_BLOCK_CHANGE_INFO_CLASS;

   private NMSExtras() {
   }

   public static void setExp(Player var0, float var1, int var2, int var3) {
      try {
         Object var4 = EXP_PACKET.invoke(var1, var2, var3);
         MinecraftConnection.sendPacket(var0, var4);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public static void lightning(Player var0, Location var1, boolean var2) {
      lightning((Collection)Collections.singletonList(var0), var1, var2);
   }

   public static void lightning(Collection<Player> var0, Location var1, boolean var2) {
      try {
         Object var3 = WORLD_HANDLE.invoke(var1.getWorld());
         Object var5;
         if (!XReflection.supports(16)) {
            Object var4 = LIGHTNING_ENTITY.invoke(var3, var1.getX(), var1.getY(), var1.getZ(), false, false);
            var5 = ENTITY_PACKET.invoke(var4);
            Iterator var6 = var0.iterator();

            while(var6.hasNext()) {
               Player var7 = (Player)var6.next();
               MinecraftConnection.sendPacket(var7, var5);
            }
         } else {
            Class var14 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "EntityType").map(MinecraftMapping.SPIGOT, "EntityTypes").unreflect();
            var5 = var14.getField(XReflection.supports(17) ? "U" : "LIGHTNING_BOLT").get(var14);
            Object var15 = LIGHTNING_ENTITY.invoke(var5, var3);
            Object var16 = var15.getClass().getMethod("getId").invoke(var15);
            Object var8 = var15.getClass().getMethod("getUniqueID").invoke(var15);
            Object var9 = VEC3D.invoke(0.0D, 0.0D, 0.0D);
            Object var10 = ENTITY_PACKET.invoke(var16, var8, var1.getX(), var1.getY(), var1.getZ(), 0.0F, 0.0F, var5, 0, var9);
            Iterator var11 = var0.iterator();

            while(var11.hasNext()) {
               Player var12 = (Player)var11.next();
               MinecraftConnection.sendPacket(var12, var10);
            }
         }
      } catch (Throwable var13) {
         var13.printStackTrace();
      }

   }

   public static Object getData(Object var0, Object var1) {
      try {
         return DATA_WATCHER_GET_ITEM.invoke(var0, var1);
      } catch (Throwable var3) {
         throw new IllegalArgumentException("Failed to create data watcher", var3);
      }
   }

   @Nullable
   public static Object getEntityHandle(Entity var0) {
      Objects.requireNonNull(var0, "Cannot get handle of null entity");

      try {
         return GET_ENTITY_HANDLE.invoke(var0);
      } catch (Throwable var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object getDataWatcher(Object var0) {
      try {
         return GET_DATA_WATCHER.invoke(var0);
      } catch (Throwable var2) {
         throw new IllegalArgumentException("Failed to get data watcher", var2);
      }
   }

   public static Object setData(Object var0, Object var1, Object var2) {
      try {
         return DATA_WATCHER_SET_ITEM.invoke(var0, var1, var2);
      } catch (Throwable var4) {
         throw new IllegalArgumentException("Failed to set data watcher item", var4);
      }
   }

   public static Object getStaticFieldIgnored(Class<?> var0, String var1) {
      return getStaticField(var0, var1, true);
   }

   public static Object getStaticField(Class<?> var0, String var1, boolean var2) {
      try {
         Field var3 = var0.getDeclaredField(var1);
         var3.setAccessible(true);
         return var3.get((Object)null);
      } catch (Throwable var4) {
         if (!var2) {
            throw new IllegalArgumentException("Failed to get static field of " + var0 + " named " + var1, var4);
         } else {
            return null;
         }
      }
   }

   public static void spinEntity(LivingEntity var0, boolean var1) {
      if (!NMSExtras.EntityPose.SPIN_ATTACK.isSupported()) {
         throw new UnsupportedOperationException("Spin attacks are not supported in " + XReflection.getVersionInformation());
      } else {
         setLivingEntityFlag(var0, NMSExtras.LivingEntityFlags.SPIN_ATTACK.getBit(), var1);
      }
   }

   public static void setLivingEntityFlag(Entity var0, int var1, boolean var2) {
      Object var3 = getEntityHandle(var0);
      Object var4 = getDataWatcher(var3);
      Object var5 = NMSExtras.DataWatcherItemType.DATA_LIVING_ENTITY_FLAGS.getId();
      byte var6 = (Byte)getData(var4, var5);
      int var7;
      if (var2) {
         var7 = var6 | var1;
      } else {
         var7 = var6 & ~var1;
      }

      setData(var4, var5, (byte)var7);
   }

   public static boolean hasLivingEntityFlag(Entity var0, int var1) {
      Object var2 = getEntityHandle(var0);
      Object var3 = getDataWatcher(var2);
      byte var4 = (Byte)getData(var3, NMSExtras.DataWatcherItemType.DATA_LIVING_ENTITY_FLAGS.getId());
      return (var4 & var1) != 0;
   }

   public boolean isAutoSpinAttack(LivingEntity var1) {
      return hasLivingEntityFlag(var1, NMSExtras.LivingEntityFlags.SPIN_ATTACK.getBit());
   }

   public static void animation(Collection<? extends Player> var0, LivingEntity var1, NMSExtras.Animation var2) {
      try {
         Object var3;
         if (XReflection.supports(17)) {
            var3 = ANIMATION_PACKET.invoke(ENTITY_HANDLE.invoke(var1), var2.ordinal());
         } else {
            var3 = ANIMATION_PACKET.invoke();
            ANIMATION_TYPE.invoke(var3, var2.ordinal());
            ANIMATION_ENTITY_ID.invoke(var3, var1.getEntityId());
         }

         Iterator var4 = var0.iterator();

         while(var4.hasNext()) {
            Player var5 = (Player)var4.next();
            MinecraftConnection.sendPacket(var5, var3);
         }
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

   }

   public static void chest(Block var0, boolean var1) {
      Location var2 = var0.getLocation();

      try {
         Object var3 = WORLD_HANDLE.invoke(var2.getWorld());
         Object var4 = XReflection.v(19, (Callable)(() -> {
            try {
               return BLOCK_POSITION.invoke(var2.getBlockX(), var2.getBlockY(), var2.getBlockZ());
            } catch (Throwable var2x) {
               throw new IllegalArgumentException("Failed to set block position", var2x);
            }
         })).orElse(() -> {
            try {
               return BLOCK_POSITION.invoke(var2.getX(), var2.getY(), var2.getZ());
            } catch (Throwable var2x) {
               throw new IllegalArgumentException("Failed to set block position", var2x);
            }
         });
         Object var5 = GET_BLOCK.invoke(GET_BLOCK_TYPE.invoke(var3, var4));
         PLAY_BLOCK_ACTION.invoke(var3, var4, var5, 1, var1 ? 1 : 0);
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

   }

   /** @deprecated */
   @Deprecated
   protected static void sendBlockChange(Player var0, Chunk var1, Map<NMSExtras.WorldlessBlockWrapper, Object> var2) {
      try {
         Object var3 = PLAY_OUT_MULTI_BLOCK_CHANGE_PACKET.invoke();
         Object var4;
         Object var5;
         int var11;
         if (XReflection.supports(16)) {
            var4 = CHUNK_WRAPPER.invoke(var1.getX(), var1.getZ());
            CHUNK_WRAPPER_SET.invoke(var4);
            var5 = Array.newInstance(BlockState, var2.size());
            Object var15 = Array.newInstance(Short.TYPE, var2.size());
            int var16 = 0;
            Iterator var17 = var2.entrySet().iterator();

            while(true) {
               if (!var17.hasNext()) {
                  SHORTS_OR_INFO.invoke(var3, var15);
                  SET_BlockState.invoke(var3, var5);
                  break;
               }

               Entry var18 = (Entry)var17.next();
               Block var19 = ((NMSExtras.WorldlessBlockWrapper)var18.getKey()).block;
               var11 = var19.getX() & 15;
               int var12 = var19.getY() & 15;
               int var13 = var19.getZ() & 15;
               ++var16;
            }
         } else {
            var4 = CHUNK_WRAPPER.invoke(var1.getX(), var1.getZ());
            CHUNK_WRAPPER_SET.invoke(var4);
            var5 = Array.newInstance(MULTI_BLOCK_CHANGE_INFO_CLASS, var2.size());
            int var6 = 0;

            for(Iterator var7 = var2.entrySet().iterator(); var7.hasNext(); ++var6) {
               Entry var8 = (Entry)var7.next();
               Block var9 = ((NMSExtras.WorldlessBlockWrapper)var8.getKey()).block;
               int var10 = var9.getX() & 15;
               var11 = var9.getZ() & 15;
            }

            SHORTS_OR_INFO.invoke(var3, var5);
         }

         MinecraftConnection.sendPacket(var0, var3);
      } catch (Throwable var14) {
         var14.printStackTrace();
      }

   }

   public static void openSign(Player var0, DyeColor var1, String[] var2, boolean var3) {
      try {
         Location var4 = var0.getLocation();
         Object var5 = BLOCK_POSITION.invoke(var4.getBlockX(), 1, var4.getBlockY());
         Object var6 = GET_IBlockState.invoke(Material.OAK_SIGN, (byte)0);
         Object var7 = PACKET_PLAY_OUT_BLOCK_CHANGE.invoke(var5, var6);
         Object var8 = SANITIZE_LINES.invoke((Object[])var2);
         Object var9 = TILE_ENTITY_SIGN.invoke(var5, var6);
         Object var11;
         if (!XReflection.supports(20)) {
            for(int var19 = 0; var19 < var2.length; ++var19) {
               var11 = Array.get(var8, var19);
               TILE_ENTITY_SIGN__SET_LINE.invoke(var9, var19, var11, var11);
            }
         } else {
            Class var10 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.item").map(MinecraftMapping.MOJANG, "DyeColor").map(MinecraftMapping.SPIGOT, "EnumColor").unreflect();
            var11 = null;
            Field[] var12 = var10.getFields();
            int var13 = var12.length;
            int var14 = 0;

            while(true) {
               if (var14 < var13) {
                  Field var15 = var12[var14];
                  Object var16 = var15.get((Object)null);
                  String var17 = (String)var10.getDeclaredMethod("b").invoke(var16);
                  if (!var1.name().equalsIgnoreCase(var17)) {
                     ++var14;
                     continue;
                  }

                  var11 = var16;
               }

               Object var21 = SIGN_TEXT.invoke(var8, var8, var11, var3);
               TILE_ENTITY_SIGN__SET_LINE.invoke(var21, true);
               break;
            }
         }

         Object var20 = TILE_ENTITY_SIGN__GET_UPDATE_PACKET.invoke(var9);
         var11 = XReflection.v(20, (Object)PACKET_PLAY_OUT_OPEN_SIGN_EDITOR.invoke(var5, true)).orElse(PACKET_PLAY_OUT_OPEN_SIGN_EDITOR.invoke(var5));
         MinecraftConnection.sendPacket(var0, var7, var20, var11);
      } catch (Throwable var18) {
         var18.printStackTrace();
      }

   }

   static {
      EntityLiving = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "LivingEntity").map(MinecraftMapping.SPIGOT, "EntityLiving").unreflect();
      BlockState = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.state").map(MinecraftMapping.MOJANG, "BlockState").map(MinecraftMapping.SPIGOT, "IBlockData").unreflect();
      MULTI_BLOCK_CHANGE_INFO_CLASS = null;
      Lookup var0 = MethodHandles.lookup();
      MethodHandle var1 = null;
      MethodHandle var2 = null;
      MethodHandle var3 = null;
      MethodHandle var4 = null;
      MethodHandle var5 = null;
      MethodHandle var6 = null;
      MethodHandle var7 = null;
      MethodHandle var8 = null;
      MethodHandle var9 = null;
      MethodHandle var10 = null;
      MethodHandle var11 = null;
      MethodHandle var12 = null;
      MethodHandle var13 = null;
      MethodHandle var14 = null;
      MethodHandle var15 = null;
      MethodHandle var16 = null;
      MethodHandle var17 = null;
      MethodHandle var18 = null;
      MethodHandle var19 = null;
      MethodHandle var20 = null;
      MethodHandle var21 = null;
      MethodHandle var22 = null;
      Object var23 = null;
      Object var24 = null;
      MethodHandle var25 = null;
      Object var26 = null;
      Object var27 = null;
      Object var28 = null;
      MethodHandle var29 = null;
      MethodHandle var30 = null;
      MethodHandle var31 = null;
      MethodHandle var32 = null;

      try {
         Class var33 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "entity").named("CraftEntity").unreflect();
         Class var34 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "EntityType").map(MinecraftMapping.SPIGOT, "EntityTypes").unreflect();
         Class var35 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").named("Entity").unreflect();
         Class var36 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "entity").named("CraftEntity").unreflect();
         Class var37 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.phys").map(MinecraftMapping.MOJANG, "Vec3").map(MinecraftMapping.SPIGOT, "Vec3D").unreflect();
         Class var38 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level").map(MinecraftMapping.MOJANG, "Level").map(MinecraftMapping.SPIGOT, "World").unreflect();
         Class var39 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundOpenSignEditorPacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutOpenSignEditor").unreflect();
         Class var40 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundBlockUpdatePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutBlockChange").unreflect();
         Class var41 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "util").named("CraftMagicNumbers").unreflect();
         Class var42 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "block").named("CraftSign").unreflect();
         Class var43 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").map(MinecraftMapping.MOJANG, "Component").map(MinecraftMapping.SPIGOT, "IChatBaseComponent").unreflect();
         Class var44 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.entity").map(MinecraftMapping.MOJANG, "SignBlockEntity").map(MinecraftMapping.SPIGOT, "TileEntitySign").unreflect();
         Class var45 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundBlockEntityDataPacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutTileEntityData").unreflect();
         Class var46 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.syncher").map(MinecraftMapping.MOJANG, "SynchedEntityData").map(MinecraftMapping.SPIGOT, "DataWatcher").unreflect();
         Class var47 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.syncher").map(MinecraftMapping.MOJANG, "SynchedEntityData$DataItem").map(MinecraftMapping.SPIGOT, "DataWatcher$Item").unreflect();
         Class var48 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.syncher").map(MinecraftMapping.MOJANG, "EntityDataAccessor").map(MinecraftMapping.SPIGOT, "DataWatcherObject").unreflect();
         var32 = var0.findVirtual(var33, "getHandle", MethodType.methodType(var35));
         var29 = (MethodHandle)XReflection.of(var35).method().returns(var46).map(MinecraftMapping.MOJANG, "getEntityData").map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(21, 9, (Object)"aC").v(21, 6, (Object)"au").v(21, 5, (Object)"ar").v(21, 3, (Object)"au").v(21, "ar").v(20, 5, (Object)"ap").v(20, 4, (Object)"an").v(20, 2, (Object)"al").v(19, "aj").v(18, "ai").orElse((Object)"getDataWatcher")).unreflect();
         var30 = (MethodHandle)XReflection.of(var46).method().returns(Object.class).parameters(var48).map(MinecraftMapping.MOJANG, "get").map(MinecraftMapping.SPIGOT, (String)XReflection.v(20, 5, (Object)"a").v(20, "b").v(18, "a").orElse((Object)"get")).unreflect();
         var31 = (MethodHandle)XReflection.of(var46).method().returns(Void.TYPE).parameters(var48, Object.class).map(MinecraftMapping.MOJANG, "set").map(MinecraftMapping.SPIGOT, (String)XReflection.v(20, 5, (Object)"a").v(18, "b").orElse((Object)"set")).unreflect();
         var12 = var0.findVirtual(var35, "getBukkitEntity", MethodType.methodType(var36));
         var4 = var0.findVirtual(var36, "getHandle", MethodType.methodType(var35));
         var1 = var0.findConstructor((Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundSetExperiencePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutExperience").unreflect(), MethodType.methodType(Void.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE));
         if (!XReflection.supports(16)) {
            var2 = (MethodHandle)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutSpawnEntityWeather").constructor().parameters(var35).unreflect();
         } else {
            var6 = var0.findConstructor(var37, MethodType.methodType(Void.TYPE, Double.TYPE, Double.TYPE, Double.TYPE));
            ArrayList var49 = new ArrayList(Arrays.asList(Integer.TYPE, UUID.class, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, var34, Integer.TYPE, var37));
            if (XReflection.supports(19)) {
               var49.add(Double.TYPE);
            }

            var2 = var0.findConstructor((Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundAddEntityPacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutSpawnEntity").unreflect(), MethodType.methodType(Void.TYPE, var49));
         }

         var3 = var0.findVirtual((Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.CB).named("CraftWorld").unreflect(), "getHandle", MethodType.methodType((Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerLevel").map(MinecraftMapping.SPIGOT, "WorldServer").unreflect()));
         MinecraftClassHandle var60 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "LightningBolt").map(MinecraftMapping.SPIGOT, "EntityLightning");
         if (!XReflection.supports(16)) {
            var5 = var0.findConstructor((Class)var60.unreflect(), MethodType.methodType(Void.TYPE, var38, Double.TYPE, Double.TYPE, Double.TYPE, Boolean.TYPE, Boolean.TYPE));
         } else {
            var5 = var0.findConstructor((Class)var60.unreflect(), MethodType.methodType(Void.TYPE, var34, var38));
         }

         Class var50 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundSectionBlocksUpdatePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutMultiBlockChange").unreflect();
         Class var51 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level").map(MinecraftMapping.MOJANG, "ChunkPos").map(MinecraftMapping.SPIGOT, "ChunkCoordIntPair").unreflect();

         try {
            if (!XReflection.supports(16)) {
               var25 = var0.findConstructor(var51, MethodType.methodType(Void.TYPE, Integer.TYPE, Integer.TYPE));
            }
         } catch (IllegalAccessException | NoSuchMethodException var58) {
            var58.printStackTrace();
         }

         Class var52 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundAnimatePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutAnimation").unreflect();
         var9 = var0.findConstructor(var52, XReflection.supports(17) ? MethodType.methodType(Void.TYPE, var35, Integer.TYPE) : MethodType.methodType(Void.TYPE));
         if (!XReflection.supports(17)) {
            Field var53 = var52.getDeclaredField("a");
            var53.setAccessible(true);
            var11 = var0.unreflectSetter(var53);
            var53 = var52.getDeclaredField("b");
            var53.setAccessible(true);
            var10 = var0.unreflectSetter(var53);
         }

         Class var61 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "core").map(MinecraftMapping.MOJANG, "BlockPos").map(MinecraftMapping.SPIGOT, "BlockPosition").unreflect();
         Class var54 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block").named("Block").unreflect();
         var13 = var0.findConstructor(var61, (MethodType)XReflection.v(19, (Object)MethodType.methodType(Void.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)).orElse((Object)MethodType.methodType(Void.TYPE, Double.TYPE, Double.TYPE, Double.TYPE)));
         var15 = (MethodHandle)XReflection.of(var38).method().returns(BlockState).parameters(var61).map(MinecraftMapping.MOJANG, "getBlockState").map(MinecraftMapping.SPIGOT, (String)XReflection.v(18, (Object)"a_").orElse((Object)"getType")).unreflect();
         if (XReflection.supports(21)) {
            var16 = (MethodHandle)((MinecraftClassHandle)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.state").map(MinecraftMapping.MOJANG, "BlockBehaviour").map(MinecraftMapping.SPIGOT, "BlockBase").inner((DynamicClassHandle)XReflection.ofMinecraft().map(MinecraftMapping.MOJANG, "BlockStateBase").map(MinecraftMapping.SPIGOT, "BlockData"))).method().returns(var54).map(MinecraftMapping.MOJANG, "getBlock").map(MinecraftMapping.SPIGOT, "b").unreflect();
         } else {
            var16 = (MethodHandle)XReflection.of(BlockState).method().returns(var54).map(MinecraftMapping.MOJANG, "getBlock").map(MinecraftMapping.SPIGOT, (String)XReflection.v(18, (Object)"b").orElse((Object)"getBlock")).unreflect();
         }

         var14 = (MethodHandle)XReflection.of(var38).method().returns(Void.TYPE).parameters(var61, var54, Integer.TYPE, Integer.TYPE).map(MinecraftMapping.MOJANG, "blockEvent").map(MinecraftMapping.SPIGOT, (String)XReflection.v(18, (Object)"a").orElse((Object)"playBlockAction")).unreflect();
         var7 = var0.findConstructor(var39, (MethodType)XReflection.v(20, (Object)MethodType.methodType(Void.TYPE, var61, Boolean.TYPE)).orElse((Object)MethodType.methodType(Void.TYPE, var61)));
         if (XReflection.supports(17)) {
            var8 = var0.findConstructor(var40, MethodType.methodType(Void.TYPE, var61, BlockState));
            var17 = var0.findStatic(var41, "getBlock", MethodType.methodType(BlockState, Material.class, Byte.TYPE));
            var18 = var0.findStatic(var42, (String)XReflection.v(17, (Object)"sanitizeLines").orElse((Object)"SANITIZE_LINES"), MethodType.methodType(XReflection.toArrayClass(var43), String[].class));
            var19 = var0.findConstructor(var44, MethodType.methodType(Void.TYPE, var61, BlockState));
            var20 = (MethodHandle)XReflection.of(var44).method().returns(var45).map(MinecraftMapping.MOJANG, "getUpdatePacket").map(MinecraftMapping.SPIGOT, (String)XReflection.v(21, 9, (Object)"l").v(21, 6, (Object)"u").v(21, 4, (Object)"s").v(21, 3, (Object)"t").v(20, 5, (Object)"l").v(20, 4, (Object)"m").v(20, "j").v(19, "f").v(18, "c").orElse((Object)"getUpdatePacket")).unreflect();
            if (XReflection.supports(20)) {
               Class var55 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.entity").named("SignText").unreflect();
               if (!XReflection.supports(20, 6)) {
                  var21 = var0.findVirtual(var44, "a", MethodType.methodType(Boolean.TYPE, var55, Boolean.TYPE));
               }

               Class var56 = (Class)XReflection.of(var43).asArray().unreflect();
               Class var57 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.item").map(MinecraftMapping.MOJANG, "DyeColor").map(MinecraftMapping.SPIGOT, "EnumColor").unreflect();
               var22 = var0.findConstructor(var55, MethodType.methodType(Void.TYPE, var56, var56, var57, Boolean.TYPE));
            } else {
               var21 = var0.findVirtual(var44, "a", MethodType.methodType(Void.TYPE, Integer.TYPE, var43, var43));
            }
         }
      } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException var59) {
         var59.printStackTrace();
      }

      GET_ENTITY_HANDLE = var32;
      GET_DATA_WATCHER = var29;
      DATA_WATCHER_GET_ITEM = var30;
      DATA_WATCHER_SET_ITEM = var31;
      EXP_PACKET = var1;
      ENTITY_PACKET = var2;
      WORLD_HANDLE = var3;
      ENTITY_HANDLE = var4;
      LIGHTNING_ENTITY = var5;
      VEC3D = var6;
      PACKET_PLAY_OUT_OPEN_SIGN_EDITOR = var7;
      PACKET_PLAY_OUT_BLOCK_CHANGE = var8;
      ANIMATION_PACKET = var9;
      ANIMATION_TYPE = var10;
      ANIMATION_ENTITY_ID = var11;
      BLOCK_POSITION = var13;
      PLAY_BLOCK_ACTION = var14;
      GET_BLOCK_TYPE = var15;
      GET_BLOCK = var16;
      GET_IBlockState = var17;
      SANITIZE_LINES = var18;
      TILE_ENTITY_SIGN = var19;
      TILE_ENTITY_SIGN__GET_UPDATE_PACKET = var20;
      TILE_ENTITY_SIGN__SET_LINE = var21;
      GET_BUKKIT_ENTITY = var12;
      PLAY_OUT_MULTI_BLOCK_CHANGE_PACKET = (MethodHandle)var23;
      MULTI_BLOCK_CHANGE_INFO = (MethodHandle)var24;
      CHUNK_WRAPPER = var25;
      CHUNK_WRAPPER_SET = (MethodHandle)var26;
      SHORTS_OR_INFO = (MethodHandle)var27;
      SET_BlockState = (MethodHandle)var28;
      SIGN_TEXT = var22;
   }

   public static enum EntityPose {
      STANDING("a"),
      FALL_FLYING("b"),
      SLEEPING("c"),
      SWIMMING("d"),
      SPIN_ATTACK("e"),
      CROUCHING("f"),
      LONG_JUMPING("g"),
      DYING("h"),
      CROAKING("i"),
      USING_TONGUE("j"),
      SITTING("k"),
      ROARING("l"),
      SNIFFING("m"),
      EMERGING("n"),
      DIGGING("o");

      public final Object enumValue;
      private final boolean supported;

      private EntityPose(String param3) {
         boolean var4 = true;
         Object var5 = null;

         try {
            Class var6 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "Pose").map(MinecraftMapping.SPIGOT, "EntityPose").reflect();
            var5 = var6.getDeclaredField((String)XReflection.v(17, (Object)var3).orElse((Object)this.name())).get((Object)null);
         } catch (Throwable var7) {
            var4 = false;
         }

         this.supported = var4;
         this.enumValue = var5;
      }

      public boolean isSupported() {
         return this.supported;
      }

      public Object getEnumValue() {
         return this.enumValue;
      }

      // $FF: synthetic method
      private static NMSExtras.EntityPose[] $values() {
         return new NMSExtras.EntityPose[]{STANDING, FALL_FLYING, SLEEPING, SWIMMING, SPIN_ATTACK, CROUCHING, LONG_JUMPING, DYING, CROAKING, USING_TONGUE, SITTING, ROARING, SNIFFING, EMERGING, DIGGING};
      }
   }

   public static enum LivingEntityFlags {
      SPIN_ATTACK(4);

      private final byte bit;

      private LivingEntityFlags(int param3) {
         this.bit = (byte)var3;
      }

      public byte getBit() {
         return this.bit;
      }

      // $FF: synthetic method
      private static NMSExtras.LivingEntityFlags[] $values() {
         return new NMSExtras.LivingEntityFlags[]{SPIN_ATTACK};
      }
   }

   public static enum DataWatcherItemType {
      DATA_LIVING_ENTITY_FLAGS(NMSExtras.getStaticFieldIgnored(NMSExtras.EntityLiving, "t"));

      private final Object id;
      private final boolean supported;

      private DataWatcherItemType(Object param3) {
         boolean var4 = true;
         Object var5 = null;

         try {
            var5 = var3;
         } catch (Throwable var7) {
            var4 = false;
         }

         this.supported = var4;
         this.id = var5;
      }

      public boolean isSupported() {
         return this.supported;
      }

      public Object getId() {
         return this.id;
      }

      // $FF: synthetic method
      private static NMSExtras.DataWatcherItemType[] $values() {
         return new NMSExtras.DataWatcherItemType[]{DATA_LIVING_ENTITY_FLAGS};
      }
   }

   public static enum Animation {
      SWING_MAIN_ARM,
      HURT,
      LEAVE_BED,
      SWING_OFF_HAND,
      CRITICAL_EFFECT,
      MAGIC_CRITICAL_EFFECT;

      // $FF: synthetic method
      private static NMSExtras.Animation[] $values() {
         return new NMSExtras.Animation[]{SWING_MAIN_ARM, HURT, LEAVE_BED, SWING_OFF_HAND, CRITICAL_EFFECT, MAGIC_CRITICAL_EFFECT};
      }
   }

   public static class WorldlessBlockWrapper {
      public final Block block;

      public WorldlessBlockWrapper(Block var1) {
         this.block = var1;
      }

      public int hashCode() {
         return (this.block.getY() + this.block.getZ() * 31) * 31 + this.block.getX();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Block)) {
            return false;
         } else {
            Block var2 = (Block)var1;
            return this.block.getX() == var2.getX() && this.block.getY() == var2.getY() && this.block.getZ() == var2.getZ();
         }
      }
   }
}
