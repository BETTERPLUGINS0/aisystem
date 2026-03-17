/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedStatusFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.AbstractWrappedFlag;

public class WrappedStatusFlag
extends AbstractWrappedFlag<WrappedState>
implements IWrappedStatusFlag {
    public WrappedStatusFlag(Flag<StateFlag.State> handle) {
        super(handle);
    }

    @Override
    public Optional<WrappedState> fromWGValue(Object value) {
        return Optional.ofNullable(value).map(state -> state == StateFlag.State.ALLOW ? WrappedState.ALLOW : WrappedState.DENY);
    }

    @Override
    public Optional<Object> fromWrapperValue(WrappedState value) {
        return Optional.ofNullable(value).map(state -> state == WrappedState.ALLOW ? StateFlag.State.ALLOW : StateFlag.State.DENY);
    }
}

