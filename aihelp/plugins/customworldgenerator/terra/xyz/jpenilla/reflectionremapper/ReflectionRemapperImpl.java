package xyz.jpenilla.reflectionremapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MappingTree.FieldMapping;
import net.fabricmc.mappingio.tree.MappingTree.MethodMapping;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.reflectionremapper.internal.util.StringPool;
import xyz.jpenilla.reflectionremapper.internal.util.Util;

@DefaultQualifier(NonNull.class)
final class ReflectionRemapperImpl implements ReflectionRemapper {
   private final Map<String, ReflectionRemapperImpl.ClassMapping> mappingsByObf;
   private final Map<String, ReflectionRemapperImpl.ClassMapping> mappingsByDeobf;

   private ReflectionRemapperImpl(final Set<ReflectionRemapperImpl.ClassMapping> mappings) {
      this.mappingsByObf = Collections.unmodifiableMap((Map)mappings.stream().collect(Collectors.toMap(ReflectionRemapperImpl.ClassMapping::obfName, Function.identity())));
      this.mappingsByDeobf = Collections.unmodifiableMap((Map)mappings.stream().collect(Collectors.toMap(ReflectionRemapperImpl.ClassMapping::deobfName, Function.identity())));
   }

   public String remapClassName(final String className) {
      ReflectionRemapperImpl.ClassMapping map = (ReflectionRemapperImpl.ClassMapping)this.mappingsByDeobf.get(className);
      return map == null ? className : map.obfName();
   }

   public String remapFieldName(final Class<?> holdingClass, final String fieldName) {
      ReflectionRemapperImpl.ClassMapping clsMap = (ReflectionRemapperImpl.ClassMapping)this.mappingsByObf.get(holdingClass.getName());
      return clsMap == null ? fieldName : (String)clsMap.fieldsDeobfToObf().getOrDefault(fieldName, fieldName);
   }

   public String remapMethodName(final Class<?> holdingClass, final String methodName, final Class<?>... paramTypes) {
      ReflectionRemapperImpl.ClassMapping clsMap = (ReflectionRemapperImpl.ClassMapping)this.mappingsByObf.get(holdingClass.getName());
      return clsMap == null ? methodName : (String)clsMap.methods().getOrDefault(methodKey(methodName, paramTypes), methodName);
   }

   private static String methodKey(final String deobfName, final Class<?>... paramTypes) {
      return deobfName + paramsDescriptor(paramTypes);
   }

   private static String methodKey(final String deobfName, final String obfMethodDesc) {
      return deobfName + paramsDescFromMethodDesc(obfMethodDesc);
   }

   private static String paramsDescriptor(final Class<?>... params) {
      StringBuilder builder = new StringBuilder();
      Class[] var2 = params;
      int var3 = params.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class<?> param = var2[var4];
         builder.append(Util.descriptorString(param));
      }

      return builder.toString();
   }

   private static String paramsDescFromMethodDesc(final String methodDescriptor) {
      String ret = methodDescriptor.substring(1);
      ret = ret.substring(0, ret.indexOf(")"));
      return ret;
   }

   static ReflectionRemapperImpl fromMappingTree(final MappingTree tree, final String fromNamespace, final String toNamespace) {
      StringPool pool = new StringPool();
      Set<ReflectionRemapperImpl.ClassMapping> mappings = new HashSet();
      Iterator var5 = tree.getClasses().iterator();

      while(var5.hasNext()) {
         net.fabricmc.mappingio.tree.MappingTree.ClassMapping cls = (net.fabricmc.mappingio.tree.MappingTree.ClassMapping)var5.next();
         Map<String, String> fields = new HashMap();
         Iterator var8 = cls.getFields().iterator();

         while(var8.hasNext()) {
            FieldMapping field = (FieldMapping)var8.next();
            fields.put(pool.string((String)Objects.requireNonNull(field.getName(fromNamespace))), pool.string((String)Objects.requireNonNull(field.getName(toNamespace))));
         }

         Map<String, String> methods = new HashMap();
         Iterator var12 = cls.getMethods().iterator();

         while(var12.hasNext()) {
            MethodMapping method = (MethodMapping)var12.next();
            methods.put(pool.string(methodKey((String)Objects.requireNonNull(method.getName(fromNamespace)), (String)Objects.requireNonNull(method.getDesc(toNamespace)))), pool.string((String)Objects.requireNonNull(method.getName(toNamespace))));
         }

         ReflectionRemapperImpl.ClassMapping map = new ReflectionRemapperImpl.ClassMapping(((String)Objects.requireNonNull(cls.getName(toNamespace))).replace('/', '.'), ((String)Objects.requireNonNull(cls.getName(fromNamespace))).replace('/', '.'), Collections.unmodifiableMap(fields), Collections.unmodifiableMap(methods));
         mappings.add(map);
      }

      return new ReflectionRemapperImpl(mappings);
   }

   private static final class ClassMapping {
      private final String obfName;
      private final String deobfName;
      private final Map<String, String> fieldsDeobfToObf;
      private final Map<String, String> methods;

      private ClassMapping(final String obfName, final String deobfName, final Map<String, String> fieldsDeobfToObf, final Map<String, String> methods) {
         this.obfName = obfName;
         this.deobfName = deobfName;
         this.fieldsDeobfToObf = fieldsDeobfToObf;
         this.methods = methods;
      }

      public String obfName() {
         return this.obfName;
      }

      public String deobfName() {
         return this.deobfName;
      }

      public Map<String, String> fieldsDeobfToObf() {
         return this.fieldsDeobfToObf;
      }

      public Map<String, String> methods() {
         return this.methods;
      }

      public boolean equals(final Object obj) {
         if (obj == this) {
            return true;
         } else if (obj != null && obj.getClass() == this.getClass()) {
            ReflectionRemapperImpl.ClassMapping that = (ReflectionRemapperImpl.ClassMapping)obj;
            return Objects.equals(this.obfName, that.obfName) && Objects.equals(this.deobfName, that.deobfName) && Objects.equals(this.fieldsDeobfToObf, that.fieldsDeobfToObf) && Objects.equals(this.methods, that.methods);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.obfName, this.deobfName, this.fieldsDeobfToObf, this.methods});
      }

      public String toString() {
         return "ClassMapping[obfName=" + this.obfName + ", deobfName=" + this.deobfName + ", fieldsDeobfToObf=" + this.fieldsDeobfToObf + ", methods=" + this.methods + ']';
      }

      // $FF: synthetic method
      ClassMapping(String x0, String x1, Map x2, Map x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }
}
