package fr.xephi.authme.libs.net.kyori.adventure.util;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public abstract class Listenable<L> {
   private final List<L> listeners = new CopyOnWriteArrayList();

   protected final void forEachListener(@NotNull final Consumer<L> consumer) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         L listener = var2.next();
         consumer.accept(listener);
      }

   }

   protected final void addListener0(@NotNull final L listener) {
      this.listeners.add(listener);
   }

   protected final void removeListener0(@NotNull final L listener) {
      this.listeners.remove(listener);
   }
}
