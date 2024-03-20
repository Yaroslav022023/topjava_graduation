package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.dto.RestaurantViewDto;
import com.topjava.graduation.dto.RestaurantVotedByUserDto;
import com.topjava.graduation.dto.RestaurantWithNumberVoicesDto;
import com.topjava.graduation.model.Restaurant;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/rest/admin/restaurants";

    @Override
    @GetMapping("/all")
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping
    public List<RestaurantViewDto> getAllWithMealsForToday() {
        return super.getAllWithMealsForToday();
    }

    @Override
    @GetMapping("/number-voices")
    public List<RestaurantWithNumberVoicesDto> getAllWithNumberVoicesForToday() {
        return super.getAllWithNumberVoicesForToday();
    }

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/voted-by-user/{id}")
    public RestaurantVotedByUserDto getVotedByUser(@PathVariable int id) {
        return super.getVotedByUser(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = super.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        super.update(restaurant, id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}
