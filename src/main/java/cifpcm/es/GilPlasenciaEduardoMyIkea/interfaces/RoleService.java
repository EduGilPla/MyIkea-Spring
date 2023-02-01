package cifpcm.es.GilPlasenciaEduardoMyIkea.interfaces;

import cifpcm.es.GilPlasenciaEduardoMyIkea.models.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
  List<Role> getRoleList();

  Optional<Role> findRoleByName(String name);
}
