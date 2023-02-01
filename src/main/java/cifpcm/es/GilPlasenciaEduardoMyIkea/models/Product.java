package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Product {
  @Id
  @GeneratedValue
  private int id;
  @NotBlank
  @Size(min = 1, max = 20)
  private String name;

  private User buyer;
}
