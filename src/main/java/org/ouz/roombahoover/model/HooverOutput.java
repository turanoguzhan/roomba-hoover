package org.ouz.roombahoover.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HooverOutput {
    private int[] coords;
    private int patches;
}
