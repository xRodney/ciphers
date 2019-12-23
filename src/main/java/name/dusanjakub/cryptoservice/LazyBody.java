package name.dusanjakub.cryptoservice;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.JsonMappingException;

public interface LazyBody<T> {
    T get() throws MethodArgumentNotValidException;

    T merge(T source) throws JsonMappingException, MethodArgumentNotValidException;
}
