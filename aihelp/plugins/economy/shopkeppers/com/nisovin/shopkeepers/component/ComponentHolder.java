package com.nisovin.shopkeepers.component;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ComponentHolder {
   private final Map<Class<? extends Component>, Component> components = new LinkedHashMap();
   private final Collection<? extends Component> componentsView;
   private final Map<Class<?>, Component> services;

   public ComponentHolder() {
      this.componentsView = Collections.unmodifiableCollection(this.components.values());
      this.services = new HashMap();
   }

   public final Collection<? extends Component> getComponents() {
      return this.componentsView;
   }

   @Nullable
   public final <C extends Component> C get(Class<? extends C> componentClass) {
      return (Component)Unsafe.cast(this.components.get(componentClass));
   }

   public final <C extends Component> C getOrAdd(Class<? extends C> componentClass) {
      C component = (Component)Unsafe.castNonNull(this.components.computeIfAbsent(componentClass, this::createComponent));
      if (component.getHolder() == null) {
         this.onComponentAdded(component);
      }

      return component;
   }

   public final <C extends Component> void add(@NonNull C component) {
      Validate.notNull(component, (String)"component is null");
      Validate.isTrue(component.getHolder() == null, "component is already attached to some holder");
      Component previousComponent = (Component)this.components.put(component.getClass(), component);
      if (previousComponent != null) {
         this.onComponentRemoved(previousComponent);
      }

      this.onComponentAdded(component);
   }

   public final <C extends Component> C remove(Class<? extends C> componentClass) {
      C component = (Component)Unsafe.cast(this.components.remove(componentClass));
      if (component != null) {
         this.onComponentRemoved(component);
      }

      return component;
   }

   public final <C extends Component> C remove(C component) {
      Validate.notNull(component, (String)"component is null");
      Class<? extends C> componentClass = (Class)Unsafe.castNonNull(component.getClass());
      if (this.components.remove(componentClass, component)) {
         this.onComponentRemoved(component);
      }

      return component;
   }

   @NonNull
   private <C extends Component> C createComponent(Class<? extends C> componentClass) {
      Validate.notNull(componentClass, (String)"componentClass is null");

      try {
         return (Component)componentClass.getDeclaredConstructor().newInstance();
      } catch (Exception var3) {
         throw new RuntimeException("Failed to create component of type " + componentClass.getName(), var3);
      }
   }

   private void onComponentAdded(Component component) {
      assert component != null && component.getHolder() == null;

      component.setHolder(this);
      this.updateServicesOnComponentAdded(component);
   }

   private void onComponentRemoved(Component component) {
      assert component != null && component.getHolder() == this;

      this.updateServicesOnComponentRemoved(component);
      component.setHolder((ComponentHolder)null);
   }

   @Nullable
   public final <S> S getService(Class<? extends S> service) {
      return Unsafe.cast(this.services.get(service));
   }

   private void updateServicesOnComponentAdded(Component component) {
      assert component != null && component.getHolder() == this;

      this.setServiceProvider(component.getClass(), component);
      component.getProvidedServices().forEach((service) -> {
         this.setServiceProvider(service, component);
      });
   }

   private void updateServicesOnComponentRemoved(Component component) {
      assert component != null;

      this.unsetServiceProvider(component.getClass(), component);
      component.getProvidedServices().forEach((service) -> {
         this.unsetServiceProvider(service, component);
      });
   }

   private void setServiceProvider(Class<?> service, Component provider) {
      assert service != null && provider != null && provider.getHolder() == this;

      Validate.isTrue(service.isAssignableFrom(provider.getClass()), () -> {
         String var10000 = String.valueOf(provider.getClass());
         return "component of type " + var10000 + " is not assignment compatible with service " + String.valueOf(service);
      });
      Component previousProvider = (Component)this.services.put(service, provider);
      if (previousProvider != null) {
         previousProvider.informServiceDeactivated(service);
      }

      provider.onServiceActivated(service);
   }

   private void unsetServiceProvider(Class<?> service, Component provider) {
      assert service != null && provider != null;

      if (this.services.remove(service, provider)) {
         provider.informServiceDeactivated(service);
         Component newProvider = this.findServiceProvider(service, provider);
         if (newProvider != null) {
            this.services.put(service, newProvider);
            newProvider.informServiceActivated(service);
         }
      }

   }

   @Nullable
   private Component findServiceProvider(Class<?> service, @Nullable Component ignore) {
      assert service != null;

      Component provider = null;
      Iterator var4 = this.getComponents().iterator();

      while(true) {
         Component component;
         do {
            do {
               if (!var4.hasNext()) {
                  return provider;
               }

               component = (Component)var4.next();
            } while(component == ignore);
         } while(component.getClass() != service && !component.getProvidedServices().contains(service));

         provider = component;
      }
   }

   public final int hashCode() {
      return super.hashCode();
   }

   public final boolean equals(@Nullable Object obj) {
      return super.equals(obj);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append(" [components=");
      builder.append(this.components);
      builder.append("]");
      return builder.toString();
   }
}
