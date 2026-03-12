package fr.xephi.authme.command;

import fr.xephi.authme.command.executable.HelpCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class CommandMapper {
   private static final Class<? extends ExecutableCommand> HELP_COMMAND_CLASS = HelpCommand.class;
   private final Collection<CommandDescription> baseCommands;
   private final PermissionsManager permissionsManager;

   @Inject
   public CommandMapper(CommandInitializer commandInitializer, PermissionsManager permissionsManager) {
      this.baseCommands = commandInitializer.getCommands();
      this.permissionsManager = permissionsManager;
   }

   public FoundCommandResult mapPartsToCommand(CommandSender sender, List<String> parts) {
      if (Utils.isCollectionEmpty(parts)) {
         return new FoundCommandResult((CommandDescription)null, parts, (List)null, 0.0D, FoundResultStatus.MISSING_BASE_COMMAND);
      } else {
         CommandDescription base = this.getBaseCommand((String)parts.get(0));
         if (base == null) {
            return new FoundCommandResult((CommandDescription)null, parts, (List)null, 0.0D, FoundResultStatus.MISSING_BASE_COMMAND);
         } else {
            List<String> remainingParts = parts.subList(1, parts.size());
            CommandDescription childCommand = getSuitableChild(base, remainingParts);
            FoundResultStatus status;
            if (childCommand != null) {
               status = this.getPermissionAwareStatus(sender, childCommand);
               FoundCommandResult result = new FoundCommandResult(childCommand, parts.subList(0, 2), parts.subList(2, parts.size()), 0.0D, status);
               return transformResultForHelp(result);
            } else if (hasSuitableArgumentCount(base, remainingParts.size())) {
               status = this.getPermissionAwareStatus(sender, base);
               return new FoundCommandResult(base, parts.subList(0, 1), parts.subList(1, parts.size()), 0.0D, status);
            } else {
               return getCommandWithSmallestDifference(base, parts);
            }
         }
      }
   }

   public Set<Class<? extends ExecutableCommand>> getCommandClasses() {
      Set<Class<? extends ExecutableCommand>> classes = new HashSet(50);
      Iterator var2 = this.baseCommands.iterator();

      while(var2.hasNext()) {
         CommandDescription command = (CommandDescription)var2.next();
         classes.add(command.getExecutableCommand());
         Iterator var4 = command.getChildren().iterator();

         while(var4.hasNext()) {
            CommandDescription child = (CommandDescription)var4.next();
            classes.add(child.getExecutableCommand());
         }
      }

      return classes;
   }

   private static FoundCommandResult getCommandWithSmallestDifference(CommandDescription base, List<String> parts) {
      if (parts.size() <= 1) {
         return new FoundCommandResult(base, parts, new ArrayList(), 0.0D, FoundResultStatus.INCORRECT_ARGUMENTS);
      } else {
         String childLabel = (String)parts.get(1);
         double minDifference = Double.POSITIVE_INFINITY;
         CommandDescription closestCommand = null;
         Iterator var6 = base.getChildren().iterator();

         while(var6.hasNext()) {
            CommandDescription child = (CommandDescription)var6.next();
            double difference = getLabelDifference(child, childLabel);
            if (difference < minDifference) {
               minDifference = difference;
               closestCommand = child;
            }
         }

         if (closestCommand == null) {
            return new FoundCommandResult(base, parts.subList(0, 1), parts.subList(1, parts.size()), 0.0D, FoundResultStatus.INCORRECT_ARGUMENTS);
         } else {
            FoundResultStatus status = minDifference == 0.0D ? FoundResultStatus.INCORRECT_ARGUMENTS : FoundResultStatus.UNKNOWN_LABEL;
            int partsSize = parts.size();
            List<String> labels = parts.subList(0, Math.min(closestCommand.getLabelCount(), partsSize));
            List<String> arguments = labels.size() == partsSize ? new ArrayList() : parts.subList(labels.size(), partsSize);
            return new FoundCommandResult(closestCommand, labels, (List)arguments, minDifference, status);
         }
      }
   }

   private CommandDescription getBaseCommand(String label) {
      String baseLabel = label.toLowerCase(Locale.ROOT);
      if (baseLabel.startsWith("authme:")) {
         baseLabel = baseLabel.substring("authme:".length());
      }

      Iterator var3 = this.baseCommands.iterator();

      CommandDescription command;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         command = (CommandDescription)var3.next();
      } while(!command.hasLabel(baseLabel));

      return command;
   }

   private static CommandDescription getSuitableChild(CommandDescription baseCommand, List<String> parts) {
      if (Utils.isCollectionEmpty(parts)) {
         return null;
      } else {
         String label = ((String)parts.get(0)).toLowerCase(Locale.ROOT);
         int argumentCount = parts.size() - 1;
         Iterator var4 = baseCommand.getChildren().iterator();

         CommandDescription child;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            child = (CommandDescription)var4.next();
         } while(!child.hasLabel(label) || !hasSuitableArgumentCount(child, argumentCount));

         return child;
      }
   }

   private static FoundCommandResult transformResultForHelp(FoundCommandResult result) {
      if (result.getCommandDescription() != null && HELP_COMMAND_CLASS == result.getCommandDescription().getExecutableCommand()) {
         List<String> arguments = new ArrayList(result.getArguments());
         arguments.add(0, (String)result.getLabels().get(0));
         return new FoundCommandResult(result.getCommandDescription(), result.getLabels(), arguments, result.getDifference(), result.getResultStatus());
      } else {
         return result;
      }
   }

   private FoundResultStatus getPermissionAwareStatus(CommandSender sender, CommandDescription command) {
      return sender != null && !this.permissionsManager.hasPermission(sender, command.getPermission()) ? FoundResultStatus.NO_PERMISSION : FoundResultStatus.SUCCESS;
   }

   private static boolean hasSuitableArgumentCount(CommandDescription command, int argumentCount) {
      int minArgs = CommandUtils.getMinNumberOfArguments(command);
      int maxArgs = CommandUtils.getMaxNumberOfArguments(command);
      return argumentCount >= minArgs && argumentCount <= maxArgs;
   }

   private static double getLabelDifference(CommandDescription command, String givenLabel) {
      return (Double)command.getLabels().stream().map((label) -> {
         return StringUtils.getDifference(label, givenLabel);
      }).min(Double::compareTo).orElseThrow(() -> {
         return new IllegalStateException("Command does not have any labels set");
      });
   }
}
