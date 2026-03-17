package advancedplugins.pm2.cv.models.api;

import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.nrpc.ModelManager;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelUpdaters;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.AnimationHandlerArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.AnimationPropertyArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypeArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data.KeyframeReaderArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.script.ScriptReaderArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitPlayer;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.EntityDataTrackers;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.ModelGenerator;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.InteractionTracker;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.MountControllerTypeArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.MountPairManager;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.Visual;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.VisualTicker;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRenderer;
import advancedplugins.pm2.cv.models.api.nms.NMSHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.network.NetworkHandler;
import advancedplugins.pm2.cv.models.api.nms.ui.AnvilHandler;
import advancedplugins.pm2.cv.models.api.utils.CompatibilityManager;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigManager;
import advancedplugins.pm2.cv.models.api.utils.scheduling.PlatformScheduler;
import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ModelAPI {
   protected static ModelAPI API;
   public static JavaPlugin PLUGIN;
   protected static final Set<Integer> renderCanceled = Sets.newConcurrentHashSet();
   protected static ConfigManager configManager;
   protected static Gson gson;
   protected static PlatformScheduler scheduler;
   protected static ModelArchive modelArchive;
   protected static ModelGenerator modelGenerator;
   protected static KeyframeTypeArchive keyframeTypeArchive;
   protected static KeyframeReaderArchive keyframeReaderArchive;
   protected static ScriptReaderArchive scriptReaderArchive;
   protected static JointActionArchive jointActionArchive;
   protected static AnimationHandlerArchive animationHandlerArchive;
   protected static AnimationPropertyArchive animationPropertyArchive;
   protected static DualTicker ticker;
   protected static ModelUpdaters modelUpdaters;
   protected static VisualTicker visualTicker;
   protected static EntityDataTrackers dataTrackers;
   protected static InteractionTracker interactionTracker;
   protected static MountPairManager mountPairManager;
   protected static MountControllerTypeArchive mountControllerTypeArchive;
   protected static NMSHandler nmsHandler;
   protected static CompatibilityManager compatibilityManager;
   private static final ModelManager modelManager = new ModelManager();

   public static NMSHandler getNMSHandler() {
      return nmsHandler;
   }

   public static EntityHandler getEntityHandler() {
      return getNMSHandler().getEntityHandler();
   }

   public static AnvilHandler getAnvilHandler() {
      return getNMSHandler().getAnvilHandler();
   }

   public static NetworkHandler getNetworkHandler() {
      return getNMSHandler().getNetworkHandler();
   }

   public static IModelContainer create(Entity var0) {
      return create((Entity)var0, (Consumer)null);
   }

   public static IModelContainer create(Entity var0, Consumer<IModelContainer> var1) {
      Object var2;
      if (var0 instanceof Player) {
         Player var3 = (Player)var0;
         var2 = new BukkitPlayer(var3);
      } else {
         var2 = new BukkitEntity(var0);
      }

      return create((BaseEntity)var2, var1);
   }

   public static IModelContainer create(BaseEntity<?> var0) {
      return create((BaseEntity)var0, (Consumer)null);
   }

   public static IModelContainer create(BaseEntity<?> var0, Consumer<IModelContainer> var1) {
      return getAPI().createModeledEntityImpl(var0, var1);
   }

   public static IModelContainer getModeledEntity(Entity var0) {
      return getModeledEntity(var0.getUniqueId());
   }

   public static IModelContainer getModeledEntity(int var0) {
      return getAPI().getModelUpdaters().getModeledEntity(var0);
   }

   public static IModelContainer getModeledEntity(UUID var0) {
      return getAPI().getModelUpdaters().getModeledEntity(var0);
   }

   public static IModelContainer getOrCreateModeledEntity(Entity var0) {
      IModelContainer var1 = getModeledEntity(var0);
      return var1 != null ? var1 : create((Entity)var0, (Consumer)null);
   }

   public static IModelContainer getOrCreateModeledEntity(Entity var0, Consumer<IModelContainer> var1) {
      IModelContainer var2 = getModeledEntity(var0);
      IModelContainer var3;
      if (var2 != null) {
         var3 = var2;
      } else {
         Object var4;
         if (var0 instanceof Player) {
            Player var5 = (Player)var0;
            var4 = new BukkitPlayer(var5);
         } else {
            var4 = new BukkitEntity(var0);
         }

         var3 = create((BaseEntity)var4, var1);
      }

      return var3;
   }

   public static IModelContainer getOrCreateModeledEntity(UUID var0, Supplier<BaseEntity<?>> var1) {
      IModelContainer var2 = getModeledEntity(var0);
      return var2 != null ? var2 : create((BaseEntity)((BaseEntity)var1.get()), (Consumer)null);
   }

   public static IModelContainer getOrCreateModeledEntity(UUID var0, Supplier<BaseEntity<?>> var1, Consumer<IModelContainer> var2) {
      IModelContainer var3 = getModeledEntity(var0);
      return var3 != null ? var3 : getAPI().createModeledEntityImpl((BaseEntity)var1.get(), var2);
   }

   public static IModelContainer removeModeledEntity(Entity var0) {
      return removeModeledEntity(var0.getUniqueId());
   }

   public static IModelContainer removeModeledEntity(int var0) {
      return getAPI().getModelUpdaters().removeModeledEntity(var0);
   }

   public static IModelContainer removeModeledEntity(UUID var0) {
      return getAPI().getModelUpdaters().removeModeledEntity(var0);
   }

   public static boolean isModeledEntity(UUID var0) {
      return getModeledEntity(var0) != null;
   }

   public static IVisualModel create(String var0) {
      return create((String)var0, (Function)null, (Function)null);
   }

   public static IVisualModel create(String var0, Function<IVisualModel, ModelRenderer> var1, Function<IVisualModel, AnimationHandler> var2) {
      ModelBlueprint var3 = (ModelBlueprint)getAPI().getModelArchive().get(var0);
      if (var3 == null) {
         throw new RuntimeException("Error while creating ActiveModel. Unknown model: " + var0);
      } else {
         return create(var3, var1, var2);
      }
   }

   public static IVisualModel create(ModelBlueprint var0) {
      return create((ModelBlueprint)var0, (Function)null, (Function)null);
   }

   public static IVisualModel create(ModelBlueprint var0, Function<IVisualModel, ModelRenderer> var1, Function<IVisualModel, AnimationHandler> var2) {
      return getAPI().createActiveModelImpl(var0, var1, var2);
   }

   public static AnimationHandler create(IVisualModel var0) {
      return getAPI().getPriorityHandler(var0);
   }

   public static AnimationHandler createStateMachineHandler(IVisualModel var0) {
      return getAPI().getStateMachineHandler(var0);
   }

   public static Visual createVisual(Entity var0) {
      return createVisual((Entity)var0, (Consumer)null, (Function)null);
   }

   public static Visual createVisual(Entity var0, Consumer<Visual> var1) {
      return createVisual((Entity)var0, var1, (Function)null);
   }

   public static Visual createVisual(Entity var0, Consumer<Visual> var1, Function<Visual, VisualRenderer> var2) {
      Object var3;
      if (var0 instanceof Player) {
         Player var4 = (Player)var0;
         var3 = new BukkitPlayer(var4);
      } else {
         var3 = new BukkitEntity(var0);
      }

      return createVisual((BaseEntity)var3, var1, var2);
   }

   public static Visual createVisual(BaseEntity<?> var0) {
      return createVisual((BaseEntity)var0, (Consumer)null, (Function)null);
   }

   public static Visual createVisual(BaseEntity<?> var0, Consumer<Visual> var1) {
      return createVisual((BaseEntity)var0, var1, (Function)null);
   }

   public static Visual createVisual(BaseEntity<?> var0, Consumer<Visual> var1, Function<Visual, VisualRenderer> var2) {
      return getAPI().create(var0, var2, var1);
   }

   public static Visual getVisual(Entity var0) {
      return getVisual(var0.getUniqueId());
   }

   public static Visual getVisual(int var0) {
      return getAPI().getVFXUpdater().getVisual(var0);
   }

   public static Visual getVisual(UUID var0) {
      return getAPI().getVFXUpdater().getVisual(var0);
   }

   public static boolean isVisual(UUID var0) {
      return getVisual(var0) != null;
   }

   public static ModelBlueprint getBlueprint(String var0) {
      return (ModelBlueprint)getAPI().getModelArchive().get(var0);
   }

   public static void setRenderCanceled(int var0, boolean var1) {
      if (var1) {
         renderCanceled.add(var0);
      } else {
         renderCanceled.remove(var0);
      }

   }

   public static boolean isRenderCanceled(int var0) {
      return renderCanceled.contains(var0);
   }

   public static void callEvent(Event var0) {
      Bukkit.getPluginManager().callEvent(var0);
   }

   public static int getPlayerProtocolVersion(UUID var0) {
      return getAPI().playerProtocolVersion(var0);
   }

   public static void onPlayerQuit(PlayerQuitEvent var0) {
      Player var1 = var0.getPlayer();
      getNetworkHandler().ejectChannel(var1);
      getMountPairManager().tryDismount(var1);
      getInteractionTracker().removeDynamicHitbox(var1.getUniqueId());
      getEntityHandler().setForcedInvisible(var1, false);
      AnimationLODHandler.setPlayerActive(var0.getPlayer().getUniqueId(), false);
   }

   public static ModelAPI getAPI() {
      return API;
   }

   public VisualTicker getVFXUpdater() {
      return visualTicker;
   }

   public abstract IModelContainer createModeledEntityImpl(BaseEntity<?> var1, Consumer<IModelContainer> var2);

   public abstract IVisualModel createActiveModelImpl(ModelBlueprint var1, Function<IVisualModel, ModelRenderer> var2, Function<IVisualModel, AnimationHandler> var3);

   public abstract Visual create(BaseEntity<?> var1, Function<Visual, VisualRenderer> var2, Consumer<Visual> var3);

   public abstract AnimationHandler getPriorityHandler(IVisualModel var1);

   public abstract AnimationHandler getStateMachineHandler(IVisualModel var1);

   public abstract int playerProtocolVersion(UUID var1);

   public Set<Integer> getRenderCanceled() {
      return renderCanceled;
   }

   public ConfigManager getConfigManager() {
      return configManager;
   }

   public Gson getGson() {
      return gson;
   }

   public static PlatformScheduler getScheduler() {
      return scheduler;
   }

   public ModelArchive getModelArchive() {
      return modelArchive;
   }

   public KeyframeTypeArchive getKeyframeTypeArchive() {
      return keyframeTypeArchive;
   }

   public KeyframeReaderArchive getKeyframeReaderArchive() {
      return keyframeReaderArchive;
   }

   public JointActionArchive getJointActionArchive() {
      return jointActionArchive;
   }

   public DualTicker getTicker() {
      return ticker;
   }

   public ModelUpdaters getModelUpdaters() {
      return modelUpdaters;
   }

   public EntityDataTrackers getDataTrackers() {
      return dataTrackers;
   }

   public CompatibilityManager getCompatibilityManager() {
      return compatibilityManager;
   }

   @Generated
   public static ModelGenerator getModelGenerator() {
      return modelGenerator;
   }

   @Generated
   public static ScriptReaderArchive getScriptReaderArchive() {
      return scriptReaderArchive;
   }

   @Generated
   public static AnimationHandlerArchive getAnimationHandlerArchive() {
      return animationHandlerArchive;
   }

   @Generated
   public static AnimationPropertyArchive getAnimationPropertyArchive() {
      return animationPropertyArchive;
   }

   @Generated
   public static InteractionTracker getInteractionTracker() {
      return interactionTracker;
   }

   @Generated
   public static MountPairManager getMountPairManager() {
      return mountPairManager;
   }

   @Generated
   public static MountControllerTypeArchive getMountControllerTypeArchive() {
      return mountControllerTypeArchive;
   }

   @Generated
   public static ModelManager getModelManager() {
      return modelManager;
   }
}
