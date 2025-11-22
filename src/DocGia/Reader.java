// Lớp đại diện cho Độc giả
package DocGia;

public class Reader {

    private int id; // Mã độc giả
    private String name; // Tên độc giả
    private String address; // Địa chỉ
    private String phone; // SĐT
    private String email; // Email

    public Reader() {
    }

    public Reader(int id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

// Ghi đè phương thức toString() để hiển thị tên độc giả trong JComboBox
    @Override
    public String toString() {
        return this.name;
    }
}
