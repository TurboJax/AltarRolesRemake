package org.turbojax.altarroles;

public enum Role {
    TRUE_HUMAN,
    TRUE_VAMPIRE,
    TRUE_PALE,
    TEMP_HUMAN,
    TEMP_VAMPIRE,
    TEMP_PALE;


    // TRUE_HUMAN can only be converted when killed by a legendary weapon
    // TRUE_VAMPIRE can only be converted when killed by a legendary weapon
    // TRUE_PALE can only be converted when killed by a legendary weapon
    // TEMP_HUMAN are converted to the team of the person who killed them, but cannot become human
    // TEMP_VAMPIRE are converted to the team of the person who killed them, but cannot become human
    // TEMP_PALE are converted to the team of the person who killed them, but cannot become human
    // if you die with pale rot effect, you get hidden rot
    // if player kills someone with the hyperion, they become a TRUE_HUMAN
    // You cannot become a TEMP_HUMAN again.

    public boolean isHuman() {
        return this == TRUE_HUMAN || this == TEMP_HUMAN;
    }

    public boolean isVampire() {
        return this == TRUE_VAMPIRE || this == TEMP_VAMPIRE;
    }

    public boolean isPale() {
        return this == TRUE_PALE || this == TEMP_PALE;
    }

    public boolean isTrue() {
        return this == TRUE_HUMAN || this == TRUE_VAMPIRE || this == TRUE_PALE;
    }
}