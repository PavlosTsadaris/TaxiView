package gr.softeng.team19.view.driver.homeactivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;

/**
 * Unit tests for HomeDriverPresenter to verify dashboard data, navigation, and availability logic.
 */
public class HomeDriverPresenterTest {

    private HomeDriverViewStub viewStub;
    private HomeDriverPresenter presenter;
    private TaxiDriverDAOMemory driverDAO;
    private final String TEST_DRIVER_USER = "driver1";

    /**
     * Prepares the data environment and initializes the presenter with a test scheduler.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new HomeDriverViewStub();
        driverDAO = new TaxiDriverDAOMemory();

        // Immediate execution scheduler to handle timed notifications during tests
        HomeDriverPresenter.Scheduler testScheduler = (task, delay) -> task.run();

        presenter = new HomeDriverPresenter(viewStub, testScheduler);
    }

    /**
     * Verifies that a valid driver's name and rating are correctly displayed on the home screen.
     */
    @Test
    public void testOnHomePageLoadSuccess() {
        presenter.onHomePageLoad(TEST_DRIVER_USER);

        TaxiDriver driver = driverDAO.find(TEST_DRIVER_USER);
        String expectedWelcome = driver.getName() + " " + driver.getSurname();

        Assert.assertEquals(expectedWelcome, viewStub.lastWelcomeMessage);
        Assert.assertNotNull(viewStub.lastRating);
    }

    /**
     * Verifies that the system handles missing or null usernames by using fallback "Guest" text.
     */
    @Test
    public void testOnHomePageLoadInvalidUsers() {
        presenter.onHomePageLoad(null);
        Assert.assertEquals("Guest Driver", viewStub.lastWelcomeMessage);

        presenter.onHomePageLoad("unknown_user");
        Assert.assertEquals("unknown_user", viewStub.lastWelcomeMessage);
        Assert.assertEquals("0.0", viewStub.lastRating);
    }

    /**
     * Verifies the location toggle logic: updating availability in the DAO and showing notifications.
     */
    @Test
    public void testOnLocationToggleLogic() {
        presenter.onHomePageLoad(TEST_DRIVER_USER);

        // Step 1: Go Online
        presenter.onLocationToggleSelected();
        Assert.assertTrue(viewStub.isLocationActive);
        Assert.assertTrue(viewStub.navigatedToChooseLocation);
        Assert.assertEquals("New requests found nearby!", viewStub.lastNotificationMessage);
        Assert.assertTrue(driverDAO.find(TEST_DRIVER_USER).getAvailability());

        // Step 2: Go Offline
        presenter.onLocationToggleSelected();
        Assert.assertFalse(viewStub.isLocationActive);
        Assert.assertFalse(driverDAO.find(TEST_DRIVER_USER).getAvailability());
    }

    /**
     * Verifies that drivers must be "Online" to view the requests list.
     */
    @Test
    public void testOnRequestsListSelected() {
        presenter.onHomePageLoad(TEST_DRIVER_USER);

        // Fail if offline
        presenter.onRequestsListSelected();
        Assert.assertEquals("You must turn on location to receive requests!", viewStub.lastErrorMessage);

        // Succeed if online
        presenter.onLocationToggleSelected();
        presenter.onRequestsListSelected();
        Assert.assertTrue(viewStub.navigatedToRequests);
    }

    /**
     * Verifies standard button clicks lead to the correct profile, history, and ratings screens.
     */
    @Test
    public void testStandardNavigations() {
        presenter.onHomePageLoad(TEST_DRIVER_USER);

        presenter.onProfileSelected();
        Assert.assertEquals(TEST_DRIVER_USER, viewStub.navigatedProfileUsername);

        presenter.onHistorySelected();
        Assert.assertTrue(viewStub.navigatedToHistory);

        presenter.onRatingsSelected();
        Assert.assertTrue(viewStub.navigatedToRatings);
    }

    /**
     * Verifies that if a driver goes offline quickly, the "new requests" notification is cancelled.
     */
    @Test
    public void testNotificationDoesNotShowIfDriverGoesOffline() {
        final Runnable[] capturedTask = new Runnable[1];
        HomeDriverPresenter.Scheduler manualScheduler = (task, delay) -> capturedTask[0] = task;

        presenter = new HomeDriverPresenter(viewStub, manualScheduler);
        presenter.onHomePageLoad(TEST_DRIVER_USER);

        presenter.onLocationToggleSelected(); // Go online
        presenter.onLocationToggleSelected(); // Immediately go offline

        capturedTask[0].run(); // Execute the delayed notification task

        Assert.assertNull("Notification should be null if driver went offline", viewStub.lastNotificationMessage);
    }

    /**
     * Verifies that toggling location for a non-existent driver does not cause a crash.
     */
    @Test
    public void testOnLocationToggleSelectedDriverNotFound() {
        presenter.onHomePageLoad("non_existent_driver");
        presenter.onLocationToggleSelected();

        Assert.assertTrue(viewStub.isLocationActive);
    }

    /**
     * Tests the default constructor for code coverage purposes.
     */
    @Test
    public void testDefaultConstructor() {
        HomeDriverPresenter prodPresenter = new HomeDriverPresenter(viewStub);
        Assert.assertNotNull(prodPresenter);
    }
}