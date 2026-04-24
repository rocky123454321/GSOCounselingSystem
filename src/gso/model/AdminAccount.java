package gso.model;

import java.io.Serializable;

public class AdminAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String fullName = "";
    public String username = "";
    public String password = "";
}
