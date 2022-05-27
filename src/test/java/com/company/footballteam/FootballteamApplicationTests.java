package com.company.footballteam;

import com.company.footballteam.controller.UserController;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FootballteamApplicationTests {

	@Autowired
	private UserController userController;
	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

}
