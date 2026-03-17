package com.bergerkiller.bukkit.tc.debug.types;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.debug.DebugToolUtil;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class DebugToolTypeRails extends DebugToolTrackWalkerType {
   public String getIdentifier() {
      return "Rails";
   }

   public String getTitle() {
      return "Rail path tool";
   }

   public String getDescription() {
      return "Display the positions on the rails along which trains move";
   }

   public String getInstructions() {
      return "Right-click rails to see the path a train would take on it";
   }

   public void onBlockInteract(TrainCarts plugin, Player player, TrackWalkingPoint walker, CommonItemStack item, boolean isRightClick) {
      player.sendMessage(ChatColor.YELLOW + "Checking for rails from path [" + MathUtil.round(walker.state.position().posX, 3) + "/" + MathUtil.round(walker.state.position().posY, 3) + "/" + MathUtil.round(walker.state.position().posZ, 3) + "]");
      int lim = 10000;
      AtomicInteger signShowLimit = new AtomicInteger(20);
      int segmentCounter;
      if (player.isSneaking()) {
         if (walker.moveFull()) {
            RailPath.Point[] var8 = walker.currentRailPath.getPoints();
            segmentCounter = var8.length;

            for(int var10 = 0; var10 < segmentCounter; ++var10) {
               RailPath.Point point = var8[var10];
               Util.spawnDustParticle(point.getLocation(walker.state.railBlock()), 0.1D, 0.1D, 1.0D);
            }

            do {
               this.showSigns(player, walker, signShowLimit);
               DebugToolUtil.showParticle(walker.state.railBlock().getLocation().add(0.5D, 0.5D, 0.5D));
               if (!walker.moveFull()) {
                  break;
               }

               --lim;
            } while(lim > 0);
         }
      } else {
         RailPiece lastRailPiece = null;
         segmentCounter = 0;
         double[][] colors = new double[][]{{1.0D, 0.0D, 0.0D}, {0.5D, 0.0D, 0.0D}};

         while(walker.move(0.3D)) {
            --lim;
            if (lim <= 0) {
               break;
            }

            if (lastRailPiece == null || !lastRailPiece.equals(walker.state.railPiece())) {
               lastRailPiece = walker.state.railPiece();
               ++segmentCounter;
               this.showSigns(player, walker, signShowLimit);
            }

            Location loc = walker.state.positionLocation();
            double[] color = colors[segmentCounter % colors.length];
            Util.spawnDustParticle(loc, color[0], color[1], color[2]);
         }
      }

   }

   private void showSigns(Player player, TrackWalkingPoint walker, AtomicInteger limit) {
      if (limit.get() > 0) {
         RailLookup.TrackedSign[] var4 = walker.state.railSigns();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            RailLookup.TrackedSign sign = var4[var6];
            SignAction action = sign.getAction();
            if (action != null) {
               limit.decrementAndGet();
               String name = action.getClass().getSimpleName();
               int signActionStart = name.toLowerCase(Locale.ENGLISH).indexOf("signaction");
               if (signActionStart != -1) {
                  name = name.substring(0, signActionStart) + name.substring(signActionStart + 10);
               }

               BlockFace face = walker.state.enterFace();
               SignActionEvent event = sign.createEvent(SignActionType.NONE);
               ChatColor color;
               if (event.isWatchedDirection(walker.state.enterFace())) {
                  color = action.overrideFacing() ? ChatColor.DARK_GREEN : ChatColor.GREEN;
               } else {
                  color = action.overrideFacing() ? ChatColor.DARK_RED : ChatColor.RED;
               }

               BlockFace signDir = sign.getFacing().getOppositeFace();
               String dirName = formatDirection(face.name().toLowerCase(Locale.ENGLISH), color);
               Direction[] var14 = new Direction[]{Direction.LEFT, Direction.FORWARD, Direction.BACKWARD, Direction.RIGHT};
               int var15 = var14.length;

               for(int var16 = 0; var16 < var15; ++var16) {
                  Direction dir = var14[var16];
                  if (dir.getDirection(signDir) == face) {
                     dirName = dirName + ChatColor.WHITE + "/" + formatDirection(dir.name().toLowerCase(Locale.ENGLISH), color);
                     break;
                  }
               }

               dirName = ChatColor.GRAY + "└┘" + ChatColor.BLUE + "→" + ChatColor.WHITE + "[" + dirName + ChatColor.WHITE + "]";
               String coord = ChatColor.WHITE + "- [" + sign.signBlock.getX() + "/" + sign.signBlock.getY() + "/" + sign.sign.getZ() + "] ";
               ChatText text = ChatText.fromMessage(coord + color + name + dirName);
               text.setHoverText(createHoverTextForSign((String[])LogicUtil.appendArray(sign.sign.getLines(), sign.getExtraLines())));
               text.sendTo(player);
            }
         }

      }
   }

   private static String formatDirection(String name, ChatColor color) {
      return color.toString() + ChatColor.UNDERLINE + name.charAt(0) + ChatColor.RESET + color + name.substring(1);
   }

   private static ChatText createHoverTextForSign(String[] lines) {
      int len;
      for(len = lines.length; len > 0 && lines[len - 1].isEmpty(); --len) {
      }

      StringBuilder str = new StringBuilder();

      for(int i = 0; i < len; ++i) {
         if (i > 0) {
            str.append('\n');
         }

         str.append(lines[i]);
      }

      return ChatText.fromMessage(str.toString());
   }
}
