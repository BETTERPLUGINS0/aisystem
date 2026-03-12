package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandRegistrationHandler;
import ac.grim.grimac.shaded.incendo.cloud.setting.ManagerSetting;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public class BukkitPluginRegistrationHandler<C> implements CommandRegistrationHandler<C> {
   private final Map<CommandComponent<C>, BukkitPluginRegistrationHandler.RegisteredCommandData<C>> registeredCommands = new HashMap();
   private final Set<String> recognizedAliases;
   private Map<String, Command> bukkitCommands;
   private BukkitCommandManager<C> bukkitCommandManager;
   private CommandMap commandMap;

   protected BukkitPluginRegistrationHandler() {
      this.recognizedAliases = new TreeSet(String.CASE_INSENSITIVE_ORDER);
   }

   final void initialize(@NonNull final BukkitCommandManager<C> bukkitCommandManager) throws ReflectiveOperationException {
      Method getCommandMap = Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap");
      getCommandMap.setAccessible(true);
      this.commandMap = (CommandMap)getCommandMap.invoke(Bukkit.getServer());
      Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommands.setAccessible(true);
      Map<String, Command> bukkitCommands = (Map)knownCommands.get(this.commandMap);
      this.bukkitCommands = bukkitCommands;
      this.bukkitCommandManager = bukkitCommandManager;
   }

   public final boolean registerCommand(@NonNull final ac.grim.grimac.shaded.incendo.cloud.Command<C> command) {
      CommandComponent<C> component = command.rootComponent();
      if (!(this.bukkitCommandManager.commandRegistrationHandler() instanceof CloudCommodoreManager) && this.registeredCommands.containsKey(component)) {
         return false;
      } else {
         String label = component.name();
         String namespacedLabel = BukkitHelper.namespacedLabel((PluginHolder)this.bukkitCommandManager, label);
         List<String> aliases = new ArrayList(component.alternativeAliases());
         BukkitCommand<C> bukkitCommand = new BukkitCommand(label, aliases, command, component, this.bukkitCommandManager);
         if (this.bukkitCommandManager.settings().get(ManagerSetting.OVERRIDE_EXISTING_COMMANDS)) {
            this.bukkitCommands.remove(label);
            Map var10001 = this.bukkitCommands;
            Objects.requireNonNull(var10001);
            aliases.forEach(var10001::remove);
         }

         Set<String> newAliases = new HashSet();
         Iterator var8 = aliases.iterator();

         while(var8.hasNext()) {
            String alias = (String)var8.next();
            String namespacedAlias = BukkitHelper.namespacedLabel((PluginHolder)this.bukkitCommandManager, alias);
            newAliases.add(namespacedAlias);
            if (!this.bukkitCommandOrAliasExists(alias)) {
               newAliases.add(alias);
            }
         }

         if (!this.bukkitCommandExists(label)) {
            newAliases.add(label);
         }

         newAliases.add(namespacedLabel);
         this.commandMap.register(label, this.bukkitCommandManager.owningPlugin().getName().toLowerCase(Locale.ROOT), bukkitCommand);
         this.recognizedAliases.addAll(newAliases);
         if (this.bukkitCommandManager.splitAliases()) {
            newAliases.forEach((aliasx) -> {
               this.registerExternal(aliasx, command, bukkitCommand);
            });
         }

         this.registeredCommands.put(component, new BukkitPluginRegistrationHandler.RegisteredCommandData(bukkitCommand, newAliases));
         return true;
      }
   }

   public final void unregisterRootCommand(@NonNull final CommandComponent<C> component) {
      BukkitPluginRegistrationHandler.RegisteredCommandData<C> registeredCommand = (BukkitPluginRegistrationHandler.RegisteredCommandData)this.registeredCommands.get(component);
      if (registeredCommand != null) {
         registeredCommand.bukkit.disable();
         Set<String> registeredAliases = registeredCommand.recognizedAliases;
         Iterator var4 = registeredAliases.iterator();

         while(var4.hasNext()) {
            String alias = (String)var4.next();
            this.bukkitCommands.remove(alias);
         }

         this.recognizedAliases.removeAll(registeredAliases);
         if (this.bukkitCommandManager.splitAliases()) {
            registeredAliases.forEach(this::unregisterExternal);
         }

         this.registeredCommands.remove(component);
         if (this.bukkitCommandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
         }

      }
   }

   public boolean isRecognized(@NonNull final String alias) {
      return this.recognizedAliases.contains(alias);
   }

   protected void registerExternal(@NonNull final String label, @NonNull final ac.grim.grimac.shaded.incendo.cloud.Command<?> command, @NonNull final BukkitCommand<C> bukkitCommand) {
   }

   @API(
      status = Status.STABLE,
      since = "1.7.0"
   )
   protected void unregisterExternal(@NonNull final String label) {
   }

   private boolean bukkitCommandExists(final String commandLabel) {
      Command existingCommand = (Command)this.bukkitCommands.get(commandLabel);
      if (existingCommand == null) {
         return false;
      } else if (!(existingCommand instanceof PluginIdentifiableCommand)) {
         return existingCommand.getLabel().equals(commandLabel);
      } else {
         return existingCommand.getLabel().equals(commandLabel) && !((PluginIdentifiableCommand)existingCommand).getPlugin().getName().equalsIgnoreCase(this.bukkitCommandManager.owningPlugin().getName());
      }
   }

   private boolean bukkitCommandOrAliasExists(final String commandLabel) {
      Command command = (Command)this.bukkitCommands.get(commandLabel);
      if (command instanceof PluginIdentifiableCommand) {
         return !((PluginIdentifiableCommand)command).getPlugin().getName().equalsIgnoreCase(this.bukkitCommandManager.owningPlugin().getName());
      } else {
         return command != null;
      }
   }

   private static final class RegisteredCommandData<C> {
      private final BukkitCommand<C> bukkit;
      private final Set<String> recognizedAliases;

      private RegisteredCommandData(final BukkitCommand<C> bukkit, final Set<String> recognizedAliases) {
         this.bukkit = bukkit;
         Set<String> treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
         treeSet.addAll(recognizedAliases);
         this.recognizedAliases = Collections.unmodifiableSet(treeSet);
      }

      // $FF: synthetic method
      RegisteredCommandData(BukkitCommand x0, Set x1, Object x2) {
         this(x0, x1);
      }
   }
}
