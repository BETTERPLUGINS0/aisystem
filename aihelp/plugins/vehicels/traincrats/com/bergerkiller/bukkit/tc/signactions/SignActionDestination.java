package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SignActionDestination extends TrainCartsSignAction {
   public SignActionDestination() {
      super("destination");
   }

   public boolean click(SignActionEvent info, Player player) {
      CartProperties cprop = info.getTrainCarts().getPlayer(player).getEditedCart();
      if (cprop == null) {
         if (Permission.COMMAND_PROPERTIES.has(player)) {
            Localization.EDIT_NOSELECT.message(player, new String[0]);
         } else {
            Localization.EDIT_NOTALLOWED.message(player, new String[0]);
         }

         return true;
      } else {
         Object prop;
         if (info.isTrainSign()) {
            prop = cprop.getTrainProperties();
         } else {
            if (!info.isCartSign()) {
               return false;
            }

            prop = cprop;
         }

         if (!((IProperties)prop).hasOwnership(player)) {
            Localization.EDIT_NOTOWNED.message(player, new String[0]);
         } else {
            String dest = info.getLine(2);
            ((IProperties)prop).setDestination(dest);
            Localization.SELECT_DESTINATION.message(player, new String[]{dest});
         }

         return true;
      }
   }

   public void execute(SignActionEvent info) {
      if (info.isRCSign()) {
         if (info.isAction(SignActionType.REDSTONE_ON)) {
            Iterator var6 = info.getRCTrainProperties().iterator();

            while(var6.hasNext()) {
               TrainProperties prop = (TrainProperties)var6.next();
               Iterator var8 = prop.iterator();

               while(var8.hasNext()) {
                  CartProperties cprop = (CartProperties)var8.next();
                  cprop.setDestination(info.getLine(3));
               }
            }
         }

      } else if (info.hasRails()) {
         if (info.isCartSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER) || info.isTrainSign() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER)) {
            PathNode node = PathNode.getOrCreate(info);
            Iterator var3 = info.getMembers().iterator();

            MinecartMember member;
            while(var3.hasNext()) {
               member = (MinecartMember)var3.next();
               member.getProperties().setLastPathNode(node.getName());
            }

            var3 = info.getMembers().iterator();

            while(var3.hasNext()) {
               member = (MinecartMember)var3.next();
               String nextDestination = this.getNextDestination(member.getProperties(), info);
               if (nextDestination != null) {
                  if (nextDestination.isEmpty()) {
                     member.getProperties().clearDestination();
                  } else {
                     member.getProperties().setDestination(nextDestination);
                  }
               }
            }

         }
      }
   }

   private String getNextDestination(CartProperties cart, SignActionEvent info) {
      String newDestination = info.getLine(3).trim();
      if (newDestination.isEmpty()) {
         newDestination = null;
      }

      if (info.isAction(SignActionType.REDSTONE_ON)) {
         return newDestination;
      } else if (!info.isPowered()) {
         return null;
      } else {
         String signDestination = info.getLine(2);
         if (signDestination.isEmpty()) {
            return newDestination;
         } else if (cart.hasDestination() && !cart.getDestination().equals(signDestination)) {
            return null;
         } else {
            String nextOnRoute = cart.getNextDestinationOnRoute(signDestination);
            if (nextOnRoute.isEmpty() && newDestination == null && !cart.getDestinationRoute().isEmpty()) {
               nextOnRoute = (String)cart.getDestinationRoute().get(0);
            }

            return nextOnRoute.isEmpty() ? newDestination : nextOnRoute;
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      if (!event.getLine(2).isEmpty()) {
         PathNode node = event.getTrainCarts().getPathProvider().getWorld(event.getWorld()).getNodeByName(event.getLine(2));
         if (node != null) {
            event.getPlayer().sendMessage(ChatColor.RED + "Destination with name '" + event.getLine(2) + "' already exists on this world!");
            ChatText text = ChatText.fromMessage(ChatColor.RED + "Find it at ");
            ChatText command = ChatText.fromMessage(ChatColor.WHITE.toString() + ChatColor.UNDERLINE + "[" + node.location.x + " / " + node.location.y + " / " + node.location.z + "]");
            command.setClickableSuggestedCommand("/tp @p " + node.location.x + " " + node.location.y + " " + node.location.z);
            text.append(command);
            text.sendTo(event.getPlayer());
            return false;
         }
      }

      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_DESTINATION).setName(event.isCartSign() ? "cart destination" : "train destination").setTraincartsWIKIHelp("TrainCarts/Signs/Destination");
      if (event.isTrainSign()) {
         opt.setDescription("set a train destination and the next destination to set once it is reached");
      } else if (event.isCartSign()) {
         opt.setDescription("set a cart destination and the next destination to set once it is reached");
      } else if (event.isRCSign()) {
         opt.setDescription("set the destination on a remote train");
      }

      return opt.handle(event);
   }

   public String getRailDestinationName(SignActionEvent info) {
      String name = info.getLine(2);
      return name.isEmpty() ? null : name;
   }

   public boolean canSupportRC() {
      return true;
   }
}
