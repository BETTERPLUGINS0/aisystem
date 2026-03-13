package me.ag4.playershop.commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;
import me.ag4.playershop.files.Lang;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      Player p = (Player)sender;
      if (args.length == 0) {
         if (!((PlayerShop)PlayerShop.getPlugin(PlayerShop.class)).getConfig().getBoolean("Plugin-Link") && !p.isOp()) {
            return false;
         } else {
            TextComponent message = new TextComponent(TextComponent.fromLegacyText(Utils.hex("#FFB000☀ &fClick here to download #FFB000&lPlayerShop &for use this link: #FFB000https://ag4dev.xyz&f.")));
            TextComponent hover = new TextComponent(TextComponent.fromLegacyText(Utils.hex("#FFB000&lCLICK HERE!")));
            message.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(hover)).create()));
            message.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "https://ag4dev.xyz"));
            p.spigot().sendMessage(message);
            return false;
         }
      } else {
         if (p.isOp() && args.length == 1 && args[0].equals("reload")) {
            PlayerShop.getInstance().reloadFiles();
            this.reloadLang();
            p.sendMessage(Utils.hex("&aPlayerShop reloaded!"));
         }

         return false;
      }
   }

   private void reloadLang() {
      File lang = new File(PlayerShop.getInstance().getDataFolder(), "lang.yml");
      YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
      Lang[] var3 = Lang.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Lang item = var3[var5];
         if (langConfig.getString(item.getPath()) == null) {
            langConfig.set(item.getPath(), item.getDefault());
         }
      }

      Set<String> validKeys = (Set)Arrays.stream(Lang.values()).map(Lang::getPath).collect(Collectors.toSet());
      Set<String> yamlKeys = langConfig.getKeys(false);
      Iterator var10 = yamlKeys.iterator();

      while(var10.hasNext()) {
         String key = (String)var10.next();
         if (!validKeys.contains(key)) {
            langConfig.set(key, (Object)null);
         }
      }

      Lang.setFile(langConfig);

      try {
         langConfig.save(lang);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }
}
