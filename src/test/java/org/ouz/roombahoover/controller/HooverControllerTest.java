package org.ouz.roombahoover.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.ouz.roombahoover.config.SecurityConfig;
import org.ouz.roombahoover.model.*;
import org.ouz.roombahoover.service.HooverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(HooverController.class)
public class HooverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HooverService hooverService;

    private RawInput rawInput;
    private String rawInputJson;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);

        rawInput = new RawInput();
        rawInput.setRoomSize(new int[]{5,5});
        rawInput.setCoords(new int[]{1,2});
        rawInput.setPatches(List.of(new int[]{1,0},new int[]{2,2},new int[]{2,3}));
        rawInput.setInstructions("NNESEESWNWW");

        objectMapper = new ObjectMapper();

        rawInputJson = objectMapper.writeValueAsString(rawInput);

    }

    @Test
    @WithMockUser(username = "admin", password = "123456")
    public void testMoveToCleanRoom() throws Exception {
        HooverOutput expectedOutput = new HooverOutput(new int[]{1, 3}, 1);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawInputJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    @WithMockUser(username = "admin")
    public void testCleanRoom_InvalidInput() throws Exception {

        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCleanRoom_Unauthorized() throws Exception {

        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawInputJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom_NoPatches() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{1,3}, 0);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawInputJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":[1,3],\"patches\":0}"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom_EmptyInstructions() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{1,2}, 0);

        rawInput.setInstructions("");
        rawInputJson = objectMapper.writeValueAsString(rawInput);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawInputJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":[1,2],\"patches\":0}"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testMoveToCleanRoom_OutOfBounds() throws Exception {

        HooverOutput expectedOutput = new HooverOutput(new int[]{5,5}, 0);

        when(hooverService.move(any(HooverInput.class))).thenReturn(expectedOutput);

        mockMvc.perform(post("/api/robot/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawInputJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"coords\":[5,5],\"patches\":0}"));
    }
}
