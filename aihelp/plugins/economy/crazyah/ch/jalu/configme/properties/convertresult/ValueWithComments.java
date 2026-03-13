package ch.jalu.configme.properties.convertresult;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueWithComments {
   private final Object value;
   private final List<String> comments;
   private final UUID uniqueCommentId;

   public ValueWithComments(@NotNull Object value, @NotNull List<String> comments, @Nullable UUID uniqueCommentId) {
      this.value = value;
      this.comments = comments;
      this.uniqueCommentId = uniqueCommentId;
   }

   public ValueWithComments(@NotNull Object value, @NotNull List<String> comments) {
      this(value, comments, (UUID)null);
   }

   @NotNull
   public Object getValue() {
      return this.value;
   }

   @NotNull
   public List<String> getComments() {
      return this.comments;
   }

   @Nullable
   public UUID getUniqueCommentId() {
      return this.uniqueCommentId;
   }

   @NotNull
   public static Object unwrapValue(@NotNull Object object) {
      return object instanceof ValueWithComments ? ((ValueWithComments)object).getValue() : object;
   }

   @NotNull
   public static Stream<String> streamThroughCommentsIfApplicable(@Nullable Object object, @Nullable Set<UUID> usedCommentIds) {
      if (object instanceof ValueWithComments) {
         ValueWithComments valueWithComments = (ValueWithComments)object;
         if (valueWithComments.getUniqueCommentId() == null || usedCommentIds == null || usedCommentIds.add(valueWithComments.getUniqueCommentId())) {
            return valueWithComments.getComments().stream();
         }
      }

      return Stream.empty();
   }
}
