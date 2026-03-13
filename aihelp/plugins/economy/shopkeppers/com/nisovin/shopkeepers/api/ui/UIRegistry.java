package com.nisovin.shopkeepers.api.ui;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.types.TypeRegistry;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface UIRegistry<T extends UIType> extends TypeRegistry<T> {
   Collection<? extends UISession> getUISessions();

   Collection<? extends UISession> getUISessions(Shopkeeper var1);

   Collection<? extends UISession> getUISessions(Shopkeeper var1, UIType var2);

   Collection<? extends UISession> getUISessions(UIType var1);

   @Nullable
   UISession getUISession(Player var1);

   void abortUISessions();

   void abortUISessions(Shopkeeper var1);

   void abortUISessionsDelayed(Shopkeeper var1);
}
