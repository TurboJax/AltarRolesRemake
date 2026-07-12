package org.turbojax.altarroles;

public enum WorldEvent {
    NONE("<white>None"),
    BLOOD_MOON("<dark_red>Blood Moon"),
    ETERNAL_NIGHT("<dark_gray>Eternal Night");

    private final String str;

    WorldEvent(String str) {
        this.str = str;
    }

    public String asString() {
        return str;
    }
}