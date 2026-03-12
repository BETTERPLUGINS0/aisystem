package ac.grim.grimac.shaded.incendo.cloud.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
final class AnnotatedElementAccessor implements AnnotationAccessor {
   private final AnnotatedElement element;

   AnnotatedElementAccessor(@NonNull final AnnotatedElement element) {
      this.element = (AnnotatedElement)Objects.requireNonNull(element, "Method may not be null");
   }

   @Nullable
   public <A extends Annotation> A annotation(@NonNull final Class<A> clazz) {
      return this.element.getAnnotation(clazz);
   }

   @NonNull
   public Collection<Annotation> annotations() {
      return Collections.unmodifiableCollection(Arrays.asList(this.element.getAnnotations()));
   }
}
