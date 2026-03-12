package javax.mail;

import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailLogger;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.logging.Level;

public final class Session {
   private final Properties props;
   private final Authenticator authenticator;
   private final Hashtable<URLName, PasswordAuthentication> authTable = new Hashtable();
   private boolean debug = false;
   private PrintStream out;
   private MailLogger logger;
   private List<Provider> providers;
   private final Map<String, Provider> providersByProtocol = new HashMap();
   private final Map<String, Provider> providersByClassName = new HashMap();
   private final Properties addressMap = new Properties();
   private boolean loadedProviders;
   private final EventQueue q;
   private static Session defaultSession = null;
   private static final String confDir;

   private Session(Properties props, Authenticator authenticator) {
      this.props = props;
      this.authenticator = authenticator;
      if (Boolean.valueOf(props.getProperty("mail.debug"))) {
         this.debug = true;
      }

      this.initLogger();
      this.logger.log(Level.CONFIG, "JavaMail version {0}", (Object)"1.6.2");
      Class cl;
      if (authenticator != null) {
         cl = authenticator.getClass();
      } else {
         cl = this.getClass();
      }

      this.loadAddressMap(cl);
      this.q = new EventQueue((Executor)props.get("mail.event.executor"));
   }

   private final synchronized void initLogger() {
      this.logger = new MailLogger(this.getClass(), "DEBUG", this.debug, this.getDebugOut());
   }

   public static Session getInstance(Properties props, Authenticator authenticator) {
      return new Session(props, authenticator);
   }

   public static Session getInstance(Properties props) {
      return new Session(props, (Authenticator)null);
   }

   public static synchronized Session getDefaultInstance(Properties props, Authenticator authenticator) {
      if (defaultSession == null) {
         SecurityManager security = System.getSecurityManager();
         if (security != null) {
            security.checkSetFactory();
         }

         defaultSession = new Session(props, authenticator);
      } else if (defaultSession.authenticator != authenticator && (defaultSession.authenticator == null || authenticator == null || defaultSession.authenticator.getClass().getClassLoader() != authenticator.getClass().getClassLoader())) {
         throw new SecurityException("Access to default session denied");
      }

      return defaultSession;
   }

   public static Session getDefaultInstance(Properties props) {
      return getDefaultInstance(props, (Authenticator)null);
   }

   public synchronized void setDebug(boolean debug) {
      this.debug = debug;
      this.initLogger();
      this.logger.log(Level.CONFIG, "setDebug: JavaMail version {0}", (Object)"1.6.2");
   }

   public synchronized boolean getDebug() {
      return this.debug;
   }

   public synchronized void setDebugOut(PrintStream out) {
      this.out = out;
      this.initLogger();
   }

   public synchronized PrintStream getDebugOut() {
      return this.out == null ? System.out : this.out;
   }

   public synchronized Provider[] getProviders() {
      List<Provider> plist = new ArrayList();
      boolean needFallback = true;
      ServiceLoader<Provider> loader = ServiceLoader.load(Provider.class);

      for(Iterator var4 = loader.iterator(); var4.hasNext(); needFallback = false) {
         Provider p = (Provider)var4.next();
         plist.add(p);
      }

      if (!this.loadedProviders) {
         this.loadProviders(needFallback);
      }

      if (this.providers != null) {
         plist.addAll(this.providers);
      }

      Provider[] _providers = new Provider[plist.size()];
      plist.toArray(_providers);
      return _providers;
   }

   public synchronized Provider getProvider(String protocol) throws NoSuchProviderException {
      if (protocol != null && protocol.length() > 0) {
         Provider _provider = null;
         String _className = this.props.getProperty("mail." + protocol + ".class");
         if (_className != null) {
            if (this.logger.isLoggable(Level.FINE)) {
               this.logger.fine("mail." + protocol + ".class property exists and points to " + _className);
            }

            _provider = this.getProviderByClassName(_className);
         }

         if (_provider == null) {
            _provider = this.getProviderByProtocol(protocol);
         }

         if (_provider == null) {
            throw new NoSuchProviderException("No provider for " + protocol);
         } else {
            if (this.logger.isLoggable(Level.FINE)) {
               this.logger.fine("getProvider() returning " + _provider.toString());
            }

            return _provider;
         }
      } else {
         throw new NoSuchProviderException("Invalid protocol: null");
      }
   }

