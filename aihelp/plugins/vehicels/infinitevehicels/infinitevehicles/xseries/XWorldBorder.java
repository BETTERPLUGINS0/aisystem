package me.PM2.infinitevehicles.xseries;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftConnection;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftPackage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class XWorldBorder {
   public static final int ABSOLUTE_MAX_SIZE = 29999984;
   public static final double MAX_SIZE = 5.9999968E7D;
   public static final double MAX_CENTER_COORDINATE = 2.9999984E7D;
   private static final boolean SUPPORTS_NATIVE_WORLDBORDERS;
   protected XWorldBorder.BorderBounds borderBounds;

   public abstract double getDamageBuffer();

   public abstract double getSizeLerpTarget();

   public abstract double getSize();

   public abstract boolean isWithinBorder(Location var1);

   public abstract int getWarningDistance();

   public abstract Duration getWarningTime();

   public abstract Location getCenter();

   public abstract void setFor(Collection<Player> var1, boolean var2);

   public final XWorldBorder.BorderBounds getBorderBounds() {
      return this.borderBounds;
   }

   public abstract XWorldBorder copy();

   public abstract XWorldBorder setDamageBuffer(double var1);

   public abstract XWorldBorder setWarningTime(Duration var1);

   public abstract XWorldBorder setWarningDistance(int var1);

   public XWorldBorder setSize(double var1) {
      return this.setSize(var1, Duration.ZERO);
   }

   public XWorldBorder setSize(double var1, @NotNull Duration var3) {
      Objects.requireNonNull(var3, "Size change duration cannot be null");
      if (var1 > 5.9999968E7D) {
         throw new IllegalArgumentException("Border size is bigger than max border size: " + var1 + " > " + 5.9999968E7D);
      } else {
         return this;
      }
   }

   public XWorldBorder setSizeLerpTarget(double var1) {
      if (var1 > 5.9999968E7D) {
         throw new IllegalArgumentException("Size lerp target size is bigger than max border size: " + var1 + " > " + 5.9999968E7D);
      } else {
         return this;
      }
   }

   public abstract XWorldBorder setCenter(Location var1);

   public XWorldBorder setCenter(double var1, double var3) {
      if (!Double.isNaN(var1) && !Double.isNaN(var3)) {
         return this;
      } else {
         throw new IllegalArgumentException("Invalid coordinates: " + var1 + ", " + var3);
      }
   }

   public XWorldBorder update(Player... var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Player array is null");
      } else {
         return this;
      }
   }

   public String toString() {
      return this.getClass().getSimpleName() + "(size: " + this.getSize() + ", warningDistance: " + this.getWarningDistance() + ", warningTime: " + this.getWarningTime() + ", center: " + this.getCenter() + ", damageBuffer: " + this.getDamageBuffer() + ')';
   }

   public final double getDistanceToBorder(Location var1) {
      if (this.borderBounds == null) {
         return this.getCenter().distanceSquared(var1);
      } else {
         double var2 = var1.getX();
         double var4 = var1.getZ();
         double var6 = var4 - this.borderBounds.minZ;
         double var8 = this.borderBounds.maxZ - var4;
         double var10 = var2 - this.borderBounds.minX;
         double var12 = this.borderBounds.maxX - var2;
         double var14 = Math.min(var10, var12);
         var14 = Math.min(var14, var6);
         return Math.min(var14, var8);
      }
   }

   protected void updateBorderBounds(Location var1) {
      this.borderBounds = new XWorldBorder.BorderBounds(var1.getWorld(), var1.getX(), var1.getZ(), this.getSize());
   }

   public static XWorldBorder create() {
      return (XWorldBorder)(SUPPORTS_NATIVE_WORLDBORDERS ? new XWorldBorder.BukkitWorldBorder(Bukkit.createWorldBorder()) : new XWorldBorder.NMSWorldBorder());
   }

   public static XWorldBorder getOrCreate(Player var0) {
      XWorldBorder var1 = get(var0);
      if (var1 != null) {
         return var1;
      } else {
         var1 = create();
         var1.setFor(Collections.singleton(var0), true);
         if (!SUPPORTS_NATIVE_WORLDBORDERS) {
            XWorldBorder.NMSWorldBorder.WORLD_BORDERS.put(var0.getUniqueId(), var1);
         }

         return var1;
      }
   }

   @Nullable
   public static XWorldBorder get(Player var0) {
      if (SUPPORTS_NATIVE_WORLDBORDERS) {
         WorldBorder var1 = var0.getWorldBorder();
         return var1 == null ? null : new XWorldBorder.BukkitWorldBorder(var1);
      } else {
         return (XWorldBorder)XWorldBorder.NMSWorldBorder.WORLD_BORDERS.get(var0.getUniqueId());
      }
   }

   @Nullable
   public static XWorldBorder remove(Player var0) {
      if (SUPPORTS_NATIVE_WORLDBORDERS) {
         WorldBorder var1 = var0.getWorldBorder();
         if (var1 == null) {
            return null;
         } else {
            var0.setWorldBorder(var0.getWorld().getWorldBorder());
            return new XWorldBorder.BukkitWorldBorder(var1);
         }
      } else {
         return (XWorldBorder)XWorldBorder.NMSWorldBorder.WORLD_BORDERS.remove(var0.getUniqueId());
      }
   }

   public static XWorldBorder from(WorldBorder var0) {
      if (SUPPORTS_NATIVE_WORLDBORDERS) {
         return (new XWorldBorder.BukkitWorldBorder(var0)).copy();
      } else {
         XWorldBorder.NMSWorldBorder var1 = new XWorldBorder.NMSWorldBorder();
         var1.world = var0.getCenter().getWorld();
         var1.centerX = var0.getCenter().getX();
         var1.centerZ = var0.getCenter().getZ();
         var1.size = var0.getSize();
         var1.sizeLerpTime = Duration.ZERO;
         var1.damagePerBlock = var0.getDamageAmount();
         var1.damageSafeZone = var0.getDamageBuffer();
         var1.warningTime = Duration.ofSeconds((long)var0.getWarningTime());
         var1.warningBlocks = var0.getWarningDistance();
         var1.handle = var1.createHandle();
         return var1;
      }
   }

   static {
      SUPPORTS_NATIVE_WORLDBORDERS = XReflection.of(Player.class).method().named("setWorldBorder").returns(Void.TYPE).parameters(WorldBorder.class).exists();
   }

   public static final class BorderBounds {
      protected final World lastCenterWorld;
      protected final double lastCenterX;
      protected final double lastCenterZ;
      public final double minX;
      public final double minZ;
      public final double maxX;
      public final double maxZ;

      private static double clamp(double var0, double var2, double var4) {
         return var0 < var2 ? var2 : Math.min(var0, var4);
      }

      public boolean isCenterSame(World var1, double var2, double var4) {
         return this.lastCenterWorld == var1 && this.lastCenterX == var2 && this.lastCenterZ == var4;
      }

      public BorderBounds(World var1, double var2, double var4, double var6) {
         this.lastCenterWorld = var1;
         this.lastCenterX = var2;
         this.lastCenterZ = var4;
         this.minX = clamp(var2 - var6 / 2.0D, -2.9999984E7D, 2.9999984E7D);
         this.minZ = clamp(var4 - var6 / 2.0D, -2.9999984E7D, 2.9999984E7D);
         this.maxX = clamp(var2 + var6 / 2.0D, -2.9999984E7D, 2.9999984E7D);
         this.maxZ = clamp(var4 + var6 / 2.0D, -2.9999984E7D, 2.9999984E7D);
      }
   }

   private static final class BukkitWorldBorder extends XWorldBorder {
      private final WorldBorder worldBorder;

      private BukkitWorldBorder(WorldBorder var1) {
         this.worldBorder = var1;
      }

      public double getDamageBuffer() {
         return this.worldBorder.getDamageBuffer();
      }

      public double getSizeLerpTarget() {
         return 0.0D;
      }

      public double getSize() {
         return this.worldBorder.getSize();
      }

      public int getWarningDistance() {
         return this.worldBorder.getWarningDistance();
      }

      public Duration getWarningTime() {
         return Duration.ofSeconds((long)this.worldBorder.getWarningTime());
      }

      public XWorldBorder setDamageBuffer(double var1) {
         this.worldBorder.setDamageBuffer(var1);
         return this;
      }

      public XWorldBorder setWarningDistance(int var1) {
         this.worldBorder.setWarningDistance(var1);
         return this;
      }

      public XWorldBorder setWarningTime(Duration var1) {
         this.worldBorder.setWarningTime((int)var1.getSeconds());
         return this;
      }

      public XWorldBorder setSize(double var1, @NotNull Duration var3) {
         this.worldBorder.setSize(var1, TimeUnit.MILLISECONDS, var3.toMillis());
         return this;
      }

      public XWorldBorder setCenter(Location var1) {
         this.worldBorder.setCenter(var1);
         return this;
      }

      public XWorldBorder setCenter(double var1, double var3) {
         this.worldBorder.setCenter(var1, var3);
         return this;
      }

      public XWorldBorder update(Player... var1) {
         return this;
      }

      public XWorldBorder setSizeLerpTarget(double var1) {
         this.worldBorder.setSize(var1);
         return this;
      }

      public void setFor(Collection<Player> var1, boolean var2) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Player var4 = (Player)var3.next();
            var4.setWorldBorder(this.worldBorder);
         }

      }

      public Location getCenter() {
         Location var1 = this.worldBorder.getCenter();
         if (this.borderBounds == null || this.borderBounds.isCenterSame(var1.getWorld(), var1.getX(), var1.getZ())) {
            this.updateBorderBounds(var1);
         }

         return var1;
      }

      public boolean isWithinBorder(Location var1) {
         return this.worldBorder.isInside(var1);
      }

      public XWorldBorder copy() {
         WorldBorder var1 = Bukkit.createWorldBorder();
         var1.setCenter(this.worldBorder.getCenter());
         var1.setSize(this.worldBorder.getSize());
         var1.setDamageBuffer(this.worldBorder.getDamageBuffer());
         var1.setDamageAmount(this.worldBorder.getDamageAmount());
         var1.setWarningDistance(this.worldBorder.getWarningDistance());
         var1.setWarningTime(this.worldBorder.getWarningTime());
         return new XWorldBorder.BukkitWorldBorder(var1);
      }

      // $FF: synthetic method
      BukkitWorldBorder(WorldBorder var1, Object var2) {
         this(var1);
      }
   }

   private static final class NMSWorldBorder extends XWorldBorder {
      private static final MethodHandle WORLD_HANDLE;
      private static final MethodHandle WORLDBORDER;
      private static final MethodHandle WORLDBORDER_WORLD;
      private static final MethodHandle CENTER;
      private static final MethodHandle WARNING_DISTANCE;
      private static final MethodHandle WARNING_TIME;
      private static final MethodHandle SIZE;
      private static final MethodHandle WorldBorder_lerpSizeBetween;
      private static final MethodHandle PACKET_WARNING_DISTANCE;
      private static final MethodHandle PACKET_WARNING_DELAY;
      private static final MethodHandle PACKET_LERP_SIZE;
      private static final MethodHandle PACKET_INIT;
      private static final MethodHandle PACKET_CENTER;
      private static final MethodHandle PACKET_SIZE;
      private static final Object INITIALIZE;
      private static final boolean SUPPORTS_SEPARATE_PACKETS;
      private static final Map<UUID, XWorldBorder> WORLD_BORDERS = new HashMap();
      private Object handle;
      private double damagePerBlock;
      private double damageSafeZone;
      private double size;
      private double sizeLerpTarget;
      private Duration warningTime;
      private Duration sizeLerpTime;
      private int warningBlocks;
      private World world;
      private double centerX;
      private double centerZ;
      private final Set<XWorldBorder.NMSWorldBorder.Component> updateRequired;
      private boolean init;

      private NMSWorldBorder() {
         this.damagePerBlock = 0.2D;
         this.damageSafeZone = 5.0D;
         this.size = 100.0D;
         this.sizeLerpTarget = 0.0D;
         this.warningTime = Duration.ofSeconds(15L);
         this.sizeLerpTime = Duration.ZERO;
         this.warningBlocks = 5;
         this.updateRequired = EnumSet.noneOf(XWorldBorder.NMSWorldBorder.Component.class);
         this.init = true;
      }

      public XWorldBorder.NMSWorldBorder copy() {
         XWorldBorder.NMSWorldBorder var1 = new XWorldBorder.NMSWorldBorder();
         var1.world = this.world;
         var1.centerX = this.centerX;
         var1.centerZ = this.centerZ;
         var1.size = this.size;
         var1.sizeLerpTime = this.sizeLerpTime;
         var1.damagePerBlock = this.damagePerBlock;
         var1.damageSafeZone = this.damageSafeZone;
         var1.warningTime = this.warningTime;
         var1.warningBlocks = this.warningBlocks;
         var1.handle = var1.createHandle();
         return var1;
      }

      public XWorldBorder setDamageAmount(double var1) {
         this.damagePerBlock = var1;
         return this;
      }

      public double getSize() {
         return this.size;
      }

      public double getDamageAmount() {
         return this.damagePerBlock;
      }

      public XWorldBorder setDamageBuffer(double var1) {
         this.damageSafeZone = var1;
         return this;
      }

      public double getDamageBuffer() {
         return this.damageSafeZone;
      }

      public XWorldBorder setWarningTime(Duration var1) {
         if (this.warningTime == var1) {
            return this;
         } else {
            this.warningTime = var1;
            this.update(XWorldBorder.NMSWorldBorder.Component.WARNING_DELAY);
            return this;
         }
      }

      public Duration getWarningTime() {
         return this.warningTime;
      }

      public XWorldBorder setWarningDistance(int var1) {
         if (this.warningBlocks == var1) {
            return this;
         } else {
            this.warningBlocks = var1;
            this.update(XWorldBorder.NMSWorldBorder.Component.WARNING_DISTANCE);
            return this;
         }
      }

      public double getSizeLerpTarget() {
         return this.sizeLerpTarget;
      }

      public XWorldBorder setSizeLerpTarget(double var1) {
         super.setSizeLerpTarget(var1);
         if (this.sizeLerpTarget == var1) {
            return this;
         } else {
            this.sizeLerpTarget = var1;
            this.update(XWorldBorder.NMSWorldBorder.Component.SIZE_LERP);
            return this;
         }
      }

      public XWorldBorder update(Player... var1) {
         this.setFor(Arrays.asList(var1), false);
         return this;
      }

      public int getWarningDistance() {
         return this.warningBlocks;
      }

      public XWorldBorder setCenter(Location var1) {
         this.setCenter(var1.getX(), var1.getZ());
         this.world = var1.getWorld();
         return this;
      }

      public XWorldBorder setCenter(double var1, double var3) {
         super.setCenter(var1, var3);
         if (this.centerX == var1 && this.centerZ == var3) {
            return this;
         } else {
            this.centerX = var1;
            this.centerZ = var3;
            this.updateBorderBounds(this.getCenter());
            this.update(XWorldBorder.NMSWorldBorder.Component.CENTER);
            return this;
         }
      }

      public Location getCenter() {
         return new Location(this.world, this.centerX, 0.0D, this.centerZ);
      }

      public XWorldBorder setSize(double var1, @NotNull Duration var3) {
         super.setSize(var1, var3);
         if (this.size == var1 && this.sizeLerpTime.equals(var3)) {
            return this;
         } else {
            this.size = var1;
            this.sizeLerpTime = var3;
            this.updateBorderBounds(this.getCenter());
            this.update(XWorldBorder.NMSWorldBorder.Component.SIZE);
            if (!var3.isZero()) {
               this.update(XWorldBorder.NMSWorldBorder.Component.SIZE_LERP);
            }

            return this;
         }
      }

      private void update(XWorldBorder.NMSWorldBorder.Component var1) {
         if (SUPPORTS_SEPARATE_PACKETS) {
            this.updateRequired.add(var1);
         }

      }

      public boolean isWithinBorder(Location var1) {
         if (this.borderBounds == null) {
            return false;
         } else if (this.world != null && this.world != var1.getWorld()) {
            return false;
         } else {
            return var1.getX() + 1.0D > this.borderBounds.minX && var1.getX() < this.borderBounds.maxX && var1.getZ() + 1.0D > this.borderBounds.minZ && var1.getZ() < this.borderBounds.maxZ;
         }
      }

      public void setFor(Collection<Player> var1, boolean var2) {
         boolean var3 = var2 || this.init;
         this.init = false;

         try {
            Iterator var5 = this.updateRequired.iterator();

            while(var5.hasNext()) {
               XWorldBorder.NMSWorldBorder.Component var6 = (XWorldBorder.NMSWorldBorder.Component)var5.next();
               var6.setHandle(this);
            }

            Object[] var4;
            if (SUPPORTS_SEPARATE_PACKETS && !var3) {
               var4 = new Object[this.updateRequired.size()];
               int var14 = 0;

               XWorldBorder.NMSWorldBorder.Component var7;
               for(Iterator var15 = this.updateRequired.iterator(); var15.hasNext(); var4[var14++] = var7.createPacket(this)) {
                  var7 = (XWorldBorder.NMSWorldBorder.Component)var15.next();
               }
            } else {
               Object var13 = XReflection.supports(17) ? PACKET_INIT.invoke(this.handle) : PACKET_INIT.invoke(this.handle, INITIALIZE);
               var4 = new Object[]{var13};
            }

            var5 = var1.iterator();

            while(var5.hasNext()) {
               Player var16 = (Player)var5.next();
               MinecraftConnection.sendPacket(var16, var4);
            }
         } catch (Throwable var11) {
            var11.printStackTrace();
         } finally {
            this.updateRequired.clear();
         }

      }

      private Object createHandle() {
         Objects.requireNonNull(this.world, "No world specified");

         try {
            Object var1 = WORLDBORDER.invoke();
            Object var2 = WORLD_HANDLE.invoke(this.world);
            WORLDBORDER_WORLD.invoke(var1, var2);
            return var1;
         } catch (Throwable var3) {
            var3.printStackTrace();
            return null;
         }
      }

      // $FF: synthetic method
      NMSWorldBorder(Object var1) {
         this();
      }

      static {
         if (!XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
            Object var0 = null;
            MethodHandle var1 = null;
            MethodHandle var2 = null;
            MethodHandle var3 = null;
            MethodHandle var4 = null;
            MethodHandle var5 = null;
            MethodHandle var6 = null;
            Lookup var8 = MethodHandles.lookup();
            MinecraftClassHandle var9 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.border").named("WorldBorder");
            MinecraftClassHandle var10 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerLevel").map(MinecraftMapping.SPIGOT, "WorldServer");
            MinecraftClassHandle var11 = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB).named("CraftWorld");

            try {
               if (!XReflection.supports(17)) {
                  Class var12;
                  try {
                     var12 = Class.forName("EnumWorldBorderAction");
                  } catch (ClassNotFoundException var18) {
                     var12 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutWorldBorder$EnumWorldBorderAction").unreflect();
                  }

                  var1 = var8.findConstructor((Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutWorldBorder").unreflect(), MethodType.methodType(Void.TYPE, var9.reflect(), var12));
                  Object[] var13 = var12.getEnumConstants();
                  int var14 = var13.length;

                  for(int var15 = 0; var15 < var14; ++var15) {
                     Object var16 = var13[var15];
                     if (var16.toString().equals("INITIALIZE")) {
                        var0 = var16;
                        break;
                     }
                  }
               }
            } catch (Exception var19) {
               var19.printStackTrace();
            }

            boolean var7;
            try {
               Function var20 = (var1x) -> {
                  return (MethodHandle)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named(var1x).constructor(new ClassHandle[]{var9}).unreflect();
               };
               var2 = (MethodHandle)var20.apply("ClientboundSetBorderWarningDistancePacket");
               var3 = (MethodHandle)var20.apply("ClientboundSetBorderWarningDelayPacket");
               var4 = (MethodHandle)var20.apply("ClientboundSetBorderLerpSizePacket");
               var1 = (MethodHandle)var20.apply("ClientboundInitializeBorderPacket");
               var5 = (MethodHandle)var20.apply("ClientboundSetBorderCenterPacket");
               var6 = (MethodHandle)var20.apply("ClientboundSetBorderSizePacket");
               var7 = true;
            } catch (Throwable var17) {
               var7 = false;
            }

            PACKET_INIT = var1;
            PACKET_SIZE = var6;
            PACKET_CENTER = var5;
            PACKET_LERP_SIZE = var4;
            PACKET_WARNING_DELAY = var3;
            PACKET_WARNING_DISTANCE = var2;
            SUPPORTS_SEPARATE_PACKETS = var7;
            WORLD_HANDLE = (MethodHandle)var11.method().named("getHandle").returns((ClassHandle)var10).unreflect();
            INITIALIZE = var0;
            WORLDBORDER = (MethodHandle)var9.constructor().unreflect();
            WORLDBORDER_WORLD = (MethodHandle)var9.field().setter().named("world").returns((ClassHandle)var10).unreflect();
            CENTER = (MethodHandle)var9.method().map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(21, 5, (Object)"d").v(18, "c").orElse((Object)"setCenter")).map(MinecraftMapping.MOJANG, "setCenter").returns(Void.TYPE).parameters(Double.TYPE, Double.TYPE).unreflect();
            SIZE = (MethodHandle)var9.method().named((String)XReflection.v(18, (Object)"a").orElse((Object)"setSize")).returns(Void.TYPE).parameters(Double.TYPE).unreflect();
            WARNING_TIME = (MethodHandle)var9.method().named((String)XReflection.v(18, (Object)"b").orElse((Object)"setWarningTime")).returns(Void.TYPE).parameters(Integer.TYPE).unreflect();
            WARNING_DISTANCE = (MethodHandle)var9.method().map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(20, (Object)"c").v(18, "b").orElse((Object)"setWarningDistance")).map(MinecraftMapping.MOJANG, "setWarningBlocks").returns(Void.TYPE).parameters(Integer.TYPE).unreflect();
            WorldBorder_lerpSizeBetween = (MethodHandle)var9.method().map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(18, (Object)"a").orElse((Object)"transitionSizeBetween")).map(MinecraftMapping.MOJANG, (String)XReflection.v(21, (Object)"lerpSizeBetween").orElse((Object)"transitionSizeBetween")).returns(Void.TYPE).parameters(Double.TYPE, Double.TYPE, Long.TYPE).unreflect();
         } else {
            PACKET_SIZE = null;
            PACKET_CENTER = null;
            PACKET_INIT = null;
            PACKET_LERP_SIZE = null;
            PACKET_WARNING_DELAY = null;
            PACKET_WARNING_DISTANCE = null;
            WorldBorder_lerpSizeBetween = null;
            SIZE = null;
            WARNING_TIME = null;
            WARNING_DISTANCE = null;
            CENTER = null;
            WORLDBORDER_WORLD = null;
            WORLDBORDER = null;
            WORLD_HANDLE = null;
            INITIALIZE = null;
            SUPPORTS_SEPARATE_PACKETS = true;
         }

      }

      private static enum Component {
         SIZE {
            protected void setHandle(XWorldBorder.NMSWorldBorder var1) {
               XWorldBorder.NMSWorldBorder.SIZE.invoke(var1.handle, var1.size);
            }

            protected Object createPacket(XWorldBorder.NMSWorldBorder var1) {
               return XWorldBorder.NMSWorldBorder.PACKET_SIZE.invoke(var1.handle);
            }
         },
         SIZE_LERP {
            protected void setHandle(XWorldBorder.NMSWorldBorder var1) {
               XWorldBorder.NMSWorldBorder.WorldBorder_lerpSizeBetween.invoke(var1.handle, var1.sizeLerpTarget, var1.size, var1.sizeLerpTime.toMillis());
            }

            protected Object createPacket(XWorldBorder.NMSWorldBorder var1) {
               return XWorldBorder.NMSWorldBorder.PACKET_LERP_SIZE.invoke(var1.handle);
            }
         },
         WARNING_DISTANCE {
            protected void setHandle(XWorldBorder.NMSWorldBorder var1) {
               XWorldBorder.NMSWorldBorder.WARNING_DISTANCE.invoke(var1.handle, var1.warningBlocks);
            }

            protected Object createPacket(XWorldBorder.NMSWorldBorder var1) {
               return XWorldBorder.NMSWorldBorder.PACKET_WARNING_DISTANCE.invoke(var1.handle);
            }
         },
         WARNING_DELAY {
            protected void setHandle(XWorldBorder.NMSWorldBorder var1) {
               XWorldBorder.NMSWorldBorder.WARNING_TIME.invoke(var1.handle, var1.warningBlocks);
            }

            protected Object createPacket(XWorldBorder.NMSWorldBorder var1) {
               return XWorldBorder.NMSWorldBorder.PACKET_WARNING_DELAY.invoke(var1.handle);
            }
         },
         CENTER {
            protected void setHandle(XWorldBorder.NMSWorldBorder var1) {
               XWorldBorder.NMSWorldBorder.CENTER.invoke(var1.handle, var1.centerX, var1.centerZ);
            }

            protected Object createPacket(XWorldBorder.NMSWorldBorder var1) {
               return XWorldBorder.NMSWorldBorder.PACKET_CENTER.invoke(var1.handle);
            }
         };

         private Component() {
         }

         protected abstract void setHandle(XWorldBorder.NMSWorldBorder var1);

         protected abstract Object createPacket(XWorldBorder.NMSWorldBorder var1);

         // $FF: synthetic method
         private static XWorldBorder.NMSWorldBorder.Component[] $values() {
            return new XWorldBorder.NMSWorldBorder.Component[]{SIZE, SIZE_LERP, WARNING_DISTANCE, WARNING_DELAY, CENTER};
         }

         // $FF: synthetic method
         Component(Object var3) {
            this();
         }
      }
   }

   public static final class Events implements Listener {
      @EventHandler
      public void onJoin(PlayerMoveEvent var1) {
         XWorldBorder var2 = XWorldBorder.get(var1.getPlayer());
         if (var2 != null) {
            Player var3 = var1.getPlayer();
            Location var4 = var3.getLocation();
            if (!var2.isWithinBorder(var4)) {
               double var5 = var2.getDistanceToBorder(var4);
               if (!(var5 < var2.getDamageBuffer())) {
                  var3.damage(var2.getDamageBuffer() * var5);
               }
            }
         }
      }

      @EventHandler
      public void onJoin(PlayerJoinEvent var1) {
         Player var2 = var1.getPlayer();
         XWorldBorder var3 = XWorldBorder.get(var2);
         if (var3 != null) {
            var3.setFor(Collections.singleton(var2), true);
         }
      }

      @EventHandler(
         ignoreCancelled = true,
         priority = EventPriority.MONITOR
      )
      public void onWorldChange(PlayerChangedWorldEvent var1) {
         Player var2 = var1.getPlayer();
         XWorldBorder var3 = XWorldBorder.get(var2);
         if (var3 != null) {
            var3.setFor(Collections.singleton(var2), true);
         }
      }
   }
}
