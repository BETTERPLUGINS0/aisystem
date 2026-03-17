/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import org.hjson.IHjsonDsfProvider;

public class HjsonOptions {
    private IHjsonDsfProvider[] dsf = new IHjsonDsfProvider[0];
    private boolean legacyRoot = true;

    public IHjsonDsfProvider[] getDsfProviders() {
        return (IHjsonDsfProvider[])this.dsf.clone();
    }

    public void setDsfProviders(IHjsonDsfProvider[] iHjsonDsfProviderArray) {
        this.dsf = (IHjsonDsfProvider[])iHjsonDsfProviderArray.clone();
    }

    public boolean getParseLegacyRoot() {
        return this.legacyRoot;
    }

    public void setParseLegacyRoot(boolean bl) {
        this.legacyRoot = bl;
    }

    @Deprecated
    public boolean getEmitRootBraces() {
        return true;
    }

    @Deprecated
    public void setEmitRootBraces(boolean bl) {
    }
}

