package me.PM2.infinitevehicles.commands;

public class HelpEntry {
   private final CommandHelp commandHelp;
   private final RegisteredCommand command;
   private int searchScore = 1;

   HelpEntry(CommandHelp commandHelp, RegisteredCommand command) {
      this.commandHelp = var1;
      this.command = var2;
   }

   RegisteredCommand getRegisteredCommand() {
      return this.command;
   }

   public String getCommand() {
      return this.command.command;
   }

   public String getCommandPrefix() {
      return this.commandHelp.getCommandPrefix();
   }

   public String getParameterSyntax() {
      return this.getParameterSyntax((CommandIssuer)null);
   }

   public String getParameterSyntax(CommandIssuer issuer) {
      String var2 = this.command.getSyntaxText(var1);
      return var2 != null ? var2 : "";
   }

   public String getDescription() {
      return this.command.getHelpText();
   }

   public void setSearchScore(int searchScore) {
      this.searchScore = var1;
   }

   public boolean shouldShow() {
      return this.searchScore > 0;
   }

   public int getSearchScore() {
      return this.searchScore;
   }

   public String getSearchTags() {
      return this.command.helpSearchTags;
   }

   public CommandParameter[] getParameters() {
      return this.command.parameters;
   }
}
