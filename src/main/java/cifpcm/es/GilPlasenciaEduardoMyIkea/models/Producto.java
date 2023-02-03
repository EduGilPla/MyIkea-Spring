package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productoffer")
public class Producto {
  @Id
  @GeneratedValue
  private int product_id;
  @NotBlank(message = "El nombre es obligatorio")
  @NotNull
  @Size(min = 1, max = 20,message = "El nombre debe estar entre 1 y 20 caracteres")
  private String product_name;
  @NotNull(message = "El precio no puede ser nulo")
  @Min(value = 1,message = "El producto debe valer al menos 1 euro")
  private int product_price;
  @NotBlank(message = "Debes seleccionar un archivo")
  @NotNull(message = "Debes seleccionar una foto")
  @Size(max = 45,message = "El nombre de archivo es demasiado largo (mas de 45 caracteres)")
  private String product_picture;
  @ManyToOne
  @JoinColumn(name = "id_municipio")
  @NotNull(message = "Debes seleccionar un municipio")
  private Municipio municipio;
  @NotNull(message = "El valor de productos no puede ser nulo")
  @Min(value = 1,message = "Debe haber al menos 1 unidad en stock")
  private int product_stock;
}
