package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.Nullable;

public class WrapperCommonClientCustomClickAction<T extends WrapperCommonClientCustomClickAction<T>> extends PacketWrapper<T> {
   public static final int MAX_PAYLOAD_SIZE = 65536;
   private ResourceLocation id;
   @Nullable
   private NBT payload;

   public WrapperCommonClientCustomClickAction(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperCommonClientCustomClickAction(PacketTypeCommon packetType, ResourceLocation id, @Nullable NBT payload) {
      super(packetType);
      this.id = id;
      this.payload = payload;
   }

   public void read() {
      this.id = ResourceLocation.read(this);
      this.payload = (NBT)this.readLengthPrefixed(65536, PacketWrapper::readNullableNBT);
   }

   public void write() {
      ResourceLocation.write(this, this.id);
      this.writeLengthPrefixed(this.payload, PacketWrapper::writeNBTRaw);
   }

   public void copy(T wrapper) {
      this.id = wrapper.getId();
      this.payload = wrapper.getPayload();
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public void setId(ResourceLocation id) {
      this.id = id;
   }

   @Nullable
   public NBT getPayload() {
      return this.payload;
   }

   public void setPayload(@Nullable NBT payload) {
      this.payload = payload;
   }
}
