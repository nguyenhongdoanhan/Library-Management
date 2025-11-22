// Lớp đại diện cho Nhân viên
package NhanVien;

public class Staff {

    private int id; // Mã nhân viên
    private String name; // Tên nhân viên
    private String position; // Chức vụ
    private String phone; // SĐT
    private String email; // Email

    public Staff() {
    }

    public Staff(int id, String name, String position, String phone, String email) {
        this.id = id;
        this.name = name;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

// Ghi đè phương thức toString() để hiển thị tên nhân viên
    @Override
    public String toString() {
        return this.name;
    }
}
