package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.BaseStatement;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.core.TypeInfo;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TypeInfoCache implements TypeInfo {
   private static final Logger LOGGER = Logger.getLogger(TypeInfoCache.class.getName());
   private final Map<String, Integer> pgNameToSQLType;
   private final Map<Integer, Integer> oidToSQLType;
   private final Map<String, String> pgNameToJavaClass;
   private final Map<Integer, String> oidToPgName;
   private final Map<String, Integer> pgNameToOid;
   private final Map<String, Integer> javaArrayTypeToOid;
   private final Map<String, Class<? extends PGobject>> pgNameToPgObject;
   private final Map<Integer, Integer> pgArrayToPgType;
   private final Map<Integer, Character> arrayOidToDelimiter;
   private final BaseConnection conn;
   private final int unknownLength;
   @Nullable
   private PreparedStatement getOidStatementSimple;
   @Nullable
   private PreparedStatement getOidStatementComplexNonArray;
   @Nullable
   private PreparedStatement getOidStatementComplexArray;
   @Nullable
   private PreparedStatement getNameStatement;
   @Nullable
   private PreparedStatement getArrayElementOidStatement;
   @Nullable
   private PreparedStatement getArrayDelimiterStatement;
   @Nullable
   private PreparedStatement getTypeInfoStatement;
   @Nullable
   private PreparedStatement getAllTypeInfoStatement;
   private final ResourceLock lock = new ResourceLock();
   private static final Object[][] types = new Object[][]{{"int2", 21, 5, "java.lang.Integer", 1005}, {"int4", 23, 4, "java.lang.Integer", 1007}, {"oid", 26, -5, "java.lang.Long", 1028}, {"int8", 20, -5, "java.lang.Long", 1016}, {"money", 790, 8, "java.lang.Double", 791}, {"numeric", 1700, 2, "java.math.BigDecimal", 1231}, {"float4", 700, 7, "java.lang.Float", 1021}, {"float8", 701, 8, "java.lang.Double", 1022}, {"char", 18, 1, "java.lang.String", 1002}, {"bpchar", 1042, 1, "java.lang.String", 1014}, {"varchar", 1043, 12, "java.lang.String", 1015}, {"varbit", 1562, 1111, "java.lang.String", 1563}, {"text", 25, 12, "java.lang.String", 1009}, {"name", 19, 12, "java.lang.String", 1003}, {"bytea", 17, -2, "[B", 1001}, {"bool", 16, -7, "java.lang.Boolean", 1000}, {"bit", 1560, -7, "java.lang.Boolean", 1561}, {"date", 1082, 91, "java.sql.Date", 1182}, {"time", 1083, 92, "java.sql.Time", 1183}, {"timetz", 1266, 92, "java.sql.Time", 1270}, {"timestamp", 1114, 93, "java.sql.Timestamp", 1115}, {"timestamptz", 1184, 93, "java.sql.Timestamp", 1185}, {"refcursor", 1790, 2012, "java.sql.ResultSet", 2201}, {"json", 114, 1111, "fr.xephi.authme.libs.org.postgresql.util.PGobject", 199}, {"point", 600, 1111, "fr.xephi.authme.libs.org.postgresql.geometric.PGpoint", 1017}, {"box", 603, 1111, "fr.xephi.authme.libs.org.postgresql.geometric.PGBox", 1020}};
   private static final ConcurrentMap<String, String> TYPE_ALIASES = new ConcurrentHashMap(30);

   public TypeInfoCache(BaseConnection conn, int unknownLength) {
      this.conn = conn;
      this.unknownLength = unknownLength;
      this.oidToPgName = new HashMap((int)Math.round((double)types.length * 1.5D));
      this.pgNameToOid = new HashMap((int)Math.round((double)types.length * 1.5D));
      this.javaArrayTypeToOid = new HashMap((int)Math.round((double)types.length * 1.5D));
      this.pgNameToJavaClass = new HashMap((int)Math.round((double)types.length * 1.5D));
      this.pgNameToPgObject = new HashMap((int)Math.round((double)types.length * 1.5D));
      this.pgArrayToPgType = new HashMap((int)Math.round((double)types.length * 1.5D));
      this.arrayOidToDelimiter = new HashMap((int)Math.round((double)types.length * 2.5D));
      this.pgNameToSQLType = Collections.synchronizedMap(new HashMap((int)Math.round((double)types.length * 1.5D)));
      this.oidToSQLType = Collections.synchronizedMap(new HashMap((int)Math.round((double)types.length * 1.5D)));
      Object[][] var3 = types;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object[] type = var3[var5];
         String pgTypeName = (String)type[0];
         Integer oid = (Integer)type[1];
         Integer sqlType = (Integer)type[2];
         String javaClass = (String)type[3];
         Integer arrayOid = (Integer)type[4];
         this.addCoreType(pgTypeName, oid, sqlType, javaClass, arrayOid);
      }

      this.pgNameToJavaClass.put("hstore", Map.class.getName());
   }

   public void addCoreType(String pgTypeName, Integer oid, Integer sqlType, String javaClass, Integer arrayOid) {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.pgNameToJavaClass.put(pgTypeName, javaClass);
         this.pgNameToOid.put(pgTypeName, oid);
         this.oidToPgName.put(oid, pgTypeName);
         this.javaArrayTypeToOid.put(javaClass, arrayOid);
         this.pgArrayToPgType.put(arrayOid, oid);
         this.pgNameToSQLType.put(pgTypeName, sqlType);
         this.oidToSQLType.put(oid, sqlType);
         Character delim = ',';
         if ("box".equals(pgTypeName)) {
            delim = ';';
         }

         this.arrayOidToDelimiter.put(oid, delim);
         this.arrayOidToDelimiter.put(arrayOid, delim);
         String pgArrayTypeName = pgTypeName + "[]";
         this.pgNameToJavaClass.put(pgArrayTypeName, "java.sql.Array");
         this.pgNameToSQLType.put(pgArrayTypeName, 2003);
         this.oidToSQLType.put(arrayOid, 2003);
         this.pgNameToOid.put(pgArrayTypeName, arrayOid);
         pgArrayTypeName = "_" + pgTypeName;
         if (!this.pgNameToJavaClass.containsKey(pgArrayTypeName)) {
            this.pgNameToJavaClass.put(pgArrayTypeName, "java.sql.Array");
            this.pgNameToSQLType.put(pgArrayTypeName, 2003);
            this.pgNameToOid.put(pgArrayTypeName, arrayOid);
            this.oidToPgName.put(arrayOid, pgArrayTypeName);
         }
      } catch (Throwable var10) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void addDataType(String type, Class<? extends PGobject> klass) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.pgNameToPgObject.put(type, klass);
         this.pgNameToJavaClass.put(type, klass.getName());
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public Iterator<String> getPGTypeNamesWithSQLTypes() {
      return this.pgNameToSQLType.keySet().iterator();
   }

   public Iterator<Integer> getPGTypeOidsWithSQLTypes() {
      return this.oidToSQLType.keySet().iterator();
   }

   private String getSQLTypeQuery(boolean typoidParam) {
      StringBuilder sql = new StringBuilder();
      sql.append("SELECT typinput='pg_catalog.array_in'::regproc as is_array, typtype, typname, pg_type.oid ");
      sql.append("  FROM pg_catalog.pg_type ");
      sql.append("  LEFT JOIN (select ns.oid as nspoid, ns.nspname, r.r ");
      sql.append("          from pg_namespace as ns ");
      sql.append("          join ( select s.r, (current_schemas(false))[s.r] as nspname ");
      sql.append("                   from generate_series(1, array_upper(current_schemas(false), 1)) as s(r) ) as r ");
      sql.append("         using ( nspname ) ");
      sql.append("       ) as sp ");
      sql.append("    ON sp.nspoid = typnamespace ");
      if (typoidParam) {
         sql.append(" WHERE pg_type.oid = ? ");
      }

      sql.append(" ORDER BY sp.r, pg_type.oid DESC;");
      return sql.toString();
   }

   private int getSQLTypeFromQueryResult(ResultSet rs) throws SQLException {
      Integer type = null;
      boolean isArray = rs.getBoolean("is_array");
      String typtype = rs.getString("typtype");
      if (isArray) {
         type = 2003;
      } else if ("c".equals(typtype)) {
         type = 2002;
      } else if ("d".equals(typtype)) {
         type = 2001;
      } else if ("e".equals(typtype)) {
         type = 12;
      }

      if (type == null) {
         type = 1111;
      }

      return type;
   }

   private PreparedStatement prepareGetAllTypeInfoStatement() throws SQLException {
      PreparedStatement getAllTypeInfoStatement = this.getAllTypeInfoStatement;
      if (getAllTypeInfoStatement == null) {
         getAllTypeInfoStatement = this.conn.prepareStatement(this.getSQLTypeQuery(false));
         this.getAllTypeInfoStatement = getAllTypeInfoStatement;
      }

      return getAllTypeInfoStatement;
   }

   public void cacheSQLTypes() throws SQLException {
      LOGGER.log(Level.FINEST, "caching all SQL typecodes");
      PreparedStatement getAllTypeInfoStatement = this.prepareGetAllTypeInfoStatement();
      if (!((BaseStatement)getAllTypeInfoStatement).executeWithFlags(16)) {
         throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
      } else {
         ResultSet rs = (ResultSet)Nullness.castNonNull(getAllTypeInfoStatement.getResultSet());

         while(rs.next()) {
            String typeName = (String)Nullness.castNonNull(rs.getString("typname"));
            Integer type = this.getSQLTypeFromQueryResult(rs);
            if (!this.pgNameToSQLType.containsKey(typeName)) {
               this.pgNameToSQLType.put(typeName, type);
            }

            Integer typeOid = this.longOidToInt((Long)Nullness.castNonNull(rs.getLong("oid")));
            if (!this.oidToSQLType.containsKey(typeOid)) {
               this.oidToSQLType.put(typeOid, type);
            }
         }

         rs.close();
      }
   }

   private PreparedStatement prepareGetTypeInfoStatement() throws SQLException {
      PreparedStatement getTypeInfoStatement = this.getTypeInfoStatement;
      if (getTypeInfoStatement == null) {
         getTypeInfoStatement = this.conn.prepareStatement(this.getSQLTypeQuery(true));
         this.getTypeInfoStatement = getTypeInfoStatement;
      }

      return getTypeInfoStatement;
   }

   public int getSQLType(String pgTypeName) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      int var4;
      label55: {
         label56: {
            short var3;
            try {
               if (!pgTypeName.endsWith("[]")) {
                  Integer i = (Integer)this.pgNameToSQLType.get(pgTypeName);
                  if (i != null) {
                     var4 = i;
                     break label55;
                  }

                  i = this.getSQLType((Integer)Nullness.castNonNull(this.getPGType(pgTypeName)));
                  this.pgNameToSQLType.put(pgTypeName, i);
                  var4 = i;
                  break label56;
               }

               var3 = 2003;
            } catch (Throwable var6) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var3;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var4;
   }

   public int getJavaArrayType(String className) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      byte var7;
      label43: {
         int var4;
         try {
            Integer oid = (Integer)this.javaArrayTypeToOid.get(className);
            if (oid == null) {
               var7 = 0;
               break label43;
            }

            var4 = oid;
         } catch (Throwable var6) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var7;
   }

   public int getSQLType(int typeOid) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      short var10;
      label63: {
         int var11;
         label64: {
            int var7;
            try {
               if (typeOid == 0) {
                  var10 = 1111;
                  break label63;
               }

               Integer i = (Integer)this.oidToSQLType.get(typeOid);
               if (i != null) {
                  var11 = i;
                  break label64;
               }

               LOGGER.log(Level.FINEST, "querying SQL typecode for pg type oid ''{0}''", this.intOidToLong(typeOid));
               PreparedStatement getTypeInfoStatement = this.prepareGetTypeInfoStatement();
               getTypeInfoStatement.setLong(1, this.intOidToLong(typeOid));
               if (!((BaseStatement)getTypeInfoStatement).executeWithFlags(16)) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               ResultSet rs = (ResultSet)Nullness.castNonNull(getTypeInfoStatement.getResultSet());
               int sqlType = 1111;
               if (rs.next()) {
                  sqlType = this.getSQLTypeFromQueryResult(rs);
               }

               rs.close();
               this.oidToSQLType.put(typeOid, sqlType);
               var7 = sqlType;
            } catch (Throwable var9) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var11;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var10;
   }

   private PreparedStatement getOidStatement(String pgTypeName) throws SQLException {
      boolean isArray = pgTypeName.endsWith("[]");
      boolean hasQuote = pgTypeName.contains("\"");
      int dotIndex = pgTypeName.indexOf(46);
      PreparedStatement oidStatementComplex;
      String fullName;
      if (dotIndex == -1 && !hasQuote && !isArray) {
         oidStatementComplex = this.getOidStatementSimple;
         if (oidStatementComplex == null) {
            fullName = "SELECT pg_type.oid, typname   FROM pg_catalog.pg_type   LEFT   JOIN (select ns.oid as nspoid, ns.nspname, r.r           from pg_namespace as ns           join ( select s.r, (current_schemas(false))[s.r] as nspname                    from generate_series(1, array_upper(current_schemas(false), 1)) as s(r) ) as r          using ( nspname )        ) as sp     ON sp.nspoid = typnamespace  WHERE typname = ?  ORDER BY sp.r, pg_type.oid DESC LIMIT 1;";
            this.getOidStatementSimple = oidStatementComplex = this.conn.prepareStatement(fullName);
         }

         fullName = pgTypeName.toLowerCase(Locale.ROOT);
         oidStatementComplex.setString(1, fullName);
         return oidStatementComplex;
      } else {
         PreparedStatement getOidStatementComplexArray;
         String schema;
         if (isArray) {
            getOidStatementComplexArray = this.getOidStatementComplexArray;
            if (getOidStatementComplexArray == null) {
               if (this.conn.haveMinimumServerVersion(ServerVersion.v8_3)) {
                  schema = "SELECT t.typarray, arr.typname   FROM pg_catalog.pg_type t  JOIN pg_catalog.pg_namespace n ON t.typnamespace = n.oid  JOIN pg_catalog.pg_type arr ON arr.oid = t.typarray WHERE t.typname = ? AND (n.nspname = ? OR ? AND n.nspname = ANY (current_schemas(true))) ORDER BY t.oid DESC LIMIT 1";
               } else {
                  schema = "SELECT t.oid, t.typname   FROM pg_catalog.pg_type t  JOIN pg_catalog.pg_namespace n ON t.typnamespace = n.oid WHERE t.typelem = (SELECT oid FROM pg_catalog.pg_type WHERE typname = ?) AND substring(t.typname, 1, 1) = '_' AND t.typlen = -1 AND (n.nspname = ? OR ? AND n.nspname = ANY (current_schemas(true))) ORDER BY t.typelem DESC LIMIT 1";
               }

               this.getOidStatementComplexArray = getOidStatementComplexArray = this.conn.prepareStatement(schema);
            }

            oidStatementComplex = getOidStatementComplexArray;
         } else {
            getOidStatementComplexArray = this.getOidStatementComplexNonArray;
            if (getOidStatementComplexArray == null) {
               schema = "SELECT t.oid, t.typname   FROM pg_catalog.pg_type t  JOIN pg_catalog.pg_namespace n ON t.typnamespace = n.oid WHERE t.typname = ? AND (n.nspname = ? OR ? AND n.nspname = ANY (current_schemas(true))) ORDER BY t.oid DESC LIMIT 1";
               this.getOidStatementComplexNonArray = getOidStatementComplexArray = this.conn.prepareStatement(schema);
            }

            oidStatementComplex = getOidStatementComplexArray;
         }

         fullName = isArray ? pgTypeName.substring(0, pgTypeName.length() - 2) : pgTypeName;
         String name;
         if (dotIndex == -1) {
            schema = null;
            name = fullName;
         } else if (fullName.startsWith("\"")) {
            if (fullName.endsWith("\"")) {
               String[] parts = fullName.split("\"\\.\"");
               schema = parts.length == 2 ? parts[0] + "\"" : null;
               name = parts.length == 2 ? "\"" + parts[1] : parts[0];
            } else {
               int lastDotIndex = fullName.lastIndexOf(46);
               name = fullName.substring(lastDotIndex + 1);
               schema = fullName.substring(0, lastDotIndex);
            }
         } else {
            schema = fullName.substring(0, dotIndex);
            name = fullName.substring(dotIndex + 1);
         }

         if (schema != null && schema.startsWith("\"") && schema.endsWith("\"")) {
            schema = schema.substring(1, schema.length() - 1);
         } else if (schema != null) {
            schema = schema.toLowerCase(Locale.ROOT);
         }

         if (name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
         } else {
            name = name.toLowerCase(Locale.ROOT);
         }

         oidStatementComplex.setString(1, name);
         oidStatementComplex.setString(2, schema);
         oidStatementComplex.setBoolean(3, schema == null);
         return oidStatementComplex;
      }
   }

   public int getPGType(String pgTypeName) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      byte var9;
      label63: {
         int var10;
         label64: {
            int var11;
            try {
               if (pgTypeName == null) {
                  var9 = 0;
                  break label63;
               }

               Integer oid = (Integer)this.pgNameToOid.get(pgTypeName);
               if (oid != null) {
                  var10 = oid;
                  break label64;
               }

               PreparedStatement oidStatement = this.getOidStatement(pgTypeName);
               if (!((BaseStatement)oidStatement).executeWithFlags(16)) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               oid = 0;
               ResultSet rs = (ResultSet)Nullness.castNonNull(oidStatement.getResultSet());
               if (rs.next()) {
                  oid = (int)rs.getLong(1);
                  String internalName = (String)Nullness.castNonNull(rs.getString(2));
                  this.oidToPgName.put(oid, internalName);
                  this.pgNameToOid.put(internalName, oid);
               }

               this.pgNameToOid.put(pgTypeName, oid);
               rs.close();
               var11 = oid;
            } catch (Throwable var8) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var11;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var10;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var9;
   }

   @Nullable
   public String getPGType(int oid) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      String pgTypeName;
      label77: {
         String var11;
         label78: {
            String var12;
            try {
               if (oid == 0) {
                  pgTypeName = null;
                  break label77;
               }

               pgTypeName = (String)this.oidToPgName.get(oid);
               if (pgTypeName != null) {
                  var11 = pgTypeName;
                  break label78;
               }

               PreparedStatement getNameStatement = this.prepareGetNameStatement();
               getNameStatement.setInt(1, oid);
               if (!((BaseStatement)getNameStatement).executeWithFlags(16)) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               ResultSet rs = (ResultSet)Nullness.castNonNull(getNameStatement.getResultSet());
               if (rs.next()) {
                  boolean onPath = rs.getBoolean(1);
                  String schema = (String)Nullness.castNonNull(rs.getString(2), "schema");
                  String name = (String)Nullness.castNonNull(rs.getString(3), "name");
                  if (onPath) {
                     pgTypeName = name;
                     this.pgNameToOid.put(schema + "." + name, oid);
                  } else {
                     pgTypeName = "\"" + schema + "\".\"" + name + "\"";
                     if (schema.equals(schema.toLowerCase(Locale.ROOT)) && schema.indexOf(46) == -1 && name.equals(name.toLowerCase(Locale.ROOT)) && name.indexOf(46) == -1) {
                        this.pgNameToOid.put(schema + "." + name, oid);
                     }
                  }

                  this.pgNameToOid.put(pgTypeName, oid);
                  this.oidToPgName.put(oid, pgTypeName);
               }

               rs.close();
               var12 = pgTypeName;
            } catch (Throwable var10) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var12;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var11;
      }

      if (ignore != null) {
         ignore.close();
      }

      return pgTypeName;
   }

   private PreparedStatement prepareGetNameStatement() throws SQLException {
      PreparedStatement getNameStatement = this.getNameStatement;
      if (getNameStatement == null) {
         String sql = "SELECT n.nspname = ANY(current_schemas(true)), n.nspname, t.typname FROM pg_catalog.pg_type t JOIN pg_catalog.pg_namespace n ON t.typnamespace = n.oid WHERE t.oid = ?";
         this.getNameStatement = getNameStatement = this.conn.prepareStatement(sql);
      }

      return getNameStatement;
   }

   public int getPGArrayType(@Nullable String elementTypeName) throws SQLException {
      elementTypeName = this.getTypeForAlias(elementTypeName);
      return this.getPGType(elementTypeName + "[]");
   }

   protected int convertArrayToBaseOid(int oid) {
      ResourceLock ignore = this.lock.obtain();

      int var4;
      label43: {
         try {
            Integer i = (Integer)this.pgArrayToPgType.get(oid);
            if (i == null) {
               var4 = oid;
               break label43;
            }

            var4 = i;
         } catch (Throwable var6) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var4;
   }

   public char getArrayDelimiter(int oid) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      char var10;
      label63: {
         char var11;
         label64: {
            char var7;
            try {
               if (oid == 0) {
                  var10 = ',';
                  break label63;
               }

               Character delim = (Character)this.arrayOidToDelimiter.get(oid);
               if (delim != null) {
                  var11 = delim;
                  break label64;
               }

               PreparedStatement getArrayDelimiterStatement = this.prepareGetArrayDelimiterStatement();
               getArrayDelimiterStatement.setInt(1, oid);
               if (!((BaseStatement)getArrayDelimiterStatement).executeWithFlags(16)) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               ResultSet rs = (ResultSet)Nullness.castNonNull(getArrayDelimiterStatement.getResultSet());
               if (!rs.next()) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               String s = (String)Nullness.castNonNull(rs.getString(1));
               delim = s.charAt(0);
               this.arrayOidToDelimiter.put(oid, delim);
               rs.close();
               var7 = delim;
            } catch (Throwable var9) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var11;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var10;
   }

   private PreparedStatement prepareGetArrayDelimiterStatement() throws SQLException {
      PreparedStatement getArrayDelimiterStatement = this.getArrayDelimiterStatement;
      if (getArrayDelimiterStatement == null) {
         String sql = "SELECT e.typdelim FROM pg_catalog.pg_type t, pg_catalog.pg_type e WHERE t.oid = ? and t.typelem = e.oid";
         this.getArrayDelimiterStatement = getArrayDelimiterStatement = this.conn.prepareStatement(sql);
      }

      return getArrayDelimiterStatement;
   }

   public int getPGArrayElement(int oid) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      byte var13;
      label73: {
         int var14;
         label74: {
            int var10;
            try {
               if (oid == 0) {
                  var13 = 0;
                  break label73;
               }

               Integer pgType = (Integer)this.pgArrayToPgType.get(oid);
               if (pgType != null) {
                  var14 = pgType;
                  break label74;
               }

               PreparedStatement getArrayElementOidStatement = this.prepareGetArrayElementOidStatement();
               getArrayElementOidStatement.setInt(1, oid);
               if (!((BaseStatement)getArrayElementOidStatement).executeWithFlags(16)) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               ResultSet rs = (ResultSet)Nullness.castNonNull(getArrayElementOidStatement.getResultSet());
               if (!rs.next()) {
                  throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
               }

               pgType = (int)rs.getLong(1);
               boolean onPath = rs.getBoolean(2);
               String schema = rs.getString(3);
               String name = (String)Nullness.castNonNull(rs.getString(4));
               this.pgArrayToPgType.put(oid, pgType);
               this.pgNameToOid.put(schema + "." + name, pgType);
               String fullName = "\"" + schema + "\".\"" + name + "\"";
               this.pgNameToOid.put(fullName, pgType);
               if (onPath && name.equals(name.toLowerCase(Locale.ROOT))) {
                  this.oidToPgName.put(pgType, name);
                  this.pgNameToOid.put(name, pgType);
               } else {
                  this.oidToPgName.put(pgType, fullName);
               }

               rs.close();
               var10 = pgType;
            } catch (Throwable var12) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }
               }

               throw var12;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var10;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var14;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var13;
   }

   private PreparedStatement prepareGetArrayElementOidStatement() throws SQLException {
      PreparedStatement getArrayElementOidStatement = this.getArrayElementOidStatement;
      if (getArrayElementOidStatement == null) {
         String sql = "SELECT e.oid, n.nspname = ANY(current_schemas(true)), n.nspname, e.typname FROM pg_catalog.pg_type t JOIN pg_catalog.pg_type e ON t.typelem = e.oid JOIN pg_catalog.pg_namespace n ON t.typnamespace = n.oid WHERE t.oid = ?";
         this.getArrayElementOidStatement = getArrayElementOidStatement = this.conn.prepareStatement(sql);
      }

      return getArrayElementOidStatement;
   }

   @Nullable
   public Class<? extends PGobject> getPGobject(String type) {
      ResourceLock ignore = this.lock.obtain();

      Class var3;
      try {
         var3 = (Class)this.pgNameToPgObject.get(type);
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public String getJavaClass(int oid) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      String result;
      label64: {
         String var5;
         label65: {
            try {
               String pgTypeName = this.getPGType(oid);
               if (pgTypeName == null) {
                  result = "java.lang.String";
                  break label64;
               }

               result = (String)this.pgNameToJavaClass.get(pgTypeName);
               if (result != null) {
                  var5 = result;
                  break label65;
               }

               if (this.getSQLType(pgTypeName) == 2003) {
                  result = "java.sql.Array";
                  this.pgNameToJavaClass.put(pgTypeName, result);
               }

               var5 = result == null ? "java.lang.String" : result;
            } catch (Throwable var7) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return result;
   }

   @Nullable
   public String getTypeForAlias(@Nullable String alias) {
      if (alias == null) {
         return null;
      } else {
         String type = (String)TYPE_ALIASES.get(alias);
         if (type != null) {
            return type;
         } else {
            type = (String)TYPE_ALIASES.get(alias.toLowerCase(Locale.ROOT));
            if (type == null) {
               type = alias;
            }

            TYPE_ALIASES.put(alias, type);
            return type;
         }
      }
   }

   public int getPrecision(int oid, int typmod) {
      oid = this.convertArrayToBaseOid(oid);
      switch(oid) {
      case 16:
      case 18:
         return 1;
      case 17:
      case 25:
      default:
         return this.unknownLength;
      case 20:
         return 19;
      case 21:
         return 5;
      case 23:
      case 26:
         return 10;
      case 700:
         return 8;
      case 701:
         return 17;
      case 1042:
      case 1043:
         if (typmod == -1) {
            return this.unknownLength;
         }

         return typmod - 4;
      case 1082:
      case 1083:
      case 1114:
      case 1184:
      case 1186:
      case 1266:
         return this.getDisplaySize(oid, typmod);
      case 1560:
         return typmod;
      case 1562:
         if (typmod == -1) {
            return this.unknownLength;
         }

         return typmod;
      case 1700:
         return typmod == -1 ? 0 : (typmod - 4 & -65536) >> 16;
      }
   }

   public int getScale(int oid, int typmod) {
      oid = this.convertArrayToBaseOid(oid);
      switch(oid) {
      case 700:
         return 8;
      case 701:
         return 17;
      case 1083:
      case 1114:
      case 1184:
      case 1266:
         if (typmod == -1) {
            return 6;
         }

         return typmod;
      case 1186:
         if (typmod == -1) {
            return 6;
         }

         return typmod & '\uffff';
      case 1700:
         if (typmod == -1) {
            return 0;
         }

         return typmod - 4 & '\uffff';
      default:
         return 0;
      }
   }

   public boolean isCaseSensitive(int oid) {
      oid = this.convertArrayToBaseOid(oid);
      switch(oid) {
      case 16:
      case 20:
      case 21:
      case 23:
      case 26:
      case 700:
      case 701:
      case 1082:
      case 1083:
      case 1114:
      case 1184:
      case 1186:
      case 1266:
      case 1560:
      case 1562:
      case 1700:
         return false;
      default:
         return true;
      }
   }

   public boolean isSigned(int oid) {
      oid = this.convertArrayToBaseOid(oid);
      switch(oid) {
      case 20:
      case 21:
      case 23:
      case 700:
      case 701:
      case 1700:
         return true;
      default:
         return false;
      }
   }

   public int getDisplaySize(int oid, int typmod) {
      oid = this.convertArrayToBaseOid(oid);
      switch(oid) {
      case 16:
         return 1;
      case 17:
      case 25:
         return this.unknownLength;
      case 18:
         return 1;
      case 20:
         return 20;
      case 21:
         return 6;
      case 23:
         return 11;
      case 26:
         return 10;
      case 700:
         return 15;
      case 701:
         return 25;
      case 1042:
      case 1043:
         if (typmod == -1) {
            return this.unknownLength;
         }

         return typmod - 4;
      case 1082:
         return 13;
      case 1083:
      case 1114:
      case 1184:
      case 1266:
         int secondSize;
         switch(typmod) {
         case -1:
            secondSize = 7;
            break;
         case 0:
            secondSize = 0;
            break;
         case 1:
            secondSize = 3;
            break;
         default:
            secondSize = typmod + 1;
         }

         switch(oid) {
         case 1083:
            return 8 + secondSize;
         case 1114:
            return 22 + secondSize;
         case 1184:
            return 22 + secondSize + 6;
         case 1266:
            return 8 + secondSize + 6;
         }
      case 1186:
         return 49;
      case 1560:
         return typmod;
      case 1562:
         if (typmod == -1) {
            return this.unknownLength;
         }

         return typmod;
      case 1700:
         if (typmod == -1) {
            return 131089;
         }

         int precision = typmod - 4 >> 16 & '\uffff';
         int scale = typmod - 4 & '\uffff';
         return 1 + precision + (scale != 0 ? 1 : 0);
      default:
         return this.unknownLength;
      }
   }

   public int getMaximumPrecision(int oid) {
      oid = this.convertArrayToBaseOid(oid);
      switch(oid) {
      case 1042:
      case 1043:
         return 10485760;
      case 1083:
      case 1266:
         return 6;
      case 1114:
      case 1184:
      case 1186:
         return 6;
      case 1560:
      case 1562:
         return 83886080;
      case 1700:
         return 1000;
      default:
         return 0;
      }
   }

   public boolean requiresQuoting(int oid) throws SQLException {
      int sqlType = this.getSQLType(oid);
      return this.requiresQuotingSqlType(sqlType);
   }

   public boolean requiresQuotingSqlType(int sqlType) throws SQLException {
      switch(sqlType) {
      case -6:
      case -5:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         return false;
      case -4:
      case -3:
      case -2:
      case -1:
      case 0:
      case 1:
      default:
         return true;
      }
   }

   public int longOidToInt(long oid) throws SQLException {
      if ((oid & -4294967296L) != 0L) {
         throw new PSQLException(GT.tr("Value is not an OID: {0}", oid), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
      } else {
         return (int)oid;
      }
   }

   public long intOidToLong(int oid) {
      return (long)oid & 4294967295L;
   }

   static {
      TYPE_ALIASES.put("bool", "bool");
      TYPE_ALIASES.put("boolean", "bool");
      TYPE_ALIASES.put("smallint", "int2");
      TYPE_ALIASES.put("int2", "int2");
      TYPE_ALIASES.put("int", "int4");
      TYPE_ALIASES.put("integer", "int4");
      TYPE_ALIASES.put("int4", "int4");
      TYPE_ALIASES.put("long", "int8");
      TYPE_ALIASES.put("int8", "int8");
      TYPE_ALIASES.put("bigint", "int8");
      TYPE_ALIASES.put("float", "float8");
      TYPE_ALIASES.put("real", "float4");
      TYPE_ALIASES.put("float4", "float4");
      TYPE_ALIASES.put("double", "float8");
      TYPE_ALIASES.put("double precision", "float8");
      TYPE_ALIASES.put("float8", "float8");
      TYPE_ALIASES.put("decimal", "numeric");
      TYPE_ALIASES.put("numeric", "numeric");
      TYPE_ALIASES.put("character varying", "varchar");
      TYPE_ALIASES.put("varchar", "varchar");
      TYPE_ALIASES.put("time without time zone", "time");
      TYPE_ALIASES.put("time", "time");
      TYPE_ALIASES.put("time with time zone", "timetz");
      TYPE_ALIASES.put("timetz", "timetz");
      TYPE_ALIASES.put("timestamp without time zone", "timestamp");
      TYPE_ALIASES.put("timestamp", "timestamp");
      TYPE_ALIASES.put("timestamp with time zone", "timestamptz");
      TYPE_ALIASES.put("timestamptz", "timestamptz");
   }
}
