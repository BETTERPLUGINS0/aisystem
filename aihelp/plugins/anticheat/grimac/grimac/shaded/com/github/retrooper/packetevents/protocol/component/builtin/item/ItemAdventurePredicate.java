package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.ComponentMatchers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemAdventurePredicate {
   private List<ItemAdventurePredicate.BlockPredicate> predicates;
   @ApiStatus.Obsolete
   private boolean showInTooltip;

   public ItemAdventurePredicate(List<ItemAdventurePredicate.BlockPredicate> predicates) {
      this(predicates, true);
   }

   @ApiStatus.Obsolete
   public ItemAdventurePredicate(List<ItemAdventurePredicate.BlockPredicate> predicates, boolean showInTooltip) {
      this.predicates = predicates;
      this.showInTooltip = showInTooltip;
   }

   public static ItemAdventurePredicate read(PacketWrapper<?> wrapper) {
      List<ItemAdventurePredicate.BlockPredicate> predicates = wrapper.readList(ItemAdventurePredicate.BlockPredicate::read);
      boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
      return new ItemAdventurePredicate(predicates, showInTooltip);
   }

   public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate predicate) {
      wrapper.writeList(predicate.predicates, ItemAdventurePredicate.BlockPredicate::write);
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(predicate.showInTooltip);
      }

   }

   public void addPredicate(ItemAdventurePredicate.BlockPredicate predicate) {
      this.predicates.add(predicate);
   }

   public List<ItemAdventurePredicate.BlockPredicate> getPredicates() {
      return this.predicates;
   }

   public void setPredicates(List<ItemAdventurePredicate.BlockPredicate> predicates) {
      this.predicates = predicates;
   }

   @ApiStatus.Obsolete
   public boolean isShowInTooltip() {
      return this.showInTooltip;
   }

   @ApiStatus.Obsolete
   public void setShowInTooltip(boolean showInTooltip) {
      this.showInTooltip = showInTooltip;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemAdventurePredicate)) {
         return false;
      } else {
         ItemAdventurePredicate that = (ItemAdventurePredicate)obj;
         return this.showInTooltip != that.showInTooltip ? false : this.predicates.equals(that.predicates);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.predicates, this.showInTooltip});
   }

   public static class RangedValueMatcher implements ItemAdventurePredicate.ValueMatcher {
      @Nullable
      private String minValue;
      @Nullable
      private String maxValue;

      public RangedValueMatcher(@Nullable String minValue, @Nullable String maxValue) {
         this.minValue = minValue;
         this.maxValue = maxValue;
      }

      public static ItemAdventurePredicate.RangedValueMatcher read(PacketWrapper<?> wrapper) {
         String minValue = (String)wrapper.readOptional(PacketWrapper::readString);
         String maxValue = (String)wrapper.readOptional(PacketWrapper::readString);
         return new ItemAdventurePredicate.RangedValueMatcher(minValue, maxValue);
      }

      public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate.RangedValueMatcher matcher) {
         wrapper.writeOptional(matcher.minValue, PacketWrapper::writeString);
         wrapper.writeOptional(matcher.maxValue, PacketWrapper::writeString);
      }

      @Nullable
      public String getMinValue() {
         return this.minValue;
      }

      public void setMinValue(@Nullable String minValue) {
         this.minValue = minValue;
      }

      @Nullable
      public String getMaxValue() {
         return this.maxValue;
      }

      public void setMaxValue(@Nullable String maxValue) {
         this.maxValue = maxValue;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemAdventurePredicate.RangedValueMatcher)) {
            return false;
         } else {
            ItemAdventurePredicate.RangedValueMatcher that = (ItemAdventurePredicate.RangedValueMatcher)obj;
            return !Objects.equals(this.minValue, that.minValue) ? false : Objects.equals(this.maxValue, that.maxValue);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.minValue, this.maxValue});
      }
   }

   public static class ExactValueMatcher implements ItemAdventurePredicate.ValueMatcher {
      private String value;

      public ExactValueMatcher(String value) {
         this.value = value;
      }

      public static ItemAdventurePredicate.ExactValueMatcher read(PacketWrapper<?> wrapper) {
         return new ItemAdventurePredicate.ExactValueMatcher(wrapper.readString());
      }

      public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate.ExactValueMatcher matcher) {
         wrapper.writeString(matcher.value);
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemAdventurePredicate.ExactValueMatcher)) {
            return false;
         } else {
            ItemAdventurePredicate.ExactValueMatcher that = (ItemAdventurePredicate.ExactValueMatcher)obj;
            return this.value.equals(that.value);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.value);
      }
   }

   public interface ValueMatcher {
      static ItemAdventurePredicate.ValueMatcher read(PacketWrapper<?> wrapper) {
         return (ItemAdventurePredicate.ValueMatcher)(wrapper.readBoolean() ? ItemAdventurePredicate.ExactValueMatcher.read(wrapper) : ItemAdventurePredicate.RangedValueMatcher.read(wrapper));
      }

      static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate.ValueMatcher matcher) {
         if (matcher instanceof ItemAdventurePredicate.ExactValueMatcher) {
            wrapper.writeBoolean(true);
            ItemAdventurePredicate.ExactValueMatcher.write(wrapper, (ItemAdventurePredicate.ExactValueMatcher)matcher);
         } else {
            if (!(matcher instanceof ItemAdventurePredicate.RangedValueMatcher)) {
               throw new IllegalArgumentException("Illegal matcher implementation: " + matcher);
            }

            wrapper.writeBoolean(false);
            ItemAdventurePredicate.RangedValueMatcher.write(wrapper, (ItemAdventurePredicate.RangedValueMatcher)matcher);
         }

      }
   }

   public static class PropertyMatcher {
      private String name;
      private ItemAdventurePredicate.ValueMatcher matcher;

      public PropertyMatcher(String name, ItemAdventurePredicate.ValueMatcher matcher) {
         this.name = name;
         this.matcher = matcher;
      }

      public static ItemAdventurePredicate.PropertyMatcher read(PacketWrapper<?> wrapper) {
         String name = wrapper.readString();
         ItemAdventurePredicate.ValueMatcher matcher = ItemAdventurePredicate.ValueMatcher.read(wrapper);
         return new ItemAdventurePredicate.PropertyMatcher(name, matcher);
      }

      public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate.PropertyMatcher matcher) {
         wrapper.writeString(matcher.name);
         ItemAdventurePredicate.ValueMatcher.write(wrapper, matcher.matcher);
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public ItemAdventurePredicate.ValueMatcher getMatcher() {
         return this.matcher;
      }

      public void setMatcher(ItemAdventurePredicate.ValueMatcher matcher) {
         this.matcher = matcher;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemAdventurePredicate.PropertyMatcher)) {
            return false;
         } else {
            ItemAdventurePredicate.PropertyMatcher that = (ItemAdventurePredicate.PropertyMatcher)obj;
            return !this.name.equals(that.name) ? false : this.matcher.equals(that.matcher);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.name, this.matcher});
      }
   }

   public static class BlockPredicate {
      @Nullable
      private MappedEntitySet<StateType.Mapped> blocks;
      @Nullable
      private List<ItemAdventurePredicate.PropertyMatcher> properties;
      @Nullable
      private NBTCompound nbt;
      private ComponentMatchers matchers;

      public BlockPredicate(@Nullable MappedEntitySet<StateType.Mapped> blocks, @Nullable List<ItemAdventurePredicate.PropertyMatcher> properties, @Nullable NBTCompound nbt) {
         this(blocks, properties, nbt, new ComponentMatchers());
      }

      public BlockPredicate(@Nullable MappedEntitySet<StateType.Mapped> blocks, @Nullable List<ItemAdventurePredicate.PropertyMatcher> properties, @Nullable NBTCompound nbt, ComponentMatchers matchers) {
         this.blocks = blocks;
         this.properties = properties;
         this.nbt = nbt;
         this.matchers = matchers;
      }

      public static ItemAdventurePredicate.BlockPredicate read(PacketWrapper<?> wrapper) {
         MappedEntitySet<StateType.Mapped> blocks = (MappedEntitySet)wrapper.readOptional((ew) -> {
            return MappedEntitySet.read(ew, StateTypes::getMappedById);
         });
         List<ItemAdventurePredicate.PropertyMatcher> properties = (List)wrapper.readOptional((ew) -> {
            return wrapper.readList(ItemAdventurePredicate.PropertyMatcher::read);
         });
         NBTCompound nbt = (NBTCompound)wrapper.readOptional(PacketWrapper::readNBT);
         ComponentMatchers matchers = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? ComponentMatchers.read(wrapper) : new ComponentMatchers();
         return new ItemAdventurePredicate.BlockPredicate(blocks, properties, nbt, matchers);
      }

      public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate.BlockPredicate predicate) {
         wrapper.writeOptional(predicate.blocks, MappedEntitySet::write);
         wrapper.writeOptional(predicate.properties, (ew, val) -> {
            ew.writeList(val, ItemAdventurePredicate.PropertyMatcher::write);
         });
         wrapper.writeOptional(predicate.nbt, PacketWrapper::writeNBT);
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            ComponentMatchers.write(wrapper, predicate.matchers);
         }

      }

      @Nullable
      public MappedEntitySet<StateType.Mapped> getBlocks() {
         return this.blocks;
      }

      public void setBlocks(@Nullable MappedEntitySet<StateType.Mapped> blocks) {
         this.blocks = blocks;
      }

      public void addProperty(ItemAdventurePredicate.PropertyMatcher propertyMatcher) {
         if (this.properties == null) {
            this.properties = new ArrayList(4);
         }

         this.properties.add(propertyMatcher);
      }

      @Nullable
      public List<ItemAdventurePredicate.PropertyMatcher> getProperties() {
         return this.properties;
      }

      public void setProperties(@Nullable List<ItemAdventurePredicate.PropertyMatcher> properties) {
         this.properties = properties;
      }

      @Nullable
      public NBTCompound getNbt() {
         return this.nbt;
      }

      public void setNbt(@Nullable NBTCompound nbt) {
         this.nbt = nbt;
      }

      public ComponentMatchers getMatchers() {
         return this.matchers;
      }

      public void setMatchers(ComponentMatchers matchers) {
         this.matchers = matchers;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemAdventurePredicate.BlockPredicate)) {
            return false;
         } else {
            ItemAdventurePredicate.BlockPredicate that = (ItemAdventurePredicate.BlockPredicate)obj;
            if (!Objects.equals(this.blocks, that.blocks)) {
               return false;
            } else if (!Objects.equals(this.properties, that.properties)) {
               return false;
            } else {
               return !Objects.equals(this.nbt, that.nbt) ? false : this.matchers.equals(that.matchers);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.blocks, this.properties, this.nbt, this.matchers});
      }
   }
}
