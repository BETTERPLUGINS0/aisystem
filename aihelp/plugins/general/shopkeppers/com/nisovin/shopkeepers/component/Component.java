package com.nisovin.shopkeepers.component;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.Collections;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Component {
   @Nullable
   private ComponentHolder holder;

   @Nullable
   public final ComponentHolder getHolder() {
      return this.holder;
   }

   void setHolder(@Nullable ComponentHolder holder) {
      if (holder == null) {
         ComponentHolder previousHolder = (ComponentHolder)Unsafe.assertNonNull(this.holder);
         this.holder = holder;
         this.onRemoved(previousHolder);
      } else {
         assert this.holder == null;

         this.holder = holder;
         this.onAdded();
      }

   }

   protected void onAdded() {
   }

   protected void onRemoved(ComponentHolder holder) {
   }

   public Set<? extends Class<?>> getProvidedServices() {
      return Collections.emptySet();
   }

   void informServiceActivated(Class<?> service) {
      assert service != null;

      assert this.getClass() == service || this.getProvidedServices().contains(service);

      this.onServiceActivated(service);
   }

   void informServiceDeactivated(Class<?> service) {
      assert service != null;

      assert this.getClass() == service || this.getProvidedServices().contains(service);

      this.onServiceDeactivated(service);
   }

   protected void onServiceActivated(Class<?> service) {
   }

   protected void onServiceDeactivated(Class<?> service) {
   }

   public final int hashCode() {
      return super.hashCode();
   }

   public final boolean equals(@Nullable Object obj) {
      return super.equals(obj);
   }
}
