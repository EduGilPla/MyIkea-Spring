package cifpcm.es.GilPlasenciaEduardoMyIkea.controllers;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProductoService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Cart;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Order;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.User;
import cifpcm.es.GilPlasenciaEduardoMyIkea.services.UserServiceDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
  @Autowired
  UserServiceDB userService;
  @Autowired
  ProductoService productoService;
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/addToCart/{id}")
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
    return "redirect:/customer/cart";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/removeFromCart/{id}")
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
    return "redirect:/customer/cart";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/customer/cart")
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
    return "/customer/cart";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/order")
  public String placeOrder(Authentication authentication, Model ViewData){
    Optional<User> userQuery = userService.findUserByEmail(authentication.getName());
    if (userQuery.isEmpty()){
      ViewData.addAttribute("error","(Usuario no identificado)");
      return "/products/list";
    }
    User user = userQuery.get();
    Cart cart = user.getCart();
    if(cart.getProductList().isEmpty()){
      ViewData.addAttribute("error","No puedes hacer un pedido vacío");
      ViewData.addAttribute("totalPrice",0);
      return "/customer/cart";
    }
    Order newOrder = new Order(cart.getProductList(),user);
    user.addOrder(newOrder);
    cart.removeAllProducts();
    userService.saveUserCart(user);
    return "redirect:/customer/orders";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/customer/orders")
  public String showOrders(Authentication authentication, Model ViewData){
    Optional<User> userQuery = userService.findUserByEmail(authentication.getName());
    if (userQuery.isEmpty()){
      ViewData.addAttribute("error","(Usuario no identificado)");
      return "/products/list";
    }
    User user = userQuery.get();
    ViewData.addAttribute("orders",user.getOrders());
    return "/customer/orders";
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/customer/orders/details/{id}")
  public String orderDetails(@PathVariable String id, Authentication authentication, Model ViewData){
    Optional<User> userQuery = userService.findUserByEmail(authentication.getName());
    if (userQuery.isEmpty()){
      ViewData.addAttribute("error","(Usuario no identificado)");
      return "/products/list";
    }
    User user = userQuery.get();
    Order orderToDetail = null;
    for(Order order : user.getOrders()){
      if(order.getId() == Integer.parseInt(id)){
        orderToDetail = order;
        break;
      }
    }
    if(orderToDetail == null){
      ViewData.addAttribute("orders",user.getOrders());
      ViewData.addAttribute("error","El pedido con id " + id + " no existe");
      return "/customer/orders";
    }
    int totalOrderPrice = 0;
    List<Producto> orderProducts = new ArrayList<>();
    for(Producto product : orderToDetail.getProducts()){
      if(orderProducts.contains(product))
        product.plusOne();
      else
        orderProducts.add(product);
      totalOrderPrice += product.getProduct_price();
    }
    ViewData.addAttribute("orderProducts",orderProducts);
    ViewData.addAttribute("total",totalOrderPrice);
    ViewData.addAttribute("order",orderToDetail);
    return "/customer/orderDetails";
  }
}
