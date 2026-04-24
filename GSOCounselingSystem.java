import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GSOCounselingSystem extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String DATA_FILE_NAME = "gso-data.ser";

    private static final String CARD_COUNSELOR_SELECT = "CARD1";
    private static final String CARD_BOOKING_FORM = "CARD2";
    private static final String CARD_THANK_YOU = "CARD3";
    private static final String CARD_COUNSELOR_LOGIN = "CARD4";
    private static final String CARD_COUNSELOR_SIGNUP = "CARD4_SIGNUP";
    private static final String CARD_SESSION_ALERT = "CARD5";
    private static final String CARD_WORKSPACE = "CARD6";
    private static final String CARD_CANCELLATION = "CARD7";
    private static final String CARD_SCHEDULE = "CARD8";
    private static final String CARD_ADMIN_LOGIN = "CARD9";
    private static final String CARD_ADMIN_SIGNUP = "CARD9_SIGNUP";
    private static final String CARD_ADMIN_MENU = "CARD10";
    private static final String CARD_MANAGE_PEOPLE = "CARD11";
    private static final String CARD_MANAGE_SESSIONS = "CARD12";

    private static final String STATUS_PENDING = "Pending";
    private static final String STATUS_IN_PROGRESS = "In Progress";
    private static final String STATUS_FINISHED = "Finished";
    private static final String STATUS_CANCELLED = "Cancelled";

    private static final String[] TIME_SLOTS = {
        "08:00 AM - 09:00 AM",
        "09:00 AM - 10:00 AM",
        "10:00 AM - 11:00 AM",
        "11:00 AM - 12:00 PM",
        "01:00 PM - 02:00 PM",
        "02:00 PM - 03:00 PM",
        "03:00 PM - 04:00 PM"
    };

    private static final Color C_BG = new Color(0xF4F2EE);
    private static final Color C_BG2 = new Color(0xECEAE4);
    private static final Color C_WHITE = Color.WHITE;
    private static final Color C_BORDER = new Color(0xD6D3CB);
    private static final Color C_BORDER2 = new Color(0xB8B4AA);
    private static final Color C_TEXT = new Color(0x1A1917);
    private static final Color C_TEXT2 = new Color(0x5A5850);
    private static final Color C_TEXT3 = new Color(0x9A9689);
    private static final Color C_ACCENT = new Color(0x1A5C3A);
    private static final Color C_ACCENT2 = new Color(0x2D7A52);
    private static final Color C_ACCENT_LT = new Color(0xE8F2EC);
    private static final Color C_RED = new Color(0xC0392B);
    private static final Color C_RED_LT = new Color(0xFDF0EE);
    private static final Color C_AMBER = new Color(0xB5620C);
    private static final Color C_AMBER_LT = new Color(0xFEF3E6);
    private static final Color C_BLUE = new Color(0x1A4A7A);
    private static final Color C_BLUE_LT = new Color(0xE8F0FA);

    private static final Font F_TITLE = new Font("Segoe UI Semibold", Font.PLAIN, 22);
    private static final Font F_HEAD = new Font("Segoe UI Semibold", Font.PLAIN, 16);
    private static final Font F_SUB = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BODY = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_LABEL = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font F_CLOCK = new Font("Segoe UI Light", Font.PLAIN, 13);

    private final File dataFile = new File(DATA_FILE_NAME);
    private final DateTimeFormatter storageDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final DateTimeFormatter displayDateFormatter =
        DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel clockLabel;

    private AppData data;
    private CounselorAccount currentCounselor;
    private AdminAccount currentAdmin;
    private CounselorAccount selectedCounselorAccount;
    private SessionRecord selectedSession;
    private String managePeopleMode = "counselors";
    private String lastBookingSummary = "";

    private final List<SessionRecord> counselorAlertView = new ArrayList<>();
    private final List<SessionRecord> counselorScheduleView = new ArrayList<>();
    private final List<CounselorAccount> manageCounselorView = new ArrayList<>();
    private final List<StudentRecord> manageStudentView = new ArrayList<>();
    private final List<SessionRecord> manageSessionView = new ArrayList<>();

    private JPanel counselorGridPanel;
    private JLabel counselorSelectSubtitle;

    private JLabel bookingCounselorValueLabel;
    private JTextField bookingNameField;
    private JTextField bookingCourseField;
    private JTextField bookingEmailField;
    private JTextField bookingContactField;
    private JComboBox<DateOption> bookingDateBox;
    private JComboBox<String> bookingTimeBox;
    private JTextArea bookingTopicArea;

    private JLabel thankYouLabel;

    private JTextField counselorLoginUsernameField;
    private JPasswordField counselorLoginPasswordField;
    private JLabel counselorLoginMessageLabel;

    private JTextField counselorSignupNameField;
    private JTextField counselorSignupSpecializationField;
    private JTextField counselorSignupEmailField;
    private JTextField counselorSignupContactField;
    private JTextField counselorSignupUsernameField;
    private JPasswordField counselorSignupPasswordField;
    private JPasswordField counselorSignupConfirmField;
    private JLabel counselorSignupMessageLabel;

    private JLabel counselorAlertHeaderLabel;
    private JLabel counselorAlertEmptyLabel;
    private DefaultTableModel counselorAlertModel;
    private JTable counselorAlertTable;

    private JLabel workspaceStatusLabel;
    private JLabel workspaceSummaryLabel;
    private JTextArea workspaceTopicArea;
    private JTextArea workspaceNotesArea;
    private JTextArea workspaceRecommendationArea;

    private JLabel cancellationSummaryLabel;
    private JTextArea cancellationReasonArea;

    private JTextField counselorScheduleSearchField;
    private JComboBox<String> counselorScheduleFilterBox;
    private DefaultTableModel counselorScheduleModel;
    private JTable counselorScheduleTable;

    private JTextField adminLoginUsernameField;
    private JPasswordField adminLoginPasswordField;
    private JLabel adminLoginMessageLabel;

    private JTextField adminSignupNameField;
    private JTextField adminSignupUsernameField;
    private JPasswordField adminSignupPasswordField;
    private JPasswordField adminSignupConfirmField;
    private JLabel adminSignupMessageLabel;

    private JLabel adminMenuSubtitleLabel;
    private JLabel statCounselorsValueLabel;
    private JLabel statStudentsValueLabel;
    private JLabel statTodayValueLabel;
    private JLabel statPendingValueLabel;

    private JLabel managePeopleTitleLabel;
    private JTextField managePeopleSearchField;
    private JComboBox<String> managePeopleFilterBox;
    private DefaultTableModel managePeopleModel;
    private JTable managePeopleTable;

    private JLabel adminSessionsBadgeLabel;
    private JTextField adminSessionSearchField;
    private JComboBox<String> adminSessionStatusBox;
    private DefaultTableModel adminSessionModel;
    private JTable adminSessionTable;

    public GSOCounselingSystem() {
        super("GSO Counseling System");
        data = loadData();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 760);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);

        initLookAndFeel();
        buildUI();
        navigateTo(CARD_COUNSELOR_SELECT);
        startClock();

        setVisible(true);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("Panel.background", C_BG);
        UIManager.put("Button.font", F_SUB);
        UIManager.put("Label.font", F_BODY);
        UIManager.put("TextField.font", F_SUB);
        UIManager.put("ComboBox.font", F_SUB);
        UIManager.put("TextArea.font", F_SUB);
        UIManager.put("Table.font", F_BODY);
        UIManager.put("TableHeader.font", F_LABEL);
        UIManager.put("TableHeader.background", C_BG2);
        UIManager.put("TableHeader.foreground", C_TEXT2);
        UIManager.put("Table.selectionBackground", C_ACCENT_LT);
        UIManager.put("Table.selectionForeground", C_TEXT);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCardPanel(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(C_ACCENT);
        bar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        bar.setPreferredSize(new Dimension(0, 52));

        JLabel brand = new JLabel("GSO Counseling System");
        brand.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        brand.setForeground(Color.WHITE);
        bar.add(brand, BorderLayout.WEST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 12));
        nav.setOpaque(false);
        String[][] navItems = {
            {"Kiosk", CARD_COUNSELOR_SELECT},
            {"Counselor", CARD_COUNSELOR_LOGIN},
            {"Admin", CARD_ADMIN_LOGIN}
        };

        for (String[] item : navItems) {
            JButton button = new JButton(item[0]);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setForeground(new Color(255, 255, 255, 190));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(3, 14, 3, 14));
            button.addActionListener(e -> navigateTo(item[1]));
            barButtonHover(button);
            nav.add(button);
        }

        bar.add(nav, BorderLayout.CENTER);

        clockLabel = new JLabel();
        clockLabel.setFont(F_CLOCK);
        clockLabel.setForeground(new Color(255, 255, 255, 190));
        bar.add(clockLabel, BorderLayout.EAST);

        return bar;
    }

    private void barButtonHover(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(new Color(255, 255, 255, 190));
            }
        });
    }

    private JPanel buildCardPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(C_BG);

        cardPanel.add(buildCard1CounselorSelect(), CARD_COUNSELOR_SELECT);
        cardPanel.add(buildCard2BookingForm(), CARD_BOOKING_FORM);
        cardPanel.add(buildCard3ThankYou(), CARD_THANK_YOU);
        cardPanel.add(buildCard4CounselorLogin(), CARD_COUNSELOR_LOGIN);
        cardPanel.add(buildCard4CounselorSignup(), CARD_COUNSELOR_SIGNUP);
        cardPanel.add(buildCard5SessionAlert(), CARD_SESSION_ALERT);
        cardPanel.add(buildCard6Workspace(), CARD_WORKSPACE);
        cardPanel.add(buildCard7Cancellation(), CARD_CANCELLATION);
        cardPanel.add(buildCard8Schedule(), CARD_SCHEDULE);
        cardPanel.add(buildCard9AdminLogin(), CARD_ADMIN_LOGIN);
        cardPanel.add(buildCard9AdminSignup(), CARD_ADMIN_SIGNUP);
        cardPanel.add(buildCard10AdminMenu(), CARD_ADMIN_MENU);
        cardPanel.add(buildCard11ManagePeople(), CARD_MANAGE_PEOPLE);
        cardPanel.add(buildCard12ManageSessions(), CARD_MANAGE_SESSIONS);

        return cardPanel;
    }

    private void navigateTo(String card) {
        refreshBeforeNavigate(card);
        cardLayout.show(cardPanel, card);
    }

    private void refreshBeforeNavigate(String card) {
        if (CARD_COUNSELOR_SELECT.equals(card)) {
            refreshCounselorSelection();
        } else if (CARD_BOOKING_FORM.equals(card)) {
            refreshBookingForm();
        } else if (CARD_THANK_YOU.equals(card)) {
            refreshThankYouCard();
        } else if (CARD_COUNSELOR_LOGIN.equals(card)) {
            refreshCounselorLogin();
        } else if (CARD_COUNSELOR_SIGNUP.equals(card)) {
            refreshCounselorSignup();
        } else if (CARD_SESSION_ALERT.equals(card)) {
            if (currentCounselor == null) {
                navigateTo(CARD_COUNSELOR_LOGIN);
                return;
            }
            refreshCounselorAlert();
        } else if (CARD_WORKSPACE.equals(card)) {
            if (currentCounselor == null) {
                navigateTo(CARD_COUNSELOR_LOGIN);
                return;
            }
            refreshWorkspace();
        } else if (CARD_CANCELLATION.equals(card)) {
            if (currentCounselor == null) {
                navigateTo(CARD_COUNSELOR_LOGIN);
                return;
            }
            refreshCancellationCard();
        } else if (CARD_SCHEDULE.equals(card)) {
            if (currentCounselor == null) {
                navigateTo(CARD_COUNSELOR_LOGIN);
                return;
            }
            refreshCounselorSchedule();
        } else if (CARD_ADMIN_LOGIN.equals(card)) {
            refreshAdminLogin();
        } else if (CARD_ADMIN_SIGNUP.equals(card)) {
            refreshAdminSignup();
        } else if (CARD_ADMIN_MENU.equals(card)) {
            if (currentAdmin == null) {
                navigateTo(CARD_ADMIN_LOGIN);
                return;
            }
            refreshAdminMenu();
        } else if (CARD_MANAGE_PEOPLE.equals(card)) {
            if (currentAdmin == null) {
                navigateTo(CARD_ADMIN_LOGIN);
                return;
            }
            refreshManagePeople();
        } else if (CARD_MANAGE_SESSIONS.equals(card)) {
            if (currentAdmin == null) {
                navigateTo(CARD_ADMIN_LOGIN);
                return;
            }
            refreshManageSessions();
        }
    }

    private JPanel buildCard1CounselorSelect() {
        JPanel root = pageRoot();
        root.add(pageTitle("Select a Counselor"));
        root.add(vspace(6));

        counselorSelectSubtitle = subtitle("Choose an active counselor to book a real session.");
        root.add(counselorSelectSubtitle);
        root.add(vspace(24));

        counselorGridPanel = new JPanel(new GridLayout(0, 3, 16, 16));
        counselorGridPanel.setOpaque(false);
        root.add(counselorGridPanel);

        return wrap(root);
    }

    private JPanel buildCard2BookingForm() {
        JPanel root = pageRoot();
        root.add(pageTitle("Book a Session"));
        root.add(vspace(4));

        bookingCounselorValueLabel = subtitle("Counselor: none selected");
        root.add(bookingCounselorValueLabel);
        root.add(vspace(24));

        JPanel form = formCard();

        bookingNameField = new JTextField(20);
        bookingCourseField = new JTextField(20);
        bookingEmailField = new JTextField(20);
        bookingContactField = new JTextField(20);
        bookingTopicArea = new JTextArea(4, 20);
        styleTextArea(bookingTopicArea);

        form.add(formRow("Full Name", bookingNameField));
        form.add(vspace(12));
        form.add(formRow("Course", bookingCourseField));
        form.add(vspace(12));
        form.add(formRow("Email Address", bookingEmailField));
        form.add(vspace(12));
        form.add(formRow("Contact Number", bookingContactField));
        form.add(vspace(12));

        JPanel slotRow = new JPanel(new GridLayout(1, 2, 16, 0));
        slotRow.setOpaque(false);

        bookingDateBox = new JComboBox<>();
        bookingTimeBox = new JComboBox<>(TIME_SLOTS);
        styleCombo(bookingDateBox);
        styleCombo(bookingTimeBox);

        JPanel left = new JPanel(new BorderLayout(0, 4));
        left.setOpaque(false);
        left.add(fieldLabel("Preferred Date"), BorderLayout.NORTH);
        left.add(bookingDateBox, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(0, 4));
        right.setOpaque(false);
        right.add(fieldLabel("Preferred Time"), BorderLayout.NORTH);
        right.add(bookingTimeBox, BorderLayout.CENTER);

        slotRow.add(left);
        slotRow.add(right);
        form.add(slotRow);
        form.add(vspace(12));

        JPanel topicPanel = new JPanel(new BorderLayout(0, 4));
        topicPanel.setOpaque(false);
        topicPanel.add(fieldLabel("Concern / Topic"), BorderLayout.NORTH);
        topicPanel.add(new JScrollPane(bookingTopicArea), BorderLayout.CENTER);
        form.add(topicPanel);
        form.add(vspace(20));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back");
        backButton.addActionListener(e -> navigateTo(CARD_COUNSELOR_SELECT));

        JButton bookButton = primaryButton("Confirm Booking");
        bookButton.addActionListener(e -> submitBooking());

        buttons.add(backButton);
        buttons.add(hspace(10));
        buttons.add(bookButton);
        form.add(buttons);

        root.add(form);
        return wrap(root);
    }

    private JPanel buildCard3ThankYou() {
        JPanel root = pageRoot();
        root.add(vspace(20));

        JPanel card = centerCard();
        card.setMaximumSize(new Dimension(560, 9999));

        JLabel title = centered("Booking Saved", new Font("Segoe UI Semibold", Font.PLAIN, 26), C_ACCENT);
        card.add(title);
        card.add(vspace(8));

        thankYouLabel = centered("", F_SUB, C_TEXT2);
        card.add(thankYouLabel);
        card.add(vspace(8));
        card.add(centered("The session now appears in the counselor and admin dashboards.",
            F_SMALL, C_TEXT3));
        card.add(vspace(24));

        JButton backButton = primaryButton("Back to Start");
        backButton.setAlignmentX(0.5f);
        backButton.addActionListener(e -> navigateTo(CARD_COUNSELOR_SELECT));
        card.add(backButton);

        root.add(centerWrap(card));
        return wrap(root);
    }

    private JPanel buildCard4CounselorLogin() {
        JPanel root = pageRoot();
        JPanel card = centerCard();
        card.setMaximumSize(new Dimension(460, 9999));

        card.add(centered("Counselor Sign In", new Font("Segoe UI Semibold", Font.PLAIN, 20), C_TEXT));
        card.add(vspace(6));
        card.add(centered("Use your real account or create one below.", F_SMALL, C_TEXT3));
        card.add(vspace(20));

        counselorLoginUsernameField = new JTextField();
        counselorLoginPasswordField = new JPasswordField();
        card.add(formRow("Username", counselorLoginUsernameField));
        card.add(vspace(12));
        card.add(formRow("Password", counselorLoginPasswordField));
        card.add(vspace(12));

        counselorLoginMessageLabel = messageLabel();
        card.add(counselorLoginMessageLabel);
        card.add(vspace(16));

        JButton loginButton = primaryButton("Sign In");
        loginButton.setAlignmentX(0.5f);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.addActionListener(e -> submitCounselorLogin());
        card.add(loginButton);
        card.add(vspace(10));

        JButton signupButton = outlineButton("Create Counselor Account");
        signupButton.setAlignmentX(0.5f);
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        signupButton.addActionListener(e -> navigateTo(CARD_COUNSELOR_SIGNUP));
        card.add(signupButton);

        root.add(vspace(18));
        root.add(centerWrap(card));
        return wrap(root);
    }

    private JPanel buildCard4CounselorSignup() {
        JPanel root = pageRoot();
        JPanel card = centerCard();
        card.setMaximumSize(new Dimension(520, 9999));

        card.add(centered("Counselor Sign Up", new Font("Segoe UI Semibold", Font.PLAIN, 20), C_TEXT));
        card.add(vspace(6));
        card.add(centered("This creates a usable counselor account for kiosk booking and login.", F_SMALL, C_TEXT3));
        card.add(vspace(20));

        counselorSignupNameField = new JTextField();
        counselorSignupSpecializationField = new JTextField();
        counselorSignupEmailField = new JTextField();
        counselorSignupContactField = new JTextField();
        counselorSignupUsernameField = new JTextField();
        counselorSignupPasswordField = new JPasswordField();
        counselorSignupConfirmField = new JPasswordField();

        card.add(formRow("Full Name", counselorSignupNameField));
        card.add(vspace(12));
        card.add(formRow("Specialization / Program", counselorSignupSpecializationField));
        card.add(vspace(12));
        card.add(formRow("Email Address", counselorSignupEmailField));
        card.add(vspace(12));
        card.add(formRow("Contact Number", counselorSignupContactField));
        card.add(vspace(12));
        card.add(formRow("Username", counselorSignupUsernameField));
        card.add(vspace(12));
        card.add(formRow("Password", counselorSignupPasswordField));
        card.add(vspace(12));
        card.add(formRow("Confirm Password", counselorSignupConfirmField));
        card.add(vspace(12));

        counselorSignupMessageLabel = messageLabel();
        card.add(counselorSignupMessageLabel);
        card.add(vspace(16));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back to Sign In");
        backButton.addActionListener(e -> navigateTo(CARD_COUNSELOR_LOGIN));

        JButton createButton = primaryButton("Create Account");
        createButton.addActionListener(e -> submitCounselorSignup());

        buttons.add(backButton);
        buttons.add(hspace(10));
        buttons.add(createButton);
        card.add(buttons);

        root.add(vspace(18));
        root.add(centerWrap(card));
        return wrap(root);
    }

    private JPanel buildCard5SessionAlert() {
        JPanel root = pageRoot();
        root.add(pageTitle("Counselor Dashboard"));
        root.add(vspace(4));

        counselorAlertHeaderLabel = subtitle("");
        root.add(counselorAlertHeaderLabel);
        root.add(vspace(16));

        counselorAlertEmptyLabel = subtitle("");
        root.add(counselorAlertEmptyLabel);
        root.add(vspace(8));

        counselorAlertModel = nonEditableModel(new String[] {"ID", "Date", "Time", "Student", "Status"});
        counselorAlertTable = new JTable(counselorAlertModel);
        styleTable(counselorAlertTable);
        installStatusColumn(counselorAlertTable, 4);
        counselorAlertTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = counselorAlertTable.getSelectedRow();
                if (row >= 0 && row < counselorAlertView.size()) {
                    selectedSession = counselorAlertView.get(row);
                }
            }
        });

        root.add(wrapTable(counselorAlertTable));
        root.add(vspace(16));

        JPanel buttons = hbox();

        JButton openButton = primaryButton("Open Session");
        openButton.addActionListener(e -> {
            if (ensureSelectedSession()) {
                navigateTo(CARD_WORKSPACE);
            }
        });

        JButton cancelButton = dangerButton("Cancel Session");
        cancelButton.addActionListener(e -> {
            if (ensureSelectedSession()) {
                navigateTo(CARD_CANCELLATION);
            }
        });

        JButton scheduleButton = outlineButton("My Schedule");
        scheduleButton.addActionListener(e -> navigateTo(CARD_SCHEDULE));

        JButton signOutButton = outlineButton("Sign Out");
        signOutButton.addActionListener(e -> logoutCounselor());

        buttons.add(openButton);
        buttons.add(hspace(10));
        buttons.add(cancelButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(scheduleButton);
        buttons.add(hspace(10));
        buttons.add(signOutButton);

        root.add(buttons);
        return wrap(root);
    }

    private JPanel buildCard6Workspace() {
        JPanel root = pageRoot();

        JPanel header = hbox();
        header.add(pageTitle("Session Workspace"));
        header.add(Box.createHorizontalGlue());
        workspaceStatusLabel = badgeLabel("", C_AMBER_LT, C_AMBER);
        header.add(workspaceStatusLabel);
        root.add(header);
        root.add(vspace(4));

        workspaceSummaryLabel = subtitle("");
        root.add(workspaceSummaryLabel);
        root.add(vspace(18));

        JPanel columns = new JPanel(new GridLayout(1, 2, 16, 0));
        columns.setOpaque(false);

        JPanel left = formCard();
        workspaceTopicArea = new JTextArea(4, 20);
        workspaceNotesArea = new JTextArea(8, 20);
        styleTextArea(workspaceTopicArea);
        styleTextArea(workspaceNotesArea);

        left.add(sectionLabel("Topic"));
        left.add(vspace(6));
        left.add(new JScrollPane(workspaceTopicArea));
        left.add(vspace(14));
        left.add(sectionLabel("Notes"));
        left.add(vspace(6));
        left.add(new JScrollPane(workspaceNotesArea));

        JPanel right = formCard();
        workspaceRecommendationArea = new JTextArea(12, 20);
        styleTextArea(workspaceRecommendationArea);

        right.add(sectionLabel("Recommendations"));
        right.add(vspace(6));
        right.add(new JScrollPane(workspaceRecommendationArea));

        columns.add(left);
        columns.add(right);
        root.add(columns);
        root.add(vspace(16));

        JPanel buttons = hbox();

        JButton backButton = outlineButton("Back");
        backButton.addActionListener(e -> navigateTo(CARD_SESSION_ALERT));

        JButton saveButton = outlineButton("Save Draft");
        saveButton.addActionListener(e -> saveSelectedSessionDraft(false));

        JButton startButton = outlineButton("Mark In Progress");
        startButton.addActionListener(e -> saveSelectedSessionDraft(true));

        JButton finishButton = primaryButton("Finish Session");
        finishButton.addActionListener(e -> finishSelectedSession());

        JButton cancelButton = dangerButton("Cancel Session");
        cancelButton.addActionListener(e -> {
            if (ensureSelectedSession()) {
                navigateTo(CARD_CANCELLATION);
            }
        });

        buttons.add(backButton);
        buttons.add(hspace(8));
        buttons.add(saveButton);
        buttons.add(hspace(8));
        buttons.add(startButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(cancelButton);
        buttons.add(hspace(8));
        buttons.add(finishButton);

        root.add(buttons);
        return wrap(root);
    }

    private JPanel buildCard7Cancellation() {
        JPanel root = pageRoot();
        root.add(pageTitle("Cancel Session"));
        root.add(vspace(4));

        cancellationSummaryLabel = subtitle("");
        root.add(cancellationSummaryLabel);
        root.add(vspace(18));

        JPanel card = formCard();
        cancellationReasonArea = new JTextArea(5, 20);
        styleTextArea(cancellationReasonArea);

        card.add(sectionLabel("Reason"));
        card.add(vspace(6));
        card.add(new JScrollPane(cancellationReasonArea));
        card.add(vspace(18));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back");
        backButton.addActionListener(e -> navigateTo(CARD_WORKSPACE));

        JButton confirmButton = dangerButton("Confirm Cancellation");
        confirmButton.addActionListener(e -> cancelSelectedSession());

        buttons.add(backButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(confirmButton);
        card.add(buttons);

        root.add(card);
        return wrap(root);
    }

    private JPanel buildCard8Schedule() {
        JPanel root = pageRoot();

        JPanel header = hbox();
        header.add(pageTitle("My Schedule and History"));
        header.add(Box.createHorizontalGlue());
        header.add(badgeLabel("Counselor View", C_BLUE_LT, C_BLUE));
        root.add(header);
        root.add(vspace(4));
        root.add(subtitle("Search, open, and monitor your own session records."));
        root.add(vspace(16));

        JPanel filters = hbox();
        counselorScheduleSearchField = new JTextField(18);
        styleField(counselorScheduleSearchField);
        counselorScheduleFilterBox = new JComboBox<>(new String[] {
            "All Sessions",
            STATUS_PENDING,
            STATUS_IN_PROGRESS,
            STATUS_FINISHED,
            STATUS_CANCELLED
        });
        styleCombo(counselorScheduleFilterBox);

        installRefreshOnTyping(counselorScheduleSearchField, this::refreshCounselorSchedule);
        counselorScheduleFilterBox.addActionListener(e -> refreshCounselorSchedule());

        filters.add(fieldLabel("Search"));
        filters.add(hspace(8));
        filters.add(counselorScheduleSearchField);
        filters.add(hspace(12));
        filters.add(fieldLabel("Status"));
        filters.add(hspace(8));
        filters.add(counselorScheduleFilterBox);

        root.add(filters);
        root.add(vspace(12));

        counselorScheduleModel = nonEditableModel(
            new String[] {"ID", "Date", "Time", "Student", "Course", "Status"});
        counselorScheduleTable = new JTable(counselorScheduleModel);
        styleTable(counselorScheduleTable);
        installStatusColumn(counselorScheduleTable, 5);
        counselorScheduleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = counselorScheduleTable.getSelectedRow();
                if (row >= 0 && row < counselorScheduleView.size()) {
                    selectedSession = counselorScheduleView.get(row);
                }
            }
        });

        root.add(wrapTable(counselorScheduleTable));
        root.add(vspace(16));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back to Dashboard");
        backButton.addActionListener(e -> navigateTo(CARD_SESSION_ALERT));

        JButton openButton = primaryButton("Open Selected");
        openButton.addActionListener(e -> {
            if (ensureSelectedSession()) {
                navigateTo(CARD_WORKSPACE);
            }
        });

        JButton signOutButton = outlineButton("Sign Out");
        signOutButton.addActionListener(e -> logoutCounselor());

        buttons.add(backButton);
        buttons.add(hspace(10));
        buttons.add(openButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(signOutButton);

        root.add(buttons);
        return wrap(root);
    }

    private JPanel buildCard9AdminLogin() {
        JPanel root = pageRoot();
        JPanel card = centerCard();
        card.setMaximumSize(new Dimension(460, 9999));

        card.add(centered("Admin Sign In", new Font("Segoe UI Semibold", Font.PLAIN, 20), C_TEXT));
        card.add(vspace(6));
        card.add(centered("No database is used. Accounts are saved locally in this folder.", F_SMALL, C_TEXT3));
        card.add(vspace(20));

        adminLoginUsernameField = new JTextField();
        adminLoginPasswordField = new JPasswordField();
        card.add(formRow("Username", adminLoginUsernameField));
        card.add(vspace(12));
        card.add(formRow("Password", adminLoginPasswordField));
        card.add(vspace(12));

        adminLoginMessageLabel = messageLabel();
        card.add(adminLoginMessageLabel);
        card.add(vspace(16));

        JButton signInButton = primaryButton("Sign In");
        signInButton.setAlignmentX(0.5f);
        signInButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signInButton.addActionListener(e -> submitAdminLogin());
        card.add(signInButton);
        card.add(vspace(10));

        JButton createButton = outlineButton("Create Admin Account");
        createButton.setAlignmentX(0.5f);
        createButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        createButton.addActionListener(e -> navigateTo(CARD_ADMIN_SIGNUP));
        card.add(createButton);

        root.add(vspace(18));
        root.add(centerWrap(card));
        return wrap(root);
    }

    private JPanel buildCard9AdminSignup() {
        JPanel root = pageRoot();
        JPanel card = centerCard();
        card.setMaximumSize(new Dimension(500, 9999));

        card.add(centered("Admin Sign Up", new Font("Segoe UI Semibold", Font.PLAIN, 20), C_TEXT));
        card.add(vspace(6));
        card.add(centered("Create an admin account to manage counselors, students, and sessions.", F_SMALL, C_TEXT3));
        card.add(vspace(20));

        adminSignupNameField = new JTextField();
        adminSignupUsernameField = new JTextField();
        adminSignupPasswordField = new JPasswordField();
        adminSignupConfirmField = new JPasswordField();

        card.add(formRow("Full Name", adminSignupNameField));
        card.add(vspace(12));
        card.add(formRow("Username", adminSignupUsernameField));
        card.add(vspace(12));
        card.add(formRow("Password", adminSignupPasswordField));
        card.add(vspace(12));
        card.add(formRow("Confirm Password", adminSignupConfirmField));
        card.add(vspace(12));

        adminSignupMessageLabel = messageLabel();
        card.add(adminSignupMessageLabel);
        card.add(vspace(16));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back to Sign In");
        backButton.addActionListener(e -> navigateTo(CARD_ADMIN_LOGIN));

        JButton createButton = primaryButton("Create Admin");
        createButton.addActionListener(e -> submitAdminSignup());

        buttons.add(backButton);
        buttons.add(hspace(10));
        buttons.add(createButton);
        card.add(buttons);

        root.add(vspace(18));
        root.add(centerWrap(card));
        return wrap(root);
    }

    private JPanel buildCard10AdminMenu() {
        JPanel root = pageRoot();
        root.add(pageTitle("Admin Control Center"));
        root.add(vspace(4));

        adminMenuSubtitleLabel = subtitle("");
        root.add(adminMenuSubtitleLabel);
        root.add(vspace(24));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 16, 16));
        buttons.setOpaque(false);
        buttons.add(menuCard("Manage Counselors",
            "Add, edit, activate, deactivate, or remove counselor accounts.",
            () -> {
                managePeopleMode = "counselors";
                navigateTo(CARD_MANAGE_PEOPLE);
            }));
        buttons.add(menuCard("Manage Sessions",
            "Monitor all bookings, update statuses, and review details.",
            () -> navigateTo(CARD_MANAGE_SESSIONS)));
        buttons.add(menuCard("Manage Students",
            "Review student records created by kiosk bookings or admin input.",
            () -> {
                managePeopleMode = "students";
                navigateTo(CARD_MANAGE_PEOPLE);
            }));

        root.add(buttons);
        root.add(vspace(24));
        root.add(sectionLabel("Quick Stats"));
        root.add(vspace(10));

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.add(statCard("Counselors", C_ACCENT_LT, C_ACCENT, true));
        stats.add(statCard("Students", C_BLUE_LT, C_BLUE, false));
        stats.add(statCard("Today", C_AMBER_LT, C_AMBER, false));
        stats.add(statCard("Pending", C_RED_LT, C_RED, false));

        root.add(stats);
        root.add(vspace(16));

        JPanel footer = hbox();
        JButton signOutButton = outlineButton("Sign Out");
        signOutButton.addActionListener(e -> logoutAdmin());
        footer.add(signOutButton);
        root.add(footer);

        return wrap(root);
    }

    private JPanel buildCard11ManagePeople() {
        JPanel root = pageRoot();

        JPanel header = hbox();
        managePeopleTitleLabel = pageTitle("Manage People");
        header.add(managePeopleTitleLabel);
        header.add(Box.createHorizontalGlue());

        JButton addButton = primaryButton("Add New");
        addButton.addActionListener(e -> openPeopleEditor());
        header.add(addButton);

        root.add(header);
        root.add(vspace(4));
        root.add(subtitle("These tables are live and reflect real signups and bookings."));
        root.add(vspace(16));

        JPanel filters = hbox();
        managePeopleSearchField = new JTextField(18);
        managePeopleFilterBox = new JComboBox<>();
        styleField(managePeopleSearchField);
        styleCombo(managePeopleFilterBox);
        installRefreshOnTyping(managePeopleSearchField, this::refreshManagePeople);
        managePeopleFilterBox.addActionListener(e -> refreshManagePeople());

        filters.add(fieldLabel("Search"));
        filters.add(hspace(8));
        filters.add(managePeopleSearchField);
        filters.add(hspace(12));
        filters.add(fieldLabel("Filter"));
        filters.add(hspace(8));
        filters.add(managePeopleFilterBox);

        root.add(filters);
        root.add(vspace(12));

        managePeopleModel = nonEditableModel(new String[] {"ID", "Name", "Details"});
        managePeopleTable = new JTable(managePeopleModel);
        styleTable(managePeopleTable);
        root.add(wrapTable(managePeopleTable));
        root.add(vspace(16));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back to Menu");
        backButton.addActionListener(e -> navigateTo(CARD_ADMIN_MENU));

        JButton editButton = outlineButton("Edit Selected");
        editButton.addActionListener(e -> editSelectedPerson());

        JButton deleteButton = dangerButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedPerson());

        buttons.add(backButton);
        buttons.add(hspace(10));
        buttons.add(editButton);
        buttons.add(hspace(10));
        buttons.add(deleteButton);

        root.add(buttons);
        return wrap(root);
    }

    private JPanel buildCard12ManageSessions() {
        JPanel root = pageRoot();

        JPanel header = hbox();
        header.add(pageTitle("Manage Sessions"));
        header.add(Box.createHorizontalGlue());
        adminSessionsBadgeLabel = badgeLabel("0 Sessions", C_ACCENT_LT, C_ACCENT);
        header.add(adminSessionsBadgeLabel);
        root.add(header);
        root.add(vspace(4));
        root.add(subtitle("Search, inspect, and update any session in the system."));
        root.add(vspace(16));

        JPanel filters = hbox();
        adminSessionSearchField = new JTextField(18);
        adminSessionStatusBox = new JComboBox<>(new String[] {
            "All Status",
            STATUS_PENDING,
            STATUS_IN_PROGRESS,
            STATUS_FINISHED,
            STATUS_CANCELLED
        });
        styleField(adminSessionSearchField);
        styleCombo(adminSessionStatusBox);
        installRefreshOnTyping(adminSessionSearchField, this::refreshManageSessions);
        adminSessionStatusBox.addActionListener(e -> refreshManageSessions());

        filters.add(fieldLabel("Search"));
        filters.add(hspace(8));
        filters.add(adminSessionSearchField);
        filters.add(hspace(12));
        filters.add(fieldLabel("Status"));
        filters.add(hspace(8));
        filters.add(adminSessionStatusBox);

        root.add(filters);
        root.add(vspace(12));

        adminSessionModel = nonEditableModel(
            new String[] {"ID", "Counselor", "Student", "Date", "Time", "Status"});
        adminSessionTable = new JTable(adminSessionModel);
        styleTable(adminSessionTable);
        installStatusColumn(adminSessionTable, 5);
        adminSessionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = adminSessionTable.getSelectedRow();
                if (row >= 0 && row < manageSessionView.size()) {
                    selectedSession = manageSessionView.get(row);
                }
            }
        });

        root.add(wrapTable(adminSessionTable));
        root.add(vspace(16));

        JPanel buttons = hbox();
        JButton backButton = outlineButton("Back to Menu");
        backButton.addActionListener(e -> navigateTo(CARD_ADMIN_MENU));

        JButton detailsButton = outlineButton("Details");
        detailsButton.addActionListener(e -> showSelectedSessionDetails());

        JButton progressButton = outlineButton("Mark In Progress");
        progressButton.addActionListener(e -> adminUpdateSelectedSessionStatus(STATUS_IN_PROGRESS));

        JButton finishButton = primaryButton("Mark Finished");
        finishButton.addActionListener(e -> adminUpdateSelectedSessionStatus(STATUS_FINISHED));

        JButton cancelButton = dangerButton("Cancel");
        cancelButton.addActionListener(e -> adminCancelSelectedSession());

        buttons.add(backButton);
        buttons.add(hspace(8));
        buttons.add(detailsButton);
        buttons.add(hspace(8));
        buttons.add(progressButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(cancelButton);
        buttons.add(hspace(8));
        buttons.add(finishButton);

        root.add(buttons);
        return wrap(root);
    }

    private void refreshCounselorSelection() {
        counselorGridPanel.removeAll();

        List<CounselorAccount> counselors = getActiveCounselors();
        if (counselors.isEmpty()) {
            counselorSelectSubtitle.setText("No active counselors yet. Create a counselor account first.");
            counselorGridPanel.setLayout(new GridLayout(1, 1, 0, 0));

            JPanel empty = formCard();
            empty.add(centered("No counselor accounts available", F_HEAD, C_TEXT));
            empty.add(vspace(8));
            empty.add(centered("Go to Counselor > Create Counselor Account to add one.", F_SUB, C_TEXT2));
            empty.add(vspace(18));

            JButton createButton = primaryButton("Create Counselor Account");
            createButton.setAlignmentX(0.5f);
            createButton.addActionListener(e -> navigateTo(CARD_COUNSELOR_SIGNUP));
            empty.add(createButton);

            counselorGridPanel.add(empty);
        } else {
            counselorSelectSubtitle.setText("Choose an active counselor and create a live booking.");
            counselorGridPanel.setLayout(new GridLayout(0, 3, 16, 16));
            for (CounselorAccount counselor : counselors) {
                counselorGridPanel.add(counselorCard(counselor));
            }
        }

        counselorGridPanel.revalidate();
        counselorGridPanel.repaint();
    }

    private void refreshBookingForm() {
        bookingCounselorValueLabel.setText(
            selectedCounselorAccount == null
                ? "Counselor: none selected"
                : "Counselor: " + selectedCounselorAccount.fullName + " (" + selectedCounselorAccount.specialization + ")");

        bookingDateBox.removeAllItems();
        for (int i = 0; i < 14; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            bookingDateBox.addItem(new DateOption(date.format(storageDateFormatter), displayDate(date)));
        }
    }

    private void refreshThankYouCard() {
        thankYouLabel.setText(lastBookingSummary.isEmpty() ? "Booking completed." : lastBookingSummary);
    }

    private void refreshCounselorLogin() {
        if (data.counselors.isEmpty()) {
            setMessage(counselorLoginMessageLabel, "No counselor account exists yet. Create one first.", C_BLUE);
        }
    }

    private void refreshCounselorSignup() {
        if (counselorSignupMessageLabel.getText().trim().isEmpty()) {
            setMessage(counselorSignupMessageLabel, "Counselor accounts appear immediately in the kiosk.", C_TEXT3);
        }
    }

    private void refreshCounselorAlert() {
        counselorAlertHeaderLabel.setText("Signed in as " + currentCounselor.fullName + " - " + currentCounselor.specialization);
        counselorAlertView.clear();

        List<SessionRecord> sessions = getCounselorSessions(currentCounselor.username);
        sessions.removeIf(session -> STATUS_FINISHED.equals(session.status) || STATUS_CANCELLED.equals(session.status));

        counselorAlertModel.setRowCount(0);
        for (SessionRecord session : sortSessionsByDate(sessions, true)) {
            counselorAlertView.add(session);
            counselorAlertModel.addRow(new Object[] {
                session.id,
                displayDate(session.dateIso),
                session.timeSlot,
                session.studentName,
                session.status
            });
        }

        if (counselorAlertView.isEmpty()) {
            selectedSession = null;
            counselorAlertEmptyLabel.setText("No active sessions right now.");
        } else {
            counselorAlertEmptyLabel.setText("Select a session to open notes, finish it, or cancel it.");
            selectSessionInTable(counselorAlertTable, counselorAlertView, selectedSession);
        }
    }

    private void refreshWorkspace() {
        if (selectedSession == null) {
            workspaceStatusLabel.setText("  No Session Selected  ");
            workspaceSummaryLabel.setText("Choose a session from the dashboard or schedule.");
            workspaceTopicArea.setText("");
            workspaceNotesArea.setText("");
            workspaceRecommendationArea.setText("");
            return;
        }

        workspaceStatusLabel.setText("  " + selectedSession.status + "  ");
        workspaceStatusLabel.setBackground(statusBackground(selectedSession.status));
        workspaceStatusLabel.setForeground(statusColor(selectedSession.status));
        workspaceSummaryLabel.setText(
            "Student: " + selectedSession.studentName + " | " +
            displayDate(selectedSession.dateIso) + " | " +
            selectedSession.timeSlot);
        workspaceTopicArea.setText(selectedSession.topic);
        workspaceNotesArea.setText(selectedSession.notes);
        workspaceRecommendationArea.setText(selectedSession.recommendations);
    }

    private void refreshCancellationCard() {
        if (selectedSession == null) {
            cancellationSummaryLabel.setText("No session selected.");
            cancellationReasonArea.setText("");
            return;
        }

        cancellationSummaryLabel.setText(
            selectedSession.studentName + " | " +
            selectedSession.counselorName + " | " +
            displayDate(selectedSession.dateIso) + " | " +
            selectedSession.timeSlot);
        cancellationReasonArea.setText(selectedSession.cancellationReason);
    }

    private void refreshCounselorSchedule() {
        counselorScheduleView.clear();
        counselorScheduleModel.setRowCount(0);

        String search = normalize(counselorScheduleSearchField.getText());
        String filter = String.valueOf(counselorScheduleFilterBox.getSelectedItem());

        for (SessionRecord session : sortSessionsByDate(getCounselorSessions(currentCounselor.username), true)) {
            boolean matchesSearch = search.isEmpty()
                || normalize(session.studentName).contains(search)
                || normalize(session.course).contains(search)
                || normalize(session.topic).contains(search);
            boolean matchesFilter = "All Sessions".equals(filter) || filter.equals(session.status);

            if (matchesSearch && matchesFilter) {
                counselorScheduleView.add(session);
                counselorScheduleModel.addRow(new Object[] {
                    session.id,
                    displayDate(session.dateIso),
                    session.timeSlot,
                    session.studentName,
                    session.course,
                    session.status
                });
            }
        }

        selectSessionInTable(counselorScheduleTable, counselorScheduleView, selectedSession);
    }

    private void refreshAdminLogin() {
        if (data.admins.isEmpty()) {
            setMessage(adminLoginMessageLabel, "No admin account exists yet. Create one first.", C_BLUE);
        }
    }

    private void refreshAdminSignup() {
        if (adminSignupMessageLabel.getText().trim().isEmpty()) {
            setMessage(adminSignupMessageLabel, "Admin accounts are stored locally, not in a database.", C_TEXT3);
        }
    }

    private void refreshAdminMenu() {
        adminMenuSubtitleLabel.setText("Signed in as " + currentAdmin.fullName + ".");
        statCounselorsValueLabel.setText(String.valueOf(countActiveCounselors()));
        statStudentsValueLabel.setText(String.valueOf(data.students.size()));
        statTodayValueLabel.setText(String.valueOf(countTodaySessions()));
        statPendingValueLabel.setText(String.valueOf(countPendingSessions()));
    }

    private void refreshManagePeople() {
        boolean counselorsMode = "counselors".equals(managePeopleMode);
        managePeopleTitleLabel.setText(counselorsMode ? "Manage Counselors" : "Manage Students");

        repopulatePeopleFilter(counselorsMode);

        String search = normalize(managePeopleSearchField.getText());
        String filter = String.valueOf(managePeopleFilterBox.getSelectedItem());

        managePeopleModel.setRowCount(0);
        manageCounselorView.clear();
        manageStudentView.clear();

        if (counselorsMode) {
            managePeopleModel.setColumnIdentifiers(new String[] {
                "ID", "Name", "Specialization", "Email", "Username", "Status"
            });

            for (CounselorAccount counselor : getSortedCounselors()) {
                boolean matchesSearch = search.isEmpty()
                    || normalize(counselor.fullName).contains(search)
                    || normalize(counselor.email).contains(search)
                    || normalize(counselor.username).contains(search)
                    || normalize(counselor.specialization).contains(search);
                boolean matchesFilter = "All".equals(filter)
                    || (counselor.active && "Active".equals(filter))
                    || (!counselor.active && "Inactive".equals(filter));

                if (matchesSearch && matchesFilter) {
                    manageCounselorView.add(counselor);
                    managePeopleModel.addRow(new Object[] {
                        counselor.id,
                        counselor.fullName,
                        counselor.specialization,
                        counselor.email,
                        counselor.username,
                        counselor.active ? "Active" : "Inactive"
                    });
                }
            }

            installStatusColumn(managePeopleTable, 5);
        } else {
            managePeopleModel.setColumnIdentifiers(new String[] {
                "ID", "Name", "Course", "Email", "Contact"
            });

            for (StudentRecord student : getSortedStudents()) {
                boolean matchesSearch = search.isEmpty()
                    || normalize(student.fullName).contains(search)
                    || normalize(student.email).contains(search)
                    || normalize(student.course).contains(search);
                boolean matchesFilter = "All".equals(filter) || filter.equals(student.course);

                if (matchesSearch && matchesFilter) {
                    manageStudentView.add(student);
                    managePeopleModel.addRow(new Object[] {
                        student.id,
                        student.fullName,
                        student.course,
                        student.email,
                        student.contact
                    });
                }
            }
        }
    }

    private void refreshManageSessions() {
        manageSessionView.clear();
        adminSessionModel.setRowCount(0);

        String search = normalize(adminSessionSearchField.getText());
        String filter = String.valueOf(adminSessionStatusBox.getSelectedItem());

        for (SessionRecord session : sortSessionsByDate(data.sessions, true)) {
            boolean matchesSearch = search.isEmpty()
                || normalize(session.studentName).contains(search)
                || normalize(session.counselorName).contains(search)
                || normalize(session.course).contains(search)
                || normalize(session.topic).contains(search);
            boolean matchesFilter = "All Status".equals(filter) || filter.equals(session.status);

            if (matchesSearch && matchesFilter) {
                manageSessionView.add(session);
                adminSessionModel.addRow(new Object[] {
                    session.id,
                    session.counselorName,
                    session.studentName,
                    displayDate(session.dateIso),
                    session.timeSlot,
                    session.status
                });
            }
        }

        adminSessionsBadgeLabel.setText("  " + manageSessionView.size() + " Sessions  ");
        selectSessionInTable(adminSessionTable, manageSessionView, selectedSession);
    }

    private void submitBooking() {
        if (selectedCounselorAccount == null) {
            showError("Select a counselor first.");
            navigateTo(CARD_COUNSELOR_SELECT);
            return;
        }

        String fullName = bookingNameField.getText().trim();
        String course = bookingCourseField.getText().trim();
        String email = bookingEmailField.getText().trim();
        String contact = bookingContactField.getText().trim();
        String topic = bookingTopicArea.getText().trim();
        DateOption dateOption = (DateOption) bookingDateBox.getSelectedItem();
        String timeSlot = (String) bookingTimeBox.getSelectedItem();

        if (fullName.isEmpty() || course.isEmpty() || email.isEmpty() || contact.isEmpty()
            || topic.isEmpty() || dateOption == null || timeSlot == null) {
            showError("Fill in all booking fields.");
            return;
        }

        if (!selectedCounselorAccount.active) {
            showError("That counselor is no longer active.");
            navigateTo(CARD_COUNSELOR_SELECT);
            return;
        }

        if (hasScheduleConflict(selectedCounselorAccount.username, dateOption.value, timeSlot, -1)) {
            showError("That schedule is already booked. Pick another date or time.");
            return;
        }

        StudentRecord student = findStudentByEmail(email);
        if (student == null) {
            student = new StudentRecord();
            student.id = data.nextStudentId++;
            data.students.add(student);
        }

        student.fullName = fullName;
        student.course = course;
        student.email = email;
        student.contact = contact;

        SessionRecord session = new SessionRecord();
        session.id = data.nextSessionId++;
        session.counselorUsername = selectedCounselorAccount.username;
        session.counselorName = selectedCounselorAccount.fullName;
        session.studentEmail = student.email;
        session.studentName = student.fullName;
        session.course = student.course;
        session.dateIso = dateOption.value;
        session.timeSlot = timeSlot;
        session.topic = topic;
        session.notes = "";
        session.recommendations = "";
        session.cancellationReason = "";
        session.status = STATUS_PENDING;

        data.sessions.add(session);
        selectedSession = session;
        saveData();

        lastBookingSummary =
            "Session booked with " + selectedCounselorAccount.fullName + " on "
                + displayDate(session.dateIso) + " at " + session.timeSlot + ".";

        clearBookingForm();
        navigateTo(CARD_THANK_YOU);
    }

    private void submitCounselorLogin() {
        String username = counselorLoginUsernameField.getText().trim();
        String password = new String(counselorLoginPasswordField.getPassword());

        CounselorAccount counselor = findCounselorByUsername(username);
        if (counselor == null || !counselor.password.equals(password)) {
            setMessage(counselorLoginMessageLabel, "Invalid counselor credentials.", C_RED);
            return;
        }

        if (!counselor.active) {
            setMessage(counselorLoginMessageLabel, "This counselor account is inactive.", C_RED);
            return;
        }

        currentCounselor = counselor;
        selectedSession = firstActiveSessionForCounselor(counselor.username);
        counselorLoginUsernameField.setText("");
        counselorLoginPasswordField.setText("");
        setMessage(counselorLoginMessageLabel, "", C_TEXT3);
        navigateTo(CARD_SESSION_ALERT);
    }

    private void submitCounselorSignup() {
        String fullName = counselorSignupNameField.getText().trim();
        String specialization = counselorSignupSpecializationField.getText().trim();
        String email = counselorSignupEmailField.getText().trim();
        String contact = counselorSignupContactField.getText().trim();
        String username = counselorSignupUsernameField.getText().trim();
        String password = new String(counselorSignupPasswordField.getPassword());
        String confirmPassword = new String(counselorSignupConfirmField.getPassword());

        if (fullName.isEmpty() || specialization.isEmpty() || email.isEmpty()
            || contact.isEmpty() || username.isEmpty() || password.isEmpty()) {
            setMessage(counselorSignupMessageLabel, "Fill in all signup fields.", C_RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            setMessage(counselorSignupMessageLabel, "Passwords do not match.", C_RED);
            return;
        }

        if (findCounselorByUsername(username) != null) {
            setMessage(counselorSignupMessageLabel, "Username already exists.", C_RED);
            return;
        }

        if (findCounselorByEmail(email) != null) {
            setMessage(counselorSignupMessageLabel, "Email is already used by another counselor.", C_RED);
            return;
        }

        CounselorAccount counselor = new CounselorAccount();
        counselor.id = data.nextCounselorId++;
        counselor.fullName = fullName;
        counselor.specialization = specialization;
        counselor.email = email;
        counselor.contact = contact;
        counselor.username = username;
        counselor.password = password;
        counselor.active = true;
        data.counselors.add(counselor);

        saveData();
        clearCounselorSignupForm();
        counselorLoginUsernameField.setText(username);
        setMessage(counselorLoginMessageLabel, "Account created. You can sign in now.", C_ACCENT);
        navigateTo(CARD_COUNSELOR_LOGIN);
    }

    private void submitAdminLogin() {
        String username = adminLoginUsernameField.getText().trim();
        String password = new String(adminLoginPasswordField.getPassword());

        AdminAccount admin = findAdminByUsername(username);
        if (admin == null || !admin.password.equals(password)) {
            setMessage(adminLoginMessageLabel, "Invalid admin credentials.", C_RED);
            return;
        }

        currentAdmin = admin;
        adminLoginUsernameField.setText("");
        adminLoginPasswordField.setText("");
        setMessage(adminLoginMessageLabel, "", C_TEXT3);
        navigateTo(CARD_ADMIN_MENU);
    }

    private void submitAdminSignup() {
        String fullName = adminSignupNameField.getText().trim();
        String username = adminSignupUsernameField.getText().trim();
        String password = new String(adminSignupPasswordField.getPassword());
        String confirm = new String(adminSignupConfirmField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            setMessage(adminSignupMessageLabel, "Fill in all signup fields.", C_RED);
            return;
        }

        if (!password.equals(confirm)) {
            setMessage(adminSignupMessageLabel, "Passwords do not match.", C_RED);
            return;
        }

        if (findAdminByUsername(username) != null) {
            setMessage(adminSignupMessageLabel, "Admin username already exists.", C_RED);
            return;
        }

        AdminAccount admin = new AdminAccount();
        admin.id = data.nextAdminId++;
        admin.fullName = fullName;
        admin.username = username;
        admin.password = password;
        data.admins.add(admin);

        saveData();
        clearAdminSignupForm();
        adminLoginUsernameField.setText(username);
        setMessage(adminLoginMessageLabel, "Admin account created. You can sign in now.", C_ACCENT);
        navigateTo(CARD_ADMIN_LOGIN);
    }

    private void saveSelectedSessionDraft(boolean markInProgress) {
        if (!ensureSelectedSession()) {
            return;
        }

        selectedSession.topic = workspaceTopicArea.getText().trim();
        selectedSession.notes = workspaceNotesArea.getText().trim();
        selectedSession.recommendations = workspaceRecommendationArea.getText().trim();

        if (markInProgress && !STATUS_FINISHED.equals(selectedSession.status)
            && !STATUS_CANCELLED.equals(selectedSession.status)) {
            selectedSession.status = STATUS_IN_PROGRESS;
        }

        saveData();
        refreshWorkspace();
        refreshCounselorAlert();
        refreshCounselorSchedule();
        refreshManageSessions();

        showInfo("Session draft saved.");
    }

    private void finishSelectedSession() {
        if (!ensureSelectedSession()) {
            return;
        }

        saveSelectedSessionDraft(true);
        selectedSession.status = STATUS_FINISHED;
        saveData();

        refreshWorkspace();
        refreshCounselorAlert();
        refreshCounselorSchedule();
        refreshManageSessions();
        navigateTo(CARD_SCHEDULE);
    }

    private void cancelSelectedSession() {
        if (!ensureSelectedSession()) {
            return;
        }

        String reason = cancellationReasonArea.getText().trim();
        if (reason.isEmpty()) {
            showError("Enter a cancellation reason.");
            return;
        }

        selectedSession.cancellationReason = reason;
        selectedSession.status = STATUS_CANCELLED;
        saveData();

        refreshCounselorAlert();
        refreshCounselorSchedule();
        refreshManageSessions();
        navigateTo(CARD_SCHEDULE);
    }

    private void logoutCounselor() {
        currentCounselor = null;
        selectedSession = null;
        navigateTo(CARD_COUNSELOR_LOGIN);
    }

    private void logoutAdmin() {
        currentAdmin = null;
        navigateTo(CARD_ADMIN_LOGIN);
    }

    private void openPeopleEditor() {
        if ("counselors".equals(managePeopleMode)) {
            openCounselorDialog(null);
        } else {
            openStudentDialog(null);
        }
    }

    private void editSelectedPerson() {
        int row = managePeopleTable.getSelectedRow();
        if (row < 0) {
            showError("Select a row first.");
            return;
        }

        if ("counselors".equals(managePeopleMode)) {
            if (row < manageCounselorView.size()) {
                openCounselorDialog(manageCounselorView.get(row));
            }
        } else if (row < manageStudentView.size()) {
            openStudentDialog(manageStudentView.get(row));
        }
    }

    private void deleteSelectedPerson() {
        int row = managePeopleTable.getSelectedRow();
        if (row < 0) {
            showError("Select a row first.");
            return;
        }

        if ("counselors".equals(managePeopleMode)) {
            CounselorAccount counselor = manageCounselorView.get(row);
            if (hasSessionsForCounselor(counselor.username)) {
                showError("Cannot delete a counselor with session history. Set the account inactive instead.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete counselor " + counselor.fullName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                data.counselors.remove(counselor);
                if (currentCounselor == counselor) {
                    currentCounselor = null;
                    selectedSession = null;
                }
                saveData();
                refreshManagePeople();
                refreshCounselorSelection();
            }
        } else {
            StudentRecord student = manageStudentView.get(row);
            if (hasSessionsForStudent(student.email)) {
                showError("Cannot delete a student with session history.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete student " + student.fullName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                data.students.remove(student);
                saveData();
                refreshManagePeople();
            }
        }
    }

    private void showSelectedSessionDetails() {
        if (!ensureSelectedSession()) {
            return;
        }

        String message =
            "ID: " + selectedSession.id + "\n"
            + "Counselor: " + selectedSession.counselorName + "\n"
            + "Student: " + selectedSession.studentName + "\n"
            + "Course: " + selectedSession.course + "\n"
            + "Date: " + displayDate(selectedSession.dateIso) + "\n"
            + "Time: " + selectedSession.timeSlot + "\n"
            + "Status: " + selectedSession.status + "\n"
            + "Topic: " + selectedSession.topic + "\n"
            + "Notes: " + selectedSession.notes + "\n"
            + "Recommendations: " + selectedSession.recommendations + "\n"
            + "Cancellation Reason: " + selectedSession.cancellationReason;

        JOptionPane.showMessageDialog(this, message, "Session Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void adminUpdateSelectedSessionStatus(String newStatus) {
        if (!ensureSelectedSession()) {
            return;
        }

        if (STATUS_CANCELLED.equals(selectedSession.status) && !STATUS_CANCELLED.equals(newStatus)) {
            selectedSession.cancellationReason = "";
        }

        selectedSession.status = newStatus;
        saveData();
        refreshManageSessions();
        refreshCounselorAlert();
        refreshCounselorSchedule();
        refreshWorkspace();
    }

    private void adminCancelSelectedSession() {
        if (!ensureSelectedSession()) {
            return;
        }

        String reason = JOptionPane.showInputDialog(
            this,
            "Cancellation reason:",
            selectedSession.cancellationReason == null ? "" : selectedSession.cancellationReason);

        if (reason == null) {
            return;
        }

        reason = reason.trim();
        if (reason.isEmpty()) {
            showError("Cancellation reason is required.");
            return;
        }

        selectedSession.cancellationReason = reason;
        selectedSession.status = STATUS_CANCELLED;
        saveData();
        refreshManageSessions();
        refreshCounselorAlert();
        refreshCounselorSchedule();
        refreshWorkspace();
    }

    private void openCounselorDialog(CounselorAccount counselor) {
        JTextField nameField = new JTextField(counselor == null ? "" : counselor.fullName);
        JTextField specializationField = new JTextField(counselor == null ? "" : counselor.specialization);
        JTextField emailField = new JTextField(counselor == null ? "" : counselor.email);
        JTextField contactField = new JTextField(counselor == null ? "" : counselor.contact);
        JTextField usernameField = new JTextField(counselor == null ? "" : counselor.username);
        JPasswordField passwordField = new JPasswordField();
        JCheckBox activeBox = new JCheckBox("Active", counselor == null || counselor.active);
        activeBox.setBackground(C_WHITE);

        Object[] content = {
            "Full Name", nameField,
            "Specialization", specializationField,
            "Email", emailField,
            "Contact", contactField,
            "Username", usernameField,
            counselor == null ? "Password" : "New Password (optional)", passwordField,
            activeBox
        };

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                this,
                content,
                counselor == null ? "Add Counselor" : "Edit Counselor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String fullName = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (fullName.isEmpty() || specialization.isEmpty() || email.isEmpty()
                || contact.isEmpty() || username.isEmpty()) {
                showError("Fill in all required counselor fields.");
                continue;
            }

            CounselorAccount existingUsername = findCounselorByUsername(username);
            if (existingUsername != null && existingUsername != counselor) {
                showError("Counselor username already exists.");
                continue;
            }

            CounselorAccount existingEmail = findCounselorByEmail(email);
            if (existingEmail != null && existingEmail != counselor) {
                showError("Counselor email already exists.");
                continue;
            }

            if (counselor == null && password.isEmpty()) {
                showError("Password is required for a new counselor.");
                continue;
            }

            String oldUsername = counselor == null ? null : counselor.username;

            if (counselor == null) {
                counselor = new CounselorAccount();
                counselor.id = data.nextCounselorId++;
                data.counselors.add(counselor);
            }

            counselor.fullName = fullName;
            counselor.specialization = specialization;
            counselor.email = email;
            counselor.contact = contact;
            counselor.username = username;
            counselor.active = activeBox.isSelected();
            if (!password.isEmpty()) {
                counselor.password = password;
            }

            for (SessionRecord session : data.sessions) {
                if (oldUsername != null && session.counselorUsername.equals(oldUsername)) {
                    session.counselorUsername = counselor.username;
                    session.counselorName = counselor.fullName;
                } else if (session.counselorUsername.equals(counselor.username)) {
                    session.counselorName = counselor.fullName;
                }
            }

            if (currentCounselor != null && oldUsername != null && currentCounselor.username.equals(oldUsername)) {
                currentCounselor = counselor;
            }

            saveData();
            refreshManagePeople();
            refreshCounselorSelection();
            refreshManageSessions();
            refreshAdminMenu();
            return;
        }
    }

    private void openStudentDialog(StudentRecord student) {
        JTextField nameField = new JTextField(student == null ? "" : student.fullName);
        JTextField courseField = new JTextField(student == null ? "" : student.course);
        JTextField emailField = new JTextField(student == null ? "" : student.email);
        JTextField contactField = new JTextField(student == null ? "" : student.contact);

        Object[] content = {
            "Full Name", nameField,
            "Course", courseField,
            "Email", emailField,
            "Contact", contactField
        };

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                this,
                content,
                student == null ? "Add Student" : "Edit Student",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String fullName = nameField.getText().trim();
            String course = courseField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();

            if (fullName.isEmpty() || course.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                showError("Fill in all required student fields.");
                continue;
            }

            StudentRecord existing = findStudentByEmail(email);
            if (existing != null && existing != student) {
                showError("Student email already exists.");
                continue;
            }

            String oldEmail = student == null ? null : student.email;

            if (student == null) {
                student = new StudentRecord();
                student.id = data.nextStudentId++;
                data.students.add(student);
            }

            student.fullName = fullName;
            student.course = course;
            student.email = email;
            student.contact = contact;

            for (SessionRecord session : data.sessions) {
                if (oldEmail != null && session.studentEmail.equals(oldEmail)) {
                    session.studentEmail = student.email;
                    session.studentName = student.fullName;
                    session.course = student.course;
                } else if (session.studentEmail.equals(student.email)) {
                    session.studentName = student.fullName;
                    session.course = student.course;
                }
            }

            saveData();
            refreshManagePeople();
            refreshManageSessions();
            refreshCounselorSchedule();
            refreshCounselorAlert();
            refreshWorkspace();
            refreshAdminMenu();
            return;
        }
    }

    private JPanel counselorCard(CounselorAccount counselor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(C_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER, 12),
            BorderFactory.createEmptyBorder(24, 20, 20, 20)));

        JPanel avatarWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        avatarWrap.setOpaque(false);
        avatarWrap.add(avatarCircle(initials(counselor.fullName), C_ACCENT, C_ACCENT_LT, 60));
        avatarWrap.setAlignmentX(0.5f);
        card.add(avatarWrap);
        card.add(vspace(14));

        JLabel nameLabel = new JLabel(counselor.fullName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        nameLabel.setForeground(C_TEXT);
        nameLabel.setAlignmentX(0.5f);
        card.add(nameLabel);
        card.add(vspace(3));

        JLabel roleLabel = new JLabel(counselor.specialization, SwingConstants.CENTER);
        roleLabel.setFont(F_SMALL);
        roleLabel.setForeground(C_TEXT3);
        roleLabel.setAlignmentX(0.5f);
        card.add(roleLabel);
        card.add(vspace(10));

        JLabel statusLabel = new JLabel("Active", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(C_ACCENT2);
        statusLabel.setAlignmentX(0.5f);
        card.add(statusLabel);
        card.add(vspace(16));

        JButton bookButton = primaryButton("Book Session");
        bookButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        bookButton.setAlignmentX(0.5f);
        bookButton.addActionListener(e -> {
            selectedCounselorAccount = counselor;
            navigateTo(CARD_BOOKING_FORM);
        });
        card.add(bookButton);

        return card;
    }

    private JPanel menuCard(String title, String desc, Runnable action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(C_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER, 12),
            BorderFactory.createEmptyBorder(24, 20, 20, 20)));

        JLabel titleLabel = centered(title, new Font("Segoe UI Semibold", Font.PLAIN, 15), C_TEXT);
        titleLabel.setAlignmentX(0.5f);
        panel.add(titleLabel);
        panel.add(vspace(10));

        JLabel descLabel = new JLabel("<html><center>" + desc + "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(F_SMALL);
        descLabel.setForeground(C_TEXT3);
        descLabel.setAlignmentX(0.5f);
        panel.add(descLabel);
        panel.add(vspace(18));

        JButton button = primaryButton("Open");
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        button.setAlignmentX(0.5f);
        button.addActionListener(e -> action.run());
        panel.add(button);

        return panel;
    }

    private JPanel statCard(String label, Color bg, Color fg, boolean first) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(fg.brighter(), 10),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JLabel valueLabel = new JLabel("0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 28));
        valueLabel.setForeground(fg);
        valueLabel.setAlignmentX(0.5f);
        panel.add(valueLabel);
        panel.add(vspace(6));

        JLabel labelLabel = centered(label, F_SMALL, fg);
        labelLabel.setAlignmentX(0.5f);
        panel.add(labelLabel);

        if (first) {
            statCounselorsValueLabel = valueLabel;
        } else if ("Students".equals(label)) {
            statStudentsValueLabel = valueLabel;
        } else if ("Today".equals(label)) {
            statTodayValueLabel = valueLabel;
        } else if ("Pending".equals(label)) {
            statPendingValueLabel = valueLabel;
        }

        return panel;
    }

    private JPanel pageRoot() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(C_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));
        return panel;
    }

    private JPanel wrap(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(C_BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(C_BG);
        outer.add(scrollPane, BorderLayout.CENTER);
        return outer;
    }

    private JPanel centerWrap(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    private JLabel pageTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(F_TITLE);
        label.setForeground(C_TEXT);
        label.setAlignmentX(0f);
        return label;
    }

    private JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(F_BODY);
        label.setForeground(C_TEXT3);
        label.setAlignmentX(0f);
        return label;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(C_TEXT3);
        label.setAlignmentX(0f);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(F_LABEL);
        label.setForeground(C_TEXT2);
        return label;
    }

    private JLabel centered(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(0.5f);
        return label;
    }

    private JLabel badgeLabel(String text, Color bg, Color fg) {
        JLabel label = new JLabel("  " + text + "  ");
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);
        label.setBackground(bg);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(fg.brighter(), 6),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return label;
    }

    private JLabel messageLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(F_SMALL);
        label.setForeground(C_TEXT3);
        label.setAlignmentX(0.5f);
        return label;
    }

    private JPanel formCard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(C_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER, 12),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        return panel;
    }

    private JPanel centerCard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(C_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER, 14),
            BorderFactory.createEmptyBorder(32, 32, 32, 32)));
        panel.setAlignmentX(0.5f);
        return panel;
    }

    private JPanel formRow(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        panel.add(fieldLabel(labelText), BorderLayout.NORTH);
        styleField(field);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel hbox() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(0f);
        return panel;
    }

    private Box.Filler vspace(int height) {
        return (Box.Filler) Box.createRigidArea(new Dimension(0, height));
    }

    private Box.Filler hspace(int width) {
        return (Box.Filler) Box.createRigidArea(new Dimension(width, 0));
    }

    private JPanel avatarCircle(String initials, Color fg, Color bg, int size) {
        return new JPanel() {
            private static final long serialVersionUID = 1L;
            {
                setOpaque(false);
                setPreferredSize(new Dimension(size, size));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, size, size);
                g2.setColor(fg);
                g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, size / 3));
                FontMetrics metrics = g2.getFontMetrics();
                int x = (size - metrics.stringWidth(initials)) / 2;
                int y = (size + metrics.getAscent() - metrics.getDescent()) / 2;
                g2.drawString(initials, x, y);
            }
        };
    }

    private String initials(String name) {
        String[] parts = name.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                builder.append(part.charAt(0));
            }
        }
        return builder.toString().toUpperCase(Locale.ENGLISH);
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        button.setBackground(C_ACCENT);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(C_ACCENT2);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(C_ACCENT);
            }
        });
        return button;
    }

    private JButton outlineButton(String text) {
        JButton button = new JButton(text);
        button.setFont(F_SUB);
        button.setBackground(C_WHITE);
        button.setForeground(C_TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER2, 8),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton dangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(F_SUB);
        button.setBackground(C_RED_LT);
        button.setForeground(C_RED);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_RED, 8),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleField(JComponent component) {
        component.setFont(F_SUB);
        component.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER, 8),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)));

        if (component instanceof JTextField || component instanceof JPasswordField) {
            component.setPreferredSize(new Dimension(220, 36));
        }
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(F_SUB);
        textArea.setBorder(fieldBorder());
    }

    private Border fieldBorder() {
        return BorderFactory.createCompoundBorder(
            new RoundedBorder(C_BORDER, 8),
            BorderFactory.createEmptyBorder(6, 8, 6, 8));
    }

    private void styleCombo(JComboBox<?> comboBox) {
        comboBox.setFont(F_SUB);
        comboBox.setBackground(C_WHITE);
        comboBox.setPreferredSize(new Dimension(160, 34));
    }

    private JScrollPane wrapTable(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new RoundedBorder(C_BORDER, 10));
        scroll.getViewport().setBackground(C_WHITE);
        return scroll;
    }

    private DefaultTableModel nonEditableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void styleTable(JTable table) {
        table.setFont(F_BODY);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setBackground(C_BG2);
        table.getTableHeader().setForeground(C_TEXT2);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER));
        table.setSelectionBackground(C_ACCENT_LT);
        table.setSelectionForeground(C_TEXT);
        table.setBorder(null);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focused,
                int row,
                int column) {
                super.getTableCellRendererComponent(table, value, selected, focused, row, column);
                applyTableCellStyle(this, table, selected, row);
                setForeground(selected ? table.getSelectionForeground() : C_TEXT);
                return this;
            }
        });
    }

    private void applyTableCellStyle(DefaultTableCellRenderer renderer, JTable table, boolean selected, int row) {
        renderer.setOpaque(true);
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        if (selected) {
            renderer.setBackground(table.getSelectionBackground());
        } else {
            renderer.setBackground(row % 2 == 0 ? C_WHITE : new Color(0xF9F8F5));
        }
    }

    private void installStatusColumn(JTable table, int columnIndex) {
        if (columnIndex < 0 || columnIndex >= table.getColumnModel().getColumnCount()) {
            return;
        }

        table.getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focused,
                int row,
                int column) {
                super.getTableCellRendererComponent(table, value, selected, focused, row, column);
                applyTableCellStyle(this, table, selected, row);
                setFont(new Font("Segoe UI", Font.BOLD, 11));
                if (selected) {
                    setForeground(table.getSelectionForeground());
                } else {
                    setForeground(statusColor(String.valueOf(value)));
                }
                return this;
            }
        });
    }

    private Color statusColor(String status) {
        if (STATUS_FINISHED.equals(status) || "Active".equals(status)) {
            return C_ACCENT;
        }
        if (STATUS_IN_PROGRESS.equals(status)) {
            return C_AMBER;
        }
        if (STATUS_CANCELLED.equals(status) || "Inactive".equals(status)) {
            return C_RED;
        }
        return C_BLUE;
    }

    private Color statusBackground(String status) {
        if (STATUS_FINISHED.equals(status) || "Active".equals(status)) {
            return C_ACCENT_LT;
        }
        if (STATUS_IN_PROGRESS.equals(status)) {
            return C_AMBER_LT;
        }
        if (STATUS_CANCELLED.equals(status) || "Inactive".equals(status)) {
            return C_RED_LT;
        }
        return C_BLUE_LT;
    }

    private void startClock() {
        Timer timer = new Timer(1000, e ->
            clockLabel.setText(java.time.LocalTime.now().format(
                DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH))));
        timer.setInitialDelay(0);
        timer.start();
    }

    private AppData loadData() {
        if (!dataFile.exists()) {
            return new AppData();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            Object loaded = in.readObject();
            if (loaded instanceof AppData) {
                return (AppData) loaded;
            }
        } catch (Exception ignored) {
        }

        return new AppData();
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to save local data file.\n" + ex.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<CounselorAccount> getActiveCounselors() {
        List<CounselorAccount> result = new ArrayList<>();
        for (CounselorAccount counselor : getSortedCounselors()) {
            if (counselor.active) {
                result.add(counselor);
            }
        }
        return result;
    }

    private List<CounselorAccount> getSortedCounselors() {
        List<CounselorAccount> result = new ArrayList<>(data.counselors);
        result.sort(Comparator.comparing(c -> normalize(c.fullName)));
        return result;
    }

    private List<StudentRecord> getSortedStudents() {
        List<StudentRecord> result = new ArrayList<>(data.students);
        result.sort(Comparator.comparing(s -> normalize(s.fullName)));
        return result;
    }

    private List<SessionRecord> getCounselorSessions(String counselorUsername) {
        List<SessionRecord> result = new ArrayList<>();
        for (SessionRecord session : data.sessions) {
            if (session.counselorUsername.equals(counselorUsername)) {
                result.add(session);
            }
        }
        return result;
    }

    private CounselorAccount findCounselorByUsername(String username) {
        for (CounselorAccount counselor : data.counselors) {
            if (normalize(counselor.username).equals(normalize(username))) {
                return counselor;
            }
        }
        return null;
    }

    private CounselorAccount findCounselorByEmail(String email) {
        for (CounselorAccount counselor : data.counselors) {
            if (normalize(counselor.email).equals(normalize(email))) {
                return counselor;
            }
        }
        return null;
    }

    private AdminAccount findAdminByUsername(String username) {
        for (AdminAccount admin : data.admins) {
            if (normalize(admin.username).equals(normalize(username))) {
                return admin;
            }
        }
        return null;
    }

    private StudentRecord findStudentByEmail(String email) {
        for (StudentRecord student : data.students) {
            if (normalize(student.email).equals(normalize(email))) {
                return student;
            }
        }
        return null;
    }

    private boolean hasScheduleConflict(String counselorUsername, String dateIso, String timeSlot, int ignoreSessionId) {
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

    private boolean hasSessionsForCounselor(String username) {
        for (SessionRecord session : data.sessions) {
            if (session.counselorUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSessionsForStudent(String email) {
        for (SessionRecord session : data.sessions) {
            if (session.studentEmail.equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private CounselorAccount counselorByUsername(String username) {
        return findCounselorByUsername(username);
    }

    private SessionRecord firstActiveSessionForCounselor(String username) {
        for (SessionRecord session : sortSessionsByDate(getCounselorSessions(username), true)) {
            if (!STATUS_FINISHED.equals(session.status) && !STATUS_CANCELLED.equals(session.status)) {
                return session;
            }
        }
        return null;
    }

    private List<SessionRecord> sortSessionsByDate(List<SessionRecord> sessions, boolean ascending) {
        List<SessionRecord> result = new ArrayList<>(sessions);
        Comparator<SessionRecord> comparator = Comparator
            .comparing((SessionRecord session) -> session.dateIso)
            .thenComparingInt(session -> timeSlotIndex(session.timeSlot))
            .thenComparingInt(session -> session.id);

        result.sort(ascending ? comparator : comparator.reversed());
        return result;
    }

    private int timeSlotIndex(String timeSlot) {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(timeSlot)) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    private int countActiveCounselors() {
        int count = 0;
        for (CounselorAccount counselor : data.counselors) {
            if (counselor.active) {
                count++;
            }
        }
        return count;
    }

    private int countTodaySessions() {
        String today = LocalDate.now().format(storageDateFormatter);
        int count = 0;
        for (SessionRecord session : data.sessions) {
            if (today.equals(session.dateIso)) {
                count++;
            }
        }
        return count;
    }

    private int countPendingSessions() {
        int count = 0;
        for (SessionRecord session : data.sessions) {
            if (STATUS_PENDING.equals(session.status)) {
                count++;
            }
        }
        return count;
    }

    private String displayDate(String isoDate) {
        return LocalDate.parse(isoDate, storageDateFormatter).format(displayDateFormatter);
    }

    private String displayDate(LocalDate date) {
        return date.format(displayDateFormatter);
    }

    private void setMessage(JLabel label, String text, Color color) {
        label.setText(text == null || text.isEmpty() ? " " : text);
        label.setForeground(color);
    }

    private void clearBookingForm() {
        bookingNameField.setText("");
        bookingCourseField.setText("");
        bookingEmailField.setText("");
        bookingContactField.setText("");
        bookingTopicArea.setText("");
        if (bookingDateBox.getItemCount() > 0) {
            bookingDateBox.setSelectedIndex(0);
        }
        bookingTimeBox.setSelectedIndex(0);
    }

    private void clearCounselorSignupForm() {
        counselorSignupNameField.setText("");
        counselorSignupSpecializationField.setText("");
        counselorSignupEmailField.setText("");
        counselorSignupContactField.setText("");
        counselorSignupUsernameField.setText("");
        counselorSignupPasswordField.setText("");
        counselorSignupConfirmField.setText("");
        setMessage(counselorSignupMessageLabel, "", C_TEXT3);
    }

    private void clearAdminSignupForm() {
        adminSignupNameField.setText("");
        adminSignupUsernameField.setText("");
        adminSignupPasswordField.setText("");
        adminSignupConfirmField.setText("");
        setMessage(adminSignupMessageLabel, "", C_TEXT3);
    }

    private void repopulatePeopleFilter(boolean counselorsMode) {
        Object currentSelection = managePeopleFilterBox.getSelectedItem();
        managePeopleFilterBox.removeAllItems();

        if (counselorsMode) {
            managePeopleFilterBox.addItem("All");
            managePeopleFilterBox.addItem("Active");
            managePeopleFilterBox.addItem("Inactive");
        } else {
            managePeopleFilterBox.addItem("All");
            for (StudentRecord student : getSortedStudents()) {
                boolean exists = false;
                for (int i = 0; i < managePeopleFilterBox.getItemCount(); i++) {
                    if (student.course.equals(managePeopleFilterBox.getItemAt(i))) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    managePeopleFilterBox.addItem(student.course);
                }
            }
        }

        if (currentSelection != null) {
            managePeopleFilterBox.setSelectedItem(currentSelection);
        }

        if (managePeopleFilterBox.getSelectedIndex() < 0 && managePeopleFilterBox.getItemCount() > 0) {
            managePeopleFilterBox.setSelectedIndex(0);
        }
    }

    private void selectSessionInTable(JTable table, List<SessionRecord> source, SessionRecord preferred) {
        if (source.isEmpty()) {
            selectedSession = null;
            return;
        }

        int selectedIndex = 0;
        if (preferred != null) {
            for (int i = 0; i < source.size(); i++) {
                if (source.get(i).id == preferred.id) {
                    selectedIndex = i;
                    break;
                }
            }
        }

        selectedSession = source.get(selectedIndex);
        if (table.getRowCount() > selectedIndex) {
            table.setRowSelectionInterval(selectedIndex, selectedIndex);
        }
    }

    private boolean ensureSelectedSession() {
        if (selectedSession == null) {
            showError("Select a session first.");
            return false;
        }
        return true;
    }

    private void installRefreshOnTyping(JTextField field, Runnable action) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                action.run();
            }
        });
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase(Locale.ENGLISH);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GSOCounselingSystem::new);
    }

    private static class DateOption {
        private final String value;
        private final String label;

        DateOption(String value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static class AppData implements Serializable {
        private static final long serialVersionUID = 1L;

        private final List<AdminAccount> admins = new ArrayList<>();
        private final List<CounselorAccount> counselors = new ArrayList<>();
        private final List<StudentRecord> students = new ArrayList<>();
        private final List<SessionRecord> sessions = new ArrayList<>();
        private int nextAdminId = 1;
        private int nextCounselorId = 1;
        private int nextStudentId = 1;
        private int nextSessionId = 1;
    }

    private static class AdminAccount implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String fullName = "";
        private String username = "";
        private String password = "";
    }

    private static class CounselorAccount implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String fullName = "";
        private String specialization = "";
        private String email = "";
        private String contact = "";
        private String username = "";
        private String password = "";
        private boolean active = true;
    }

    private static class StudentRecord implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String fullName = "";
        private String course = "";
        private String email = "";
        private String contact = "";
    }

    private static class SessionRecord implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String counselorUsername = "";
        private String counselorName = "";
        private String studentEmail = "";
        private String studentName = "";
        private String course = "";
        private String dateIso = "";
        private String timeSlot = "";
        private String topic = "";
        private String notes = "";
        private String recommendations = "";
        private String cancellationReason = "";
        private String status = STATUS_PENDING;
    }

    static class RoundedBorder implements Border {
        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }
}
