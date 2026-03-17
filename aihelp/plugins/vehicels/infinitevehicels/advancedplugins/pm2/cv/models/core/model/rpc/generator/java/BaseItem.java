package advancedplugins.pm2.cv.models.core.model.rpc.generator.java;

import advancedplugins.pm2.cv.models.core.model.rpc.generator.ModelIdCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Generated;

public class BaseItem {
   private final List<BaseItem.JavaOverride> overrides = new ArrayList();
   private transient String name;
   private String parent;
   private Map<String, String> textures;

   public void clearOverrides() {
      this.overrides.clear();
   }

   public void addModels(String var1, ModelIdCache var2) {
      var2.sortedIterate((var2x, var3) -> {
         this.overrides.add(new BaseItem.JavaOverride(var1 + ":" + var2x, var3));
      });
   }

   @Generated
   public List<BaseItem.JavaOverride> getOverrides() {
      return this.overrides;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public String getParent() {
      return this.parent;
   }

   @Generated
   public Map<String, String> getTextures() {
      return this.textures;
   }

   @Generated
   public void setName(String var1) {
      this.name = var1;
   }

   @Generated
   public void setParent(String var1) {
      this.parent = var1;
   }

   @Generated
   public void setTextures(Map<String, String> var1) {
      this.textures = var1;
   }

   static class JavaOverride {
      private final BaseItem.JavaPredicate predicate;
      private final String model;

      public JavaOverride(String var1, int var2) {
         this.model = var1;
         this.predicate = new BaseItem.JavaPredicate(var2);
      }

      @Generated
      public BaseItem.JavaPredicate getPredicate() {
         return this.predicate;
      }

      @Generated
      public String getModel() {
         return this.model;
      }
   }

   static class JavaPredicate {
      private final int custom_model_data;

      public JavaPredicate(int var1) {
         this.custom_model_data = var1;
      }

      @Generated
      public int getCustom_model_data() {
         return this.custom_model_data;
      }
   }
}
