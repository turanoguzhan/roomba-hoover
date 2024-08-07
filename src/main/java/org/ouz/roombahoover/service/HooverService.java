package org.ouz.roombahoover.service;

import lombok.extern.slf4j.Slf4j;
import org.ouz.roombahoover.enums.Direction;
import org.ouz.roombahoover.mapper.HooverMapper;
import org.ouz.roombahoover.model.HooverInput;
import org.ouz.roombahoover.model.HooverOutput;
import org.ouz.roombahoover.model.Position;
import org.ouz.roombahoover.model.Room;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class HooverService {

    /**
     * move the robot based on the instructions.
     * Respectively, ;
     * Validate input data
     * start from the initial coordinates and then visit positions based on instructions
     * keep visited coordinates
     * count input patches which ones matches with visited positions
     * @param input : json input data.
     * @return : HooverOutput which is mapped by mapper
     */
    public HooverOutput move(HooverInput input) {

        validateRoomSize(input.getRoomSize());

        validateInitialCoords(input.getCoords(),input.getRoomSize());

        Set<Position> visitedPositions = new HashSet<>();

        int newX = input.getCoords().x();
        int newY = input.getCoords().y();

        for (char command : input.getInstructions().toCharArray()) {

            Direction direction = Direction.valueOf(String.valueOf(command).toUpperCase());

            switch (direction) {
                case N:
                    newY = Math.min(newY + 1, input.getRoomSize().getHeight() - 1);
                    break;
                case S:
                    newY = Math.max(newY - 1, 0);
                    break;
                case E:
                    newX = Math.min(newX + 1, input.getRoomSize().getWidth() - 1);
                    break;
                case W:
                    newX = Math.max(newX - 1, 0);
                    break;
                default:
                    log.warn("Invalid direction charachter : {}", command);
                    break;
            }

            visitedPositions.add(new Position(newX, newY));
        }

        int cleanedPatches = countCleanedPatches(visitedPositions,input.getPatches());

        return HooverMapper.INSTANCE.toHooverOutput(new Position(newX, newY),cleanedPatches);
    }

    /**
     * validate roomSize input parameter.
     * If it's null throw illegalArgumentException with explanatory message.
     * If the room's width or height lesser than 1 then again throw illegalArgumentException with
     * explanatory message.
     * @param roomSize : roomSize values of the input data
     */
    private void validateRoomSize(Room roomSize) {
        if (roomSize == null) {
            throw new IllegalArgumentException("Room size cannot be null Room: " + roomSize);
        }
        if (roomSize.getHeight() < 1 || roomSize.getWidth() < 1) {
            throw new IllegalArgumentException("Room's width and height must be greater than zero. Room: " + roomSize);
        }
    }

    /**
     * validate inital coordinate values.
     * if coords null throw illegalArgumentException with explanatory message.
     * if one of coords lesser than zero then throw illegalArgumentException with explanatory message.
     * if one of coords higher than related roomSize value then throw illegalArgumentException with explanatory message.
     * @param coords : initial robot coordinates
     * @param roomSize : roomSize values of the input data
     */
    private void validateInitialCoords(Position coords,Room roomSize) {
        if (coords == null) {
            throw new IllegalArgumentException("Coords cannot be null. Coords: " + coords);
        }
        if (coords.x() < 0 || coords.y() < 0) {
            throw new IllegalArgumentException("Each point of inital coords must be greater than zero. Coords: " + coords);
        }
        if(coords.x() > roomSize.getWidth() || coords.y() > roomSize.getHeight()) {
            throw new IllegalArgumentException("Each point of inital coords must be less than the room size. Coords: " + coords);
        }
    }

    /**
     * count cleaned patches number.
     * If there is no patches value in the input data then return 0.
     * Otherwise count matches positions with visited pointers.
     * @param visitedPositions : visited positions based on instructions
     * @param patches : patches coordinates from input data
     * @return : count of cleaned patches
     */
    private int countCleanedPatches(Set<Position> visitedPositions, List<Position> patches) {
        if(patches.isEmpty()){
            return 0;
        }

        return (int)patches.stream().filter(visitedPositions::contains).count();
    }


}