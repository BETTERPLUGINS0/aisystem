package advancedplugins.pm2.cv.models.core.model.rpc.generator.parser.blockbench.json;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorCollector;
import advancedplugins.pm2.cv.models.api.model.rpc.error.IError;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchBehaviorParser;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.PlayerLimb;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.joml.Vector3f;

public class DefaultBehaviorParser implements BlockbenchBehaviorParser {
   public void processModel(ErrorCollector var1, BlockbenchModel var2, ModelBlueprint var3) {
      this.searchJoint(var2, "hitbox", (var4) -> {
         Hitbox var5 = this.readHitbox(var1, var2, var4);
         if (var5 == null) {
            return false;
         } else {
            var3.setMainHitbox(var5);
            return true;
         }
      }, () -> {
         IError.NO_HITBOX.log(var1);
      });
      this.searchJoint(var2, "shadow", (var3x) -> {
         float var4 = this.readShadow(var2, var3x);
         if ((double)var4 < 1.0E-5D) {
            return false;
         } else {
            var3.setShadowRadius(var4);
            return true;
         }
      }, (Runnable)null);
   }

   public void processJoint(ErrorCollector var1, BlockbenchModel var2, BlockbenchModel.Group var3, BlueprintJoint var4) {
      String var5 = this.readOptions(var2, var3, var4);
      var4.setName(var5);
   }

   private void searchJoint(BlockbenchModel var1, String var2, Predicate<BlockbenchModel.Group> var3, Runnable var4) {
      BlockbenchModel.Group var5 = var1.getGroup(var2);
      if (var5 != null && var3.test(var5)) {
         var1.removeGroup(var5);
      } else if (var4 != null) {
         var4.run();
      }

   }

   private Hitbox readHitbox(ErrorCollector var1, BlockbenchModel var2, BlockbenchModel.Group var3) {
      Float[] var4 = var3.getOrigin();
      Iterator var5 = var3.getElement().iterator();

      BlockbenchModel.Element var7;
      do {
         if (!var5.hasNext()) {
            return null;
         }

         UUID var6 = (UUID)var5.next();
         var7 = (BlockbenchModel.Element)var2.getElements().get(var6);
      } while(!(var7 instanceof BlockbenchModel.Cube));

      BlockbenchModel.Cube var8 = (BlockbenchModel.Cube)var7;
      Float[] var9 = var8.getOrigin();
      float var10 = var4[1] <= 0.0F ? var9[1] : var4[1];
      if (var10 <= 0.0F) {
         IError.BAD_EYE_HEIGHT.log(var1);
      }

      return new Hitbox((double)(var8.width() * 0.0625F), (double)(var8.height() * 0.0625F), (double)(var8.depth() * 0.0625F), (double)(var10 * 0.0625F));
   }

   private float readShadow(BlockbenchModel var1, BlockbenchModel.Group var2) {
      Iterator var3 = var2.getElement().iterator();

      BlockbenchModel.Element var5;
      do {
         if (!var3.hasNext()) {
            return -1.0F;
         }

         UUID var4 = (UUID)var3.next();
         var5 = (BlockbenchModel.Element)var1.getElements().get(var4);
      } while(!(var5 instanceof BlockbenchModel.Cube));

      BlockbenchModel.Cube var6 = (BlockbenchModel.Cube)var5;
      return Math.max(var6.width(), var6.depth()) * 0.03125F;
   }

