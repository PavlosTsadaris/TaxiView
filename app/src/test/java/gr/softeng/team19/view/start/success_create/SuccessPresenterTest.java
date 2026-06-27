package gr.softeng.team19.view.start.success_create;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for SuccessPresenter to verify UI initialization and navigation.
 */
public class SuccessPresenterTest {

    private SuccessPresenter presenter;
    private SuccessViewStub viewStub;

    /**
     * Initializes the view stub and presenter before each test case.
     */
    @Before
    public void setUp() {
        viewStub = new SuccessViewStub();
        presenter = new SuccessPresenter(viewStub);
    }

    /**
     * Verifies that a valid string is displayed correctly in the view.
     */
    @Test
    public void testOnViewCreatedWithValidMessage() {
        String testMsg = "Account Created!";
        presenter.onViewCreated(testMsg);
        Assert.assertEquals(testMsg, viewStub.capturedMessage);
    }

    /**
     * Verifies that the view is not updated if the message is null.
     */
    @Test
    public void testOnViewCreatedWithNullMessage() {
        presenter.onViewCreated(null);
        Assert.assertNull(viewStub.capturedMessage);
    }

    /**
     * Verifies that the view is not updated if the message is empty.
     */
    @Test
    public void testOnViewCreatedWithEmptyMessage() {
        presenter.onViewCreated("");
        Assert.assertNull(viewStub.capturedMessage);
    }

    /**
     * Verifies that the login action triggers navigation to the login screen.
     */
    @Test
    public void testOnLoginPressed() {
        presenter.onLoginPressed();
        Assert.assertTrue(viewStub.navigatedToLogin);
    }
}