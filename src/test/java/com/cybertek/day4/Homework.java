package com.cybertek.day4;

import com.cybertek.utilities.HRTestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework extends HRTestBase {
    @DisplayName("GET request to countries with Path method")
    @Test
    public void test1() {

        Response response = given().accept(ContentType.JSON)
                .queryParam("q", "{\"country_id\": \"US\"}")
                .when()
                .get("/countries");

        assertEquals(200, response.statusCode());


        //print countryId
        String countryId = response.path("items[0].country_id");
        System.out.println("countryId = " + countryId);
        assertEquals("US", countryId);

        //print country name
        String countryName = response.path("items[0].country_name");
        System.out.println("countryName = " + countryName);
        assertEquals("United States of America", countryName);

        int regionId = response.path("items[0].region_id");
        System.out.println("regionId = " + regionId);
        assertEquals(2, regionId);

    }

    @DisplayName("Employees table")
    @Test
    public void test2() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("q", "{\"department_id\":80}")
                .when()
                .get("/employees");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.header("Content-Type"));
        System.out.println(response.path("items.job_id").toString());

        //make sure all job_id start with SA
        assertTrue(response.body().asString().contains("SA_"));


        List<Integer> allDepId = response.path("items.department_id");
        int count = 0;
        for (Integer depID : allDepId) {

            count++;
            assertEquals(80, depID);
        }
        assertTrue(count == 25);
    }

    /*
-Query param value q= region_id 3
- When users sends request to /countries
- Then status code is 200
- And all regions_id is 3
- And count is 6
- And hasMore is false
- And Country_name are;
Australia,China,India,Japan,Malaysia,Singapore
     */
    @DisplayName("GET request to countries with Path method")
    @Test
    public void test3() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("q", "{\"region_id\":3}")
                .when()
                .get("/countries");
        JsonPath jsonPath = response.jsonPath();
        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.header("Content-Type"));
        List<Integer> allRegionId = jsonPath.getList("items.region_id");
        int count = 0;
        for (Integer regionID : allRegionId) {

            count++;
            assertEquals(3, regionID);
        }
        assertTrue(count == 6);
        boolean check = jsonPath.getBoolean("hasMore");
        assertTrue(check == false);
        List<String> allCountries = jsonPath.getList("items.country_name");
        List<String> expected = new ArrayList<String>();
        expected.add("Australia");
        expected.add("China");
        expected.add("India");
        expected.add("Japan");
        expected.add("Malaysia");
        expected.add("Singapore");
        assertTrue(allCountries.containsAll(expected));

    }
}