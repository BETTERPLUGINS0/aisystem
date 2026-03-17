package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;

public class ModelAssets {
   private final List<BlueprintTexture> textures = new ArrayList();
   private final Map<String, Collection<JavaItemModel>> models = new ConcurrentHashMap();
   private String name;

   @Generated
   public List<BlueprintTexture> getTextures() {
      return this.textures;
   }

   @Generated
   public Map<String, Collection<JavaItemModel>> getModels() {
      return this.models;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public void setName(String var1) {
      this.name = var1;
   }
}
