package advancedplugins.pm2.cv.models.v1_21_R10;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntityManager;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.LeashRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.NameTagRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SegmentRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SubHitboxRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualDisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRendererParser;
import advancedplugins.pm2.cv.models.api.nms.NMSHandler;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.network.NetworkHandler;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.EntityManagementSystem;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.fake.FakeDisplayEntityManagerImpl;
import advancedplugins.pm2.cv.models.v1_21_R10.network.ChannelManagerImpl;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior.ConnectionLinkHandler;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior.InteractionZoneManager;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior.JointedDisplayCoordinator;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior.RideableEntityProcessor;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.behavior.TextLabelController;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.model.ModelEntitySynchronizer;
import advancedplugins.pm2.cv.models.v1_21_R10.parser.visual.EffectRenderer;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class MinecraftProtocolAdapter implements NMSHandler {
   private final EntityHandler entityHandler = this.initializeEntitySystem();
   private final NetworkHandler networkHandler = this.initializeNetworkSystem();
   private final RenderParsers globalParsers = this.configureRenderingPipeline();
   private final EffectRenderer vfxDisplayParser = new EffectRenderer();
   private final FakeDisplayEntityManager fakeDisplayEntityManager = new FakeDisplayEntityManagerImpl();

   private EntityHandler initializeEntitySystem() {
      return new EntityManagementSystem();
   }

   private NetworkHandler initializeNetworkSystem() {
      return new ChannelManagerImpl();
   }

   public RenderParsers createParsers() {
      return this.configureRenderingPipeline();
   }

   private RenderParsers configureRenderingPipeline() {
      RenderParsers var1 = new RenderParsers();
      this.registerModelProcessors(var1);
      this.registerBehaviorProcessors(var1);
      return var1;
   }

   private void registerModelProcessors(RenderParsers var1) {
      var1.registerModelParser((var0) -> {
         return var0 instanceof DisplayRenderer;
      }, ModelEntitySynchronizer::new);
   }

   private void registerBehaviorProcessors(RenderParsers var1) {
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof MountRenderer;
      }, RideableEntityProcessor::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof LeashRenderer;
      }, ConnectionLinkHandler::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof NameTagRenderer;
      }, TextLabelController::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof SubHitboxRenderer;
      }, InteractionZoneManager::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof SegmentRenderer;
      }, JointedDisplayCoordinator::new);
   }

   public Set<ItemStack> createStack(ItemModelData var1, ItemModelData.Context var2) {
      ItemStack var3 = this.constructBaseItem();
      this.applyModelData(var3, var1, var2);
      return Set.of(var3);
   }

   private ItemStack constructBaseItem() {
      return new ItemStack(Material.BONE);
   }

   private void applyModelData(ItemStack var1, ItemModelData var2, ItemModelData.Context var3) {
      ItemMeta var4 = var1.getItemMeta();
      this.configureItemModel(var4, var2);
      this.applyColorData(var4, var3.color());
      var1.setItemMeta(var4);
   }

   private void configureItemModel(ItemMeta var1, ItemModelData var2) {
      var1.setItemModel(var2.getSingleComposite().model());
   }

   private void applyColorData(ItemMeta var1, Color var2) {
      CustomModelDataComponent var3 = var1.getCustomModelDataComponent();
      var3.setColors(List.of(var2));
      var1.setCustomModelDataComponent(var3);
   }

   public boolean colorStack(ItemStack var1, Color var2) {
      if (!this.hasValidMetadata(var1)) {
         return false;
      } else {
         ItemMeta var3 = var1.getItemMeta();
         this.updateItemColor(var3, var2);
         var1.setItemMeta(var3);
         return true;
      }
   }

   private boolean hasValidMetadata(ItemStack var1) {
      return var1.getItemMeta() != null;
   }

   private void updateItemColor(ItemMeta var1, Color var2) {
      CustomModelDataComponent var3 = var1.getCustomModelDataComponent();
      var3.setColors(List.of(var2));
      var1.setCustomModelDataComponent(var3);
   }

   public <T extends ModelRenderer> ModelRendererParser<T> getModelRendererParser(T var1) {
      return this.globalParsers.getModelParser(var1);
   }

   public <T extends BehaviorRenderer> BehaviorRendererParser<T> getBehaviorRendererParser(T var1) {
      return this.globalParsers.getBehaviorParser(var1);
   }

   public <T extends VisualRenderer> VisualRendererParser<T> getVFXRendererParser(T var1) {
      return this.resolveVisualParser(var1);
   }

   private <T extends VisualRenderer> VisualRendererParser<T> resolveVisualParser(T var1) {
      return var1 instanceof VisualDisplayRenderer ? this.vfxDisplayParser : null;
   }

   @Generated
   public EntityHandler getEntityHandler() {
      return this.entityHandler;
   }

   @Generated
   public NetworkHandler getNetworkHandler() {
      return this.networkHandler;
   }

   @Generated
   public RenderParsers getGlobalParsers() {
      return this.globalParsers;
   }

   @Generated
   public EffectRenderer getVfxDisplayParser() {
      return this.vfxDisplayParser;
   }

   @Generated
   public FakeDisplayEntityManager getFakeDisplayEntityManager() {
      return this.fakeDisplayEntityManager;
   }
}
