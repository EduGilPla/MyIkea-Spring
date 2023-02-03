package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.*;
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
  @Column(name = "user_id")
  private int id;
  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  private User owner;
  //private List<Producto> list;

  public Cart(User Owner){
    id = 0;
    owner = Owner;
  }
}

