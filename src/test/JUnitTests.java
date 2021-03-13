package test;

import static org.junit.Assert.*;

import model.Database;
import model.Gateway;
import org.junit.Test;

public class JUnitTests {


    @Test
    public void setup(){
        Database db = Database.getInstance();
        Gateway gateway = new Gateway(db.createConnection());

        String sample = "prova";
        gateway.registerNewUser(sample,sample);
        assertTrue(gateway.checkUserPresence(sample,sample));

        //TODO:ELIMINARE UTENTE(VA FATTA FUNZIONE AD HOC,MAGARI PENSARE ANCHE AD UN UNREGISTER)
    }
}
