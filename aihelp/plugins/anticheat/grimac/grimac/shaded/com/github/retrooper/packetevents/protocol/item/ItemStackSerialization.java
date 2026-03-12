package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public final class ItemStackSerialization {
   private ItemStackSerialization() {
   }

   public static ItemStack read(PacketWrapper<?> wrapper) {
      return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5) ? readModern(wrapper) : readLegacy(wrapper);
   }

   public static void write(PacketWrapper<?> wrapper, @Nullable ItemStack stack) {
      ItemStack replacedStack = stack == null ? ItemStack.EMPTY : stack;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         writeModern(wrapper, replacedStack);
      } else {
         writeLegacy(wrapper, replacedStack);
      }

   }

   private static ItemStack readLegacy(PacketWrapper<?> wrapper) {
      boolean v1_13_2 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13_2);
      if (v1_13_2 && !wrapper.readBoolean()) {
         return ItemStack.EMPTY;
      } else {
         int typeId = v1_13_2 ? wrapper.readVarInt() : wrapper.readShort();
         if (typeId < 0 && !v1_13_2) {
            return ItemStack.EMPTY;
         } else {
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            ItemType type = (ItemType)ItemTypes.getRegistry().getByIdOrThrow(version, typeId);
            int amount = wrapper.readByte();
            int legacyData = version.isOlderThan(ClientVersion.V_1_13) ? wrapper.readShort() : -1;
            NBTCompound nbt = wrapper.readNBT();
            return ItemStack.builder().type(type).amount(amount).nbt(nbt).legacyData(legacyData).wrapper(wrapper).build();
         }
      }
   }

   private static void writeLegacy(PacketWrapper<?> wrapper, ItemStack stack) {
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_13_2)) {
         int typeId = stack.isEmpty() ? -1 : stack.getType().getId(wrapper.getServerVersion().toClientVersion());
         wrapper.writeShort(typeId);
         if (typeId != -1) {
            wrapper.writeByte(stack.getAmount());
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_13)) {
               wrapper.writeShort(stack.getLegacyData());
            }

            wrapper.writeNBT(stack.getNBT());
         }
      } else if (stack.isEmpty()) {
         wrapper.writeBoolean(false);
      } else {
         wrapper.writeBoolean(true);
         wrapper.writeMappedEntity(stack.getType());
         wrapper.writeByte(stack.getAmount());
         wrapper.writeNBT(stack.getNBT());
      }

   }

   public static ItemStack readModern(PacketWrapper<?> wrapper) {
      return readModern(wrapper, false);
   }

   public static ItemStack readUntrusted(PacketWrapper<?> wrapper) {
      return readModern(wrapper, true);
   }

   private static ItemStack readModern(PacketWrapper<?> wrapper, boolean lengthPrefixed) {
      int count = wrapper.readVarInt();
      if (count <= 0) {
         return ItemStack.EMPTY;
      } else {
         ItemType itemType = (ItemType)wrapper.readMappedEntity((IRegistry)ItemTypes.getRegistry());
         int presentCount = wrapper.readVarInt();
         int absentCount = wrapper.readVarInt();
         if (presentCount == 0 && absentCount == 0) {
            return ItemStack.builder().type(itemType).amount(count).wrapper(wrapper).build();
         } else {
            PatchableComponentMap components = new PatchableComponentMap(itemType.getComponents(wrapper.getServerVersion().toClientVersion()), new HashMap(presentCount + absentCount));

            int i;
            for(i = 0; i < presentCount; ++i) {
               ComponentType<?> type = (ComponentType)wrapper.readMappedEntity((IRegistry)ComponentTypes.getRegistry());
               int expectedReaderIndex;
               if (lengthPrefixed) {
                  int size = wrapper.readVarInt();
                  if (size > ByteBufHelper.readableBytes(wrapper.buffer)) {
                     throw new RuntimeException("Component size " + size + " for " + type.getName() + " out of bounds");
                  }

                  expectedReaderIndex = ByteBufHelper.readerIndex(wrapper.buffer) + size;
               } else {
                  expectedReaderIndex = -1;
               }

               Object value = type.read(wrapper);
               if (expectedReaderIndex != -1) {
                  int readerIndex = ByteBufHelper.readerIndex(wrapper.buffer);
                  if (readerIndex != expectedReaderIndex) {
                     throw new RuntimeException("Invalid component read for " + type.getName() + "; expected reader index " + expectedReaderIndex + ", got reader index " + readerIndex);
                  }
               }

               components.set(type, value);
            }

            for(i = 0; i < absentCount; ++i) {
               components.unset((ComponentType)wrapper.readMappedEntity((IRegistry)ComponentTypes.getRegistry()));
            }

            return ItemStack.builder().type(itemType).amount(count).components(components).wrapper(wrapper).build();
         }
      }
   }

   public static void writeModern(PacketWrapper<?> wrapper, ItemStack stack) {
      writeModern(wrapper, stack, false);
   }

   public static void writeUntrusted(PacketWrapper<?> wrapper, ItemStack stack) {
      writeModern(wrapper, stack, true);
   }

   private static void writeModern(PacketWrapper<?> wrapper, ItemStack stack, boolean lengthPrefixed) {
      if (stack.isEmpty()) {
         wrapper.writeByte(0);
      } else {
         wrapper.writeVarInt(stack.getAmount());
         wrapper.writeMappedEntity(stack.getType());
         if (!stack.hasComponentPatches()) {
            wrapper.writeShort(0);
         } else {
            Map<ComponentType<?>, Optional<?>> allPatches = stack.getComponents().getPatches();
            int presentCount = 0;
            int absentCount = 0;
            Iterator var6 = allPatches.entrySet().iterator();

            Entry patch;
            while(var6.hasNext()) {
               patch = (Entry)var6.next();
               if (((Optional)patch.getValue()).isPresent()) {
                  ++presentCount;
               } else {
                  ++absentCount;
               }
            }

            wrapper.writeVarInt(presentCount);
            wrapper.writeVarInt(absentCount);
            var6 = allPatches.entrySet().iterator();

            while(var6.hasNext()) {
               patch = (Entry)var6.next();
               if (((Optional)patch.getValue()).isPresent()) {
                  wrapper.writeVarInt(((ComponentType)patch.getKey()).getId(wrapper.getServerVersion().toClientVersion()));
                  Runnable writer = () -> {
                     ((ComponentType)patch.getKey()).write(wrapper, ((Optional)patch.getValue()).get());
                  };
                  if (lengthPrefixed) {
                     Object originalBuffer = wrapper.buffer;
                     wrapper.buffer = ByteBufHelper.allocateNewBuffer(originalBuffer);
                     writer.run();
                     Object componentBuffer = wrapper.buffer;
                     wrapper.buffer = originalBuffer;
                     wrapper.writeVarInt(ByteBufHelper.readableBytes(componentBuffer));
                     ByteBufHelper.writeBytes(wrapper.buffer, componentBuffer);
                     ByteBufHelper.release(componentBuffer);
                  } else {
                     writer.run();
                  }
               }
            }

            var6 = allPatches.entrySet().iterator();

            while(var6.hasNext()) {
               patch = (Entry)var6.next();
               if (!((Optional)patch.getValue()).isPresent()) {
                  wrapper.writeVarInt(((ComponentType)patch.getKey()).getId(wrapper.getServerVersion().toClientVersion()));
               }
            }

         }
      }
   }
}
