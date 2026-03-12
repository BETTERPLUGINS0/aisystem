package ac.grim.grimac.shaded.geantyref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class AnnotatedCaptureTypeImpl extends AnnotatedTypeImpl implements AnnotatedCaptureType {
   private final AnnotatedWildcardType wildcard;
   private final AnnotatedTypeVariable variable;
   private final AnnotatedType[] lowerBounds;
   private AnnotatedType[] upperBounds;
   private final CaptureType type;
   private final Annotation[] declaredAnnotations;

   AnnotatedCaptureTypeImpl(AnnotatedWildcardType wildcard, AnnotatedTypeVariable variable) {
      this(new CaptureTypeImpl((WildcardType)wildcard.getType(), (TypeVariable)variable.getType()), wildcard, variable);
   }

   AnnotatedCaptureTypeImpl(CaptureType type, AnnotatedWildcardType wildcard, AnnotatedTypeVariable variable) {
      this(type, wildcard, variable, (AnnotatedType[])null, (Annotation[])null);
   }

   AnnotatedCaptureTypeImpl(CaptureType type, AnnotatedWildcardType wildcard, AnnotatedTypeVariable variable, AnnotatedType[] upperBounds, Annotation[] annotations) {
      this(type, wildcard, variable, wildcard.getAnnotatedLowerBounds(), upperBounds, annotations);
   }

   AnnotatedCaptureTypeImpl(CaptureType type, AnnotatedWildcardType wildcard, AnnotatedTypeVariable variable, AnnotatedType[] lowerBounds, AnnotatedType[] upperBounds, Annotation[] annotations) {
      super(type, annotations != null ? annotations : (Annotation[])Stream.concat(Arrays.stream(wildcard.getAnnotations()), Arrays.stream(variable.getAnnotations())).toArray((x$0) -> {
         return new Annotation[x$0];
      }));
      this.type = type;
      this.wildcard = wildcard;
      this.variable = variable;
      this.lowerBounds = lowerBounds;
      this.upperBounds = upperBounds;
      this.declaredAnnotations = (Annotation[])Stream.concat(Arrays.stream(wildcard.getDeclaredAnnotations()), Arrays.stream(variable.getDeclaredAnnotations())).toArray((x$0) -> {
         return new Annotation[x$0];
      });
   }

   void init(VarMap varMap) {
      ArrayList<AnnotatedType> upperBoundsList = new ArrayList(Arrays.asList(varMap.map(this.variable.getAnnotatedBounds())));
      List<AnnotatedType> wildcardUpperBounds = Arrays.asList(this.wildcard.getAnnotatedUpperBounds());
      if (!wildcardUpperBounds.isEmpty() && ((AnnotatedType)wildcardUpperBounds.get(0)).getType() == Object.class) {
         upperBoundsList.addAll(wildcardUpperBounds.subList(1, wildcardUpperBounds.size()));
      } else {
         upperBoundsList.addAll(wildcardUpperBounds);
      }

      this.upperBounds = new AnnotatedType[upperBoundsList.size()];
      upperBoundsList.toArray(this.upperBounds);
      ((CaptureTypeImpl)this.type).init(varMap);
   }

   AnnotatedCaptureTypeImpl setAnnotations(Annotation[] annotations) {
      this.annotations = this.toMap(annotations);
      return this;
   }

   public Annotation[] getDeclaredAnnotations() {
      return this.declaredAnnotations;
   }

   public AnnotatedType[] getAnnotatedUpperBounds() {
      assert this.upperBounds != null;

      return (AnnotatedType[])this.upperBounds.clone();
   }

   public void setAnnotatedUpperBounds(AnnotatedType[] upperBounds) {
      this.upperBounds = upperBounds;
      this.type.setUpperBounds((Type[])Arrays.stream(upperBounds).map(AnnotatedType::getType).toArray((x$0) -> {
         return new Type[x$0];
      }));
   }

   public AnnotatedType[] getAnnotatedLowerBounds() {
      return (AnnotatedType[])this.lowerBounds.clone();
   }

   public AnnotatedTypeVariable getAnnotatedTypeVariable() {
      return this.variable;
   }

   public AnnotatedWildcardType getAnnotatedWildcardType() {
      return this.wildcard;
   }
}
