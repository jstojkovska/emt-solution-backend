package mk.ukim.finki.fooddeliverybackend.service.domain.impl;

import mk.ukim.finki.fooddeliverybackend.model.domain.Dish;
import mk.ukim.finki.fooddeliverybackend.model.domain.Order;
import mk.ukim.finki.fooddeliverybackend.model.domain.Restaurant;
import mk.ukim.finki.fooddeliverybackend.model.exceptions.DishOutOfStockException;
import mk.ukim.finki.fooddeliverybackend.repository.DishRepository;
import mk.ukim.finki.fooddeliverybackend.repository.OrderRepository;
import mk.ukim.finki.fooddeliverybackend.service.domain.DishService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public DishServiceImpl(DishRepository dishRepository, OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Dish> findAll() {
        return dishRepository
                .findAll();
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    @Override
    public Dish save(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public Optional<Dish> update(Long id, Dish dish) {
        Dish updated = dishRepository.findById(id).get();
        updated.setName(dish.getName());
        updated.setDescription(dish.getDescription());
        updated.setPrice(dish.getPrice());
        updated.setQuantity(dish.getQuantity());
        updated.setRestaurant(dish.getRestaurant());
        dishRepository.save(updated);
        return Optional.of(updated);
    }

    @Override
    public Optional<Dish> deleteById(Long id) {
        Dish dish = dishRepository.findById(id).get();
        dishRepository.deleteById(id);
        return Optional.of(dish);
    }

    @Override
    public Order addToOrder(Dish dish, Order order) {
        if (dish.getQuantity() == 0) {
            throw new DishOutOfStockException(dish.getId());
        }
        dish.setQuantity(dish.getQuantity() - 1);
        order.getDishes().add(dish);
        orderRepository.save(order);

        return order;
    }

    @Override
    public Order removeFromOrder(Dish dish, Order order) {
        dish.setQuantity(dish.getQuantity() + 1);
        order.getDishes().remove(dish);
        orderRepository.save(order);

        return order;
    }

}
