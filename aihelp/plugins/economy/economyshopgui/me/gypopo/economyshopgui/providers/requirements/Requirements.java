package me.gypopo.economyshopgui.providers.requirements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class Requirements implements Collection<ItemRequirement> {
   private final Collection<ItemRequirement> requirements = new ArrayList();

   public int size() {
      return this.requirements.size();
   }

   public boolean isEmpty() {
      return this.requirements.isEmpty();
   }

   public boolean contains(Object o) {
      return this.requirements.contains(o);
   }

   @NotNull
   public Iterator<ItemRequirement> iterator() {
      return this.requirements.iterator();
   }

   @NotNull
   public Object[] toArray() {
      return this.requirements.toArray();
   }

   @NotNull
   public <T> T[] toArray(@NotNull T[] a) {
      return null;
   }

   public boolean add(ItemRequirement requirement) {
      return this.requirements.add(requirement);
   }

   public boolean remove(Object o) {
      return this.requirements.remove(o);
   }

   public boolean containsAll(@NotNull Collection<?> c) {
      return this.requirements.containsAll(c);
   }

   public boolean addAll(@NotNull Collection<? extends ItemRequirement> c) {
      return this.requirements.addAll(c);
   }

   public boolean removeAll(@NotNull Collection<?> c) {
      return this.requirements.removeAll(c);
   }

   public boolean retainAll(@NotNull Collection<?> c) {
      return this.requirements.retainAll(c);
   }

   public void clear() {
      this.requirements.clear();
   }

   public ItemRequirement getByType(RequirementType type) {
      Iterator var2 = this.requirements.iterator();

      ItemRequirement requirement;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         requirement = (ItemRequirement)var2.next();
      } while(requirement.getClass() != type.getClazz());

      return requirement;
   }

   public boolean hasType(RequirementType type) {
      Iterator var2 = this.requirements.iterator();

      ItemRequirement requirement;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         requirement = (ItemRequirement)var2.next();
      } while(requirement.getClass() != type.getClazz());

      return true;
   }

   public boolean removeType(RequirementType type) {
      Iterator var2 = this.requirements.iterator();

      ItemRequirement requirement;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         requirement = (ItemRequirement)var2.next();
      } while(requirement.getClass() != type.getClazz());

      return this.requirements.remove(requirement);
   }
}
