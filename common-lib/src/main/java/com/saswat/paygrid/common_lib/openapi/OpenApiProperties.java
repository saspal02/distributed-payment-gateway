package com.saswat.paygrid.common_lib.openapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.openapi")
@Getter
@Setter
public class OpenApiProperties {

    private String title = "";

    private String description = "API documentation";

    private String version = "v1";
}
