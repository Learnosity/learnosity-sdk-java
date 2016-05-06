package com.learnosity.sdk.example

import com.learnosity.sdk.request.Init
import org.junit.Assert
import org.junit.Test

class InitTest {

	@Test(expected = NullPointerException.class)
	public void testInit() {
		def init = new Init(null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitSecretRequired() {
		def init = new Init(Init.Service.Assess, null, null);
	}

	@Test(expected = NullPointerException.class)
	public void testInitSecurityPacketNull() {
		def init = new Init(Init.Service.Assess, null, "sdfasd");
		assert 1 == 0
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitSecurityPacketEmptyMap() {
		def init = new Init(Init.Service.Assess, [:], "asdfsad");
	}

	@Test
	public void testInitSecurityPacketDataPacket() {
		def init = new Init(Init.Service.Data, ["consumer_key":"xyz"], "asdfsad");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitSecurityPacketDataPacketBadArgument() {
		def init = new Init(Init.Service.Data, ["XXXXYYYYZZZZ":"xyz"], "asdfsad");
	}
}

