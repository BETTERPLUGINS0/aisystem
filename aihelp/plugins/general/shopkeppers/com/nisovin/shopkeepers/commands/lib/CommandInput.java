package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public final class CommandInput {
   private final CommandSender sender;
   private final Command command;
   private final String commandAlias;
   private final List<? extends String> arguments;

   public CommandInput(CommandSender sender, Command command, String commandAlias, String[] arguments) {
      this(sender, command, commandAlias, Arrays.asList((String[])Validate.notNull(arguments, (String)"arguments is null")));
   }

   public CommandInput(CommandSender sender, Command command, String commandAlias, List<? extends String> arguments) {
      Validate.notNull(sender, (String)"sender is null");
      Validate.notNull(command, (String)"command is null");
      Validate.notEmpty(commandAlias, "commandAlias is null or empty");
      Validate.notNull(arguments, (String)"arguments is null");
      Validate.isTrue(!CollectionUtils.containsNull(arguments), "arguments contains null");
      this.sender = sender;
      this.command = command;
      this.commandAlias = commandAlias;
      this.arguments = Collections.unmodifiableList(arguments);
   }

   public CommandSender getSender() {
      return this.sender;
   }

   public Command getCommand() {
      return this.command;
   }

   public String getCommandAlias() {
      return this.commandAlias;
   }

   public List<? extends String> getArguments() {
      return this.arguments;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CommandInput [sender=");
      builder.append(this.sender.getName());
      builder.append(", command=");
      builder.append(this.command.getName());
      builder.append(", commandAlias=");
      builder.append(this.commandAlias);
      builder.append(", arguments=");
      builder.append(this.arguments);
      builder.append("]");
      return builder.toString();
   }
}
