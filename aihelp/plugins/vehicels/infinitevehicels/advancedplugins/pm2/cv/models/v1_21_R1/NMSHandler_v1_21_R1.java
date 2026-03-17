package advancedplugins.pm2.cv.models.v1_21_R1;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntityManager;
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
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityHandlerImpl;
import advancedplugins.pm2.cv.models.v1_21_R1.network.NetworkHandlerImpl;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior.LeashParser;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior.MountParser;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior.NameTagParser;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior.SegmentParser;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.behavior.SubHitboxParser;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.model.DisplayParser;
import advancedplugins.pm2.cv.models.v1_21_R1.parser.visual.VisualDisplayParser;
import lombok.Generated;

public class NMSHandler_v1_21_R1 implements NMSHandler {
   private final EntityHandler entityHandler = new EntityHandlerImpl();
   private final NetworkHandler networkHandler = new NetworkHandlerImpl();
   private final RenderParsers globalParsers = this.createParsers();
   private final VisualDisplayParser vfxDisplayParser = new VisualDisplayParser();
   private final FakeDisplayEntityManager fakeDisplayEntityManager = null;

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
