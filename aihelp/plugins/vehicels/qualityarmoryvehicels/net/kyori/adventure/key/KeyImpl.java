/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.RegExp
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.key;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

final class KeyImpl
implements Key {
    static final Comparator<? super Key> COMPARATOR = Comparator.comparing(Key::value).thenComparing(Key::namespace);
    @RegExp
    static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
    @RegExp
    static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";
    private final String namespace;
    private final String value;

    KeyImpl(@NotNull String string, @NotNull String string2) {
        KeyImpl.checkError("namespace", string, string2, Key.checkNamespace(string));
        KeyImpl.checkError("value", string, string2, Key.checkValue(string2));
        this.namespace = Objects.requireNonNull(string, "namespace");
        this.value = Objects.requireNonNull(string2, "value");
    }

    private static void checkError(String string, String string2, String string3, OptionalInt optionalInt) {
        if (optionalInt.isPresent()) {
            int n = optionalInt.getAsInt();
            char c = string3.charAt(n);
            throw new InvalidKeyException(string2, string3, String.format("Non [a-z0-9_.-] character in %s of Key[%s] at index %d ('%s', bytes: %s)", string, KeyImpl.asString(string2, string3), n, Character.valueOf(c), Arrays.toString(String.valueOf(c).getBytes(StandardCharsets.UTF_8))));
        }
    }

    static boolean allowedInNamespace(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.';
    }

    static boolean allowedInValue(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.' || c == '/';
    }

    @Override
    @NotNull
    public String namespace() {
        return this.namespace;
    }

    @Override
    @NotNull
    public String value() {
        return this.value;
    }

    @Override
    @NotNull
    public String asString() {
        return KeyImpl.asString(this.namespace, this.value);
    }

    @NotNull
    private static String asString(@NotNull String string, @NotNull String string2) {
        return string + ':' + string2;
    }

    @NotNull
    public String toString() {
        return this.asString();
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("namespace", this.namespace), ExaminableProperty.of("value", this.value));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Key)) {
            return false;
        }
        Key key = (Key)object;
        return Objects.equals(this.namespace, key.namespace()) && Objects.equals(this.value, key.value());
    }

    public int hashCode() {
        int n = this.namespace.hashCode();
        n = 31 * n + this.value.hashCode();
        return n;
    }

    @Override
    public int compareTo(@NotNull Key key) {
        return Key.super.compareTo(key);
    }
}

