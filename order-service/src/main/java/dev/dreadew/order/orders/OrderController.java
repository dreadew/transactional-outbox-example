package dev.dreadew.order.orders;

import dev.dreadew.order.domain.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Order create(@RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request.getCustomerEmail(), request.getTotalPrice());
  }

  @Data
  public static class CreateOrderRequest {
    @Email
    @NotBlank
    private String customerEmail;

    @NotNull
    private BigDecimal totalPrice;
  }

}
