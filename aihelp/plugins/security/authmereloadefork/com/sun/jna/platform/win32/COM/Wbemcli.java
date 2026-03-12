package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OaIdlUtil;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface Wbemcli {
   int WBEM_FLAG_RETURN_WBEM_COMPLETE = 0;
   int WBEM_FLAG_RETURN_IMMEDIATELY = 16;
   int WBEM_FLAG_FORWARD_ONLY = 32;
   int WBEM_FLAG_NO_ERROR_OBJECT = 64;
   int WBEM_FLAG_SEND_STATUS = 128;
   int WBEM_FLAG_ENSURE_LOCATABLE = 256;
   int WBEM_FLAG_DIRECT_READ = 512;
   int WBEM_MASK_RESERVED_FLAGS = 126976;
   int WBEM_FLAG_USE_AMENDED_QUALIFIERS = 131072;
   int WBEM_FLAG_STRONG_VALIDATION = 1048576;
   int WBEM_INFINITE = -1;
   int WBEM_S_NO_ERROR = 0;
   int WBEM_S_FALSE = 1;
   int WBEM_S_TIMEDOUT = 262148;
   int WBEM_S_NO_MORE_DATA = 262149;
   int WBEM_E_INVALID_NAMESPACE = -2147217394;
   int WBEM_E_INVALID_CLASS = -2147217392;
   int WBEM_E_INVALID_QUERY = -2147217385;
   int CIM_ILLEGAL = 4095;
   int CIM_EMPTY = 0;
   int CIM_SINT8 = 16;
   int CIM_UINT8 = 17;
   int CIM_SINT16 = 2;
   int CIM_UINT16 = 18;
   int CIM_SINT32 = 3;
   int CIM_UINT32 = 19;
   int CIM_SINT64 = 20;
   int CIM_UINT64 = 21;
   int CIM_REAL32 = 4;
   int CIM_REAL64 = 5;
   int CIM_BOOLEAN = 11;
   int CIM_STRING = 8;
   int CIM_DATETIME = 101;
   int CIM_REFERENCE = 102;
   int CIM_CHAR16 = 103;
   int CIM_OBJECT = 13;
   int CIM_FLAG_ARRAY = 8192;

   public static class IWbemContext extends Unknown {
      public static final Guid.CLSID CLSID_WbemContext = new Guid.CLSID("674B6698-EE92-11D0-AD71-00C04FD8FDFF");
      public static final Guid.GUID IID_IWbemContext = new Guid.GUID("44aca674-e8fc-11d0-a07c-00c04fb68820");

      public IWbemContext() {
      }

      public static Wbemcli.IWbemContext create() {
         PointerByReference pbr = new PointerByReference();
         WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(CLSID_WbemContext, (Pointer)null, 1, IID_IWbemContext, pbr);
         return COMUtils.FAILED(hres) ? null : new Wbemcli.IWbemContext(pbr.getValue());
      }

      public IWbemContext(Pointer pvInstance) {
         super(pvInstance);
      }

      public void SetValue(String wszName, int lFlag, Variant.VARIANT pValue) {
         WTypes.BSTR wszNameBSTR = OleAuto.INSTANCE.SysAllocString(wszName);

         try {
            WinNT.HRESULT res = (WinNT.HRESULT)this._invokeNativeObject(8, new Object[]{this.getPointer(), wszNameBSTR, lFlag, pValue}, WinNT.HRESULT.class);
            COMUtils.checkRC(res);
         } finally {
            OleAuto.INSTANCE.SysFreeString(wszNameBSTR);
         }

      }

      public void SetValue(String wszName, int lFlag, boolean pValue) {
         Variant.VARIANT aVariant = new Variant.VARIANT();
         aVariant.setValue(11, pValue ? Variant.VARIANT_TRUE : Variant.VARIANT_FALSE);
         this.SetValue(wszName, lFlag, aVariant);
         OleAuto.INSTANCE.VariantClear(aVariant);
      }

      public void SetValue(String wszName, int lFlag, String pValue) {
         Variant.VARIANT aVariant = new Variant.VARIANT();
         WTypes.BSTR strValue = OleAuto.INSTANCE.SysAllocString(pValue);

         try {
            aVariant.setValue(30, strValue);
            this.SetValue(wszName, lFlag, aVariant);
         } finally {
            OleAuto.INSTANCE.SysFreeString(strValue);
         }

      }
   }

   public static class IWbemServices extends Unknown {
      public IWbemServices() {
      }

      public IWbemServices(Pointer pvInstance) {
         super(pvInstance);
      }

      public WinNT.HRESULT ExecQuery(WTypes.BSTR strQueryLanguage, WTypes.BSTR strQuery, int lFlags, Wbemcli.IWbemContext pCtx, PointerByReference ppEnum) {
         return (WinNT.HRESULT)this._invokeNativeObject(20, new Object[]{this.getPointer(), strQueryLanguage, strQuery, lFlags, pCtx, ppEnum}, WinNT.HRESULT.class);
      }

      public Wbemcli.IEnumWbemClassObject ExecQuery(String strQueryLanguage, String strQuery, int lFlags, Wbemcli.IWbemContext pCtx) {
         WTypes.BSTR strQueryLanguageBSTR = OleAuto.INSTANCE.SysAllocString(strQueryLanguage);
         WTypes.BSTR strQueryBSTR = OleAuto.INSTANCE.SysAllocString(strQuery);

         Wbemcli.IEnumWbemClassObject var9;
         try {
            PointerByReference pbr = new PointerByReference();
            WinNT.HRESULT res = this.ExecQuery(strQueryLanguageBSTR, strQueryBSTR, lFlags, pCtx, pbr);
            COMUtils.checkRC(res);
            var9 = new Wbemcli.IEnumWbemClassObject(pbr.getValue());
         } finally {
            OleAuto.INSTANCE.SysFreeString(strQueryLanguageBSTR);
            OleAuto.INSTANCE.SysFreeString(strQueryBSTR);
         }

         return var9;
      }

      public WinNT.HRESULT GetObject(WTypes.BSTR strObjectPath, int lFlags, Wbemcli.IWbemContext pCtx, PointerByReference ppObject, PointerByReference ppCallResult) {
         return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), strObjectPath, lFlags, pCtx, ppObject, ppCallResult}, WinNT.HRESULT.class);
      }

      public Wbemcli.IWbemClassObject GetObject(String strObjectPath, int lFlags, Wbemcli.IWbemContext pCtx) {
         WTypes.BSTR strObjectPathBSTR = OleAuto.INSTANCE.SysAllocString(strObjectPath);

         Wbemcli.IWbemClassObject var7;
         try {
            PointerByReference ppObject = new PointerByReference();
            WinNT.HRESULT res = this.GetObject(strObjectPathBSTR, lFlags, pCtx, ppObject, (PointerByReference)null);
            COMUtils.checkRC(res);
            var7 = new Wbemcli.IWbemClassObject(ppObject.getValue());
         } finally {
            OleAuto.INSTANCE.SysFreeString(strObjectPathBSTR);
         }

         return var7;
      }
   }

   public static class IWbemLocator extends Unknown {
      public static final Guid.CLSID CLSID_WbemLocator = new Guid.CLSID("4590f811-1d3a-11d0-891f-00aa004b2e24");
      public static final Guid.GUID IID_IWbemLocator = new Guid.GUID("dc12a687-737f-11cf-884d-00aa004b2e24");

      public IWbemLocator() {
      }

      private IWbemLocator(Pointer pvInstance) {
         super(pvInstance);
      }

      public static Wbemcli.IWbemLocator create() {
         PointerByReference pbr = new PointerByReference();
         WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(CLSID_WbemLocator, (Pointer)null, 1, IID_IWbemLocator, pbr);
         return COMUtils.FAILED(hres) ? null : new Wbemcli.IWbemLocator(pbr.getValue());
      }

      public WinNT.HRESULT ConnectServer(WTypes.BSTR strNetworkResource, WTypes.BSTR strUser, WTypes.BSTR strPassword, WTypes.BSTR strLocale, int lSecurityFlags, WTypes.BSTR strAuthority, Wbemcli.IWbemContext pCtx, PointerByReference ppNamespace) {
         return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), strNetworkResource, strUser, strPassword, strLocale, lSecurityFlags, strAuthority, pCtx, ppNamespace}, WinNT.HRESULT.class);
      }

      public Wbemcli.IWbemServices ConnectServer(String strNetworkResource, String strUser, String strPassword, String strLocale, int lSecurityFlags, String strAuthority, Wbemcli.IWbemContext pCtx) {
         WTypes.BSTR strNetworkResourceBSTR = OleAuto.INSTANCE.SysAllocString(strNetworkResource);
         WTypes.BSTR strUserBSTR = OleAuto.INSTANCE.SysAllocString(strUser);
         WTypes.BSTR strPasswordBSTR = OleAuto.INSTANCE.SysAllocString(strPassword);
         WTypes.BSTR strLocaleBSTR = OleAuto.INSTANCE.SysAllocString(strLocale);
         WTypes.BSTR strAuthorityBSTR = OleAuto.INSTANCE.SysAllocString(strAuthority);
         PointerByReference pbr = new PointerByReference();

         Wbemcli.IWbemServices var15;
         try {
            WinNT.HRESULT result = this.ConnectServer(strNetworkResourceBSTR, strUserBSTR, strPasswordBSTR, strLocaleBSTR, lSecurityFlags, strAuthorityBSTR, pCtx, pbr);
            COMUtils.checkRC(result);
            var15 = new Wbemcli.IWbemServices(pbr.getValue());
         } finally {
            OleAuto.INSTANCE.SysFreeString(strNetworkResourceBSTR);
            OleAuto.INSTANCE.SysFreeString(strUserBSTR);
            OleAuto.INSTANCE.SysFreeString(strPasswordBSTR);
            OleAuto.INSTANCE.SysFreeString(strLocaleBSTR);
            OleAuto.INSTANCE.SysFreeString(strAuthorityBSTR);
         }

         return var15;
      }
   }

   public static class IEnumWbemClassObject extends Unknown {
      public IEnumWbemClassObject() {
      }

      public IEnumWbemClassObject(Pointer pvInstance) {
         super(pvInstance);
      }

      public WinNT.HRESULT Next(int lTimeOut, int uCount, Pointer[] ppObjects, IntByReference puReturned) {
         return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), lTimeOut, uCount, ppObjects, puReturned}, WinNT.HRESULT.class);
      }

      public Wbemcli.IWbemClassObject[] Next(int lTimeOut, int uCount) {
         Pointer[] resultArray = new Pointer[uCount];
         IntByReference resultCount = new IntByReference();
         WinNT.HRESULT result = this.Next(lTimeOut, uCount, resultArray, resultCount);
         COMUtils.checkRC(result);
         Wbemcli.IWbemClassObject[] returnValue = new Wbemcli.IWbemClassObject[resultCount.getValue()];

         for(int i = 0; i < resultCount.getValue(); ++i) {
            returnValue[i] = new Wbemcli.IWbemClassObject(resultArray[i]);
         }

         return returnValue;
      }
   }

   public static class IWbemQualifierSet extends Unknown {
      public IWbemQualifierSet(Pointer pvInstance) {
         super(pvInstance);
      }

      public WinNT.HRESULT Get(WString wszName, int lFlags, Variant.VARIANT.ByReference pVal, IntByReference plFlavor) {
         return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), wszName, lFlags, pVal, plFlavor}, WinNT.HRESULT.class);
      }

      public String Get(String wszName) {
         WString wszNameStr = new WString(wszName);
         Variant.VARIANT.ByReference pQualifierVal = new Variant.VARIANT.ByReference();
         WinNT.HRESULT hres = this.Get(wszNameStr, 0, pQualifierVal, (IntByReference)null);
         if (hres.intValue() == -2147217406) {
            return null;
         } else {
            int qualifierInt = pQualifierVal.getVarType().intValue();
            switch(qualifierInt) {
            case 8:
               return pQualifierVal.stringValue();
            case 11:
               return String.valueOf(pQualifierVal.booleanValue());
            default:
               return null;
            }
         }
      }

      public WinNT.HRESULT GetNames(int lFlags, PointerByReference pNames) {
         return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[]{this.getPointer(), lFlags, pNames}, WinNT.HRESULT.class);
      }

      public String[] GetNames() {
         PointerByReference pbr = new PointerByReference();
         COMUtils.checkRC(this.GetNames(0, pbr));
         Object[] nameObjects = (Object[])((Object[])OaIdlUtil.toPrimitiveArray(new OaIdl.SAFEARRAY(pbr.getValue()), true));
         String[] qualifierNames = new String[nameObjects.length];

         for(int i = 0; i < nameObjects.length; ++i) {
            qualifierNames[i] = (String)nameObjects[i];
         }

         return qualifierNames;
      }
   }

   public static class IWbemClassObject extends Unknown {
      public IWbemClassObject() {
      }

      public IWbemClassObject(Pointer pvInstance) {
         super(pvInstance);
      }

      public WinNT.HRESULT Get(WString wszName, int lFlags, Variant.VARIANT.ByReference pVal, IntByReference pType, IntByReference plFlavor) {
         return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[]{this.getPointer(), wszName, lFlags, pVal, pType, plFlavor}, WinNT.HRESULT.class);
      }

      public WinNT.HRESULT Get(String wszName, int lFlags, Variant.VARIANT.ByReference pVal, IntByReference pType, IntByReference plFlavor) {
         return this.Get(wszName == null ? null : new WString(wszName), lFlags, pVal, pType, plFlavor);
      }

      public WinNT.HRESULT GetNames(String wszQualifierName, int lFlags, Variant.VARIANT.ByReference pQualifierVal, PointerByReference pNames) {
         return this.GetNames(wszQualifierName == null ? null : new WString(wszQualifierName), lFlags, pQualifierVal, pNames);
      }

      public WinNT.HRESULT GetNames(WString wszQualifierName, int lFlags, Variant.VARIANT.ByReference pQualifierVal, PointerByReference pNames) {
         return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[]{this.getPointer(), wszQualifierName, lFlags, pQualifierVal, pNames}, WinNT.HRESULT.class);
      }

      public String[] GetNames(String wszQualifierName, int lFlags, Variant.VARIANT.ByReference pQualifierVal) {
         PointerByReference pbr = new PointerByReference();
         COMUtils.checkRC(this.GetNames(wszQualifierName, lFlags, pQualifierVal, pbr));
         Object[] nameObjects = (Object[])((Object[])OaIdlUtil.toPrimitiveArray(new OaIdl.SAFEARRAY(pbr.getValue()), true));
         String[] names = new String[nameObjects.length];

         for(int i = 0; i < nameObjects.length; ++i) {
            names[i] = (String)nameObjects[i];
         }

         return names;
      }

      public WinNT.HRESULT GetQualifierSet(PointerByReference ppQualSet) {
         return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[]{this.getPointer(), ppQualSet}, WinNT.HRESULT.class);
      }

      public Wbemcli.IWbemQualifierSet GetQualifierSet() {
         PointerByReference ppQualSet = new PointerByReference();
         WinNT.HRESULT hr = this.GetQualifierSet(ppQualSet);
         COMUtils.checkRC(hr);
         Wbemcli.IWbemQualifierSet qualifier = new Wbemcli.IWbemQualifierSet(ppQualSet.getValue());
         return qualifier;
      }

      public WinNT.HRESULT GetPropertyQualifierSet(WString wszProperty, PointerByReference ppQualSet) {
         return (WinNT.HRESULT)this._invokeNativeObject(11, new Object[]{this.getPointer(), wszProperty, ppQualSet}, WinNT.HRESULT.class);
      }

      public Wbemcli.IWbemQualifierSet GetPropertyQualifierSet(String strProperty) {
         WString wszProperty = new WString(strProperty);
         PointerByReference ppQualSet = new PointerByReference();
         COMUtils.checkRC(this.GetPropertyQualifierSet(wszProperty, ppQualSet));
         Wbemcli.IWbemQualifierSet qualifier = new Wbemcli.IWbemQualifierSet(ppQualSet.getValue());
         return qualifier;
      }
   }

   public interface WBEM_CONDITION_FLAG_TYPE {
      int WBEM_FLAG_ALWAYS = 0;
      int WBEM_FLAG_ONLY_IF_TRUE = 1;
      int WBEM_FLAG_ONLY_IF_FALSE = 2;
      int WBEM_FLAG_ONLY_IF_IDENTICAL = 3;
      int WBEM_MASK_PRIMARY_CONDITION = 3;
      int WBEM_FLAG_KEYS_ONLY = 4;
      int WBEM_FLAG_REFS_ONLY = 8;
      int WBEM_FLAG_LOCAL_ONLY = 16;
      int WBEM_FLAG_PROPAGATED_ONLY = 32;
      int WBEM_FLAG_SYSTEM_ONLY = 48;
      int WBEM_FLAG_NONSYSTEM_ONLY = 64;
      int WBEM_MASK_CONDITION_ORIGIN = 112;
      int WBEM_FLAG_CLASS_OVERRIDES_ONLY = 256;
      int WBEM_FLAG_CLASS_LOCAL_AND_OVERRIDES = 512;
      int WBEM_MASK_CLASS_CONDITION = 768;
   }
}
