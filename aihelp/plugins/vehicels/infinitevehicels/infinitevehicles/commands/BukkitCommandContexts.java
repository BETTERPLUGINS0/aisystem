package me.PM2.infinitevehicles.commands;

import java.util.HashSet;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Contract;

public class BukkitCommandContexts extends CommandContexts<BukkitCommandExecutionContext> {
   public BukkitCommandContexts(BukkitCommandManager manager) {
      super(var1);
      this.registerContext(OnlinePlayer.class, (var1x) -> {
         return this.getOnlinePlayer((BukkitCommandIssuer)var1x.getIssuer(), var1x.popFirstArg(), false);
      });
      this.registerContext(me.PM2.infinitevehicles.commands.contexts.OnlinePlayer.class, (var1x) -> {
         OnlinePlayer var2 = this.getOnlinePlayer((BukkitCommandIssuer)var1x.getIssuer(), var1x.popFirstArg(), false);
         return new me.PM2.infinitevehicles.commands.contexts.OnlinePlayer(var2.getPlayer());
      });
      this.registerContext(OnlinePlayer[].class, (var1x) -> {
         BukkitCommandIssuer var2 = (BukkitCommandIssuer)var1x.getIssuer();
         String var3 = var1x.popFirstArg();
         boolean var4 = var1x.hasFlag("allowmissing");
         HashSet var5 = new HashSet();
         Pattern var6 = ACFPatterns.COMMA;
         String var7 = var1x.getFlagValue("splitter", (String)null);
         if (var7 != null) {
            var6 = Pattern.compile(Pattern.quote(var7));
         }

         String[] var8 = var6.split(var3);
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String var11 = var8[var10];
            OnlinePlayer var12 = this.getOnlinePlayer(var2, var11, var4);
            if (var12 != null) {
               var5.add(var12);
            }
         }

         if (var5.isEmpty() && !var1x.hasFlag("allowempty")) {
            var2.sendError(MinecraftMessageKeys.NO_PLAYER_FOUND_SERVER, new String[]{"{search}", var3});
            throw new InvalidCommandArgument(false);
         } else {
            return (OnlinePlayer[])var5.toArray(new OnlinePlayer[var5.size()]);
         }
      });
      this.registerIssuerAwareContext(World.class, (var0) -> {
         String var1 = var0.getFirstArg();
         World var2 = var1 != null ? Bukkit.getWorld(var1) : null;
         if (var2 != null) {
            var0.popFirstArg();
         }

         if (var2 == null && var0.getSender() instanceof Player) {
            var2 = ((Entity)var0.getSender()).getWorld();
         }

         if (var2 == null) {
            throw new InvalidCommandArgument(MinecraftMessageKeys.INVALID_WORLD, new String[0]);
         } else {
            return var2;
         }
      });
      this.registerIssuerAwareContext(CommandSender.class, BukkitCommandExecutionContext::getSender);
      this.registerIssuerAwareContext(Player.class, (var1x) -> {
         boolean var2 = var1x.isOptional();
         CommandSender var3 = var1x.getSender();
         boolean var4 = var3 instanceof Player;
         if (!var1x.hasFlag("other")) {
            Player var7 = var4 ? (Player)var3 : null;
            if (var7 == null && !var2) {
               throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false, new String[0]);
            } else {
               PlayerInventory var8 = var7 != null ? var7.getInventory() : null;
               if (var8 != null && var1x.hasFlag("itemheld") && !ACFBukkitUtil.isValidItem(var8.getItem(var8.getHeldItemSlot()))) {
                  throw new InvalidCommandArgument(MinecraftMessageKeys.YOU_MUST_BE_HOLDING_ITEM, false, new String[0]);
               } else {
                  return var7;
               }
            }
         } else {
            String var5 = var1x.popFirstArg();
            if (var5 == null && var2) {
               if (var1x.hasFlag("defaultself")) {
                  if (var4) {
                     return (Player)var3;
                  } else {
                     throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false, new String[0]);
                  }
               } else {
                  return null;
               }
            } else if (var5 == null) {
               throw new InvalidCommandArgument();
            } else {
               OnlinePlayer var6 = this.getOnlinePlayer((BukkitCommandIssuer)var1x.getIssuer(), var5, false);
               return var6.getPlayer();
            }
         }
      });
      this.registerContext(OfflinePlayer.class, (var1x) -> {
         String var2 = var1x.popFirstArg();
         OfflinePlayer var3;
         if (var1x.hasFlag("uuid")) {
            UUID var4;
            try {
               var4 = UUID.fromString(var2);
            } catch (IllegalArgumentException var6) {
               throw new InvalidCommandArgument(MinecraftMessageKeys.NO_PLAYER_FOUND_OFFLINE, new String[]{"{search}", var2});
            }

            var3 = Bukkit.getOfflinePlayer(var4);
         } else {
            var3 = Bukkit.getOfflinePlayer(var2);
         }

         if (var3 != null && (var3.hasPlayedBefore() || var3.isOnline())) {
            return var3;
         } else if (!var1x.hasFlag("uuid") && !var1.isValidName(var2)) {
            throw new InvalidCommandArgument(MinecraftMessageKeys.IS_NOT_A_VALID_NAME, new String[]{"{name}", var2});
         } else {
            throw new InvalidCommandArgument(MinecraftMessageKeys.NO_PLAYER_FOUND_OFFLINE, new String[]{"{search}", var2});
         }
      });
      this.registerContext(ChatColor.class, (var0) -> {
         String var1 = var0.popFirstArg();
         Stream var2 = Stream.of(ChatColor.values());
         if (var0.hasFlag("colorsonly")) {
            var2 = var2.filter((var0x) -> {
               return var0x.ordinal() <= 15;
            });
         }

         String var3 = var0.getFlagValue("filter", (String)null);
         if (var3 != null) {
            var3 = ACFUtil.simplifyString(var3);
            var2 = var2.filter((var1x) -> {
               return var3.equals(ACFUtil.simplifyString(var1x.name()));
            });
         }

         ChatColor var4 = (ChatColor)ACFUtil.simpleMatch(ChatColor.class, var1);
         if (var4 == null) {
            String var5 = (String)var2.map((var0x) -> {
               return "<c2>" + ACFUtil.simplifyString(var0x.name()) + "</c2>";
            }).collect(Collectors.joining("<c1>,</c1> "));
            throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_ONE_OF, new String[]{"{valid}", var5});
         } else {
            return var4;
         }
      });
      this.registerContext(Location.class, (var0) -> {
         String var1 = var0.popFirstArg();
         CommandSender var2 = var0.getSender();
         String[] var3 = ACFPatterns.COLON.split(var1, 2);
         if (var3.length == 0) {
            throw new InvalidCommandArgument(true);
         } else if (var3.length < 2 && !(var2 instanceof Player) && !(var2 instanceof BlockCommandSender)) {
            throw new InvalidCommandArgument(MinecraftMessageKeys.LOCATION_PLEASE_SPECIFY_WORLD, new String[0]);
         } else {
            Location var6 = null;
            String var4;
            String var5;
            if (var3.length == 2) {
               var4 = var3[0];
               var5 = var3[1];
            } else if (var2 instanceof Player) {
               var6 = ((Player)var2).getLocation();
               var4 = var6.getWorld().getName();
               var5 = var3[0];
            } else {
               if (!(var2 instanceof BlockCommandSender)) {
                  throw new InvalidCommandArgument(true);
               }

               var6 = ((BlockCommandSender)var2).getBlock().getLocation();
               var4 = var6.getWorld().getName();
               var5 = var3[0];
            }

            boolean var7 = var5.startsWith("~");
            var3 = ACFPatterns.COMMA.split(var7 ? var5.substring(1) : var5);
            if (var3.length < 3) {
               throw new InvalidCommandArgument(MinecraftMessageKeys.LOCATION_PLEASE_SPECIFY_XYZ, new String[0]);
            } else {
               Double var8 = ACFUtil.parseDouble(var3[0], var7 ? 0.0D : null);
               Double var9 = ACFUtil.parseDouble(var3[1], var7 ? 0.0D : null);
               Double var10 = ACFUtil.parseDouble(var3[2], var7 ? 0.0D : null);
               if (var6 != null && var7) {
                  var8 = var8 + var6.getX();
                  var9 = var9 + var6.getY();
                  var10 = var10 + var6.getZ();
               } else if (var7) {
                  throw new InvalidCommandArgument(MinecraftMessageKeys.LOCATION_CONSOLE_NOT_RELATIVE, new String[0]);
               }

               if (var8 != null && var9 != null && var10 != null) {
                  World var11 = Bukkit.getWorld(var4);
                  if (var11 == null) {
                     throw new InvalidCommandArgument(MinecraftMessageKeys.INVALID_WORLD, new String[0]);
                  } else if (var3.length >= 5) {
                     Float var12 = ACFUtil.parseFloat(var3[3]);
                     Float var13 = ACFUtil.parseFloat(var3[4]);
                     if (var13 != null && var12 != null) {
                        return new Location(var11, var8, var9, var10, var12, var13);
                     } else {
                        throw new InvalidCommandArgument(MinecraftMessageKeys.LOCATION_PLEASE_SPECIFY_XYZ, new String[0]);
                     }
                  } else {
                     return new Location(var11, var8, var9, var10);
                  }
               } else {
                  throw new InvalidCommandArgument(MinecraftMessageKeys.LOCATION_PLEASE_SPECIFY_XYZ, new String[0]);
               }
            }
         }
      });
      if (var1.mcMinorVersion >= 12) {
         BukkitCommandContexts_1_12.register(this);
      }

   }

   @Contract("_,_,false -> !null")
   OnlinePlayer getOnlinePlayer(BukkitCommandIssuer issuer, String lookup, boolean allowMissing) {
      Player var4 = ACFBukkitUtil.findPlayerSmart((CommandIssuer)var1, var2);
      if (var4 == null) {
         if (var3) {
            return null;
         } else {
            throw new InvalidCommandArgument(false);
         }
      } else {
         return new OnlinePlayer(var4);
      }
   }
}
