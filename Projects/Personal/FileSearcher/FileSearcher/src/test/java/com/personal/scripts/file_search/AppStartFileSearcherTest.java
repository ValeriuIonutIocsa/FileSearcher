package com.personal.scripts.file_search;

import org.junit.jupiter.api.Test;

import com.utils.test.TestInputUtils;

class AppStartFileSearcherTest {

	@Test
	void testMain() {

		final String[] args;
		final int input = TestInputUtils.parseTestInputNumber("1");
		if (input == 1) {
			args = new String[] {
					"--rg_exe_path=C:\\IVI\\Apps\\RipGrep\\rg.exe",
					"--npp_exe_path=C:\\IVI\\Apps\\" +
							"PortableApps\\PortableApps\\Notepad++Portable\\Notepad++Portable.exe"
			};
		} else {
			throw new RuntimeException();
		}

		AppStartFileSearcher.main(args);
	}
}
