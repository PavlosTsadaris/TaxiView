package gr.softeng.team19.view.start.welcome;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Tests for WelcomePresenter verifying navigation logic.
 */
public class WelcomePresenterTest {

    private WelcomePresenter presenter;
    private WelcomeViewStub viewStub;

    /**
     * Initializes the testing environment before each test.
     */
    @Before
    public void setUp() {
        viewStub = new WelcomeViewStub();
        presenter = new WelcomePresenter(viewStub);
    }

    /**
     * Verifies that the create account action triggers sign-up navigation.
     */
    @Test
    public void testOnCreateAccount() {
        presenter.onCreateAccount();

        Assert.assertEquals(1, viewStub.signUpClicks);
        Assert.assertEquals(0, viewStub.loginClicks);
    }

    /**
     * Verifies that the login action triggers login navigation.
     */
    @Test
    public void testOnLogin() {
        presenter.onLogin();

        Assert.assertEquals(1, viewStub.loginClicks);
        Assert.assertEquals(0, viewStub.signUpClicks);
    }
}