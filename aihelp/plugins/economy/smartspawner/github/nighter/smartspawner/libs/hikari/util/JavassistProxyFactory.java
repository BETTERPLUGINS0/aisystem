package github.nighter.smartspawner.libs.hikari.util;

import github.nighter.smartspawner.libs.hikari.pool.ProxyCallableStatement;
import github.nighter.smartspawner.libs.hikari.pool.ProxyConnection;
import github.nighter.smartspawner.libs.hikari.pool.ProxyDatabaseMetaData;
import github.nighter.smartspawner.libs.hikari.pool.ProxyPreparedStatement;
import github.nighter.smartspawner.libs.hikari.pool.ProxyResultSet;
import github.nighter.smartspawner.libs.hikari.pool.ProxyStatement;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javassist.CannotCompileException;
import javassist.ClassMap;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

public final class JavassistProxyFactory {
   private static ClassPool classPool;
   private static String genDirectory;

   public static void main(String... args) throws Exception {
      classPool = new ClassPool();
      classPool.importPackage("java.sql");
      classPool.appendClassPath(new LoaderClassPath(JavassistProxyFactory.class.getClassLoader()));
      String parentDir;
      if (args.length > 0) {
         parentDir = args[0];
         if (!parentDir.endsWith(File.separator)) {
            parentDir = parentDir + File.separator;
         }

         genDirectory = parentDir + genDirectory;
      }

      System.out.println("Generating following classes to " + genDirectory);
      parentDir = "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }";
      generateProxyClass(Connection.class, ProxyConnection.class.getName(), parentDir);
      generateProxyClass(Statement.class, ProxyStatement.class.getName(), parentDir);
      generateProxyClass(ResultSet.class, ProxyResultSet.class.getName(), parentDir);
      generateProxyClass(DatabaseMetaData.class, ProxyDatabaseMetaData.class.getName(), parentDir);
      parentDir = "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }";
      generateProxyClass(PreparedStatement.class, ProxyPreparedStatement.class.getName(), parentDir);
      generateProxyClass(CallableStatement.class, ProxyCallableStatement.class.getName(), parentDir);
      modifyProxyFactory();
   }

   private static void modifyProxyFactory() throws NotFoundException, CannotCompileException, IOException {
      System.out.println("Generating method bodies for com.zaxxer.hikari.proxy.ProxyFactory");
      String packageName = ProxyConnection.class.getPackage().getName();
      CtClass proxyCt = classPool.getCtClass("github.nighter.smartspawner.libs.hikari.pool.ProxyFactory");
      CtMethod[] var2 = proxyCt.getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CtMethod method = var2[var4];
         String var6 = method.getName();
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -1995233385:
            if (var6.equals("getProxyStatement")) {
               var7 = 1;
            }
            break;
         case -1729648339:
            if (var6.equals("getProxyResultSet")) {
               var7 = 4;
            }
            break;
         case -443793985:
            if (var6.equals("getProxyCallableStatement")) {
               var7 = 3;
            }
            break;
         case 1457258178:
            if (var6.equals("getProxyDatabaseMetaData")) {
               var7 = 5;
            }
            break;
         case 2011710902:
            if (var6.equals("getProxyConnection")) {
               var7 = 0;
            }
            break;
         case 2145615834:
            if (var6.equals("getProxyPreparedStatement")) {
               var7 = 2;
            }
         }

