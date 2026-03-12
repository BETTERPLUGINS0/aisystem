package fr.xephi.authme.libs.org.jboss.security.mapping;

public class MappingResult<T> {
   private T mappedObject = null;

   public T getMappedObject() {
      return this.mappedObject;
   }

   public void setMappedObject(T mappedObject) {
      this.mappedObject = mappedObject;
   }
}
