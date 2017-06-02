package p4.guide_animals.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Kravtsov.a on 17.04.2015.
 */
public class HTTPtransport {

    private RequestParams requestParams = null;
    private String token = null;

    public HTTPtransport()
    {
        setDefault();
    }

    enum RequestParams {
        DEFAULT,AUTHORIZATION
    }

    public RequestParams setDefault(){
        return requestParams = RequestParams.DEFAULT;
    }

    public RequestParams setAuthorization(){
        return requestParams = RequestParams.AUTHORIZATION;
    }

    public void setToken(String token){this.token = token;}

    /**
     * Issue a POST request to the server.
     * @param url_post POST address.
     * @param params request parameters.
     */
    public  void postServer(String url_post, Map<String, String> params)
            throws IOException
    {

        URL url = isUrl(url_post);
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext())
        {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }

        String body = bodyBuilder.toString();
        //  Log.v(LogActivity.TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();

        //Создание параметров в заголовке для соединения с сервером
        switch (requestParams)
        {
            case DEFAULT:
                initHttpConnector(url,bytes);
            case AUTHORIZATION:
                if(this.token!=null)
                {
                    initHttpsConnector(url,bytes);
                }
                break;
            default:
        }

    }

    /**
     * Issue a POST request to the server.
     * @param url_post POST address.
     * @param params request parameters.
     */
    public JSONArray getJSONArray(String url_post, Map<String, String> params)
    {
        JSONArray jsonArray = null;
        String jsonString = getJSONParamString(url_post, params);

        try {
            if(!jsonString.equals(""))
                jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }



    /**
     * Issue a POST request to the server.
     * @param url_post POST address.
     * @param params request parameters.
     */
    public JSONArray getJSONArrayParamObject(String url_post, Map<String, Object> params)
    {
        JSONArray jsonArray = null;
        String jsonString = getJSON(url_post, params);
        try {
            if(!jsonString.equals(""))
                jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }



    /**
     * Issue a POST request to the server.
     * @param url_post POST address.
     * @param params request parameters.
     */
    public JSONObject getJSONObject(String url_post, Map<String, String> params)
    {
        JSONObject jsonObject = null;

        String jsonString = getJSONParamString(url_post, params);

        try {
            if(!jsonString.equals(""))
                jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public String getJSONParamString(String url_post, Map<String, String> params)
    {
        URL url = isUrl(url_post);

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext())
        {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }

        String body = bodyBuilder.toString();
        byte[] bytes = body.getBytes();

        //Создание параметров в заголовке для соединения с сервером
        switch (requestParams)
        {
            case DEFAULT:
                return   initHttpConnector(url,bytes);
            case AUTHORIZATION:
                if(this.token!=null)
                {
                    return  initHttpsConnector(url,bytes);
                }
                break;
            default:
        }

        return null;
    }


    public String getJSON(String url_post, Map<String, Object> params)
    {
        URL url = isUrl(url_post);
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext())
        {
            Map.Entry<String, Object> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());

            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }

        String body = bodyBuilder.toString();
        byte[] bytes = body.getBytes();
        //Создание параметров в заголовке для соединения с сервером
        switch (requestParams)
        {
            case DEFAULT:
                return   initHttpConnector(url,bytes);
            case AUTHORIZATION:
                if(this.token!=null)
                {
                    return  initHttpsConnector(url,bytes);
                }
                break;
            default:
        }

        return null;
    }


    private URL isUrl(String url_post)
    {
        URL url;
        try
        {
            url = new URL(url_post);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("invalid url: " + url_post);
        }

        return url;
    }

    public void getDownloadFile(String url_file,String file_save_path)
    {
        try {
            File file = new File(file_save_path);
            if(!file.exists())
            {
                  URL url = new URL(url_file);
                  URLConnection connection = url.openConnection();
                  connection.connect();
                  // this will be useful so that you can show a typical 0-100% progress bar
                 // int fileLength = connection.getContentLength();

                  // download the file
                  InputStream input = new BufferedInputStream(connection.getInputStream());
                  OutputStream output = new FileOutputStream(file_save_path);
                  byte data[] = new byte[1024];
                  long total = 0;
                  int count;
                  while ((count = input.read(data)) != -1) {
                      total += count;
                      // publishing the progress....
                     /* Bundle resultData = new Bundle();
                      resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                      receiver.send(UPDATE_PROGRESS, resultData);*/
                      output.write(data, 0, count);
                  }

                  output.flush();
                  output.close();
                  input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String initHttpsConnector(URL url, byte[] bytes)
    {
        HttpURLConnection sconn = null;
        String responseString = null;
        String line;
        try {
            sconn = (HttpURLConnection) url.openConnection();
            sconn.setDoOutput(true);
            sconn.setFixedLengthStreamingMode(bytes.length);
            sconn.setRequestMethod("POST");
            sconn.setRequestProperty("Authorization","Bearer "+ this.token);
            sconn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;");

            // post the request
            OutputStream out = sconn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = sconn.getResponseCode();

            if (status != 200) {
                responseString =  sconn.getResponseMessage();
                throw new IOException("Post failed with error code " + status);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sconn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();


            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line + '\n');
            }

            responseString = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sconn.disconnect();
        }

        return responseString;
    }


    private String initHttpConnector(URL url, byte[] bytes)
    {
        HttpURLConnection conn =null;
        String responseString = null;
        String line;
        try {


            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");

            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();

            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();


            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line + '\n');
            }

            responseString = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return responseString;
    }


}
