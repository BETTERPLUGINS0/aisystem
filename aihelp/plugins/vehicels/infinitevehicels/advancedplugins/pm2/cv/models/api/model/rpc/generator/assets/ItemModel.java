package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.bukkit.NamespacedKey;

public abstract class ItemModel {
   protected final String type;

   public ItemModel(String var1) {
      this.type = var1;
   }

   public static ItemModel.Composite composite(ItemModel... var0) {
      ItemModel.Composite var1 = new ItemModel.Composite();
      var1.models.addAll(List.of(var0));
      return var1;
   }

   public static class Composite extends ItemModel {
      protected final List<ItemModel> models = new ArrayList();

      public Composite() {
         super("minecraft:composite");
      }

      @Generated
      public List<ItemModel> getModels() {
         return this.models;
      }
   }

   public static class Model extends ItemModel {
      protected final String model;
      protected final List<TintSource> tints = new ArrayList();

      public Model(NamespacedKey var1) {
         super("minecraft:model");
         this.model = this.asString(var1);
         this.tints.add(new TintSource.CustomModelData(0));
      }

      private String asString(NamespacedKey var1) {
         String var10000 = var1.getNamespace();
         return var10000 + ":" + var1.getKey();
      }
   }
}
