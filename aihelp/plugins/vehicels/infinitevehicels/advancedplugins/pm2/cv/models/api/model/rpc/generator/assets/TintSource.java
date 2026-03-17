package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import com.google.gson.annotations.SerializedName;

public abstract class TintSource {
   protected final String type;

   public TintSource(String var1) {
      this.type = var1;
   }

   public static class CustomModelData extends TintSource {
      protected final int index;
      @SerializedName("default")
      protected final int def = 16777215;

      public CustomModelData(int var1) {
         super("minecraft:custom_model_data");
         this.index = var1;
      }
   }
}
