package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import java.lang.reflect.Method;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class BukkitBackwardsBrigadierSenderMapper<C, S> implements Function<C, S> {
   private static final Class<?> VANILLA_COMMAND_WRAPPER_CLASS = CraftBukkitReflection.needOBCClass("command.VanillaCommandWrapper");
   private static final Method GET_LISTENER_METHOD;
   private final SenderMapper<?, C> senderMapper;

   public BukkitBackwardsBrigadierSenderMapper(@NonNull final SenderMapper<?, C> senderMapper) {
      this.senderMapper = senderMapper;
   }

   public S apply(@NonNull final C cloud) {
      try {
         return GET_LISTENER_METHOD.invoke((Object)null, this.senderMapper.reverse(cloud));
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException(var3);
      }
   }

   static {
      GET_LISTENER_METHOD = CraftBukkitReflection.needMethod(VANILLA_COMMAND_WRAPPER_CLASS, "getListener", CommandSender.class);
   }
}
