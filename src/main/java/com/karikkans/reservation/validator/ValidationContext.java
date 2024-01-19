package com.karikkans.reservation.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Optional.empty;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationContext {

    private Map<String, Object> parameters;

    public static ValidationContext getInstance() {
        return new ValidationContext(new HashMap<>());
    }

    public void enrichContext(String key, Object value) {
        this.parameters.put(key, value);
    }

    public <T> Optional<T> findDataByKey(String key, Class<T> clazz) {

        Object data = this.getParameters().get(key);

        if (isNull(data)) {
            log.info("Not data exists in context with key {}", key);
            return empty();
        }
        return Optional.of(clazz.cast(data));
    }
}
