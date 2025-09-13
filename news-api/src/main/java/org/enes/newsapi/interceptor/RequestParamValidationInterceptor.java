package org.enes.newsapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.enes.newsapi.annotation.ValidateRequestParams;
import org.enes.newsapi.exception.InvalidRequestParamException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestParamValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler
                             ) {

        if (handler instanceof HandlerMethod handlerMethod) {
            ValidateRequestParams annotation =
                    handlerMethod.getMethodAnnotation(ValidateRequestParams.class);

            if (annotation != null) {
                Set<String> allowedParams = Set.of(annotation.allowed());
                Set<String> receivedParams = request.getParameterMap().keySet();

                Set<String> invalidParams = receivedParams.stream()
                        .filter(param -> !allowedParams.contains(param))
                        .collect(Collectors.toSet());

                if (!invalidParams.isEmpty()) {
                    throw new InvalidRequestParamException(invalidParams);
                }
            }
        }
        return true;
    }
}
