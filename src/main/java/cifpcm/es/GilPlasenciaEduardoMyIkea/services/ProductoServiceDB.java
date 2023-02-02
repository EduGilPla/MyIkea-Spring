package cifpcm.es.GilPlasenciaEduardoMyIkea.services;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProductoService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;
import cifpcm.es.GilPlasenciaEduardoMyIkea.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ProductoServiceDB implements ProductoService {
  @Autowired
  ProductoRepository productoRepository;
  private final boolean OPERATION_SUCCESS = true;
  private final boolean OPERATION_FAILED = false;
  public List<Producto> getProductList(){ return productoRepository.findAll();}

  @Override
  public boolean addProduct(Producto newProduct){
    try{
      productoRepository.save(newProduct);
      return OPERATION_SUCCESS;
    }
    catch (Exception exception){
      System.out.println(exception);
      return OPERATION_FAILED;
    }
  }

  @Override
  public boolean deleteProduct(int id) {
    try {
      productoRepository.deleteById(id);
      return OPERATION_SUCCESS;
    }
    catch (Exception exception){
      System.out.println(exception);
      return OPERATION_FAILED;
    }
  }

  @Override
  public boolean updateProduct(Producto toUpdate) {
    try{
      productoRepository.save(toUpdate);
      return OPERATION_SUCCESS;
    }
    catch (Exception exception){
      System.out.println(exception);
      return OPERATION_FAILED;
    }
  }

  @Override
  public Optional<Producto> findProduct(int id) { return productoRepository.findById(id); }
}
