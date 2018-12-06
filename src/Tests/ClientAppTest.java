package Tests;

import App.ClientApp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.Assert.*;

public class ClientAppTest {

    int players, boot;
    ClientApp app;
    Method method;

    @Before
    public void setEverythink() {
        app = new ClientApp();
        app.setPlayers(5);
        app.setBoot(2);

    }

    @Test
    public void TestsetCheckBoxNotSelected() {
        assertEquals(5, app.getPlayers());
        assertEquals(2, app.getBoot());
    }

    @Test
    public void TestcheckPlayersNumber() {
        assertTrue(app.checkPlayersNumber(2, 2));
        assertTrue(app.checkPlayersNumber(1, 3));
        assertTrue(app.checkPlayersNumber(1, 1));
        assertNotNull(app.checkPlayersNumber(3, 3));
    }
}