   public synchronized void setProvider(Provider provider) throws NoSuchProviderException {
      if (provider == null) {
         throw new NoSuchProviderException("Can't set null provider");
      } else {
         this.providersByProtocol.put(provider.getProtocol(), provider);
         this.providersByClassName.put(provider.getClassName(), provider);
         this.props.put("mail." + provider.getProtocol() + ".class", provider.getClassName());
      }
   }

   public Store getStore() throws NoSuchProviderException {
      return this.getStore(this.getProperty("mail.store.protocol"));
   }

   public Store getStore(String protocol) throws NoSuchProviderException {
      return this.getStore(new URLName(protocol, (String)null, -1, (String)null, (String)null, (String)null));
   }

   public Store getStore(URLName url) throws NoSuchProviderException {
      String protocol = url.getProtocol();
      Provider p = this.getProvider(protocol);
      return this.getStore(p, url);
   }

   public Store getStore(Provider provider) throws NoSuchProviderException {
      return this.getStore(provider, (URLName)null);
   }

   private Store getStore(Provider provider, URLName url) throws NoSuchProviderException {
      if (provider != null && provider.getType() == Provider.Type.STORE) {
         return (Store)this.getService(provider, url, Store.class);
      } else {
         throw new NoSuchProviderException("invalid provider");
      }
   }

   public Folder getFolder(URLName url) throws MessagingException {
      Store store = this.getStore(url);
      store.connect();
      return store.getFolder(url);
   }

   public Transport getTransport() throws NoSuchProviderException {
      String prot = this.getProperty("mail.transport.protocol");
      if (prot != null) {
         return this.getTransport(prot);
      } else {
         prot = (String)this.addressMap.get("rfc822");
         return prot != null ? this.getTransport(prot) : this.getTransport("smtp");
      }
   }

   public Transport getTransport(String protocol) throws NoSuchProviderException {
      return this.getTransport(new URLName(protocol, (String)null, -1, (String)null, (String)null, (String)null));
   }

   public Transport getTransport(URLName url) throws NoSuchProviderException {
      String protocol = url.getProtocol();
      Provider p = this.getProvider(protocol);
      return this.getTransport(p, url);
   }

   public Transport getTransport(Provider provider) throws NoSuchProviderException {
      return this.getTransport(provider, (URLName)null);
   }

   public Transport getTransport(Address address) throws NoSuchProviderException {
      String transportProtocol = this.getProperty("mail.transport.protocol." + address.getType());
      if (transportProtocol != null) {
         return this.getTransport(transportProtocol);
      } else {
         transportProtocol = (String)this.addressMap.get(address.getType());
         if (transportProtocol != null) {
            return this.getTransport(transportProtocol);
         } else {
            throw new NoSuchProviderException("No provider for Address type: " + address.getType());
         }
      }
   }

   private Transport getTransport(Provider provider, URLName url) throws NoSuchProviderException {
      if (provider != null && provider.getType() == Provider.Type.TRANSPORT) {
         return (Transport)this.getService(provider, url, Transport.class);
      } else {
         throw new NoSuchProviderException("invalid provider");
      }
   }

   private <T extends Service> T getService(Provider provider, URLName url, Class<T> type) throws NoSuchProviderException {
      if (provider == null) {
         throw new NoSuchProviderException("null");
      } else {
         if (url == null) {
            url = new URLName(provider.getProtocol(), (String)null, -1, (String)null, (String)null, (String)null);
         }

         Object service = null;
         ClassLoader cl;
         if (this.authenticator != null) {
            cl = this.authenticator.getClass().getClassLoader();
         } else {
            cl = this.getClass().getClassLoader();
         }

         Class serviceClass = null;

         try {
            ClassLoader ccl = getContextClassLoader();
            if (ccl != null) {
               try {
                  serviceClass = Class.forName(provider.getClassName(), false, ccl);
               } catch (ClassNotFoundException var12) {
               }
            }

            if (serviceClass == null || !type.isAssignableFrom(serviceClass)) {
               serviceClass = Class.forName(provider.getClassName(), false, cl);
            }

            if (!type.isAssignableFrom(serviceClass)) {
               throw new ClassCastException(type.getName() + " " + serviceClass.getName());
            }
         } catch (Exception var13) {
            try {
               serviceClass = Class.forName(provider.getClassName());
               if (!type.isAssignableFrom(serviceClass)) {
                  throw new ClassCastException(type.getName() + " " + serviceClass.getName());
               }
            } catch (Exception var11) {
               this.logger.log(Level.FINE, "Exception loading provider", (Throwable)var11);
               throw new NoSuchProviderException(provider.getProtocol());
            }
         }

         try {
            Class<?>[] c = new Class[]{Session.class, URLName.class};
            Constructor<?> cons = serviceClass.getConstructor(c);
            Object[] o = new Object[]{this, url};
            service = cons.newInstance(o);
         } catch (Exception var10) {
            this.logger.log(Level.FINE, "Exception loading provider", (Throwable)var10);
            throw new NoSuchProviderException(provider.getProtocol());
         }

         return (Service)type.cast(service);
      }
   }

