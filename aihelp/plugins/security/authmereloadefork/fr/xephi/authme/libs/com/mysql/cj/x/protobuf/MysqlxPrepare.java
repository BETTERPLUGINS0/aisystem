package fr.xephi.authme.libs.com.mysql.cj.x.protobuf;

import fr.xephi.authme.libs.com.google.protobuf.AbstractMessageLite;
import fr.xephi.authme.libs.com.google.protobuf.AbstractParser;
import fr.xephi.authme.libs.com.google.protobuf.ByteString;
import fr.xephi.authme.libs.com.google.protobuf.CodedInputStream;
import fr.xephi.authme.libs.com.google.protobuf.CodedOutputStream;
import fr.xephi.authme.libs.com.google.protobuf.Descriptors;
import fr.xephi.authme.libs.com.google.protobuf.ExtensionRegistry;
import fr.xephi.authme.libs.com.google.protobuf.ExtensionRegistryLite;
import fr.xephi.authme.libs.com.google.protobuf.GeneratedMessageV3;
import fr.xephi.authme.libs.com.google.protobuf.Internal;
import fr.xephi.authme.libs.com.google.protobuf.InvalidProtocolBufferException;
import fr.xephi.authme.libs.com.google.protobuf.Message;
import fr.xephi.authme.libs.com.google.protobuf.MessageLite;
import fr.xephi.authme.libs.com.google.protobuf.MessageOrBuilder;
import fr.xephi.authme.libs.com.google.protobuf.Parser;
import fr.xephi.authme.libs.com.google.protobuf.ProtocolMessageEnum;
import fr.xephi.authme.libs.com.google.protobuf.RepeatedFieldBuilderV3;
import fr.xephi.authme.libs.com.google.protobuf.SingleFieldBuilderV3;
import fr.xephi.authme.libs.com.google.protobuf.UninitializedMessageException;
import fr.xephi.authme.libs.com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MysqlxPrepare {
   private static final Descriptors.Descriptor internal_static_Mysqlx_Prepare_Prepare_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Prepare_Prepare_fieldAccessorTable;
   private static final Descriptors.Descriptor internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_fieldAccessorTable;
   private static final Descriptors.Descriptor internal_static_Mysqlx_Prepare_Execute_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Prepare_Execute_fieldAccessorTable;
   private static final Descriptors.Descriptor internal_static_Mysqlx_Prepare_Deallocate_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Prepare_Deallocate_fieldAccessorTable;
   private static Descriptors.FileDescriptor descriptor;

   private MysqlxPrepare() {
   }

   public static void registerAllExtensions(ExtensionRegistryLite registry) {
   }

   public static void registerAllExtensions(ExtensionRegistry registry) {
      registerAllExtensions((ExtensionRegistryLite)registry);
   }

   public static Descriptors.FileDescriptor getDescriptor() {
      return descriptor;
   }

   static {
      String[] descriptorData = new String[]{"\n\u0014mysqlx_prepare.proto\u0012\u000eMysqlx.Prepare\u001a\fmysqlx.proto\u001a\u0010mysqlx_sql.proto\u001a\u0011mysqlx_crud.proto\u001a\u0016mysqlx_datatypes.proto\"\u009d\u0003\n\u0007Prepare\u0012\u000f\n\u0007stmt_id\u0018\u0001 \u0002(\r\u00122\n\u0004stmt\u0018\u0002 \u0002(\u000b2$.Mysqlx.Prepare.Prepare.OneOfMessage\u001aÆ\u0002\n\fOneOfMessage\u00127\n\u0004type\u0018\u0001 \u0002(\u000e2).Mysqlx.Prepare.Prepare.OneOfMessage.Type\u0012\u001f\n\u0004find\u0018\u0002 \u0001(\u000b2\u0011.Mysqlx.Crud.Find\u0012#\n\u0006insert\u0018\u0003 \u0001(\u000b2\u0013.Mysqlx.Crud.Insert\u0012#\n\u0006update\u0018\u0004 \u0001(\u000b2\u0013.Mysqlx.Crud.Update\u0012#\n\u0006delete\u0018\u0005 \u0001(\u000b2\u0013.Mysqlx.Crud.Delete\u0012-\n\fstmt_execute\u0018\u0006 \u0001(\u000b2\u0017.Mysqlx.Sql.StmtExecute\">\n\u0004Type\u0012\b\n\u0004FIND\u0010\u0000\u0012\n\n\u0006INSERT\u0010\u0001\u0012\n\n\u0006UPDATE\u0010\u0002\u0012\n\n\u0006DELETE\u0010\u0004\u0012\b\n\u0004STMT\u0010\u0005:\u0004\u0088ê0(\"f\n\u0007Execute\u0012\u000f\n\u0007stmt_id\u0018\u0001 \u0002(\r\u0012#\n\u0004args\u0018\u0002 \u0003(\u000b2\u0015.Mysqlx.Datatypes.Any\u0012\u001f\n\u0010compact_metadata\u0018\u0003 \u0001(\b:\u0005false:\u0004\u0088ê0)\"#\n\nDeallocate\u0012\u000f\n\u0007stmt_id\u0018\u0001 \u0002(\r:\u0004\u0088ê0*B\u0019\n\u0017com.mysql.cj.x.protobuf"};
      descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[]{Mysqlx.getDescriptor(), MysqlxSql.getDescriptor(), MysqlxCrud.getDescriptor(), MysqlxDatatypes.getDescriptor()});
      internal_static_Mysqlx_Prepare_Prepare_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(0);
      internal_static_Mysqlx_Prepare_Prepare_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Prepare_Prepare_descriptor, new String[]{"StmtId", "Stmt"});
      internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_descriptor = (Descriptors.Descriptor)internal_static_Mysqlx_Prepare_Prepare_descriptor.getNestedTypes().get(0);
      internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_descriptor, new String[]{"Type", "Find", "Insert", "Update", "Delete", "StmtExecute"});
      internal_static_Mysqlx_Prepare_Execute_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(1);
      internal_static_Mysqlx_Prepare_Execute_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Prepare_Execute_descriptor, new String[]{"StmtId", "Args", "CompactMetadata"});
      internal_static_Mysqlx_Prepare_Deallocate_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(2);
      internal_static_Mysqlx_Prepare_Deallocate_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Prepare_Deallocate_descriptor, new String[]{"StmtId"});
      ExtensionRegistry registry = ExtensionRegistry.newInstance();
      registry.add(Mysqlx.clientMessageId);
      Descriptors.FileDescriptor.internalUpdateFileDescriptor(descriptor, registry);
      Mysqlx.getDescriptor();
      MysqlxSql.getDescriptor();
      MysqlxCrud.getDescriptor();
      MysqlxDatatypes.getDescriptor();
   }

   public static final class Deallocate extends GeneratedMessageV3 implements MysqlxPrepare.DeallocateOrBuilder {
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      public static final int STMT_ID_FIELD_NUMBER = 1;
      private int stmtId_;
      private byte memoizedIsInitialized;
      private static final MysqlxPrepare.Deallocate DEFAULT_INSTANCE = new MysqlxPrepare.Deallocate();
      /** @deprecated */
      @Deprecated
      public static final Parser<MysqlxPrepare.Deallocate> PARSER = new AbstractParser<MysqlxPrepare.Deallocate>() {
         public MysqlxPrepare.Deallocate parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            MysqlxPrepare.Deallocate.Builder builder = MysqlxPrepare.Deallocate.newBuilder();

            try {
               builder.mergeFrom(input, extensionRegistry);
            } catch (InvalidProtocolBufferException var5) {
               throw var5.setUnfinishedMessage(builder.buildPartial());
            } catch (UninitializedMessageException var6) {
               throw var6.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
            } catch (IOException var7) {
               throw (new InvalidProtocolBufferException(var7)).setUnfinishedMessage(builder.buildPartial());
            }

            return builder.buildPartial();
         }
      };

      private Deallocate(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private Deallocate() {
         this.memoizedIsInitialized = -1;
      }

      protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
         return new MysqlxPrepare.Deallocate();
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return MysqlxPrepare.internal_static_Mysqlx_Prepare_Deallocate_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return MysqlxPrepare.internal_static_Mysqlx_Prepare_Deallocate_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Deallocate.class, MysqlxPrepare.Deallocate.Builder.class);
      }

      public boolean hasStmtId() {
         return (this.bitField0_ & 1) != 0;
      }

      public int getStmtId() {
         return this.stmtId_;
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else if (!this.hasStmtId()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         if ((this.bitField0_ & 1) != 0) {
            output.writeUInt32(1, this.stmtId_);
         }

         this.getUnknownFields().writeTo(output);
      }

      public int getSerializedSize() {
         int size = this.memoizedSize;
         if (size != -1) {
            return size;
         } else {
            size = 0;
            if ((this.bitField0_ & 1) != 0) {
               size += CodedOutputStream.computeUInt32Size(1, this.stmtId_);
            }

            size += this.getUnknownFields().getSerializedSize();
            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof MysqlxPrepare.Deallocate)) {
            return super.equals(obj);
         } else {
            MysqlxPrepare.Deallocate other = (MysqlxPrepare.Deallocate)obj;
            if (this.hasStmtId() != other.hasStmtId()) {
               return false;
            } else if (this.hasStmtId() && this.getStmtId() != other.getStmtId()) {
               return false;
            } else {
               return this.getUnknownFields().equals(other.getUnknownFields());
            }
         }
      }

      public int hashCode() {
         if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
         } else {
            int hash = 41;
            int hash = 19 * hash + getDescriptor().hashCode();
            if (this.hasStmtId()) {
               hash = 37 * hash + 1;
               hash = 53 * hash + this.getStmtId();
            }

            hash = 29 * hash + this.getUnknownFields().hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static MysqlxPrepare.Deallocate parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Deallocate)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Deallocate parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Deallocate)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Deallocate parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Deallocate)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Deallocate parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Deallocate)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Deallocate parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Deallocate)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Deallocate parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Deallocate)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Deallocate parseFrom(InputStream input) throws IOException {
         return (MysqlxPrepare.Deallocate)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Deallocate parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Deallocate)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static MysqlxPrepare.Deallocate parseDelimitedFrom(InputStream input) throws IOException {
         return (MysqlxPrepare.Deallocate)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Deallocate parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Deallocate)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static MysqlxPrepare.Deallocate parseFrom(CodedInputStream input) throws IOException {
         return (MysqlxPrepare.Deallocate)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Deallocate parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Deallocate)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public MysqlxPrepare.Deallocate.Builder newBuilderForType() {
         return newBuilder();
      }

      public static MysqlxPrepare.Deallocate.Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static MysqlxPrepare.Deallocate.Builder newBuilder(MysqlxPrepare.Deallocate prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public MysqlxPrepare.Deallocate.Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new MysqlxPrepare.Deallocate.Builder() : (new MysqlxPrepare.Deallocate.Builder()).mergeFrom(this);
      }

      protected MysqlxPrepare.Deallocate.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         MysqlxPrepare.Deallocate.Builder builder = new MysqlxPrepare.Deallocate.Builder(parent);
         return builder;
      }

      public static MysqlxPrepare.Deallocate getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<MysqlxPrepare.Deallocate> parser() {
         return PARSER;
      }

      public Parser<MysqlxPrepare.Deallocate> getParserForType() {
         return PARSER;
      }

      public MysqlxPrepare.Deallocate getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      Deallocate(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<MysqlxPrepare.Deallocate.Builder> implements MysqlxPrepare.DeallocateOrBuilder {
         private int bitField0_;
         private int stmtId_;

         public static final Descriptors.Descriptor getDescriptor() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Deallocate_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Deallocate_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Deallocate.class, MysqlxPrepare.Deallocate.Builder.class);
         }

         private Builder() {
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
         }

         public MysqlxPrepare.Deallocate.Builder clear() {
            super.clear();
            this.stmtId_ = 0;
            this.bitField0_ &= -2;
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Deallocate_descriptor;
         }

         public MysqlxPrepare.Deallocate getDefaultInstanceForType() {
            return MysqlxPrepare.Deallocate.getDefaultInstance();
         }

         public MysqlxPrepare.Deallocate build() {
            MysqlxPrepare.Deallocate result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public MysqlxPrepare.Deallocate buildPartial() {
            MysqlxPrepare.Deallocate result = new MysqlxPrepare.Deallocate(this);
            int from_bitField0_ = this.bitField0_;
            int to_bitField0_ = 0;
            if ((from_bitField0_ & 1) != 0) {
               result.stmtId_ = this.stmtId_;
               to_bitField0_ |= 1;
            }

            result.bitField0_ = to_bitField0_;
            this.onBuilt();
            return result;
         }

         public MysqlxPrepare.Deallocate.Builder clone() {
            return (MysqlxPrepare.Deallocate.Builder)super.clone();
         }

         public MysqlxPrepare.Deallocate.Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (MysqlxPrepare.Deallocate.Builder)super.setField(field, value);
         }

         public MysqlxPrepare.Deallocate.Builder clearField(Descriptors.FieldDescriptor field) {
            return (MysqlxPrepare.Deallocate.Builder)super.clearField(field);
         }

         public MysqlxPrepare.Deallocate.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (MysqlxPrepare.Deallocate.Builder)super.clearOneof(oneof);
         }

         public MysqlxPrepare.Deallocate.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (MysqlxPrepare.Deallocate.Builder)super.setRepeatedField(field, index, value);
         }

         public MysqlxPrepare.Deallocate.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (MysqlxPrepare.Deallocate.Builder)super.addRepeatedField(field, value);
         }

         public MysqlxPrepare.Deallocate.Builder mergeFrom(Message other) {
            if (other instanceof MysqlxPrepare.Deallocate) {
               return this.mergeFrom((MysqlxPrepare.Deallocate)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public MysqlxPrepare.Deallocate.Builder mergeFrom(MysqlxPrepare.Deallocate other) {
            if (other == MysqlxPrepare.Deallocate.getDefaultInstance()) {
               return this;
            } else {
               if (other.hasStmtId()) {
                  this.setStmtId(other.getStmtId());
               }

               this.mergeUnknownFields(other.getUnknownFields());
               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            return this.hasStmtId();
         }

         public MysqlxPrepare.Deallocate.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (extensionRegistry == null) {
               throw new NullPointerException();
            } else {
               try {
                  boolean done = false;

                  while(!done) {
                     int tag = input.readTag();
                     switch(tag) {
                     case 0:
                        done = true;
                        break;
                     case 8:
                        this.stmtId_ = input.readUInt32();
                        this.bitField0_ |= 1;
                        break;
                     default:
                        if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                           done = true;
                        }
                     }
                  }
               } catch (InvalidProtocolBufferException var8) {
                  throw var8.unwrapIOException();
               } finally {
                  this.onChanged();
               }

               return this;
            }
         }

         public boolean hasStmtId() {
            return (this.bitField0_ & 1) != 0;
         }

         public int getStmtId() {
            return this.stmtId_;
         }

         public MysqlxPrepare.Deallocate.Builder setStmtId(int value) {
            this.bitField0_ |= 1;
            this.stmtId_ = value;
            this.onChanged();
            return this;
         }

         public MysqlxPrepare.Deallocate.Builder clearStmtId() {
            this.bitField0_ &= -2;
            this.stmtId_ = 0;
            this.onChanged();
            return this;
         }

         public final MysqlxPrepare.Deallocate.Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (MysqlxPrepare.Deallocate.Builder)super.setUnknownFields(unknownFields);
         }

         public final MysqlxPrepare.Deallocate.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (MysqlxPrepare.Deallocate.Builder)super.mergeUnknownFields(unknownFields);
         }

         // $FF: synthetic method
         Builder(Object x0) {
            this();
         }

         // $FF: synthetic method
         Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
            this(x0);
         }
      }
   }

   public interface DeallocateOrBuilder extends MessageOrBuilder {
      boolean hasStmtId();

      int getStmtId();
   }

   public static final class Execute extends GeneratedMessageV3 implements MysqlxPrepare.ExecuteOrBuilder {
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      public static final int STMT_ID_FIELD_NUMBER = 1;
      private int stmtId_;
      public static final int ARGS_FIELD_NUMBER = 2;
      private List<MysqlxDatatypes.Any> args_;
      public static final int COMPACT_METADATA_FIELD_NUMBER = 3;
      private boolean compactMetadata_;
      private byte memoizedIsInitialized;
      private static final MysqlxPrepare.Execute DEFAULT_INSTANCE = new MysqlxPrepare.Execute();
      /** @deprecated */
      @Deprecated
      public static final Parser<MysqlxPrepare.Execute> PARSER = new AbstractParser<MysqlxPrepare.Execute>() {
         public MysqlxPrepare.Execute parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            MysqlxPrepare.Execute.Builder builder = MysqlxPrepare.Execute.newBuilder();

            try {
               builder.mergeFrom(input, extensionRegistry);
            } catch (InvalidProtocolBufferException var5) {
               throw var5.setUnfinishedMessage(builder.buildPartial());
            } catch (UninitializedMessageException var6) {
               throw var6.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
            } catch (IOException var7) {
               throw (new InvalidProtocolBufferException(var7)).setUnfinishedMessage(builder.buildPartial());
            }

            return builder.buildPartial();
         }
      };

      private Execute(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private Execute() {
         this.memoizedIsInitialized = -1;
         this.args_ = Collections.emptyList();
      }

      protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
         return new MysqlxPrepare.Execute();
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return MysqlxPrepare.internal_static_Mysqlx_Prepare_Execute_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return MysqlxPrepare.internal_static_Mysqlx_Prepare_Execute_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Execute.class, MysqlxPrepare.Execute.Builder.class);
      }

      public boolean hasStmtId() {
         return (this.bitField0_ & 1) != 0;
      }

      public int getStmtId() {
         return this.stmtId_;
      }

      public List<MysqlxDatatypes.Any> getArgsList() {
         return this.args_;
      }

      public List<? extends MysqlxDatatypes.AnyOrBuilder> getArgsOrBuilderList() {
         return this.args_;
      }

      public int getArgsCount() {
         return this.args_.size();
      }

      public MysqlxDatatypes.Any getArgs(int index) {
         return (MysqlxDatatypes.Any)this.args_.get(index);
      }

      public MysqlxDatatypes.AnyOrBuilder getArgsOrBuilder(int index) {
         return (MysqlxDatatypes.AnyOrBuilder)this.args_.get(index);
      }

      public boolean hasCompactMetadata() {
         return (this.bitField0_ & 2) != 0;
      }

      public boolean getCompactMetadata() {
         return this.compactMetadata_;
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else if (!this.hasStmtId()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else {
            for(int i = 0; i < this.getArgsCount(); ++i) {
               if (!this.getArgs(i).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         if ((this.bitField0_ & 1) != 0) {
            output.writeUInt32(1, this.stmtId_);
         }

         for(int i = 0; i < this.args_.size(); ++i) {
            output.writeMessage(2, (MessageLite)this.args_.get(i));
         }

         if ((this.bitField0_ & 2) != 0) {
            output.writeBool(3, this.compactMetadata_);
         }

         this.getUnknownFields().writeTo(output);
      }

      public int getSerializedSize() {
         int size = this.memoizedSize;
         if (size != -1) {
            return size;
         } else {
            size = 0;
            if ((this.bitField0_ & 1) != 0) {
               size += CodedOutputStream.computeUInt32Size(1, this.stmtId_);
            }

            for(int i = 0; i < this.args_.size(); ++i) {
               size += CodedOutputStream.computeMessageSize(2, (MessageLite)this.args_.get(i));
            }

            if ((this.bitField0_ & 2) != 0) {
               size += CodedOutputStream.computeBoolSize(3, this.compactMetadata_);
            }

            size += this.getUnknownFields().getSerializedSize();
            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof MysqlxPrepare.Execute)) {
            return super.equals(obj);
         } else {
            MysqlxPrepare.Execute other = (MysqlxPrepare.Execute)obj;
            if (this.hasStmtId() != other.hasStmtId()) {
               return false;
            } else if (this.hasStmtId() && this.getStmtId() != other.getStmtId()) {
               return false;
            } else if (!this.getArgsList().equals(other.getArgsList())) {
               return false;
            } else if (this.hasCompactMetadata() != other.hasCompactMetadata()) {
               return false;
            } else if (this.hasCompactMetadata() && this.getCompactMetadata() != other.getCompactMetadata()) {
               return false;
            } else {
               return this.getUnknownFields().equals(other.getUnknownFields());
            }
         }
      }

      public int hashCode() {
         if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
         } else {
            int hash = 41;
            int hash = 19 * hash + getDescriptor().hashCode();
            if (this.hasStmtId()) {
               hash = 37 * hash + 1;
               hash = 53 * hash + this.getStmtId();
            }

            if (this.getArgsCount() > 0) {
               hash = 37 * hash + 2;
               hash = 53 * hash + this.getArgsList().hashCode();
            }

            if (this.hasCompactMetadata()) {
               hash = 37 * hash + 3;
               hash = 53 * hash + Internal.hashBoolean(this.getCompactMetadata());
            }

            hash = 29 * hash + this.getUnknownFields().hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static MysqlxPrepare.Execute parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Execute)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Execute parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Execute)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Execute parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Execute)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Execute parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Execute)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Execute parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Execute)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Execute parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Execute)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Execute parseFrom(InputStream input) throws IOException {
         return (MysqlxPrepare.Execute)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Execute parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Execute)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static MysqlxPrepare.Execute parseDelimitedFrom(InputStream input) throws IOException {
         return (MysqlxPrepare.Execute)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Execute parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Execute)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static MysqlxPrepare.Execute parseFrom(CodedInputStream input) throws IOException {
         return (MysqlxPrepare.Execute)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Execute parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Execute)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public MysqlxPrepare.Execute.Builder newBuilderForType() {
         return newBuilder();
      }

      public static MysqlxPrepare.Execute.Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static MysqlxPrepare.Execute.Builder newBuilder(MysqlxPrepare.Execute prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public MysqlxPrepare.Execute.Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new MysqlxPrepare.Execute.Builder() : (new MysqlxPrepare.Execute.Builder()).mergeFrom(this);
      }

      protected MysqlxPrepare.Execute.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         MysqlxPrepare.Execute.Builder builder = new MysqlxPrepare.Execute.Builder(parent);
         return builder;
      }

      public static MysqlxPrepare.Execute getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<MysqlxPrepare.Execute> parser() {
         return PARSER;
      }

      public Parser<MysqlxPrepare.Execute> getParserForType() {
         return PARSER;
      }

      public MysqlxPrepare.Execute getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      Execute(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<MysqlxPrepare.Execute.Builder> implements MysqlxPrepare.ExecuteOrBuilder {
         private int bitField0_;
         private int stmtId_;
         private List<MysqlxDatatypes.Any> args_;
         private RepeatedFieldBuilderV3<MysqlxDatatypes.Any, MysqlxDatatypes.Any.Builder, MysqlxDatatypes.AnyOrBuilder> argsBuilder_;
         private boolean compactMetadata_;

         public static final Descriptors.Descriptor getDescriptor() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Execute_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Execute_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Execute.class, MysqlxPrepare.Execute.Builder.class);
         }

         private Builder() {
            this.args_ = Collections.emptyList();
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.args_ = Collections.emptyList();
         }

         public MysqlxPrepare.Execute.Builder clear() {
            super.clear();
            this.stmtId_ = 0;
            this.bitField0_ &= -2;
            if (this.argsBuilder_ == null) {
               this.args_ = Collections.emptyList();
            } else {
               this.args_ = null;
               this.argsBuilder_.clear();
            }

            this.bitField0_ &= -3;
            this.compactMetadata_ = false;
            this.bitField0_ &= -5;
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Execute_descriptor;
         }

         public MysqlxPrepare.Execute getDefaultInstanceForType() {
            return MysqlxPrepare.Execute.getDefaultInstance();
         }

         public MysqlxPrepare.Execute build() {
            MysqlxPrepare.Execute result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public MysqlxPrepare.Execute buildPartial() {
            MysqlxPrepare.Execute result = new MysqlxPrepare.Execute(this);
            int from_bitField0_ = this.bitField0_;
            int to_bitField0_ = 0;
            if ((from_bitField0_ & 1) != 0) {
               result.stmtId_ = this.stmtId_;
               to_bitField0_ |= 1;
            }

            if (this.argsBuilder_ == null) {
               if ((this.bitField0_ & 2) != 0) {
                  this.args_ = Collections.unmodifiableList(this.args_);
                  this.bitField0_ &= -3;
               }

               result.args_ = this.args_;
            } else {
               result.args_ = this.argsBuilder_.build();
            }

            if ((from_bitField0_ & 4) != 0) {
               result.compactMetadata_ = this.compactMetadata_;
               to_bitField0_ |= 2;
            }

            result.bitField0_ = to_bitField0_;
            this.onBuilt();
            return result;
         }

         public MysqlxPrepare.Execute.Builder clone() {
            return (MysqlxPrepare.Execute.Builder)super.clone();
         }

         public MysqlxPrepare.Execute.Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (MysqlxPrepare.Execute.Builder)super.setField(field, value);
         }

         public MysqlxPrepare.Execute.Builder clearField(Descriptors.FieldDescriptor field) {
            return (MysqlxPrepare.Execute.Builder)super.clearField(field);
         }

         public MysqlxPrepare.Execute.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (MysqlxPrepare.Execute.Builder)super.clearOneof(oneof);
         }

         public MysqlxPrepare.Execute.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (MysqlxPrepare.Execute.Builder)super.setRepeatedField(field, index, value);
         }

         public MysqlxPrepare.Execute.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (MysqlxPrepare.Execute.Builder)super.addRepeatedField(field, value);
         }

         public MysqlxPrepare.Execute.Builder mergeFrom(Message other) {
            if (other instanceof MysqlxPrepare.Execute) {
               return this.mergeFrom((MysqlxPrepare.Execute)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public MysqlxPrepare.Execute.Builder mergeFrom(MysqlxPrepare.Execute other) {
            if (other == MysqlxPrepare.Execute.getDefaultInstance()) {
               return this;
            } else {
               if (other.hasStmtId()) {
                  this.setStmtId(other.getStmtId());
               }

               if (this.argsBuilder_ == null) {
                  if (!other.args_.isEmpty()) {
                     if (this.args_.isEmpty()) {
                        this.args_ = other.args_;
                        this.bitField0_ &= -3;
                     } else {
                        this.ensureArgsIsMutable();
                        this.args_.addAll(other.args_);
                     }

                     this.onChanged();
                  }
               } else if (!other.args_.isEmpty()) {
                  if (this.argsBuilder_.isEmpty()) {
                     this.argsBuilder_.dispose();
                     this.argsBuilder_ = null;
                     this.args_ = other.args_;
                     this.bitField0_ &= -3;
                     this.argsBuilder_ = MysqlxPrepare.Execute.alwaysUseFieldBuilders ? this.getArgsFieldBuilder() : null;
                  } else {
                     this.argsBuilder_.addAllMessages(other.args_);
                  }
               }

               if (other.hasCompactMetadata()) {
                  this.setCompactMetadata(other.getCompactMetadata());
               }

               this.mergeUnknownFields(other.getUnknownFields());
               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            if (!this.hasStmtId()) {
               return false;
            } else {
               for(int i = 0; i < this.getArgsCount(); ++i) {
                  if (!this.getArgs(i).isInitialized()) {
                     return false;
                  }
               }

               return true;
            }
         }

         public MysqlxPrepare.Execute.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (extensionRegistry == null) {
               throw new NullPointerException();
            } else {
               try {
                  boolean done = false;

                  while(!done) {
                     int tag = input.readTag();
                     switch(tag) {
                     case 0:
                        done = true;
                        break;
                     case 8:
                        this.stmtId_ = input.readUInt32();
                        this.bitField0_ |= 1;
                        break;
                     case 18:
                        MysqlxDatatypes.Any m = (MysqlxDatatypes.Any)input.readMessage(MysqlxDatatypes.Any.PARSER, extensionRegistry);
                        if (this.argsBuilder_ == null) {
                           this.ensureArgsIsMutable();
                           this.args_.add(m);
                        } else {
                           this.argsBuilder_.addMessage(m);
                        }
                        break;
                     case 24:
                        this.compactMetadata_ = input.readBool();
                        this.bitField0_ |= 4;
                        break;
                     default:
                        if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                           done = true;
                        }
                     }
                  }
               } catch (InvalidProtocolBufferException var9) {
                  throw var9.unwrapIOException();
               } finally {
                  this.onChanged();
               }

               return this;
            }
         }

         public boolean hasStmtId() {
            return (this.bitField0_ & 1) != 0;
         }

         public int getStmtId() {
            return this.stmtId_;
         }

         public MysqlxPrepare.Execute.Builder setStmtId(int value) {
            this.bitField0_ |= 1;
            this.stmtId_ = value;
            this.onChanged();
            return this;
         }

         public MysqlxPrepare.Execute.Builder clearStmtId() {
            this.bitField0_ &= -2;
            this.stmtId_ = 0;
            this.onChanged();
            return this;
         }

         private void ensureArgsIsMutable() {
            if ((this.bitField0_ & 2) == 0) {
               this.args_ = new ArrayList(this.args_);
               this.bitField0_ |= 2;
            }

         }

         public List<MysqlxDatatypes.Any> getArgsList() {
            return this.argsBuilder_ == null ? Collections.unmodifiableList(this.args_) : this.argsBuilder_.getMessageList();
         }

         public int getArgsCount() {
            return this.argsBuilder_ == null ? this.args_.size() : this.argsBuilder_.getCount();
         }

         public MysqlxDatatypes.Any getArgs(int index) {
            return this.argsBuilder_ == null ? (MysqlxDatatypes.Any)this.args_.get(index) : (MysqlxDatatypes.Any)this.argsBuilder_.getMessage(index);
         }

         public MysqlxPrepare.Execute.Builder setArgs(int index, MysqlxDatatypes.Any value) {
            if (this.argsBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureArgsIsMutable();
               this.args_.set(index, value);
               this.onChanged();
            } else {
               this.argsBuilder_.setMessage(index, value);
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder setArgs(int index, MysqlxDatatypes.Any.Builder builderForValue) {
            if (this.argsBuilder_ == null) {
               this.ensureArgsIsMutable();
               this.args_.set(index, builderForValue.build());
               this.onChanged();
            } else {
               this.argsBuilder_.setMessage(index, builderForValue.build());
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder addArgs(MysqlxDatatypes.Any value) {
            if (this.argsBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureArgsIsMutable();
               this.args_.add(value);
               this.onChanged();
            } else {
               this.argsBuilder_.addMessage(value);
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder addArgs(int index, MysqlxDatatypes.Any value) {
            if (this.argsBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureArgsIsMutable();
               this.args_.add(index, value);
               this.onChanged();
            } else {
               this.argsBuilder_.addMessage(index, value);
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder addArgs(MysqlxDatatypes.Any.Builder builderForValue) {
            if (this.argsBuilder_ == null) {
               this.ensureArgsIsMutable();
               this.args_.add(builderForValue.build());
               this.onChanged();
            } else {
               this.argsBuilder_.addMessage(builderForValue.build());
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder addArgs(int index, MysqlxDatatypes.Any.Builder builderForValue) {
            if (this.argsBuilder_ == null) {
               this.ensureArgsIsMutable();
               this.args_.add(index, builderForValue.build());
               this.onChanged();
            } else {
               this.argsBuilder_.addMessage(index, builderForValue.build());
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder addAllArgs(Iterable<? extends MysqlxDatatypes.Any> values) {
            if (this.argsBuilder_ == null) {
               this.ensureArgsIsMutable();
               AbstractMessageLite.Builder.addAll(values, this.args_);
               this.onChanged();
            } else {
               this.argsBuilder_.addAllMessages(values);
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder clearArgs() {
            if (this.argsBuilder_ == null) {
               this.args_ = Collections.emptyList();
               this.bitField0_ &= -3;
               this.onChanged();
            } else {
               this.argsBuilder_.clear();
            }

            return this;
         }

         public MysqlxPrepare.Execute.Builder removeArgs(int index) {
            if (this.argsBuilder_ == null) {
               this.ensureArgsIsMutable();
               this.args_.remove(index);
               this.onChanged();
            } else {
               this.argsBuilder_.remove(index);
            }

            return this;
         }

         public MysqlxDatatypes.Any.Builder getArgsBuilder(int index) {
            return (MysqlxDatatypes.Any.Builder)this.getArgsFieldBuilder().getBuilder(index);
         }

         public MysqlxDatatypes.AnyOrBuilder getArgsOrBuilder(int index) {
            return this.argsBuilder_ == null ? (MysqlxDatatypes.AnyOrBuilder)this.args_.get(index) : (MysqlxDatatypes.AnyOrBuilder)this.argsBuilder_.getMessageOrBuilder(index);
         }

         public List<? extends MysqlxDatatypes.AnyOrBuilder> getArgsOrBuilderList() {
            return this.argsBuilder_ != null ? this.argsBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.args_);
         }

         public MysqlxDatatypes.Any.Builder addArgsBuilder() {
            return (MysqlxDatatypes.Any.Builder)this.getArgsFieldBuilder().addBuilder(MysqlxDatatypes.Any.getDefaultInstance());
         }

         public MysqlxDatatypes.Any.Builder addArgsBuilder(int index) {
            return (MysqlxDatatypes.Any.Builder)this.getArgsFieldBuilder().addBuilder(index, MysqlxDatatypes.Any.getDefaultInstance());
         }

         public List<MysqlxDatatypes.Any.Builder> getArgsBuilderList() {
            return this.getArgsFieldBuilder().getBuilderList();
         }

         private RepeatedFieldBuilderV3<MysqlxDatatypes.Any, MysqlxDatatypes.Any.Builder, MysqlxDatatypes.AnyOrBuilder> getArgsFieldBuilder() {
            if (this.argsBuilder_ == null) {
               this.argsBuilder_ = new RepeatedFieldBuilderV3(this.args_, (this.bitField0_ & 2) != 0, this.getParentForChildren(), this.isClean());
               this.args_ = null;
            }

            return this.argsBuilder_;
         }

         public boolean hasCompactMetadata() {
            return (this.bitField0_ & 4) != 0;
         }

         public boolean getCompactMetadata() {
            return this.compactMetadata_;
         }

         public MysqlxPrepare.Execute.Builder setCompactMetadata(boolean value) {
            this.bitField0_ |= 4;
            this.compactMetadata_ = value;
            this.onChanged();
            return this;
         }

         public MysqlxPrepare.Execute.Builder clearCompactMetadata() {
            this.bitField0_ &= -5;
            this.compactMetadata_ = false;
            this.onChanged();
            return this;
         }

         public final MysqlxPrepare.Execute.Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (MysqlxPrepare.Execute.Builder)super.setUnknownFields(unknownFields);
         }

         public final MysqlxPrepare.Execute.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (MysqlxPrepare.Execute.Builder)super.mergeUnknownFields(unknownFields);
         }

         // $FF: synthetic method
         Builder(Object x0) {
            this();
         }

         // $FF: synthetic method
         Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
            this(x0);
         }
      }
   }

   public interface ExecuteOrBuilder extends MessageOrBuilder {
      boolean hasStmtId();

      int getStmtId();

      List<MysqlxDatatypes.Any> getArgsList();

      MysqlxDatatypes.Any getArgs(int var1);

      int getArgsCount();

      List<? extends MysqlxDatatypes.AnyOrBuilder> getArgsOrBuilderList();

      MysqlxDatatypes.AnyOrBuilder getArgsOrBuilder(int var1);

      boolean hasCompactMetadata();

      boolean getCompactMetadata();
   }

   public static final class Prepare extends GeneratedMessageV3 implements MysqlxPrepare.PrepareOrBuilder {
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      public static final int STMT_ID_FIELD_NUMBER = 1;
      private int stmtId_;
      public static final int STMT_FIELD_NUMBER = 2;
      private MysqlxPrepare.Prepare.OneOfMessage stmt_;
      private byte memoizedIsInitialized;
      private static final MysqlxPrepare.Prepare DEFAULT_INSTANCE = new MysqlxPrepare.Prepare();
      /** @deprecated */
      @Deprecated
      public static final Parser<MysqlxPrepare.Prepare> PARSER = new AbstractParser<MysqlxPrepare.Prepare>() {
         public MysqlxPrepare.Prepare parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            MysqlxPrepare.Prepare.Builder builder = MysqlxPrepare.Prepare.newBuilder();

            try {
               builder.mergeFrom(input, extensionRegistry);
            } catch (InvalidProtocolBufferException var5) {
               throw var5.setUnfinishedMessage(builder.buildPartial());
            } catch (UninitializedMessageException var6) {
               throw var6.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
            } catch (IOException var7) {
               throw (new InvalidProtocolBufferException(var7)).setUnfinishedMessage(builder.buildPartial());
            }

            return builder.buildPartial();
         }
      };

      private Prepare(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private Prepare() {
         this.memoizedIsInitialized = -1;
      }

      protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
         return new MysqlxPrepare.Prepare();
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Prepare.class, MysqlxPrepare.Prepare.Builder.class);
      }

      public boolean hasStmtId() {
         return (this.bitField0_ & 1) != 0;
      }

      public int getStmtId() {
         return this.stmtId_;
      }

      public boolean hasStmt() {
         return (this.bitField0_ & 2) != 0;
      }

      public MysqlxPrepare.Prepare.OneOfMessage getStmt() {
         return this.stmt_ == null ? MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance() : this.stmt_;
      }

      public MysqlxPrepare.Prepare.OneOfMessageOrBuilder getStmtOrBuilder() {
         return this.stmt_ == null ? MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance() : this.stmt_;
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else if (!this.hasStmtId()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else if (!this.hasStmt()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else if (!this.getStmt().isInitialized()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         if ((this.bitField0_ & 1) != 0) {
            output.writeUInt32(1, this.stmtId_);
         }

         if ((this.bitField0_ & 2) != 0) {
            output.writeMessage(2, this.getStmt());
         }

         this.getUnknownFields().writeTo(output);
      }

      public int getSerializedSize() {
         int size = this.memoizedSize;
         if (size != -1) {
            return size;
         } else {
            size = 0;
            if ((this.bitField0_ & 1) != 0) {
               size += CodedOutputStream.computeUInt32Size(1, this.stmtId_);
            }

            if ((this.bitField0_ & 2) != 0) {
               size += CodedOutputStream.computeMessageSize(2, this.getStmt());
            }

            size += this.getUnknownFields().getSerializedSize();
            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof MysqlxPrepare.Prepare)) {
            return super.equals(obj);
         } else {
            MysqlxPrepare.Prepare other = (MysqlxPrepare.Prepare)obj;
            if (this.hasStmtId() != other.hasStmtId()) {
               return false;
            } else if (this.hasStmtId() && this.getStmtId() != other.getStmtId()) {
               return false;
            } else if (this.hasStmt() != other.hasStmt()) {
               return false;
            } else if (this.hasStmt() && !this.getStmt().equals(other.getStmt())) {
               return false;
            } else {
               return this.getUnknownFields().equals(other.getUnknownFields());
            }
         }
      }

      public int hashCode() {
         if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
         } else {
            int hash = 41;
            int hash = 19 * hash + getDescriptor().hashCode();
            if (this.hasStmtId()) {
               hash = 37 * hash + 1;
               hash = 53 * hash + this.getStmtId();
            }

            if (this.hasStmt()) {
               hash = 37 * hash + 2;
               hash = 53 * hash + this.getStmt().hashCode();
            }

            hash = 29 * hash + this.getUnknownFields().hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static MysqlxPrepare.Prepare parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Prepare)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Prepare parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Prepare)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Prepare parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Prepare)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Prepare parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Prepare)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Prepare parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Prepare)PARSER.parseFrom(data);
      }

      public static MysqlxPrepare.Prepare parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (MysqlxPrepare.Prepare)PARSER.parseFrom(data, extensionRegistry);
      }

      public static MysqlxPrepare.Prepare parseFrom(InputStream input) throws IOException {
         return (MysqlxPrepare.Prepare)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Prepare parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Prepare)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static MysqlxPrepare.Prepare parseDelimitedFrom(InputStream input) throws IOException {
         return (MysqlxPrepare.Prepare)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Prepare parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Prepare)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static MysqlxPrepare.Prepare parseFrom(CodedInputStream input) throws IOException {
         return (MysqlxPrepare.Prepare)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static MysqlxPrepare.Prepare parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (MysqlxPrepare.Prepare)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public MysqlxPrepare.Prepare.Builder newBuilderForType() {
         return newBuilder();
      }

      public static MysqlxPrepare.Prepare.Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static MysqlxPrepare.Prepare.Builder newBuilder(MysqlxPrepare.Prepare prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public MysqlxPrepare.Prepare.Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new MysqlxPrepare.Prepare.Builder() : (new MysqlxPrepare.Prepare.Builder()).mergeFrom(this);
      }

      protected MysqlxPrepare.Prepare.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         MysqlxPrepare.Prepare.Builder builder = new MysqlxPrepare.Prepare.Builder(parent);
         return builder;
      }

      public static MysqlxPrepare.Prepare getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<MysqlxPrepare.Prepare> parser() {
         return PARSER;
      }

      public Parser<MysqlxPrepare.Prepare> getParserForType() {
         return PARSER;
      }

      public MysqlxPrepare.Prepare getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      Prepare(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<MysqlxPrepare.Prepare.Builder> implements MysqlxPrepare.PrepareOrBuilder {
         private int bitField0_;
         private int stmtId_;
         private MysqlxPrepare.Prepare.OneOfMessage stmt_;
         private SingleFieldBuilderV3<MysqlxPrepare.Prepare.OneOfMessage, MysqlxPrepare.Prepare.OneOfMessage.Builder, MysqlxPrepare.Prepare.OneOfMessageOrBuilder> stmtBuilder_;

         public static final Descriptors.Descriptor getDescriptor() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Prepare.class, MysqlxPrepare.Prepare.Builder.class);
         }

         private Builder() {
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.maybeForceBuilderInitialization();
         }

         private void maybeForceBuilderInitialization() {
            if (MysqlxPrepare.Prepare.alwaysUseFieldBuilders) {
               this.getStmtFieldBuilder();
            }

         }

         public MysqlxPrepare.Prepare.Builder clear() {
            super.clear();
            this.stmtId_ = 0;
            this.bitField0_ &= -2;
            if (this.stmtBuilder_ == null) {
               this.stmt_ = null;
            } else {
               this.stmtBuilder_.clear();
            }

            this.bitField0_ &= -3;
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_descriptor;
         }

         public MysqlxPrepare.Prepare getDefaultInstanceForType() {
            return MysqlxPrepare.Prepare.getDefaultInstance();
         }

         public MysqlxPrepare.Prepare build() {
            MysqlxPrepare.Prepare result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public MysqlxPrepare.Prepare buildPartial() {
            MysqlxPrepare.Prepare result = new MysqlxPrepare.Prepare(this);
            int from_bitField0_ = this.bitField0_;
            int to_bitField0_ = 0;
            if ((from_bitField0_ & 1) != 0) {
               result.stmtId_ = this.stmtId_;
               to_bitField0_ |= 1;
            }

            if ((from_bitField0_ & 2) != 0) {
               if (this.stmtBuilder_ == null) {
                  result.stmt_ = this.stmt_;
               } else {
                  result.stmt_ = (MysqlxPrepare.Prepare.OneOfMessage)this.stmtBuilder_.build();
               }

               to_bitField0_ |= 2;
            }

            result.bitField0_ = to_bitField0_;
            this.onBuilt();
            return result;
         }

         public MysqlxPrepare.Prepare.Builder clone() {
            return (MysqlxPrepare.Prepare.Builder)super.clone();
         }

         public MysqlxPrepare.Prepare.Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (MysqlxPrepare.Prepare.Builder)super.setField(field, value);
         }

         public MysqlxPrepare.Prepare.Builder clearField(Descriptors.FieldDescriptor field) {
            return (MysqlxPrepare.Prepare.Builder)super.clearField(field);
         }

         public MysqlxPrepare.Prepare.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (MysqlxPrepare.Prepare.Builder)super.clearOneof(oneof);
         }

         public MysqlxPrepare.Prepare.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (MysqlxPrepare.Prepare.Builder)super.setRepeatedField(field, index, value);
         }

         public MysqlxPrepare.Prepare.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (MysqlxPrepare.Prepare.Builder)super.addRepeatedField(field, value);
         }

         public MysqlxPrepare.Prepare.Builder mergeFrom(Message other) {
            if (other instanceof MysqlxPrepare.Prepare) {
               return this.mergeFrom((MysqlxPrepare.Prepare)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public MysqlxPrepare.Prepare.Builder mergeFrom(MysqlxPrepare.Prepare other) {
            if (other == MysqlxPrepare.Prepare.getDefaultInstance()) {
               return this;
            } else {
               if (other.hasStmtId()) {
                  this.setStmtId(other.getStmtId());
               }

               if (other.hasStmt()) {
                  this.mergeStmt(other.getStmt());
               }

               this.mergeUnknownFields(other.getUnknownFields());
               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            if (!this.hasStmtId()) {
               return false;
            } else if (!this.hasStmt()) {
               return false;
            } else {
               return this.getStmt().isInitialized();
            }
         }

         public MysqlxPrepare.Prepare.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (extensionRegistry == null) {
               throw new NullPointerException();
            } else {
               try {
                  boolean done = false;

                  while(!done) {
                     int tag = input.readTag();
                     switch(tag) {
                     case 0:
                        done = true;
                        break;
                     case 8:
                        this.stmtId_ = input.readUInt32();
                        this.bitField0_ |= 1;
                        break;
                     case 18:
                        input.readMessage((MessageLite.Builder)this.getStmtFieldBuilder().getBuilder(), extensionRegistry);
                        this.bitField0_ |= 2;
                        break;
                     default:
                        if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                           done = true;
                        }
                     }
                  }
               } catch (InvalidProtocolBufferException var8) {
                  throw var8.unwrapIOException();
               } finally {
                  this.onChanged();
               }

               return this;
            }
         }

         public boolean hasStmtId() {
            return (this.bitField0_ & 1) != 0;
         }

         public int getStmtId() {
            return this.stmtId_;
         }

         public MysqlxPrepare.Prepare.Builder setStmtId(int value) {
            this.bitField0_ |= 1;
            this.stmtId_ = value;
            this.onChanged();
            return this;
         }

         public MysqlxPrepare.Prepare.Builder clearStmtId() {
            this.bitField0_ &= -2;
            this.stmtId_ = 0;
            this.onChanged();
            return this;
         }

         public boolean hasStmt() {
            return (this.bitField0_ & 2) != 0;
         }

         public MysqlxPrepare.Prepare.OneOfMessage getStmt() {
            if (this.stmtBuilder_ == null) {
               return this.stmt_ == null ? MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance() : this.stmt_;
            } else {
               return (MysqlxPrepare.Prepare.OneOfMessage)this.stmtBuilder_.getMessage();
            }
         }

         public MysqlxPrepare.Prepare.Builder setStmt(MysqlxPrepare.Prepare.OneOfMessage value) {
            if (this.stmtBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.stmt_ = value;
               this.onChanged();
            } else {
               this.stmtBuilder_.setMessage(value);
            }

            this.bitField0_ |= 2;
            return this;
         }

         public MysqlxPrepare.Prepare.Builder setStmt(MysqlxPrepare.Prepare.OneOfMessage.Builder builderForValue) {
            if (this.stmtBuilder_ == null) {
               this.stmt_ = builderForValue.build();
               this.onChanged();
            } else {
               this.stmtBuilder_.setMessage(builderForValue.build());
            }

            this.bitField0_ |= 2;
            return this;
         }

         public MysqlxPrepare.Prepare.Builder mergeStmt(MysqlxPrepare.Prepare.OneOfMessage value) {
            if (this.stmtBuilder_ == null) {
               if ((this.bitField0_ & 2) != 0 && this.stmt_ != null && this.stmt_ != MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance()) {
                  this.stmt_ = MysqlxPrepare.Prepare.OneOfMessage.newBuilder(this.stmt_).mergeFrom(value).buildPartial();
               } else {
                  this.stmt_ = value;
               }

               this.onChanged();
            } else {
               this.stmtBuilder_.mergeFrom(value);
            }

            this.bitField0_ |= 2;
            return this;
         }

         public MysqlxPrepare.Prepare.Builder clearStmt() {
            if (this.stmtBuilder_ == null) {
               this.stmt_ = null;
               this.onChanged();
            } else {
               this.stmtBuilder_.clear();
            }

            this.bitField0_ &= -3;
            return this;
         }

         public MysqlxPrepare.Prepare.OneOfMessage.Builder getStmtBuilder() {
            this.bitField0_ |= 2;
            this.onChanged();
            return (MysqlxPrepare.Prepare.OneOfMessage.Builder)this.getStmtFieldBuilder().getBuilder();
         }

         public MysqlxPrepare.Prepare.OneOfMessageOrBuilder getStmtOrBuilder() {
            if (this.stmtBuilder_ != null) {
               return (MysqlxPrepare.Prepare.OneOfMessageOrBuilder)this.stmtBuilder_.getMessageOrBuilder();
            } else {
               return this.stmt_ == null ? MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance() : this.stmt_;
            }
         }

         private SingleFieldBuilderV3<MysqlxPrepare.Prepare.OneOfMessage, MysqlxPrepare.Prepare.OneOfMessage.Builder, MysqlxPrepare.Prepare.OneOfMessageOrBuilder> getStmtFieldBuilder() {
            if (this.stmtBuilder_ == null) {
               this.stmtBuilder_ = new SingleFieldBuilderV3(this.getStmt(), this.getParentForChildren(), this.isClean());
               this.stmt_ = null;
            }

            return this.stmtBuilder_;
         }

         public final MysqlxPrepare.Prepare.Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (MysqlxPrepare.Prepare.Builder)super.setUnknownFields(unknownFields);
         }

         public final MysqlxPrepare.Prepare.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (MysqlxPrepare.Prepare.Builder)super.mergeUnknownFields(unknownFields);
         }

         // $FF: synthetic method
         Builder(Object x0) {
            this();
         }

         // $FF: synthetic method
         Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
            this(x0);
         }
      }

      public static final class OneOfMessage extends GeneratedMessageV3 implements MysqlxPrepare.Prepare.OneOfMessageOrBuilder {
         private static final long serialVersionUID = 0L;
         private int bitField0_;
         public static final int TYPE_FIELD_NUMBER = 1;
         private int type_;
         public static final int FIND_FIELD_NUMBER = 2;
         private MysqlxCrud.Find find_;
         public static final int INSERT_FIELD_NUMBER = 3;
         private MysqlxCrud.Insert insert_;
         public static final int UPDATE_FIELD_NUMBER = 4;
         private MysqlxCrud.Update update_;
         public static final int DELETE_FIELD_NUMBER = 5;
         private MysqlxCrud.Delete delete_;
         public static final int STMT_EXECUTE_FIELD_NUMBER = 6;
         private MysqlxSql.StmtExecute stmtExecute_;
         private byte memoizedIsInitialized;
         private static final MysqlxPrepare.Prepare.OneOfMessage DEFAULT_INSTANCE = new MysqlxPrepare.Prepare.OneOfMessage();
         /** @deprecated */
         @Deprecated
         public static final Parser<MysqlxPrepare.Prepare.OneOfMessage> PARSER = new AbstractParser<MysqlxPrepare.Prepare.OneOfMessage>() {
            public MysqlxPrepare.Prepare.OneOfMessage parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
               MysqlxPrepare.Prepare.OneOfMessage.Builder builder = MysqlxPrepare.Prepare.OneOfMessage.newBuilder();

               try {
                  builder.mergeFrom(input, extensionRegistry);
               } catch (InvalidProtocolBufferException var5) {
                  throw var5.setUnfinishedMessage(builder.buildPartial());
               } catch (UninitializedMessageException var6) {
                  throw var6.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
               } catch (IOException var7) {
                  throw (new InvalidProtocolBufferException(var7)).setUnfinishedMessage(builder.buildPartial());
               }

               return builder.buildPartial();
            }
         };

         private OneOfMessage(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
         }

         private OneOfMessage() {
            this.memoizedIsInitialized = -1;
            this.type_ = 0;
         }

         protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new MysqlxPrepare.Prepare.OneOfMessage();
         }

         public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Prepare.OneOfMessage.class, MysqlxPrepare.Prepare.OneOfMessage.Builder.class);
         }

         public boolean hasType() {
            return (this.bitField0_ & 1) != 0;
         }

         public MysqlxPrepare.Prepare.OneOfMessage.Type getType() {
            MysqlxPrepare.Prepare.OneOfMessage.Type result = MysqlxPrepare.Prepare.OneOfMessage.Type.valueOf(this.type_);
            return result == null ? MysqlxPrepare.Prepare.OneOfMessage.Type.FIND : result;
         }

         public boolean hasFind() {
            return (this.bitField0_ & 2) != 0;
         }

         public MysqlxCrud.Find getFind() {
            return this.find_ == null ? MysqlxCrud.Find.getDefaultInstance() : this.find_;
         }

         public MysqlxCrud.FindOrBuilder getFindOrBuilder() {
            return this.find_ == null ? MysqlxCrud.Find.getDefaultInstance() : this.find_;
         }

         public boolean hasInsert() {
            return (this.bitField0_ & 4) != 0;
         }

         public MysqlxCrud.Insert getInsert() {
            return this.insert_ == null ? MysqlxCrud.Insert.getDefaultInstance() : this.insert_;
         }

         public MysqlxCrud.InsertOrBuilder getInsertOrBuilder() {
            return this.insert_ == null ? MysqlxCrud.Insert.getDefaultInstance() : this.insert_;
         }

         public boolean hasUpdate() {
            return (this.bitField0_ & 8) != 0;
         }

         public MysqlxCrud.Update getUpdate() {
            return this.update_ == null ? MysqlxCrud.Update.getDefaultInstance() : this.update_;
         }

         public MysqlxCrud.UpdateOrBuilder getUpdateOrBuilder() {
            return this.update_ == null ? MysqlxCrud.Update.getDefaultInstance() : this.update_;
         }

         public boolean hasDelete() {
            return (this.bitField0_ & 16) != 0;
         }

         public MysqlxCrud.Delete getDelete() {
            return this.delete_ == null ? MysqlxCrud.Delete.getDefaultInstance() : this.delete_;
         }

         public MysqlxCrud.DeleteOrBuilder getDeleteOrBuilder() {
            return this.delete_ == null ? MysqlxCrud.Delete.getDefaultInstance() : this.delete_;
         }

         public boolean hasStmtExecute() {
            return (this.bitField0_ & 32) != 0;
         }

         public MysqlxSql.StmtExecute getStmtExecute() {
            return this.stmtExecute_ == null ? MysqlxSql.StmtExecute.getDefaultInstance() : this.stmtExecute_;
         }

         public MysqlxSql.StmtExecuteOrBuilder getStmtExecuteOrBuilder() {
            return this.stmtExecute_ == null ? MysqlxSql.StmtExecute.getDefaultInstance() : this.stmtExecute_;
         }

         public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
               return true;
            } else if (isInitialized == 0) {
               return false;
            } else if (!this.hasType()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else if (this.hasFind() && !this.getFind().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else if (this.hasInsert() && !this.getInsert().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else if (this.hasUpdate() && !this.getUpdate().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else if (this.hasDelete() && !this.getDelete().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else if (this.hasStmtExecute() && !this.getStmtExecute().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }

         public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) != 0) {
               output.writeEnum(1, this.type_);
            }

            if ((this.bitField0_ & 2) != 0) {
               output.writeMessage(2, this.getFind());
            }

            if ((this.bitField0_ & 4) != 0) {
               output.writeMessage(3, this.getInsert());
            }

            if ((this.bitField0_ & 8) != 0) {
               output.writeMessage(4, this.getUpdate());
            }

            if ((this.bitField0_ & 16) != 0) {
               output.writeMessage(5, this.getDelete());
            }

            if ((this.bitField0_ & 32) != 0) {
               output.writeMessage(6, this.getStmtExecute());
            }

            this.getUnknownFields().writeTo(output);
         }

         public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
               return size;
            } else {
               size = 0;
               if ((this.bitField0_ & 1) != 0) {
                  size += CodedOutputStream.computeEnumSize(1, this.type_);
               }

               if ((this.bitField0_ & 2) != 0) {
                  size += CodedOutputStream.computeMessageSize(2, this.getFind());
               }

               if ((this.bitField0_ & 4) != 0) {
                  size += CodedOutputStream.computeMessageSize(3, this.getInsert());
               }

               if ((this.bitField0_ & 8) != 0) {
                  size += CodedOutputStream.computeMessageSize(4, this.getUpdate());
               }

               if ((this.bitField0_ & 16) != 0) {
                  size += CodedOutputStream.computeMessageSize(5, this.getDelete());
               }

               if ((this.bitField0_ & 32) != 0) {
                  size += CodedOutputStream.computeMessageSize(6, this.getStmtExecute());
               }

               size += this.getUnknownFields().getSerializedSize();
               this.memoizedSize = size;
               return size;
            }
         }

         public boolean equals(Object obj) {
            if (obj == this) {
               return true;
            } else if (!(obj instanceof MysqlxPrepare.Prepare.OneOfMessage)) {
               return super.equals(obj);
            } else {
               MysqlxPrepare.Prepare.OneOfMessage other = (MysqlxPrepare.Prepare.OneOfMessage)obj;
               if (this.hasType() != other.hasType()) {
                  return false;
               } else if (this.hasType() && this.type_ != other.type_) {
                  return false;
               } else if (this.hasFind() != other.hasFind()) {
                  return false;
               } else if (this.hasFind() && !this.getFind().equals(other.getFind())) {
                  return false;
               } else if (this.hasInsert() != other.hasInsert()) {
                  return false;
               } else if (this.hasInsert() && !this.getInsert().equals(other.getInsert())) {
                  return false;
               } else if (this.hasUpdate() != other.hasUpdate()) {
                  return false;
               } else if (this.hasUpdate() && !this.getUpdate().equals(other.getUpdate())) {
                  return false;
               } else if (this.hasDelete() != other.hasDelete()) {
                  return false;
               } else if (this.hasDelete() && !this.getDelete().equals(other.getDelete())) {
                  return false;
               } else if (this.hasStmtExecute() != other.hasStmtExecute()) {
                  return false;
               } else if (this.hasStmtExecute() && !this.getStmtExecute().equals(other.getStmtExecute())) {
                  return false;
               } else {
                  return this.getUnknownFields().equals(other.getUnknownFields());
               }
            }
         }

         public int hashCode() {
            if (this.memoizedHashCode != 0) {
               return this.memoizedHashCode;
            } else {
               int hash = 41;
               int hash = 19 * hash + getDescriptor().hashCode();
               if (this.hasType()) {
                  hash = 37 * hash + 1;
                  hash = 53 * hash + this.type_;
               }

               if (this.hasFind()) {
                  hash = 37 * hash + 2;
                  hash = 53 * hash + this.getFind().hashCode();
               }

               if (this.hasInsert()) {
                  hash = 37 * hash + 3;
                  hash = 53 * hash + this.getInsert().hashCode();
               }

               if (this.hasUpdate()) {
                  hash = 37 * hash + 4;
                  hash = 53 * hash + this.getUpdate().hashCode();
               }

               if (this.hasDelete()) {
                  hash = 37 * hash + 5;
                  hash = 53 * hash + this.getDelete().hashCode();
               }

               if (this.hasStmtExecute()) {
                  hash = 37 * hash + 6;
                  hash = 53 * hash + this.getStmtExecute().hashCode();
               }

               hash = 29 * hash + this.getUnknownFields().hashCode();
               this.memoizedHashCode = hash;
               return hash;
            }
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return (MysqlxPrepare.Prepare.OneOfMessage)PARSER.parseFrom(data);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (MysqlxPrepare.Prepare.OneOfMessage)PARSER.parseFrom(data, extensionRegistry);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (MysqlxPrepare.Prepare.OneOfMessage)PARSER.parseFrom(data);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (MysqlxPrepare.Prepare.OneOfMessage)PARSER.parseFrom(data, extensionRegistry);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (MysqlxPrepare.Prepare.OneOfMessage)PARSER.parseFrom(data);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (MysqlxPrepare.Prepare.OneOfMessage)PARSER.parseFrom(data, extensionRegistry);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(InputStream input) throws IOException {
            return (MysqlxPrepare.Prepare.OneOfMessage)GeneratedMessageV3.parseWithIOException(PARSER, input);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (MysqlxPrepare.Prepare.OneOfMessage)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseDelimitedFrom(InputStream input) throws IOException {
            return (MysqlxPrepare.Prepare.OneOfMessage)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (MysqlxPrepare.Prepare.OneOfMessage)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(CodedInputStream input) throws IOException {
            return (MysqlxPrepare.Prepare.OneOfMessage)GeneratedMessageV3.parseWithIOException(PARSER, input);
         }

         public static MysqlxPrepare.Prepare.OneOfMessage parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (MysqlxPrepare.Prepare.OneOfMessage)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
         }

         public MysqlxPrepare.Prepare.OneOfMessage.Builder newBuilderForType() {
            return newBuilder();
         }

         public static MysqlxPrepare.Prepare.OneOfMessage.Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
         }

         public static MysqlxPrepare.Prepare.OneOfMessage.Builder newBuilder(MysqlxPrepare.Prepare.OneOfMessage prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
         }

         public MysqlxPrepare.Prepare.OneOfMessage.Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new MysqlxPrepare.Prepare.OneOfMessage.Builder() : (new MysqlxPrepare.Prepare.OneOfMessage.Builder()).mergeFrom(this);
         }

         protected MysqlxPrepare.Prepare.OneOfMessage.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            MysqlxPrepare.Prepare.OneOfMessage.Builder builder = new MysqlxPrepare.Prepare.OneOfMessage.Builder(parent);
            return builder;
         }

         public static MysqlxPrepare.Prepare.OneOfMessage getDefaultInstance() {
            return DEFAULT_INSTANCE;
         }

         public static Parser<MysqlxPrepare.Prepare.OneOfMessage> parser() {
            return PARSER;
         }

         public Parser<MysqlxPrepare.Prepare.OneOfMessage> getParserForType() {
            return PARSER;
         }

         public MysqlxPrepare.Prepare.OneOfMessage getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
         }

         // $FF: synthetic method
         OneOfMessage(GeneratedMessageV3.Builder x0, Object x1) {
            this(x0);
         }

         public static final class Builder extends GeneratedMessageV3.Builder<MysqlxPrepare.Prepare.OneOfMessage.Builder> implements MysqlxPrepare.Prepare.OneOfMessageOrBuilder {
            private int bitField0_;
            private int type_;
            private MysqlxCrud.Find find_;
            private SingleFieldBuilderV3<MysqlxCrud.Find, MysqlxCrud.Find.Builder, MysqlxCrud.FindOrBuilder> findBuilder_;
            private MysqlxCrud.Insert insert_;
            private SingleFieldBuilderV3<MysqlxCrud.Insert, MysqlxCrud.Insert.Builder, MysqlxCrud.InsertOrBuilder> insertBuilder_;
            private MysqlxCrud.Update update_;
            private SingleFieldBuilderV3<MysqlxCrud.Update, MysqlxCrud.Update.Builder, MysqlxCrud.UpdateOrBuilder> updateBuilder_;
            private MysqlxCrud.Delete delete_;
            private SingleFieldBuilderV3<MysqlxCrud.Delete, MysqlxCrud.Delete.Builder, MysqlxCrud.DeleteOrBuilder> deleteBuilder_;
            private MysqlxSql.StmtExecute stmtExecute_;
            private SingleFieldBuilderV3<MysqlxSql.StmtExecute, MysqlxSql.StmtExecute.Builder, MysqlxSql.StmtExecuteOrBuilder> stmtExecuteBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
               return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_descriptor;
            }

            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
               return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_fieldAccessorTable.ensureFieldAccessorsInitialized(MysqlxPrepare.Prepare.OneOfMessage.class, MysqlxPrepare.Prepare.OneOfMessage.Builder.class);
            }

            private Builder() {
               this.type_ = 0;
               this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
               super(parent);
               this.type_ = 0;
               this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
               if (MysqlxPrepare.Prepare.OneOfMessage.alwaysUseFieldBuilders) {
                  this.getFindFieldBuilder();
                  this.getInsertFieldBuilder();
                  this.getUpdateFieldBuilder();
                  this.getDeleteFieldBuilder();
                  this.getStmtExecuteFieldBuilder();
               }

            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clear() {
               super.clear();
               this.type_ = 0;
               this.bitField0_ &= -2;
               if (this.findBuilder_ == null) {
                  this.find_ = null;
               } else {
                  this.findBuilder_.clear();
               }

               this.bitField0_ &= -3;
               if (this.insertBuilder_ == null) {
                  this.insert_ = null;
               } else {
                  this.insertBuilder_.clear();
               }

               this.bitField0_ &= -5;
               if (this.updateBuilder_ == null) {
                  this.update_ = null;
               } else {
                  this.updateBuilder_.clear();
               }

               this.bitField0_ &= -9;
               if (this.deleteBuilder_ == null) {
                  this.delete_ = null;
               } else {
                  this.deleteBuilder_.clear();
               }

               this.bitField0_ &= -17;
               if (this.stmtExecuteBuilder_ == null) {
                  this.stmtExecute_ = null;
               } else {
                  this.stmtExecuteBuilder_.clear();
               }

               this.bitField0_ &= -33;
               return this;
            }

            public Descriptors.Descriptor getDescriptorForType() {
               return MysqlxPrepare.internal_static_Mysqlx_Prepare_Prepare_OneOfMessage_descriptor;
            }

            public MysqlxPrepare.Prepare.OneOfMessage getDefaultInstanceForType() {
               return MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance();
            }

            public MysqlxPrepare.Prepare.OneOfMessage build() {
               MysqlxPrepare.Prepare.OneOfMessage result = this.buildPartial();
               if (!result.isInitialized()) {
                  throw newUninitializedMessageException(result);
               } else {
                  return result;
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage buildPartial() {
               MysqlxPrepare.Prepare.OneOfMessage result = new MysqlxPrepare.Prepare.OneOfMessage(this);
               int from_bitField0_ = this.bitField0_;
               int to_bitField0_ = 0;
               if ((from_bitField0_ & 1) != 0) {
                  to_bitField0_ |= 1;
               }

               result.type_ = this.type_;
               if ((from_bitField0_ & 2) != 0) {
                  if (this.findBuilder_ == null) {
                     result.find_ = this.find_;
                  } else {
                     result.find_ = (MysqlxCrud.Find)this.findBuilder_.build();
                  }

                  to_bitField0_ |= 2;
               }

               if ((from_bitField0_ & 4) != 0) {
                  if (this.insertBuilder_ == null) {
                     result.insert_ = this.insert_;
                  } else {
                     result.insert_ = (MysqlxCrud.Insert)this.insertBuilder_.build();
                  }

                  to_bitField0_ |= 4;
               }

               if ((from_bitField0_ & 8) != 0) {
                  if (this.updateBuilder_ == null) {
                     result.update_ = this.update_;
                  } else {
                     result.update_ = (MysqlxCrud.Update)this.updateBuilder_.build();
                  }

                  to_bitField0_ |= 8;
               }

               if ((from_bitField0_ & 16) != 0) {
                  if (this.deleteBuilder_ == null) {
                     result.delete_ = this.delete_;
                  } else {
                     result.delete_ = (MysqlxCrud.Delete)this.deleteBuilder_.build();
                  }

                  to_bitField0_ |= 16;
               }

               if ((from_bitField0_ & 32) != 0) {
                  if (this.stmtExecuteBuilder_ == null) {
                     result.stmtExecute_ = this.stmtExecute_;
                  } else {
                     result.stmtExecute_ = (MysqlxSql.StmtExecute)this.stmtExecuteBuilder_.build();
                  }

                  to_bitField0_ |= 32;
               }

               result.bitField0_ = to_bitField0_;
               this.onBuilt();
               return result;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clone() {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.clone();
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setField(Descriptors.FieldDescriptor field, Object value) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.setField(field, value);
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearField(Descriptors.FieldDescriptor field) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.clearField(field);
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.clearOneof(oneof);
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.setRepeatedField(field, index, value);
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.addRepeatedField(field, value);
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeFrom(Message other) {
               if (other instanceof MysqlxPrepare.Prepare.OneOfMessage) {
                  return this.mergeFrom((MysqlxPrepare.Prepare.OneOfMessage)other);
               } else {
                  super.mergeFrom(other);
                  return this;
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeFrom(MysqlxPrepare.Prepare.OneOfMessage other) {
               if (other == MysqlxPrepare.Prepare.OneOfMessage.getDefaultInstance()) {
                  return this;
               } else {
                  if (other.hasType()) {
                     this.setType(other.getType());
                  }

                  if (other.hasFind()) {
                     this.mergeFind(other.getFind());
                  }

                  if (other.hasInsert()) {
                     this.mergeInsert(other.getInsert());
                  }

                  if (other.hasUpdate()) {
                     this.mergeUpdate(other.getUpdate());
                  }

                  if (other.hasDelete()) {
                     this.mergeDelete(other.getDelete());
                  }

                  if (other.hasStmtExecute()) {
                     this.mergeStmtExecute(other.getStmtExecute());
                  }

                  this.mergeUnknownFields(other.getUnknownFields());
                  this.onChanged();
                  return this;
               }
            }

            public final boolean isInitialized() {
               if (!this.hasType()) {
                  return false;
               } else if (this.hasFind() && !this.getFind().isInitialized()) {
                  return false;
               } else if (this.hasInsert() && !this.getInsert().isInitialized()) {
                  return false;
               } else if (this.hasUpdate() && !this.getUpdate().isInitialized()) {
                  return false;
               } else if (this.hasDelete() && !this.getDelete().isInitialized()) {
                  return false;
               } else {
                  return !this.hasStmtExecute() || this.getStmtExecute().isInitialized();
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
               if (extensionRegistry == null) {
                  throw new NullPointerException();
               } else {
                  try {
                     boolean done = false;

                     while(!done) {
                        int tag = input.readTag();
                        switch(tag) {
                        case 0:
                           done = true;
                           break;
                        case 8:
                           int tmpRaw = input.readEnum();
                           MysqlxPrepare.Prepare.OneOfMessage.Type tmpValue = MysqlxPrepare.Prepare.OneOfMessage.Type.forNumber(tmpRaw);
                           if (tmpValue == null) {
                              this.mergeUnknownVarintField(1, tmpRaw);
                           } else {
                              this.type_ = tmpRaw;
                              this.bitField0_ |= 1;
                           }
                           break;
                        case 18:
                           input.readMessage((MessageLite.Builder)this.getFindFieldBuilder().getBuilder(), extensionRegistry);
                           this.bitField0_ |= 2;
                           break;
                        case 26:
                           input.readMessage((MessageLite.Builder)this.getInsertFieldBuilder().getBuilder(), extensionRegistry);
                           this.bitField0_ |= 4;
                           break;
                        case 34:
                           input.readMessage((MessageLite.Builder)this.getUpdateFieldBuilder().getBuilder(), extensionRegistry);
                           this.bitField0_ |= 8;
                           break;
                        case 42:
                           input.readMessage((MessageLite.Builder)this.getDeleteFieldBuilder().getBuilder(), extensionRegistry);
                           this.bitField0_ |= 16;
                           break;
                        case 50:
                           input.readMessage((MessageLite.Builder)this.getStmtExecuteFieldBuilder().getBuilder(), extensionRegistry);
                           this.bitField0_ |= 32;
                           break;
                        default:
                           if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                              done = true;
                           }
                        }
                     }
                  } catch (InvalidProtocolBufferException var10) {
                     throw var10.unwrapIOException();
                  } finally {
                     this.onChanged();
                  }

                  return this;
               }
            }

            public boolean hasType() {
               return (this.bitField0_ & 1) != 0;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Type getType() {
               MysqlxPrepare.Prepare.OneOfMessage.Type result = MysqlxPrepare.Prepare.OneOfMessage.Type.valueOf(this.type_);
               return result == null ? MysqlxPrepare.Prepare.OneOfMessage.Type.FIND : result;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setType(MysqlxPrepare.Prepare.OneOfMessage.Type value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 1;
                  this.type_ = value.getNumber();
                  this.onChanged();
                  return this;
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearType() {
               this.bitField0_ &= -2;
               this.type_ = 0;
               this.onChanged();
               return this;
            }

            public boolean hasFind() {
               return (this.bitField0_ & 2) != 0;
            }

            public MysqlxCrud.Find getFind() {
               if (this.findBuilder_ == null) {
                  return this.find_ == null ? MysqlxCrud.Find.getDefaultInstance() : this.find_;
               } else {
                  return (MysqlxCrud.Find)this.findBuilder_.getMessage();
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setFind(MysqlxCrud.Find value) {
               if (this.findBuilder_ == null) {
                  if (value == null) {
                     throw new NullPointerException();
                  }

                  this.find_ = value;
                  this.onChanged();
               } else {
                  this.findBuilder_.setMessage(value);
               }

               this.bitField0_ |= 2;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setFind(MysqlxCrud.Find.Builder builderForValue) {
               if (this.findBuilder_ == null) {
                  this.find_ = builderForValue.build();
                  this.onChanged();
               } else {
                  this.findBuilder_.setMessage(builderForValue.build());
               }

               this.bitField0_ |= 2;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeFind(MysqlxCrud.Find value) {
               if (this.findBuilder_ == null) {
                  if ((this.bitField0_ & 2) != 0 && this.find_ != null && this.find_ != MysqlxCrud.Find.getDefaultInstance()) {
                     this.find_ = MysqlxCrud.Find.newBuilder(this.find_).mergeFrom(value).buildPartial();
                  } else {
                     this.find_ = value;
                  }

                  this.onChanged();
               } else {
                  this.findBuilder_.mergeFrom(value);
               }

               this.bitField0_ |= 2;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearFind() {
               if (this.findBuilder_ == null) {
                  this.find_ = null;
                  this.onChanged();
               } else {
                  this.findBuilder_.clear();
               }

               this.bitField0_ &= -3;
               return this;
            }

            public MysqlxCrud.Find.Builder getFindBuilder() {
               this.bitField0_ |= 2;
               this.onChanged();
               return (MysqlxCrud.Find.Builder)this.getFindFieldBuilder().getBuilder();
            }

            public MysqlxCrud.FindOrBuilder getFindOrBuilder() {
               if (this.findBuilder_ != null) {
                  return (MysqlxCrud.FindOrBuilder)this.findBuilder_.getMessageOrBuilder();
               } else {
                  return this.find_ == null ? MysqlxCrud.Find.getDefaultInstance() : this.find_;
               }
            }

            private SingleFieldBuilderV3<MysqlxCrud.Find, MysqlxCrud.Find.Builder, MysqlxCrud.FindOrBuilder> getFindFieldBuilder() {
               if (this.findBuilder_ == null) {
                  this.findBuilder_ = new SingleFieldBuilderV3(this.getFind(), this.getParentForChildren(), this.isClean());
                  this.find_ = null;
               }

               return this.findBuilder_;
            }

            public boolean hasInsert() {
               return (this.bitField0_ & 4) != 0;
            }

            public MysqlxCrud.Insert getInsert() {
               if (this.insertBuilder_ == null) {
                  return this.insert_ == null ? MysqlxCrud.Insert.getDefaultInstance() : this.insert_;
               } else {
                  return (MysqlxCrud.Insert)this.insertBuilder_.getMessage();
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setInsert(MysqlxCrud.Insert value) {
               if (this.insertBuilder_ == null) {
                  if (value == null) {
                     throw new NullPointerException();
                  }

                  this.insert_ = value;
                  this.onChanged();
               } else {
                  this.insertBuilder_.setMessage(value);
               }

               this.bitField0_ |= 4;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setInsert(MysqlxCrud.Insert.Builder builderForValue) {
               if (this.insertBuilder_ == null) {
                  this.insert_ = builderForValue.build();
                  this.onChanged();
               } else {
                  this.insertBuilder_.setMessage(builderForValue.build());
               }

               this.bitField0_ |= 4;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeInsert(MysqlxCrud.Insert value) {
               if (this.insertBuilder_ == null) {
                  if ((this.bitField0_ & 4) != 0 && this.insert_ != null && this.insert_ != MysqlxCrud.Insert.getDefaultInstance()) {
                     this.insert_ = MysqlxCrud.Insert.newBuilder(this.insert_).mergeFrom(value).buildPartial();
                  } else {
                     this.insert_ = value;
                  }

                  this.onChanged();
               } else {
                  this.insertBuilder_.mergeFrom(value);
               }

               this.bitField0_ |= 4;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearInsert() {
               if (this.insertBuilder_ == null) {
                  this.insert_ = null;
                  this.onChanged();
               } else {
                  this.insertBuilder_.clear();
               }

               this.bitField0_ &= -5;
               return this;
            }

            public MysqlxCrud.Insert.Builder getInsertBuilder() {
               this.bitField0_ |= 4;
               this.onChanged();
               return (MysqlxCrud.Insert.Builder)this.getInsertFieldBuilder().getBuilder();
            }

            public MysqlxCrud.InsertOrBuilder getInsertOrBuilder() {
               if (this.insertBuilder_ != null) {
                  return (MysqlxCrud.InsertOrBuilder)this.insertBuilder_.getMessageOrBuilder();
               } else {
                  return this.insert_ == null ? MysqlxCrud.Insert.getDefaultInstance() : this.insert_;
               }
            }

            private SingleFieldBuilderV3<MysqlxCrud.Insert, MysqlxCrud.Insert.Builder, MysqlxCrud.InsertOrBuilder> getInsertFieldBuilder() {
               if (this.insertBuilder_ == null) {
                  this.insertBuilder_ = new SingleFieldBuilderV3(this.getInsert(), this.getParentForChildren(), this.isClean());
                  this.insert_ = null;
               }

               return this.insertBuilder_;
            }

            public boolean hasUpdate() {
               return (this.bitField0_ & 8) != 0;
            }

            public MysqlxCrud.Update getUpdate() {
               if (this.updateBuilder_ == null) {
                  return this.update_ == null ? MysqlxCrud.Update.getDefaultInstance() : this.update_;
               } else {
                  return (MysqlxCrud.Update)this.updateBuilder_.getMessage();
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setUpdate(MysqlxCrud.Update value) {
               if (this.updateBuilder_ == null) {
                  if (value == null) {
                     throw new NullPointerException();
                  }

                  this.update_ = value;
                  this.onChanged();
               } else {
                  this.updateBuilder_.setMessage(value);
               }

               this.bitField0_ |= 8;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setUpdate(MysqlxCrud.Update.Builder builderForValue) {
               if (this.updateBuilder_ == null) {
                  this.update_ = builderForValue.build();
                  this.onChanged();
               } else {
                  this.updateBuilder_.setMessage(builderForValue.build());
               }

               this.bitField0_ |= 8;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeUpdate(MysqlxCrud.Update value) {
               if (this.updateBuilder_ == null) {
                  if ((this.bitField0_ & 8) != 0 && this.update_ != null && this.update_ != MysqlxCrud.Update.getDefaultInstance()) {
                     this.update_ = MysqlxCrud.Update.newBuilder(this.update_).mergeFrom(value).buildPartial();
                  } else {
                     this.update_ = value;
                  }

                  this.onChanged();
               } else {
                  this.updateBuilder_.mergeFrom(value);
               }

               this.bitField0_ |= 8;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearUpdate() {
               if (this.updateBuilder_ == null) {
                  this.update_ = null;
                  this.onChanged();
               } else {
                  this.updateBuilder_.clear();
               }

               this.bitField0_ &= -9;
               return this;
            }

            public MysqlxCrud.Update.Builder getUpdateBuilder() {
               this.bitField0_ |= 8;
               this.onChanged();
               return (MysqlxCrud.Update.Builder)this.getUpdateFieldBuilder().getBuilder();
            }

            public MysqlxCrud.UpdateOrBuilder getUpdateOrBuilder() {
               if (this.updateBuilder_ != null) {
                  return (MysqlxCrud.UpdateOrBuilder)this.updateBuilder_.getMessageOrBuilder();
               } else {
                  return this.update_ == null ? MysqlxCrud.Update.getDefaultInstance() : this.update_;
               }
            }

            private SingleFieldBuilderV3<MysqlxCrud.Update, MysqlxCrud.Update.Builder, MysqlxCrud.UpdateOrBuilder> getUpdateFieldBuilder() {
               if (this.updateBuilder_ == null) {
                  this.updateBuilder_ = new SingleFieldBuilderV3(this.getUpdate(), this.getParentForChildren(), this.isClean());
                  this.update_ = null;
               }

               return this.updateBuilder_;
            }

            public boolean hasDelete() {
               return (this.bitField0_ & 16) != 0;
            }

            public MysqlxCrud.Delete getDelete() {
               if (this.deleteBuilder_ == null) {
                  return this.delete_ == null ? MysqlxCrud.Delete.getDefaultInstance() : this.delete_;
               } else {
                  return (MysqlxCrud.Delete)this.deleteBuilder_.getMessage();
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setDelete(MysqlxCrud.Delete value) {
               if (this.deleteBuilder_ == null) {
                  if (value == null) {
                     throw new NullPointerException();
                  }

                  this.delete_ = value;
                  this.onChanged();
               } else {
                  this.deleteBuilder_.setMessage(value);
               }

               this.bitField0_ |= 16;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setDelete(MysqlxCrud.Delete.Builder builderForValue) {
               if (this.deleteBuilder_ == null) {
                  this.delete_ = builderForValue.build();
                  this.onChanged();
               } else {
                  this.deleteBuilder_.setMessage(builderForValue.build());
               }

               this.bitField0_ |= 16;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeDelete(MysqlxCrud.Delete value) {
               if (this.deleteBuilder_ == null) {
                  if ((this.bitField0_ & 16) != 0 && this.delete_ != null && this.delete_ != MysqlxCrud.Delete.getDefaultInstance()) {
                     this.delete_ = MysqlxCrud.Delete.newBuilder(this.delete_).mergeFrom(value).buildPartial();
                  } else {
                     this.delete_ = value;
                  }

                  this.onChanged();
               } else {
                  this.deleteBuilder_.mergeFrom(value);
               }

               this.bitField0_ |= 16;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearDelete() {
               if (this.deleteBuilder_ == null) {
                  this.delete_ = null;
                  this.onChanged();
               } else {
                  this.deleteBuilder_.clear();
               }

               this.bitField0_ &= -17;
               return this;
            }

            public MysqlxCrud.Delete.Builder getDeleteBuilder() {
               this.bitField0_ |= 16;
               this.onChanged();
               return (MysqlxCrud.Delete.Builder)this.getDeleteFieldBuilder().getBuilder();
            }

            public MysqlxCrud.DeleteOrBuilder getDeleteOrBuilder() {
               if (this.deleteBuilder_ != null) {
                  return (MysqlxCrud.DeleteOrBuilder)this.deleteBuilder_.getMessageOrBuilder();
               } else {
                  return this.delete_ == null ? MysqlxCrud.Delete.getDefaultInstance() : this.delete_;
               }
            }

            private SingleFieldBuilderV3<MysqlxCrud.Delete, MysqlxCrud.Delete.Builder, MysqlxCrud.DeleteOrBuilder> getDeleteFieldBuilder() {
               if (this.deleteBuilder_ == null) {
                  this.deleteBuilder_ = new SingleFieldBuilderV3(this.getDelete(), this.getParentForChildren(), this.isClean());
                  this.delete_ = null;
               }

               return this.deleteBuilder_;
            }

            public boolean hasStmtExecute() {
               return (this.bitField0_ & 32) != 0;
            }

            public MysqlxSql.StmtExecute getStmtExecute() {
               if (this.stmtExecuteBuilder_ == null) {
                  return this.stmtExecute_ == null ? MysqlxSql.StmtExecute.getDefaultInstance() : this.stmtExecute_;
               } else {
                  return (MysqlxSql.StmtExecute)this.stmtExecuteBuilder_.getMessage();
               }
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setStmtExecute(MysqlxSql.StmtExecute value) {
               if (this.stmtExecuteBuilder_ == null) {
                  if (value == null) {
                     throw new NullPointerException();
                  }

                  this.stmtExecute_ = value;
                  this.onChanged();
               } else {
                  this.stmtExecuteBuilder_.setMessage(value);
               }

               this.bitField0_ |= 32;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder setStmtExecute(MysqlxSql.StmtExecute.Builder builderForValue) {
               if (this.stmtExecuteBuilder_ == null) {
                  this.stmtExecute_ = builderForValue.build();
                  this.onChanged();
               } else {
                  this.stmtExecuteBuilder_.setMessage(builderForValue.build());
               }

               this.bitField0_ |= 32;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder mergeStmtExecute(MysqlxSql.StmtExecute value) {
               if (this.stmtExecuteBuilder_ == null) {
                  if ((this.bitField0_ & 32) != 0 && this.stmtExecute_ != null && this.stmtExecute_ != MysqlxSql.StmtExecute.getDefaultInstance()) {
                     this.stmtExecute_ = MysqlxSql.StmtExecute.newBuilder(this.stmtExecute_).mergeFrom(value).buildPartial();
                  } else {
                     this.stmtExecute_ = value;
                  }

                  this.onChanged();
               } else {
                  this.stmtExecuteBuilder_.mergeFrom(value);
               }

               this.bitField0_ |= 32;
               return this;
            }

            public MysqlxPrepare.Prepare.OneOfMessage.Builder clearStmtExecute() {
               if (this.stmtExecuteBuilder_ == null) {
                  this.stmtExecute_ = null;
                  this.onChanged();
               } else {
                  this.stmtExecuteBuilder_.clear();
               }

               this.bitField0_ &= -33;
               return this;
            }

            public MysqlxSql.StmtExecute.Builder getStmtExecuteBuilder() {
               this.bitField0_ |= 32;
               this.onChanged();
               return (MysqlxSql.StmtExecute.Builder)this.getStmtExecuteFieldBuilder().getBuilder();
            }

            public MysqlxSql.StmtExecuteOrBuilder getStmtExecuteOrBuilder() {
               if (this.stmtExecuteBuilder_ != null) {
                  return (MysqlxSql.StmtExecuteOrBuilder)this.stmtExecuteBuilder_.getMessageOrBuilder();
               } else {
                  return this.stmtExecute_ == null ? MysqlxSql.StmtExecute.getDefaultInstance() : this.stmtExecute_;
               }
            }

            private SingleFieldBuilderV3<MysqlxSql.StmtExecute, MysqlxSql.StmtExecute.Builder, MysqlxSql.StmtExecuteOrBuilder> getStmtExecuteFieldBuilder() {
               if (this.stmtExecuteBuilder_ == null) {
                  this.stmtExecuteBuilder_ = new SingleFieldBuilderV3(this.getStmtExecute(), this.getParentForChildren(), this.isClean());
                  this.stmtExecute_ = null;
               }

               return this.stmtExecuteBuilder_;
            }

            public final MysqlxPrepare.Prepare.OneOfMessage.Builder setUnknownFields(UnknownFieldSet unknownFields) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.setUnknownFields(unknownFields);
            }

            public final MysqlxPrepare.Prepare.OneOfMessage.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
               return (MysqlxPrepare.Prepare.OneOfMessage.Builder)super.mergeUnknownFields(unknownFields);
            }

            // $FF: synthetic method
            Builder(Object x0) {
               this();
            }

            // $FF: synthetic method
            Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
               this(x0);
            }
         }

         public static enum Type implements ProtocolMessageEnum {
            FIND(0),
            INSERT(1),
            UPDATE(2),
            DELETE(4),
            STMT(5);

            public static final int FIND_VALUE = 0;
            public static final int INSERT_VALUE = 1;
            public static final int UPDATE_VALUE = 2;
            public static final int DELETE_VALUE = 4;
            public static final int STMT_VALUE = 5;
            private static final Internal.EnumLiteMap<MysqlxPrepare.Prepare.OneOfMessage.Type> internalValueMap = new Internal.EnumLiteMap<MysqlxPrepare.Prepare.OneOfMessage.Type>() {
               public MysqlxPrepare.Prepare.OneOfMessage.Type findValueByNumber(int number) {
                  return MysqlxPrepare.Prepare.OneOfMessage.Type.forNumber(number);
               }
            };
            private static final MysqlxPrepare.Prepare.OneOfMessage.Type[] VALUES = values();
            private final int value;

            public final int getNumber() {
               return this.value;
            }

            /** @deprecated */
            @Deprecated
            public static MysqlxPrepare.Prepare.OneOfMessage.Type valueOf(int value) {
               return forNumber(value);
            }

            public static MysqlxPrepare.Prepare.OneOfMessage.Type forNumber(int value) {
               switch(value) {
               case 0:
                  return FIND;
               case 1:
                  return INSERT;
               case 2:
                  return UPDATE;
               case 3:
               default:
                  return null;
               case 4:
                  return DELETE;
               case 5:
                  return STMT;
               }
            }

            public static Internal.EnumLiteMap<MysqlxPrepare.Prepare.OneOfMessage.Type> internalGetValueMap() {
               return internalValueMap;
            }

            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
               return (Descriptors.EnumValueDescriptor)getDescriptor().getValues().get(this.ordinal());
            }

            public final Descriptors.EnumDescriptor getDescriptorForType() {
               return getDescriptor();
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
               return (Descriptors.EnumDescriptor)MysqlxPrepare.Prepare.OneOfMessage.getDescriptor().getEnumTypes().get(0);
            }

            public static MysqlxPrepare.Prepare.OneOfMessage.Type valueOf(Descriptors.EnumValueDescriptor desc) {
               if (desc.getType() != getDescriptor()) {
                  throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
               } else {
                  return VALUES[desc.getIndex()];
               }
            }

            private Type(int value) {
               this.value = value;
            }
         }
      }

      public interface OneOfMessageOrBuilder extends MessageOrBuilder {
         boolean hasType();

         MysqlxPrepare.Prepare.OneOfMessage.Type getType();

         boolean hasFind();

         MysqlxCrud.Find getFind();

         MysqlxCrud.FindOrBuilder getFindOrBuilder();

         boolean hasInsert();

         MysqlxCrud.Insert getInsert();

         MysqlxCrud.InsertOrBuilder getInsertOrBuilder();

         boolean hasUpdate();

         MysqlxCrud.Update getUpdate();

         MysqlxCrud.UpdateOrBuilder getUpdateOrBuilder();

         boolean hasDelete();

         MysqlxCrud.Delete getDelete();

         MysqlxCrud.DeleteOrBuilder getDeleteOrBuilder();

         boolean hasStmtExecute();

         MysqlxSql.StmtExecute getStmtExecute();

         MysqlxSql.StmtExecuteOrBuilder getStmtExecuteOrBuilder();
      }
   }

   public interface PrepareOrBuilder extends MessageOrBuilder {
      boolean hasStmtId();

      int getStmtId();

      boolean hasStmt();

      MysqlxPrepare.Prepare.OneOfMessage getStmt();

      MysqlxPrepare.Prepare.OneOfMessageOrBuilder getStmtOrBuilder();
   }
}
