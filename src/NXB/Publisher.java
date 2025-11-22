// Lớp đại diện cho Nhà xuất bản
package NXB;

public class Publisher {

    private int id; // Mã NXB
    private String name; // Tên NXB
    private String address; // Địa chỉ
    private String phone; // Số điện thoại

    public Publisher() {
    }

    public Publisher(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
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

    @Override
    public String toString() {
        return name;
    }
}
