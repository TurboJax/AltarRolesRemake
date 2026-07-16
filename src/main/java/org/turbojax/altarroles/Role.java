package org.turbojax.altarroles;

import net.kyori.adventure.text.Component;

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

    public Role toTemp() {
        return switch (this) {
            case TRUE_HUMAN -> TEMP_HUMAN;
            case TRUE_VAMPIRE -> TEMP_VAMPIRE;
            case TRUE_PALE -> TEMP_PALE;
            default -> this;
        };
    }

    public Role toTrue() {
        return switch (this) {
            case TEMP_HUMAN -> TRUE_HUMAN;
            case TEMP_VAMPIRE -> TRUE_VAMPIRE;
            case TEMP_PALE -> TRUE_PALE;
            default -> this;
        };
    }

    public Component prettyName() {
        return switch (this) {
            case TEMP_HUMAN -> Component.text("Human", MainConfig.getColor(this));
            case TRUE_HUMAN -> Component.text("Human", MainConfig.getColor(this));
            case TEMP_VAMPIRE -> Component.text("Vampire", MainConfig.getColor(this));
            case TRUE_VAMPIRE -> Component.text("Vampire", MainConfig.getColor(this));
            case TEMP_PALE -> Component.text("Pale", MainConfig.getColor(this));
            case TRUE_PALE -> Component.text("Pale", MainConfig.getColor(this));
        };
    }

    public Component prettyLongName() {
        return switch (this) {
            case TEMP_HUMAN -> Component.text("Temp Human", MainConfig.getColor(this));
            case TRUE_HUMAN -> Component.text("True Human", MainConfig.getColor(this));
            case TEMP_VAMPIRE -> Component.text("Temp Vampire", MainConfig.getColor(this));
            case TRUE_VAMPIRE -> Component.text("True Vampire", MainConfig.getColor(this));
            case TEMP_PALE -> Component.text("Temp Pale", MainConfig.getColor(this));
            case TRUE_PALE -> Component.text("True Pale", MainConfig.getColor(this));
        };
    }

    public boolean matchesTeam(Role other) {
        if (isHuman() && other.isHuman()) return true;
        if (isVampire() && other.isVampire()) return true;
        if (isPale() && other.isPale()) return true;
        
        return false;
    }
}