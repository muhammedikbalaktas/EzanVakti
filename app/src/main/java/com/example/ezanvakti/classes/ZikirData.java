package com.example.ezanvakti.classes;

public class ZikirData {

    private String isim;
    private int hedef;
    private int tamamlanan=0;

    public int getTamamlanan() {
        return tamamlanan;
    }

    public void setTamamlanan(int tamamlanan) {
        this.tamamlanan = tamamlanan;
    }

    private boolean checked=false;

    public ZikirData(String isim, int hedef) {
        this.isim = isim;
        this.hedef = hedef;
    }

    private boolean expanded=false;

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public int getHedef() {
        return hedef;
    }

    public void setHedef(int hedef) {
        this.hedef = hedef;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
