package aChaushev.architects.service;

import aChaushev.architects.model.dto.UserLoginDTO;
import aChaushev.architects.model.dto.UserRegisterDTO;

public interface UserService {

  boolean register(UserRegisterDTO userRegisterDTO);

}
