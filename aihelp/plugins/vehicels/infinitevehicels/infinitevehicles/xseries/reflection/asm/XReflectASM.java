package me.PM2.infinitevehicles.xseries.reflection.asm;

import com.google.common.collect.Streams;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FieldMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ClassOverloadedMethods;
import me.PM2.infinitevehicles.xseries.reflection.proxy.OverloadedMethod;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxyObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.processors.MappedType;
import me.PM2.infinitevehicles.xseries.reflection.proxy.processors.ProxyMethodInfo;
import me.PM2.infinitevehicles.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

@Internal
public final class XReflectASM<T extends ReflectiveProxyObject> extends ClassVisitor {
   private static final int JAVA_VERSION;
   private static final int ASM_VERSION;
   private static final String CONSTRUCTOR_NAME = "<init>";
   private static final String STATIC_BLOCK = "<clinit>";
   private static final String INSTANCE_FIELD = "instance";
   private static final String METHOD_HANDLE_PREFIX = "H_";
   private static final String XSERIES_ANNOTATIONS;
   private static final String GENERATED_CLASS_PACKAGE_PREFIX = "generated";
   private static final String GENERATED_CLASS_SUFFIX;
   private static final String MAGIC_ACCESSOR_IMPL;
   private static final String SUPER_CLASS;
   private static final ASMClassLoader CLASS_LOADER;
   private static final Map<Class<?>, XReflectASM<?>> PROCESSED;
   private final ClassWriter classWriter;
   private final ClassReader classReader;
   private final Class<T> templateClass;
   private final Class<?> targetClass;
   private final Type templateClassType;
   private final Type targetClassType;
   private final Type generatedClassType;
   private final String generatedClassName;
   private final String generatedClassPath;
   private Class<?> loaded;
   private byte[] bytecode;
   private final ClassOverloadedMethods<XReflectASM.ASMProxyInfo> mapped;

   private static String getGeneratedClassPath(Class<?> var0) {
      return var0.getPackage().getName() + '.' + "generated" + '.' + var0.getSimpleName() + GENERATED_CLASS_SUFFIX;
   }

   private static String descriptorProcessor(ProxyMethodInfo var0) {
      Type var1 = Type.getType(var0.rType.synthetic);
      Type[] var2 = (Type[])Arrays.stream(var0.pTypes).map((var0x) -> {
         return Type.getType(var0x.synthetic);
      }).toArray((var0x) -> {
         return new Type[var0x];
      });
      return Type.getMethodDescriptor(var1, var2);
   }

