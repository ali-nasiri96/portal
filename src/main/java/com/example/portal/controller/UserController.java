package com.example.portal.controller;


import com.example.portal.model.dto.request.UserLoginRequest;
import com.example.portal.model.dto.request.UserRegistrationRequest;
import com.example.portal.model.dto.response.BasicResponse;
import com.example.portal.model.dto.response.UserLoginResponse;
import com.example.portal.model.dto.response.admin.UserResponse;
import com.example.portal.model.exception.WebException;
import com.example.portal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user")
@RestController
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/userRegistration")
    ResponseEntity<BasicResponse> userRegistration(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) throws WebException {
        BasicResponse basicResponse = userService.userRegistration(userRegistrationRequest);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }


    @PostMapping("/userLogin")
    ResponseEntity<UserLoginResponse> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest) throws WebException {
        UserLoginResponse userLogin = userService.userLogin(userLoginRequest);
        return new ResponseEntity<>(userLogin, HttpStatus.OK);
    }


}
