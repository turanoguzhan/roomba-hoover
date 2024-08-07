package org.ouz.roombahoover.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ouz.roombahoover.mapper.HooverMapper;
import org.ouz.roombahoover.model.HooverInput;
import org.ouz.roombahoover.model.HooverOutput;
import org.ouz.roombahoover.model.Position;
import org.ouz.roombahoover.model.Room;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HooverServiceTest {

    @InjectMocks
    private HooverService hooverService;

    @Mock
    private HooverMapper hooverMapper;

    HooverInput input;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Room roomSize = new Room(5, 5);
        Position initialCoords = new Position(1, 2);
        List<Position> patches = Arrays.asList(new Position(1, 0), new Position(2, 2), new Position(2, 3));
        String instructions = "NNESEESWNWW";
        input = new HooverInput(roomSize, initialCoords, patches, instructions);
    }

    @Test
    public void testMove() {


        int cleanedPatches = 1;
        HooverOutput expectedOutput = new HooverOutput(new int[]{1, 3}, cleanedPatches);

        when(hooverMapper.toHooverOutput(any(Position.class), anyInt())).thenReturn(expectedOutput);

        HooverOutput output = hooverService.move(input);

        assertNotNull(output);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testValidateRoomSize_NullRoom() {
        input.setRoomSize(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                hooverService.move(input));

        assertEquals("Room size cannot be null Room: null", exception.getMessage());
    }

    @Test
    public void testValidateRoomSize_InvalidDimensions() {
        Room invalidRoom = new Room(0, 5);
        input.setRoomSize(invalidRoom);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hooverService.move(input)
        );

        assertEquals("Room's width and height must be greater than zero. Room: Room(width=0, height=5)", exception.getMessage());
    }

    @Test
    public void testValidateInitialCoords_NullCoords() {
        input.setCoords(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hooverService.move(input)
        );

        assertEquals("Coords cannot be null. Coords: null", exception.getMessage());
    }

    @Test
    public void testValidateInitialCoords_InvalidCoords() {
        Room roomSize = new Room(5, 5);
        Position invalidCoords = new Position(-1, 2);
        input.setRoomSize(roomSize);
        input.setCoords(invalidCoords);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hooverService.move(input)
        );

        assertEquals("Each point of inital coords must be greater than zero. Coords: Position[x=-1, y=2]", exception.getMessage());
    }

    @Test
    public void testValidateInitialCoords_ExceedsRoomSize() {
        Room roomSize = new Room(5, 5);
        Position invalidCoords = new Position(6, 2);
        input.setRoomSize(roomSize);
        input.setCoords(invalidCoords);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hooverService.move(input)
        );

        assertEquals("Each point of inital coords must be less than the room size. Coords: Position[x=6, y=2]", exception.getMessage());
    }

    @Test
    public void testCountCleanedPatches_NoPatches() {
        List<Position> patches = Collections.emptyList();
        input.setPatches(patches);

        HooverOutput output = hooverService.move(input);

        assertEquals(0, output.getPatches());
    }

    @Test
    public void testCountCleanedPatches_WithPatches() {
        List<Position> patches = Arrays.asList(new Position(1, 2), new Position(3, 3));

        input.setPatches(patches);

        HooverOutput output = hooverService.move(input);

        assertEquals(1, output.getPatches());
    }
}
