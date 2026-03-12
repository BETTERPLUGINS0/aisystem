package ac.grim.grimac.shaded.fastutil;

import java.util.Iterator;

public interface BidirectionalIterator<K> extends Iterator<K> {
   K previous();

   boolean hasPrevious();
}