   public void setPasswordAuthentication(URLName url, PasswordAuthentication pw) {
      if (pw == null) {
         this.authTable.remove(url);
      } else {
         this.authTable.put(url, pw);
      }

   }

   public PasswordAuthentication getPasswordAuthentication(URLName url) {
      return (PasswordAuthentication)this.authTable.get(url);
   }

   public PasswordAuthentication requestPasswordAuthentication(InetAddress addr, int port, String protocol, String prompt, String defaultUserName) {
      return this.authenticator != null ? this.authenticator.requestPasswordAuthentication(addr, port, protocol, prompt, defaultUserName) : null;
   }

   public Properties getProperties() {
      return this.props;
   }

   public String getProperty(String name) {
      return this.props.getProperty(name);
   }

   private Provider getProviderByClassName(String className) {
      Provider p = (Provider)this.providersByClassName.get(className);
      if (p != null) {
         return p;
      } else {
         ServiceLoader<Provider> loader = ServiceLoader.load(Provider.class);
         Iterator var4 = loader.iterator();

         Provider pp;
         do {
            if (!var4.hasNext()) {
               if (!this.loadedProviders) {
                  this.loadProviders(true);
                  p = (Provider)this.providersByClassName.get(className);
               }

               return p;
            }

            pp = (Provider)var4.next();
         } while(!className.equals(pp.getClassName()));

         return pp;
      }
   }

   private Provider getProviderByProtocol(String protocol) {
      Provider p = (Provider)this.providersByProtocol.get(protocol);
      if (p != null) {
         return p;
      } else {
         ServiceLoader<Provider> loader = ServiceLoader.load(Provider.class);
         Iterator var4 = loader.iterator();

         Provider pp;
         do {
            if (!var4.hasNext()) {
               if (!this.loadedProviders) {
                  this.loadProviders(true);
                  p = (Provider)this.providersByProtocol.get(protocol);
               }

               return p;
            }

            pp = (Provider)var4.next();
         } while(!protocol.equals(pp.getProtocol()));

         return pp;
      }
   }

   private void loadProviders(boolean fallback) {
      StreamLoader loader = new StreamLoader() {
         public void load(InputStream is) throws IOException {
            Session.this.loadProvidersFromStream(is);
         }
      };

      try {
         if (confDir != null) {
            this.loadFile(confDir + "javamail.providers", loader);
         }
      } catch (SecurityException var4) {
      }

      Class cl;
      if (this.authenticator != null) {
         cl = this.authenticator.getClass();
      } else {
         cl = this.getClass();
      }

      this.loadAllResources("META-INF/javamail.providers", cl, loader);
      this.loadResource("/META-INF/javamail.default.providers", cl, loader, false);
      if ((this.providers == null || this.providers.size() == 0) && fallback) {
         this.logger.config("failed to load any providers, using defaults");
         this.addProvider(new Provider(Provider.Type.STORE, "imap", "com.sun.mail.imap.IMAPStore", "Oracle", "1.6.2"));
         this.addProvider(new Provider(Provider.Type.STORE, "imaps", "com.sun.mail.imap.IMAPSSLStore", "Oracle", "1.6.2"));
         this.addProvider(new Provider(Provider.Type.STORE, "pop3", "com.sun.mail.pop3.POP3Store", "Oracle", "1.6.2"));
         this.addProvider(new Provider(Provider.Type.STORE, "pop3s", "com.sun.mail.pop3.POP3SSLStore", "Oracle", "1.6.2"));
         this.addProvider(new Provider(Provider.Type.TRANSPORT, "smtp", "com.sun.mail.smtp.SMTPTransport", "Oracle", "1.6.2"));
         this.addProvider(new Provider(Provider.Type.TRANSPORT, "smtps", "com.sun.mail.smtp.SMTPSSLTransport", "Oracle", "1.6.2"));
      }

      if (this.logger.isLoggable(Level.CONFIG)) {
         this.logger.config("Tables of loaded providers from javamail.providers");
         this.logger.config("Providers Listed By Class Name: " + this.providersByClassName.toString());
         this.logger.config("Providers Listed By Protocol: " + this.providersByProtocol.toString());
      }

      this.loadedProviders = true;
   }

