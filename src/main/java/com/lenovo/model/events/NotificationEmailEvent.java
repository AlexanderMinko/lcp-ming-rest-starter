package com.lenovo.model.events;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmailEvent extends Event {

  private String userEmail;
  private String userId;
  private String orderId;
  private Instant orderCreatedDate;
  private List<OrderItem> orderItems;

}
