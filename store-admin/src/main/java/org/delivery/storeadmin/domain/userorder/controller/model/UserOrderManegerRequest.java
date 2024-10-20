package org.delivery.storeadmin.domain.userorder.controller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.db.userorder.enums.UserOrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderManegerRequest {

    private Long Id;

    private UserOrderStatus status;
}
