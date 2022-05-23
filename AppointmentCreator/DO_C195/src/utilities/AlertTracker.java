package utilities;

/**
 * This class lets the other controllers know if the 15 minute appointment alert has been triggered. This is to prevent
 * the alert from popping up every time the Home Page is opened.
 */
public class AlertTracker {

    // Status to let the other controllers know if the alerts been prompted
    public static AppointmentAlert appointmentAlert;

    /**
     * Retrieves the status of the appointment alert
     * @return <code>ENUM</code> value "FirstAlert" or "AlreadyAlerted"
     */
    public static AppointmentAlert getAppointmentAlert() {
        return appointmentAlert;
    }

    /**
     * Sets the status of the appointment alert
     * @param appointmentAlert the <code>ENUM - AppointmentAlert</code> value "FirstAlert" or "AlreadyAlerted"
     */
    public static void setAppointmentAlert(AppointmentAlert appointmentAlert) {
        AlertTracker.appointmentAlert = appointmentAlert;
    }
}
