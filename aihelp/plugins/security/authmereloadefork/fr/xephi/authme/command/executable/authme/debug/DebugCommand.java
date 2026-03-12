package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.ch.jalu.injector.factory.Factory;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements ExecutableCommand {
   private static final Set<Class<? extends DebugSection>> SECTION_CLASSES = ImmutableSet.of(PermissionGroups.class, DataStatistics.class, CountryLookup.class, PlayerAuthViewer.class, InputValidator.class, LimboPlayerViewer.class, CountryLookup.class, HasPermissionChecker.class, TestEmailSender.class, SpawnLocationViewer.class, MySqlDefaultChanger.class);
   @Inject
   private Factory<DebugSection> debugSectionFactory;
   @Inject
   private PermissionsManager permissionsManager;
   private Map<String, DebugSection> sections;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      DebugSection debugSection = this.findDebugSection(arguments);
      if (debugSection == null) {
         this.sendAvailableSections(sender);
      } else {
         this.executeSection(debugSection, sender, arguments);
      }

   }

   private DebugSection findDebugSection(List<String> arguments) {
      return arguments.isEmpty() ? null : (DebugSection)this.getSections().get(((String)arguments.get(0)).toLowerCase(Locale.ROOT));
   }

   private void sendAvailableSections(CommandSender sender) {
      sender.sendMessage(ChatColor.BLUE + "AuthMe debug utils");
      sender.sendMessage("Sections available to you:");
      long availableSections = this.getSections().values().stream().filter((section) -> {
         return this.permissionsManager.hasPermission(sender, section.getRequiredPermission());
      }).peek((e) -> {
         sender.sendMessage("- " + e.getName() + ": " + e.getDescription());
      }).count();
      if (availableSections == 0L) {
         sender.sendMessage(ChatColor.RED + "You don't have permission to view any debug section");
      }

   }

   private void executeSection(DebugSection section, CommandSender sender, List<String> arguments) {
      if (this.permissionsManager.hasPermission(sender, section.getRequiredPermission())) {
         section.execute(sender, arguments.subList(1, arguments.size()));
      } else {
         sender.sendMessage(ChatColor.RED + "You don't have permission for this section. See /authme debug");
      }

   }

   private Map<String, DebugSection> getSections() {
      if (this.sections == null) {
         Map<String, DebugSection> sections = new TreeMap();
         Iterator var2 = SECTION_CLASSES.iterator();

         while(var2.hasNext()) {
            Class<? extends DebugSection> sectionClass = (Class)var2.next();
            DebugSection section = (DebugSection)this.debugSectionFactory.newInstance(sectionClass);
            sections.put(section.getName(), section);
         }

         this.sections = sections;
      }

      return this.sections;
   }
}
