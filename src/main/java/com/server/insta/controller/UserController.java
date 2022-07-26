package com.server.insta.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="User API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
}
