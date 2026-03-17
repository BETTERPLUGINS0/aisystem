package me.PM2.infinitevehicles.xseries.particles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParticleDisplay {
   private static final boolean ISFLAT;
   private static final boolean SUPPORTS_ALPHA_COLORS;
   public static final Color[] NOTE_COLORS;
   @NotNull
   private static final XParticle DEFAULT_PARTICLE;
   public int count = 1;
   public double extra;
   public boolean force;
   @NotNull
   private XParticle particle;
   @Nullable
   private Location location;
   @Nullable
   private Location lastLocation;
   @NotNull
   private Vector offset;
   @Nullable
   private Vector particleDirection;
   @NotNull
   private Vector direction;
   @NotNull
   public List<List<ParticleDisplay.Rotation>> rotations;
   @Nullable
   private List<ParticleDisplay.Quaternion> cachedFinalRotationQuaternions;
   @Nullable
   private ParticleDisplay.ParticleData data;
   @Nullable
   private Consumer<ParticleDisplay.CalculationContext> preCalculation;
   @Nullable
   private Consumer<ParticleDisplay.CalculationContext> postCalculation;
   @Nullable
   private Function<Double, Double> onAdvance;
   @Nullable
   private Set<Player> players;

   public ParticleDisplay() {
      this.particle = DEFAULT_PARTICLE;
      this.offset = new Vector();
      this.direction = new Vector(0, 1, 0);
      this.rotations = new ArrayList();
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ParticleDisplay colored(@Nullable Location var0, int var1, int var2, int var3, float var4) {
      return of(XParticle.DUST).withLocation(var0).withColor((float)var1, (float)var2, (float)var3, var4);
   }

   @Nullable
   public Set<Player> getPlayers() {
      return this.players;
   }

   public ParticleDisplay onlyVisibleTo(Collection<Player> var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.players == null) {
            this.players = Collections.newSetFromMap(new WeakHashMap());
         }

         this.players.addAll(var1);
         return this;
      } else {
         this.players = null;
         return this;
      }
   }

   public ParticleDisplay onlyVisibleTo(Player... var1) {
      if (var1 != null && var1.length != 0) {
         if (this.players == null) {
            this.players = Collections.newSetFromMap(new WeakHashMap());
         }

         Collections.addAll(this.players, var1);
         return this;
      } else {
         this.players = null;
         return this;
      }
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ParticleDisplay colored(Location var0, @NotNull Color var1, float var2) {
      return of(XParticle.DUST).withLocation(var0).withColor(var1, var2);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ParticleDisplay simple(@Nullable Location var0, @NotNull Particle var1) {
      Objects.requireNonNull(var1, "Cannot build ParticleDisplay with null particle");
      ParticleDisplay var2 = new ParticleDisplay();
      var2.particle = XParticle.of(var1);
      var2.location = var0;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ParticleDisplay of(@NotNull Particle var0) {
      return of(XParticle.of(var0));
   }

   @NotNull
   public static ParticleDisplay of(@NotNull XParticle var0) {
      ParticleDisplay var1 = new ParticleDisplay();
      var1.particle = var0;
      return var1;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ParticleDisplay display(@NotNull Location var0, @NotNull Particle var1) {
      Objects.requireNonNull(var0, "Cannot display particle in null location");
      ParticleDisplay var2 = simple(var0, var1);
      var2.spawn();
      return var2;
   }

   public static ParticleDisplay fromConfig(@NotNull ConfigurationSection var0) {
      return edit(new ParticleDisplay(), var0);
   }

   private static int toInt(String var0) {
      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var2) {
         return 0;
      }
   }

   private static double toDouble(String var0) {
      try {
         return Double.parseDouble(var0);
      } catch (NumberFormatException var2) {
         return 0.0D;
      }
   }

   private static List<String> split(@NotNull String var0, char var1) {
      ArrayList var2 = new ArrayList(5);
      boolean var3 = false;
      boolean var4 = false;
      int var5 = var0.length();
      int var6 = 0;

      for(int var7 = 0; var7 < var5; ++var7) {
         if (var0.charAt(var7) == var1) {
            if (var3) {
               var2.add(var0.substring(var6, var7));
               var3 = false;
               var4 = true;
            }

            var6 = var7 + 1;
         } else {
            var4 = false;
            var3 = true;
         }
      }

      if (var3 || var4) {
         var2.add(var0.substring(var6, var5));
      }

      return var2;
   }

   @NotNull
   public static ParticleDisplay edit(@NotNull ParticleDisplay var0, @NotNull ConfigurationSection var1) {
      Objects.requireNonNull(var0, "Cannot edit a null particle display");
      Objects.requireNonNull(var1, "Cannot parse ParticleDisplay from a null config section");
      String var2 = var1.getString("particle");
      Optional var3 = var2 == null ? Optional.empty() : XParticle.of(var2);
      var3.ifPresent((var1x) -> {
         var0.particle = var1x;
      });
      if (var1.isSet("count")) {
         var0.withCount(var1.getInt("count"));
      }

      if (var1.isSet("extra")) {
         var0.withExtra(var1.getDouble("extra"));
      }

      if (var1.isSet("force")) {
         var0.forceSpawn(var1.getBoolean("force"));
      }

      String var4 = var1.getString("offset");
      if (var4 != null) {
         List var5 = split(var4.replace(" ", ""), ',');
         double var6;
         if (var5.size() >= 3) {
            var6 = toDouble((String)var5.get(0));
            double var8 = toDouble((String)var5.get(1));
            double var10 = toDouble((String)var5.get(2));
            var0.offset(var6, var8, var10);
         } else {
            var6 = toDouble((String)var5.get(0));
            var0.offset(var6);
         }
      }

      String var20 = var1.getString("direction");
      double var7;
      if (var20 != null) {
         List var21 = split(var20.replace(" ", ""), ',');
         if (var21.size() >= 3) {
            var7 = toDouble((String)var21.get(0));
            double var9 = toDouble((String)var21.get(1));
            double var11 = toDouble((String)var21.get(2));
            var0.particleDirection(var7, var9, var11);
         }
      }

      ConfigurationSection var22 = var1.getConfigurationSection("rotations");
      String var12;
      if (var22 != null) {
         Iterator var23 = var22.getKeys(false).iterator();

         while(var23.hasNext()) {
            String var24 = (String)var23.next();
            ConfigurationSection var25 = var22.getConfigurationSection(var24);
            ArrayList var27 = new ArrayList();

            double var14;
            Vector var16;
            for(Iterator var29 = var25.getKeys(false).iterator(); var29.hasNext(); var27.add(ParticleDisplay.Rotation.of(var14, var16))) {
               var12 = (String)var29.next();
               ConfigurationSection var13 = var25.getConfigurationSection(var12);
               var14 = var13.getDouble("angle");
               String var17 = var13.getString("vector").toUpperCase(Locale.ENGLISH).replace(" ", "");
               if (var17.length() == 1) {
                  var16 = ParticleDisplay.Axis.valueOf(var17).vector;
               } else {
                  String[] var18 = var17.split(",");
                  var16 = new Vector(Math.toRadians(Double.parseDouble(var18[0])), Math.toRadians(Double.parseDouble(var18[1])), Math.toRadians(Double.parseDouble(var18[2])));
               }
            }

            var0.rotations.add(var27);
         }
      }

      if (var1.isSet("size")) {
         var7 = var1.getDouble("size");
         var0.extra = var7;
      } else {
         var7 = 1.0D;
      }

      String var26 = var1.getString("color");
      String var28 = var1.getString("blockdata");
      String var30 = var1.getString("itemstack");
      var12 = var1.getString("materialdata");
      if (var26 != null) {
         List var31 = split(var26.replace(" ", ""), ',');
         if (var31.size() <= 3 || var31.size() == 6) {
            Color var33 = Color.white;
            Color var15 = null;
            if (var31.size() <= 2) {
               try {
                  var33 = Color.decode((String)var31.get(0));
                  if (var31.size() == 2) {
                     var15 = Color.decode((String)var31.get(1));
                  }
               } catch (NumberFormatException var19) {
               }
            } else {
               var33 = new Color(toInt((String)var31.get(0)), toInt((String)var31.get(1)), toInt((String)var31.get(2)));
               if (var31.size() == 6) {
                  var15 = new Color(toInt((String)var31.get(3)), toInt((String)var31.get(4)), toInt((String)var31.get(5)));
               }
            }

            if (var15 != null) {
               var0.data = new ParticleDisplay.DustTransitionParticleColor(var33, var15, var7);
            } else {
               var0.data = new ParticleDisplay.RGBParticleColor(var33);
            }
         }
      } else {
         Material var32;
         if (var28 != null) {
            var32 = Material.getMaterial(var28);
            if (var32 != null && var32.isBlock()) {
               var0.data = new ParticleDisplay.ParticleBlockData(var32.createBlockData());
            }
         } else if (var30 != null) {
            var32 = Material.getMaterial(var30);
            if (var32 != null && var32.isItem()) {
               var0.data = new ParticleDisplay.ParticleItemData(new ItemStack(var32, 1));
            }
         } else if (var12 != null) {
            var32 = Material.getMaterial(var12);
            if (var32 != null && var32.isBlock()) {
               var0.data = new ParticleDisplay.ParticleMaterialData(var32.getNewData((byte)0));
            }
         }
      }

      return var0;
   }

   public static void serialize(ParticleDisplay var0, ConfigurationSection var1) {
      var1.set("particle", var0.particle.name());
      if (var0.count != 1) {
         var1.set("count", var0.count);
      }

      if (var0.extra != 0.0D) {
         var1.set("extra", var0.extra);
      }

      if (var0.force) {
         var1.set("force", true);
      }

      Vector var2;
      if (!isZero(var0.offset)) {
         var2 = var0.offset;
         var1.set("offset", var2.getX() + ", " + var2.getY() + ", " + var2.getZ());
      }

      if (var0.particleDirection != null) {
         var2 = var0.particleDirection;
         var1.set("direction", var2.getX() + ", " + var2.getY() + ", " + var2.getZ());
      }

      if (!var0.rotations.isEmpty()) {
         ConfigurationSection var13 = var1.createSection("rotations");
         int var3 = 1;
         Iterator var4 = var0.rotations.iterator();

         while(var4.hasNext()) {
            List var5 = (List)var4.next();
            ConfigurationSection var6 = var13.createSection("group-" + var3++);
            int var7 = 1;
            Iterator var8 = var5.iterator();

            while(var8.hasNext()) {
               ParticleDisplay.Rotation var9 = (ParticleDisplay.Rotation)var8.next();
               ConfigurationSection var10 = var6.createSection(String.valueOf(var7++));
               var10.set("angle", var9.angle);
               Vector var11 = var9.axis;
               Optional var12 = Arrays.stream(ParticleDisplay.Axis.values()).filter((var1x) -> {
                  return var1x.vector.equals(var11);
               }).findFirst();
               if (var12.isPresent()) {
                  var10.set("axis", ((ParticleDisplay.Axis)var12.get()).name());
               } else {
                  var10.set("axis", var11.getX() + ", " + var11.getY() + ", " + var11.getZ());
               }
            }
         }
      }

      if (var0.data != null) {
         var0.data.serialize(var1);
      }

   }

   public static Vector rotateAround(@NotNull Vector var0, @NotNull ParticleDisplay.Axis var1, @NotNull Vector var2) {
      Objects.requireNonNull(var1, "Cannot rotate around null axis");
      Objects.requireNonNull(var2, "Rotation vector cannot be null");
      switch(var1.ordinal()) {
      case 0:
         return rotateAround(var0, var1, var2.getX());
      case 1:
         return rotateAround(var0, var1, var2.getY());
      case 2:
         return rotateAround(var0, var1, var2.getZ());
      default:
         throw new AssertionError("Unknown rotation axis: " + var1);
      }
   }

   public static Vector rotateAround(@NotNull Vector var0, double var1, double var3, double var5) {
      rotateAround(var0, ParticleDisplay.Axis.X, var1);
      rotateAround(var0, ParticleDisplay.Axis.Y, var3);
      rotateAround(var0, ParticleDisplay.Axis.Z, var5);
      return var0;
   }

   public static Vector rotateAround(@NotNull Vector var0, @NotNull ParticleDisplay.Axis var1, double var2) {
      Objects.requireNonNull(var0, "Cannot rotate a null location");
      Objects.requireNonNull(var1, "Cannot rotate around null axis");
      if (var2 == 0.0D) {
         return var0;
      } else {
         double var4 = Math.cos(var2);
         double var6 = Math.sin(var2);
         double var8;
         double var10;
         switch(var1.ordinal()) {
         case 0:
            var8 = var0.getY() * var4 - var0.getZ() * var6;
            var10 = var0.getY() * var6 + var0.getZ() * var4;
            return var0.setY(var8).setZ(var10);
         case 1:
            var8 = var0.getX() * var4 + var0.getZ() * var6;
            var10 = var0.getX() * -var6 + var0.getZ() * var4;
            return var0.setX(var8).setZ(var10);
         case 2:
            var8 = var0.getX() * var4 - var0.getY() * var6;
            var10 = var0.getX() * var6 + var0.getY() * var4;
            return var0.setX(var8).setY(var10);
         default:
            throw new AssertionError("Unknown rotation axis: " + var1);
         }
      }
   }

   public ParticleDisplay preCalculation(@Nullable Consumer<ParticleDisplay.CalculationContext> var1) {
      this.preCalculation = var1;
      return this;
   }

   public ParticleDisplay postCalculation(@Nullable Consumer<ParticleDisplay.CalculationContext> var1) {
      this.postCalculation = var1;
      return this;
   }

   public ParticleDisplay onAdvance(@Nullable Function<Double, Double> var1) {
      this.onAdvance = var1;
      return this;
   }

   public ParticleDisplay withParticle(@NotNull Particle var1) {
      return this.withParticle(XParticle.of((Particle)Objects.requireNonNull(var1, "Particle cannot be null")));
   }

   public ParticleDisplay withParticle(@NotNull XParticle var1) {
      this.particle = (XParticle)Objects.requireNonNull(var1, "Particle cannot be null");
      return this;
   }

   @NotNull
   public Vector getDirection() {
      return this.direction;
   }

   public void advanceInDirection(double var1) {
      Objects.requireNonNull(this.direction, "Cannot advance with null direction");
      if (var1 != 0.0D) {
         if (this.onAdvance != null) {
            var1 = (Double)this.onAdvance.apply(var1);
         }

         this.location.add(this.direction.clone().multiply(var1));
      }
   }

   public ParticleDisplay withDirection(@Nullable Vector var1) {
      this.direction = var1.clone().normalize();
      return this;
   }

   @NotNull
   public XParticle getParticle() {
      return this.particle;
   }

   public int getCount() {
      return this.count;
   }

   public double getExtra() {
      return this.extra;
   }

   @Nullable
   public ParticleDisplay.ParticleData getData() {
      return this.data;
   }

   public ParticleDisplay withData(ParticleDisplay.ParticleData var1) {
      this.data = var1;
      return this;
   }

   public String toString() {
      return "ParticleDisplay:[Particle=" + this.particle + ", Count=" + this.count + ", Offset:{" + this.offset.getX() + ", " + this.offset.getY() + ", " + this.offset.getZ() + "}, " + (this.location != null ? "Location:{" + this.location.getWorld().getName() + this.location.getX() + ", " + this.location.getY() + ", " + this.location.getZ() + "}, " : "") + "Rotation:" + this.rotations + ", Extra=" + this.extra + ", Force=" + this.force + ", Data=" + this.data;
   }

   @NotNull
   public ParticleDisplay withCount(int var1) {
      this.count = var1;
      return this;
   }

   @NotNull
   public ParticleDisplay withExtra(double var1) {
      this.extra = var1;
      return this;
   }

   @NotNull
   public ParticleDisplay forceSpawn(boolean var1) {
      this.force = var1;
      return this;
   }

   @NotNull
   public ParticleDisplay withColor(@NotNull Color var1, float var2) {
      return this.withColor((float)var1.getRed(), (float)var1.getGreen(), (float)var1.getBlue(), var2);
   }

   @NotNull
   public ParticleDisplay withColor(@NotNull Color var1) {
      return this.withColor(var1, 1.0F);
   }

   @NotNull
   public ParticleDisplay withNoteColor(int var1) {
      this.data = new ParticleDisplay.NoteParticleColor(var1);
      return this;
   }

   @NotNull
   public ParticleDisplay withNoteColor(Note var1) {
      return this.withNoteColor(var1.getId());
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public ParticleDisplay withColor(float var1, float var2, float var3, float var4) {
      this.data = new ParticleDisplay.RGBParticleColor((int)var1, (int)var2, (int)var3);
      this.extra = (double)var4;
      return this;
   }

   @NotNull
   public ParticleDisplay withTransitionColor(@NotNull Color var1, float var2, @NotNull Color var3) {
      this.data = new ParticleDisplay.DustTransitionParticleColor(var1, var3, (double)var2);
      this.extra = (double)var2;
      return this;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public ParticleDisplay withTransitionColor(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      return this.withTransitionColor(new Color((int)var1, (int)var2, (int)var3), var4, new Color((int)var5, (int)var6, (int)var7));
   }

   @NotNull
   public ParticleDisplay withBlock(@NotNull BlockData var1) {
      this.data = new ParticleDisplay.ParticleBlockData(var1);
      return this;
   }

   @NotNull
   public ParticleDisplay withBlock(@NotNull MaterialData var1) {
      this.data = new ParticleDisplay.ParticleMaterialData(var1);
      return this;
   }

   @NotNull
   public ParticleDisplay withItem(@NotNull ItemStack var1) {
      this.data = new ParticleDisplay.ParticleItemData(var1);
      return this;
   }

   @NotNull
   public Vector getOffset() {
      return this.offset;
   }

   @NotNull
   public Vector getParticleDirection() {
      return this.direction;
   }

   @NotNull
   public ParticleDisplay withEntity(@NotNull Entity var1) {
      Objects.requireNonNull(var1);
      return this.withLocationCaller(var1::getLocation);
   }

   @NotNull
   public ParticleDisplay withLocationCaller(@Nullable Callable<Location> var1) {
      this.preCalculation = (var2) -> {
         try {
            var2.location = (Location)var1.call();
         } catch (Exception var4) {
            throw new IllegalStateException("Failed to calculate location of particle: " + this, var4);
         }
      };
      return this;
   }

   @Nullable
   public Location getLocation() {
      return this.location;
   }

   public ParticleDisplay withLocation(@Nullable Location var1) {
      this.location = var1;
      return this;
   }

   @NotNull
   public ParticleDisplay face(@NotNull Entity var1) {
      return this.face(((Entity)Objects.requireNonNull(var1, "Cannot face null entity")).getLocation());
   }

   @NotNull
   public ParticleDisplay face(@NotNull Location var1) {
      Objects.requireNonNull(var1, "Cannot face null location");
      this.rotate(ParticleDisplay.Rotation.of(Math.toRadians((double)var1.getYaw()), ParticleDisplay.Axis.Y), ParticleDisplay.Rotation.of(Math.toRadians((double)(-var1.getPitch())), ParticleDisplay.Axis.X));
      this.direction = var1.getDirection().clone().normalize();
      return this;
   }

   @Nullable
   public Location cloneLocation(double var1, double var3, double var5) {
      return this.location == null ? null : cloneLocation(this.location).add(var1, var3, var5);
   }

   @NotNull
   private static Location cloneLocation(@NotNull Location var0) {
      return new Location(var0.getWorld(), var0.getX(), var0.getY(), var0.getZ(), var0.getYaw(), var0.getPitch());
   }

   private static boolean isZero(@NotNull Vector var0) {
      return var0.getX() == 0.0D && var0.getY() == 0.0D && var0.getZ() == 0.0D;
   }

   @NotNull
   public ParticleDisplay cloneWithLocation(double var1, double var3, double var5) {
      ParticleDisplay var7 = this.copy();
      if (this.location == null) {
         return var7;
      } else {
         var7.location.add(var1, var3, var5);
         return var7;
      }
   }

   @NotNull
   public ParticleDisplay copy() {
      ParticleDisplay var1 = of(this.particle).withDirection(this.direction).withCount(this.count).offset(this.offset.clone()).forceSpawn(this.force).onlyVisibleTo((Collection)this.players).preCalculation(this.preCalculation).postCalculation(this.postCalculation);
      if (this.location != null) {
         var1.location = cloneLocation(this.location);
      }

      if (!this.rotations.isEmpty()) {
         var1.rotations = new ArrayList(this.rotations);
      }

      var1.data = this.data;
      return var1;
   }

   public static Vector getPrincipalAxesRotation(Location var0) {
      return getPrincipalAxesRotation(var0.getPitch(), var0.getYaw(), 0.0F);
   }

   public static Vector getPrincipalAxesRotation(float var0, float var1, float var2) {
      return new Vector(Math.toRadians((double)(var0 + 90.0F)), Math.toRadians((double)(-var1)), (double)var2);
   }

   public static float[] getYawPitch(Vector var0) {
      double var1 = 6.283185307179586D;
      double var3 = var0.getX();
      double var5 = var0.getZ();
      float var7;
      float var8;
      if (var3 == 0.0D && var5 == 0.0D) {
         var8 = 0.0F;
         var7 = var0.getY() > 0.0D ? -90.0F : 90.0F;
      } else {
         double var9 = Math.atan2(-var3, var5);
         var8 = (float)Math.toDegrees((var9 + 6.283185307179586D) % 6.283185307179586D);
         double var11 = NumberConversions.square(var3);
         double var13 = NumberConversions.square(var5);
         double var15 = Math.sqrt(var11 + var13);
         var7 = (float)Math.toDegrees(Math.atan(-var0.getY() / var15));
      }

      return new float[]{var8, var7};
   }

   @NotNull
   public List<ParticleDisplay.Quaternion> getRotation(boolean var1) {
      if (this.rotations.isEmpty()) {
         return new ArrayList();
      } else {
         if (var1) {
            this.cachedFinalRotationQuaternions = null;
         }

         if (this.cachedFinalRotationQuaternions == null) {
            this.cachedFinalRotationQuaternions = new ArrayList();
            Iterator var2 = this.rotations.iterator();

            while(var2.hasNext()) {
               List var3 = (List)var2.next();
               ParticleDisplay.Quaternion var4 = null;
               Iterator var5 = var3.iterator();

               while(var5.hasNext()) {
                  ParticleDisplay.Rotation var6 = (ParticleDisplay.Rotation)var5.next();
                  ParticleDisplay.Quaternion var7 = ParticleDisplay.Quaternion.rotation(var6.angle, var6.axis);
                  if (var4 == null) {
                     var4 = var7;
                  } else {
                     var4 = var4.mul(var7);
                  }
               }

               this.cachedFinalRotationQuaternions.add(var4);
            }
         }

         return this.cachedFinalRotationQuaternions;
      }
   }

   @NotNull
   public ParticleDisplay rotate(double var1, double var3, double var5) {
      return this.rotate(ParticleDisplay.Rotation.of(var1, ParticleDisplay.Axis.X), ParticleDisplay.Rotation.of(var3, ParticleDisplay.Axis.Y), ParticleDisplay.Rotation.of(var5, ParticleDisplay.Axis.Z));
   }

   public ParticleDisplay rotate(ParticleDisplay.Rotation... var1) {
      Objects.requireNonNull(var1, "Null rotations");
      if (var1.length != 0) {
         List var2 = (List)Arrays.stream(var1).filter((var0) -> {
            return var0.angle != 0.0D;
         }).collect(Collectors.toList());
         if (!var2.isEmpty()) {
            this.rotations.add(var2);
            if (this.cachedFinalRotationQuaternions != null) {
               this.cachedFinalRotationQuaternions.clear();
            }
         }
      }

      return this;
   }

   public ParticleDisplay rotate(ParticleDisplay.Rotation var1) {
      Objects.requireNonNull(var1, "Null rotation");
      if (var1.angle != 0.0D) {
         this.rotations.add(Collections.singletonList(var1));
         if (this.cachedFinalRotationQuaternions != null) {
            this.cachedFinalRotationQuaternions.clear();
         }
      }

      return this;
   }

   @Nullable
   public Location getLastLocation() {
      return this.lastLocation == null ? this.getLocation() : this.lastLocation;
   }

   @Nullable
   public Location finalizeLocation(@Nullable Vector var1) {
      ParticleDisplay.CalculationContext var2 = new ParticleDisplay.CalculationContext(this.location, var1);
      if (this.preCalculation != null) {
         this.preCalculation.accept(var2);
      }

      if (!var2.shouldSpawn) {
         return null;
      } else {
         Location var3 = var2.location;
         if (var3 == null) {
            throw new IllegalStateException("Attempting to spawn particle when no location is set");
         } else {
            var1 = var2.local;
            if (var1 != null && !this.rotations.isEmpty()) {
               List var4 = this.getRotation(false);

               ParticleDisplay.Quaternion var6;
               for(Iterator var5 = var4.iterator(); var5.hasNext(); var1 = ParticleDisplay.Quaternion.rotate(var1, var6)) {
                  var6 = (ParticleDisplay.Quaternion)var5.next();
               }
            }

            var3 = cloneLocation(var3);
            if (var1 != null) {
               var3.add(var1);
            }

            ParticleDisplay.CalculationContext var7 = new ParticleDisplay.CalculationContext(var3, var1);
            if (this.postCalculation != null) {
               this.postCalculation.accept(var7);
            }

            return !var7.shouldSpawn ? null : var3;
         }
      }
   }

   @NotNull
   public ParticleDisplay offset(double var1, double var3, double var5) {
      return this.offset(new Vector(var1, var3, var5));
   }

   @NotNull
   public ParticleDisplay offset(@NotNull Vector var1) {
      this.offset = (Vector)Objects.requireNonNull(var1, "Particle offset cannot be null");
      return this;
   }

   @NotNull
   public ParticleDisplay offset(double var1) {
      return this.offset(var1, var1, var1);
   }

   @NotNull
   public ParticleDisplay particleDirection(double var1, double var3, double var5) {
      return this.particleDirection(new Vector(var1, var3, var5));
   }

   @NotNull
   public ParticleDisplay particleDirection(@Nullable Vector var1) {
      this.particleDirection = var1;
      if (var1 != null && this.extra == 0.0D) {
         this.extra = 1.0D;
      }

      return this;
   }

   @NotNull
   public ParticleDisplay directional() {
      this.particleDirection = new Vector();
      return this;
   }

   public boolean isDirectional() {
      return this.particleDirection != null;
   }

   @Nullable
   public Location spawn() {
      return this.spawn(this.finalizeLocation((Vector)null));
   }

   @Nullable
   public Location spawn(@Nullable Vector var1) {
      return this.spawn(this.finalizeLocation(var1));
   }

   @Nullable
   public Location spawn(double var1, double var3, double var5) {
      return this.spawn(this.finalizeLocation(new Vector(var1, var3, var5)));
   }

   @Nullable
   public Location spawn(Location var1) {
      if (var1 == null) {
         return null;
      } else {
         this.lastLocation = var1;
         Particle var2 = this.particle.get();
         Objects.requireNonNull(var2, () -> {
            return "Cannot spawn unsupported particle: " + var2;
         });
         if (this.count == 0) {
            this.count = 1;
         }

         Object var3 = null;
         if (this.data != null) {
            this.data = this.data.transform(this);
            Vector var4 = this.data.offsetValues(this);
            if (var4 != null) {
               this.spawnWithDataInOffset(var2, var1, var4, (Object)null);
               return var1;
            }

            var3 = this.data.data(this);
            if (!var2.getDataType().isInstance(var3)) {
               var3 = null;
            }
         }

         if (this.particleDirection != null) {
            this.spawnWithDataInOffset(var2, var1, this.particleDirection, var3);
            return var1;
         } else {
            this.spawnRaw(var2, var1, this.count, this.offset, var3);
            return var1;
         }
      }
   }

   private void spawnWithDataInOffset(Particle var1, Location var2, Vector var3, Object var4) {
      if (isZero(this.offset) && this.count < 2) {
         this.spawnRaw(var1, var2, 0, var3, var4);
      } else {
         double var5 = this.offset.getX();
         double var7 = this.offset.getY();
         double var9 = this.offset.getZ();
         ThreadLocalRandom var11 = ThreadLocalRandom.current();

         for(int var12 = 0; var12 < this.count; ++var12) {
            double var13 = var5 == 0.0D ? 0.0D : var11.nextGaussian() * 4.0D * var5;
            double var15 = var7 == 0.0D ? 0.0D : var11.nextGaussian() * 4.0D * var7;
            double var17 = var9 == 0.0D ? 0.0D : var11.nextGaussian() * 4.0D * var9;
            Location var19 = cloneLocation(var2).add(var13, var15, var17);
            this.spawnRaw(var1, var19, 0, var3, var4);
         }

      }
   }

   private void spawnRaw(Particle var1, Location var2, int var3, Vector var4, Object var5) {
      double var6 = var4.getX();
      double var8 = var4.getY();
      double var10 = var4.getZ();
      double var12 = this.extra;
      if (this.particle == XParticle.DUST || this.particle == XParticle.NOTE) {
         var12 = 1.0D;
      }

      if (this.players != null && !this.players.isEmpty()) {
         Player[] var14 = (Player[])this.players.toArray(new Player[0]);
         int var15 = var14.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            Player var17 = var14[var16];
            var17.spawnParticle(var1, var2, var3, var6, var8, var10, var12, var5);
         }
      } else if (ISFLAT) {
         var2.getWorld().spawnParticle(var1, var2, var3, var6, var8, var10, var12, var5, this.force);
      } else {
         var2.getWorld().spawnParticle(var1, var2, var3, var6, var8, var10, var12, var5);
      }

   }

   public static int findNearestNoteColor(Color var0) {
      double var1 = colorDistanceSquared(var0, NOTE_COLORS[0]);
      int var3 = 0;

      for(int var4 = 1; var4 < NOTE_COLORS.length; ++var4) {
         double var5 = colorDistanceSquared(var0, NOTE_COLORS[var4]);
         if (var5 < var1) {
            var1 = var5;
            var3 = var4;
         }
      }

      return var3;
   }

   public static double colorDistanceSquared(Color var0, Color var1) {
      int var2 = var0.getRed();
      int var3 = var1.getRed();
      int var4 = var2 + var3 >> 1;
      int var5 = var2 - var3;
      int var6 = var0.getGreen() - var1.getGreen();
      int var7 = var0.getBlue() - var1.getBlue();
      return (double)(((512 + var4) * var5 * var5 >> 8) + 4 * var6 * var6 + ((767 - var4) * var7 * var7 >> 8));
   }

   static {
      boolean var0;
      try {
         World.class.getDeclaredMethod("spawnParticle", Particle.class, Location.class, Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Object.class, Boolean.TYPE);
         var0 = true;
      } catch (NoSuchMethodException var4) {
         var0 = false;
      }

      ISFLAT = var0;

      boolean var1;
      try {
         org.bukkit.Color.fromARGB(0);
         var1 = true;
      } catch (NoSuchMethodError var3) {
         var1 = false;
      }

      SUPPORTS_ALPHA_COLORS = var1;
      NOTE_COLORS = new Color[]{new Color(7853824), new Color(9814016), new Color(11707648), new Color(13403648), new Color(14836992), new Color(15941888), new Color(16522752), new Color(16646159), new Color(16187443), new Color(15204442), new Color(13566083), new Color(11403433), new Color(8782028), new Color(5964007), new Color(2949369), new Color(133886), new Color(14326), new Color(26848), new Color(39612), new Color(50829), new Color(59736), new Color(64545), new Color(2096128), new Color(5892096), new Color(9748736)};
      DEFAULT_PARTICLE = XParticle.FLAME;
   }

   public static enum Axis {
      X(new Vector(1, 0, 0)),
      Y(new Vector(0, 1, 0)),
      Z(new Vector(0, 0, 1));

      private final Vector vector;

      private Axis(Vector param3) {
         this.vector = var3;
      }

      public Vector getVector() {
         return this.vector;
      }

      // $FF: synthetic method
      private static ParticleDisplay.Axis[] $values() {
         return new ParticleDisplay.Axis[]{X, Y, Z};
      }
   }

   public static class Rotation {
      public double angle;
      public Vector axis;

      public Rotation(double var1, Vector var3) {
         this.angle = var1;
         this.axis = var3;
      }

      public ParticleDisplay.Rotation copy() {
         return new ParticleDisplay.Rotation(this.angle, this.axis.clone());
      }

      public static ParticleDisplay.Rotation of(double var0, Vector var2) {
         return new ParticleDisplay.Rotation(var0, var2);
      }

      public static ParticleDisplay.Rotation of(double var0, ParticleDisplay.Axis var2) {
         return new ParticleDisplay.Rotation(var0, var2.vector);
      }
   }

   public static class DustTransitionParticleColor implements ParticleDisplay.ParticleData {
      private final DustTransition dustTransition;

      public DustTransitionParticleColor(Color var1, Color var2, double var3) {
         this.dustTransition = new DustTransition(org.bukkit.Color.fromRGB(var1.getRed(), var1.getGreen(), var1.getBlue()), org.bukkit.Color.fromRGB(var2.getRed(), var2.getGreen(), var2.getBlue()), (float)var3);
      }

      public Object data(ParticleDisplay var1) {
         return this.dustTransition;
      }

      public void serialize(ConfigurationSection var1) {
         StringJoiner var2 = new StringJoiner(", ");
         org.bukkit.Color var3 = this.dustTransition.getColor();
         org.bukkit.Color var4 = this.dustTransition.getToColor();
         var2.add(Integer.toString(var3.getRed()));
         var2.add(Integer.toString(var3.getGreen()));
         var2.add(Integer.toString(var3.getBlue()));
         var2.add(Integer.toString(var4.getRed()));
         var2.add(Integer.toString(var4.getGreen()));
         var2.add(Integer.toString(var4.getBlue()));
         var1.set("color", var2.toString());
      }
   }

   public interface ParticleData {
      default Vector offsetValues(ParticleDisplay display) {
         return null;
      }

      Object data(ParticleDisplay var1);

      void serialize(ConfigurationSection var1);

      default ParticleDisplay.ParticleData transform(ParticleDisplay display) {
         return this;
      }
   }

   public static class RGBParticleColor implements ParticleDisplay.ParticleData {
      private final Color color;

      public RGBParticleColor(Color var1) {
         this.color = var1;
      }

      public RGBParticleColor(int var1, int var2, int var3) {
         this(new Color(var1, var2, var3));
      }

      public Vector offsetValues(ParticleDisplay var1) {
         if (!ParticleDisplay.ISFLAT || var1.particle == XParticle.ENTITY_EFFECT && var1.particle.isSupported() && var1.particle.get().getDataType() == Void.class) {
            double var2 = this.color.getRed() == 0 ? 1.401298464324817E-45D : (double)this.color.getRed() / 255.0D;
            return new Vector(var2, (double)this.color.getGreen() / 255.0D, (double)this.color.getBlue() / 255.0D);
         } else {
            return null;
         }
      }

      public Object data(ParticleDisplay var1) {
         float var2 = var1.extra == 0.0D ? 1.0F : (float)var1.extra;
         if (var1.particle == XParticle.DUST) {
            return new DustOptions(org.bukkit.Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue()), var2);
         } else if (var1.particle == XParticle.DUST_COLOR_TRANSITION) {
            org.bukkit.Color var3 = org.bukkit.Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
            return new DustTransition(var3, var3, var2);
         } else {
            return ParticleDisplay.SUPPORTS_ALPHA_COLORS ? org.bukkit.Color.fromARGB(this.color.getAlpha(), this.color.getRed(), this.color.getGreen(), this.color.getBlue()) : org.bukkit.Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
         }
      }

      public void serialize(ConfigurationSection var1) {
         StringJoiner var2 = new StringJoiner(", ");
         var2.add(Integer.toString(this.color.getRed()));
         var2.add(Integer.toString(this.color.getGreen()));
         var2.add(Integer.toString(this.color.getBlue()));
         var1.set("color", var2.toString());
      }

      public ParticleDisplay.ParticleData transform(ParticleDisplay var1) {
         return (ParticleDisplay.ParticleData)(var1.particle == XParticle.NOTE ? new ParticleDisplay.NoteParticleColor(ParticleDisplay.findNearestNoteColor(this.color)) : this);
      }
   }

   public static class ParticleBlockData implements ParticleDisplay.ParticleData {
      private final BlockData blockData;

      public ParticleBlockData(BlockData var1) {
         this.blockData = var1;
      }

      public Object data(ParticleDisplay var1) {
         return this.blockData;
      }

      public void serialize(ConfigurationSection var1) {
         var1.set("blockdata", this.blockData.getMaterial().name());
      }
   }

   public static class ParticleItemData implements ParticleDisplay.ParticleData {
      private final ItemStack item;

      public ParticleItemData(ItemStack var1) {
         this.item = var1;
      }

      public Object data(ParticleDisplay var1) {
         return this.item;
      }

      public void serialize(ConfigurationSection var1) {
         var1.set("itemstack", this.item.getType());
      }
   }

   public static class ParticleMaterialData implements ParticleDisplay.ParticleData {
      private final MaterialData materialData;

      public ParticleMaterialData(MaterialData var1) {
         this.materialData = var1;
      }

      public Object data(ParticleDisplay var1) {
         return this.materialData;
      }

      public void serialize(ConfigurationSection var1) {
         var1.set("materialdata", this.materialData.getItemType().name());
      }
   }

   public static class NoteParticleColor implements ParticleDisplay.ParticleData {
      private final int note;

      public NoteParticleColor(int var1) {
         this.note = var1;
      }

      public NoteParticleColor(Note var1) {
         this(var1.getId());
      }

      public Vector offsetValues(ParticleDisplay var1) {
         return new Vector((double)this.note / 24.0D, 0.0D, 0.0D);
      }

      public Object data(ParticleDisplay var1) {
         return null;
      }

      public void serialize(ConfigurationSection var1) {
         var1.set("color", this.note);
      }

      public ParticleDisplay.ParticleData transform(ParticleDisplay var1) {
         return (ParticleDisplay.ParticleData)(var1.particle == XParticle.NOTE ? this : new ParticleDisplay.RGBParticleColor(ParticleDisplay.NOTE_COLORS[this.note]));
      }
   }

   public static class Quaternion {
      public final double w;
      public final double x;
      public final double y;
      public final double z;

      public Quaternion(double var1, double var3, double var5, double var7) {
         this.w = var1;
         this.x = var3;
         this.y = var5;
         this.z = var7;
      }

      public ParticleDisplay.Quaternion copy() {
         return new ParticleDisplay.Quaternion(this.w, this.x, this.y, this.z);
      }

      public static Vector rotate(Vector var0, ParticleDisplay.Quaternion var1) {
         return var1.mul(from(var0)).mul(var1.inverse()).toVector();
      }

      public static Vector rotate(Vector var0, Vector var1, double var2) {
         return rotate(var0, rotation(var2, var1));
      }

      public static ParticleDisplay.Quaternion from(Vector var0) {
         return new ParticleDisplay.Quaternion(0.0D, var0.getX(), var0.getY(), var0.getZ());
      }

      public static ParticleDisplay.Quaternion rotation(double var0, Vector var2) {
         var2 = var2.normalize();
         var0 /= 2.0D;
         double var3 = Math.sin(var0);
         return new ParticleDisplay.Quaternion(Math.cos(var0), var2.getX() * var3, var2.getY() * var3, var2.getZ() * var3);
      }

      public String getInverseString() {
         double var1 = Math.acos(this.w);
         double var3 = Math.toDegrees(var1) * 2.0D;
         double var5 = Math.sin(var1);
         Vector var7 = new Vector(this.x / var5, this.y / var5, this.z / var5);
         return var3 + ", " + var7.getX() + ", " + var7.getY() + ", " + var7.getZ();
      }

      public Vector toVector() {
         return new Vector(this.x, this.y, this.z);
      }

      public ParticleDisplay.Quaternion inverse() {
         double var1 = this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z;
         return new ParticleDisplay.Quaternion(this.w / var1, -this.x / var1, -this.y / var1, -this.z / var1);
      }

      public ParticleDisplay.Quaternion conjugate() {
         return new ParticleDisplay.Quaternion(this.w, -this.x, -this.y, -this.z);
      }

      public ParticleDisplay.Quaternion mul(ParticleDisplay.Quaternion var1) {
         double var2 = var1.w * this.w - var1.x * this.x - var1.y * this.y - var1.z * this.z;
         double var4 = var1.w * this.x + var1.x * this.w + var1.y * this.z - var1.z * this.y;
         double var6 = var1.w * this.y - var1.x * this.z + var1.y * this.w + var1.z * this.x;
         double var8 = var1.w * this.z + var1.x * this.y - var1.y * this.x + var1.z * this.w;
         return new ParticleDisplay.Quaternion(var2, var4, var6, var8);
      }

      public Vector mul(Vector var1) {
         double var2 = this.x * 2.0D;
         double var4 = this.y * 2.0D;
         double var6 = this.z * 2.0D;
         double var8 = this.x * var2;
         double var10 = this.y * var4;
         double var12 = this.z * var6;
         double var14 = this.x * var4;
         double var16 = this.x * var6;
         double var18 = this.y * var6;
         double var20 = this.w * var2;
         double var22 = this.w * var4;
         double var24 = this.w * var6;
         double var26 = (1.0D - (var10 + var12)) * var1.getX() + (var14 - var24) * var1.getY() + (var16 + var22) * var1.getZ();
         double var28 = (var14 + var24) * var1.getX() + (1.0D - (var8 + var12)) * var1.getY() + (var18 - var20) * var1.getZ();
         double var30 = (var16 - var22) * var1.getX() + (var18 + var20) * var1.getY() + (1.0D - (var8 + var10)) * var1.getZ();
         return new Vector(var26, var28, var30);
      }
   }

   public final class CalculationContext {
      private Location location;
      private Vector local;
      private boolean shouldSpawn = true;

      public CalculationContext(Location param2, Vector param3) {
         this.location = var2;
         this.local = var3;
      }

      @Nullable
      public Location getLocation() {
         return this.location;
      }

      @Nullable
      public Vector getLocal() {
         return this.local;
      }

      public void setLocal(Vector var1) {
         this.local = var1;
      }

      public void setLocation(Location var1) {
         this.location = var1;
      }

      public void dontSpawn() {
         this.shouldSpawn = false;
      }

      public ParticleDisplay getDisplay() {
         return ParticleDisplay.this;
      }
   }
}
