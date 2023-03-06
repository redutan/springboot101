package xyz.groundx.helloworld;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class OperatorConverter implements Converter<String, Operator> {
    @Override
    public Operator convert(String source) {
        return Operator.valueOf(source.toUpperCase());
    }
}
