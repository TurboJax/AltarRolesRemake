package org.turbojax.altarroles.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.turbojax.altarroles.Team;

public class TeamPDCType implements PersistentDataType<Byte, Team> {
    @Override
    public @NotNull Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public @NotNull Class<Team> getComplexType() {
        return Team.class;
    }

    @Override
    public @NonNull Byte toPrimitive(@NonNull Team complex, @NotNull PersistentDataAdapterContext context) {
        return (byte) complex.ordinal();
    }

    @Override
    public @NonNull Team fromPrimitive(@NonNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
        return Team.values()[primitive];
    }
}
