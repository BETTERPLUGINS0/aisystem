package advancedplugins.pm2.cv.models.core.model.rpc.generator.processed;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.JavaItemModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ModelAssets;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.util.ItemGroup;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.util.RotationSolver;
import advancedplugins.pm2.cv.models.api.utils.math.Direction;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ProcessedJoint {
   private final String name;
   private final Vector3f jointOrigin;
   private final Vector3f rotation;
   private final Set<ProcessedJoint.Cube> cubes = new LinkedHashSet();
   private final Set<ItemGroup> groups = new LinkedHashSet();
   private final Set<JavaItemModel> models = new LinkedHashSet();
   private int scale;

   public ProcessedJoint(String var1, Vector3f var2, Vector3f var3) {
      this.name = var1;
      this.jointOrigin = var2;
      this.rotation = var3;
   }

   public void splitModels(BlockbenchModel var1, ModelAssets var2) {
      RotationSolver.solve(this.groups, this.cubes);
      float var3 = 0.0F;
      Iterator var4 = this.groups.iterator();

      while(var4.hasNext()) {
         ItemGroup var5 = (ItemGroup)var4.next();
         JavaItemModel var6 = var5.toJavaItemModel(this.name, var1, var2);
         var3 = Math.max(var6.getMaxDistToOrigin(), var3);
         this.models.add(var6);
      }

      JavaItemModel var8;
      for(Iterator var7 = this.models.iterator(); var7.hasNext(); this.scale = var8.scaleToFit()) {
         var8 = (JavaItemModel)var7.next();
         var8.setMaxDistToOrigin(var3);
      }

   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Vector3f getJointOrigin() {
      return this.jointOrigin;
   }

   @Generated
   public Vector3f getRotation() {
      return this.rotation;
   }

   @Generated
   public Set<ProcessedJoint.Cube> getCubes() {
      return this.cubes;
   }

   @Generated
   public Set<ItemGroup> getGroups() {
      return this.groups;
   }

   @Generated
   public Set<JavaItemModel> getModels() {
      return this.models;
   }

   @Generated
   public int getScale() {
      return this.scale;
   }

   public static class Cube {
      private final String name;
      private final Vector3d origin;
      private final Vector3d rotation;
      private final Quaterniond quaternion;
      private final Vector3d from;
      private final Vector3d to;
      private final Map<Direction, ProcessedJoint.Face> faces;
      private final float inflate;

      public Cube(String var1, Vector3d var2, Vector3d var3, Vector3d var4, Vector3d var5, Map<Direction, ProcessedJoint.Face> var6, float var7) {
         this.name = var1;
         this.origin = new Vector3d(var2);
         this.rotation = new Vector3d(var3);
         this.quaternion = MathUtils.fromEulerZYX(var3);
         this.from = new Vector3d(var4);
         this.to = new Vector3d(var5);
         this.faces = var6;
         this.inflate = var7;
      }

      public List<Vector3d> getCorners() {
         return List.of(new Vector3d(this.from.x, this.from.y, this.from.z), new Vector3d(this.from.x, this.from.y, this.to.z), new Vector3d(this.from.x, this.to.y, this.from.z), new Vector3d(this.from.x, this.to.y, this.to.z), new Vector3d(this.to.x, this.from.y, this.from.z), new Vector3d(this.to.x, this.from.y, this.to.z), new Vector3d(this.to.x, this.to.y, this.from.z), new Vector3d(this.to.x, this.to.y, this.to.z));
      }

      public void rotate(Quaterniond var1) {
         this.from.sub(this.origin);
         this.to.sub(this.origin);
         this.origin.rotate(var1);
         this.quaternion.premul(var1);
         this.rotation.set(MathUtils.fixEuler(MathUtils.toEulerZYX(this.quaternion)));
         this.from.add(this.origin);
         this.to.add(this.origin);
      }

      @Nullable
      public JavaItemModel.JavaElement.Rotation rotation() {
         int var1 = 0;
         float[] var2 = MathUtils.unwrap(this.origin);
         float[] var3 = MathUtils.unwrap(this.rotation);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            float var6 = var3[var5];
            var1 += var6 == 0.0F ? 1 : 0;
         }

         if (var1 == 3) {
            return null;
         } else {
            JavaItemModel.JavaElement.Rotation var8 = new JavaItemModel.JavaElement.Rotation();
            var4 = MathUtils.absMax(var3[0], var3[1], var3[2]);
            String var10000;
            switch(var4) {
            case 1:
               var10000 = "y";
               break;
            case 2:
               var10000 = "z";
               break;
            default:
               var10000 = "x";
            }

            String var9 = var10000;
            var8.setAxis(var9);
            float var7 = (float)Math.round(var3[var4] / 22.5F) * 22.5F;
            var8.setAngle(var7);
            var8.origin(var2);
            return var8;
         }
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public Vector3d getOrigin() {
         return this.origin;
      }

      @Generated
      public Vector3d getRotation() {
         return this.rotation;
      }

      @Generated
      public Quaterniond getQuaternion() {
         return this.quaternion;
      }

      @Generated
      public Vector3d getFrom() {
         return this.from;
      }

      @Generated
      public Vector3d getTo() {
         return this.to;
      }

      @Generated
      public Map<Direction, ProcessedJoint.Face> getFaces() {
         return this.faces;
      }

      @Generated
      public float getInflate() {
         return this.inflate;
      }
   }

   public static record Face(ProcessedJoint.UV uv, int texture) {
      public Face(ProcessedJoint.UV uv, int texture) {
         this.uv = var1;
         this.texture = var2;
      }

      public boolean isEmpty() {
         return MathUtils.isSimilar(this.uv.u1(), this.uv.u2()) || MathUtils.isSimilar(this.uv.v1, this.uv.v2);
      }

      public ProcessedJoint.UV uv() {
         return this.uv;
      }

      public int texture() {
         return this.texture;
      }
   }

   public static record UV(float u1, float v1, float u2, float v2, int rotation) {
      public UV(float u1, float v1, float u2, float v2, int rotation) {
         this.u1 = var1;
         this.v1 = var2;
         this.u2 = var3;
         this.v2 = var4;
         this.rotation = var5;
      }

      public float u1() {
         return this.u1;
      }

      public float v1() {
         return this.v1;
      }

      public float u2() {
         return this.u2;
      }

      public float v2() {
         return this.v2;
      }

      public int rotation() {
         return this.rotation;
      }
   }
}
