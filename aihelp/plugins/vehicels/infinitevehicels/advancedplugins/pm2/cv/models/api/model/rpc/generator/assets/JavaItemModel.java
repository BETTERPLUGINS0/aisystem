package advancedplugins.pm2.cv.models.api.model.rpc.generator.assets;

import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class JavaItemModel {
   private static final float DIST_DIVIDER = 0.041666668F;
   private final Map<String, String> textures = new ConcurrentHashMap();
   private final List<JavaItemModel.JavaElement> elements = new ArrayList();
   private transient String name;
   private transient float maxDistToOrigin = 0.0F;
   private Map<String, Map<String, float[]>> display = new ConcurrentHashMap();

   public JavaItemModel() {
      this.setDisplay(JavaDisplay.GUI, JavaDisplay.Transform.ROTATION, 30.0F, 225.0F, 0.0F);
   }

   public void setDisplay(JavaDisplay var1, JavaDisplay.Transform var2, float var3, float var4, float var5) {
      float[] var6 = (float[])((Map)this.display.computeIfAbsent(var1.toString(), (var0) -> {
         return new ConcurrentHashMap();
      })).computeIfAbsent(var2.toString(), (var0) -> {
         return new float[3];
      });
      var6[0] = var3;
      var6[1] = var4;
      var6[2] = var5;
      var2.sanitize(var6);
   }

   public void addElement(JavaItemModel.JavaElement var1) {
      this.elements.add(var1);

      for(int var2 = 0; var2 < 3; ++var2) {
         this.maxDistToOrigin = Math.max(Math.max(Math.abs(var1.from[var2] - 8.0F), Math.abs(var1.to[var2] - 8.0F)), this.maxDistToOrigin);
      }

   }

   public int scaleToFit() {
      if (this.maxDistToOrigin <= 24.0F) {
         return 1;
      } else {
         int var1 = (int)Math.ceil((double)(this.maxDistToOrigin * 0.041666668F));
         float var2 = 1.0F / (float)var1;
         Iterator var3 = this.elements.iterator();

         while(var3.hasNext()) {
            JavaItemModel.JavaElement var4 = (JavaItemModel.JavaElement)var3.next();
            float[] var5 = var4.getRotation() == null ? null : var4.getRotation().origin;

            for(int var6 = 0; var6 < 3; ++var6) {
               var4.from[var6] = MathUtils.clamp((var4.from[var6] - 8.0F) * var2 + 8.0F, -16.0F, 32.0F);
               var4.to[var6] = MathUtils.clamp((var4.to[var6] - 8.0F) * var2 + 8.0F, -16.0F, 32.0F);
               if (var5 != null) {
                  var5[var6] = (var5[var6] - 8.0F) * var2 + 8.0F;
               }
            }
         }

         return var1;
      }
   }

   public void finalizeModel() {
   }

   public Map<String, String> getTextures() {
      return this.textures;
   }

   public List<JavaItemModel.JavaElement> getElements() {
      return this.elements;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public float getMaxDistToOrigin() {
      return this.maxDistToOrigin;
   }

   public void setMaxDistToOrigin(float var1) {
      this.maxDistToOrigin = var1;
   }

   public Map<String, Map<String, float[]>> getDisplay() {
      return this.display;
   }

   public void setDisplay(Map<String, Map<String, float[]>> var1) {
      this.display = var1;
   }

   public static class JavaElement {
      private final float[] from = new float[3];
      private final float[] to = new float[3];
      private final Map<String, JavaItemModel.JavaElement.Face> faces = new ConcurrentHashMap();
      private JavaItemModel.JavaElement.Rotation rotation;

      public void from(float[] var1, float[] var2, float var3) {
         this.from[0] = var2[0] - var1[0] + 8.0F - var3;
         this.from[1] = var2[1] - var1[1] + 8.0F - var3;
         this.from[2] = var2[2] - var1[2] + 8.0F - var3;
      }

      public void from(float[] var1, float var2) {
         this.from[0] = var1[0] + 8.0F - var2;
         this.from[1] = var1[1] + 8.0F - var2;
         this.from[2] = var1[2] + 8.0F - var2;
      }

      public void to(float[] var1, float[] var2, float var3) {
         this.to[0] = var2[0] - var1[0] + 8.0F + var3;
         this.to[1] = var2[1] - var1[1] + 8.0F + var3;
         this.to[2] = var2[2] - var1[2] + 8.0F + var3;
      }

      public void to(float[] var1, float var2) {
         this.to[0] = var1[0] + 8.0F + var2;
         this.to[1] = var1[1] + 8.0F + var2;
         this.to[2] = var1[2] + 8.0F + var2;
      }

      public float[] getFrom() {
         return this.from;
      }

      public float[] getTo() {
         return this.to;
      }

      public Map<String, JavaItemModel.JavaElement.Face> getFaces() {
         return this.faces;
      }

      public JavaItemModel.JavaElement.Rotation getRotation() {
         return this.rotation;
      }

      public void setRotation(JavaItemModel.JavaElement.Rotation var1) {
         this.rotation = var1;
      }

      public static class Rotation {
         private final float[] origin = new float[]{8.0F, 8.0F, 8.0F};
         private float angle;
         private String axis = "x";

         public void origin(float[] var1, float[] var2) {
            this.origin[0] = var2[0] - var1[0] + 8.0F;
            this.origin[1] = var2[1] - var1[1] + 8.0F;
            this.origin[2] = var2[2] - var1[2] + 8.0F;
         }

         public void origin(float[] var1) {
            this.origin[0] = var1[0] + 8.0F;
            this.origin[1] = var1[1] + 8.0F;
            this.origin[2] = var1[2] + 8.0F;
         }

         public float[] getOrigin() {
            return this.origin;
         }

         public float getAngle() {
            return this.angle;
         }

         public void setAngle(float var1) {
            this.angle = var1;
         }

         public String getAxis() {
            return this.axis;
         }

         public void setAxis(String var1) {
            this.axis = var1;
         }
      }

      public static class Face {
         private final float[] uv = new float[4];
         private final int tintindex = 0;
         private int rotation;
         private String texture = "";

         public void uv(int var1, int var2, float[] var3) {
            float var4 = 16.0F / (float)var1;
            float var5 = 16.0F / (float)var2;
            this.uv[0] = var3[0] * var4;
            this.uv[1] = var3[1] * var5;
            this.uv[2] = var3[2] * var4;
            this.uv[3] = var3[3] * var5;
         }

         public float[] getUv() {
            return this.uv;
         }

         public int getTintindex() {
            Objects.requireNonNull(this);
            return 0;
         }

         public int getRotation() {
            return this.rotation;
         }

         public void setRotation(int var1) {
            this.rotation = var1;
         }

         public String getTexture() {
            return this.texture;
         }

         public void setTexture(String var1) {
            this.texture = var1;
         }
      }
   }
}
