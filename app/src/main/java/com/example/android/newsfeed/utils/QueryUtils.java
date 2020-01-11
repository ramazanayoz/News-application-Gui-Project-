

package com.example.android.newsfeed.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.android.newsfeed.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsfeed.utils.Constants.JSON_KEY_WEB_TITLE;

/**yapılan işlemler
 * url ile bağlantı sağlıyoruz ve url içindekileri string değerine aktararak json üzerinden parse işlemleri yapıyoruz
 *
 * Belirlenen keyler'in value'leri string olarak alınıyor ayrıştırılıyor(parse) jsondan biçiminden,
 * sonra alınan value'ler newlist değişkenli arrayliste kaydediliyor List<News> newsList = new ArrayList<>();
 * return  newList olur
 */
public class QueryUtils {

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl,Context mContext) {

        /** String şeklindeki linki url nesnesine çeviriyoruz üzerinde openconnection gibi bağlantılar yapmak için */
        URL url = createUrl(requestUrl);
        System.out.println("göster ---> QueryUtils ---> fetchNewsData(String requestUrl) ----> url ---->"+ url);


        /**makeHttpRequest(url) fonk kullanılarak json sayfası içindekiler string olarak jsonResponse veriable'ye aktarılıyor */
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url,mContext);
            System.out.println("göster ---> QueryUtils ---> fetchNewsData(String requestUrl) ----> jsonResponse ---->"+ jsonResponse);
        } catch (IOException e) {
            // Contextde Toast biçiminde hata mesajı gösteriyor
            Toast.makeText(mContext, "Problem making the HTTP request. "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        /**Belirlenen keyler'in value'leri string olarak alınıyor ayrıştırılıyor(parse) jsondan biçiminden,
         * sonra alınan value'ler newlist değişkenli arrayliste kaydediliyor List<News> newsList = new ArrayList<>();*/
        List<News> newsList = extractFeatureFromJSON(jsonResponse,mContext);
        System.out.println("göster ---> QueryUtils ---> fetchNewsData(String requestUrl) ----> newsList ---->"+ newsList);
        //System.out.println("göster ---> QueryUtils ---> fetchNewsData(String requestUrl) ----> newsList ---->"+ newsList.get(1).getTitle());



        // Return List<News> newsList
        return newsList;
    }


    /** Url objesi oluşturuluyor stringUrl deki url kullanılarak*/
    private static URL createUrl(String stringUrl) {
        System.out.println("createUrl --> createUrl(String stringUrl) --> stringUrl -->" +stringUrl);
        URL url = null;
        try {
            url = new URL(stringUrl);
            System.out.println("createUrl --> createUrl(String stringUrl) --> url -->" +url);
        } catch (MalformedURLException e) {
            /**"Problem building the URL.", e);*/
        }
        return url;
    }




    /**belirtilen url'ye http request yapılıyor ve  response Alarak url içindekiler JsonResponse Veriable'a  string olarak aktarılıyor sonra return yapılıyorak alınıyor*/

    private static String makeHttpRequest(URL url,Context mContext) throws IOException { //
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();// urlye bağlantı sağlanıyor
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(Constants.CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod(Constants.REQUEST_METHOD_GET); //get
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == Constants.SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);//url içindeki değerler tek tek okunur ve jsonresponse aktarılır
            } else {
                // Contextde Toast biçiminde hata mesajı gösteriyor
                Toast.makeText(mContext,"Error response code: " + urlConnection.getResponseCode(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**kısaca url içindeki değerler tek tek okunur ve jsonresponse veriable'ye aktarılır*/
    private static String readFromStream(InputStream inputStream) throws IOException {
        System.out.println("QueryUtils --> readFromStream(InputStream inputStream) --> inputStream --> "+ inputStream );
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            System.out.println("QueryUtils --> readFromStream(InputStream inputStream) --> inputStreamReader --> "+ inputStreamReader );
            BufferedReader reader = new BufferedReader(inputStreamReader);
            System.out.println("QueryUtils --> readFromStream(InputStream inputStream) --> reader --> "+ reader );
            String line = reader.readLine();
            System.out.println("QueryUtils --> readFromStream(InputStream inputStream) --> line1 --> "+ line );
            while (line != null) {
                output.append(line);
                System.out.println("QueryUtils --> readFromStream(InputStream inputStream) -->output --> "+output);
                line = reader.readLine();
                System.out.println("QueryUtils --> readFromStream(InputStream inputStream) --> line2 --> "+ line );
            }
        }
        return output.toString();
    }

    /**Belirlenen keyler'in value'leri string olarak alınıyor ayrıştırılıyor(parse) jsondan biçiminden,
     * sonra alınan value'ler newlist değişkenli arrayliste kaydediliyor List<News> newsList = new ArrayList<>();
     List<News> newsList biçiminde oluşturulan değişken return ediliyor*/
    private static List<News> extractFeatureFromJSON(String jsonResponse,Context mContext) {
        //Json boşssa veya null ise veri gelmediği için direk null olarak return ediyoruz
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        //Jsondan alacağımız string value'leri ekleyeceğimiz boş bir array list oluşturuyoruz
        List<News> newsList = new ArrayList<>();


        try {
            //jsonResponse değişkeninden JSONObject oluşturuyoruz ÇÜNKÜ baseJsonResponse ile verileri parse(aayrıştırma) yaparak,
            // key/value eşleşmesi yapıyoruz Json'dan
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            //Json veri nasıl çekeceğimi anlamak için denemeler
            System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> baseJsonResponse ---->"+ baseJsonResponse);
            System.out.println("göster ---> QueryUtils --->****111111***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS).getJSONObject(0).getString(JSON_KEY_WEB_TITLE));
            //response keyi ile eşleşen valueleri gösterir
            System.out.println("göster ---> QueryUtils --->****222222***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE));
            //results keyi ile eşleşen valueleri gösterir
            System.out.println("göster ---> QueryUtils --->****333333***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS));
            //results keyi ile eşleşen ve  index 2 olduğu için 2.arraydeki verileri gösterir
            System.out.println("göster ---> QueryUtils --->****4444444***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS).getJSONObject(2));
            System.out.println("göster ---> QueryUtils --->****5555555***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS).getJSONObject(2).getString("webTitle"));
            //item null olabilir            System.out.println("göster ---> QueryUtils --->****6666666***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS).getJSONObject(2).getJSONArray("tags").getJSONObject(0).getString("webTitle"));
            //item null olabilir     System.out.println("göster ---> QueryUtils --->****7777777***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS).getJSONObject(2).getJSONArray("tags").getJSONObject(1));
            //item null olabilir            System.out.println("göster ---> QueryUtils --->****7777777***** ---->"+ baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE).getJSONArray(Constants.JSON_KEY_RESULTS).getJSONObject(2).getJSONObject("fields").getString("thumbnail"));






            //response olarak adlandırılan key ile ilişkilendirilmiş alt değere getJSONObject ile ulaşır
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE);
            System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> responseJsonObject ---->"+ responseJsonObject);


            //"results" olarak adlandırılan key ile ilişkilendirilmiş JSONArray'e ulaşır
            JSONArray resultsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS);
            System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> resultsArray ---->"+ resultsArray);

            //burdan sonra artık kısaca şu işlemleri yapıyoruz yukarda hazırlıkları bitirdik artık,
            // Belirlenen keylerin valueleri string olarak alınıyor jsondan, sonra alınan valueler newlist değişkenli arrayliste
            // kaydediliyor List<News> newsList = new ArrayList<>(); ve newlist return edilir

            // İstediğimiz değerleri getString ile alıyoruz key value eşleşmesi sayesinde sonra o değerleri veriable kaydediyoruz
            for (int i = 0; i < resultsArray.length(); i++) {

                //haberlerin içindeki i pozisyondaki haberi alır currentNews değişkenine
                JSONObject currentNews = resultsArray.getJSONObject(i);
                System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> currentNews  "+i+" ---->"+ currentNews);

                // i pozisyondaki haberden "webTitle" key ile value alır
                String webTitle = currentNews.getString(JSON_KEY_WEB_TITLE);
                System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> webTitle "+i+" ---->"+ webTitle);

                // i pozisyondaki haberden "sectionName" key ile value alır
                String sectionName = currentNews.getString(Constants.JSON_KEY_SECTION_NAME);
                System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> sectionName "+i+" ---->"+ sectionName);

                // i pozisyondaki haberden "webPublicationDate" key ile value alır
                String webPublicationDate = currentNews.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);
                System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> webPublicationDate "+i+" ---->"+ webPublicationDate);

                // i pozisyondaki haberden "webUrl" key ile value alır
                String webUrl = currentNews.getString(Constants.JSON_KEY_WEB_URL);
                System.out.println("göster ---> QueryUtils ---> extractFeatureFromJSON(String jsonResponse) ----> webUrl "+i+" ---->"+ webUrl);



                // burda yazarın valuesi alınırken i pozisyondaki haberden tags isimli JsonArray'e ulaşılarak getString methoduyla webTitle keyden alınır
                String author = null;
                if (currentNews.has(Constants.JSON_KEY_TAGS)) {
                    // Extract the JSONArray associated with the key called "tags"
                    JSONArray tagsArray = currentNews.getJSONArray(Constants.JSON_KEY_TAGS);
                    if (tagsArray.length() != 0) {
                        // Extract the first JSONObject in the tagsArray
                        JSONObject firstTagsItem = tagsArray.getJSONObject(0);
                        // Extract the value for the key called "webTitle"
                        author = firstTagsItem.getString(JSON_KEY_WEB_TITLE);
                    }
                }

                // burda thumbnail ve trailText i pozisyondaki haberden fields olarak adlandırılan key ile ilişkilendirilmiş alt
                // değere getJSONObject ile ulaşır ve sonra
                // getString methoduyla thumbnail ve trailText key'lerinden value'ler alınır
                String thumbnail = null;
                String trailText = null;
                if (currentNews.has(Constants.JSON_KEY_FIELDS)) {
                    // Extract the JSONObject associated with the key called "fields"
                    JSONObject fieldsObject = currentNews.getJSONObject(Constants.JSON_KEY_FIELDS);
                    // If there is the key called "thumbnail", extract the value for the key called "thumbnail"
                    if (fieldsObject.has(Constants.JSON_KEY_THUMBNAIL)) {
                        thumbnail = fieldsObject.getString(Constants.JSON_KEY_THUMBNAIL);
                    }
                    // If there is the key called "trailText", extract the value for the key called "trailText"
                    if (fieldsObject.has(Constants.JSON_KEY_TRAIL_TEXT)) {
                        trailText = fieldsObject.getString(Constants.JSON_KEY_TRAIL_TEXT);
                    }
                }

                //sonra alınan valueler newlist değişkenli arrayliste kaydediliyor List<News> newsList = new ArrayList<>();
                // ve newlist return edilir
                News news = new News(webTitle, sectionName, author, webPublicationDate, webUrl, thumbnail, trailText);

                //her bir news objessi newliste ekleme yapılacak
                newsList.add(news);
            }
        } catch (JSONException e) {
            Toast.makeText(mContext, "Problem parsing the news JSON results "+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        // Return  list of news
        return newsList;
    }
}