   public static <T extends ReflectiveProxyObject> XReflectASM<T> proxify(Class<T> var0) {
      XReflectASM var1 = (XReflectASM)PROCESSED.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         ReflectiveAnnotationProcessor var3 = new ReflectiveAnnotationProcessor(var0);
         var3.process(XReflectASM::descriptorProcessor);
         XReflectASM var2 = new XReflectASM(var0, var3.getTargetClass(), var3.getMapped());
         PROCESSED.put(var0, var2);
         Map var10001 = PROCESSED;
         Objects.requireNonNull(var10001);
         var3.loadDependencies(var10001::containsKey);
         var2.generate();
         return var2;
      }
   }

   @NotNull
   public T create() {
      Class var1 = this.loadClass();

      try {
         Optional var2 = Arrays.stream(var1.getDeclaredConstructors()).filter((var0) -> {
            return XAccessFlag.PUBLIC.isSet(var0.getModifiers()) && var0.getParameterCount() == 1;
         }).findFirst();
         if (!var2.isPresent()) {
            throw new IllegalStateException("Cannot find appropriate constructor for " + Arrays.toString(var1.getDeclaredConstructors()));
         } else {
            return (ReflectiveProxyObject)((Constructor)var2.get()).newInstance(null);
         }
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         throw new IllegalStateException("Couldn't initialize proxified ASM class: " + this.templateClass + " -> " + var1, var3);
      }
   }

   public void verify(boolean var1) {
      this.generate();
      PrintWriter var2 = new PrintWriter(var1 ? System.err : System.out);
      ASMAnalyzer.verify(new ClassReader(this.bytecode), XReflectASM.class.getClassLoader(), !var1, var2);
   }

   public void writeToFile(Path var1) {
      this.generate();

      try {
         Files.write(var1.resolve(this.generatedClassName + ".class"), this.bytecode, new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE});
      } catch (IOException var3) {
         throw new IllegalStateException("Cannot write generated file", var3);
      }
   }

   public XReflectASM(Class<T> var1, Class<?> var2, ClassOverloadedMethods<ProxyMethodInfo> var3) {
      super(ASM_VERSION);
      this.mapped = mapTypes(var3);

      try {
         this.classReader = new ClassReader(var1.getName());
      } catch (IOException var5) {
         throw new IllegalStateException("Unable to read class: " + var1, var5);
      }

      this.cv = this.classWriter = new ClassWriter(this.classReader, 3);
      this.templateClass = var1;
      this.templateClassType = Type.getType(var1);
      this.targetClass = var2;
      this.targetClassType = Type.getType(var2);
      this.generatedClassName = var1.getSimpleName() + GENERATED_CLASS_SUFFIX;
      this.generatedClassPath = getGeneratedClassPath(var1);
      this.generatedClassType = Type.getType('L' + this.generatedClassPath.replace('.', '/') + ';');
   }

   public void generate() {
      if (this.bytecode == null) {
         this.classReader.accept(this, 0);
         this.bytecode = this.classWriter.toByteArray();
      }
   }

   public byte[] getBytecode() {
      return this.bytecode;
   }

   private static boolean shouldRemoveAnnotation(String var0) {
      return var0.startsWith(XSERIES_ANNOTATIONS);
   }

   private static ClassOverloadedMethods<XReflectASM.ASMProxyInfo> mapTypes(ClassOverloadedMethods<ProxyMethodInfo> var0) {
      OverloadedMethod.Builder var1 = new OverloadedMethod.Builder((var0x) -> {
         return descriptorProcessor(var0x.info);
      });
      Iterator var2 = var0.mappings().entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         Collection var4 = ((OverloadedMethod)var3.getValue()).getOverloads();
         int var5 = 0;
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            ProxyMethodInfo var7 = (ProxyMethodInfo)var6.next();
            ReflectedObject var8 = (ReflectedObject)var7.handle.jvm().unreflect();
            if (!var8.accessFlags().contains(XAccessFlag.PUBLIC)) {
               String var9;
               switch(var8.type()) {
               case CONSTRUCTOR:
                  var9 = "$init$" + (var4.size() == 1 ? "" : "_" + var5++);
                  break;
               case FIELD:
                  FieldMemberHandle var10 = (FieldMemberHandle)var7.handle.unwrap();
                  var9 = var8.name() + '_' + (var10.isGetter() ? "getter" : "setter");
                  break;
               case METHOD:
                  var9 = var8.name() + (var4.size() == 1 ? "" : "_" + var5++);
                  break;
               default:
                  throw new IllegalStateException("Unexpected JVM type: " + var8);
               }

               var1.add(new XReflectASM.ASMProxyInfo(var7, var9), (String)var3.getKey());
            } else {
               var1.add(new XReflectASM.ASMProxyInfo(var7, (String)null), (String)var3.getKey());
            }
         }
      }

      return var1.build();
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      return shouldRemoveAnnotation(var1) ? null : super.visitAnnotation(var1, var2);
   }

   public AnnotationVisitor visitTypeAnnotation(int var1, TypePath var2, String var3, boolean var4) {
      return shouldRemoveAnnotation(var3) ? null : super.visitTypeAnnotation(var1, var2, var3, var4);
   }

   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
      this.classWriter.visit(JAVA_VERSION, 49, this.generatedClassType.getInternalName(), (String)null, SUPER_CLASS, new String[]{this.templateClassType.getInternalName()});
   }

   public void visitSource(String var1, String var2) {
      this.classWriter.visitSource(this.generatedClassName + ".java", (String)null);
      boolean var3 = false;
      Iterator var4 = this.mapped.mappings().values().iterator();

      while(var4.hasNext()) {
         OverloadedMethod var5 = (OverloadedMethod)var4.next();
         Iterator var6 = var5.getOverloads().iterator();

         while(var6.hasNext()) {
            XReflectASM.ASMProxyInfo var7 = (XReflectASM.ASMProxyInfo)var6.next();
            if (var7.isInaccessible()) {
               var3 = true;
               this.writeMethodHandleField(var7.methodHandleName);
            }
         }
      }

      this.writePrivateFinalField(false, "instance", this.targetClass);
      if (var3) {
         this.initStaticFields();
      }

      this.writeConstructor();
   }

   public void visitEnd() {
      this.generateGetTargetClass();
      this.generateIsInstance();
      this.generateNewArraySingleDim();
      this.generateNewArrayMultiDim();
      this.generateInstance();
      this.generateBindTo();
      this.generateEquals();
      this.generateHashCode();
      this.generateToString();
      super.visitEnd();
   }

   public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
      throw new UnsupportedOperationException("Raw fields are not supported");
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      if (!"<init>".equals(var2) && !"<clinit>".equals(var2)) {
         XReflectASM.ASMProxyInfo var6 = (XReflectASM.ASMProxyInfo)this.mapped.get(var2, () -> {
            return var3;
         }, true);
         return var6 == null ? null : new XReflectASM.MethodRewriter(var6, var1, var2, var3, var4, var5);
      } else {
         return null;
      }
   }

   private static int magicMaxs(String var0, boolean var1) {
      return Type.getArgumentsAndReturnSizes(var0) >> 2 + (var1 ? -1 : 0);
   }

   public static Type getType(String var0) {
      return Type.getType('L' + var0.replace('.', '/') + ';');
   }

   private static Type[] convert(MappedType[] var0) {
      return (Type[])Arrays.stream(var0).map((var0x) -> {
         return Type.getType(var0x.real);
      }).toArray((var0x) -> {
         return new Type[var0x];
      });
   }

   private void getInstance(MethodVisitor var1) {
      var1.visitFieldInsn(180, this.generatedClassType.getInternalName(), "instance", this.targetClassType.getDescriptor());
   }

   private void writeConstructor() {
      GeneratorAdapter var1 = this.createMethod(1, "<init>", Type.getMethodDescriptor(Type.getType(Void.TYPE), new Type[]{this.targetClassType}));
      Label var2 = new Label();
      var1.visitLabel(var2);
      var1.loadThis();
      var1.visitMethodInsn(183, SUPER_CLASS, "<init>", "()V", false);
      var1.loadThis();
      var1.loadArg(0);
      var1.putField(this.generatedClassType, "instance", this.targetClassType);
      var1.returnValue();
      Label var3 = new Label();
      var1.visitLabel(var3);
      this.visitThis(var1, var2, var3);
      var1.visitLocalVariable("instance", this.targetClassType.getDescriptor(), (String)null, var2, var3, 1);
      var1.visitMaxs(2, 2);
      var1.visitEnd();
   }

   private void writeMethodHandleField(String var1) {
      this.writePrivateFinalField(true, "H_" + var1, MethodHandle.class);
   }

   private void writePrivateFinalField(boolean var1, String var2, Class<?> var3) {
      int var4 = 18;
      if (var1) {
         var4 |= 8;
      }

      FieldVisitor var5 = this.classWriter.visitField(var4, var2, Type.getDescriptor(var3), (String)null, (Object)null);
      var5.visitEnd();
   }

   private void initStaticFields() {
      GeneratorAdapter var1 = this.createMethod(8, "<clinit>", "()V");
      var1.visitCode();
      Label var2 = new Label();
      Label var3 = new Label();
      Label var4 = new Label();
      var1.visitTryCatchBlock(var2, var3, var4, "java/lang/Throwable");
      var1.visitLabel(var2);
      int var5 = var1.newLocal(Type.getType(Class.class));
      var1.visitLdcInsn(this.targetClass.getName());
      var1.invokeStatic(Type.getType(Class.class), Method.getMethod("Class forName(String)"));
      var1.storeLocal(var5);
      Type var6 = Type.getType(ASMPrivateLookup.class);
      int var7 = var1.newLocal(var6);
      var1.newInstance(var6);
      var1.dup();
      var1.loadLocal(var5);
      var1.invokeConstructor(var6, Method.getMethod("void <init>(Class)"));
      var1.storeLocal(var7);
      Iterator var8 = this.mapped.mappings().values().iterator();

      label52:
      while(var8.hasNext()) {
         OverloadedMethod var9 = (OverloadedMethod)var8.next();
         Iterator var10 = var9.getOverloads().iterator();

         while(true) {
            XReflectASM.ASMProxyInfo var11;
            do {
               if (!var10.hasNext()) {
                  continue label52;
               }

               var11 = (XReflectASM.ASMProxyInfo)var10.next();
            } while(!var11.isInaccessible());

            ReflectedObject var12 = (ReflectedObject)var11.info.handle.jvm().unreflect();
            Label var13 = new Label();
            var1.visitLabel(var13);
            ArrayInsnGenerator var14;
            MappedType[] var15;
            int var16;
            int var17;
            MappedType var18;
            switch(var12.type()) {
            case CONSTRUCTOR:
               var1.loadLocal(var7);
               var14 = new ArrayInsnGenerator(var1, Class.class, var11.info.pTypes.length);
               var15 = var11.info.pTypes;
               var16 = var15.length;

               for(var17 = 0; var17 < var16; ++var17) {
                  var18 = var15[var17];
                  var14.add(() -> {
                     var1.push(Type.getType(var18.real));
                  });
               }

               var1.invokeVirtual(var6, Method.getMethod("java.lang.invoke.MethodHandle findConstructor(Class[])"));
               break;
            case FIELD:
               FieldMemberHandle var23 = (FieldMemberHandle)var11.info.handle.unwrap();
               var1.loadLocal(var7);
               var1.push(var12.name());
               var1.push(Type.getType(var11.info.rType.real));
               var1.push(var23.isGetter());
               var1.invokeVirtual(var6, Method.getMethod("java.lang.invoke.MethodHandle findField(String, Class, boolean)"));
               break;
            case METHOD:
               var1.loadLocal(var7);
               var1.push(var12.name());
               var1.push(Type.getType(var11.info.rType.real));
               var14 = new ArrayInsnGenerator(var1, Class.class, var11.info.pTypes.length);
               var15 = var11.info.pTypes;
               var16 = var15.length;

               for(var17 = 0; var17 < var16; ++var17) {
                  var18 = var15[var17];
                  var14.add(() -> {
                     var1.push(Type.getType(var18.real));
                  });
               }

               var1.invokeVirtual(var6, Method.getMethod("java.lang.invoke.MethodHandle findMethod(String, Class, Class[])"));
               break;
            default:
               throw new IllegalStateException("Unknown ReflectedObject type: " + var12);
            }

            var1.visitFieldInsn(179, this.generatedClassType.getInternalName(), "H_" + var11.methodHandleName, Type.getDescriptor(MethodHandle.class));
         }
      }

      var1.visitLabel(var3);
      Label var19 = new Label();
      var1.visitJumpInsn(167, var19);
      var1.visitLabel(var4);
      int var20 = var1.newLocal(Type.getType(Throwable.class));
      var1.storeLocal(var20);
      Label var21 = new Label();
      var1.visitLabel(var21);
      var1.visitTypeInsn(187, "java/lang/RuntimeException");
      var1.visitInsn(89);
      String var22 = "java/lang/StringBuilder";
      var1.visitTypeInsn(187, var22);
      var1.visitInsn(89);
      var1.visitMethodInsn(183, var22, "<init>", "()V", false);
      var1.visitLdcInsn("Failed to get inaccessible members for ");
      var1.visitMethodInsn(182, var22, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
      var1.visitLdcInsn(this.generatedClassType);
      var1.visitMethodInsn(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
      var1.visitMethodInsn(182, var22, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
      var1.visitMethodInsn(182, var22, "toString", "()Ljava/lang/String;", false);
      var1.loadLocal(var20);
      var1.visitMethodInsn(183, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
      var1.visitInsn(191);
      var1.visitLabel(var19);
      var1.visitInsn(177);
      var1.visitLocalVariable("targetClass", Type.getDescriptor(Class.class), "Ljava/lang/Class<*>;", var2, var19, var5);
      var1.visitLocalVariable("lookup", Type.getDescriptor(ASMPrivateLookup.class), (String)null, var2, var19, var7);
      var1.visitLocalVariable("ex", Type.getDescriptor(Throwable.class), (String)null, var21, var19, var20);
      var1.visitMaxs(-1, -1);
      var1.visitEnd();
   }

   private void generateInstance() {
      GeneratorAdapter var1 = this.createMethod(1, "instance", Type.getMethodDescriptor(Type.getType(Object.class), new Type[0]));
      Label var2 = new Label();
      var1.visitLabel(var2);
      var1.visitLineNumber(33, var2);
      var1.loadThis();
      this.getInstance(var1);
      var1.returnValue();
      Label var3 = new Label();
      var1.visitLabel(var3);
      this.visitThis(var1, var2, var3);
      var1.visitMaxs(1, 1);
      var1.visitEnd();
   }

   private void generateBindTo() {
      GeneratorAdapter var1 = this.createMethod(1, "bindTo", Type.getMethodDescriptor(this.templateClassType, new Type[]{Type.getType(Object.class)}));
      Label var2 = var1.newLabel();
      var1.visitLabel(var2);
      Label var3 = new Label();
      var1.loadThis();
      this.getInstance(var1);
      var1.visitJumpInsn(198, var3);
      var1.throwException(Type.getType(UnsupportedOperationException.class), "bindTo() must be called from the factory object, not on an instance");
      var1.visitLabel(var3);
      var1.newInstance(this.generatedClassType);
      var1.dup();
      var1.loadArg(0);
      var1.checkCast(this.targetClassType);
      var1.invokeConstructor(this.generatedClassType, new Method("<init>", Type.VOID_TYPE, new Type[]{this.targetClassType}));
      var1.returnValue();
      Label var4 = new Label();
      var1.visitLabel(var4);
      this.visitThis(var1, var2, var4);
      var1.visitLocalVariable("instance", this.targetClassType.getDescriptor(), (String)null, var2, var4, 1);
      var1.visitMaxs(3, 2);
      var1.visitEnd();
   }

   private void generateHashCode() {
      GeneratorAdapter var1 = this.createMethod(1, "hashCode", "()I");
      Label var2 = var1.newLabel();
      var1.visitLabel(var2);
      var1.loadThis();
      this.getInstance(var1);
      Label var3 = var1.newLabel();
      var1.ifNonNull(var3);
      var1.loadThis();
      var1.invokeVirtual(this.generatedClassType, Method.getMethod("int hashCode()"));
      var1.returnValue();
      var1.visitLabel(var3);
      var1.loadThis();
      this.getInstance(var1);
      var1.invokeVirtual(this.targetClassType, Method.getMethod("int hashCode()"));
      var1.returnValue();
      var1.visitMaxs(1, 1);
      var1.visitEnd();
   }

   private void generateEquals() {
      GeneratorAdapter var1 = this.createMethod(1, "equals", "(Ljava/lang/Object;)Z");
      Label var2 = new Label();
      var1.visitLabel(var2);
      var1.loadThis();
      var1.loadArg(0);
      Label var3 = new Label();
      var1.visitJumpInsn(166, var3);
      var1.visitInsn(4);
      var1.visitInsn(172);
      var1.visitLabel(var3);
      var1.loadThis();
      this.getInstance(var1);
      Label var4 = new Label();
      var1.visitJumpInsn(199, var4);
      var1.visitInsn(3);
      var1.visitInsn(172);
      var1.visitLabel(var4);
      var1.loadArg(0);
      Label var5 = new Label();
      var1.visitJumpInsn(199, var5);
      var1.visitInsn(3);
      var1.visitInsn(172);
      var1.visitLabel(var5);
      var1.loadArg(0);
      var1.instanceOf(this.templateClassType);
      Label var6 = new Label();
      var1.visitJumpInsn(153, var6);
      Label var7 = new Label();
      var1.visitLabel(var7);
      var1.loadThis();
      this.getInstance(var1);
      var1.loadArg(0);
      var1.checkCast(this.templateClassType);
      var1.invokeInterface(this.templateClassType, Method.getMethod("Object instance();"));
      var1.invokeVirtual(this.targetClassType, Method.getMethod("boolean equals(Object);"));
      var1.visitInsn(172);
      var1.visitLabel(var6);
      var1.loadArg(0);
      var1.instanceOf(this.targetClassType);
      Label var8 = new Label();
      var1.visitJumpInsn(153, var8);
      Label var9 = new Label();
      var1.visitLabel(var9);
      var1.loadThis();
      this.getInstance(var1);
      var1.loadArg(0);
      var1.invokeVirtual(this.targetClassType, Method.getMethod("boolean equals(Object);"));
      var1.visitInsn(172);
      var1.visitLabel(var8);
      var1.visitInsn(3);
      var1.visitInsn(172);
      Label var10 = new Label();
      var1.visitLabel(var10);
      this.visitThis(var1, var2, var10);
      var1.visitLocalVariable("obj", "Ljava/lang/Object;", (String)null, var2, var10, 1);
      var1.visitMaxs(2, 2);
      var1.visitEnd();
   }

   private void generateGetTargetClass() {
      GeneratorAdapter var1 = this.createMethod(1, "Class getTargetClass()");
      var1.push(this.targetClassType);
      var1.returnValue();
      var1.visitMaxs(1, 0);
      var1.visitEnd();
   }

   private void generateIsInstance() {
      GeneratorAdapter var1 = this.createMethod(1, "boolean isInstance(Object)");
      var1.loadArg(0);
      var1.instanceOf(this.targetClassType);
      var1.returnValue();
      var1.visitMaxs(1, 1);
      var1.visitEnd();
   }

   private void generateNewArraySingleDim() {
      GeneratorAdapter var1 = this.createMethod(1, "Object[] newArray(int)");
      Label var2 = new Label();
      var1.visitLabel(var2);
      var1.loadArg(0);
      var1.newArray(this.targetClassType);
      var1.visitInsn(176);
      Label var3 = new Label();
      var1.visitLabel(var3);
      this.visitThis(var1, var2, var3);
      var1.visitLocalVariable("length", "I", (String)null, var2, var3, 1);
      var1.visitMaxs(1, 2);
      var1.visitEnd();
   }

   private void generateNewArrayMultiDim() {
      GeneratorAdapter var1 = this.createMethod(129, "Object[] newArray(int[])");
      Label var2 = new Label();
      var1.visitLabel(var2);
      var1.loadArg(0);
      var1.arrayLength();
      Label var3 = new Label();
      Label var4 = new Label();
      Label var5 = new Label();
      Label var6 = new Label();
      var1.visitTableSwitchInsn(1, 3, var6, new Label[]{var3, var4, var5});
      var1.visitLabel(var3);
      var1.loadArg(0);
      var1.visitInsn(3);
      var1.visitInsn(46);
      var1.newArray(this.targetClassType);
      var1.visitInsn(176);
      var1.visitLabel(var4);
      var1.loadArg(0);
      var1.visitInsn(3);
      var1.visitInsn(46);
      var1.loadArg(0);
      var1.visitInsn(4);
      var1.visitInsn(46);
      var1.visitMultiANewArrayInsn("[[" + this.targetClassType.getDescriptor(), 2);
      var1.visitInsn(176);
      var1.visitLabel(var5);
      var1.loadArg(0);
      var1.visitInsn(3);
      var1.visitInsn(46);
      var1.loadArg(0);
      var1.visitInsn(4);
      var1.visitInsn(46);
      var1.loadArg(0);
      var1.visitInsn(5);
      var1.visitInsn(46);
      var1.visitMultiANewArrayInsn("[[[" + this.targetClassType.getDescriptor(), 3);
      var1.visitInsn(176);
      var1.visitLabel(var6);
      var1.push(this.targetClassType);
      var1.loadArg(0);
      var1.visitMethodInsn(184, "java/lang/reflect/Array", "newInstance", "(Ljava/lang/Class;[I)Ljava/lang/Object;", false);
      var1.checkCast(Type.getType(Object[].class));
      var1.visitInsn(176);
      Label var7 = new Label();
      var1.visitLabel(var7);
      this.visitThis(var1, var2, var7);
      var1.visitLocalVariable("dimensions", "[I", (String)null, var2, var7, 1);
      var1.visitMaxs(4, 2);
      var1.visitEnd();
   }

   private void generateToString() {
      Type var1 = Type.getType(StringBuilder.class);
      GeneratorAdapter var2 = this.createMethod(1, "String toString()");
      Label var3 = var2.newLabel();
      var2.visitLabel(var3);
      var2.newInstance(var1);
      var2.dup();
      var2.invokeConstructor(var1, Method.getMethod("void <init>()"));
      var2.loadThis();
      var2.invokeVirtual(Type.getType(Object.class), Method.getMethod("Class getClass()"));
      var2.invokeVirtual(Type.getType(Class.class), Method.getMethod("String getSimpleName()"));
      var2.invokeVirtual(var1, Method.getMethod("StringBuilder append(String)"));
      var2.push("(instance=");
      var2.invokeVirtual(var1, Method.getMethod("StringBuilder append(String)"));
      var2.loadThis();
      this.getInstance(var2);
      var2.invokeVirtual(var1, Method.getMethod("StringBuilder append(Object)"));
      var2.push(41);
      var2.invokeVirtual(var1, Method.getMethod("StringBuilder append(char)"));
      var2.invokeVirtual(var1, Method.getMethod("String toString()"));
      var2.returnValue();
      Label var4 = new Label();
      var2.visitLabel(var4);
      this.visitThis(var2, var3, var4);
      var2.visitMaxs(2, 1);
      var2.visitEnd();
   }

   private GeneratorAdapter createMethod(int var1, String var2) {
      Method var3 = Method.getMethod(var2);
      return this.createMethod(var1, var3.getName(), var3.getDescriptor());
   }

   private GeneratorAdapter createMethod(int var1, @Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)|(<init>)|(<clinit>)") String var2, String var3) {
      GeneratorAdapter var4 = new GeneratorAdapter(this.classWriter.visitMethod(var1, var2, var3, (String)null, (String[])null), var1, var2, var3);
      var4.visitCode();
      return var4;
   }

   private void visitThis(MethodVisitor var1, Label var2, Label var3) {
      var1.visitLocalVariable("this", this.generatedClassType.getDescriptor(), (String)null, var2, var3, 0);
   }

   @NotNull
   public Class<?> loadClass() {
      if (this.loaded != null) {
         return this.loaded;
      } else {
         this.generate();
         this.verify(true);
         return this.loaded = CLASS_LOADER.defineClass(this.generatedClassPath, this.bytecode);
      }
   }

   static {
      JAVA_VERSION = ASMVersion.USED_JAVA_FILE_FORMAT;
      ASM_VERSION = ASMVersion.USED_ASM_OPCODE_VERSION;
      XSERIES_ANNOTATIONS = 'L' + "me.PM2.infinitevehicles.xseries.reflection.proxy.annotations".replace('.', '/');
      GENERATED_CLASS_SUFFIX = "_XSeriesGen_" + ASM_VERSION + '_' + JAVA_VERSION;

      String var0;
      try {
         Class.forName("sun.reflect.MagicAccessorImpl");
         var0 = "sun/reflect/MagicAccessorImpl";
      } catch (ClassNotFoundException var5) {
         try {
            Class.forName("jdk.internal.reflect.MagicAccessorImpl");
            var0 = "jdk/internal/reflect/MagicAccessorImpl";
         } catch (ClassNotFoundException var4) {
            IllegalStateException var3 = new IllegalStateException("Cannot find MagicAccessorImpl class");
            var3.addSuppressed(var5);
            var3.addSuppressed(var4);
            throw var3;
         }
      }

      MAGIC_ACCESSOR_IMPL = var0;
      SUPER_CLASS = Type.getInternalName(Object.class);
      CLASS_LOADER = new ASMClassLoader();
      PROCESSED = new IdentityHashMap();
   }

   private static final class ASMProxyInfo {
      private final ProxyMethodInfo info;
      private final String methodHandleName;

      private ASMProxyInfo(ProxyMethodInfo var1, String var2) {
         this.info = var1;
         this.methodHandleName = var2;
      }

      private boolean isInaccessible() {
         return this.methodHandleName != null;
      }

      // $FF: synthetic method
      ASMProxyInfo(ProxyMethodInfo var1, String var2, Object var3) {
         this(var1, var2);
      }
   }

   private final class MethodRewriter extends MethodVisitor {
      private final XReflectASM.ASMProxyInfo handle;
      private final GeneratorAdapter adapter;
      private final String descriptor;

      MethodRewriter(XReflectASM.ASMProxyInfo param2, int param3, String param4, String param5, String param6, String[] param7) {
         super(XReflectASM.ASM_VERSION, XReflectASM.super.visitMethod(XAccessFlag.ABSTRACT.remove(var3), var4, var5, var6, var7));
         this.handle = var2;
         this.adapter = new GeneratorAdapter(this.mv, var3, var4, var5);
         this.descriptor = var5;
         this.generateCode();
      }

      private void generateCode() {
         this.adapter.visitCode();
         boolean var2 = XReflectASM.this.targetClass.isInterface();

         ReflectedObject.Type var1;
         boolean var3;
         String var4;
         try {
            ReflectedObject var5 = (ReflectedObject)this.handle.info.handle.jvm().reflect();
            var1 = var5.type();
            var4 = var5.name();
            var3 = var5.accessFlags().contains(XAccessFlag.STATIC);
         } catch (ReflectiveOperationException var16) {
            throw new IllegalStateException(var16);
         }

         Label var17 = new Label();
         this.adapter.visitLabel(var17);
         if (var1 == ReflectedObject.Type.CONSTRUCTOR) {
            this.adapter.loadThis();
            XReflectASM.this.getInstance(this.adapter);
            Label var6 = new Label();
            this.adapter.ifNull(var6);
            this.adapter.throwException(Type.getType(UnsupportedOperationException.class), "Constructor method must be called from the factory object, not on an instance");
            this.adapter.visitLabel(var6);
         }

         MappedType var9 = this.handle.info.rType;
         Type var7;
         boolean var8;
         Type var18;
         if (var9.isDifferent()) {
            if (var9.synthetic.isAssignableFrom(var9.real)) {
               var7 = null;
               var18 = null;
               var8 = false;
            } else {
               if (!ReflectiveProxyObject.class.isAssignableFrom(var9.synthetic)) {
                  throw new VerifyError("Cannot convert return type " + var9.synthetic + " to " + var9.real + " in proxy method " + this.handle.info.interfaceMethod);
               }

               var8 = true;
               var7 = Type.getType(var9.real);
               var18 = XReflectASM.getType(XReflectASM.getGeneratedClassPath(var9.synthetic));
               this.adapter.newInstance(var18);
               this.adapter.dup();
            }
         } else {
            var7 = null;
            var18 = null;
            var8 = false;
         }

         if (this.handle.isInaccessible()) {
            this.adapter.getStatic(XReflectASM.this.generatedClassType, "H_" + this.handle.methodHandleName, Type.getType(MethodHandle.class));
         }

         if (var1 == ReflectedObject.Type.CONSTRUCTOR) {
            if (!this.handle.isInaccessible()) {
               this.adapter.newInstance(XReflectASM.this.targetClassType);
               this.adapter.dup();
            }
         } else if (!var3) {
            this.adapter.loadThis();
            XReflectASM.this.getInstance(this.adapter);
         }

         int var10 = 1;
         Type[] var11 = this.adapter.getArgumentTypes();

         for(int var12 = 0; var12 < var11.length; ++var12) {
            Type var13 = var11[var12];
            MappedType var14 = this.handle.info.pTypes[var12];
            if (var14.isDifferent()) {
               if (!ReflectiveProxyObject.class.isAssignableFrom(var14.synthetic)) {
                  throw new VerifyError("Cannot convert parameter type " + var14.synthetic + " to " + var14.real + " in proxy method " + this.handle.info.interfaceMethod);
               }

               this.adapter.visitVarInsn(var13.getOpcode(21), var10);
               this.adapter.invokeInterface(Type.getType(ReflectiveProxyObject.class), Method.getMethod("Object instance()"));
               this.adapter.checkCast(Type.getType(var14.real));
            } else {
               this.adapter.visitVarInsn(var13.getOpcode(21), var10);
            }

            var10 += var13.getSize();
         }

         String var19 = "invokeExact";
         switch(var1) {
         case CONSTRUCTOR:
            if (this.handle.isInaccessible()) {
               this.adapter.invokeVirtual(Type.getType(MethodHandle.class), new Method(var19, XReflectASM.this.targetClassType, XReflectASM.convert(this.handle.info.pTypes)));
            } else {
               this.adapter.visitMethodInsn(183, XReflectASM.this.targetClassType.getInternalName(), "<init>", Type.getMethodDescriptor(Type.getType(Void.TYPE), var11), false);
            }
            break;
         case FIELD:
            boolean var20 = var11.length != 0;
            Type var22;
            if (var11.length != 0) {
               var22 = this.adapter.getArgumentTypes()[0];
            } else {
               var22 = this.adapter.getReturnType();
            }

            if (this.handle.isInaccessible()) {
               ArrayList var15 = new ArrayList(3);
               if (!var3) {
                  var15.add(XReflectASM.this.targetClassType);
               }

               if (var20) {
                  var15.add(Type.getType(this.handle.info.pTypes[0].real));
               }

               this.adapter.invokeVirtual(Type.getType(MethodHandle.class), new Method(var19, Type.getType(this.handle.info.rType.real), (Type[])var15.toArray(new Type[0])));
            } else {
               short var24;
               if (var20) {
                  if (var3) {
                     var24 = 179;
                  } else {
                     var24 = 181;
                  }
               } else if (var3) {
                  var24 = 178;
               } else {
                  var24 = 180;
               }

               this.adapter.visitFieldInsn(var24, XReflectASM.this.targetClassType.getInternalName(), var4, var22.getDescriptor());
            }
            break;
         case METHOD:
            if (this.handle.isInaccessible()) {
               this.adapter.invokeVirtual(Type.getType(MethodHandle.class), new Method(var19, Type.getType(this.handle.info.rType.real), (Type[])Streams.concat(new Stream[]{var3 ? Stream.of() : Stream.of(XReflectASM.this.targetClassType), Arrays.stream(this.handle.info.pTypes).map((var0) -> {
                  return Type.getType(var0.real);
               })}).toArray((var0) -> {
                  return new Type[var0];
               })));
            } else {
               this.adapter.visitMethodInsn(var3 ? 184 : (var2 ? 185 : 182), XReflectASM.this.targetClassType.getInternalName(), var4, Type.getMethodDescriptor(Type.getType(var9.real), XReflectASM.convert(this.handle.info.pTypes)), var2);
            }
            break;
         default:
            throw new IllegalStateException("Unknown ReflectedObject type: " + var1);
         }

         if (var8) {
            this.adapter.invokeConstructor(var18, new Method("<init>", Type.VOID_TYPE, new Type[]{var7}));
         }

         this.adapter.returnValue();
         Label var21 = new Label();
         this.adapter.visitLabel(var21);
         if (!var3 && var1 != ReflectedObject.Type.CONSTRUCTOR) {
            XReflectASM.this.visitThis(this.adapter, var17, var21);
         }

         int var23 = XReflectASM.magicMaxs(this.descriptor, var3);
         this.adapter.visitMaxs(var23, var23);
         this.adapter.visitEnd();
      }

      public void visitCode() {
      }

      public AnnotationVisitor visitParameterAnnotation(int var1, String var2, boolean var3) {
         return XReflectASM.shouldRemoveAnnotation(var2) ? null : super.visitParameterAnnotation(var1, var2, var3);
      }

      public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
         return XReflectASM.shouldRemoveAnnotation(var1) ? null : super.visitAnnotation(var1, var2);
      }

      public AnnotationVisitor visitTypeAnnotation(int var1, TypePath var2, String var3, boolean var4) {
         return XReflectASM.shouldRemoveAnnotation(var3) ? null : super.visitTypeAnnotation(var1, var2, var3, var4);
      }
   }
}
