package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
  @Id
  @GeneratedValue
  private int id;
  @OneToOne(mappedBy = "cart")
  private User owner;
  private List<Producto> list;
}
