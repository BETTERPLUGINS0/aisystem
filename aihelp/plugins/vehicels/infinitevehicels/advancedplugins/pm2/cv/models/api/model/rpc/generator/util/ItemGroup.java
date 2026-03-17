package advancedplugins.pm2.cv.models.api.model.rpc.generator.util;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.BlueprintTexture;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.JavaDisplay;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.JavaItemModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ModelAssets;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.utils.Utils;
import advancedplugins.pm2.cv.models.api.utils.math.Direction;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public record ItemGroup(int debug, Quaterniond displayQuaternion, Vector3d displayRotation, List<ProcessedJoint.Cube> cubes) {
   public ItemGroup(int debug, Quaterniond displayQuaternion, Vector3d displayRotation, List<ProcessedJoint.Cube> cubes) {
      this.debug = var1;
      this.displayQuaternion = var2;
      this.displayRotation = var3;
      this.cubes = var4;
   }

   public JavaItemModel toJavaItemModel(String var1, BlockbenchModel var2, ModelAssets var3) {
      JavaItemModel var4 = new JavaItemModel();
      var4.setName(var1);
      Iterator var5 = this.cubes.iterator();

      while(var5.hasNext()) {
         ProcessedJoint.Cube var6 = (ProcessedJoint.Cube)var5.next();
         JavaItemModel.JavaElement var7 = new JavaItemModel.JavaElement();
         var7.from(MathUtils.unwrap(var6.getFrom()), var6.getInflate());
         var7.to(MathUtils.unwrap(var6.getTo()), var6.getInflate());
         var7.setRotation(var6.rotation());
         Iterator var8 = var6.getFaces().entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            Direction var10 = (Direction)var9.getKey();
            ProcessedJoint.Face var11 = (ProcessedJoint.Face)var9.getValue();
            if (!var11.isEmpty()) {
               int var12 = var11.texture();
               if (var12 < var3.getTextures().size()) {
                  BlockbenchModel.Texture var13 = (BlockbenchModel.Texture)var2.getTextures().get(var12);
                  JavaItemModel.JavaElement.Face var14 = new JavaItemModel.JavaElement.Face();
                  var14.setRotation(var11.uv().rotation());
                  var14.uv((Integer)Utils.or(var13.getUv_width(), var2.getResolution().getWidth()), (Integer)Utils.or(var13.getUv_height(), var2.getResolution().getHeight()), new float[]{var11.uv().u1(), var11.uv().v1(), var11.uv().u2(), var11.uv().v2()});
                  var14.setTexture("#" + var12);
                  var7.getFaces().put(var10.name().toLowerCase(Locale.ENGLISH), var14);
                  Map var15 = var4.getTextures();
                  var15.computeIfAbsent(String.valueOf(var12), (var2x) -> {
                     return ((BlueprintTexture)var3.getTextures().get(var12)).getPath().toString();
                  });
                  if (var15.size() == 1) {
                     var15.computeIfAbsent("particle", (var1x) -> {
                        return "#" + var12;
                     });
                  }
               }
            }
         }

         var4.addElement(var7);
      }

      var4.setDisplay(JavaDisplay.THIRDPERSON_RIGHTHAND, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)this.displayRotation.y, (float)this.displayRotation.z);
      var4.setDisplay(JavaDisplay.FIRSTPERSON_RIGHTHAND, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)this.displayRotation.y, (float)this.displayRotation.z);
      var4.setDisplay(JavaDisplay.GROUND, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)this.displayRotation.y, (float)this.displayRotation.z);
      var4.setDisplay(JavaDisplay.HEAD, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)this.displayRotation.y, (float)this.displayRotation.z);
      var4.setDisplay(JavaDisplay.FIXED, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)this.displayRotation.y, (float)this.displayRotation.z);
      Vector3d var16 = MathUtils.toEulerXYZ(MathUtils.fromEulerXYZ(new Vector3d(30.0D, 225.0D, 0.0D)).mul(this.displayQuaternion));
      var4.setDisplay(JavaDisplay.GUI, JavaDisplay.Transform.ROTATION, (float)var16.x, (float)var16.y, (float)var16.z);
      var4.setDisplay(JavaDisplay.THIRDPERSON_LEFTHAND, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)(-this.displayRotation.y), (float)(-this.displayRotation.z));
      var4.setDisplay(JavaDisplay.FIRSTPERSON_LEFTHAND, JavaDisplay.Transform.ROTATION, (float)this.displayRotation.x, (float)(-this.displayRotation.y), (float)(-this.displayRotation.z));
      return var4;
   }

   public int debug() {
      return this.debug;
   }

   public Quaterniond displayQuaternion() {
      return this.displayQuaternion;
   }

   public Vector3d displayRotation() {
      return this.displayRotation;
   }

   public List<ProcessedJoint.Cube> cubes() {
      return this.cubes;
   }
}
