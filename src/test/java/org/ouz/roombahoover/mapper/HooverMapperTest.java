package org.ouz.roombahoover.mapper;

import org.junit.jupiter.api.Test;
import org.ouz.roombahoover.model.HooverInput;
import org.ouz.roombahoover.model.RawInput;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HooverMapperTest {

    @Test
    public void testToServiceInput() {

        RawInput input = new RawInput();

        input.setRoomSize(new int[]{5, 5});
        input.setCoords(new int[]{1, 2});

        List<int[]> patches = List.of(new int[]{1, 0}, new int[]{2, 2},  new int[]{2, 3});
        input.setPatches(patches);

        input.setInstructions("NNESEESWNWW");

        HooverInput serviceInput = HooverMapper.INSTANCE.toHooverInput(input);

        assertEquals(5, serviceInput.getRoomSize().getHeight());
        assertEquals(5, serviceInput.getRoomSize().getWidth());
        assertEquals(1, serviceInput.getCoords().x());
        assertEquals(2, serviceInput.getCoords().y());
        assertEquals(3, serviceInput.getPatches().size());
        assertEquals("NNESEESWNWW", serviceInput.getInstructions());
    }
}
