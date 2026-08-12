package requestflow.user.service;

import requestflow.user.User;
import requestflow.user.dto.NewUserDto;

import java.util.Collection;

public interface UserService {
    User save(NewUserDto newUserDto);

    Collection<User> getAll();

    User getById(Long id);
}
