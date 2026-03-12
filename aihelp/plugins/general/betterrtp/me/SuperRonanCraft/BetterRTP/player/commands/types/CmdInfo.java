package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.player.commands.RTP_SETUP_TYPE;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTPSetupInformation;
import me.SuperRonanCraft.BetterRTP.player.rtp.effects.RTPEffect_Particles;
import me.SuperRonanCraft.BetterRTP.references.PermissionCheck;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.messages.Message;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueHandler;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldDefault;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import me.SuperRonanCraft.BetterRTP.references.web.LogUploader;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xyz.xenondevs.particle.ParticleEffect;

public class CmdInfo implements RTPCommand, RTPCommandHelpable {
   public String getName() {
      return "info";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      if (args.length > 1) {
         if (args[1].equalsIgnoreCase(CmdInfo.CmdInfoSub.PARTICLES.name())) {
            this.infoParticles(sendi);
         } else if (args[1].equalsIgnoreCase(CmdInfo.CmdInfoSub.SHAPES.name())) {
            this.infoShapes(sendi);
         } else if (args[1].equalsIgnoreCase(CmdInfo.CmdInfoSub.POTION_EFFECTS.name())) {
            this.infoEffects(sendi);
         } else {
            World world;
            Player player;
            if (args[1].equalsIgnoreCase(CmdInfo.CmdInfoSub.WORLD.name())) {
               world = null;
               player = null;
               if (args.length <= 2) {
                  MessagesCore.DISABLED_WORLD.send(sendi, (Object)"NULL");
                  return;
               }

               world = Bukkit.getWorld(args[2]);
               if (world == null) {
                  MessagesCore.DISABLED_WORLD.send(sendi, (Object)args[2]);
                  return;
               }

               if (args.length > 3) {
                  player = Bukkit.getPlayer(args[3]);
                  if (player == null) {
                     MessagesCore.NOTONLINE.send(sendi, (Object)args[2]);
                     return;
                  }
               }

               sendInfoWorld(sendi, infoGetWorld(sendi, world, player, (WorldPlayer)null), label, args);
            } else if (args[1].equalsIgnoreCase(CmdInfo.CmdInfoSub.PLAYER.name())) {
               world = null;
               player = null;
               if (args.length > 2) {
                  player = Bukkit.getPlayer(args[2]);
                  if (player != null) {
                     world = player.getWorld();
                  }
               }

               if (player == null) {
                  MessagesCore.NOTONLINE.send(sendi, (Object)(args.length > 2 ? args[2] : "NULL"));
                  return;
               }

               if (world == null) {
                  world = player.getWorld();
               }

               sendInfoWorld(sendi, infoGetWorld(sendi, world, player, (WorldPlayer)null), label, args);
            }
         }
      } else {
         this.infoWorld(sendi, label, args);
      }

   }

   public String getHelp() {
      return MessagesHelp.INFO.get();
   }

   private void infoParticles(CommandSender sendi) {
      List<String> info = new ArrayList();
      Iterator var3 = ParticleEffect.VALUES.iterator();

      while(true) {
         while(var3.hasNext()) {
            ParticleEffect eff = (ParticleEffect)var3.next();
            if (!info.isEmpty() && info.size() % 2 != 0) {
               info.add("&f" + eff.name() + "&r");
            } else {
               info.add("&7" + eff.name() + "&r");
            }
         }

         info.forEach((str) -> {
            info.set(info.indexOf(str), Message.color(str));
         });
         sendi.sendMessage(info.toString());
         return;
      }
   }

   private void infoShapes(CommandSender sendi) {
      List<String> info = new ArrayList();
      String[] var3 = RTPEffect_Particles.shapeTypes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String shape = var3[var5];
         if (!info.isEmpty() && info.size() % 2 != 0) {
            info.add("&f" + shape + "&r");
         } else {
            info.add("&7" + shape + "&r");
         }
      }

