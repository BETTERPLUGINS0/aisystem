package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.incendo.cloud.CommandTree;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierManagerHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierCommand;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.LiteralBrigadierNodeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.PluginHolder;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBackwardsBrigadierSenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBrigadierMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import java.util.regex.Pattern;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;

class LegacyPaperBrigadier<C> implements Listener, BrigadierManagerHolder<C, BukkitBrigadierCommandSource> {
   private final CloudBrigadierManager<C, BukkitBrigadierCommandSource> brigadierManager;
   private final LegacyPaperCommandManager<C> paperCommandManager;

   LegacyPaperBrigadier(@NonNull final LegacyPaperCommandManager<C> paperCommandManager) {
      this.paperCommandManager = paperCommandManager;
      this.brigadierManager = new CloudBrigadierManager(this.paperCommandManager, SenderMapper.create((sender) -> {
         return this.paperCommandManager.senderMapper().map(sender.getBukkitSender());
      }, new BukkitBackwardsBrigadierSenderMapper(this.paperCommandManager.senderMapper())));
      BukkitBrigadierMapper<C> mapper = new BukkitBrigadierMapper(this.paperCommandManager.owningPlugin().getLogger(), this.brigadierManager);
      mapper.registerBuiltInMappings();
      PaperBrigadierMappings.register(mapper);
   }

   public final boolean hasBrigadierManager() {
      return true;
   }

   @NonNull
   public final CloudBrigadierManager<C, BukkitBrigadierCommandSource> brigadierManager() {
      return this.brigadierManager;
   }

   @EventHandler
   public void onCommandRegister(@NonNull final CommandRegisteredEvent<BukkitBrigadierCommandSource> event) {
      if (event.getCommand() instanceof PluginIdentifiableCommand) {
         if (((PluginIdentifiableCommand)event.getCommand()).getPlugin().equals(this.paperCommandManager.owningPlugin())) {
            CommandTree<C> commandTree = this.paperCommandManager.commandTree();
            String label;
            if (event.getCommandLabel().contains(":")) {
               label = event.getCommandLabel().split(Pattern.quote(":"))[1];
            } else {
               label = event.getCommandLabel();
            }

            CommandNode<C> node = commandTree.getNamedNode(label);
            if (node != null) {
               BrigadierPermissionChecker<C> permissionChecker = (sender, permission) -> {
                  return commandTree.getNamedNode(label) == null ? false : this.paperCommandManager.testPermission(sender, permission).allowed();
               };
               LiteralBrigadierNodeFactory<C, BukkitBrigadierCommandSource> literalFactory = this.brigadierManager.literalBrigadierNodeFactory();
               event.setLiteral(literalFactory.createNode(event.getLiteral().getLiteral(), (CommandNode)node, new CloudBrigadierCommand(this.paperCommandManager, this.brigadierManager, (command) -> {
                  return BukkitHelper.stripNamespace((PluginHolder)this.paperCommandManager, command);
               }), permissionChecker));
            }
         }
      }
   }
}
