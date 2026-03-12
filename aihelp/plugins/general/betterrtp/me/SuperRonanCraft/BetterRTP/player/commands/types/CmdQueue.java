package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTPSetupInformation;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.messages.Message;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueData;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueHandler;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import me.SuperRonanCraft.BetterRTP.references.web.LogUploader;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CmdQueue implements RTPCommand {
   public String getName() {
      return "queue";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      Player p = (Player)sendi;
      World world = args.length > 1 ? Bukkit.getWorld(args[1]) : null;
      AsyncHandler.async(() -> {
         if (world != null) {
            sendInfo(sendi, queueGetWorld(p, world), label, args);
         } else {
            this.queueWorlds(p, label, args);
         }

      });
   }

   public static void sendInfo(CommandSender sendi, List<String> list, String label, String[] args) {
      boolean upload = Arrays.asList(args).contains("_UPLOAD_");
      list.add(0, "&e&m-----&6 BetterRTP &8| Queue &e&m-----");
      list.forEach((str) -> {
         list.set(list.indexOf(str), Message.color(str));
      });
      String cmd = "/" + label + " " + String.join(" ", args);
      if (!upload) {
         sendi.sendMessage((String[])list.toArray(new String[0]));
         if (sendi instanceof Player) {
            TextComponent component = new TextComponent(Message.color("&7- &7Click to upload command log to &flogs.ronanplugins.com"));
            component.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, cmd + " _UPLOAD_"));
            component.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(Message.color("&6Suggested command&f: &7/betterrtp " + String.join(" ", args) + " _UPLOAD_"))).create()));
            ((Player)sendi).spigot().sendMessage(component);
         } else {
            sendi.sendMessage("Execute `" + cmd + " _UPLOAD_` to upload command log to https://logs.ronanplugins.com");
         }
      } else {
         list.add(0, "Command: " + cmd);
         list.forEach((str) -> {
            list.set(list.indexOf(str), ChatColor.stripColor(str));
         });
         CompletableFuture.runAsync(() -> {
            String key = LogUploader.post(list);
            if (key == null) {
               Message.sms((CommandSender)sendi, (List)(new ArrayList(Collections.singletonList("&cAn error occured attempting to upload log!"))), (Object)null);
            } else {
               try {
                  JSONObject json = (JSONObject)(new JSONParser()).parse(key);
                  Message.sms((CommandSender)sendi, (List)Arrays.asList(" ", Message.getPrefix(Message_RTP.msg) + "&aLog uploaded! &fView&7: &6https://logs.ronanplugins.com/" + json.get("key")), (Object)null);
               } catch (ParseException var4) {
                  throw new RuntimeException(var4);
               }
            }

         });
      }

   }

   private void queueWorlds(Player p, String label, String[] args) {
      List<String> info = new ArrayList();
      int locs = 0;

      List list;
      for(Iterator var6 = Bukkit.getWorlds().iterator(); var6.hasNext(); locs += list.size()) {
         World w = (World)var6.next();
         list = queueGetWorld(p, w);
         info.addAll(list);
      }

      info.add("&eTotal of &a%amount% &egenerated locations".replace("%amount%", String.valueOf(locs)));
      sendInfo(p, info, label, args);
   }

   private static List<String> queueGetWorld(Player player, World world) {
      List<String> info = new ArrayList();
      info.add("&eWorld: &6" + world.getName());
      RTPSetupInformation setup_info = new RTPSetupInformation(HelperRTP.getActualWorld(player, world), player, player, true);
      WorldPlayer pWorld = HelperRTP.getPlayerWorld(setup_info);
      Iterator var5 = QueueHandler.getApplicableAsync(pWorld).iterator();

      while(var5.hasNext()) {
         QueueData queue = (QueueData)var5.next();
         String str = "&8- &7x= &b%x, &7z= &b%z";
         Location loc = queue.getLocation();
         str = str.replace("%x", String.valueOf(loc.getBlockX())).replace("%z", String.valueOf(loc.getBlockZ()));
         info.add(str);
      }

      return info;
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      List<String> info = new ArrayList();
      if (args.length == 2) {
         Iterator var4 = Bukkit.getWorlds().iterator();

         while(var4.hasNext()) {
            World world = (World)var4.next();
            if (world.getName().startsWith(args[1])) {
               info.add(world.getName());
            }
         }
      }

      return info;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.ADMIN;
   }
}
