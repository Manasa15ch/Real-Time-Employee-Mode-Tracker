import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Scanner;

public class EmployeeModeTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("company");
        MongoCollection<Document> collection = database.getCollection("employees");

        System.out.println("---- Real Time Employee Mode Tracker ----");

        while (true) {
            System.out.println("1. Add Employee");
            System.out.println("2. Update Employee Mode");
            System.out.println("3. View All Employees");
            System.out.println("4. Start Mode Tracker");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter employee name: ");
                String name = sc.nextLine();
                System.out.print("Enter mode (Office/Remote/Leave): ");
                String mode = sc.nextLine();
                Document doc = new Document("name", name).append("mode", mode);
                collection.insertOne(doc);
                System.out.println("Employee added successfully.\n");
            } else if (choice == 2) {
                System.out.print("Enter employee name: ");
                String name = sc.nextLine();
                System.out.print("Enter new mode: ");
                String mode = sc.nextLine();
                collection.updateOne(new Document("name", name),
                        new Document("$set", new Document("mode", mode)));
                System.out.println("Employee mode updated.\n");
            } else if (choice == 3) {
                for (Document doc : collection.find()) {
                    System.out.println(doc.getString("name") + " -> " + doc.getString("mode"));
                }
                System.out.println();
            } else if (choice == 4) {
                System.out.println("Tracking employee mode changes every 5 seconds...\n");
                while (true) {
                    for (Document doc : collection.find()) {
                        System.out.println(doc.getString("name") + " : " + doc.getString("mode"));
                    }
                    System.out.println("-------------------------------------");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (choice == 5) {
                System.out.println("Exiting...");
                mongoClient.close();
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
