package my.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;

@RestController
@EnableWebMvc
public class APIController {
    private Logger log = LoggerFactory.getLogger(APIController.class);

    /**
     * Test ping endpoint.
     */
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public Map<String, String> ping() {
        log.info("Received ping, sending pong");
        Map<String, String> pong = new HashMap<>();
        pong.put("pong", "Hello, World!");
        return pong;
    }

    /**
     * Handles AWS event. Delivered from {@link StreamLambdaHandler#handleRequest} by constructing special proxy request.
     */
    @RequestMapping(path = "/event", method = RequestMethod.POST)
    public Map<String, String> event(@RequestParam("type") String type, @RequestBody String post) {
        log.info("Received event {} body {}", type, post);
        Map<String, String> pong = new HashMap<>();
        pong.put("pong", "Hello, World!");
        return pong;
    }
}
