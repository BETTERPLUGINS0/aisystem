package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierManagerHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierCommand;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.PluginHolder;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBackwardsBrigadierSenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBrigadierMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandRegistrationHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.papermc.paper.command.brigadier.CommandRegistrationFlag;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ModernPaperBrigadier<C, B> implements CommandRegistrationHandler<C>, BrigadierManagerHolder<C, CommandSourceStack> {
   private final CommandManager<C> manager;
   private final Runnable lockRegistration;
   private final PluginMetaHolder metaHolder;
   private final CloudBrigadierManager<C, CommandSourceStack> brigadierManager;
   private final Map<String, Set<String>> aliases = new ConcurrentHashMap();
   private final Set<Command<C>> registeredCommands = new HashSet();
   @Nullable
   private volatile Commands commands;
   @MonotonicNonNull
   private static Method commandnodeRemoveMethod = null;
   @MonotonicNonNull
   private static Field commandsInvalidField = null;

   ModernPaperBrigadier(final Class<B> baseType, final CommandManager<C> manager, final SenderMapper<B, C> senderMapper, final Runnable lockRegistration) {
      this.manager = manager;
      this.lockRegistration = lockRegistration;
      if (manager instanceof PluginMetaHolder) {
         this.metaHolder = (PluginMetaHolder)manager;
      } else {
         if (!(manager instanceof PluginHolder)) {
            throw new IllegalArgumentException(manager.toString());
         }

         this.metaHolder = PluginMetaHolder.fromPluginHolder((PluginHolder)manager);
      }

      this.brigadierManager = new CloudBrigadierManager(this.manager, SenderMapper.create((source) -> {
         return baseType.equals(CommandSender.class) ? senderMapper.map(source.getSender()) : senderMapper.map(source);
      }, (sender) -> {
         return baseType.equals(CommandSender.class) ? (CommandSourceStack)(new BukkitBackwardsBrigadierSenderMapper(senderMapper)).apply(sender) : (CommandSourceStack)senderMapper.reverse(sender);
      }));
      BukkitBrigadierMapper<C> mapper = new BukkitBrigadierMapper(Logger.getLogger(this.metaHolder.owningPluginMeta().getName()), this.brigadierManager);
      mapper.registerBuiltInMappings();
      PaperBrigadierMappings.register(mapper);
   }

   void registerPlugin(final Plugin plugin) {
      plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this::register);
   }

   void registerBootstrap(final BootstrapContext context) {
      context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this::register);
   }

   private void register(final ReloadableRegistrarEvent<Commands> event) {
      this.lockRegistration.run();
      Commands commands = (Commands)event.registrar();
      this.commands = commands;
      this.aliases.clear();
      Iterator var3 = this.manager.commandTree().rootNodes().iterator();

      while(var3.hasNext()) {
         CommandNode<C> rootNode = (CommandNode)var3.next();
         this.registerCommand(commands, rootNode);
      }

   }

   private void registerCommand(final Commands commands, final CommandNode<C> rootNode) {
      Set<String> registered = commands.registerWithFlags(this.metaHolder.owningPluginMeta(), this.createRootNode(rootNode, rootNode.component().name()), this.findBukkitDescription(rootNode), new ArrayList(rootNode.component().alternativeAliases()), new HashSet(Collections.singletonList(CommandRegistrationFlag.FLATTEN_ALIASES)));
      this.aliases.put(rootNode.component().name(), registered);
   }

   private LiteralCommandNode<CommandSourceStack> createRootNode(final CommandNode<C> rootNode, final String label) {
      BrigadierPermissionChecker<C> permissionChecker = (sender, permission) -> {
         return this.manager.commandTree().getNamedNode(rootNode.component().name()) == null ? false : this.manager.testPermission(sender, permission).allowed();
      };
      return this.brigadierManager.literalBrigadierNodeFactory().createNode(label, (CommandNode)rootNode, new CloudBrigadierCommand(this.manager, this.brigadierManager, (command) -> {
         return BukkitHelper.stripNamespace(this.metaHolder.owningPluginMeta().getName(), command);
      }), permissionChecker);
   }

   private String findBukkitDescription(final CommandNode<C> node) {
      if (node.command() != null) {
         return BukkitHelper.description(node.command());
      } else {
         Iterator var2 = node.children().iterator();

         String result;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            CommandNode<C> child = (CommandNode)var2.next();
            result = this.findBukkitDescription(child);
         } while(result == null);

         return result;
      }
   }

   public boolean hasBrigadierManager() {
      return true;
   }

   @NonNull
   public CloudBrigadierManager<C, CommandSourceStack> brigadierManager() {
      return this.brigadierManager;
   }

   public boolean registerCommand(final Command<C> command) {
      if (!this.registeredCommands.add(command)) {
         return true;
      } else {
         Commands commands = this.commands;
         if (commands == null) {
            return true;
         } else {
            if (this.aliases.containsKey(command.rootComponent().name())) {
               CommandDispatcher<CommandSourceStack> dispatcher = (CommandDispatcher)unsafeGet(commands, Commands::getDispatcher);
               Set<String> registered = (Set)this.aliases.get(command.rootComponent().name());
               LiteralCommandNode<CommandSourceStack> newRoot = this.createRootNode(this.manager.commandTree().getNamedNode(command.rootComponent().name()), command.rootComponent().name());
               Iterator var6 = registered.iterator();

               while(var6.hasNext()) {
                  String label = (String)var6.next();
                  com.mojang.brigadier.tree.CommandNode<CommandSourceStack> node = dispatcher.getRoot().getChild(label);
                  Iterator var9 = newRoot.getChildren().iterator();

                  while(var9.hasNext()) {
                     com.mojang.brigadier.tree.CommandNode<CommandSourceStack> newChild = (com.mojang.brigadier.tree.CommandNode)var9.next();
                     node.addChild(newChild);
                  }
               }
            } else {
               unsafeOperation(commands, (cmds) -> {
                  this.registerCommand(cmds, this.manager.commandTree().getNamedNode(command.rootComponent().name()));
               });
            }

            this.resendCommands();
            Set<String> registered = (Set)this.aliases.get(command.rootComponent().name());
            boolean ret = registered != null && !registered.isEmpty();
            if (!ret) {
               this.registeredCommands.remove(command);
            }

            return ret;
         }
      }
   }

   private void unregisterRoot(final Commands commands, final String label) {
      Set<String> removed = (Set)this.aliases.remove(label);
      if (removed != null && !removed.isEmpty()) {
         this.registeredCommands.removeIf((command) -> {
            return command.rootComponent().name().equals(label);
         });

         try {
            if (commandnodeRemoveMethod == null) {
               commandnodeRemoveMethod = com.mojang.brigadier.tree.CommandNode.class.getMethod("removeCommand", String.class);
               commandnodeRemoveMethod.setAccessible(true);
            }
         } catch (ReflectiveOperationException var5) {
            throw new RuntimeException("Failed to find removeCommand method", var5);
         }

         unsafeOperation(commands, (cmds) -> {
            CommandDispatcher<CommandSourceStack> dispatcher = cmds.getDispatcher();
            RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();
            Iterator var4 = removed.iterator();

            while(var4.hasNext()) {
               String removedLabel = (String)var4.next();

               try {
                  commandnodeRemoveMethod.invoke(root, removedLabel);
               } catch (ReflectiveOperationException var7) {
                  throw new RuntimeException("Failed to delete node " + removedLabel, var7);
               }
            }

         });
      }
   }

   public void unregisterRootCommand(final CommandComponent<C> rootCommand) {
      Commands commands = this.commands;
      if (commands != null) {
         this.unregisterRoot(commands, rootCommand.name());
         this.resendCommands();
      }
   }

   private void resendCommands() {
      Iterator var1 = this.metaHolder.owningPlugin().getServer().getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         player.updateCommands();
      }

   }

   private static void unsafeOperation(final Commands commands, final Consumer<Commands> task) {
      unsafeGet(commands, (cmds) -> {
         task.accept(cmds);
         return null;
      });
   }

   private static <T> T unsafeGet(final Commands commands, final Function<Commands, T> task) {
      try {
         if (commandsInvalidField == null) {
            commandsInvalidField = commands.getClass().getDeclaredField("invalid");
            commandsInvalidField.setAccessible(true);
         }

         boolean prev = commandsInvalidField.getBoolean(commands);

         Object var3;
         try {
            commandsInvalidField.setBoolean(commands, false);
            var3 = task.apply(commands);
         } finally {
            commandsInvalidField.setBoolean(commands, prev);
         }

         return var3;
      } catch (ReflectiveOperationException var8) {
         throw new RuntimeException("Failed to perform unsafe command operation", var8);
      }
   }
}
