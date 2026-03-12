package fr.xephi.authme.libs.com.alessiodp.libby.transitive;

import fr.xephi.authme.libs.com.alessiodp.libby.Util;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ExcludedDependency {
   @NotNull
   private final String groupId;
   @NotNull
   private final String artifactId;

   public ExcludedDependency(@NotNull String groupId, @NotNull String artifactId) {
      this.groupId = Util.replaceWithDots((String)Objects.requireNonNull(groupId, "groupId"));
      this.artifactId = Util.replaceWithDots((String)Objects.requireNonNull(artifactId, "artifactId"));
   }

   @NotNull
   public String getGroupId() {
      return this.groupId;
   }

   @NotNull
   public String getArtifactId() {
      return this.artifactId;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ExcludedDependency that = (ExcludedDependency)o;
         return !this.groupId.equals(that.groupId) ? false : this.artifactId.equals(that.artifactId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.groupId.hashCode();
      result = 31 * result + this.artifactId.hashCode();
      return result;
   }
}
