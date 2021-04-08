package testSuite;

import com.google.gson.Gson;
import testSuite.pet.model.PetRequestModel;
import util.DataGenerator;
import util.ProjectConfig;

public class BaseTest {
    public ProjectConfig projectConfig;
    public Gson gson;
    public DataGenerator data;
    public PetRequestModel petRequest;

    public String petEndPoint;

    public BaseTest()
    {
        projectConfig = new ProjectConfig();
        gson = new Gson();
        data = new DataGenerator();
        petRequest = new PetRequestModel();

        petEndPoint = projectConfig.getAPIList("pet").get("path").getAsString();
    }
}
