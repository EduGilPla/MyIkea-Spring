package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue
  private int id;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User buyer;

  @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
  @JoinTable(
      name = "ORDER_PRODUCT",
      joinColumns = { @JoinColumn(name = "order_id")},
      inverseJoinColumns = { @JoinColumn(name = "product_id")}
  )
  private List<Producto> products;
  private Date orderDate;

  public Order(List<Producto> Products, User Buyer){
    buyer = Buyer;
    products = Products;
    orderDate = new Date();
  }
}