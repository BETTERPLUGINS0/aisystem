/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.generic;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.CancellablePrompt;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NumbersPrompt<T extends Number>
extends CancellablePrompt {
    private final Pattern regex;
    private final int numNumbers;

    public NumbersPrompt(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append("(-?\\d+(?:\\.\\d+)?)");
            if (i >= n - 1) continue;
            stringBuilder.append(",\\s?");
        }
        this.regex = Pattern.compile(stringBuilder.toString());
        this.numNumbers = n;
    }

    @Override
    protected boolean isInputFullyValid(@NotNull ConversationContext conversationContext, @NotNull String string) {
        return this.regex.matcher(string).matches();
    }

    @Override
    protected Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @Nullable String string) {
        if (string == null) {
            return END_OF_CONVERSATION;
        }
        Matcher matcher = this.regex.matcher(string);
        if (!matcher.matches()) {
            throw new IllegalStateException("Input is not fully valid, but was accepted as valid. Input: '" + string + "', regex: '" + this.regex.pattern() + "'");
        }
        ArrayList<T> arrayList = new ArrayList<T>();
        try {
            for (int i = 1; i <= matcher.groupCount(); ++i) {
                arrayList.add(this.parseFromString(matcher.group(i)));
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            return END_OF_CONVERSATION;
        }
        if (arrayList.size() != this.numNumbers) {
            conversationContext.getForWhom().sendRawMessage(Locale.getMessage(PluginMessage.PROMPTS_GENERAL_NUMBER_INVALID, (Map<String, String>)Map.of((Object)"%num%", (Object)String.valueOf(this.numNumbers), (Object)"%input%", (Object)String.valueOf(arrayList.size()))));
            return END_OF_CONVERSATION;
        }
        return this.acceptFullyValidatedNumbersInput(conversationContext, arrayList);
    }

    protected abstract Prompt acceptFullyValidatedNumbersInput(@NotNull ConversationContext var1, @NotNull List<T> var2);

    private T parseFromString(@NotNull String string) {
        Class clazz = (Class)((ParameterizedType)((Object)((Object)this)).getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (Integer.class.isAssignableFrom(clazz)) {
            return (T)Integer.valueOf(string);
        }
        if (Double.class.isAssignableFrom(clazz)) {
            return (T)Double.valueOf(string);
        }
        if (Float.class.isAssignableFrom(clazz)) {
            return (T)Float.valueOf(string);
        }
        if (Long.class.isAssignableFrom(clazz)) {
            return (T)Long.valueOf(string);
        }
        if (Short.class.isAssignableFrom(clazz)) {
            return (T)Short.valueOf(string);
        }
        if (Byte.class.isAssignableFrom(clazz)) {
            return (T)Byte.valueOf(string);
        }
        throw new IllegalArgumentException("Could not parse input '" + string + "' to a number.");
    }
}

