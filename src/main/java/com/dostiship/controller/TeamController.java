package com.dostiship.controller;

import com.dostiship.model.TeamMember;
import com.dostiship.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@Tag(name = "Team Information", description = "Team members information APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    @Operation(summary = "Get all team members", description = "Get a list of all team members for the 'Meet the Team' section")
    public ResponseEntity<List<TeamMember>> getAllTeamMembers() {
        List<TeamMember> teamMembers = teamService.getAllTeamMembers();
        return ResponseEntity.ok(teamMembers);
    }
}