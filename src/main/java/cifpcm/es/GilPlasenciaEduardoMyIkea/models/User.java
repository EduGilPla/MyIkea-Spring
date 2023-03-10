package cifpcm.es.GilPlasenciaEduardoMyIkea.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "El nombre es obligatorio")
    @NotNull
    @Size(min = 1, max = 20,message = "El nombre debe estar entre 1 y 20 caracteres")
    private String name;
    @NotBlank(message = "Los apellidos son obligatorios")
    @NotNull
    @Size(min = 1, max = 40,message = "Los apellidos deben estar entre 1 y 40 caracteres")
    private String surname;
    @NotBlank(message = "El email es obligatorio")
    @NotNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",message = "El email no tiene el formato correcto")
    @Size(min = 1, max = 30,message = "El nombre debe estar entre 1 y 30 caracteres")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    @NotNull
    @Size(min = 4,message = "La contraseña debe tener al menos 4 caracteres")
    private String password;
    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(
        name = "USER_ROLE",
        joinColumns = { @JoinColumn(name = "user_id")},
        inverseJoinColumns = { @JoinColumn(name = "role_id")}
    )
    private List<Role> roles;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    @PrimaryKeyJoinColumn
    private Cart cart;
    @OneToMany(mappedBy = "buyer",
        cascade = CascadeType.MERGE,
        orphanRemoval = true)
    private List<Order> orders;

    public User(){
        cart = new Cart(this);
    }
    public User(String Name, String Surname, String Email, String Password){
        name = Name;
        surname = Surname;
        email = Email;
        password = Password;
        cart = new Cart(this);
    }
    public void addOrder(Order newOrder){
        orders.add(newOrder);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
