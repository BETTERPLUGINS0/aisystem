package com.bergerkiller.bukkit.tc.debug.types;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.debug.DebugTool;
import com.bergerkiller.bukkit.tc.debug.DebugToolUtil;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.text.NumberFormat;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DebugToolTypeTrackDistance extends DebugToolTrackWalkerType {
   public String getIdentifier() {
      return "TrackDistance";
   }

   public String getTitle() {
      return "Track distance calculator";
   }

   public String getDescription() {
      return "Calculates and displays the track distance between two points";
   }

   public String getInstructions() {
      return "Left-click on one point and right-click another to display the track distance between the two points";
   }

   public boolean handlesLeftClick() {
      return true;
   }

   public void onBlockInteract(TrainCarts plugin, Player player, TrackWalkingPoint walker, CommonItemStack item, boolean isRightClick) {
      item = item.clone();
      RailState start;
      RailState goal;
      if (isRightClick) {
         saveRailState(item, "pos2", walker.state);
         start = loadRailState(player, item, "pos1");
         goal = walker.state;
      } else {
         saveRailState(item, "pos1", walker.state);
         start = walker.state;
         goal = loadRailState(player, item, "pos2");
      }

      DebugTool.updateToolItem(player, item);
      if (start != null && goal != null && !player.isSneaking()) {
         if (start.railWorld() != goal.railWorld()) {
            player.sendMessage(ChatColor.RED + "The two positions are on different worlds!");
         } else {
            double distance = start.position().distance(goal.position());
            if (distance > 2000.0D) {
               player.sendMessage(ChatColor.RED + "Distance between the two positions is too large!");
            } else {
               TrackWalkingPoint measure = new TrackWalkingPoint(start);
               double PARTICLE_STEP = 0.5D;
               int cycleCtr = 10000;
               double bestRemaining = Double.MAX_VALUE;
               double bestTotal = 0.0D;
               boolean foundGoalRailBlock = false;

               double remaining;
               while((remaining = measure.state.position().distance(goal.position())) > 1.0E-4D) {
                  if (!measure.move(Math.min(0.5D, remaining))) {
                     DebugToolUtil.showEndOfTheRail(player, measure, 0.0D);
                     return;
                  }

                  if (!(measure.movedTotal > 2000.0D)) {
                     --cycleCtr;
                     if (cycleCtr > 0) {
                        DebugToolUtil.showParticle(measure.state.positionLocation());
                        if (measure.state.railPiece().equals(goal.railPiece())) {
                           if (remaining < bestRemaining) {
                              bestRemaining = remaining;
                              bestTotal = measure.movedTotal;
                           }

                           foundGoalRailBlock = true;
                           continue;
                        }

                        if (!foundGoalRailBlock) {
                           continue;
                        }

                        measure.movedTotal = bestTotal;
                        break;
                     }
                  }

                  player.sendMessage(ChatColor.RED + "Distance between the two positions is too large!");
                  return;
               }

               double totalDistance = measure.movedTotal;
               NumberFormat df = NumberFormat.getNumberInstance(Locale.ENGLISH);
               df.setGroupingUsed(false);
               df.setMinimumFractionDigits(2);
               if (isRightClick) {
                  player.sendMessage(ChatColor.GREEN + "Distance from start to " + ChatColor.YELLOW + "end" + ChatColor.GREEN + " is " + ChatColor.WHITE + df.format(totalDistance) + ChatColor.GREEN + " blocks");
               } else {
                  player.sendMessage(ChatColor.GREEN + "Distance from " + ChatColor.YELLOW + "start" + ChatColor.GREEN + " to end is " + ChatColor.WHITE + df.format(totalDistance) + ChatColor.GREEN + " blocks");
               }

            }
         }
      } else {
         DebugToolUtil.showParticle(walker.state.positionLocation());
         if (isRightClick) {
            player.sendMessage(ChatColor.YELLOW + "End" + ChatColor.GREEN + " position set");
         } else {
            player.sendMessage(ChatColor.YELLOW + "Start" + ChatColor.GREEN + " position set");
         }

      }
   }

   private static void saveRailState(CommonItemStack item, String prefix, RailState state) {
      state.position().assertAbsolute();
      item.updateCustomData((tag) -> {
         CommonTagCompound meta = tag.createCompound(prefix);
         meta.putValue("world", state.railWorld().getName());
         meta.putValue("posX", state.position().posX);
         meta.putValue("posY", state.position().posY);
         meta.putValue("posZ", state.position().posZ);
         meta.putValue("motX", state.position().motX);
         meta.putValue("motY", state.position().motY);
         meta.putValue("motZ", state.position().motZ);
      });
   }

   private static RailState loadRailState(Player player, CommonItemStack item, String prefix) {
      CommonTagCompound meta = (CommonTagCompound)item.getCustomData().get(prefix, CommonTagCompound.class);
      if (meta == null) {
         return null;
      } else {
         String worldName = (String)meta.getValue("world", "");
         World world = Bukkit.getWorld(worldName);
         if (world == null) {
            player.sendMessage("Other position is on a world that is not loaded: " + worldName);
            return null;
         } else {
            RailPath.Position position = new RailPath.Position();
            position.relative = false;
            position.posX = (Double)meta.getValue("posX", 0.0D);
            position.posY = (Double)meta.getValue("posY", 0.0D);
            position.posZ = (Double)meta.getValue("posZ", 0.0D);
            position.motX = (Double)meta.getValue("motX", 0.0D);
            position.motY = (Double)meta.getValue("motY", 0.0D);
            position.motZ = (Double)meta.getValue("motZ", 0.0D);
            RailState state = new RailState();
            state.setRailPiece(RailPiece.createWorldPlaceholder(world));
            state.setPosition(position);
            if (!RailType.loadRailInformation(state)) {
               player.sendMessage("Rails at the other position doesn't exist anymore!");
               return null;
            } else {
               return state;
            }
         }
      }
   }
}
