public class Student {
    private int id;
    private String name;
    private int age;
    private String department;
    private String phone;
    private String email;

    public Student(int id, String name, int age, String department, String phone, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object[] toTableRow() {
        return new Object[]{ id, name, age, department, phone, email };
    }

    public String toCsvLine() {
        return id + "," + escapeCsv(name) + "," + age + "," + escapeCsv(department) + "," + escapeCsv(phone) + "," + escapeCsv(email);
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}
