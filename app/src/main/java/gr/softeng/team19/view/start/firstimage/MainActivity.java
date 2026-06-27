package gr.softeng.team19.view.start.firstimage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team19.R;
import gr.softeng.team19.view.start.welcome.WelcomeActivity;

/**
 * The main entry point of the application (Splash Screen).
 * This activity shows the app logo for a few seconds and prepares the data
 * before moving to the Welcome screen.
 */
public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter presenter;

    /**
     * Sets up the splash screen, initializes the database data, and starts a timer.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the presenter to handle the timing logic
        presenter = new MainPresenter(this);

        // Initialize the memory database with sample data (Drivers, Customers, etc.)
        gr.softeng.team19.memorydao.MemoryInitializer.prepareData();

        // Wait for 2.5 seconds (2500ms) and then tell the presenter to move forward
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onSplashFinished();
            }
        }, 2500);
    }

    /**
     * Opens the WelcomeActivity and adds a smooth fade transition.
     * The splash screen is closed so the user cannot go back to it.
     */
    @Override
    public void navigateToWelcome() {
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);

        // Apply a smooth visual transition between the screens
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Finish this activity so it's removed from the app history
        finish();
    }
}