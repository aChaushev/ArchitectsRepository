package aChaushev.architects.service;

import aChaushev.architects.model.dto.UserDetailsDTO;
import aChaushev.architects.model.dto.UserLoginDTO;
import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.enums.UserRoleEnum;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean register(UserRegisterDTO userRegisterDTO);


    void saveUser(User user, UserRoleEnum roleEnum);

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    void deleteUser(Long id);

    void updateUser(Long id, UserDetailsDTO userDetails);
}
