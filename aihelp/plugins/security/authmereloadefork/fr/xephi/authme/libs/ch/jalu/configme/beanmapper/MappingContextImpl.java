package fr.xephi.authme.libs.ch.jalu.configme.beanmapper;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;

public class MappingContextImpl implements MappingContext {
   private final String path;
   private final TypeInformation typeInformation;
   private final ConvertErrorRecorder errorRecorder;

   protected MappingContextImpl(String path, TypeInformation typeInformation, ConvertErrorRecorder errorRecorder) {
      this.path = path;
      this.typeInformation = typeInformation;
      this.errorRecorder = errorRecorder;
   }

   public static MappingContextImpl createRoot(TypeInformation typeInformation, ConvertErrorRecorder errorRecorder) {
      return new MappingContextImpl("", typeInformation, errorRecorder);
   }

   public MappingContext createChild(String subPath, TypeInformation typeInformation) {
      return this.path.isEmpty() ? new MappingContextImpl(subPath, typeInformation, this.errorRecorder) : new MappingContextImpl(this.path + "." + subPath, typeInformation, this.errorRecorder);
   }

   public TypeInformation getTypeInformation() {
      return this.typeInformation;
   }

   public String createDescription() {
      return "Path: '" + this.path + "', type: '" + this.typeInformation.getType() + "'";
   }

   public void registerError(String reason) {
      this.errorRecorder.setHasError("At path '" + this.path + "': " + reason);
   }

   public String toString() {
      return this.getClass().getSimpleName() + "[" + this.createDescription() + "]";
   }
}
