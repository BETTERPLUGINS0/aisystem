package advancedplugins.pm2.cv.models.api.nms;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntityManager;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRendererParser;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRendererParser;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.network.NetworkHandler;
import advancedplugins.pm2.cv.models.api.nms.ui.AnvilHandler;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface NMSHandler {
   EntityHandler getEntityHandler();

   NetworkHandler getNetworkHandler();

   default AnvilHandler getAnvilHandler() {
      return null;
   }

   RenderParsers getGlobalParsers();

   RenderParsers createParsers();

   FakeDisplayEntityManager getFakeDisplayEntityManager();

   default Set<ItemStack> createStack(ItemModelData data, ItemModelData.Context context) {
      Collection<ItemModelData.SubModel> subModels = data.getMultiModels().getSubModels();
      return (Set)subModels.stream().map((subModel) -> {
         return subModel.getItem().create(context.color(), subModel.getData());
      }).collect(Collectors.toSet());
   }

   default boolean colorStack(ItemStack stack, Color color) {
      BaseItemEnum base = BaseItemEnum.fromMaterial(stack.getType());
      if (base == null) {
         return false;
      } else {
         ItemMeta meta = stack.getItemMeta();
         base.color(meta, color);
         stack.setItemMeta(meta);
         return true;
      }
   }

   <T extends ModelRenderer> ModelRendererParser<T> getModelRendererParser(T var1);

   <T extends BehaviorRenderer> BehaviorRendererParser<T> getBehaviorRendererParser(T var1);

   <T extends VisualRenderer> VisualRendererParser<T> getVFXRendererParser(T var1);
}
