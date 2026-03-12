package fr.xephi.authme.libs.com.google.protobuf.compiler;

import fr.xephi.authme.libs.com.google.protobuf.AbstractMessageLite;
import fr.xephi.authme.libs.com.google.protobuf.AbstractParser;
import fr.xephi.authme.libs.com.google.protobuf.ByteString;
import fr.xephi.authme.libs.com.google.protobuf.CodedInputStream;
import fr.xephi.authme.libs.com.google.protobuf.CodedOutputStream;
import fr.xephi.authme.libs.com.google.protobuf.DescriptorProtos;
import fr.xephi.authme.libs.com.google.protobuf.Descriptors;
import fr.xephi.authme.libs.com.google.protobuf.ExtensionRegistry;
import fr.xephi.authme.libs.com.google.protobuf.ExtensionRegistryLite;
import fr.xephi.authme.libs.com.google.protobuf.GeneratedMessageV3;
import fr.xephi.authme.libs.com.google.protobuf.Internal;
import fr.xephi.authme.libs.com.google.protobuf.InvalidProtocolBufferException;
import fr.xephi.authme.libs.com.google.protobuf.LazyStringArrayList;
import fr.xephi.authme.libs.com.google.protobuf.LazyStringList;
import fr.xephi.authme.libs.com.google.protobuf.Message;
import fr.xephi.authme.libs.com.google.protobuf.MessageLite;
import fr.xephi.authme.libs.com.google.protobuf.MessageOrBuilder;
import fr.xephi.authme.libs.com.google.protobuf.Parser;
import fr.xephi.authme.libs.com.google.protobuf.ProtocolMessageEnum;
import fr.xephi.authme.libs.com.google.protobuf.ProtocolStringList;
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

public final class PluginProtos {
   private static final Descriptors.Descriptor internal_static_google_protobuf_compiler_Version_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_google_protobuf_compiler_Version_fieldAccessorTable;
   private static final Descriptors.Descriptor internal_static_google_protobuf_compiler_CodeGeneratorRequest_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_google_protobuf_compiler_CodeGeneratorRequest_fieldAccessorTable;
   private static final Descriptors.Descriptor internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_google_protobuf_compiler_CodeGeneratorResponse_fieldAccessorTable;
   private static final Descriptors.Descriptor internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_fieldAccessorTable;
   private static Descriptors.FileDescriptor descriptor;

