package me.SuperRonanCraft.BetterRTP.references.depends;

import java.util.Iterator;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTPSetupInformation;
import me.SuperRonanCraft.BetterRTP.references.PermissionCheck;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperDate;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP_Check;
import me.SuperRonanCraft.BetterRTP.references.player.HelperPlayer;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerData;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DepPlaceholderAPI extends PlaceholderExpansion {
   @NotNull
   public String getIdentifier() {
      return BetterRTP.getInstance().getDescription().getName().toLowerCase();
   }

   @NotNull
   public String getAuthor() {
      return (String)BetterRTP.getInstance().getDescription().getAuthors().get(0);
   }

   @NotNull
   public String getVersion() {
      return BetterRTP.getInstance().getDescription().getVersion();
   }

   public String onPlaceholderRequest(Player player, String request) {
      PlayerData data = HelperPlayer.getData(player);
      if (request.equalsIgnoreCase("count")) {
         return String.valueOf(data.getRtpCount());
      } else {
         if (request.startsWith("cooldown")) {
            if (request.equalsIgnoreCase("cooldown")) {
               return this.cooldown(data, player.getWorld());
            }

            if (request.startsWith("cooldown_")) {
               return this.cooldown(data, this.getWorld(request.replace("cooldown_", "")));
            }

            if (request.equalsIgnoreCase("cooldowntime")) {
               return this.cooldownTime(data, player.getWorld());
            }

            if (request.startsWith("cooldowntime_")) {
               return this.cooldownTime(data, this.getWorld(request.replace("cooldowntime_", "")));
            }
         } else if (request.startsWith("canrtp")) {
            if (request.equalsIgnoreCase("canrtp")) {
               return this.canRTPALL(player, player.getWorld());
            }

            if (request.startsWith("canrtp_")) {
               return this.canRTPALL(player, this.getWorld(request.replace("canrtp_", "")));
            }

            if (request.startsWith("canrtpcooldown")) {
               if (request.equalsIgnoreCase("canrtpcooldown")) {
                  return this.canRTP_cooldown(player, player.getWorld());
               }

               if (request.startsWith("canrtpcooldown_")) {
                  return this.canRTP_cooldown(player, this.getWorld(request.replace("canrtpcooldown_", "")));
               }
            } else if (request.startsWith("canrtpprice")) {
               if (request.equalsIgnoreCase("canrtpprice")) {
                  return this.canRTP_price(player, player.getWorld());
               }

               if (request.startsWith("canrtpprice_")) {
                  return this.canRTP_price(player, this.getWorld(request.replace("canrtpprice_", "")));
               }
            } else if (request.startsWith("canrtphunger")) {
               if (request.equalsIgnoreCase("canrtphunger")) {
                  return this.canRTP_hunger(player, player.getWorld());
               }

               if (request.startsWith("canrtphunger_")) {
                  return this.canRTP_hunger(player, this.getWorld(request.replace("canrtphunger_", "")));
               }
            }
         } else if (request.startsWith("price")) {
            if (request.equalsIgnoreCase("price")) {
               return this.price(player, player.getWorld());
            }

            if (request.startsWith("price_")) {
               return this.price(player, this.getWorld(request.replace("price_", "")));
            }
         }

         return null;
      }
   }

   private String cooldown(PlayerData data, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         long lng = BetterRTP.getInstance().getCooldowns().locked(data.player) ? -1L : HelperRTP_Check.getCooldown(data.player, HelperRTP.getPlayerWorld(new RTPSetupInformation(world, data.player, data.player, true)));
         return HelperDate.total(lng);
      }
   }

   private String cooldownTime(PlayerData data, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         RTPSetupInformation setup_info = new RTPSetupInformation(HelperRTP.getActualWorld(data.player, world), data.player, data.player, true);
         WorldPlayer pWorld = HelperRTP.getPlayerWorld(setup_info);
         Long cooldownTime = BetterRTP.getInstance().getCooldowns().locked(data.player) ? -1L : (HelperRTP_Check.applyCooldown(data.player) ? pWorld.getCooldown() * 1000L : 0L);
         return HelperDate.total(cooldownTime);
      }
   }

   private String canRTPALL(Player player, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         world = HelperRTP.getActualWorld(player, world);
         if (!PermissionCheck.getAWorld(player, world.getName())) {
            return BetterRTP.getInstance().getSettings().getPlaceholder_nopermission();
         } else {
            RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
            WorldPlayer pWorld = HelperRTP.getPlayerWorld(setupInformation);
            if (HelperRTP_Check.isCoolingDown(player, pWorld)) {
               return BetterRTP.getInstance().getSettings().getPlaceholder_cooldown();
            } else if (!BetterRTP.getInstance().getEco().hasBalance(pWorld)) {
               return BetterRTP.getInstance().getSettings().getPlaceholder_balance();
            } else {
               return !BetterRTP.getInstance().getEco().hasHunger(pWorld) ? BetterRTP.getInstance().getSettings().getPlaceholder_hunger() : BetterRTP.getInstance().getSettings().getPlaceholder_true();
            }
         }
      }
   }

   private String canRTP_cooldown(Player player, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         world = HelperRTP.getActualWorld(player, world);
         RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
         WorldPlayer pWorld = HelperRTP.getPlayerWorld(setupInformation);
         return HelperRTP_Check.isCoolingDown(player, pWorld) ? BetterRTP.getInstance().getSettings().getPlaceholder_cooldown() : BetterRTP.getInstance().getSettings().getPlaceholder_true();
      }
   }

   private String canRTP_price(Player player, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         world = HelperRTP.getActualWorld(player, world);
         RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
         WorldPlayer pWorld = HelperRTP.getPlayerWorld(setupInformation);
         return !BetterRTP.getInstance().getEco().hasBalance(pWorld) ? BetterRTP.getInstance().getSettings().getPlaceholder_balance() : BetterRTP.getInstance().getSettings().getPlaceholder_true();
      }
   }

   private String canRTP_hunger(Player player, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         world = HelperRTP.getActualWorld(player, world);
         RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
         WorldPlayer pWorld = HelperRTP.getPlayerWorld(setupInformation);
         return !BetterRTP.getInstance().getEco().hasHunger(pWorld) ? BetterRTP.getInstance().getSettings().getPlaceholder_hunger() : BetterRTP.getInstance().getSettings().getPlaceholder_true();
      }
   }

   private String price(Player player, World world) {
      if (world == null) {
         return "Invalid World";
      } else {
         world = HelperRTP.getActualWorld(player, world);
         RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
         WorldPlayer pWorld = HelperRTP.getPlayerWorld(setupInformation);
         return String.valueOf(pWorld.getPrice());
      }
   }

   private World getWorld(String world_name) {
      World world = null;
      if (world_name.length() > 0) {
         Iterator var3 = Bukkit.getWorlds().iterator();

         while(var3.hasNext()) {
            World _world = (World)var3.next();
            if (world_name.equalsIgnoreCase(_world.getName())) {
               world = _world;
               break;
            }
         }
      }

      return world;
   }
}
