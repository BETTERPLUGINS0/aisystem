package advancedplugins.pm2.cv.models.core.model.rpc.generator.atlas;

import advancedplugins.pm2.cv.models.api.utils.data.ResourceLocation;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class Atlas {
   private final List<Atlas.Source> sources = new ArrayList();

   @Generated
   public List<Atlas.Source> getSources() {
      return this.sources;
   }

   public static class Source {
      public final String type;

      public Source(String var1) {
         this.type = var1;
      }
   }

   public static class Single extends Atlas.Source {
      public final String resource;
      public final String sprite;

      public Single(String var1) {
         super("single");
         this.resource = var1;
         this.sprite = var1;
      }
   }

   public static class Directory extends Atlas.Source {
      public final String source;
      public final String prefix;

      public Directory(String var1) {
         super("directory");
         this.source = var1;
         this.prefix = var1 + "/";
      }
   }

   public static class Filter extends Atlas.Source {
      public final ResourceLocation pattern;

      public Filter(ResourceLocation var1) {
         super("filter");
         this.pattern = var1;
      }
   }
}
