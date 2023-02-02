package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "productoffer")
public class Producto {
  @Id
  @GeneratedValue
  private int product_id;
  @NotBlank
  @Size(min = 1, max = 20)
  private String product_name;
  @NotBlank
  @NotNull
  private int product_price;
  @NotBlank
  @Size(min = 1, max = 20)
  private String product_picture;
  //Hay que añadir la clase municipio aqui relacion OneToMany
  @ManyToOne
  @JoinColumn(name = "id_municipio")
  private Municipio municipio;
  @NotBlank
  @NotNull
  private int product_stock;
}
