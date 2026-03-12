package fr.xephi.authme.command.help;

import fr.xephi.authme.command.CommandArgumentDescription;
import fr.xephi.authme.command.CommandDescription;
import fr.xephi.authme.command.CommandUtils;
import fr.xephi.authme.command.FoundCommandResult;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.DefaultPermission;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpProvider implements Reloadable {
   public static final int SHOW_COMMAND = 1;
   public static final int SHOW_DESCRIPTION = 2;
   public static final int SHOW_LONG_DESCRIPTION = 4;
   public static final int SHOW_ARGUMENTS = 8;
   public static final int SHOW_PERMISSIONS = 16;
   public static final int SHOW_ALTERNATIVES = 32;
   public static final int SHOW_CHILDREN = 64;
   public static final int ALL_OPTIONS = -1;
   private final PermissionsManager permissionsManager;
   private final HelpMessagesService helpMessagesService;
   private Integer enabledSections;

   @Inject
   HelpProvider(PermissionsManager permissionsManager, HelpMessagesService helpMessagesService) {
      this.permissionsManager = permissionsManager;
      this.helpMessagesService = helpMessagesService;
   }

   private List<String> buildHelpOutput(CommandSender sender, FoundCommandResult result, int options) {
      if (result.getCommandDescription() == null) {
         return Collections.singletonList(ChatColor.DARK_RED + "Failed to retrieve any help information!");
      } else {
         List<String> lines = new ArrayList();
         options = this.filterDisabledSections(options);
         if (options == 0) {
            return lines;
         } else {
            String header = this.helpMessagesService.getMessage(HelpMessage.HEADER);
            if (!header.isEmpty()) {
               lines.add(ChatColor.GOLD + header);
            }

            CommandDescription command = this.helpMessagesService.buildLocalizedDescription(result.getCommandDescription());
            List<String> correctLabels = ImmutableList.copyOf((Collection)filterCorrectLabels(command, result.getLabels()));
            if (hasFlag(1, options)) {
               lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.COMMAND) + ": " + CommandUtils.buildSyntax(command, correctLabels));
            }

            if (hasFlag(2, options)) {
               lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.SHORT_DESCRIPTION) + ": " + ChatColor.WHITE + command.getDescription());
            }

            if (hasFlag(4, options)) {
               lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.DETAILED_DESCRIPTION) + ":");
               lines.add(ChatColor.WHITE + " " + command.getDetailedDescription());
            }

            if (hasFlag(8, options)) {
               this.addArgumentsInfo(command, lines);
            }

            if (hasFlag(16, options) && sender != null) {
               this.addPermissionsInfo(command, sender, lines);
            }

            if (hasFlag(32, options)) {
               this.addAlternativesInfo(command, correctLabels, lines);
            }

            if (hasFlag(64, options)) {
               this.addChildrenInfo(command, correctLabels, lines);
            }

            return lines;
         }
      }
   }

   public void outputHelp(CommandSender sender, FoundCommandResult result, int options) {
      List<String> lines = this.buildHelpOutput(sender, result, options);
      Iterator var5 = lines.iterator();

      while(var5.hasNext()) {
         String line = (String)var5.next();
         sender.sendMessage(line);
      }

   }

   public void reload() {
      this.enabledSections = null;
   }

   private int filterDisabledSections(int options) {
      if (this.enabledSections == null) {
         this.enabledSections = this.flagFor(HelpSection.COMMAND, 1) | this.flagFor(HelpSection.SHORT_DESCRIPTION, 2) | this.flagFor(HelpSection.DETAILED_DESCRIPTION, 4) | this.flagFor(HelpSection.ARGUMENTS, 8) | this.flagFor(HelpSection.PERMISSIONS, 16) | this.flagFor(HelpSection.ALTERNATIVES, 32) | this.flagFor(HelpSection.CHILDREN, 64);
      }

      return options & this.enabledSections;
   }

   private int flagFor(HelpSection section, int flag) {
      return this.helpMessagesService.getMessage(section).isEmpty() ? 0 : flag;
   }

   private void addArgumentsInfo(CommandDescription command, List<String> lines) {
      if (!command.getArguments().isEmpty()) {
         lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.ARGUMENTS) + ":");
         StringBuilder argString = new StringBuilder();
         String optionalText = " (" + this.helpMessagesService.getMessage(HelpMessage.OPTIONAL) + ")";

         for(Iterator var5 = command.getArguments().iterator(); var5.hasNext(); lines.add(argString.toString())) {
            CommandArgumentDescription argument = (CommandArgumentDescription)var5.next();
            argString.setLength(0);
            argString.append(" ").append(ChatColor.YELLOW).append(ChatColor.ITALIC).append(argument.getName()).append(": ").append(ChatColor.WHITE).append(argument.getDescription());
            if (argument.isOptional()) {
               argString.append(ChatColor.GRAY).append(ChatColor.ITALIC).append(optionalText);
            }
         }

      }
   }

   private void addAlternativesInfo(CommandDescription command, List<String> correctLabels, List<String> lines) {
      if (command.getLabels().size() > 1) {
         lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.ALTERNATIVES) + ":");
         String usedLabel;
         Function commandLabelsFn;
         if (correctLabels.size() == 1) {
            usedLabel = (String)correctLabels.get(0);
            commandLabelsFn = (labelx) -> {
               return Collections.singletonList(labelx);
            };
         } else {
            usedLabel = (String)correctLabels.get(1);
            commandLabelsFn = (labelx) -> {
               return Arrays.asList((String)correctLabels.get(0), labelx);
            };
         }

         Iterator var6 = command.getLabels().iterator();

         while(var6.hasNext()) {
            String label = (String)var6.next();
            if (!label.equalsIgnoreCase(usedLabel)) {
               lines.add(" " + CommandUtils.buildSyntax(command, (List)commandLabelsFn.apply(label)));
            }
         }

      }
   }

   private void addPermissionsInfo(CommandDescription command, CommandSender sender, List<String> lines) {
      PermissionNode permission = command.getPermission();
      if (permission != null) {
         lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.PERMISSIONS) + ":");
         boolean hasPermission = this.permissionsManager.hasPermission(sender, permission);
         lines.add(String.format(" " + ChatColor.YELLOW + ChatColor.ITALIC + "%s" + ChatColor.GRAY + " (%s)", permission.getNode(), this.getLocalPermissionText(hasPermission)));
         DefaultPermission defaultPermission = permission.getDefaultPermission();
         String addendum = "";
         if (DefaultPermission.OP_ONLY.equals(defaultPermission)) {
            addendum = " (" + this.getLocalPermissionText(defaultPermission.evaluate(sender)) + ")";
         }

         lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpMessage.DEFAULT) + ": " + ChatColor.GRAY + ChatColor.ITALIC + this.helpMessagesService.getMessage(defaultPermission) + addendum);
         ChatColor permissionColor;
         String permissionText;
         if (this.permissionsManager.hasPermission(sender, command.getPermission())) {
            permissionColor = ChatColor.GREEN;
            permissionText = this.getLocalPermissionText(true);
         } else {
            permissionColor = ChatColor.DARK_RED;
            permissionText = this.getLocalPermissionText(false);
         }

         lines.add(String.format(ChatColor.GOLD + " %s: %s" + ChatColor.ITALIC + "%s", this.helpMessagesService.getMessage(HelpMessage.RESULT), permissionColor, permissionText));
      }
   }

   private String getLocalPermissionText(boolean hasPermission) {
      return hasPermission ? this.helpMessagesService.getMessage(HelpMessage.HAS_PERMISSION) : this.helpMessagesService.getMessage(HelpMessage.NO_PERMISSION);
   }

   private void addChildrenInfo(CommandDescription command, List<String> correctLabels, List<String> lines) {
      if (!command.getChildren().isEmpty()) {
         lines.add(ChatColor.GOLD + this.helpMessagesService.getMessage(HelpSection.CHILDREN) + ":");
         String parentCommandPath = String.join(" ", correctLabels);
         Iterator var5 = command.getChildren().iterator();

         while(var5.hasNext()) {
            CommandDescription child = (CommandDescription)var5.next();
            lines.add(" /" + parentCommandPath + " " + (String)child.getLabels().get(0) + ChatColor.GRAY + ChatColor.ITALIC + ": " + this.helpMessagesService.getDescription(child));
         }

      }
   }

   private static boolean hasFlag(int flag, int options) {
      return (flag & options) != 0;
   }

   @VisibleForTesting
   static List<String> filterCorrectLabels(CommandDescription command, List<String> labels) {
      List<CommandDescription> commands = CommandUtils.constructParentList(command);
      List<String> correctLabels = new ArrayList();
      boolean foundIncorrectLabel = false;

      for(int i = 0; i < commands.size(); ++i) {
         if (!foundIncorrectLabel && i < labels.size() && ((CommandDescription)commands.get(i)).hasLabel((String)labels.get(i))) {
            correctLabels.add((String)labels.get(i));
         } else {
            foundIncorrectLabel = true;
            correctLabels.add((String)((CommandDescription)commands.get(i)).getLabels().get(0));
         }
      }

      return correctLabels;
   }
}
