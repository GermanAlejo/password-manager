package com.passwordmanager.password_manager.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordmanager.password_manager.dto.LoginRequestDTO;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthControllerTest.class)
@AutoConfigureMockMvc
@AutoConfigureDataMongo() // evitar autoconfig de Mongo
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    private ObjectMapper objectMapper;

    public void registerUser_Success() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testUsername1", "testUsername1@mail.com", "123456789");
        User user = new User("testUsername1", "testUsername1@mail.com", "123456789", "test-salt");
        when(userService.registerNewUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isCreated());

    }
}
