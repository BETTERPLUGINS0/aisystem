package emanondev.itemedit.command;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.Util;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCommand implements TabExecutor {
   private final String PATH;
   private final String name;
   private final APlugin plugin;
   private final YMLConfig config;
   private final List<SubCmd> subCmds;
   private final AbstractCommand.HelpSubCommand helpSubCommand;

   public AbstractCommand(@NotNull String name, @NotNull APlugin plugin) {
      this(name, plugin, false);
   }

   public AbstractCommand(@NotNull String name, @NotNull APlugin plugin, boolean multiPageHelp) {
      this.subCmds = new ArrayList();
      this.name = name.toLowerCase(Locale.ENGLISH);
      this.plugin = plugin;
      this.PATH = this.getName();
      this.config = plugin.getConfig("commands.yml");
      if (multiPageHelp) {
         this.helpSubCommand = new AbstractCommand.HelpSubCommand(this);
      } else {
         this.helpSubCommand = null;
      }

   }

   public void reload() {
      this.config.reload();
      Iterator var1 = this.subCmds.iterator();

      while(var1.hasNext()) {
         SubCmd sub = (SubCmd)var1.next();
         sub.reload();
      }

      if (this.helpSubCommand != null) {
         this.helpSubCommand.reload();
      }

   }

   @NotNull
   public List<SubCmd> getAllowedSubCommands(@NotNull CommandSender sender) {
      List<SubCmd> list = new ArrayList();
      this.subCmds.forEach((sub) -> {
         if (sender.hasPermission(sub.getPermission())) {
            list.add(sub);
         }

      });
      if (this.helpSubCommand != null && !this.subCmds.isEmpty()) {
         list.add(this.helpSubCommand);
      }

      return list;
   }

   public void registerSubCommand(@NotNull SubCmd sub) {
      this.subCmds.add(sub);
   }

   public boolean registerSubCommand(@NotNull Supplier<SubCmd> sub) {
      try {
         SubCmd subCommand = (SubCmd)sub.get();
         if (subCommand != null) {
            this.subCmds.add(subCommand);
            return true;
         }
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      return false;
   }

   public boolean registerSubCommand(@NotNull Supplier<SubCmd> sub, boolean condition) {
      return !condition ? false : this.registerSubCommand(sub);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
      SubCmd subCmd = args.length > 0 ? this.getSubCmd(args[0], sender) : null;
      if (this.validateRequires(subCmd, sender, label)) {
         subCmd.onCommand(sender, label, args);
      }

      return true;
   }

   public void sendPermissionLackMessage(@NotNull String permission, @NotNull CommandSender sender) {
      Util.sendMessage(sender, this.getPlugin().getLanguageConfig(sender).loadMessage("lack-permission", "&cYou lack of permission %permission%", sender instanceof Player ? (Player)sender : null, true, "%permission%", permission));
   }

   public void sendPermissionLackGenericMessage(@NotNull CommandSender sender) {
      Util.sendMessage(sender, this.getPlugin().getLanguageConfig(sender).loadMessage("lack-permission-generic", "&cYou don't have permission to use this command", sender instanceof Player ? (Player)sender : null, true));
   }

   public void sendPlayerOnly(@NotNull CommandSender sender) {
      Util.sendMessage(sender, this.getPlugin().getLanguageConfig(sender).loadMessage("player-only", "&cCommand for Players only", sender instanceof Player ? (Player)sender : null, true));
   }

   public void sendNoItemInHand(@NotNull CommandSender sender) {
      Util.sendMessage(sender, this.getPlugin().getLanguageConfig(sender).loadMessage("no-item-on-hand", "&cYou need to hold an item in hand", sender instanceof Player ? (Player)sender : null, true));
   }

   @Contract("null,_,_-> false")
   private boolean validateRequires(@Nullable SubCmd sub, @NotNull CommandSender sender, @NotNull String alias) {
      if (sub == null) {
         this.help(sender, alias);
         return false;
      } else if (!sender.hasPermission(sub.getPermission()) && sub != this.helpSubCommand) {
         this.sendPermissionLackMessage(sub.getPermission(), sender);
         return false;
      } else if (sub.isPlayerOnly() && !(sender instanceof Player)) {
         this.sendPlayerOnly(sender);
         return false;
      } else {
         if (sub.isPlayerOnly() && sub.checkNonNullItem()) {
            ItemStack item = ItemUtils.getHandItem((Player)sender);
            if (ItemUtils.isAirOrNull(item)) {
               this.sendNoItemInHand(sender);
               return false;
            }
         }

         return true;
      }
   }

   private void help(@NotNull CommandSender sender, @NotNull String alias) {
      if (this.helpSubCommand != null) {
         this.helpSubCommand.help(sender, alias, 1);
      } else {
         ComponentBuilder help = new ComponentBuilder(this.getLanguageString("help-header", "&3&l" + this.getName() + " - Help", sender) + "\n");
         boolean c = false;
         Iterator var5 = this.subCmds.iterator();

         while(var5.hasNext()) {
            SubCmd cmd = (SubCmd)var5.next();
            if (sender.hasPermission(cmd.getPermission())) {
               if (c) {
                  help.append("\n");
               } else {
                  c = true;
               }

               help = cmd.getHelp(help, sender, alias);
            }
         }

         if (c) {
            Util.sendMessage(sender, help.create());
         } else {
            this.sendPermissionLackGenericMessage(sender);
         }

      }
   }

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      List<String> l = new ArrayList();
      if (args.length == 1) {
         this.completeCmd((List)l, args[0], sender);
         return (List)l;
      } else {
         if (args.length > 1) {
            SubCmd subCmd = this.getSubCmd(args[0], sender);
            if (subCmd != null && sender.hasPermission(subCmd.getPermission())) {
               l = subCmd.onComplete(sender, args);
            }
         }

         return (List)l;
      }
   }

   public SubCmd getSubCmd(@NotNull String cmd, @NotNull CommandSender sender) {
      Iterator var3 = this.subCmds.iterator();

      SubCmd subCmd;
      do {
         if (!var3.hasNext()) {
            if (this.helpSubCommand != null && this.helpSubCommand.getName().equalsIgnoreCase(cmd) && !this.getAllowedSubCommands(sender).isEmpty()) {
               return this.helpSubCommand;
            }

            return null;
         }

         subCmd = (SubCmd)var3.next();
      } while(!subCmd.getName().equalsIgnoreCase(cmd));

      return subCmd;
   }

   public void completeCmd(@NotNull List<String> l, @NotNull String prefix, @NotNull CommandSender sender) {
      String text = prefix.toLowerCase(Locale.ENGLISH);
      this.getAllowedSubCommands(sender).forEach((cmd) -> {
         if (cmd.getName().startsWith(text)) {
            l.add(cmd.getName());
         }

      });
   }

   protected String getLanguageString(String path, String def, CommandSender sender, String... holders) {
      return this.getPlugin().getLanguageConfig(sender).loadMessage(this.PATH + "." + path, def == null ? "" : def, sender instanceof Player ? (Player)sender : null, true, holders);
   }

   protected List<String> getLanguageStringList(String path, List<String> def, CommandSender sender, String... holders) {
      return this.getPlugin().getLanguageConfig(sender).loadMultiMessage(this.PATH + "." + path, (List)(def == null ? new ArrayList() : def), sender instanceof Player ? (Player)sender : null, true, holders);
   }

   protected String getConfString(String path) {
      return this.config.loadMessage(this.PATH + "." + path, "", true);
   }

   protected int getConfInt(String path) {
      return this.config.loadInteger(this.PATH + "." + path, 0);
   }

   protected long getConfLong(String path) {
      return this.config.loadLong(this.PATH + "." + path, 0L);
   }

   protected boolean getConfBoolean(String path) {
      return this.config.loadBoolean(this.PATH + "." + path, true);
   }

   public String getName() {
      return this.name;
   }

   public APlugin getPlugin() {
      return this.plugin;
   }

   private class HelpSubCommand extends SubCmd {
      private int commandPerPage = Math.max(4, this.getConfigInt("commands_per_page"));

      public HelpSubCommand(@NotNull AbstractCommand param2) {
         super("help", cmd, false, false);
      }

      private int getMaxPageFor(int elements) {
         return elements / this.commandPerPage + (elements % this.commandPerPage == 0 ? 0 : 1);
      }

      public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
         int page = 1;
         if (args.length > 1) {
            try {
               page = Integer.parseInt(args[1]);
            } catch (Exception var7) {
               SubCmd sub = AbstractCommand.this.getSubCmd(args[1], sender);
               if (sub != null) {
                  this.help(sender, alias, sub);
                  return;
               }
            }
         }

         this.help(sender, alias, page);
      }

      public void help(CommandSender sender, String alias, SubCmd sub) {
         ComponentBuilder help = new ComponentBuilder(this.getLanguageString("header-sub", "&3&l" + this.getName() + " %sub% - Help", sender, new String[]{"%sub%", sub.getName()}));
         help.append("\n");
         String helpTxt = ChatColor.DARK_GREEN + "/" + alias + " " + ChatColor.GREEN + sub.getName() + " ";
         help.append(helpTxt + sub.getLanguageString("params", "", sender).replace(ChatColor.RESET.toString(), ChatColor.GREEN.toString()));
         help.append("\n");
         help.append(sub.getDescription(sender));
         Util.sendMessage(sender, help.create());
      }

      public void help(CommandSender sender, String alias, int page) {
         List<SubCmd> cmds = AbstractCommand.this.getAllowedSubCommands(sender);
         if (!cmds.isEmpty()) {
            page = Math.max(1, Math.min(this.getMaxPageFor(cmds.size()), page));
            ComponentBuilder help = new ComponentBuilder("");
            this.injectClickablePages(help, this.getLanguageString("header", "&3&l" + this.getName() + " - Help", sender, new String[0]), sender, alias, page);
            help.append("\n");
            Iterator var6 = cmds.subList(this.commandPerPage * (page - 1), Math.min(cmds.size(), this.commandPerPage * page)).iterator();

            while(var6.hasNext()) {
               SubCmd cmd = (SubCmd)var6.next();
               help = cmd.getHelp(help, sender, alias);
               help.append("\n");
            }

            this.injectClickablePages(help, this.getLanguageString("footer", "&3&l" + this.getName() + " - Help", sender, new String[0]), sender, alias, page);
            Util.sendMessage(sender, help.create());
         } else {
            AbstractCommand.this.sendPermissionLackGenericMessage(sender);
         }

      }

      private void injectClickablePages(ComponentBuilder comp, String text, CommandSender sender, String alias, int page) {
         int maxPage = this.getMaxPageFor(AbstractCommand.this.getAllowedSubCommands(sender).size());
         text = text.replace("%page%", String.valueOf(page)).replace("%max_page%", String.valueOf(maxPage));
         int index = text.indexOf("%prev_clickable%");
         String text1;
         String text2;
         if (index != -1) {
            text1 = text.substring(0, index);
            text2 = text.substring(index + "%prev_clickable%".length());
         } else {
            text1 = text;
            text2 = null;
         }

         String text21 = null;
         String text22 = null;
         index = text1.indexOf("%next_clickable%");
         String text11;
         String text12;
         if (index != -1) {
            text11 = text1.substring(0, index);
            text12 = text1.substring(index + "%next_clickable%".length());
         } else {
            text11 = text1;
            text12 = null;
         }

         if (text2 != null) {
            index = text2.indexOf("%next_clickable%");
            if (index != -1) {
               text21 = text2.substring(0, index);
               text22 = text2.substring(index + "%next_clickable%".length());
            } else {
               text21 = text2;
            }
         }

         comp.retain(FormatRetention.NONE).append(text11);
         if (text12 != null) {
            if (page < maxPage) {
               comp.append(this.getLanguageString("next_text", ">>>>", sender, new String[]{"%target%", String.valueOf(page + 1), "%page%", String.valueOf(page)})).event(new ClickEvent(Action.RUN_COMMAND, "/" + alias + " " + this.getName() + " " + (page + 1))).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(this.getLanguageString("next_hover", "Go to page %target%", sender, new String[]{"%target%", String.valueOf(page + 1), "%page%", String.valueOf(page), "%max_page%", String.valueOf(maxPage)}))).create()));
            } else {
               comp.append(this.getLanguageString("next_void", ">>>>", sender, new String[]{"%page%", String.valueOf(page), "%max_page%", String.valueOf(maxPage)}));
            }

            comp.append(text12).retain(FormatRetention.FORMATTING);
         }

         if (text21 != null) {
            if (page > 1) {
               comp.append(this.getLanguageString("prev_text", "<<<<", sender, new String[]{"%target%", String.valueOf(page - 1), "%page%", String.valueOf(page)})).event(new ClickEvent(Action.RUN_COMMAND, "/" + alias + " " + this.getName() + " " + (page - 1))).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(this.getLanguageString("prev_hover", "Go to page %target%", sender, new String[]{"%target%", String.valueOf(page - 1), "%page%", String.valueOf(page), "%max_page%", String.valueOf(maxPage)}))).create()));
            } else {
               comp.append(this.getLanguageString("prev_void", "<<<<", sender, new String[]{"%page%", String.valueOf(page), "%max_page%", String.valueOf(maxPage)}));
            }

            comp.append(text21).retain(FormatRetention.FORMATTING);
         }

         if (text22 != null) {
            if (page < maxPage) {
               comp.append(this.getLanguageString("next_text", ">>>>", sender, new String[]{"%target%", String.valueOf(page + 1), "%page%", String.valueOf(page)})).event(new ClickEvent(Action.RUN_COMMAND, "/" + alias + " " + this.getName() + " " + (page + 1))).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(this.getLanguageString("next_hover", "Go to page %target%", sender, new String[]{"%target%", String.valueOf(page + 1), "%page%", String.valueOf(page), "%max_page%", String.valueOf(maxPage)}))).create()));
            } else {
               comp.append(this.getLanguageString("next_void", ">>>>", sender, new String[]{"%page%", String.valueOf(page), "%max_page%", String.valueOf(maxPage)}));
            }

            comp.append(text22).retain(FormatRetention.FORMATTING);
         }

      }

      public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
         if (args.length != 2) {
            return Collections.emptyList();
         } else {
            ArrayList<String> tabs = new ArrayList();
            List<SubCmd> subs = AbstractCommand.this.getAllowedSubCommands(sender);

            for(int i = 0; i < this.getMaxPageFor(subs.size()); ++i) {
               tabs.add(String.valueOf(i + 1));
            }

            Iterator var7 = subs.iterator();

            while(var7.hasNext()) {
               SubCmd sub = (SubCmd)var7.next();
               tabs.add(sub.getName());
            }

            return CompleteUtility.complete(args[1], (Collection)tabs);
         }
      }

      public void reload() {
         super.reload();
         this.commandPerPage = Math.max(4, this.getConfigInt("commands_per_page"));
      }
   }
}
