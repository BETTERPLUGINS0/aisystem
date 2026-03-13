package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.decree.DecreeParameter;
import com.volmit.iris.util.decree.virtual.VirtualDecreeCommand;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.kyori.adventure.text.Component;
import com.volmit.iris.util.kyori.adventure.text.minimessage.MiniMessage;
import com.volmit.iris.util.kyori.adventure.title.Title;
import com.volmit.iris.util.kyori.adventure.title.Title.Times;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.J;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandSender.Spigot;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class VolmitSender implements CommandSender {
   private static final KMap<String, String> helpCache = new KMap();
   private final CommandSender s;
   private String tag;
   private String command;

   public VolmitSender(CommandSender s) {
      this.tag = "";
      this.s = var1;
   }

   public VolmitSender(CommandSender s, String tag) {
      this.tag = var2;
      this.s = var1;
   }

   public static long getTick() {
      return M.ms() / 16L;
   }

   public static String pulse(String colorA, String colorB, double speed) {
      return "<gradient:" + var0 + ":" + var1 + ":" + pulse(var2) + ">";
   }

   public static String pulse(double speed) {
      return Form.f(invertSpread((double)getTick() * 15.0D * var0 % 1000.0D / 1000.0D), 3).replaceAll("\\Q,\\E", ".").replaceAll("\\Q?\\E", "-");
   }

   public static double invertSpread(double v) {
      return (1.0D - var0) * 2.0D - 1.0D;
   }

   public static <T> KList<T> paginate(KList<T> all, int linesPerPage, int page, AtomicBoolean hasNext) {
      int var4 = (int)Math.ceil((double)var0.size() / (double)var1);
      var2 = var2 < 0 ? 0 : (var2 >= var4 ? var4 - 1 : var2);
      var3.set(var2 < var4 - 1);
      KList var5 = new KList();

      for(int var6 = var1 * var2; var6 < Math.min(var0.size(), var1 * (var2 + 1)); ++var6) {
         var5.add((Object)var0.get(var6));
      }

      return var5;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String tag) {
      this.tag = var1;
   }

   public boolean isPlayer() {
      return this.getS() instanceof Player;
   }

   public Player player() {
      return (Player)this.getS();
   }

   public CommandSender getS() {
      return this.s;
   }

   public boolean isPermissionSet(String name) {
      return this.s.isPermissionSet(var1);
   }

   public boolean isPermissionSet(org.bukkit.permissions.Permission perm) {
      return this.s.isPermissionSet(var1);
   }

   public boolean hasPermission(String name) {
      return this.s.hasPermission(var1);
   }

   public boolean hasPermission(org.bukkit.permissions.Permission perm) {
      return this.s.hasPermission(var1);
   }

   public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
      return this.s.addAttachment(var1, var2, var3);
   }

   public PermissionAttachment addAttachment(Plugin plugin) {
      return this.s.addAttachment(var1);
   }

   public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
      return this.s.addAttachment(var1, var2, var3, var4);
   }

   public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
      return this.s.addAttachment(var1, var2);
   }

   public void removeAttachment(PermissionAttachment attachment) {
      this.s.removeAttachment(var1);
   }

   public void recalculatePermissions() {
      this.s.recalculatePermissions();
   }

   public Set<PermissionAttachmentInfo> getEffectivePermissions() {
      return this.s.getEffectivePermissions();
   }

   public boolean isOp() {
      return this.s.isOp();
   }

   public void setOp(boolean value) {
      this.s.setOp(var1);
   }

   public void hr() {
      this.s.sendMessage("========================================================");
   }

   public void sendTitle(String title, String subtitle, int i, int s, int o) {
      Iris.audiences.player(this.player()).showTitle(Title.title(this.createComponent(var1), this.createComponent(var2), Times.times(Duration.ofMillis((long)var3), Duration.ofMillis((long)var4), Duration.ofMillis((long)var5))));
   }

   public void sendProgress(double percent, String thing) {
      String var10001;
      byte var4;
      int var5;
      if (var1 < 0.0D) {
         var4 = 44;
         var5 = (int)(1.0D * (double)var4);
         this.sendTitle(String.valueOf(C.IRIS) + var3 + " ", 0, 500, 250);
         var10001 = pulse("#00ff80", "#00373d", 1.0D);
         this.sendActionNoProcessing(var10001 + "<underlined> " + Form.repeat(" ", var5) + "<reset>" + Form.repeat(" ", var4 - var5));
      } else {
         var4 = 44;
         var5 = (int)(var1 * (double)var4);
         var10001 = String.valueOf(C.IRIS);
         this.sendTitle(var10001 + var3 + " " + String.valueOf(C.BLUE) + "<font:minecraft:uniform>" + Form.pc(var1, 0), 0, 500, 250);
         var10001 = pulse("#00ff80", "#00373d", 1.0D);
         this.sendActionNoProcessing(var10001 + "<underlined> " + Form.repeat(" ", var5) + "<reset>" + Form.repeat(" ", var4 - var5));
      }

   }

   public void sendAction(String action) {
      Iris.audiences.player(this.player()).sendActionBar(this.createNoPrefixComponent(var1));
   }

   public void sendActionNoProcessing(String action) {
      Iris.audiences.player(this.player()).sendActionBar(this.createNoPrefixComponentNoProcessing(var1));
   }

   public void sendTitle(String subtitle, int i, int s, int o) {
      Iris.audiences.player(this.player()).showTitle(Title.title(this.createNoPrefixComponent(" "), this.createNoPrefixComponent(var1), Times.times(Duration.ofMillis((long)var2), Duration.ofMillis((long)var3), Duration.ofMillis((long)var4))));
   }

   private Component createNoPrefixComponent(String message) {
      String var2;
      if (!IrisSettings.get().getGeneral().canUseCustomColors(this)) {
         var2 = C.translateAlternateColorCodes('&', MiniMessage.miniMessage().stripTags(var1));
         return MiniMessage.miniMessage().deserialize(C.mini(var2));
      } else {
         var2 = C.translateAlternateColorCodes('&', var1);
         String var3 = C.aura(var2, IrisSettings.get().getGeneral().getSpinh(), IrisSettings.get().getGeneral().getSpins(), IrisSettings.get().getGeneral().getSpinb(), 0.36D);
         return MiniMessage.miniMessage().deserialize(var3);
      }
   }

   private Component createNoPrefixComponentNoProcessing(String message) {
      return MiniMessage.builder().postProcessor((var0) -> {
         return var0;
      }).build().deserialize(C.mini(var1));
   }

   private Component createComponent(String message) {
      String var2;
      if (!IrisSettings.get().getGeneral().canUseCustomColors(this)) {
         MiniMessage var4 = MiniMessage.miniMessage();
         String var10002 = this.getTag();
         var2 = C.translateAlternateColorCodes('&', var4.stripTags(var10002 + var1));
         return MiniMessage.miniMessage().deserialize(C.mini(var2));
      } else {
         String var10001 = this.getTag();
         var2 = C.translateAlternateColorCodes('&', var10001 + var1);
         String var3 = C.aura(var2, IrisSettings.get().getGeneral().getSpinh(), IrisSettings.get().getGeneral().getSpins(), IrisSettings.get().getGeneral().getSpinb());
         return MiniMessage.miniMessage().deserialize(var3);
      }
   }

   private Component createComponentRaw(String message) {
      String var2;
      if (!IrisSettings.get().getGeneral().canUseCustomColors(this)) {
         MiniMessage var3 = MiniMessage.miniMessage();
         String var10002 = this.getTag();
         var2 = C.translateAlternateColorCodes('&', var3.stripTags(var10002 + var1));
         return MiniMessage.miniMessage().deserialize(C.mini(var2));
      } else {
         String var10001 = this.getTag();
         var2 = C.translateAlternateColorCodes('&', var10001 + var1);
         return MiniMessage.miniMessage().deserialize(C.mini(var2));
      }
   }

   public <T> void showWaiting(String passive, CompletableFuture<T> f) {
      AtomicInteger var3 = new AtomicInteger();
      AtomicReference var4 = new AtomicReference();
      var3.set(J.ar(() -> {
         if (var2.isDone() && var4.get() != null) {
            J.car(var3.get());
            this.sendAction(" ");
         } else {
            this.sendProgress(-1.0D, var1);
         }
      }, 0));
      J.a(() -> {
         try {
            var4.set(var2.get());
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         } catch (ExecutionException var4x) {
            var4x.printStackTrace();
         }

      });
   }

   public void sendMessage(String message) {
      if (!(this.s instanceof CommandDummy)) {
         CommandSender var10000;
         String var10002;
         if ((IrisSettings.get().getGeneral().isUseCustomColorsIngame() || !(this.s instanceof Player)) && IrisSettings.get().getGeneral().isUseConsoleCustomColors()) {
            if (var1.contains("<NOMINI>")) {
               var10000 = this.s;
               var10002 = this.getTag();
               var10000.sendMessage(C.translateAlternateColorCodes('&', var10002 + var1.replaceAll("\\Q<NOMINI>\\E", "")));
            } else {
               try {
                  Iris.audiences.sender(this.s).sendMessage(this.createComponent(var1));
               } catch (Throwable var5) {
                  String var10001 = this.getTag();
                  String var3 = C.translateAlternateColorCodes('&', var10001 + var1);
                  String var4 = C.aura(var3, IrisSettings.get().getGeneral().getSpinh(), IrisSettings.get().getGeneral().getSpins(), IrisSettings.get().getGeneral().getSpinb());
                  Iris.debug("<NOMINI>Failure to parse " + var4);
                  var10000 = this.s;
                  var10002 = this.getTag();
                  var10000.sendMessage(C.translateAlternateColorCodes('&', var10002 + var1));
               }

            }
         } else {
            var10000 = this.s;
            var10002 = this.getTag();
            var10000.sendMessage(C.translateAlternateColorCodes('&', var10002 + var1));
         }
      }
   }

   public void sendMessageBasic(String message) {
      CommandSender var10000 = this.s;
      String var10002 = this.getTag();
      var10000.sendMessage(C.translateAlternateColorCodes('&', var10002 + var1));
   }

   public void sendMessageRaw(String message) {
      if (!(this.s instanceof CommandDummy)) {
         if ((IrisSettings.get().getGeneral().isUseCustomColorsIngame() || !(this.s instanceof Player)) && IrisSettings.get().getGeneral().isUseConsoleCustomColors()) {
            if (var1.contains("<NOMINI>")) {
               this.s.sendMessage(var1.replaceAll("\\Q<NOMINI>\\E", ""));
            } else {
               try {
                  Iris.audiences.sender(this.s).sendMessage(this.createComponentRaw(var1));
               } catch (Throwable var5) {
                  String var10001 = this.getTag();
                  String var3 = C.translateAlternateColorCodes('&', var10001 + var1);
                  String var4 = C.aura(var3, IrisSettings.get().getGeneral().getSpinh(), IrisSettings.get().getGeneral().getSpins(), IrisSettings.get().getGeneral().getSpinb());
                  Iris.debug("<NOMINI>Failure to parse " + var4);
                  CommandSender var10000 = this.s;
                  String var10002 = this.getTag();
                  var10000.sendMessage(C.translateAlternateColorCodes('&', var10002 + var1));
               }

            }
         } else {
            this.s.sendMessage(C.translateAlternateColorCodes('&', var1));
         }
      }
   }

   public void sendMessage(String[] messages) {
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         this.sendMessage(var5);
      }

   }

   public void sendMessage(UUID uuid, String message) {
      this.sendMessage(var2);
   }

   public void sendMessage(UUID uuid, String[] messages) {
      this.sendMessage(var2);
   }

   public Server getServer() {
      return this.s.getServer();
   }

   public String getName() {
      return this.s.getName();
   }

   public Spigot spigot() {
      return this.s.spigot();
   }

   private String pickRandoms(int max, VirtualDecreeCommand i) {
      KList var3 = new KList();

      for(int var4 = 0; var4 < var1; ++var4) {
         var3.add((Object)(var2.isNode() ? (var2.getNode().getParameters().isNotEmpty() ? "<#aebef2>✦ <#5ef288>" + var2.getParentPath() + " <#42ecf5>" + var2.getName() + " " + var2.getNode().getParameters().shuffleCopy(RNG.r).convert((var0) -> {
            return !var0.isRequired() && !RNG.r.b(0.5D) ? "" : "<#f2e15e>" + (String)var0.getNames().getRandom() + "=<#d665f0>" + var0.example();
         }).toString(" ") : "") : ""));
      }

      return var3.removeDuplicates().convert((var0) -> {
         return var0.replaceAll("\\Q  \\E", " ");
      }).toString("\n");
   }

   public void sendHeader(String name, int overrideLength) {
      int var4 = var1.length() + 2;
      String var5 = Form.repeat(" ", var2 - var4 - 4);
      String var6 = Form.repeat("(", 3);
      String var7 = Form.repeat(")", 3);
      String var8 = "[";
      String var9 = "]";
      if (var1.trim().isEmpty()) {
         this.sendMessageRaw("<font:minecraft:uniform><strikethrough><gradient:#34eb6b:#32bfad>" + var8 + var5 + "<reset><font:minecraft:uniform><strikethrough><gradient:#32bfad:#34eb6b>" + var5 + var9);
      } else {
         this.sendMessageRaw("<font:minecraft:uniform><strikethrough><gradient:#34eb6b:#32bfad>" + var8 + var5 + var6 + "<reset> <gradient:#3299bf:#323bbf>" + var1 + "<reset> <font:minecraft:uniform><strikethrough><gradient:#32bfad:#34eb6b>" + var7 + var5 + var9);
      }

   }

   public void sendHeader(String name) {
      this.sendHeader(var1, 44);
   }

   public void sendDecreeHelp(VirtualDecreeCommand v) {
      this.sendDecreeHelp(var1, 0);
   }

   public void sendDecreeHelp(VirtualDecreeCommand v, int page) {
      if (!this.isPlayer()) {
         Iterator var7 = var1.getNodes().iterator();

         while(var7.hasNext()) {
            VirtualDecreeCommand var8 = (VirtualDecreeCommand)var7.next();
            this.sendDecreeHelpNode(var8);
         }

      } else {
         int var3 = var1.getNodes().size();
         this.sendMessageRaw("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
         if (var1.getNodes().isNotEmpty()) {
            String var10001 = var1.getPath();
            this.sendHeader(var10001 + (var2 > 0 ? " {" + (var2 + 1) + "}" : ""));
            if (this.isPlayer() && var1.getParent() != null) {
               var10001 = Form.capitalize(var1.getParent().getName());
               this.sendMessageRaw("<hover:show_text:'<#b54b38>Click to go back to <#3299bf>" + var10001 + " Help'><click:run_command:" + var1.getParent().getPath() + "><font:minecraft:uniform><#f58571>〈 Back</click></hover>");
            }

            AtomicBoolean var4 = new AtomicBoolean(false);
            Iterator var5 = paginate(var1.getNodes(), 17, var2, var4).iterator();

            while(var5.hasNext()) {
               VirtualDecreeCommand var6 = (VirtualDecreeCommand)var5.next();
               this.sendDecreeHelpNode(var6);
            }

            String var9 = "";
            int var10 = 75 - (var2 > 0 ? 10 : 0) - (var4.get() ? 10 : 0);
            if (var2 > 0) {
               var9 = var9 + "<hover:show_text:'<green>Click to go back to page " + var2 + "'><click:run_command:" + var1.getPath() + " help=" + var2 + "><gradient:#27b84d:#2770b8>〈 Page " + var2 + "</click></hover><reset> ";
            }

            var9 = var9 + "<reset><font:minecraft:uniform><strikethrough><gradient:#32bfad:#34eb6b>" + Form.repeat(" ", var10) + "<reset>";
            if (var4.get()) {
               var9 = var9 + " <hover:show_text:'<green>Click to go to back to page " + (var2 + 2) + "'><click:run_command:" + var1.getPath() + " help=" + (var2 + 2) + "><gradient:#2770b8:#27b84d>Page " + (var2 + 2) + " ❭</click></hover>";
            }

            this.sendMessageRaw(var9);
         } else {
            this.sendMessage(String.valueOf(C.RED) + "There are no subcommands in this group! Contact support, this is a command design issue!");
         }

      }
   }

   public void sendDecreeHelpNode(VirtualDecreeCommand i) {
      if (!this.isPlayer() && !(this.s instanceof CommandDummy)) {
         this.sendMessage(var1.getPath());
      } else {
         this.sendMessageRaw((String)helpCache.computeIfAbsent(var1.getPath(), (var2) -> {
            String var3 = "<reset>\n";
            String var10000 = var1.getPath();
            String var4 = var10000 + " ><#46826a>⇀<gradient:#42ecf5:#428df5> " + var1.getName();
            String var5 = var1.getNames().copy().reverse().convert((var0) -> {
               return "<#42ecf5>" + var0;
            }).toString(", ");
            String var6 = "<#3fe05a>✎ <#6ad97d><font:minecraft:uniform>" + var1.getDescription();
            String var7 = "<#bbe03f>✒ <#a8e0a2><font:minecraft:uniform>";
            String var8;
            if (var1.isNode()) {
               if (var1.getNode().getParameters().isEmpty()) {
                  var7 = var7 + "There are no parameters. Click to type command.";
                  var8 = "suggest_command";
               } else {
                  var7 = var7 + "Hover over all of the parameters to learn more.";
                  var8 = "suggest_command";
               }
            } else {
               var7 = var7 + "This is a command category. Click to run.";
               var8 = "run_command";
            }

            String var9 = "";
            String var10 = "";
            if (var1.isNode() && var1.getNode().getParameters().isNotEmpty()) {
               var9 = var9 + var3 + "<#aebef2>✦ <#5ef288><font:minecraft:uniform>" + var1.getParentPath() + " <#42ecf5>" + var1.getName() + " " + var1.getNode().getParameters().convert((var0) -> {
                  return "<#d665f0>" + var0.example();
               }).toString(" ");
               var10 = var10 + var3 + "<font:minecraft:uniform>" + this.pickRandoms(Math.min(var1.getNode().getParameters().size() + 1, 5), var1);
            }

            StringBuilder var11 = new StringBuilder();
            if (var1.isNode()) {
               Iterator var12 = var1.getNode().getParameters().iterator();

               while(var12.hasNext()) {
                  DecreeParameter var13 = (DecreeParameter)var12.next();
                  String var14 = "<gradient:#d665f0:#a37feb>" + var13.getName();
                  String var15 = var13.getNames().convert((var0) -> {
                     return "<#d665f0>" + var0;
                  }).toString(", ");
                  String var16 = "<#3fe05a>✎ <#6ad97d><font:minecraft:uniform>" + var13.getDescription();
                  boolean var21 = var13.isContextual();
                  Iris.debug("Contextual: " + var21 + " / player: " + this.isPlayer());
                  String var17;
                  String var18;
                  if (var13.isContextual() && (this.isPlayer() || this.s instanceof CommandDummy)) {
                     var18 = "<#ffcc00>[" + var14 + "<#ffcc00>] ";
                     var17 = "<#ff9900>➱ <#ffcc00><font:minecraft:uniform>The value may be derived from environment context.";
                  } else if (var13.isRequired()) {
                     var18 = "<red>[" + var14 + "<red>] ";
                     var17 = "<#db4321>⚠ <#faa796><font:minecraft:uniform>This parameter is required.";
                  } else if (var13.hasDefault()) {
                     var18 = "<#4f4f4f>⊰" + var14 + "<#4f4f4f>⊱";
                     var17 = "<#2181db>✔ <#78dcf0><font:minecraft:uniform>Defaults to \"" + var13.getParam().defaultValue() + "\" if undefined.";
                  } else {
                     var18 = "<#4f4f4f>⊰" + var14 + "<#4f4f4f>⊱";
                     var17 = "<#a73abd>✔ <#78dcf0><font:minecraft:uniform>This parameter is optional.";
                  }

                  String var19 = "<#cc00ff>✢ <#ff33cc><font:minecraft:uniform>This parameter is of type " + var13.getType().getSimpleName() + ".";
                  var11.append("<hover:show_text:'").append(var15).append(var3).append(var16).append(var3).append(var17).append(var3).append(var19).append("'>").append(var18).append("</hover>");
               }
            } else {
               var11 = new StringBuilder("<gradient:#afe3d3:#a2dae0> - Category of Commands");
            }

            String var20 = "<hover:show_text:'" + var5 + var3 + var6 + var3 + var7 + var9 + var10 + "'><click:" + var8 + ":" + var4 + "</click></hover> " + String.valueOf(var11);
            return var20;
         }));
      }

   }

   public void playSound(Sound sound, float volume, float pitch) {
      if (this.isPlayer()) {
         this.player().playSound(this.player().getLocation(), var1, var2, var3);
      }

   }

   @Generated
   public static KMap<String, String> getHelpCache() {
      return helpCache;
   }

   @Generated
   public String getCommand() {
      return this.command;
   }

   @Generated
   public void setCommand(final String command) {
      this.command = var1;
   }
}
