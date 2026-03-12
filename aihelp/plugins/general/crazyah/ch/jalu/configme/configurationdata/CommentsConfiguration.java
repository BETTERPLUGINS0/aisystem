package ch.jalu.configme.configurationdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

public class CommentsConfiguration {
   @NotNull
   private final Map<String, List<String>> comments;

   public CommentsConfiguration() {
      this.comments = new HashMap();
   }

   public CommentsConfiguration(@NotNull Map<String, List<String>> comments) {
      this.comments = comments;
   }

   public void setComment(@NotNull String path, @NotNull String... commentLines) {
      this.comments.put(path, Collections.unmodifiableList(Arrays.asList(commentLines)));
   }

   @NotNull
   @UnmodifiableView
   public Map<String, List<String>> getAllComments() {
      return Collections.unmodifiableMap(this.comments);
   }
}
