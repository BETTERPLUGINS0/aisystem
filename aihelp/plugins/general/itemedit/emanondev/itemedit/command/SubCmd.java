package emanondev.itemedit.command;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class SubCmd {
   public final String ID;
   private final String permission;
   private final String PATH;
   private final YMLConfig config;
   private final String commandName;
   private final boolean playerOnly;
   private final boolean checkNonNullItem;
   private final AbstractCommand command;
   @NotNull
   private String name;

   public SubCmd(@NotNull String id, @NotNull AbstractCommand command, boolean playerOnly, boolean checkNonNullItem) {
      if (!id.isEmpty() && !id.contains(" ")) {
         this.ID = id.toLowerCase(Locale.ENGLISH);
         this.command = command;
         this.commandName = command.getName();
         this.playerOnly = playerOnly;
         this.checkNonNullItem = checkNonNullItem;
         this.PATH = this.getCommand().getName() + "." + this.ID + ".";
         this.config = this.getPlugin().getConfig("commands.yml");
         this.load();
         this.permission = this.getPlugin().getName().toLowerCase(Locale.ENGLISH) + "." + this.commandName + "." + this.ID;
      } else {
         throw new IllegalArgumentException();
      }
   }

   @NotNull
   public AbstractCommand getCommand() {
      return this.command;
   }

   @NotNull
   public APlugin getPlugin() {
      return this.command.getPlugin();
   }

   public boolean checkNonNullItem() {
      return this.checkNonNullItem;
   }

   @NotNull
   protected ItemStack getItemInHand(@NotNull Player p) {
      return ItemUtils.getHandItem(p);
   }

   private void load() {
      this.name = this.getConfigString("name").toLowerCase(Locale.ENGLISH);
      if (this.name.isEmpty() || this.name.contains(" ")) {
         this.name = this.ID;
      }

   }

   public void reload() {
      this.load();
   }

   protected BaseComponent[] craftFailFeedback(String alias, String params, List<String> desc) {
      if (params == null) {
         params = "";
      }

      ComponentBuilder fail = (new ComponentBuilder(ChatColor.RED + "/" + alias + " " + this.name + " " + params)).event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + alias + " " + this.name + " " + params));
      if (desc != null && !desc.isEmpty()) {
         fail.event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(String.join("\n", desc))).create()));
      }

      return fail.create();
   }

   protected void sendFeedbackForSub(CommandSender target, @NotNull String subSubCommand, String... holders) {
      this.sendCustomFeedbackForSub(target, subSubCommand, "feedback", holders);
   }

   protected void sendCustomFeedbackForSub(CommandSender target, @NotNull String subSubCommand, @NotNull String feedbackPath, String... holders) {
      Util.sendMessage(target, this.getLanguageString(subSubCommand + "." + feedbackPath, (String)null, target, holders));
   }

   protected void sendFailFeedbackForSub(CommandSender target, String alias, String subSubCommand) {
      String params = this.getLanguageString(subSubCommand + ".params", (String)null, target);
      target.spigot().sendMessage(this.craftFailFeedback(alias, subSubCommand + (params != null && !params.isEmpty() ? " " + params : ""), this.getLanguageStringList(subSubCommand + ".description", (List)null, target)));
   }

   protected <T> void onWrongAlias(String pathMessage, CommandSender sender, IAliasSet<T> set, String... holders) {
      String msg = this.getLanguageString(pathMessage, (String)null, sender, holders);
      if (msg != null && !msg.isEmpty()) {
         YMLConfig language = ItemEdit.get().getLanguageConfig(sender);
         StringBuilder hover = (new StringBuilder(language.getMessage("itemedit.listaliases.error-pre-hover", ""))).append("\n");
         String color1 = language.getMessage("itemedit.listaliases.first_color", "");
         String color2 = language.getMessage("itemedit.listaliases.second_color", "");
         boolean color = true;
         int counter = 0;
         Iterator var12 = set.getValues().iterator();

         while(var12.hasNext()) {
            T value = var12.next();
            String alias = set.getName(value);
            counter += alias.length() + 1;
            hover.append(color ? color1 : color2).append(alias);
            color = !color;
            if (counter > 30) {
               counter = 0;
               hover.append("\n");
            } else {
               hover.append(" ");
            }
         }

         Util.sendMessage(sender, (new ComponentBuilder(msg)).event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + ItemEditCommand.get().getName() + " " + ItemEdit.get().getConfig("commands.yml").getString("itemedit.listaliases.name") + " " + set.getID())).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(hover.toString())).create())).create());
      }
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

   @NotNull
   public ComponentBuilder getHelp(@NotNull ComponentBuilder base, @NotNull CommandSender sender, @NotNull String alias) {
      String help = ChatColor.DARK_GREEN + "/" + alias + " " + ChatColor.GREEN + this.name + " ";
      base.append(help + this.getLanguageString("params", "", sender).replace(ChatColor.RESET.toString(), ChatColor.GREEN.toString())).event(new ClickEvent(Action.SUGGEST_COMMAND, ChatColor.stripColor(help))).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(this.getDescription(sender))).create()));
      return base;
   }

   public void onFail(@NotNull CommandSender target, @NotNull String alias) {
      String params = this.getLanguageString("params", "", target);
      Util.sendMessage(target, (new ComponentBuilder(ChatColor.RED + "/" + alias + " " + this.name + " " + ChatColor.stripColor(params))).event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + alias + " " + this.name + " " + ChatColor.stripColor(params))).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(this.getDescription(target))).create())).create());
   }

   protected String getDescription(@NotNull CommandSender target) {
      return String.join("\n", this.getLanguageStringList("description", (List)null, target));
   }

   public abstract void onCommand(@NotNull CommandSender var1, @NotNull String var2, String[] var3);

   public abstract List<String> onComplete(@NotNull CommandSender var1, String[] var2);

   protected void updateView(@NotNull Player player) {
      InventoryUtils.updateView(player);
   }

   public String getPermission() {
      return this.permission;
   }

   public boolean isPlayerOnly() {
      return this.playerOnly;
   }

   @NotNull
   public String getName() {
      return this.name;
   }
}
