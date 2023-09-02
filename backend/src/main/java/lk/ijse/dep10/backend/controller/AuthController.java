package lk.ijse.dep10.backend.controller;

import lk.ijse.dep10.backend.config.UserAuthProvider;
import lk.ijse.dep10.backend.dto.CredentialsDto;
import lk.ijse.dep10.backend.dto.SignUpDto;
import lk.ijse.dep10.backend.dto.UserDto;
import lk.ijse.dep10.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto){
        UserDto user=userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        UserDto user=userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/users/"+user.getId())).body(user);
    }
}
