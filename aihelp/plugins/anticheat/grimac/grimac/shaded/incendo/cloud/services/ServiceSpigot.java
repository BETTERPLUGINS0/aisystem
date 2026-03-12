package ac.grim.grimac.shaded.incendo.cloud.services;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.services.type.ConsumerService;
import ac.grim.grimac.shaded.incendo.cloud.services.type.Service;
import ac.grim.grimac.shaded.incendo.cloud.services.type.SideEffectService;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServiceSpigot<Context, Result> {
   private final Context context;
   private final ServicePipeline pipeline;
   private final ServiceRepository<Context, Result> repository;

   ServiceSpigot(@NonNull final ServicePipeline pipeline, @NonNull final Context context, @NonNull final TypeToken<? extends Service<Context, Result>> type) {
      this.context = context;
      this.pipeline = pipeline;
      this.repository = pipeline.getRepository(type);
   }

   public Result complete() throws IllegalStateException, PipelineException {
      LinkedList<? extends ServiceRepository<Context, Result>.ServiceWrapper<? extends Service<Context, Result>>> queue = this.repository.queue();
      queue.sort((Comparator)null);
      boolean consumerService = false;

      ServiceRepository.ServiceWrapper wrapper;
      while((wrapper = (ServiceRepository.ServiceWrapper)queue.pollLast()) != null) {
         consumerService = wrapper.implementation() instanceof ConsumerService;
         if (ServiceFilterHandler.INSTANCE.passes(wrapper, this.context)) {
            Object result;
            try {
               result = wrapper.implementation().handle(this.context);
            } catch (Exception var6) {
               throw new PipelineException(String.format("Failed to retrieve result from %s", wrapper), var6);
            }

            if (wrapper.implementation() instanceof SideEffectService) {
               if (result == null) {
                  throw new IllegalStateException(String.format("SideEffectService '%s' returned null", wrapper));
               }

               if (result == State.ACCEPTED) {
                  return result;
               }
            } else if (result != null) {
               return result;
            }
         }
      }

      if (consumerService) {
         return State.ACCEPTED;
      } else {
         throw new IllegalStateException("No service consumed the context. This means that the pipeline was not constructed properly.");
      }
   }

   public void complete(@NonNull final BiConsumer<Result, Throwable> consumer) {
      try {
         consumer.accept(this.complete(), (Object)null);
      } catch (PipelineException var3) {
         consumer.accept((Object)null, var3.getCause());
      } catch (Exception var4) {
         consumer.accept((Object)null, var4);
      }

   }

   @NonNull
   public CompletableFuture<Result> completeAsynchronously() {
      return CompletableFuture.supplyAsync(this::complete, this.pipeline.executor());
   }

   @NonNull
   public ServicePump<Result> forward() {
      return this.pipeline.pump(this.complete());
   }

   @NonNull
   public CompletableFuture<ServicePump<Result>> forwardAsynchronously() {
      CompletableFuture var10000 = this.completeAsynchronously();
      ServicePipeline var10001 = this.pipeline;
      Objects.requireNonNull(var10001);
      return var10000.thenApply(var10001::pump);
   }
}
