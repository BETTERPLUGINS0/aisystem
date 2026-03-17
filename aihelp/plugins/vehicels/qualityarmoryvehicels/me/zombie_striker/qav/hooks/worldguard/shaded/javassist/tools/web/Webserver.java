/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Translator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.web.BadHttpRequest;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.web.ServiceThread;

public class Webserver {
    private ServerSocket socket;
    private ClassPool classPool;
    protected Translator translator;
    private static final byte[] endofline = new byte[]{13, 10};
    private static final int typeHtml = 1;
    private static final int typeClass = 2;
    private static final int typeGif = 3;
    private static final int typeJpeg = 4;
    private static final int typeText = 5;
    public String debugDir = null;
    public String htmlfileBase = null;

    public static void main(String[] stringArray) {
        if (stringArray.length == 1) {
            Webserver webserver = new Webserver(stringArray[0]);
            webserver.run();
        } else {
            System.err.println("Usage: java javassist.tools.web.Webserver <port number>");
        }
    }

    public Webserver(String string) {
        this(Integer.parseInt(string));
    }

    public Webserver(int n) {
        this.socket = new ServerSocket(n);
        this.classPool = null;
        this.translator = null;
    }

    public void setClassPool(ClassPool classPool) {
        this.classPool = classPool;
    }

    public void addTranslator(ClassPool classPool, Translator translator) {
        this.classPool = classPool;
        this.translator = translator;
        translator.start(this.classPool);
    }

    public void end() {
        this.socket.close();
    }

    public void logging(String string) {
        System.out.println(string);
    }

    public void logging(String string, String string2) {
        System.out.print(string);
        System.out.print(" ");
        System.out.println(string2);
    }

    public void logging(String string, String string2, String string3) {
        System.out.print(string);
        System.out.print(" ");
        System.out.print(string2);
        System.out.print(" ");
        System.out.println(string3);
    }

    public void logging2(String string) {
        System.out.print("    ");
        System.out.println(string);
    }

    public void run() {
        System.err.println("ready to service...");
        while (true) {
            try {
                while (true) {
                    ServiceThread serviceThread = new ServiceThread(this, this.socket.accept());
                    serviceThread.start();
                }
            } catch (IOException iOException) {
                this.logging(iOException.toString());
                continue;
            }
            break;
        }
    }

    final void process(Socket socket) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        String string = this.readLine(bufferedInputStream);
        this.logging(socket.getInetAddress().getHostName(), new Date().toString(), string);
        while (this.skipLine(bufferedInputStream) > 0) {
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        try {
            this.doReply(bufferedInputStream, bufferedOutputStream, string);
        } catch (BadHttpRequest badHttpRequest) {
            this.replyError(bufferedOutputStream, badHttpRequest);
        }
        ((OutputStream)bufferedOutputStream).flush();
        ((InputStream)bufferedInputStream).close();
        ((OutputStream)bufferedOutputStream).close();
        socket.close();
    }

    private String readLine(InputStream inputStream) {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        while ((n = inputStream.read()) >= 0 && n != 13) {
            stringBuilder.append((char)n);
        }
        inputStream.read();
        return stringBuilder.toString();
    }

    private int skipLine(InputStream inputStream) {
        int n;
        int n2 = 0;
        while ((n = inputStream.read()) >= 0 && n != 13) {
            ++n2;
        }
        inputStream.read();
        return n2;
    }

    public void doReply(InputStream inputStream, OutputStream outputStream, String string) {
        InputStream inputStream2;
        File file;
        String string2;
        if (!string.startsWith("GET /")) {
            throw new BadHttpRequest();
        }
        String string3 = string2 = string.substring(5, string.indexOf(32, 5));
        int n = string3.endsWith(".class") ? 2 : (string3.endsWith(".html") || string3.endsWith(".htm") ? 1 : (string3.endsWith(".gif") ? 3 : (string3.endsWith(".jpg") ? 4 : 5)));
        int n2 = string3.length();
        if (n == 2 && this.letUsersSendClassfile(outputStream, string3, n2)) {
            return;
        }
        this.checkFilename(string3, n2);
        if (this.htmlfileBase != null) {
            string3 = this.htmlfileBase + string3;
        }
        if (File.separatorChar != '/') {
            string3 = string3.replace('/', File.separatorChar);
        }
        if ((file = new File(string3)).canRead()) {
            this.sendHeader(outputStream, file.length(), n);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] byArray = new byte[4096];
            while ((n2 = fileInputStream.read(byArray)) > 0) {
                outputStream.write(byArray, 0, n2);
            }
            fileInputStream.close();
            return;
        }
        if (n == 2 && (inputStream2 = this.getClass().getResourceAsStream("/" + string2)) != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byArray = new byte[4096];
            while ((n2 = inputStream2.read(byArray)) > 0) {
                byteArrayOutputStream.write(byArray, 0, n2);
            }
            byte[] byArray2 = byteArrayOutputStream.toByteArray();
            this.sendHeader(outputStream, byArray2.length, 2);
            outputStream.write(byArray2);
            inputStream2.close();
            return;
        }
        throw new BadHttpRequest();
    }

    private void checkFilename(String string, int n) {
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (Character.isJavaIdentifierPart(c) || c == '.' || c == '/') continue;
            throw new BadHttpRequest();
        }
        if (string.indexOf("..") >= 0) {
            throw new BadHttpRequest();
        }
    }

    private boolean letUsersSendClassfile(OutputStream outputStream, String string, int n) {
        byte[] byArray;
        if (this.classPool == null) {
            return false;
        }
        String string2 = string.substring(0, n - 6).replace('/', '.');
        try {
            if (this.translator != null) {
                this.translator.onLoad(this.classPool, string2);
            }
            CtClass ctClass = this.classPool.get(string2);
            byArray = ctClass.toBytecode();
            if (this.debugDir != null) {
                ctClass.writeFile(this.debugDir);
            }
        } catch (Exception exception) {
            throw new BadHttpRequest(exception);
        }
        this.sendHeader(outputStream, byArray.length, 2);
        outputStream.write(byArray);
        return true;
    }

    private void sendHeader(OutputStream outputStream, long l, int n) {
        outputStream.write("HTTP/1.0 200 OK".getBytes());
        outputStream.write(endofline);
        outputStream.write("Content-Length: ".getBytes());
        outputStream.write(Long.toString(l).getBytes());
        outputStream.write(endofline);
        if (n == 2) {
            outputStream.write("Content-Type: application/octet-stream".getBytes());
        } else if (n == 1) {
            outputStream.write("Content-Type: text/html".getBytes());
        } else if (n == 3) {
            outputStream.write("Content-Type: image/gif".getBytes());
        } else if (n == 4) {
            outputStream.write("Content-Type: image/jpg".getBytes());
        } else if (n == 5) {
            outputStream.write("Content-Type: text/plain".getBytes());
        }
        outputStream.write(endofline);
        outputStream.write(endofline);
    }

    private void replyError(OutputStream outputStream, BadHttpRequest badHttpRequest) {
        this.logging2("bad request: " + badHttpRequest.toString());
        outputStream.write("HTTP/1.0 400 Bad Request".getBytes());
        outputStream.write(endofline);
        outputStream.write(endofline);
        outputStream.write("<H1>Bad Request</H1>".getBytes());
    }
}

