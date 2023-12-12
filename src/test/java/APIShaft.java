import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APIShaft {
    private SHAFT.API api;
    String token;
    String iD;
    ///srevices names:
    private final String authentication_serviceName = "/auth";
    private final String booking_serviceName="/booking" ;

    ///// status codes
    private final int success_statusCode= 200;
    private final int delete_statusCode = 201;


    @Test
    public void createBookingTest(){
        createBooking("Jim","Brown","Breakfast");
        validateBookingCreation("Jim","Brown","Breakfast");
    }

    /*@Test
    public void deleteBookingTest(){
       deleteBooking();
    }*/
    @Test
    public void BookingIDTest(){
        deleteBooking("Jim", "Brown");
    }

    @BeforeClass
    public void beforeClass(){
        api = new SHAFT.API("https://restful-booker.herokuapp.com");
        login("admin","password123");

    }

    public void login(String username, String password){
        String tokenBody= """
                {
                    "username" : "{USERNAME}",
                    "password" : "{PASSWORD}"
                }
                """
                .replace("{USERNAME}",username)
                .replace("{PASSWORD}", password);
        api.post(authentication_serviceName).setContentType(ContentType.JSON).setRequestBody(tokenBody)
                .setTargetStatusCode(success_statusCode).perform();
        token = api.getResponseJSONValue("token");
        api.addHeader("Cookie", "token="+token);
    }
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