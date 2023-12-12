package org.example;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

import static org.example.MainApis.delete_statusCode;
import static org.example.MainApis.success_statusCode;

public class BookingApi {
    private SHAFT.API api;

    public BookingApi(SHAFT.API api) {
        this.api = api;
    }

    String token;
    String iD;

    private final String booking_serviceName="/booking" ;
    public void createBooking(String firstName, String lastname, String additinalneeds){
        String bookingBody= """
                {
                    "firstname" : "{FIRSTNAME}",
                    "lastname" : "{LASTNAME}",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "{ADDITIONALNEEDS}"
                }
                """
                .replace("{FIRSTNAME}",firstName)
                .replace("{LASTNAME}",lastname)
                .replace("{ADDITIONALNEEDS}",additinalneeds);
        api.post(booking_serviceName)
                .setContentType(ContentType.JSON).setRequestBody(bookingBody)
                .setTargetStatusCode(success_statusCode).perform();
        iD = api.getResponseJSONValue("bookingid");

    }

    public void deleteBooking(String username, String password){
        String bookingiD = getBookingID(username, password);
        api.delete(booking_serviceName+"/"+bookingiD).setContentType(ContentType.JSON)
                .setTargetStatusCode(delete_statusCode).perform();
    }


    public String getBookingID(String firstname, String lastname){
        api.get(booking_serviceName).setUrlArguments("firstname"+firstname+ "lastname"+lastname).perform();
        return api.getResponseJSONValue("$[0].bookingid");
    }


    public void validateBookingCreation(String expectedfirstName, String expectedlastname, String expectedadditinalneeds){
        api.verifyThatResponse().extractedJsonValue("booking.firstname")
                .isEqualTo(expectedfirstName).perform();
        api.verifyThatResponse().extractedJsonValue("booking.lastname")
                .isEqualTo(expectedlastname).perform();
        api.verifyThatResponse().extractedJsonValue("booking.additionalneeds")
                .isEqualTo(expectedadditinalneeds).perform();
        api.verifyThatResponse().body().contains("\"bookingid\":").perform();
    }

}
