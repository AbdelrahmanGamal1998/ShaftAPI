import com.shaft.driver.SHAFT;
import org.example.BookingApi;
import org.example.MainApis;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.example.MainApis.baseUrl;


public class APIShaft {
    private SHAFT.API api;
    private MainApis MainApis;
    private BookingApi BookingApi;
    ///srevices names:


    ///// status codes
 //   private final int success_statusCode= 200;
   // private final int delete_statusCode = 201;


    @Test
    public void createBookingTest(){
        BookingApi.createBooking("Jim","Brown","Breakfast");
        BookingApi.validateBookingCreation("Jim","Brown","Breakfast");
    }


    @Test
    public void BookingIDTest(){
        BookingApi.deleteBooking("Jim", "Brown");
    }

    @BeforeClass
    public void beforeClass(){
        api = new SHAFT.API(baseUrl);
        MainApis = new MainApis(api);
        BookingApi =new BookingApi(api);
        MainApis.login("admin","password123");

    }





}