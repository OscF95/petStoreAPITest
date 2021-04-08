package testSuite.pet.test;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testSuite.BaseTest;
import testSuite.pet.model.CategoryModel;
import testSuite.pet.model.TagModel;

import static org.hamcrest.Matchers.*;

public class petAPI extends BaseTest {
    private int petId;
    private String findByStatus;
    private String petIdURL;


    @BeforeClass
    public void testSetup()
    {
        CategoryModel category = new CategoryModel();
        TagModel tag = new TagModel();

        petId = data.randomNumber();
        findByStatus = petEndPoint + projectConfig.getAPIList("pet").get("findByStatus").getAsString();
        petIdURL = String.format("%s/%s", petEndPoint, petId);
        String[] photoUrls = new String[]{"Url1", "Url2"};

        tag.setId(data.randomNumber());
        tag.setName("Klagan tag");

        category.setId(data.randomNumber());
        category.setName("CategoryName");

        petRequest.setId(petId);
        petRequest.setName("Dog");
        petRequest.setCategory(category);
        petRequest.setPhotoUrls(photoUrls);
        petRequest.setTags(new TagModel[]{tag});
        petRequest.setStatus("available");
    }

    @Test(priority = 1)
    public void cratePet()
    {
        String createPetBodyRequest = gson.toJson(petRequest);

        RestAssured
                .given()
                    .baseUri(projectConfig.baseURL)
                    .and()
                    .contentType("application/json")
                    .with()
                    .request().body(createPetBodyRequest)
                .when()
                    .post(petEndPoint)
                .then()
                    .log().all()
                    .and().assertThat().statusCode(is(equalTo(200)))
                    .and()
                    .body("id", equalTo(petId));
    }

    @Test(priority = 2)
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

    @Test(priority = 3)
    public void findById()
    {
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

    @Test(priority = 4)
    public void updatePet()
    {
        petRequest.setName("Cat");
        petRequest.setStatus("pending");
        String createPetBodyRequest = gson.toJson(petRequest);

        RestAssured
                .given()
                    .baseUri(projectConfig.baseURL)
                    .and()
                    .contentType("application/json")
                    .with()
                    .request().body(createPetBodyRequest)
                .when()
                    .put(petEndPoint)
                .then()
                    .log().all()
                    .and().assertThat().statusCode(is(equalTo(200)))
                    .and()
                    .body("name", equalTo("Cat"));
    }

    @Test(priority = 5)
    public void deletePet()
    {
        RestAssured
                .given()
                    .baseUri(projectConfig.baseURL)
                .when()
                    .delete(petIdURL)
                .then()
                    .log().all()
                    .and().assertThat().statusCode(is(equalTo(200)))
                    .and().extract().body().asString();;
    }
}
