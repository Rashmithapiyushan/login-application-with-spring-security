package lk.ijse.dep10.backend.service;

import lk.ijse.dep10.backend.dto.CredentialsDto;
import lk.ijse.dep10.backend.dto.SignUpDto;
import lk.ijse.dep10.backend.dto.UserDto;
import lk.ijse.dep10.backend.entity.User;
import lk.ijse.dep10.backend.exception.AppException;
import lk.ijse.dep10.backend.mappers.UserMapper;
import lk.ijse.dep10.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto){
        User user=userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(()->new AppException("Unknown User", HttpStatus.NOT_FOUND));
        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),user.getPassword())){
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid Password",HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> oUser = userRepository.findByLogin(signUpDto.login());
        if(oUser.isPresent()){
            throw new AppException("Login Already Exists", HttpStatus.BAD_REQUEST);
        }
        User user=userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }
}
