package requestflow.user.mapper;

import requestflow.user.User;
import requestflow.user.dto.UserDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt());
    }
}
