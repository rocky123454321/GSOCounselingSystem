package gso.model;

import java.io.Serializable;

public class StudentRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String fullName = "";
    public String course = "";
    public String email = "";
    public String contact = "";
}
