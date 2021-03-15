import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

class Web_handler {
    public static final double EXAMFEE = 1000.0;
    public static String EXAMDATE = "15th April 2019";
    public String username;
    private String password;
    Database db;

    public Web_handler(Database db) {
        this.db = db;
    }

    public Student register(String username, String password) {
        System.out.println("");
        Student s = new Student(username, password, LocalDateTime.now().toString());
        db.addStudent(s);
        return s;
    };

    public Student login(String username, String password) {
        this.username = username;
        this.password = password;
        System.out.println("Logging in");
        Student student = db.getStudent(this.username, this.password);
        if (student != null) {
            System.out.println("Login Successful");
            return student;
        } else {
            System.out.println("Login Unsuccessful");
            return null;
        }

    };

    public void show(Student s) {
        System.out.println("Profile of " + s.username);
    }

    public static void main(String args[]) {
        Database db = new Database();
        db.count = 0;
        Web_handler obj = new Web_handler(db);
        Student s = new Student("", "", LocalDateTime.now().toString());
        Scanner sc = new Scanner(System.in);
        int c = 0;
        while (c != 5) {
            System.out.println("Enter 1 to register 2 to login 3 to show profile");
            int n = sc.nextInt();
            if (n == 1) {
                System.out.println("enter username and password");
                String username = sc.next();
                String password = sc.next();
                s = obj.register(username, password);
            } else if (n == 2) {
                System.out.println("enter username and password to login");
                String username = sc.next();
                String password = sc.next();
                if (obj.login(username, password) != null)
                    s = obj.login(username, password);
            }

            else if (n == 3 && s != null)
                obj.show(s);
            c++;
        }
        sc.close();
    }
}

class Database {
    public int count;
    public Student[] std = new Student[1000];

    public void addStudent(Student s) {
        std[count] = s;
        count++;
        System.out.println("Student is added");
    }

    public Student getStudent(String username, String password) {
        for (int i = 0; i < count; i++) {
            if (username.equals(std[i].username))
                if (std[i].getPassword().equals(password))
                    return std[i];
        }
        return null;
    }
}

enum Status {
    Unverified, FeeUnpaid, Verified;
}

class Student {
    public String name;
    public Date dateOfBirth;
    public char gender;
    public String[] qualifications;
    public String residentialAddress;
    public long mobileNo;
    public String emailID;
    public String username;
    private String password;
    boolean isVerified = false;
    double amountPaid;
    private long rollNo;
    String stateOfEligibility;
    String nationality;
    final String applicationDate;
    private Status currentStatus;
    private HallTicket hallTicket;

    public Student(String username, String password, String applicationDate) {
        this.applicationDate = applicationDate;
        this.username = username;
        this.password = password;
    }

    public void updateStudent(String name) {
        this.name = name;
        System.out.println("Student is updated");
    }

    public String getPassword() {
        return this.password;
    }

    public void setStatus(Status s) {
        this.currentStatus = s;
    }

    boolean verifyDetails() {
        if (dateOfBirth.before(new Date(2000, 01, 01)) && nationality == "INDIAN") {
            currentStatus = Status.FeeUnpaid;
            return true;
        }
        return false;
    }

    public void setHallTicket(HallTicket h) {
        this.hallTicket = h;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }
}

class HallTicket {
    long rollNo;
    String name;
    String examDate;
    String examCentre;

    void showHallTicket() {
        System.out.println("Hall ticket for Mr." + name);
        System.out.println("RollNo = " + rollNo);
        System.out.println("Exam Centre = " + examCentre);
    }

    void setExamCentre(String studentAddress) {
        String nearestCentre = getNearbyAddress(studentAddress);
        this.examCentre = nearestCentre;
    }

    String getNearbyAddress(String studentAddress) {
        return "Some Address";
    }

    long generateRollNo(String applicationDate) {
        long rollNo = applicationDate.hashCode();
        return rollNo;
    }

    void generateHallTicket(Student s) {
        this.rollNo = generateRollNo(s.applicationDate);
        this.name = s.name;
        this.examDate = Web_handler.EXAMDATE;
    }
}

class ExamController {
    Student s;

    ExamController(Student s) {
        this.s = s;
    }

    void dispatchHallTicket() {
        if (s.getCurrentStatus() == Status.Verified) {

            HallTicket hallTicket = new HallTicket();
            hallTicket.generateHallTicket(s);
            System.out.println("Hall Ticket Generated Succesfully");
            s.setHallTicket(hallTicket);

        } else if (s.getCurrentStatus() == Status.Unverified) {

            System.err.println("Details are incorrect/not verified");

        } else if (s.getCurrentStatus() == Status.FeeUnpaid) {

            System.err.println("Fee not paid");

        }
    }

    void setCurrentStatus() {
        if (s.amountPaid == Web_handler.EXAMFEE) {
            s.setStatus(Status.Verified);
            s.isVerified = true;
        }
    }
}