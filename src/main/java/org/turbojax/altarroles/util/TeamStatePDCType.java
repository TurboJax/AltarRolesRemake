package org.turbojax.altarroles.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.turbojax.altarroles.TeamState;

public class TeamStatePDCType implements PersistentDataType<Boolean, TeamState> {
    @Override
    public @NotNull Class<Boolean> getPrimitiveType() {
        return Boolean.class;
    }

    @Override
    public @NotNull Class<TeamState> getComplexType() {
        return TeamState.class;
    }

    @Override
    public @NonNull Boolean toPrimitive(@NonNull TeamState complex, @NotNull PersistentDataAdapterContext context) {
        return complex == TeamState.TRUE;
    }

    @Override
    public @NonNull TeamState fromPrimitive(@NonNull Boolean primitive, @NotNull PersistentDataAdapterContext context) {
        return primitive ? TeamState.TRUE : TeamState.TEMP;
    }
}