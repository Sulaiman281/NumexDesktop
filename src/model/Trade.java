package model;

public class Trade {
    private int id;
    private String name;
    private String dates;
    private String beforeAfterDays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getBeforeAfterDays() {
        return beforeAfterDays;
    }

    public void setBeforeAfterDays(String beforeAfterDays) {
        this.beforeAfterDays = beforeAfterDays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuery(){
        return "INSERT INTO trade (name,dates,beforeAfterDays) VALUES ('" +
                getName()+"','"+
                getDates()+"','"+
                getBeforeAfterDays()+"')";
    }
}