         switch(var7) {
         case 0:
            method.setBody("{return new " + packageName + ".HikariProxyConnection($$);}");
            break;
         case 1:
            method.setBody("{return new " + packageName + ".HikariProxyStatement($$);}");
            break;
         case 2:
            method.setBody("{return new " + packageName + ".HikariProxyPreparedStatement($$);}");
            break;
         case 3:
            method.setBody("{return new " + packageName + ".HikariProxyCallableStatement($$);}");
            break;
         case 4:
            method.setBody("{return new " + packageName + ".HikariProxyResultSet($$);}");
            break;
         case 5:
            method.setBody("{return new " + packageName + ".HikariProxyDatabaseMetaData($$);}");
         }
      }

      proxyCt.writeFile(genDirectory);
   }

   private static <T> void generateProxyClass(Class<T> primaryInterface, String superClassName, String methodBody) throws Exception {
      String newClassName = superClassName.replaceAll("(.+)\\.(\\w+)", "$1.Hikari$2");
      CtClass superCt = classPool.getCtClass(superClassName);
      CtClass targetCt = classPool.makeClass(newClassName, superCt);
      targetCt.setModifiers(Modifier.setPublic(16));
      System.out.println("Generating " + newClassName);
      HashSet<String> superSigs = new HashSet();
      CtMethod[] var7 = superCt.getMethods();
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         CtMethod method = var7[var9];
         if ((method.getModifiers() & 16) == 16) {
            String var10001 = method.getName();
            superSigs.add(var10001 + method.getSignature());
         }
      }

      HashSet<String> methods = new HashSet();
      Iterator var20 = getAllInterfaces(primaryInterface).iterator();

      while(var20.hasNext()) {
         Class<?> intf = (Class)var20.next();
         CtClass intfCt = classPool.getCtClass(intf.getName());
         targetCt.addInterface(intfCt);
         CtMethod[] var11 = intfCt.getDeclaredMethods();
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            CtMethod intfMethod = var11[var13];
            String var10000 = intfMethod.getName();
            String signature = var10000 + intfMethod.getSignature();
            if (!superSigs.contains(signature) && !methods.contains(signature)) {
               methods.add(signature);
               CtMethod method = CtNewMethod.copy(intfMethod, targetCt, (ClassMap)null);
               String modifiedBody = methodBody;
               CtMethod superMethod = superCt.getMethod(intfMethod.getName(), intfMethod.getSignature());
               if ((superMethod.getModifiers() & 1024) != 1024 && !isDefaultMethod(intf, intfMethod)) {
                  modifiedBody = methodBody.replace("((cast) ", "");
                  modifiedBody = modifiedBody.replace("delegate", "super");
                  modifiedBody = modifiedBody.replace("super)", "super");
               }

               modifiedBody = modifiedBody.replace("cast", primaryInterface.getName());
               if (isThrowsSqlException(intfMethod)) {
                  modifiedBody = modifiedBody.replace("method", method.getName());
               } else {
                  modifiedBody = "{ return ((cast) delegate).method($$); }".replace("method", method.getName()).replace("cast", primaryInterface.getName());
               }

               if (method.getReturnType() == CtClass.voidType) {
                  modifiedBody = modifiedBody.replace("return", "");
               }

               method.setBody(modifiedBody);
               targetCt.addMethod(method);
            }
         }
      }

      targetCt.getClassFile().setMajorVersion(52);
      targetCt.writeFile(genDirectory);
   }

   private static boolean isThrowsSqlException(CtMethod method) {
      try {
         CtClass[] var1 = method.getExceptionTypes();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CtClass clazz = var1[var3];
            if (clazz.getSimpleName().equals("SQLException")) {
               return true;
            }
         }
      } catch (NotFoundException var5) {
      }

      return false;
   }

   private static boolean isDefaultMethod(Class<?> intf, CtMethod intfMethod) throws Exception {
      ArrayList<Class<?>> paramTypes = new ArrayList();
      CtClass[] var3 = intfMethod.getParameterTypes();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CtClass pt = var3[var5];
         paramTypes.add(toJavaClass(pt));
      }

      return intf.getDeclaredMethod(intfMethod.getName(), (Class[])paramTypes.toArray(new Class[0])).toString().contains("default ");
   }

   private static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
      LinkedHashSet<Class<?>> interfaces = new LinkedHashSet();
      Class[] var2 = clazz.getInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class<?> intf = var2[var4];
         if (intf.getInterfaces().length > 0) {
            interfaces.addAll(getAllInterfaces(intf));
         }

         interfaces.add(intf);
      }

      if (clazz.getSuperclass() != null) {
         interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
      }

      if (clazz.isInterface()) {
         interfaces.add(clazz);
      }

      return interfaces;
   }

   private static Class<?> toJavaClass(CtClass cls) throws Exception {
      return cls.getName().endsWith("[]") ? Array.newInstance(toJavaClass(cls.getName().replace("[]", "")), 0).getClass() : toJavaClass(cls.getName());
   }

   private static Class<?> toJavaClass(String cn) throws Exception {
      byte var2 = -1;
      switch(cn.hashCode()) {
      case -1325958191:
         if (cn.equals("double")) {
            var2 = 5;
         }
         break;
      case 104431:
         if (cn.equals("int")) {
            var2 = 0;
         }
         break;
      case 3039496:
         if (cn.equals("byte")) {
            var2 = 3;
         }
         break;
      case 3052374:
         if (cn.equals("char")) {
            var2 = 7;
         }
         break;
      case 3327612:
         if (cn.equals("long")) {
            var2 = 1;
         }
         break;
      case 3625364:
         if (cn.equals("void")) {
            var2 = 8;
         }
         break;
      case 64711720:
         if (cn.equals("boolean")) {
            var2 = 6;
         }
         break;
      case 97526364:
         if (cn.equals("float")) {
            var2 = 4;
         }
         break;
      case 109413500:
         if (cn.equals("short")) {
            var2 = 2;
         }
      }

      switch(var2) {
      case 0:
         return Integer.TYPE;
      case 1:
         return Long.TYPE;
      case 2:
         return Short.TYPE;
      case 3:
         return Byte.TYPE;
      case 4:
         return Float.TYPE;
      case 5:
         return Double.TYPE;
      case 6:
         return Boolean.TYPE;
      case 7:
         return Character.TYPE;
      case 8:
         return Void.TYPE;
      default:
         return Class.forName(cn);
      }
   }

   static {
      genDirectory = "target" + File.separator + "classes";
   }
}
