package org.cinema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
class Request {

    public int Id;
    public int firstRow;
    public int firstSeat;
    public int lastRow;
    public int lastSeat;


    public static Request getRequest(String rawRequest) {

        String[] parts = rawRequest.split("[,]", -1);
        String[] start = parts[1].split("[:]", -1);
        String[] end = parts[2].split("[:]", -1);

        Request request = new Request();
        request.Id = Integer.parseInt(parts[0].replace("(", ""));
        request.firstRow = Integer.parseInt(start[0]);
        request.firstSeat = Integer.parseInt(start[1]);
        request.lastRow = Integer.parseInt(end[0]);
        request.lastSeat = Integer.parseInt(end[1].replace(")", ""));

        return request;
    }

    public static Iterable<Request> Parse(Object[] requestObjectArr) {
        List<Request> parsedRequestsList = new ArrayList<>();

        for (Object requestObject : requestObjectArr) {
            parsedRequestsList.add(getRequest(requestObject.toString()));
        }
        return parsedRequestsList;
    }
}