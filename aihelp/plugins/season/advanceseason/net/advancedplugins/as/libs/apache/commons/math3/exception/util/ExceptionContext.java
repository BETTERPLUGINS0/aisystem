/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception.util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.ArgUtils;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ExceptionContext
implements Serializable {
    private static final long serialVersionUID = -6024911025449780478L;
    private Throwable throwable;
    private List<Localizable> msgPatterns;
    private List<Object[]> msgArguments;
    private Map<String, Object> context;

    public ExceptionContext(Throwable throwable) {
        this.throwable = throwable;
        this.msgPatterns = new ArrayList<Localizable>();
        this.msgArguments = new ArrayList<Object[]>();
        this.context = new HashMap<String, Object>();
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void addMessage(Localizable localizable, Object ... objectArray) {
        this.msgPatterns.add(localizable);
        this.msgArguments.add(ArgUtils.flatten(objectArray));
    }

    public void setValue(String string, Object object) {
        this.context.put(string, object);
    }

    public Object getValue(String string) {
        return this.context.get(string);
    }

    public Set<String> getKeys() {
        return this.context.keySet();
    }

    public String getMessage() {
        return this.getMessage(Locale.US);
    }

    public String getLocalizedMessage() {
        return this.getMessage(Locale.getDefault());
    }

    public String getMessage(Locale locale) {
        return this.buildMessage(locale, ": ");
    }

    public String getMessage(Locale locale, String string) {
        return this.buildMessage(locale, string);
    }

    private String buildMessage(Locale locale, String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        int n2 = this.msgPatterns.size();
        for (int i = 0; i < n2; ++i) {
            Localizable localizable = this.msgPatterns.get(i);
            Object[] objectArray = this.msgArguments.get(i);
            MessageFormat messageFormat = new MessageFormat(localizable.getLocalizedString(locale), locale);
            stringBuilder.append(messageFormat.format(objectArray));
            if (++n >= n2) continue;
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeObject(this.throwable);
        this.serializeMessages(objectOutputStream);
        this.serializeContext(objectOutputStream);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.throwable = (Throwable)objectInputStream.readObject();
        this.deSerializeMessages(objectInputStream);
        this.deSerializeContext(objectInputStream);
    }

    private void serializeMessages(ObjectOutputStream objectOutputStream) {
        int n = this.msgPatterns.size();
        objectOutputStream.writeInt(n);
        for (int i = 0; i < n; ++i) {
            Localizable localizable = this.msgPatterns.get(i);
            objectOutputStream.writeObject(localizable);
            Object[] objectArray = this.msgArguments.get(i);
            int n2 = objectArray.length;
            objectOutputStream.writeInt(n2);
            for (int j = 0; j < n2; ++j) {
                if (objectArray[j] instanceof Serializable) {
                    objectOutputStream.writeObject(objectArray[j]);
                    continue;
                }
                objectOutputStream.writeObject(this.nonSerializableReplacement(objectArray[j]));
            }
        }
    }

    private void deSerializeMessages(ObjectInputStream objectInputStream) {
        int n = objectInputStream.readInt();
        this.msgPatterns = new ArrayList<Localizable>(n);
        this.msgArguments = new ArrayList<Object[]>(n);
        for (int i = 0; i < n; ++i) {
            Localizable localizable = (Localizable)objectInputStream.readObject();
            this.msgPatterns.add(localizable);
            int n2 = objectInputStream.readInt();
            Object[] objectArray = new Object[n2];
            for (int j = 0; j < n2; ++j) {
                objectArray[j] = objectInputStream.readObject();
            }
            this.msgArguments.add(objectArray);
        }
    }

    private void serializeContext(ObjectOutputStream objectOutputStream) {
        int n = this.context.size();
        objectOutputStream.writeInt(n);
        for (Map.Entry<String, Object> entry : this.context.entrySet()) {
            objectOutputStream.writeObject(entry.getKey());
            Object object = entry.getValue();
            if (object instanceof Serializable) {
                objectOutputStream.writeObject(object);
                continue;
            }
            objectOutputStream.writeObject(this.nonSerializableReplacement(object));
        }
    }

    private void deSerializeContext(ObjectInputStream objectInputStream) {
        int n = objectInputStream.readInt();
        this.context = new HashMap<String, Object>();
        for (int i = 0; i < n; ++i) {
            String string = (String)objectInputStream.readObject();
            Object object = objectInputStream.readObject();
            this.context.put(string, object);
        }
    }

    private String nonSerializableReplacement(Object object) {
        return "[Object could not be serialized: " + object.getClass().getName() + "]";
    }
}