      info.forEach((str) -> {
         info.set(info.indexOf(str), Message.color(str));
      });
      sendi.sendMessage(info.toString());
   }

   public static void sendInfoWorld(CommandSender sendi, List<String> list, String label, String[] args) {
      boolean upload = Arrays.asList(args).contains("_UPLOAD_");
      list.add(0, "&e&m-----&6 BetterRTP &8| Info &e&m-----");
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

   private void infoWorld(CommandSender sendi, String label, String[] args) {
      List<String> info = new ArrayList();
      Iterator var5 = Bukkit.getWorlds().iterator();

      while(var5.hasNext()) {
         World w = (World)var5.next();
         info.addAll(infoGetWorld(sendi, w, (Player)null, (WorldPlayer)null));
      }

      sendInfoWorld(sendi, info, label, args);
   }

   public static List<String> infoGetWorld(CommandSender sendi, World world, Player player, WorldPlayer _rtpworld) {
      List<String> info = new ArrayList();
      BetterRTP pl = BetterRTP.getInstance();
      String _true = "&aTrue";
      String _false = "&bFalse";
      info.add("&bRTP info for &7" + world.getName() + (player != null ? " &d(personalized)" : ""));
      info.add("&7- &eViewing as: &b" + (player != null ? player.getName() : "ADMIN"));
      info.add("&7- &6Allowed: " + (player != null ? (PermissionCheck.getAWorld(player, world.getName()) ? _true : _false) : "&cN/A"));
      if (pl.getRTP().getDisabledWorlds().contains(world.getName()) && !pl.getRTP().overriden.containsKey(world.getName())) {
         info.add("&7- &eDisabled: " + _true);
      } else {
         info.add("&7- &eDisabled: " + _false);
         if (pl.getRTP().overriden.containsKey(world.getName())) {
            world = Bukkit.getWorld((String)pl.getRTP().overriden.get(world.getName()));
            info.add("&7- &6Overriden: " + _true + " &7- target `" + world.getName() + "`");
         } else {
            info.add("&7- &6Overriden&7: " + _false);
         }

         if (_rtpworld == null) {
            _rtpworld = HelperRTP.getPlayerWorld(new RTPSetupInformation(world, (CommandSender)(player != null ? player : sendi), player, player != null));
         }

         WorldDefault worldDefault = BetterRTP.getInstance().getRTP().getRTPdefaultWorld();
         info.add("&7- &eSetup Type&7: " + _rtpworld.setup_type.name() + getInfo(_rtpworld, worldDefault, "setup"));
         info.add("&7- &6Use World Border&7: " + (_rtpworld.getUseWorldborder() ? _true : _false));
         info.add("&7- &eWorld Type&7: &f" + _rtpworld.getWorldtype().name());
         info.add("&7- &6Center X&7: &f" + _rtpworld.getCenterX() + getInfo(_rtpworld, worldDefault, "centerx"));
         info.add("&7- &eCenter Z&7: &f" + _rtpworld.getCenterZ() + getInfo(_rtpworld, worldDefault, "centerz"));
         info.add("&7- &6Max Radius&7: &f" + _rtpworld.getMaxRadius() + getInfo(_rtpworld, worldDefault, "maxrad"));
         info.add("&7- &eMin Radius&7: &f" + _rtpworld.getMinRadius() + getInfo(_rtpworld, worldDefault, "minrad"));
         info.add("&7- &6Min Y&7: &f" + _rtpworld.getMinY());
         info.add("&7- &eMax Y&7: &f" + _rtpworld.getMaxY());
         info.add("&7- &6Price&7: &f" + _rtpworld.getPrice() + getInfo(_rtpworld, worldDefault, "price"));
         info.add("&7- &eCooldown&7: &f" + _rtpworld.getCooldown() + getInfo(_rtpworld, worldDefault, "cooldown"));
         info.add("&7- &6Biomes&7: &f" + _rtpworld.getBiomes().toString());
         info.add("&7- &eShape&7: &f" + _rtpworld.getShape().toString() + getInfo(_rtpworld, worldDefault, "shape"));
         info.add("&7- &6Permission Group&7: " + (_rtpworld.getConfig() != null ? "&a" + _rtpworld.getConfig().getGroupName() : "&cN/A"));
         info.add("&7- &eQueue Available&7: " + (QueueHandler.isEnabled() ? QueueHandler.getApplicableAsync(_rtpworld).size() : "&cDisabled"));
      }

      return info;
   }

   private static String getInfo(WorldPlayer worldPlayer, WorldDefault worldDefault, String type) {
      byte var4 = -1;
      switch(type.hashCode()) {
      case -1081120719:
         if (type.equals("maxrad")) {
            var4 = 2;
         }
         break;
      case -1074030461:
         if (type.equals("minrad")) {
            var4 = 3;
         }
         break;
      case -546109589:
         if (type.equals("cooldown")) {
            var4 = 7;
         }
         break;
      case 106934601:
         if (type.equals("price")) {
            var4 = 4;
         }
         break;
      case 109329021:
         if (type.equals("setup")) {
            var4 = 6;
         }
         break;
      case 109399969:
         if (type.equals("shape")) {
            var4 = 5;
         }
         break;
      case 665239235:
         if (type.equals("centerx")) {
            var4 = 0;
         }
         break;
      case 665239237:
         if (type.equals("centerz")) {
            var4 = 1;
         }
      }

      switch(var4) {
      case 0:
         return !worldPlayer.getUseWorldborder() && worldPlayer.getCenterX() != worldDefault.getCenterX() ? "" : (worldPlayer.getUseWorldborder() ? " &8(worldborder)" : " &8(default)");
      case 1:
         return !worldPlayer.getUseWorldborder() && worldPlayer.getCenterZ() != worldDefault.getCenterZ() ? "" : (worldPlayer.getUseWorldborder() ? " &8(worldborder)" : " &8(default)");
      case 2:
         return !worldPlayer.getUseWorldborder() && worldPlayer.getMaxRadius() != worldDefault.getMaxRadius() ? "" : (worldPlayer.getUseWorldborder() ? ((double)worldPlayer.getMaxRadius() >= worldPlayer.getWorld().getWorldBorder().getSize() ? " &8(worldborder)" : " &8(custom)") : " &8(default)");
      case 3:
         return worldPlayer.getMinRadius() == worldDefault.getMinRadius() ? " &8(default)" : "";
      case 4:
         return worldPlayer.getPrice() == worldDefault.getPrice() ? " &8(default)" : "";
      case 5:
         return worldPlayer.getShape() == worldDefault.getShape() ? " &8(default)" : "";
      case 6:
         return worldPlayer.setup_type == RTP_SETUP_TYPE.LOCATION ? " &7(" + worldPlayer.setup_name + ")" : "";
      case 7:
         return worldPlayer.getPlayer() != null ? (PermissionNode.BYPASS_COOLDOWN.check(worldPlayer.getPlayer()) ? " &8(bypassing)" : "") : " &cN/A";
      default:
         return "";
      }
   }

   private void infoEffects(CommandSender sendi) {
      List<String> info = new ArrayList();
      PotionEffectType[] var3 = PotionEffectType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PotionEffectType effect = var3[var5];
         if (!info.isEmpty() && info.size() % 2 != 0) {
            info.add("&f" + effect.getName() + "&r");
         } else {
            info.add("&7" + effect.getName() + "&r");
         }
      }

      info.forEach((str) -> {
         info.set(info.indexOf(str), Message.color(str));
      });
      sendi.sendMessage(info.toString());
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      List<String> info = new ArrayList();
      if (args.length == 2) {
         CmdInfo.CmdInfoSub[] var4 = CmdInfo.CmdInfoSub.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            CmdInfo.CmdInfoSub cmd = var4[var6];
            if (cmd.name().toLowerCase().startsWith(args[1].toLowerCase())) {
               info.add(cmd.name().toLowerCase());
            }
         }
      } else {
         Iterator var8;
         Player p;
         if (args.length == 3) {
            if (CmdInfo.CmdInfoSub.WORLD.name().toLowerCase().startsWith(args[1].toLowerCase())) {
               var8 = Bukkit.getWorlds().iterator();

               while(var8.hasNext()) {
                  World world = (World)var8.next();
                  if (world.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                     info.add(world.getName());
                  }
               }
            } else if (CmdInfo.CmdInfoSub.PLAYER.name().toLowerCase().startsWith(args[1].toLowerCase())) {
               var8 = Bukkit.getOnlinePlayers().iterator();

               while(var8.hasNext()) {
                  p = (Player)var8.next();
                  if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                     info.add(p.getName());
                  }
               }
            }
         } else if (args.length == 4 && CmdInfo.CmdInfoSub.WORLD.name().toLowerCase().startsWith(args[1].toLowerCase())) {
            var8 = Bukkit.getOnlinePlayers().iterator();

            while(var8.hasNext()) {
               p = (Player)var8.next();
               if (p.getName().toLowerCase().startsWith(args[3].toLowerCase())) {
                  info.add(p.getName());
               }
            }
         }
      }

      return info;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.INFO;
   }

   static enum CmdInfoSub {
      PARTICLES,
      SHAPES,
      POTION_EFFECTS,
      WORLD,
      PLAYER;

      // $FF: synthetic method
      private static CmdInfo.CmdInfoSub[] $values() {
         return new CmdInfo.CmdInfoSub[]{PARTICLES, SHAPES, POTION_EFFECTS, WORLD, PLAYER};
      }
   }
}
