package fr.xephi.authme.libs.ch.jalu.injector.context;

import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ObjectIdentifier {
   private final ResolutionType resolutionType;
   private final Type type;
   private final List<Annotation> annotations;

   public ObjectIdentifier(ResolutionType resolutionType, Type type, Annotation... annotations) {
      this.resolutionType = resolutionType;
      this.type = type;
      this.annotations = Arrays.asList(annotations);
   }

   public ResolutionType getResolutionType() {
      return this.resolutionType;
   }

   public Type getType() {
      return this.type;
   }

   public Class<?> getTypeAsClass() {
      if (this.type instanceof Class) {
         return (Class)this.type;
      } else if (this.type instanceof ParameterizedType) {
         Type rawType = ((ParameterizedType)this.type).getRawType();
         if (rawType instanceof Class) {
            return (Class)rawType;
         } else {
            throw new InjectorException("Parameterized type '" + this.type + "' does not have a Class as its raw type");
         }
      } else {
         throw new InjectorException("Unknown Type '" + this.type + "' (" + this.type.getClass() + ") cannot be converted to Class");
      }
   }

   public List<Annotation> getAnnotations() {
      return this.annotations;
   }

   public String toString() {
      return "ObjId[type=" + this.type + ", annotations=" + this.annotations + "]";
   }
}
