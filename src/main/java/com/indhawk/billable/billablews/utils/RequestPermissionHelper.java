package com.indhawk.billable.billablews.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestPermissionHelper {

    public static boolean isRequestPermitted(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURI();
        for (PermittedAPIs api : PermittedAPIs.values()) {
            if (url.contains(api.endPoint) && method.equalsIgnoreCase(api.getMethod())) {
                return true;
            }
        }
        return false;
    }

    private enum PermittedAPIs {
        SWAGGER_UI("/swagger-ui.html", "GET"),
        AUTHENTICATE_USER("/api/auth/authenticate", "POST"),
        CREATE_USER("/api/users", "POST");

        String endPoint;
        String method;

        PermittedAPIs (String endPoint, String method) {
            this.endPoint = endPoint;
            this.method = method;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public String getMethod() {
            return method;
        }
    }



}
