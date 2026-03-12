package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.util.StringUtils;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class BukkitCommand<C> extends Command implements PluginIdentifiableCommand {
   private final CommandComponent<C> command;
   private final BukkitCommandManager<C> manager;
   private final ac.grim.grimac.shaded.incendo.cloud.Command<C> cloudCommand;
   private boolean disabled;

   BukkitCommand(@NonNull final String label, @NonNull final List<String> aliases, @NonNull final ac.grim.grimac.shaded.incendo.cloud.Command<C> cloudCommand, @NonNull final CommandComponent<C> command, @NonNull final BukkitCommandManager<C> manager) {
      super(label, BukkitHelper.description(cloudCommand), "", aliases);
      this.command = command;
      this.manager = manager;
      this.cloudCommand = cloudCommand;
      this.disabled = false;
   }

   @NonNull
   public List<String> tabComplete(@NonNull final CommandSender sender, @NonNull final String alias, @NonNull final String[] args) throws IllegalArgumentException {
      StringBuilder builder = new StringBuilder(this.command.name());
      String[] var5 = args;
      int var6 = args.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String string = var5[var7];
         builder.append(" ").append(string);
      }

      Suggestions<C, ?> result = this.manager.suggestionFactory().suggestImmediately(this.manager.senderMapper().map(sender), builder.toString());
      return (List)result.list().stream().map(Suggestion::suggestion).map((suggestion) -> {
         return StringUtils.trimBeforeLastSpace(suggestion, result.commandInput());
      }).filter(Objects::nonNull).collect(Collectors.toList());
   }

   public boolean execute(@NonNull final CommandSender commandSender, @NonNull final String commandLabel, @NonNull final String[] strings) {
      StringBuilder builder = new StringBuilder(this.command.name());
      String[] var5 = strings;
      int var6 = strings.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String string = var5[var7];
         builder.append(" ").append(string);
      }

      C sender = this.manager.senderMapper().map(commandSender);
      this.manager.commandExecutor().executeCommand(sender, builder.toString());
      return true;
   }

   @NonNull
   public String getDescription() {
      return BukkitHelper.description(this.cloudCommand);
   }

   @NonNull
   public Plugin getPlugin() {
      return this.manager.owningPlugin();
   }

   @NonNull
   public String getUsage() {
      CommandNode<C> node = this.namedNode();
      if (node == null) {
         this.getPlugin().getLogger().log(Level.WARNING, "Node does not exist in tree for command " + this.getLabel() + ".");
         return "";
      } else {
         return this.manager.commandSyntaxFormatter().apply((Object)null, Collections.singletonList((CommandComponent)Objects.requireNonNull(node.component())), node);
      }
   }

   public boolean testPermissionSilent(@NonNull final CommandSender target) {
      CommandNode<C> node = this.namedNode();
      if (!this.disabled && node != null) {
         Map<Type, Permission> accessMap = (Map)node.nodeMeta().getOrDefault(CommandNode.META_KEY_ACCESS, Collections.emptyMap());
         C cloudSender = this.manager.senderMapper().map(target);
         Iterator var5 = accessMap.entrySet().iterator();

         Entry entry;
         do {
            if (!var5.hasNext()) {
               return false;
            }

            entry = (Entry)var5.next();
         } while(!GenericTypeReflector.isSuperType((Type)entry.getKey(), cloudSender.getClass()) || !this.manager.testPermission(cloudSender, (Permission)entry.getValue()).allowed());

         return true;
      } else {
         return false;
      }
   }

   @API(
      status = Status.INTERNAL,
      since = "1.7.0"
   )
   void disable() {
      this.disabled = true;
   }

   public boolean isRegistered() {
      return !this.disabled;
   }

   @Nullable
   private CommandNode<C> namedNode() {
      return this.manager.commandTree().getNamedNode(this.command.name());
   }
}
