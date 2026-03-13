package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import java.util.List;
import org.bukkit.entity.Player;

public interface TradingRecipesAdapter {
   List<TradingRecipeDraft> getTradingRecipes();

   int updateTradingRecipes(Player var1, List<? extends TradingRecipeDraft> var2);
}
