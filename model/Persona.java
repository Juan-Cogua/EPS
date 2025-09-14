package model;

import java.util.ArrayList;
import java.util.List;

class Persona {
    private String name;
    private byte age;
    private String id;
    private String bloodType;
    private String address;
    private String phone;
    
    public Persona(String name, byte age, String id, String bloodType, String address, String phone) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.bloodType = bloodType;
        this.address = address;
        this.phone = phone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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
    //Metodos Adicionales
    
    
}