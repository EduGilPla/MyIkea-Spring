package cifpcm.es.GilPlasenciaEduardoMyIkea.controllers;


import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Cart;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.User;
import cifpcm.es.GilPlasenciaEduardoMyIkea.services.UserServiceDB;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {
  @Autowired
  UserServiceDB userService;
  private final String ErrorAttributeName = "error";
  @GetMapping("/login")
  public String Login(){
    return "/authentication/login";
  }
  @GetMapping("/register")
  public String Register(Model ViewData){
    ViewData.addAttribute("user", new User());
    return "/authentication/register";
  }
  @PostMapping("/register")
  public String Register(@Valid @ModelAttribute("user") User newUser, BindingResult bindingResult, Model ViewData){
    if(bindingResult.hasErrors()){
      return "/authentication/register";
    }
    newUser.setCart(new Cart(newUser));
    if(userService.registerUser(newUser)){
      String CORRECT_REGISTER_MESSAGE = "¡El usuario " + newUser.getEmail() + " se ha registrado correctamente!";
      ViewData.addAttribute("registerCorrecto",CORRECT_REGISTER_MESSAGE);
      return "/common/welcome";
    }
    ViewData.addAttribute(ErrorAttributeName,"El usuario " + newUser.getEmail() + " ya existe.");
    return "/common/welcome";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/users")
  public String ListUsers(Model ViewData, Authentication authentication){
    List<User> userList = userService.getUserList();
    Optional<User> adminUser = userService.findUserByEmail(authentication.getName());
    userList.remove(adminUser.get());
    ViewData.addAttribute("userList",userList);
    return "/authentication/users";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/users/delete/{id}")
  public String deleteUser(@PathVariable String id, Authentication authentication, Model ViewData){
    Optional<User> deleteQuery = userService.findUserById(Integer.parseInt(id));
    if(deleteQuery.isEmpty()){
      String USER_NOT_FOUND_ERROR = "El usuario con id " + id + "no existe en la base de datos.";
      ViewData.addAttribute(ErrorAttributeName,USER_NOT_FOUND_ERROR);
      return "/authentication/users";
    }
    try {
      userService.deleteUser(deleteQuery.get());
      return "redirect:/users";
    }
    catch (Exception exception){
      ViewData.addAttribute(ErrorAttributeName,exception.getMessage());
      return "/authentication/users";
    }
  }
}
