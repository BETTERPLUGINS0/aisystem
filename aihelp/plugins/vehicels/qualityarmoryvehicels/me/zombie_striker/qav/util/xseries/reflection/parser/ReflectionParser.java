/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.intellij.lang.annotations.Pattern
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection.parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveNamespace;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.ConstructorMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FieldMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MethodMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.PackageHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.StaticClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.UnknownClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftPackage;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ReflectionParser {
    private static final String[] DEFAULT_CHECKED_PACKAGES = new String[]{"java.util", "java.util.function", "java.lang", "java.io"};
    private final String declaration;
    private Pattern pattern;
    private Matcher matcher;
    private ReflectiveNamespace namespace;
    private Map<String, Class<?>> cachedImports;
    private String[] checkedPackages = DEFAULT_CHECKED_PACKAGES;
    private final Set<Flag> flags = EnumSet.noneOf(Flag.class);
    private static final PackageHandle[] PACKAGE_HANDLES = MinecraftPackage.values();
    @Language(value="RegExp")
    private static final String GENERIC = "(?:\\s*<\\s*[.\\w<>\\[\\], ]+\\s*>)?";
    @Language(value="RegExp")
    private static final String ARRAY = "(?:(?:\\[])*)";
    @Language(value="RegExp")
    private static final String PACKAGE_REGEX = "(?:package\\s+(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*;\\s*)?";
    @Language(value="RegExp")
    private static final String CLASS_TYPES = "(?<classType>class|interface|enum|record)";
    @Language(value="RegExp")
    private static final String PARAMETERS = "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)";
    @Language(value="RegExp")
    private static final String THROWS = "(?:\\s*throws\\s+(?<throws>(?:" + ReflectionParser.type(null).array(false) + ")(?:\\s*,\\s*" + ReflectionParser.type(null).array(false) + ")*))?";
    @Language(value="RegExp")
    private static final String END_DECL = "\\s*;?\\s*";
    private static final Pattern CLASS = Pattern.compile("(?:package\\s+(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*;\\s*)?" + Flag.access$000() + "(?<classType>class|interface|enum|record)" + "\\s+" + ReflectionParser.type("className") + "(?:\\(\\))?(?:\\s+extends\\s+" + ReflectionParser.id("superclasses") + ")?(?:\\s+implements\\s+(?<interfaces>(?:" + ReflectionParser.type(null).array(false) + ")(?:\\s*,\\s*" + ReflectionParser.type(null).array(false) + ")*))?(?:\\s*\\{\\s*})?\\s*");
    private static final Pattern METHOD = Pattern.compile(Flag.access$000() + ReflectionParser.type("methodReturnType") + "\\s+" + ReflectionParser.id("methodName") + "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)" + THROWS + "\\s*;?\\s*");
    private static final Pattern CONSTRUCTOR = Pattern.compile(Flag.access$000() + "\\s+" + ReflectionParser.id("className") + "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)" + "\\s*;?\\s*");
    private static final Pattern FIELD = Pattern.compile(Flag.access$000() + ReflectionParser.type("fieldType") + "\\s+" + ReflectionParser.id("fieldName") + "\\s*;?\\s*");
    private static final Map<String, Class<?>> PREDEFINED_TYPES = new HashMap();

    public ReflectionParser(@Language(value="Java") String string) {
        this.declaration = string;
    }

    public ReflectionParser checkedPackages(@org.intellij.lang.annotations.Pattern(value="(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        this.checkedPackages = stringArray;
        return this;
    }

    private static IDHandler id(@NotNull @Language(value="RegExp") String string) {
        return new IDHandler(string, false);
    }

    private static IDHandler type(@Language(value="RegExp") String string) {
        return new IDHandler(string, true).generic(true).array(true);
    }

    private ClassHandle[] parseTypes(String[] stringArray) {
        ClassHandle[] classHandleArray = new ClassHandle[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            String string = stringArray[i];
            string = string.trim().substring(0, string.lastIndexOf(32)).trim();
            classHandleArray[i] = this.parseType(string);
        }
        return classHandleArray;
    }

    private ClassHandle parseType(String object) {
        Object object2;
        if (this.cachedImports == null && this.namespace != null) {
            this.cachedImports = this.namespace.getImports();
        }
        String string = object;
        object = ((String)object).replace(" ", "");
        int n = 0;
        if (((String)object).endsWith("[]")) {
            object2 = ((String)object).replace("[]", "");
            n = (((String)object).length() - ((String)object2).length()) / 2;
            object = object2;
        }
        if (((String)object).endsWith(">")) {
            object = ((String)object).substring(0, ((String)object).indexOf(60));
        }
        if ((object2 = this.stringToClass((String)object)) == null) {
            return new UnknownClassHandle(this.getOrCreateNamespace(), string + " -> " + (String)object);
        }
        if (n != 0) {
            object2 = (Class)XReflection.of(object2).asArray(n).unreflect();
        }
        return new StaticClassHandle(this.getOrCreateNamespace(), (Class<?>)object2);
    }

    @Nullable
    private Class<?> stringToClass(String string) {
        Class<?> clazz = null;
        if (!string.contains(".")) {
            if (this.cachedImports != null) {
                clazz = this.cachedImports.get(string);
            }
            if (clazz == null) {
                clazz = PREDEFINED_TYPES.get(string);
            }
            if (clazz == null && this.checkedPackages != null) {
                for (String string2 : this.checkedPackages) {
                    boolean bl = string2.endsWith("$");
                    clazz = ReflectionParser.classNamed(string2 + (bl ? "" : Character.valueOf('.')) + string);
                    if (clazz != null) break;
                }
            }
        }
        if (clazz == null) {
            clazz = ReflectionParser.classNamed(string);
        }
        return clazz;
    }

    private static Class<?> classNamed(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
    }

    private ReflectiveNamespace getOrCreateNamespace() {
        return this.namespace == null ? XReflection.namespaced() : this.namespace;
    }

    public ReflectionParser imports(ReflectiveNamespace reflectiveNamespace) {
        this.namespace = reflectiveNamespace;
        return this;
    }

    private void pattern(Pattern pattern, ReflectiveHandle<?> reflectiveHandle) {
        this.pattern = pattern;
        this.matcher = pattern.matcher(this.declaration);
        this.start(reflectiveHandle);
    }

    public <T extends DynamicClassHandle> T parseClass(T t) {
        String string;
        this.pattern(CLASS, t);
        String string2 = this.group("package");
        if (string2 != null && !string2.isEmpty()) {
            boolean bl = false;
            for (PackageHandle packageHandle : PACKAGE_HANDLES) {
                String string3 = packageHandle.packageId().toLowerCase(Locale.ENGLISH);
                if (!string2.startsWith(string3)) continue;
                if (string2.indexOf(46) == -1) {
                    t.inPackage(packageHandle);
                } else {
                    t.inPackage(packageHandle, string2.substring(string3.length() + 1));
                }
                bl = true;
                break;
            }
            if (!bl) {
                t.inPackage(string2);
            }
        }
        if ((string = this.group("className")).contains("<")) {
            string = string.substring(0, string.indexOf(60));
        }
        t.named(string);
        return t;
    }

    private void includeInnerClassOf(MemberHandle memberHandle) {
        Class clazz = (Class)memberHandle.getClassHandle().reflectOrNull();
        if (clazz == null) {
            return;
        }
        int n = DEFAULT_CHECKED_PACKAGES.length + 2;
        this.checkedPackages = Arrays.copyOf(DEFAULT_CHECKED_PACKAGES, n);
        this.checkedPackages[n - 1] = clazz.getName() + '$';
        this.checkedPackages[n - 2] = clazz.getPackage().getName();
    }

    public <T extends ConstructorMemberHandle> T parseConstructor(T t) {
        this.includeInnerClassOf(t);
        this.pattern(CONSTRUCTOR, t);
        if (this.has("className") && !t.getClassHandle().getPossibleNames().contains(this.group("className"))) {
            this.error("Wrong class name associated to constructor, possible names: " + t.getClassHandle().getPossibleNames());
        }
        if (this.has("parameters")) {
            t.parameters(this.parseTypes(this.group("parameters").split(",")));
        }
        return t;
    }

    public <T extends MethodMemberHandle> T parseMethod(T t) {
        this.includeInnerClassOf(t);
        this.pattern(METHOD, t);
        t.named(this.group("methodName").split("\\$"));
        t.returns(this.parseType(this.group("methodReturnType")));
        if (this.has("parameters")) {
            t.parameters(this.parseTypes(this.group("parameters").split(",")));
        }
        return t;
    }

    public <T extends FieldMemberHandle> T parseField(T t) {
        this.includeInnerClassOf(t);
        this.pattern(FIELD, t);
        t.named(this.group("fieldName").split("\\$"));
        t.returns(this.parseType(this.group("fieldType")));
        return t;
    }

    private String group(String string) {
        return this.matcher.group(string);
    }

    private boolean has(String string) {
        String string2 = this.group(string);
        return string2 != null && !string2.isEmpty();
    }

    private void start(ReflectiveHandle<?> reflectiveHandle) {
        if (!this.matcher.matches()) {
            this.error("Not a " + reflectiveHandle + " declaration");
        }
        this.parseFlags();
        if (reflectiveHandle instanceof MemberHandle) {
            MemberHandle memberHandle = (MemberHandle)reflectiveHandle;
            if (!ReflectionParser.hasOneOf(this.flags, Flag.PUBLIC, Flag.PROTECTED, Flag.PRIVATE)) {
                Class clazz = (Class)memberHandle.getClassHandle().reflectOrNull();
                if (clazz != null && !clazz.isInterface()) {
                    memberHandle.makeAccessible();
                }
            } else if (ReflectionParser.hasOneOf(this.flags, Flag.PRIVATE, Flag.PROTECTED)) {
                memberHandle.makeAccessible();
            }
            if (reflectiveHandle instanceof FieldMemberHandle && this.flags.contains((Object)Flag.FINAL)) {
                ((FieldMemberHandle)reflectiveHandle).asFinal();
            }
            if (reflectiveHandle instanceof FlaggedNamedMemberHandle && this.flags.contains((Object)Flag.STATIC)) {
                ((FlaggedNamedMemberHandle)reflectiveHandle).asStatic();
            }
        }
    }

    private void parseFlags() {
        if (!this.has("flags")) {
            return;
        }
        String string = this.group("flags");
        for (String string2 : string.split("\\s+")) {
            if (this.flags.add(Flag.valueOf(string2.toUpperCase(Locale.ENGLISH)))) continue;
            this.error("Repeated flag: " + string2);
        }
        if (ReflectionParser.containsDuplicates(this.flags, Flag.PUBLIC, Flag.PROTECTED, Flag.PRIVATE)) {
            this.error("Duplicate visibility flags");
        }
    }

    @SafeVarargs
    private static <T> boolean containsDuplicates(Collection<T> collection, T ... TArray) {
        boolean bl = false;
        for (T t : TArray) {
            if (!collection.contains(t)) continue;
            if (bl) {
                return true;
            }
            bl = true;
        }
        return false;
    }

    @SafeVarargs
    private static <T> boolean hasOneOf(Collection<T> collection, T ... TArray) {
        return Arrays.stream(TArray).anyMatch(collection::contains);
    }

    private void error(String string) {
        throw new ReflectionParserException(string + " in: " + this.declaration + " (RegEx: " + this.pattern.pattern() + "), (Imports: " + this.cachedImports + ')');
    }

    static {
        Arrays.asList(Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.TYPE, Character.TYPE, Void.TYPE, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class, Void.class, Object.class, String.class, CharSequence.class, StringBuilder.class, StringBuffer.class, UUID.class, Optional.class, Map.class, HashMap.class, Date.class, Calendar.class, Duration.class, TimeUnit.class, Path.class, Files.class, ConcurrentHashMap.class, Callable.class, Future.class, CompletableFuture.class, Throwable.class, Error.class, Exception.class, IllegalArgumentException.class, IllegalStateException.class).forEach(clazz -> PREDEFINED_TYPES.put(clazz.getSimpleName(), (Class<?>)clazz));
    }

    private static enum Flag {
        PUBLIC,
        PROTECTED,
        PRIVATE,
        FINAL,
        TRANSIENT,
        ABSTRACT,
        STATIC,
        NATIVE,
        SYNCHRONIZED,
        STRICTFP,
        VOLATILE;

        private static final String FLAGS_REGEX;

        static /* synthetic */ String access$000() {
            return FLAGS_REGEX;
        }

        static {
            FLAGS_REGEX = "(?<flags>(?:(?:" + Arrays.stream(Flag.values()).map(Enum::name).map(string -> string.toLowerCase(Locale.ENGLISH)).collect(Collectors.joining("|")) + ")\\s*)+)?";
        }
    }

    private static final class IDHandler {
        private boolean generic;
        private boolean array;
        private final String groupName;
        private final boolean isFullyQualified;

        private IDHandler(String string, boolean bl) {
            this.groupName = string;
            this.isFullyQualified = bl;
        }

        public IDHandler generic(boolean bl) {
            this.generic = bl;
            return this;
        }

        public IDHandler array(boolean bl) {
            this.array = bl;
            return this;
        }

        public String toString() {
            String string = (this.isFullyQualified ? "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*" : "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") + (this.generic ? ReflectionParser.GENERIC : "") + (this.array ? ReflectionParser.ARRAY : "");
            if (this.groupName == null) {
                return "(?:" + string + ')';
            }
            return "(?<" + this.groupName + '>' + string + ')';
        }
    }

    public static final class ReflectionParserException
    extends RuntimeException {
        public ReflectionParserException(String string) {
            super(string);
        }
    }
}

