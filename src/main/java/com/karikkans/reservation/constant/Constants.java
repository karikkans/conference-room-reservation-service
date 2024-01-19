package com.karikkans.reservation.constant;

public interface Constants {

    String RESERVATION_SUCCESS_MESSAGE = "Reserved {0} for {1} participant(s) from {2} to {3}";
    String TIME_QUARTER_ERROR_MESSAGE = "Meeting times should be given in quarters";
    String TIME_INTERVAL_ERROR_MESSAGE = "Meeting duration is not in intervals of {0} minutes";
    String MINIMUM_START_TIME_ERROR_MESSAGE = "Requested start time is before the allowed start time {0}";
    String MAXIMUM_START_TIME_ERROR_MESSAGE = "Requested end time after the allowed end time {0}";
    String TIME_RANGE_ERROR_MESSAGE = "Start time is greater than end time";
    String MATCHING_TIME_RANGE_ERROR_MESSAGE = "Start and end time are same. Choose a valid interval";
    String MAINTENANCE_SCHEDULE_CONFLICT_ERROR_MESSAGE = "Requested duration is conflicting with {0} maintenance schedule ({1} - {2})";
    String NO_ROOMS_AVAILABLE_ERROR_MESSAGE = "No rooms are available for the requested time slot ({0} - {1})";
    String RESERVATION_FAILED_ERROR_MESSAGE = "Reservation failed";
}
