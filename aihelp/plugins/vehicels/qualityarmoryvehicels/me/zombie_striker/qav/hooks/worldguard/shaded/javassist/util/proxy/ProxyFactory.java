/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.lang.invoke.MethodHandles;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.DuplicateMemberException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.FactoryHelper;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.MethodFilter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.MethodHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.Proxy;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyObject;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.RuntimeSupport;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.SecurityActions;

public class ProxyFactory {
    private Class<?> superClass = null;
    private Class<?>[] interfaces = null;
    private MethodFilter methodFilter = null;
    private MethodHandler handler = null;
    private List<Map.Entry<String, Method>> signatureMethods = null;
    private boolean hasGetHandler = false;
    private byte[] signature = null;
    private String classname;
    private String basename;
    private String superName;
    private Class<?> thisClass = null;
    private String genericSignature = null;
    private boolean factoryUseCache = useCache;
    private boolean factoryWriteReplace = useWriteReplace;
    public static boolean onlyPublicMethods = false;
    public String writeDirectory = null;
    private static final Class<?> OBJECT_TYPE = Object.class;
    private static final String HOLDER = "_methods_";
    private static final String HOLDER_TYPE = "[Ljava/lang/reflect/Method;";
    private static final String FILTER_SIGNATURE_FIELD = "_filter_signature";
    private static final String FILTER_SIGNATURE_TYPE = "[B";
    private static final String HANDLER = "handler";
    private static final String NULL_INTERCEPTOR_HOLDER = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.RuntimeSupport";
    private static final String DEFAULT_INTERCEPTOR = "default_interceptor";
    private static final String HANDLER_TYPE = 'L' + MethodHandler.class.getName().replace('.', '/') + ';';
    private static final String HANDLER_SETTER = "setHandler";
    private static final String HANDLER_SETTER_TYPE = "(" + HANDLER_TYPE + ")V";
    private static final String HANDLER_GETTER = "getHandler";
    private static final String HANDLER_GETTER_TYPE = "()" + HANDLER_TYPE;
    private static final String SERIAL_VERSION_UID_FIELD = "serialVersionUID";
    private static final String SERIAL_VERSION_UID_TYPE = "J";
    private static final long SERIAL_VERSION_UID_VALUE = -1L;
    public static volatile boolean useCache = true;
    public static volatile boolean useWriteReplace = true;
    private static Map<ClassLoader, Map<String, ProxyDetails>> proxyCache = new WeakHashMap<ClassLoader, Map<String, ProxyDetails>>();
    private static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider(){

        @Override
        public ClassLoader get(ProxyFactory proxyFactory) {
            return proxyFactory.getClassLoader0();
        }
    };
    public static UniqueName nameGenerator = new UniqueName(){
        private final String sep = "_$$_jvst" + Integer.toHexString(this.hashCode() & 0xFFF) + "_";
        private int counter = 0;

        @Override
        public String get(String string) {
            return string + this.sep + Integer.toHexString(this.counter++);
        }
    };
    private static final String packageForJavaBase = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.";
    private static Comparator<Map.Entry<String, Method>> sorter = new Comparator<Map.Entry<String, Method>>(){

        @Override
        public int compare(Map.Entry<String, Method> entry, Map.Entry<String, Method> entry2) {
            return entry.getKey().compareTo(entry2.getKey());
        }
    };
    private static final String HANDLER_GETTER_KEY = "getHandler:()";

    public boolean isUseCache() {
        return this.factoryUseCache;
    }

    public void setUseCache(boolean bl) {
        if (this.handler != null && bl) {
            throw new RuntimeException("caching cannot be enabled if the factory default interceptor has been set");
        }
        this.factoryUseCache = bl;
    }

    public boolean isUseWriteReplace() {
        return this.factoryWriteReplace;
    }

    public void setUseWriteReplace(boolean bl) {
        this.factoryWriteReplace = bl;
    }

    public static boolean isProxyClass(Class<?> clazz) {
        return Proxy.class.isAssignableFrom(clazz);
    }

    public void setSuperclass(Class<?> clazz) {
        this.superClass = clazz;
        this.signature = null;
    }

    public Class<?> getSuperclass() {
        return this.superClass;
    }

    public void setInterfaces(Class<?>[] classArray) {
        this.interfaces = classArray;
        this.signature = null;
    }

    public Class<?>[] getInterfaces() {
        return this.interfaces;
    }

    public void setFilter(MethodFilter methodFilter) {
        this.methodFilter = methodFilter;
        this.signature = null;
    }

    public void setGenericSignature(String string) {
        this.genericSignature = string;
    }

