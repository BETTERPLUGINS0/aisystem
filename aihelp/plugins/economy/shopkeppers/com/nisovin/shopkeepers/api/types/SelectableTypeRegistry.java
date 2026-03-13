package com.nisovin.shopkeepers.api.types;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface SelectableTypeRegistry<T extends SelectableType> extends TypeRegistry<T> {
   boolean canBeSelected(Player var1, @NonNull T var2);

   @Nullable
   T getDefaultSelection(Player var1);

   @Nullable
   T getSelection(Player var1);

   @Nullable
   T selectNext(Player var1);

   @Nullable
   T selectPrevious(Player var1);

   void clearSelection(Player var1);

   void clearAllSelections();
}
