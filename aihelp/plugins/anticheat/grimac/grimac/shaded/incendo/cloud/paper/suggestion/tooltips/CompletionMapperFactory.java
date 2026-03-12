package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips;

import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public interface CompletionMapperFactory {
   @NonNull
   static CompletionMapperFactory detectingRelocation() {
      return new CompletionMapperFactory.CompletionMapperFactoryImpl();
   }

   @NonNull
   CompletionMapper createMapper();

   public static final class CompletionMapperFactoryImpl implements CompletionMapperFactory {
      private CompletionMapperFactoryImpl() {
      }

      @NonNull
      public CompletionMapper createMapper() {
         return (CompletionMapper)(Audience.class.isAssignableFrom(Player.class) ? new NativeCompletionMapper() : new ReflectiveCompletionMapper());
      }

      // $FF: synthetic method
      CompletionMapperFactoryImpl(Object x0) {
         this();
      }
   }
}
