package fr.xephi.authme.libs.com.google.common.escape;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use Escapers.nullEscaper() or another methods from the *Escapers classes")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class Escaper {
   private final Function<String, String> asFunction = this::escape;

   protected Escaper() {
   }

   public abstract String escape(String var1);

   public final Function<String, String> asFunction() {
      return this.asFunction;
   }
}
