package fr.xephi.authme.command;

import fr.xephi.authme.libs.com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;

public final class CommandUtils {
   private CommandUtils() {
   }

   public static int getMinNumberOfArguments(CommandDescription command) {
      int mandatoryArguments = 0;
      Iterator var2 = command.getArguments().iterator();

      while(var2.hasNext()) {
         CommandArgumentDescription argument = (CommandArgumentDescription)var2.next();
         if (!argument.isOptional()) {
            ++mandatoryArguments;
         }
      }

      return mandatoryArguments;
   }

   public static int getMaxNumberOfArguments(CommandDescription command) {
      return command.getArguments().size();
   }

   public static List<CommandDescription> constructParentList(CommandDescription command) {
      List<CommandDescription> commands = new ArrayList();

      for(CommandDescription currentCommand = command; currentCommand != null; currentCommand = currentCommand.getParent()) {
         commands.add(currentCommand);
      }

      return Lists.reverse(commands);
   }

   public static String constructCommandPath(CommandDescription command) {
      StringBuilder sb = new StringBuilder();
      String prefix = "/";

      for(Iterator var3 = constructParentList(command).iterator(); var3.hasNext(); prefix = " ") {
         CommandDescription ancestor = (CommandDescription)var3.next();
         sb.append(prefix).append((String)ancestor.getLabels().get(0));
      }

      return sb.toString();
   }

   public static String buildSyntax(CommandDescription command, List<String> correctLabels) {
      StringBuilder commandSyntax = new StringBuilder(ChatColor.WHITE + "/" + (String)correctLabels.get(0) + ChatColor.YELLOW);

      for(int i = 1; i < correctLabels.size(); ++i) {
         commandSyntax.append(" ").append((String)correctLabels.get(i));
      }

      Iterator var5 = command.getArguments().iterator();

      while(var5.hasNext()) {
         CommandArgumentDescription argument = (CommandArgumentDescription)var5.next();
         commandSyntax.append(" ").append(formatArgument(argument));
      }

      return commandSyntax.toString();
   }

   public static String formatArgument(CommandArgumentDescription argument) {
      return argument.isOptional() ? "[" + argument.getName() + "]" : "<" + argument.getName() + ">";
   }
}
