package mk.ukim.finki.fooddeliverybackend.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import mk.ukim.finki.fooddeliverybackend.model.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customer_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Dish> dishes;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(User user) {
        this.user = user;
        dishes = new ArrayList<>();
        status = OrderStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void confirm() {
        status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        status = OrderStatus.CANCELED;
    }

}
