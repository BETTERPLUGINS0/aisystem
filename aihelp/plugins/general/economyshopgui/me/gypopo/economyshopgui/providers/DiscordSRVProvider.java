package me.gypopo.economyshopgui.providers;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.EmbedType;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.AuthorInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Provider;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.VideoInfo;
import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DiscordSRVProvider {
   private final EconomyShopGUI plugin;
   private TextChannel transactionChannel;
   public boolean enabled;
   private String raw;
   private String title;
   private String description;
   private String authorTitle;
   private String authorImage;
   private String footerTitle;
   private String footerImage;
   private ArrayList<DiscordSRVProvider.Field> fields;
   private int color;

   public DiscordSRVProvider(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public DiscordSRVProvider(EconomyShopGUI plugin, ConfigurationSection section) {
      this.plugin = plugin;
      this.enabled = this.init(section);
   }

   public boolean init(ConfigurationSection section) {
      TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(section.getString("channel"));
      if (channel != null) {
         this.transactionChannel = channel;
         if (section.contains("embed.color")) {
            this.color = this.getRGBColor();
         }

         if (section.contains("embed.title")) {
            this.title = section.getString("embed.title");
         }

         if (section.contains("embed.description")) {
            this.description = section.getString("embed.description");
         }

         if (section.contains("embed.author.title")) {
            this.authorTitle = section.getString("embed.author.title");
         }

         if (section.contains("embed.author.imageURL")) {
            this.authorImage = section.getString("embed.author.imageURL");
         }

         if (section.contains("embed.footer.title")) {
            this.footerTitle = section.getString("embed.footer.title");
         }

         if (section.contains("embed.footer.imageURL")) {
            this.footerImage = section.getString("embed.footer.imageURL");
         }

         ArrayList<DiscordSRVProvider.Field> fields = new ArrayList();
         ConfigurationSection conf = ConfigManager.getConfig().getConfigurationSection("discordsrv-transactions.embed.fields");
         if (conf != null) {
            Iterator var5 = conf.getKeys(false).iterator();

            while(var5.hasNext()) {
               String i = (String)var5.next();
               ConfigurationSection f = ConfigManager.getConfig().getConfigurationSection("discordsrv-transactions.embed.fields." + i);
               fields.add(new DiscordSRVProvider.Field(f.getString("title"), f.getString("message")));
            }
         }

         this.fields = fields;
         if (section.contains("raw")) {
            this.raw = section.getString("raw");
         }

         SendMessage.infoMessage("Successfully enabled discordSRV hook, transactions will be logged to #" + this.transactionChannel);
         return true;
      } else {
         SendMessage.warnMessage("Failed to hook into DiscordSRV because no game channel with name '" + section.getString("channel") + "' was found inside the DiscordSRV config.");
         return false;
      }
   }

   @Subscribe
   public void discordReadyEvent(DiscordReadyEvent event) {
      this.enabled = this.init(ConfigManager.getConfig().getConfigurationSection("discordsrv-transactions"));
   }

   private int getRGBColor() {
      String raw = ConfigManager.getConfig().getString("discordsrv-transactions.embed.color");
      raw = raw.replace("#", "");
      if (!raw.startsWith("0x")) {
         raw = "0x" + raw;
      }

      try {
         return Color.decode(raw).getRGB();
      } catch (NumberFormatException var3) {
         SendMessage.logDebugMessage(Lang.RGB_COLOR_FORMATTED_WRONG.get().replace("%path%", "discordsrv-transactions.embed.color"));
         SendMessage.errorItemConfig("discordsrv-transactions.embed.color");
         return 255;
      }
   }

   private String translateMessage(String message, Player player, int amount, String transactionMode, String items, String price) {
      return message.replace("%translations-discord-srv-transaction%", Lang.DISCORD_SRV_PLAYER_TRANSACTION.get().getLegacy()).replace("%player_name%", player.getName()).replace("%player_displayname%", ChatColor.stripColor(this.plugin.getDisplayName(player))).replace("%amount%", String.valueOf(amount)).replace("%bought/sold%", transactionMode).replace("%items%", ChatUtil.stripColor(items)).replace("%price%", ChatColor.stripColor(price));
   }

   private List<github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Field> getFields(Player player, int amount, String transactionMode, String items, String price) {
      List<github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Field> fields = new ArrayList();
      if (this.fields == null) {
         return fields;
      } else {
         Iterator var7 = this.fields.iterator();

         while(var7.hasNext()) {
            DiscordSRVProvider.Field field = (DiscordSRVProvider.Field)var7.next();
            String title = this.translateMessage(field.title, player, amount, transactionMode, items, price);
            String value = this.translateMessage(field.message, player, amount, transactionMode, items, price);
            fields.add(new github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Field(title, value, false));
         }

         return fields;
      }
   }

   public void logTransaction(Player player, int amount, String transactionMode, String items, String price) {
      if (this.raw != null && !this.raw.isEmpty()) {
         this.transactionChannel.sendMessage(this.translateMessage(this.raw, player, amount, transactionMode, items, price)).queue();
      } else {
         this.transactionChannel.sendMessageEmbeds(new MessageEmbed((String)null, this.title, this.description != null && !this.description.isEmpty() ? this.translateMessage(this.description, player, amount, transactionMode, items, price) : null, EmbedType.RICH, (OffsetDateTime)null, this.color, (Thumbnail)null, (Provider)null, this.authorTitle != null && !this.authorTitle.isEmpty() ? new AuthorInfo(this.authorTitle, (String)null, this.getAuthorImage(player), (String)null) : null, (VideoInfo)null, this.footerTitle != null && !this.footerTitle.isEmpty() ? new Footer(this.footerTitle, this.getFooterImage(player), (String)null) : null, (ImageInfo)null, this.getFields(player, amount, transactionMode, items, price)), new MessageEmbed[0]).queue();
      }

   }

   private String getFooterImage(Player player) {
      return this.footerImage.replace("%player_name%", player.getName());
   }

   private String getAuthorImage(Player player) {
      return this.authorImage.replace("%player_name%", player.getName());
   }

   private class Field {
      final String title;
      final String message;

      public Field(String param2, String param3) {
         this.title = title;
         this.message = message;
      }
   }
}
