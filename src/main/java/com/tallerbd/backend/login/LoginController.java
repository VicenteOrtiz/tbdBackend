package com.tallerbd.backend.login;

import com.tallerbd.backend.user.User;
import com.tallerbd.backend.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRepository loginRepository;


    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ResponseEntity login(@RequestBody User user){

        String typedEmail = user.getEmail();
        String typedPassword = user.getPassword();

        User userToBeUsed = userRepository.findByEmail(typedEmail);

        if(userToBeUsed == null){
            return new ResponseEntity<>("ingresa un correo idoneo xfa",HttpStatus.BAD_REQUEST);
        }

        if(typedPassword.compareTo(userToBeUsed.getPassword()) == 0){ //SI ES 0 SE CUMPLE 

            Login login = new Login("NOW()", userToBeUsed, true); 
            loginRepository.save(login);
            

            return new ResponseEntity<>("Login correcto", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Contraseña o Usuario incorrecto", HttpStatus.BAD_REQUEST);
        }

    }


}