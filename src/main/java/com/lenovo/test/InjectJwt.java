package com.lenovo.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface InjectJwt {

  String realm() default "LCPRealm";

  String subject() default "a9d96d74-0d17-4322-a633-fc6a59fc2cce";

  String email() default "alexander_ming@i.ua";

  String[] roles() default {
      "default-roles-lcprealm",
      "offline_access",
      "developer",
      "uma_authorization",
      "customer"
  };

  String username() default "alexander_ming";

}
