package advancedplugins.pm2.cv.models.core.command.sub;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.command.AbstractCommand;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.core.command.InfiniteModelsCommand;
import advancedplugins.pm2.cv.models.core.command.ModelOptionParser;
import advancedplugins.pm2.cv.models.core.model.nrpc.Model;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.JointImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.renderer.DisplayRendererImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DevCommand extends AbstractCommand {
   public DevCommand(AbstractCommand var1) {
      super(var1);
   }

   public boolean onCommand(CommandSender var1, String[] var2) {
      if (var2.length < 1) {
         return false;
      } else {
         ModelBlueprint var3 = ModelAPI.getBlueprint(var2[0]);
         if (var3 == null) {
            return false;
         } else {
            Player var4 = (Player)var1;
            Model var5 = new Model(var3.getName(), var3.getBlockbenchModel());
            var5.setLocation(var4.getLocation());
            var5.spawn();
            if (var2.length >= 2) {
               String var6 = var2[1];
               if (!var5.playAnimation(var6)) {
                  String var10001 = String.valueOf(ChatColor.RED);
                  var1.sendMessage(var10001 + "Animation not found: " + var6);
                  return false;
               }
            }

            return true;
         }
      }
   }

   private DevCommand.PlacementResult calculatePlacement(Block var1, BlockFace var2, Player var3) {
      Location var4 = this.determineTargetLocation(var1, var2);
      if (var4 == null) {
         return this.createFailureResult();
      } else if (this.isDownwardPlacement(var2) && !this.isLocationEmpty(var4)) {
         return this.createFailureResult();
      } else if (!this.hasPlacementPermission(var4, var3)) {
         return this.createFailureResult();
      } else {
         BlockFace var5 = getCardinalDirection(var3.getLocation().getYaw());
         return this.createSuccessResult(var4, var5);
      }
   }

   private Location determineTargetLocation(Block var1, BlockFace var2) {
      return var2 != BlockFace.UP && var2.getModY() != 0 && var2 != BlockFace.DOWN ? null : var1.getRelative(var2).getLocation();
   }

   private boolean isDownwardPlacement(BlockFace var1) {
      return var1 == BlockFace.DOWN;
   }

   private boolean isLocationEmpty(Location var1) {
      return var1.getBlock().getType() == Material.AIR;
   }

   private boolean hasPlacementPermission(Location var1, Player var2) {
      return true;
   }

   private DevCommand.PlacementResult createFailureResult() {
      return new DevCommand.PlacementResult(false, (Location)null, (BlockFace)null);
   }

   private DevCommand.PlacementResult createSuccessResult(Location var1, BlockFace var2) {
      return new DevCommand.PlacementResult(true, var1, var2);
   }

   private void spawnCustomModel(IVisualModel var1, Player var2) {
      Block var3 = var2.getLocation().getBlock().getRelative(BlockFace.DOWN);
      DevCommand.PlacementResult var4 = this.calculatePlacement(var3, BlockFace.UP, var2);
      if (!var4.valid()) {
         var2.sendMessage("Invalid placement location!");
      } else {
         Location var5 = var4.location();
         BlockFace var6 = var4.face();
         var5.setX((double)var5.getBlockX());
         var5.setY((double)var5.getBlockY());
         var5.setZ((double)var5.getBlockZ());
         float var7 = this.getYawFromFacing(var6);
         Quaternionf var8 = (new Quaternionf()).rotateY((float)Math.toRadians((double)var7));
         ArrayList var9 = new ArrayList();
         ArrayList var10 = new ArrayList(var1.getBlueprint().getBlockbenchModel().getElements().values());
         Location var11 = this.getCenteredLocation(var5, var6);
         Iterator var12 = var10.iterator();

         while(var12.hasNext()) {
            BlockbenchModel.Element var13 = (BlockbenchModel.Element)var12.next();
            if (var13 instanceof BlockbenchModel.Cube) {
               BlockbenchModel.Cube var14 = (BlockbenchModel.Cube)var13;
               Vector3f var15 = new Vector3f((float)((double)var14.getOrigin()[0] / 16.0D), (float)((double)var14.getOrigin()[1] / 16.0D), (float)((double)var14.getOrigin()[2] / 16.0D));
               Vector3f var16 = new Vector3f((float)((double)var14.getFrom()[0] / 16.0D) - var15.x(), (float)((double)var14.getFrom()[1] / 16.0D) - var15.y(), (float)((double)var14.getFrom()[2] / 16.0D) - var15.z());
               float var17 = (var14.getTo()[0] - var14.getFrom()[0]) / 16.0F;
               float var18 = (var14.getTo()[1] - var14.getFrom()[1]) / 16.0F;
               float var19 = (var14.getTo()[2] - var14.getFrom()[2]) / 16.0F;
               Quaternionf var20 = this.getQuaternionf(var14);
               Vector3f var21 = new Vector3f(var16);
               var20.transform(var21);
               Vector3f var22 = new Vector3f(var21);
               Vector3f var23 = new Vector3f(var15);
               var8.transform(var22);
               var8.transform(var23);
               Vector3f var24 = (new Vector3f(var23)).add(var22);
               Quaternionf var25 = (new Quaternionf(var8)).mul(var20);
               Location var26 = var11.clone().add((double)var24.x(), (double)var24.y(), (double)var24.z());
               BlockDisplay var27 = (BlockDisplay)var26.getWorld().spawn(var26, BlockDisplay.class);
               var27.setBlock(Material.DIAMOND_BLOCK.createBlockData());
               var9.add(var27);
               Transformation var28 = new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), var25, new Vector3f(var17, var18, var19), new Quaternionf());
               var27.setTransformation(var28);
            } else {
               System.err.println("Element " + var13.getName() + " is not a cube");
            }
         }

      }
   }

   private Location getCenteredLocation(Location var1, BlockFace var2) {
      var1 = var1.clone();
      switch(var2) {
      case NORTH:
         var1.add(0.0D, 0.0D, 0.0D);
         break;
      case EAST:
         var1.add(1.0D, 0.0D, 0.0D);
         break;
      case SOUTH:
         var1.add(1.0D, 0.0D, 1.0D);
         break;
      case WEST:
         var1.add(0.0D, 0.0D, 1.0D);
      }

      return var1;
   }

   private float getYawFromFacing(BlockFace var1) {
      float var10000;
      switch(var1) {
      case NORTH:
         var10000 = 0.0F;
         break;
      case EAST:
         var10000 = -90.0F;
         break;
      case SOUTH:
         var10000 = 180.0F;
         break;
      case WEST:
         var10000 = 90.0F;
         break;
      default:
         var10000 = 0.0F;
      }

      return var10000;
   }

   public static BlockFace getCardinalDirection(float var0) {
      if (var0 < 0.0F) {
         var0 += 360.0F;
      }

      if ((var0 %= 360.0F) <= 45.0F) {
         return BlockFace.NORTH;
      } else if (var0 <= 135.0F) {
         return BlockFace.EAST;
      } else if (var0 <= 225.0F) {
         return BlockFace.SOUTH;
      } else {
         return var0 <= 315.0F ? BlockFace.WEST : BlockFace.NORTH;
      }
   }

   @NotNull
   private Quaternionf getQuaternionf(BlockbenchModel.Cube var1) {
      Quaternionf var2 = new Quaternionf();
      if (var1.getRotation() == null) {
         return var2;
      } else if (var1.getRotation().length == 0) {
         return var2;
      } else {
         List var3 = Arrays.asList(var1.getRotation());
         if (!var3.isEmpty()) {
            float var4 = (Float)var3.get(0);
            float var5 = (Float)var3.get(1);
            float var6 = (Float)var3.get(2);
            var2.rotationXYZ((float)Math.toRadians((double)var4), (float)Math.toRadians((double)var5), (float)Math.toRadians((double)var6));
         }

         return var2;
      }
   }

   private void applyCustomSettings(BukkitEntity var1, IModelContainer var2, IVisualModel var3) {
      var3.getJoints().values().forEach((var2x) -> {
         ProcessedJoint var3x = var2x.getBlueprintJoint().getProcessedJoint();
         JointImpl var4 = (JointImpl)var2x;
         DisplayRendererImpl var5 = (DisplayRendererImpl)var4.getVisualModel().getModelRenderer();
         if (var3x != null && !var3x.getCubes().isEmpty()) {
            ProcessedJoint.Cube var6 = (ProcessedJoint.Cube)(new ArrayList(var3x.getCubes())).get(0);
            float var7 = (float)(Double.valueOf(var6.getTo().get(0)) - Double.valueOf(var6.getFrom().get(0))) / 16.0F;
            float var8 = (float)(Double.valueOf(var6.getTo().get(1)) - Double.valueOf(var6.getFrom().get(1))) / 16.0F;
            float var9 = (float)(Double.valueOf(var6.getTo().get(2)) - Double.valueOf(var6.getFrom().get(2))) / 16.0F;
            DisplayRenderer.Joint var10 = (DisplayRenderer.Joint)var5.getAllJoints().stream().filter((var1) -> {
               return Objects.equals(var1.getJoint().getJointId(), var2x.getJointId());
            }).findFirst().orElse((Object)null);
            var10.getScale().set(new Vector3f(var7, var8, var9));
            var10.getScale().setProtectAgainstChanges(true);
            var10.updateJointData(ModelAPI.getEntityHandler(), var5.getPivot(), var2x.getModels());
            this.plugin.getLogger().info("Joint " + var2x.getJointId() + " has scale " + var7 + ", " + var8 + ", " + var9);
         } else {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = var2x.getJointId();
            var10000.warning("Joint " + var10001 + " has no processed joint in " + var3.getBlueprint().getName());
         }
      });
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      switch(var2.length) {
      case 1:
         InfiniteModelsCommand.getModelIdTabComplete(var3, var2[0]);
         break;
      case 2:
         String var4 = var2[1];
         EntityType[] var5 = EntityType.values();
         EntityType[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EntityType var9 = var6[var8];
            String var10 = var9.name();
            if (var10.startsWith(var4.toUpperCase(Locale.ENGLISH))) {
               var3.add(var10);
            }
         }

         return var3;
      default:
         var3.addAll(ModelOptionParser.getTabCompletion(var2.length > 1 ? 2 : 1, var2));
      }

      return var3;
   }

   public String getPermissionNode() {
      return "infinitemodel.command.dev";
   }

   public boolean isConsoleFriendly() {
      return false;
   }

   public String getName() {
      return "dev";
   }

   private static class PlacementResult {
      private final boolean valid;
      private final Location location;
      private final BlockFace face;

      public PlacementResult(boolean var1, Location var2, BlockFace var3) {
         this.valid = var1;
         this.location = var2;
         this.face = var3;
      }

      public boolean valid() {
         return this.valid;
      }

      public Location location() {
         return this.location;
      }

      public BlockFace face() {
         return this.face;
      }
   }
}
