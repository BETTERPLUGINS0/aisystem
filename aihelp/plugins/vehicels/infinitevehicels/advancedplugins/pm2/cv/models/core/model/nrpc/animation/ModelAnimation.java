package advancedplugins.pm2.cv.models.core.model.nrpc.animation;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.nrpc.AbstractModelPart;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.core.model.nrpc.Model;
import advancedplugins.pm2.cv.models.core.model.nrpc.ModelPart;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelAnimation {
   private static final ExecutorService ANIMATION_EXECUTOR = Executors.newWorkStealingPool(4);
   private final Model model;
   private final String animationName;
   private final BlockbenchModel.Animation animation;
   private final Map<String, ModelAnimation.BoneAnimationData> boneAnimations = new ConcurrentHashMap();
   private final Map<AbstractModelPart, ModelAnimation.TransformationCache> transformCache = new ConcurrentHashMap();
   private double currentTime = 0.0D;
   private final double animationLength;
   private final ModelAnimation.AnimationLoopMode loopMode;
   private final boolean override;
   private final double speed = 1.0D;
   private ModelAnimation.AnimationPhase phase;
   private final Set<UUID> dirtyViewers;
   private long lastUpdateTime;
   private BukkitTask animationTask;
   private final double DEBUG_TIME;

   public ModelAnimation(Model var1, String var2, BlockbenchModel.Animation var3) {
      this.phase = ModelAnimation.AnimationPhase.PLAY;
      this.dirtyViewers = ConcurrentHashMap.newKeySet();
      this.lastUpdateTime = System.currentTimeMillis();
      this.DEBUG_TIME = 0.002D;
      this.model = var1;
      this.animationName = var2;
      this.animation = var3;
      this.animationLength = (double)var3.getLength();
      this.loopMode = ModelAnimation.AnimationLoopMode.fromString(var3.getLoop());
      this.override = var3.getOverride() != null ? var3.getOverride() : false;
      this.initialize();
   }

   private void initialize() {
      Iterator var1 = this.animation.getAnimators().values().iterator();

      while(var1.hasNext()) {
         BlockbenchModel.Animator var2 = (BlockbenchModel.Animator)var1.next();
         ModelAnimation.BoneAnimationData var3 = new ModelAnimation.BoneAnimationData(var2.getName());
         var2.getChannels().forEach((var2x, var3x) -> {
            ModelAnimation.AnimationChannel var4 = this.createChannel(var2x, var3x);
            var3.channels.put(var2x.toLowerCase(), var4);
         });
         this.boneAnimations.put(var2.getName(), var3);
      }

   }

   public static void shutdownExecutor() {
      ANIMATION_EXECUTOR.shutdown();

      try {
         if (!ANIMATION_EXECUTOR.awaitTermination(5L, TimeUnit.SECONDS)) {
            ANIMATION_EXECUTOR.shutdownNow();
         }
      } catch (InterruptedException var1) {
         ANIMATION_EXECUTOR.shutdownNow();
      }

   }

   private ModelAnimation.AnimationChannel createChannel(String var1, Map<Float, BlockbenchModel.Keyframe> var2) {
      ModelAnimation.AnimationChannel var3 = new ModelAnimation.AnimationChannel(var1);
      TreeMap var4 = new TreeMap();
      var2.forEach((var1x, var2x) -> {
         ModelAnimation.AnimationKeyframe var3 = new ModelAnimation.AnimationKeyframe(var1x);
         if (!var2x.getData_points().isEmpty()) {
            Map var4x = (Map)var2x.getData_points().getFirst();
            var3.value = new Vector3f(Float.parseFloat((String)var4x.get("x")), Float.parseFloat((String)var4x.get("y")), Float.parseFloat((String)var4x.get("z")));
         }

         var3.interpolation = var2x.getInterpolation();
         if ("bezier".equals(var3.interpolation)) {
            var3.bezierLeftTime = var2x.getBezier_left_time();
            var3.bezierLeftValue = var2x.getBezier_left_value();
            var3.bezierRightTime = var2x.getBezier_right_time();
            var3.bezierRightValue = var2x.getBezier_right_value();
         }

         var4.put(var1x, var3);
      });
      var3.keyframes = var4;
      return var3;
   }

   public void start() {
      if (this.animationTask != null) {
         this.animationTask.cancel();
      }

      this.currentTime = 0.0D;
      this.phase = ModelAnimation.AnimationPhase.PLAY;
      this.animationTask = Bukkit.getScheduler().runTaskTimer(ModelAPI.PLUGIN, this::tick, 0L, 1L);
   }

   public void stop() {
      if (this.animationTask != null) {
         this.animationTask.cancel();
         this.animationTask = null;
      }

      Iterator var1 = this.model.getParts().values().iterator();

      while(var1.hasNext()) {
         AbstractModelPart var2 = (AbstractModelPart)var1.next();
         if (var2 instanceof ModelPart) {
            ModelPart var3 = (ModelPart)var2;
            var3.setCurrentTransformation(var3.getDefaultTransformation());
         }
      }

   }

   private void tick() {
      long var1 = System.currentTimeMillis();
      double var3 = (double)(var1 - this.lastUpdateTime) / 1000.0D;
      this.lastUpdateTime = var1;
      this.updateTime(var3);
      CompletableFuture.supplyAsync(this::calculateTransformations, ANIMATION_EXECUTOR).thenAccept((var1x) -> {
         Bukkit.getScheduler().runTask(ModelAPI.PLUGIN, () -> {
            this.applyTransformations(var1x);
         });
      });
   }

   private void updateTime(double var1) {
      this.currentTime += var1 * 1.0D;
      switch(this.loopMode.ordinal()) {
      case 0:
         if (this.currentTime >= this.animationLength) {
            this.currentTime = this.animationLength;
            this.stop();
         }
         break;
      case 1:
         if (this.currentTime >= this.animationLength) {
            this.currentTime = this.animationLength;
         }
         break;
      case 2:
         if (this.currentTime >= this.animationLength) {
            this.currentTime %= this.animationLength;
         }
      }

   }

   private Map<AbstractModelPart, Transformation> calculateTransformations() {
      HashMap var1 = new HashMap();
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap(this.model.getBlockbenchModel().getFlatOutlinerByName());
      HashSet var4 = new HashSet();
      Iterator var5 = this.boneAnimations.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if (!var4.contains(var6)) {
            this.calculateBoneTransformRecursive(var6, (new Matrix4f()).identity(), var3, var2, var4);
         }
      }

      var5 = this.model.getParts().values().iterator();

      while(var5.hasNext()) {
         AbstractModelPart var21 = (AbstractModelPart)var5.next();
         if (var21 instanceof ModelPart) {
            ModelPart var7 = (ModelPart)var21;
            String var8 = var21.getParentBoneName();
            if (var8 != null && var2.containsKey(var8)) {
               Matrix4f var9 = (Matrix4f)var2.get(var8);
               BlockbenchModel.Group var10 = (BlockbenchModel.Group)var3.get(var8);
               Transformation var11 = var7.getDefaultTransformation();
               Vector3f var12 = new Vector3f(var10.getOrigin()[0] / 16.0F, var10.getOrigin()[1] / 16.0F, var10.getOrigin()[2] / 16.0F);
               Quaternionf var13 = (new Quaternionf()).rotateY((float)Math.toRadians(180.0D));
               var13.transform(var12);
               Vector3f var14 = new Vector3f(var7.getPositionOffset());
               var14.sub(var12);
               var9.transformPosition(var14);
               var14.add(var12);
               Quaternionf var15 = new Quaternionf();
               var9.getNormalizedRotation(var15);
               Quaternionf var16 = (new Quaternionf(var15)).mul(var11.getLeftRotation());
               ModelAnimation.BoneAnimationData var17 = (ModelAnimation.BoneAnimationData)this.boneAnimations.get(var8);
               Vector3f var18 = null;
               if (var17 != null) {
                  var18 = this.interpolateChannel((ModelAnimation.AnimationChannel)var17.channels.get("scale"), this.currentTime);
               }

               Vector3f var19 = new Vector3f(var11.getScale());
               if (var18 != null) {
                  var19.mul(var18);
               }

               Transformation var20 = new Transformation(var14, var16, var19, var11.getRightRotation());
               var1.put(var21, var20);
            }
         }
      }

      return var1;
   }

   private void calculateBoneTransformRecursive(String var1, Matrix4f var2, Map<String, BlockbenchModel.Group> var3, Map<String, Matrix4f> var4, Set<String> var5) {
      if (!var5.contains(var1)) {
         var5.add(var1);
         BlockbenchModel.Group var6 = (BlockbenchModel.Group)var3.get(var1);
         ModelAnimation.BoneAnimationData var7 = (ModelAnimation.BoneAnimationData)this.boneAnimations.get(var1);
         Matrix4f var8 = (new Matrix4f()).identity();
         if (var6 != null) {
            Float[] var9 = var6.getRotation();
            if (var9 == null) {
               var9 = new Float[]{0.0F, 0.0F, 0.0F};
            }

            Quaternionf var10 = (new Quaternionf()).rotateXYZ((float)Math.toRadians((double)var9[0]), (float)Math.toRadians((double)var9[1]), (float)Math.toRadians((double)var9[2]));
            Quaternionf var11 = new Quaternionf();
            Vector3f var12 = new Vector3f();
            if (var7 != null) {
               Vector3f var13 = this.interpolateChannel((ModelAnimation.AnimationChannel)var7.channels.get("position"), this.currentTime);
               Vector3f var14 = this.interpolateChannel((ModelAnimation.AnimationChannel)var7.channels.get("rotation"), this.currentTime);
               if (var14 != null) {
                  var11.rotateZYX((float)Math.toRadians((double)var14.z), (float)Math.toRadians((double)var14.y), (float)Math.toRadians((double)var14.x));
               }

               if (var13 != null) {
                  var12.add(var13.x / 16.0F, var13.y / 16.0F, var13.z / 16.0F);
                  (new Quaternionf()).rotateY((float)Math.toRadians(180.0D)).transform(var12);
               }
            }

            Quaternionf var18 = (new Quaternionf(var10)).mul(var11);
            var8.identity().translate(var12).rotate(var18);
         }

         Matrix4f var15 = (new Matrix4f(var2)).mul(var8);
         var4.put(var1, var15);
         if (var6 != null) {
            Iterator var16 = var6.getChildGroup().values().iterator();

            while(var16.hasNext()) {
               BlockbenchModel.Group var17 = (BlockbenchModel.Group)var16.next();
               this.calculateBoneTransformRecursive(var17.getName(), var15, var3, var4, var5);
            }
         }

      }
   }

   private Vector3f getParentTransform(BlockbenchModel.Group var1, Map<String, Matrix4f> var2) {
      if (var1.getParentGroup() == null) {
         return new Vector3f();
      } else {
         BlockbenchModel.Group var3 = this.model.getBlockbenchModel().getGroup(var1.getParentGroup());
         Vector3f var4 = new Vector3f(var3.getOrigin()[0] / 16.0F, var3.getOrigin()[1] / 16.0F, var3.getOrigin()[2] / 16.0F);
         Vector3f var5 = this.getParentTransform(var3, var2);
         return (new Vector3f(var5)).mul(var4);
      }
   }

   private BlockbenchModel.Group findGroupByName(String var1) {
      Iterator var2 = this.model.getBlockbenchModel().getOutliner().values().iterator();

      BlockbenchModel.Group var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         BlockbenchModel.Group var3 = (BlockbenchModel.Group)var2.next();
         var4 = this.findGroupByNameRecursive(var3, var1);
      } while(var4 == null);

      return var4;
   }

   private BlockbenchModel.Group findGroupByNameRecursive(BlockbenchModel.Group var1, String var2) {
      if (var1.getName().equals(var2)) {
         return var1;
      } else {
         Iterator var3 = var1.getChildGroup().values().iterator();

         BlockbenchModel.Group var5;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            BlockbenchModel.Group var4 = (BlockbenchModel.Group)var3.next();
            var5 = this.findGroupByNameRecursive(var4, var2);
         } while(var5 == null);

         return var5;
      }
   }

   private Vector3f interpolateChannel(ModelAnimation.AnimationChannel var1, double var2) {
      if (var1 != null && !var1.keyframes.isEmpty()) {
         Entry var4 = var1.keyframes.floorEntry((float)var2);
         Entry var5 = var1.keyframes.ceilingEntry((float)var2);
         if (var4 == null) {
            return var5 != null ? ((ModelAnimation.AnimationKeyframe)var5.getValue()).value : null;
         } else if (var5 != null && !((Float)var4.getKey()).equals(var5.getKey())) {
            ModelAnimation.AnimationKeyframe var6 = (ModelAnimation.AnimationKeyframe)var4.getValue();
            ModelAnimation.AnimationKeyframe var7 = (ModelAnimation.AnimationKeyframe)var5.getValue();
            float var8 = (Float)var4.getKey();
            float var9 = (Float)var5.getKey();
            float var10 = (float)((var2 - (double)var8) / (double)(var9 - var8));
            String var11 = var6.interpolation.toLowerCase();
            byte var12 = -1;
            switch(var11.hashCode()) {
            case -1392296225:
               if (var11.equals("bezier")) {
                  var12 = 2;
               }
               break;
            case -1102672091:
               if (var11.equals("linear")) {
                  var12 = 0;
               }
               break;
            case 3540684:
               if (var11.equals("step")) {
                  var12 = 3;
               }
               break;
            case 204479250:
               if (var11.equals("catmullrom")) {
                  var12 = 1;
               }
            }

            switch(var12) {
            case 0:
               return var6.value.lerp(var7.value, var10, new Vector3f());
            case 1:
               Entry var13 = var1.keyframes.lowerEntry(var8);
               Entry var14 = var1.keyframes.higherEntry(var9);
               Vector3f var15 = var13 != null ? ((ModelAnimation.AnimationKeyframe)var13.getValue()).value : var6.value;
               Vector3f var16 = var6.value;
               Vector3f var17 = var7.value;
               Vector3f var18 = var14 != null ? ((ModelAnimation.AnimationKeyframe)var14.getValue()).value : var7.value;
               return MathUtils.smoothLerp(var15, var16, var17, var18, var10);
            case 2:
               return this.interpolateBezier(var6, var7, var10);
            case 3:
               return var6.value;
            default:
               return var6.value.lerp(var7.value, var10, new Vector3f());
            }
         } else {
            return ((ModelAnimation.AnimationKeyframe)var4.getValue()).value;
         }
      } else {
         return null;
      }
   }

   private Vector3f interpolateBezier(ModelAnimation.AnimationKeyframe var1, ModelAnimation.AnimationKeyframe var2, float var3) {
      return var1.value.lerp(var2.value, var3, new Vector3f());
   }

   private void applyTransformations(Map<AbstractModelPart, Transformation> var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.entrySet().iterator();

      while(true) {
         AbstractModelPart var5;
         Transformation var6;
         ModelPart var7;
         ModelAnimation.TransformationCache var8;
         do {
            do {
               if (!var3.hasNext()) {
                  this.sendBatchedUpdates(var2);
                  return;
               }

               Entry var4 = (Entry)var3.next();
               var5 = (AbstractModelPart)var4.getKey();
               var6 = (Transformation)var4.getValue();
            } while(!(var5 instanceof ModelPart));

            var7 = (ModelPart)var5;
            var8 = (ModelAnimation.TransformationCache)this.transformCache.computeIfAbsent(var5, (var0) -> {
               return new ModelAnimation.TransformationCache();
            });
         } while(!var8.hasChanged(var6));

         var7.setCurrentTransformation(var6);
         var8.update(var6);
         Iterator var9 = var5.fakeDisplayEntity().getViewers().iterator();

         while(var9.hasNext()) {
            UUID var10 = (UUID)var9.next();
            ((List)var2.computeIfAbsent(var10, (var0) -> {
               return new ArrayList();
            })).add(var5);
         }
      }
   }

   private void sendBatchedUpdates(Map<UUID, List<AbstractModelPart>> var1) {
      var1.forEach((var1x, var2) -> {
         Player var3 = Bukkit.getPlayer(var1x);
         if (var3 != null && var3.isOnline()) {
            double var4 = var3.getLocation().distanceSquared(this.model.getLocation());
            double var6 = 2304.0D;
            if (var4 <= var6) {
               Iterator var8 = var2.iterator();

               while(var8.hasNext()) {
                  AbstractModelPart var9 = (AbstractModelPart)var8.next();
                  var9.fakeDisplayEntity().packAndSend(var3);
               }
            }
         }

      });
   }

   @Generated
   public Model getModel() {
      return this.model;
   }

   @Generated
   public String getAnimationName() {
      return this.animationName;
   }

   @Generated
   public BlockbenchModel.Animation getAnimation() {
      return this.animation;
   }

   @Generated
   public Map<String, ModelAnimation.BoneAnimationData> getBoneAnimations() {
      return this.boneAnimations;
   }

   @Generated
   public Map<AbstractModelPart, ModelAnimation.TransformationCache> getTransformCache() {
      return this.transformCache;
   }

   @Generated
   public double getCurrentTime() {
      return this.currentTime;
   }

   @Generated
   public double getAnimationLength() {
      return this.animationLength;
   }

   @Generated
   public ModelAnimation.AnimationLoopMode getLoopMode() {
      return this.loopMode;
   }

   @Generated
   public boolean isOverride() {
      return this.override;
   }

   @Generated
   public double getSpeed() {
      Objects.requireNonNull(this);
      return 1.0D;
   }

   @Generated
   public ModelAnimation.AnimationPhase getPhase() {
      return this.phase;
   }

   @Generated
   public Set<UUID> getDirtyViewers() {
      return this.dirtyViewers;
   }

   @Generated
   public long getLastUpdateTime() {
      return this.lastUpdateTime;
   }

   @Generated
   public BukkitTask getAnimationTask() {
      return this.animationTask;
   }

   @Generated
   public double getDEBUG_TIME() {
      Objects.requireNonNull(this);
      return 0.002D;
   }

   public static enum AnimationPhase {
      LERPIN,
      PLAY,
      LERPOUT;

      // $FF: synthetic method
      private static ModelAnimation.AnimationPhase[] $values() {
         return new ModelAnimation.AnimationPhase[]{LERPIN, PLAY, LERPOUT};
      }
   }

   public static enum AnimationLoopMode {
      ONCE,
      HOLD,
      LOOP;

      public static ModelAnimation.AnimationLoopMode fromString(String var0) {
         if (var0 == null) {
            return ONCE;
         } else {
            String var1 = var0.toLowerCase();
            byte var2 = -1;
            switch(var1.hashCode()) {
            case 3208383:
               if (var1.equals("hold")) {
                  var2 = 1;
               }
               break;
            case 3327652:
               if (var1.equals("loop")) {
                  var2 = 0;
               }
            }

            ModelAnimation.AnimationLoopMode var10000;
            switch(var2) {
            case 0:
               var10000 = LOOP;
               break;
            case 1:
               var10000 = HOLD;
               break;
            default:
               var10000 = ONCE;
            }

            return var10000;
         }
      }

      // $FF: synthetic method
      private static ModelAnimation.AnimationLoopMode[] $values() {
         return new ModelAnimation.AnimationLoopMode[]{ONCE, HOLD, LOOP};
      }
   }

   private static class BoneAnimationData {
      private final String boneName;
      private final Map<String, ModelAnimation.AnimationChannel> channels = new HashMap();

      public BoneAnimationData(String var1) {
         this.boneName = var1;
      }

      @Generated
      public String getBoneName() {
         return this.boneName;
      }

      @Generated
      public Map<String, ModelAnimation.AnimationChannel> getChannels() {
         return this.channels;
      }
   }

   private static class AnimationChannel {
      private final String type;
      private TreeMap<Float, ModelAnimation.AnimationKeyframe> keyframes;

      public AnimationChannel(String var1) {
         this.type = var1;
      }

      @Generated
      public String getType() {
         return this.type;
      }

      @Generated
      public TreeMap<Float, ModelAnimation.AnimationKeyframe> getKeyframes() {
         return this.keyframes;
      }
   }

   private static class AnimationKeyframe {
      private final float time;
      private Vector3f value;
      private String interpolation = "linear";
      private Float[] bezierLeftTime;
      private Float[] bezierLeftValue;
      private Float[] bezierRightTime;
      private Float[] bezierRightValue;

      public AnimationKeyframe(float var1) {
         this.time = var1;
      }

      @Generated
      public float getTime() {
         return this.time;
      }

      @Generated
      public Vector3f getValue() {
         return this.value;
      }

      @Generated
      public String getInterpolation() {
         return this.interpolation;
      }

      @Generated
      public Float[] getBezierLeftTime() {
         return this.bezierLeftTime;
      }

      @Generated
      public Float[] getBezierLeftValue() {
         return this.bezierLeftValue;
      }

      @Generated
      public Float[] getBezierRightTime() {
         return this.bezierRightTime;
      }

      @Generated
      public Float[] getBezierRightValue() {
         return this.bezierRightValue;
      }
   }

   private static class TransformationCache {
      private Transformation lastTransformation;
      private long lastUpdate;

      public boolean hasChanged(Transformation var1) {
         if (this.lastTransformation == null) {
            return true;
         } else {
            boolean var2 = !this.lastTransformation.getTranslation().equals(var1.getTranslation(), 0.001F);
            boolean var3 = !this.lastTransformation.getLeftRotation().equals(var1.getLeftRotation(), 0.001F);
            boolean var4 = !this.lastTransformation.getScale().equals(var1.getScale(), 0.001F);
            return var2 || var3 || var4;
         }
      }

      public void update(Transformation var1) {
         this.lastTransformation = new Transformation(new Vector3f(var1.getTranslation()), new Quaternionf(var1.getLeftRotation()), new Vector3f(var1.getScale()), new Quaternionf(var1.getRightRotation()));
         this.lastUpdate = System.currentTimeMillis();
      }
   }
}
