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
                .to("direct:sql");

        from("quartz2://sq/polling-timer?cron=0/30+*+*+*+*+?")
                .to("direct:sql");

        from("direct:sql")
                .routeId("RestToSqlRoute")
                .to("sql://select id, name, description from todo")
                .log("${body}")
                .setBody(constant(null))
                .to("https://postman-echo.com/get?bridgeEndpoint=true")
                .transform().simple("${body}");
    }
}
