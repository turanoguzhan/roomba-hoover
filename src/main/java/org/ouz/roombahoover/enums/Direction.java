package org.ouz.roombahoover.enums;

public enum Direction {
    N("NORTH"), E("EAST"), S("SOUTH"), W("WEST");

    public final String value;

    Direction(String val)
    {
        this.value = val;
    }
}