   private void loadProvidersFromStream(InputStream is) throws IOException {
      if (is != null) {
         LineInputStream lis = new LineInputStream(is);

         while(true) {
            while(true) {
               String currLine;
               do {
                  do {
                     if ((currLine = lis.readLine()) == null) {
                        return;
                     }
                  } while(currLine.startsWith("#"));
               } while(currLine.trim().length() == 0);

               Provider.Type type = null;
               String protocol = null;
               String className = null;
               String vendor = null;
               String version = null;
               StringTokenizer tuples = new StringTokenizer(currLine, ";");

               while(tuples.hasMoreTokens()) {
                  String currTuple = tuples.nextToken().trim();
                  int sep = currTuple.indexOf("=");
                  if (currTuple.startsWith("protocol=")) {
                     protocol = currTuple.substring(sep + 1);
                  } else if (currTuple.startsWith("type=")) {
                     String strType = currTuple.substring(sep + 1);
                     if (strType.equalsIgnoreCase("store")) {
                        type = Provider.Type.STORE;
                     } else if (strType.equalsIgnoreCase("transport")) {
                        type = Provider.Type.TRANSPORT;
                     }
                  } else if (currTuple.startsWith("class=")) {
                     className = currTuple.substring(sep + 1);
                  } else if (currTuple.startsWith("vendor=")) {
                     vendor = currTuple.substring(sep + 1);
                  } else if (currTuple.startsWith("version=")) {
                     version = currTuple.substring(sep + 1);
                  }
               }

               if (type != null && protocol != null && className != null && protocol.length() > 0 && className.length() > 0) {
                  Provider provider = new Provider(type, protocol, className, vendor, version);
                  this.addProvider(provider);
               } else {
                  this.logger.log(Level.CONFIG, "Bad provider entry: {0}", (Object)currLine);
               }
            }
         }
      }
   }

   public synchronized void addProvider(Provider provider) {
      if (this.providers == null) {
         this.providers = new ArrayList();
      }

      this.providers.add(provider);
      this.providersByClassName.put(provider.getClassName(), provider);
      if (!this.providersByProtocol.containsKey(provider.getProtocol())) {
         this.providersByProtocol.put(provider.getProtocol(), provider);
      }

   }

   private void loadAddressMap(Class<?> cl) {
      StreamLoader loader = new StreamLoader() {
         public void load(InputStream is) throws IOException {
            Session.this.addressMap.load(is);
         }
      };
      this.loadResource("/META-INF/javamail.default.address.map", cl, loader, true);
      this.loadAllResources("META-INF/javamail.address.map", cl, loader);

      try {
         if (confDir != null) {
            this.loadFile(confDir + "javamail.address.map", loader);
         }
      } catch (SecurityException var4) {
      }

      if (this.addressMap.isEmpty()) {
         this.logger.config("failed to load address map, using defaults");
         this.addressMap.put("rfc822", "smtp");
      }

   }

   public synchronized void setProtocolForAddress(String addresstype, String protocol) {
      if (protocol == null) {
         this.addressMap.remove(addresstype);
      } else {
         this.addressMap.put(addresstype, protocol);
      }

   }

