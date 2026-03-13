/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Supplier;
import org.apache.commons.lang3.exception.UncheckedException;
import org.apache.commons.lang3.function.FailableBiConsumer;

public final class AppendableJoiner<T> {
    private final CharSequence prefix;
    private final CharSequence suffix;
    private final CharSequence delimiter;
    private final FailableBiConsumer<Appendable, T, IOException> appender;

    public static <T> Builder<T> builder() {
        return new Builder();
    }

    @SafeVarargs
    static <A extends Appendable, T> A joinA(A a2, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, FailableBiConsumer<Appendable, T, IOException> failableBiConsumer, T ... TArray) {
        return AppendableJoiner.joinArray(a2, charSequence, charSequence2, charSequence3, failableBiConsumer, TArray);
    }

    private static <A extends Appendable, T> A joinArray(A a2, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, FailableBiConsumer<Appendable, T, IOException> failableBiConsumer, T[] TArray) {
        a2.append(charSequence);
        if (TArray != null) {
            if (TArray.length > 0) {
                failableBiConsumer.accept(a2, (A)TArray[0]);
            }
            for (int i = 1; i < TArray.length; ++i) {
                a2.append(charSequence3);
                failableBiConsumer.accept(a2, (A)TArray[i]);
            }
        }
        a2.append(charSequence2);
        return a2;
    }

    static <T> StringBuilder joinI(StringBuilder stringBuilder, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, FailableBiConsumer<Appendable, T, IOException> failableBiConsumer, Iterable<T> iterable) {
        try {
            return AppendableJoiner.joinIterable(stringBuilder, charSequence, charSequence2, charSequence3, failableBiConsumer, iterable);
        } catch (IOException iOException) {
            throw new UncheckedException(iOException);
        }
    }

    private static <A extends Appendable, T> A joinIterable(A a2, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, FailableBiConsumer<Appendable, T, IOException> failableBiConsumer, Iterable<T> iterable) {
        a2.append(charSequence);
        if (iterable != null) {
            Iterator<T> iterator = iterable.iterator();
            if (iterator.hasNext()) {
                failableBiConsumer.accept(a2, (A)iterator.next());
            }
            while (iterator.hasNext()) {
                a2.append(charSequence3);
                failableBiConsumer.accept(a2, (A)iterator.next());
            }
        }
        a2.append(charSequence2);
        return a2;
    }

    @SafeVarargs
    static <T> StringBuilder joinSB(StringBuilder stringBuilder, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, FailableBiConsumer<Appendable, T, IOException> failableBiConsumer, T ... TArray) {
        try {
            return AppendableJoiner.joinArray(stringBuilder, charSequence, charSequence2, charSequence3, failableBiConsumer, TArray);
        } catch (IOException iOException) {
            throw new UncheckedException(iOException);
        }
    }

    private static CharSequence nonNull(CharSequence charSequence) {
        return charSequence != null ? charSequence : "";
    }

    private AppendableJoiner(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, FailableBiConsumer<Appendable, T, IOException> failableBiConsumer) {
        this.prefix = AppendableJoiner.nonNull(charSequence);
        this.suffix = AppendableJoiner.nonNull(charSequence2);
        this.delimiter = AppendableJoiner.nonNull(charSequence3);
        this.appender = failableBiConsumer != null ? failableBiConsumer : (appendable, object) -> appendable.append(String.valueOf(object));
    }

    public StringBuilder join(StringBuilder stringBuilder, Iterable<T> iterable) {
        return AppendableJoiner.joinI(stringBuilder, this.prefix, this.suffix, this.delimiter, this.appender, iterable);
    }

    public StringBuilder join(StringBuilder stringBuilder, T ... TArray) {
        return AppendableJoiner.joinSB(stringBuilder, this.prefix, this.suffix, this.delimiter, this.appender, TArray);
    }

    public <A extends Appendable> A joinA(A a2, Iterable<T> iterable) {
        return AppendableJoiner.joinIterable(a2, this.prefix, this.suffix, this.delimiter, this.appender, iterable);
    }

    public <A extends Appendable> A joinA(A a2, T ... TArray) {
        return AppendableJoiner.joinA(a2, this.prefix, this.suffix, this.delimiter, this.appender, TArray);
    }

    public static final class Builder<T>
    implements Supplier<AppendableJoiner<T>> {
        private CharSequence prefix;
        private CharSequence suffix;
        private CharSequence delimiter;
        private FailableBiConsumer<Appendable, T, IOException> appender;

        Builder() {
        }

        @Override
        public AppendableJoiner<T> get() {
            return new AppendableJoiner(this.prefix, this.suffix, this.delimiter, this.appender);
        }

        public Builder<T> setDelimiter(CharSequence charSequence) {
            this.delimiter = charSequence;
            return this;
        }

        public Builder<T> setElementAppender(FailableBiConsumer<Appendable, T, IOException> failableBiConsumer) {
            this.appender = failableBiConsumer;
            return this;
        }

        public Builder<T> setPrefix(CharSequence charSequence) {
            this.prefix = charSequence;
            return this;
        }

        public Builder<T> setSuffix(CharSequence charSequence) {
            this.suffix = charSequence;
            return this;
        }
    }
}

