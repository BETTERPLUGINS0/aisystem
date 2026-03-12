package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.xml.DefaultPGXmlFactoryFactory;
import fr.xephi.authme.libs.org.postgresql.xml.PGXmlFactoryFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class PgSQLXML implements SQLXML {
   private final ResourceLock lock;
   private final BaseConnection conn;
   @Nullable
   private String data;
   private boolean initialized;
   private boolean active;
   private boolean freed;
   @Nullable
   private ByteArrayOutputStream byteArrayOutputStream;
   @Nullable
   private StringWriter stringWriter;
   @Nullable
   private DOMResult domResult;

   public PgSQLXML(BaseConnection conn) {
      this(conn, (String)null, false);
   }

   public PgSQLXML(BaseConnection conn, @Nullable String data) {
      this(conn, data, true);
   }

   private PgSQLXML(BaseConnection conn, @Nullable String data, boolean initialized) {
      this.lock = new ResourceLock();
      this.conn = conn;
      this.data = data;
      this.initialized = initialized;
      this.active = false;
      this.freed = false;
   }

   private PGXmlFactoryFactory getXmlFactoryFactory() throws SQLException {
      return (PGXmlFactoryFactory)(this.conn != null ? this.conn.getXmlFactoryFactory() : DefaultPGXmlFactoryFactory.INSTANCE);
   }

   public void free() {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.freed = true;
         this.data = null;
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   @Nullable
   public InputStream getBinaryStream() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ByteArrayInputStream var2;
      label46: {
         try {
            this.checkFreed();
            this.ensureInitialized();
            if (this.data == null) {
               var2 = null;
               break label46;
            }

            try {
               var2 = new ByteArrayInputStream(this.conn.getEncoding().encode(this.data));
            } catch (IOException var5) {
               throw new PSQLException("Failed to re-encode xml data.", PSQLState.DATA_ERROR, var5);
            }
         } catch (Throwable var6) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var6.addSuppressed(var4);
               }
            }

            throw var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   @Nullable
   public Reader getCharacterStream() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      StringReader var2;
      label43: {
         try {
            this.checkFreed();
            this.ensureInitialized();
            if (this.data == null) {
               var2 = null;
               break label43;
            }

            var2 = new StringReader(this.data);
         } catch (Throwable var5) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   @Nullable
   public <T extends Source> T getSource(@Nullable Class<T> sourceClass) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      XMLInputFactory xif;
      label97: {
         Source var6;
         label98: {
            Source var11;
            label99: {
               label100: {
                  DOMSource var7;
                  try {
                     label101: {
                        this.checkFreed();
                        this.ensureInitialized();
                        String data = this.data;
                        if (data == null) {
                           xif = null;
                           break label97;
                        }

                        try {
                           InputSource is;
                           if (sourceClass == null || DOMSource.class.equals(sourceClass)) {
                              DocumentBuilder builder = this.getXmlFactoryFactory().newDocumentBuilder();
                              is = new InputSource(new StringReader(data));
                              DOMSource domSource = new DOMSource(builder.parse(is));
                              var7 = domSource;
                              break label101;
                           }

                           if (SAXSource.class.equals(sourceClass)) {
                              XMLReader reader = this.getXmlFactoryFactory().createXMLReader();
                              is = new InputSource(new StringReader(data));
                              var6 = (Source)sourceClass.cast(new SAXSource(reader, is));
                              break label98;
                           }

                           if (StreamSource.class.equals(sourceClass)) {
                              var11 = (Source)sourceClass.cast(new StreamSource(new StringReader(data)));
                              break label99;
                           }

                           if (StAXSource.class.equals(sourceClass)) {
                              xif = this.getXmlFactoryFactory().newXMLInputFactory();
                              XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(data));
                              var6 = (Source)sourceClass.cast(new StAXSource(xsr));
                              break label100;
                           }
                        } catch (Exception var9) {
                           throw new PSQLException(GT.tr("Unable to decode xml data."), PSQLState.DATA_ERROR, var9);
                        }

                        throw new PSQLException(GT.tr("Unknown XML Source class: {0}", sourceClass), PSQLState.INVALID_PARAMETER_TYPE);
                     }
                  } catch (Throwable var10) {
                     if (ignore != null) {
                        try {
                           ignore.close();
                        } catch (Throwable var8) {
                           var10.addSuppressed(var8);
                        }
                     }

                     throw var10;
                  }

                  if (ignore != null) {
                     ignore.close();
                  }

                  return var7;
               }

               if (ignore != null) {
                  ignore.close();
               }

               return var6;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var11;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return xif;
   }

   @Nullable
   public String getString() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      String var2;
      try {
         this.checkFreed();
         this.ensureInitialized();
         var2 = this.data;
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public OutputStream setBinaryStream() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ByteArrayOutputStream var2;
      try {
         this.checkFreed();
         this.initialize();
         this.active = true;
         this.byteArrayOutputStream = new ByteArrayOutputStream();
         var2 = this.byteArrayOutputStream;
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public Writer setCharacterStream() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      StringWriter var2;
      try {
         this.checkFreed();
         this.initialize();
         this.active = true;
         this.stringWriter = new StringWriter();
         var2 = this.stringWriter;
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public <T extends Result> T setResult(Class<T> resultClass) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      Result var12;
      label83: {
         Result var6;
         label84: {
            DOMResult var13;
            label85: {
               Result var5;
               try {
                  this.checkFreed();
                  this.initialize();
                  if (resultClass == null || DOMResult.class.equals(resultClass)) {
                     this.domResult = new DOMResult();
                     this.active = true;
                     var13 = this.domResult;
                     break label85;
                  }

                  if (!SAXResult.class.equals(resultClass)) {
                     if (StreamResult.class.equals(resultClass)) {
                        this.stringWriter = new StringWriter();
                        this.active = true;
                        var12 = (Result)resultClass.cast(new StreamResult(this.stringWriter));
                        break label83;
                     }

                     if (StAXResult.class.equals(resultClass)) {
                        StringWriter stringWriter = new StringWriter();
                        this.stringWriter = stringWriter;

                        try {
                           XMLOutputFactory xof = this.getXmlFactoryFactory().newXMLOutputFactory();
                           XMLStreamWriter xsw = xof.createXMLStreamWriter(stringWriter);
                           this.active = true;
                           var6 = (Result)resultClass.cast(new StAXResult(xsw));
                           break label84;
                        } catch (XMLStreamException var9) {
                           throw new PSQLException(GT.tr("Unable to create StAXResult for SQLXML"), PSQLState.UNEXPECTED_ERROR, var9);
                        }
                     }

                     throw new PSQLException(GT.tr("Unknown XML Result class: {0}", resultClass), PSQLState.INVALID_PARAMETER_TYPE);
                  }

                  try {
                     SAXTransformerFactory transformerFactory = this.getXmlFactoryFactory().newSAXTransformerFactory();
                     TransformerHandler transformerHandler = transformerFactory.newTransformerHandler();
                     this.stringWriter = new StringWriter();
                     transformerHandler.setResult(new StreamResult(this.stringWriter));
                     this.active = true;
                     var5 = (Result)resultClass.cast(new SAXResult(transformerHandler));
                  } catch (TransformerException var8) {
                     throw new PSQLException(GT.tr("Unable to create SAXResult for SQLXML."), PSQLState.UNEXPECTED_ERROR, var8);
                  }
               } catch (Throwable var10) {
                  if (ignore != null) {
                     try {
                        ignore.close();
                     } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                     }
                  }

                  throw var10;
               }

               if (ignore != null) {
                  ignore.close();
               }

               return var5;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var13;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var12;
   }

   public void setString(String value) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         this.initialize();
         this.data = value;
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

   }

   private void checkFreed() throws SQLException {
      if (this.freed) {
         throw new PSQLException(GT.tr("This SQLXML object has already been freed."), PSQLState.OBJECT_NOT_IN_STATE);
      }
   }

   private void ensureInitialized() throws SQLException {
      if (!this.initialized) {
         throw new PSQLException(GT.tr("This SQLXML object has not been initialized, so you cannot retrieve data from it."), PSQLState.OBJECT_NOT_IN_STATE);
      } else if (this.active) {
         if (this.byteArrayOutputStream != null) {
            try {
               this.data = this.conn.getEncoding().decode(this.byteArrayOutputStream.toByteArray());
            } catch (IOException var18) {
               throw new PSQLException(GT.tr("Failed to convert binary xml data to encoding: {0}.", this.conn.getEncoding().name()), PSQLState.DATA_ERROR, var18);
            } finally {
               this.byteArrayOutputStream = null;
               this.active = false;
            }
         } else if (this.stringWriter != null) {
            this.data = this.stringWriter.toString();
            this.stringWriter = null;
            this.active = false;
         } else if (this.domResult != null) {
            DOMResult domResult = this.domResult;

            try {
               TransformerFactory factory = this.getXmlFactoryFactory().newTransformerFactory();
               Transformer transformer = factory.newTransformer();
               DOMSource domSource = new DOMSource(domResult.getNode());
               StringWriter stringWriter = new StringWriter();
               StreamResult streamResult = new StreamResult(stringWriter);
               transformer.transform(domSource, streamResult);
               this.data = stringWriter.toString();
            } catch (TransformerException var16) {
               throw new PSQLException(GT.tr("Unable to convert DOMResult SQLXML data to a string."), PSQLState.DATA_ERROR, var16);
            } finally {
               domResult = null;
               this.active = false;
            }
         }

      }
   }

   private void initialize() throws SQLException {
      if (this.initialized) {
         throw new PSQLException(GT.tr("This SQLXML object has already been initialized, so you cannot manipulate it further."), PSQLState.OBJECT_NOT_IN_STATE);
      } else {
         this.initialized = true;
      }
   }
}