   private void loadFile(String name, StreamLoader loader) {
      BufferedInputStream clis = null;

      try {
         clis = new BufferedInputStream(new FileInputStream(name));
         loader.load(clis);
         this.logger.log(Level.CONFIG, "successfully loaded file: {0}", (Object)name);
      } catch (FileNotFoundException var17) {
      } catch (IOException var18) {
         if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.log(Level.CONFIG, "not loading file: " + name, (Throwable)var18);
         }
      } catch (SecurityException var19) {
         if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.log(Level.CONFIG, "not loading file: " + name, (Throwable)var19);
         }
      } finally {
         try {
            if (clis != null) {
               clis.close();
            }
         } catch (IOException var16) {
         }

      }

   }

   private void loadResource(String name, Class<?> cl, StreamLoader loader, boolean expected) {
      InputStream clis = null;

      try {
         clis = getResourceAsStream(cl, name);
         if (clis != null) {
            loader.load(clis);
            this.logger.log(Level.CONFIG, "successfully loaded resource: {0}", (Object)name);
         } else if (expected) {
            this.logger.log(Level.WARNING, "expected resource not found: {0}", (Object)name);
         }
      } catch (IOException var17) {
         this.logger.log(Level.CONFIG, "Exception loading resource", (Throwable)var17);
      } catch (SecurityException var18) {
         this.logger.log(Level.CONFIG, "Exception loading resource", (Throwable)var18);
      } finally {
         try {
            if (clis != null) {
               clis.close();
            }
         } catch (IOException var16) {
         }

      }

   }

   private void loadAllResources(String name, Class<?> cl, StreamLoader loader) {
      boolean anyLoaded = false;

      try {
         ClassLoader cld = null;
         cld = getContextClassLoader();
         if (cld == null) {
            cld = cl.getClassLoader();
         }

         URL[] urls;
         if (cld != null) {
            urls = getResources(cld, name);
         } else {
            urls = getSystemResources(name);
         }

         if (urls != null) {
            for(int i = 0; i < urls.length; ++i) {
               URL url = urls[i];
               InputStream clis = null;
               this.logger.log(Level.CONFIG, "URL {0}", (Object)url);

               try {
                  clis = openStream(url);
                  if (clis != null) {
                     loader.load(clis);
                     anyLoaded = true;
                     this.logger.log(Level.CONFIG, "successfully loaded resource: {0}", (Object)url);
                  } else {
                     this.logger.log(Level.CONFIG, "not loading resource: {0}", (Object)url);
                  }
               } catch (FileNotFoundException var24) {
               } catch (IOException var25) {
                  this.logger.log(Level.CONFIG, "Exception loading resource", (Throwable)var25);
               } catch (SecurityException var26) {
                  this.logger.log(Level.CONFIG, "Exception loading resource", (Throwable)var26);
               } finally {
                  try {
                     if (clis != null) {
                        clis.close();
                     }
                  } catch (IOException var23) {
                  }

               }
            }
         }
      } catch (Exception var28) {
         this.logger.log(Level.CONFIG, "Exception loading resource", (Throwable)var28);
      }

      if (!anyLoaded) {
         this.loadResource("/" + name, cl, loader, false);
      }

   }

   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            ClassLoader cl = null;

            try {
               cl = Thread.currentThread().getContextClassLoader();
            } catch (SecurityException var3) {
            }

            return cl;
         }
      });
   }

   private static InputStream getResourceAsStream(final Class<?> c, final String name) throws IOException {
      try {
         return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
            public InputStream run() throws IOException {
               try {
                  return c.getResourceAsStream(name);
               } catch (RuntimeException var3) {
                  IOException ioex = new IOException("ClassLoader.getResourceAsStream failed");
                  ioex.initCause(var3);
                  throw ioex;
               }
            }
         });
      } catch (PrivilegedActionException var3) {
         throw (IOException)var3.getException();
      }
   }

   private static URL[] getResources(final ClassLoader cl, final String name) {
      return (URL[])AccessController.doPrivileged(new PrivilegedAction<URL[]>() {
         public URL[] run() {
            URL[] ret = null;

            try {
               List<URL> v = Collections.list(cl.getResources(name));
               if (!v.isEmpty()) {
                  ret = new URL[v.size()];
                  v.toArray(ret);
               }
            } catch (IOException var3) {
            } catch (SecurityException var4) {
            }

            return ret;
         }
      });
   }

   private static URL[] getSystemResources(final String name) {
      return (URL[])AccessController.doPrivileged(new PrivilegedAction<URL[]>() {
         public URL[] run() {
            URL[] ret = null;

            try {
               List<URL> v = Collections.list(ClassLoader.getSystemResources(name));
               if (!v.isEmpty()) {
                  ret = new URL[v.size()];
                  v.toArray(ret);
               }
            } catch (IOException var3) {
            } catch (SecurityException var4) {
            }

            return ret;
         }
      });
   }

   private static InputStream openStream(final URL url) throws IOException {
      try {
         return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
            public InputStream run() throws IOException {
               return url.openStream();
            }
         });
      } catch (PrivilegedActionException var2) {
         throw (IOException)var2.getException();
      }
   }

   EventQueue getEventQueue() {
      return this.q;
   }

   static {
      String dir = null;

      try {
         dir = (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
               String home = System.getProperty("java.home");
               String newdir = home + File.separator + "conf";
               File conf = new File(newdir);
               return conf.exists() ? newdir + File.separator : home + File.separator + "lib" + File.separator;
            }
         });
      } catch (Exception var2) {
      }

      confDir = dir;
   }
}
