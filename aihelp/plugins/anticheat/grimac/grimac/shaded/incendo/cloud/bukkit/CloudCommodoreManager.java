package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBackwardsBrigadierSenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBrigadierMapper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

class CloudCommodoreManager<C> extends BukkitPluginRegistrationHandler<C> {
   private final BukkitCommandManager<C> commandManager;
   private final CloudBrigadierManager<C, Object> brigadierManager;
   private final Commodore commodore;

   CloudCommodoreManager(@NonNull final BukkitCommandManager<C> commandManager) {
      if (!CommodoreProvider.isSupported()) {
         throw new IllegalStateException("CommodoreProvider reports isSupported = false");
      } else {
         this.commandManager = commandManager;
         this.commodore = CommodoreProvider.getCommodore(commandManager.owningPlugin());
         this.brigadierManager = new CloudBrigadierManager(commandManager, SenderMapper.create((sender) -> {
            CommandSender bukkitSender = getBukkitSender(sender);
            return this.commandManager.senderMapper().map(bukkitSender);
         }, new BukkitBackwardsBrigadierSenderMapper(this.commandManager.senderMapper())));
         BukkitBrigadierMapper<C> mapper = new BukkitBrigadierMapper(this.commandManager.owningPlugin().getLogger(), this.brigadierManager);
         mapper.registerBuiltInMappings();
      }
   }

   protected void registerExternal(@NonNull final String label, @NonNull final Command<?> command, @NonNull final BukkitCommand<C> bukkitCommand) {
      this.registerWithCommodore(label, command);
   }

   protected void unregisterExternal(@NonNull final String label) {
      this.unregisterWithCommodore(label);
   }

   @NonNull
   protected CloudBrigadierManager<C, Object> brigadierManager() {
      return this.brigadierManager;
   }

   private void registerWithCommodore(@NonNull final String label, @NonNull final Command<C> command) {
      LiteralCommandNode<?> literalCommandNode = this.brigadierManager.literalBrigadierNodeFactory().createNode(label, command, (o) -> {
         return 1;
      }, (sender, commandPermission) -> {
         return this.commandManager.commandTree().getNamedNode(label) == null ? false : this.commandManager.testPermission(sender, commandPermission).allowed();
      });
      CommandNode existingNode = this.getDispatcher().findNode(Collections.singletonList(label));
      if (existingNode != null) {
         this.mergeChildren(existingNode, literalCommandNode);
      } else {
         this.commodore.register(literalCommandNode);
      }

   }

   private void unregisterWithCommodore(@NonNull final String label) {
      CommandDispatcher<?> dispatcher = this.getDispatcher();
      CommandNode node = dispatcher.findNode(Collections.singletonList(label));
      if (node != null) {
         try {
            Class commodoreImpl = this.commodore.getClass();

            Method removeChild;
            try {
               removeChild = commodoreImpl.getDeclaredMethod("removeChild", RootCommandNode.class, String.class);
            } catch (NoSuchMethodException var8) {
               removeChild = commodoreImpl.getSuperclass().getDeclaredMethod("removeChild", RootCommandNode.class, String.class);
            }

            removeChild.setAccessible(true);
            removeChild.invoke((Object)null, dispatcher.getRoot(), node.getName());
            Field registeredNodesField = commodoreImpl.getDeclaredField("registeredNodes");
            registeredNodesField.setAccessible(true);
            List<?> registeredNodes = (List)registeredNodesField.get(this.commodore);
            registeredNodes.remove(node);
         } catch (Exception var9) {
            throw new RuntimeException(String.format("Failed to unregister command '%s' with commodore", label), var9);
         }
      }
   }

   private void mergeChildren(final CommandNode<?> existingNode, final CommandNode<?> node) {
      Iterator var3 = node.getChildren().iterator();

      while(var3.hasNext()) {
         CommandNode child = (CommandNode)var3.next();
         CommandNode<?> existingChild = existingNode.getChild(child.getName());
         if (existingChild == null) {
            existingNode.addChild(child);
         } else {
            this.mergeChildren(existingChild, child);
         }
      }

   }

   private CommandDispatcher<?> getDispatcher() {
      try {
         Method getDispatcherMethod = this.commodore.getClass().getDeclaredMethod("getDispatcher");
         getDispatcherMethod.setAccessible(true);
         return (CommandDispatcher)getDispatcherMethod.invoke(this.commodore);
      } catch (ReflectiveOperationException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static CommandSender getBukkitSender(@NonNull final Object commandSourceStack) {
      Objects.requireNonNull(commandSourceStack, "commandSourceStack");

      try {
         Method getBukkitSenderMethod = commandSourceStack.getClass().getDeclaredMethod("getBukkitSender");
         getBukkitSenderMethod.setAccessible(true);
         return (CommandSender)getBukkitSenderMethod.invoke(commandSourceStack);
      } catch (ReflectiveOperationException var2) {
         throw new RuntimeException(var2);
      }
   }
}
