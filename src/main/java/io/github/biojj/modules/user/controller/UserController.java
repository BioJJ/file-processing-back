package io.github.biojj.modules.user.controller;

import io.github.biojj.exception.UserExistingException;
import io.github.biojj.modules.user.model.User;
import io.github.biojj.modules.user.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("registry")
    @ResponseStatus(HttpStatus.CREATED)
    public void registry(@RequestBody @Valid User user) {
        try {
            userService.save(user);
        } catch (UserExistingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid User user) {
        try {
            userService.save(user);
        } catch (UserExistingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userService.findAll(pageable);
    }

    @GetMapping("{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        userService.delete(id);

    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Long id, @RequestBody @Valid User clienteAtualizado) {
        userService.update(id, clienteAtualizado);
    }
}
