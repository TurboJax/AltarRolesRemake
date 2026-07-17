package org.turbojax.altarroles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum WorldEvent {
    NONE,
    BLOOD_MOON,
    ETERNAL_NIGHT;

    private static WorldEvent active = NONE;

    public static WorldEvent getActiveEvent() {
        return active;
    }

    public static void setActiveEvent(WorldEvent event) {
        active = event;
    }

    public Component prettyName() {
        return switch (this) {
            case NONE -> Component.text("None");
            case BLOOD_MOON -> Component.text("Blood Moon", NamedTextColor.DARK_RED);
            case ETERNAL_NIGHT -> Component.text("Eternal Night", NamedTextColor.DARK_GRAY);
        };
    }
}