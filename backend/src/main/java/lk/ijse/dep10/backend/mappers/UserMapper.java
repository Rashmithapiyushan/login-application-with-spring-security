package lk.ijse.dep10.backend.mappers;

import lk.ijse.dep10.backend.dto.SignUpDto;
import lk.ijse.dep10.backend.dto.UserDto;
import lk.ijse.dep10.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password",ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
