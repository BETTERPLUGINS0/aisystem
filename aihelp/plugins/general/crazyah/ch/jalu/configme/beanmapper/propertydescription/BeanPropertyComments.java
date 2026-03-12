package ch.jalu.configme.beanmapper.propertydescription;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeanPropertyComments {
   public static final BeanPropertyComments EMPTY = new BeanPropertyComments(Collections.emptyList(), (UUID)null);
   private final List<String> comments;
   private final UUID uuid;

   public BeanPropertyComments(@NotNull List<String> comments, @Nullable UUID uuid) {
      this.comments = comments;
      this.uuid = uuid;
   }

   @NotNull
   public List<String> getComments() {
      return this.comments;
   }

   @Nullable
   public UUID getUuid() {
      return this.uuid;
   }
}
