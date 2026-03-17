package me.PM2.infinitevehicles.commands;

import java.util.Iterator;
import java.util.List;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;
import org.jetbrains.annotations.NotNull;

public class CommandHelpFormatter {
   private final CommandManager manager;

   public CommandHelpFormatter(CommandManager manager) {
      this.manager = var1;
   }

   public void showAllResults(CommandHelp commandHelp, List<HelpEntry> entries) {
      CommandIssuer var3 = var1.getIssuer();
      this.printHelpHeader(var1, var3);
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         HelpEntry var5 = (HelpEntry)var4.next();
         this.printHelpCommand(var1, var3, var5);
      }

      this.printHelpFooter(var1, var3);
   }

   public void showSearchResults(CommandHelp commandHelp, List<HelpEntry> entries) {
      CommandIssuer var3 = var1.getIssuer();
      this.printSearchHeader(var1, var3);
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         HelpEntry var5 = (HelpEntry)var4.next();
         this.printSearchEntry(var1, var3, var5);
      }

      this.printSearchFooter(var1, var3);
   }

   public void showDetailedHelp(CommandHelp commandHelp, HelpEntry entry) {
      CommandIssuer var3 = var1.getIssuer();
      this.printDetailedHelpCommand(var1, var3, var2);
      CommandParameter[] var4 = var2.getParameters();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CommandParameter var7 = var4[var6];
         String var8 = var7.getDescription();
         if (var8 != null && !var8.isEmpty()) {
            this.printDetailedParameter(var1, var3, var2, var7);
         }
      }

   }

   public void printHelpHeader(CommandHelp help, CommandIssuer issuer) {
      var2.sendMessage(MessageType.HELP, (MessageKeyProvider)MessageKeys.HELP_HEADER, this.getHeaderFooterFormatReplacements(var1));
   }

   public void printHelpCommand(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
      String var4 = this.manager.formatMessage(var2, MessageType.HELP, MessageKeys.HELP_FORMAT, this.getEntryFormatReplacements(var1, var3));
      String[] var5 = ACFPatterns.NEWLINE.split(var4);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var2.sendMessageInternal(ACFUtil.rtrim(var8));
      }

   }

   public void printHelpFooter(CommandHelp help, CommandIssuer issuer) {
      if (!var1.isOnlyPage()) {
         var2.sendMessage(MessageType.HELP, (MessageKeyProvider)MessageKeys.HELP_PAGE_INFORMATION, this.getHeaderFooterFormatReplacements(var1));
      }
   }

   public void printSearchHeader(CommandHelp help, CommandIssuer issuer) {
      var2.sendMessage(MessageType.HELP, (MessageKeyProvider)MessageKeys.HELP_SEARCH_HEADER, this.getHeaderFooterFormatReplacements(var1));
   }

   public void printSearchEntry(CommandHelp help, CommandIssuer issuer, HelpEntry page) {
      String var4 = this.manager.formatMessage(var2, MessageType.HELP, MessageKeys.HELP_FORMAT, this.getEntryFormatReplacements(var1, var3));
      String[] var5 = ACFPatterns.NEWLINE.split(var4);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var2.sendMessageInternal(ACFUtil.rtrim(var8));
      }

   }

   public void printSearchFooter(CommandHelp help, CommandIssuer issuer) {
      if (!var1.isOnlyPage()) {
         var2.sendMessage(MessageType.HELP, (MessageKeyProvider)MessageKeys.HELP_PAGE_INFORMATION, this.getHeaderFooterFormatReplacements(var1));
      }
   }

   public void printDetailedHelpHeader(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
      var2.sendMessage(MessageType.HELP, (MessageKeyProvider)MessageKeys.HELP_DETAILED_HEADER, "{command}", var3.getCommand(), "{commandprefix}", var1.getCommandPrefix());
   }

   public void printDetailedHelpCommand(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
      String var4 = this.manager.formatMessage(var2, MessageType.HELP, MessageKeys.HELP_DETAILED_COMMAND_FORMAT, this.getEntryFormatReplacements(var1, var3));
      String[] var5 = ACFPatterns.NEWLINE.split(var4);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var2.sendMessageInternal(ACFUtil.rtrim(var8));
      }

   }

   public void printDetailedParameter(CommandHelp help, CommandIssuer issuer, HelpEntry entry, CommandParameter param) {
      String var5 = this.manager.formatMessage(var2, MessageType.HELP, MessageKeys.HELP_DETAILED_PARAMETER_FORMAT, this.getParameterFormatReplacements(var1, var4, var3));
      String[] var6 = ACFPatterns.NEWLINE.split(var5);
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         var2.sendMessageInternal(ACFUtil.rtrim(var9));
      }

   }

   public void printDetailedHelpFooter(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
   }

   public String[] getHeaderFooterFormatReplacements(CommandHelp help) {
      return new String[]{"{search}", var1.search != null ? String.join(" ", var1.search) : "", "{command}", var1.getCommandName(), "{commandprefix}", var1.getCommandPrefix(), "{rootcommand}", var1.getCommandName(), "{page}", "" + var1.getPage(), "{totalpages}", "" + var1.getTotalPages(), "{results}", "" + var1.getTotalResults()};
   }

   public String[] getEntryFormatReplacements(CommandHelp help, HelpEntry entry) {
      return new String[]{"{command}", var2.getCommand(), "{commandprefix}", var1.getCommandPrefix(), "{parameters}", var2.getParameterSyntax(var1.getIssuer()), "{separator}", var2.getDescription().isEmpty() ? "" : "-", "{description}", var2.getDescription()};
   }

   @NotNull
   public String[] getParameterFormatReplacements(CommandHelp help, CommandParameter param, HelpEntry entry) {
      return new String[]{"{name}", var2.getDisplayName(var1.getIssuer()), "{syntaxorname}", (String)ACFUtil.nullDefault(var2.getSyntax(var1.getIssuer()), var2.getDisplayName(var1.getIssuer())), "{syntax}", (String)ACFUtil.nullDefault(var2.getSyntax(var1.getIssuer()), ""), "{description}", (String)ACFUtil.nullDefault(var2.getDescription(), ""), "{command}", var1.getCommandName(), "{fullcommand}", var3.getCommand(), "{commandprefix}", var1.getCommandPrefix()};
   }
}
