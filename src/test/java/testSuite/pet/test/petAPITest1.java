package testSuite.pet.test;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testSuite.BaseTest;
import testSuite.pet.model.CategoryModel;
import testSuite.pet.model.PetRequestModel;
import testSuite.pet.model.TagModel;
import util.DataGenerator;

import static org.hamcrest.Matchers.*;

public class petAPITest1 extends BaseTest {
    private String petBodyRequest;
    private int petId;
    private String petEndPoint;
    private String findByStatus;


    @BeforeClass
    public void testSetup()
    {
        PetRequestModel petRequest = new PetRequestModel();
        CategoryModel category = new CategoryModel();
        TagModel tag = new TagModel();
        DataGenerator data = new DataGenerator();

        petEndPoint = projectConfig.getAPIList("pet").get("path").getAsString();
        findByStatus = petEndPoint + projectConfig.getAPIList("pet").get("findByStatus").getAsString();

        petId = data.randomNumber();
        String[] photoUrls = new String[]{"Url1", "Url2"};

        tag.setId(data.randomNumber());
        tag.setName("Klagan tag");

        category.setId(data.randomNumber());
        category.setName("CategoryName");

        petRequest.setId(petId);
        petRequest.setName("Some name");
        petRequest.setCategory(category);
        petRequest.setPhotoUrls(photoUrls);
        petRequest.setTags(new TagModel[]{tag});
        petRequest.setStatus("available");

        petBodyRequest = gson.toJson(petRequest);
    }

    @Test
    public void cratePet()
    {
        RestAssured
                .given()
                    .baseUri(projectConfig.baseURL)
                    .and()
                    .contentType("application/json")
                    .with()
                    .request().body(petBodyRequest)
                .when()
                    .post(petEndPoint)
                .then()
                    .log().all()
                    .and().assertThat().statusCode(is(equalTo(200)))
                    .and()
                    .body("id", equalTo(petId));
    }

    @Test
    public void findPetByStatus()
    {
        RestAssured
                .given()
                    .baseUri(projectConfig.baseURL)
                    .and()
                    .queryParam("format", "json")
                .when()
                    .get(findByStatus + "available")
                .then()
                    .log().all()
                    .and().assertThat().statusCode(is(equalTo(200)))
                    .and()
                    .body("id", hasItem(petId))
                    .body("status", hasItem("available"))
                    .and().extract().body().asString();
    }

    @Test
    public void findById()
    {
        String petIdURL = String.format("/pet/%s", petId);

        RestAssured
                .given()
                    .baseUri(projectConfig.baseURL)
                    .and()
                    .queryParam("format", "json")
                .when()
                    .get(petIdURL)
                .then()
                    .log().all()
                    .and().assertThat().statusCode(is(equalTo(200)))
                    .and()
                    .body("id", equalTo(petId))
                    .and().extract().body().asString();
    }
}
