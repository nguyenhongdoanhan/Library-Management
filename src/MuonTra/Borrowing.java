// Lớp đại diện cho Phiếu mượn sách
package MuonTra;

import java.sql.Date;

public class Borrowing {
private int id; // Mã phiếu mượn
private int readerId; // Mã độc giả
private int bookId; // Mã sách
private int staffId; // Mã nhân viên
private Date borrowDate; // Ngày mượn
private Date returnDate; // Ngày trả (có thể null)
private String status; // Trạng thái

public Borrowing() {}

public Borrowing(int id, int readerId, int bookId, int staffId, Date borrowDate, Date returnDate, String status) {
    this.id = id;
    this.readerId = readerId;
    this.bookId = bookId;
    this.staffId = staffId;
    this.borrowDate = borrowDate;
    this.returnDate = returnDate;
    this.status = status;
}

public int getId() { return id; }
public void setId(int id) { this.id = id; }

public int getReaderId() { return readerId; }
public void setReaderId(int readerId) { this.readerId = readerId; }

public int getBookId() { return bookId; }
public void setBookId(int bookId) { this.bookId = bookId; }

public int getStaffId() { return staffId; }
public void setStaffId(int staffId) { this.staffId = staffId; }

public Date getBorrowDate() { return borrowDate; }
public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }

public Date getReturnDate() { return returnDate; }
public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }
}