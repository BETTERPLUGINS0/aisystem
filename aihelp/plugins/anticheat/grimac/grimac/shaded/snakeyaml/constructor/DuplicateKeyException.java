package ac.grim.grimac.shaded.snakeyaml.constructor;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;

public class DuplicateKeyException extends ConstructorException {
   protected DuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
      super("while constructing a mapping", contextMark, "found duplicate key " + key, problemMark);
   }
}
