package com.nisovin.shopkeepers.ui.villager.editor;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.editor.AbstractEditorViewProvider;
import com.nisovin.shopkeepers.ui.editor.DefaultTradingRecipesAdapter;
import com.nisovin.shopkeepers.ui.editor.EditorLayout;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.ui.villager.VillagerViewContext;
import com.nisovin.shopkeepers.util.bukkit.MerchantUtils;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.MerchantRecipe;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class VillagerEditorViewProvider extends AbstractEditorViewProvider {
   public VillagerEditorViewProvider(AbstractVillager villager) {
      super(SKDefaultUITypes.VILLAGER_EDITOR(), new VillagerViewContext(villager), new VillagerEditorViewProvider.TradingRecipesAdapter(villager));
   }

   public VillagerViewContext getContext() {
      return (VillagerViewContext)super.getContext();
   }

   public AbstractVillager getVillager() {
      return this.getContext().getObject();
   }

   public boolean canAccess(Player player, boolean silent) {
      Validate.notNull(player, (String)"player is null");
      return this.checkEditPermission(player, silent);
   }

   private boolean checkEditPermission(Player player, boolean silent) {
      return this.getVillager() instanceof WanderingTrader ? this.checkEditWanderingTraderPermission(player, silent) : this.checkEditVillagerPermission(player, silent);
   }

   private boolean checkEditWanderingTraderPermission(Player player, boolean silent) {
      return this.checkEditPermission(player, silent, "shopkeeper.edit-wandering-traders", Messages.missingEditWanderingTradersPerm);
   }

   private boolean checkEditVillagerPermission(Player player, boolean silent) {
      return this.checkEditPermission(player, silent, "shopkeeper.edit-villagers", Messages.missingEditVillagersPerm);
   }

   private boolean checkEditPermission(Player player, boolean silent, String permission, Text missingPermissionMessage) {
      if (PermissionUtils.hasPermission(player, permission)) {
         return true;
      } else {
         if (!silent) {
            this.debugNotOpeningUI(player, "Player is missing the required edit permission.");
            TextUtils.sendMessage(player, (Text)missingPermissionMessage);
         }

         return false;
      }
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new VillagerEditorView(this, player, uiState);
   }

   protected EditorLayout createLayout() {
      return new VillagerEditorLayout(this.getVillager());
   }

   protected void setupButtons() {
      super.setupButtons();
      VillagerEditorLayout layout = (VillagerEditorLayout)this.getLayout();
      layout.setupVillagerButtons();
   }

   private static class TradingRecipesAdapter extends DefaultTradingRecipesAdapter<MerchantRecipe> {
      private final AbstractVillager villager;

      private TradingRecipesAdapter(AbstractVillager villager) {
         assert villager != null;

         this.villager = villager;
      }

      public List<TradingRecipeDraft> getTradingRecipes() {
         assert this.villager.isValid();

         List<MerchantRecipe> merchantRecipes = this.villager.getRecipes();
         List<TradingRecipeDraft> recipes = MerchantUtils.createTradingRecipeDrafts(merchantRecipes);
         return recipes;
      }

      protected List<? extends MerchantRecipe> getOffers() {
         assert this.villager.isValid();

         return this.villager.getRecipes();
      }

      protected void setOffers(List<? extends MerchantRecipe> newOffers) {
         assert this.villager.isValid();

         HumanEntity trader = this.villager.getTrader();
         if (trader != null) {
            trader.closeInventory();
         }

         this.villager.setRecipes((List)Unsafe.castNonNull(newOffers));
      }

      @Nullable
      protected MerchantRecipe createOffer(TradingRecipeDraft recipe) {
         return MerchantUtils.createMerchantRecipe(recipe);
      }

      protected boolean areOffersEqual(MerchantRecipe oldOffer, MerchantRecipe newOffer) {
         return MerchantUtils.MERCHANT_RECIPES_EQUAL_ITEMS.equals(oldOffer, newOffer);
      }
   }
}
