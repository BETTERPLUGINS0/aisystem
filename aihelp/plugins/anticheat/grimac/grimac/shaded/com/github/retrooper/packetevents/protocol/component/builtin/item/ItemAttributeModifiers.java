package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeOperation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.DefaultAttributeDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemAttributeModifiers {
   public static final ItemAttributeModifiers EMPTY = new ItemAttributeModifiers(Collections.emptyList(), true) {
      public void setModifiers(List<ItemAttributeModifiers.ModifierEntry> modifiers) {
         throw new UnsupportedOperationException();
      }

      public void setShowInTooltip(boolean showInTooltip) {
         throw new UnsupportedOperationException();
      }
   };
   private List<ItemAttributeModifiers.ModifierEntry> modifiers;
   @ApiStatus.Obsolete
   private boolean showInTooltip;

   public ItemAttributeModifiers(List<ItemAttributeModifiers.ModifierEntry> modifiers) {
      this(modifiers, true);
   }

   @ApiStatus.Obsolete
   public ItemAttributeModifiers(List<ItemAttributeModifiers.ModifierEntry> modifiers, boolean showInTooltip) {
      this.modifiers = modifiers;
      this.showInTooltip = showInTooltip;
   }

   public static ItemAttributeModifiers read(PacketWrapper<?> wrapper) {
      List<ItemAttributeModifiers.ModifierEntry> modifiers = wrapper.readList(ItemAttributeModifiers.ModifierEntry::read);
      boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
      return new ItemAttributeModifiers(modifiers, showInTooltip);
   }

   public static void write(PacketWrapper<?> wrapper, ItemAttributeModifiers modifiers) {
      wrapper.writeList(modifiers.modifiers, ItemAttributeModifiers.ModifierEntry::write);
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(modifiers.showInTooltip);
      }

   }

   public void addModifier(ItemAttributeModifiers.ModifierEntry modifier) {
      this.modifiers.add(modifier);
   }

   public List<ItemAttributeModifiers.ModifierEntry> getModifiers() {
      return this.modifiers;
   }

   public void setModifiers(List<ItemAttributeModifiers.ModifierEntry> modifiers) {
      this.modifiers = modifiers;
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
      } else if (!(obj instanceof ItemAttributeModifiers)) {
         return false;
      } else {
         ItemAttributeModifiers that = (ItemAttributeModifiers)obj;
         return this.showInTooltip != that.showInTooltip ? false : this.modifiers.equals(that.modifiers);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.modifiers, this.showInTooltip});
   }

   public String toString() {
      return "ItemAttributeModifiers{modifiers=" + this.modifiers + ", showInTooltip=" + this.showInTooltip + '}';
   }

   public static enum EquipmentSlotGroup {
      ANY("any"),
      MAINHAND("mainhand"),
      OFFHAND("offhand"),
      HAND("hand"),
      FEET("feet"),
      LEGS("legs"),
      CHEST("chest"),
      HEAD("head"),
      ARMOR("armor"),
      BODY("body"),
      SADDLE("saddle");

      public static final Index<String, ItemAttributeModifiers.EquipmentSlotGroup> ID_INDEX = Index.create(ItemAttributeModifiers.EquipmentSlotGroup.class, ItemAttributeModifiers.EquipmentSlotGroup::getId);
      private final String id;

      private EquipmentSlotGroup(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      // $FF: synthetic method
      private static ItemAttributeModifiers.EquipmentSlotGroup[] $values() {
         return new ItemAttributeModifiers.EquipmentSlotGroup[]{ANY, MAINHAND, OFFHAND, HAND, FEET, LEGS, CHEST, HEAD, ARMOR, BODY, SADDLE};
      }
   }

   public static class Modifier {
      private UUID id;
      private String name;
      private double value;
      private AttributeOperation operation;

      public Modifier(ResourceLocation name, double value, AttributeOperation operation) {
         this(WrapperPlayServerUpdateAttributes.PropertyModifier.generateSemiUniqueId(name), name.toString(), value, operation);
      }

      @ApiStatus.Obsolete
      public Modifier(UUID id, String name, double value, AttributeOperation operation) {
         this.id = id;
         this.name = name;
         this.value = value;
         this.operation = operation;
      }

      public static ItemAttributeModifiers.Modifier read(PacketWrapper<?> wrapper) {
         UUID id;
         String name;
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            ResourceLocation nameKey = wrapper.readIdentifier();
            name = nameKey.toString();
            id = WrapperPlayServerUpdateAttributes.PropertyModifier.generateSemiUniqueId(nameKey);
         } else {
            id = wrapper.readUUID();
            name = wrapper.readString();
         }

         double value = wrapper.readDouble();
         AttributeOperation operation = (AttributeOperation)wrapper.readEnum((Enum[])AttributeOperation.values());
         return new ItemAttributeModifiers.Modifier(id, name, value, operation);
      }

      public static void write(PacketWrapper<?> wrapper, ItemAttributeModifiers.Modifier modifier) {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            wrapper.writeIdentifier(new ResourceLocation(modifier.name));
         } else {
            wrapper.writeUUID(modifier.id);
            wrapper.writeString(modifier.name);
         }

         wrapper.writeDouble(modifier.value);
         wrapper.writeEnum(modifier.operation);
      }

      public ResourceLocation getNameKey() {
         return new ResourceLocation(this.name);
      }

      public void setNameKey(ResourceLocation nameKey) {
         this.name = nameKey.toString();
      }

      @ApiStatus.Obsolete
      public UUID getId() {
         return this.id;
      }

      @ApiStatus.Obsolete
      public void setId(UUID id) {
         this.id = id;
      }

      @ApiStatus.Obsolete
      public String getName() {
         return this.name;
      }

      @ApiStatus.Obsolete
      public void setName(String name) {
         this.name = name;
      }

      public double getValue() {
         return this.value;
      }

      public void setValue(double value) {
         this.value = value;
      }

      public AttributeOperation getOperation() {
         return this.operation;
      }

      public void setOperation(AttributeOperation operation) {
         this.operation = operation;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemAttributeModifiers.Modifier)) {
            return false;
         } else {
            ItemAttributeModifiers.Modifier modifier = (ItemAttributeModifiers.Modifier)obj;
            if (Double.compare(modifier.value, this.value) != 0) {
               return false;
            } else if (!this.id.equals(modifier.id)) {
               return false;
            } else if (!this.name.equals(modifier.name)) {
               return false;
            } else {
               return this.operation == modifier.operation;
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.id, this.name, this.value, this.operation});
      }

      public String toString() {
         return "Modifier{id=" + this.id + ", name='" + this.name + '\'' + ", value=" + this.value + ", operation=" + this.operation + '}';
      }
   }

   public static class ModifierEntry {
      private Attribute attribute;
      private ItemAttributeModifiers.Modifier modifier;
      private ItemAttributeModifiers.EquipmentSlotGroup slotGroup;
      private AttributeDisplay display;

      @ApiStatus.Obsolete
      public ModifierEntry(Attribute attribute, ItemAttributeModifiers.Modifier modifier, ItemAttributeModifiers.EquipmentSlotGroup slotGroup) {
         this(attribute, modifier, slotGroup, DefaultAttributeDisplay.INSTANCE);
      }

      public ModifierEntry(Attribute attribute, ItemAttributeModifiers.Modifier modifier, ItemAttributeModifiers.EquipmentSlotGroup slotGroup, AttributeDisplay display) {
         this.attribute = attribute;
         this.modifier = modifier;
         this.slotGroup = slotGroup;
         this.display = display;
      }

      public static ItemAttributeModifiers.ModifierEntry read(PacketWrapper<?> wrapper) {
         Attribute attribute = (Attribute)wrapper.readMappedEntity(Attributes::getById);
         ItemAttributeModifiers.Modifier modifier = ItemAttributeModifiers.Modifier.read(wrapper);
         ItemAttributeModifiers.EquipmentSlotGroup slot = (ItemAttributeModifiers.EquipmentSlotGroup)wrapper.readEnum((Enum[])ItemAttributeModifiers.EquipmentSlotGroup.values());
         AttributeDisplay display = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_6) ? AttributeDisplay.read(wrapper) : DefaultAttributeDisplay.INSTANCE;
         return new ItemAttributeModifiers.ModifierEntry(attribute, modifier, slot, (AttributeDisplay)display);
      }

      public static void write(PacketWrapper<?> wrapper, ItemAttributeModifiers.ModifierEntry entry) {
         wrapper.writeMappedEntity(entry.attribute);
         ItemAttributeModifiers.Modifier.write(wrapper, entry.modifier);
         wrapper.writeEnum(entry.slotGroup);
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
            AttributeDisplay.write(wrapper, entry.display);
         }

      }

      public Attribute getAttribute() {
         return this.attribute;
      }

      public void setAttribute(Attribute attribute) {
         this.attribute = attribute;
      }

      public ItemAttributeModifiers.Modifier getModifier() {
         return this.modifier;
      }

      public void setModifier(ItemAttributeModifiers.Modifier modifier) {
         this.modifier = modifier;
      }

      public ItemAttributeModifiers.EquipmentSlotGroup getSlotGroup() {
         return this.slotGroup;
      }

      public void setSlotGroup(ItemAttributeModifiers.EquipmentSlotGroup slotGroup) {
         this.slotGroup = slotGroup;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemAttributeModifiers.ModifierEntry)) {
            return false;
         } else {
            ItemAttributeModifiers.ModifierEntry that = (ItemAttributeModifiers.ModifierEntry)obj;
            if (!this.attribute.equals(that.attribute)) {
               return false;
            } else if (!this.modifier.equals(that.modifier)) {
               return false;
            } else {
               return this.slotGroup == that.slotGroup;
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.attribute, this.modifier, this.slotGroup});
      }

      public String toString() {
         return "ModifierEntry{attribute=" + this.attribute + ", modifier=" + this.modifier + ", slotGroup=" + this.slotGroup + '}';
      }
   }
}