   private PluginProtos() {
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
      String[] descriptorData = new String[]{"\n%google/protobuf/compiler/plugin.proto\u0012\u0018google.protobuf.compiler\u001a google/protobuf/descriptor.proto\"F\n\u0007Version\u0012\r\n\u0005major\u0018\u0001 \u0001(\u0005\u0012\r\n\u0005minor\u0018\u0002 \u0001(\u0005\u0012\r\n\u0005patch\u0018\u0003 \u0001(\u0005\u0012\u000e\n\u0006suffix\u0018\u0004 \u0001(\t\"º\u0001\n\u0014CodeGeneratorRequest\u0012\u0018\n\u0010file_to_generate\u0018\u0001 \u0003(\t\u0012\u0011\n\tparameter\u0018\u0002 \u0001(\t\u00128\n\nproto_file\u0018\u000f \u0003(\u000b2$.google.protobuf.FileDescriptorProto\u0012;\n\u0010compiler_version\u0018\u0003 \u0001(\u000b2!.google.protobuf.compiler.Version\"Á\u0002\n\u0015CodeGeneratorResponse\u0012\r\n\u0005error\u0018\u0001 \u0001(\t\u0012\u001a\n\u0012supported_features\u0018\u0002 \u0001(\u0004\u0012B\n\u0004file\u0018\u000f \u0003(\u000b24.google.protobuf.compiler.CodeGeneratorResponse.File\u001a\u007f\n\u0004File\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u0017\n\u000finsertion_point\u0018\u0002 \u0001(\t\u0012\u000f\n\u0007content\u0018\u000f \u0001(\t\u0012?\n\u0013generated_code_info\u0018\u0010 \u0001(\u000b2\".google.protobuf.GeneratedCodeInfo\"8\n\u0007Feature\u0012\u0010\n\fFEATURE_NONE\u0010\u0000\u0012\u001b\n\u0017FEATURE_PROTO3_OPTIONAL\u0010\u0001BW\n\u001ccom.google.protobuf.compilerB\fPluginProtosZ)google.golang.org/protobuf/types/pluginpb"};
      descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[]{DescriptorProtos.getDescriptor()});
      internal_static_google_protobuf_compiler_Version_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(0);
      internal_static_google_protobuf_compiler_Version_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_google_protobuf_compiler_Version_descriptor, new String[]{"Major", "Minor", "Patch", "Suffix"});
      internal_static_google_protobuf_compiler_CodeGeneratorRequest_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(1);
      internal_static_google_protobuf_compiler_CodeGeneratorRequest_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_google_protobuf_compiler_CodeGeneratorRequest_descriptor, new String[]{"FileToGenerate", "Parameter", "ProtoFile", "CompilerVersion"});
      internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(2);
      internal_static_google_protobuf_compiler_CodeGeneratorResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor, new String[]{"Error", "SupportedFeatures", "File"});
      internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_descriptor = (Descriptors.Descriptor)internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor.getNestedTypes().get(0);
      internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_descriptor, new String[]{"Name", "InsertionPoint", "Content", "GeneratedCodeInfo"});
      DescriptorProtos.getDescriptor();
   }

   public static final class CodeGeneratorResponse extends GeneratedMessageV3 implements PluginProtos.CodeGeneratorResponseOrBuilder {
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      public static final int ERROR_FIELD_NUMBER = 1;
      private volatile Object error_;
      public static final int SUPPORTED_FEATURES_FIELD_NUMBER = 2;
      private long supportedFeatures_;
      public static final int FILE_FIELD_NUMBER = 15;
      private List<PluginProtos.CodeGeneratorResponse.File> file_;
      private byte memoizedIsInitialized;
      private static final PluginProtos.CodeGeneratorResponse DEFAULT_INSTANCE = new PluginProtos.CodeGeneratorResponse();
      /** @deprecated */
      @Deprecated
      public static final Parser<PluginProtos.CodeGeneratorResponse> PARSER = new AbstractParser<PluginProtos.CodeGeneratorResponse>() {
         public PluginProtos.CodeGeneratorResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            PluginProtos.CodeGeneratorResponse.Builder builder = PluginProtos.CodeGeneratorResponse.newBuilder();

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

      private CodeGeneratorResponse(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private CodeGeneratorResponse() {
         this.memoizedIsInitialized = -1;
         this.error_ = "";
         this.file_ = Collections.emptyList();
      }

      protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
         return new PluginProtos.CodeGeneratorResponse();
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.CodeGeneratorResponse.class, PluginProtos.CodeGeneratorResponse.Builder.class);
      }

      public boolean hasError() {
         return (this.bitField0_ & 1) != 0;
      }

      public String getError() {
         Object ref = this.error_;
         if (ref instanceof String) {
            return (String)ref;
         } else {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
               this.error_ = s;
            }

            return s;
         }
      }

      public ByteString getErrorBytes() {
         Object ref = this.error_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.error_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public boolean hasSupportedFeatures() {
         return (this.bitField0_ & 2) != 0;
      }

      public long getSupportedFeatures() {
         return this.supportedFeatures_;
      }

      public List<PluginProtos.CodeGeneratorResponse.File> getFileList() {
         return this.file_;
      }

      public List<? extends PluginProtos.CodeGeneratorResponse.FileOrBuilder> getFileOrBuilderList() {
         return this.file_;
      }

      public int getFileCount() {
         return this.file_.size();
      }

      public PluginProtos.CodeGeneratorResponse.File getFile(int index) {
         return (PluginProtos.CodeGeneratorResponse.File)this.file_.get(index);
      }

      public PluginProtos.CodeGeneratorResponse.FileOrBuilder getFileOrBuilder(int index) {
         return (PluginProtos.CodeGeneratorResponse.FileOrBuilder)this.file_.get(index);
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         if ((this.bitField0_ & 1) != 0) {
            GeneratedMessageV3.writeString(output, 1, this.error_);
         }

         if ((this.bitField0_ & 2) != 0) {
            output.writeUInt64(2, this.supportedFeatures_);
         }

         for(int i = 0; i < this.file_.size(); ++i) {
            output.writeMessage(15, (MessageLite)this.file_.get(i));
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
               size += GeneratedMessageV3.computeStringSize(1, this.error_);
            }

            if ((this.bitField0_ & 2) != 0) {
               size += CodedOutputStream.computeUInt64Size(2, this.supportedFeatures_);
            }

            for(int i = 0; i < this.file_.size(); ++i) {
               size += CodedOutputStream.computeMessageSize(15, (MessageLite)this.file_.get(i));
            }

            size += this.getUnknownFields().getSerializedSize();
            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof PluginProtos.CodeGeneratorResponse)) {
            return super.equals(obj);
         } else {
            PluginProtos.CodeGeneratorResponse other = (PluginProtos.CodeGeneratorResponse)obj;
            if (this.hasError() != other.hasError()) {
               return false;
            } else if (this.hasError() && !this.getError().equals(other.getError())) {
               return false;
            } else if (this.hasSupportedFeatures() != other.hasSupportedFeatures()) {
               return false;
            } else if (this.hasSupportedFeatures() && this.getSupportedFeatures() != other.getSupportedFeatures()) {
               return false;
            } else if (!this.getFileList().equals(other.getFileList())) {
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
            if (this.hasError()) {
               hash = 37 * hash + 1;
               hash = 53 * hash + this.getError().hashCode();
            }

            if (this.hasSupportedFeatures()) {
               hash = 37 * hash + 2;
               hash = 53 * hash + Internal.hashLong(this.getSupportedFeatures());
            }

            if (this.getFileCount() > 0) {
               hash = 37 * hash + 15;
               hash = 53 * hash + this.getFileList().hashCode();
            }

            hash = 29 * hash + this.getUnknownFields().hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorResponse)PARSER.parseFrom(data);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorResponse)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorResponse)PARSER.parseFrom(data);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorResponse)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorResponse)PARSER.parseFrom(data);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorResponse)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(InputStream input) throws IOException {
         return (PluginProtos.CodeGeneratorResponse)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.CodeGeneratorResponse)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorResponse parseDelimitedFrom(InputStream input) throws IOException {
         return (PluginProtos.CodeGeneratorResponse)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static PluginProtos.CodeGeneratorResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.CodeGeneratorResponse)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(CodedInputStream input) throws IOException {
         return (PluginProtos.CodeGeneratorResponse)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static PluginProtos.CodeGeneratorResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.CodeGeneratorResponse)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public PluginProtos.CodeGeneratorResponse.Builder newBuilderForType() {
         return newBuilder();
      }

      public static PluginProtos.CodeGeneratorResponse.Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static PluginProtos.CodeGeneratorResponse.Builder newBuilder(PluginProtos.CodeGeneratorResponse prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public PluginProtos.CodeGeneratorResponse.Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new PluginProtos.CodeGeneratorResponse.Builder() : (new PluginProtos.CodeGeneratorResponse.Builder()).mergeFrom(this);
      }

      protected PluginProtos.CodeGeneratorResponse.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         PluginProtos.CodeGeneratorResponse.Builder builder = new PluginProtos.CodeGeneratorResponse.Builder(parent);
         return builder;
      }

      public static PluginProtos.CodeGeneratorResponse getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<PluginProtos.CodeGeneratorResponse> parser() {
         return PARSER;
      }

      public Parser<PluginProtos.CodeGeneratorResponse> getParserForType() {
         return PARSER;
      }

      public PluginProtos.CodeGeneratorResponse getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      CodeGeneratorResponse(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<PluginProtos.CodeGeneratorResponse.Builder> implements PluginProtos.CodeGeneratorResponseOrBuilder {
         private int bitField0_;
         private Object error_;
         private long supportedFeatures_;
         private List<PluginProtos.CodeGeneratorResponse.File> file_;
         private RepeatedFieldBuilderV3<PluginProtos.CodeGeneratorResponse.File, PluginProtos.CodeGeneratorResponse.File.Builder, PluginProtos.CodeGeneratorResponse.FileOrBuilder> fileBuilder_;

         public static final Descriptors.Descriptor getDescriptor() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.CodeGeneratorResponse.class, PluginProtos.CodeGeneratorResponse.Builder.class);
         }

         private Builder() {
            this.error_ = "";
            this.file_ = Collections.emptyList();
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.error_ = "";
            this.file_ = Collections.emptyList();
         }

         public PluginProtos.CodeGeneratorResponse.Builder clear() {
            super.clear();
            this.error_ = "";
            this.bitField0_ &= -2;
            this.supportedFeatures_ = 0L;
            this.bitField0_ &= -3;
            if (this.fileBuilder_ == null) {
               this.file_ = Collections.emptyList();
            } else {
               this.file_ = null;
               this.fileBuilder_.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_descriptor;
         }

         public PluginProtos.CodeGeneratorResponse getDefaultInstanceForType() {
            return PluginProtos.CodeGeneratorResponse.getDefaultInstance();
         }

         public PluginProtos.CodeGeneratorResponse build() {
            PluginProtos.CodeGeneratorResponse result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public PluginProtos.CodeGeneratorResponse buildPartial() {
            PluginProtos.CodeGeneratorResponse result = new PluginProtos.CodeGeneratorResponse(this);
            int from_bitField0_ = this.bitField0_;
            int to_bitField0_ = 0;
            if ((from_bitField0_ & 1) != 0) {
               to_bitField0_ |= 1;
            }

            result.error_ = this.error_;
            if ((from_bitField0_ & 2) != 0) {
               result.supportedFeatures_ = this.supportedFeatures_;
               to_bitField0_ |= 2;
            }

            if (this.fileBuilder_ == null) {
               if ((this.bitField0_ & 4) != 0) {
                  this.file_ = Collections.unmodifiableList(this.file_);
                  this.bitField0_ &= -5;
               }

               result.file_ = this.file_;
            } else {
               result.file_ = this.fileBuilder_.build();
            }

            result.bitField0_ = to_bitField0_;
            this.onBuilt();
            return result;
         }

         public PluginProtos.CodeGeneratorResponse.Builder clone() {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.clone();
         }

         public PluginProtos.CodeGeneratorResponse.Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.setField(field, value);
         }

         public PluginProtos.CodeGeneratorResponse.Builder clearField(Descriptors.FieldDescriptor field) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.clearField(field);
         }

         public PluginProtos.CodeGeneratorResponse.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.clearOneof(oneof);
         }

         public PluginProtos.CodeGeneratorResponse.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.setRepeatedField(field, index, value);
         }

         public PluginProtos.CodeGeneratorResponse.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.addRepeatedField(field, value);
         }

         public PluginProtos.CodeGeneratorResponse.Builder mergeFrom(Message other) {
            if (other instanceof PluginProtos.CodeGeneratorResponse) {
               return this.mergeFrom((PluginProtos.CodeGeneratorResponse)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public PluginProtos.CodeGeneratorResponse.Builder mergeFrom(PluginProtos.CodeGeneratorResponse other) {
            if (other == PluginProtos.CodeGeneratorResponse.getDefaultInstance()) {
               return this;
            } else {
               if (other.hasError()) {
                  this.bitField0_ |= 1;
                  this.error_ = other.error_;
                  this.onChanged();
               }

               if (other.hasSupportedFeatures()) {
                  this.setSupportedFeatures(other.getSupportedFeatures());
               }

               if (this.fileBuilder_ == null) {
                  if (!other.file_.isEmpty()) {
                     if (this.file_.isEmpty()) {
                        this.file_ = other.file_;
                        this.bitField0_ &= -5;
                     } else {
                        this.ensureFileIsMutable();
                        this.file_.addAll(other.file_);
                     }

                     this.onChanged();
                  }
               } else if (!other.file_.isEmpty()) {
                  if (this.fileBuilder_.isEmpty()) {
                     this.fileBuilder_.dispose();
                     this.fileBuilder_ = null;
                     this.file_ = other.file_;
                     this.bitField0_ &= -5;
                     this.fileBuilder_ = PluginProtos.CodeGeneratorResponse.alwaysUseFieldBuilders ? this.getFileFieldBuilder() : null;
                  } else {
                     this.fileBuilder_.addAllMessages(other.file_);
                  }
               }

               this.mergeUnknownFields(other.getUnknownFields());
               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            return true;
         }

         public PluginProtos.CodeGeneratorResponse.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                     case 10:
                        this.error_ = input.readBytes();
                        this.bitField0_ |= 1;
                        break;
                     case 16:
                        this.supportedFeatures_ = input.readUInt64();
                        this.bitField0_ |= 2;
                        break;
                     case 122:
                        PluginProtos.CodeGeneratorResponse.File m = (PluginProtos.CodeGeneratorResponse.File)input.readMessage(PluginProtos.CodeGeneratorResponse.File.PARSER, extensionRegistry);
                        if (this.fileBuilder_ == null) {
                           this.ensureFileIsMutable();
                           this.file_.add(m);
                        } else {
                           this.fileBuilder_.addMessage(m);
                        }
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

         public boolean hasError() {
            return (this.bitField0_ & 1) != 0;
         }

         public String getError() {
            Object ref = this.error_;
            if (!(ref instanceof String)) {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               if (bs.isValidUtf8()) {
                  this.error_ = s;
               }

               return s;
            } else {
               return (String)ref;
            }
         }

         public ByteString getErrorBytes() {
            Object ref = this.error_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.error_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public PluginProtos.CodeGeneratorResponse.Builder setError(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.bitField0_ |= 1;
               this.error_ = value;
               this.onChanged();
               return this;
            }
         }

         public PluginProtos.CodeGeneratorResponse.Builder clearError() {
            this.bitField0_ &= -2;
            this.error_ = PluginProtos.CodeGeneratorResponse.getDefaultInstance().getError();
            this.onChanged();
            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder setErrorBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.bitField0_ |= 1;
               this.error_ = value;
               this.onChanged();
               return this;
            }
         }

         public boolean hasSupportedFeatures() {
            return (this.bitField0_ & 2) != 0;
         }

         public long getSupportedFeatures() {
            return this.supportedFeatures_;
         }

         public PluginProtos.CodeGeneratorResponse.Builder setSupportedFeatures(long value) {
            this.bitField0_ |= 2;
            this.supportedFeatures_ = value;
            this.onChanged();
            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder clearSupportedFeatures() {
            this.bitField0_ &= -3;
            this.supportedFeatures_ = 0L;
            this.onChanged();
            return this;
         }

         private void ensureFileIsMutable() {
            if ((this.bitField0_ & 4) == 0) {
               this.file_ = new ArrayList(this.file_);
               this.bitField0_ |= 4;
            }

         }

         public List<PluginProtos.CodeGeneratorResponse.File> getFileList() {
            return this.fileBuilder_ == null ? Collections.unmodifiableList(this.file_) : this.fileBuilder_.getMessageList();
         }

         public int getFileCount() {
            return this.fileBuilder_ == null ? this.file_.size() : this.fileBuilder_.getCount();
         }

         public PluginProtos.CodeGeneratorResponse.File getFile(int index) {
            return this.fileBuilder_ == null ? (PluginProtos.CodeGeneratorResponse.File)this.file_.get(index) : (PluginProtos.CodeGeneratorResponse.File)this.fileBuilder_.getMessage(index);
         }

         public PluginProtos.CodeGeneratorResponse.Builder setFile(int index, PluginProtos.CodeGeneratorResponse.File value) {
            if (this.fileBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureFileIsMutable();
               this.file_.set(index, value);
               this.onChanged();
            } else {
               this.fileBuilder_.setMessage(index, value);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder setFile(int index, PluginProtos.CodeGeneratorResponse.File.Builder builderForValue) {
            if (this.fileBuilder_ == null) {
               this.ensureFileIsMutable();
               this.file_.set(index, builderForValue.build());
               this.onChanged();
            } else {
               this.fileBuilder_.setMessage(index, builderForValue.build());
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder addFile(PluginProtos.CodeGeneratorResponse.File value) {
            if (this.fileBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureFileIsMutable();
               this.file_.add(value);
               this.onChanged();
            } else {
               this.fileBuilder_.addMessage(value);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder addFile(int index, PluginProtos.CodeGeneratorResponse.File value) {
            if (this.fileBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureFileIsMutable();
               this.file_.add(index, value);
               this.onChanged();
            } else {
               this.fileBuilder_.addMessage(index, value);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder addFile(PluginProtos.CodeGeneratorResponse.File.Builder builderForValue) {
            if (this.fileBuilder_ == null) {
               this.ensureFileIsMutable();
               this.file_.add(builderForValue.build());
               this.onChanged();
            } else {
               this.fileBuilder_.addMessage(builderForValue.build());
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder addFile(int index, PluginProtos.CodeGeneratorResponse.File.Builder builderForValue) {
            if (this.fileBuilder_ == null) {
               this.ensureFileIsMutable();
               this.file_.add(index, builderForValue.build());
               this.onChanged();
            } else {
               this.fileBuilder_.addMessage(index, builderForValue.build());
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder addAllFile(Iterable<? extends PluginProtos.CodeGeneratorResponse.File> values) {
            if (this.fileBuilder_ == null) {
               this.ensureFileIsMutable();
               AbstractMessageLite.Builder.addAll(values, this.file_);
               this.onChanged();
            } else {
               this.fileBuilder_.addAllMessages(values);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder clearFile() {
            if (this.fileBuilder_ == null) {
               this.file_ = Collections.emptyList();
               this.bitField0_ &= -5;
               this.onChanged();
            } else {
               this.fileBuilder_.clear();
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.Builder removeFile(int index) {
            if (this.fileBuilder_ == null) {
               this.ensureFileIsMutable();
               this.file_.remove(index);
               this.onChanged();
            } else {
               this.fileBuilder_.remove(index);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorResponse.File.Builder getFileBuilder(int index) {
            return (PluginProtos.CodeGeneratorResponse.File.Builder)this.getFileFieldBuilder().getBuilder(index);
         }

         public PluginProtos.CodeGeneratorResponse.FileOrBuilder getFileOrBuilder(int index) {
            return this.fileBuilder_ == null ? (PluginProtos.CodeGeneratorResponse.FileOrBuilder)this.file_.get(index) : (PluginProtos.CodeGeneratorResponse.FileOrBuilder)this.fileBuilder_.getMessageOrBuilder(index);
         }

         public List<? extends PluginProtos.CodeGeneratorResponse.FileOrBuilder> getFileOrBuilderList() {
            return this.fileBuilder_ != null ? this.fileBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.file_);
         }

         public PluginProtos.CodeGeneratorResponse.File.Builder addFileBuilder() {
            return (PluginProtos.CodeGeneratorResponse.File.Builder)this.getFileFieldBuilder().addBuilder(PluginProtos.CodeGeneratorResponse.File.getDefaultInstance());
         }

         public PluginProtos.CodeGeneratorResponse.File.Builder addFileBuilder(int index) {
            return (PluginProtos.CodeGeneratorResponse.File.Builder)this.getFileFieldBuilder().addBuilder(index, PluginProtos.CodeGeneratorResponse.File.getDefaultInstance());
         }

         public List<PluginProtos.CodeGeneratorResponse.File.Builder> getFileBuilderList() {
            return this.getFileFieldBuilder().getBuilderList();
         }

         private RepeatedFieldBuilderV3<PluginProtos.CodeGeneratorResponse.File, PluginProtos.CodeGeneratorResponse.File.Builder, PluginProtos.CodeGeneratorResponse.FileOrBuilder> getFileFieldBuilder() {
            if (this.fileBuilder_ == null) {
               this.fileBuilder_ = new RepeatedFieldBuilderV3(this.file_, (this.bitField0_ & 4) != 0, this.getParentForChildren(), this.isClean());
               this.file_ = null;
            }

            return this.fileBuilder_;
         }

         public final PluginProtos.CodeGeneratorResponse.Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.setUnknownFields(unknownFields);
         }

         public final PluginProtos.CodeGeneratorResponse.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (PluginProtos.CodeGeneratorResponse.Builder)super.mergeUnknownFields(unknownFields);
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

      public static final class File extends GeneratedMessageV3 implements PluginProtos.CodeGeneratorResponse.FileOrBuilder {
         private static final long serialVersionUID = 0L;
         private int bitField0_;
         public static final int NAME_FIELD_NUMBER = 1;
         private volatile Object name_;
         public static final int INSERTION_POINT_FIELD_NUMBER = 2;
         private volatile Object insertionPoint_;
         public static final int CONTENT_FIELD_NUMBER = 15;
         private volatile Object content_;
         public static final int GENERATED_CODE_INFO_FIELD_NUMBER = 16;
         private DescriptorProtos.GeneratedCodeInfo generatedCodeInfo_;
         private byte memoizedIsInitialized;
         private static final PluginProtos.CodeGeneratorResponse.File DEFAULT_INSTANCE = new PluginProtos.CodeGeneratorResponse.File();
         /** @deprecated */
         @Deprecated
         public static final Parser<PluginProtos.CodeGeneratorResponse.File> PARSER = new AbstractParser<PluginProtos.CodeGeneratorResponse.File>() {
            public PluginProtos.CodeGeneratorResponse.File parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
               PluginProtos.CodeGeneratorResponse.File.Builder builder = PluginProtos.CodeGeneratorResponse.File.newBuilder();

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

         private File(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
         }

         private File() {
            this.memoizedIsInitialized = -1;
            this.name_ = "";
            this.insertionPoint_ = "";
            this.content_ = "";
         }

         protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new PluginProtos.CodeGeneratorResponse.File();
         }

         public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.CodeGeneratorResponse.File.class, PluginProtos.CodeGeneratorResponse.File.Builder.class);
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) != 0;
         }

         public String getName() {
            Object ref = this.name_;
            if (ref instanceof String) {
               return (String)ref;
            } else {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               if (bs.isValidUtf8()) {
                  this.name_ = s;
               }

               return s;
            }
         }

         public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.name_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public boolean hasInsertionPoint() {
            return (this.bitField0_ & 2) != 0;
         }

         public String getInsertionPoint() {
            Object ref = this.insertionPoint_;
            if (ref instanceof String) {
               return (String)ref;
            } else {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               if (bs.isValidUtf8()) {
                  this.insertionPoint_ = s;
               }

               return s;
            }
         }

         public ByteString getInsertionPointBytes() {
            Object ref = this.insertionPoint_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.insertionPoint_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public boolean hasContent() {
            return (this.bitField0_ & 4) != 0;
         }

         public String getContent() {
            Object ref = this.content_;
            if (ref instanceof String) {
               return (String)ref;
            } else {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               if (bs.isValidUtf8()) {
                  this.content_ = s;
               }

               return s;
            }
         }

         public ByteString getContentBytes() {
            Object ref = this.content_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.content_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public boolean hasGeneratedCodeInfo() {
            return (this.bitField0_ & 8) != 0;
         }

         public DescriptorProtos.GeneratedCodeInfo getGeneratedCodeInfo() {
            return this.generatedCodeInfo_ == null ? DescriptorProtos.GeneratedCodeInfo.getDefaultInstance() : this.generatedCodeInfo_;
         }

         public DescriptorProtos.GeneratedCodeInfoOrBuilder getGeneratedCodeInfoOrBuilder() {
            return this.generatedCodeInfo_ == null ? DescriptorProtos.GeneratedCodeInfo.getDefaultInstance() : this.generatedCodeInfo_;
         }

         public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
               return true;
            } else if (isInitialized == 0) {
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }

         public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) != 0) {
               GeneratedMessageV3.writeString(output, 1, this.name_);
            }

            if ((this.bitField0_ & 2) != 0) {
               GeneratedMessageV3.writeString(output, 2, this.insertionPoint_);
            }

            if ((this.bitField0_ & 4) != 0) {
               GeneratedMessageV3.writeString(output, 15, this.content_);
            }

            if ((this.bitField0_ & 8) != 0) {
               output.writeMessage(16, this.getGeneratedCodeInfo());
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
                  size += GeneratedMessageV3.computeStringSize(1, this.name_);
               }

               if ((this.bitField0_ & 2) != 0) {
                  size += GeneratedMessageV3.computeStringSize(2, this.insertionPoint_);
               }

               if ((this.bitField0_ & 4) != 0) {
                  size += GeneratedMessageV3.computeStringSize(15, this.content_);
               }

               if ((this.bitField0_ & 8) != 0) {
                  size += CodedOutputStream.computeMessageSize(16, this.getGeneratedCodeInfo());
               }

               size += this.getUnknownFields().getSerializedSize();
               this.memoizedSize = size;
               return size;
            }
         }

         public boolean equals(Object obj) {
            if (obj == this) {
               return true;
            } else if (!(obj instanceof PluginProtos.CodeGeneratorResponse.File)) {
               return super.equals(obj);
            } else {
               PluginProtos.CodeGeneratorResponse.File other = (PluginProtos.CodeGeneratorResponse.File)obj;
               if (this.hasName() != other.hasName()) {
                  return false;
               } else if (this.hasName() && !this.getName().equals(other.getName())) {
                  return false;
               } else if (this.hasInsertionPoint() != other.hasInsertionPoint()) {
                  return false;
               } else if (this.hasInsertionPoint() && !this.getInsertionPoint().equals(other.getInsertionPoint())) {
                  return false;
               } else if (this.hasContent() != other.hasContent()) {
                  return false;
               } else if (this.hasContent() && !this.getContent().equals(other.getContent())) {
                  return false;
               } else if (this.hasGeneratedCodeInfo() != other.hasGeneratedCodeInfo()) {
                  return false;
               } else if (this.hasGeneratedCodeInfo() && !this.getGeneratedCodeInfo().equals(other.getGeneratedCodeInfo())) {
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
               if (this.hasName()) {
                  hash = 37 * hash + 1;
                  hash = 53 * hash + this.getName().hashCode();
               }

               if (this.hasInsertionPoint()) {
                  hash = 37 * hash + 2;
                  hash = 53 * hash + this.getInsertionPoint().hashCode();
               }

               if (this.hasContent()) {
                  hash = 37 * hash + 15;
                  hash = 53 * hash + this.getContent().hashCode();
               }

               if (this.hasGeneratedCodeInfo()) {
                  hash = 37 * hash + 16;
                  hash = 53 * hash + this.getGeneratedCodeInfo().hashCode();
               }

               hash = 29 * hash + this.getUnknownFields().hashCode();
               this.memoizedHashCode = hash;
               return hash;
            }
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return (PluginProtos.CodeGeneratorResponse.File)PARSER.parseFrom(data);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (PluginProtos.CodeGeneratorResponse.File)PARSER.parseFrom(data, extensionRegistry);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (PluginProtos.CodeGeneratorResponse.File)PARSER.parseFrom(data);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (PluginProtos.CodeGeneratorResponse.File)PARSER.parseFrom(data, extensionRegistry);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (PluginProtos.CodeGeneratorResponse.File)PARSER.parseFrom(data);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (PluginProtos.CodeGeneratorResponse.File)PARSER.parseFrom(data, extensionRegistry);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(InputStream input) throws IOException {
            return (PluginProtos.CodeGeneratorResponse.File)GeneratedMessageV3.parseWithIOException(PARSER, input);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (PluginProtos.CodeGeneratorResponse.File)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseDelimitedFrom(InputStream input) throws IOException {
            return (PluginProtos.CodeGeneratorResponse.File)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (PluginProtos.CodeGeneratorResponse.File)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(CodedInputStream input) throws IOException {
            return (PluginProtos.CodeGeneratorResponse.File)GeneratedMessageV3.parseWithIOException(PARSER, input);
         }

         public static PluginProtos.CodeGeneratorResponse.File parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (PluginProtos.CodeGeneratorResponse.File)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
         }

         public PluginProtos.CodeGeneratorResponse.File.Builder newBuilderForType() {
            return newBuilder();
         }

         public static PluginProtos.CodeGeneratorResponse.File.Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
         }

         public static PluginProtos.CodeGeneratorResponse.File.Builder newBuilder(PluginProtos.CodeGeneratorResponse.File prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
         }

         public PluginProtos.CodeGeneratorResponse.File.Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new PluginProtos.CodeGeneratorResponse.File.Builder() : (new PluginProtos.CodeGeneratorResponse.File.Builder()).mergeFrom(this);
         }

         protected PluginProtos.CodeGeneratorResponse.File.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            PluginProtos.CodeGeneratorResponse.File.Builder builder = new PluginProtos.CodeGeneratorResponse.File.Builder(parent);
            return builder;
         }

         public static PluginProtos.CodeGeneratorResponse.File getDefaultInstance() {
            return DEFAULT_INSTANCE;
         }

         public static Parser<PluginProtos.CodeGeneratorResponse.File> parser() {
            return PARSER;
         }

         public Parser<PluginProtos.CodeGeneratorResponse.File> getParserForType() {
            return PARSER;
         }

         public PluginProtos.CodeGeneratorResponse.File getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
         }

         // $FF: synthetic method
         File(GeneratedMessageV3.Builder x0, Object x1) {
            this(x0);
         }

         public static final class Builder extends GeneratedMessageV3.Builder<PluginProtos.CodeGeneratorResponse.File.Builder> implements PluginProtos.CodeGeneratorResponse.FileOrBuilder {
            private int bitField0_;
            private Object name_;
            private Object insertionPoint_;
            private Object content_;
            private DescriptorProtos.GeneratedCodeInfo generatedCodeInfo_;
            private SingleFieldBuilderV3<DescriptorProtos.GeneratedCodeInfo, DescriptorProtos.GeneratedCodeInfo.Builder, DescriptorProtos.GeneratedCodeInfoOrBuilder> generatedCodeInfoBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
               return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_descriptor;
            }

            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
               return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.CodeGeneratorResponse.File.class, PluginProtos.CodeGeneratorResponse.File.Builder.class);
            }

            private Builder() {
               this.name_ = "";
               this.insertionPoint_ = "";
               this.content_ = "";
               this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
               super(parent);
               this.name_ = "";
               this.insertionPoint_ = "";
               this.content_ = "";
               this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
               if (PluginProtos.CodeGeneratorResponse.File.alwaysUseFieldBuilders) {
                  this.getGeneratedCodeInfoFieldBuilder();
               }

            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clear() {
               super.clear();
               this.name_ = "";
               this.bitField0_ &= -2;
               this.insertionPoint_ = "";
               this.bitField0_ &= -3;
               this.content_ = "";
               this.bitField0_ &= -5;
               if (this.generatedCodeInfoBuilder_ == null) {
                  this.generatedCodeInfo_ = null;
               } else {
                  this.generatedCodeInfoBuilder_.clear();
               }

               this.bitField0_ &= -9;
               return this;
            }

            public Descriptors.Descriptor getDescriptorForType() {
               return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorResponse_File_descriptor;
            }

            public PluginProtos.CodeGeneratorResponse.File getDefaultInstanceForType() {
               return PluginProtos.CodeGeneratorResponse.File.getDefaultInstance();
            }

            public PluginProtos.CodeGeneratorResponse.File build() {
               PluginProtos.CodeGeneratorResponse.File result = this.buildPartial();
               if (!result.isInitialized()) {
                  throw newUninitializedMessageException(result);
               } else {
                  return result;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File buildPartial() {
               PluginProtos.CodeGeneratorResponse.File result = new PluginProtos.CodeGeneratorResponse.File(this);
               int from_bitField0_ = this.bitField0_;
               int to_bitField0_ = 0;
               if ((from_bitField0_ & 1) != 0) {
                  to_bitField0_ |= 1;
               }

               result.name_ = this.name_;
               if ((from_bitField0_ & 2) != 0) {
                  to_bitField0_ |= 2;
               }

               result.insertionPoint_ = this.insertionPoint_;
               if ((from_bitField0_ & 4) != 0) {
                  to_bitField0_ |= 4;
               }

               result.content_ = this.content_;
               if ((from_bitField0_ & 8) != 0) {
                  if (this.generatedCodeInfoBuilder_ == null) {
                     result.generatedCodeInfo_ = this.generatedCodeInfo_;
                  } else {
                     result.generatedCodeInfo_ = (DescriptorProtos.GeneratedCodeInfo)this.generatedCodeInfoBuilder_.build();
                  }

                  to_bitField0_ |= 8;
               }

               result.bitField0_ = to_bitField0_;
               this.onBuilt();
               return result;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clone() {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.clone();
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setField(Descriptors.FieldDescriptor field, Object value) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.setField(field, value);
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clearField(Descriptors.FieldDescriptor field) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.clearField(field);
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.clearOneof(oneof);
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.setRepeatedField(field, index, value);
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.addRepeatedField(field, value);
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder mergeFrom(Message other) {
               if (other instanceof PluginProtos.CodeGeneratorResponse.File) {
                  return this.mergeFrom((PluginProtos.CodeGeneratorResponse.File)other);
               } else {
                  super.mergeFrom(other);
                  return this;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder mergeFrom(PluginProtos.CodeGeneratorResponse.File other) {
               if (other == PluginProtos.CodeGeneratorResponse.File.getDefaultInstance()) {
                  return this;
               } else {
                  if (other.hasName()) {
                     this.bitField0_ |= 1;
                     this.name_ = other.name_;
                     this.onChanged();
                  }

                  if (other.hasInsertionPoint()) {
                     this.bitField0_ |= 2;
                     this.insertionPoint_ = other.insertionPoint_;
                     this.onChanged();
                  }

                  if (other.hasContent()) {
                     this.bitField0_ |= 4;
                     this.content_ = other.content_;
                     this.onChanged();
                  }

                  if (other.hasGeneratedCodeInfo()) {
                     this.mergeGeneratedCodeInfo(other.getGeneratedCodeInfo());
                  }

                  this.mergeUnknownFields(other.getUnknownFields());
                  this.onChanged();
                  return this;
               }
            }

            public final boolean isInitialized() {
               return true;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                        case 10:
                           this.name_ = input.readBytes();
                           this.bitField0_ |= 1;
                           break;
                        case 18:
                           this.insertionPoint_ = input.readBytes();
                           this.bitField0_ |= 2;
                           break;
                        case 122:
                           this.content_ = input.readBytes();
                           this.bitField0_ |= 4;
                           break;
                        case 130:
                           input.readMessage((MessageLite.Builder)this.getGeneratedCodeInfoFieldBuilder().getBuilder(), extensionRegistry);
                           this.bitField0_ |= 8;
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

            public boolean hasName() {
               return (this.bitField0_ & 1) != 0;
            }

            public String getName() {
               Object ref = this.name_;
               if (!(ref instanceof String)) {
                  ByteString bs = (ByteString)ref;
                  String s = bs.toStringUtf8();
                  if (bs.isValidUtf8()) {
                     this.name_ = s;
                  }

                  return s;
               } else {
                  return (String)ref;
               }
            }

            public ByteString getNameBytes() {
               Object ref = this.name_;
               if (ref instanceof String) {
                  ByteString b = ByteString.copyFromUtf8((String)ref);
                  this.name_ = b;
                  return b;
               } else {
                  return (ByteString)ref;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setName(String value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 1;
                  this.name_ = value;
                  this.onChanged();
                  return this;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clearName() {
               this.bitField0_ &= -2;
               this.name_ = PluginProtos.CodeGeneratorResponse.File.getDefaultInstance().getName();
               this.onChanged();
               return this;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setNameBytes(ByteString value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 1;
                  this.name_ = value;
                  this.onChanged();
                  return this;
               }
            }

            public boolean hasInsertionPoint() {
               return (this.bitField0_ & 2) != 0;
            }

            public String getInsertionPoint() {
               Object ref = this.insertionPoint_;
               if (!(ref instanceof String)) {
                  ByteString bs = (ByteString)ref;
                  String s = bs.toStringUtf8();
                  if (bs.isValidUtf8()) {
                     this.insertionPoint_ = s;
                  }

                  return s;
               } else {
                  return (String)ref;
               }
            }

            public ByteString getInsertionPointBytes() {
               Object ref = this.insertionPoint_;
               if (ref instanceof String) {
                  ByteString b = ByteString.copyFromUtf8((String)ref);
                  this.insertionPoint_ = b;
                  return b;
               } else {
                  return (ByteString)ref;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setInsertionPoint(String value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 2;
                  this.insertionPoint_ = value;
                  this.onChanged();
                  return this;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clearInsertionPoint() {
               this.bitField0_ &= -3;
               this.insertionPoint_ = PluginProtos.CodeGeneratorResponse.File.getDefaultInstance().getInsertionPoint();
               this.onChanged();
               return this;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setInsertionPointBytes(ByteString value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 2;
                  this.insertionPoint_ = value;
                  this.onChanged();
                  return this;
               }
            }

            public boolean hasContent() {
               return (this.bitField0_ & 4) != 0;
            }

            public String getContent() {
               Object ref = this.content_;
               if (!(ref instanceof String)) {
                  ByteString bs = (ByteString)ref;
                  String s = bs.toStringUtf8();
                  if (bs.isValidUtf8()) {
                     this.content_ = s;
                  }

                  return s;
               } else {
                  return (String)ref;
               }
            }

            public ByteString getContentBytes() {
               Object ref = this.content_;
               if (ref instanceof String) {
                  ByteString b = ByteString.copyFromUtf8((String)ref);
                  this.content_ = b;
                  return b;
               } else {
                  return (ByteString)ref;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setContent(String value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 4;
                  this.content_ = value;
                  this.onChanged();
                  return this;
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clearContent() {
               this.bitField0_ &= -5;
               this.content_ = PluginProtos.CodeGeneratorResponse.File.getDefaultInstance().getContent();
               this.onChanged();
               return this;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setContentBytes(ByteString value) {
               if (value == null) {
                  throw new NullPointerException();
               } else {
                  this.bitField0_ |= 4;
                  this.content_ = value;
                  this.onChanged();
                  return this;
               }
            }

            public boolean hasGeneratedCodeInfo() {
               return (this.bitField0_ & 8) != 0;
            }

            public DescriptorProtos.GeneratedCodeInfo getGeneratedCodeInfo() {
               if (this.generatedCodeInfoBuilder_ == null) {
                  return this.generatedCodeInfo_ == null ? DescriptorProtos.GeneratedCodeInfo.getDefaultInstance() : this.generatedCodeInfo_;
               } else {
                  return (DescriptorProtos.GeneratedCodeInfo)this.generatedCodeInfoBuilder_.getMessage();
               }
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setGeneratedCodeInfo(DescriptorProtos.GeneratedCodeInfo value) {
               if (this.generatedCodeInfoBuilder_ == null) {
                  if (value == null) {
                     throw new NullPointerException();
                  }

                  this.generatedCodeInfo_ = value;
                  this.onChanged();
               } else {
                  this.generatedCodeInfoBuilder_.setMessage(value);
               }

               this.bitField0_ |= 8;
               return this;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder setGeneratedCodeInfo(DescriptorProtos.GeneratedCodeInfo.Builder builderForValue) {
               if (this.generatedCodeInfoBuilder_ == null) {
                  this.generatedCodeInfo_ = builderForValue.build();
                  this.onChanged();
               } else {
                  this.generatedCodeInfoBuilder_.setMessage(builderForValue.build());
               }

               this.bitField0_ |= 8;
               return this;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder mergeGeneratedCodeInfo(DescriptorProtos.GeneratedCodeInfo value) {
               if (this.generatedCodeInfoBuilder_ == null) {
                  if ((this.bitField0_ & 8) != 0 && this.generatedCodeInfo_ != null && this.generatedCodeInfo_ != DescriptorProtos.GeneratedCodeInfo.getDefaultInstance()) {
                     this.generatedCodeInfo_ = DescriptorProtos.GeneratedCodeInfo.newBuilder(this.generatedCodeInfo_).mergeFrom(value).buildPartial();
                  } else {
                     this.generatedCodeInfo_ = value;
                  }

                  this.onChanged();
               } else {
                  this.generatedCodeInfoBuilder_.mergeFrom(value);
               }

               this.bitField0_ |= 8;
               return this;
            }

            public PluginProtos.CodeGeneratorResponse.File.Builder clearGeneratedCodeInfo() {
               if (this.generatedCodeInfoBuilder_ == null) {
                  this.generatedCodeInfo_ = null;
                  this.onChanged();
               } else {
                  this.generatedCodeInfoBuilder_.clear();
               }

               this.bitField0_ &= -9;
               return this;
            }

            public DescriptorProtos.GeneratedCodeInfo.Builder getGeneratedCodeInfoBuilder() {
               this.bitField0_ |= 8;
               this.onChanged();
               return (DescriptorProtos.GeneratedCodeInfo.Builder)this.getGeneratedCodeInfoFieldBuilder().getBuilder();
            }

            public DescriptorProtos.GeneratedCodeInfoOrBuilder getGeneratedCodeInfoOrBuilder() {
               if (this.generatedCodeInfoBuilder_ != null) {
                  return (DescriptorProtos.GeneratedCodeInfoOrBuilder)this.generatedCodeInfoBuilder_.getMessageOrBuilder();
               } else {
                  return this.generatedCodeInfo_ == null ? DescriptorProtos.GeneratedCodeInfo.getDefaultInstance() : this.generatedCodeInfo_;
               }
            }

            private SingleFieldBuilderV3<DescriptorProtos.GeneratedCodeInfo, DescriptorProtos.GeneratedCodeInfo.Builder, DescriptorProtos.GeneratedCodeInfoOrBuilder> getGeneratedCodeInfoFieldBuilder() {
               if (this.generatedCodeInfoBuilder_ == null) {
                  this.generatedCodeInfoBuilder_ = new SingleFieldBuilderV3(this.getGeneratedCodeInfo(), this.getParentForChildren(), this.isClean());
                  this.generatedCodeInfo_ = null;
               }

               return this.generatedCodeInfoBuilder_;
            }

            public final PluginProtos.CodeGeneratorResponse.File.Builder setUnknownFields(UnknownFieldSet unknownFields) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.setUnknownFields(unknownFields);
            }

            public final PluginProtos.CodeGeneratorResponse.File.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
               return (PluginProtos.CodeGeneratorResponse.File.Builder)super.mergeUnknownFields(unknownFields);
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

      public interface FileOrBuilder extends MessageOrBuilder {
         boolean hasName();

         String getName();

         ByteString getNameBytes();

         boolean hasInsertionPoint();

         String getInsertionPoint();

         ByteString getInsertionPointBytes();

         boolean hasContent();

         String getContent();

         ByteString getContentBytes();

         boolean hasGeneratedCodeInfo();

         DescriptorProtos.GeneratedCodeInfo getGeneratedCodeInfo();

         DescriptorProtos.GeneratedCodeInfoOrBuilder getGeneratedCodeInfoOrBuilder();
      }

      public static enum Feature implements ProtocolMessageEnum {
         FEATURE_NONE(0),
         FEATURE_PROTO3_OPTIONAL(1);

         public static final int FEATURE_NONE_VALUE = 0;
         public static final int FEATURE_PROTO3_OPTIONAL_VALUE = 1;
         private static final Internal.EnumLiteMap<PluginProtos.CodeGeneratorResponse.Feature> internalValueMap = new Internal.EnumLiteMap<PluginProtos.CodeGeneratorResponse.Feature>() {
            public PluginProtos.CodeGeneratorResponse.Feature findValueByNumber(int number) {
               return PluginProtos.CodeGeneratorResponse.Feature.forNumber(number);
            }
         };
         private static final PluginProtos.CodeGeneratorResponse.Feature[] VALUES = values();
         private final int value;

         public final int getNumber() {
            return this.value;
         }

         /** @deprecated */
         @Deprecated
         public static PluginProtos.CodeGeneratorResponse.Feature valueOf(int value) {
            return forNumber(value);
         }

         public static PluginProtos.CodeGeneratorResponse.Feature forNumber(int value) {
            switch(value) {
            case 0:
               return FEATURE_NONE;
            case 1:
               return FEATURE_PROTO3_OPTIONAL;
            default:
               return null;
            }
         }

         public static Internal.EnumLiteMap<PluginProtos.CodeGeneratorResponse.Feature> internalGetValueMap() {
            return internalValueMap;
         }

         public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return (Descriptors.EnumValueDescriptor)getDescriptor().getValues().get(this.ordinal());
         }

         public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
         }

         public static final Descriptors.EnumDescriptor getDescriptor() {
            return (Descriptors.EnumDescriptor)PluginProtos.CodeGeneratorResponse.getDescriptor().getEnumTypes().get(0);
         }

         public static PluginProtos.CodeGeneratorResponse.Feature valueOf(Descriptors.EnumValueDescriptor desc) {
            if (desc.getType() != getDescriptor()) {
               throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            } else {
               return VALUES[desc.getIndex()];
            }
         }

         private Feature(int value) {
            this.value = value;
         }
      }
   }

   public interface CodeGeneratorResponseOrBuilder extends MessageOrBuilder {
      boolean hasError();

      String getError();

      ByteString getErrorBytes();

      boolean hasSupportedFeatures();

      long getSupportedFeatures();

      List<PluginProtos.CodeGeneratorResponse.File> getFileList();

      PluginProtos.CodeGeneratorResponse.File getFile(int var1);

      int getFileCount();

      List<? extends PluginProtos.CodeGeneratorResponse.FileOrBuilder> getFileOrBuilderList();

      PluginProtos.CodeGeneratorResponse.FileOrBuilder getFileOrBuilder(int var1);
   }

   public static final class CodeGeneratorRequest extends GeneratedMessageV3 implements PluginProtos.CodeGeneratorRequestOrBuilder {
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      public static final int FILE_TO_GENERATE_FIELD_NUMBER = 1;
      private LazyStringList fileToGenerate_;
      public static final int PARAMETER_FIELD_NUMBER = 2;
      private volatile Object parameter_;
      public static final int PROTO_FILE_FIELD_NUMBER = 15;
      private List<DescriptorProtos.FileDescriptorProto> protoFile_;
      public static final int COMPILER_VERSION_FIELD_NUMBER = 3;
      private PluginProtos.Version compilerVersion_;
      private byte memoizedIsInitialized;
      private static final PluginProtos.CodeGeneratorRequest DEFAULT_INSTANCE = new PluginProtos.CodeGeneratorRequest();
      /** @deprecated */
      @Deprecated
      public static final Parser<PluginProtos.CodeGeneratorRequest> PARSER = new AbstractParser<PluginProtos.CodeGeneratorRequest>() {
         public PluginProtos.CodeGeneratorRequest parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            PluginProtos.CodeGeneratorRequest.Builder builder = PluginProtos.CodeGeneratorRequest.newBuilder();

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

      private CodeGeneratorRequest(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private CodeGeneratorRequest() {
         this.memoizedIsInitialized = -1;
         this.fileToGenerate_ = LazyStringArrayList.EMPTY;
         this.parameter_ = "";
         this.protoFile_ = Collections.emptyList();
      }

      protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
         return new PluginProtos.CodeGeneratorRequest();
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorRequest_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.CodeGeneratorRequest.class, PluginProtos.CodeGeneratorRequest.Builder.class);
      }

      public ProtocolStringList getFileToGenerateList() {
         return this.fileToGenerate_;
      }

      public int getFileToGenerateCount() {
         return this.fileToGenerate_.size();
      }

      public String getFileToGenerate(int index) {
         return (String)this.fileToGenerate_.get(index);
      }

      public ByteString getFileToGenerateBytes(int index) {
         return this.fileToGenerate_.getByteString(index);
      }

      public boolean hasParameter() {
         return (this.bitField0_ & 1) != 0;
      }

      public String getParameter() {
         Object ref = this.parameter_;
         if (ref instanceof String) {
            return (String)ref;
         } else {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
               this.parameter_ = s;
            }

            return s;
         }
      }

      public ByteString getParameterBytes() {
         Object ref = this.parameter_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.parameter_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public List<DescriptorProtos.FileDescriptorProto> getProtoFileList() {
         return this.protoFile_;
      }

      public List<? extends DescriptorProtos.FileDescriptorProtoOrBuilder> getProtoFileOrBuilderList() {
         return this.protoFile_;
      }

      public int getProtoFileCount() {
         return this.protoFile_.size();
      }

      public DescriptorProtos.FileDescriptorProto getProtoFile(int index) {
         return (DescriptorProtos.FileDescriptorProto)this.protoFile_.get(index);
      }

      public DescriptorProtos.FileDescriptorProtoOrBuilder getProtoFileOrBuilder(int index) {
         return (DescriptorProtos.FileDescriptorProtoOrBuilder)this.protoFile_.get(index);
      }

      public boolean hasCompilerVersion() {
         return (this.bitField0_ & 2) != 0;
      }

      public PluginProtos.Version getCompilerVersion() {
         return this.compilerVersion_ == null ? PluginProtos.Version.getDefaultInstance() : this.compilerVersion_;
      }

      public PluginProtos.VersionOrBuilder getCompilerVersionOrBuilder() {
         return this.compilerVersion_ == null ? PluginProtos.Version.getDefaultInstance() : this.compilerVersion_;
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else {
            for(int i = 0; i < this.getProtoFileCount(); ++i) {
               if (!this.getProtoFile(i).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         int i;
         for(i = 0; i < this.fileToGenerate_.size(); ++i) {
            GeneratedMessageV3.writeString(output, 1, this.fileToGenerate_.getRaw(i));
         }

         if ((this.bitField0_ & 1) != 0) {
            GeneratedMessageV3.writeString(output, 2, this.parameter_);
         }

         if ((this.bitField0_ & 2) != 0) {
            output.writeMessage(3, this.getCompilerVersion());
         }

         for(i = 0; i < this.protoFile_.size(); ++i) {
            output.writeMessage(15, (MessageLite)this.protoFile_.get(i));
         }

         this.getUnknownFields().writeTo(output);
      }

      public int getSerializedSize() {
         int size = this.memoizedSize;
         if (size != -1) {
            return size;
         } else {
            int size = 0;
            int i = 0;

            for(int i = 0; i < this.fileToGenerate_.size(); ++i) {
               i += computeStringSizeNoTag(this.fileToGenerate_.getRaw(i));
            }

            size = size + i;
            size += 1 * this.getFileToGenerateList().size();
            if ((this.bitField0_ & 1) != 0) {
               size += GeneratedMessageV3.computeStringSize(2, this.parameter_);
            }

            if ((this.bitField0_ & 2) != 0) {
               size += CodedOutputStream.computeMessageSize(3, this.getCompilerVersion());
            }

            for(i = 0; i < this.protoFile_.size(); ++i) {
               size += CodedOutputStream.computeMessageSize(15, (MessageLite)this.protoFile_.get(i));
            }

            size += this.getUnknownFields().getSerializedSize();
            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof PluginProtos.CodeGeneratorRequest)) {
            return super.equals(obj);
         } else {
            PluginProtos.CodeGeneratorRequest other = (PluginProtos.CodeGeneratorRequest)obj;
            if (!this.getFileToGenerateList().equals(other.getFileToGenerateList())) {
               return false;
            } else if (this.hasParameter() != other.hasParameter()) {
               return false;
            } else if (this.hasParameter() && !this.getParameter().equals(other.getParameter())) {
               return false;
            } else if (!this.getProtoFileList().equals(other.getProtoFileList())) {
               return false;
            } else if (this.hasCompilerVersion() != other.hasCompilerVersion()) {
               return false;
            } else if (this.hasCompilerVersion() && !this.getCompilerVersion().equals(other.getCompilerVersion())) {
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
            if (this.getFileToGenerateCount() > 0) {
               hash = 37 * hash + 1;
               hash = 53 * hash + this.getFileToGenerateList().hashCode();
            }

            if (this.hasParameter()) {
               hash = 37 * hash + 2;
               hash = 53 * hash + this.getParameter().hashCode();
            }

            if (this.getProtoFileCount() > 0) {
               hash = 37 * hash + 15;
               hash = 53 * hash + this.getProtoFileList().hashCode();
            }

            if (this.hasCompilerVersion()) {
               hash = 37 * hash + 3;
               hash = 53 * hash + this.getCompilerVersion().hashCode();
            }

            hash = 29 * hash + this.getUnknownFields().hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorRequest)PARSER.parseFrom(data);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorRequest)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorRequest)PARSER.parseFrom(data);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorRequest)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorRequest)PARSER.parseFrom(data);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.CodeGeneratorRequest)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(InputStream input) throws IOException {
         return (PluginProtos.CodeGeneratorRequest)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.CodeGeneratorRequest)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorRequest parseDelimitedFrom(InputStream input) throws IOException {
         return (PluginProtos.CodeGeneratorRequest)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static PluginProtos.CodeGeneratorRequest parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.CodeGeneratorRequest)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(CodedInputStream input) throws IOException {
         return (PluginProtos.CodeGeneratorRequest)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static PluginProtos.CodeGeneratorRequest parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.CodeGeneratorRequest)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public PluginProtos.CodeGeneratorRequest.Builder newBuilderForType() {
         return newBuilder();
      }

      public static PluginProtos.CodeGeneratorRequest.Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static PluginProtos.CodeGeneratorRequest.Builder newBuilder(PluginProtos.CodeGeneratorRequest prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public PluginProtos.CodeGeneratorRequest.Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new PluginProtos.CodeGeneratorRequest.Builder() : (new PluginProtos.CodeGeneratorRequest.Builder()).mergeFrom(this);
      }

      protected PluginProtos.CodeGeneratorRequest.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         PluginProtos.CodeGeneratorRequest.Builder builder = new PluginProtos.CodeGeneratorRequest.Builder(parent);
         return builder;
      }

      public static PluginProtos.CodeGeneratorRequest getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<PluginProtos.CodeGeneratorRequest> parser() {
         return PARSER;
      }

      public Parser<PluginProtos.CodeGeneratorRequest> getParserForType() {
         return PARSER;
      }

      public PluginProtos.CodeGeneratorRequest getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      CodeGeneratorRequest(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<PluginProtos.CodeGeneratorRequest.Builder> implements PluginProtos.CodeGeneratorRequestOrBuilder {
         private int bitField0_;
         private LazyStringList fileToGenerate_;
         private Object parameter_;
         private List<DescriptorProtos.FileDescriptorProto> protoFile_;
         private RepeatedFieldBuilderV3<DescriptorProtos.FileDescriptorProto, DescriptorProtos.FileDescriptorProto.Builder, DescriptorProtos.FileDescriptorProtoOrBuilder> protoFileBuilder_;
         private PluginProtos.Version compilerVersion_;
         private SingleFieldBuilderV3<PluginProtos.Version, PluginProtos.Version.Builder, PluginProtos.VersionOrBuilder> compilerVersionBuilder_;

         public static final Descriptors.Descriptor getDescriptor() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorRequest_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.CodeGeneratorRequest.class, PluginProtos.CodeGeneratorRequest.Builder.class);
         }

         private Builder() {
            this.fileToGenerate_ = LazyStringArrayList.EMPTY;
            this.parameter_ = "";
            this.protoFile_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.fileToGenerate_ = LazyStringArrayList.EMPTY;
            this.parameter_ = "";
            this.protoFile_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private void maybeForceBuilderInitialization() {
            if (PluginProtos.CodeGeneratorRequest.alwaysUseFieldBuilders) {
               this.getProtoFileFieldBuilder();
               this.getCompilerVersionFieldBuilder();
            }

         }

         public PluginProtos.CodeGeneratorRequest.Builder clear() {
            super.clear();
            this.fileToGenerate_ = LazyStringArrayList.EMPTY;
            this.bitField0_ &= -2;
            this.parameter_ = "";
            this.bitField0_ &= -3;
            if (this.protoFileBuilder_ == null) {
               this.protoFile_ = Collections.emptyList();
            } else {
               this.protoFile_ = null;
               this.protoFileBuilder_.clear();
            }

            this.bitField0_ &= -5;
            if (this.compilerVersionBuilder_ == null) {
               this.compilerVersion_ = null;
            } else {
               this.compilerVersionBuilder_.clear();
            }

            this.bitField0_ &= -9;
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return PluginProtos.internal_static_google_protobuf_compiler_CodeGeneratorRequest_descriptor;
         }

         public PluginProtos.CodeGeneratorRequest getDefaultInstanceForType() {
            return PluginProtos.CodeGeneratorRequest.getDefaultInstance();
         }

         public PluginProtos.CodeGeneratorRequest build() {
            PluginProtos.CodeGeneratorRequest result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public PluginProtos.CodeGeneratorRequest buildPartial() {
            PluginProtos.CodeGeneratorRequest result = new PluginProtos.CodeGeneratorRequest(this);
            int from_bitField0_ = this.bitField0_;
            int to_bitField0_ = 0;
            if ((this.bitField0_ & 1) != 0) {
               this.fileToGenerate_ = this.fileToGenerate_.getUnmodifiableView();
               this.bitField0_ &= -2;
            }

            result.fileToGenerate_ = this.fileToGenerate_;
            if ((from_bitField0_ & 2) != 0) {
               to_bitField0_ |= 1;
            }

            result.parameter_ = this.parameter_;
            if (this.protoFileBuilder_ == null) {
               if ((this.bitField0_ & 4) != 0) {
                  this.protoFile_ = Collections.unmodifiableList(this.protoFile_);
                  this.bitField0_ &= -5;
               }

               result.protoFile_ = this.protoFile_;
            } else {
               result.protoFile_ = this.protoFileBuilder_.build();
            }

            if ((from_bitField0_ & 8) != 0) {
               if (this.compilerVersionBuilder_ == null) {
                  result.compilerVersion_ = this.compilerVersion_;
               } else {
                  result.compilerVersion_ = (PluginProtos.Version)this.compilerVersionBuilder_.build();
               }

               to_bitField0_ |= 2;
            }

            result.bitField0_ = to_bitField0_;
            this.onBuilt();
            return result;
         }

         public PluginProtos.CodeGeneratorRequest.Builder clone() {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.clone();
         }

         public PluginProtos.CodeGeneratorRequest.Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.setField(field, value);
         }

         public PluginProtos.CodeGeneratorRequest.Builder clearField(Descriptors.FieldDescriptor field) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.clearField(field);
         }

         public PluginProtos.CodeGeneratorRequest.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.clearOneof(oneof);
         }

         public PluginProtos.CodeGeneratorRequest.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.setRepeatedField(field, index, value);
         }

         public PluginProtos.CodeGeneratorRequest.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.addRepeatedField(field, value);
         }

         public PluginProtos.CodeGeneratorRequest.Builder mergeFrom(Message other) {
            if (other instanceof PluginProtos.CodeGeneratorRequest) {
               return this.mergeFrom((PluginProtos.CodeGeneratorRequest)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public PluginProtos.CodeGeneratorRequest.Builder mergeFrom(PluginProtos.CodeGeneratorRequest other) {
            if (other == PluginProtos.CodeGeneratorRequest.getDefaultInstance()) {
               return this;
            } else {
               if (!other.fileToGenerate_.isEmpty()) {
                  if (this.fileToGenerate_.isEmpty()) {
                     this.fileToGenerate_ = other.fileToGenerate_;
                     this.bitField0_ &= -2;
                  } else {
                     this.ensureFileToGenerateIsMutable();
                     this.fileToGenerate_.addAll(other.fileToGenerate_);
                  }

                  this.onChanged();
               }

               if (other.hasParameter()) {
                  this.bitField0_ |= 2;
                  this.parameter_ = other.parameter_;
                  this.onChanged();
               }

               if (this.protoFileBuilder_ == null) {
                  if (!other.protoFile_.isEmpty()) {
                     if (this.protoFile_.isEmpty()) {
                        this.protoFile_ = other.protoFile_;
                        this.bitField0_ &= -5;
                     } else {
                        this.ensureProtoFileIsMutable();
                        this.protoFile_.addAll(other.protoFile_);
                     }

                     this.onChanged();
                  }
               } else if (!other.protoFile_.isEmpty()) {
                  if (this.protoFileBuilder_.isEmpty()) {
                     this.protoFileBuilder_.dispose();
                     this.protoFileBuilder_ = null;
                     this.protoFile_ = other.protoFile_;
                     this.bitField0_ &= -5;
                     this.protoFileBuilder_ = PluginProtos.CodeGeneratorRequest.alwaysUseFieldBuilders ? this.getProtoFileFieldBuilder() : null;
                  } else {
                     this.protoFileBuilder_.addAllMessages(other.protoFile_);
                  }
               }

               if (other.hasCompilerVersion()) {
                  this.mergeCompilerVersion(other.getCompilerVersion());
               }

               this.mergeUnknownFields(other.getUnknownFields());
               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            for(int i = 0; i < this.getProtoFileCount(); ++i) {
               if (!this.getProtoFile(i).isInitialized()) {
                  return false;
               }
            }

            return true;
         }

         public PluginProtos.CodeGeneratorRequest.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                     case 10:
                        ByteString bs = input.readBytes();
                        this.ensureFileToGenerateIsMutable();
                        this.fileToGenerate_.add(bs);
                        break;
                     case 18:
                        this.parameter_ = input.readBytes();
                        this.bitField0_ |= 2;
                        break;
                     case 26:
                        input.readMessage((MessageLite.Builder)this.getCompilerVersionFieldBuilder().getBuilder(), extensionRegistry);
                        this.bitField0_ |= 8;
                        break;
                     case 122:
                        DescriptorProtos.FileDescriptorProto m = (DescriptorProtos.FileDescriptorProto)input.readMessage(DescriptorProtos.FileDescriptorProto.PARSER, extensionRegistry);
                        if (this.protoFileBuilder_ == null) {
                           this.ensureProtoFileIsMutable();
                           this.protoFile_.add(m);
                        } else {
                           this.protoFileBuilder_.addMessage(m);
                        }
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

         private void ensureFileToGenerateIsMutable() {
            if ((this.bitField0_ & 1) == 0) {
               this.fileToGenerate_ = new LazyStringArrayList(this.fileToGenerate_);
               this.bitField0_ |= 1;
            }

         }

         public ProtocolStringList getFileToGenerateList() {
            return this.fileToGenerate_.getUnmodifiableView();
         }

         public int getFileToGenerateCount() {
            return this.fileToGenerate_.size();
         }

         public String getFileToGenerate(int index) {
            return (String)this.fileToGenerate_.get(index);
         }

         public ByteString getFileToGenerateBytes(int index) {
            return this.fileToGenerate_.getByteString(index);
         }

         public PluginProtos.CodeGeneratorRequest.Builder setFileToGenerate(int index, String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.ensureFileToGenerateIsMutable();
               this.fileToGenerate_.set(index, (Object)value);
               this.onChanged();
               return this;
            }
         }

         public PluginProtos.CodeGeneratorRequest.Builder addFileToGenerate(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.ensureFileToGenerateIsMutable();
               this.fileToGenerate_.add((Object)value);
               this.onChanged();
               return this;
            }
         }

         public PluginProtos.CodeGeneratorRequest.Builder addAllFileToGenerate(Iterable<String> values) {
            this.ensureFileToGenerateIsMutable();
            AbstractMessageLite.Builder.addAll(values, (List)this.fileToGenerate_);
            this.onChanged();
            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder clearFileToGenerate() {
            this.fileToGenerate_ = LazyStringArrayList.EMPTY;
            this.bitField0_ &= -2;
            this.onChanged();
            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder addFileToGenerateBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.ensureFileToGenerateIsMutable();
               this.fileToGenerate_.add(value);
               this.onChanged();
               return this;
            }
         }

         public boolean hasParameter() {
            return (this.bitField0_ & 2) != 0;
         }

         public String getParameter() {
            Object ref = this.parameter_;
            if (!(ref instanceof String)) {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               if (bs.isValidUtf8()) {
                  this.parameter_ = s;
               }

               return s;
            } else {
               return (String)ref;
            }
         }

         public ByteString getParameterBytes() {
            Object ref = this.parameter_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.parameter_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public PluginProtos.CodeGeneratorRequest.Builder setParameter(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.bitField0_ |= 2;
               this.parameter_ = value;
               this.onChanged();
               return this;
            }
         }

         public PluginProtos.CodeGeneratorRequest.Builder clearParameter() {
            this.bitField0_ &= -3;
            this.parameter_ = PluginProtos.CodeGeneratorRequest.getDefaultInstance().getParameter();
            this.onChanged();
            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder setParameterBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.bitField0_ |= 2;
               this.parameter_ = value;
               this.onChanged();
               return this;
            }
         }

         private void ensureProtoFileIsMutable() {
            if ((this.bitField0_ & 4) == 0) {
               this.protoFile_ = new ArrayList(this.protoFile_);
               this.bitField0_ |= 4;
            }

         }

         public List<DescriptorProtos.FileDescriptorProto> getProtoFileList() {
            return this.protoFileBuilder_ == null ? Collections.unmodifiableList(this.protoFile_) : this.protoFileBuilder_.getMessageList();
         }

         public int getProtoFileCount() {
            return this.protoFileBuilder_ == null ? this.protoFile_.size() : this.protoFileBuilder_.getCount();
         }

         public DescriptorProtos.FileDescriptorProto getProtoFile(int index) {
            return this.protoFileBuilder_ == null ? (DescriptorProtos.FileDescriptorProto)this.protoFile_.get(index) : (DescriptorProtos.FileDescriptorProto)this.protoFileBuilder_.getMessage(index);
         }

         public PluginProtos.CodeGeneratorRequest.Builder setProtoFile(int index, DescriptorProtos.FileDescriptorProto value) {
            if (this.protoFileBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureProtoFileIsMutable();
               this.protoFile_.set(index, value);
               this.onChanged();
            } else {
               this.protoFileBuilder_.setMessage(index, value);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder setProtoFile(int index, DescriptorProtos.FileDescriptorProto.Builder builderForValue) {
            if (this.protoFileBuilder_ == null) {
               this.ensureProtoFileIsMutable();
               this.protoFile_.set(index, builderForValue.build());
               this.onChanged();
            } else {
               this.protoFileBuilder_.setMessage(index, builderForValue.build());
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder addProtoFile(DescriptorProtos.FileDescriptorProto value) {
            if (this.protoFileBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureProtoFileIsMutable();
               this.protoFile_.add(value);
               this.onChanged();
            } else {
               this.protoFileBuilder_.addMessage(value);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder addProtoFile(int index, DescriptorProtos.FileDescriptorProto value) {
            if (this.protoFileBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.ensureProtoFileIsMutable();
               this.protoFile_.add(index, value);
               this.onChanged();
            } else {
               this.protoFileBuilder_.addMessage(index, value);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder addProtoFile(DescriptorProtos.FileDescriptorProto.Builder builderForValue) {
            if (this.protoFileBuilder_ == null) {
               this.ensureProtoFileIsMutable();
               this.protoFile_.add(builderForValue.build());
               this.onChanged();
            } else {
               this.protoFileBuilder_.addMessage(builderForValue.build());
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder addProtoFile(int index, DescriptorProtos.FileDescriptorProto.Builder builderForValue) {
            if (this.protoFileBuilder_ == null) {
               this.ensureProtoFileIsMutable();
               this.protoFile_.add(index, builderForValue.build());
               this.onChanged();
            } else {
               this.protoFileBuilder_.addMessage(index, builderForValue.build());
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder addAllProtoFile(Iterable<? extends DescriptorProtos.FileDescriptorProto> values) {
            if (this.protoFileBuilder_ == null) {
               this.ensureProtoFileIsMutable();
               AbstractMessageLite.Builder.addAll(values, this.protoFile_);
               this.onChanged();
            } else {
               this.protoFileBuilder_.addAllMessages(values);
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder clearProtoFile() {
            if (this.protoFileBuilder_ == null) {
               this.protoFile_ = Collections.emptyList();
               this.bitField0_ &= -5;
               this.onChanged();
            } else {
               this.protoFileBuilder_.clear();
            }

            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder removeProtoFile(int index) {
            if (this.protoFileBuilder_ == null) {
               this.ensureProtoFileIsMutable();
               this.protoFile_.remove(index);
               this.onChanged();
            } else {
               this.protoFileBuilder_.remove(index);
            }

            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder getProtoFileBuilder(int index) {
            return (DescriptorProtos.FileDescriptorProto.Builder)this.getProtoFileFieldBuilder().getBuilder(index);
         }

         public DescriptorProtos.FileDescriptorProtoOrBuilder getProtoFileOrBuilder(int index) {
            return this.protoFileBuilder_ == null ? (DescriptorProtos.FileDescriptorProtoOrBuilder)this.protoFile_.get(index) : (DescriptorProtos.FileDescriptorProtoOrBuilder)this.protoFileBuilder_.getMessageOrBuilder(index);
         }

         public List<? extends DescriptorProtos.FileDescriptorProtoOrBuilder> getProtoFileOrBuilderList() {
            return this.protoFileBuilder_ != null ? this.protoFileBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.protoFile_);
         }

         public DescriptorProtos.FileDescriptorProto.Builder addProtoFileBuilder() {
            return (DescriptorProtos.FileDescriptorProto.Builder)this.getProtoFileFieldBuilder().addBuilder(DescriptorProtos.FileDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorProto.Builder addProtoFileBuilder(int index) {
            return (DescriptorProtos.FileDescriptorProto.Builder)this.getProtoFileFieldBuilder().addBuilder(index, DescriptorProtos.FileDescriptorProto.getDefaultInstance());
         }

         public List<DescriptorProtos.FileDescriptorProto.Builder> getProtoFileBuilderList() {
            return this.getProtoFileFieldBuilder().getBuilderList();
         }

         private RepeatedFieldBuilderV3<DescriptorProtos.FileDescriptorProto, DescriptorProtos.FileDescriptorProto.Builder, DescriptorProtos.FileDescriptorProtoOrBuilder> getProtoFileFieldBuilder() {
            if (this.protoFileBuilder_ == null) {
               this.protoFileBuilder_ = new RepeatedFieldBuilderV3(this.protoFile_, (this.bitField0_ & 4) != 0, this.getParentForChildren(), this.isClean());
               this.protoFile_ = null;
            }

            return this.protoFileBuilder_;
         }

         public boolean hasCompilerVersion() {
            return (this.bitField0_ & 8) != 0;
         }

         public PluginProtos.Version getCompilerVersion() {
            if (this.compilerVersionBuilder_ == null) {
               return this.compilerVersion_ == null ? PluginProtos.Version.getDefaultInstance() : this.compilerVersion_;
            } else {
               return (PluginProtos.Version)this.compilerVersionBuilder_.getMessage();
            }
         }

         public PluginProtos.CodeGeneratorRequest.Builder setCompilerVersion(PluginProtos.Version value) {
            if (this.compilerVersionBuilder_ == null) {
               if (value == null) {
                  throw new NullPointerException();
               }

               this.compilerVersion_ = value;
               this.onChanged();
            } else {
               this.compilerVersionBuilder_.setMessage(value);
            }

            this.bitField0_ |= 8;
            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder setCompilerVersion(PluginProtos.Version.Builder builderForValue) {
            if (this.compilerVersionBuilder_ == null) {
               this.compilerVersion_ = builderForValue.build();
               this.onChanged();
            } else {
               this.compilerVersionBuilder_.setMessage(builderForValue.build());
            }

            this.bitField0_ |= 8;
            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder mergeCompilerVersion(PluginProtos.Version value) {
            if (this.compilerVersionBuilder_ == null) {
               if ((this.bitField0_ & 8) != 0 && this.compilerVersion_ != null && this.compilerVersion_ != PluginProtos.Version.getDefaultInstance()) {
                  this.compilerVersion_ = PluginProtos.Version.newBuilder(this.compilerVersion_).mergeFrom(value).buildPartial();
               } else {
                  this.compilerVersion_ = value;
               }

               this.onChanged();
            } else {
               this.compilerVersionBuilder_.mergeFrom(value);
            }

            this.bitField0_ |= 8;
            return this;
         }

         public PluginProtos.CodeGeneratorRequest.Builder clearCompilerVersion() {
            if (this.compilerVersionBuilder_ == null) {
               this.compilerVersion_ = null;
               this.onChanged();
            } else {
               this.compilerVersionBuilder_.clear();
            }

            this.bitField0_ &= -9;
            return this;
         }

         public PluginProtos.Version.Builder getCompilerVersionBuilder() {
            this.bitField0_ |= 8;
            this.onChanged();
            return (PluginProtos.Version.Builder)this.getCompilerVersionFieldBuilder().getBuilder();
         }

         public PluginProtos.VersionOrBuilder getCompilerVersionOrBuilder() {
            if (this.compilerVersionBuilder_ != null) {
               return (PluginProtos.VersionOrBuilder)this.compilerVersionBuilder_.getMessageOrBuilder();
            } else {
               return this.compilerVersion_ == null ? PluginProtos.Version.getDefaultInstance() : this.compilerVersion_;
            }
         }

         private SingleFieldBuilderV3<PluginProtos.Version, PluginProtos.Version.Builder, PluginProtos.VersionOrBuilder> getCompilerVersionFieldBuilder() {
            if (this.compilerVersionBuilder_ == null) {
               this.compilerVersionBuilder_ = new SingleFieldBuilderV3(this.getCompilerVersion(), this.getParentForChildren(), this.isClean());
               this.compilerVersion_ = null;
            }

            return this.compilerVersionBuilder_;
         }

         public final PluginProtos.CodeGeneratorRequest.Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.setUnknownFields(unknownFields);
         }

         public final PluginProtos.CodeGeneratorRequest.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (PluginProtos.CodeGeneratorRequest.Builder)super.mergeUnknownFields(unknownFields);
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

   public interface CodeGeneratorRequestOrBuilder extends MessageOrBuilder {
      List<String> getFileToGenerateList();

      int getFileToGenerateCount();

      String getFileToGenerate(int var1);

      ByteString getFileToGenerateBytes(int var1);

      boolean hasParameter();

      String getParameter();

      ByteString getParameterBytes();

      List<DescriptorProtos.FileDescriptorProto> getProtoFileList();

      DescriptorProtos.FileDescriptorProto getProtoFile(int var1);

      int getProtoFileCount();

      List<? extends DescriptorProtos.FileDescriptorProtoOrBuilder> getProtoFileOrBuilderList();

      DescriptorProtos.FileDescriptorProtoOrBuilder getProtoFileOrBuilder(int var1);

      boolean hasCompilerVersion();

      PluginProtos.Version getCompilerVersion();

      PluginProtos.VersionOrBuilder getCompilerVersionOrBuilder();
   }

   public static final class Version extends GeneratedMessageV3 implements PluginProtos.VersionOrBuilder {
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      public static final int MAJOR_FIELD_NUMBER = 1;
      private int major_;
      public static final int MINOR_FIELD_NUMBER = 2;
      private int minor_;
      public static final int PATCH_FIELD_NUMBER = 3;
      private int patch_;
      public static final int SUFFIX_FIELD_NUMBER = 4;
      private volatile Object suffix_;
      private byte memoizedIsInitialized;
      private static final PluginProtos.Version DEFAULT_INSTANCE = new PluginProtos.Version();
      /** @deprecated */
      @Deprecated
      public static final Parser<PluginProtos.Version> PARSER = new AbstractParser<PluginProtos.Version>() {
         public PluginProtos.Version parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            PluginProtos.Version.Builder builder = PluginProtos.Version.newBuilder();

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

      private Version(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private Version() {
         this.memoizedIsInitialized = -1;
         this.suffix_ = "";
      }

      protected Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
         return new PluginProtos.Version();
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return PluginProtos.internal_static_google_protobuf_compiler_Version_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return PluginProtos.internal_static_google_protobuf_compiler_Version_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.Version.class, PluginProtos.Version.Builder.class);
      }

      public boolean hasMajor() {
         return (this.bitField0_ & 1) != 0;
      }

      public int getMajor() {
         return this.major_;
      }

      public boolean hasMinor() {
         return (this.bitField0_ & 2) != 0;
      }

      public int getMinor() {
         return this.minor_;
      }

      public boolean hasPatch() {
         return (this.bitField0_ & 4) != 0;
      }

      public int getPatch() {
         return this.patch_;
      }

      public boolean hasSuffix() {
         return (this.bitField0_ & 8) != 0;
      }

      public String getSuffix() {
         Object ref = this.suffix_;
         if (ref instanceof String) {
            return (String)ref;
         } else {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
               this.suffix_ = s;
            }

            return s;
         }
      }

      public ByteString getSuffixBytes() {
         Object ref = this.suffix_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.suffix_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         if ((this.bitField0_ & 1) != 0) {
            output.writeInt32(1, this.major_);
         }

         if ((this.bitField0_ & 2) != 0) {
            output.writeInt32(2, this.minor_);
         }

         if ((this.bitField0_ & 4) != 0) {
            output.writeInt32(3, this.patch_);
         }

         if ((this.bitField0_ & 8) != 0) {
            GeneratedMessageV3.writeString(output, 4, this.suffix_);
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
               size += CodedOutputStream.computeInt32Size(1, this.major_);
            }

            if ((this.bitField0_ & 2) != 0) {
               size += CodedOutputStream.computeInt32Size(2, this.minor_);
            }

            if ((this.bitField0_ & 4) != 0) {
               size += CodedOutputStream.computeInt32Size(3, this.patch_);
            }

            if ((this.bitField0_ & 8) != 0) {
               size += GeneratedMessageV3.computeStringSize(4, this.suffix_);
            }

            size += this.getUnknownFields().getSerializedSize();
            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof PluginProtos.Version)) {
            return super.equals(obj);
         } else {
            PluginProtos.Version other = (PluginProtos.Version)obj;
            if (this.hasMajor() != other.hasMajor()) {
               return false;
            } else if (this.hasMajor() && this.getMajor() != other.getMajor()) {
               return false;
            } else if (this.hasMinor() != other.hasMinor()) {
               return false;
            } else if (this.hasMinor() && this.getMinor() != other.getMinor()) {
               return false;
            } else if (this.hasPatch() != other.hasPatch()) {
               return false;
            } else if (this.hasPatch() && this.getPatch() != other.getPatch()) {
               return false;
            } else if (this.hasSuffix() != other.hasSuffix()) {
               return false;
            } else if (this.hasSuffix() && !this.getSuffix().equals(other.getSuffix())) {
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
            if (this.hasMajor()) {
               hash = 37 * hash + 1;
               hash = 53 * hash + this.getMajor();
            }

            if (this.hasMinor()) {
               hash = 37 * hash + 2;
               hash = 53 * hash + this.getMinor();
            }

            if (this.hasPatch()) {
               hash = 37 * hash + 3;
               hash = 53 * hash + this.getPatch();
            }

            if (this.hasSuffix()) {
               hash = 37 * hash + 4;
               hash = 53 * hash + this.getSuffix().hashCode();
            }

            hash = 29 * hash + this.getUnknownFields().hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static PluginProtos.Version parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (PluginProtos.Version)PARSER.parseFrom(data);
      }

      public static PluginProtos.Version parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.Version)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.Version parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (PluginProtos.Version)PARSER.parseFrom(data);
      }

      public static PluginProtos.Version parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.Version)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.Version parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (PluginProtos.Version)PARSER.parseFrom(data);
      }

      public static PluginProtos.Version parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (PluginProtos.Version)PARSER.parseFrom(data, extensionRegistry);
      }

      public static PluginProtos.Version parseFrom(InputStream input) throws IOException {
         return (PluginProtos.Version)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static PluginProtos.Version parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.Version)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static PluginProtos.Version parseDelimitedFrom(InputStream input) throws IOException {
         return (PluginProtos.Version)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static PluginProtos.Version parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.Version)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static PluginProtos.Version parseFrom(CodedInputStream input) throws IOException {
         return (PluginProtos.Version)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static PluginProtos.Version parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (PluginProtos.Version)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public PluginProtos.Version.Builder newBuilderForType() {
         return newBuilder();
      }

      public static PluginProtos.Version.Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static PluginProtos.Version.Builder newBuilder(PluginProtos.Version prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public PluginProtos.Version.Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new PluginProtos.Version.Builder() : (new PluginProtos.Version.Builder()).mergeFrom(this);
      }

      protected PluginProtos.Version.Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         PluginProtos.Version.Builder builder = new PluginProtos.Version.Builder(parent);
         return builder;
      }

      public static PluginProtos.Version getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<PluginProtos.Version> parser() {
         return PARSER;
      }

      public Parser<PluginProtos.Version> getParserForType() {
         return PARSER;
      }

      public PluginProtos.Version getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      Version(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<PluginProtos.Version.Builder> implements PluginProtos.VersionOrBuilder {
         private int bitField0_;
         private int major_;
         private int minor_;
         private int patch_;
         private Object suffix_;

         public static final Descriptors.Descriptor getDescriptor() {
            return PluginProtos.internal_static_google_protobuf_compiler_Version_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return PluginProtos.internal_static_google_protobuf_compiler_Version_fieldAccessorTable.ensureFieldAccessorsInitialized(PluginProtos.Version.class, PluginProtos.Version.Builder.class);
         }

         private Builder() {
            this.suffix_ = "";
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.suffix_ = "";
         }

         public PluginProtos.Version.Builder clear() {
            super.clear();
            this.major_ = 0;
            this.bitField0_ &= -2;
            this.minor_ = 0;
            this.bitField0_ &= -3;
            this.patch_ = 0;
            this.bitField0_ &= -5;
            this.suffix_ = "";
            this.bitField0_ &= -9;
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return PluginProtos.internal_static_google_protobuf_compiler_Version_descriptor;
         }

         public PluginProtos.Version getDefaultInstanceForType() {
            return PluginProtos.Version.getDefaultInstance();
         }

         public PluginProtos.Version build() {
            PluginProtos.Version result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public PluginProtos.Version buildPartial() {
            PluginProtos.Version result = new PluginProtos.Version(this);
            int from_bitField0_ = this.bitField0_;
            int to_bitField0_ = 0;
            if ((from_bitField0_ & 1) != 0) {
               result.major_ = this.major_;
               to_bitField0_ |= 1;
            }

            if ((from_bitField0_ & 2) != 0) {
               result.minor_ = this.minor_;
               to_bitField0_ |= 2;
            }

            if ((from_bitField0_ & 4) != 0) {
               result.patch_ = this.patch_;
               to_bitField0_ |= 4;
            }

            if ((from_bitField0_ & 8) != 0) {
               to_bitField0_ |= 8;
            }

            result.suffix_ = this.suffix_;
            result.bitField0_ = to_bitField0_;
            this.onBuilt();
            return result;
         }

         public PluginProtos.Version.Builder clone() {
            return (PluginProtos.Version.Builder)super.clone();
         }

         public PluginProtos.Version.Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (PluginProtos.Version.Builder)super.setField(field, value);
         }

         public PluginProtos.Version.Builder clearField(Descriptors.FieldDescriptor field) {
            return (PluginProtos.Version.Builder)super.clearField(field);
         }

         public PluginProtos.Version.Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (PluginProtos.Version.Builder)super.clearOneof(oneof);
         }

         public PluginProtos.Version.Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (PluginProtos.Version.Builder)super.setRepeatedField(field, index, value);
         }

         public PluginProtos.Version.Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (PluginProtos.Version.Builder)super.addRepeatedField(field, value);
         }

         public PluginProtos.Version.Builder mergeFrom(Message other) {
            if (other instanceof PluginProtos.Version) {
               return this.mergeFrom((PluginProtos.Version)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public PluginProtos.Version.Builder mergeFrom(PluginProtos.Version other) {
            if (other == PluginProtos.Version.getDefaultInstance()) {
               return this;
            } else {
               if (other.hasMajor()) {
                  this.setMajor(other.getMajor());
               }

               if (other.hasMinor()) {
                  this.setMinor(other.getMinor());
               }

               if (other.hasPatch()) {
                  this.setPatch(other.getPatch());
               }

               if (other.hasSuffix()) {
                  this.bitField0_ |= 8;
                  this.suffix_ = other.suffix_;
                  this.onChanged();
               }

               this.mergeUnknownFields(other.getUnknownFields());
               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            return true;
         }

         public PluginProtos.Version.Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
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
                        this.major_ = input.readInt32();
                        this.bitField0_ |= 1;
                        break;
                     case 16:
                        this.minor_ = input.readInt32();
                        this.bitField0_ |= 2;
                        break;
                     case 24:
                        this.patch_ = input.readInt32();
                        this.bitField0_ |= 4;
                        break;
                     case 34:
                        this.suffix_ = input.readBytes();
                        this.bitField0_ |= 8;
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

         public boolean hasMajor() {
            return (this.bitField0_ & 1) != 0;
         }

         public int getMajor() {
            return this.major_;
         }

         public PluginProtos.Version.Builder setMajor(int value) {
            this.bitField0_ |= 1;
            this.major_ = value;
            this.onChanged();
            return this;
         }

         public PluginProtos.Version.Builder clearMajor() {
            this.bitField0_ &= -2;
            this.major_ = 0;
            this.onChanged();
            return this;
         }

         public boolean hasMinor() {
            return (this.bitField0_ & 2) != 0;
         }

         public int getMinor() {
            return this.minor_;
         }

         public PluginProtos.Version.Builder setMinor(int value) {
            this.bitField0_ |= 2;
            this.minor_ = value;
            this.onChanged();
            return this;
         }

         public PluginProtos.Version.Builder clearMinor() {
            this.bitField0_ &= -3;
            this.minor_ = 0;
            this.onChanged();
            return this;
         }

         public boolean hasPatch() {
            return (this.bitField0_ & 4) != 0;
         }

         public int getPatch() {
            return this.patch_;
         }

         public PluginProtos.Version.Builder setPatch(int value) {
            this.bitField0_ |= 4;
            this.patch_ = value;
            this.onChanged();
            return this;
         }

         public PluginProtos.Version.Builder clearPatch() {
            this.bitField0_ &= -5;
            this.patch_ = 0;
            this.onChanged();
            return this;
         }

         public boolean hasSuffix() {
            return (this.bitField0_ & 8) != 0;
         }

         public String getSuffix() {
            Object ref = this.suffix_;
            if (!(ref instanceof String)) {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               if (bs.isValidUtf8()) {
                  this.suffix_ = s;
               }

               return s;
            } else {
               return (String)ref;
            }
         }

         public ByteString getSuffixBytes() {
            Object ref = this.suffix_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.suffix_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public PluginProtos.Version.Builder setSuffix(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.bitField0_ |= 8;
               this.suffix_ = value;
               this.onChanged();
               return this;
            }
         }

         public PluginProtos.Version.Builder clearSuffix() {
            this.bitField0_ &= -9;
            this.suffix_ = PluginProtos.Version.getDefaultInstance().getSuffix();
            this.onChanged();
            return this;
         }

         public PluginProtos.Version.Builder setSuffixBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.bitField0_ |= 8;
               this.suffix_ = value;
               this.onChanged();
               return this;
            }
         }

         public final PluginProtos.Version.Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (PluginProtos.Version.Builder)super.setUnknownFields(unknownFields);
         }

         public final PluginProtos.Version.Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (PluginProtos.Version.Builder)super.mergeUnknownFields(unknownFields);
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

   public interface VersionOrBuilder extends MessageOrBuilder {
      boolean hasMajor();

      int getMajor();

      boolean hasMinor();

      int getMinor();

      boolean hasPatch();

      int getPatch();

      boolean hasSuffix();

      String getSuffix();

      ByteString getSuffixBytes();
   }
}
