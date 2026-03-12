package ac.grim.grimac.shaded.maps.weak;

import java.util.Iterator;
import java.util.stream.Stream;

class BreadthChildIterator implements Iterator<Dynamic> {
   private final Dynamic root;
   private int depth;
   private Iterator<Dynamic> current;

   BreadthChildIterator(Dynamic root) {
      this.root = root;
      this.depth = 1;
      this.current = root.children().iterator();
   }

   private Stream<Dynamic> nextDepth() {
      Stream<Dynamic> childrenAtNextDepth = this.root.children();

      for(int nextDepth = 1; nextDepth <= this.depth; ++nextDepth) {
         childrenAtNextDepth = childrenAtNextDepth.flatMap(Dynamic::children);
      }

      return childrenAtNextDepth;
   }

   private boolean moveDepthIfAvailable() {
      Iterator<Dynamic> nextDepth = this.nextDepth().iterator();
      if (nextDepth.hasNext()) {
         this.current = nextDepth;
         ++this.depth;
         return true;
      } else {
         return false;
      }
   }

   public boolean hasNext() {
      return this.current.hasNext() || this.moveDepthIfAvailable();
   }

   public Dynamic next() {
      if (!this.current.hasNext()) {
         this.moveDepthIfAvailable();
      }

      return (Dynamic)this.current.next();
   }
}
