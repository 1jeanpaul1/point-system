package com.fcgl.madrid.points.controller;

import com.fcgl.madrid.points.model.UserAction;
import com.fcgl.madrid.points.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points/user_actions/v1")
public class UserActionController {
    private UserActionService userActionsService;

    @Autowired
    public void setPostService(UserActionService userActionsService) {
        this.userActionsService = userActionsService;
    }

    @GetMapping("/all")
    public List<UserAction> findAll() {
        return userActionsService.findAll();
    }
}
