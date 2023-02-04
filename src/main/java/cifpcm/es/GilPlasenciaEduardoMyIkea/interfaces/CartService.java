package cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces;

import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Cart;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Producto;

import java.util.Optional;

public interface CartService {
  Optional<Cart> findCart(int id);
}
