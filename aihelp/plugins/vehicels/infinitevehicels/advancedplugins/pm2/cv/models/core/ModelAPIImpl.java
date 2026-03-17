package advancedplugins.pm2.cv.models.core;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelUpdaters;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.AnimationHandlerArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.AnimationPropertyArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.ModelState;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypeArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data.KeyframeReaderArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.SimpleProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.script.ScriptReaderArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.EntityDataTrackers;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.AbstractEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.InteractionTracker;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.ProceduralType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.DefaultRenderType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.PlayerLimb;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.MountControllerTypeArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.MountPairManager;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountControllerTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.Visual;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.VisualTicker;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRenderer;
import advancedplugins.pm2.cv.models.api.utils.CompatibilityManager;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigManager;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.api.utils.scheduling.BukkitPlatformScheduler;
import advancedplugins.pm2.cv.models.api.utils.scheduling.FoliaPlatformScheduler;
import advancedplugins.pm2.cv.models.api.utils.scheduling.PlatformScheduler;
import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import advancedplugins.pm2.cv.models.core.animation.handler.PriorityHandler;
import advancedplugins.pm2.cv.models.core.animation.handler.StateMachineHandler;
import advancedplugins.pm2.cv.models.core.animation.script.InfiniteModelSR;
import advancedplugins.pm2.cv.models.core.citizens.CitizensCommand;
import advancedplugins.pm2.cv.models.core.citizens.ModelTrait;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import advancedplugins.pm2.cv.models.core.listener.ChatListener;
import advancedplugins.pm2.cv.models.core.listener.EntityListener;
import advancedplugins.pm2.cv.models.core.listener.ItemsAdderListener;
import advancedplugins.pm2.cv.models.core.listener.PlayerListener;
import advancedplugins.pm2.cv.models.core.listener.WorldListener;
import advancedplugins.pm2.cv.models.core.model.nrpc.animation.ModelAnimation;
import advancedplugins.pm2.cv.models.core.model.rpc.ModelContainer;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.ModelGeneratorImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.LeashManagerImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.MountManagerImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.GhostImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.HeadForcedImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.HeadImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.HeldItemImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.LeashImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.MountImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.NameTagImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.PlayerLimbImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.SegmentImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.SubHitboxImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.TailImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer.HeldItemRendererImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer.LeashRendererImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer.MountRendererImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer.NameTagRendererImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer.SegmentRendererImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer.SubHitboxRendererImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.visual.VisualImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.visual.VisualModel;
import advancedplugins.pm2.cv.models.core.util.StartupUtil;
import advancedplugins.pm2.cv.models.core.util.exception.UnknownVersionException;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Vector3f;

public class ModelAPIImpl extends ModelAPI {
   public static ModelAPIImpl CORE;
   private static AbstractCommand commandRoot;
   private static PluginManager pluginManager;
   private static boolean loadListeners = false;

   public static void load(JavaPlugin var0) {
      load(var0, true);
   }

   public static void load(JavaPlugin var0, boolean var1) {
      ModelAPI.PLUGIN = var0;
      loadListeners = var1;
      PLUGIN.getServer().getServicesManager().register(ModelAPIImpl.class, new ModelAPIImpl(), var0, ServicePriority.Highest);
      ModelAPI.API = (ModelAPI)PLUGIN.getServer().getServicesManager().load(ModelAPIImpl.class);
      CORE = (ModelAPIImpl)PLUGIN.getServer().getServicesManager().load(ModelAPIImpl.class);
      LogUtil.logger = PLUGIN.getLogger();
      pluginManager = Bukkit.getPluginManager();
   }

   public static void enable() {
      configureConfig();

      try {
         nmsHandler = StartupUtil.findNMSHandler();
      } catch (UnknownVersionException var1) {
         throw new RuntimeException(var1);
      }

      gson = (new GsonBuilder()).setPrettyPrinting().serializeSpecialFloatingPointValues().create();
      scheduler = (PlatformScheduler)(ServerInfo.IS_FOLIA ? new FoliaPlatformScheduler() : new BukkitPlatformScheduler());
      modelArchive = new ModelArchive();
      modelGenerator = new ModelGeneratorImpl(API);
      configureKeyframeTypeArchive();
      keyframeReaderArchive = new KeyframeReaderArchive();
      configureScriptReaderArchive();
      configureJointActionArchive();
      configureTicker();
      configureMountControllerTypeArchive();
      configureAnimationHandlerArchive();
      configureAnimationPropertyArchive();
      configureCommands();
      configureCompatibility();
      if (loadListeners) {
         pluginManager.registerEvents(new PlayerListener(), PLUGIN);
         pluginManager.registerEvents(new EntityListener(), PLUGIN);
         pluginManager.registerEvents(new WorldListener(), PLUGIN);
         pluginManager.registerEvents(new ChatListener(), PLUGIN);
         if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null && Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
            pluginManager.registerEvents(new ItemsAdderListener(), PLUGIN);
         }
      }

