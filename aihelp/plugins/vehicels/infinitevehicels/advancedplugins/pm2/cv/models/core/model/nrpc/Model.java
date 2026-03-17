package advancedplugins.pm2.cv.models.core.model.nrpc;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.nrpc.AbstractModel;
import advancedplugins.pm2.cv.models.api.model.nrpc.AbstractModelPart;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.core.ModelAPIImpl;
import advancedplugins.pm2.cv.models.core.model.nrpc.animation.ModelAnimation;
import advancedplugins.pm2.cv.models.core.util.ModelHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Model extends AbstractModel {
   private static final double VIEW_DISTANCE = 48.0D;
   private static final double VIEW_DISTANCE_SQUARED = 2304.0D;
   private Location location = null;
   private ModelAnimation activeAnimation = null;
   private final Map<String, ModelAnimation> animations = new ConcurrentHashMap();
   private final Set<UUID> viewers = ConcurrentHashMap.newKeySet();
   private final Map<String, List<AbstractModelPart>> bonePartMap = new ConcurrentHashMap();
   private BukkitTask viewerDetectionTask;
   private YamlConfiguration config;

   public Model(String var1, BlockbenchModel var2) {
      super(var1, var2);
      File var3 = new File(ModelAPIImpl.PLUGIN.getDataFolder(), "blueprints" + File.separator + "models" + File.separator + var1 + ".yml");
      this.config = YamlConfiguration.loadConfiguration(var3);
   }

   public void spawn() {
      if (this.getLocation() == null) {
         throw new IllegalStateException("You must set the location first!");
      } else {
         ModelAPI.getModelManager().register(this);
         this.setupModel();
         if (!this.validateParts()) {
            ModelAPI.PLUGIN.getLogger().severe("Model " + this.getName() + " has uninitialized parts!");
         } else {
            Iterator var1 = this.location.getWorld().getPlayers().iterator();

            while(var1.hasNext()) {
               Player var2 = (Player)var1.next();
               double var3 = var2.getLocation().distanceSquared(this.location);
               if (var3 <= 2304.0D) {
                  this.spawnForPlayer(var2);
               }
            }

            this.startViewerDetection();
         }
      }
   }

   private boolean validateParts() {
      boolean var1 = true;
      Iterator var2 = this.getParts().entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((AbstractModelPart)var3.getValue()).fakeDisplayEntity() == null) {
            ModelAPI.PLUGIN.getLogger().warning("Part " + String.valueOf(var3.getKey()) + " has null fakeDisplayEntity!");
            var1 = false;
         }
      }

      return var1;
   }

   public void setLocation(Location var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Location cannot be null");
      } else {
         Location var2 = this.location;
         this.location = var1.clone();
         if (var2 != null && !var2.equals(var1)) {
            double var3 = var1.getX() - var2.getX();
            double var5 = var1.getY() - var2.getY();
            double var7 = var1.getZ() - var2.getZ();
            Iterator var9 = this.getParts().values().iterator();

            while(var9.hasNext()) {
               AbstractModelPart var10 = (AbstractModelPart)var9.next();
               if (var10.fakeDisplayEntity() != null) {
                  Vector3f var11 = var10.fakeDisplayEntity().getPosition();
                  var10.fakeDisplayEntity().positionSync((double)var11.x + var3, (double)var11.y + var5, (double)var11.z + var7);
               }
            }

            this.updateViewersAfterMove();
         }

      }
   }

   private void updateViewersAfterMove() {
      if (this.location != null) {
         HashSet var1 = new HashSet();
         Iterator var2 = this.location.getWorld().getPlayers().iterator();

         while(var2.hasNext()) {
            Player var3 = (Player)var2.next();
            double var4 = var3.getLocation().distanceSquared(this.location);
            if (var4 <= 2304.0D) {
               var1.add(var3.getUniqueId());
            }
         }

         var2 = var1.iterator();

         while(var2.hasNext()) {
            UUID var7 = (UUID)var2.next();
            if (!this.viewers.contains(var7)) {
               Player var9 = Bukkit.getPlayer(var7);
               if (var9 != null) {
                  this.spawnForPlayer(var9);
               }
            }
         }

         HashSet var6 = new HashSet();
         Iterator var8 = this.viewers.iterator();

         while(var8.hasNext()) {
            UUID var10 = (UUID)var8.next();
            if (!var1.contains(var10)) {
               Player var5 = Bukkit.getPlayer(var10);
               if (var5 != null) {
                  this.despawnForPlayer(var5);
               }

               var6.add(var10);
            }
         }

         this.viewers.removeAll(var6);
      }
   }

   private void spawnForPlayer(Player var1) {
      if (var1 != null && var1.isOnline()) {
         this.viewers.add(var1.getUniqueId());
         Iterator var2 = this.getParts().values().iterator();

         while(var2.hasNext()) {
            AbstractModelPart var3 = (AbstractModelPart)var2.next();
            if (var3.fakeDisplayEntity() != null) {
               var3.fakeDisplayEntity().spawn(var1);
            } else {
               ModelAPI.PLUGIN.getLogger().warning("Skipping spawn for part " + var3.getName() + " - fakeDisplayEntity is null");
            }
         }

      }
   }

   private void despawnForPlayer(Player var1) {
      if (var1 != null) {
         this.viewers.remove(var1.getUniqueId());
         Iterator var2 = this.getParts().values().iterator();

         while(var2.hasNext()) {
            AbstractModelPart var3 = (AbstractModelPart)var2.next();
            if (var3.fakeDisplayEntity() != null) {
               var3.fakeDisplayEntity().despawn(var1);
            }
         }

      }
   }

   private void startViewerDetection() {
      this.viewerDetectionTask = Bukkit.getScheduler().runTaskTimerAsynchronously(ModelAPI.PLUGIN, () -> {
         if (this.location != null && this.location.getWorld() != null) {
            HashSet var1 = new HashSet(this.viewers);
            HashSet var2 = new HashSet();
            List var3 = this.location.getWorld().getPlayers();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               Player var5 = (Player)var4.next();
               if (var5 != null && var5.isOnline()) {
                  double var6 = var5.getLocation().distanceSquared(this.location);
                  if (var6 <= 2304.0D) {
                     var2.add(var5.getUniqueId());
                  }
               }
            }

            Bukkit.getScheduler().runTask(ModelAPI.PLUGIN, () -> {
               Iterator var3 = var2.iterator();

               UUID var4;
               Player var5;
               while(var3.hasNext()) {
                  var4 = (UUID)var3.next();
                  if (!var1.contains(var4)) {
                     var5 = Bukkit.getPlayer(var4);
                     if (var5 != null && var5.isOnline()) {
                        this.spawnForPlayer(var5);
                     }
                  }
               }

               var3 = var1.iterator();

               while(var3.hasNext()) {
                  var4 = (UUID)var3.next();
                  if (!var2.contains(var4)) {
                     var5 = Bukkit.getPlayer(var4);
                     if (var5 != null) {
                        this.despawnForPlayer(var5);
                     }
                  }
               }

            });
         }
      }, 20L, 20L);
   }

   private void setupModel() {
      Block var1 = this.getLocation().getBlock().getRelative(BlockFace.DOWN);
      ModelHelper.PlacementResult var2 = ModelHelper.calculatePlacement(var1);
      Location var3 = var2.location();
      BlockFace var4 = BlockFace.SOUTH;
      var3.setX((double)var3.getBlockX());
      var3.setY((double)var3.getBlockY());
      var3.setZ((double)var3.getBlockZ());
      float var5 = ModelHelper.getYawFromFacing(var4);
      ModelAPIImpl.PLUGIN.getLogger().info("Rotating model by " + var5 + " degrees");
      Quaternionf var6 = (new Quaternionf()).rotateY((float)Math.toRadians((double)var5));
      ArrayList var7 = new ArrayList(this.getBlockbenchModel().getElements().values());
      this.location = ModelHelper.getCenteredLocation(var3, var4);
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         BlockbenchModel.Element var9 = (BlockbenchModel.Element)var8.next();
         if (var9 instanceof BlockbenchModel.Cube) {
            BlockbenchModel.Cube var10 = (BlockbenchModel.Cube)var9;
            BlockbenchModel.Group var11 = this.findElementParentName(var10.getUuid(), (BlockbenchModel.Group)null);
            String var12 = var11 != null ? var11.getName() : null;
            ModelPart var13 = this.createModelPart(var10, var11, var6);
            var13.spawn();
            if (var13.fakeDisplayEntity() == null) {
               ModelAPI.PLUGIN.getLogger().severe("ModelPart " + var13.getName() + " failed to initialize fakeDisplayEntity even after spawn()!");
            } else {
               this.addPart(var13);
               if (var12 != null) {
                  ((List)this.bonePartMap.computeIfAbsent(var12, (var0) -> {
                     return new ArrayList();
                  })).add(var13);
               }
            }
         } else {
            ModelAPI.PLUGIN.getLogger().warning("Element " + var9.getName() + " is not a cube");
         }
      }

      this.loadAnimations();
   }

   private ModelPart createModelPart(BlockbenchModel.Cube var1, BlockbenchModel.Group var2, Quaternionf var3) {
      Vector3f var4 = new Vector3f(var1.getOrigin()[0] / 16.0F, var1.getOrigin()[1] / 16.0F, var1.getOrigin()[2] / 16.0F);
      Vector3f var5 = new Vector3f(var1.getFrom()[0] / 16.0F - var4.x(), var1.getFrom()[1] / 16.0F - var4.y(), var1.getFrom()[2] / 16.0F - var4.z());
      float var6 = (var1.getTo()[0] - var1.getFrom()[0]) / 16.0F;
      float var7 = (var1.getTo()[1] - var1.getFrom()[1]) / 16.0F;
      float var8 = (var1.getTo()[2] - var1.getFrom()[2]) / 16.0F;
      Quaternionf var9 = ModelHelper.getQuaternionf(var1);
      Vector3f var10 = new Vector3f(var5);
      var9.transform(var10);
      Vector3f var11 = new Vector3f(var4);
      var3.transform(var10);
      var3.transform(var11);
      Vector3f var12 = (new Vector3f(var11)).add(var10);
      Quaternionf var13 = (new Quaternionf(var3)).mul(var9);
      Transformation var14 = new Transformation(var12, var13, new Vector3f(var6, var7, var8), new Quaternionf());
      Material var15 = var1.getName().equalsIgnoreCase("hitbox") ? Material.AIR : Material.DIAMOND_BLOCK;
      byte var16 = 0;
      if (var2 != null) {
         String var17 = "parts." + var2.getName();
         String var18 = var17 + "#" + (new LinkedList(var2.getElement())).indexOf(var1.getUuid());
         String var19 = this.config.getString(var17 + ".material");
         String var20 = this.config.getString(var18 + ".material");
         if (var19 != null && Material.matchMaterial(var19) != null) {
            var15 = Material.matchMaterial(var19);
         }

         if (var20 != null && Material.matchMaterial(var20) != null) {
            var15 = Material.matchMaterial(var20);
         }
      }

      ModelPart var21 = new ModelPart(var1.getUuid(), var1.getName(), this, var14, (Material)Objects.requireNonNull(var15), var1, var12, var2, var4, var10);
      var21.setTint(var16);
      return var21;
   }

   private BlockbenchModel.Group findElementParentName(UUID var1, BlockbenchModel.Group var2) {
      Iterator var3;
      BlockbenchModel.Group var4;
      BlockbenchModel.Group var5;
      if (var2 == null) {
         var3 = this.getBlockbenchModel().getOutliner().values().iterator();

         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (BlockbenchModel.Group)var3.next();
            var5 = this.findElementParentName(var1, var4);
         } while(var5 == null);

         return var5;
      } else if (var2.getElement().contains(var1)) {
         return var2;
      } else {
         var3 = var2.getChildGroup().values().iterator();

         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (BlockbenchModel.Group)var3.next();
            var5 = this.findElementParentName(var1, var4);
         } while(var5 == null);

         return var5;
      }
   }

   private void loadAnimations() {
      Iterator var1 = this.getBlockbenchModel().getAnimations().values().iterator();

      while(var1.hasNext()) {
         BlockbenchModel.Animation var2 = (BlockbenchModel.Animation)var1.next();
         ModelAnimation var3 = new ModelAnimation(this, var2.getName(), var2);
         this.animations.put(var2.getName(), var3);
      }

   }

   public boolean playAnimation(String var1) {
      if (this.activeAnimation != null) {
         this.activeAnimation.stop();
         this.activeAnimation = null;
      }

      ModelAnimation var2 = (ModelAnimation)this.animations.get(var1);
      if (var2 == null) {
         ModelAPI.PLUGIN.getLogger().warning("Animation not found: " + var1);
         return false;
      } else {
         this.activeAnimation = var2;
         this.activeAnimation.start();
         return true;
      }
   }

   public void stopAnimation() {
      if (this.activeAnimation != null) {
         this.activeAnimation.stop();
         this.activeAnimation = null;
      }

   }

   public boolean isAnimationPlaying() {
      return this.activeAnimation != null;
   }

   @Nullable
   public String getCurrentAnimationName() {
      return this.activeAnimation != null ? this.activeAnimation.getAnimationName() : null;
   }

   public void destroy() {
      if (this.viewerDetectionTask != null && !this.viewerDetectionTask.isCancelled()) {
         this.viewerDetectionTask.cancel();
         this.viewerDetectionTask = null;
      }

      if (this.activeAnimation != null) {
         this.activeAnimation.stop();
         this.activeAnimation = null;
      }

      ModelAPI.getModelManager().remove(this.getName());
      HashSet var1 = new HashSet(this.viewers);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         UUID var3 = (UUID)var2.next();
         Player var4 = Bukkit.getPlayer(var3);
         if (var4 != null) {
            this.despawnForPlayer(var4);
         }
      }

      this.getParts().values().forEach((var0) -> {
         if (var0.fakeDisplayEntity() != null) {
            var0.fakeDisplayEntity().destroy();
         }

      });
      this.viewers.clear();
      this.animations.clear();
      this.bonePartMap.clear();
      this.getParts().clear();
   }

   public List<AbstractModelPart> getPartsFromParent(String var1) {
      return (List)(var1 == null ? new ArrayList() : (List)this.bonePartMap.getOrDefault(var1, new ArrayList()));
   }

   public Set<UUID> getViewers() {
      return new HashSet(this.viewers);
   }

   public boolean isViewedBy(Player var1) {
      return this.viewers.contains(var1.getUniqueId());
   }

   public double getViewDistance() {
      return 48.0D;
   }

   @Generated
   public Location getLocation() {
      return this.location;
   }

   @Generated
   public ModelAnimation getActiveAnimation() {
      return this.activeAnimation;
   }

   @Generated
   public Map<String, ModelAnimation> getAnimations() {
      return this.animations;
   }

   @Generated
   public Map<String, List<AbstractModelPart>> getBonePartMap() {
      return this.bonePartMap;
   }

   @Generated
   public BukkitTask getViewerDetectionTask() {
      return this.viewerDetectionTask;
   }

   @Generated
   public YamlConfiguration getConfig() {
      return this.config;
   }

   @Generated
   public void setActiveAnimation(ModelAnimation var1) {
      this.activeAnimation = var1;
   }

   @Generated
   public void setViewerDetectionTask(BukkitTask var1) {
      this.viewerDetectionTask = var1;
   }

   @Generated
   public void setConfig(YamlConfiguration var1) {
      this.config = var1;
   }
}
