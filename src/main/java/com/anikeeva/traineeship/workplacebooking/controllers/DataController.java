package com.anikeeva.traineeship.workplacebooking.controllers;

import com.anikeeva.traineeship.workplacebooking.dto.UserForSelectDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForSelectDTO;
import com.anikeeva.traineeship.workplacebooking.services.UserService;
import com.anikeeva.traineeship.workplacebooking.services.WorkplaceService;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    private final WorkplaceService workplaceService;
    private final UserService userService;

    public DataController(WorkplaceService workplaceService, UserService userService) {
        this.workplaceService = workplaceService;
        this.userService = userService;
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getData(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        try {
            List<WorkplaceForSelectDTO> workplaces = workplaceService.showAllWorkplaceForSelect(currentUser);
            List<UserForSelectDTO> users = userService.showAllUsersForSelect();
            boolean isAdmin = userService.checkAdminRole(currentUser.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("workplaces", workplaces);
            response.put("users", users);
            response.put("isAdmin", isAdmin);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}