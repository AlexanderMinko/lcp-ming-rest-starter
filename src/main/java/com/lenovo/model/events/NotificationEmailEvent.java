package com.lenovo.model.events;

import com.lenovo.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmailEvent extends Event {
    private NotificationEmail notificationEmail;
}
