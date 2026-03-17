package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemModelData {
   private final ItemModelData.MultiModels multiModels = new ItemModelData.MultiModels();
   private ItemModelData.SingleComposite singleComposite;
   private boolean isTranslucent;

   public static ItemModelData.Context.ContextBuilder context() {
      return ItemModelData.Context.builder();
   }

   public Collection<ItemStack> createItemStack() {
      return this.createItemStack(ItemModelData.Context.builder().color(Color.WHITE).build());
   }

   public Set<ItemStack> createItemStack(@Nullable ItemModelData.Context var1) {
      return ModelAPI.getNMSHandler().createStack(this, var1);
   }

   @Generated
   public ItemModelData.MultiModels getMultiModels() {
      return this.multiModels;
   }

   @Generated
   public ItemModelData.SingleComposite getSingleComposite() {
      return this.singleComposite;
   }

   @Generated
   public boolean isTranslucent() {
      return this.isTranslucent;
   }

   @Generated
   public void setSingleComposite(ItemModelData.SingleComposite var1) {
      this.singleComposite = var1;
   }

   @Generated
   public void setTranslucent(boolean var1) {
      this.isTranslucent = var1;
   }

   public static class MultiModels {
      private final Map<String, ItemModelData.SubModel> map = new Object2ObjectOpenHashMap();

      public void addSubModel(ItemModelData.SubModel var1) {
         this.map.put(var1.id, var1);
      }

      public ItemModelData.SubModel getSubModel(String var1) {
         return (ItemModelData.SubModel)this.map.get(var1);
      }

      public Collection<ItemModelData.SubModel> getSubModels() {
         return this.map.values();
      }

      public Set<String> getKeys() {
         return this.map.keySet();
      }
   }

   public static record Context(Color color) {
      public Context(Color color) {
         this.color = var1;
      }

      public static ItemModelData.Context.ContextBuilder builder() {
         return new ItemModelData.Context.ContextBuilder();
      }

      public Color color() {
         return this.color;
      }

      public static class ContextBuilder {
         private Color color;

         ContextBuilder() {
         }

         public ItemModelData.Context.ContextBuilder color(Color var1) {
            this.color = var1;
            return this;
         }

         public ItemModelData.Context build() {
            return new ItemModelData.Context(this.color);
         }

         public String toString() {
            return "ItemModelData.Context.ContextBuilder(color=" + String.valueOf(this.color) + ")";
         }
      }
   }

   public static record SingleComposite(NamespacedKey model) {
      public SingleComposite(NamespacedKey model) {
         this.model = var1;
      }

      public NamespacedKey model() {
         return this.model;
      }
   }

   public static class SubModel {
      private final String id;
      private BaseItemEnum item;
      private int data;

      public SubModel(String var1) {
         this.id = var1;
      }

      public String getId() {
         return this.id;
      }

      public BaseItemEnum getItem() {
         return this.item;
      }

      public void setItem(BaseItemEnum var1) {
         this.item = var1;
      }

      public int getData() {
         return this.data;
      }

      public void setData(int var1) {
         this.data = var1;
      }
   }
}
