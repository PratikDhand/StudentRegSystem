public class Student {
    private int id;
    private String name;
    private String email;
    private String phone;
    private int age;
    private String gender;
    private String course;
    private String address;

    // Constructor
    public Student() {}

    public Student(String name, String email, String phone, int age,
                   String gender, String course, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.course = course;
        this.address = address;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
