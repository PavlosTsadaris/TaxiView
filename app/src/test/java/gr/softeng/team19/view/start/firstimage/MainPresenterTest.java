package gr.softeng.team19.view.start.firstimage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Tests for MainPresenter focusing on splash screen navigation.
 */
public class MainPresenterTest {

    private MainPresenter presenter;
    private MainViewStub viewStub;

    /**
     * Initializes the ViewStub and Presenter before each test execution.
     */
    @Before
    public void setUp() {
        viewStub = new MainViewStub();
        presenter = new MainPresenter(viewStub);
    }

    /**
     * Verifies that the splash finish event triggers navigation to the welcome screen.
     */
    @Test
    public void testOnSplashFinished() {
        presenter.onSplashFinished();

        // Assert that the navigation method was called exactly once
        Assert.assertEquals(1, viewStub.welcomeClicks);
    }
}