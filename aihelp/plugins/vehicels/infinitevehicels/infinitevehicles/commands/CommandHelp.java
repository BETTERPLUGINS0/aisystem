package me.PM2.infinitevehicles.commands;

import com.google.common.collect.SetMultimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;

public class CommandHelp {
   private final CommandManager manager;
   private final CommandIssuer issuer;
   private final List<HelpEntry> helpEntries = new ArrayList();
   private final String commandName;
   final String commandPrefix;
   private int page = 1;
   private int perPage;
   List<String> search;
   private Set<HelpEntry> selectedEntry = new HashSet();
   private int totalResults;
   private int totalPages;
   private boolean lastPage;

   public CommandHelp(CommandManager manager, RootCommand rootCommand, CommandIssuer issuer) {
      this.manager = var1;
      this.issuer = var3;
      this.perPage = var1.defaultHelpPerPage;
      this.commandPrefix = var1.getCommandPrefix(var3);
      this.commandName = var2.getCommandName();
      SetMultimap var4 = var2.getSubCommands();
      HashSet var5 = new HashSet();
      if (!var2.getDefCommand().hasHelpCommand) {
         RegisteredCommand var6 = var2.getDefaultRegisteredCommand();
         if (var6 != null) {
            this.helpEntries.add(new HelpEntry(this, var6));
            var5.add(var6);
         }
      }

      var4.entries().forEach((var3x) -> {
         String var4 = (String)var3x.getKey();
         if (!var4.equals("__default") && !var4.equals("__catchunknown")) {
            RegisteredCommand var5x = (RegisteredCommand)var3x.getValue();
            if (!var5x.isPrivate && var5x.hasPermission(var3) && !var5.contains(var5x)) {
               this.helpEntries.add(new HelpEntry(this, var5x));
               var5.add(var5x);
            }

         }
      });
   }

   @UnstableAPI
   protected void updateSearchScore(HelpEntry help) {
      if (this.search != null && !this.search.isEmpty()) {
         RegisteredCommand var2 = var1.getRegisteredCommand();
         int var3 = 0;
         Iterator var4 = this.search.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            Pattern var6 = Pattern.compile(".*" + Pattern.quote(var5) + ".*", 2);
            Iterator var7 = var2.registeredSubcommands.iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               Pattern var9 = Pattern.compile(".*" + Pattern.quote(var8) + ".*", 2);
               if (var6.matcher(var8).matches()) {
                  var3 += 3;
               } else if (var9.matcher(var5).matches()) {
                  ++var3;
               }
            }

            if (var6.matcher(var1.getDescription()).matches()) {
               var3 += 2;
            }

            if (var6.matcher(var1.getParameterSyntax(this.issuer)).matches()) {
               ++var3;
            }

            if (var1.getSearchTags() != null && var6.matcher(var1.getSearchTags()).matches()) {
               var3 += 2;
            }
         }

         var1.setSearchScore(var3);
      } else {
         var1.setSearchScore(1);
      }
   }

   public CommandManager getManager() {
      return this.manager;
   }

   public boolean testExactMatch(String command) {
      this.selectedEntry.clear();
      Iterator var2 = this.helpEntries.iterator();

      while(var2.hasNext()) {
         HelpEntry var3 = (HelpEntry)var2.next();
         if (var3.getCommand().endsWith(" " + var1)) {
            this.selectedEntry.add(var3);
         }
      }

      return !this.selectedEntry.isEmpty();
   }

   public void showHelp() {
      this.showHelp(this.issuer);
   }

   public void showHelp(CommandIssuer issuer) {
      CommandHelpFormatter var2 = this.manager.getHelpFormatter();
      Iterator var4;
      if (!this.selectedEntry.isEmpty()) {
         HelpEntry var10 = (HelpEntry)ACFUtil.getFirstElement(this.selectedEntry);
         var2.printDetailedHelpHeader(this, var1, var10);
         var4 = this.selectedEntry.iterator();

         while(var4.hasNext()) {
            HelpEntry var11 = (HelpEntry)var4.next();
            var2.showDetailedHelp(this, var11);
         }

         var2.printDetailedHelpFooter(this, var1, var10);
      } else {
         List var3 = (List)this.getHelpEntries().stream().filter(HelpEntry::shouldShow).collect(Collectors.toList());
         var4 = var3.stream().sorted(Comparator.comparingInt((var0) -> {
            return var0.getSearchScore() * -1;
         })).iterator();
         if (!var4.hasNext()) {
            var1.sendMessage(MessageType.ERROR, (MessageKeyProvider)MessageKeys.NO_COMMAND_MATCHED_SEARCH, "{search}", ACFUtil.join((Collection)this.search, " "));
            var3 = this.getHelpEntries();
            var4 = var3.iterator();
         }

         this.totalResults = var3.size();
         int var5 = (this.page - 1) * this.perPage;
         int var6 = var5 + this.perPage;
         this.totalPages = (int)Math.ceil((double)((float)this.totalResults / (float)this.perPage));
         int var7 = 0;
         if (var5 >= this.totalResults) {
            var1.sendMessage(MessageType.HELP, (MessageKeyProvider)MessageKeys.HELP_NO_RESULTS);
         } else {
            ArrayList var8 = new ArrayList();

            while(var4.hasNext()) {
               HelpEntry var9 = (HelpEntry)var4.next();
               if (var7 >= var6) {
                  break;
               }

               if (var7++ >= var5) {
                  var8.add(var9);
               }
            }

            this.lastPage = var6 >= this.totalResults;
            if (this.search == null) {
               var2.showAllResults(this, var8);
            } else {
               var2.showSearchResults(this, var8);
            }

         }
      }
   }

   public List<HelpEntry> getHelpEntries() {
      return this.helpEntries;
   }

   public void setPerPage(int perPage) {
      this.perPage = var1;
   }

   public void setPage(int page) {
      this.page = var1;
   }

   public void setPage(int page, int perPage) {
      this.setPage(var1);
      this.setPerPage(var2);
   }

   public void setSearch(List<String> search) {
      this.search = var1;
      this.getHelpEntries().forEach(this::updateSearchScore);
   }

   public CommandIssuer getIssuer() {
      return this.issuer;
   }

   public String getCommandName() {
      return this.commandName;
   }

   public String getCommandPrefix() {
      return this.commandPrefix;
   }

   public int getPage() {
      return this.page;
   }

   public int getPerPage() {
      return this.perPage;
   }

   public List<String> getSearch() {
      return this.search;
   }

   public Set<HelpEntry> getSelectedEntry() {
      return this.selectedEntry;
   }

   public int getTotalResults() {
      return this.totalResults;
   }

   public int getTotalPages() {
      return this.totalPages;
   }

   public boolean isOnlyPage() {
      return this.page == 1 && this.lastPage;
   }

   public boolean isLastPage() {
      return this.lastPage;
   }
}
