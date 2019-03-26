package com.example.demo;

import javax.persistence.Entity;

@Entity
public class JobsPreStage {
    private String jobName;
    private String jobDescription;
    private String jobStage;
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    public JobsPreStage(String jobName, String jobDescription, String jobStage,
                        String customerName, String customerPhone, String customerAddress) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobStage = jobStage;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;

    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobStage() {
        return jobStage;
    }

    public void setJobStage(String jobStage) {
        this.jobStage = jobStage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
