package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data;

public class EntityData<T> {
   private int index;
   private EntityDataType<T> type;
   private T value;

   public EntityData(int index, EntityDataType<T> type, T value) {
      this.index = index;
      this.type = type;
      this.value = value;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public EntityDataType<T> getType() {
      return this.type;
   }

   public void setType(EntityDataType<T> type) {
      this.type = type;
   }

   public T getValue() {
      return this.value;
   }

   public void setValue(T value) {
      this.value = value;
   }
}
