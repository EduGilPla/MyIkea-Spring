package cifpcm.es.GilPlasenciaEduardoMyIkea.services;


import cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces.RoleService;
import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Role;
import cifpcm.es.GilPlasenciaEduardoMyIkea.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class RoleServiceDB implements RoleService {
  @Autowired
  RoleRepository roleRepository;
  @Override
  public List<Role> getRoleList() {
    return roleRepository.findAll();
  }
  @Override
  public Optional<Role> findRoleByName(String name) {
    return roleRepository.findByName(name);
  }
}
