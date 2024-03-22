package com.topjava.graduation.web.restaurant;

import com.topjava.graduation.dto.RestaurantViewDto;
import com.topjava.graduation.dto.RestaurantVotedByUserDto;
import com.topjava.graduation.dto.RestaurantWithNumberVoicesDto;
import com.topjava.graduation.web.security.AuthorizedUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/rest/restaurants";

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

    @GetMapping("/voted-by-user")
    public RestaurantVotedByUserDto getVotedByUser(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.getVotedByUser(authUser.getId());
    }

    @PostMapping(value = "{restaurantId}")
    public ResponseEntity<RestaurantVotedByUserDto> vote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        super.vote(authUser.getId(), restaurantId);
        URI uriOfVotedByUser = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/voted-by-user")
                .build().toUri();
        return ResponseEntity.created(uriOfVotedByUser).body(getVotedByUser(authUser));
    }
}
