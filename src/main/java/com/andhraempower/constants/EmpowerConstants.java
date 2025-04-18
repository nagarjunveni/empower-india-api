package com.andhraempower.constants;

import java.util.List;

public class EmpowerConstants {

    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String SUCCESS_CODE = "200";
    public static final String BAD_REQUEST_CODE = "400";
    public static final String UNAUTHORIZED_CODE = "401";
    public static final String FORBIDDEN_CODE = "403";
    public static final String RESOURCE_NOT_FOUND_CODE = "404";
    public static final String UNEXPECTED_SERVER_ERROR_CODE = "5XX";
    public static final String SUCCESS_CODE_DESC = "Successful data fetch";
    public static final String BAD_REQUEST_CODE_DESC = "Bad request";
    public static final String UNAUTHORIZED_CODE_DESC = "Unauthorized";
    public static final String FORBIDDEN_CODE_DESC = "Forbidden";
    public static final String RESOURCE_NOT_FOUND_CODE_DESC = "Resource not found";
    public static final String UNEXPECTED_SERVER_ERROR_CODE_DESC = "Unexpected Server Error";

    public static final String USER_ADMIN = "Admin";

    public static final String PROJECT_FLOW = "PROJECT_FLOW";

    public static final List<String> ENDPOINTS_FOR_ALL_USERS = List.of("/api/v1/login",
            "/api/v1/committee/**",
            "/api/v1/contact-us/**",
            "/api/v1/dash-board/**",
            "/api/v1/finance/**",
            "/api/v1/gallery-images/**",
            "/api/v1/lookup/**",
            "/api/v1/bank/**",
            "/api/v1/project/**",
            "/api/v1/project/status/**",
            "/api/v1/status/**",
            "/api/v1/projectType/**",
            "/api/v1/roles/**",
            "/Sponsors",
            "/api/v1/users/**",
            "/api/v1/vendors/**",
            "/api/v1/village/**",
            "/api/v1/donars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    );


}
