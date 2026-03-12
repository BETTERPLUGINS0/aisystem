package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.Util;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomFlag implements Listener, Comparable<CustomFlag> {
   private final String ID;
   private final String key;
   private final Flag subCommand;
   private final String PATH;
   private final YMLConfig config;

   public CustomFlag(@NotNull String id, @NotNull String key, @NotNull Flag subCommand) {
      if (!id.isEmpty() && !id.contains(" ")) {
         this.ID = id;
         this.key = key;
         this.subCommand = subCommand;
         this.config = this.getPlugin().getConfig("commands.yml");
         this.PATH = subCommand.getCommand().getName() + "." + subCommand.ID + "." + this.ID + ".";
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int compareTo(CustomFlag flag) {
      return this.getId().compareTo(flag.getId());
   }

   public APlugin getPlugin() {
      return this.subCommand.getPlugin();
   }

   protected String getLanguageString(String path, String def, CommandSender sender, String... holders) {
      return this.getPlugin().getLanguageConfig(sender).loadMessage(this.PATH + path, def == null ? "" : def, sender instanceof Player ? (Player)sender : null, true, holders);
   }

   protected void sendLanguageString(String path, String def, CommandSender sender, String... holders) {
      Util.sendMessage(sender, this.getLanguageString(path, def, sender, holders));
   }

   protected List<String> getLanguageStringList(String path, List<String> def, CommandSender sender, String... holders) {
      return this.getPlugin().getLanguageConfig(sender).loadMultiMessage(this.PATH + path, (List)(def == null ? new ArrayList() : def), sender instanceof Player ? (Player)sender : null, true, holders);
   }

   protected String getConfigString(String path, String... holders) {
      return this.config.loadMessage(this.PATH + path, "", (Player)null, true, holders);
   }

   protected int getConfigInt(String path) {
      return this.config.loadInteger(this.PATH + path, 0);
   }

   protected long getConfigLong(String path) {
      return this.config.loadLong(this.PATH + path, 0L);
   }

   protected boolean getConfigBoolean(String path) {
      return this.config.loadBoolean(this.PATH + path, true);
   }

   protected List<String> getConfigStringList(String path, String... holders) {
      return this.config.loadMultiMessage(this.PATH + path, new ArrayList(), (Player)null, true, holders);
   }

   public final String getId() {
      return this.ID;
   }

   public boolean getValue(TagItem item) {
      Boolean value = item.getBoolean(this.key);
      return value == null ? this.defaultValue() : value;
   }

   public void setValue(TagItem item, boolean val) {
      if (val == this.defaultValue()) {
         item.removeTag(this.getKey());
      } else {
         item.setTag(this.getKey(), val);
      }

   }

   public boolean defaultValue() {
      return true;
   }

   public abstract ItemStack getGuiItem();

   public final String getKey() {
      return this.key;
   }

   public void reload() {
   }

   protected ItemStack getItemInHand(Player p) {
      return ItemUtils.getHandItem(p);
   }
}
