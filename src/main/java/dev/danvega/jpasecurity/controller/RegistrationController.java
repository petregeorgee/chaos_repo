package dev.danvega.jpasecurity.controller;


import dev.danvega.jpasecurity.model.User;
import dev.danvega.jpasecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class RegistrationController
{
    @Autowired
    UserRepository users;

    @PostMapping(
            value = "/new",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})    public ResponseEntity<User> register(@RequestBody User user)
    {
        validateUser(user);
        users.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public @ResponseBody User getUser(@PathVariable("username") String username)
    {
        User user = users.findByUsername(username).get();
        return user;
    }

    private void validateUser(User user)
    {

    }
}
