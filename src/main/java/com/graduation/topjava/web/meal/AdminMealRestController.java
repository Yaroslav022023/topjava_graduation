package com.graduation.topjava.web.meal;

import com.graduation.topjava.model.Meal;
import com.graduation.topjava.service.MealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.graduation.topjava.util.ValidationUtil.assureIdConsistent;
import static com.graduation.topjava.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMealRestController {
    static final String REST_URL = "/rest/admin/restaurants/{restaurantId}/meals";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public AdminMealRestController(MealService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Meal> getAll(@PathVariable int restaurantId) {
        log.info("getAll");
        return service.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get meal={} for restaurant={}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable int restaurantId, @RequestBody Meal meal) {
        log.info("create {} for restaurant={}", meal, restaurantId);
        checkNew(meal);
        Meal created = service.save(meal, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @PathVariable int id, @RequestBody Meal meal) {
        log.info("update {} for restaurant={}", meal, restaurantId);
        assureIdConsistent(meal, id);
        service.save(meal, restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete={} from restaurant={}", id, restaurantId);
        service.delete(id, restaurantId);
    }
}
