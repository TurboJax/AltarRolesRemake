package org.turbojax.altarroles.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.turbojax.altarroles.Role;

public class RolePDCType implements PersistentDataType<Byte, Role> {
    @Override
    public @NotNull Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public @NotNull Class<Role> getComplexType() {
        return Role.class;
    }

    @Override
    public @NonNull Byte toPrimitive(@NonNull Role complex, @NotNull PersistentDataAdapterContext context) {
        return (byte) complex.ordinal();
    }

    @Override
    public @NonNull Role fromPrimitive(@NonNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
        return Role.values()[primitive];
    }
}