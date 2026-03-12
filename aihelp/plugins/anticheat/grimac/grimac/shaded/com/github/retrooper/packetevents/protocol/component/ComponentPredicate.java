package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class ComponentPredicate implements Predicate<IComponentMap> {
   private List<ComponentValue<?>> requiredComponents;

   public ComponentPredicate() {
      this(new ArrayList());
   }

   public ComponentPredicate(List<ComponentValue<?>> requiredComponents) {
      this.requiredComponents = requiredComponents;
   }

   public static ComponentPredicate read(PacketWrapper<?> wrapper) {
      List<ComponentValue<?>> components = wrapper.readList(ComponentValue::read);
      return new ComponentPredicate(components);
   }

   public static void write(PacketWrapper<?> wrapper, ComponentPredicate predicate) {
      wrapper.writeList(predicate.requiredComponents, ComponentValue::write);
   }

   public static ComponentPredicate emptyPredicate() {
      return new ComponentPredicate(new ArrayList());
   }

   public static ComponentPredicate fromPatches(PatchableComponentMap components) {
      Map<ComponentType<?>, Optional<?>> patches = components.getPatches();
      List<ComponentValue<?>> values = new ArrayList(patches.size());
      Iterator var3 = patches.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ComponentType<?>, Optional<?>> patch = (Entry)var3.next();
         if (((Optional)patch.getValue()).isPresent()) {
            values.add(new ComponentValue((ComponentType)patch.getKey(), ((Optional)patch.getValue()).get()));
         }
      }

      return new ComponentPredicate(values);
   }

   public PatchableComponentMap asPatches(StaticComponentMap base) {
      PatchableComponentMap patched = new PatchableComponentMap(base);
      Iterator var3 = this.requiredComponents.iterator();

      while(var3.hasNext()) {
         ComponentValue<?> component = (ComponentValue)var3.next();
         patched.set(component);
      }

      return patched;
   }

   public boolean test(IComponentMap components) {
      Iterator var2 = this.requiredComponents.iterator();

      ComponentValue component;
      Optional value;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         component = (ComponentValue)var2.next();
         value = components.getOptional(component.getType());
      } while(value.isPresent() && component.getValue().equals(value.get()));

      return false;
   }

   public List<ComponentValue<?>> getRequiredComponents() {
      return this.requiredComponents;
   }

   public void setRequiredComponents(List<ComponentValue<?>> requiredComponents) {
      this.requiredComponents = requiredComponents;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ComponentPredicate)) {
         return false;
      } else {
         ComponentPredicate that = (ComponentPredicate)obj;
         return this.requiredComponents.equals(that.requiredComponents);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.requiredComponents);
   }

   public String toString() {
      return "ComponentPredicate{requiredComponents=" + this.requiredComponents + '}';
   }
}
