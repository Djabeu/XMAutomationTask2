import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SWAPITest {

  private JSONObject getJsonObject(String url) {
    Response movieResponse = RestAssured.get(url);
    String movieResponseStr = movieResponse.asString();
    return new JSONObject(movieResponseStr);
  }

  @Test
  void test() {
    JSONObject movieResponseJsonObj = getJsonObject("http://swapi.dev/api/films/?search=Hope");

    JSONArray movieResponseJsonArr = movieResponseJsonObj.getJSONArray("results");
    JSONArray characters = movieResponseJsonArr.getJSONObject(0).getJSONArray("characters");

    String biggsDarklighter = "";
    for (int i = 0; i < characters.length(); i++) {
      Response characterResponse = RestAssured.get((String) characters.get(i));
      String characterJsonStr = characterResponse.asString();
      JSONObject characterJsonObj = new JSONObject(characterJsonStr);

      if (characterJsonObj.get("name").equals("Biggs Darklighter")) {
        biggsDarklighter = (String) characters.get(i);
        break;
      }
    }

    JSONObject characterResponseJsonObj = getJsonObject(biggsDarklighter);

    JSONArray starships = characterResponseJsonObj.getJSONArray("starships");
    String starship = (String) starships.get(0);

    JSONObject starshipResponseJsonObj = getJsonObject(starship);

    Assert.assertEquals(starshipResponseJsonObj.get("starship_class"), "Starfighter");
    JSONArray pilots = starshipResponseJsonObj.getJSONArray("pilots");

    boolean lukeSkywalkerIsPilot = false;
    for (int i = 0; i < pilots.length(); i++) {
      JSONObject pilotsJsonObj = getJsonObject((String) pilots.get(i));

      if (pilotsJsonObj.get("name").equals("Luke Skywalker")) {
        lukeSkywalkerIsPilot = true;
        break;
      }
    }
    Assert.assertTrue(lukeSkywalkerIsPilot);
  }
}
