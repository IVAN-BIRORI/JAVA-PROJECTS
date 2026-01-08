import controller.ClientConnector;
import Service.ElderlyService;
import java.util.List;
import model.ElderlyPerson;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing connection to server...");
            ElderlyService service = ClientConnector.elderlyService();
            List<ElderlyPerson> list = service.getAllElderly();
            System.out.println("Connection successful! Found " + list.size() + " elderly persons.");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}