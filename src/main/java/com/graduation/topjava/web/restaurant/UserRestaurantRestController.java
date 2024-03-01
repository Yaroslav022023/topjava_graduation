package com.graduation.topjava.web.restaurant;

import com.graduation.topjava.dto.RestaurantViewDto;
import com.graduation.topjava.dto.RestaurantVotedByUserDto;
import com.graduation.topjava.dto.RestaurantWithNumberVoicesDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.graduation.topjava.web.security.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/rest/restaurants";

    @Override
    @GetMapping
    public List<RestaurantViewDto> getAllWithMealsForToday() {
        log.info("getAllWithMealsForToday");
        return super.getAllWithMealsForToday();
    }

    @Override
    @GetMapping("/number-voices")
    public List<RestaurantWithNumberVoicesDto> getAllWithNumberVoicesForToday() {
        return super.getAllWithNumberVoicesForToday();
    }

    @GetMapping("/voted-by-user")
    public RestaurantVotedByUserDto getVotedByUser() {
        return super.getVotedByUser(authUserId());
    }

    @PostMapping(value = "{restaurantId}")
    public ResponseEntity<RestaurantVotedByUserDto> vote(@PathVariable int restaurantId) {
        super.vote(authUserId(), restaurantId);
        URI uriOfVotedByUser = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/voted-by-user")
                .build().toUri();
        return ResponseEntity.created(uriOfVotedByUser).body(getVotedByUser());
    }
}
