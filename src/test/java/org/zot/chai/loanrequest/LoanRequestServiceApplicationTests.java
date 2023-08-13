package org.zot.chai.loanrequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zot.chai.loanrequest.web.LoanRequestController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoanRequestServiceApplicationTests {

	@Autowired
	private LoanRequestController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
