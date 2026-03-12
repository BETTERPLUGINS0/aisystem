package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
final class FunctionalEquivalence<F, T> extends Equivalence<F> implements Serializable {
   private static final long serialVersionUID = 0L;
   private final Function<? super F, ? extends T> function;
   private final Equivalence<T> resultEquivalence;

   FunctionalEquivalence(Function<? super F, ? extends T> function, Equivalence<T> resultEquivalence) {
      this.function = (Function)Preconditions.checkNotNull(function);
      this.resultEquivalence = (Equivalence)Preconditions.checkNotNull(resultEquivalence);
   }

   protected boolean doEquivalent(F a, F b) {
      return this.resultEquivalence.equivalent(this.function.apply(a), this.function.apply(b));
   }

   protected int doHash(F a) {
      return this.resultEquivalence.hash(this.function.apply(a));
   }

   public boolean equals(@CheckForNull Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof FunctionalEquivalence)) {
         return false;
      } else {
         FunctionalEquivalence<?, ?> that = (FunctionalEquivalence)obj;
         return this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.function, this.resultEquivalence);
   }

   public String toString() {
      String var1 = String.valueOf(this.resultEquivalence);
      String var2 = String.valueOf(this.function);
      return (new StringBuilder(13 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".onResultOf(").append(var2).append(")").toString();
   }
}
