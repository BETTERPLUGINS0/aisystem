package org.terraform.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.command.contants.TerraCommandArgument;
import org.terraform.main.TerraformCommandManager;
import org.terraform.main.TerraformGeneratorPlugin;

public class HelpCommand extends TerraCommand {
   private final TerraformCommandManager man;

   public HelpCommand(TerraformGeneratorPlugin plugin, TerraformCommandManager man, String... string) {
      super(plugin, string);
      this.man = man;
   }

   public boolean isInAcceptedParamRange(@NotNull Stack<String> args) {
      return args.size() <= 1;
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(CommandSender sender) {
      return true;
   }

   public void execute(@NotNull CommandSender sender, @NotNull Stack<String> args) {
      ArrayList<TerraCommand> cmds = new ArrayList();
      Iterator var4 = this.man.getCommands().iterator();

      while(var4.hasNext()) {
         TerraCommand cmd = (TerraCommand)var4.next();
         if (cmd.hasPermission(sender)) {
            cmds.add(cmd);
         }
      }

      int maxPages = (int)((double)(cmds.size() / 6));
      int page = 0;
      if (!args.isEmpty()) {
         try {
            page = Integer.parseInt((String)args.pop());
            if (page <= 0) {
               sender.sendMessage(this.plugin.getLang().fetchLang("command.help.postive-pages"));
               return;
            }

            --page;
         } catch (NumberFormatException var12) {
            sender.sendMessage(this.plugin.getLang().fetchLang("command.help.postive-pages"));
            return;
         }
      }

      if (page > maxPages) {
         page = maxPages;
      }

      String base = (String)this.man.bases.get(0);
      String var10001 = String.valueOf(ChatColor.GOLD);
      sender.sendMessage(var10001 + "============[" + String.valueOf(ChatColor.AQUA) + this.plugin.getName() + String.valueOf(ChatColor.GOLD) + "][" + String.valueOf(ChatColor.YELLOW) + "Pg. " + (page + 1) + String.valueOf(ChatColor.GOLD) + "]============");
      sender.sendMessage("");

      for(int i = 0; i < 6; ++i) {
         if (cmds.size() > page * 5 + i) {
            TerraCommand cmd = (TerraCommand)cmds.get(page * 5 + i);
            StringBuilder params = new StringBuilder();
            params.append(ChatColor.YELLOW).append("/").append(base).append(" ").append(String.join("/", cmd.aliases)).append(' ');
            Iterator var10 = cmd.parameters.iterator();

            while(var10.hasNext()) {
               TerraCommandArgument<?> param = (TerraCommandArgument)var10.next();
               if (param.isOptional()) {
                  params.append(ChatColor.GRAY).append("<").append(param.getName()).append("> ");
               } else {
                  params.append(ChatColor.AQUA).append("[").append(param.getName()).append("] ");
               }
            }

            params.append(ChatColor.DARK_GRAY).append("- ").append(ChatColor.YELLOW).append(this.plugin.getLang().fetchLang(cmd.getLangPath()));
            sender.sendMessage(params.toString());
         }
      }

      sender.sendMessage("");
      if (page != maxPages) {
         var10001 = String.valueOf(ChatColor.GRAY);
         sender.sendMessage(var10001 + "/" + base + " h " + (page + 2) + String.valueOf(ChatColor.DARK_GRAY) + "- " + String.valueOf(ChatColor.YELLOW) + (page + 1) + "/" + (maxPages + 1));
      } else {
         var10001 = String.valueOf(ChatColor.AQUA);
         sender.sendMessage(var10001 + (page + 1) + "/" + (maxPages + 1));
      }

   }

   @NotNull
   public String getDefaultDescription() {
      return "Displays a list of commands for this plugin.";
   }
}
