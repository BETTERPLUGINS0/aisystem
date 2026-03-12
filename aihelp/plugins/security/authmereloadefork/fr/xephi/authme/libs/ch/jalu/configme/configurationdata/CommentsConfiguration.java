package fr.xephi.authme.libs.ch.jalu.configme.configurationdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsConfiguration {
   private final Map<String, List<String>> comments;

   public CommentsConfiguration() {
      this.comments = new HashMap();
   }

   public CommentsConfiguration(Map<String, List<String>> comments) {
      this.comments = comments;
   }

   public void setComment(String path, String... commentLines) {
      this.comments.put(path, Collections.unmodifiableList(Arrays.asList(commentLines)));
   }

   public Map<String, List<String>> getAllComments() {
      return Collections.unmodifiableMap(this.comments);
   }
}
