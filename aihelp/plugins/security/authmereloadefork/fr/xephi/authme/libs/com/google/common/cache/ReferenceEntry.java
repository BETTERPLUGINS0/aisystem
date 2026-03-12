package fr.xephi.authme.libs.com.google.common.cache;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
interface ReferenceEntry<K, V> {
   @CheckForNull
   LocalCache.ValueReference<K, V> getValueReference();

   void setValueReference(LocalCache.ValueReference<K, V> var1);

   @CheckForNull
   ReferenceEntry<K, V> getNext();

   int getHash();

   @CheckForNull
   K getKey();

   long getAccessTime();

   void setAccessTime(long var1);

   ReferenceEntry<K, V> getNextInAccessQueue();

   void setNextInAccessQueue(ReferenceEntry<K, V> var1);

   ReferenceEntry<K, V> getPreviousInAccessQueue();

   void setPreviousInAccessQueue(ReferenceEntry<K, V> var1);

   long getWriteTime();

   void setWriteTime(long var1);

   ReferenceEntry<K, V> getNextInWriteQueue();

   void setNextInWriteQueue(ReferenceEntry<K, V> var1);

   ReferenceEntry<K, V> getPreviousInWriteQueue();

   void setPreviousInWriteQueue(ReferenceEntry<K, V> var1);
}
