package org.ouz.roombahoover.mapper;

import javax.annotation.processing.Generated;
import org.ouz.roombahoover.model.HooverInput;
import org.ouz.roombahoover.model.HooverOutput;
import org.ouz.roombahoover.model.Position;
import org.ouz.roombahoover.model.RawInput;
import org.ouz.roombahoover.model.Room;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-07T03:17:50+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
public class HooverMapperImpl implements HooverMapper {

    @Override
    public HooverInput toHooverInput(RawInput rawInput) {
        if ( rawInput == null ) {
            return null;
        }

        HooverInput hooverInput = new HooverInput();

        hooverInput.setRoomSize( getRoom( rawInput.getRoomSize() ) );
        hooverInput.setCoords( getCoords( rawInput.getCoords() ) );
        hooverInput.setPatches( getPatches( rawInput.getPatches() ) );
        hooverInput.setInstructions( rawInput.getInstructions() );

        return hooverInput;
    }

    @Override
    public HooverOutput toHooverOutput(Position position, int cleanedPatches) {
        if ( position == null ) {
            return null;
        }

        HooverOutput hooverOutput = new HooverOutput();

        hooverOutput.setPatches( cleanedPatches );
        hooverOutput.setCoords( new int[]{position.x(),position.y()} );

        return hooverOutput;
    }
}
