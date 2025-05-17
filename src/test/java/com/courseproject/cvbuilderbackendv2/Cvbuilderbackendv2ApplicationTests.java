package com.courseproject.cvbuilderbackendv2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Cvbuilderbackendv2ApplicationTests {
	private static final boolean test = true;

	@Test
	void applicationStartsSuccessfully() {
		assertThat(test).isTrue();
	}
}
