package ac.grim.grimac.shaded.maps.weak;

public abstract class AbstractDynamic<T> implements Dynamic {
   protected final T inner;

   public AbstractDynamic(T inner) {
      this.inner = inner;
   }

   public boolean isPresent() {
      return true;
   }

   public Object asObject() {
      return this.inner;
   }

   public int hashCode() {
      return this.inner.hashCode();
   }

   protected abstract Object keyLiteral();

   public Dynamic key() {
      return DynamicChild.key(this, this.keyLiteral());
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         AbstractDynamic other = (AbstractDynamic)o;
         return this.inner.equals(other.inner);
      } else {
         return false;
      }
   }
}
