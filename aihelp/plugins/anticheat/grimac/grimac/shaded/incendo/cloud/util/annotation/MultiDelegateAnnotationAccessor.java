package ac.grim.grimac.shaded.incendo.cloud.util.annotation;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
final class MultiDelegateAnnotationAccessor implements AnnotationAccessor {
   private final AnnotationAccessor[] accessors;

   MultiDelegateAnnotationAccessor(@NonNull final AnnotationAccessor... accessors) {
      this.accessors = accessors;
   }

   @Nullable
   public <A extends Annotation> A annotation(@NonNull final Class<A> clazz) {
      A instance = null;
      AnnotationAccessor[] var3 = this.accessors;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         AnnotationAccessor annotationAccessor = var3[var5];
         instance = annotationAccessor.annotation(clazz);
         if (instance != null) {
            break;
         }
      }

      return instance;
   }

   @NonNull
   public Collection<Annotation> annotations() {
      List<Annotation> annotationList = new LinkedList();
      AnnotationAccessor[] var2 = this.accessors;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AnnotationAccessor annotationAccessor = var2[var4];
         annotationList.addAll(annotationAccessor.annotations());
      }

      return Collections.unmodifiableCollection(annotationList);
   }
}
