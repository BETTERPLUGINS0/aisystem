package advancedplugins.pm2.cv.models.v1_21_R4;

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
import advancedplugins.pm2.cv.models.v1_21_R4.entity.EntityHandlerImpl;
import advancedplugins.pm2.cv.models.v1_21_R4.entity.fake.FakeDisplayEntityManagerImpl;
import advancedplugins.pm2.cv.models.v1_21_R4.network.NetworkHandlerImpl;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior.LeashParser;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior.MountParser;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior.NameTagParser;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior.SegmentParser;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.behavior.SubHitboxParser;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.model.DisplayParser;
import advancedplugins.pm2.cv.models.v1_21_R4.parser.visual.VisualDisplayParser;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class NMSHandler_v1_21_R4 implements NMSHandler {
   private final EntityHandler entityHandler = new EntityHandlerImpl();
   private final NetworkHandler networkHandler = new NetworkHandlerImpl();
   private final RenderParsers globalParsers = this.createParsers();
   private final VisualDisplayParser vfxDisplayParser = new VisualDisplayParser();
   private final FakeDisplayEntityManager fakeDisplayEntityManager = new FakeDisplayEntityManagerImpl();

   public RenderParsers createParsers() {
      RenderParsers var1 = new RenderParsers();
      var1.registerModelParser((var0) -> {
         return var0 instanceof DisplayRenderer;
      }, DisplayParser::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof MountRenderer;
      }, MountParser::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof LeashRenderer;
      }, LeashParser::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof NameTagRenderer;
      }, NameTagParser::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof SubHitboxRenderer;
      }, SubHitboxParser::new);
      var1.registerBehaviorParser((var0) -> {
         return var0 instanceof SegmentRenderer;
      }, SegmentParser::new);
      return var1;
   }

   public Set<ItemStack> createStack(ItemModelData data, ItemModelData.Context context) {
      ItemStack var3 = new ItemStack(Material.BONE);
      var3.editMeta((var2x) -> {
         var2x.setItemModel(var1.getSingleComposite().model());
         CustomModelDataComponent var3 = var2x.getCustomModelDataComponent();
         var3.setColors(List.of(var2.color()));
         var2x.setCustomModelDataComponent(var3);
      });
      return Set.of(var3);
   }

   public boolean colorStack(ItemStack stack, Color color) {
      var1.editMeta((var1x) -> {
         CustomModelDataComponent var2x = var1x.getCustomModelDataComponent();
         var2x.setColors(List.of(var2));
         var1x.setCustomModelDataComponent(var2x);
      });
      return true;
   }

   public <T extends ModelRenderer> ModelRendererParser<T> getModelRendererParser(T renderer) {
      return this.globalParsers.getModelParser(var1);
   }

   public <T extends BehaviorRenderer> BehaviorRendererParser<T> getBehaviorRendererParser(T renderer) {
      return this.globalParsers.getBehaviorParser(var1);
   }

   public <T extends VisualRenderer> VisualRendererParser<T> getVFXRendererParser(T renderer) {
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
   public VisualDisplayParser getVfxDisplayParser() {
      return this.vfxDisplayParser;
   }

   @Generated
   public FakeDisplayEntityManager getFakeDisplayEntityManager() {
      return this.fakeDisplayEntityManager;
   }
}
