
package Sach;
public class Book {
private int id; // Mã sác
private String title; // Tên sách
private int authorId; // Mã tác giả
private int publisherId; // Mã nhà xuất bản 
private int categoryId; // Mã thể loại
private int year; // Năm xuất bản
private double price; // Giá sách
private int stock; // Số lượng tồn

public Book() {}

public Book(int id, String title, int authorId, int publisherId, int categoryId, int year, double price, int stock) {
    this.id = id;
    this.title = title;
    this.authorId = authorId;
    this.publisherId = publisherId;
    this.categoryId = categoryId;
    this.year = year;
    this.price = price;
    this.stock = stock;
}

// Getter và Setter (phương thức truy cập)
public int getId() { return id; }
public void setId(int id) { this.id = id; }

public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }

public int getAuthorId() { return authorId; }
public void setAuthorId(int authorId) { this.authorId = authorId; }

public int getPublisherId() { return publisherId; }
public void setPublisherId(int publisherId) { this.publisherId = publisherId; }

public int getCategoryId() { return categoryId; }
public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

public int getYear() { return year; }
public void setYear(int year) { this.year = year; }

public double getPrice() { return price; }
public void setPrice(double price) { this.price = price; }

public int getStock() { return stock; }
public void setStock(int stock) { this.stock = stock; }

// Ghi đè phương thức toString() để hiển thị tên sách trong JComboBox
@Override
public String toString() {
    return this.title;
}
}