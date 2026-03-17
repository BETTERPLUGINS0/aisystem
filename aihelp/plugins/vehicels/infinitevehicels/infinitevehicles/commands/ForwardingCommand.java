package me.PM2.infinitevehicles.commands;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForwardingCommand extends BaseCommand {
   private final BaseCommand command;
   private final String[] baseArgs;
   private final RegisteredCommand regCommand;

   ForwardingCommand(BaseCommand baseCommand, RegisteredCommand regCommand, String[] baseArgs) {
      this.regCommand = var2;
      this.commandName = var1.commandName;
      this.command = var1;
      this.baseArgs = var3;
      this.manager = var1.manager;
      this.subCommands.put("__default", var2);
   }

   public List<RegisteredCommand> getRegisteredCommands() {
      return Collections.singletonList(this.regCommand);
   }

   public CommandOperationContext getLastCommandOperationContext() {
      return this.command.getLastCommandOperationContext();
   }

   public Set<String> getRequiredPermissions() {
      return this.command.getRequiredPermissions();
   }

   public boolean hasPermission(Object issuer) {
      return this.command.hasPermission(var1);
   }

   public boolean requiresPermission(String permission) {
      return this.command.requiresPermission(var1);
   }

   public boolean hasPermission(CommandIssuer sender) {
      return this.command.hasPermission(var1);
   }

   public List<String> tabComplete(CommandIssuer issuer, RootCommand rootCommand, String[] args, boolean isAsync) {
      return this.command.tabComplete(var1, var2, var3, var4);
   }

   public void execute(CommandIssuer issuer, CommandRouter.CommandRouteResult result) {
      var2 = new CommandRouter.CommandRouteResult(this.regCommand, var2.args, ACFUtil.join(this.baseArgs), var2.commandLabel);
      this.command.execute(var1, var2);
   }

   BaseCommand getCommand() {
      return this.command;
   }
}
