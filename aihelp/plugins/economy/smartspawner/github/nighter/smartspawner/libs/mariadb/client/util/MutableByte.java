package github.nighter.smartspawner.libs.mariadb.client.util;

public class MutableByte {
   private byte value = -1;

   public void set(byte value) {
      this.value = value;
   }

   public byte get() {
      return this.value;
   }

   public byte incrementAndGet() {
      return ++this.value;
   }
}
