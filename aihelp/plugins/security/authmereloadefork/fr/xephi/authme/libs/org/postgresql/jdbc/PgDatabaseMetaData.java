package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.core.BaseStatement;
import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import fr.xephi.authme.libs.org.postgresql.core.TypeInfo;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import fr.xephi.authme.libs.org.postgresql.util.DriverInfo;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.JdbcBlackHole;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PgDatabaseMetaData implements DatabaseMetaData {
   @Nullable
   private String keywords;
   protected final PgConnection connection;
   private int nameDataLength;
   private int indexMaxKeys;
   private static final Map<String, Map<String, String>> tableTypeClauses = new HashMap();

   public PgDatabaseMetaData(PgConnection conn) {
      this.connection = conn;
   }

   protected int getMaxIndexKeys() throws SQLException {
      if (this.indexMaxKeys == 0) {
         String sql = "SELECT setting FROM pg_catalog.pg_settings WHERE name='max_index_keys'";
         Statement stmt = this.connection.createStatement();
         ResultSet rs = null;

         try {
            rs = stmt.executeQuery(sql);
            if (!rs.next()) {
               stmt.close();
               throw new PSQLException(GT.tr("Unable to determine a value for MaxIndexKeys due to missing system catalog data."), PSQLState.UNEXPECTED_ERROR);
            }

            this.indexMaxKeys = rs.getInt(1);
         } finally {
            JdbcBlackHole.close(rs);
            JdbcBlackHole.close(stmt);
         }
      }

      return this.indexMaxKeys;
   }

   protected int getMaxNameLength() throws SQLException {
      if (this.nameDataLength == 0) {
         String sql = "SELECT t.typlen FROM pg_catalog.pg_type t, pg_catalog.pg_namespace n WHERE t.typnamespace=n.oid AND t.typname='name' AND n.nspname='pg_catalog'";
         Statement stmt = this.connection.createStatement();
         ResultSet rs = null;

         try {
            rs = stmt.executeQuery(sql);
            if (!rs.next()) {
               throw new PSQLException(GT.tr("Unable to find name datatype in the system catalogs."), PSQLState.UNEXPECTED_ERROR);
            }

            this.nameDataLength = rs.getInt("typlen");
         } finally {
            JdbcBlackHole.close(rs);
            JdbcBlackHole.close(stmt);
         }
      }

      return this.nameDataLength - 1;
   }

   public boolean allProceduresAreCallable() throws SQLException {
      return true;
   }

   public boolean allTablesAreSelectable() throws SQLException {
      return true;
   }

   public String getURL() throws SQLException {
      return this.connection.getURL();
   }

   public String getUserName() throws SQLException {
      return this.connection.getUserName();
   }

   public boolean isReadOnly() throws SQLException {
      return this.connection.isReadOnly();
   }

   public boolean nullsAreSortedHigh() throws SQLException {
      return true;
   }

   public boolean nullsAreSortedLow() throws SQLException {
      return false;
   }

   public boolean nullsAreSortedAtStart() throws SQLException {
      return false;
   }

   public boolean nullsAreSortedAtEnd() throws SQLException {
      return false;
   }

   public String getDatabaseProductName() throws SQLException {
      return "PostgreSQL";
   }

   public String getDatabaseProductVersion() throws SQLException {
      return this.connection.getDBVersionNumber();
   }

   public String getDriverName() {
      return "PostgreSQL JDBC Driver";
   }

   public String getDriverVersion() {
      return "42.7.3";
   }

   public int getDriverMajorVersion() {
      return 42;
   }

   public int getDriverMinorVersion() {
      return 7;
   }

   public boolean usesLocalFiles() throws SQLException {
      return false;
   }

   public boolean usesLocalFilePerTable() throws SQLException {
      return false;
   }

   public boolean supportsMixedCaseIdentifiers() throws SQLException {
      return false;
   }

   public boolean storesUpperCaseIdentifiers() throws SQLException {
      return false;
   }

   public boolean storesLowerCaseIdentifiers() throws SQLException {
      return true;
   }

   public boolean storesMixedCaseIdentifiers() throws SQLException {
      return false;
   }

   public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
      return true;
   }

   public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
      return false;
   }

   public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
      return false;
   }

   public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
      return false;
   }

   public String getIdentifierQuoteString() throws SQLException {
      return "\"";
   }

   public String getSQLKeywords() throws SQLException {
      this.connection.checkClosed();
      String keywords = this.keywords;
      if (keywords == null) {
         if (this.connection.haveMinimumServerVersion(ServerVersion.v9_0)) {
            String sql = "select string_agg(word, ',') from pg_catalog.pg_get_keywords() where word <> ALL ('{a,abs,absolute,action,ada,add,admin,after,all,allocate,alter,always,and,any,are,array,as,asc,asensitive,assertion,assignment,asymmetric,at,atomic,attribute,attributes,authorization,avg,before,begin,bernoulli,between,bigint,binary,blob,boolean,both,breadth,by,c,call,called,cardinality,cascade,cascaded,case,cast,catalog,catalog_name,ceil,ceiling,chain,char,char_length,character,character_length,character_set_catalog,character_set_name,character_set_schema,characteristics,characters,check,checked,class_origin,clob,close,coalesce,cobol,code_units,collate,collation,collation_catalog,collation_name,collation_schema,collect,column,column_name,command_function,command_function_code,commit,committed,condition,condition_number,connect,connection_name,constraint,constraint_catalog,constraint_name,constraint_schema,constraints,constructors,contains,continue,convert,corr,corresponding,count,covar_pop,covar_samp,create,cross,cube,cume_dist,current,current_collation,current_date,current_default_transform_group,current_path,current_role,current_time,current_timestamp,current_transform_group_for_type,current_user,cursor,cursor_name,cycle,data,date,datetime_interval_code,datetime_interval_precision,day,deallocate,dec,decimal,declare,default,defaults,deferrable,deferred,defined,definer,degree,delete,dense_rank,depth,deref,derived,desc,describe,descriptor,deterministic,diagnostics,disconnect,dispatch,distinct,domain,double,drop,dynamic,dynamic_function,dynamic_function_code,each,element,else,end,end-exec,equals,escape,every,except,exception,exclude,excluding,exec,execute,exists,exp,external,extract,false,fetch,filter,final,first,float,floor,following,for,foreign,fortran,found,free,from,full,function,fusion,g,general,get,global,go,goto,grant,granted,group,grouping,having,hierarchy,hold,hour,identity,immediate,implementation,in,including,increment,indicator,initially,inner,inout,input,insensitive,insert,instance,instantiable,int,integer,intersect,intersection,interval,into,invoker,is,isolation,join,k,key,key_member,key_type,language,large,last,lateral,leading,left,length,level,like,ln,local,localtime,localtimestamp,locator,lower,m,map,match,matched,max,maxvalue,member,merge,message_length,message_octet_length,message_text,method,min,minute,minvalue,mod,modifies,module,month,more,multiset,mumps,name,names,national,natural,nchar,nclob,nesting,new,next,no,none,normalize,normalized,not,\"null\",nullable,nullif,nulls,number,numeric,object,octet_length,octets,of,old,on,only,open,option,options,or,order,ordering,ordinality,others,out,outer,output,over,overlaps,overlay,overriding,pad,parameter,parameter_mode,parameter_name,parameter_ordinal_position,parameter_specific_catalog,parameter_specific_name,parameter_specific_schema,partial,partition,pascal,path,percent_rank,percentile_cont,percentile_disc,placing,pli,position,power,preceding,precision,prepare,preserve,primary,prior,privileges,procedure,public,range,rank,read,reads,real,recursive,ref,references,referencing,regr_avgx,regr_avgy,regr_count,regr_intercept,regr_r2,regr_slope,regr_sxx,regr_sxy,regr_syy,relative,release,repeatable,restart,result,return,returned_cardinality,returned_length,returned_octet_length,returned_sqlstate,returns,revoke,right,role,rollback,rollup,routine,routine_catalog,routine_name,routine_schema,row,row_count,row_number,rows,savepoint,scale,schema,schema_name,scope_catalog,scope_name,scope_schema,scroll,search,second,section,security,select,self,sensitive,sequence,serializable,server_name,session,session_user,set,sets,similar,simple,size,smallint,some,source,space,specific,specific_name,specifictype,sql,sqlexception,sqlstate,sqlwarning,sqrt,start,state,statement,static,stddev_pop,stddev_samp,structure,style,subclass_origin,submultiset,substring,sum,symmetric,system,system_user,table,table_name,tablesample,temporary,then,ties,time,timestamp,timezone_hour,timezone_minute,to,top_level_count,trailing,transaction,transaction_active,transactions_committed,transactions_rolled_back,transform,transforms,translate,translation,treat,trigger,trigger_catalog,trigger_name,trigger_schema,trim,true,type,uescape,unbounded,uncommitted,under,union,unique,unknown,unnamed,unnest,update,upper,usage,user,user_defined_type_catalog,user_defined_type_code,user_defined_type_name,user_defined_type_schema,using,value,values,var_pop,var_samp,varchar,varying,view,when,whenever,where,width_bucket,window,with,within,without,work,write,year,zone}'::text[])";
            Statement stmt = null;
            ResultSet rs = null;

            try {
               stmt = this.connection.createStatement();
               rs = stmt.executeQuery(sql);
               if (!rs.next()) {
                  throw new PSQLException(GT.tr("Unable to find keywords in the system catalogs."), PSQLState.UNEXPECTED_ERROR);
               }

               keywords = rs.getString(1);
            } finally {
               JdbcBlackHole.close(rs);
               JdbcBlackHole.close(stmt);
            }
         } else {
            keywords = "abort,access,aggregate,also,analyse,analyze,backward,bit,cache,checkpoint,class,cluster,comment,concurrently,connection,conversion,copy,csv,database,delimiter,delimiters,disable,do,enable,encoding,encrypted,exclusive,explain,force,forward,freeze,greatest,handler,header,if,ilike,immutable,implicit,index,indexes,inherit,inherits,instead,isnull,least,limit,listen,load,location,lock,mode,move,nothing,notify,notnull,nowait,off,offset,oids,operator,owned,owner,password,prepared,procedural,quote,reassign,recheck,reindex,rename,replace,reset,restrict,returning,rule,setof,share,show,stable,statistics,stdin,stdout,storage,strict,sysid,tablespace,temp,template,truncate,trusted,unencrypted,unlisten,until,vacuum,valid,validator,verbose,volatile";
         }

         this.keywords = (String)Nullness.castNonNull(keywords);
      }

      return keywords;
   }

   public String getNumericFunctions() throws SQLException {
      return "abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,round,sign,sin,sqrt,tan,truncate";
   }

   public String getStringFunctions() throws SQLException {
      String funcs = "ascii,char,concat,lcase,left,length,ltrim,repeat,rtrim,space,substring,ucase";
      funcs = funcs + ",replace";
      return funcs;
   }

   public String getSystemFunctions() throws SQLException {
      return "database,ifnull,user";
   }

   public String getTimeDateFunctions() throws SQLException {
      String timeDateFuncs = "curdate,curtime,dayname,dayofmonth,dayofweek,dayofyear,hour,minute,month,monthname,now,quarter,second,week,year";
      timeDateFuncs = timeDateFuncs + ",timestampadd";
      return timeDateFuncs;
   }

   public String getSearchStringEscape() throws SQLException {
      return "\\";
   }

   public String getExtraNameCharacters() throws SQLException {
      return "";
   }

   public boolean supportsAlterTableWithAddColumn() throws SQLException {
      return true;
   }

   public boolean supportsAlterTableWithDropColumn() throws SQLException {
      return true;
   }

   public boolean supportsColumnAliasing() throws SQLException {
      return true;
   }

   public boolean nullPlusNonNullIsNull() throws SQLException {
      return true;
   }

   public boolean supportsConvert() throws SQLException {
      return false;
   }

   public boolean supportsConvert(int fromType, int toType) throws SQLException {
      return false;
   }

   public boolean supportsTableCorrelationNames() throws SQLException {
      return true;
   }

   public boolean supportsDifferentTableCorrelationNames() throws SQLException {
      return false;
   }

   public boolean supportsExpressionsInOrderBy() throws SQLException {
      return true;
   }

   public boolean supportsOrderByUnrelated() throws SQLException {
      return true;
   }

   public boolean supportsGroupBy() throws SQLException {
      return true;
   }

   public boolean supportsGroupByUnrelated() throws SQLException {
      return true;
   }

   public boolean supportsGroupByBeyondSelect() throws SQLException {
      return true;
   }

   public boolean supportsLikeEscapeClause() throws SQLException {
      return true;
   }

   public boolean supportsMultipleResultSets() throws SQLException {
      return true;
   }

   public boolean supportsMultipleTransactions() throws SQLException {
      return true;
   }

   public boolean supportsNonNullableColumns() throws SQLException {
      return true;
   }

   public boolean supportsMinimumSQLGrammar() throws SQLException {
      return true;
   }

   public boolean supportsCoreSQLGrammar() throws SQLException {
      return false;
   }

   public boolean supportsExtendedSQLGrammar() throws SQLException {
      return false;
   }

   public boolean supportsANSI92EntryLevelSQL() throws SQLException {
      return true;
   }

   public boolean supportsANSI92IntermediateSQL() throws SQLException {
      return false;
   }

   public boolean supportsANSI92FullSQL() throws SQLException {
      return false;
   }

   public boolean supportsIntegrityEnhancementFacility() throws SQLException {
      return true;
   }

   public boolean supportsOuterJoins() throws SQLException {
      return true;
   }

   public boolean supportsFullOuterJoins() throws SQLException {
      return true;
   }

   public boolean supportsLimitedOuterJoins() throws SQLException {
      return true;
   }

   public String getSchemaTerm() throws SQLException {
      return "schema";
   }

   public String getProcedureTerm() throws SQLException {
      return "function";
   }

   public String getCatalogTerm() throws SQLException {
      return "database";
   }

   public boolean isCatalogAtStart() throws SQLException {
      return true;
   }

   public String getCatalogSeparator() throws SQLException {
      return ".";
   }

   public boolean supportsSchemasInDataManipulation() throws SQLException {
      return true;
   }

   public boolean supportsSchemasInProcedureCalls() throws SQLException {
      return true;
   }

   public boolean supportsSchemasInTableDefinitions() throws SQLException {
      return true;
   }

   public boolean supportsSchemasInIndexDefinitions() throws SQLException {
      return true;
   }

   public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
      return true;
   }

   public boolean supportsCatalogsInDataManipulation() throws SQLException {
      return false;
   }

   public boolean supportsCatalogsInProcedureCalls() throws SQLException {
      return false;
   }

   public boolean supportsCatalogsInTableDefinitions() throws SQLException {
      return false;
   }

   public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
      return false;
   }

   public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
      return false;
   }

   public boolean supportsPositionedDelete() throws SQLException {
      return false;
   }

   public boolean supportsPositionedUpdate() throws SQLException {
      return false;
   }

   public boolean supportsSelectForUpdate() throws SQLException {
      return true;
   }

   public boolean supportsStoredProcedures() throws SQLException {
      return true;
   }

   public boolean supportsSubqueriesInComparisons() throws SQLException {
      return true;
   }

   public boolean supportsSubqueriesInExists() throws SQLException {
      return true;
   }

   public boolean supportsSubqueriesInIns() throws SQLException {
      return true;
   }

   public boolean supportsSubqueriesInQuantifieds() throws SQLException {
      return true;
   }

   public boolean supportsCorrelatedSubqueries() throws SQLException {
      return true;
   }

   public boolean supportsUnion() throws SQLException {
      return true;
   }

   public boolean supportsUnionAll() throws SQLException {
      return true;
   }

   public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
      return false;
   }

   public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
      return false;
   }

   public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
      return true;
   }

   public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
      return true;
   }

   public int getMaxCharLiteralLength() throws SQLException {
      return 0;
   }

   public int getMaxBinaryLiteralLength() throws SQLException {
      return 0;
   }

   public int getMaxColumnNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getMaxColumnsInGroupBy() throws SQLException {
      return 0;
   }

   public int getMaxColumnsInIndex() throws SQLException {
      return this.getMaxIndexKeys();
   }

   public int getMaxColumnsInOrderBy() throws SQLException {
      return 0;
   }

   public int getMaxColumnsInSelect() throws SQLException {
      return 0;
   }

   public int getMaxColumnsInTable() throws SQLException {
      return 1600;
   }

   public int getMaxConnections() throws SQLException {
      return 8192;
   }

   public int getMaxCursorNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getMaxIndexLength() throws SQLException {
      return 0;
   }

   public int getMaxSchemaNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getMaxProcedureNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getMaxCatalogNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getMaxRowSize() throws SQLException {
      return 1073741824;
   }

   public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
      return false;
   }

   public int getMaxStatementLength() throws SQLException {
      return 0;
   }

   public int getMaxStatements() throws SQLException {
      return 0;
   }

   public int getMaxTableNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getMaxTablesInSelect() throws SQLException {
      return 0;
   }

   public int getMaxUserNameLength() throws SQLException {
      return this.getMaxNameLength();
   }

   public int getDefaultTransactionIsolation() throws SQLException {
      String sql = "SELECT setting FROM pg_catalog.pg_settings WHERE name='default_transaction_isolation'";
      Statement stmt = this.connection.createStatement();

      byte var7;
      label128: {
         label129: {
            label130: {
               try {
                  ResultSet rs;
                  label132: {
                     label133: {
                        label134: {
                           rs = stmt.executeQuery(sql);

                           try {
                              String level = null;
                              if (rs.next()) {
                                 level = rs.getString(1);
                              }

                              if (level == null) {
                                 throw new PSQLException(GT.tr("Unable to determine a value for DefaultTransactionIsolation due to missing  entry in pg_catalog.pg_settings WHERE name='default_transaction_isolation'."), PSQLState.UNEXPECTED_ERROR);
                              }

                              String var5 = level.toLowerCase(Locale.ROOT);
                              byte var6 = -1;
                              switch(var5.hashCode()) {
                              case -1876824735:
                                 if (var5.equals("repeatable read")) {
                                    var6 = 1;
                                 }
                                 break;
                              case -228069006:
                                 if (var5.equals("read committed")) {
                                    var6 = 3;
                                 }
                                 break;
                              case 861034751:
                                 if (var5.equals("serializable")) {
                                    var6 = 2;
                                 }
                                 break;
                              case 1210423673:
                                 if (var5.equals("read uncommitted")) {
                                    var6 = 0;
                                 }
                              }

                              switch(var6) {
                              case 0:
                                 var7 = 1;
                                 break;
                              case 1:
                                 var7 = 4;
                                 break label134;
                              case 2:
                                 var7 = 8;
                                 break label133;
                              case 3:
                              default:
                                 var7 = 2;
                                 break label132;
                              }
                           } catch (Throwable var10) {
                              if (rs != null) {
                                 try {
                                    rs.close();
                                 } catch (Throwable var9) {
                                    var10.addSuppressed(var9);
                                 }
                              }

                              throw var10;
                           }

                           if (rs != null) {
                              rs.close();
                           }
                           break label130;
                        }

                        if (rs != null) {
                           rs.close();
                        }
                        break label129;
                     }

                     if (rs != null) {
                        rs.close();
                     }
                     break label128;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var11) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var8) {
                        var11.addSuppressed(var8);
                     }
                  }

                  throw var11;
               }

               if (stmt != null) {
                  stmt.close();
               }

               return var7;
            }

            if (stmt != null) {
               stmt.close();
            }

            return var7;
         }

         if (stmt != null) {
            stmt.close();
         }

         return var7;
      }

      if (stmt != null) {
         stmt.close();
      }

      return var7;
   }

   public boolean supportsTransactions() throws SQLException {
      return true;
   }

   public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
      switch(level) {
      case 1:
      case 2:
      case 4:
      case 8:
         return true;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         return false;
      }
   }

   public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
      return true;
   }

   public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
      return false;
   }

   public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
      return false;
   }

   public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
      return false;
   }

   protected String escapeQuotes(String s) throws SQLException {
      StringBuilder sb = new StringBuilder();
      if (!this.connection.getStandardConformingStrings()) {
         sb.append("E");
      }

      sb.append("'");
      sb.append(this.connection.escapeString(s));
      sb.append("'");
      return sb.toString();
   }

   public ResultSet getProcedures(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String procedureNamePattern) throws SQLException {
      String sql = "SELECT NULL AS PROCEDURE_CAT, n.nspname AS PROCEDURE_SCHEM, p.proname AS PROCEDURE_NAME, NULL, NULL, NULL, d.description AS REMARKS, 2 AS PROCEDURE_TYPE,  p.proname || '_' || p.oid AS SPECIFIC_NAME  FROM pg_catalog.pg_namespace n, pg_catalog.pg_proc p  LEFT JOIN pg_catalog.pg_description d ON (p.oid=d.objoid)  LEFT JOIN pg_catalog.pg_class c ON (d.classoid=c.oid AND c.relname='pg_proc')  LEFT JOIN pg_catalog.pg_namespace pn ON (c.relnamespace=pn.oid AND pn.nspname='pg_catalog')  WHERE p.pronamespace=n.oid ";
      if (this.connection.haveMinimumServerVersion(ServerVersion.v11)) {
         sql = sql + " AND p.prokind='p'";
      }

      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (procedureNamePattern != null && !procedureNamePattern.isEmpty()) {
         sql = sql + " AND p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
      }

      if (this.connection.getHideUnprivilegedObjects()) {
         sql = sql + " AND has_function_privilege(p.oid,'EXECUTE')";
      }

      sql = sql + " ORDER BY PROCEDURE_SCHEM, PROCEDURE_NAME, p.oid::text ";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
      int columns = 20;
      Field[] f = new Field[columns];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("PROCEDURE_CAT", 1043);
      f[1] = new Field("PROCEDURE_SCHEM", 1043);
      f[2] = new Field("PROCEDURE_NAME", 1043);
      f[3] = new Field("COLUMN_NAME", 1043);
      f[4] = new Field("COLUMN_TYPE", 21);
      f[5] = new Field("DATA_TYPE", 21);
      f[6] = new Field("TYPE_NAME", 1043);
      f[7] = new Field("PRECISION", 23);
      f[8] = new Field("LENGTH", 23);
      f[9] = new Field("SCALE", 21);
      f[10] = new Field("RADIX", 21);
      f[11] = new Field("NULLABLE", 21);
      f[12] = new Field("REMARKS", 1043);
      f[13] = new Field("COLUMN_DEF", 1043);
      f[14] = new Field("SQL_DATA_TYPE", 23);
      f[15] = new Field("SQL_DATETIME_SUB", 23);
      f[16] = new Field("CHAR_OCTET_LENGTH", 23);
      f[17] = new Field("ORDINAL_POSITION", 23);
      f[18] = new Field("IS_NULLABLE", 1043);
      f[19] = new Field("SPECIFIC_NAME", 1043);
      String sql = "SELECT n.nspname,p.proname,p.prorettype,p.proargtypes, t.typtype,t.typrelid,  p.proargnames, p.proargmodes, p.proallargtypes, p.oid  FROM pg_catalog.pg_proc p, pg_catalog.pg_namespace n, pg_catalog.pg_type t  WHERE p.pronamespace=n.oid AND p.prorettype=t.oid ";
      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (procedureNamePattern != null && !procedureNamePattern.isEmpty()) {
         sql = sql + " AND p.proname LIKE " + this.escapeQuotes(procedureNamePattern);
      }

      sql = sql + " ORDER BY n.nspname, p.proname, p.oid::text ";
      byte[] isnullableUnknown = new byte[0];
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while(true) {
         byte[] schema;
         byte[] procedureName;
         byte[] specificName;
         String returnTypeType;
         int returnTypeRelid;
         Array argModesArray;
         int argOid;
         do {
            if (!rs.next()) {
               rs.close();
               stmt.close();
               return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
            }

            schema = rs.getBytes("nspname");
            procedureName = rs.getBytes("proname");
            specificName = this.connection.encodeString(rs.getString("proname") + "_" + rs.getString("oid"));
            int returnType = (int)rs.getLong("prorettype");
            returnTypeType = rs.getString("typtype");
            returnTypeRelid = (int)rs.getLong("typrelid");
            String strArgTypes = (String)Nullness.castNonNull(rs.getString("proargtypes"));
            StringTokenizer st = new StringTokenizer(strArgTypes);
            ArrayList argTypes = new ArrayList();

            while(st.hasMoreTokens()) {
               argTypes.add(Long.valueOf(st.nextToken()));
            }

            String[] argNames = null;
            Array argNamesArray = rs.getArray("proargnames");
            if (argNamesArray != null) {
               argNames = (String[])argNamesArray.getArray();
            }

            String[] argModes = null;
            argModesArray = rs.getArray("proargmodes");
            if (argModesArray != null) {
               argModes = (String[])argModesArray.getArray();
            }

            int numArgs = argTypes.size();
            Long[] allArgTypes = null;
            Array allArgTypesArray = rs.getArray("proallargtypes");
            if (allArgTypesArray != null) {
               allArgTypes = (Long[])allArgTypesArray.getArray();
               numArgs = allArgTypes.length;
            }

            if ("b".equals(returnTypeType) || "d".equals(returnTypeType) || "e".equals(returnTypeType) || "p".equals(returnTypeType) && argModesArray == null) {
               byte[][] tuple = new byte[columns][];
               tuple[0] = null;
               tuple[1] = schema;
               tuple[2] = procedureName;
               tuple[3] = this.connection.encodeString("returnValue");
               tuple[4] = this.connection.encodeString(Integer.toString(5));
               tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(returnType)));
               tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(returnType));
               tuple[7] = null;
               tuple[8] = null;
               tuple[9] = null;
               tuple[10] = null;
               tuple[11] = this.connection.encodeString(Integer.toString(2));
               tuple[12] = null;
               tuple[17] = this.connection.encodeString(Integer.toString(0));
               tuple[18] = isnullableUnknown;
               tuple[19] = specificName;
               v.add(new Tuple(tuple));
            }

            for(int i = 0; i < numArgs; ++i) {
               byte[][] tuple = new byte[columns][];
               tuple[0] = null;
               tuple[1] = schema;
               tuple[2] = procedureName;
               if (argNames != null) {
                  tuple[3] = this.connection.encodeString(argNames[i]);
               } else {
                  tuple[3] = this.connection.encodeString("$" + (i + 1));
               }

               int columnMode = 1;
               if (argModes != null && "o".equals(argModes[i])) {
                  columnMode = 4;
               } else if (argModes != null && "b".equals(argModes[i])) {
                  columnMode = 2;
               } else if (argModes != null && "t".equals(argModes[i])) {
                  columnMode = 5;
               }

               tuple[4] = this.connection.encodeString(Integer.toString(columnMode));
               if (allArgTypes != null) {
                  argOid = allArgTypes[i].intValue();
               } else {
                  argOid = ((Long)argTypes.get(i)).intValue();
               }

               tuple[5] = this.connection.encodeString(Integer.toString((Integer)Nullness.castNonNull(this.connection.getTypeInfo().getSQLType(argOid))));
               tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
               tuple[7] = null;
               tuple[8] = null;
               tuple[9] = null;
               tuple[10] = null;
               tuple[11] = this.connection.encodeString(Integer.toString(2));
               tuple[12] = null;
               tuple[17] = this.connection.encodeString(Integer.toString(i + 1));
               tuple[18] = isnullableUnknown;
               tuple[19] = specificName;
               v.add(new Tuple(tuple));
            }
         } while(!"c".equals(returnTypeType) && (!"p".equals(returnTypeType) || argModesArray == null));

         String columnsql = "SELECT a.attname,a.atttypid FROM pg_catalog.pg_attribute a  WHERE a.attrelid = " + returnTypeRelid + " AND NOT a.attisdropped AND a.attnum > 0 ORDER BY a.attnum ";
         Statement columnstmt = this.connection.createStatement();
         ResultSet columnrs = columnstmt.executeQuery(columnsql);

         while(columnrs.next()) {
            argOid = (int)columnrs.getLong("atttypid");
            byte[][] tuple = new byte[columns][];
            tuple[0] = null;
            tuple[1] = schema;
            tuple[2] = procedureName;
            tuple[3] = columnrs.getBytes("attname");
            tuple[4] = this.connection.encodeString(Integer.toString(3));
            tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
            tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
            tuple[7] = null;
            tuple[8] = null;
            tuple[9] = null;
            tuple[10] = null;
            tuple[11] = this.connection.encodeString(Integer.toString(2));
            tuple[12] = null;
            tuple[17] = this.connection.encodeString(Integer.toString(0));
            tuple[18] = isnullableUnknown;
            tuple[19] = specificName;
            v.add(new Tuple(tuple));
         }

         columnrs.close();
         columnstmt.close();
      }
   }

   public ResultSet getTables(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String tableNamePattern, @Nullable String[] types) throws SQLException {
      String useSchemas = "SCHEMAS";
      String select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, c.relname AS TABLE_NAME,  CASE n.nspname ~ '^pg_' OR n.nspname = 'information_schema'  WHEN true THEN CASE  WHEN n.nspname = 'pg_catalog' OR n.nspname = 'information_schema' THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TABLE'   WHEN 'v' THEN 'SYSTEM VIEW'   WHEN 'i' THEN 'SYSTEM INDEX'   ELSE NULL   END  WHEN n.nspname = 'pg_toast' THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TOAST TABLE'   WHEN 'i' THEN 'SYSTEM TOAST INDEX'   ELSE NULL   END  ELSE CASE c.relkind   WHEN 'r' THEN 'TEMPORARY TABLE'   WHEN 'p' THEN 'TEMPORARY TABLE'   WHEN 'i' THEN 'TEMPORARY INDEX'   WHEN 'S' THEN 'TEMPORARY SEQUENCE'   WHEN 'v' THEN 'TEMPORARY VIEW'   ELSE NULL   END  END  WHEN false THEN CASE c.relkind  WHEN 'r' THEN 'TABLE'  WHEN 'p' THEN 'PARTITIONED TABLE'  WHEN 'i' THEN 'INDEX'  WHEN 'P' then 'PARTITIONED INDEX'  WHEN 'S' THEN 'SEQUENCE'  WHEN 'v' THEN 'VIEW'  WHEN 'c' THEN 'TYPE'  WHEN 'f' THEN 'FOREIGN TABLE'  WHEN 'm' THEN 'MATERIALIZED VIEW'  ELSE NULL  END  ELSE NULL  END  AS TABLE_TYPE, d.description AS REMARKS,  '' as TYPE_CAT, '' as TYPE_SCHEM, '' as TYPE_NAME, '' AS SELF_REFERENCING_COL_NAME, '' AS REF_GENERATION  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c  LEFT JOIN pg_catalog.pg_description d ON (c.oid = d.objoid AND d.objsubid = 0  and d.classoid = 'pg_class'::regclass)  WHERE c.relnamespace = n.oid ";
      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         select = select + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (this.connection.getHideUnprivilegedObjects()) {
         select = select + " AND has_table_privilege(c.oid,  'SELECT, INSERT, UPDATE, DELETE, RULE, REFERENCES, TRIGGER')";
      }

      String orderby = " ORDER BY TABLE_TYPE,TABLE_SCHEM,TABLE_NAME ";
      if (tableNamePattern != null && !tableNamePattern.isEmpty()) {
         select = select + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
      }

      if (types != null) {
         select = select + " AND (false ";
         StringBuilder orclause = new StringBuilder();
         String[] var9 = types;
         int var10 = types.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String type = var9[var11];
            Map<String, String> clauses = (Map)tableTypeClauses.get(type);
            if (clauses != null) {
               String clause = (String)clauses.get(useSchemas);
               orclause.append(" OR ( ").append(clause).append(" ) ");
            }
         }

         select = select + orclause.toString() + ") ";
      }

      String sql = select + orderby;
      return ((PgResultSet)this.createMetaDataStatement().executeQuery(sql)).upperCaseFieldLabels();
   }

   public ResultSet getSchemas() throws SQLException {
      return this.getSchemas((String)null, (String)null);
   }

   public ResultSet getSchemas(@Nullable String catalog, @Nullable String schemaPattern) throws SQLException {
      String sql = "SELECT nspname AS TABLE_SCHEM, NULL AS TABLE_CATALOG FROM pg_catalog.pg_namespace  WHERE nspname <> 'pg_toast' AND (nspname !~ '^pg_temp_'  OR nspname = (pg_catalog.current_schemas(true))[1]) AND (nspname !~ '^pg_toast_temp_'  OR nspname = replace((pg_catalog.current_schemas(true))[1], 'pg_temp_', 'pg_toast_temp_')) ";
      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (this.connection.getHideUnprivilegedObjects()) {
         sql = sql + " AND has_schema_privilege(nspname, 'USAGE, CREATE')";
      }

      sql = sql + " ORDER BY TABLE_SCHEM";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   public ResultSet getCatalogs() throws SQLException {
      String sql = "SELECT datname AS TABLE_CAT FROM pg_catalog.pg_database WHERE datallowconn = true ORDER BY datname";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   public ResultSet getTableTypes() throws SQLException {
      String[] types = (String[])tableTypeClauses.keySet().toArray(new String[0]);
      Arrays.sort(types);
      Field[] f = new Field[1];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("TABLE_TYPE", 1043);
      String[] var4 = types;
      int var5 = types.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String type = var4[var6];
         byte[][] tuple = new byte[][]{this.connection.encodeString(type)};
         v.add(new Tuple(tuple));
      }

      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
      int numberOfFields = 24;
      List<Tuple> v = new ArrayList();
      Field[] f = new Field[numberOfFields];
      f[0] = new Field("TABLE_CAT", 1043);
      f[1] = new Field("TABLE_SCHEM", 1043);
      f[2] = new Field("TABLE_NAME", 1043);
      f[3] = new Field("COLUMN_NAME", 1043);
      f[4] = new Field("DATA_TYPE", 21);
      f[5] = new Field("TYPE_NAME", 1043);
      f[6] = new Field("COLUMN_SIZE", 23);
      f[7] = new Field("BUFFER_LENGTH", 1043);
      f[8] = new Field("DECIMAL_DIGITS", 23);
      f[9] = new Field("NUM_PREC_RADIX", 23);
      f[10] = new Field("NULLABLE", 23);
      f[11] = new Field("REMARKS", 1043);
      f[12] = new Field("COLUMN_DEF", 1043);
      f[13] = new Field("SQL_DATA_TYPE", 23);
      f[14] = new Field("SQL_DATETIME_SUB", 23);
      f[15] = new Field("CHAR_OCTET_LENGTH", 1043);
      f[16] = new Field("ORDINAL_POSITION", 23);
      f[17] = new Field("IS_NULLABLE", 1043);
      f[18] = new Field("SCOPE_CATALOG", 1043);
      f[19] = new Field("SCOPE_SCHEMA", 1043);
      f[20] = new Field("SCOPE_TABLE", 1043);
      f[21] = new Field("SOURCE_DATA_TYPE", 21);
      f[22] = new Field("IS_AUTOINCREMENT", 1043);
      f[23] = new Field("IS_GENERATEDCOLUMN", 1043);
      String sql;
      if (this.connection.haveMinimumServerVersion(ServerVersion.v8_4)) {
         sql = "SELECT * FROM (";
      } else {
         sql = "";
      }

      sql = sql + "SELECT n.nspname,c.relname,a.attname,a.atttypid,a.attnotnull OR (t.typtype = 'd' AND t.typnotnull) AS attnotnull,a.atttypmod,a.attlen,t.typtypmod,";
      if (this.connection.haveMinimumServerVersion(ServerVersion.v8_4)) {
         sql = sql + "row_number() OVER (PARTITION BY a.attrelid ORDER BY a.attnum) AS attnum, ";
      } else {
         sql = sql + "a.attnum,";
      }

      if (this.connection.haveMinimumServerVersion(ServerVersion.v10)) {
         sql = sql + "nullif(a.attidentity, '') as attidentity,";
      } else {
         sql = sql + "null as attidentity,";
      }

      if (this.connection.haveMinimumServerVersion(ServerVersion.v12)) {
         sql = sql + "nullif(a.attgenerated, '') as attgenerated,";
      } else {
         sql = sql + "null as attgenerated,";
      }

      sql = sql + "pg_catalog.pg_get_expr(def.adbin, def.adrelid) AS adsrc,dsc.description,t.typbasetype,t.typtype  FROM pg_catalog.pg_namespace n  JOIN pg_catalog.pg_class c ON (c.relnamespace = n.oid)  JOIN pg_catalog.pg_attribute a ON (a.attrelid=c.oid)  JOIN pg_catalog.pg_type t ON (a.atttypid = t.oid)  LEFT JOIN pg_catalog.pg_attrdef def ON (a.attrelid=def.adrelid AND a.attnum = def.adnum)  LEFT JOIN pg_catalog.pg_description dsc ON (c.oid=dsc.objoid AND a.attnum = dsc.objsubid)  LEFT JOIN pg_catalog.pg_class dc ON (dc.oid=dsc.classoid AND dc.relname='pg_class')  LEFT JOIN pg_catalog.pg_namespace dn ON (dc.relnamespace=dn.oid AND dn.nspname='pg_catalog')  WHERE c.relkind in ('r','p','v','f','m') and a.attnum > 0 AND NOT a.attisdropped ";
      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (tableNamePattern != null && !tableNamePattern.isEmpty()) {
         sql = sql + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
      }

      if (this.connection.haveMinimumServerVersion(ServerVersion.v8_4)) {
         sql = sql + ") c WHERE true ";
      }

      if (columnNamePattern != null && !columnNamePattern.isEmpty()) {
         sql = sql + " AND attname LIKE " + this.escapeQuotes(columnNamePattern);
      }

      sql = sql + " ORDER BY nspname,c.relname,attnum ";
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()) {
         byte[][] tuple = new byte[numberOfFields][];
         int typeOid = (int)rs.getLong("atttypid");
         int typeMod = rs.getInt("atttypmod");
         tuple[0] = null;
         tuple[1] = rs.getBytes("nspname");
         tuple[2] = rs.getBytes("relname");
         tuple[3] = rs.getBytes("attname");
         String typtype = rs.getString("typtype");
         int sqlType;
         if ("c".equals(typtype)) {
            sqlType = 2002;
         } else if ("d".equals(typtype)) {
            sqlType = 2001;
         } else if ("e".equals(typtype)) {
            sqlType = 12;
         } else {
            sqlType = this.connection.getTypeInfo().getSQLType(typeOid);
         }

         tuple[4] = this.connection.encodeString(Integer.toString(sqlType));
         String pgType = this.connection.getTypeInfo().getPGType(typeOid);
         tuple[5] = this.connection.encodeString(pgType);
         tuple[7] = null;
         String defval = rs.getString("adsrc");
         if (defval != null && defval.contains("nextval(")) {
            if ("int4".equals(pgType)) {
               tuple[5] = this.connection.encodeString("serial");
            } else if ("int8".equals(pgType)) {
               tuple[5] = this.connection.encodeString("bigserial");
            } else if ("int2".equals(pgType) && this.connection.haveMinimumServerVersion(ServerVersion.v9_2)) {
               tuple[5] = this.connection.encodeString("smallserial");
            }
         }

         String identity = rs.getString("attidentity");
         String generated = rs.getString("attgenerated");
         int baseTypeOid = (int)rs.getLong("typbasetype");
         int decimalDigits;
         int columnSize;
         if (sqlType == 2001) {
            int typtypmod = rs.getInt("typtypmod");
            decimalDigits = this.connection.getTypeInfo().getScale(baseTypeOid, typeMod);
            if (typtypmod == -1) {
               columnSize = this.connection.getTypeInfo().getPrecision(baseTypeOid, typeMod);
            } else if (baseTypeOid == 1700) {
               decimalDigits = this.connection.getTypeInfo().getScale(baseTypeOid, typtypmod);
               columnSize = this.connection.getTypeInfo().getPrecision(baseTypeOid, typtypmod);
            } else {
               columnSize = typtypmod;
            }
         } else {
            decimalDigits = this.connection.getTypeInfo().getScale(typeOid, typeMod);
            columnSize = this.connection.getTypeInfo().getPrecision(typeOid, typeMod);
            if (sqlType != 2 && columnSize == 0) {
               columnSize = this.connection.getTypeInfo().getDisplaySize(typeOid, typeMod);
            }
         }

         tuple[6] = this.connection.encodeString(Integer.toString(columnSize));
         if ((sqlType == 2 || sqlType == 3) && typeMod == -1) {
            tuple[8] = null;
         } else {
            tuple[8] = this.connection.encodeString(Integer.toString(decimalDigits));
         }

         tuple[9] = this.connection.encodeString("10");
         if ("bit".equals(pgType) || "varbit".equals(pgType)) {
            tuple[9] = this.connection.encodeString("2");
         }

         tuple[10] = this.connection.encodeString(Integer.toString(rs.getBoolean("attnotnull") ? 0 : 1));
         tuple[11] = rs.getBytes("description");
         tuple[12] = rs.getBytes("adsrc");
         tuple[13] = null;
         tuple[14] = null;
         tuple[15] = tuple[6];
         tuple[16] = this.connection.encodeString(String.valueOf(rs.getInt("attnum")));
         tuple[17] = this.connection.encodeString(rs.getBoolean("attnotnull") ? "NO" : "YES");
         tuple[18] = null;
         tuple[19] = null;
         tuple[20] = null;
         tuple[21] = baseTypeOid == 0 ? null : this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(baseTypeOid)));
         String autoinc = "NO";
         if (defval != null && defval.contains("nextval(") || identity != null) {
            autoinc = "YES";
         }

         tuple[22] = this.connection.encodeString(autoinc);
         String generatedcolumn = "NO";
         if (generated != null) {
            generatedcolumn = "YES";
         }

         tuple[23] = this.connection.encodeString(generatedcolumn);
         v.add(new Tuple(tuple));
      }

      rs.close();
      stmt.close();
      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
      Field[] f = new Field[8];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("TABLE_CAT", 1043);
      f[1] = new Field("TABLE_SCHEM", 1043);
      f[2] = new Field("TABLE_NAME", 1043);
      f[3] = new Field("COLUMN_NAME", 1043);
      f[4] = new Field("GRANTOR", 1043);
      f[5] = new Field("GRANTEE", 1043);
      f[6] = new Field("PRIVILEGE", 1043);
      f[7] = new Field("IS_GRANTABLE", 1043);
      String sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl, " + (this.connection.haveMinimumServerVersion(ServerVersion.v8_4) ? "a.attacl, " : "") + " a.attname  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c,  pg_catalog.pg_roles r, pg_catalog.pg_attribute a  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.oid = a.attrelid  AND c.relkind = 'r'  AND a.attnum > 0 AND NOT a.attisdropped ";
      if (schema != null && !schema.isEmpty()) {
         sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
      }

      if (table != null && !table.isEmpty()) {
         sql = sql + " AND c.relname = " + this.escapeQuotes(table);
      }

      if (columnNamePattern != null && !columnNamePattern.isEmpty()) {
         sql = sql + " AND a.attname LIKE " + this.escapeQuotes(columnNamePattern);
      }

      sql = sql + " ORDER BY attname ";
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()) {
         byte[] schemaName = rs.getBytes("nspname");
         byte[] tableName = rs.getBytes("relname");
         byte[] column = rs.getBytes("attname");
         String owner = (String)Nullness.castNonNull(rs.getString("rolname"));
         String relAcl = rs.getString("relacl");
         Map<String, Map<String, List<String[]>>> permissions = this.parseACL(relAcl, owner);
         if (this.connection.haveMinimumServerVersion(ServerVersion.v8_4)) {
            String acl = rs.getString("attacl");
            Map<String, Map<String, List<String[]>>> relPermissions = this.parseACL(acl, owner);
            permissions.putAll(relPermissions);
         }

         String[] permNames = (String[])permissions.keySet().toArray(new String[0]);
         Arrays.sort(permNames);
         String[] var32 = permNames;
         int var18 = permNames.length;

         for(int var19 = 0; var19 < var18; ++var19) {
            String permName = var32[var19];
            byte[] privilege = this.connection.encodeString(permName);
            Map<String, List<String[]>> grantees = (Map)permissions.get(permName);
            Iterator var23 = grantees.entrySet().iterator();

            while(var23.hasNext()) {
               Entry<String, List<String[]>> userToGrantable = (Entry)var23.next();
               List<String[]> grantor = (List)userToGrantable.getValue();
               String grantee = (String)userToGrantable.getKey();
               Iterator var27 = grantor.iterator();

               while(var27.hasNext()) {
                  String[] grants = (String[])var27.next();
                  String grantable = owner.equals(grantee) ? "YES" : grants[1];
                  byte[][] tuple = new byte[][]{null, schemaName, tableName, column, this.connection.encodeString(grants[0]), this.connection.encodeString(grantee), privilege, this.connection.encodeString(grantable)};
                  v.add(new Tuple(tuple));
               }
            }
         }
      }

      rs.close();
      stmt.close();
      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
      Field[] f = new Field[7];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("TABLE_CAT", 1043);
      f[1] = new Field("TABLE_SCHEM", 1043);
      f[2] = new Field("TABLE_NAME", 1043);
      f[3] = new Field("GRANTOR", 1043);
      f[4] = new Field("GRANTEE", 1043);
      f[5] = new Field("PRIVILEGE", 1043);
      f[6] = new Field("IS_GRANTABLE", 1043);
      String sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c, pg_catalog.pg_roles r  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.relkind IN ('r','p','v','m','f') ";
      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (tableNamePattern != null && !tableNamePattern.isEmpty()) {
         sql = sql + " AND c.relname LIKE " + this.escapeQuotes(tableNamePattern);
      }

      sql = sql + " ORDER BY nspname, relname ";
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()) {
         byte[] schema = rs.getBytes("nspname");
         byte[] table = rs.getBytes("relname");
         String owner = (String)Nullness.castNonNull(rs.getString("rolname"));
         String acl = rs.getString("relacl");
         Map<String, Map<String, List<String[]>>> permissions = this.parseACL(acl, owner);
         String[] permNames = (String[])permissions.keySet().toArray(new String[0]);
         Arrays.sort(permNames);
         String[] var15 = permNames;
         int var16 = permNames.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            String permName = var15[var17];
            byte[] privilege = this.connection.encodeString(permName);
            Map<String, List<String[]>> grantees = (Map)permissions.get(permName);
            Iterator var21 = grantees.entrySet().iterator();

            while(var21.hasNext()) {
               Entry<String, List<String[]>> userToGrantable = (Entry)var21.next();
               List<String[]> grants = (List)userToGrantable.getValue();
               String granteeUser = (String)userToGrantable.getKey();
               Iterator var25 = grants.iterator();

               while(var25.hasNext()) {
                  String[] grantTuple = (String[])var25.next();
                  String grantor = grantTuple[0] == null ? owner : grantTuple[0];
                  String grantable = owner.equals(granteeUser) ? "YES" : grantTuple[1];
                  byte[][] tuple = new byte[][]{null, schema, table, this.connection.encodeString(grantor), this.connection.encodeString(granteeUser), privilege, this.connection.encodeString(grantable)};
                  v.add(new Tuple(tuple));
               }
            }
         }
      }

      rs.close();
      stmt.close();
      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   private static List<String> parseACLArray(String aclString) {
      List<String> acls = new ArrayList();
      if (aclString != null && !aclString.isEmpty()) {
         boolean inQuotes = false;
         int beginIndex = 1;
         char prevChar = ' ';

         int i;
         for(i = beginIndex; i < aclString.length(); ++i) {
            char c = aclString.charAt(i);
            if (c == '"' && prevChar != '\\') {
               inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
               acls.add(aclString.substring(beginIndex, i));
               beginIndex = i + 1;
            }

            prevChar = c;
         }

         acls.add(aclString.substring(beginIndex, aclString.length() - 1));

         for(i = 0; i < acls.size(); ++i) {
            String acl = (String)acls.get(i);
            if (acl.startsWith("\"") && acl.endsWith("\"")) {
               acl = acl.substring(1, acl.length() - 1);
               acls.set(i, acl);
            }
         }

         return acls;
      } else {
         return acls;
      }
   }

   private static void addACLPrivileges(String acl, Map<String, Map<String, List<String[]>>> privileges) {
      int equalIndex = acl.lastIndexOf("=");
      int slashIndex = acl.lastIndexOf("/");
      if (equalIndex != -1) {
         String user = acl.substring(0, equalIndex);
         String grantor = null;
         if (user.isEmpty()) {
            user = "PUBLIC";
         }

         String privs;
         if (slashIndex != -1) {
            privs = acl.substring(equalIndex + 1, slashIndex);
            grantor = acl.substring(slashIndex + 1, acl.length());
         } else {
            privs = acl.substring(equalIndex + 1, acl.length());
         }

         for(int i = 0; i < privs.length(); ++i) {
            char c = privs.charAt(i);
            if (c != '*') {
               String grantable;
               if (i < privs.length() - 1 && privs.charAt(i + 1) == '*') {
                  grantable = "YES";
               } else {
                  grantable = "NO";
               }

               String sqlpriv;
               switch(c) {
               case 'C':
                  sqlpriv = "CREATE";
                  break;
               case 'D':
                  sqlpriv = "TRUNCATE";
                  break;
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'S':
               case 'V':
               case 'W':
               case 'Y':
               case 'Z':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'b':
               case 'c':
               case 'e':
               case 'f':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'n':
               case 'o':
               case 'q':
               case 's':
               case 'u':
               case 'v':
               default:
                  sqlpriv = "UNKNOWN";
                  break;
               case 'R':
                  sqlpriv = "RULE";
                  break;
               case 'T':
                  sqlpriv = "CREATE TEMP";
                  break;
               case 'U':
                  sqlpriv = "USAGE";
                  break;
               case 'X':
                  sqlpriv = "EXECUTE";
                  break;
               case 'a':
                  sqlpriv = "INSERT";
                  break;
               case 'd':
                  sqlpriv = "DELETE";
                  break;
               case 'p':
               case 'r':
                  sqlpriv = "SELECT";
                  break;
               case 't':
                  sqlpriv = "TRIGGER";
                  break;
               case 'w':
                  sqlpriv = "UPDATE";
                  break;
               case 'x':
                  sqlpriv = "REFERENCES";
               }

               Map<String, List<String[]>> usersWithPermission = (Map)privileges.get(sqlpriv);
               if (usersWithPermission == null) {
                  usersWithPermission = new HashMap();
                  privileges.put(sqlpriv, usersWithPermission);
               }

               List<String[]> permissionByGrantor = (List)((Map)usersWithPermission).get(user);
               if (permissionByGrantor == null) {
                  permissionByGrantor = new ArrayList();
                  ((Map)usersWithPermission).put(user, permissionByGrantor);
               }

               String[] grant = new String[]{grantor, grantable};
               ((List)permissionByGrantor).add(grant);
            }
         }

      }
   }

   public Map<String, Map<String, List<String[]>>> parseACL(String aclArray, String owner) {
      if (aclArray == null) {
         String perms = this.connection.haveMinimumServerVersion(ServerVersion.v8_4) ? "arwdDxt" : "arwdxt";
         aclArray = "{" + owner + "=" + perms + "/" + owner + "}";
      }

      List<String> acls = parseACLArray(aclArray);
      Map<String, Map<String, List<String[]>>> privileges = new HashMap();
      Iterator var5 = acls.iterator();

      while(var5.hasNext()) {
         String acl = (String)var5.next();
         addACLPrivileges(acl, privileges);
      }

      return privileges;
   }

   public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
      Field[] f = new Field[8];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("SCOPE", 21);
      f[1] = new Field("COLUMN_NAME", 1043);
      f[2] = new Field("DATA_TYPE", 21);
      f[3] = new Field("TYPE_NAME", 1043);
      f[4] = new Field("COLUMN_SIZE", 23);
      f[5] = new Field("BUFFER_LENGTH", 23);
      f[6] = new Field("DECIMAL_DIGITS", 21);
      f[7] = new Field("PSEUDO_COLUMN", 21);
      String sql = "SELECT a.attname, a.atttypid, atttypmod FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN (SELECT i.indexrelid, i.indrelid, i.indisprimary,              information_schema._pg_expandarray(i.indkey) AS keys         FROM pg_catalog.pg_index i) i     ON (a.attnum = (i.keys).x AND a.attrelid = i.indrelid) WHERE true ";
      if (schema != null && !schema.isEmpty()) {
         sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
      }

      sql = sql + " AND ct.relname = " + this.escapeQuotes(table) + " AND i.indisprimary  ORDER BY a.attnum ";
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()) {
         byte[][] tuple = new byte[8][];
         int typeOid = (int)rs.getLong("atttypid");
         int sqlType = this.connection.getTypeInfo().getSQLType(typeOid);
         int typeMod = rs.getInt("atttypmod");
         int decimalDigits = this.connection.getTypeInfo().getScale(typeOid, typeMod);
         int columnSize = this.connection.getTypeInfo().getPrecision(typeOid, typeMod);
         if (sqlType != 2 && columnSize == 0) {
            columnSize = this.connection.getTypeInfo().getDisplaySize(typeOid, typeMod);
         }

         tuple[0] = this.connection.encodeString(Integer.toString(scope));
         tuple[1] = rs.getBytes("attname");
         tuple[2] = this.connection.encodeString(Integer.toString(sqlType));
         tuple[3] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(typeOid));
         tuple[4] = this.connection.encodeString(Integer.toString(columnSize));
         tuple[5] = null;
         tuple[6] = this.connection.encodeString(Integer.toString(decimalDigits));
         tuple[7] = this.connection.encodeString(Integer.toString(1));
         v.add(new Tuple(tuple));
      }

      rs.close();
      stmt.close();
      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
      Field[] f = new Field[8];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("SCOPE", 21);
      f[1] = new Field("COLUMN_NAME", 1043);
      f[2] = new Field("DATA_TYPE", 21);
      f[3] = new Field("TYPE_NAME", 1043);
      f[4] = new Field("COLUMN_SIZE", 23);
      f[5] = new Field("BUFFER_LENGTH", 23);
      f[6] = new Field("DECIMAL_DIGITS", 21);
      f[7] = new Field("PSEUDO_COLUMN", 21);
      byte[][] tuple = new byte[][]{null, this.connection.encodeString("ctid"), this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType("tid"))), this.connection.encodeString("tid"), null, null, null, this.connection.encodeString(Integer.toString(2))};
      v.add(new Tuple(tuple));
      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public ResultSet getPrimaryKeys(@Nullable String catalog, @Nullable String schema, String table) throws SQLException {
      String sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, a.attname AS COLUMN_NAME,   (information_schema._pg_expandarray(i.indkey)).n AS KEY_SEQ, ci.relname AS PK_NAME,   information_schema._pg_expandarray(i.indkey) AS KEYS, a.attnum AS A_ATTNUM FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN pg_catalog.pg_index i ON ( a.attrelid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid) WHERE true ";
      if (schema != null && !schema.isEmpty()) {
         sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
      }

      if (table != null && !table.isEmpty()) {
         sql = sql + " AND ct.relname = " + this.escapeQuotes(table);
      }

      sql = sql + " AND i.indisprimary ";
      sql = "SELECT        result.TABLE_CAT,        result.TABLE_SCHEM,        result.TABLE_NAME,        result.COLUMN_NAME,        result.KEY_SEQ,        result.PK_NAME FROM      (" + sql + " ) result where  result.A_ATTNUM = (result.KEYS).x ";
      sql = sql + " ORDER BY result.table_name, result.pk_name, result.key_seq";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   protected ResultSet getPrimaryUniqueKeys(@Nullable String catalog, @Nullable String schema, String table) throws SQLException {
      String sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, a.attname AS COLUMN_NAME,   (information_schema._pg_expandarray(i.indkey)).n AS KEY_SEQ, ci.relname AS PK_NAME,   information_schema._pg_expandarray(i.indkey) AS KEYS, a.attnum AS A_ATTNUM,   a.attnotnull AS IS_NOT_NULL FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN pg_catalog.pg_index i ON ( a.attrelid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid) WHERE (i.indisprimary OR (     i.indisunique     AND i.indisvalid     AND i.indpred IS NULL     AND i.indexprs IS NULL   )) ";
      if (schema != null && !schema.isEmpty()) {
         sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
      }

      if (table != null && !table.isEmpty()) {
         sql = sql + " AND ct.relname = " + this.escapeQuotes(table);
      }

      sql = "SELECT        result.TABLE_CAT,        result.TABLE_SCHEM,        result.TABLE_NAME,        result.COLUMN_NAME,        result.KEY_SEQ,        result.PK_NAME,        result.IS_NOT_NULL FROM      (" + sql + " ) result where  result.A_ATTNUM = (result.KEYS).x ";
      sql = sql + " ORDER BY result.table_name, result.pk_name, result.key_seq";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   protected ResultSet getImportedExportedKeys(@Nullable String primaryCatalog, @Nullable String primarySchema, @Nullable String primaryTable, @Nullable String foreignCatalog, @Nullable String foreignSchema, @Nullable String foreignTable) throws SQLException {
      String sql = "SELECT NULL::text AS PKTABLE_CAT, pkn.nspname AS PKTABLE_SCHEM, pkc.relname AS PKTABLE_NAME, pka.attname AS PKCOLUMN_NAME, NULL::text AS FKTABLE_CAT, fkn.nspname AS FKTABLE_SCHEM, fkc.relname AS FKTABLE_NAME, fka.attname AS FKCOLUMN_NAME, pos.n AS KEY_SEQ, CASE con.confupdtype  WHEN 'c' THEN 0 WHEN 'n' THEN 2 WHEN 'd' THEN 4 WHEN 'r' THEN 1 WHEN 'p' THEN 1 WHEN 'a' THEN 3 ELSE NULL END AS UPDATE_RULE, CASE con.confdeltype  WHEN 'c' THEN 0 WHEN 'n' THEN 2 WHEN 'd' THEN 4 WHEN 'r' THEN 1 WHEN 'p' THEN 1 WHEN 'a' THEN 3 ELSE NULL END AS DELETE_RULE, con.conname AS FK_NAME, pkic.relname AS PK_NAME, CASE  WHEN con.condeferrable AND con.condeferred THEN 5 WHEN con.condeferrable THEN 6 ELSE 7 END AS DEFERRABILITY  FROM  pg_catalog.pg_namespace pkn, pg_catalog.pg_class pkc, pg_catalog.pg_attribute pka,  pg_catalog.pg_namespace fkn, pg_catalog.pg_class fkc, pg_catalog.pg_attribute fka,  pg_catalog.pg_constraint con,  pg_catalog.generate_series(1, " + this.getMaxIndexKeys() + ") pos(n),  pg_catalog.pg_class pkic";
      if (!this.connection.haveMinimumServerVersion(ServerVersion.v9_0)) {
         sql = sql + ", pg_catalog.pg_depend dep ";
      }

      sql = sql + " WHERE pkn.oid = pkc.relnamespace AND pkc.oid = pka.attrelid AND pka.attnum = con.confkey[pos.n] AND con.confrelid = pkc.oid  AND fkn.oid = fkc.relnamespace AND fkc.oid = fka.attrelid AND fka.attnum = con.conkey[pos.n] AND con.conrelid = fkc.oid  AND con.contype = 'f' ";
      if (!this.connection.haveMinimumServerVersion(ServerVersion.v11)) {
         sql = sql + "AND pkic.relkind = 'i' ";
      } else {
         sql = sql + "AND (pkic.relkind = 'i' OR pkic.relkind = 'I')";
      }

      if (!this.connection.haveMinimumServerVersion(ServerVersion.v9_0)) {
         sql = sql + " AND con.oid = dep.objid AND pkic.oid = dep.refobjid AND dep.classid = 'pg_constraint'::regclass::oid AND dep.refclassid = 'pg_class'::regclass::oid ";
      } else {
         sql = sql + " AND pkic.oid = con.conindid ";
      }

      if (primarySchema != null && !primarySchema.isEmpty()) {
         sql = sql + " AND pkn.nspname = " + this.escapeQuotes(primarySchema);
      }

      if (foreignSchema != null && !foreignSchema.isEmpty()) {
         sql = sql + " AND fkn.nspname = " + this.escapeQuotes(foreignSchema);
      }

      if (primaryTable != null && !primaryTable.isEmpty()) {
         sql = sql + " AND pkc.relname = " + this.escapeQuotes(primaryTable);
      }

      if (foreignTable != null && !foreignTable.isEmpty()) {
         sql = sql + " AND fkc.relname = " + this.escapeQuotes(foreignTable);
      }

      if (primaryTable != null) {
         sql = sql + " ORDER BY fkn.nspname,fkc.relname,con.conname,pos.n";
      } else {
         sql = sql + " ORDER BY pkn.nspname,pkc.relname, con.conname,pos.n";
      }

      return this.createMetaDataStatement().executeQuery(sql);
   }

   public ResultSet getImportedKeys(@Nullable String catalog, @Nullable String schema, String table) throws SQLException {
      return this.getImportedExportedKeys((String)null, (String)null, (String)null, catalog, schema, table);
   }

   public ResultSet getExportedKeys(@Nullable String catalog, @Nullable String schema, String table) throws SQLException {
      return this.getImportedExportedKeys(catalog, schema, table, (String)null, (String)null, (String)null);
   }

   public ResultSet getCrossReference(@Nullable String primaryCatalog, @Nullable String primarySchema, String primaryTable, @Nullable String foreignCatalog, @Nullable String foreignSchema, String foreignTable) throws SQLException {
      return this.getImportedExportedKeys(primaryCatalog, primarySchema, primaryTable, foreignCatalog, foreignSchema, foreignTable);
   }

   public ResultSet getTypeInfo() throws SQLException {
      Field[] f = new Field[18];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("TYPE_NAME", 1043);
      f[1] = new Field("DATA_TYPE", 21);
      f[2] = new Field("PRECISION", 23);
      f[3] = new Field("LITERAL_PREFIX", 1043);
      f[4] = new Field("LITERAL_SUFFIX", 1043);
      f[5] = new Field("CREATE_PARAMS", 1043);
      f[6] = new Field("NULLABLE", 21);
      f[7] = new Field("CASE_SENSITIVE", 16);
      f[8] = new Field("SEARCHABLE", 21);
      f[9] = new Field("UNSIGNED_ATTRIBUTE", 16);
      f[10] = new Field("FIXED_PREC_SCALE", 16);
      f[11] = new Field("AUTO_INCREMENT", 16);
      f[12] = new Field("LOCAL_TYPE_NAME", 1043);
      f[13] = new Field("MINIMUM_SCALE", 21);
      f[14] = new Field("MAXIMUM_SCALE", 21);
      f[15] = new Field("SQL_DATA_TYPE", 23);
      f[16] = new Field("SQL_DATETIME_SUB", 23);
      f[17] = new Field("NUM_PREC_RADIX", 23);
      String sql = "SELECT t.typname,t.oid FROM pg_catalog.pg_type t JOIN pg_catalog.pg_namespace n ON (t.typnamespace = n.oid)  WHERE n.nspname  != 'pg_toast' AND  (t.typrelid = 0 OR (SELECT c.relkind = 'c' FROM pg_catalog.pg_class c WHERE c.oid = t.typrelid))";
      if (this.connection.getHideUnprivilegedObjects() && this.connection.haveMinimumServerVersion(ServerVersion.v9_2)) {
         sql = sql + " AND has_type_privilege(t.oid, 'USAGE')";
      }

      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      byte[] bZero = this.connection.encodeString("0");
      byte[] b10 = this.connection.encodeString("10");
      byte[] bf = this.connection.encodeString("f");
      byte[] bt = this.connection.encodeString("t");
      byte[] bliteral = this.connection.encodeString("'");
      byte[] bNullable = this.connection.encodeString(Integer.toString(1));
      byte[] bSearchable = this.connection.encodeString(Integer.toString(3));
      TypeInfo ti = this.connection.getTypeInfo();
      if (ti instanceof TypeInfoCache) {
         ((TypeInfoCache)ti).cacheSQLTypes();
      }

      while(rs.next()) {
         byte[][] tuple = new byte[19][];
         String typname = (String)Nullness.castNonNull(rs.getString(1));
         int typeOid = (int)rs.getLong(2);
         tuple[0] = this.connection.encodeString(typname);
         int sqlType = this.connection.getTypeInfo().getSQLType(typname);
         tuple[1] = this.connection.encodeString(Integer.toString(sqlType));
         tuple[18] = BigInteger.valueOf((long)sqlType).toByteArray();
         tuple[2] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getMaximumPrecision(typeOid)));
         if (this.connection.getTypeInfo().requiresQuotingSqlType(sqlType)) {
            tuple[3] = bliteral;
            tuple[4] = bliteral;
         }

         tuple[6] = bNullable;
         tuple[7] = this.connection.getTypeInfo().isCaseSensitive(typeOid) ? bt : bf;
         tuple[8] = bSearchable;
         tuple[9] = this.connection.getTypeInfo().isSigned(typeOid) ? bf : bt;
         tuple[10] = bf;
         tuple[11] = bf;
         tuple[13] = bZero;
         tuple[14] = typeOid == 1700 ? this.connection.encodeString("1000") : bZero;
         tuple[17] = b10;
         v.add(new Tuple(tuple));
         byte[][] tuple1;
         if ("int4".equals(typname)) {
            tuple1 = (byte[][])tuple.clone();
            tuple1[0] = this.connection.encodeString("serial");
            tuple1[11] = bt;
            v.add(new Tuple(tuple1));
         } else if ("int8".equals(typname)) {
            tuple1 = (byte[][])tuple.clone();
            tuple1[0] = this.connection.encodeString("bigserial");
            tuple1[11] = bt;
            v.add(new Tuple(tuple1));
         } else if ("int2".equals(typname) && this.connection.haveMinimumServerVersion(ServerVersion.v9_2)) {
            tuple1 = (byte[][])tuple.clone();
            tuple1[0] = this.connection.encodeString("smallserial");
            tuple1[11] = bt;
            v.add(new Tuple(tuple1));
         }
      }

      rs.close();
      stmt.close();
      Collections.sort(v, new Comparator<Tuple>() {
         public int compare(Tuple o1, Tuple o2) {
            int i1 = ByteConverter.bytesToInt((byte[])Nullness.castNonNull(o1.get(18)));
            int i2 = ByteConverter.bytesToInt((byte[])Nullness.castNonNull(o2.get(18)));
            return i1 < i2 ? -1 : (i1 == i2 ? 0 : 1);
         }
      });
      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public ResultSet getIndexInfo(@Nullable String catalog, @Nullable String schema, String tableName, boolean unique, boolean approximate) throws SQLException {
      String sql;
      if (this.connection.haveMinimumServerVersion(ServerVersion.v8_3)) {
         sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, NOT i.indisunique AS NON_UNIQUE,   NULL AS INDEX_QUALIFIER, ci.relname AS INDEX_NAME,   CASE i.indisclustered     WHEN true THEN 1    ELSE CASE am.amname       WHEN 'hash' THEN 2      ELSE 3    END   END AS TYPE,   (information_schema._pg_expandarray(i.indkey)).n AS ORDINAL_POSITION,   ci.reltuples AS CARDINALITY,   ci.relpages AS PAGES,   pg_catalog.pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION,   ci.oid AS CI_OID,   i.indoption AS I_INDOPTION, " + (this.connection.haveMinimumServerVersion(ServerVersion.v9_6) ? "  am.amname AS AM_NAME " : "  am.amcanorder AS AM_CANORDER ") + "FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN pg_catalog.pg_index i ON (ct.oid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid)   JOIN pg_catalog.pg_am am ON (ci.relam = am.oid) WHERE true ";
         if (schema != null && !schema.isEmpty()) {
            sql = sql + " AND n.nspname = " + this.escapeQuotes(schema);
         }

         sql = sql + " AND ct.relname = " + this.escapeQuotes(tableName);
         if (unique) {
            sql = sql + " AND i.indisunique ";
         }

         sql = "SELECT     tmp.TABLE_CAT,     tmp.TABLE_SCHEM,     tmp.TABLE_NAME,     tmp.NON_UNIQUE,     tmp.INDEX_QUALIFIER,     tmp.INDEX_NAME,     tmp.TYPE,     tmp.ORDINAL_POSITION,     trim(both '\"' from pg_catalog.pg_get_indexdef(tmp.CI_OID, tmp.ORDINAL_POSITION, false)) AS COLUMN_NAME, " + (this.connection.haveMinimumServerVersion(ServerVersion.v9_6) ? "  CASE tmp.AM_NAME     WHEN 'btree' THEN CASE tmp.I_INDOPTION[tmp.ORDINAL_POSITION - 1] & 1::smallint       WHEN 1 THEN 'D'       ELSE 'A'     END     ELSE NULL   END AS ASC_OR_DESC, " : "  CASE tmp.AM_CANORDER     WHEN true THEN CASE tmp.I_INDOPTION[tmp.ORDINAL_POSITION - 1] & 1::smallint       WHEN 1 THEN 'D'       ELSE 'A'     END     ELSE NULL   END AS ASC_OR_DESC, ") + "    tmp.CARDINALITY,     tmp.PAGES,     tmp.FILTER_CONDITION FROM (" + sql + ") AS tmp";
      } else {
         String select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, ";
         String from = " FROM pg_catalog.pg_namespace n, pg_catalog.pg_class ct, pg_catalog.pg_class ci,  pg_catalog.pg_attribute a, pg_catalog.pg_am am ";
         String where = " AND n.oid = ct.relnamespace ";
         from = from + ", pg_catalog.pg_index i ";
         if (schema != null && !schema.isEmpty()) {
            where = where + " AND n.nspname = " + this.escapeQuotes(schema);
         }

         sql = select + " ct.relname AS TABLE_NAME, NOT i.indisunique AS NON_UNIQUE, NULL AS INDEX_QUALIFIER, ci.relname AS INDEX_NAME,  CASE i.indisclustered  WHEN true THEN " + 1 + " ELSE CASE am.amname  WHEN 'hash' THEN " + 2 + " ELSE " + 3 + " END  END AS TYPE,  a.attnum AS ORDINAL_POSITION,  CASE WHEN i.indexprs IS NULL THEN a.attname  ELSE pg_catalog.pg_get_indexdef(ci.oid,a.attnum,false) END AS COLUMN_NAME,  NULL AS ASC_OR_DESC,  ci.reltuples AS CARDINALITY,  ci.relpages AS PAGES,  pg_catalog.pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION " + from + " WHERE ct.oid=i.indrelid AND ci.oid=i.indexrelid AND a.attrelid=ci.oid AND ci.relam=am.oid " + where;
         sql = sql + " AND ct.relname = " + this.escapeQuotes(tableName);
         if (unique) {
            sql = sql + " AND i.indisunique ";
         }
      }

      sql = sql + " ORDER BY NON_UNIQUE, TYPE, INDEX_NAME, ORDINAL_POSITION ";
      return ((PgResultSet)this.createMetaDataStatement().executeQuery(sql)).upperCaseFieldLabels();
   }

   public boolean supportsResultSetType(int type) throws SQLException {
      return type != 1005;
   }

   public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
      if (type == 1005) {
         return false;
      } else {
         return concurrency == 1008 ? true : true;
      }
   }

   public boolean ownUpdatesAreVisible(int type) throws SQLException {
      return true;
   }

   public boolean ownDeletesAreVisible(int type) throws SQLException {
      return true;
   }

   public boolean ownInsertsAreVisible(int type) throws SQLException {
      return true;
   }

   public boolean othersUpdatesAreVisible(int type) throws SQLException {
      return false;
   }

   public boolean othersDeletesAreVisible(int i) throws SQLException {
      return false;
   }

   public boolean othersInsertsAreVisible(int type) throws SQLException {
      return false;
   }

   public boolean updatesAreDetected(int type) throws SQLException {
      return false;
   }

   public boolean deletesAreDetected(int i) throws SQLException {
      return false;
   }

   public boolean insertsAreDetected(int type) throws SQLException {
      return false;
   }

   public boolean supportsBatchUpdates() throws SQLException {
      return true;
   }

   public ResultSet getUDTs(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String typeNamePattern, @Nullable int[] types) throws SQLException {
      String sql = "select null as type_cat, n.nspname as type_schem, t.typname as type_name,  null as class_name, CASE WHEN t.typtype='c' then 2002 else 2001 end as data_type, pg_catalog.obj_description(t.oid, 'pg_type')  as remarks, CASE WHEN t.typtype = 'd' then  (select CASE";
      TypeInfo typeInfo = this.connection.getTypeInfo();
      StringBuilder sqlwhen = new StringBuilder();
      Iterator i = typeInfo.getPGTypeOidsWithSQLTypes();

      int type;
      while(i.hasNext()) {
         Integer typOid = (Integer)i.next();
         long longTypOid = typeInfo.intOidToLong(typOid);
         type = typeInfo.getSQLType(typOid);
         sqlwhen.append(" when base_type.oid = ").append(longTypOid).append(" then ").append(type);
      }

      sql = sql + sqlwhen.toString();
      sql = sql + " else 1111 end from pg_type base_type where base_type.oid=t.typbasetype) else null end as base_type from pg_catalog.pg_type t, pg_catalog.pg_namespace n where t.typnamespace = n.oid and n.nspname != 'pg_catalog' and n.nspname != 'pg_toast'";
      StringBuilder toAdd = new StringBuilder();
      int secondQualifier;
      if (types != null) {
         toAdd.append(" and (false ");
         int[] var14 = types;
         secondQualifier = types.length;

         for(int var11 = 0; var11 < secondQualifier; ++var11) {
            type = var14[var11];
            if (type == 2002) {
               toAdd.append(" or t.typtype = 'c'");
            } else if (type == 2001) {
               toAdd.append(" or t.typtype = 'd'");
            }
         }

         toAdd.append(" ) ");
      } else {
         toAdd.append(" and t.typtype IN ('c','d') ");
      }

      if (typeNamePattern != null) {
         int firstQualifier = typeNamePattern.indexOf(46);
         secondQualifier = typeNamePattern.lastIndexOf(46);
         if (firstQualifier != -1) {
            if (firstQualifier != secondQualifier) {
               schemaPattern = typeNamePattern.substring(firstQualifier + 1, secondQualifier);
            } else {
               schemaPattern = typeNamePattern.substring(0, firstQualifier);
            }

            typeNamePattern = typeNamePattern.substring(secondQualifier + 1);
         }

         toAdd.append(" and t.typname like ").append(this.escapeQuotes(typeNamePattern));
      }

      if (schemaPattern != null) {
         toAdd.append(" and n.nspname like ").append(this.escapeQuotes(schemaPattern));
      }

      sql = sql + toAdd.toString();
      if (this.connection.getHideUnprivilegedObjects() && this.connection.haveMinimumServerVersion(ServerVersion.v9_2)) {
         sql = sql + " AND has_type_privilege(t.oid, 'USAGE')";
      }

      sql = sql + " order by data_type, type_schem, type_name";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   public Connection getConnection() throws SQLException {
      return this.connection;
   }

   protected Statement createMetaDataStatement() throws SQLException {
      return this.connection.createStatement(1004, 1007);
   }

   public long getMaxLogicalLobSize() throws SQLException {
      return 0L;
   }

   public boolean supportsRefCursors() throws SQLException {
      return true;
   }

   public RowIdLifetime getRowIdLifetime() throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getRowIdLifetime()");
   }

   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
      return true;
   }

   public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
      return false;
   }

   public ResultSet getClientInfoProperties() throws SQLException {
      Field[] f = new Field[]{new Field("NAME", 1043), new Field("MAX_LEN", 23), new Field("DEFAULT_VALUE", 1043), new Field("DESCRIPTION", 1043)};
      List<Tuple> v = new ArrayList();
      if (this.connection.haveMinimumServerVersion(ServerVersion.v9_0)) {
         byte[][] tuple = new byte[][]{this.connection.encodeString("ApplicationName"), this.connection.encodeString(Integer.toString(this.getMaxNameLength())), this.connection.encodeString(""), this.connection.encodeString("The name of the application currently utilizing the connection.")};
         v.add(new Tuple(tuple));
      }

      return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return iface.isAssignableFrom(this.getClass());
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (iface.isAssignableFrom(this.getClass())) {
         return iface.cast(this);
      } else {
         throw new SQLException("Cannot unwrap to " + iface.getName());
      }
   }

   public ResultSet getFunctions(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String functionNamePattern) throws SQLException {
      boolean pgFuncResultExists = this.connection.haveMinimumServerVersion(ServerVersion.v8_4);
      String funcTypeSql = "0 ";
      if (pgFuncResultExists) {
         funcTypeSql = " CASE    WHEN (format_type(p.prorettype, null) = 'unknown') THEN 0   WHEN      (substring(pg_get_function_result(p.oid) from 0 for 6) = 'TABLE') OR      (substring(pg_get_function_result(p.oid) from 0 for 6) = 'SETOF') THEN 2   ELSE 1 END ";
      }

      String sql = "SELECT current_database() AS FUNCTION_CAT, n.nspname AS FUNCTION_SCHEM, p.proname AS FUNCTION_NAME,  d.description AS REMARKS, " + funcTypeSql + " AS FUNCTION_TYPE,  p.proname || '_' || p.oid AS SPECIFIC_NAME FROM pg_catalog.pg_proc p INNER JOIN pg_catalog.pg_namespace n ON p.pronamespace=n.oid LEFT JOIN pg_catalog.pg_description d ON p.oid=d.objoid WHERE true  ";
      if (this.connection.haveMinimumServerVersion(ServerVersion.v11)) {
         sql = sql + " AND p.prokind='f'";
      }

      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (functionNamePattern != null && !functionNamePattern.isEmpty()) {
         sql = sql + " AND p.proname LIKE " + this.escapeQuotes(functionNamePattern);
      }

      if (this.connection.getHideUnprivilegedObjects()) {
         sql = sql + " AND has_function_privilege(p.oid,'EXECUTE')";
      }

      sql = sql + " ORDER BY FUNCTION_SCHEM, FUNCTION_NAME, p.oid::text ";
      return this.createMetaDataStatement().executeQuery(sql);
   }

   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
      int columns = 17;
      Field[] f = new Field[columns];
      List<Tuple> v = new ArrayList();
      f[0] = new Field("FUNCTION_CAT", 1043);
      f[1] = new Field("FUNCTION_SCHEM", 1043);
      f[2] = new Field("FUNCTION_NAME", 1043);
      f[3] = new Field("COLUMN_NAME", 1043);
      f[4] = new Field("COLUMN_TYPE", 21);
      f[5] = new Field("DATA_TYPE", 21);
      f[6] = new Field("TYPE_NAME", 1043);
      f[7] = new Field("PRECISION", 21);
      f[8] = new Field("LENGTH", 23);
      f[9] = new Field("SCALE", 21);
      f[10] = new Field("RADIX", 21);
      f[11] = new Field("NULLABLE", 21);
      f[12] = new Field("REMARKS", 1043);
      f[13] = new Field("CHAR_OCTET_LENGTH", 23);
      f[14] = new Field("ORDINAL_POSITION", 23);
      f[15] = new Field("IS_NULLABLE", 1043);
      f[16] = new Field("SPECIFIC_NAME", 1043);
      String sql = "SELECT n.nspname,p.proname,p.prorettype,p.proargtypes, t.typtype,t.typrelid,  p.proargnames, p.proargmodes, p.proallargtypes, p.oid  FROM pg_catalog.pg_proc p, pg_catalog.pg_namespace n, pg_catalog.pg_type t  WHERE p.pronamespace=n.oid AND p.prorettype=t.oid ";
      if (schemaPattern != null && !schemaPattern.isEmpty()) {
         sql = sql + " AND n.nspname LIKE " + this.escapeQuotes(schemaPattern);
      }

      if (functionNamePattern != null && !functionNamePattern.isEmpty()) {
         sql = sql + " AND p.proname LIKE " + this.escapeQuotes(functionNamePattern);
      }

      sql = sql + " ORDER BY n.nspname, p.proname, p.oid::text ";
      byte[] isnullableUnknown = new byte[0];
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while(true) {
         byte[] schema;
         byte[] functionName;
         byte[] specificName;
         String returnTypeType;
         int returnTypeRelid;
         Array argModesArray;
         int argOid;
         do {
            if (!rs.next()) {
               rs.close();
               stmt.close();
               return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
            }

            schema = rs.getBytes("nspname");
            functionName = rs.getBytes("proname");
            specificName = this.connection.encodeString(rs.getString("proname") + "_" + rs.getString("oid"));
            int returnType = (int)rs.getLong("prorettype");
            returnTypeType = rs.getString("typtype");
            returnTypeRelid = (int)rs.getLong("typrelid");
            String strArgTypes = (String)Nullness.castNonNull(rs.getString("proargtypes"));
            StringTokenizer st = new StringTokenizer(strArgTypes);
            ArrayList argTypes = new ArrayList();

            while(st.hasMoreTokens()) {
               argTypes.add(Long.valueOf(st.nextToken()));
            }

            String[] argNames = null;
            Array argNamesArray = rs.getArray("proargnames");
            if (argNamesArray != null) {
               argNames = (String[])argNamesArray.getArray();
            }

            String[] argModes = null;
            argModesArray = rs.getArray("proargmodes");
            if (argModesArray != null) {
               argModes = (String[])argModesArray.getArray();
            }

            int numArgs = argTypes.size();
            Long[] allArgTypes = null;
            Array allArgTypesArray = rs.getArray("proallargtypes");
            if (allArgTypesArray != null) {
               allArgTypes = (Long[])allArgTypesArray.getArray();
               numArgs = allArgTypes.length;
            }

            if ("b".equals(returnTypeType) || "d".equals(returnTypeType) || "e".equals(returnTypeType) || "p".equals(returnTypeType) && argModesArray == null) {
               byte[][] tuple = new byte[columns][];
               tuple[0] = null;
               tuple[1] = schema;
               tuple[2] = functionName;
               tuple[3] = this.connection.encodeString("returnValue");
               tuple[4] = this.connection.encodeString(Integer.toString(4));
               tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(returnType)));
               tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(returnType));
               tuple[7] = null;
               tuple[8] = null;
               tuple[9] = null;
               tuple[10] = null;
               tuple[11] = this.connection.encodeString(Integer.toString(2));
               tuple[12] = null;
               tuple[14] = this.connection.encodeString(Integer.toString(0));
               tuple[15] = isnullableUnknown;
               tuple[16] = specificName;
               v.add(new Tuple(tuple));
            }

            for(int i = 0; i < numArgs; ++i) {
               byte[][] tuple = new byte[columns][];
               tuple[0] = null;
               tuple[1] = schema;
               tuple[2] = functionName;
               if (argNames != null) {
                  tuple[3] = this.connection.encodeString(argNames[i]);
               } else {
                  tuple[3] = this.connection.encodeString("$" + (i + 1));
               }

               int columnMode = 1;
               if (argModes != null && argModes[i] != null) {
                  if ("o".equals(argModes[i])) {
                     columnMode = 3;
                  } else if ("b".equals(argModes[i])) {
                     columnMode = 2;
                  } else if ("t".equals(argModes[i])) {
                     columnMode = 4;
                  }
               }

               tuple[4] = this.connection.encodeString(Integer.toString(columnMode));
               if (allArgTypes != null) {
                  argOid = allArgTypes[i].intValue();
               } else {
                  argOid = ((Long)argTypes.get(i)).intValue();
               }

               tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
               tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
               tuple[7] = null;
               tuple[8] = null;
               tuple[9] = null;
               tuple[10] = null;
               tuple[11] = this.connection.encodeString(Integer.toString(2));
               tuple[12] = null;
               tuple[14] = this.connection.encodeString(Integer.toString(i + 1));
               tuple[15] = isnullableUnknown;
               tuple[16] = specificName;
               v.add(new Tuple(tuple));
            }
         } while(!"c".equals(returnTypeType) && (!"p".equals(returnTypeType) || argModesArray == null));

         String columnsql = "SELECT a.attname,a.atttypid FROM pg_catalog.pg_attribute a  WHERE a.attrelid = " + returnTypeRelid + " AND NOT a.attisdropped AND a.attnum > 0 ORDER BY a.attnum ";
         Statement columnstmt = this.connection.createStatement();
         ResultSet columnrs = columnstmt.executeQuery(columnsql);

         while(columnrs.next()) {
            argOid = (int)columnrs.getLong("atttypid");
            byte[][] tuple = new byte[columns][];
            tuple[0] = null;
            tuple[1] = schema;
            tuple[2] = functionName;
            tuple[3] = columnrs.getBytes("attname");
            tuple[4] = this.connection.encodeString(Integer.toString(5));
            tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
            tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
            tuple[7] = null;
            tuple[8] = null;
            tuple[9] = null;
            tuple[10] = null;
            tuple[11] = this.connection.encodeString(Integer.toString(2));
            tuple[12] = null;
            tuple[14] = this.connection.encodeString(Integer.toString(0));
            tuple[15] = isnullableUnknown;
            tuple[16] = specificName;
            v.add(new Tuple(tuple));
         }

         columnrs.close();
         columnstmt.close();
      }
   }

   public ResultSet getPseudoColumns(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String tableNamePattern, @Nullable String columnNamePattern) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getPseudoColumns(String, String, String, String)");
   }

   public boolean generatedKeyAlwaysReturned() throws SQLException {
      return true;
   }

   public boolean supportsSavepoints() throws SQLException {
      return true;
   }

   public boolean supportsNamedParameters() throws SQLException {
      return false;
   }

   public boolean supportsMultipleOpenResults() throws SQLException {
      return false;
   }

   public boolean supportsGetGeneratedKeys() throws SQLException {
      return true;
   }

   public ResultSet getSuperTypes(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String typeNamePattern) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getSuperTypes(String,String,String)");
   }

   public ResultSet getSuperTables(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String tableNamePattern) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getSuperTables(String,String,String,String)");
   }

   public ResultSet getAttributes(@Nullable String catalog, @Nullable String schemaPattern, @Nullable String typeNamePattern, @Nullable String attributeNamePattern) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getAttributes(String,String,String,String)");
   }

   public boolean supportsResultSetHoldability(int holdability) throws SQLException {
      return true;
   }

   public int getResultSetHoldability() throws SQLException {
      return 1;
   }

   public int getDatabaseMajorVersion() throws SQLException {
      return this.connection.getServerMajorVersion();
   }

   public int getDatabaseMinorVersion() throws SQLException {
      return this.connection.getServerMinorVersion();
   }

   public int getJDBCMajorVersion() {
      return DriverInfo.JDBC_MAJOR_VERSION;
   }

   public int getJDBCMinorVersion() {
      return DriverInfo.JDBC_MINOR_VERSION;
   }

   public int getSQLStateType() throws SQLException {
      return 2;
   }

   public boolean locatorsUpdateCopy() throws SQLException {
      return true;
   }

   public boolean supportsStatementPooling() throws SQLException {
      return false;
   }

   static {
      Map<String, String> ht = new HashMap();
      tableTypeClauses.put("TABLE", ht);
      ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
      ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname !~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("PARTITIONED TABLE", ht);
      ht.put("SCHEMAS", "c.relkind = 'p' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
      ht.put("NOSCHEMAS", "c.relkind = 'p' AND c.relname !~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("VIEW", ht);
      ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname <> 'pg_catalog' AND n.nspname <> 'information_schema'");
      ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname !~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("INDEX", ht);
      ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
      ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname !~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("PARTITIONED INDEX", ht);
      ht.put("SCHEMAS", "c.relkind = 'I' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
      ht.put("NOSCHEMAS", "c.relkind = 'I' AND c.relname !~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("SEQUENCE", ht);
      ht.put("SCHEMAS", "c.relkind = 'S'");
      ht.put("NOSCHEMAS", "c.relkind = 'S'");
      ht = new HashMap();
      tableTypeClauses.put("TYPE", ht);
      ht.put("SCHEMAS", "c.relkind = 'c' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
      ht.put("NOSCHEMAS", "c.relkind = 'c' AND c.relname !~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("SYSTEM TABLE", ht);
      ht.put("SCHEMAS", "c.relkind = 'r' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema')");
      ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_' AND c.relname !~ '^pg_toast_' AND c.relname !~ '^pg_temp_'");
      ht = new HashMap();
      tableTypeClauses.put("SYSTEM TOAST TABLE", ht);
      ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname = 'pg_toast'");
      ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_toast_'");
      ht = new HashMap();
      tableTypeClauses.put("SYSTEM TOAST INDEX", ht);
      ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname = 'pg_toast'");
      ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname ~ '^pg_toast_'");
      ht = new HashMap();
      tableTypeClauses.put("SYSTEM VIEW", ht);
      ht.put("SCHEMAS", "c.relkind = 'v' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema') ");
      ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_'");
      ht = new HashMap();
      tableTypeClauses.put("SYSTEM INDEX", ht);
      ht.put("SCHEMAS", "c.relkind = 'i' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema') ");
      ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_' AND c.relname !~ '^pg_toast_' AND c.relname !~ '^pg_temp_'");
      ht = new HashMap();
      tableTypeClauses.put("TEMPORARY TABLE", ht);
      ht.put("SCHEMAS", "c.relkind IN ('r','p') AND n.nspname ~ '^pg_temp_' ");
      ht.put("NOSCHEMAS", "c.relkind IN ('r','p') AND c.relname ~ '^pg_temp_' ");
      ht = new HashMap();
      tableTypeClauses.put("TEMPORARY INDEX", ht);
      ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname ~ '^pg_temp_' ");
      ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname ~ '^pg_temp_' ");
      ht = new HashMap();
      tableTypeClauses.put("TEMPORARY VIEW", ht);
      ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname ~ '^pg_temp_' ");
      ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_temp_' ");
      ht = new HashMap();
      tableTypeClauses.put("TEMPORARY SEQUENCE", ht);
      ht.put("SCHEMAS", "c.relkind = 'S' AND n.nspname ~ '^pg_temp_' ");
      ht.put("NOSCHEMAS", "c.relkind = 'S' AND c.relname ~ '^pg_temp_' ");
      ht = new HashMap();
      tableTypeClauses.put("FOREIGN TABLE", ht);
      ht.put("SCHEMAS", "c.relkind = 'f'");
      ht.put("NOSCHEMAS", "c.relkind = 'f'");
      ht = new HashMap();
      tableTypeClauses.put("MATERIALIZED VIEW", ht);
      ht.put("SCHEMAS", "c.relkind = 'm'");
      ht.put("NOSCHEMAS", "c.relkind = 'm'");
   }
}
