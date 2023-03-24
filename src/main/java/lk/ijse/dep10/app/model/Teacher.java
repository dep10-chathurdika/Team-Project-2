package lk.ijse.dep10.app.model;

import java.io.Serializable;

public class Teacher implements Serializable {
    private String id;
    private String name;
    private String address;

    public Teacher(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
    public Teacher(){}

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
