package aChaushev.architects;

import aChaushev.architects.config.ForexApiConfig;
import aChaushev.architects.service.impl.ExRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ArchitectsApplicationTests {

	@MockBean
	private ForexApiConfig config;

	@MockBean
	private ExRateServiceImpl exRateService;

	@Test
	void contextLoads() {
	}

}
