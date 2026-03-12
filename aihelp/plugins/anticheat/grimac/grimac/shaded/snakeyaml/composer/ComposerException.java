package ac.grim.grimac.shaded.snakeyaml.composer;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;
import ac.grim.grimac.shaded.snakeyaml.error.MarkedYAMLException;

public class ComposerException extends MarkedYAMLException {
   private static final long serialVersionUID = 2146314636913113935L;

   protected ComposerException(String context, Mark contextMark, String problem, Mark problemMark) {
      super(context, contextMark, problem, problemMark);
   }
}