    public Class<?> createClass() {
        if (this.signature == null) {
            this.computeSignature(this.methodFilter);
        }
        return this.createClass1(null);
    }

    public Class<?> createClass(MethodFilter methodFilter) {
        this.computeSignature(methodFilter);
        return this.createClass1(null);
    }

    Class<?> createClass(byte[] byArray) {
        this.installSignature(byArray);
        return this.createClass1(null);
    }

    public Class<?> createClass(MethodHandles.Lookup lookup) {
        if (this.signature == null) {
            this.computeSignature(this.methodFilter);
        }
        return this.createClass1(lookup);
    }

    public Class<?> createClass(MethodHandles.Lookup lookup, MethodFilter methodFilter) {
        this.computeSignature(methodFilter);
        return this.createClass1(lookup);
    }

    Class<?> createClass(MethodHandles.Lookup lookup, byte[] byArray) {
        this.installSignature(byArray);
        return this.createClass1(lookup);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Class<?> createClass1(MethodHandles.Lookup lookup) {
        Class<?> clazz = this.thisClass;
        if (clazz == null) {
            ClassLoader classLoader = this.getClassLoader();
            Map<ClassLoader, Map<String, ProxyDetails>> map = proxyCache;
            synchronized (map) {
                if (this.factoryUseCache) {
                    this.createClass2(classLoader, lookup);
                } else {
                    this.createClass3(classLoader, lookup);
                }
                clazz = this.thisClass;
                this.thisClass = null;
            }
        }
        return clazz;
    }

    public String getKey(Class<?> clazz, Class<?>[] classArray, byte[] byArray, boolean bl) {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        if (clazz != null) {
            stringBuilder.append(clazz.getName());
        }
        stringBuilder.append(':');
        for (n = 0; n < classArray.length; ++n) {
            stringBuilder.append(classArray[n].getName());
            stringBuilder.append(':');
        }
        for (n = 0; n < byArray.length; ++n) {
            byte by = byArray[n];
            int n2 = by & 0xF;
            int n3 = by >> 4 & 0xF;
            stringBuilder.append(hexDigits[n2]);
            stringBuilder.append(hexDigits[n3]);
        }
        if (bl) {
            stringBuilder.append(":w");
        }
        return stringBuilder.toString();
    }

    private void createClass2(ClassLoader classLoader, MethodHandles.Lookup lookup) {
        ProxyDetails proxyDetails;
        String string = this.getKey(this.superClass, this.interfaces, this.signature, this.factoryWriteReplace);
        Map<String, ProxyDetails> map = proxyCache.get(classLoader);
        if (map == null) {
            map = new HashMap<String, ProxyDetails>();
            proxyCache.put(classLoader, map);
        }
        if ((proxyDetails = map.get(string)) != null) {
            Reference<Class<?>> reference = proxyDetails.proxyClass;
            this.thisClass = reference.get();
            if (this.thisClass != null) {
                return;
            }
        }
        this.createClass3(classLoader, lookup);
        proxyDetails = new ProxyDetails(this.signature, this.thisClass, this.factoryWriteReplace);
        map.put(string, proxyDetails);
    }

    private void createClass3(ClassLoader classLoader, MethodHandles.Lookup lookup) {
        this.allocateClassName();
        try {
            ClassFile classFile = this.make();
            if (this.writeDirectory != null) {
                FactoryHelper.writeFile(classFile, this.writeDirectory);
            }
            this.thisClass = lookup == null ? FactoryHelper.toClass(classFile, this.getClassInTheSamePackage(), classLoader, this.getDomain()) : FactoryHelper.toClass(classFile, lookup);
            this.setField(FILTER_SIGNATURE_FIELD, this.signature);
            if (!this.factoryUseCache) {
                this.setField(DEFAULT_INTERCEPTOR, this.handler);
            }
        } catch (CannotCompileException cannotCompileException) {
            throw new RuntimeException(cannotCompileException.getMessage(), cannotCompileException);
        }
    }

    private Class<?> getClassInTheSamePackage() {
        if (this.basename.startsWith(packageForJavaBase)) {
            return this.getClass();
        }
        if (this.superClass != null && this.superClass != OBJECT_TYPE) {
            return this.superClass;
        }
        if (this.interfaces != null && this.interfaces.length > 0) {
            return this.interfaces[0];
        }
        return this.getClass();
    }

    private void setField(String string, Object object) {
        if (this.thisClass != null && object != null) {
            try {
                Field field = this.thisClass.getField(string);
                SecurityActions.setAccessible(field, true);
                field.set(null, object);
                SecurityActions.setAccessible(field, false);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    static byte[] getFilterSignature(Class<?> clazz) {
        return (byte[])ProxyFactory.getField(clazz, FILTER_SIGNATURE_FIELD);
    }

    private static Object getField(Class<?> clazz, String string) {
        try {
            Field field = clazz.getField(string);
            field.setAccessible(true);
            Object object = field.get(null);
            field.setAccessible(false);
            return object;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static MethodHandler getHandler(Proxy proxy) {
        try {
            Field field = proxy.getClass().getDeclaredField(HANDLER);
            field.setAccessible(true);
            Object object = field.get(proxy);
            field.setAccessible(false);
            return (MethodHandler)object;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    protected ClassLoader getClassLoader() {
        return classLoaderProvider.get(this);
    }

    protected ClassLoader getClassLoader0() {
        ClassLoader classLoader = null;
        if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
            classLoader = this.superClass.getClassLoader();
        } else if (this.interfaces != null && this.interfaces.length > 0) {
            classLoader = this.interfaces[0].getClassLoader();
        }
        if (classLoader == null && (classLoader = this.getClass().getClassLoader()) == null && (classLoader = Thread.currentThread().getContextClassLoader()) == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    protected ProtectionDomain getDomain() {
        Class<?> clazz = this.superClass != null && !this.superClass.getName().equals("java.lang.Object") ? this.superClass : (this.interfaces != null && this.interfaces.length > 0 ? this.interfaces[0] : this.getClass());
        return clazz.getProtectionDomain();
    }

    public Object create(Class<?>[] classArray, Object[] objectArray, MethodHandler methodHandler) {
        Object object = this.create(classArray, objectArray);
        ((Proxy)object).setHandler(methodHandler);
        return object;
    }

    public Object create(Class<?>[] classArray, Object[] objectArray) {
        Class<?> clazz = this.createClass();
        Constructor<?> constructor = clazz.getConstructor(classArray);
        return constructor.newInstance(objectArray);
    }

    @Deprecated
    public void setHandler(MethodHandler methodHandler) {
        if (this.factoryUseCache && methodHandler != null) {
            this.factoryUseCache = false;
            this.thisClass = null;
        }
        this.handler = methodHandler;
        this.setField(DEFAULT_INTERCEPTOR, this.handler);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String makeProxyName(String string) {
        UniqueName uniqueName = nameGenerator;
        synchronized (uniqueName) {
            return nameGenerator.get(string);
        }
    }

    private ClassFile make() {
        Object object;
        FieldInfo fieldInfo;
        ClassFile classFile = new ClassFile(false, this.classname, this.superName);
        classFile.setAccessFlags(1);
        ProxyFactory.setInterfaces(classFile, this.interfaces, this.hasGetHandler ? Proxy.class : ProxyObject.class);
        ConstPool constPool = classFile.getConstPool();
        if (!this.factoryUseCache) {
            fieldInfo = new FieldInfo(constPool, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
            fieldInfo.setAccessFlags(9);
            classFile.addField(fieldInfo);
        }
        fieldInfo = new FieldInfo(constPool, HANDLER, HANDLER_TYPE);
        fieldInfo.setAccessFlags(2);
        classFile.addField(fieldInfo);
        FieldInfo fieldInfo2 = new FieldInfo(constPool, FILTER_SIGNATURE_FIELD, FILTER_SIGNATURE_TYPE);
        fieldInfo2.setAccessFlags(9);
        classFile.addField(fieldInfo2);
        FieldInfo fieldInfo3 = new FieldInfo(constPool, SERIAL_VERSION_UID_FIELD, SERIAL_VERSION_UID_TYPE);
        fieldInfo3.setAccessFlags(25);
        classFile.addField(fieldInfo3);
        if (this.genericSignature != null) {
            object = new SignatureAttribute(constPool, this.genericSignature);
            classFile.addAttribute((AttributeInfo)object);
        }
        this.makeConstructors(this.classname, classFile, constPool, this.classname);
        object = new ArrayList();
        int n = this.overrideMethods(classFile, constPool, this.classname, (List<Find2MethodsArgs>)object);
        ProxyFactory.addClassInitializer(classFile, constPool, this.classname, n, (List<Find2MethodsArgs>)object);
        ProxyFactory.addSetter(this.classname, classFile, constPool);
        if (!this.hasGetHandler) {
            ProxyFactory.addGetter(this.classname, classFile, constPool);
        }
        if (this.factoryWriteReplace) {
            try {
                classFile.addMethod(ProxyFactory.makeWriteReplace(constPool));
            } catch (DuplicateMemberException duplicateMemberException) {
                // empty catch block
            }
        }
        this.thisClass = null;
        return classFile;
    }

    private void checkClassAndSuperName() {
        if (this.interfaces == null) {
            this.interfaces = new Class[0];
        }
        if (this.superClass == null) {
            this.superClass = OBJECT_TYPE;
            this.superName = this.superClass.getName();
            this.basename = this.interfaces.length == 0 ? this.superName : this.interfaces[0].getName();
        } else {
            this.basename = this.superName = this.superClass.getName();
        }
        if (Modifier.isFinal(this.superClass.getModifiers())) {
            throw new RuntimeException(this.superName + " is final");
        }
        if (this.basename.startsWith("java.") || this.basename.startsWith("jdk.") || onlyPublicMethods) {
            this.basename = packageForJavaBase + this.basename.replace('.', '_');
        }
    }

    private void allocateClassName() {
        this.classname = ProxyFactory.makeProxyName(this.basename);
    }

    private void makeSortedMethodList() {
        this.checkClassAndSuperName();
        this.hasGetHandler = false;
        Map<String, Method> map = this.getMethods(this.superClass, this.interfaces);
        this.signatureMethods = new ArrayList<Map.Entry<String, Method>>(map.entrySet());
        Collections.sort(this.signatureMethods, sorter);
    }

    private void computeSignature(MethodFilter methodFilter) {
        this.makeSortedMethodList();
        int n = this.signatureMethods.size();
        int n2 = n + 7 >> 3;
        this.signature = new byte[n2];
        for (int i = 0; i < n; ++i) {
            Method method = this.signatureMethods.get(i).getValue();
            int n3 = method.getModifiers();
            if (Modifier.isFinal(n3) || Modifier.isStatic(n3) || !ProxyFactory.isVisible(n3, this.basename, method) || methodFilter != null && !methodFilter.isHandled(method)) continue;
            this.setBit(this.signature, i);
        }
    }

    private void installSignature(byte[] byArray) {
        this.makeSortedMethodList();
        int n = this.signatureMethods.size();
        int n2 = n + 7 >> 3;
        if (byArray.length != n2) {
            throw new RuntimeException("invalid filter signature length for deserialized proxy class");
        }
        this.signature = byArray;
    }

    private boolean testBit(byte[] byArray, int n) {
        int n2 = n >> 3;
        if (n2 > byArray.length) {
            return false;
        }
        byte by = byArray[n2];
        int n3 = n & 7;
        int n4 = 1 << n3;
        return (by & n4) != 0;
    }

    private void setBit(byte[] byArray, int n) {
        int n2 = n >> 3;
        if (n2 < byArray.length) {
            int n3 = n & 7;
            int n4 = 1 << n3;
            byte by = byArray[n2];
            byArray[n2] = (byte)(by | n4);
        }
    }

    private static void setInterfaces(ClassFile classFile, Class<?>[] classArray, Class<?> clazz) {
        String[] stringArray;
        String string = clazz.getName();
        if (classArray == null || classArray.length == 0) {
            stringArray = new String[]{string};
        } else {
            stringArray = new String[classArray.length + 1];
            for (int i = 0; i < classArray.length; ++i) {
                stringArray[i] = classArray[i].getName();
            }
            stringArray[classArray.length] = string;
        }
        classFile.setInterfaces(stringArray);
    }

    private static void addClassInitializer(ClassFile classFile, ConstPool constPool, String string, int n, List<Find2MethodsArgs> list) {
        FieldInfo fieldInfo = new FieldInfo(constPool, HOLDER, HOLDER_TYPE);
        fieldInfo.setAccessFlags(10);
        classFile.addField(fieldInfo);
        MethodInfo methodInfo = new MethodInfo(constPool, "<clinit>", "()V");
        methodInfo.setAccessFlags(8);
        ProxyFactory.setThrows(methodInfo, constPool, new Class[]{ClassNotFoundException.class});
        Bytecode bytecode = new Bytecode(constPool, 0, 2);
        bytecode.addIconst(n * 2);
        bytecode.addAnewarray("java.lang.reflect.Method");
        boolean bl = false;
        bytecode.addAstore(0);
        bytecode.addLdc(string);
        bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        boolean bl2 = true;
        bytecode.addAstore(1);
        for (Find2MethodsArgs find2MethodsArgs : list) {
            ProxyFactory.callFind2Methods(bytecode, find2MethodsArgs.methodName, find2MethodsArgs.delegatorName, find2MethodsArgs.origIndex, find2MethodsArgs.descriptor, 1, 0);
        }
        bytecode.addAload(0);
        bytecode.addPutstatic(string, HOLDER, HOLDER_TYPE);
        bytecode.addLconst(-1L);
        bytecode.addPutstatic(string, SERIAL_VERSION_UID_FIELD, SERIAL_VERSION_UID_TYPE);
        bytecode.addOpcode(177);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        classFile.addMethod(methodInfo);
    }

    private static void callFind2Methods(Bytecode bytecode, String string, String string2, int n, String string3, int n2, int n3) {
        String string4 = RuntimeSupport.class.getName();
        String string5 = "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[Ljava/lang/reflect/Method;)V";
        bytecode.addAload(n2);
        bytecode.addLdc(string);
        if (string2 == null) {
            bytecode.addOpcode(1);
        } else {
            bytecode.addLdc(string2);
        }
        bytecode.addIconst(n);
        bytecode.addLdc(string3);
        bytecode.addAload(n3);
        bytecode.addInvokestatic(string4, "find2Methods", string5);
    }

    private static void addSetter(String string, ClassFile classFile, ConstPool constPool) {
        MethodInfo methodInfo = new MethodInfo(constPool, HANDLER_SETTER, HANDLER_SETTER_TYPE);
        methodInfo.setAccessFlags(1);
        Bytecode bytecode = new Bytecode(constPool, 2, 2);
        bytecode.addAload(0);
        bytecode.addAload(1);
        bytecode.addPutfield(string, HANDLER, HANDLER_TYPE);
        bytecode.addOpcode(177);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        classFile.addMethod(methodInfo);
    }

    private static void addGetter(String string, ClassFile classFile, ConstPool constPool) {
        MethodInfo methodInfo = new MethodInfo(constPool, HANDLER_GETTER, HANDLER_GETTER_TYPE);
        methodInfo.setAccessFlags(1);
        Bytecode bytecode = new Bytecode(constPool, 1, 1);
        bytecode.addAload(0);
        bytecode.addGetfield(string, HANDLER, HANDLER_TYPE);
        bytecode.addOpcode(176);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        classFile.addMethod(methodInfo);
    }

    private int overrideMethods(ClassFile classFile, ConstPool constPool, String string, List<Find2MethodsArgs> list) {
        String string2 = ProxyFactory.makeUniqueName("_d", this.signatureMethods);
        Iterator<Map.Entry<String, Method>> iterator = this.signatureMethods.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, Method> entry = iterator.next();
            if ((ClassFile.MAJOR_VERSION < 49 || !ProxyFactory.isBridge(entry.getValue())) && this.testBit(this.signature, n)) {
                this.override(string, entry.getValue(), string2, n, ProxyFactory.keyToDesc(entry.getKey(), entry.getValue()), classFile, constPool, list);
            }
            ++n;
        }
        return n;
    }

    private static boolean isBridge(Method method) {
        return method.isBridge();
    }

    private void override(String string, Method method, String string2, int n, String string3, ClassFile classFile, ConstPool constPool, List<Find2MethodsArgs> list) {
        MethodInfo methodInfo;
        Class<?> clazz = method.getDeclaringClass();
        String string4 = string2 + n + method.getName();
        if (Modifier.isAbstract(method.getModifiers())) {
            string4 = null;
        } else {
            methodInfo = this.makeDelegator(method, string3, constPool, clazz, string4);
            methodInfo.setAccessFlags(methodInfo.getAccessFlags() & 0xFFFFFFBF);
            classFile.addMethod(methodInfo);
        }
        methodInfo = ProxyFactory.makeForwarder(string, method, string3, constPool, clazz, string4, n, list);
        classFile.addMethod(methodInfo);
    }

    private void makeConstructors(String string, ClassFile classFile, ConstPool constPool, String string2) {
        Constructor<?>[] constructorArray = SecurityActions.getDeclaredConstructors(this.superClass);
        boolean bl = !this.factoryUseCache;
        for (int i = 0; i < constructorArray.length; ++i) {
            Constructor<?> constructor = constructorArray[i];
            int n = constructor.getModifiers();
            if (Modifier.isFinal(n) || Modifier.isPrivate(n) || !ProxyFactory.isVisible(n, this.basename, constructor)) continue;
            MethodInfo methodInfo = ProxyFactory.makeConstructor(string, constructor, constPool, this.superClass, bl);
            classFile.addMethod(methodInfo);
        }
    }

    private static String makeUniqueName(String string, List<Map.Entry<String, Method>> list) {
        if (ProxyFactory.makeUniqueName0(string, list.iterator())) {
            return string;
        }
        for (int i = 100; i < 999; ++i) {
            String string2 = string + i;
            if (!ProxyFactory.makeUniqueName0(string2, list.iterator())) continue;
            return string2;
        }
        throw new RuntimeException("cannot make a unique method name");
    }

    private static boolean makeUniqueName0(String string, Iterator<Map.Entry<String, Method>> iterator) {
        while (iterator.hasNext()) {
            if (!iterator.next().getKey().startsWith(string)) continue;
            return false;
        }
        return true;
    }

    private static boolean isVisible(int n, String string, Member member) {
        if ((n & 2) != 0) {
            return false;
        }
        if ((n & 5) != 0) {
            return true;
        }
        String string2 = ProxyFactory.getPackageName(string);
        String string3 = ProxyFactory.getPackageName(member.getDeclaringClass().getName());
        if (string2 == null) {
            return string3 == null;
        }
        return string2.equals(string3);
    }

    private static String getPackageName(String string) {
        int n = string.lastIndexOf(46);
        if (n < 0) {
            return null;
        }
        return string.substring(0, n);
    }

    private Map<String, Method> getMethods(Class<?> clazz, Class<?>[] classArray) {
        HashMap<String, Method> hashMap = new HashMap<String, Method>();
        HashSet hashSet = new HashSet();
        for (int i = 0; i < classArray.length; ++i) {
            this.getMethods(hashMap, classArray[i], hashSet);
        }
        this.getMethods(hashMap, clazz, hashSet);
        return hashMap;
    }

    private void getMethods(Map<String, Method> map, Class<?> clazz, Set<Class<?>> set) {
        if (!set.add(clazz)) {
            return;
        }
        Class<?>[] classArray = clazz.getInterfaces();
        for (int i = 0; i < classArray.length; ++i) {
            this.getMethods(map, classArray[i], set);
        }
        Class<?> clazz2 = clazz.getSuperclass();
        if (clazz2 != null) {
            this.getMethods(map, clazz2, set);
        }
        Method[] methodArray = SecurityActions.getDeclaredMethods(clazz);
        for (int i = 0; i < methodArray.length; ++i) {
            Method method;
            if (Modifier.isPrivate(methodArray[i].getModifiers())) continue;
            Method method2 = methodArray[i];
            String string = method2.getName() + ':' + RuntimeSupport.makeDescriptor(method2);
            if (string.startsWith(HANDLER_GETTER_KEY)) {
                this.hasGetHandler = true;
            }
            if (null != (method = map.put(string, method2)) && ProxyFactory.isBridge(method2) && !Modifier.isPublic(method.getDeclaringClass().getModifiers()) && !Modifier.isAbstract(method.getModifiers()) && !ProxyFactory.isDuplicated(i, methodArray)) {
                map.put(string, method);
            }
            if (null == method || !Modifier.isPublic(method.getModifiers()) || Modifier.isPublic(method2.getModifiers())) continue;
            map.put(string, method);
        }
    }

    private static boolean isDuplicated(int n, Method[] methodArray) {
        String string = methodArray[n].getName();
        for (int i = 0; i < methodArray.length; ++i) {
            if (i == n || !string.equals(methodArray[i].getName()) || !ProxyFactory.areParametersSame(methodArray[n], methodArray[i])) continue;
            return true;
        }
        return false;
    }

    private static boolean areParametersSame(Method method, Method method2) {
        Class<?>[] classArray;
        Class<?>[] classArray2 = method.getParameterTypes();
        if (classArray2.length == (classArray = method2.getParameterTypes()).length) {
            for (int i = 0; i < classArray2.length; ++i) {
                if (classArray2[i].getName().equals(classArray[i].getName())) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private static String keyToDesc(String string, Method method) {
        return string.substring(string.indexOf(58) + 1);
    }

    private static MethodInfo makeConstructor(String string, Constructor<?> constructor, ConstPool constPool, Class<?> clazz, boolean bl) {
        String string2 = RuntimeSupport.makeDescriptor(constructor.getParameterTypes(), Void.TYPE);
        MethodInfo methodInfo = new MethodInfo(constPool, "<init>", string2);
        methodInfo.setAccessFlags(1);
        ProxyFactory.setThrows(methodInfo, constPool, constructor.getExceptionTypes());
        Bytecode bytecode = new Bytecode(constPool, 0, 0);
        if (bl) {
            bytecode.addAload(0);
            bytecode.addGetstatic(string, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
            bytecode.addPutfield(string, HANDLER, HANDLER_TYPE);
            bytecode.addGetstatic(string, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
            bytecode.addOpcode(199);
            bytecode.addIndex(10);
        }
        bytecode.addAload(0);
        bytecode.addGetstatic(NULL_INTERCEPTOR_HOLDER, DEFAULT_INTERCEPTOR, HANDLER_TYPE);
        bytecode.addPutfield(string, HANDLER, HANDLER_TYPE);
        int n = bytecode.currentPc();
        bytecode.addAload(0);
        int n2 = ProxyFactory.addLoadParameters(bytecode, constructor.getParameterTypes(), 1);
        bytecode.addInvokespecial(clazz.getName(), "<init>", string2);
        bytecode.addOpcode(177);
        bytecode.setMaxLocals(n2 + 1);
        CodeAttribute codeAttribute = bytecode.toCodeAttribute();
        methodInfo.setCodeAttribute(codeAttribute);
        StackMapTable.Writer writer = new StackMapTable.Writer(32);
        writer.sameFrame(n);
        codeAttribute.setAttribute(writer.toStackMapTable(constPool));
        return methodInfo;
    }

    private MethodInfo makeDelegator(Method method, String string, ConstPool constPool, Class<?> clazz, String string2) {
        MethodInfo methodInfo = new MethodInfo(constPool, string2, string);
        methodInfo.setAccessFlags(0x11 | method.getModifiers() & 0xFFFFFAD9);
        ProxyFactory.setThrows(methodInfo, constPool, method);
        Bytecode bytecode = new Bytecode(constPool, 0, 0);
        bytecode.addAload(0);
        int n = ProxyFactory.addLoadParameters(bytecode, method.getParameterTypes(), 1);
        Class<?> clazz2 = this.invokespecialTarget(clazz);
        bytecode.addInvokespecial(clazz2.isInterface(), constPool.addClassInfo(clazz2.getName()), method.getName(), string);
        ProxyFactory.addReturn(bytecode, method.getReturnType());
        bytecode.setMaxLocals(++n);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        return methodInfo;
    }

    private Class<?> invokespecialTarget(Class<?> clazz) {
        if (clazz.isInterface()) {
            for (Class<?> clazz2 : this.interfaces) {
                if (!clazz.isAssignableFrom(clazz2)) continue;
                return clazz2;
            }
        }
        return this.superClass;
    }

    private static MethodInfo makeForwarder(String string, Method method, String string2, ConstPool constPool, Class<?> clazz, String string3, int n, List<Find2MethodsArgs> list) {
        MethodInfo methodInfo = new MethodInfo(constPool, method.getName(), string2);
        methodInfo.setAccessFlags(0x10 | method.getModifiers() & 0xFFFFFADF);
        ProxyFactory.setThrows(methodInfo, constPool, method);
        int n2 = Descriptor.paramSize(string2);
        Bytecode bytecode = new Bytecode(constPool, 0, n2 + 2);
        int n3 = n * 2;
        int n4 = n * 2 + 1;
        int n5 = n2 + 1;
        bytecode.addGetstatic(string, HOLDER, HOLDER_TYPE);
        bytecode.addAstore(n5);
        list.add(new Find2MethodsArgs(method.getName(), string3, string2, n3));
        bytecode.addAload(0);
        bytecode.addGetfield(string, HANDLER, HANDLER_TYPE);
        bytecode.addAload(0);
        bytecode.addAload(n5);
        bytecode.addIconst(n3);
        bytecode.addOpcode(50);
        bytecode.addAload(n5);
        bytecode.addIconst(n4);
        bytecode.addOpcode(50);
        ProxyFactory.makeParameterList(bytecode, method.getParameterTypes());
        bytecode.addInvokeinterface(MethodHandler.class.getName(), "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 5);
        Class<?> clazz2 = method.getReturnType();
        ProxyFactory.addUnwrapper(bytecode, clazz2);
        ProxyFactory.addReturn(bytecode, clazz2);
        CodeAttribute codeAttribute = bytecode.toCodeAttribute();
        methodInfo.setCodeAttribute(codeAttribute);
        return methodInfo;
    }

    private static void setThrows(MethodInfo methodInfo, ConstPool constPool, Method method) {
        Class<?>[] classArray = method.getExceptionTypes();
        ProxyFactory.setThrows(methodInfo, constPool, classArray);
    }

    private static void setThrows(MethodInfo methodInfo, ConstPool constPool, Class<?>[] classArray) {
        if (classArray.length == 0) {
            return;
        }
        String[] stringArray = new String[classArray.length];
        for (int i = 0; i < classArray.length; ++i) {
            stringArray[i] = classArray[i].getName();
        }
        ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute(constPool);
        exceptionsAttribute.setExceptions(stringArray);
        methodInfo.setExceptionsAttribute(exceptionsAttribute);
    }

    private static int addLoadParameters(Bytecode bytecode, Class<?>[] classArray, int n) {
        int n2 = 0;
        int n3 = classArray.length;
        for (int i = 0; i < n3; ++i) {
            n2 += ProxyFactory.addLoad(bytecode, n2 + n, classArray[i]);
        }
        return n2;
    }

    private static int addLoad(Bytecode bytecode, int n, Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == Long.TYPE) {
                bytecode.addLload(n);
                return 2;
            }
            if (clazz == Float.TYPE) {
                bytecode.addFload(n);
            } else {
                if (clazz == Double.TYPE) {
                    bytecode.addDload(n);
                    return 2;
                }
                bytecode.addIload(n);
            }
        } else {
            bytecode.addAload(n);
        }
        return 1;
    }

    private static int addReturn(Bytecode bytecode, Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == Long.TYPE) {
                bytecode.addOpcode(173);
                return 2;
            }
            if (clazz == Float.TYPE) {
                bytecode.addOpcode(174);
            } else {
                if (clazz == Double.TYPE) {
                    bytecode.addOpcode(175);
                    return 2;
                }
                if (clazz == Void.TYPE) {
                    bytecode.addOpcode(177);
                    return 0;
                }
                bytecode.addOpcode(172);
            }
        } else {
            bytecode.addOpcode(176);
        }
        return 1;
    }

    private static void makeParameterList(Bytecode bytecode, Class<?>[] classArray) {
        int n = 1;
        int n2 = classArray.length;
        bytecode.addIconst(n2);
        bytecode.addAnewarray("java/lang/Object");
        for (int i = 0; i < n2; ++i) {
            bytecode.addOpcode(89);
            bytecode.addIconst(i);
            Class<?> clazz = classArray[i];
            if (clazz.isPrimitive()) {
                n = ProxyFactory.makeWrapper(bytecode, clazz, n);
            } else {
                bytecode.addAload(n);
                ++n;
            }
            bytecode.addOpcode(83);
        }
    }

    private static int makeWrapper(Bytecode bytecode, Class<?> clazz, int n) {
        int n2 = FactoryHelper.typeIndex(clazz);
        String string = FactoryHelper.wrapperTypes[n2];
        bytecode.addNew(string);
        bytecode.addOpcode(89);
        ProxyFactory.addLoad(bytecode, n, clazz);
        bytecode.addInvokespecial(string, "<init>", FactoryHelper.wrapperDesc[n2]);
        return n + FactoryHelper.dataSize[n2];
    }

    private static void addUnwrapper(Bytecode bytecode, Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == Void.TYPE) {
                bytecode.addOpcode(87);
            } else {
                int n = FactoryHelper.typeIndex(clazz);
                String string = FactoryHelper.wrapperTypes[n];
                bytecode.addCheckcast(string);
                bytecode.addInvokevirtual(string, FactoryHelper.unwarpMethods[n], FactoryHelper.unwrapDesc[n]);
            }
        } else {
            bytecode.addCheckcast(clazz.getName());
        }
    }

    private static MethodInfo makeWriteReplace(ConstPool constPool) {
        MethodInfo methodInfo = new MethodInfo(constPool, "writeReplace", "()Ljava/lang/Object;");
        String[] stringArray = new String[]{"java.io.ObjectStreamException"};
        ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute(constPool);
        exceptionsAttribute.setExceptions(stringArray);
        methodInfo.setExceptionsAttribute(exceptionsAttribute);
        Bytecode bytecode = new Bytecode(constPool, 0, 1);
        bytecode.addAload(0);
        bytecode.addInvokestatic(NULL_INTERCEPTOR_HOLDER, "makeSerializedProxy", "(Ljava/lang/Object;)Ljavassist/util/proxy/SerializedProxy;");
        bytecode.addOpcode(176);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        return methodInfo;
    }

    static class ProxyDetails {
        byte[] signature;
        Reference<Class<?>> proxyClass;
        boolean isUseWriteReplace;

        ProxyDetails(byte[] byArray, Class<?> clazz, boolean bl) {
            this.signature = byArray;
            this.proxyClass = new WeakReference(clazz);
            this.isUseWriteReplace = bl;
        }
    }

    public static interface ClassLoaderProvider {
        public ClassLoader get(ProxyFactory var1);
    }

    public static interface UniqueName {
        public String get(String var1);
    }

    static class Find2MethodsArgs {
        String methodName;
        String delegatorName;
        String descriptor;
        int origIndex;

        Find2MethodsArgs(String string, String string2, String string3, int n) {
            this.methodName = string;
            this.delegatorName = string2;
            this.descriptor = string3;
            this.origIndex = n;
        }
    }
}

