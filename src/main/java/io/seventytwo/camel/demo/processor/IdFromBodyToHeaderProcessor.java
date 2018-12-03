package io.seventytwo.camel.demo.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdFromBodyToHeaderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Map row = (Map<String, String>) exchange.getIn().getBody();
        exchange.getOut().setHeader("ID", row.get("ID"));
    }
}
