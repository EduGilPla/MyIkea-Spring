package cifpcm.es.GilPlasenciaEduardoMyIkea.services;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.CartService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Cart;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;
import cifpcm.es.GilPlasenciaEduardoMyIkea.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class CartServiceDB implements CartService {
  @Autowired
  CartRepository cartRepository;

  @Override
  public Optional<Cart> findCart(int id){ return cartRepository.findById(id); }
}
