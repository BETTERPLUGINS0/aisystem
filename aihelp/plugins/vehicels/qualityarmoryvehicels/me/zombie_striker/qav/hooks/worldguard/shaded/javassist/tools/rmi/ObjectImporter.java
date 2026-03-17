/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

import java.applet.Applet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.URL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.ObjectNotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.Proxy;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.RemoteException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.RemoteRef;

public class ObjectImporter
implements Serializable {
    private static final long serialVersionUID = 1L;
    private final byte[] endofline = new byte[]{13, 10};
    private String servername;
    private String orgServername;
    private int port;
    private int orgPort;
    protected byte[] lookupCommand = "POST /lookup HTTP/1.0".getBytes();
    protected byte[] rmiCommand = "POST /rmi HTTP/1.0".getBytes();
    private static final Class<?>[] proxyConstructorParamTypes = new Class[]{ObjectImporter.class, Integer.TYPE};

    public ObjectImporter(Applet applet) {
        URL uRL = applet.getCodeBase();
        this.orgServername = this.servername = uRL.getHost();
        this.orgPort = this.port = uRL.getPort();
    }

    public ObjectImporter(String string, int n) {
        this.orgServername = this.servername = string;
        this.orgPort = this.port = n;
    }

    public Object getObject(String string) {
        try {
            return this.lookupObject(string);
        } catch (ObjectNotFoundException objectNotFoundException) {
            return null;
        }
    }

    public void setHttpProxy(String string, int n) {
        String string2 = "POST http://" + this.orgServername + ":" + this.orgPort;
        String string3 = string2 + "/lookup HTTP/1.0";
        this.lookupCommand = string3.getBytes();
        string3 = string2 + "/rmi HTTP/1.0";
        this.rmiCommand = string3.getBytes();
        this.servername = string;
        this.port = n;
    }

    public Object lookupObject(String string) {
        try {
            Socket socket = new Socket(this.servername, this.port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(this.lookupCommand);
            outputStream.write(this.endofline);
            outputStream.write(this.endofline);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeUTF(string);
            objectOutputStream.flush();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            this.skipHeader(bufferedInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
            int n = objectInputStream.readInt();
            String string2 = objectInputStream.readUTF();
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            if (n >= 0) {
                return this.createProxy(n, string2);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ObjectNotFoundException(string, exception);
        }
        throw new ObjectNotFoundException(string);
    }

    private Object createProxy(int n, String string) {
        Class<?> clazz = Class.forName(string);
        Constructor<?> constructor = clazz.getConstructor(proxyConstructorParamTypes);
        return constructor.newInstance(this, n);
    }

    public Object call(int n, int n2, Object[] objectArray) {
        String string;
        Object object;
        boolean bl;
        try {
            Socket socket = new Socket(this.servername, this.port);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            ((OutputStream)bufferedOutputStream).write(this.rmiCommand);
            ((OutputStream)bufferedOutputStream).write(this.endofline);
            ((OutputStream)bufferedOutputStream).write(this.endofline);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            objectOutputStream.writeInt(n);
            objectOutputStream.writeInt(n2);
            this.writeParameters(objectOutputStream, objectArray);
            objectOutputStream.flush();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            this.skipHeader(bufferedInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
            bl = objectInputStream.readBoolean();
            object = null;
            string = null;
            if (bl) {
                object = objectInputStream.readObject();
            } else {
                string = objectInputStream.readUTF();
            }
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            if (object instanceof RemoteRef) {
                RemoteRef remoteRef = (RemoteRef)object;
                object = this.createProxy(remoteRef.oid, remoteRef.classname);
            }
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RemoteException(classNotFoundException);
        } catch (IOException iOException) {
            throw new RemoteException(iOException);
        } catch (Exception exception) {
            throw new RemoteException(exception);
        }
        if (bl) {
            return object;
        }
        throw new RemoteException(string);
    }

    private void skipHeader(InputStream inputStream) {
        int n;
        do {
            int n2;
            n = 0;
            while ((n2 = inputStream.read()) >= 0 && n2 != 13) {
                ++n;
            }
            inputStream.read();
        } while (n > 0);
    }

    private void writeParameters(ObjectOutputStream objectOutputStream, Object[] objectArray) {
        int n = objectArray.length;
        objectOutputStream.writeInt(n);
        for (int i = 0; i < n; ++i) {
            if (objectArray[i] instanceof Proxy) {
                Proxy proxy = (Proxy)objectArray[i];
                objectOutputStream.writeObject(new RemoteRef(proxy._getObjectId()));
                continue;
            }
            objectOutputStream.writeObject(objectArray[i]);
        }
    }
}

