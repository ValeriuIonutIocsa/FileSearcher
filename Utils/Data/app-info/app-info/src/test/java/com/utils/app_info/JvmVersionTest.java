package com.utils.app_info;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class JvmVersionTest {

	@Test
	void testIs32BitJvm() {

		final boolean jvm32Bit = JvmVersion.isJvm32Bit();
		Logger.printLine("32 bit jvm: " + jvm32Bit);
	}

	@Test
	void testIs64BitJvm() {

		final boolean jvm64Bit = JvmVersion.isJvm64Bit();
		Logger.printLine("64 bit jvm: " + jvm64Bit);
	}
}
