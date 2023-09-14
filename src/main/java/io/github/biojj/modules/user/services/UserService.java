package io.github.biojj.modules.user.services;

import io.github.biojj.exception.UserExistingException;
import io.github.biojj.modules.user.model.User;
import io.github.biojj.modules.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User usuario) {
        boolean exists = repository.existsByUsername(usuario.getUsername());
        if (exists) {
            throw new UserExistingException(usuario.getUsername());
        }
        return repository.save(usuario);
    }

    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public User findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User não encontrado"));
    }

    public void delete(Long id) {
        repository
                .findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User não encontrado"));
    }

    public void update(Long id,
                       User userDto) {

        repository
                .findById(id)
                .map(user -> {
                    user.setName(userDto.getName());

                    return repository.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User não encontrado"));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        io.github.biojj.modules.user.model.User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado."));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build()
                ;

    }
}
