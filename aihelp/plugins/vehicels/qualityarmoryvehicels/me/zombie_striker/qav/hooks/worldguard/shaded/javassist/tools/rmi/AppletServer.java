/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.ExportedObject;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.RemoteRef;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.StubGenerator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.web.Webserver;

public class AppletServer
extends Webserver {
    private StubGenerator stubGen;
    private Map<String, ExportedObject> exportedNames = new Hashtable<String, ExportedObject>();
    private List<ExportedObject> exportedObjects = new Vector<ExportedObject>();
    private static final byte[] okHeader = "HTTP/1.0 200 OK\r\n\r\n".getBytes();

    public AppletServer(String string) {
        this(Integer.parseInt(string));
    }

    public AppletServer(int n) {
        this(ClassPool.getDefault(), new StubGenerator(), n);
    }

    public AppletServer(int n, ClassPool classPool) {
        this(new ClassPool(classPool), new StubGenerator(), n);
    }

    private AppletServer(ClassPool classPool, StubGenerator stubGenerator, int n) {
        super(n);
        this.stubGen = stubGenerator;
        this.addTranslator(classPool, stubGenerator);
    }

    @Override
    public void run() {
        super.run();
    }

    public synchronized int exportObject(String string, Object object) {
        Class<?> clazz = object.getClass();
        ExportedObject exportedObject = new ExportedObject();
        exportedObject.object = object;
        exportedObject.methods = clazz.getMethods();
        this.exportedObjects.add(exportedObject);
        exportedObject.identifier = this.exportedObjects.size() - 1;
        if (string != null) {
            this.exportedNames.put(string, exportedObject);
        }
        try {
            this.stubGen.makeProxyClass(clazz);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
        return exportedObject.identifier;
    }

    @Override
    public void doReply(InputStream inputStream, OutputStream outputStream, String string) {
        if (string.startsWith("POST /rmi ")) {
            this.processRMI(inputStream, outputStream);
        } else if (string.startsWith("POST /lookup ")) {
            this.lookupName(string, inputStream, outputStream);
        } else {
            super.doReply(inputStream, outputStream, string);
        }
    }

    private void processRMI(InputStream inputStream, OutputStream outputStream) {
        Object object;
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        int n = objectInputStream.readInt();
        int n2 = objectInputStream.readInt();
        Exception exception = null;
        Object object2 = null;
        try {
            object = this.exportedObjects.get(n);
            Object[] objectArray = this.readParameters(objectInputStream);
            object2 = this.convertRvalue(((ExportedObject)object).methods[n2].invoke(((ExportedObject)object).object, objectArray));
        } catch (Exception exception2) {
            exception = exception2;
            this.logging2(exception2.toString());
        }
        outputStream.write(okHeader);
        object = new ObjectOutputStream(outputStream);
        if (exception != null) {
            ((ObjectOutputStream)object).writeBoolean(false);
            ((ObjectOutputStream)object).writeUTF(exception.toString());
        } else {
            try {
                ((ObjectOutputStream)object).writeBoolean(true);
                ((ObjectOutputStream)object).writeObject(object2);
            } catch (NotSerializableException notSerializableException) {
                this.logging2(notSerializableException.toString());
            } catch (InvalidClassException invalidClassException) {
                this.logging2(invalidClassException.toString());
            }
        }
        ((ObjectOutputStream)object).flush();
        ((ObjectOutputStream)object).close();
        objectInputStream.close();
    }

    private Object[] readParameters(ObjectInputStream objectInputStream) {
        int n = objectInputStream.readInt();
        Object[] objectArray = new Object[n];
        for (int i = 0; i < n; ++i) {
            Object object = objectInputStream.readObject();
            if (object instanceof RemoteRef) {
                RemoteRef remoteRef = (RemoteRef)object;
                ExportedObject exportedObject = this.exportedObjects.get(remoteRef.oid);
                object = exportedObject.object;
            }
            objectArray[i] = object;
        }
        return objectArray;
    }

    private Object convertRvalue(Object object) {
        if (object == null) {
            return null;
        }
        String string = object.getClass().getName();
        if (this.stubGen.isProxyClass(string)) {
            return new RemoteRef(this.exportObject(null, object), string);
        }
        return object;
    }

    private void lookupName(String string, InputStream inputStream, OutputStream outputStream) {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        String string2 = DataInputStream.readUTF(objectInputStream);
        ExportedObject exportedObject = this.exportedNames.get(string2);
        outputStream.write(okHeader);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        if (exportedObject == null) {
            this.logging2(string2 + "not found.");
            objectOutputStream.writeInt(-1);
            objectOutputStream.writeUTF("error");
        } else {
            this.logging2(string2);
            objectOutputStream.writeInt(exportedObject.identifier);
            objectOutputStream.writeUTF(exportedObject.object.getClass().getName());
        }
        objectOutputStream.flush();
        objectOutputStream.close();
        objectInputStream.close();
    }
}

