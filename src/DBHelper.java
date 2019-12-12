
// note : for the scope of the project image table hasn't been used.

import java.util.*;
import java.sql.*;
import java.time.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DBHelper {
    private static final String INSERT_SQL_PATIENT="INSERT INTO Patient (patient_id, name, email, address, age, gender, phone_number, last_visits ) VALUES (NULL,?,?,?,?,?,?, NULL)";

    //STEP 2a: Set JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/hospital_db";

    //  Database credentials
    static final String USER = "user";
    static final String PASS = "";

    static Scanner sc = new Scanner(System.in); 

    static Connection conn = null;
    static Statement st = null;
    static PreparedStatement stmt = null;
    static ResultSet rs;
    String sql = null;
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    Date dateobj = new Date();
    String todaysDate = df.format(dateobj);

   
    public void activateConnection() {
        
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void registerNewPatient(){
        System.out.println("NOTE : * indicates mandatory fields");
        System.out.println("Name* :");
        String Name = sc.nextLine();
        System.out.println("Phone Number* :");
        String phoneNumber = sc.nextLine();
        System.out.println("Email: ");
        String Email = sc.nextLine();
        System.out.println("Address: ");
        String Address = sc.nextLine();
        System.out.println("Age*: ");
        String Age = sc.nextLine();
        System.out.println("Gender* (Enter 1 for female and 0 for male):");
        String Gender = sc.nextLine();
        try{
            stmt = conn.prepareStatement(INSERT_SQL_PATIENT);
            stmt.setString(1, Name);
            stmt.setString(2, Email);
            stmt.setString(3, Address);
            stmt.setInt(4, Integer.parseInt(Age));             
            stmt.setInt(5, Integer.parseInt(Gender));             
            stmt.setLong(6, Long.parseLong(phoneNumber));            
            stmt.executeUpdate();
            System.out.println("Patient Succesfully Registered! Press Enter to Continue");
            sc.nextLine();
        }
        catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            if( e instanceof NumberFormatException){
                System.out.println("Invalid data or Missing Mandatory Fields . Enter again");
                sc.nextLine();
                registerNewPatient();
            }
        }
    }


    public void fetchPatientRecord(){
        try{
            String sql = null;
            ResultSet rs = null;
            st = conn.createStatement();
            System.out.println("Please Enter Option");
            System.out.println("1: Search Using Patient Id");
            System.out.println("2: Search Using Phone Number");
            System.out.println("3: Search Using Email Id");

            int option = sc.nextInt();
            if(option == 1){
                System.out.println("Enter Patient Id");
                int PatientId = sc.nextInt();
                sql = "SELECT * FROM Patient where patient_id = " + PatientId;
                sc.nextLine();
            } else if (option == 2){
                System.out.println("Enter Patient phoneNumber");
                long phoneNumber = sc.nextLong();
                sql = "SELECT * FROM Patient where phone_number = " + phoneNumber;
                sc.nextLine();
            } else if (option == 3){
                System.out.println("Enter Patient email id");
                String email = sc.nextLine();
                sql = "SELECT * FROM Patient where email = " + "'" + email + "'";
                sc.nextLine();
            }
            
            rs = st.executeQuery(sql);
            if (!rs.isBeforeFirst() ) {    
                System.out.println("Patient Not registered.");
            } 
            while(rs.next()){
                System.out.println("Patient id:" + rs.getInt("patient_id"));
                System.out.println("Name:" + rs.getString("name"));
                System.out.println("Age:" + rs.getInt("age"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Phone Number: " + rs.getLong("phone_number"));
                System.out.println("Email: " + rs.getString("email"));
                if(rs.getInt("gender") == 1){
                    System.out.println("Gender: Female");
                } else if(rs.getInt("gender") == 0){
                    System.out.println("Gender: Male");
                }

                System.out.println("Success. Press Enter To Continue");
                sc.nextLine();
            }
        }
            catch(SQLException se){
        //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            if ( e instanceof InputMismatchException){
                System.out.println("Check the data type of the option entered.Try again");
                sc.nextLine();
                fetchPatientRecord();
            }
        }
    }

    public void fetchPatientHistory(){
        String PatientId = null;
        System.out.println("Enter Patient Id");
        PatientId = sc.nextLine();
        sql = "SELECT * FROM Patient_Medical_History where patient_id = " + PatientId;

        try{
            st = conn.createStatement();
            rs = st.executeQuery(sql);
               if (!rs.isBeforeFirst() ) {    
                System.out.println("No data available.");
            } 
            while (rs.next()){
                System.out.println("Patient Id: " + rs.getInt("patient_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Past health issues: " + rs.getString("past_health_issues"));
                System.out.println("Family history diseases: " + rs.getString("family_history_diseases"));
            }
        } catch(SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    String getLastVisits(int PatientId){
        String lastVisits = null;
        String patientName = null;
        try{
            sql = "SELECT * FROM Patient where patient_id = " + PatientId;
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (!rs.isBeforeFirst() ) {    
                System.out.println("Patient not registered.");
                System.exit(0);
            }
            rs.next();
            lastVisits = rs.getString("last_visits");

        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        return lastVisits;
    }

    public void updateLastVisits(int PatientId){
        System.out.println("Updating Last visits");
        String old_last_visits = null;
        old_last_visits = getLastVisits(PatientId);
        if (old_last_visits == "empty"){
            return;
        }
        String reasonForVisit = null;
        System.out.println("Enter reason for visit");
        sc.nextLine();
        reasonForVisit = sc.nextLine();
        String updated_last_visits = null;
        updated_last_visits = todaysDate + "-" + reasonForVisit + "," + old_last_visits;

        sql = "UPDATE Patient SET last_visits = ?  WHERE patient_id = ? ";
        try{
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,updated_last_visits);
            stmt.setInt(2, PatientId);
            stmt.executeUpdate();
            System.out.println("last visits updated successfully");
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadPrescriptions(int PatientId){
        String doctorsName = null;
        String description = null;
        System.out.println("Uploading Prescriptions");
        sql = "INSERT INTO Prescriptions (prescription_id, prescription_date, doctors_name, description, patient_id) VALUES (NULL,?,?,?,?)";
        System.out.println("Enter Doctor's name: ");
        doctorsName = sc.nextLine();
        System.out.println("Enter prescription description");
        description = sc.nextLine();

        try{
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,todaysDate);
            stmt.setString(2,doctorsName);
            stmt.setString(3, description);
            stmt.setInt(4, PatientId);
            stmt.executeUpdate();
            System.out.println("Prescriptions Uploaded");
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void uploadMedicalBills(int PatientId){
        String amount = null;
        System.out.println("Uploading Bills");
        sql = "INSERT INTO Medical_Bills (bill_id, bill_date, amount, patient_id) VALUES (NULL,?,?,?)";
        System.out.println("Enter Bill Amount: ");
        amount = sc.nextLine();

        try{
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,todaysDate);
            stmt.setFloat(2,Float.parseFloat(amount));
            stmt.setInt(3, PatientId);
            stmt.executeUpdate();
            System.out.println("Bill Uploaded");
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void updatePatientRecord(){
        System.out.println("Enter Patient Id");
        int PatientId = sc.nextInt();
        updateLastVisits(PatientId);
        uploadPrescriptions(PatientId);
        uploadMedicalBills(PatientId);
    }


    public void viewPatientLastVisits(){
        int visits = -1;
        int PatientId = 0;
        System.out.println("1: To display certain number of past visits");
        System.out.println("2: To display all past visits");
        String str = null;
        int option = sc.nextInt();
        if (option == 1){
            System.out.println("Enter no. of visits ");
            visits = sc.nextInt();
            System.out.println("Enter Paient id");
            PatientId = sc.nextInt();
        } else if (option == 2){
            System.out.println("Enter Patient id");
            PatientId = sc.nextInt();
        }
        try{
            sql = "SELECT last_visits FROM Patient where patient_id = " + PatientId;
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            if (!rs.isBeforeFirst() ) {    
                System.out.println("No data available.");
            } 
            while(rs.next()){
                str = rs.getString("last_visits"); 
                String[] values = str.split(",");
                if (visits != -1 && visits <= values.length){
                    for(int i=0;i< visits; i++){  
                        System.out.println(values[i]);  
                    }  
                } else {
                    for(int i=0; i<values.length; i++){
                        System.out.println(values[i]);
                    }
                } 
            }
        } catch(SQLException se){
            //Handle errors for JDBC
                se.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }
    }



    public void viewPrescriptions(){
        int PatientId = 0;
        int prescriptionId = 0;
        System.out.println("Enter Patient Id");
        PatientId = sc.nextInt();
        sql = "SELECT * FROM Prescriptions WHERE patient_id = " + PatientId;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(sql);

             if (!rs.isBeforeFirst() ) {    
                System.out.println("Data not available.");
            } 

            while(rs.next()){
                System.out.println("patient id: " + rs.getInt("patient_id"));
                System.out.println("prescription id: " + rs.getInt("prescription_id"));
                System.out.println("date :" + " " + rs.getString("prescription_date"));
                System.out.println("Doctor's Name:  " +  rs.getString("doctors_name"));
                System.out.println("Description: " + rs.getString("description"));
                
                System.out.println("Press Enter To Continue");
                sc.nextLine();
                sc.nextLine();  
                
            }
        } catch(SQLException se){
                se.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
        }
    }



    public void viewMedicalBills(){
        int PatientId = 0;
        int billId = 0;
        System.out.println("Enter Patient Id");
        PatientId = sc.nextInt();
        sql = "SELECT * FROM Medical_Bills WHERE patient_id = " + PatientId;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            if (!rs.isBeforeFirst() ) {    
                System.out.println("Data not available.");
            } 
            
            while(rs.next()){
                System.out.println("Patient id: " + rs.getInt("patient_id"));
                billId = rs.getInt("bill_id");
                System.out.println("Bill id: " + billId);
                System.out.println("Bill Date: " + rs.getDate("bill_date"));
                System.out.println("Bill Amount: " + rs.getFloat("amount"));
            }
        } catch(SQLException se){
                se.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
        }
    }


    public void deactivateConnection(){
        try{
            if(stmt!=null)
                stmt.close();
        }catch(SQLException se2){
        }
        try{
            if(conn!=null)
                conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }
}
