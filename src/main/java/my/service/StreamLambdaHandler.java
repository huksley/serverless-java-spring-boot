package my.service;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.jaxrs.AwsProxySecurityContext;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyRequestContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.Headers;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static Logger log = LoggerFactory.getLogger(StreamLambdaHandler.class);

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            log.info("Starting AWS Lambda");
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
            log.info("Created AWS Handler, {}", handler);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        log.info("Handle request {}, {}, {}, {}", context, context.getFunctionName(), context.getInvokedFunctionArn(),
                context.getLogStreamName());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bos);
        String event = new String(bos.toByteArray(),
                LambdaContainerHandler.getContainerConfig().getDefaultContentCharset());
        log.info("Got event {} context {}", event);

        ObjectMapper mapper = LambdaContainerHandler.getObjectMapper();
        mapper.registerModule(new JodaModule());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(SNSRecord.class, new SNSRecordDeserializer());
        module.addDeserializer(SQSMessage.class, new SQSMessageDeserializer());
        mapper.registerModule(module);

        AwsProxyRequest request = mapper.readValue(event, AwsProxyRequest.class);
        if (request.getHttpMethod() == null || "".equals(request.getHttpMethod())) {
            log.info("Not an AWS HTTP event, skipping {}", request);
            Map<String, Object> raw = (Map<String, Object>) mapper.readValue(event, Map.class);

            // Peek inside one event
            if (raw.get("Records") instanceof List) {
                List<Map<String, Object>> events = (List<Map<String, Object>>) raw.get("Records");
                if (events.size() > 0) {
                    raw = events.get(0);
                } else {
                    log.warn("Empty, dummy event records {}", events);
                }
            }

            String eventSource = (String) raw.get("source"); // CloudWatch event
            if (eventSource == null) {
                eventSource = (String) raw.get("EventSource"); // SNS
            }
            if (eventSource == null) {
                eventSource = (String) raw.get("eventSource"); // SQS!?!
            }

            if (eventSource == null) {
                log.warn("Can`t get event type: {}", raw);
            } else if ("aws.events".equals(eventSource)) {
                ScheduledEvent ev = mapper.readValue(event, AnnotatedScheduledEvent.class);
                request = convertToRequest(ev, context);
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            } else if ("aws:sns".equals(eventSource)) {
                SNSEvent ev = mapper.readValue(event, AnnotatedSNSEvent.class);
                request = convertToRequest(ev, context);
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            } else if ("aws:sqs".equals(eventSource)) {
                SQSEvent ev = mapper.readValue(event, AnnotatedSQSEvent.class);
                request = convertToRequest(ev, context);
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            } else {
                log.warn("Unhandled event type {}", eventSource);
            }
        } else {
            log.info("Handling via Spring Boot rest APIs {}", request);
            AwsProxyResponse response = handler.proxy(request, context);
            mapper.writeValue(outputStream, response);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedScheduledEvent extends ScheduledEvent {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSNSRecord extends SNSRecord {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSNSEvent extends SNSEvent {
        @Override
        @JsonProperty("Records")
        public List<SNSRecord> getRecords() {
            return super.getRecords();
        }
    }

    public class SNSRecordDeserializer extends StdDeserializer<SNSRecord> {
        public SNSRecordDeserializer() {
            this(null);
        }

        public SNSRecordDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public SNSRecord deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return ctxt.readValue(jp, AnnotatedSNSRecord.class);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSQSMessage extends SQSMessage {
    }

    public class SQSMessageDeserializer extends StdDeserializer<SQSMessage> {
        public SQSMessageDeserializer() {
            this(null);
        }

        public SQSMessageDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public SQSMessage deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return ctxt.readValue(jp, AnnotatedSQSMessage.class);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSQSEvent extends SQSEvent {
        @Override
        @JsonProperty("Records")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public List<SQSMessage> getRecords() {
            return super.getRecords();
        }
    }

    /**
     * FIXME: This conversion is application depended and should be configurable?
     */
    public static AwsProxyRequest convertToRequest(Object ev, Context context) {
        AwsProxyRequest r = new AwsProxyRequest();
        r.setPath("/ping");
        r.setResource("/ping");
        r.setHttpMethod("GET");
        r.setMultiValueHeaders(new Headers());
        AwsProxyRequestContext rc = new AwsProxyRequestContext();
        rc.setIdentity(new ApiGatewayRequestIdentity());
        r.setRequestContext(rc);
        return r;
    }
}