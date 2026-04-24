package gso.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;

    public final List<AdminAccount> admins = new ArrayList<>();
    public final List<CounselorAccount> counselors = new ArrayList<>();
    public final List<StudentRecord> students = new ArrayList<>();
    public final List<SessionRecord> sessions = new ArrayList<>();
    public int nextAdminId = 1;
    public int nextCounselorId = 1;
    public int nextStudentId = 1;
    public int nextSessionId = 1;
}
