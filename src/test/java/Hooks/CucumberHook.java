package Hooks;
import io.cucumber.java.Before;

import static io.restassured.RestAssured.baseURI;

public class CucumberHook {
    @Before
    public  void setup() {
        baseURI="http://127.0.0.1:8000";
    }
}
