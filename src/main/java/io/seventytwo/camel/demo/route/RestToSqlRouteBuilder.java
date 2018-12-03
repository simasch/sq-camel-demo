package io.seventytwo.camel.demo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestToSqlRouteBuilder extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        restConfiguration()
                .contextPath("/camel");

        rest("/sql")
                .get()
                .produces(MediaType.TEXT_PLAIN_VALUE)
                .to("direct:hello");

        from("direct:sql")
                .routeId("RestToSqlRoute")
                .log("${headers}")
                .transform().simple("Hello ${headers.name}");
    }
}
