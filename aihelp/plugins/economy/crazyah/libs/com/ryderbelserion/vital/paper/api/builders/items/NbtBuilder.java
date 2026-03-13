package libs.com.ryderbelserion.vital.paper.api.builders.items;

import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NbtBuilder {
   private ItemStack itemStack;

   public NbtBuilder() {
      this(Material.STONE, 1);
   }

   public NbtBuilder(@NotNull Material material) {
      this(material, 1);
   }

   public NbtBuilder(@NotNull Material material, int amount) {
      this(ItemStack.of(material, amount), true);
   }

   public NbtBuilder(@NotNull ItemStack itemStack, boolean createNewStack) {
      this.itemStack = createNewStack ? itemStack.clone() : itemStack;
   }

   public final boolean hasItemMeta() {
      return !this.itemStack.hasItemMeta();
   }

   @NotNull
   public final NbtBuilder setItemStack(ItemStack itemStack) {
      this.itemStack = itemStack;
      return this;
   }

   @NotNull
   public final NbtBuilder setPersistentDouble(@NotNull NamespacedKey key, double value) {
      this.itemStack.editMeta((itemMeta) -> {
         itemMeta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
      });
      return this;
   }

   @NotNull
   public final NbtBuilder setPersistentInteger(@NotNull NamespacedKey key, int value) {
      this.itemStack.editMeta((itemMeta) -> {
         itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
      });
      return this;
   }

   @NotNull
   public final NbtBuilder setPersistentBoolean(@NotNull NamespacedKey key, boolean value) {
      this.itemStack.editMeta((itemMeta) -> {
         itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, value);
      });
      return this;
   }

   @NotNull
   public final NbtBuilder setPersistentString(@NotNull NamespacedKey key, @NotNull String value) {
      this.itemStack.editMeta((itemMeta) -> {
         itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
      });
      return this;
   }

   @NotNull
   public final NbtBuilder setPersistentList(@NotNull NamespacedKey key, @NotNull List<String> values) {
      this.itemStack.editMeta((itemMeta) -> {
         itemMeta.getPersistentDataContainer().set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), values);
      });
      return this;
   }

   public final boolean getBoolean(@NotNull NamespacedKey key) {
      return (Boolean)this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false);
   }

   public final double getDouble(@NotNull NamespacedKey key) {
      return (Double)this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0.0D);
   }

   public final int getInteger(@NotNull NamespacedKey key) {
      return (Integer)this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
   }

   @NotNull
   public final List<String> getList(@NotNull NamespacedKey key) {
      return (List)this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LIST.strings(), Collections.emptyList());
   }

   @NotNull
   public final String getString(@NotNull NamespacedKey key) {
      return (String)this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
   }

   @NotNull
   public final NbtBuilder removePersistentKey(@Nullable NamespacedKey key) {
      if (key == null) {
         return this;
      } else if (this.hasItemMeta()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            itemMeta.getPersistentDataContainer().remove(key);
         });
         return this;
      }
   }

   public final boolean hasKey(@NotNull NamespacedKey key) {
      return this.itemStack.getPersistentDataContainer().has(key);
   }

   public final ItemStack getItemStack() {
      return this.itemStack;
   }
}
