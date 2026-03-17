package advancedplugins.pm2.cv.models.core.citizens;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import java.util.ArrayList;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CitizensCommand extends AbstractCommand {
   public CitizensCommand(AbstractCommand var1) {
      super(var1);
      this.addSubCommands(new AbstractCommand[]{new CitizensCommand.ModelCommand(this)});
      this.addSubCommands(new AbstractCommand[]{new CitizensCommand.StateCommand(this)});
   }

   private static void getNPCIdTabComplete(List<String> var0) {
      CitizensAPI.getNPCRegistries().forEach((var1) -> {
         var1.sorted().forEach((var2) -> {
            if (var2.isSpawned() && var2.hasTrait(ModelTrait.class)) {
               String var10001 = var1.getName();
               var0.add(var10001 + ":" + var2.getId() + ":[" + var2.getName().replace(" ", "-") + "]");
            }

         });
      });
   }

   private static String tryGetOrDefault(String[] var0, int var1, String var2) {
      return var0.length <= var1 ? var2 : var0[var1];
   }

   private static NPC getNPC(String var0) {
      String[] var1 = var0.split(":");
      if (var1.length < 2) {
         throw new IllegalArgumentException("NPC ID must be formatted as <archive>:<NPC ID>");
      } else {
         NPCRegistry var2 = CitizensAPI.getNamedNPCRegistry(var1[0]);
         if (var2 == null) {
            throw new IllegalArgumentException("Unknown NPC archive: " + var1[0]);
         } else {
            NPC var3 = var2.getById(Integer.parseInt(var1[1]));
            if (var3 == null) {
               throw new IllegalArgumentException("Unknown NPC ID: " + var1[1]);
            } else {
               return var3;
            }
         }
      }
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }

   public String getPermissionNode() {
      return "infinite.command.npc";
   }

   public boolean isConsoleFriendly() {
      return true;
   }

   public String getName() {
      return "npc";
   }

   private static class ModelCommand extends AbstractCommand {
      public ModelCommand(AbstractCommand var1) {
         super(var1);
      }

      public boolean onCommand(CommandSender var1, String[] var2) {
         if (var2.length < 1) {
            return false;
         } else {
            NPC var3 = CitizensCommand.getNPC(var2[0]);
            ModelTrait var4 = (ModelTrait)var3.getTraitNullable(ModelTrait.class);
            if (var4 == null) {
               return false;
            } else if (!var3.isSpawned()) {
               var1.sendMessage(String.valueOf(ChatColor.RED) + "Please spawn the NPC before editing.");
               return true;
            } else {
               IModelContainer var5 = var4.getOrCreateModeledEntity();
               if (var5 == null) {
                  var1.sendMessage(String.valueOf(ChatColor.RED) + "An error occurred while retrieving the model of this NPC.");
                  return true;
               } else if (var2.length < 2) {
                  if (var5.getModels().isEmpty()) {
                     var1.sendMessage("This NPC has no models.");
                  } else {
                     StringBuilder var12 = new StringBuilder("Models: ");
                     var5.getModels().keySet().forEach((var1x) -> {
                        var12.append(var1x).append(", ");
                     });
                     var12.delete(var12.length() - 2, var12.length());
                     var1.sendMessage(var12.toString());
                  }

                  return true;
               } else if (var2.length < 3) {
                  return true;
               } else {
                  String var6 = var2[2];
                  String var7 = var2[1];
                  byte var8 = -1;
                  switch(var7.hashCode()) {
                  case -934610812:
                     if (var7.equals("remove")) {
                        var8 = 1;
                     }
                     break;
                  case 96417:
                     if (var7.equals("add")) {
                        var8 = 0;
                     }
                  }

                  switch(var8) {
                  case 0:
                     boolean var9 = Boolean.parseBoolean(CitizensCommand.tryGetOrDefault(var2, 3, "false"));
                     boolean var10 = Boolean.parseBoolean(CitizensCommand.tryGetOrDefault(var2, 4, "true"));
                     IVisualModel var11 = ModelAPI.create(var6);
                     if (var11 == null) {
                        return false;
                     }

                     var5.setBaseEntityVisible(var9);
                     var5.addModel(var11, var10).ifPresent(IVisualModel::destroy);
                     var1.sendMessage("Added model " + var6 + " to " + var3.getName());
                     break;
                  case 1:
                     var5.removeModel(var6).ifPresent(IVisualModel::destroy);
                     if (var5.getModels().isEmpty()) {
                        var5.setBaseEntityVisible(true);
                     }

                     var1.sendMessage("Removed model " + var6 + " from " + var3.getName());
                     break;
                  default:
                     return false;
                  }

                  return true;
               }
            }
         }
      }

      public List<String> onTabComplete(CommandSender var1, String[] var2) {
         ArrayList var3 = new ArrayList();
         String var4 = var2[var2.length - 1];
         switch(var2.length) {
         case 1:
            CitizensCommand.getNPCIdTabComplete(var3);
            break;
         case 2:
            if ("add".startsWith(var4)) {
               var3.add("add");
            }

            if ("remove".startsWith(var4)) {
               var3.add("remove");
            }
            break;
         case 3:
            String var5 = var2[1];
            byte var6 = -1;
            switch(var5.hashCode()) {
            case -934610812:
               if (var5.equals("remove")) {
                  var6 = 1;
               }
               break;
            case 96417:
               if (var5.equals("add")) {
                  var6 = 0;
               }
            }

            switch(var6) {
            case 0:
               InfiniteModelsCommand.getModelIdTabComplete(var3, var4);
               return var3;
            case 1:
               ModelTrait var7 = (ModelTrait)CitizensCommand.getNPC(var2[0]).getTraitNullable(ModelTrait.class);
               if (var7 != null) {
                  InfiniteModelsCommand.getModelIdTabComplete(var3, var4, var7.getModeledEntity());
               }

               return var3;
            default:
               return var3;
            }
         case 4:
            if ("add".equals(var2[1])) {
               var3.add("[showBaseEntity]");
               if ("true".startsWith(var4)) {
                  var3.add("true");
               }

               if ("false".startsWith(var4)) {
                  var3.add("false");
               }
            }
            break;
         case 5:
            if ("add".equals(var2[1])) {
               var3.add("[overrideHitbox]");
               if ("true".startsWith(var4)) {
                  var3.add("true");
               }

               if ("false".startsWith(var4)) {
                  var3.add("false");
               }
            }
         }

         return var3;
      }

      public String getPermissionNode() {
         return "infinite.command.npc.model";
      }

      public boolean isConsoleFriendly() {
         return true;
      }

      public String getName() {
         return "model";
      }
   }

   private static class StateCommand extends AbstractCommand {
      public StateCommand(AbstractCommand var1) {
         super(var1);
      }

      public boolean onCommand(CommandSender var1, String[] var2) {
         if (var2.length < 1) {
            return false;
         } else {
            NPC var3 = CitizensCommand.getNPC(var2[0]);
            ModelTrait var4 = (ModelTrait)var3.getTraitNullable(ModelTrait.class);
            if (var4 == null) {
               return false;
            } else if (!var3.isSpawned()) {
               var1.sendMessage(String.valueOf(ChatColor.RED) + "Please spawn the NPC before editing.");
               return true;
            } else {
               IModelContainer var5 = var4.getOrCreateModeledEntity();
               if (var5 == null) {
                  var1.sendMessage(String.valueOf(ChatColor.RED) + "An error occurred while retrieving the model of this NPC.");
                  return true;
               } else if (var2.length < 2) {
                  return false;
               } else {
                  IVisualModel var6 = (IVisualModel)var5.getModel(var2[1]).orElse((Object)null);
                  if (var6 == null) {
                     return false;
                  } else if (var2.length < 3) {
                     StringBuilder var14 = new StringBuilder();
                     var14.append(var2[1]).append(": ");
                     var6.getAnimationHandler().getAnimations().values().forEach((var1x) -> {
                        var14.append(var1x.getName()).append(", ");
                     });
                     var14.delete(var14.length() - 2, var14.length());
                     var1.sendMessage(var14.toString());
                     return true;
                  } else if (var2.length < 4) {
                     return false;
                  } else {
                     String var7 = var2[3];
                     String var8 = var2[2];
                     byte var9 = -1;
                     switch(var8.hashCode()) {
                     case -934610812:
                        if (var8.equals("remove")) {
                           var9 = 1;
                        }
                        break;
                     case 96417:
                        if (var8.equals("add")) {
                           var9 = 0;
                        }
                     }

                     switch(var9) {
                     case 0:
                        if (!var6.getBlueprint().getAnimations().containsKey(var7)) {
                           return false;
                        }

                        int var10 = Integer.parseInt(CitizensCommand.tryGetOrDefault(var2, 4, "0"));
                        int var11 = Integer.parseInt(CitizensCommand.tryGetOrDefault(var2, 5, "0"));
                        double var12 = Double.parseDouble(CitizensCommand.tryGetOrDefault(var2, 6, "1"));
                        var6.getAnimationHandler().playAnimation(var7, (double)var10 / 20.0D, (double)var11 / 20.0D, var12, true);
                        var1.sendMessage("Added state " + var7 + " to " + var3.getName());
                        break;
                     case 1:
                        var6.getAnimationHandler().forceStopAnimation(var7);
                        var1.sendMessage("Removed state " + var7 + " from " + var3.getName());
                        break;
                     default:
                        return false;
                     }

                     return true;
                  }
               }
            }
         }
      }

      public List<String> onTabComplete(CommandSender var1, String[] var2) {
         ArrayList var3 = new ArrayList();
         String var4 = var2[var2.length - 1];
         String var9;
         byte var10;
         switch(var2.length) {
         case 1:
            CitizensCommand.getNPCIdTabComplete(var3);
            break;
         case 2:
            ModelTrait var5 = (ModelTrait)CitizensCommand.getNPC(var2[0]).getTraitNullable(ModelTrait.class);
            if (var5 != null) {
               InfiniteModelsCommand.getModelIdTabComplete(var3, var4, var5.getModeledEntity());
            }
            break;
         case 3:
            if ("add".startsWith(var4)) {
               var3.add("add");
            }

            if ("remove".startsWith(var4)) {
               var3.add("remove");
            }
            break;
         case 4:
            ModelTrait var6 = (ModelTrait)CitizensCommand.getNPC(var2[0]).getTraitNullable(ModelTrait.class);
            if (var6 == null) {
               return var3;
            }

            IModelContainer var7 = var6.getModeledEntity();
            IVisualModel var8 = (IVisualModel)var7.getModel(var2[1]).orElse((Object)null);
            if (var8 == null) {
               return var3;
            }

            var9 = var2[2];
            var10 = -1;
            switch(var9.hashCode()) {
            case -934610812:
               if (var9.equals("remove")) {
                  var10 = 1;
               }
               break;
            case 96417:
               if (var9.equals("add")) {
                  var10 = 0;
               }
            }

            switch(var10) {
            case 0:
               InfiniteModelsCommand.getStateTabComplete(var3, var4, (ModelBlueprint)var8.getBlueprint());
               return var3;
            case 1:
               InfiniteModelsCommand.getStateTabComplete(var3, var4, (IVisualModel)var8);
               return var3;
            default:
               return var3;
            }
         case 5:
            var9 = var2[2];
            var10 = -1;
            switch(var9.hashCode()) {
            case -934610812:
               if (var9.equals("remove")) {
                  var10 = 1;
               }
               break;
            case 96417:
               if (var9.equals("add")) {
                  var10 = 0;
               }
            }

            switch(var10) {
            case 0:
               var3.add("[lerpin]");
               return var3;
            case 1:
               var3.add("[ignoreLerp]");
               if ("true".startsWith(var4)) {
                  var3.add("true");
               }

               if ("false".startsWith(var4)) {
                  var3.add("false");
               }

               return var3;
            default:
               return var3;
            }
         case 6:
            if ("add".equals(var2[2])) {
               var3.add("[lerpout]");
            }
            break;
         case 7:
            if ("add".equals(var2[2])) {
               var3.add("[speed]");
            }
         }

         return var3;
      }

      public String getPermissionNode() {
         return "infinite.command.npc.state";
      }

      public boolean isConsoleFriendly() {
         return true;
      }

      public String getName() {
         return "state";
      }
   }
}
