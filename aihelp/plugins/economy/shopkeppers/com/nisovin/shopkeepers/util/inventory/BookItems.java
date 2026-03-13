package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class BookItems {
   public static boolean isWrittenBook(@Nullable @ReadOnly ItemStack itemStack) {
      if (itemStack == null) {
         return false;
      } else if (itemStack.getType() != Material.WRITTEN_BOOK) {
         return false;
      } else {
         return itemStack.getAmount() > 0;
      }
   }

   @Nullable
   public static BookMeta getBookMeta(@Nullable @ReadOnly ItemStack itemStack) {
      return !isWrittenBook(itemStack) ? null : (BookMeta)Unsafe.castNonNull(((ItemStack)Unsafe.assertNonNull(itemStack)).getItemMeta());
   }

   @Nullable
   public static BookMeta getBookMeta(@Nullable UnmodifiableItemStack itemStack) {
      return getBookMeta(ItemUtils.asItemStackOrNull(itemStack));
   }

   @Nullable
   public static String getBookTitle(@Nullable @ReadOnly ItemStack itemStack) {
      BookMeta bookMeta = getBookMeta(itemStack);
      return bookMeta == null ? null : getTitle(bookMeta);
   }

   @Nullable
   public static String getTitle(@ReadOnly BookMeta bookMeta) {
      Validate.notNull(bookMeta, (String)"bookMeta is null");
      return StringUtils.getNotEmpty(bookMeta.getTitle());
   }

   public static Generation getGeneration(@ReadOnly BookMeta bookMeta) {
      Validate.notNull(bookMeta, (String)"bookMeta is null");
      Generation generation = bookMeta.getGeneration();
      return generation == null ? Generation.ORIGINAL : generation;
   }

   public static boolean isCopyable(Generation generation) {
      return getCopyGeneration(generation) != null;
   }

   @Nullable
   public static Generation getCopyGeneration(Generation generation) {
      Validate.notNull(generation, (String)"generation is null");
      switch(generation) {
      case ORIGINAL:
         return Generation.COPY_OF_ORIGINAL;
      case COPY_OF_ORIGINAL:
         return Generation.COPY_OF_COPY;
      default:
         return null;
      }
   }

   public static boolean isCopy(Generation generation) {
      return generation == Generation.COPY_OF_ORIGINAL || generation == Generation.COPY_OF_COPY;
   }

   public static boolean isCopyableBook(@Nullable @ReadOnly ItemStack itemStack) {
      BookMeta bookMeta = getBookMeta(itemStack);
      return bookMeta == null ? false : isCopyable(bookMeta);
   }

   public static boolean isCopyable(@ReadOnly BookMeta bookMeta) {
      Generation generation = getGeneration(bookMeta);
      return isCopyable(generation);
   }

   public static boolean isBookCopy(@Nullable @ReadOnly ItemStack itemStack) {
      BookMeta bookMeta = getBookMeta(itemStack);
      return bookMeta == null ? false : isCopy(bookMeta);
   }

   public static boolean isCopy(@ReadOnly BookMeta bookMeta) {
      Generation generation = getGeneration(bookMeta);
      return isCopy(generation);
   }

   public static ItemStack copyBook(@ReadOnly ItemStack bookItem) {
      Validate.isTrue(isWrittenBook(bookItem), "bookItem is not a written book");
      ItemStack copy = ItemUtils.copyWithAmount((ItemStack)bookItem, 1);
      BookMeta copyBookMeta = (BookMeta)Unsafe.castNonNull(copy.getItemMeta());
      Generation oldGeneration = getGeneration(copyBookMeta);
      Generation copyGeneration = getCopyGeneration(oldGeneration);
      Validate.notNull(copyGeneration, (String)"bookItem is not copyable");
      copyBookMeta.setGeneration(copyGeneration);
      copy.setItemMeta(copyBookMeta);
      return copy;
   }

   private BookItems() {
   }
}
