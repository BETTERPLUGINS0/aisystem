package com.nisovin.shopkeepers.currency;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Currencies {
   private static final Predicate<ItemStack> MATCHES_ANY = Currencies::matchesAny;
   private static final List<Currency> ALL = new ArrayList();
   private static final List<? extends Currency> ALL_VIEW;
   private static boolean SKIP_ITEM_DATA_CHECK;

   public static Predicate<ItemStack> matchesAny() {
      return MATCHES_ANY;
   }

   public static void load() {
      ALL.clear();
      add(new Currency("base", "base", Settings.currencyItem, 1));
      if (Settings.highCurrencyValue > 0 && Settings.highCurrencyItem.getType() != Material.AIR) {
         add(new Currency("high", "high", Settings.highCurrencyItem, Settings.highCurrencyValue));
      }

      Collections.sort(ALL, (c1, c2) -> {
         return Integer.compare(c1.getValue(), c2.getValue());
      });
      Validate.State.isTrue(getBase().getValue() == 1, "There is no currency with value 1!");
   }

   private static void add(Currency currency) {
      assert currency != null;

      assert !ALL.contains(currency);

      if (getById(currency.getId()) != null) {
         Log.severe("Invalid currency '" + currency.getId() + "': There is already another currency with the same id!");
      } else {
         if (!SKIP_ITEM_DATA_CHECK) {
            label46: {
               Iterator var1 = ALL.iterator();

               Currency otherCurrency;
               do {
                  if (!var1.hasNext()) {
                     break label46;
                  }

                  otherCurrency = (Currency)var1.next();
               } while(!otherCurrency.getItemData().matches(currency.getItemData().asUnmodifiableItemStack()) && !currency.getItemData().matches(otherCurrency.getItemData().asUnmodifiableItemStack()));

               Log.severe("Invalid currency '" + currency.getId() + "': There is already another currency with a matching item!");
               return;
            }
         }

         ALL.add(currency);
      }
   }

   public static List<? extends Currency> getAll() {
      return ALL_VIEW;
   }

   @Nullable
   public static Currency getById(String id) {
      for(int i = 0; i < ALL.size(); ++i) {
         Currency currency = (Currency)ALL.get(i);
         if (currency.getId().matches(id)) {
            return currency;
         }
      }

      return null;
   }

   @Nullable
   public static Currency match(@Nullable @ReadOnly ItemStack itemStack) {
      if (ItemUtils.isEmpty(itemStack)) {
         return null;
      } else {
         for(int i = 0; i < ALL.size(); ++i) {
            Currency currency = (Currency)ALL.get(i);
            if (currency.getItemData().matches(itemStack)) {
               return currency;
            }
         }

         return null;
      }
   }

   @Nullable
   public static Currency match(@Nullable UnmodifiableItemStack itemStack) {
      return match(ItemUtils.asItemStackOrNull(itemStack));
   }

   public static boolean matchesAny(@Nullable @ReadOnly ItemStack itemStack) {
      return match(itemStack) != null;
   }

   public static boolean matchesAny(@Nullable UnmodifiableItemStack itemStack) {
      return matchesAny(ItemUtils.asItemStackOrNull(itemStack));
   }

   public static Currency getBase() {
      return (Currency)ALL.get(0);
   }

   public static Currency getHigh() {
      Validate.State.isTrue(isHighCurrencyEnabled(), "The high currency is disabled!");
      return (Currency)ALL.get(1);
   }

   @Nullable
   public static Currency getHighOrNull() {
      return isHighCurrencyEnabled() ? getHigh() : null;
   }

   public static boolean isHighCurrencyEnabled() {
      return ALL.size() > 1;
   }

   private Currencies() {
   }

   static {
      ALL_VIEW = Collections.unmodifiableList(ALL);
      SKIP_ITEM_DATA_CHECK = true;
      load();
      SKIP_ITEM_DATA_CHECK = false;
   }
}
