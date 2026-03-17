package advancedplugins.pm2.cv.util;

import advancedplugins.pm2.cv.InfiniteVehiclesPlugin;
import advancedplugins.pm2.cv.api.enums.EnumInterpolationMode;
import advancedplugins.pm2.cv.api.enums.EnumLoopMode;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationKeyframeConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.BoneConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.PartConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.RigConfiguration;
import advancedplugins.pm2.cv.fake.FakeEntityShowGroup;
import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.ModelState;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.SimpleProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Dummy;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.JointItems;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.vehicle.model.compound.CompoundModel;
import advancedplugins.pm2.cv.vehicle.model.compound.Part;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfiniteModelUtil {
   @Nullable
   public static InfiniteModelUtil.ModelFetcherResult getGeneratedModel(@NotNull String modelName, @NotNull Location location) {
      Validate.notNull(var0);
      Validate.notEmpty(var0);
      var1.getChunk().addPluginChunkTicket(InfiniteVehiclesPlugin.getInstance());
      var1.getChunk().setForceLoaded(true);
      var1.getChunk().load();
      Dummy var2 = new Dummy();
      var2.setLocation(var1);
      IModelContainer var3 = ModelAPI.create((BaseEntity)var2);
      IVisualModel var4 = ModelAPI.create(var0);
      if (var4 == null) {
         return null;
      } else {
         var3.addModel(var4, true);
         var4.generateModel();
         var2.setVisible(false);
         var2.setDetectingPlayers(false);
         return new InfiniteModelUtil.ModelFetcherResult(var4, var3, var2);
      }
   }

   public static void loadModelBones(CompoundModel compoundModel, World world, double x, double y, double z, Vehicle vehicle, FakeEntityShowGroup.FakeEntityShowGroupBuilder showGroupBuilder) {
      ArrayList var10 = new ArrayList(var8.getBlockBenchBones());
      Iterator var11 = var10.iterator();

      while(var11.hasNext()) {
         IJoint var12 = (IJoint)var11.next();
         ItemStack var13 = ((JointItems)var12.getModelTracker().get()).getFirst();
         Location var14 = var12.getLocation();
         double var15 = var14.getX() - var2;
         double var17 = var14.getY() - var4;
         double var19 = var14.getZ() - var6;
         Part var21 = new Part(var0, PartConfiguration.builder().id(var12.getJointId()).identifier(UUID.randomUUID()).material(var13.getType()).item(var13).scale(new Vector3D((double)var12.getGlobalScale().x, (double)var12.getGlobalScale().y, (double)var12.getGlobalScale().z)).rotation(new Vector3D((double)var12.getGlobalRightRotation().x, (double)var12.getGlobalRightRotation().y, (double)var12.getGlobalRightRotation().z)).offset(new Vector3D(var15, var17, var19)).build(), var1, var2, var4, var6);
         var0.getParts().add(var21);
         ((CompoundModelConfiguration)var0.getConfiguration()).getParts().add(var21.getConfiguration());
         var9.entry(var21.getDisplay());
         var0.getMetadataLinker().link(var21.getDisplay());
         var0.getBone().addPassenger(var21.getDisplay(), true);
      }

      var8.getConfiguration().setBlockBenchPartsLoaded(true);
   }

   public static void loadBlockBenchAnimations(CompoundModel compoundModel, Location location, String modelID, Vehicle vehicle) {
      InfiniteModelUtil.ModelFetcherResult var4 = getGeneratedModel(var2, var1);
      if (var4 != null) {
         IVisualModel var5 = var4.model();
         RigConfiguration.Builder var6 = RigConfiguration.builder();
         BoneConfiguration var7 = new BoneConfiguration(UUID.randomUUID(), "root", new Vector3D(0.0D, 0.0D, 0.0D), new Vector3D(0.0D, 0.0D, 0.0D));
         ((CompoundModelConfiguration)var0.getConfiguration()).getBones().add(var7);
         var6.root(var7);
         HashMap var8 = new HashMap();
         Iterator var9 = var0.getParts().iterator();

         while(var9.hasNext()) {
            Part var10 = (Part)var9.next();
            BoneConfiguration var11 = new BoneConfiguration(UUID.randomUUID(), var10.getConfiguration().getId(), new Vector3D(0.0D, 0.0D, 0.0D), new Vector3D(0.0D, 0.0D, 0.0D));
            ((CompoundModelConfiguration)var0.getConfiguration()).getBones().add(var11);
            var8.put(var10, var11);
            var6.bind(var10.getConfiguration(), var11);
         }

         try {
            ((CompoundModelConfiguration)var0.getConfiguration()).setRig(var6.build());
         } catch (InvalidConfigurationException var35) {
            throw new RuntimeException(var35);
         }

         var5.getAnimationHandler().prepare();
         var9 = var5.getBlueprint().getAnimations().keySet().iterator();

         while(true) {
            ModelState var12;
            BlueprintAnimation var37;
            do {
               if (!var9.hasNext()) {
                  var3.getConfiguration().setBlockBenchAnimationsLoaded(true);
                  var4.armorStand().setRemoved(true);
                  var4.modelContainer().destroy();
                  var4.model().destroy();
                  return;
               }

               String var36 = (String)var9.next();
               var37 = (BlueprintAnimation)var5.getBlueprint().getAnimations().get(var36);
               var12 = ModelState.get(var36);
            } while(var12 == null);

            SimpleProperty var13 = new SimpleProperty(var5, var37);
            var5.getAnimationHandler().playAnimation(var13, true);
            ArrayList var14 = new ArrayList(var3.getBlockBenchBones());
            ArrayList var15 = new ArrayList();
            double var16 = var37.getLength();
            double var18 = var13.getSpeed();
            double var20 = 0.05D;
            int var22 = (int)(var16 / (var18 * var20));

            for(int var23 = 0; var23 < var22 + 1; ++var23) {
               HashMap var24 = new HashMap();
               HashMap var25 = new HashMap();
               HashMap var26 = new HashMap();
               HashMap var27 = new HashMap();
               HashMap var28 = new HashMap();
               HashMap var29 = new HashMap();
               var5.tick();
               var5.getModelRenderer().init();
               DisplayRenderer var30 = (DisplayRenderer)var5.getModelRenderer();
               var30.init();
               Iterator var31 = var0.getParts().iterator();

               while(var31.hasNext()) {
                  Part var32 = (Part)var31.next();
                  BoneConfiguration var33 = (BoneConfiguration)var8.get(var32);
                  IJoint var34 = (IJoint)var14.stream().filter((var1x) -> {
                     return var1x.getJointId().equals(var32.getConfiguration().getId());
                  }).findFirst().orElse((Object)null);
                  if (var34 != null) {
                     var34.tick();
                     var34.setRenderer(true);
                     var30.init();
                     var30.dispatch(ModelAPI.getNMSHandler().createParsers());
                     if (var30.getRendered().containsKey(var34.getJointId())) {
                        ((DisplayRenderer.Joint)var30.getRendered().get(var34.getJointId())).getLeftRotation().ifDirty((var2x) -> {
                           var27.put(var33.getIdentifier(), var2x);
                        });
                        ((DisplayRenderer.Joint)var30.getRendered().get(var34.getJointId())).getPosition().ifDirty((var2x) -> {
                           var28.put(var33.getIdentifier(), var2x);
                        });
                        ((DisplayRenderer.Joint)var30.getRendered().get(var34.getJointId())).getScale().ifDirty((var2x) -> {
                           var29.put(var33.getIdentifier(), var2x);
                        });
                        var24.put(var33.getIdentifier(), new Vector3D(1.0D, 1.0D, 1.0D));
                        var26.put(var33.getIdentifier(), new Vector3D(1.0D, 1.0D, 1.0D));
                        var25.put(var33.getIdentifier(), new Vector3D(1.0D, 1.0D, 1.0D));
                     }
                  }
               }

               var15.add((new AnimationKeyframeConfiguration(0, var24, var26, var25)).setBlockBenchRotations(var27).setBlockBenchPositions(var28).setBlockBenchScales(var29));
               var13.update();
            }

            if (var15.size() == 1) {
               AnimationKeyframeConfiguration var38 = (AnimationKeyframeConfiguration)var15.get(0);
               var15.add(1, var38);
            }

            AnimationConfiguration var39 = new AnimationConfiguration(var13.getName(), EnumInterpolationMode.SMOOTH, EnumLoopMode.LOOP, var15, getStatesToApply(var12));
            ((CompoundModelConfiguration)var0.getConfiguration()).getAnimations().add(var39);
         }
      }
   }

   public static List<String> getStatesToApply(@NotNull ModelState modelState) {
      Validate.notNull(var0);
      ArrayList var1 = new ArrayList();
      switch(var0) {
      case IDLE:
      case SPAWN:
         var1.add(VehicleState.IDLE);
         break;
      case WALK:
         var1.add(VehicleState.MOVING);
         var1.add(VehicleState.INCREASING_HEIGHT);
         var1.add(VehicleState.DECREASING_HEIGHT);
         var1.add(VehicleState.TURNING_LEFT);
         var1.add(VehicleState.TURNING_RIGHT);
         var1.add(VehicleState.MOVING_BACKWARDS);
         var1.add(VehicleState.MOVING_BACKWARDS_TURNING_LEFT);
         var1.add(VehicleState.MOVING_BACKWARDS_TURNING_RIGHT);
         var1.add(VehicleState.MOVING_TURNING_LEFT);
         var1.add(VehicleState.MOVING_TURNING_RIGHT);
         var1.add(VehicleState.DECREASING_HEIGHT_TURNING_LEFT);
         var1.add(VehicleState.DECREASING_HEIGHT_TURNING_RIGHT);
         var1.add(VehicleState.INCREASING_HEIGHT_TURNING_RIGHT);
         var1.add(VehicleState.INCREASING_HEIGHT_TURNING_LEFT);
         break;
      case FLY:
      case JUMP:
      case JUMP_END:
      case JUMP_START:
         var1.add(VehicleState.INCREASING_HEIGHT);
         var1.add(VehicleState.DECREASING_HEIGHT);
         break;
      case HOVER:
         var1.add(VehicleState.DECREASING_HEIGHT);
      }

      return new ArrayList(var1.stream().map(VehicleState::getName).toList());
   }

   public static record ModelFetcherResult(@NotNull IVisualModel model, @NotNull IModelContainer modelContainer, @NotNull Dummy<ArmorStand> armorStand) {
      public ModelFetcherResult(@NotNull IVisualModel model, @NotNull IModelContainer modelContainer, @NotNull Dummy<ArmorStand> armorStand) {
         this.model = var1;
         this.modelContainer = var2;
         this.armorStand = var3;
      }

      @NotNull
      public IVisualModel model() {
         return this.model;
      }

      @NotNull
      public IModelContainer modelContainer() {
         return this.modelContainer;
      }

      @NotNull
      public Dummy<ArmorStand> armorStand() {
         return this.armorStand;
      }
   }
}
