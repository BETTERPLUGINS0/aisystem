package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
abstract class ForwardingImmutableSet<E> {
   private ForwardingImmutableSet() {
   }
}
