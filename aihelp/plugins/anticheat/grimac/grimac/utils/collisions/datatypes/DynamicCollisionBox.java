package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import java.util.List;
import lombok.Generated;

public class DynamicCollisionBox implements CollisionBox {
   private final GrimPlayer player;
   private final CollisionFactory box;
   private ClientVersion version;
   private WrappedBlockState block;
   private int x;
   private int y;
   private int z;

   public DynamicCollisionBox(GrimPlayer player, ClientVersion version, CollisionFactory box, WrappedBlockState block) {
      this.player = player;
      this.version = version;
      this.box = box;
      this.block = block;
   }

   public CollisionBox union(SimpleCollisionBox other) {
      CollisionBox dynamicBox = this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset((double)this.x, (double)this.y, (double)this.z);
      return dynamicBox.union(other);
   }

   public boolean isCollided(SimpleCollisionBox other) {
      return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset((double)this.x, (double)this.y, (double)this.z).isCollided(other);
   }

   public boolean isIntersected(SimpleCollisionBox other) {
      return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset((double)this.x, (double)this.y, (double)this.z).isIntersected(other);
   }

   public CollisionBox copy() {
      return (new DynamicCollisionBox(this.player, this.version, this.box, this.block)).offset((double)this.x, (double)this.y, (double)this.z);
   }

   public CollisionBox offset(double x, double y, double z) {
      this.x = (int)((double)this.x + x);
      this.y = (int)((double)this.y + y);
      this.z = (int)((double)this.z + z);
      return this;
   }

   public int downCast(SimpleCollisionBox[] list) {
      return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset((double)this.x, (double)this.y, (double)this.z).downCast(list);
   }

   public void downCast(List<SimpleCollisionBox> list) {
      this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset((double)this.x, (double)this.y, (double)this.z).downCast(list);
   }

   public boolean isNull() {
      return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).isNull();
   }

   public boolean isFullBlock() {
      return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset((double)this.x, (double)this.y, (double)this.z).isFullBlock();
   }

   @Generated
   public void setVersion(ClientVersion version) {
      this.version = version;
   }

   @Generated
   public void setBlock(WrappedBlockState block) {
      this.block = block;
   }
}
