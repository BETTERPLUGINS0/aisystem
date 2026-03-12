package ac.grim.grimac.shaded.incendo.cloud;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.component.DefaultValue;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.context.ParsingContext;
import ac.grim.grimac.shaded.incendo.cloud.exception.AmbiguousNodeException;
import ac.grim.grimac.shaded.incendo.cloud.exception.ArgumentParseException;
import ac.grim.grimac.shaded.incendo.cloud.exception.InvalidCommandSenderException;
import ac.grim.grimac.shaded.incendo.cloud.exception.InvalidSyntaxException;
import ac.grim.grimac.shaded.incendo.cloud.exception.NoCommandInLeafException;
import ac.grim.grimac.shaded.incendo.cloud.exception.NoPermissionException;
import ac.grim.grimac.shaded.incendo.cloud.exception.NoSuchCommandException;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.internal.SuggestionContext;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.aggregate.AggregateParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlagParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.LiteralParser;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import ac.grim.grimac.shaded.incendo.cloud.permission.PermissionResult;
import ac.grim.grimac.shaded.incendo.cloud.setting.ManagerSetting;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionMapper;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.util.CompletableFutures;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class CommandTree<C> {
   private final Object commandLock = new Object();
   private final CommandNode<C> internalTree = new CommandNode((CommandComponent)null);
   private final CommandManager<C> commandManager;

   private CommandTree(@NonNull final CommandManager<C> commandManager) {
      this.commandManager = commandManager;
   }

   @NonNull
   public static <C> CommandTree<C> newTree(@NonNull final CommandManager<C> commandManager) {
      return new CommandTree(commandManager);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CommandManager<C> commandManager() {
      return this.commandManager;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Collection<CommandNode<C>> rootNodes() {
      return this.internalTree.children();
   }

   @Nullable
   public CommandNode<C> getNamedNode(@Nullable final String name) {
      Iterator var2 = this.rootNodes().iterator();

      while(true) {
         CommandNode node;
         CommandComponent component;
         do {
            do {
               if (!var2.hasNext()) {
                  return null;
               }

               node = (CommandNode)var2.next();
               component = node.component();
            } while(component == null);
         } while(component.type() != CommandComponent.ComponentType.LITERAL);

         Iterator var5 = component.aliases().iterator();

         while(var5.hasNext()) {
            String alias = (String)var5.next();
            if (alias.equalsIgnoreCase(name)) {
               return node;
            }
         }
      }
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CompletableFuture<Command<C>> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput, @NonNull final Executor parsingExecutor) {
      return CompletableFutures.scheduleOn(parsingExecutor, () -> {
         return this.parseDirect(commandContext, commandInput, parsingExecutor);
      }).thenApply((command) -> {
         if (command != null) {
            commandContext.command(command);
         }

         return command;
      });
   }

   @NonNull
   private CompletableFuture<Command<C>> parseDirect(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput, @NonNull final Executor parsingExecutor) {
      return this.internalTree.isLeaf() && this.internalTree.component() == null ? CompletableFutures.failedFuture(new NoSuchCommandException(commandContext.sender(), new ArrayList(), commandInput.peekString())) : this.parseCommand(new ArrayList(), commandContext, commandInput, this.internalTree, parsingExecutor).thenCompose((command) -> {
         return command != null && command.senderType().isPresent() && !GenericTypeReflector.isSuperType(((TypeToken)command.senderType().get()).getType(), commandContext.sender().getClass()) ? CompletableFutures.failedFuture(new InvalidCommandSenderException(commandContext.sender(), ((TypeToken)command.senderType().get()).getType(), new ArrayList(command.components()), command)) : CompletableFuture.completedFuture(command);
      });
   }

   private CompletableFuture<Command<C>> parseCommand(final List<CommandComponent<C>> parsedArguments, final CommandContext<C> commandContext, final CommandInput commandInput, final CommandNode<C> root, final Executor executor) {
      Optional<PermissionResult> permissionResult = this.determineAccess(commandContext.sender(), root);
      if (!permissionResult.isPresent()) {
         return CompletableFutures.failedFuture(new InvalidCommandSenderException(commandContext.sender(), (Set)root.nodeMeta().get(CommandNode.META_KEY_SENDER_TYPES), this.getComponentChain(root), (Command)null));
      } else if (((PermissionResult)permissionResult.get()).denied()) {
         return CompletableFutures.failedFuture(new NoPermissionException((PermissionResult)permissionResult.get(), commandContext.sender(), this.getComponentChain(root)));
      } else {
         CompletableFuture<Command<C>> parsedChild = this.attemptParseUnambiguousChild(parsedArguments, commandContext, root, commandInput, executor);
         if (parsedChild != null) {
            return parsedChild;
         } else if (root.children().isEmpty()) {
            CommandComponent<C> rootComponent = root.component();
            return rootComponent != null && root.command() != null && commandInput.isEmpty() ? CompletableFuture.completedFuture(root.command()) : CompletableFutures.failedFuture(new InvalidSyntaxException(this.commandManager.commandSyntaxFormatter().apply(commandContext.sender(), parsedArguments, root), commandContext.sender(), this.getComponentChain(root)));
         } else {
            CompletableFuture<Command<C>> childCompletable = CompletableFuture.completedFuture((Object)null);
            Iterator var9 = (new ArrayList(root.children())).iterator();

            while(var9.hasNext()) {
               CommandNode<C> child = (CommandNode)var9.next();
               if (child.component() != null) {
                  childCompletable = childCompletable.thenCompose((previousResult) -> {
                     if (previousResult != null) {
                        return CompletableFuture.completedFuture(previousResult);
                     } else {
                        CommandComponent<C> component = (CommandComponent)Objects.requireNonNull(child.component());
                        ParsingContext<C> parsingContext = commandContext.createParsingContext(component);
                        commandInput.skipWhitespace(1);
                        CommandInput currentInput = commandInput.copy();
                        parsingContext.markStart();
                        return component.parser().parseFuture(commandContext, commandInput).thenComposeAsync((result) -> {
                           parsingContext.markEnd();
                           parsingContext.success(!result.failure().isPresent());
                           parsingContext.consumedInput(currentInput, commandInput);
                           if (result.parsedValue().isPresent()) {
                              parsedArguments.add(component);
                              return this.parseCommand(parsedArguments, commandContext, commandInput, child, executor);
                           } else {
                              if (result.failure().isPresent()) {
                                 commandInput.cursor(currentInput.cursor());
                              }

                              return CompletableFuture.completedFuture((Object)null);
                           }
                        }, executor);
                     }
                  });
               }
            }

            return childCompletable.thenCompose((completedCommand) -> {
               if (completedCommand != null) {
                  return CompletableFuture.completedFuture(completedCommand);
               } else if (root.equals(this.internalTree)) {
                  return CompletableFutures.failedFuture(new NoSuchCommandException(commandContext.sender(), (List)this.getChain(root).stream().map(CommandNode::component).collect(Collectors.toList()), commandInput.peekString()));
               } else {
                  CommandComponent<C> rootComponent = root.component();
                  if (rootComponent != null && root.command() != null && commandInput.isEmpty()) {
                     Command<C> command = root.command();
                     PermissionResult check = this.commandManager.testPermission(commandContext.sender(), command.commandPermission());
                     return check.denied() ? CompletableFutures.failedFuture(new NoPermissionException(check, commandContext.sender(), this.getComponentChain(root))) : CompletableFuture.completedFuture(root.command());
                  } else {
                     return CompletableFutures.failedFuture(new InvalidSyntaxException(this.commandManager.commandSyntaxFormatter().apply(commandContext.sender(), parsedArguments, root), commandContext.sender(), this.getComponentChain(root)));
                  }
               }
            });
         }
      }
   }

   @Nullable
   private CompletableFuture<Command<C>> attemptParseUnambiguousChild(@NonNull final List<CommandComponent<C>> parsedArguments, @NonNull final CommandContext<C> commandContext, @NonNull final CommandNode<C> root, @NonNull final CommandInput commandInput, @NonNull final Executor executor) {
      C sender = commandContext.sender();
      List<CommandNode<C>> children = root.children();
      if (!commandInput.isEmpty() && this.matchesLiteral(children, commandInput.peekString())) {
         return null;
      } else {
         List<CommandNode<C>> argumentNodes = (List)children.stream().filter((n) -> {
            return n.component() != null && n.component().type() != CommandComponent.ComponentType.LITERAL;
         }).collect(Collectors.toList());
         if (argumentNodes.size() > 1) {
            throw new IllegalStateException("Unexpected ambiguity detected, number of dynamic child nodes should not exceed 1");
         } else if (argumentNodes.isEmpty()) {
            return null;
         } else {
            CommandNode<C> child = (CommandNode)argumentNodes.get(0);
            Optional<PermissionResult> childCheck = this.determineAccess(sender, child);
            if (!childCheck.isPresent()) {
               return CompletableFutures.failedFuture(new InvalidCommandSenderException(sender, (Set)child.nodeMeta().get(CommandNode.META_KEY_SENDER_TYPES), this.getComponentChain(child), (Command)null));
            } else if (!commandInput.isEmpty() && ((PermissionResult)childCheck.get()).denied()) {
               return CompletableFutures.failedFuture(new NoPermissionException((PermissionResult)childCheck.get(), sender, this.getComponentChain(child)));
            } else if (child.component() == null) {
               return null;
            } else {
               ArgumentParseResult<?> argumentValue = null;
               CommandComponent component;
               if (commandInput.isEmpty() && child.component().type() != CommandComponent.ComponentType.FLAG) {
                  component = (CommandComponent)Objects.requireNonNull(child.component());
                  if (!component.hasDefaultValue()) {
                     if (child.component().required()) {
                        PermissionResult check;
                        Command command;
                        CommandComponent rootComponent;
                        if (child.isLeaf()) {
                           rootComponent = root.component();
                           if (rootComponent != null && root.command() != null) {
                              command = root.command();
                              check = this.commandManager().testPermission(sender, command.commandPermission());
                              if (check.allowed()) {
                                 return CompletableFuture.completedFuture(command);
                              }

                              return CompletableFutures.failedFuture(new NoPermissionException(check, sender, this.getComponentChain(root)));
                           }

                           List<CommandComponent<C>> components = ((Command)Objects.requireNonNull(child.command())).components();
                           return CompletableFutures.failedFuture(new InvalidSyntaxException(this.commandManager.commandSyntaxFormatter().apply(commandContext.sender(), components, child), sender, this.getComponentChain(root)));
                        }

                        rootComponent = root.component();
                        if (rootComponent != null && root.command() != null) {
                           command = (Command)Objects.requireNonNull(root.command());
                           check = this.commandManager().testPermission(sender, command.commandPermission());
                           if (check.allowed()) {
                              return CompletableFuture.completedFuture(command);
                           }

                           return CompletableFutures.failedFuture(new NoPermissionException(check, sender, this.getComponentChain(root)));
                        }

                        return CompletableFutures.failedFuture(new InvalidSyntaxException(this.commandManager.commandSyntaxFormatter().apply(commandContext.sender(), parsedArguments, root), sender, this.getComponentChain(root)));
                     }

                     if (child.command() == null) {
                        CommandNode node = child;

                        while(!node.isLeaf()) {
                           node = (CommandNode)node.children().get(0);
                           CommandComponent<C> nodeComponent = node.component();
                           if (nodeComponent != null && node.command() != null) {
                              child.command(node.command());
                           }
                        }
                     }

                     return CompletableFuture.completedFuture(child.command());
                  }

                  DefaultValue<C, ?> defaultValue = (DefaultValue)Objects.requireNonNull(component.defaultValue(), "defaultValue");
                  if (defaultValue instanceof DefaultValue.ParsedDefaultValue) {
                     return this.attemptParseUnambiguousChild(parsedArguments, commandContext, root, commandInput.appendString(((DefaultValue.ParsedDefaultValue)defaultValue).value()), executor);
                  }

                  argumentValue = defaultValue.evaluateDefault(commandContext);
               }

               component = (CommandComponent)Objects.requireNonNull(child.component());
               CompletableFuture parseResult;
               if (argumentValue != null) {
                  if (argumentValue.parsedValue().isPresent()) {
                     parseResult = CompletableFuture.completedFuture(argumentValue.parsedValue().get());
                  } else {
                     parseResult = CompletableFutures.failedFuture(this.argumentParseException(commandContext, child, argumentValue));
                  }
               } else {
                  parseResult = this.parseArgument(commandContext, child, commandInput, executor).thenApply((result) -> {
                     return result.parsedValue().orElse((Object)null);
                  });
               }

               return parseResult.thenComposeAsync((value) -> {
                  if (value == null) {
                     return CompletableFuture.completedFuture((Object)null);
                  } else {
                     commandContext.store(component.name(), value);
                     if (child.isLeaf()) {
                        return commandInput.isEmpty() ? CompletableFuture.completedFuture(child.command()) : CompletableFutures.failedFuture(new InvalidSyntaxException(this.commandManager.commandSyntaxFormatter().apply(commandContext.sender(), parsedArguments, child), sender, this.getComponentChain(root)));
                     } else {
                        parsedArguments.add((CommandComponent)Objects.requireNonNull(child.component()));
                        return this.parseCommand(parsedArguments, commandContext, commandInput, child, executor);
                     }
                  }
               }, executor);
            }
         }
      }
   }

   private boolean matchesLiteral(@NonNull final List<CommandNode<C>> children, @NonNull final String input) {
      return children.stream().map(CommandNode::component).filter(Objects::nonNull).filter((n) -> {
         return n.type() == CommandComponent.ComponentType.LITERAL;
      }).flatMap((arg) -> {
         return Stream.concat(Stream.of(arg.name()), arg.aliases().stream());
      }).anyMatch((arg) -> {
         return arg.equals(input);
      });
   }

   @NonNull
   private CompletableFuture<ArgumentParseResult<?>> parseArgument(@NonNull final CommandContext<C> commandContext, @NonNull final CommandNode<C> node, @NonNull final CommandInput commandInput, @NonNull final Executor executor) {
      ParsingContext<C> parsingContext = commandContext.createParsingContext(node.component());
      parsingContext.markStart();
      ArgumentParseResult<Boolean> preParseResult = node.component().preprocess(commandContext, commandInput);
      if (!preParseResult.failure().isPresent() && (Boolean)preParseResult.parsedValue().orElse(false)) {
         commandInput.skipWhitespace(1);
         CommandInput currentInput = commandInput.copy();
         return node.component().parser().parseFuture(commandContext, commandInput).thenComposeAsync((result) -> {
            parsingContext.consumedInput(currentInput, commandInput);
            parsingContext.markEnd();
            parsingContext.success(false);
            if (result.failure().isPresent()) {
               commandInput.cursor(currentInput.cursor());
               return CompletableFutures.failedFuture(this.argumentParseException(commandContext, node, result));
            } else {
               return CompletableFuture.completedFuture(result);
            }
         }, executor);
      } else {
         parsingContext.markEnd();
         parsingContext.success(false);
         return preParseResult.failure().isPresent() ? CompletableFutures.failedFuture(this.argumentParseException(commandContext, node, preParseResult)) : CompletableFuture.completedFuture(preParseResult);
      }
   }

   @NonNull
   private ArgumentParseException argumentParseException(final CommandContext<C> commandContext, final CommandNode<C> node, final ArgumentParseResult<?> result) {
      return new ArgumentParseException((Throwable)result.failure().get(), commandContext.sender(), this.getComponentChain(node));
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public <S extends Suggestion> CompletableFuture<Suggestions<C, S>> getSuggestions(@NonNull final CommandContext<C> context, @NonNull final CommandInput commandInput, @NonNull final SuggestionMapper<S> mapper, @NonNull final Executor executor) {
      return CompletableFutures.scheduleOn(executor, () -> {
         return this.getSuggestionsDirect(context, commandInput, mapper, executor);
      });
   }

   @NonNull
   private <S extends Suggestion> CompletableFuture<Suggestions<C, S>> getSuggestionsDirect(@NonNull final CommandContext<C> context, @NonNull final CommandInput commandInput, @NonNull final SuggestionMapper<S> mapper, @NonNull final Executor executor) {
      SuggestionContext<C, S> suggestionCtx = new SuggestionContext(this.commandManager.suggestionProcessor(), context, commandInput, mapper);
      return this.getSuggestions(suggestionCtx, commandInput, this.internalTree, executor).thenApply(($) -> {
         return suggestionCtx.makeSuggestions();
      });
   }

   @NonNull
   private CompletableFuture<SuggestionContext<C, ?>> getSuggestions(@NonNull final SuggestionContext<C, ?> context, @NonNull final CommandInput commandInput, @NonNull final CommandNode<C> root, @NonNull final Executor executor) {
      if (!(Boolean)this.determineAccess(context.commandContext().sender(), root).map(PermissionResult::allowed).orElse(false)) {
         return CompletableFuture.completedFuture(context);
      } else {
         List<CommandNode<C>> children = root.children();
         List<CommandNode<C>> staticArguments = (List)children.stream().filter((n) -> {
            return n.component() != null;
         }).filter((n) -> {
            return n.component().type() == CommandComponent.ComponentType.LITERAL;
         }).collect(Collectors.toList());
         if (!commandInput.isEmpty()) {
            commandInput.skipWhitespace(1);
         }

         Iterator var8;
         CommandNode child;
         if (!staticArguments.isEmpty() && !commandInput.isEmpty(true)) {
            CommandInput commandInputCopy = commandInput.copy();
            var8 = staticArguments.iterator();

            while(var8.hasNext()) {
               child = (CommandNode)var8.next();
               CommandComponent<C> childComponent = child.component();
               if (childComponent != null) {
                  ArgumentParseResult<?> result = childComponent.parser().parse(context.commandContext(), commandInput);
                  if (result.failure().isPresent()) {
                     commandInput.cursor(commandInputCopy.cursor());
                  }

                  if (result.parsedValue().isPresent()) {
                     if (!commandInput.isEmpty()) {
                        return this.getSuggestions(context, commandInput, child, executor);
                     }
                     break;
                  }
               }
            }

            commandInput.cursor(commandInputCopy.cursor());
         }

         CompletableFuture<SuggestionContext<C, ?>> suggestionFuture = CompletableFuture.completedFuture(context);
         if (commandInput.remainingTokens() <= 1) {
            for(var8 = staticArguments.iterator(); var8.hasNext(); suggestionFuture = suggestionFuture.thenCompose((ctx) -> {
               return this.addSuggestionsForLiteralArgument(context, child, commandInput);
            })) {
               child = (CommandNode)var8.next();
            }
         }

         var8 = root.children().iterator();

         while(var8.hasNext()) {
            child = (CommandNode)var8.next();
            if (child.component() != null && child.component().type() != CommandComponent.ComponentType.LITERAL) {
               suggestionFuture = suggestionFuture.thenCompose((ctx) -> {
                  return this.addSuggestionsForDynamicArgument(context, commandInput, child, executor, false);
               });
            }
         }

         return suggestionFuture;
      }
   }

   private CompletableFuture<SuggestionContext<C, ?>> addSuggestionsForLiteralArgument(@NonNull final SuggestionContext<C, ?> context, @NonNull final CommandNode<C> node, @NonNull final CommandInput input) {
      if (!(Boolean)this.determineAccess(context.commandContext().sender(), node).map(PermissionResult::allowed).orElse(false)) {
         return CompletableFuture.completedFuture(context);
      } else {
         CommandComponent<C> component = (CommandComponent)Objects.requireNonNull(node.component());
         return component.suggestionProvider().suggestionsFuture(context.commandContext(), input.copy()).thenApply((suggestionsToAdd) -> {
            String string = input.peekString();
            Iterator var4 = suggestionsToAdd.iterator();

            while(var4.hasNext()) {
               Suggestion suggestion = (Suggestion)var4.next();
               if (!suggestion.suggestion().equals(string) && suggestion.suggestion().startsWith(string)) {
                  context.addSuggestion(suggestion);
               }
            }

            return context;
         });
      }
   }

   @NonNull
   private CompletableFuture<SuggestionContext<C, ?>> addSuggestionsForDynamicArgument(@NonNull final SuggestionContext<C, ?> context, @NonNull final CommandInput commandInput, @NonNull final CommandNode<C> child, @NonNull final Executor executor, final boolean inFlag) {
      CommandComponent<C> component = child.component();
      if (component == null) {
         return CompletableFuture.completedFuture(context);
      } else if (!inFlag && component.parser() instanceof CommandFlagParser) {
         CommandFlagParser<C> parser = (CommandFlagParser)component.parser();
         return parser.parseCurrentFlag(context.commandContext(), commandInput, executor).thenCompose((lastFlag) -> {
            if (lastFlag.isPresent()) {
               context.commandContext().store((CloudKey)CommandFlagParser.FLAG_META_KEY, (String)lastFlag.get());
            } else {
               context.commandContext().remove(CommandFlagParser.FLAG_META_KEY);
            }

            return this.addSuggestionsForDynamicArgument(context, commandInput, child, executor, true);
         });
      } else if (commandInput.isEmpty() || commandInput.remainingTokens() == 1 || child.isLeaf() && child.component().parser() instanceof AggregateParser || child.isLeaf() && child.component().parser() instanceof CommandFlagParser) {
         return this.addArgumentSuggestions(context, child, commandInput, executor);
      } else {
         CommandInput commandInputOriginal = commandInput.copy();
         ArgumentParseResult<Boolean> preParseResult = component.preprocess(context.commandContext(), commandInput);
         boolean preParseSuccess = !preParseResult.failure().isPresent() && (Boolean)preParseResult.parsedValue().orElse(false);
         CompletableFuture parsingFuture;
         if (!preParseSuccess) {
            parsingFuture = CompletableFuture.completedFuture((Object)null);
         } else {
            ParsingContext<C> parsingContext = context.commandContext().createParsingContext(child.component());
            parsingContext.markStart();
            CommandInput preParseInput = commandInput.copy();
            parsingFuture = child.component().parser().parseFuture(context.commandContext(), commandInput).thenComposeAsync((result) -> {
               Optional<?> parsedValue = result.parsedValue();
               boolean parseSuccess = parsedValue.isPresent();
               if (result.failure().isPresent()) {
                  commandInput.cursor(preParseInput.cursor());
                  return this.addArgumentSuggestions(context, child, commandInput, executor);
               } else {
                  if (child.isLeaf()) {
                     if (!commandInput.isEmpty()) {
                        return CompletableFuture.completedFuture(context);
                     }

                     commandInput.cursor(commandInputOriginal.cursor());
                     this.addArgumentSuggestions(context, child, commandInput, executor);
                  }

                  if (!parseSuccess || commandInput.isEmpty() && !commandInput.input().endsWith(" ")) {
                     if (!parseSuccess && commandInputOriginal.remainingTokens() > 1) {
                        commandInput.cursor(commandInputOriginal.cursor());
                        return CompletableFuture.completedFuture(context);
                     } else {
                        return CompletableFuture.completedFuture((Object)null);
                     }
                  } else {
                     if (commandInput.isEmpty()) {
                        commandInput.moveCursor(-1);
                     }

                     context.commandContext().store(child.component().name(), parsedValue.get());
                     parsingContext.success(true);
                     return this.getSuggestions(context, commandInput, child, executor);
                  }
               }
            }, executor);
         }

         return parsingFuture.thenCompose((previousResult) -> {
            if (previousResult != null) {
               return CompletableFuture.completedFuture(previousResult);
            } else {
               commandInput.cursor(commandInputOriginal.cursor());
               return !preParseSuccess && commandInput.remainingTokens() > 1 ? CompletableFuture.completedFuture(context) : this.addArgumentSuggestions(context, child, commandInput, executor);
            }
         });
      }
   }

   @NonNull
   private CompletableFuture<SuggestionContext<C, ?>> addArgumentSuggestions(@NonNull final SuggestionContext<C, ?> context, @NonNull final CommandNode<C> node, @NonNull final CommandInput input, @NonNull final Executor executor) {
      CommandComponent<C> component = (CommandComponent)Objects.requireNonNull(node.component());
      return this.addArgumentSuggestions(context, component, input, executor).thenCompose((ctx) -> {
         boolean isParsingFlag = component.type() == CommandComponent.ComponentType.FLAG && !node.children().isEmpty() && (!input.hasRemainingInput() || input.peek() != '-') && !context.commandContext().optional(CommandFlagParser.FLAG_META_KEY).isPresent();
         return !isParsingFlag ? CompletableFuture.completedFuture(ctx) : CompletableFuture.allOf((CompletableFuture[])node.children().stream().map((child) -> {
            return this.addArgumentSuggestions(context, (CommandComponent)Objects.requireNonNull(child.component()), input, executor);
         }).toArray((x$0) -> {
            return new CompletableFuture[x$0];
         })).thenApply((v) -> {
            return ctx;
         });
      });
   }

   private CompletableFuture<SuggestionContext<C, ?>> addArgumentSuggestions(@NonNull final SuggestionContext<C, ?> context, @NonNull final CommandComponent<C> component, @NonNull final CommandInput input, @NonNull final Executor executor) {
      CompletableFuture var10000 = component.suggestionProvider().suggestionsFuture(context.commandContext(), input.copy());
      Objects.requireNonNull(context);
      return var10000.thenAcceptAsync(context::addSuggestions, executor).thenApply((in) -> {
         return context;
      });
   }

   public void insertCommand(@NonNull final Command<C> command) {
      synchronized(this.commandLock) {
         CommandComponent<C> flagComponent = command.flagComponent();
         List<CommandComponent<C>> nonFlagArguments = command.nonFlagArguments();
         int flagStartIdx = this.flagStartIndex(nonFlagArguments);
         CommandNode<C> node = this.internalTree;

         for(int i = 0; i < nonFlagArguments.size(); ++i) {
            CommandComponent<C> component = (CommandComponent)nonFlagArguments.get(i);
            CommandNode<C> tempNode = node.getChild(component);
            if (tempNode == null) {
               tempNode = node.addChild(component);
            } else if (component.type() == CommandComponent.ComponentType.LITERAL && tempNode.component() != null) {
               Iterator var10 = component.aliases().iterator();

               while(var10.hasNext()) {
                  String alias = (String)var10.next();
                  ((LiteralParser)tempNode.component().parser()).insertAlias(alias);
               }
            }

            if (!node.children().isEmpty()) {
               node.sortChildren();
            }

            tempNode.parent(node);
            node = tempNode;
            if (flagComponent != null && i >= flagStartIdx) {
               tempNode = tempNode.addChild(flagComponent);
               tempNode.parent(node);
               node = tempNode;
            }
         }

         CommandComponent<C> nodeComponent = node.component();
         if (nodeComponent != null) {
            if (node.command() != null) {
               throw new IllegalStateException(String.format("Duplicate command chains detected. Node '%s' already has an owning command (%s)", node, node.command()));
            }

            node.command(command);
         }

         this.verifyAndRegister();
      }
   }

   private int flagStartIndex(@NonNull final List<CommandComponent<C>> components) {
      if (this.commandManager.settings().get(ManagerSetting.LIBERAL_FLAG_PARSING)) {
         for(int i = components.size() - 1; i >= 0; --i) {
            if (((CommandComponent)components.get(i)).type() == CommandComponent.ComponentType.LITERAL) {
               return i;
            }
         }
      }

      return components.size() - 1;
   }

   private Optional<PermissionResult> determineAccess(@NonNull final C sender, @NonNull final CommandNode<C> node) {
      Map<Type, Permission> accessMap = (Map)node.nodeMeta().getOrNull(CommandNode.META_KEY_ACCESS);
      if (accessMap == null) {
         throw new IllegalStateException("Expected access requirements to be propagated");
      } else {
         Set<Permission> failed = new HashSet();
         Iterator var5 = accessMap.entrySet().iterator();

         while(var5.hasNext()) {
            Entry<Type, Permission> entry = (Entry)var5.next();
            if (GenericTypeReflector.isSuperType((Type)entry.getKey(), sender.getClass())) {
               PermissionResult result = this.commandManager.testPermission(sender, (Permission)entry.getValue());
               if (result.allowed()) {
                  return Optional.of(result);
               }

               failed.add((Permission)entry.getValue());
            }
         }

         if (failed.isEmpty()) {
            return Optional.empty();
         } else {
            return Optional.of(PermissionResult.denied(Permission.anyOf((Collection)failed)));
         }
      }
   }

   private void verifyAndRegister() {
      this.internalTree.children().stream().map(CommandNode::component).forEach((component) -> {
         if (component.type() != CommandComponent.ComponentType.LITERAL) {
            throw new IllegalStateException("Top level command argument cannot be a variable");
         }
      });
      this.checkAmbiguity(this.internalTree);
      this.getLeaves(this.internalTree).forEach((leaf) -> {
         if (leaf.command() == null) {
            throw new NoCommandInLeafException(leaf.component());
         } else {
            Command<C> owningCommand = leaf.command();
            this.commandManager.commandRegistrationHandler().registerCommand(owningCommand);
         }
      });
      this.getExecutorNodes(this.internalTree).forEach(this::propagateRequirements);
   }

   @API(
      status = Status.INTERNAL
   )
   @NonNull
   public CommandNode<C> rootNode() {
      return this.internalTree;
   }

   private void propagateRequirements(@NonNull final CommandNode<C> leafNode) {
      Permission commandPermission = leafNode.command().commandPermission();
      Type senderType = (Type)leafNode.command().senderType().map(TypeToken::getType).orElse((Object)null);
      if (senderType == null) {
         senderType = Object.class;
      }

      List<CommandNode<C>> chain = this.getChain(leafNode);
      Collections.reverse(chain);
      Iterator var5 = chain.iterator();

      while(var5.hasNext()) {
         CommandNode<C> commandArgumentNode = (CommandNode)var5.next();
         Set<Type> senderTypes = (Set)commandArgumentNode.nodeMeta().computeIfAbsent(CommandNode.META_KEY_SENDER_TYPES, ($) -> {
            return new HashSet();
         });
         updateSenderRequirements(senderTypes, (Type)senderType);
         Map<Type, Permission> accessMap = (Map)commandArgumentNode.nodeMeta().computeIfAbsent(CommandNode.META_KEY_ACCESS, ($) -> {
            return new HashMap();
         });
         updateAccess(accessMap, (Type)senderType, commandPermission);
      }

   }

   private static void updateAccess(final Map<Type, Permission> senderTypes, final Type senderType, final Permission commandPermission) {
      senderTypes.compute(senderType, (key, existing) -> {
         return existing == null ? commandPermission : Permission.anyOf(existing, commandPermission);
      });
   }

   private static void updateSenderRequirements(final Set<Type> senderTypes, final Type senderType) {
      boolean add = true;
      Iterator iterator = senderTypes.iterator();

      while(iterator.hasNext()) {
         Type existingType = (Type)iterator.next();
         if (GenericTypeReflector.isSuperType(existingType, senderType)) {
            add = false;
            break;
         }

         if (GenericTypeReflector.isSuperType(senderType, existingType)) {
            iterator.remove();
            break;
         }
      }

      if (add) {
         senderTypes.add(senderType);
      }

   }

   private void checkAmbiguity(@NonNull final CommandNode<C> node) throws AmbiguousNodeException {
      if (!node.isLeaf()) {
         List<CommandNode<C>> childVariableArguments = (List)node.children().stream().filter((n) -> {
            return n.component() != null;
         }).filter((n) -> {
            return n.component().type() != CommandComponent.ComponentType.LITERAL;
         }).collect(Collectors.toList());
         if (childVariableArguments.size() > 1) {
            CommandNode<C> child = (CommandNode)childVariableArguments.get(0);
            throw new AmbiguousNodeException(node, child, (List)node.children().stream().filter((n) -> {
               return n.component() != null;
            }).collect(Collectors.toList()));
         } else {
            List<CommandNode<C>> childStaticArguments = (List)node.children().stream().filter((n) -> {
               return n.component() != null;
            }).filter((n) -> {
               return n.component().type() == CommandComponent.ComponentType.LITERAL;
            }).collect(Collectors.toList());
            Set<String> checkedLiterals = new HashSet();
            Iterator var5 = childStaticArguments.iterator();

            while(var5.hasNext()) {
               CommandNode<C> child = (CommandNode)var5.next();
               Iterator var7 = child.component().aliases().iterator();

               while(var7.hasNext()) {
                  String nameOrAlias = (String)var7.next();
                  if (!checkedLiterals.add(nameOrAlias)) {
                     throw new AmbiguousNodeException(node, child, (List)node.children().stream().filter((n) -> {
                        return n.component() != null;
                     }).collect(Collectors.toList()));
                  }
               }
            }

            node.children().forEach(this::checkAmbiguity);
         }
      }
   }

   @API(
      status = Status.INTERNAL
   )
   @NonNull
   public List<CommandNode<C>> getLeavesRaw(@NonNull final CommandNode<C> node) {
      List<CommandNode<C>> leaves = new LinkedList();
      if (node.isLeaf()) {
         if (node.component() != null) {
            leaves.add(node);
         }
      } else {
         node.children().forEach((child) -> {
            leaves.addAll(this.getLeavesRaw(child));
         });
      }

      return leaves;
   }

   @NonNull
   private List<CommandNode<C>> getExecutorNodes(@NonNull final CommandNode<C> node) {
      List<CommandNode<C>> leaves = new LinkedList();
      if (node.command() != null) {
         leaves.add(node);
      }

      Iterator var3 = node.children().iterator();

      while(var3.hasNext()) {
         CommandNode<C> child = (CommandNode)var3.next();
         leaves.addAll(this.getExecutorNodes(child));
      }

      return leaves;
   }

   @API(
      status = Status.INTERNAL
   )
   @NonNull
   public List<CommandNode<C>> getLeaves(@NonNull final CommandNode<C> node) {
      return (List)this.getLeavesRaw(node).stream().filter((n) -> {
         return n.component() != null;
      }).collect(Collectors.toList());
   }

   @NonNull
   private List<CommandComponent<?>> getComponentChain(@NonNull final CommandNode<C> end) {
      return (List)this.getChain(end).stream().map(CommandNode::component).filter(Objects::nonNull).collect(Collectors.toList());
   }

   @NonNull
   private List<CommandNode<C>> getChain(@Nullable final CommandNode<C> end) {
      List<CommandNode<C>> chain = new LinkedList();

      for(CommandNode tail = end; tail != null; tail = tail.parent()) {
         chain.add(tail);
      }

      Collections.reverse(chain);
      return chain;
   }

   void deleteRecursively(final CommandNode<C> node, final boolean root, final Consumer<Command<C>> commandConsumer) {
      Iterator var4 = (new ArrayList(node.children())).iterator();

      while(var4.hasNext()) {
         CommandNode<C> child = (CommandNode)var4.next();
         this.deleteRecursively(child, false, commandConsumer);
      }

      CommandComponent<C> component = node.component();
      Command<C> owner = component == null ? null : node.command();
      if (owner != null) {
         commandConsumer.accept(owner);
      }

      this.removeNode(node, root);
   }

   private void removeNode(@NonNull final CommandNode<C> node, final boolean root) {
      if (root) {
         this.internalTree.removeChild(node);
      } else {
         ((CommandNode)Objects.requireNonNull(node.parent(), "parent")).removeChild(node);
      }

   }
}
