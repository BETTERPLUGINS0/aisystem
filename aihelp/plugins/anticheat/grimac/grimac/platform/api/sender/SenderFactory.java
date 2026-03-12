package ac.grim.grimac.platform.api.sender;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import java.util.UUID;

public abstract class SenderFactory<T> {
   protected abstract UUID getUniqueId(T var1);

   protected abstract String getName(T var1);

   protected abstract void sendMessage(T var1, String var2);

   protected abstract void sendMessage(T var1, Component var2);

   protected abstract boolean hasPermission(T var1, String var2);

   protected abstract boolean hasPermission(T var1, String var2, boolean var3);

   protected abstract void performCommand(T var1, String var2);

   protected abstract boolean isConsole(T var1);

   protected abstract boolean isPlayer(T var1);

   protected boolean shouldSplitNewlines(T sender) {
      return this.isConsole(sender);
   }

   @NotNull
   public final Sender wrap(@NotNull T sender) {
      Objects.requireNonNull(sender, "sender");
      return new AbstractSender(this, sender);
   }

   @NotNull
   public final T unwrap(@NotNull Sender sender) {
      Objects.requireNonNull(sender, "sender");
      return sender.getNativeSender();
   }
}
