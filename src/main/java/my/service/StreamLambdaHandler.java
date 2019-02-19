package my.service;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        log.info("Handle request {}, {}, {}, {}", context, context.getFunctionName(), context.getInvokedFunctionArn(),
                context.getLogStreamName());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bos);
        String event = new String(bos.toByteArray(), LambdaContainerHandler.getContainerConfig().getDefaultContentCharset());
        log.info("Got event {}", event);
        
        ObjectMapper mapper = LambdaContainerHandler.getObjectMapper();
        AwsProxyRequest request = mapper.readValue(event, AwsProxyRequest.class);
        if (request.getHttpMethod() == null || "".equals(request.getHttpMethod())) {
            log.info("Not an AWS HTTP event, skipping {}", request);
            Map<String,Object> raw = (Map<String,Object>) mapper.readValue(event, Map.class);
            
        } else {
            log.info("Handling via Spring Boot rest APIs {}", request);
            AwsProxyResponse response = handler.proxy(request, context);
            mapper.writeValue(outputStream, response);
        }
    }
}