package image.encrypt.decrypt.service;

import image.encrypt.decrypt.dto.UserDto;
import image.encrypt.decrypt.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
