package org.cinema;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class BookingService {

    static final int TOTAL_ROWS = 100;
    static final int TOTAL_SEATS = 50;
    static final int MAX_ACCEPT_BOOKING = 5;
    static int REJECTED_REQUESTS = 0;

    public static void main(String[] args) {

        Path path = Paths.get("src/main/resources/sample_booking_requests");

        try {
            Object[] rawRequests = Files.readAllLines(path).toArray();
            Iterable<Request> parsedRequests = Request.Parse(rawRequests);
            List<Integer> rejectedIds = new ArrayList<>();

            boolean[][] rowSeatArr = new boolean[TOTAL_ROWS][TOTAL_SEATS];

            for (Request request : parsedRequests) {
                if (IsRequestValid(request, rowSeatArr)) {
                    bookSeats(request, rowSeatArr);
                } else {
                    rejectedIds.add(request.Id);
                    ++REJECTED_REQUESTS;
                }
            }
            System.out.printf("Number of rejected requests: %S\n", REJECTED_REQUESTS);
            System.out.println("Rejected ids : " + rejectedIds);

        } catch (Exception e) {
            System.out.println("something went wrong... :(");
        }
    }

    static void bookSeats(Request request, boolean[][] rowSeatArr) {
        for (int i = request.firstSeat; i <= request.lastSeat; i++) {
            rowSeatArr[request.firstRow][i] = true;
        }
    }

    static boolean IsRequestValid(Request request, boolean[][] rowSeatArr) {

        int numberOfSeats = Math.abs(request.lastSeat - request.firstSeat) + 1;
        if (numberOfSeats > MAX_ACCEPT_BOOKING) {
            return false;
        }

        if (request.firstRow != request.lastRow) {
            return false;
        }

        if (request.firstRow > TOTAL_ROWS - 1 || request.firstSeat > TOTAL_SEATS - 1 || request.lastSeat > TOTAL_SEATS - 1) {
            return false;
        }

        if (!checkAvailability(request, rowSeatArr)) {
            return false;
        }
        return !hasSingleGap(request, rowSeatArr);
    }

    static boolean hasSingleGap(Request req, boolean[][] rowSeatArr) {

        if (req.firstSeat > 1 && !rowSeatArr[req.firstRow][req.firstSeat - 1] && rowSeatArr[req.firstRow][req.firstSeat - 2]) {
            return true;
        }
        if ((req.firstSeat == 1 && !rowSeatArr[req.firstRow][0]) || (req.lastSeat == TOTAL_SEATS - 2 && !rowSeatArr[req.firstRow][TOTAL_SEATS - 1])) {
            return true;
        }
        return req.lastSeat < TOTAL_SEATS - 2 && !rowSeatArr[req.firstRow][req.lastSeat + 1] && rowSeatArr[req.firstRow][req.lastSeat + 2];
    }

    static boolean checkAvailability(Request request, boolean[][] rowSeatArr) {
        for (int i = request.firstSeat; i <= request.lastSeat; i++) {
            if (rowSeatArr[request.firstRow][i]) {
                return false;
            }
        }
        return true;
    }
}
