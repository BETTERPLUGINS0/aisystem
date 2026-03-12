package ac.grim.grimac.utils.lists;

import java.util.List;
import lombok.Generated;

public abstract class ListWrapper<T> implements List<T> {
   protected final List<T> base;

   @Generated
   public List<T> getBase() {
      return this.base;
   }

   @Generated
   public ListWrapper(List<T> base) {
      this.base = base;
   }
}
