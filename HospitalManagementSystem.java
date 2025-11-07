import java.util.*;

// ------------------ Patient Node (Linked List Node) ------------------
class PatientNode {
    int patientID;
    String patientName;
    String admissionDate;
    String treatmentDetails;
    PatientNode next;

    public PatientNode(int id, String name, String date, String treatment) {
        this.patientID = id;
        this.patientName = name;
        this.admissionDate = date;
        this.treatmentDetails = treatment;
        this.next = null;
    }
}


// ------------------ Linked List for Patient Records ------------------
class PatientList {
    PatientNode head;

    public void insertPatient(PatientNode newPatient) {
        if (head == null) head = newPatient;
        else {
            PatientNode temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newPatient;
        }
    }

    public PatientNode deletePatient(int id) {
        PatientNode temp = head, prev = null;

        while (temp != null && temp.patientID != id) {
            prev = temp;
            temp = temp.next;
        }

        if (temp == null) return null; // Not found

        if (prev == null) head = temp.next;
        else prev.next = temp.next;

        return temp;
    }

    public PatientNode retrievePatient(int id) {
        PatientNode temp = head;
        while (temp != null) {
            if (temp.patientID == id) return temp;
            temp = temp.next;
        }
        return null;
    }

    public void displayPatients() {
        PatientNode temp = head;
        System.out.println("\n--- Current Patient Records ---");
        while (temp != null) {
            System.out.println("ID: " + temp.patientID + " | " + temp.patientName
                    + " | " + temp.admissionDate + " | " + temp.treatmentDetails);
            temp = temp.next;
        }
        System.out.println();
    }
}


// ------------------ Stack for Undo Admission ------------------
class UndoStack {
    PatientNode[] stack = new PatientNode[50];
    int top = -1;

    public void push(PatientNode p) {
        stack[++top] = p;
    }

    public PatientNode pop() {
        if (top == -1) return null;
        return stack[top--];
    }
}


// ------------------ Priority Queue for Emergency Patients ------------------
class EmergencyQueue {
    ArrayList<PatientNode> queue = new ArrayList<>();

    public void enqueue(PatientNode p) {
        queue.add(p);
        // Simulating priority: shorter treatment details = more serious (just for demonstration)
        queue.sort(Comparator.comparingInt(a -> a.treatmentDetails.length()));
    }

    public PatientNode dequeue() {
        if (queue.isEmpty()) return null;
        return queue.remove(0);
    }
}


// ------------------ Polynomial Linked List for Billing ------------------
class BillingNode {
    int coefficient;
    int power;
    BillingNode next;

    public BillingNode(int c, int p) {
        coefficient = c;
        power = p;
        next = null;
    }
}

class Billing {
    BillingNode head;

    public void insertTerm(int c, int p) {
        BillingNode n = new BillingNode(c, p);
        n.next = head;
        head = n;
    }

    public int calculate(int days) {
        int bill = 0;
        BillingNode temp = head;
        while (temp != null) {
            bill += temp.coefficient * Math.pow(days, temp.power);
            temp = temp.next;
        }
        return bill;
    }
}


// ------------------ Postfix Expression Evaluation (Inventory Calculation) ------------------
class PostfixCalculator {
    public int evaluate(String exp) {
        Stack<Integer> stack = new Stack<>();
        for (char c : exp.toCharArray()) {
            if (Character.isDigit(c)) stack.push(c - '0');
            else {
                int b = stack.pop();
                int a = stack.pop();
                switch (c) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }
}


// ------------------ MAIN SYSTEM (Menu Driven) ------------------
public class HospitalManagementSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PatientList list = new PatientList();
        UndoStack undo = new UndoStack();
        EmergencyQueue emergency = new EmergencyQueue();
        Billing billing = new Billing();
        PostfixCalculator inventory = new PostfixCalculator();

        int choice = 0;

        while (choice != 6) {
            System.out.println("\n------ Hospital Patient Record System ------");
            System.out.println("1. Add Patient");
            System.out.println("2. Undo Last Admission");
            System.out.println("3. Process Emergency Patient");
            System.out.println("4. Calculate Billing (Polynomial)");
            System.out.println("5. Evaluate Inventory (Postfix Expression)");
            System.out.println("6. Display Patients & Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Patient ID: "); int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Patient Name: "); String name = sc.nextLine();
                    System.out.print("Admission Date: "); String date = sc.nextLine();
                    System.out.print("Treatment Details: "); String treatment = sc.nextLine();

                    PatientNode p = new PatientNode(id, name, date, treatment);
                    list.insertPatient(p);
                    undo.push(p);
                    emergency.enqueue(p);
                    System.out.println("Patient Added.\n");
                    break;

                case 2:
                    PatientNode removed = undo.pop();
                    if (removed != null) {
                        list.deletePatient(removed.patientID);
                        System.out.println("Undo Successful: Removed Patient " + removed.patientName);
                    } else System.out.println("No records to undo.");
                    break;

                case 3:
                    PatientNode emergencyPatient = emergency.dequeue();
                    if (emergencyPatient != null)
                        System.out.println("Emergency Patient Processed: " + emergencyPatient.patientName);
                    else System.out.println("No Emergency Patients in Queue.");
                    break;

                case 4:
                    billing.insertTerm(200, 1);
                    billing.insertTerm(500, 0); 
                    System.out.print("Enter number of treatment days: ");
                    int days = sc.nextInt();
                    System.out.println("Total Bill = ₹" + billing.calculate(days));
                    break;

                case 5:
                    sc.nextLine();
                    System.out.print("Enter postfix expression (e.g., 23*54*+): ");
                    String exp = sc.nextLine();
                    System.out.println("Inventory Value = " + inventory.evaluate(exp));
                    break;

                case 6:
                    list.displayPatients();
                    System.out.println("Exiting System...");
                    break;
            }
        }
    }
}
