package com.greaticker.demo.intergration_test.exceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleIllegalArgumentException() throws Exception {
        // This endpoint is expected to throw IllegalArgumentException
        mockMvc.perform(MockMvcRequestBuilders.get("/no-exist-path")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess", is(false)))
                //아래 문구는 실제로 내가 적용한 익셉션 핸들러가 캐치한 예외 메시지 문구를 그대로 복붙한 것.
                //따라서 이 테스트가 통과한다는 건 글로벌 익셉션 핸들러가 잘 동작한다는 것이다.
                .andExpect(jsonPath("$.message", is("No static resource no-exist-path.")));
    }
}
