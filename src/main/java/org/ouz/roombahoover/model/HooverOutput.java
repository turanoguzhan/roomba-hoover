package org.ouz.roombahoover.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HooverOutput {
    private Position coords;
    private int patches;
}
