package org.ouz.roombahoover.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HooverInput {
	private Room roomSize;
	private Position coords;
	private List<Position> patches;
	private String instructions;
}