package me.SuperRonanCraft.BetterRTP.references.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdLocation;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTPSetupInformation;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_ERROR_REQUEST_REASON;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_PlayerInfo;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import me.SuperRonanCraft.BetterRTP.references.PermissionCheck;
import me.SuperRonanCraft.BetterRTP.references.WarningHandler;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.placeholder.Placeholders;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.PermissionGroup;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldLocation;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPermissionGroup;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HelperRTP {
   public static void tp(Player player, World world, List<String> biomes, RTP_TYPE rtpType) {
      tp(player, player, world, biomes, rtpType);
   }

   public static void tp(Player player, CommandSender sendi, World world, List<String> biomes, RTP_TYPE rtpType) {
      tp(player, sendi, world, biomes, rtpType, false, false);
   }

   public static void tp(Player player, CommandSender sendi, World world, List<String> biomes, RTP_TYPE rtpType, boolean ignoreCooldown, boolean ignoreDelay) {
      tp(player, sendi, world, biomes, rtpType, ignoreCooldown, ignoreDelay, (WorldLocation)null);
   }

   public static void tp(@NotNull Player player, CommandSender sendi, @Nullable World world, List<String> biomes, RTP_TYPE rtpType, boolean ignoreCooldown, boolean ignoreDelay, @Nullable WorldLocation location) {
      tp(player, sendi, world, biomes, rtpType, location, new RTP_PlayerInfo(!ignoreDelay, true, !ignoreCooldown));
   }

   public static void tp(@NotNull Player player, CommandSender sendi, @Nullable World world, List<String> biomes, RTP_TYPE rtpType, @Nullable WorldLocation location, @NotNull RTP_PlayerInfo playerInfo) {
      world = getActualWorld(player, world, location);
      RTPSetupInformation setup_info = new RTPSetupInformation(world, sendi, player, true, biomes, rtpType, location, playerInfo);
      tp(player, sendi, setup_info);
   }

   public static void tp(@NotNull Player player, CommandSender sendi, @NotNull RTPSetupInformation setup_info) {
      WorldPlayer pWorld = getPlayerWorld(setup_info);
      RTP_ERROR_REQUEST_REASON cantReason = HelperRTP_Check.canRTP(player, sendi, pWorld, setup_info.getPlayerInfo());
      if (cantReason != null) {
         String msg = cantReason.getMsg().get(player, (Object)null);
         if (cantReason == RTP_ERROR_REQUEST_REASON.COOLDOWN) {
            msg = msg.replace(Placeholders.COOLDOWN.name, HelperDate.total(HelperRTP_Check.getCooldown(player, pWorld)));
            msg = msg.replace(Placeholders.TIME.name, HelperDate.total(HelperRTP_Check.getCooldown(player, pWorld)));
         }

         Message_RTP.sms(player, (String)msg, (Object)pWorld);
         if (sendi != player) {
            Message_RTP.sms(sendi, (String)msg, (Object)pWorld);
         }

      } else {
         BetterRTP.getInstance().getRTP().start(pWorld);
      }
   }

   public static World getActualWorld(Player player, World world, @Nullable WorldLocation location) {
      if (world == null) {
         world = player.getWorld();
      }

      if (location != null) {
         world = location.getWorld();
      }

      if (BetterRTP.getInstance().getRTP().overriden.containsKey(world.getName())) {
         world = Bukkit.getWorld((String)BetterRTP.getInstance().getRTP().overriden.get(world.getName()));
      }

      return world;
   }

   public static World getActualWorld(Player player, World world) {
      return getActualWorld(player, world, (WorldLocation)null);
   }

   @Nullable
   public static WorldLocation getRandomLocation(CommandSender sender, World world) {
      HashMap<String, RTPWorld> locations_permissible = CmdLocation.getLocations(sender, world);
      if (!locations_permissible.isEmpty()) {
         List<String> valuesList = new ArrayList(locations_permissible.keySet());
         String randomIndex = (String)valuesList.get((new Random()).nextInt(valuesList.size()));
         return (WorldLocation)locations_permissible.get(randomIndex);
      } else {
         return null;
      }
   }

   public static WorldPlayer getPlayerWorld(RTPSetupInformation setup_info) {
      WorldPlayer pWorld = new WorldPlayer(setup_info);
      if (setup_info.getLocation() == null && BetterRTP.getInstance().getSettings().isLocationEnabled() && BetterRTP.getInstance().getSettings().isUseLocationIfAvailable()) {
         WorldLocation worldLocation = getRandomLocation(setup_info.getSender(), setup_info.getWorld());
         if (worldLocation != null) {
            setup_info.setLocation(worldLocation);
            setup_info.setWorld(worldLocation.getWorld());
         }

         if (setup_info.getLocation() == null && BetterRTP.getInstance().getSettings().isDebug()) {
            WarningHandler.warn(WarningHandler.WARNING.USELOCATION_ENABLED_NO_LOCATION_AVAILABLE, "This is not an error! UseLocationIfAvailable is set to `true`, but no location was found for " + setup_info.getSender().getName() + "! Using world defaults! (Maybe they dont have permission?)");
         }
      }

      if (setup_info.getLocation() != null) {
         String setup_name = null;
         Iterator var3 = BetterRTP.getInstance().getRTP().getRTPworldLocations().entrySet().iterator();

         while(var3.hasNext()) {
            Entry<String, RTPWorld> location_set = (Entry)var3.next();
            RTPWorld location = (RTPWorld)location_set.getValue();
            if (location == setup_info.getLocation()) {
               setup_name = (String)location_set.getKey();
               break;
            }
         }

         pWorld.setup(setup_name, setup_info.getLocation(), setup_info.getLocation().getBiomes());
      }

      if (!pWorld.isSetup()) {
         WorldPermissionGroup group = getGroup(pWorld);
         if (group != null) {
            pWorld.setup((String)null, group, setup_info.getBiomes());
            pWorld.config = group;
         } else if (BetterRTP.getInstance().getRTP().getRTPcustomWorld().containsKey(setup_info.getWorld().getName())) {
            RTPWorld cWorld = (RTPWorld)BetterRTP.getInstance().getRTP().getRTPcustomWorld().get(pWorld.getWorld().getName());
            pWorld.setup((String)null, cWorld, setup_info.getBiomes());
         } else {
            pWorld.setup((String)null, BetterRTP.getInstance().getRTP().getRTPdefaultWorld(), setup_info.getBiomes());
         }
      }

      pWorld.setWorldtype(getWorldType(pWorld.getWorld()));
      return pWorld;
   }

   public static WORLD_TYPE getWorldType(World world) {
      RTP rtp = BetterRTP.getInstance().getRTP();
      WORLD_TYPE world_type;
      if (rtp.world_type.containsKey(world.getName())) {
         world_type = (WORLD_TYPE)rtp.world_type.get(world.getName());
      } else {
         world_type = WORLD_TYPE.NORMAL;
         rtp.world_type.put(world.getName(), world_type);
         WarningHandler.warn(WarningHandler.WARNING.NO_WORLD_TYPE_DECLARED, "Seems like the world `" + world.getName() + "` does not have a `WorldType` declared. Please add/fix this in the config.yml file! This world will be treated as an overworld! If this world is a nether world, configure it to NETHER (example: `- " + world.getName() + ": NETHER`", false);
      }

      return world_type;
   }

   public static WorldPermissionGroup getGroup(WorldPlayer pWorld) {
      WorldPermissionGroup group = null;
      if (pWorld.getPlayer() != null) {
         Iterator var2 = BetterRTP.getInstance().getRTP().getPermissionGroups().entrySet().iterator();

         label40:
         while(var2.hasNext()) {
            Entry<String, PermissionGroup> permissionGroup = (Entry)var2.next();
            Iterator var4 = ((PermissionGroup)permissionGroup.getValue()).getWorlds().entrySet().iterator();

            while(true) {
               Entry worldPermission;
               do {
                  do {
                     do {
                        if (!var4.hasNext()) {
                           continue label40;
                        }

                        worldPermission = (Entry)var4.next();
                     } while(!pWorld.getWorld().equals(((WorldPermissionGroup)worldPermission.getValue()).getWorld()));
                  } while(!PermissionCheck.getPermissionGroup(pWorld.getPlayer(), (String)permissionGroup.getKey()));
               } while(group != null && group.getPriority() < ((WorldPermissionGroup)worldPermission.getValue()).getPriority());

               group = (WorldPermissionGroup)worldPermission.getValue();
            }
         }
      }

      return group;
   }
}
