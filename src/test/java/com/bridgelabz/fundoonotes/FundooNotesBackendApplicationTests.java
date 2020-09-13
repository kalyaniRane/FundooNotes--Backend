package com.bridgelabz.fundoonotes;

import com.bridgelabz.fundoonotes.properties.FileProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest

class FundooNotesBackendApplicationTests {

    @MockBean
    FileProperties fileProperties;

    @Test
    void contextLoads() {
    }

}
