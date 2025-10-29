package gk1.nguyenanhkiet.thigk;

public class BaiThuoc {
    private String ten;
    private String congDung;

    public BaiThuoc(String ten, String congDung) {
        this.ten = ten;
        this.congDung = congDung;
    }

    public String getTen() {
        return ten;
    }

    public String getCongDung() {
        return congDung;
    }
}