package ch.jalu.configme.beanmapper;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;

public class MappingContextImpl implements MappingContext {
   private final String path;
   private final TypeInformation typeInformation;
   private final ConvertErrorRecorder errorRecorder;

   protected MappingContextImpl(@NotNull String path, @NotNull TypeInformation typeInformation, @NotNull ConvertErrorRecorder errorRecorder) {
      this.path = path;
      this.typeInformation = typeInformation;
      this.errorRecorder = errorRecorder;
   }

   @NotNull
   public static MappingContextImpl createRoot(@NotNull TypeInformation typeInformation, @NotNull ConvertErrorRecorder errorRecorder) {
      return new MappingContextImpl("", typeInformation, errorRecorder);
   }

   @NotNull
   public MappingContext createChild(@NotNull String subPath, @NotNull TypeInformation typeInformation) {
      return this.path.isEmpty() ? new MappingContextImpl(subPath, typeInformation, this.errorRecorder) : new MappingContextImpl(this.path + "." + subPath, typeInformation, this.errorRecorder);
   }

   @NotNull
   public TypeInformation getTypeInformation() {
      return this.typeInformation;
   }

   @NotNull
   public String createDescription() {
      return "Path: '" + this.path + "', type: '" + this.typeInformation.getType() + "'";
   }

   public void registerError(@NotNull String reason) {
      this.errorRecorder.setHasError("At path '" + this.path + "': " + reason);
   }

   @NotNull
   public String toString() {
      return this.getClass().getSimpleName() + "[" + this.createDescription() + "]";
   }
}