   private String readOptions(BlockbenchModel var1, BlockbenchModel.Group var2, BlueprintJoint var3) {
      String var4 = var3.getName();
      Map var5 = var3.getBehaviors();
      if (var4.startsWith("h_")) {
         var4 = var4.substring(2);
         var5.put(JointBehaviorTypes.HEAD.getId(), new ConcurrentHashMap());
      } else if (var4.startsWith("hi_")) {
         var4 = var4.substring(3);
         var5.put(JointBehaviorTypes.HEAD.getId(), new ConcurrentHashMap<String, Object>() {
            {
               this.put("inherited", true);
            }
         });
      }

      if (var4.equals("mount")) {
         var5.put(JointBehaviorTypes.MOUNT.getId(), new ConcurrentHashMap<String, Object>() {
            {
               this.put("driver", true);
            }
         });
      } else {
         boolean var6 = var4.startsWith("ob_");
         if (!var6 && !var4.startsWith("b_")) {
            String[] var14 = var4.split("_");
            int var15;
            String var16;
            byte var17;
            StringBuilder var18;
            int var19;
            if (!var2.getElement().isEmpty()) {
               for(var15 = 0; var15 < var14.length; ++var15) {
                  var16 = var14[var15];
                  var17 = -1;
                  switch(var16.hashCode()) {
                  case 106463762:
                     if (var16.equals("pbody")) {
                        var17 = 3;
                     }
                     break;
                  case 106632784:
                     if (var16.equals("phead")) {
                        var17 = 0;
                     }
                     break;
                  case 106748640:
                     if (var16.equals("plarm")) {
                        var17 = 2;
                     }
                     break;
                  case 106758802:
                     if (var16.equals("plleg")) {
                        var17 = 5;
                     }
                     break;
                  case 106927386:
                     if (var16.equals("prarm")) {
                        var17 = 1;
                     }
                     break;
                  case 106937548:
                     if (var16.equals("prleg")) {
                        var17 = 4;
                     }
                  }

                  switch(var17) {
                  case 0:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.HEAD);
                        }
                     });
                     break;
                  case 1:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.RIGHT_ARM);
                        }
                     });
                     break;
                  case 2:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.LEFT_ARM);
                        }
                     });
                     break;
                  case 3:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.BODY);
                        }
                     });
                     break;
                  case 4:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.RIGHT_LEG);
                        }
                     });
                     break;
                  case 5:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.LEFT_LEG);
                        }
                     });
                     break;
                  default:
                     var18 = new StringBuilder(var16);

                     for(var19 = var15 + 1; var19 < var14.length; ++var19) {
                        var18.append("_").append(var14[var19]);
                     }

                     return var18.toString();
                  }
               }
            } else {
               for(var15 = 0; var15 < var14.length; ++var15) {
                  var16 = var14[var15];
                  var17 = -1;
                  switch(var16.hashCode()) {
                  case 103:
                     if (var16.equals("g")) {
                        var17 = 0;
                     }
                     break;
                  case 108:
                     if (var16.equals("l")) {
                        var17 = 6;
                     }
                     break;
                  case 112:
                     if (var16.equals("p")) {
                        var17 = 2;
                     }
                     break;
                  case 3359:
                     if (var16.equals("ih")) {
                        var17 = 5;
                     }
                     break;
                  case 3363:
                     if (var16.equals("il")) {
                        var17 = 4;
                     }
                     break;
                  case 3369:
                     if (var16.equals("ir")) {
                        var17 = 3;
                     }
                     break;
                  case 3704:
                     if (var16.equals("tl")) {
                        var17 = 14;
                     }
                     break;
                  case 113749:
                     if (var16.equals("seg")) {
                        var17 = 13;
                     }
                     break;
                  case 114586:
                     if (var16.equals("tag")) {
                        var17 = 1;
                     }
                     break;
                  case 106463762:
                     if (var16.equals("pbody")) {
                        var17 = 10;
                     }
                     break;
                  case 106632784:
                     if (var16.equals("phead")) {
                        var17 = 7;
                     }
                     break;
                  case 106748640:
                     if (var16.equals("plarm")) {
                        var17 = 9;
                     }
                     break;
                  case 106758802:
                     if (var16.equals("plleg")) {
                        var17 = 12;
                     }
                     break;
                  case 106927386:
                     if (var16.equals("prarm")) {
                        var17 = 8;
                     }
                     break;
                  case 106937548:
                     if (var16.equals("prleg")) {
                        var17 = 11;
                     }
                  }

                  switch(var17) {
                  case 0:
                     var5.put(JointBehaviorTypes.GHOST.getId(), new ConcurrentHashMap());
                     break;
                  case 1:
                     var5.put(JointBehaviorTypes.NAMETAG.getId(), new ConcurrentHashMap());
                     break;
                  case 2:
                     var5.put(JointBehaviorTypes.MOUNT.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("driver", false);
                        }
                     });
                     break;
                  case 3:
                     var5.put(JointBehaviorTypes.ITEM.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("display", ItemDisplayTransform.THIRDPERSON_RIGHTHAND);
                        }
                     });
                     break;
                  case 4:
                     var5.put(JointBehaviorTypes.ITEM.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("display", ItemDisplayTransform.THIRDPERSON_LEFTHAND);
                        }
                     });
                     break;
                  case 5:
                     var5.put(JointBehaviorTypes.ITEM.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("display", ItemDisplayTransform.HEAD);
                        }
                     });
                     break;
                  case 6:
                     var5.put(JointBehaviorTypes.LEASH.getId(), new ConcurrentHashMap());
                     break;
                  case 7:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.HEAD);
                        }
                     });
                     break;
                  case 8:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.RIGHT_ARM);
                        }
                     });
                     break;
                  case 9:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.LEFT_ARM);
                        }
                     });
                     break;
                  case 10:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.BODY);
                        }
                     });
                     break;
                  case 11:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.RIGHT_LEG);
                        }
                     });
                     break;
                  case 12:
                     var5.put(JointBehaviorTypes.PLAYER_LIMB.getId(), new ConcurrentHashMap<String, Object>() {
                        {
                           this.put("limb", PlayerLimb.Limb.LEFT_LEG);
                        }
                     });
                     break;
                  case 13:
                     var5.put(JointBehaviorTypes.SEGMENT.getId(), new ConcurrentHashMap());
                     break;
                  case 14:
                     var5.put(JointBehaviorTypes.TAIL.getId(), new ConcurrentHashMap());
                     break;
                  default:
                     var18 = new StringBuilder(var16);

                     for(var19 = var15 + 1; var19 < var14.length; ++var19) {
                        var18.append("_").append(var14[var19]);
                     }

                     return var18.toString();
                  }
               }
            }
         } else {
            ConcurrentHashMap var7 = new ConcurrentHashMap();
            if (var6) {
               var7.put("obb", true);
            }

            Iterator var8 = var2.getElement().iterator();

            while(var8.hasNext()) {
               UUID var9 = (UUID)var8.next();
               BlockbenchModel.Element var10 = (BlockbenchModel.Element)var1.getElements().get(var9);
               if (var10 instanceof BlockbenchModel.Cube) {
                  BlockbenchModel.Cube var11 = (BlockbenchModel.Cube)var10;
                  var7.put("dimension", new Hitbox((double)var11.width() * 0.0625D, (double)var11.height() * 0.0625D, (double)var11.depth() * 0.0625D, 0.0D));
                  Float[] var12 = var11.getFrom();
                  Float[] var13 = var11.getTo();
                  var7.put("origin", new Vector3f((var12[0] + var13[0]) * 0.03125F, (var12[1] + var13[1]) * 0.03125F, (var12[2] + var13[2]) * 0.03125F));
                  break;
               }
            }

            var5.put(JointBehaviorTypes.SUB_HITBOX.getId(), var7);
         }
      }

      return var4;
   }
}
