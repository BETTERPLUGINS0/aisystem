package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.CommandTree;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionMapper;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.checkerframework.dataflow.qual.Pure;

@API(
   status = Status.STABLE
)
public interface ExecutionCoordinator<C> {
   @Pure
   @NonNull
   static <C> ExecutionCoordinator.Builder<C> builder() {
      return new ExecutionCoordinatorBuilderImpl();
   }

   @Pure
   @NonNull
   static <C> ExecutionCoordinator<C> simpleCoordinator() {
      return builder().build();
   }

   @Pure
   @NonNull
   static <C> ExecutionCoordinator<C> coordinatorFor(@NonNull final Executor executor) {
      return builder().executor(executor).build();
   }

   @Pure
   @NonNull
   static <C> ExecutionCoordinator<C> asyncCoordinator() {
      return builder().commonPoolExecutor().build();
   }

   @NonNull
   CompletableFuture<CommandResult<C>> coordinateExecution(@NonNull CommandTree<C> commandTree, @NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput);

   @NonNull
   <S extends Suggestion> CompletableFuture<Suggestions<C, S>> coordinateSuggestions(@NonNull CommandTree<C> commandTree, @NonNull CommandContext<C> context, @NonNull CommandInput commandInput, @NonNull SuggestionMapper<S> mapper);

   @Pure
   @NonNull
   static Executor nonSchedulingExecutor() {
      return ExecutionCoordinatorImpl.NON_SCHEDULING_EXECUTOR;
   }

   @API(
      status = Status.STABLE
   )
   public interface Builder<C> {
      @This
      @NonNull
      default ExecutionCoordinator.Builder<C> executor(@NonNull final Executor executor) {
         return this.parsingExecutor(executor).suggestionsExecutor(executor).executionSchedulingExecutor(executor);
      }

      @This
      @NonNull
      default ExecutionCoordinator.Builder<C> commonPoolExecutor() {
         return this.executor(ForkJoinPool.commonPool());
      }

      @This
      @NonNull
      ExecutionCoordinator.Builder<C> parsingExecutor(@NonNull Executor executor);

      @This
      @NonNull
      ExecutionCoordinator.Builder<C> suggestionsExecutor(@NonNull Executor executor);

      @This
      @NonNull
      ExecutionCoordinator.Builder<C> executionSchedulingExecutor(@NonNull Executor executor);

      @This
      @NonNull
      default ExecutionCoordinator.Builder<C> synchronizeExecution() {
         return this.synchronizeExecution(true);
      }

      @This
      @NonNull
      ExecutionCoordinator.Builder<C> synchronizeExecution(boolean synchronizeExecution);

      @NonNull
      ExecutionCoordinator<C> build();
   }
}
