package name.dusanjakub.cryptoservice;

import org.springframework.validation.BindingResult;

public interface LazyBody<T> {
    T get();

    BindingResult validate();

    T merge(T source);
}
