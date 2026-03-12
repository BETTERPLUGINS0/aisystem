package ac.grim.grimac.shaded.incendo.cloud.brigadier;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Pair;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.tree.CommandNode;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public final class CloudBrigadierCommand<C, S> implements Command<S> {
   private final CommandManager<C> commandManager;
   private final CloudBrigadierManager<C, S> brigadierManager;
   private final Function<String, String> inputMapper;

   public CloudBrigadierCommand(@NonNull final CommandManager<C> commandManager, @NonNull final CloudBrigadierManager<C, S> brigadierManager) {
      this.commandManager = commandManager;
      this.brigadierManager = brigadierManager;
      this.inputMapper = Function.identity();
   }

   public CloudBrigadierCommand(@NonNull final CommandManager<C> commandManager, @NonNull final CloudBrigadierManager<C, S> brigadierManager, @NonNull final Function<String, String> inputMapper) {
      this.commandManager = commandManager;
      this.brigadierManager = brigadierManager;
      this.inputMapper = inputMapper;
   }

   public int run(@NonNull final CommandContext<S> ctx) {
      S source = ctx.getSource();
      String input = (String)this.inputMapper.apply(ctx.getInput().substring(((StringRange)((Pair)parsedNodes(ctx.getLastChild()).get(0)).second()).getStart()));
      C sender = this.brigadierManager.senderMapper().map(source);
      this.commandManager.commandExecutor().executeCommand(sender, input, (cloudContext) -> {
         cloudContext.store("_cloud_brigadier_native_sender", source);
      });
      return 1;
   }

   public static <S> List<Pair<CommandNode<S>, StringRange>> parsedNodes(final CommandContext<S> commandContext) {
      try {
         Method getNodesMethod = commandContext.getClass().getDeclaredMethod("getNodes");
         Object nodes = getNodesMethod.invoke(commandContext);
         if (nodes instanceof List) {
            return CloudBrigadierCommand.ParsedCommandNodeHandler.toPairList((List)nodes);
         } else if (nodes instanceof Map) {
            return (List)((Map)nodes).entrySet().stream().map((entry) -> {
               return Pair.of((CommandNode)entry.getKey(), (StringRange)entry.getValue());
            }).collect(Collectors.toList());
         } else {
            throw new IllegalStateException();
         }
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException(var3);
      }
   }

   private static final class ParsedCommandNodeHandler {
      private static <S> List<Pair<CommandNode<S>, StringRange>> toPairList(final List<?> nodes) {
         return (List)nodes.stream().map((n) -> {
            return Pair.of(n.getNode(), n.getRange());
         }).collect(Collectors.toList());
      }
   }
}
