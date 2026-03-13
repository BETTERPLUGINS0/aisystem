package github.nighter.smartspawner.libs.mariadb.client.util;

public class MutableInt {
   private int value;

   public MutableInt() {
      this.value = -1;
   }

   public MutableInt(int value) {
      this.value = value;
   }

   public void set(int value) {
      this.value = value;
   }

   public int get() {
      return this.value;
   }

   public int incrementAndGet() {
      return ++this.value;
   }
}
