package org.ouz.roombahoover.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HooverInput {
	private Room roomSize;
	private Position coords;
	private List<Position> patches;
	private String instructions;
}