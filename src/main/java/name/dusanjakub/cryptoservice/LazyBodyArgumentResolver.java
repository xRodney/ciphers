package name.dusanjakub.cryptoservice;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LazyBodyArgumentResolver extends RequestResponseBodyMethodProcessor {
    private final ObjectMapper mapper;
    private final MethodParameter mapParameter;

    public LazyBodyArgumentResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
        this.mapper = new ObjectMapper();
        this.mapParameter = MapParameterHolder.getMapParameter();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LazyBody.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return new LazyBodyImpl<>(mapper) {
            @Override
            protected Map<String, Object> readIntoMap() throws Exception {
                return (Map<String, Object>) LazyBodyArgumentResolver.super.resolveArgument(mapParameter, mavContainer, webRequest, null);
            }

            @Override
            protected Object readIntoObject() throws Exception {
                return LazyBodyArgumentResolver.super.resolveArgument(parameter.nested(), mavContainer, webRequest, null);
            }

            @Override
            protected void validateResult(Object result) throws MethodArgumentNotValidException {
                LazyBodyArgumentResolver.this.validateResult(result, parameter, webRequest, binderFactory);
            }
        };
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {
        throw new UnsupportedOperationException();
    }

    private void validateResult(Object result, MethodParameter parameter, NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) throws MethodArgumentNotValidException {
        if (binderFactory != null && result != null) {
            String name = Conventions.getVariableNameForParameter(parameter);
            WebDataBinder binder;
            try {
                binder = binderFactory.createBinder(webRequest, result, name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            validateIfApplicable(binder, parameter.nested());
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
    }

    abstract private static class LazyBodyImpl<T> implements LazyBody<T> {
        private final ObjectMapper mapper;
        private Map<String, Object> objectMap;
        private T incompleteObject;

        public LazyBodyImpl(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public T get() throws MethodArgumentNotValidException {
            validateResult(getObject());
            return incompleteObject;
        }

        @Override
        public T merge(T source) throws JsonMappingException, MethodArgumentNotValidException {
            T result = mapper.updateValue(source, getMap());
            validateResult(result);
            return result;
        }

        @Override
        public String toString() {
            return "LazyBodyImpl{" + getObject() + '}';
        }

        private T getObject() {
            if (incompleteObject == null) {
                try {
                    incompleteObject = readIntoObject();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return incompleteObject;
        }

        private Map<String, Object> getMap() {
            if (objectMap == null) {
                try {
                    objectMap = readIntoMap();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return objectMap;
        }

        abstract protected Map<String, Object> readIntoMap() throws Exception;

        abstract protected T readIntoObject() throws Exception;

        abstract protected void validateResult(Object result) throws MethodArgumentNotValidException;
    }

    private static class MapParameterHolder {
        private MapParameterHolder(Map<String, Object> map) {
        }

        public static MethodParameter getMapParameter() {
            try {
                Constructor<MapParameterHolder> constructor = MapParameterHolder.class.getDeclaredConstructor(Map.class);
                return MethodParameter.forExecutable(constructor, 0);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
