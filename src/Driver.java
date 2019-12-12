import java.util.* ;

public class Driver{

    public static void printOptions(){
        System.out.println("Welcome to the Hospital Management System! Please enter option");
        System.out.println("1: Register New Patient");
        System.out.println("2: Fetch Patient Record");
        System.out.println("3: Fetch Patient History");
        System.out.println("4: Update Patient Record After Visit");
        System.out.println("5: View Prescriptions");
        System.out.println("6: View Last Visits");
        System.out.println("7: View Medical Bills");
        System.out.println("8: Exit");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in); 

        DBHelper db = new DBHelper();

        db.activateConnection();

        printOptions();
        int option = sc.nextInt();
        while(option != 8){
            switch(option){
                case 1: 
                    db.registerNewPatient();
                    break ;
                case 2:
                    db.fetchPatientRecord();
                    break;
                case 3:
                    db.fetchPatientHistory();
                    break;
                case 4:
                    db.updatePatientRecord();
                    break;
                case 5:
                    db.viewPrescriptions();
                    break;
                case 6:
                    db.viewPatientLastVisits();
                    break;
                case 7:
                    db.viewMedicalBills();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            printOptions();
            option = sc.nextInt();
        }
        db.deactivateConnection();
    }

}