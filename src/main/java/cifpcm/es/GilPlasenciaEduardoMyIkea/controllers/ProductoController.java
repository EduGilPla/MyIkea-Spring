package cifpcm.es.GilPlasenciaEduardoMyIkea.controllers;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProductoService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProvinciaService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ProductoController {
  @Autowired
  ProductoService productoService;
  @Autowired
  ProvinciaService provinciaService;
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
}
