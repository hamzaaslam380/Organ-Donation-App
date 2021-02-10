package com.example.organdonationapp;

public class Donor {
    String id;
    String name;
    String phone;
    String disease;
    String age;
    String blood;
    String doc_id;
    String address;
    String CNIC;
    String organ_part;

    public Donor() {
    }

    public Donor(String id, String name, String phone, String disease, String age, String blood, String doc_id, String address, String CNIC, String organ_part) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.disease = disease;
        this.age = age;
        this.blood = blood;
        this.doc_id = doc_id;
        this.address = address;
        this.CNIC = CNIC;
        this.organ_part = organ_part;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getOrgan_part() {
        return organ_part;
    }

    public void setOrgan_part(String organ_part) {
        this.organ_part = organ_part;
    }
}
