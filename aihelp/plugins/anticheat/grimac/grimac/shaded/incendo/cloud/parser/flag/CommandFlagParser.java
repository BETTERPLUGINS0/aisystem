package ac.grim.grimac.shaded.incendo.cloud.parser.flag;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.caption.Caption;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.caption.StandardCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class CommandFlagParser<C> implements ArgumentParser.FutureArgumentParser<C, Object>, SuggestionProvider<C> {
   public static final Object FLAG_PARSE_RESULT_OBJECT = new Object();
   public static final CloudKey<String> FLAG_META_KEY = CloudKey.of("__last_flag__", TypeToken.get(String.class));
   public static final CloudKey<Integer> FLAG_CURSOR_KEY = CloudKey.of("__flag_cursor__", TypeToken.get(Integer.class));
   public static final CloudKey<Set<CommandFlag<?>>> PARSED_FLAGS = CloudKey.of("__parsed_flags__", new TypeToken<Set<CommandFlag<?>>>() {
   });
   private static final Pattern FLAG_PRIMARY_PATTERN = Pattern.compile(" --(?<name>([A-Za-z]+))");
   private static final Pattern FLAG_ALIAS_PATTERN = Pattern.compile(" -(?<name>([A-Za-z]+))");
   private final Collection<CommandFlag<?>> flags;

   public CommandFlagParser(@NonNull final Collection<CommandFlag<?>> flags) {
      this.flags = flags;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Collection<CommandFlag<?>> flags() {
      return Collections.unmodifiableCollection(this.flags);
   }

   @NonNull
   public CompletableFuture<ArgumentParseResult<Object>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return (new CommandFlagParser.FlagParser()).parse(commandContext, commandInput);
   }

   @API(
      status = Status.STABLE
   )
   public CompletableFuture<Optional<String>> parseCurrentFlag(final CommandContext<C> commandContext, final CommandInput commandInput, final Executor completionExecutor) {
      if (commandInput.isEmpty()) {
         return CompletableFuture.completedFuture(Optional.empty());
      } else {
         String lastInputValue = commandInput.lastRemainingToken();
         CommandFlagParser<C>.FlagParser parser = new CommandFlagParser.FlagParser();
         CompletableFuture<ArgumentParseResult<Object>> result = parser.parse(commandContext, commandInput);
         return result.thenApplyAsync((parseResult) -> {
            if (commandContext.contains(FLAG_CURSOR_KEY)) {
               commandInput.cursor((Integer)commandContext.get(FLAG_CURSOR_KEY));
            } else if (parser.lastParsedFlag() == null && commandInput.isEmpty()) {
               int count = lastInputValue.length();
               commandInput.moveCursor(-count);
            }

            return Optional.ofNullable(parser.lastParsedFlag());
         }, completionExecutor);
      }
   }

   @NonNull
   public CompletableFuture<Iterable<Suggestion>> suggestionsFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      String lastArg = (String)Objects.requireNonNull((String)commandContext.getOrDefault(FLAG_META_KEY, ""));
      Iterator var8;
      String currentFlag;
      if (!lastArg.startsWith("-")) {
         String readInput = input.readInput();
         List<CommandFlag<?>> usedFlags = new LinkedList();
         Matcher primaryMatcher = FLAG_PRIMARY_PATTERN.matcher(readInput);

         while(true) {
            while(primaryMatcher.find()) {
               String name = primaryMatcher.group("name");
               var8 = this.flags.iterator();

               while(var8.hasNext()) {
                  CommandFlag<?> flag = (CommandFlag)var8.next();
                  if (flag.name().equalsIgnoreCase(name)) {
                     usedFlags.add(flag);
                     break;
                  }
               }
            }

            Matcher aliasMatcher = FLAG_ALIAS_PATTERN.matcher(readInput);

            Iterator var11;
            String nextToken;
            label149:
            while(aliasMatcher.find()) {
               nextToken = aliasMatcher.group("name");
               Iterator var24 = this.flags.iterator();

               while(true) {
                  while(true) {
                     if (!var24.hasNext()) {
                        continue label149;
                     }

                     CommandFlag<?> flag = (CommandFlag)var24.next();
                     var11 = flag.aliases().iterator();

                     while(var11.hasNext()) {
                        String alias = (String)var11.next();
                        if (nextToken.contains(alias)) {
                           usedFlags.add(flag);
                           break;
                        }
                     }
                  }
               }
            }

            nextToken = input.peekString();
            if (nextToken.length() > 1) {
               currentFlag = nextToken.substring(1);
            } else {
               currentFlag = "";
            }

            List<Suggestion> suggestions = new LinkedList();
            var11 = this.flags.iterator();

            while(true) {
               CommandFlag flag;
               do {
                  if (!var11.hasNext()) {
                     boolean suggestCombined = nextToken.length() > 1 && nextToken.startsWith("-") && !nextToken.startsWith("--");
                     Iterator var28 = this.flags.iterator();

                     label116:
                     while(true) {
                        CommandFlag flag;
                        do {
                           do {
                              if (!var28.hasNext()) {
                                 if (suggestCombined) {
                                    suggestions.add(Suggestion.suggestion(input.peekString()));
                                 }

                                 return CompletableFuture.completedFuture(suggestions);
                              }

                              flag = (CommandFlag)var28.next();
                           } while(usedFlags.contains(flag) && flag.mode() != CommandFlag.FlagMode.REPEATABLE);
                        } while(!commandContext.hasPermission(flag.permission()));

                        Iterator var14 = flag.aliases().iterator();

                        while(true) {
                           while(true) {
                              String alias;
                              do {
                                 if (!var14.hasNext()) {
                                    continue label116;
                                 }

                                 alias = (String)var14.next();
                              } while(alias.equalsIgnoreCase(currentFlag));

                              if (suggestCombined && flag.commandComponent() == null) {
                                 suggestions.add(Suggestion.suggestion(String.format("%s%s", input.peekString(), alias)));
                              } else {
                                 suggestions.add(Suggestion.suggestion(String.format("-%s", alias)));
                              }
                           }
                        }
                     }
                  }

                  flag = (CommandFlag)var11.next();
               } while(usedFlags.contains(flag) && flag.mode() != CommandFlag.FlagMode.REPEATABLE);

               if (commandContext.hasPermission(flag.permission())) {
                  suggestions.add(Suggestion.suggestion(String.format("--%s", flag.name())));
               }
            }
         }
      } else {
         CommandFlag<?> currentFlag = null;
         String flagName;
         Iterator var6;
         CommandFlag flag;
         if (lastArg.startsWith("--")) {
            flagName = lastArg.substring(2);
            var6 = this.flags.iterator();

            while(var6.hasNext()) {
               flag = (CommandFlag)var6.next();
               if (flagName.equalsIgnoreCase(flag.name())) {
                  currentFlag = flag;
                  break;
               }
            }
         } else {
            flagName = lastArg.substring(1);
            var6 = this.flags.iterator();

            label173:
            while(var6.hasNext()) {
               flag = (CommandFlag)var6.next();
               var8 = flag.aliases().iterator();

               while(var8.hasNext()) {
                  currentFlag = (String)var8.next();
                  if (currentFlag.equalsIgnoreCase(flagName)) {
                     currentFlag = flag;
                     break label173;
                  }
               }
            }
         }

         if (currentFlag != null && commandContext.hasPermission(currentFlag.permission()) && currentFlag.commandComponent() != null) {
            SuggestionProvider suggestionProvider = currentFlag.commandComponent().suggestionProvider();
            return suggestionProvider.suggestionsFuture(commandContext, input);
         } else {
            commandContext.store((CloudKey)FLAG_META_KEY, "");
            return this.suggestionsFuture(commandContext, input);
         }
      }
   }

   private final class FlagParser {
      private String lastParsedFlag;

      private FlagParser() {
      }

      @NonNull
      private CompletableFuture<ArgumentParseResult<Object>> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
         CompletableFuture<ArgumentParseResult<Object>> result = CompletableFuture.completedFuture((Object)null);
         Set<CommandFlag<?>> parsedFlags = (Set)commandContext.computeIfAbsent(CommandFlagParser.PARSED_FLAGS, (k) -> {
            return new HashSet();
         });
         int remainingTokens = commandInput.remainingTokens();

         for(int i = 0; i <= remainingTokens; ++i) {
            result = result.thenCompose((parseResult) -> {
               commandInput.skipWhitespace();
               if (parseResult == null && !commandInput.isEmpty()) {
                  String string = commandInput.peekString();
                  if (!string.startsWith("-")) {
                     return CompletableFuture.completedFuture(ArgumentParseResult.success(CommandFlagParser.FLAG_PARSE_RESULT_OBJECT));
                  } else {
                     this.lastParsedFlag = null;
                     if (string.startsWith("--")) {
                        commandInput.moveCursor(2);
                     } else {
                        commandInput.moveCursor(1);
                     }

                     String flagName = commandInput.readStringSkipWhitespace();
                     CommandFlag<?> flag = null;
                     Iterator var8;
                     CommandFlag flagCandidate;
                     if (string.startsWith("--")) {
                        var8 = CommandFlagParser.this.flags.iterator();

                        while(var8.hasNext()) {
                           flagCandidate = (CommandFlag)var8.next();
                           if (flagName.equalsIgnoreCase(flagCandidate.name())) {
                              flag = flagCandidate;
                              break;
                           }
                        }
                     } else {
                        if (flagName.length() != 1) {
                           boolean flagFound = false;

                           for(int j = 0; j < flagName.length(); ++j) {
                              String parsedFlag = Character.toString(flagName.charAt(j)).toLowerCase(Locale.ENGLISH);
                              Iterator var17 = CommandFlagParser.this.flags.iterator();

                              while(var17.hasNext()) {
                                 CommandFlag<?> candidateFlag = (CommandFlag)var17.next();
                                 if (candidateFlag.commandComponent() == null && candidateFlag.aliases().contains(parsedFlag)) {
                                    if (parsedFlags.contains(candidateFlag) && candidateFlag.mode() != CommandFlag.FlagMode.REPEATABLE) {
                                       return this.fail(new CommandFlagParser.FlagParseException(string, CommandFlagParser.FailureReason.DUPLICATE_FLAG, commandContext));
                                    }

                                    if (!commandContext.hasPermission(candidateFlag.permission())) {
                                       return this.fail(new CommandFlagParser.FlagParseException(string, CommandFlagParser.FailureReason.NO_PERMISSION, commandContext));
                                    }

                                    commandContext.flags().addPresenceFlag(candidateFlag);
                                    parsedFlags.add(candidateFlag);
                                    flagFound = true;
                                 }
                              }
                           }

                           if (!flagFound) {
                              return this.fail(new CommandFlagParser.FlagParseException(string, CommandFlagParser.FailureReason.NO_FLAG_STARTED, commandContext));
                           }

                           return CompletableFuture.completedFuture((Object)null);
                        }

                        var8 = CommandFlagParser.this.flags.iterator();

                        label97:
                        while(var8.hasNext()) {
                           flagCandidate = (CommandFlag)var8.next();
                           Iterator var10 = flagCandidate.aliases().iterator();

                           while(var10.hasNext()) {
                              String alias = (String)var10.next();
                              if (alias.equalsIgnoreCase(flagName)) {
                                 flag = flagCandidate;
                                 break label97;
                              }
                           }
                        }
                     }

                     if (flag == null) {
                        return this.fail(new CommandFlagParser.FlagParseException(string, CommandFlagParser.FailureReason.UNKNOWN_FLAG, commandContext));
                     } else if (parsedFlags.contains(flag) && flag.mode() != CommandFlag.FlagMode.REPEATABLE) {
                        return this.fail(new CommandFlagParser.FlagParseException(string, CommandFlagParser.FailureReason.DUPLICATE_FLAG, commandContext));
                     } else if (!commandContext.hasPermission(flag.permission())) {
                        return this.fail(new CommandFlagParser.FlagParseException(string, CommandFlagParser.FailureReason.NO_PERMISSION, commandContext));
                     } else if (flag.commandComponent() == null) {
                        commandContext.remove(CommandFlagParser.FLAG_CURSOR_KEY);
                        commandContext.flags().addPresenceFlag(flag);
                        parsedFlags.add(flag);
                        return CompletableFuture.completedFuture((Object)null);
                     } else {
                        if (commandInput.hasRemainingInput() && commandInput.peek() == ' ') {
                           this.lastParsedFlag = string;
                        }

                        if (commandInput.isEmpty(true)) {
                           return this.fail(new CommandFlagParser.FlagParseException(flag.name(), CommandFlagParser.FailureReason.MISSING_ARGUMENT, commandContext));
                        } else {
                           this.lastParsedFlag = string;
                           CommandInput commandInputCopy = commandInput.copy();
                           return flag.commandComponent().parser().parseFuture(commandContext, commandInput).thenApply((parsedValue) -> {
                              if (parsedValue.failure().isPresent() || commandInput.isEmpty() || commandInput.peek() != ' ') {
                                 commandContext.store((CloudKey)CommandFlagParser.FLAG_CURSOR_KEY, commandInputCopy.cursor());
                              }

                              if (parsedValue.failure().isPresent()) {
                                 return parsedValue;
                              } else {
                                 commandContext.flags().addValueFlag(flag, parsedValue.parsedValue().get());
                                 parsedFlags.add(flag);
                                 if (!commandInput.isEmpty(false) && commandInput.peek() == ' ') {
                                    this.lastParsedFlag = null;
                                 }

                                 return null;
                              }
                           });
                        }
                     }
                  }
               } else {
                  return CompletableFuture.completedFuture(parseResult);
               }
            });
         }

         return result.thenApply((r) -> {
            return r == null ? ArgumentParseResult.success(CommandFlagParser.FLAG_PARSE_RESULT_OBJECT) : r;
         });
      }

      @Nullable
      private String lastParsedFlag() {
         return this.lastParsedFlag;
      }

      @NonNull
      private CompletableFuture<ArgumentParseResult<Object>> fail(@NonNull final Throwable exception) {
         return ArgumentParseResult.failureFuture(exception);
      }

      // $FF: synthetic method
      FlagParser(Object x1) {
         this();
      }
   }

   @API(
      status = Status.STABLE
   )
   public static final class FlagParseException extends ParserException {
      private final String input;
      private final CommandFlagParser.FailureReason failureReason;

      public FlagParseException(@NonNull final String input, @NonNull final CommandFlagParser.FailureReason failureReason, @NonNull final CommandContext<?> context) {
         super(CommandFlagParser.class, context, failureReason.caption(), CaptionVariable.of("input", input), CaptionVariable.of("flag", input));
         this.input = input;
         this.failureReason = failureReason;
      }

      public String input() {
         return this.input;
      }

      @API(
         status = Status.STABLE
      )
      @NonNull
      public CommandFlagParser.FailureReason failureReason() {
         return this.failureReason;
      }
   }

   @API(
      status = Status.STABLE
   )
   public static enum FailureReason {
      UNKNOWN_FLAG(StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_FLAG_UNKNOWN_FLAG),
      DUPLICATE_FLAG(StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_FLAG_DUPLICATE_FLAG),
      NO_FLAG_STARTED(StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_FLAG_NO_FLAG_STARTED),
      MISSING_ARGUMENT(StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_FLAG_MISSING_ARGUMENT),
      NO_PERMISSION(StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_FLAG_NO_PERMISSION);

      private final Caption caption;

      private FailureReason(@NonNull final Caption caption) {
         this.caption = caption;
      }

      @NonNull
      public Caption caption() {
         return this.caption;
      }

      // $FF: synthetic method
      private static CommandFlagParser.FailureReason[] $values() {
         return new CommandFlagParser.FailureReason[]{UNKNOWN_FLAG, DUPLICATE_FLAG, NO_FLAG_STARTED, MISSING_ARGUMENT, NO_PERMISSION};
      }
   }
}
