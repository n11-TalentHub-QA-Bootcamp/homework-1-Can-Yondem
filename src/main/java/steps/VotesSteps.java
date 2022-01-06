package steps;

import filter.CustomLogFilter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class VotesSteps {
    String key;
    Response response;
    CustomLogFilter customLogFilter ;

    @Given("x-api-key and baseURI are already acquired.")
    public void x_api_key_and_base_uri_are_already_acquired() {
        key = "YOUR_API_KEY";
        baseURI = "https://api.thedogapi.com/v1/";
        customLogFilter = new CustomLogFilter();
    }

    @When("I will create one more vote for this {string} and {string}")
    public void i_will_create_one_more_vote(String sub_id, String image_id) {
        // Taslaktan gelen image_id ve sub_id'yi post metodu ile endpoint'e yolladık.
        String requestBody = "{\n" +
                "  \"image_id\": \""+image_id+"\",\n" +
                "  \"sub_id\": \""+sub_id+"\""+",\n"+
                "  \"value\": \"1\"\n}";

       try {
           response = given()
                   .headers("x-api-key",key)
                   .contentType(ContentType.JSON)
                   .filter(customLogFilter)
                   .and()
                   .body(requestBody)
                   .when()
                   .post("votes")
                   .then()
                   .statusCode(200)
                   .and()
                   .contentType(ContentType.JSON)
                   .extract().response();
       }catch (AssertionError assertionError){
           System.out.println(assertionError.getMessage());
           System.out.println(customLogFilter.getRequestBuilder().toString());
           System.out.println(customLogFilter.getResponseBuilder().toString());
       }
    }

    @Then("I should see {string} and {string}")
    public void i_have_numbers_plus_one_votes_for_this(String sub_id, String image_id) {
        //API'dan get metodu sub_id parametresi göndererek filtreleme yaptık.
        response = given().headers("x-api-key",key)
                .accept(ContentType.JSON)
                .pathParam("sub_id",sub_id)
                .when()
                .get("votes?sub_id={sub_id}")
                .then()
                .statusCode(200)
                .extract().response();

        //Response'dan gelen json verinin image_id keyine göre filtreledik ve listeye çevirdik.
        List<String> voteList = response.getBody().jsonPath().getList("image_id");

        //Verilen oy son indekse ekleneceği için listenin son indeksini aldık ve string veri tipine çevirdik.
        String lastIndexVoteList = voteList.get(voteList.size()-1).toString();

        //Assert ile taslaktan gelen veri ile son indeks eşit mi diye kontrol ettirdik.
        Assert.assertEquals(image_id,lastIndexVoteList);
    }

}
