package org.delivery.storeadmin.domain.userorder.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.storeadmin.domain.userorder.business.UserOrderBusiness;
import org.delivery.storeadmin.domain.userorder.controller.model.UserOrderManegerRequest;
import org.delivery.storeadmin.domain.userorder.controller.model.UserOrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manage-user-order")
@RequiredArgsConstructor
public class UserOrderApiController {

    private final UserOrderBusiness userOrderBusiness;

    @PostMapping("/accept")
    public UserOrderResponse acceptUserOrder(
            @RequestBody UserOrderManegerRequest request
    ){
        return userOrderBusiness.acceptUserOrder(request);
    }
}
