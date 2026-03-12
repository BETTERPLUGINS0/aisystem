package ac.grim.grimac.shaded.incendo.cloud.help;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.help.result.CommandEntry;
import ac.grim.grimac.shaded.incendo.cloud.help.result.HelpQueryResult;
import ac.grim.grimac.shaded.incendo.cloud.help.result.IndexCommandResult;
import ac.grim.grimac.shaded.incendo.cloud.help.result.MultipleCommandResult;
import ac.grim.grimac.shaded.incendo.cloud.help.result.VerboseCommandResult;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandInputTokenizer;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class StandardHelpHandler<C> implements HelpHandler<C> {
   private final CommandManager<C> commandManager;
   private final CommandPredicate<C> commandFilter;

   public StandardHelpHandler(@NonNull final CommandManager<C> commandManager, @NonNull final CommandPredicate<C> commandPredicate) {
      this.commandManager = commandManager;
      this.commandFilter = commandPredicate;
   }

   @NonNull
   public HelpQueryResult<C> query(@NonNull final HelpQuery<C> query) {
      List<CommandEntry<C>> commands = this.commands(query.sender());
      if (query.query().replace(" ", "").isEmpty()) {
         return IndexCommandResult.of(query, commands);
      } else {
         List<String> queryFragments = (new CommandInputTokenizer(query.query())).tokenize();
         String rootFragment = (String)queryFragments.get(0);
         List<Command<C>> availableCommands = new LinkedList();
         Set<String> availableCommandLabels = new HashSet();
         boolean exactMatch = false;
         Iterator var8 = commands.iterator();

         while(var8.hasNext()) {
            CommandEntry<C> entry = (CommandEntry)var8.next();
            Command<C> command = entry.command();
            CommandComponent<C> component = command.rootComponent();
            Iterator var12 = component.aliases().iterator();

            String alias;
            while(var12.hasNext()) {
               alias = (String)var12.next();
               if (alias.toLowerCase(Locale.ENGLISH).startsWith(rootFragment.toLowerCase(Locale.ENGLISH))) {
                  availableCommands.add(command);
                  availableCommandLabels.add(component.name());
                  break;
               }
            }

            var12 = component.aliases().iterator();

            while(var12.hasNext()) {
               alias = (String)var12.next();
               if (alias.equalsIgnoreCase(rootFragment)) {
                  exactMatch = true;
                  break;
               }
            }

            if (rootFragment.equalsIgnoreCase(component.name())) {
               availableCommandLabels.clear();
               availableCommands.clear();
               availableCommandLabels.add(component.name());
               availableCommands.add(command);
               break;
            }
         }

         if (availableCommands.isEmpty()) {
            return IndexCommandResult.of(query, Collections.emptyList());
         } else if (exactMatch && availableCommandLabels.size() <= 1) {
            CommandNode<C> node = this.commandManager.commandTree().getNamedNode((String)availableCommandLabels.iterator().next());
            List<CommandComponent<C>> traversedNodes = new LinkedList();
            CommandNode<C> head = node;
            int index = 0;

            label118:
            while(true) {
               if (head != null && this.isNodeVisible(head)) {
                  ++index;
                  traversedNodes.add(head.component());
                  if (head.component() != null && head.command() != null && (head.isLeaf() || index == queryFragments.size()) && this.isAllowed(query.sender(), head.command())) {
                     return VerboseCommandResult.of(query, CommandEntry.of(head.command(), this.commandManager.commandSyntaxFormatter().apply(query.sender(), head.command().components(), (CommandNode)null)));
                  }

                  if (head.children().size() == 1) {
                     head = (CommandNode)head.children().get(0);
                     continue;
                  }

                  if (index < queryFragments.size()) {
                     CommandNode<C> potentialVariable = null;
                     Iterator var23 = head.children().iterator();

                     while(true) {
                        while(var23.hasNext()) {
                           CommandNode<C> child = (CommandNode)var23.next();
                           if (child.component() != null && child.component().type() == CommandComponent.ComponentType.LITERAL) {
                              Iterator var15 = child.component().aliases().iterator();

                              while(var15.hasNext()) {
                                 String childAlias = (String)var15.next();
                                 if (childAlias.equalsIgnoreCase((String)queryFragments.get(index))) {
                                    head = child;
                                    continue label118;
                                 }
                              }
                           } else if (child.component() != null) {
                              potentialVariable = child;
                           }
                        }

                        if (potentialVariable != null) {
                           head = potentialVariable;
                           continue label118;
                        }
                        break;
                     }
                  }

                  String currentDescription = this.commandManager.commandSyntaxFormatter().apply(query.sender(), traversedNodes, (CommandNode)null);
                  List<String> childSuggestions = new LinkedList();
                  Iterator var25 = head.children().iterator();

                  while(true) {
                     CommandNode child;
                     LinkedList traversedNodesSub;
                     do {
                        do {
                           if (!var25.hasNext()) {
                              return MultipleCommandResult.of(query, currentDescription, childSuggestions);
                           }

                           child = (CommandNode)var25.next();
                        } while(!this.isNodeVisible(child));

                        traversedNodesSub = new LinkedList(traversedNodes);
                     } while(child.component() != null && child.command() != null && !this.isAllowed(query.sender(), child.command()));

                     traversedNodesSub.add(child.component());
                     childSuggestions.add(this.commandManager.commandSyntaxFormatter().apply(query.sender(), traversedNodesSub, child));
                  }
               }

               return IndexCommandResult.of(query, Collections.emptyList());
            }
         } else {
            return IndexCommandResult.of(query, (List)availableCommands.stream().map((commandx) -> {
               return CommandEntry.of(commandx, this.commandManager.commandSyntaxFormatter().apply(query.sender(), commandx.components(), (CommandNode)null));
            }).sorted().filter((entryx) -> {
               return this.isAllowed(query.sender(), entryx.command());
            }).collect(Collectors.toList()));
         }
      }
   }

   @NonNull
   protected List<CommandEntry<C>> commands(@NonNull final C sender) {
      return (List)this.commandManager.commands().stream().filter(this.commandFilter).filter((command) -> {
         return this.isAllowed(sender, command);
      }).map((command) -> {
         return CommandEntry.of(command, this.commandManager.commandSyntaxFormatter().apply(sender, command.components(), (CommandNode)null));
      }).sorted().collect(Collectors.toList());
   }

   private boolean isAllowed(final C sender, final Command<C> command) {
      return command.senderType().isPresent() && !GenericTypeReflector.isSuperType(((TypeToken)command.senderType().get()).getType(), sender.getClass()) ? false : this.commandManager.testPermission(sender, command.commandPermission()).allowed();
   }

   protected boolean isNodeVisible(@NonNull final CommandNode<C> node) {
      CommandComponent<C> component = node.component();
      if (component != null) {
         Command<C> owningCommand = node.command();
         if (owningCommand != null && this.commandFilter.test(owningCommand)) {
            return true;
         }
      }

      Iterator var5 = node.children().iterator();

      CommandNode childNode;
      do {
         if (!var5.hasNext()) {
            return false;
         }

         childNode = (CommandNode)var5.next();
      } while(!this.isNodeVisible(childNode));

      return true;
   }
}
