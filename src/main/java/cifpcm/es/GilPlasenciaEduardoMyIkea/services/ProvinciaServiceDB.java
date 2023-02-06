package cifpcm.es.GilPlasenciaEduardoMyIkea.services;

import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.ProvinciaService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Provincia;
import cifpcm.es.GilPlasenciaEduardoMyIkea.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class ProvinciaServiceDB implements ProvinciaService {
  @Autowired
  ProvinciaRepository provinciaRepository;
  @Override
  public List<Provincia> getProvinciaList() {
    return provinciaRepository.findAll();
  }
}
