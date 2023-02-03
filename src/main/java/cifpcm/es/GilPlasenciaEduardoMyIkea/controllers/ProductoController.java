package cifpcm.es.GilPlasenciaEduardoMyIkea.controllers;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProductoService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProvinciaService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class ProductoController {
  @Autowired
  ProductoService productoService;
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
}
