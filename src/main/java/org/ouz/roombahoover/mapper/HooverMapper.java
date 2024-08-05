package org.ouz.roombahoover.mapper;

import org.mapstruct.Mapping;
import org.ouz.roombahoover.model.HooverOutput;
import org.ouz.roombahoover.model.Position;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HooverMapper {
	HooverMapper INSTANCE = Mappers.getMapper(HooverMapper.class);

	@Mapping(target="coords", source="position")
	@Mapping(target="patches", source="cleanedPatches")
	HooverOutput toHooverOutput(Position position, int cleanedPatches);
}