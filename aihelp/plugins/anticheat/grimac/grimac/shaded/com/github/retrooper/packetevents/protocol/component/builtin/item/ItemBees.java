package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TypedEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ItemBees {
   private List<ItemBees.BeeEntry> bees;

   public ItemBees(List<ItemBees.BeeEntry> bees) {
      this.bees = bees;
   }

   public static ItemBees read(PacketWrapper<?> wrapper) {
      List<ItemBees.BeeEntry> bees = wrapper.readList(ItemBees.BeeEntry::read);
      return new ItemBees(bees);
   }

   public static void write(PacketWrapper<?> wrapper, ItemBees bees) {
      wrapper.writeList(bees.bees, ItemBees.BeeEntry::write);
   }

   public void addBee(ItemBees.BeeEntry bee) {
      this.bees.add(bee);
   }

   public List<ItemBees.BeeEntry> getBees() {
      return this.bees;
   }

   public void setBees(List<ItemBees.BeeEntry> bees) {
      this.bees = bees;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemBees)) {
         return false;
      } else {
         ItemBees itemBees = (ItemBees)obj;
         return this.bees.equals(itemBees.bees);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.bees);
   }

   public static class BeeEntry {
      private TypedEntityData entityData;
      private int ticksInHive;
      private int minTicksInHive;

      /** @deprecated */
      @Deprecated
      public BeeEntry(NBTCompound entityData, int ticksInHive, int minTicksInHive) {
         this(new TypedEntityData(entityData), ticksInHive, minTicksInHive);
      }

      public BeeEntry(TypedEntityData entityData, int ticksInHive, int minTicksInHive) {
         this.entityData = entityData;
         this.ticksInHive = ticksInHive;
         this.minTicksInHive = minTicksInHive;
      }

      public static ItemBees.BeeEntry read(PacketWrapper<?> wrapper) {
         TypedEntityData entityData = TypedEntityData.read(wrapper);
         int ticksInHive = wrapper.readVarInt();
         int minTicksInHive = wrapper.readVarInt();
         return new ItemBees.BeeEntry(entityData, ticksInHive, minTicksInHive);
      }

      public static void write(PacketWrapper<?> wrapper, ItemBees.BeeEntry bee) {
         TypedEntityData.write(wrapper, bee.entityData);
         wrapper.writeVarInt(bee.ticksInHive);
         wrapper.writeVarInt(bee.minTicksInHive);
      }

      public TypedEntityData getTypedEntityData() {
         return this.entityData;
      }

      public void setTypedEntityData(TypedEntityData entityData) {
         this.entityData = entityData;
      }

      public NBTCompound getEntityData() {
         return this.entityData.getCompound();
      }

      public void setEntityData(NBTCompound entityData) {
         this.entityData = new TypedEntityData(entityData);
      }

      public int getTicksInHive() {
         return this.ticksInHive;
      }

      public void setTicksInHive(int ticksInHive) {
         this.ticksInHive = ticksInHive;
      }

      public int getMinTicksInHive() {
         return this.minTicksInHive;
      }

      public void setMinTicksInHive(int minTicksInHive) {
         this.minTicksInHive = minTicksInHive;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemBees.BeeEntry)) {
            return false;
         } else {
            ItemBees.BeeEntry beeEntry = (ItemBees.BeeEntry)obj;
            if (this.ticksInHive != beeEntry.ticksInHive) {
               return false;
            } else {
               return this.minTicksInHive != beeEntry.minTicksInHive ? false : this.entityData.equals(beeEntry.entityData);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.entityData, this.ticksInHive, this.minTicksInHive});
      }
   }
}
