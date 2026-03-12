package libs.com.ryderbelserion.vital.common.api.commands.context;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

public abstract class CommandInfo<S> {
   private final CommandContext<S> context;

   public CommandInfo(@NotNull CommandContext<S> context) {
      this.context = context;
   }

   @NotNull
   public final S getSource() {
      return this.context.getSource();
   }

   @NotNull
   public final String getStringArgument(@NotNull String key) {
      return (String)this.context.getArgument(key, String.class);
   }

   public final int getIntegerArgument(@NotNull String key) {
      return (Integer)this.context.getArgument(key, Integer.class);
   }

   public final float getFloatArgument(@NotNull String key) {
      return (Float)this.context.getArgument(key, Float.class);
   }

   public final double getDoubleArgument(@NotNull String key) {
      return (Double)this.context.getArgument(key, Double.class);
   }
}
