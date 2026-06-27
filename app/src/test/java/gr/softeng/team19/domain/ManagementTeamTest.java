package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ManagementTeamTest {

    private ManagementTeam manager;

    @Before
    public void setUp() throws Exception{
        manager = new ManagementTeam(
                "adminUser",
                "adminPass",
                "admin@gmail.com",
                "recovery@gmail.com"
        );
    }


    @Test
    public void constructor_initializesCorrectly() {
        assertEquals("adminUser", manager.getUserName());
        assertEquals("adminPass", manager.getPassword());
        assertEquals("admin@gmail.com", manager.getEmail());
        assertEquals("recovery@gmail.com", manager.getRecoveryEmail());
    }

    @Test
    public void setUserName(){
        manager.setUserName("Panos");
        assertEquals("Panos", manager.getUserName());
    }

    @Test
    public void setEmail(){
        manager.setEmail("pousairebro@email.com");
        assertEquals("pousairebro@email.com", manager.getEmail());
    }


    @Test
    public void getFullDetails_containsAll() {
        String details = manager.getFullDetails();

        assertTrue(details.contains("Management Team Details"));
        assertTrue(details.contains("adminUser"));
        assertTrue(details.contains("adminPass"));
        assertTrue(details.contains("admin@gmail.com"));
        assertTrue(details.contains("recovery@gmail.com"));
    }

    @Test
    public void getStatistics_returnsCorrectValue() {
        assertEquals("Statitics", manager.getStatistics());
    }
    @Test
    public void managementTeam_isaUser() {
        assertTrue(manager instanceof User);
    }
}