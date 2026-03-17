package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SignActionTicket extends TrainCartsSignAction {
   public SignActionTicket() {
      super("ticket");
   }

   public void execute(SignActionEvent info) {
      Economy economy = info.getTrainCarts().getEconomy();
      if (economy != null) {
         boolean isTrain;
         if (info.isCartSign() && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)) {
            isTrain = false;
         } else {
            if (!info.isTrainSign() || !info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)) {
               return;
            }

            isTrain = true;
         }

         if (info.hasMember() && info.isPowered()) {
            String mode = info.getLine(2).toLowerCase(Locale.ENGLISH).trim();
            double money = ParseUtil.parseDouble(info.getLine(3), 0.0D);
            Object members;
            if (isTrain) {
               members = info.getGroup();
            } else {
               members = new ArrayList(1);
               ((List)members).add(info.getMember());
            }

            Iterator var8 = ((List)members).iterator();

            label92:
            while(true) {
               MinecartMember member;
               do {
                  if (!var8.hasNext()) {
                     return;
                  }

                  member = (MinecartMember)var8.next();
               } while(!((CommonMinecart)member.getEntity()).hasPlayerPassenger());

               Set<String> owners = member.getProperties().getOwners();
               Iterator var11 = ((CommonMinecart)member.getEntity()).getPlayerPassengers().iterator();

               while(true) {
                  while(true) {
                     if (!var11.hasNext()) {
                        continue label92;
                     }

                     Player player = (Player)var11.next();
                     if (mode.equals("add") && money > 0.0D) {
                        economy.depositPlayer(player, money);
                        Localization.TICKET_ADD.message(player, new String[]{TrainCarts.getCurrencyText(money)});
                     } else if (mode.equals("check")) {
                        Localization.TICKET_CHECK.message(player, new String[]{TrainCarts.getCurrencyText(economy.getBalance(player))});
                     } else if (mode.equals("buy") && money > 0.0D) {
                        if (economy.has(player, money)) {
                           economy.withdrawPlayer(player, money);
                           Localization.TICKET_BUY.message(player, new String[]{TrainCarts.getCurrencyText(money)});
                        } else {
                           Localization.TICKET_BUYFAIL.message(player, new String[]{TrainCarts.getCurrencyText(money)});
                           ((CommonMinecart)member.getEntity()).removePassenger(player);
                        }
                     } else if (mode.equals("pay") && money > 0.0D && !member.getProperties().isOwner(player)) {
                        if (economy.has(player, money)) {
                           economy.withdrawPlayer(player, money);
                           Localization.TICKET_BUY.message(player, new String[]{TrainCarts.getCurrencyText(money)});
                           if (owners.size() > 0) {
                              double ownerPayment = money / (double)owners.size();
                              Iterator var15 = owners.iterator();

                              while(var15.hasNext()) {
                                 String owner = (String)var15.next();
                                 OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(owner);
                                 economy.depositPlayer(offlinePlayer, ownerPayment);
                                 if (offlinePlayer.isOnline()) {
                                    Localization.TICKET_BUYOWNER.message(offlinePlayer.getPlayer(), new String[]{player.getDisplayName(), TrainCarts.getCurrencyText(money), member.getProperties().getTrainProperties().getTrainName()});
                                 }
                              }
                           }
                        } else {
                           Localization.TICKET_BUYFAIL.message(player, new String[]{TrainCarts.getCurrencyText(money)});
                           ((CommonMinecart)member.getEntity()).removePassenger(player);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_TICKET).setName("ticket system").setDescription("charges the passengers of a train").setTraincartsWIKIHelp("TrainCarts/Signs/Ticket").handle(event);
   }
}
