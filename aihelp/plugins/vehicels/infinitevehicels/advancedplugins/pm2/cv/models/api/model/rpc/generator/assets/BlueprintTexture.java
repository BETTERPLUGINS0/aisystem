package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import advancedplugins.pm2.cv.models.api.utils.data.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

public class BlueprintTexture {
   private int id;
   private int frameWidth;
   private int frameHeight;
   private ResourceLocation path;
   private BlueprintTexture.MCMeta mcMeta;
   private String source;

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public int getFrameWidth() {
      return this.frameWidth;
   }

   public void setFrameWidth(int var1) {
      this.frameWidth = var1;
   }

   public int getFrameHeight() {
      return this.frameHeight;
   }

   public void setFrameHeight(int var1) {
      this.frameHeight = var1;
   }

   public ResourceLocation getPath() {
      return this.path;
   }

   public void setPath(ResourceLocation var1) {
      this.path = var1;
   }

   public BlueprintTexture.MCMeta getMcMeta() {
      return this.mcMeta;
   }

   public void setMcMeta(BlueprintTexture.MCMeta var1) {
      this.mcMeta = var1;
   }

   public String getSource() {
      return this.source;
   }

   public void setSource(String var1) {
      this.source = var1;
   }

   public static class MCMeta {
      private transient boolean mustGenerate;
      private Boolean interpolate;
      private Integer width;
      private Integer height;
      private Integer frametime;
      private List<Object> frames;

      public void addFrame(int var1) {
         if (this.frames == null) {
            this.frames = new ArrayList();
         }

         this.frames.add(var1);
      }

      public void addFrame(int var1, int var2) {
         if (this.frames == null) {
            this.frames = new ArrayList();
         }

         this.frames.add(new BlueprintTexture.MCMeta.Frame(var1, var2));
      }

      public boolean isMustGenerate() {
         return this.mustGenerate;
      }

      public void setMustGenerate(boolean var1) {
         this.mustGenerate = var1;
      }

      public Boolean getInterpolate() {
         return this.interpolate;
      }

      public void setInterpolate(Boolean var1) {
         this.interpolate = var1;
      }

      public Integer getWidth() {
         return this.width;
      }

      public void setWidth(Integer var1) {
         this.width = var1;
      }

      public Integer getHeight() {
         return this.height;
      }

      public void setHeight(Integer var1) {
         this.height = var1;
      }

      public Integer getFrametime() {
         return this.frametime;
      }

      public void setFrametime(Integer var1) {
         this.frametime = var1;
      }

      public List<Object> getFrames() {
         return this.frames;
      }

      public static record Frame(int index, int time) {
         public Frame(int index, int time) {
            this.index = var1;
            this.time = var2;
         }

         public int index() {
            return this.index;
         }

         public int time() {
            return this.time;
         }
      }
   }
}
