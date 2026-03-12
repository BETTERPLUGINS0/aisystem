package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemStack {
   public static final ItemStack EMPTY = builder().nbt(new NBTCompound()).build();
   private final ClientVersion version;
   private final IRegistryHolder registryHolder;
   private final ItemType type;
   private int amount;
   @ApiStatus.Obsolete
   @Nullable
   private NBTCompound nbt;
   @Nullable
   private PatchableComponentMap components;
   @ApiStatus.Obsolete
   private int legacyData;

   private ItemStack(ItemType type, int amount, @Nullable NBTCompound nbt, @Nullable PatchableComponentMap components, int legacyData, ClientVersion version, IRegistryHolder registryHolder) {
      this.type = type;
      this.amount = amount;
      this.nbt = nbt;
      this.components = components;
      this.legacyData = legacyData;
      this.version = version;
      this.registryHolder = registryHolder;
   }

   public static ItemStack decode(NBT nbt, PacketWrapper<?> wrapper) {
      return decode(nbt, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static ItemStack decode(NBT nbt, ClientVersion version) {
      if (nbt instanceof NBTString) {
         ResourceLocation itemName = new ResourceLocation(((NBTString)nbt).getValue());
         return builder().type(ItemTypes.getByName(itemName.toString())).build();
      } else {
         NBTCompound compound = (NBTCompound)nbt;
         ItemStack.Builder builder = builder();
         ResourceLocation itemName = (ResourceLocation)((Optional)Optional.ofNullable(compound.getStringTagValueOrNull("id")).map(Optional::of).orElseGet(() -> {
            return Optional.ofNullable(compound.getStringTagValueOrNull("item"));
         })).map(ResourceLocation::new).orElseThrow(() -> {
            return new IllegalArgumentException("No item type specified: " + compound.getTags().keySet());
         });
         builder.type(ItemTypes.getByName(itemName.toString()));
         builder.nbt(compound.getCompoundTagOrNull("tag"));
         Optional var10000 = ((Optional)Optional.ofNullable(compound.getNumberTagOrNull("Count")).map(Optional::of).orElseGet(() -> {
            return Optional.ofNullable(compound.getNumberTagOrNull("count"));
         })).map(NBTNumber::getAsInt);
         Objects.requireNonNull(builder);
         var10000.ifPresent(builder::amount);
         return builder.build();
      }
   }

   public static NBT encode(PacketWrapper<?> wrapper, ItemStack itemStack) {
      return encodeForParticle(itemStack, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static NBT encodeForParticle(ItemStack itemStack, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         boolean simple = itemStack.isEmpty() || itemStack.components == null || itemStack.components.getPatches().isEmpty();
         if (simple) {
            return new NBTString(itemStack.type.getName().toString());
         }
      }

      NBTCompound compound = new NBTCompound();
      compound.setTag("id", new NBTString(itemStack.type.getName().toString()));
      if (version.isOlderThan(ClientVersion.V_1_20_5)) {
         compound.setTag("Count", new NBTInt(itemStack.getAmount()));
         if (itemStack.nbt != null) {
            compound.setTag("tag", itemStack.nbt);
         }
      }

      return compound;
   }

   public int getMaxStackSize() {
      return this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? (Integer)this.getComponentOr(ComponentTypes.MAX_STACK_SIZE, 1) : this.getType().getMaxAmount();
   }

   public boolean isStackable() {
      return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
   }

   public boolean isDamageableItem() {
      if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         return this.hasComponent(ComponentTypes.MAX_DAMAGE) && !this.hasComponent(ComponentTypes.UNBREAKABLE_MODERN) && this.hasComponent(ComponentTypes.DAMAGE);
      } else {
         return !this.isEmpty() && this.getMaxDamage() > 0 && (this.nbt == null || !this.nbt.getBoolean("Unbreakable"));
      }
   }

   public boolean isDamaged() {
      return this.isDamageableItem() && this.getDamageValue() > 0;
   }

   public int getDamageValue() {
      if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         int value = (Integer)this.getComponentOr(ComponentTypes.DAMAGE, 0);
         return MathUtil.clamp(value, 0, this.getMaxDamage());
      } else if (this.version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         NBTNumber damage = this.nbt != null ? this.nbt.getNumberTagOrNull("Damage") : null;
         return damage == null ? 0 : damage.getAsInt();
      } else {
         return Math.max(0, this.legacyData);
      }
   }

   public void setDamageValue(int damage) {
      if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         this.setComponent(ComponentTypes.DAMAGE, (Object)MathUtil.clamp(damage, 0, this.getMaxDamage()));
      } else if (this.version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         this.getOrCreateTag().setTag("Damage", new NBTInt(Math.max(0, damage)));
      } else {
         this.legacyData = Math.max(0, damage);
      }

   }

   public int getMaxDamage() {
      return this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? (Integer)this.getComponentOr(ComponentTypes.MAX_DAMAGE, 0) : this.getType().getMaxDurability();
   }

   public NBTCompound getOrCreateTag() {
      if (this.nbt == null) {
         this.nbt = new NBTCompound();
      }

      return this.nbt;
   }

   public ItemType getType() {
      if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
         return this.isEmpty() ? ItemTypes.AIR : this.type;
      } else {
         return this.type;
      }
   }

   public int getAmount() {
      if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
         return this.isEmpty() ? 0 : this.amount;
      } else {
         return this.amount;
      }
   }

   public void shrink(int amount) {
      this.amount -= amount;
   }

   public void grow(int amount) {
      this.amount += amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public ItemStack split(int toTake) {
      int i = Math.min(toTake, this.getAmount());
      ItemStack itemstack = this.copy();
      itemstack.setAmount(i);
      this.shrink(i);
      return itemstack;
   }

   public ItemStack copy() {
      return this.isEmpty() ? EMPTY : new ItemStack(this.type, this.amount, this.nbt == null ? null : this.nbt.copy(), this.components == null ? null : this.components.copy(), this.legacyData, this.version, this.registryHolder);
   }

   @Nullable
   public NBTCompound getNBT() {
      return this.nbt;
   }

   public void setNBT(NBTCompound nbt) {
      this.nbt = nbt;
   }

   public <T> T getComponentOr(ComponentType<T> type, T otherValue) {
      return this.hasComponentPatches() ? this.getComponents().getOr(type, otherValue) : this.getType().getComponents().getOr(type, otherValue);
   }

   public <T> Optional<T> getComponent(ComponentType<T> type) {
      return this.hasComponentPatches() ? this.getComponents().getOptional(type) : this.getType().getComponents().getOptional(type);
   }

   public <T> void setComponent(ComponentType<T> type, T value) {
      this.getComponents().set(type, value);
   }

   public <T> void unsetComponent(ComponentType<T> type) {
      this.getComponents().unset(type);
   }

   public <T> void setComponent(ComponentType<T> type, Optional<T> value) {
      this.getComponents().set(type, value);
   }

   public boolean hasComponent(ComponentType<?> type) {
      return this.hasComponentPatches() ? this.getComponents().has(type) : this.getType().getComponents().has(type);
   }

   public boolean hasComponentPatches() {
      return this.components != null && !this.components.getPatches().isEmpty();
   }

   public PatchableComponentMap getComponents() {
      if (this.components == null) {
         this.components = new PatchableComponentMap(this.type.getComponents(), new HashMap(4));
      }

      return this.components;
   }

   public void setComponents(@Nullable PatchableComponentMap components) {
      this.components = components;
   }

   public int getLegacyData() {
      return this.legacyData;
   }

   public void setLegacyData(int legacyData) {
      this.legacyData = legacyData;
   }

   public boolean isEnchantable() {
      return this.isEnchantable(this.version);
   }

   /** @deprecated */
   @Deprecated
   public boolean isEnchantable(ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         return this.hasComponent(ComponentTypes.ENCHANTABLE) && !this.isEnchanted(version);
      } else if (this.type == ItemTypes.BOOK) {
         return this.getAmount() == 1;
      } else if (this.type == ItemTypes.ENCHANTED_BOOK) {
         return false;
      } else {
         return this.getMaxStackSize() == 1 && this.canBeDepleted() && !this.isEnchanted(version);
      }
   }

   public boolean isEnchanted() {
      return this.isEnchanted(this.version);
   }

   /** @deprecated */
   @Deprecated
   public boolean isEnchanted(ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         return !((ItemEnchantments)this.getComponentOr(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty() || !((ItemEnchantments)this.getComponentOr(ComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty();
      } else if (this.nbt == null) {
         return false;
      } else {
         String tagName = this.getEnchantmentsTagName(version);
         NBTList<NBTCompound> enchantments = this.nbt.getCompoundListTagOrNull(tagName);
         return enchantments != null && !enchantments.getTags().isEmpty();
      }
   }

   public List<Enchantment> getEnchantments() {
      return this.getEnchantments(this.version);
   }

   /** @deprecated */
   @Deprecated
   public List<Enchantment> getEnchantments(ClientVersion version) {
      if (!version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> nbtList = this.nbt.getCompoundListTagOrNull(tagName);
            if (nbtList != null) {
               List<NBTCompound> compounds = nbtList.getTags();
               List<Enchantment> enchantments = new ArrayList(compounds.size());
               Iterator var16 = compounds.iterator();

               while(var16.hasNext()) {
                  NBTCompound compound = (NBTCompound)var16.next();
                  EnchantmentType type = getEnchantmentTypeFromTag(compound, version);
                  if (type != null) {
                     NBTNumber levelTag = compound.getNumberTagOrNull("lvl");
                     if (levelTag != null) {
                        int level = levelTag.getAsInt();
                        Enchantment enchantment = Enchantment.builder().type(type).level(level).build();
                        enchantments.add(enchantment);
                     }
                  }
               }

               return enchantments;
            }
         }

         return new ArrayList(0);
      } else {
         ItemEnchantments enchantmentsComp = (ItemEnchantments)this.getComponentOr(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY);
         ItemEnchantments storedEnchantmentsComp = (ItemEnchantments)this.getComponentOr(ComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
         List<Enchantment> enchantmentsList = new ArrayList(enchantmentsComp.getEnchantmentCount() + storedEnchantmentsComp.getEnchantmentCount());
         Iterator var5 = enchantmentsComp.iterator();

         Entry enchantment;
         while(var5.hasNext()) {
            enchantment = (Entry)var5.next();
            enchantmentsList.add(new Enchantment((EnchantmentType)enchantment.getKey(), (Integer)enchantment.getValue()));
         }

         var5 = storedEnchantmentsComp.iterator();

         while(var5.hasNext()) {
            enchantment = (Entry)var5.next();
            enchantmentsList.add(new Enchantment((EnchantmentType)enchantment.getKey(), (Integer)enchantment.getValue()));
         }

         return enchantmentsList;
      }
   }

   public int getEnchantmentLevel(EnchantmentType enchantment) {
      return this.getEnchantmentLevel(enchantment, this.version);
   }

   /** @deprecated */
   @Deprecated
   public int getEnchantmentLevel(EnchantmentType enchantment, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         ItemEnchantments enchantmentsComp = (ItemEnchantments)this.getComponentOr(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY);
         if (!enchantmentsComp.isEmpty()) {
            int level = enchantmentsComp.getEnchantmentLevel(enchantment);
            if (level > 0) {
               return level;
            }
         }

         ItemEnchantments storedEnchantmentsComp = (ItemEnchantments)this.getComponentOr(ComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
         return !storedEnchantmentsComp.isEmpty() ? storedEnchantmentsComp.getEnchantmentLevel(enchantment) : 0;
      } else {
         if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> nbtList = this.nbt.getCompoundListTagOrNull(tagName);
            if (nbtList != null) {
               Iterator var5 = nbtList.getTags().iterator();

               while(var5.hasNext()) {
                  NBTCompound base = (NBTCompound)var5.next();
                  EnchantmentType type = getEnchantmentTypeFromTag(base, version);
                  if (Objects.equals(type, enchantment)) {
                     NBTNumber nbtLevel = base.getNumberTagOrNull("lvl");
                     return nbtLevel != null ? nbtLevel.getAsInt() : 0;
                  }
               }
            }
         }

         return 0;
      }
   }

   @Nullable
   private static EnchantmentType getEnchantmentTypeFromTag(NBTCompound tag, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         String id = tag.getStringTagValueOrNull("id");
         return EnchantmentTypes.getByName(id);
      } else {
         NBTShort idTag = (NBTShort)tag.getTagOfTypeOrNull("id", NBTShort.class);
         return idTag != null ? EnchantmentTypes.getById(version, idTag.getAsInt()) : null;
      }
   }

   public void setEnchantments(List<Enchantment> enchantments) {
      this.setEnchantments(enchantments, this.version);
   }

   /** @deprecated */
   @Deprecated
   public void setEnchantments(List<Enchantment> enchantments, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         Map<EnchantmentType, Integer> enchantmentsMap = new HashMap(enchantments.size());
         Iterator var4 = enchantments.iterator();

         while(var4.hasNext()) {
            Enchantment enchantment = (Enchantment)var4.next();
            enchantmentsMap.put(enchantment.getType(), enchantment.getLevel());
         }

         ComponentType<ItemEnchantments> componentType = this.hasComponent(ComponentTypes.STORED_ENCHANTMENTS) ? ComponentTypes.STORED_ENCHANTMENTS : ComponentTypes.ENCHANTMENTS;
         Optional<ItemEnchantments> prevEnchantments = this.getComponent(componentType);
         boolean showInTooltip = (Boolean)prevEnchantments.map(ItemEnchantments::isShowInTooltip).orElse(true);
         this.setComponent(componentType, (Object)(new ItemEnchantments(enchantmentsMap, showInTooltip)));
      } else {
         String tagName = this.getEnchantmentsTagName(version);
         if (enchantments.isEmpty()) {
            if (this.nbt != null && this.nbt.getTagOrNull(tagName) != null) {
               this.nbt.removeTag(tagName);
            }
         } else {
            List<NBTCompound> list = new ArrayList();
            Iterator var12 = enchantments.iterator();

            while(var12.hasNext()) {
               Enchantment enchantment = (Enchantment)var12.next();
               NBTCompound compound = new NBTCompound();
               if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
                  compound.setTag("id", new NBTString(enchantment.getType().getName().toString()));
               } else {
                  compound.setTag("id", new NBTShort((short)enchantment.getType().getId(version)));
               }

               compound.setTag("lvl", new NBTShort((short)enchantment.getLevel()));
               list.add(compound);
            }

            this.getOrCreateTag().setTag(tagName, new NBTList(NBTType.COMPOUND, list));
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public String getEnchantmentsTagName(ClientVersion version) {
      String tagName = version.isNewerThanOrEquals(ClientVersion.V_1_13) ? "Enchantments" : "ench";
      if (this.type == ItemTypes.ENCHANTED_BOOK) {
         tagName = "StoredEnchantments";
      }

      return tagName;
   }

   public boolean canBeDepleted() {
      return this.getMaxDamage() > 0;
   }

   public boolean is(ItemType type) {
      return this.getType() == type;
   }

   public static boolean isSameItemSameTags(ItemStack stack, ItemStack otherStack) {
      return isSameItemSameComponents(stack, otherStack);
   }

   public static boolean isSameItemSameComponents(ItemStack stack, ItemStack otherStack) {
      if (stack.version != otherStack.version) {
         throw new IllegalArgumentException("Can't compare two ItemStacks across versions: " + stack.version + " != " + otherStack.version);
      } else if (stack.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         return stack.is(otherStack.getType()) && (stack.isEmpty() && otherStack.isEmpty() || stack.getComponents().equals(otherStack.getComponents()));
      } else {
         return stack.is(otherStack.getType()) && (stack.isEmpty() && otherStack.isEmpty() || Objects.equals(stack.nbt, otherStack.nbt));
      }
   }

   public static boolean tagMatches(@Nullable ItemStack stack, @Nullable ItemStack otherStack) {
      if (stack == otherStack) {
         return true;
      } else if (stack == null) {
         return otherStack.isEmpty();
      } else if (otherStack == null) {
         return stack.isEmpty();
      } else if (stack.version != otherStack.version) {
         throw new IllegalArgumentException("Can't compare two ItemStacks across versions: " + stack.version + " != " + otherStack.version);
      } else {
         return stack.version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? stack.getComponents().equals(otherStack.getComponents()) : Objects.equals(stack.nbt, otherStack.nbt);
      }
   }

   public boolean isEmpty() {
      boolean baseEmpty = this.type == ItemTypes.AIR || this.amount <= 0;
      if (!this.version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         return baseEmpty;
      } else {
         return baseEmpty || this.legacyData < -32768 || this.legacyData > 65536;
      }
   }

   public ClientVersion getVersion() {
      return this.version;
   }

   public IRegistryHolder getRegistryHolder() {
      return this.registryHolder;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemStack)) {
         return false;
      } else {
         ItemStack itemStack = (ItemStack)obj;
         return this.type.equals(itemStack.type) && this.amount == itemStack.amount && Objects.equals(this.nbt, itemStack.nbt) && Objects.equals(this.components, itemStack.components) && this.legacyData == itemStack.legacyData;
      }
   }

   public String toString() {
      return this.isEmpty() ? "ItemStack[EMPTY]" : "ItemStack[" + this.getAmount() + "x/" + this.getMaxStackSize() + "x " + this.type.getName() + (this.nbt != null ? ", nbt tag names=" + this.nbt.getTagNames() : "") + (this.legacyData != -1 ? ", legacy data=" + this.legacyData : "") + (this.components != null ? ", components=" + this.components.getPatches() : "") + "]";
   }

   public static ItemStack.Builder builder() {
      return new ItemStack.Builder();
   }

   // $FF: synthetic method
   ItemStack(ItemType x0, int x1, NBTCompound x2, PatchableComponentMap x3, int x4, ClientVersion x5, IRegistryHolder x6, Object x7) {
      this(x0, x1, x2, x3, x4, x5, x6);
   }

   public static class Builder {
      private ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
      private IRegistryHolder registryHolder;
      private ItemType type;
      private int amount;
      @Nullable
      private NBTCompound nbt;
      @Nullable
      private PatchableComponentMap components;
      private int legacyData;

      public Builder() {
         this.registryHolder = GlobalRegistryHolder.INSTANCE;
         this.type = ItemTypes.AIR;
         this.amount = 1;
         this.nbt = null;
         this.components = null;
         this.legacyData = -1;
      }

      public ItemStack.Builder type(ItemType type) {
         this.type = type;
         return this;
      }

      public ItemStack.Builder amount(int amount) {
         this.amount = amount;
         return this;
      }

      public ItemStack.Builder nbt(NBTCompound nbt) {
         this.nbt = nbt;
         return this;
      }

      public ItemStack.Builder nbt(String key, NBT tag) {
         if (this.nbt == null) {
            this.nbt = new NBTCompound();
         }

         this.nbt.setTag(key, tag);
         return this;
      }

      public ItemStack.Builder components(@Nullable PatchableComponentMap components) {
         this.components = components;
         return this;
      }

      public <T> ItemStack.Builder component(ComponentType<T> type, @Nullable T value) {
         if (this.components == null) {
            this.components = new PatchableComponentMap(this.type.getComponents(this.version));
         }

         this.components.set(type, value);
         return this;
      }

      public ItemStack.Builder legacyData(int legacyData) {
         this.legacyData = legacyData;
         return this;
      }

      public ItemStack.Builder user(User user) {
         return this.version(user.getPacketVersion()).registryHolder(user);
      }

      public ItemStack.Builder wrapper(PacketWrapper<?> wrapper) {
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         return this.version(version).registryHolder(wrapper.getRegistryHolder());
      }

      public ItemStack.Builder version(ClientVersion version) {
         this.version = version;
         return this;
      }

      public ItemStack.Builder registryHolder(IRegistryHolder registryHolder) {
         this.registryHolder = registryHolder;
         return this;
      }

      public ItemStack build() {
         return new ItemStack(this.type, this.amount, this.nbt, this.components, this.legacyData, this.version, this.registryHolder);
      }
   }
}
