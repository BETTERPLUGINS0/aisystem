package ac.grim.grimac.utils.data.packetentity.dragon;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;

public final class PacketEntityEnderDragon extends PacketEntity {
   private final List<PacketEntityEnderDragonPart> parts = new ArrayList();

   public PacketEntityEnderDragon(GrimPlayer player, UUID uuid, int entityID, double x, double y, double z) {
      super(player, uuid, EntityTypes.ENDER_DRAGON, x, y, z);
      Int2ObjectOpenHashMap<PacketEntity> entityMap = player.compensatedEntities.entityMap;
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.HEAD, x, y, z, 1.0F, 1.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.NECK, x, y, z, 3.0F, 3.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.BODY, x, y, z, 5.0F, 3.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.TAIL, x, y, z, 2.0F, 2.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.TAIL, x, y, z, 2.0F, 2.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.TAIL, x, y, z, 2.0F, 2.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.WING, x, y, z, 4.0F, 2.0F));
      this.parts.add(new PacketEntityEnderDragonPart(player, DragonPart.WING, x, y, z, 4.0F, 2.0F));

      for(int i = 1; i < this.parts.size() + 1; ++i) {
         entityMap.put(entityID + i, (PacketEntity)this.parts.get(i - 1));
      }

   }

   @Generated
   public List<PacketEntityEnderDragonPart> getParts() {
      return this.parts;
   }
}
