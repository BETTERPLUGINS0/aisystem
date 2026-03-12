package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ListAliases extends SubCmd {
   public ListAliases(ItemEditCommand cmd) {
      super("listaliases", cmd, false, false);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      try {
         if (args.length != 1 && args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String postfix;
         String postfix;
         String colorOne;
         String colorTwo;
         Iterator var12;
         String aliasS;
         if (args.length == 1) {
            String prefix = this.getLanguageString("prefix_line", (String)null, sender, new String[0]);
            postfix = this.getLanguageString("postfix_line", (String)null, sender, new String[0]);
            postfix = this.getLanguageString("first_color", (String)null, sender, new String[0]);
            colorOne = this.getLanguageString("second_color", (String)null, sender, new String[0]);
            colorTwo = this.getLanguageString("hover_type", (String)null, sender, new String[0]);
            ComponentBuilder comp;
            if (prefix != null && !prefix.isEmpty()) {
               comp = new ComponentBuilder(prefix + "\n");
            } else {
               comp = new ComponentBuilder("");
            }

            boolean counter = true;
            List<String> values = new ArrayList(Aliases.getTypes().keySet());
            Collections.sort(values);

            for(var12 = values.iterator(); var12.hasNext(); counter = !counter) {
               aliasS = (String)var12.next();
               comp.retain(FormatRetention.NONE).append((counter ? postfix : colorOne) + aliasS).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(colorTwo)).create())).event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + alias + " " + this.getName() + " " + aliasS)).append(" ");
            }

            if (postfix != null && !postfix.isEmpty()) {
               comp.retain(FormatRetention.NONE).append("\n" + postfix);
            }

            Util.sendMessage(sender, comp.create());
         } else {
            IAliasSet set = (IAliasSet)Aliases.getTypes().get(args[1].toLowerCase(Locale.ENGLISH));
            if (set == null) {
               this.onFail(sender, alias);
               return;
            }

            postfix = this.getLanguageString("prefix_line", (String)null, sender, new String[0]);
            postfix = this.getLanguageString("postfix_line", (String)null, sender, new String[0]);
            colorOne = this.getLanguageString("first_color", (String)null, sender, new String[0]);
            colorTwo = this.getLanguageString("second_color", (String)null, sender, new String[0]);
            String hover = this.getLanguageString("hover_info", (String)null, sender, new String[]{"%default%", "%default%"});
            ComponentBuilder comp;
            if (postfix != null && !postfix.isEmpty()) {
               comp = new ComponentBuilder(postfix + "\n");
            } else {
               comp = new ComponentBuilder("");
            }

            boolean counter = true;

            for(var12 = set.getAliases().iterator(); var12.hasNext(); counter = !counter) {
               aliasS = (String)var12.next();
               comp.retain(FormatRetention.NONE).append((counter ? colorOne : colorTwo) + aliasS).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(hover.replace("%default%", set.getName(set.convertAlias(aliasS))))).create())).append(" ");
            }

            if (postfix != null && !postfix.isEmpty()) {
               comp.retain(FormatRetention.NONE).append("\n" + postfix);
            }

            Util.sendMessage(sender, comp.create());
         }
      } catch (Exception var14) {
         this.onFail(sender, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (Collection)Aliases.getTypes().keySet()) : Collections.emptyList();
   }
}
