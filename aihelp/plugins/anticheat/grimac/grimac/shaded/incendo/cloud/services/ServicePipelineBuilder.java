package ac.grim.grimac.shaded.incendo.cloud.services;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServicePipelineBuilder {
   private Executor executor = Executors.newSingleThreadExecutor();

   ServicePipelineBuilder() {
   }

   @NonNull
   public ServicePipeline build() {
      return new ServicePipeline(this.executor);
   }

   @NonNull
   public ServicePipelineBuilder withExecutor(@NonNull final Executor executor) {
      this.executor = (Executor)Objects.requireNonNull(executor, "Executor may not be null");
      return this;
   }
}
