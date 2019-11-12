package name.dusanjakub.cryptoservice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LazyBodyArgumentResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper mapper;
    private RequestMappingHandlerAdapter adapter;
    private RequestResponseBodyMethodProcessor delegate;

    public LazyBodyArgumentResolver(RequestMappingHandlerAdapter adapter) {
        this.adapter = adapter;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LazyBody.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return new LazyBodyImpl(getDelegate(), parameter, binderFactory, webRequest, mapper);
    }

    private RequestResponseBodyMethodProcessor getDelegate() {
        RequestResponseBodyMethodProcessor delegate = this.delegate;
        if (delegate == null) {
            delegate = this.adapter.getArgumentResolvers()
                    .stream()
                    .filter(r -> r instanceof RequestResponseBodyMethodProcessor)
                    .findFirst()
                    .map(r -> (RequestResponseBodyMethodProcessor) r)
                    .orElseThrow();
            this.delegate = delegate;
        }
        return delegate;
    }

    private static class LazyBodyImpl implements LazyBody {
        private static MethodParameter mapParameter;
        private final RequestResponseBodyMethodProcessor delegate;
        private final MethodParameter parameter;
        private final WebDataBinderFactory binderFactory;
        private final NativeWebRequest webRequest;
        private final ObjectMapper mapper;
        private BindingResult bindingResult;
        private Object result;

        public LazyBodyImpl(RequestResponseBodyMethodProcessor delegate, MethodParameter parameter, WebDataBinderFactory binderFactory,
                            NativeWebRequest webRequest, ObjectMapper mapper) {
            this.delegate = delegate;
            this.parameter = parameter;
            this.binderFactory = binderFactory;
            this.webRequest = webRequest;
            this.mapper = mapper;
        }

        @Override
        public Object get() {
            try {
                Object result = this.result;
                if (result == null) {
                    result = delegate.resolveArgument(parameter.nested(), null, webRequest, null);
                    Objects.requireNonNull(result, "Body must not be null");
                    this.result = result;
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public BindingResult validate() {
            Object result = get();
            if (binderFactory != null && result != null) {
                String name = Conventions.getVariableNameForParameter(parameter);
                WebDataBinder binder;
                try {
                    binder = binderFactory.createBinder(webRequest, result, name);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                validateIfApplicable(binder, parameter);
                this.bindingResult = binder.getBindingResult();
                return this.bindingResult;
            }
            return null;
        }

        @Override
        public Object merge(Object source) {
            try {
                Map<String, Object> map = (Map<String, Object>) delegate.resolveArgument(getMapParameter(), null, webRequest, null);
                return this.result = mapper.updateValue(source, map);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void _method(Map<String, Object> map) {
        }

        private static MethodParameter getMapParameter() {
            MethodParameter mp = mapParameter;
            if (mp == null) {
                try {
                    Method m = LazyBodyImpl.class.getMethod("_method", Map.class);
                    mp = MethodParameter.forExecutable(m, 0);
                    mapParameter = mp;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            return mp;
        }

        protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
            Annotation[] annotations = parameter.getParameterAnnotations();
            for (Annotation ann : annotations) {
                Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
                if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                    Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                    Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                    binder.validate(validationHints);
                    break;
                }
            }
        }
    }
}
