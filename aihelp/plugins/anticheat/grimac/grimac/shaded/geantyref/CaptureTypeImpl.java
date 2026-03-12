package ac.grim.grimac.shaded.geantyref;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CaptureTypeImpl implements CaptureType {
   private final WildcardType wildcard;
   private final TypeVariable<?> variable;
   private final Type[] lowerBounds;
   private Type[] upperBounds;

   CaptureTypeImpl(WildcardType wildcard, TypeVariable<?> variable) {
      this.wildcard = wildcard;
      this.variable = variable;
      this.lowerBounds = wildcard.getLowerBounds();
   }

   void init(VarMap varMap) {
      ArrayList<Type> upperBoundsList = new ArrayList(Arrays.asList(varMap.map(this.variable.getBounds())));
      List<Type> wildcardUpperBounds = Arrays.asList(this.wildcard.getUpperBounds());
      if (wildcardUpperBounds.size() > 0 && wildcardUpperBounds.get(0) == Object.class) {
         upperBoundsList.addAll(wildcardUpperBounds.subList(1, wildcardUpperBounds.size()));
      } else {
         upperBoundsList.addAll(wildcardUpperBounds);
      }

      this.upperBounds = new Type[upperBoundsList.size()];
      upperBoundsList.toArray(this.upperBounds);
   }

   public Type[] getLowerBounds() {
      return (Type[])this.lowerBounds.clone();
   }

   public Type[] getUpperBounds() {
      assert this.upperBounds != null;

      return (Type[])this.upperBounds.clone();
   }

   public void setUpperBounds(Type[] upperBounds) {
      this.upperBounds = upperBounds;
   }

   public TypeVariable<?> getTypeVariable() {
      return this.variable;
   }

   public WildcardType getWildcardType() {
      return this.wildcard;
   }

   public String toString() {
      return "capture of " + this.wildcard;
   }
}
