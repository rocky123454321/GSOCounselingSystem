package gso.model;

import java.io.Serializable;

import static gso.constants.AppConstants.STATUS_PENDING;

public class SessionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String counselorUsername = "";
    public String counselorName = "";
    public String studentEmail = "";
    public String studentName = "";
    public String course = "";
    public String dateIso = "";
    public String timeSlot = "";
    public String topic = "";
    public String notes = "";
    public String recommendations = "";
    public String cancellationReason = "";
    public String status = STATUS_PENDING;
}
