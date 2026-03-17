package me.PM2.infinitevehicles.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.commands.bukkit.contexts.OnlinePlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.StringUtil;

public class BukkitCommandCompletions extends CommandCompletions<BukkitCommandCompletionContext> {
   public BukkitCommandCompletions(BukkitCommandManager manager) {
      super(var1);
      this.registerAsyncCompletion("mobs", (var0) -> {
         Stream var1 = Stream.of(EntityType.values()).map((var0x) -> {
            return ACFUtil.simplifyString(var0x.getName());
         });
         return (Collection)var1.collect(Collectors.toList());
      });
      this.registerAsyncCompletion("chatcolors", (var0) -> {
         Stream var1 = Stream.of(ChatColor.values());
         if (var0.hasConfig("colorsonly")) {
            var1 = var1.filter((var0x) -> {
               return var0x.ordinal() <= 15;
            });
         }

         String var2 = var0.getConfig("filter");
         if (var2 != null) {
            Set var3 = (Set)Arrays.stream(ACFPatterns.COLON.split(var2)).map(ACFUtil::simplifyString).collect(Collectors.toSet());
            var1 = var1.filter((var1x) -> {
               return var3.contains(ACFUtil.simplifyString(var1x.name()));
            });
         }

         return (Collection)var1.map((var0x) -> {
            return ACFUtil.simplifyString(var0x.name());
         }).collect(Collectors.toList());
      });
      this.registerAsyncCompletion("dyecolors", (var0) -> {
         return ACFUtil.enumNames((Enum[])DyeColor.values());
      });
      this.registerCompletion("worlds", (var0) -> {
         return (Collection)Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList());
      });
      this.registerCompletion("players", (var0) -> {
         CommandSender var1 = var0.getSender();
         Validate.notNull(var1, "Sender cannot be null");
         Player var2 = var1 instanceof Player ? (Player)var1 : null;
         ArrayList var3 = new ArrayList();
         Iterator var4 = Bukkit.getOnlinePlayers().iterator();

         while(true) {
            Player var5;
            String var6;
            do {
               if (!var4.hasNext()) {
                  var3.sort(String.CASE_INSENSITIVE_ORDER);
                  return var3;
               }

               var5 = (Player)var4.next();
               var6 = var5.getName();
            } while(var2 != null && !var2.canSee(var5));

            if (StringUtil.startsWithIgnoreCase(var6, var0.getInput())) {
               var3.add(var6);
            }
         }
      });
      this.setDefaultCompletion("players", new Class[]{OnlinePlayer.class, me.PM2.infinitevehicles.commands.contexts.OnlinePlayer.class, Player.class});
      this.setDefaultCompletion("worlds", new Class[]{World.class});
   }
}
