package io.seventytwo.camel.demo.route;

import io.seventytwo.camel.demo.processor.IdFromBodyToHeaderProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestToSqlRouteBuilder extends RouteBuilder {

    private final IdFromBodyToHeaderProcessor idFromBodyToHeaderProcessor;

    public RestToSqlRouteBuilder(IdFromBodyToHeaderProcessor idFromBodyToHeaderProcessor) {
        this.idFromBodyToHeaderProcessor = idFromBodyToHeaderProcessor;
    }

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
                .log("Full Body: ${body}")
                .split(body()).parallelProcessing()
                    .log("After Split: ${body}")
                    .process(idFromBodyToHeaderProcessor)
                    .to("https://postman-echo.com/get?bridgeEndpoint=true&id=${header.ID}")
                    .log("After echo: ${body}")
                .end()
                .setBody(simple("Done"))
                .end();
    }
}
