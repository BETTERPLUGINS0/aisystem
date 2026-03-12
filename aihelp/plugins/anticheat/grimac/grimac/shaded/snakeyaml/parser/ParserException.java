package ac.grim.grimac.shaded.snakeyaml.parser;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;
import ac.grim.grimac.shaded.snakeyaml.error.MarkedYAMLException;

public class ParserException extends MarkedYAMLException {
   private static final long serialVersionUID = -2349253802798398038L;

   public ParserException(String context, Mark contextMark, String problem, Mark problemMark) {
      super(context, contextMark, problem, problemMark, (String)null, (Throwable)null);
   }
}
