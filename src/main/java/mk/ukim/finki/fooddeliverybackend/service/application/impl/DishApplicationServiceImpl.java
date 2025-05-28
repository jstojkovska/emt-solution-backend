package mk.ukim.finki.fooddeliverybackend.service.application.impl;

import mk.ukim.finki.fooddeliverybackend.dto.domain.CreateDishDto;
import mk.ukim.finki.fooddeliverybackend.dto.domain.DisplayDishDetailsDto;
import mk.ukim.finki.fooddeliverybackend.dto.domain.DisplayDishDto;
import mk.ukim.finki.fooddeliverybackend.dto.domain.DisplayOrderDto;
import mk.ukim.finki.fooddeliverybackend.model.domain.Dish;
import mk.ukim.finki.fooddeliverybackend.model.domain.Order;
import mk.ukim.finki.fooddeliverybackend.model.domain.Restaurant;
import mk.ukim.finki.fooddeliverybackend.model.exceptions.DishNotFoundException;
import mk.ukim.finki.fooddeliverybackend.model.exceptions.RestaurantNotFoundException;
import mk.ukim.finki.fooddeliverybackend.service.application.DishApplicationService;
import mk.ukim.finki.fooddeliverybackend.service.domain.DishService;
import mk.ukim.finki.fooddeliverybackend.service.domain.OrderService;
import mk.ukim.finki.fooddeliverybackend.service.domain.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishApplicationServiceImpl implements DishApplicationService {

    private final DishService dishService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;

    public DishApplicationServiceImpl(DishService dishService, RestaurantService restaurantService, OrderService orderService) {
        this.dishService = dishService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @Override
    public List<DisplayDishDto> findAll() {
        return dishService.findAll()
                .stream().map(DisplayDishDto::from).collect(Collectors.toList());
    }

    @Override
    public Optional<DisplayDishDto> findById(Long id) {
        return dishService
                .findById(id)
                .map(DisplayDishDto::from);
    }

    @Override
    public Optional<DisplayDishDetailsDto> findByIdWithDetails(Long id) {
        return dishService.findById(id)
                .map(DisplayDishDetailsDto::from);
    }

    @Override
    public DisplayDishDto save(CreateDishDto createDishDto) {
        Restaurant restaurant = restaurantService.findById(createDishDto.restaurantId()).get();
        Dish dish = dishService.save(createDishDto.toDish(restaurant));
        return DisplayDishDto.from(dishService.save(dish));
    }

    @Override
    public Optional<DisplayDishDto> update(Long id, CreateDishDto createDishDto) {
        Restaurant restaurant = restaurantService.findById(createDishDto.restaurantId()).get();
        return dishService.update(id, createDishDto.toDish(restaurant))
                .map(DisplayDishDto::from);
    }

    @Override
    public Optional<DisplayDishDto> deleteById(Long id) {
        return dishService.deleteById(id)
                .map(DisplayDishDto::from);
    }

    @Override
    public DisplayOrderDto addToOrder(Long id, String username) {
        Dish dish=dishService.findById(id).get();
        Order order=orderService.findPending(username).get();
        return DisplayOrderDto.from(dishService.addToOrder(dish,order));
    }

    @Override
    public DisplayOrderDto removeFromOrder(Long id, String username) {
        Dish dish=dishService.findById(id).get();
        Order order=orderService.findPending(username).get();
        return DisplayOrderDto.from(dishService.removeFromOrder(dish,order));
    }

}
