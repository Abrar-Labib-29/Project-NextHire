package application;

public class Job {
    private String title;
    private String company;
    private String location;
    private String description;
    private String url;
    private double salaryMin;
    private double salaryMax;

    public Job(String title, String company, String location, String description, String url, double salaryMin, double salaryMax) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.description = description;
        this.url = url;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
    }

    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public double getSalaryMin() { return salaryMin; }
    public double getSalaryMax() { return salaryMax; }
} 