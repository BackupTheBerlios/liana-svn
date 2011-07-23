package org.papernapkin.liana.event;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Philip A. Chapman <pchapman@pcsw.us>
 */
public class ResponderInfoTest
{
	@Test
	public void testResponderCreationWithoutParameters1() {
		MockResponder responder = new MockResponder();
		ResponderInfo test = new ResponderInfo(responder, "doEvent", null);
		assertNotNull(test);
	}

	@Test
	public void testResponderCreationWithParameters1() {
		MockResponder responder = new MockResponder();
		ParameterInfo[] parameterBindings = new ParameterInfo[1];
		parameterBindings[0] = new ParameterInfo(0, MockEvent.class, "getData");
		ResponderInfo test = new ResponderInfo(responder, "doEventWithParameters", parameterBindings);
		assertNotNull(test);
	}

	@Test
	public void testResponderCreationWithoutParameters2() {
		MockResponder responder = new MockResponder();
		ResponderInfo test = new ResponderInfo(responder, "doEvent", null);
		assertNotNull(test);
	}

	@Test
	public void testResponderCreationWithParameters2() {
		MockResponder responder = new MockResponder();
		ParameterInfo[] parameterBindings = new ParameterInfo[1];
		parameterBindings[0] = new ParameterInfo(0, MockEvent.class, "getData");
		ResponderInfo test = new ResponderInfo(responder, "doEventWithParameters", parameterBindings);
		assertNotNull(test);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testResponderCreationFailureWithoutParameters() {
		MockResponder responder = new MockResponder();
		ResponderInfo test = new ResponderInfo(responder, "handleEvent", null);
		assertNotNull(test);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testResponderCreationFailureWithParameters() {
		MockResponder responder = new MockResponder();
		ParameterInfo[] parameterBindings = new ParameterInfo[1];
		parameterBindings[0] = new ParameterInfo(0, MockEvent.class, "getData");
		ResponderInfo test = new ResponderInfo(responder, "handleEventWithParameters", parameterBindings);
		assertNotNull(test);
	}
}

class MockResponder
{
	public void doEvent() {}

	public void doEventWithParameters(String s) {}
}

class MockEvent
{
	public String getData() {return null;}
}