      modelGenerator.importModels(true);
   }

   public static void disable() {
      try {
         ModelAnimation.shutdownExecutor();
      } catch (Exception var1) {
         LogUtil.error(1, "Failed to shutdown animation executor: " + var1.getMessage());
      }

      if (ticker != null) {
         ticker.stop();
      }

      if (modelUpdaters != null) {
         modelUpdaters.saveAllModels();
      }

   }

   private static void configureConfig() {
      configManager = new ConfigManager(PLUGIN);
      Stream var10000 = Arrays.stream(ConfigProperty.values());
      ConfigManager var10001 = configManager;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::register);
      var10000 = Arrays.stream(ModelState.values());
      var10001 = configManager;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::register);
      configManager.save();
      configManager.registerReferenceUpdate(AbstractEntityData::updateConfig);
      configManager.registerReferenceUpdate(AnimationLODHandler::updateConfig);
   }

   private static void configureKeyframeTypeArchive() {
      keyframeTypeArchive = new KeyframeTypeArchive();
      keyframeTypeArchive.registerKeyframeType(KeyframeTypes.POSITION);
      keyframeTypeArchive.registerKeyframeType(KeyframeTypes.ROTATION);
      keyframeTypeArchive.registerKeyframeType(KeyframeTypes.SCALE);
      keyframeTypeArchive.registerKeyframeType(KeyframeTypes.SCRIPT);
   }

   private static void configureScriptReaderArchive() {
      scriptReaderArchive = new ScriptReaderArchive();
      scriptReaderArchive.registerAndDefault("imodel", new InfiniteModelSR());
   }

   private static void configureJointActionArchive() {
      jointActionArchive = new JointActionArchive();
      JointBehaviorTypes.HEAD = JointActionType.Builder.of(HeadImpl::new, (JointActionType.BehaviorManagerProvider)null, "head").optional("local", Boolean.class).optional("inherited", Boolean.class).predicate(JointActionType::noProcedural).forced(HeadForcedImpl::new).build();
      JointBehaviorTypes.GHOST = JointActionType.Builder.of(GhostImpl::new, (JointActionType.BehaviorManagerProvider)null, "ghost").renderType(DefaultRenderType.NONE).build();
      JointBehaviorTypes.MOUNT = JointActionType.Builder.of(MountImpl::new, MountManagerImpl::new, "mount").required("driver", Boolean.class).renderType(MountRendererImpl::new).build();
      JointBehaviorTypes.SUB_HITBOX = JointActionType.Builder.of(SubHitboxImpl::new, (JointActionType.BehaviorManagerProvider)null, "sub_hitbox").required("dimension", Hitbox.class).optional("obb", Boolean.class).optional("origin", Vector3f.class).renderType(SubHitboxRendererImpl::new).ignoreCubes().build();
      JointBehaviorTypes.NAMETAG = JointActionType.Builder.of(NameTagImpl::new, (JointActionType.BehaviorManagerProvider)null, "nametag").renderType(NameTagRendererImpl::new).build();
      JointBehaviorTypes.ITEM = JointActionType.Builder.of(HeldItemImpl::new, (JointActionType.BehaviorManagerProvider)null, "item").required("display", ItemDisplayTransform.class).renderType(HeldItemRendererImpl::new).build();
      JointBehaviorTypes.SEGMENT = JointActionType.Builder.of(SegmentImpl::new, (JointActionType.BehaviorManagerProvider)null, "segment").optional("bounded", Boolean.class).optional("roll_lock", Boolean.class).optional("angle_limit", Float.class).optional("extend_rate", Float.class).renderType(SegmentRendererImpl::new).procedural(ProceduralType.ANIMATION, ProceduralType.TRANSFORM).pivot().build();
      JointBehaviorTypes.TAIL = JointActionType.Builder.of(TailImpl::new, (JointActionType.BehaviorManagerProvider)null, "tail").optional("bounded", Boolean.class).optional("roll_lock", Boolean.class).optional("angle_limit", Float.class).optional("extend_rate", Float.class).procedural(ProceduralType.ANIMATION, ProceduralType.TRANSFORM).build();
      JointBehaviorTypes.LEASH = JointActionType.Builder.of(LeashImpl::new, LeashManagerImpl::new, "leash").optional("main", Boolean.class).renderType(LeashRendererImpl::new).build();
      JointBehaviorTypes.PLAYER_LIMB = JointActionType.Builder.of(PlayerLimbImpl::new, (JointActionType.BehaviorManagerProvider)null, "player_limb").required("limb", PlayerLimb.Limb.class).renderType(DefaultRenderType.NONE).ignoreCubes().build();
      jointActionArchive.register(JointBehaviorTypes.HEAD);
      jointActionArchive.register(JointBehaviorTypes.GHOST);
      jointActionArchive.register(JointBehaviorTypes.MOUNT);
      jointActionArchive.register(JointBehaviorTypes.SUB_HITBOX);
      jointActionArchive.register(JointBehaviorTypes.NAMETAG);
      jointActionArchive.register(JointBehaviorTypes.ITEM);
      jointActionArchive.register(JointBehaviorTypes.SEGMENT);
      jointActionArchive.register(JointBehaviorTypes.TAIL);
      jointActionArchive.register(JointBehaviorTypes.LEASH);
      jointActionArchive.register(JointBehaviorTypes.PLAYER_LIMB);
   }

   private static void configureAnimationHandlerArchive() {
      animationHandlerArchive = new AnimationHandlerArchive();
      animationHandlerArchive.register("priority", PriorityHandler::create);
      animationHandlerArchive.register("state_machine", StateMachineHandler::create);
   }

   private static void configureAnimationPropertyArchive() {
      animationPropertyArchive = new AnimationPropertyArchive();
      animationPropertyArchive.register("simple", SimpleProperty::create);
   }

   private static void configureTicker() {
      ticker = new DualTicker(PLUGIN, getScheduler());
      mountPairManager = new MountPairManager();
      dataTrackers = new EntityDataTrackers(PLUGIN, getScheduler());
      modelUpdaters = new ModelUpdaters();
      visualTicker = new VisualTicker();
      interactionTracker = new InteractionTracker();
      MountPairManager var10000 = mountPairManager;
      Objects.requireNonNull(var10000);
      DualTicker.queueRepeatingSyncTask((Runnable)(var10000::updatePassengerPosition), 0, 0);
      VisualTicker var0 = visualTicker;
      Objects.requireNonNull(var0);
      DualTicker.queueRepeatingAsyncTask((Runnable)(var0::updateVisuals), 0, 0);
      InteractionTracker var1 = interactionTracker;
      Objects.requireNonNull(var1);
      DualTicker.queueRepeatingAsyncTask((Runnable)(var1::raytraceHitboxes), 0, 0);
      ticker.start();
   }

   private static void configureMountControllerTypeArchive() {
      mountControllerTypeArchive = new MountControllerTypeArchive();
      mountControllerTypeArchive.registerAndDefault("walking", MountControllerTypes.WALKING);
      mountControllerTypeArchive.register("force_walking", MountControllerTypes.WALKING_FORCE);
      mountControllerTypeArchive.register("flying", MountControllerTypes.FLYING);
      mountControllerTypeArchive.register("force_flying", MountControllerTypes.FLYING_FORCE);
   }

   private static void configureCommands() {
      PluginCommand var0 = PLUGIN.getCommand("imodel");
      if (var0 != null) {
         var0.setExecutor(commandRoot = new InfiniteModelsCommand(PLUGIN));
      }

   }

   private static void configureCompatibility() {
      compatibilityManager = new CompatibilityManager(PLUGIN);
      compatibilityManager.registerSupport("ViaVersion", (var0) -> {
         try {
            Class.forName("com.viaversion.viaversion.api.Via");
         } catch (ClassNotFoundException var2) {
            ServerInfo.HAS_VIAVERSION = false;
            return false;
         }

         ServerInfo.HAS_VIAVERSION = true;
         return true;
      });
   }

   private static boolean configureCitizensSupport(Plugin var0) {
      ServerInfo.HAS_CITIZENS = true;
      CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ModelTrait.class));
      commandRoot.addSubCommands(new CitizensCommand(commandRoot));
      return true;
   }

   public IModelContainer createModeledEntityImpl(BaseEntity<?> var1, Consumer<IModelContainer> var2) {
      return new ModelContainer(var1, var2);
   }

   public IVisualModel createActiveModelImpl(ModelBlueprint var1, Function<IVisualModel, ModelRenderer> var2, Function<IVisualModel, AnimationHandler> var3) {
      return new VisualModel(var1, var2, var3);
   }

   public Visual create(BaseEntity<?> var1, Function<Visual, VisualRenderer> var2, Consumer<Visual> var3) {
      return new VisualImpl(var1, var2, var3);
   }

   public AnimationHandler getPriorityHandler(IVisualModel var1) {
      return new PriorityHandler(var1);
   }

   public AnimationHandler getStateMachineHandler(IVisualModel var1) {
      return new StateMachineHandler(var1);
   }

   public int playerProtocolVersion(UUID var1) {
      return getNetworkHandler().getProtocolVersion();
   }

   public static JavaPlugin getPlugin() {
      return PLUGIN;
   }
}
