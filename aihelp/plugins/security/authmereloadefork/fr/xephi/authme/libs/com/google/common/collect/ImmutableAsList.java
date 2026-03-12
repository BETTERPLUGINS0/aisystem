package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
abstract class ImmutableAsList<E> extends ImmutableList<E> {
   abstract ImmutableCollection<E> delegateCollection();

   public boolean contains(@CheckForNull Object target) {
      return this.delegateCollection().contains(target);
   }

   public int size() {
      return this.delegateCollection().size();
   }

   public boolean isEmpty() {
      return this.delegateCollection().isEmpty();
   }

   boolean isPartialView() {
      return this.delegateCollection().isPartialView();
   }

   @GwtIncompatible
   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
      throw new InvalidObjectException("Use SerializedForm");
   }

   @GwtIncompatible
   Object writeReplace() {
      return new ImmutableAsList.SerializedForm(this.delegateCollection());
   }

   @GwtIncompatible
   static class SerializedForm implements Serializable {
      final ImmutableCollection<?> collection;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableCollection<?> collection) {
         this.collection = collection;
      }

      Object readResolve() {
         return this.collection.asList();
      }
   }
}
