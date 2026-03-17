package advancedplugins.pm2.cv.models.core.model.rpc.generator.atlas;

import advancedplugins.pm2.cv.models.api.utils.FileUtils;
import advancedplugins.pm2.cv.models.api.utils.data.ResourceLocation;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.ModelGeneratorImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;

public class AtlasManager {
   private final ModelGeneratorImpl generator;
   private final File atlases;
   private final Set<ResourceLocation> registeredPaths = new HashSet();
   private final Atlas atlas;

   public AtlasManager(ModelGeneratorImpl var1) {
      this.generator = var1;
      this.atlases = FileUtils.createDirectory(var1.getPackFolder(), "assets", "minecraft", "atlases");
      this.atlas = new Atlas();
      this.reset();
   }

   public void reset() {
      this.registeredPaths.clear();
      this.atlas.getSources().clear();
      this.atlas.getSources().add(new Atlas.Directory("entity"));
      this.atlas.getSources().add(new Atlas.Filter(new ResourceLocation("minecraft", "entity/fishing_hook")));
   }

   public void addSingle(ResourceLocation var1) {
      String var2 = var1.getPath();
      if (!var2.startsWith("entity") && !var2.startsWith("item") && !var2.startsWith("block") && this.registeredPaths.add(var1)) {
         this.atlas.getSources().add(new Atlas.Single(var1.toString()));
      }

   }

   public void generateFile() {
      try {
         File var1 = FileUtils.createFile(this.atlases, "blocks.json");
         FileWriter var2 = new FileWriter(var1);
         var2.write(this.generator.getGson().toJson(this.atlas));
         var2.close();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   @Generated
   public Atlas getAtlas() {
      return this.atlas;
   }
}
