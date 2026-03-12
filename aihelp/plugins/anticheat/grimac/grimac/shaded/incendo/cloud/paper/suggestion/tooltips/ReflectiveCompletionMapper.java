package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.mojang.brigadier.Message;
import io.papermc.paper.brigadier.PaperBrigadier;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import org.checkerframework.checker.nullness.qual.NonNull;

final class ReflectiveCompletionMapper implements CompletionMapper {
   private final CompletionMapper wrapped;

   ReflectiveCompletionMapper() {
      if (CraftBukkitReflection.classExists("io.papermc.paper.command.brigadier.MessageComponentSerializer")) {
         this.wrapped = new ReflectiveCompletionMapper.Modern();
      } else {
         this.wrapped = new ReflectiveCompletionMapper.Legacy();
      }

   }

   @NonNull
   public Completion map(@NonNull final TooltipSuggestion suggestion) {
      return this.wrapped.map(suggestion);
   }

   private static final class Modern implements CompletionMapper {
      private final Object serializer;
      private final Method deserializeOrNull;
      private final Method completionWithTooltipMethod;

      Modern() {
         Method instance = CraftBukkitReflection.needMethod(MessageComponentSerializer.class, "message");

         try {
            this.serializer = instance.invoke((Object)null);
            this.deserializeOrNull = CraftBukkitReflection.needMethod(MessageComponentSerializer.class, "deserializeOrNull", Object.class);
            this.completionWithTooltipMethod = CraftBukkitReflection.needMethod(Completion.class, "completion", String.class, this.deserializeOrNull.getReturnType());
         } catch (ReflectiveOperationException var3) {
            throw new RuntimeException(var3);
         }
      }

      @NonNull
      public Completion map(@NonNull final TooltipSuggestion suggestion) {
         try {
            return (Completion)this.completionWithTooltipMethod.invoke((Object)null, suggestion.suggestion(), this.deserializeOrNull.invoke(this.serializer, suggestion.tooltip()));
         } catch (ReflectiveOperationException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   private static final class Legacy implements CompletionMapper {
      private final MethodHandle completionWithTooltip;
      private final MethodHandle componentFromMessage;

      Legacy() {
         Method componentFromMessageMethod = CraftBukkitReflection.needMethod(PaperBrigadier.class, "componentFromMessage", Message.class);
         Method completionWithTooltipMethod = CraftBukkitReflection.needMethod(Completion.class, "completion", String.class, componentFromMessageMethod.getReturnType());

         try {
            this.componentFromMessage = MethodHandles.publicLookup().unreflect(componentFromMessageMethod);
            this.completionWithTooltip = MethodHandles.publicLookup().unreflect(completionWithTooltipMethod);
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4);
         }
      }

      @NonNull
      public Completion map(@NonNull final TooltipSuggestion suggestion) {
         Message tooltip = suggestion.tooltip();
         if (tooltip == null) {
            return Completion.completion(suggestion.suggestion());
         } else {
            try {
               Object component = this.componentFromMessage.invoke(tooltip);
               return this.completionWithTooltip.invoke(suggestion.suggestion(), component);
            } catch (Throwable var4) {
               throw new RuntimeException(var4);
            }
         }
      }
   }
}
