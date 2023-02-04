package cifpcm.es.GilPlasenciaEduardoMyIkea.controllers;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProductoService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProvinciaService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Cart;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.User;
import cifpcm.es.GilPlasenciaEduardoMyIkea.services.UserServiceDB;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Controller
public class ProductoController {
  @Autowired
  ProductoService productoService;
  @Autowired
  UserServiceDB userService;
  @Autowired
  ProvinciaService provinciaService;
  public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images/";
  @GetMapping("/")
  public String Start(){
    return "/common/welcome";
  }
  @GetMapping("/products")
  public String List(Model ViewData){
    ViewData.addAttribute("productList",productoService.getProductList());
    return "/products/list";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/products/create")
  public String Create(Model ViewData){
    Producto newProduct = new Producto();
    ViewData.addAttribute("newProduct", newProduct);
    ViewData.addAttribute("provinceList", provinciaService.getProvinciaList());
    return "/products/create";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/products/create")
  public String Create(@Valid @ModelAttribute("newProduct") Producto newProduct, BindingResult bindingResult, Model ViewData, @RequestParam("img")MultipartFile img){
    StringBuilder fileNames = new StringBuilder();
    newProduct.setProduct_picture(img.getOriginalFilename());
    if(bindingResult.hasErrors()){
      ViewData.addAttribute("provinceList", provinciaService.getProvinciaList());
      return "/products/create";
    }
    try {
      Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY,img.getOriginalFilename());
      fileNames.append(img.getOriginalFilename());
      Files.write(fileNameAndPath,img.getBytes());
    }
    catch (IOException exception){
      ViewData.addAttribute("productList",productoService.getProductList());
      ViewData.addAttribute("error",exception.getMessage());
      return "/products/list";
    }
    if(productoService.addProduct(newProduct)){
      return "redirect:/products";
    }
    ViewData.addAttribute("productList",productoService.getProductList());
    ViewData.addAttribute("error","No se ha podido crear el producto, fallo de servidor");
    return "/products/list";
  }
  @GetMapping("/products/details/{id}")
  public String Details(@PathVariable String id, Model ViewData){

    Optional<Producto> foundProduct = productoService.findProduct(Integer.parseInt(id));

    if (foundProduct.isEmpty()){
      ViewData.addAttribute("error","El producto con id " + id + " no existe");
      ViewData.addAttribute("productList",productoService.getProductList());
      return "/products/list";
    }
    ViewData.addAttribute("product" , foundProduct.get());
    return "/products/details";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/products/delete/{id}")
  public String Delete(@PathVariable String id, Model ViewData){

    Optional<Producto> productToDelete = productoService.findProduct(Integer.parseInt(id));

    if (productToDelete.isEmpty()){
      ViewData.addAttribute("error","El producto con id " + id + " no existe");
      ViewData.addAttribute("productList",productoService.getProductList());
      return "/products/list";
    }
    ViewData.addAttribute("product",productToDelete.get());
    return "/products/delete";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/products/delete/{id}")
  public String DeletePost(@PathVariable String id, Model ViewData){
    if(!productoService.deleteProduct(Integer.parseInt(id))){
      ViewData.addAttribute("error","No se ha podido eliminar el animal");
      ViewData.addAttribute("productList",productoService.getProductList());
      return "/products/list";
    }
    return "redirect:/products";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/products/update/{id}")
  public String Update(@PathVariable String id, Model ViewData){
    Optional<Producto> productoToUpdate = productoService.findProduct(Integer.parseInt(id));
    if (productoToUpdate.isEmpty()){
      ViewData.addAttribute("error","El producto con id " + id + " no existe");
      ViewData.addAttribute("productList",productoService.getProductList());
      return "/products/list";
    }
    ViewData.addAttribute("product",productoToUpdate.get());
    ViewData.addAttribute("provinceList", provinciaService.getProvinciaList());
    return "/products/update";
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/products/update/{id}")
  public String Update(@Valid @ModelAttribute("product") Producto modifiedProduct, BindingResult bindingResult, Model ViewData){
    if(bindingResult.hasErrors()){
      ViewData.addAttribute("product",modifiedProduct);
      return "/products/update";
    }
    if(!productoService.updateProduct(modifiedProduct)){
      ViewData.addAttribute("productList",productoService.getProductList());
      ViewData.addAttribute("error","No se ha podido modificar el objeto por un fallo del servidor");
      return "/products/list";
    }
    return "redirect:/products";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/products/addToCart/{id}")
  public String addToCart(@PathVariable String id, Authentication authentication, Model ViewData){
    Optional<User> userQuery = userService.findUserByEmail(authentication.getName());
    if (userQuery.isEmpty()){
      ViewData.addAttribute("error","No se ha podido añadir el objeto al carrito (Usuario no identificado)");
      return "/products/list";
    }
    User user = userQuery.get();
    Optional<Producto> productQuery = productoService.findProduct(Integer.parseInt(id));
    if(productQuery.isEmpty()){
      ViewData.addAttribute("error","No se ha podido añadir el objeto al carrito (El producto con id: " + id + " no existe.");
      return "/products/list";
    }
    Producto product = productQuery.get();
    Cart userCart = userQuery.get().getCart();
    userCart.addProduct(product);
    userService.saveUserCart(user);
    return "redirect:/products/cart";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/products/removeFromCart/{id}")
  public String removeFromCart(@PathVariable String id, Authentication authentication, Model ViewData){
    Optional<User> userQuery = userService.findUserByEmail(authentication.getName());
    if (userQuery.isEmpty()){
      ViewData.addAttribute("error","No se ha podido eliminar el objeto del carrito (Usuario no identificado)");
      return "/products/list";
    }
    User user = userQuery.get();
    Optional<Producto> productQuery = productoService.findProduct(Integer.parseInt(id));
    if(productQuery.isEmpty()){
      ViewData.addAttribute("error","No se ha podido eliminar el objeto del carrito (El producto con id: " + id + " no existe.");
      return "/products/list";
    }
    Producto product = productQuery.get();
    Cart userCart = userQuery.get().getCart();
    userCart.removeProduct(product);
    userService.saveUserCart(user);
    return "redirect:/products/cart";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/products/cart")
  public String showCart(Authentication authentication, Model ViewData){
    Optional<User> userQuery = userService.findUserByEmail(authentication.getName());
    if (userQuery.isEmpty()){
      ViewData.addAttribute("error","(Usuario no identificado)");
      return "/products/list";
    }
    User user = userQuery.get();
    Cart cart = user.getCart();
    List<Producto> cartList = cart.getProductList();
    List<Producto> noRepetitionProductList = new ArrayList<>();
    int cartTotal = 0;
    for(Producto product : cartList){
      if(noRepetitionProductList.contains(product))
        product.plusOne();
      else
        noRepetitionProductList.add(product);
      cartTotal += product.getProduct_price();
    }
    ViewData.addAttribute("totalPrice", cartTotal);
    ViewData.addAttribute("cart",noRepetitionProductList);
    return "/products/cart";
  }
}
