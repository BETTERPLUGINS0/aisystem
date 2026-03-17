package advancedplugins.pm2.cv.models.core.model.rpc.generator.parser.blockbench;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.Timeline;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data.KeyframeReaderArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.ScriptKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.VectorKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorCollector;
import advancedplugins.pm2.cv.models.api.model.rpc.error.WarnBadTexture;
import advancedplugins.pm2.cv.models.api.model.rpc.error.WarningDuplicateJointName;
import advancedplugins.pm2.cv.models.api.model.rpc.events.RegisterBehaviorParserEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.BlueprintTexture;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ModelAssets;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.ModelParser;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchBehaviorParser;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchDeserializer;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.utils.FileUtils;
import advancedplugins.pm2.cv.models.api.utils.Utils;
import advancedplugins.pm2.cv.models.api.utils.data.ResourceLocation;
import advancedplugins.pm2.cv.models.api.utils.math.Direction;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.ModelGeneratorImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.parser.blockbench.json.DefaultBehaviorParser;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.parser.blockbench.json.MCMetaDeserializer;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unimi.dsi.fastutil.Pair;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class BlockbenchParser implements ModelParser {
   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(BlockbenchModel.class, new BlockbenchDeserializer()).registerTypeAdapter(BlueprintTexture.MCMeta.class, new MCMetaDeserializer()).create();
   private final Set<BlockbenchBehaviorParser> behaviorParsers = new LinkedHashSet();
   private final ModelGeneratorImpl generator;

   public BlockbenchParser(ModelGeneratorImpl var1) {
      this.generator = var1;
      this.behaviorParsers.add(new DefaultBehaviorParser());
      ModelAPI.callEvent(new RegisterBehaviorParserEvent(this.behaviorParsers));
   }

   private static Vector3f vector3f(@Nullable Float[] var0) {
      return var0 == null ? new Vector3f(0.0F) : new Vector3f(var0[0] == null ? 0.0F : var0[0], var0[1] == null ? 0.0F : var0[1], var0[2] == null ? 0.0F : var0[2]);
   }

   private static Vector3d vector3d(@Nullable Float[] var0) {
      return var0 == null ? new Vector3d(0.0D) : new Vector3d(var0[0] == null ? 0.0D : (double)var0[0], var0[1] == null ? 0.0D : (double)var0[1], var0[2] == null ? 0.0D : (double)var0[2]);
   }

   private static float[] unwrap(Float[] var0) {
      float[] var1 = new float[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   private static void putVectorKeyframes(BlockbenchModel.Animator var0, String var1, Timeline var2, KeyframeType<VectorKeyframe, Vector3f> var3, float var4, float var5, float var6) {
      Map var7 = (Map)var0.getChannels().get(var1);
      if (var7 != null) {
         Iterator var8 = var7.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            Float var10 = (Float)var9.getKey();
            BlockbenchModel.Keyframe var11 = (BlockbenchModel.Keyframe)var9.getValue();
            VectorKeyframe var12 = (VectorKeyframe)var2.getKeyframe(var10, var3);
            if (!var11.getData_points().isEmpty()) {
               KeyframeReaderArchive var13 = ModelAPI.getAPI().getKeyframeReaderArchive();
               Map var14 = (Map)var11.getData_points().get(0);
               var12.setXFactor(var4).setYFactor(var5).setZFactor(var6).setX(var13.tryParse((String)var14.getOrDefault("x", "0"))).setY(var13.tryParse((String)var14.getOrDefault("y", "0"))).setZ(var13.tryParse((String)var14.getOrDefault("z", "0")));
               if (var11.getData_points().size() >= 2) {
                  Map var15 = (Map)var11.getData_points().get(1);
                  var12.setDiscontinuous(true);
                  var12.setPostX(var13.tryParse((String)var15.getOrDefault("x", "0"))).setPostY(var13.tryParse((String)var15.getOrDefault("y", "0"))).setPostZ(var13.tryParse((String)var15.getOrDefault("z", "0")));
               }
            }

            var12.setInterpolation(var11.getInterpolation());
            if (var12.isBezier()) {
               var12.setBezierLeftTime(var11.getBezier_left_time()[0], var11.getBezier_left_time()[1], var11.getBezier_left_time()[2]);
               var12.setBezierLeftValue(var11.getBezier_left_value()[0], var11.getBezier_left_value()[1], var11.getBezier_left_value()[2]);
               var12.setBezierRightTime(var11.getBezier_right_time()[0], var11.getBezier_right_time()[1], var11.getBezier_right_time()[2]);
               var12.setBezierRightValue(var11.getBezier_right_value()[0], var11.getBezier_right_value()[1], var11.getBezier_right_value()[2]);
            }
         }
      }

   }

   private static ProcessedJoint.Face getProcFace(BlockbenchModel.Face var0) {
      Float[] var1 = var0.getUv();
      ProcessedJoint.UV var2 = new ProcessedJoint.UV(var1[0] == null ? 0.0F : var1[0], var1[1] == null ? 0.0F : var1[1], var1[2] == null ? 0.0F : var1[2], var1[3] == null ? 0.0F : var1[3], var0.getRotation() == null ? 0 : var0.getRotation());
      return new ProcessedJoint.Face(var2, var0.getTexture());
   }

   public boolean validateFile(File var1) {
      return FileUtils.isExtension(var1.getName(), "bbmodel");
   }

   public Pair<ModelBlueprint, ModelAssets> generate(File var1, ErrorCollector var2) {
      String var3 = FileUtils.removeExtension(var1.getName()).toLowerCase(Locale.ENGLISH);
      FileReader var4 = new FileReader(var1);
      BlockbenchModel var5 = (BlockbenchModel)this.gson.fromJson(var4, BlockbenchModel.class);
      ModelBlueprint var6 = new ModelBlueprint();
      var6.setBlockbenchModel(var5);
      var6.setName(var3);
      this.populateBlueprint(var5, var6, var2);
      var6.constructFlatJointMap(var2);
      var6.cacheJointActions(var2);
      ModelAssets var7 = new ModelAssets();
      var7.setName(var3);
      this.populateAssets(var5, var6, var7, var2);
      var4.close();
      return Pair.of(var6, var7);
   }

   private void populateBlueprint(BlockbenchModel var1, ModelBlueprint var2, ErrorCollector var3) {
      this.behaviorParsers.forEach((var3x) -> {
         var3x.processModel(var3, var1, var2);
      });
      ConcurrentHashMap var4 = new ConcurrentHashMap();
      var1.getOutliner().forEach((var5x, var6x) -> {
         BlueprintJoint var7 = this.readJoint(var3, var1, (BlueprintJoint)null, var6x, var4);
         if (var2.getJoints().containsKey(var7.getName())) {
            (new WarningDuplicateJointName(var7.getName(), var7.getUuid())).log(var3);
            var7.setName(var5x.toString());
         }

         var2.getJoints().put(var7.getName(), var7);
         var4.put(var7.getUuid(), var7);
      });
      Iterator var5 = var1.getAnimations().entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         String var7 = (String)var6.getKey();
         BlockbenchModel.Animation var8 = (BlockbenchModel.Animation)var6.getValue();
         BlueprintAnimation var9 = new BlueprintAnimation(var2, var7);
         if (var8.getEffects() != null) {
            Map var10 = var8.getEffects().getChannels();
            Map var11 = (Map)var10.get("timeline");
            if (var11 != null) {
               Iterator var12 = var11.entrySet().iterator();

               while(var12.hasNext()) {
                  Entry var13 = (Entry)var12.next();
                  BlockbenchModel.Keyframe var14 = (BlockbenchModel.Keyframe)var13.getValue();
                  ScriptKeyframe var15 = (ScriptKeyframe)var9.getGlobalTimeline().getKeyframe((Float)var13.getKey(), KeyframeTypes.SCRIPT);
                  Iterator var16 = var14.getData_points().iterator();

                  while(var16.hasNext()) {
                     Map var17 = (Map)var16.next();
                     String var18 = (String)var17.getOrDefault("script", "");
                     String[] var19 = var18.split("\n");
                     String[] var20 = var19;
                     int var21 = var19.length;

                     for(int var22 = 0; var22 < var21; ++var22) {
                        String var23 = var20[var22];
                        var15.getScript().add(ScriptKeyframe.Script.from(var23));
                     }
                  }
               }
            }
         }

         Iterator var29 = var8.getAnimators().entrySet().iterator();

         while(var29.hasNext()) {
            Entry var31 = (Entry)var29.next();
            BlockbenchModel.Animator var32 = (BlockbenchModel.Animator)var31.getValue();
            Timeline var33 = new Timeline(var9, var32.getRotationGlobal() != null && var32.getRotationGlobal());
            putVectorKeyframes(var32, "position", var33, KeyframeTypes.POSITION, -0.0625F, 0.0625F, 0.0625F);
            putVectorKeyframes(var32, "rotation", var33, KeyframeTypes.ROTATION, -0.017453292F, -0.017453292F, 0.017453292F);
            putVectorKeyframes(var32, "scale", var33, KeyframeTypes.SCALE, 1.0F, 1.0F, 1.0F);
            UUID var34 = var32.getUuid();
            if (var34 != null) {
               var9.getTimelines().put(var34, var33);
            }
         }

         var9.setLength((double)var8.getLength());
         var9.setLoopMode(BlueprintAnimation.LoopMode.get(var8.getLoop()));
         var9.setOverride(var8.getOverride());
         var2.getAnimations().put(var7, var9);
      }

      String[] var24 = var1.getAnimationVariablePlaceholders().split("\n");
      String[] var25 = var24;
      int var26 = var24.length;

      for(int var27 = 0; var27 < var26; ++var27) {
         String var28 = var25[var27];
         String[] var30 = var28.split("=", 2);
         if (var30.length >= 2) {
            var2.getAnimationsPlaceholders().put(var30[0], var30[1]);
         }
      }

   }

   private BlueprintJoint readJoint(ErrorCollector var1, BlockbenchModel var2, @Nullable BlueprintJoint var3, BlockbenchModel.Group var4, Map<UUID, BlueprintJoint> var5) {
      BlueprintJoint var6 = new BlueprintJoint();
      var6.setName(var4.getName().toLowerCase(Locale.ENGLISH));
      var6.setUuid(var4.getUuid());
      var6.setGlobalPosition(vector3f(var4.getOrigin()).mul(0.0625F));
      Vector3f var7 = vector3f(var4.getRotation()).mul(0.017453292F);
      var6.setLocalRotation(var7);
      var6.getLocalQuaternion().rotateZYX(var7.z, var7.y, var7.x);
      if (var3 != null) {
         var6.setLocalPosition(var6.getGlobalPosition().sub(var3.getGlobalPosition(), new Vector3f()));
         Quaternionf var8 = (new Quaternionf()).rotationZYX(var7.z, var7.y, var7.x);
         Quaternionf var9 = var3.getGlobalQuaternion();
         var9.mul(var8, var8);
         Vector3f var10 = MathUtils.getEulerAnglesZYX(var8, new Vector3f());
         var6.setGlobalRotation(new Vector3f(var10.x, var10.y, var10.z));
         var6.setGlobalQuaternion(var8);
         Vector3f var11 = var6.getLocalPosition().rotate(var9, new Vector3f());
         var6.setRotatedGlobalPosition(var11.add(var3.getRotatedGlobalPosition()));
      } else {
         var6.setLocalPosition(new Vector3f(var6.getGlobalPosition()));
         var6.setGlobalRotation(var7);
         var6.setGlobalQuaternion(new Quaternionf(var6.getLocalQuaternion()));
         var6.setRotatedGlobalPosition(new Vector3f(var6.getGlobalPosition()));
      }

      var6.setParent(var3);
      this.behaviorParsers.forEach((var4x) -> {
         var4x.processJoint(var1, var2, var4, var6);
      });

      Iterator var13;
      BlueprintJoint var16;
      for(var13 = var4.getChildGroup().entrySet().iterator(); var13.hasNext(); var6.getChildren().put(var16.getName(), var16)) {
         Entry var14 = (Entry)var13.next();
         var16 = this.readJoint(var1, var2, var6, (BlockbenchModel.Group)var14.getValue(), var5);
         if (var6.getChildren().containsKey(var16.getName())) {
            (new WarningDuplicateJointName(var16.getName(), var16.getUuid())).log(var1);
            var16.setName(var16.getUuid().toString());
         }
      }

      var13 = var4.getElement().iterator();

      while(var13.hasNext()) {
         UUID var15 = (UUID)var13.next();
         Object var17 = var2.getElements().get(var15);
         if (var17 instanceof BlockbenchModel.AnimatableElement) {
            BlockbenchModel.AnimatableElement var18 = (BlockbenchModel.AnimatableElement)var17;
            BlueprintJoint var12 = this.readElementAsJoint(var6, var18, var5);
            if (var6.getChildren().containsKey(var12.getName())) {
               (new WarningDuplicateJointName(var12.getName(), var12.getUuid())).log(var1);
               var12.setName(var12.getUuid().toString());
            }

            var6.getChildren().put(var12.getName(), var12);
         }
      }

      var5.put(var6.getUuid(), var6);
      return var6;
   }

   private BlueprintJoint readElementAsJoint(@Nullable BlueprintJoint var1, BlockbenchModel.AnimatableElement var2, Map<UUID, BlueprintJoint> var3) {
      BlueprintJoint var4 = new BlueprintJoint();
      var4.setName(var2.getName().toLowerCase(Locale.ENGLISH));
      var4.setUuid(var2.getUuid());
      var4.setGlobalPosition(vector3f(var2.getOrigin()).mul(0.0625F));
      Vector3f var5 = vector3f(var2.getRotation()).mul(0.017453292F);
      var4.setLocalRotation(var5);
      var4.getLocalQuaternion().rotateZYX(var5.z, var5.y, var5.x);
      if (var1 != null) {
         var4.setLocalPosition(var4.getGlobalPosition().sub(var1.getGlobalPosition(), new Vector3f()));
         Quaternionf var6 = (new Quaternionf()).rotationZYX(var5.z, var5.y, var5.x);
         Quaternionf var7 = var1.getGlobalQuaternion();
         var7.mul(var6, var6);
         Vector3f var8 = MathUtils.getEulerAnglesZYX(var6, new Vector3f());
         var4.setGlobalRotation(new Vector3f(var8.x, var8.y, var8.z));
         var4.setGlobalQuaternion(var6);
         Vector3f var9 = var4.getLocalPosition().rotate(var7, new Vector3f());
         var4.setRotatedGlobalPosition(var9.add(var1.getRotatedGlobalPosition()));
      } else {
         var4.setLocalPosition(new Vector3f(var4.getGlobalPosition()));
         var4.setGlobalRotation(var5);
         var4.setGlobalQuaternion(new Quaternionf(var4.getLocalQuaternion()));
         var4.setRotatedGlobalPosition(new Vector3f(var4.getGlobalPosition()));
      }

      var4.setParent(var1);
      var3.put(var4.getUuid(), var4);
      return var4;
   }

   private void populateAssets(BlockbenchModel var1, ModelBlueprint var2, ModelAssets var3, ErrorCollector var4) {
      this.populateTexture(var1, var3, var4);
      this.populateModel(var1, var2, var3);
   }

   private void populateTexture(BlockbenchModel var1, ModelAssets var2, ErrorCollector var3) {
      Map var4 = var1.getTextures();
      Iterator var5 = var4.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         Integer var7 = (Integer)var6.getKey();
         BlockbenchModel.Texture var8 = (BlockbenchModel.Texture)var6.getValue();
         BlueprintTexture.MCMeta var9;
         if (var8.getRaw_mcmeta() == null) {
            var9 = new BlueprintTexture.MCMeta();
            var9.setFrametime(var8.getFrame_time());
            var9.setInterpolate((Boolean)Utils.orDef(false, var8.getFrame_interpolate()) ? true : null);
            if (var8.getFrame_order() != null && !var8.getFrame_order().isBlank()) {
               String[] var10 = var8.getFrame_order().split(" ");
               String[] var11 = var10;
               int var12 = var10.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  String var14 = var11[var13];
                  var9.addFrame(MathUtils.tryParse(var14, 0));
               }
            }
         } else {
            var9 = (BlueprintTexture.MCMeta)this.gson.fromJson(var8.getRaw_mcmeta(), BlueprintTexture.MCMeta.class);
            var9.setMustGenerate(true);
         }

         BlueprintTexture var15 = this.constructBlueprintTexture(var7, var1, var8, var9, var3);
         var2.getTextures().add(var15);
      }

   }

   @NotNull
   private BlueprintTexture constructBlueprintTexture(Integer var1, BlockbenchModel var2, BlockbenchModel.Texture var3, BlueprintTexture.MCMeta var4, ErrorCollector var5) {
      BlueprintTexture var6 = new BlueprintTexture();
      var6.setId(var1);
      var6.setFrameWidth((Integer)Utils.or(var3.getUv_width(), var2.getResolution().getWidth()));
      var6.setFrameHeight((Integer)Utils.or(var3.getUv_height(), var2.getResolution().getHeight()));
      String var7 = var3.getNamespace().isBlank() ? this.generator.getNamespace() : var3.getNamespace();
      String var8 = !var7.equals(this.generator.getNamespace()) && !var3.getFolder().isBlank() ? var3.getFolder().toLowerCase(Locale.ENGLISH) : "entity";
      String var9 = FileUtils.removeExtension(var3.getName()).toLowerCase(Locale.ENGLISH);
      ResourceLocation var10 = new ResourceLocation(var7, var8 + "/" + var9);
      if (!var10.isValid()) {
         ResourceLocation var11 = new ResourceLocation(this.generator.getNamespace(), "entity/" + String.valueOf(Utils.generateUUIDFromString(var9)));
         (new WarnBadTexture(var10, var11)).log(var5);
         var10 = var11;
      }

      var6.setPath(var10);
      var6.setMcMeta(var4);
      var6.setSource(var3.getSource());
      return var6;
   }

   private void populateModel(BlockbenchModel var1, ModelBlueprint var2, ModelAssets var3) {
      Iterator var4 = var2.getFlatMap().entrySet().iterator();

      while(true) {
         String var6;
         BlueprintJoint var7;
         ProcessedJoint var14;
         Set var15;
         do {
            BlockbenchModel.Group var8;
            boolean var9;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     Entry var5 = (Entry)var4.next();
                     var6 = (String)var5.getKey();
                     var7 = (BlueprintJoint)var5.getValue();
                     var8 = var1.getGroup(var7.getUuid());
                  } while(var8 == null);
               } while(!var8.isExport());

               var9 = false;
               Iterator var10 = var7.getCachedBehaviorProvider().keySet().iterator();

               while(var10.hasNext()) {
                  JointActionType var11 = (JointActionType)var10.next();
                  if (var11.isIgnoreCubes()) {
                     var9 = true;
                     break;
                  }
               }
            } while(var9);

            var14 = this.process(var1, var7, var8, var3);
            var15 = var14.getModels();
         } while(var15.isEmpty());

         var7.setRenderer(true);
         var7.setScale(var14.getScale());
         ItemModelData.MultiModels var12 = var7.getModelData().getMultiModels();

         for(int var13 = 0; var13 < var15.size(); ++var13) {
            String var10003 = var2.getName();
            var12.addSubModel(new ItemModelData.SubModel(var10003 + ":" + (var13 == 0 ? var7.getName() : var7.getName() + "/" + var13)));
         }

         var3.getModels().put(var6, var15);
      }
   }

   private ProcessedJoint process(BlockbenchModel var1, BlueprintJoint var2, BlockbenchModel.Group var3, ModelAssets var4) {
      ProcessedJoint var5 = new ProcessedJoint(var2.getName(), vector3f(var3.getOrigin()), vector3f(var3.getRotation()));
      var2.setProcessedJoint(var5);
      Iterator var6 = var3.getElement().iterator();

      while(true) {
         Object var8;
         do {
            if (!var6.hasNext()) {
               var5.splitModels(var1, var4);
               return var5;
            }

            UUID var7 = (UUID)var6.next();
            var8 = var1.getElements().get(var7);
         } while(!(var8 instanceof BlockbenchModel.Cube));

         BlockbenchModel.Cube var9 = (BlockbenchModel.Cube)var8;
         ConcurrentHashMap var10 = new ConcurrentHashMap();
         Iterator var11 = var9.getFaces().entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            BlockbenchModel.Face var13 = (BlockbenchModel.Face)var12.getValue();
            if (!var13.isEmpty()) {
               ProcessedJoint.Face var14 = getProcFace(var13);
               var10.put(Direction.valueOf(((String)var12.getKey()).toUpperCase(Locale.ENGLISH)), var14);
            }
         }

         if (!var10.isEmpty()) {
            ProcessedJoint.Cube var15 = new ProcessedJoint.Cube(var9.getName(), vector3d(var9.getOrigin()).sub(var5.getJointOrigin()), vector3d(var9.getRotation()), vector3d(var9.getFrom()).sub(var5.getJointOrigin()), vector3d(var9.getTo()).sub(var5.getJointOrigin()), var10, var9.getInflate() == null ? 0.0F : var9.getInflate());
            var5.getCubes().add(var15);
            if (var9.isTranslucent()) {
               var2.getModelData().setTranslucent(true);
            }
         }
      }
   }
}
