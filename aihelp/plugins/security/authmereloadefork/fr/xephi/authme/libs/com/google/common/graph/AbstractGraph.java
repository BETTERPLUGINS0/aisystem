package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class AbstractGraph<N> extends AbstractBaseGraph<N> implements Graph<N> {
   public final boolean equals(@CheckForNull Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Graph)) {
         return false;
      } else {
         Graph<?> other = (Graph)obj;
         return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && this.edges().equals(other.edges());
      }
   }

   public final int hashCode() {
      return this.edges().hashCode();
   }

   public String toString() {
      boolean var1 = this.isDirected();
      boolean var2 = this.allowsSelfLoops();
      String var3 = String.valueOf(this.nodes());
      String var4 = String.valueOf(this.edges());
      return (new StringBuilder(59 + String.valueOf(var3).length() + String.valueOf(var4).length())).append("isDirected: ").append(var1).append(", allowsSelfLoops: ").append(var2).append(", nodes: ").append(var3).append(", edges: ").append(var4).toString();
   }
}
