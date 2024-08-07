package org.ouz.roombahoover.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.ouz.roombahoover.model.*;

import java.util.List;

@Mapper(imports = Room.class)
public interface HooverMapper {
	HooverMapper INSTANCE = Mappers.getMapper(HooverMapper.class);

	@Mapping(target = "roomSize", source="roomSize", qualifiedByName = "getRoom")
	@Mapping(target = "coords", source="coords", qualifiedByName = "getCoords")
	@Mapping(target = "patches", source = "patches", qualifiedByName = "getPatches")
	HooverInput toHooverInput(RawInput rawInput);

	@Named("getRoom")
	default Room getRoom(int[] roomSize){
		if(roomSize == null || roomSize.length<1){
			return null;
		}
		return new Room(roomSize[0],roomSize[1]);
	}

	@Named("getCoords")
	default Position getCoords(int[] coords){
		if(coords == null || coords.length<1){
			return null;
		}
		return new Position(coords[0],coords[1]);
	}

	@Named("getPatches")
	default List<Position> getPatches(List<int[]> patches){
		if(patches == null || patches.isEmpty()){
			return null;
		}
		return patches.stream().map(patch->new Position(patch[0],patch[1])).toList();
	}

	@Mapping(target="coords", expression="java(new int[]{position.x(),position.y()})")
	@Mapping(target="patches", source="cleanedPatches")
	HooverOutput toHooverOutput(Position position, int cleanedPatches);

}