// Lớp đại diện cho Tác giả
package TacGia;

public class Author {
private int id; // Mã tác giả
private String name; // Tên tác giả
private String bio; // Thông tin mô tả

public Author() {}

public Author(int id, String name, String bio) {
    this.id = id;
    this.name = name;
    this.bio = bio;
}

public int getId() { return id; }
public void setId(int id) { this.id = id; }

public String getName() { return name; }
public void setName(String name) { this.name = name; }

public String getBio() { return bio; }
public void setBio(String bio) { this.bio = bio; }
@Override
public String toString() {
    return name;
}
}