package com.study.StudyUp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.study.StudyUp.DTOs.UserRegistrationDto;
import com.study.StudyUp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest-> lets your test run your Spring Boot app almost like it’s really running.
// This way, all the parts of your app—like services, repositories, and configurations—are available,
// and you can test how they work together.
//So instead of testing just one piece, you’re testing the app as a whole.

@SpringBootTest


//@AutoConfigureMockMvc-> Automatically sets up MockMvc in a Spring Boot test class.
//It’s like a fake browser for your controllers—you can hit endpoints, check status codes,
// headers, and response bodies, all in-memory, without starting a real server.
@AutoConfigureMockMvc
class AuthControllerTest {

    //@Autowired → Tells Spring: “Please give me this object automatically.”
    //MockMvc → The object that lets you simulate HTTP requests to your controllers.
    //Spring automatically gives your test a ready-to-use MockMvc
    // so you can test endpoints without starting a server
    @Autowired
    private MockMvc mockMvc;

    //@MockBean = “Give me a fake version of this bean for testing, so I can control its behavior.”
    @MockBean
    private UserServiceImpl userService;

    //@MockBean PasswordEncoder = “Give me a fake password encoder for testing, so I can control its behavior
    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthControllerTest.class);

    //Pretends to be a user sending a registration request to your API.
    //Checks that the API responds with 201 Created (success).
    //Prints logs showing the request and response so you can see what happened.
    //Runs in memory, so it doesn’t start a real server.

    //It’s a quick way to test the registration endpoint works and see the details.
    @Test

    //Exception → Means this method can throw an error if something goes wrong.
    //log.info(...) → Write an informational message to the logs

    void registerUser_success() throws Exception {
        log.info("Running registerUser_success test...");

        //String json->creates the JSON data that will be sent in the registration request during the test
        String json = """
            {"username":"newuser","password":"secret"}
        """;

        //log.debug(...) → Logs the JSON payload for debugging.
        log.debug("Request payload: {}", json);

        //This code sends a fake registration request, prints the request/response,
        // and checks that the endpoint returns success—all in-memory without starting a real server.



        //mockMvc.perform(...) → Sends a simulated POST request to /api/auth/register with the JSON payload
        //.andDo(print()) → Prints the request and response details in the console.
        //.andExpect(status().isCreated()) → Checks that the response status is 201 Created, confirming success.

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print()) // logs the full response
                .andExpect(status().isCreated());
    }
}