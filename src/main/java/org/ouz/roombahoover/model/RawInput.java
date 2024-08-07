package org.ouz.roombahoover.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RawInput {
	@NotNull
	@Size(min = 1)
	private int[] roomSize;

	@NotNull
	@Size(min = 1)
	private int[] coords;

	private List<int[]> patches;

	private String instructions;

    public boolean checkAllParametersNull() {
		return roomSize==null && coords==null && patches==null && instructions==null;
    }
}