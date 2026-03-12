package fr.xephi.authme.libs.ch.jalu.injector.context;

import fr.xephi.authme.libs.ch.jalu.injector.Injector;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import java.util.ArrayList;
import java.util.List;

public class ResolutionContext {
   private final Injector injector;
   private final ObjectIdentifier originalIdentifier;
   private ObjectIdentifier identifier;
   private List<ResolutionContext> parents = new ArrayList();

   public ResolutionContext(Injector injector, ObjectIdentifier identifier) {
      this.injector = injector;
      this.originalIdentifier = identifier;
      this.identifier = identifier;
   }

   public Injector getInjector() {
      return this.injector;
   }

   public ObjectIdentifier getOriginalIdentifier() {
      return this.originalIdentifier;
   }

   public ObjectIdentifier getIdentifier() {
      return this.identifier;
   }

   public List<ResolutionContext> getParents() {
      return this.parents;
   }

   public void setIdentifier(ObjectIdentifier identifier) {
      if (this.originalIdentifier.getTypeAsClass().isAssignableFrom(identifier.getTypeAsClass())) {
         this.identifier = identifier;
      } else {
         throw new InjectorException("New mapped class '" + identifier.getTypeAsClass() + "' is not a child of original class '" + this.originalIdentifier.getTypeAsClass() + "'");
      }
   }

   public ResolutionContext createChildContext(ObjectIdentifier identifier) {
      ResolutionContext child = new ResolutionContext(this.injector, identifier);
      child.parents.addAll(this.parents);
      child.parents.add(this);
      return child;
   }
}
