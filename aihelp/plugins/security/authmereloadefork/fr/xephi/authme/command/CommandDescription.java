package fr.xephi.authme.command;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandDescription {
   private List<String> labels;
   private String description;
   private String detailedDescription;
   private Class<? extends ExecutableCommand> executableCommand;
   private CommandDescription parent;
   private List<CommandDescription> children;
   private List<CommandArgumentDescription> arguments;
   private PermissionNode permission;

   private CommandDescription(List<String> labels, String description, String detailedDescription, Class<? extends ExecutableCommand> executableCommand, CommandDescription parent, List<CommandArgumentDescription> arguments, PermissionNode permission) {
      this.children = new ArrayList();
      this.labels = labels;
      this.description = description;
      this.detailedDescription = detailedDescription;
      this.executableCommand = executableCommand;
      this.parent = parent;
      this.arguments = arguments;
      this.permission = permission;
   }

   public List<String> getLabels() {
      return this.labels;
   }

   public boolean hasLabel(String commandLabel) {
      Iterator var2 = this.labels.iterator();

      String label;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         label = (String)var2.next();
      } while(!label.equalsIgnoreCase(commandLabel));

      return true;
   }

   public Class<? extends ExecutableCommand> getExecutableCommand() {
      return this.executableCommand;
   }

   public CommandDescription getParent() {
      return this.parent;
   }

   public int getLabelCount() {
      return this.parent == null ? 1 : this.parent.getLabelCount() + 1;
   }

   public List<CommandDescription> getChildren() {
      return this.children;
   }

   public List<CommandArgumentDescription> getArguments() {
      return this.arguments;
   }

   public String getDescription() {
      return this.description;
   }

   public String getDetailedDescription() {
      return this.detailedDescription;
   }

   public PermissionNode getPermission() {
      return this.permission;
   }

   public static CommandDescription.CommandBuilder builder() {
      return new CommandDescription.CommandBuilder();
   }

   // $FF: synthetic method
   CommandDescription(List x0, String x1, String x2, Class x3, CommandDescription x4, List x5, PermissionNode x6, Object x7) {
      this(x0, x1, x2, x3, x4, x5, x6);
   }

   public static final class CommandBuilder {
      private List<String> labels;
      private String description;
      private String detailedDescription;
      private Class<? extends ExecutableCommand> executableCommand;
      private CommandDescription parent;
      private List<CommandArgumentDescription> arguments = new ArrayList();
      private PermissionNode permission;

      public CommandDescription register() {
         CommandDescription command = this.build();
         if (command.parent != null) {
            command.parent.children.add(command);
         }

         return command;
      }

      public CommandDescription build() {
         Preconditions.checkArgument(!Utils.isCollectionEmpty(this.labels), "Labels may not be empty");
         Preconditions.checkArgument(!StringUtils.isBlank(this.description), "Description may not be empty");
         Preconditions.checkArgument(!StringUtils.isBlank(this.detailedDescription), "Detailed description may not be empty");
         Preconditions.checkArgument(this.executableCommand != null, "Executable command must be set");
         return new CommandDescription(this.labels, this.description, this.detailedDescription, this.executableCommand, this.parent, this.arguments, this.permission);
      }

      public CommandDescription.CommandBuilder labels(List<String> labels) {
         this.labels = labels;
         return this;
      }

      public CommandDescription.CommandBuilder labels(String... labels) {
         return this.labels(Arrays.asList(labels));
      }

      public CommandDescription.CommandBuilder description(String description) {
         this.description = description;
         return this;
      }

      public CommandDescription.CommandBuilder detailedDescription(String detailedDescription) {
         this.detailedDescription = detailedDescription;
         return this;
      }

      public CommandDescription.CommandBuilder executableCommand(Class<? extends ExecutableCommand> executableCommand) {
         this.executableCommand = executableCommand;
         return this;
      }

      public CommandDescription.CommandBuilder parent(CommandDescription parent) {
         this.parent = parent;
         return this;
      }

      public CommandDescription.CommandBuilder withArgument(String label, String description, boolean isOptional) {
         this.arguments.add(new CommandArgumentDescription(label, description, isOptional));
         return this;
      }

      public CommandDescription.CommandBuilder permission(PermissionNode permission) {
         this.permission = permission;
         return this;
      }
   }
}
