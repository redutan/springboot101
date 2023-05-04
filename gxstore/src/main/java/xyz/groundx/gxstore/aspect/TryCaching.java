package xyz.groundx.gxstore.aspect;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TryCaching {
    String name();
}
