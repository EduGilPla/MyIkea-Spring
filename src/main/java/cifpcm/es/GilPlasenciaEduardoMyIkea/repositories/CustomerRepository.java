package cifpcm.es.GilPlasenciaEduardoMyIkea.repositories;

import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
