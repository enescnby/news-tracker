package org.enes.newsapi.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.enes.newsapi.annotation.ValidateRequestParams;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestParamValidationInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public RequestParamValidationInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler
                             ) throws Exception {

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
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.setContentType(("application/json"));

                    Map<String, Object> errorResponse = new LinkedHashMap<>();
                    errorResponse.put("error", "Invalid Parameters");
                    errorResponse.put("timestamp", LocalDateTime.now());
                    errorResponse.put("Invalid Parameters", invalidParams);
                    errorResponse.put("Allowed Parameters", allowedParams);

                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return false;
                }
            }
        }
        return true;
    }
}
