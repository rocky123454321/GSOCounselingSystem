package gso.service;

import gso.model.AdminAccount;
import gso.model.AppData;
import gso.model.CounselorAccount;
import gso.model.SessionRecord;
import gso.model.StudentRecord;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static gso.constants.AppConstants.STATUS_CANCELLED;
import static gso.constants.AppConstants.STATUS_FINISHED;
import static gso.constants.AppConstants.STATUS_PENDING;
import static gso.constants.AppConstants.TIME_SLOTS;

public class CounselingDataService {
    private final AppData data;

    public CounselingDataService(AppData data) {
        this.data = data;
    }

    public List<CounselorAccount> getActiveCounselors() {
        List<CounselorAccount> activeCounselors = new ArrayList<>();
        for (CounselorAccount counselor : data.counselors) {
            if (counselor.active) {
                activeCounselors.add(counselor);
            }
        }
        return activeCounselors;
    }

    public List<CounselorAccount> getSortedCounselors() {
        List<CounselorAccount> result = new ArrayList<>(data.counselors);
        result.sort(Comparator.comparing(counselor -> normalize(counselor.fullName)));
        return result;
    }

    public List<StudentRecord> getSortedStudents() {
        List<StudentRecord> result = new ArrayList<>(data.students);
        result.sort(Comparator.comparing(student -> normalize(student.fullName)));
        return result;
    }

    public List<SessionRecord> getCounselorSessions(String counselorUsername) {
        List<SessionRecord> result = new ArrayList<>();
        for (SessionRecord session : data.sessions) {
            if (session.counselorUsername.equals(counselorUsername)) {
                result.add(session);
            }
        }
        return result;
    }

    public CounselorAccount findCounselorByUsername(String username) {
        for (CounselorAccount counselor : data.counselors) {
            if (normalize(counselor.username).equals(normalize(username))) {
                return counselor;
            }
        }
        return null;
    }

    public CounselorAccount findCounselorByEmail(String email) {
        for (CounselorAccount counselor : data.counselors) {
            if (normalize(counselor.email).equals(normalize(email))) {
                return counselor;
            }
        }
        return null;
    }

    public AdminAccount findAdminByUsername(String username) {
        for (AdminAccount admin : data.admins) {
            if (normalize(admin.username).equals(normalize(username))) {
                return admin;
            }
        }
        return null;
    }

    public StudentRecord findStudentByEmail(String email) {
        for (StudentRecord student : data.students) {
            if (normalize(student.email).equals(normalize(email))) {
                return student;
            }
        }
        return null;
    }

    public boolean hasScheduleConflict(String counselorUsername, String dateIso, String timeSlot, int ignoreSessionId) {
        for (SessionRecord session : data.sessions) {
            if (session.id == ignoreSessionId) {
                continue;
            }
            if (!session.counselorUsername.equals(counselorUsername)) {
                continue;
            }
            if (!session.dateIso.equals(dateIso) || !session.timeSlot.equals(timeSlot)) {
                continue;
            }
            if (!STATUS_CANCELLED.equals(session.status)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSessionsForCounselor(String username) {
        for (SessionRecord session : data.sessions) {
            if (session.counselorUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSessionsForStudent(String email) {
        for (SessionRecord session : data.sessions) {
            if (session.studentEmail.equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public CounselorAccount counselorByUsername(String username) {
        return findCounselorByUsername(username);
    }

    public SessionRecord firstActiveSessionForCounselor(String username) {
        for (SessionRecord session : sortSessionsByDate(getCounselorSessions(username), true)) {
            if (!STATUS_FINISHED.equals(session.status) && !STATUS_CANCELLED.equals(session.status)) {
                return session;
            }
        }
        return null;
    }

    public List<SessionRecord> sortSessionsByDate(List<SessionRecord> sessions, boolean ascending) {
        List<SessionRecord> result = new ArrayList<>(sessions);
        Comparator<SessionRecord> comparator = Comparator
            .comparing((SessionRecord session) -> session.dateIso)
            .thenComparingInt(session -> timeSlotIndex(session.timeSlot))
            .thenComparingInt(session -> session.id);
        result.sort(ascending ? comparator : comparator.reversed());
        return result;
    }

    public int timeSlotIndex(String timeSlot) {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(timeSlot)) {
                return i;
            }
        }
        return TIME_SLOTS.length;
    }

    public int countActiveCounselors() {
        int count = 0;
        for (CounselorAccount counselor : data.counselors) {
            if (counselor.active) {
                count++;
            }
        }
        return count;
    }

    public int countTodaySessions() {
        String today = LocalDate.now().toString();
        int count = 0;
        for (SessionRecord session : data.sessions) {
            if (today.equals(session.dateIso)) {
                count++;
            }
        }
        return count;
    }

    public int countPendingSessions() {
        int count = 0;
        for (SessionRecord session : data.sessions) {
            if (STATUS_PENDING.equals(session.status)) {
                count++;
            }
        }
        return count;
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase(Locale.ENGLISH);
    }
}
