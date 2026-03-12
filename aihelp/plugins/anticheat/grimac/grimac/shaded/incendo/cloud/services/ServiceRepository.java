package ac.grim.grimac.shaded.incendo.cloud.services;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.services.annotation.Order;
import ac.grim.grimac.shaded.incendo.cloud.services.type.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServiceRepository<Context, Response> {
   private final Object lock = new Object();
   private final TypeToken<? extends Service<Context, Response>> serviceType;
   private final List<ServiceRepository<Context, Response>.ServiceWrapper<? extends Service<Context, Response>>> implementations;
   private int registrationOrder = 0;

   ServiceRepository(@NonNull final TypeToken<? extends Service<Context, Response>> serviceType) {
      this.serviceType = serviceType;
      this.implementations = new LinkedList();
   }

   <T extends Service<Context, Response>> void registerImplementation(@NonNull final T service, @NonNull final Collection<Predicate<Context>> filters) {
      synchronized(this.lock) {
         this.implementations.add(new ServiceRepository.ServiceWrapper(service, filters));
      }
   }

   @NonNull
   LinkedList<ServiceRepository<Context, Response>.ServiceWrapper<? extends Service<Context, Response>>> queue() {
      synchronized(this.lock) {
         return new LinkedList(this.implementations);
      }
   }

   final class ServiceWrapper<T extends Service<Context, Response>> implements Comparable<ServiceRepository<Context, Response>.ServiceWrapper<T>> {
      private final boolean defaultImplementation;
      private final T implementation;
      private final Collection<Predicate<Context>> filters;
      private final int registrationOrder;
      private final ExecutionOrder executionOrder;

      private ServiceWrapper(@NonNull final T implementation, @NonNull final Collection<Predicate<Context>> filters) {
         this.registrationOrder = ServiceRepository.this.registrationOrder++;
         this.defaultImplementation = ServiceRepository.this.implementations.isEmpty();
         this.implementation = implementation;
         this.filters = filters;
         ExecutionOrder executionOrder = implementation.order();
         if (executionOrder == null) {
            Order order = (Order)implementation.getClass().getAnnotation(Order.class);
            if (order != null) {
               executionOrder = order.value();
            } else {
               executionOrder = ExecutionOrder.SOON;
            }
         }

         this.executionOrder = executionOrder;
      }

      @NonNull
      T implementation() {
         return this.implementation;
      }

      @NonNull
      Collection<Predicate<Context>> filters() {
         return Collections.unmodifiableCollection(this.filters);
      }

      boolean isDefaultImplementation() {
         return this.defaultImplementation;
      }

      public String toString() {
         return String.format("ServiceWrapper{type=%s,implementation=%s}", ServiceRepository.this.serviceType.getType().getTypeName(), TypeToken.get(this.implementation.getClass()).getType().getTypeName());
      }

      public int compareTo(final ServiceRepository<Context, Response>.ServiceWrapper<T> other) {
         return Comparator.comparingInt((wrapper) -> {
            return wrapper.isDefaultImplementation() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
         }).thenComparingInt((wrapper) -> {
            return wrapper.executionOrder.ordinal();
         }).thenComparingInt((wrapper) -> {
            return wrapper.registrationOrder;
         }).compare(this, other);
      }

      // $FF: synthetic method
      ServiceWrapper(Service x1, Collection x2, Object x3) {
         this(x1, x2);
      }
   }
}
