package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Primitives;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class MutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
   private final Map<Class<? extends B>, B> delegate;

   public static <B> MutableClassToInstanceMap<B> create() {
      return new MutableClassToInstanceMap(new HashMap());
   }

   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
      return new MutableClassToInstanceMap(backingMap);
   }

   private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
      this.delegate = (Map)Preconditions.checkNotNull(delegate);
   }

   protected Map<Class<? extends B>, B> delegate() {
      return this.delegate;
   }

   private static <B> Entry<Class<? extends B>, B> checkedEntry(final Entry<Class<? extends B>, B> entry) {
      return new ForwardingMapEntry<Class<? extends B>, B>() {
         protected Entry<Class<? extends B>, B> delegate() {
            return entry;
         }

         public B setValue(B value) {
            return super.setValue(MutableClassToInstanceMap.cast((Class)this.getKey(), value));
         }
      };
   }

   public Set<Entry<Class<? extends B>, B>> entrySet() {
      return new ForwardingSet<Entry<Class<? extends B>, B>>() {
         protected Set<Entry<Class<? extends B>, B>> delegate() {
            return MutableClassToInstanceMap.this.delegate().entrySet();
         }

         public Spliterator<Entry<Class<? extends B>, B>> spliterator() {
            return CollectSpliterators.map(this.delegate().spliterator(), (x$0) -> {
               return MutableClassToInstanceMap.checkedEntry(x$0);
            });
         }

         public Iterator<Entry<Class<? extends B>, B>> iterator() {
            return new TransformedIterator<Entry<Class<? extends B>, B>, Entry<Class<? extends B>, B>>(this, this.delegate().iterator()) {
               Entry<Class<? extends B>, B> transform(Entry<Class<? extends B>, B> from) {
                  return MutableClassToInstanceMap.checkedEntry(from);
               }
            };
         }

         public Object[] toArray() {
            Object[] result = this.standardToArray();
            return result;
         }

         public <T> T[] toArray(T[] array) {
            return this.standardToArray(array);
         }
      };
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public B put(Class<? extends B> key, B value) {
      return super.put(key, cast(key, value));
   }

   public void putAll(Map<? extends Class<? extends B>, ? extends B> map) {
      Map<Class<? extends B>, B> copy = new LinkedHashMap(map);
      Iterator var3 = copy.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<? extends Class<? extends B>, B> entry = (Entry)var3.next();
         cast((Class)entry.getKey(), entry.getValue());
      }

      super.putAll(copy);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public <T extends B> T putInstance(Class<T> type, T value) {
      return cast(type, this.put(type, value));
   }

   @CheckForNull
   public <T extends B> T getInstance(Class<T> type) {
      return cast(type, this.get(type));
   }

   @CheckForNull
   @CanIgnoreReturnValue
   private static <B, T extends B> T cast(Class<T> type, @CheckForNull B value) {
      return Primitives.wrap(type).cast(value);
   }

   private Object writeReplace() {
      return new MutableClassToInstanceMap.SerializedForm(this.delegate());
   }

   private static final class SerializedForm<B> implements Serializable {
      private final Map<Class<? extends B>, B> backingMap;
      private static final long serialVersionUID = 0L;

      SerializedForm(Map<Class<? extends B>, B> backingMap) {
         this.backingMap = backingMap;
      }

      Object readResolve() {
         return MutableClassToInstanceMap.create(this.backingMap);
      }
   }
}
