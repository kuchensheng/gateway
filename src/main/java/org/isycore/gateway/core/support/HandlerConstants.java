package org.isycore.gateway.core.support;

public class HandlerConstants {

    public enum EnumHandlerType {
        ERROR_TYPE("error"),POST_TYPE("post"),PRE_TYPE("pre"),ROUTE_TYPE("route")
        ;
        private String typeName;

        EnumHandlerType(String typeName) {
            this.typeName = typeName;
        }
    }
}
