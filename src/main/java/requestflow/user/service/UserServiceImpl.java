package requestflow.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import requestflow.exception.ConflictException;
import requestflow.exception.NotFoundException;
import requestflow.user.User;
import requestflow.user.dto.NewUserDto;
import requestflow.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User save(NewUserDto newUserDto) {

        if (userRepository.existsByEmail(newUserDto.getEmail())) {
            throw new ConflictException("Пользователь с email=" + newUserDto.getEmail() + " уже существует");
        }

        User user = new User();
        user.setName(newUserDto.getName());
        user.setEmail(newUserDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}
