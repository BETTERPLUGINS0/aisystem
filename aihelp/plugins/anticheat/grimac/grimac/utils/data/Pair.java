package ac.grim.grimac.utils.data;

public record Pair<A, B>(A first, B second) {
   public Pair(A first, B second) {
      this.first = first;
      this.second = second;
   }

   public A first() {
      return this.first;
   }

   public B second() {
      return this.second;
   }
}
