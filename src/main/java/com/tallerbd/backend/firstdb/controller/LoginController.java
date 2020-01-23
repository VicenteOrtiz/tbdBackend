package com.tallerbd.backend.firstdb.controller;

import com.tallerbd.backend.firstdb.domain.Login;
import com.tallerbd.backend.firstdb.domain.User;
import com.tallerbd.backend.firstdb.repo.LoginRepository;
import com.tallerbd.backend.firstdb.repo.UserRepository;

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


    //AQUI ES DONDE SUCEDE LA MAGIA DEL LOGIN, FALTA VALIDAR QUE NO HAYA UN USUARIO LOGEADO YA.
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ResponseEntity login(@RequestBody User user){

        String typedEmail = user.getEmail();
        String typedPassword = user.getPassword();

        User userToBeUsed = userRepository.findByEmail(typedEmail);

        Login lastLogin = loginRepository.findTopByOrderByIdDesc(); //ESTO RETORNA EL ÚLTIMO ELEMENTO DE LA TABLA LOGIN

        if(lastLogin!=null){
            if(lastLogin.getLoginStatus()==true){
                return new ResponseEntity<>("Ya hay un usuario logueado, porfavor cierra sesión antes de iniciar sesión",HttpStatus.BAD_REQUEST);
            }else{
                if(userToBeUsed == null){
                    return new ResponseEntity<>("Contraseña o Usuario incorrecto(correo)",HttpStatus.BAD_REQUEST);
                }
        
                if(typedPassword.compareTo(userToBeUsed.getPassword()) == 0){ //SI ES 0 SE CUMPLE 
        
                    Login login = new Login("NOW()", userToBeUsed, true); 
                    loginRepository.save(login);
                    
        
                    return new ResponseEntity<>("Login correcto", HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Contraseña o Usuario incorrecto", HttpStatus.BAD_REQUEST);
                }
            }
        }else{
            if(userToBeUsed == null){
                return new ResponseEntity<>("Contraseña o Usuario incorrecto(correo)",HttpStatus.BAD_REQUEST);
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

    //ESTE MÉTODO RETORNA EL ÚLTIMO USUARIO ACTUALMENTE LOGEADO, Y SI NO ESTÁ LOGEADO RETORNA -99999999.
    @RequestMapping(value="/login/last", method = RequestMethod.GET)
    public Long getLoggedInUser(){
        Login lastLogin = loginRepository.findTopByOrderByIdDesc(); //ESTO RETORNA EL ÚLTIMO ELEMENTO DE LA TABLA LOGIN
        //Long lastUserLoggedInId;
        User lastUserLoggedIn;
        Long error = new Long(-999999999);
        
        if(lastLogin.getLoginStatus()==true){
            lastUserLoggedIn = lastLogin.getUser();
            return lastUserLoggedIn.getId();
        }else{
            return error;
        }
    }

    //este metodo hace el logout, pero le faltan casos bordes y validaciones
    //hay que ver primero que haya una cuenta para hacer logout.
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public ResponseEntity logOut(){
        Login lastLogin = loginRepository.findTopByOrderByIdDesc();
        lastLogin.setLoginStatus(false);
        loginRepository.save(lastLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}