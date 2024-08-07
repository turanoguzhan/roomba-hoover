package org.ouz.roombahoover.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ouz.roombahoover.mapper.HooverMapper;
import org.ouz.roombahoover.model.HooverOutput;
import org.ouz.roombahoover.model.RawInput;
import org.ouz.roombahoover.service.HooverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/robot")
@RequiredArgsConstructor
public class HooverController {

	private final HooverService hooverService;

	@Tag(name = "move", description = "POST method to initate the robot to move based on the instruction")
	@PostMapping("/move")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<HooverOutput> move(@RequestBody(
			description = "Input parameter defines room, robot initial position, patches and instructions",
			required = true,
			content = @Content(schema=@Schema(implementation = RawInput.class)))
												 @Valid @org.springframework.web.bind.annotation.RequestBody RawInput rawInput) {

		if(rawInput.checkAllParametersNull()){
			return new ResponseEntity<>(new HooverOutput(),HttpStatus.BAD_REQUEST);
		}
		HooverOutput hooverOutput = hooverService.move(HooverMapper.INSTANCE.toHooverInput(rawInput));
		return new ResponseEntity<>(hooverOutput, HttpStatus.OK);
	}
}