/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Pattern
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.AnnotationVisitor
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.FieldVisitor
 *  org.objectweb.asm.Label
 *  org.objectweb.asm.MethodVisitor
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.TypePath
 *  org.objectweb.asm.commons.GeneratorAdapter
 *  org.objectweb.asm.commons.Method
 */
package me.zombie_striker.qav.util.xseries.reflection.asm;

import com.google.common.collect.Streams;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.asm.ASMAnalyzer;
import me.zombie_striker.qav.util.xseries.reflection.asm.ASMClassLoader;
import me.zombie_striker.qav.util.xseries.reflection.asm.ASMPrivateLookup;
import me.zombie_striker.qav.util.xseries.reflection.asm.ASMVersion;
import me.zombie_striker.qav.util.xseries.reflection.asm.ArrayInsnGenerator;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FieldMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ClassOverloadedMethods;
import me.zombie_striker.qav.util.xseries.reflection.proxy.OverloadedMethod;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.MappedType;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ProxyMethodInfo;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
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

@ApiStatus.Internal
public final class XReflectASM<T extends ReflectiveProxyObject>
extends ClassVisitor {
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
    private final ClassOverloadedMethods<ASMProxyInfo> mapped;

    private static String getGeneratedClassPath(Class<?> clazz) {
        return clazz.getPackage().getName() + '.' + GENERATED_CLASS_PACKAGE_PREFIX + '.' + clazz.getSimpleName() + GENERATED_CLASS_SUFFIX;
    }

    private static String descriptorProcessor(ProxyMethodInfo proxyMethodInfo) {
        Type type = Type.getType(proxyMethodInfo.rType.synthetic);
        Type[] typeArray = (Type[])Arrays.stream(proxyMethodInfo.pTypes).map(mappedType -> Type.getType(mappedType.synthetic)).toArray(Type[]::new);
        return Type.getMethodDescriptor((Type)type, (Type[])typeArray);
    }

    public static <T extends ReflectiveProxyObject> XReflectASM<T> proxify(Class<T> clazz) {
        Object object = PROCESSED.get(clazz);
        if (object != null) {
            return object;
        }
        object = new ReflectiveAnnotationProcessor(clazz);
        ((ReflectiveAnnotationProcessor)object).process(XReflectASM::descriptorProcessor);
        XReflectASM<T> xReflectASM = new XReflectASM<T>(clazz, ((ReflectiveAnnotationProcessor)object).getTargetClass(), ((ReflectiveAnnotationProcessor)object).getMapped());
        PROCESSED.put(clazz, xReflectASM);
        ((ReflectiveAnnotationProcessor)object).loadDependencies(PROCESSED::containsKey);
        xReflectASM.generate();
        return xReflectASM;
    }

    @NotNull
    public T create() {
        Class<?> clazz = this.loadClass();
        try {
            Optional<Constructor> optional = Arrays.stream(clazz.getDeclaredConstructors()).filter(constructor -> XAccessFlag.PUBLIC.isSet(constructor.getModifiers()) && constructor.getParameterCount() == 1).findFirst();
            if (!optional.isPresent()) {
                throw new IllegalStateException("Cannot find appropriate constructor for " + Arrays.toString(clazz.getDeclaredConstructors()));
            }
            return (T)((ReflectiveProxyObject)optional.get().newInstance(new Object[]{null}));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException reflectiveOperationException) {
            throw new IllegalStateException("Couldn't initialize proxified ASM class: " + this.templateClass + " -> " + clazz, reflectiveOperationException);
        }
    }

    public void verify(boolean bl) {
        this.generate();
        PrintWriter printWriter = new PrintWriter(bl ? System.err : System.out);
        ASMAnalyzer.verify(new ClassReader(this.bytecode), XReflectASM.class.getClassLoader(), !bl, printWriter);
    }

    public void writeToFile(Path path) {
        this.generate();
        try {
            Files.write(path.resolve(this.generatedClassName + ".class"), this.bytecode, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException iOException) {
            throw new IllegalStateException("Cannot write generated file", iOException);
        }
    }

    public XReflectASM(Class<T> clazz, Class<?> clazz2, ClassOverloadedMethods<ProxyMethodInfo> classOverloadedMethods) {
        super(ASM_VERSION);
        this.mapped = XReflectASM.mapTypes(classOverloadedMethods);
        try {
            this.classReader = new ClassReader(clazz.getName());
        } catch (IOException iOException) {
            throw new IllegalStateException("Unable to read class: " + clazz, iOException);
        }
        this.classWriter = new ClassWriter(this.classReader, 3);
        this.cv = this.classWriter;
        this.templateClass = clazz;
        this.templateClassType = Type.getType(clazz);
        this.targetClass = clazz2;
        this.targetClassType = Type.getType(clazz2);
        this.generatedClassName = clazz.getSimpleName() + GENERATED_CLASS_SUFFIX;
        this.generatedClassPath = XReflectASM.getGeneratedClassPath(clazz);
        this.generatedClassType = Type.getType((String)('L' + this.generatedClassPath.replace('.', '/') + ';'));
    }

    public void generate() {
        if (this.bytecode != null) {
            return;
        }
        this.classReader.accept((ClassVisitor)this, 0);
        this.bytecode = this.classWriter.toByteArray();
    }

    public byte[] getBytecode() {
        return this.bytecode;
    }

    private static boolean shouldRemoveAnnotation(String string) {
        return string.startsWith(XSERIES_ANNOTATIONS);
    }

    private static ClassOverloadedMethods<ASMProxyInfo> mapTypes(ClassOverloadedMethods<ProxyMethodInfo> classOverloadedMethods) {
        OverloadedMethod.Builder<ASMProxyInfo> builder = new OverloadedMethod.Builder<ASMProxyInfo>(aSMProxyInfo -> XReflectASM.descriptorProcessor(((ASMProxyInfo)aSMProxyInfo).info));
        for (Map.Entry<String, OverloadedMethod<ProxyMethodInfo>> entry : classOverloadedMethods.mappings().entrySet()) {
            Collection<ProxyMethodInfo> collection = entry.getValue().getOverloads();
            int n = 0;
            for (ProxyMethodInfo proxyMethodInfo : collection) {
                ReflectedObject reflectedObject = proxyMethodInfo.handle.jvm().unreflect();
                if (!reflectedObject.accessFlags().contains((Object)XAccessFlag.PUBLIC)) {
                    String string;
                    switch (reflectedObject.type()) {
                        case CONSTRUCTOR: {
                            string = "$init$" + (collection.size() == 1 ? "" : "_" + n++);
                            break;
                        }
                        case FIELD: {
                            FieldMemberHandle fieldMemberHandle = (FieldMemberHandle)proxyMethodInfo.handle.unwrap();
                            string = reflectedObject.name() + '_' + (fieldMemberHandle.isGetter() ? "getter" : "setter");
                            break;
                        }
                        case METHOD: {
                            string = reflectedObject.name() + (collection.size() == 1 ? "" : "_" + n++);
                            break;
                        }
                        default: {
                            throw new IllegalStateException("Unexpected JVM type: " + reflectedObject);
                        }
                    }
                    builder.add(new ASMProxyInfo(proxyMethodInfo, string), entry.getKey());
                    continue;
                }
                builder.add(new ASMProxyInfo(proxyMethodInfo, null), entry.getKey());
            }
        }
        return builder.build();
    }

    public AnnotationVisitor visitAnnotation(String string, boolean bl) {
        if (XReflectASM.shouldRemoveAnnotation(string)) {
            return null;
        }
        return super.visitAnnotation(string, bl);
    }

    public AnnotationVisitor visitTypeAnnotation(int n, TypePath typePath, String string, boolean bl) {
        if (XReflectASM.shouldRemoveAnnotation(string)) {
            return null;
        }
        return super.visitTypeAnnotation(n, typePath, string, bl);
    }

    public void visit(int n, int n2, String string, String string2, String string3, String[] stringArray) {
        this.classWriter.visit(JAVA_VERSION, 49, this.generatedClassType.getInternalName(), null, SUPER_CLASS, new String[]{this.templateClassType.getInternalName()});
    }

    public void visitSource(String string, String string2) {
        this.classWriter.visitSource(this.generatedClassName + ".java", null);
        boolean bl = false;
        for (OverloadedMethod<ASMProxyInfo> overloadedMethod : this.mapped.mappings().values()) {
            for (ASMProxyInfo aSMProxyInfo : overloadedMethod.getOverloads()) {
                if (!aSMProxyInfo.isInaccessible()) continue;
                bl = true;
                this.writeMethodHandleField(aSMProxyInfo.methodHandleName);
            }
        }
        this.writePrivateFinalField(false, INSTANCE_FIELD, this.targetClass);
        if (bl) {
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

    public FieldVisitor visitField(int n, String string, String string2, String string3, Object object) {
        throw new UnsupportedOperationException("Raw fields are not supported");
    }

    public MethodVisitor visitMethod(int n, String string, String string2, String string3, String[] stringArray) {
        if (CONSTRUCTOR_NAME.equals(string) || STATIC_BLOCK.equals(string)) {
            return null;
        }
        ASMProxyInfo aSMProxyInfo = this.mapped.get(string, () -> string2, true);
        if (aSMProxyInfo == null) {
            return null;
        }
        return new MethodRewriter(aSMProxyInfo, n, string, string2, string3, stringArray);
    }

    private static int magicMaxs(String string, boolean bl) {
        return Type.getArgumentsAndReturnSizes((String)string) >> 2 + (bl ? -1 : 0);
    }

    public static Type getType(String string) {
        return Type.getType((String)('L' + string.replace('.', '/') + ';'));
    }

    private static Type[] convert(MappedType[] mappedTypeArray) {
        return (Type[])Arrays.stream(mappedTypeArray).map(mappedType -> Type.getType(mappedType.real)).toArray(Type[]::new);
    }

    private void getInstance(MethodVisitor methodVisitor) {
        methodVisitor.visitFieldInsn(180, this.generatedClassType.getInternalName(), INSTANCE_FIELD, this.targetClassType.getDescriptor());
    }

    private void writeConstructor() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, CONSTRUCTOR_NAME, Type.getMethodDescriptor((Type)Type.getType(Void.TYPE), (Type[])new Type[]{this.targetClassType}));
        Label label = new Label();
        generatorAdapter.visitLabel(label);
        generatorAdapter.loadThis();
        generatorAdapter.visitMethodInsn(183, SUPER_CLASS, CONSTRUCTOR_NAME, "()V", false);
        generatorAdapter.loadThis();
        generatorAdapter.loadArg(0);
        generatorAdapter.putField(this.generatedClassType, INSTANCE_FIELD, this.targetClassType);
        generatorAdapter.returnValue();
        Label label2 = new Label();
        generatorAdapter.visitLabel(label2);
        this.visitThis((MethodVisitor)generatorAdapter, label, label2);
        generatorAdapter.visitLocalVariable(INSTANCE_FIELD, this.targetClassType.getDescriptor(), null, label, label2, 1);
        generatorAdapter.visitMaxs(2, 2);
        generatorAdapter.visitEnd();
    }

    private void writeMethodHandleField(String string) {
        this.writePrivateFinalField(true, METHOD_HANDLE_PREFIX + string, MethodHandle.class);
    }

    private void writePrivateFinalField(boolean bl, String string, Class<?> clazz) {
        int n = 18;
        if (bl) {
            n |= 8;
        }
        FieldVisitor fieldVisitor = this.classWriter.visitField(n, string, Type.getDescriptor(clazz), null, null);
        fieldVisitor.visitEnd();
    }

    private void initStaticFields() {
        GeneratorAdapter generatorAdapter = this.createMethod(8, STATIC_BLOCK, "()V");
        generatorAdapter.visitCode();
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        generatorAdapter.visitTryCatchBlock(label, label2, label3, "java/lang/Throwable");
        generatorAdapter.visitLabel(label);
        int n = generatorAdapter.newLocal(Type.getType(Class.class));
        generatorAdapter.visitLdcInsn((Object)this.targetClass.getName());
        generatorAdapter.invokeStatic(Type.getType(Class.class), Method.getMethod((String)"Class forName(String)"));
        generatorAdapter.storeLocal(n);
        Type type = Type.getType(ASMPrivateLookup.class);
        int n2 = generatorAdapter.newLocal(type);
        generatorAdapter.newInstance(type);
        generatorAdapter.dup();
        generatorAdapter.loadLocal(n);
        generatorAdapter.invokeConstructor(type, Method.getMethod((String)"void <init>(Class)"));
        generatorAdapter.storeLocal(n2);
        for (OverloadedMethod<ASMProxyInfo> overloadedMethod : this.mapped.mappings().values()) {
            for (ASMProxyInfo object2 : overloadedMethod.getOverloads()) {
                if (!object2.isInaccessible()) continue;
                ReflectedObject reflectedObject = ((ASMProxyInfo)object2).info.handle.jvm().unreflect();
                Label label4 = new Label();
                generatorAdapter.visitLabel(label4);
                switch (reflectedObject.type()) {
                    case CONSTRUCTOR: {
                        generatorAdapter.loadLocal(n2);
                        Object object = new ArrayInsnGenerator(generatorAdapter, Class.class, ((ASMProxyInfo)object2).info.pTypes.length);
                        for (MappedType mappedType : ((ASMProxyInfo)object2).info.pTypes) {
                            ((ArrayInsnGenerator)object).add(() -> generatorAdapter.push(Type.getType(mappedType.real)));
                        }
                        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"java.lang.invoke.MethodHandle findConstructor(Class[])"));
                        break;
                    }
                    case FIELD: {
                        Object object = (FieldMemberHandle)((ASMProxyInfo)object2).info.handle.unwrap();
                        generatorAdapter.loadLocal(n2);
                        generatorAdapter.push(reflectedObject.name());
                        generatorAdapter.push(Type.getType(((ASMProxyInfo)object2).info.rType.real));
                        generatorAdapter.push(((FieldMemberHandle)object).isGetter());
                        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"java.lang.invoke.MethodHandle findField(String, Class, boolean)"));
                        break;
                    }
                    case METHOD: {
                        generatorAdapter.loadLocal(n2);
                        generatorAdapter.push(reflectedObject.name());
                        generatorAdapter.push(Type.getType(((ASMProxyInfo)object2).info.rType.real));
                        Object object = new ArrayInsnGenerator(generatorAdapter, Class.class, ((ASMProxyInfo)object2).info.pTypes.length);
                        for (MappedType mappedType : ((ASMProxyInfo)object2).info.pTypes) {
                            ((ArrayInsnGenerator)object).add(() -> generatorAdapter.push(Type.getType(mappedType.real)));
                        }
                        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"java.lang.invoke.MethodHandle findMethod(String, Class, Class[])"));
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown ReflectedObject type: " + reflectedObject);
                    }
                }
                generatorAdapter.visitFieldInsn(179, this.generatedClassType.getInternalName(), METHOD_HANDLE_PREFIX + object2.methodHandleName, Type.getDescriptor(MethodHandle.class));
            }
        }
        generatorAdapter.visitLabel(label2);
        Label label5 = new Label();
        generatorAdapter.visitJumpInsn(167, label5);
        generatorAdapter.visitLabel(label3);
        int n3 = generatorAdapter.newLocal(Type.getType(Throwable.class));
        generatorAdapter.storeLocal(n3);
        Label label6 = new Label();
        generatorAdapter.visitLabel(label6);
        generatorAdapter.visitTypeInsn(187, "java/lang/RuntimeException");
        generatorAdapter.visitInsn(89);
        String string = "java/lang/StringBuilder";
        generatorAdapter.visitTypeInsn(187, string);
        generatorAdapter.visitInsn(89);
        generatorAdapter.visitMethodInsn(183, string, CONSTRUCTOR_NAME, "()V", false);
        generatorAdapter.visitLdcInsn((Object)"Failed to get inaccessible members for ");
        generatorAdapter.visitMethodInsn(182, string, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        generatorAdapter.visitLdcInsn((Object)this.generatedClassType);
        generatorAdapter.visitMethodInsn(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
        generatorAdapter.visitMethodInsn(182, string, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        generatorAdapter.visitMethodInsn(182, string, "toString", "()Ljava/lang/String;", false);
        generatorAdapter.loadLocal(n3);
        generatorAdapter.visitMethodInsn(183, "java/lang/RuntimeException", CONSTRUCTOR_NAME, "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
        generatorAdapter.visitInsn(191);
        generatorAdapter.visitLabel(label5);
        generatorAdapter.visitInsn(177);
        generatorAdapter.visitLocalVariable("targetClass", Type.getDescriptor(Class.class), "Ljava/lang/Class<*>;", label, label5, n);
        generatorAdapter.visitLocalVariable("lookup", Type.getDescriptor(ASMPrivateLookup.class), null, label, label5, n2);
        generatorAdapter.visitLocalVariable("ex", Type.getDescriptor(Throwable.class), null, label6, label5, n3);
        generatorAdapter.visitMaxs(-1, -1);
        generatorAdapter.visitEnd();
    }

    private void generateInstance() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, INSTANCE_FIELD, Type.getMethodDescriptor((Type)Type.getType(Object.class), (Type[])new Type[0]));
        Label label = new Label();
        generatorAdapter.visitLabel(label);
        generatorAdapter.visitLineNumber(33, label);
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        generatorAdapter.returnValue();
        Label label2 = new Label();
        generatorAdapter.visitLabel(label2);
        this.visitThis((MethodVisitor)generatorAdapter, label, label2);
        generatorAdapter.visitMaxs(1, 1);
        generatorAdapter.visitEnd();
    }

    private void generateBindTo() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, "bindTo", Type.getMethodDescriptor((Type)this.templateClassType, (Type[])new Type[]{Type.getType(Object.class)}));
        Label label = generatorAdapter.newLabel();
        generatorAdapter.visitLabel(label);
        Label label2 = new Label();
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        generatorAdapter.visitJumpInsn(198, label2);
        generatorAdapter.throwException(Type.getType(UnsupportedOperationException.class), "bindTo() must be called from the factory object, not on an instance");
        generatorAdapter.visitLabel(label2);
        generatorAdapter.newInstance(this.generatedClassType);
        generatorAdapter.dup();
        generatorAdapter.loadArg(0);
        generatorAdapter.checkCast(this.targetClassType);
        generatorAdapter.invokeConstructor(this.generatedClassType, new Method(CONSTRUCTOR_NAME, Type.VOID_TYPE, new Type[]{this.targetClassType}));
        generatorAdapter.returnValue();
        Label label3 = new Label();
        generatorAdapter.visitLabel(label3);
        this.visitThis((MethodVisitor)generatorAdapter, label, label3);
        generatorAdapter.visitLocalVariable(INSTANCE_FIELD, this.targetClassType.getDescriptor(), null, label, label3, 1);
        generatorAdapter.visitMaxs(3, 2);
        generatorAdapter.visitEnd();
    }

    private void generateHashCode() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, "hashCode", "()I");
        Label label = generatorAdapter.newLabel();
        generatorAdapter.visitLabel(label);
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        Label label2 = generatorAdapter.newLabel();
        generatorAdapter.ifNonNull(label2);
        generatorAdapter.loadThis();
        generatorAdapter.invokeVirtual(this.generatedClassType, Method.getMethod((String)"int hashCode()"));
        generatorAdapter.returnValue();
        generatorAdapter.visitLabel(label2);
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        generatorAdapter.invokeVirtual(this.targetClassType, Method.getMethod((String)"int hashCode()"));
        generatorAdapter.returnValue();
        generatorAdapter.visitMaxs(1, 1);
        generatorAdapter.visitEnd();
    }

    private void generateEquals() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, "equals", "(Ljava/lang/Object;)Z");
        Label label = new Label();
        generatorAdapter.visitLabel(label);
        generatorAdapter.loadThis();
        generatorAdapter.loadArg(0);
        Label label2 = new Label();
        generatorAdapter.visitJumpInsn(166, label2);
        generatorAdapter.visitInsn(4);
        generatorAdapter.visitInsn(172);
        generatorAdapter.visitLabel(label2);
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        Label label3 = new Label();
        generatorAdapter.visitJumpInsn(199, label3);
        generatorAdapter.visitInsn(3);
        generatorAdapter.visitInsn(172);
        generatorAdapter.visitLabel(label3);
        generatorAdapter.loadArg(0);
        Label label4 = new Label();
        generatorAdapter.visitJumpInsn(199, label4);
        generatorAdapter.visitInsn(3);
        generatorAdapter.visitInsn(172);
        generatorAdapter.visitLabel(label4);
        generatorAdapter.loadArg(0);
        generatorAdapter.instanceOf(this.templateClassType);
        Label label5 = new Label();
        generatorAdapter.visitJumpInsn(153, label5);
        Label label6 = new Label();
        generatorAdapter.visitLabel(label6);
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        generatorAdapter.loadArg(0);
        generatorAdapter.checkCast(this.templateClassType);
        generatorAdapter.invokeInterface(this.templateClassType, Method.getMethod((String)"Object instance();"));
        generatorAdapter.invokeVirtual(this.targetClassType, Method.getMethod((String)"boolean equals(Object);"));
        generatorAdapter.visitInsn(172);
        generatorAdapter.visitLabel(label5);
        generatorAdapter.loadArg(0);
        generatorAdapter.instanceOf(this.targetClassType);
        Label label7 = new Label();
        generatorAdapter.visitJumpInsn(153, label7);
        Label label8 = new Label();
        generatorAdapter.visitLabel(label8);
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        generatorAdapter.loadArg(0);
        generatorAdapter.invokeVirtual(this.targetClassType, Method.getMethod((String)"boolean equals(Object);"));
        generatorAdapter.visitInsn(172);
        generatorAdapter.visitLabel(label7);
        generatorAdapter.visitInsn(3);
        generatorAdapter.visitInsn(172);
        Label label9 = new Label();
        generatorAdapter.visitLabel(label9);
        this.visitThis((MethodVisitor)generatorAdapter, label, label9);
        generatorAdapter.visitLocalVariable("obj", "Ljava/lang/Object;", null, label, label9, 1);
        generatorAdapter.visitMaxs(2, 2);
        generatorAdapter.visitEnd();
    }

    private void generateGetTargetClass() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, "Class getTargetClass()");
        generatorAdapter.push(this.targetClassType);
        generatorAdapter.returnValue();
        generatorAdapter.visitMaxs(1, 0);
        generatorAdapter.visitEnd();
    }

    private void generateIsInstance() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, "boolean isInstance(Object)");
        generatorAdapter.loadArg(0);
        generatorAdapter.instanceOf(this.targetClassType);
        generatorAdapter.returnValue();
        generatorAdapter.visitMaxs(1, 1);
        generatorAdapter.visitEnd();
    }

    private void generateNewArraySingleDim() {
        GeneratorAdapter generatorAdapter = this.createMethod(1, "Object[] newArray(int)");
        Label label = new Label();
        generatorAdapter.visitLabel(label);
        generatorAdapter.loadArg(0);
        generatorAdapter.newArray(this.targetClassType);
        generatorAdapter.visitInsn(176);
        Label label2 = new Label();
        generatorAdapter.visitLabel(label2);
        this.visitThis((MethodVisitor)generatorAdapter, label, label2);
        generatorAdapter.visitLocalVariable("length", "I", null, label, label2, 1);
        generatorAdapter.visitMaxs(1, 2);
        generatorAdapter.visitEnd();
    }

    private void generateNewArrayMultiDim() {
        GeneratorAdapter generatorAdapter = this.createMethod(129, "Object[] newArray(int[])");
        Label label = new Label();
        generatorAdapter.visitLabel(label);
        generatorAdapter.loadArg(0);
        generatorAdapter.arrayLength();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        Label label5 = new Label();
        generatorAdapter.visitTableSwitchInsn(1, 3, label5, new Label[]{label2, label3, label4});
        generatorAdapter.visitLabel(label2);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitInsn(3);
        generatorAdapter.visitInsn(46);
        generatorAdapter.newArray(this.targetClassType);
        generatorAdapter.visitInsn(176);
        generatorAdapter.visitLabel(label3);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitInsn(3);
        generatorAdapter.visitInsn(46);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitInsn(4);
        generatorAdapter.visitInsn(46);
        generatorAdapter.visitMultiANewArrayInsn("[[" + this.targetClassType.getDescriptor(), 2);
        generatorAdapter.visitInsn(176);
        generatorAdapter.visitLabel(label4);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitInsn(3);
        generatorAdapter.visitInsn(46);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitInsn(4);
        generatorAdapter.visitInsn(46);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitInsn(5);
        generatorAdapter.visitInsn(46);
        generatorAdapter.visitMultiANewArrayInsn("[[[" + this.targetClassType.getDescriptor(), 3);
        generatorAdapter.visitInsn(176);
        generatorAdapter.visitLabel(label5);
        generatorAdapter.push(this.targetClassType);
        generatorAdapter.loadArg(0);
        generatorAdapter.visitMethodInsn(184, "java/lang/reflect/Array", "newInstance", "(Ljava/lang/Class;[I)Ljava/lang/Object;", false);
        generatorAdapter.checkCast(Type.getType(Object[].class));
        generatorAdapter.visitInsn(176);
        Label label6 = new Label();
        generatorAdapter.visitLabel(label6);
        this.visitThis((MethodVisitor)generatorAdapter, label, label6);
        generatorAdapter.visitLocalVariable("dimensions", "[I", null, label, label6, 1);
        generatorAdapter.visitMaxs(4, 2);
        generatorAdapter.visitEnd();
    }

    private void generateToString() {
        Type type = Type.getType(StringBuilder.class);
        GeneratorAdapter generatorAdapter = this.createMethod(1, "String toString()");
        Label label = generatorAdapter.newLabel();
        generatorAdapter.visitLabel(label);
        generatorAdapter.newInstance(type);
        generatorAdapter.dup();
        generatorAdapter.invokeConstructor(type, Method.getMethod((String)"void <init>()"));
        generatorAdapter.loadThis();
        generatorAdapter.invokeVirtual(Type.getType(Object.class), Method.getMethod((String)"Class getClass()"));
        generatorAdapter.invokeVirtual(Type.getType(Class.class), Method.getMethod((String)"String getSimpleName()"));
        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"StringBuilder append(String)"));
        generatorAdapter.push("(instance=");
        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"StringBuilder append(String)"));
        generatorAdapter.loadThis();
        this.getInstance((MethodVisitor)generatorAdapter);
        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"StringBuilder append(Object)"));
        generatorAdapter.push(41);
        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"StringBuilder append(char)"));
        generatorAdapter.invokeVirtual(type, Method.getMethod((String)"String toString()"));
        generatorAdapter.returnValue();
        Label label2 = new Label();
        generatorAdapter.visitLabel(label2);
        this.visitThis((MethodVisitor)generatorAdapter, label, label2);
        generatorAdapter.visitMaxs(2, 1);
        generatorAdapter.visitEnd();
    }

    private GeneratorAdapter createMethod(int n, String string) {
        Method method = Method.getMethod((String)string);
        return this.createMethod(n, method.getName(), method.getDescriptor());
    }

    private GeneratorAdapter createMethod(int n, @Pattern(value="(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)|(<init>)|(<clinit>)") String string, String string2) {
        GeneratorAdapter generatorAdapter = new GeneratorAdapter(this.classWriter.visitMethod(n, string, string2, null, null), n, string, string2);
        generatorAdapter.visitCode();
        return generatorAdapter;
    }

    private void visitThis(MethodVisitor methodVisitor, Label label, Label label2) {
        methodVisitor.visitLocalVariable("this", this.generatedClassType.getDescriptor(), null, label, label2, 0);
    }

    @NotNull
    public Class<?> loadClass() {
        if (this.loaded != null) {
            return this.loaded;
        }
        this.generate();
        this.verify(true);
        this.loaded = CLASS_LOADER.defineClass(this.generatedClassPath, this.bytecode);
        return this.loaded;
    }

    static {
        String string;
        JAVA_VERSION = ASMVersion.USED_JAVA_FILE_FORMAT;
        ASM_VERSION = ASMVersion.USED_ASM_OPCODE_VERSION;
        XSERIES_ANNOTATIONS = 'L' + "me.zombie_striker.qav.util.xseries.reflection.proxy.annotations".replace('.', '/');
        GENERATED_CLASS_SUFFIX = "_XSeriesGen_" + ASM_VERSION + '_' + JAVA_VERSION;
        try {
            Class.forName("sun.reflect.MagicAccessorImpl");
            string = "sun/reflect/MagicAccessorImpl";
        } catch (ClassNotFoundException classNotFoundException) {
            try {
                Class.forName("jdk.internal.reflect.MagicAccessorImpl");
                string = "jdk/internal/reflect/MagicAccessorImpl";
            } catch (ClassNotFoundException classNotFoundException2) {
                IllegalStateException illegalStateException = new IllegalStateException("Cannot find MagicAccessorImpl class");
                illegalStateException.addSuppressed(classNotFoundException);
                illegalStateException.addSuppressed(classNotFoundException2);
                throw illegalStateException;
            }
        }
        MAGIC_ACCESSOR_IMPL = string;
        SUPER_CLASS = Type.getInternalName(Object.class);
        CLASS_LOADER = new ASMClassLoader();
        PROCESSED = new IdentityHashMap();
    }

    private static final class ASMProxyInfo {
        private final ProxyMethodInfo info;
        private final String methodHandleName;

        private ASMProxyInfo(ProxyMethodInfo proxyMethodInfo, String string) {
            this.info = proxyMethodInfo;
            this.methodHandleName = string;
        }

        private boolean isInaccessible() {
            return this.methodHandleName != null;
        }
    }

    private final class MethodRewriter
    extends MethodVisitor {
        private final ASMProxyInfo handle;
        private final GeneratorAdapter adapter;
        private final String descriptor;

        MethodRewriter(ASMProxyInfo aSMProxyInfo, int n, String string, String string2, String string3, String[] stringArray) {
            super(ASM_VERSION, XReflectASM.super.visitMethod(XAccessFlag.ABSTRACT.remove(n), string, string2, string3, stringArray));
            this.handle = aSMProxyInfo;
            this.adapter = new GeneratorAdapter(this.mv, n, string, string2);
            this.descriptor = string2;
            this.generateCode();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private void generateCode() {
            MappedType mappedType2;
            boolean bl;
            Type type;
            MappedType mappedType3;
            Label label;
            boolean bl2;
            String string;
            ReflectedObject.Type type2;
            ReflectedObject reflectedObject;
            this.adapter.visitCode();
            boolean bl3 = XReflectASM.this.targetClass.isInterface();
            try {
                reflectedObject = ((ASMProxyInfo)this.handle).info.handle.jvm().reflect();
                type2 = reflectedObject.type();
                string = reflectedObject.name();
                bl2 = reflectedObject.accessFlags().contains((Object)XAccessFlag.STATIC);
            } catch (ReflectiveOperationException reflectiveOperationException) {
                throw new IllegalStateException(reflectiveOperationException);
            }
            reflectedObject = new Label();
            this.adapter.visitLabel((Label)reflectedObject);
            if (type2 == ReflectedObject.Type.CONSTRUCTOR) {
                this.adapter.loadThis();
                XReflectASM.this.getInstance((MethodVisitor)this.adapter);
                label = new Label();
                this.adapter.ifNull(label);
                this.adapter.throwException(Type.getType(UnsupportedOperationException.class), "Constructor method must be called from the factory object, not on an instance");
                this.adapter.visitLabel(label);
            }
            if ((mappedType3 = ((ASMProxyInfo)this.handle).info.rType).isDifferent()) {
                if (mappedType3.synthetic.isAssignableFrom(mappedType3.real)) {
                    type = null;
                    label = null;
                    bl = false;
                } else {
                    if (!ReflectiveProxyObject.class.isAssignableFrom(mappedType3.synthetic)) throw new VerifyError("Cannot convert return type " + mappedType3.synthetic + " to " + mappedType3.real + " in proxy method " + ((ASMProxyInfo)this.handle).info.interfaceMethod);
                    bl = true;
                    type = Type.getType(mappedType3.real);
                    label = XReflectASM.getType(XReflectASM.getGeneratedClassPath(mappedType3.synthetic));
                    this.adapter.newInstance((Type)label);
                    this.adapter.dup();
                }
            } else {
                type = null;
                label = null;
                bl = false;
            }
            if (this.handle.isInaccessible()) {
                this.adapter.getStatic(XReflectASM.this.generatedClassType, XReflectASM.METHOD_HANDLE_PREFIX + this.handle.methodHandleName, Type.getType(MethodHandle.class));
            }
            if (type2 == ReflectedObject.Type.CONSTRUCTOR) {
                if (!this.handle.isInaccessible()) {
                    this.adapter.newInstance(XReflectASM.this.targetClassType);
                    this.adapter.dup();
                }
            } else if (!bl2) {
                this.adapter.loadThis();
                XReflectASM.this.getInstance((MethodVisitor)this.adapter);
            }
            int n = 1;
            Type[] typeArray = this.adapter.getArgumentTypes();
            for (int i = 0; i < typeArray.length; ++i) {
                Type type3 = typeArray[i];
                mappedType2 = ((ASMProxyInfo)this.handle).info.pTypes[i];
                if (mappedType2.isDifferent()) {
                    if (!ReflectiveProxyObject.class.isAssignableFrom(mappedType2.synthetic)) {
                        throw new VerifyError("Cannot convert parameter type " + mappedType2.synthetic + " to " + mappedType2.real + " in proxy method " + ((ASMProxyInfo)this.handle).info.interfaceMethod);
                    }
                    this.adapter.visitVarInsn(type3.getOpcode(21), n);
                    this.adapter.invokeInterface(Type.getType(ReflectiveProxyObject.class), Method.getMethod((String)"Object instance()"));
                    this.adapter.checkCast(Type.getType(mappedType2.real));
                } else {
                    this.adapter.visitVarInsn(type3.getOpcode(21), n);
                }
                n += type3.getSize();
            }
            String string2 = "invokeExact";
            switch (type2) {
                case METHOD: {
                    if (this.handle.isInaccessible()) {
                        this.adapter.invokeVirtual(Type.getType(MethodHandle.class), new Method(string2, Type.getType(((ASMProxyInfo)this.handle).info.rType.real), (Type[])Streams.concat(bl2 ? Stream.of(new Type[0]) : Stream.of(XReflectASM.this.targetClassType), Arrays.stream(((ASMProxyInfo)this.handle).info.pTypes).map(mappedType -> Type.getType(mappedType.real))).toArray(Type[]::new)));
                        break;
                    }
                    this.adapter.visitMethodInsn(bl2 ? 184 : (bl3 ? 185 : 182), XReflectASM.this.targetClassType.getInternalName(), string, Type.getMethodDescriptor((Type)Type.getType(mappedType3.real), (Type[])XReflectASM.convert(((ASMProxyInfo)this.handle).info.pTypes)), bl3);
                    break;
                }
                case FIELD: {
                    boolean bl4 = typeArray.length != 0;
                    mappedType2 = typeArray.length != 0 ? this.adapter.getArgumentTypes()[0] : this.adapter.getReturnType();
                    if (this.handle.isInaccessible()) {
                        ArrayList<Type> arrayList = new ArrayList<Type>(3);
                        if (!bl2) {
                            arrayList.add(XReflectASM.this.targetClassType);
                        }
                        if (bl4) {
                            arrayList.add(Type.getType(((ASMProxyInfo)this.handle).info.pTypes[0].real));
                        }
                        this.adapter.invokeVirtual(Type.getType(MethodHandle.class), new Method(string2, Type.getType(((ASMProxyInfo)this.handle).info.rType.real), arrayList.toArray(new Type[0])));
                        break;
                    }
                    int n2 = bl4 ? (bl2 ? 179 : 181) : (bl2 ? 178 : 180);
                    this.adapter.visitFieldInsn(n2, XReflectASM.this.targetClassType.getInternalName(), string, mappedType2.getDescriptor());
                    break;
                }
                case CONSTRUCTOR: {
                    if (this.handle.isInaccessible()) {
                        this.adapter.invokeVirtual(Type.getType(MethodHandle.class), new Method(string2, XReflectASM.this.targetClassType, XReflectASM.convert(((ASMProxyInfo)this.handle).info.pTypes)));
                        break;
                    }
                    this.adapter.visitMethodInsn(183, XReflectASM.this.targetClassType.getInternalName(), XReflectASM.CONSTRUCTOR_NAME, Type.getMethodDescriptor((Type)Type.getType(Void.TYPE), (Type[])typeArray), false);
                    break;
                }
                default: {
                    throw new IllegalStateException("Unknown ReflectedObject type: " + (Object)((Object)type2));
                }
            }
            if (bl) {
                this.adapter.invokeConstructor((Type)label, new Method(XReflectASM.CONSTRUCTOR_NAME, Type.VOID_TYPE, new Type[]{type}));
            }
            this.adapter.returnValue();
            Label label2 = new Label();
            this.adapter.visitLabel(label2);
            if (!bl2 && type2 != ReflectedObject.Type.CONSTRUCTOR) {
                XReflectASM.this.visitThis((MethodVisitor)this.adapter, (Label)reflectedObject, label2);
            }
            int n3 = XReflectASM.magicMaxs(this.descriptor, bl2);
            this.adapter.visitMaxs(n3, n3);
            this.adapter.visitEnd();
        }

        public void visitCode() {
        }

        public AnnotationVisitor visitParameterAnnotation(int n, String string, boolean bl) {
            if (XReflectASM.shouldRemoveAnnotation(string)) {
                return null;
            }
            return super.visitParameterAnnotation(n, string, bl);
        }

        public AnnotationVisitor visitAnnotation(String string, boolean bl) {
            if (XReflectASM.shouldRemoveAnnotation(string)) {
                return null;
            }
            return super.visitAnnotation(string, bl);
        }

        public AnnotationVisitor visitTypeAnnotation(int n, TypePath typePath, String string, boolean bl) {
            if (XReflectASM.shouldRemoveAnnotation(string)) {
                return null;
            }
            return super.visitTypeAnnotation(n, typePath, string, bl);
        }
    }
}

