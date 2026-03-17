package advancedplugins.pm2.cv.models.core.animation.script;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.script.ScriptReader;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag;
import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import org.bukkit.Color;

public class InfiniteModelSR extends AbstractArchive<BiConsumer<IVisualModel, Map<String, String>>> implements ScriptReader {
   private boolean shouldPrintWarning;

   public InfiniteModelSR() {
      ModelAPI.getAPI().getConfigManager().registerReferenceUpdate(this::updateConfig);
      this.register("changeparent", this::changeParent);
      this.register("partvis", this::partVisibility);
      this.register("tint", this::tint);
      this.register("enchant", this::enchant);
      this.register("tag", this::tag);
      this.register("changepart", this::changePart);
      this.register("remap", this::remap);
   }

   public void updateConfig() {
      this.shouldPrintWarning = ConfigProperty.SCRIPT_WARNING.getBoolean();
   }

   public void read(IAnimationProperty var1, String var2) {
      String[] var3 = var2.split("\\{", 2);
      String var4 = var3[0].toLowerCase(Locale.ENGLISH);
      BiConsumer var5 = (BiConsumer)this.get(var4);
      if (var5 == null) {
         if (this.shouldPrintWarning) {
            LogUtil.warn("Unknown script: " + var2);
         }
      } else if (var3.length != 2) {
         if (this.shouldPrintWarning) {
            LogUtil.warn("Invalid script: " + var2);
         }
      } else {
         IVisualModel var6 = var1.getModel();
         ModelBlueprint var7 = var6.getBlueprint();
         ConcurrentHashMap var8 = new ConcurrentHashMap();
         String[] var9 = var3[1].substring(0, var3[1].length() - 1).split(";");
         String[] var10 = var9;
         int var11 = var9.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            String var13 = var10[var12];
            String[] var14 = var13.split("=", 2);
            var8.put(var14[0].strip().toLowerCase(Locale.ENGLISH), var14.length == 2 ? this.getAnimationPlaceholder(var7, var14[1].strip()) : "");
         }

         var5.accept(var6, var8);
      }

   }

   private String getAnimationPlaceholder(ModelBlueprint var1, String var2) {
      if (var2.startsWith("<") && var2.endsWith(">")) {
         String var3 = var2.substring(1, var2.length() - 1);
         return (String)var1.getAnimationsPlaceholders().getOrDefault(var3, var2);
      } else {
         return var2;
      }
   }

   private void changeParent(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("parent");
      String var4 = (String)var2.get("child");
      var1.getJoint(var3).ifPresent((var2x) -> {
         var1.getJoint(var4).ifPresent((var1x) -> {
            var1x.setParent(var2x);
         });
      });
   }

   private void partVisibility(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("part");
      boolean var4 = Boolean.parseBoolean((String)var2.get("visible"));
      boolean var5 = Boolean.parseBoolean((String)var2.get("exact"));
      if (var5) {
         var1.getJoint(var3).ifPresent((var1x) -> {
            var1x.setVisible(var4);
         });
      } else {
         Iterator var6 = var1.getJoints().entrySet().iterator();

         while(var6.hasNext()) {
            Entry var7 = (Entry)var6.next();
            if (((String)var7.getKey()).contains(var3)) {
               ((IJoint)var7.getValue()).setVisible(var4);
            }
         }
      }

   }

   private void tint(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("part");
      String var4 = (String)var2.get("color");
      if (var4.startsWith("#")) {
         var4 = var4.substring(1);
      }

      Color var5 = Color.fromRGB(Integer.parseInt(var4, 16));
      boolean var6 = Boolean.parseBoolean((String)var2.get("exact"));
      boolean var7 = Boolean.parseBoolean((String)var2.get("damage"));
      if (var3.isBlank()) {
         if (var7) {
            var1.setDamageTint(var5);
         } else {
            var1.setDefaultTint(var5);
         }
      } else if (var6) {
         var1.getJoint(var3).ifPresent((var2x) -> {
            if (var7) {
               var2x.setDamageTint(var5);
            } else {
               var2x.setDefaultTint(var5);
            }

         });
      } else {
         Iterator var8 = var1.getJoints().entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            if (((String)var9.getKey()).contains(var3)) {
               if (var7) {
                  ((IJoint)var9.getValue()).setDamageTint(var5);
               } else {
                  ((IJoint)var9.getValue()).setDefaultTint(var5);
               }
            }
         }
      }

   }

   private void enchant(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("part");
      boolean var4 = Boolean.parseBoolean((String)var2.get("enchant"));
      boolean var5 = Boolean.parseBoolean((String)var2.get("exact"));
      if (var3 != null && !var3.isBlank()) {
         if (var5) {
            var1.getJoint(var3).ifPresent((var1x) -> {
               var1x.setEnchanted(var4);
            });
         } else {
            var1.getJoints().entrySet().stream().filter((var1x) -> {
               return ((String)var1x.getKey()).contains(var3);
            }).forEach((var1x) -> {
               ((IJoint)var1x.getValue()).setEnchanted(var4);
            });
         }

      } else {
         var1.getJoints().values().forEach((var1x) -> {
            var1x.setEnchanted(var4);
         });
      }
   }

   private void tag(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("part");
      String var4 = (String)var2.get("tag");
      boolean var5 = Boolean.parseBoolean((String)var2.getOrDefault("visible", "true"));
      var1.getJoint(var3).flatMap((var0) -> {
         return var0.getJointAction(JointBehaviorTypes.NAMETAG);
      }).ifPresent((var2x) -> {
         ((NameTag)var2x).setString(var4);
         ((NameTag)var2x).setVisible(var5);
      });
   }

   private void changePart(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("part");
      String var4 = (String)var2.get("nmodel");
      String var5 = (String)var2.get("npart");
      var1.getJoint(var3).ifPresent((var2x) -> {
         if (var2x.isRenderer()) {
            ModelBlueprint var3 = ModelAPI.getBlueprint(var4);
            if (var3 != null) {
               BlueprintJoint var4x = (BlueprintJoint)var3.getFlatMap().get(var5);
               if (var4x != null && var4x.isRenderer()) {
                  var2x.setModelScale(var4x.getScale());
                  var2x.setModel(var4x);
               }
            }
         }

      });
   }

   private void remap(IVisualModel var1, Map<String, String> var2) {
      String var3 = (String)var2.get("model");
      String var4 = (String)var2.get("map");
      ModelBlueprint var5 = ModelAPI.getBlueprint(var3);
      if (var5 != null) {
         if (var4 != null) {
            this.remapWithMap(var1, var5, var4);
         } else {
            this.remapDirect(var1, var5);
         }

      }
   }

   private void remapWithMap(IVisualModel var1, ModelBlueprint var2, String var3) {
      ModelBlueprint var4 = ModelAPI.getBlueprint(var3);
      if (var4 != null) {
         var4.getFlatMap().keySet().forEach((var2x) -> {
            BlueprintJoint var3 = (BlueprintJoint)var2.getFlatMap().get(var2x);
            if (var3 != null && var3.isRenderer()) {
               var1.getJoint(var2x).ifPresent((var1x) -> {
                  if (var1x.isRenderer()) {
                     var1x.setModel(var3);
                  }

               });
            }

         });
      }
   }

   private void remapDirect(IVisualModel var1, ModelBlueprint var2) {
      Set var3 = var2.getFlatMap().size() < var1.getJoints().size() ? var2.getFlatMap().keySet() : var1.getJoints().keySet();
      var3.forEach((var2x) -> {
         BlueprintJoint var3 = (BlueprintJoint)var2.getFlatMap().get(var2x);
         if (var3 != null && var3.isRenderer()) {
            var1.getJoint(var2x).ifPresent((var1x) -> {
               if (var1x.isRenderer()) {
                  var1x.setModelScale(var3.getScale());
                  var1x.setModel(var3);
               }

            });
         } else {
            String var10000 = var2.getName();
            LogUtil.log(var10000 + ": " + var2x);
         }

      });
   }
}
