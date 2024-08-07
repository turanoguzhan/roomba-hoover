package org.ouz.roombahoover.controller;

import org.ouz.roombahoover.model.HooverInput;
import org.ouz.roombahoover.model.HooverOutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ouz.roombahoover.model.Position;
import org.ouz.roombahoover.model.Room;
import org.ouz.roombahoover.service.HooverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HooverController.class)
public class HooverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HooverService hooverService;

    private HooverInput input;

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
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{1,3}, 1);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        // Act & Assert
        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomSize\":{\"width\":5,\"height\":5},\"coords\":{\"x\":1,\"y\":2},\"patches\":[{\"x\":1,\"y\":0},{\"x\":2,\"y\":2},{\"x\":2,\"y\":3}],\"instructions\":\"NNESEESWNWW\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":{\"x\":1,\"y\":3},\"patches\":1}"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testCleanRoom_InvalidInput() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCleanRoom_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomSize\":{\"width\":5,\"height\":5},\"coords\":{\"x\":1,\"y\":2},\"patches\":[{\"x\":1,\"y\":0},{\"x\":2,\"y\":2},{\"x\":2,\"y\":3}],\"instructions\":\"NNESEESWNWW\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom_NoPatches() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{1,3}, 0);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        // Act & Assert
        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomSize\":{\"width\":5,\"height\":5},\"coords\":{\"x\":1,\"y\":2},\"patches\":[],\"instructions\":\"NNESEESWNWW\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":{\"x\":1,\"y\":3},\"patches\":0}"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom_EmptyInstructions() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{1,2}, 0);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        // Act & Assert
        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomSize\":{\"width\":5,\"height\":5},\"coords\":{\"x\":1,\"y\":2},\"patches\":[{\"x\":1,\"y\":0},{\"x\":2,\"y\":2},{\"x\":2,\"y\":3}],\"instructions\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":{\"x\":1,\"y\":2},\"patches\":0}"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom_OutOfBounds() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{5,5}, 0);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        // Act & Assert
        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomSize\":{\"width\":5,\"height\":5},\"coords\":{\"x\":1,\"y\":2},\"patches\":[{\"x\":1,\"y\":0},{\"x\":2,\"y\":2},{\"x\":2,\"y\":3}],\"instructions\":\"NNNNNNNNNN\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":{\"x\":5,\"y\":5},\"patches\":0}"));
    }
}
