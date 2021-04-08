package testSuite;

import com.google.gson.Gson;
import util.ProjectConfig;

public class BaseTest {
    public ProjectConfig projectConfig;
    public Gson gson;

    public BaseTest()
    {
        projectConfig = new ProjectConfig();
        gson = new Gson();
    }
}
