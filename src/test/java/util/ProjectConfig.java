package util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class ProjectConfig {

    private JsonObject configJSON;
    public String baseURL;

    public ProjectConfig()
    {
        readConfigJSON();
        baseURL = String.valueOf(getProjectConfig().get("baseURL").getAsString());
    }

    public void readConfigJSON()
    {
        String configFilePath = "./src/test/java/config.json";
        Gson gson = new Gson();

        try
        {
            File file = new File(configFilePath);
            String configJsonFile = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            configJSON = gson.fromJson(configJsonFile, JsonObject.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public JsonObject getProjectConfig()
    {
        return configJSON.getAsJsonObject("projectConfig");
    }

    public JsonObject getAPIList(String endPointName)
    {
        return configJSON.getAsJsonObject("api").getAsJsonObject(endPointName);
    }
}