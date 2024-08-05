package org.ouz.roombahoover.controller;

import lombok.RequiredArgsConstructor;
import org.ouz.roombahoover.model.HooverInput;
import org.ouz.roombahoover.model.HooverOutput;
import org.ouz.roombahoover.service.HooverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/robot")
@Api(value = "Roomba Hoover API", tags = {"Roomba Hoover API"})
@RequiredArgsConstructor
public class HooverController {

	private final HooverService hooverService;

    @PostMapping("/move")
	@ApiOperation(value = "Move robot through the given instructions to clean patches" +
			"and then return the latest position and cleaned patches as a result")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<HooverOutput> moveRobot(@RequestBody HooverInput input) {
		HooverOutput output = hooverService.move(input);
		return new ResponseEntity<>(output, HttpStatus.OK);
	}
}