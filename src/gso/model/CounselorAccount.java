package gso.model;

import java.io.Serializable;

public class CounselorAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String fullName = "";
    public String specialization = "";
    public String email = "";
    public String contact = "";
    public String username = "";
    public String password = "";
    public boolean active = true;
}
