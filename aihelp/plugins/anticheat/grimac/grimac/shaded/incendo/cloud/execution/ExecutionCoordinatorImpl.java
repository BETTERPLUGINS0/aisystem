package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandTree;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.CommandExecutionException;
import ac.grim.grimac.shaded.incendo.cloud.exception.CommandParseException;
import ac.grim.grimac.shaded.incendo.cloud.services.State;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionMapper;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Pair;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
final class ExecutionCoordinatorImpl<C> implements ExecutionCoordinator<C> {
   static final Executor NON_SCHEDULING_EXECUTOR = new ExecutionCoordinatorImpl.NonSchedulingExecutor();
   @NonNull
   private final Executor parsingExecutor;
   @NonNull
   private final Executor suggestionsExecutor;
   @NonNull
   private final Executor defaultExecutionExecutor;
   @Nullable
   private final Semaphore executionLock;

   ExecutionCoordinatorImpl(@Nullable final Executor parsingExecutor, @Nullable final Executor suggestionsExecutor, @Nullable final Executor defaultExecutionExecutor, final boolean syncExecution) {
      this.parsingExecutor = orRunNow(parsingExecutor);
      this.suggestionsExecutor = orRunNow(suggestionsExecutor);
      this.defaultExecutionExecutor = orRunNow(defaultExecutionExecutor);
      this.executionLock = syncExecution ? new Semaphore(1) : null;
   }

   @NonNull
   private static Executor orRunNow(@Nullable final Executor e) {
      return e == null ? ExecutionCoordinator.nonSchedulingExecutor() : e;
   }

   @NonNull
   public CompletableFuture<CommandResult<C>> coordinateExecution(@NonNull final CommandTree<C> commandTree, @NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return commandTree.parse(commandContext, commandInput, this.parsingExecutor).thenApplyAsync((command) -> {
         boolean passedPostprocessing = commandTree.commandManager().postprocessContext(commandContext, command) == State.ACCEPTED;
         return Pair.of(command, passedPostprocessing);
      }, this.parsingExecutor).thenComposeAsync((preprocessResult) -> {
         if (!(Boolean)preprocessResult.second()) {
            return CompletableFuture.completedFuture(CommandResult.of(commandContext));
         } else {
            if (this.executionLock != null) {
               try {
                  this.executionLock.acquire();
               } catch (InterruptedException var7) {
                  Thread.currentThread().interrupt();
               }
            }

            CompletableFuture commandResultFuture = null;

            try {
               commandResultFuture = ((Command)preprocessResult.first()).commandExecutionHandler().executeFuture(commandContext).exceptionally((exception) -> {
                  Throwable workingException;
                  if (exception instanceof CompletionException) {
                     workingException = exception.getCause();
                  } else {
                     workingException = exception;
                  }

                  if (workingException instanceof CommandParseException) {
                     throw (CommandParseException)workingException;
                  } else if (workingException instanceof CommandExecutionException) {
                     throw (CommandExecutionException)workingException;
                  } else {
                     throw new CommandExecutionException(workingException, commandContext);
                  }
               }).thenApply((v) -> {
                  return CommandResult.of(commandContext);
               });
            } finally {
               if (this.executionLock != null) {
                  if (commandResultFuture != null) {
                     commandResultFuture.whenComplete(($, $$) -> {
                        this.executionLock.release();
                     });
                  } else {
                     this.executionLock.release();
                  }
               }

            }

            return commandResultFuture;
         }
      }, this.defaultExecutionExecutor);
   }

   @NonNull
   public <S extends Suggestion> CompletableFuture<Suggestions<C, S>> coordinateSuggestions(@NonNull final CommandTree<C> commandTree, @NonNull final CommandContext<C> context, @NonNull final CommandInput commandInput, @NonNull final SuggestionMapper<S> mapper) {
      return commandTree.getSuggestions(context, commandInput, mapper, this.suggestionsExecutor);
   }

   private static final class NonSchedulingExecutor implements Executor {
      private NonSchedulingExecutor() {
      }

      public void execute(final Runnable command) {
         command.run();
      }

      // $FF: synthetic method
      NonSchedulingExecutor(Object x0) {
         this();
      }
   }
}
