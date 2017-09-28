package ws;


import java.io.OutputStreamWriter;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import ws.*;

public class JSONWS
{
    public static String sendData(String urlWS , String parameter, String datosParaEnviar)
    {
        URL url;
        String data = "";

        try
        {
            url = new URL(urlWS);

            if(url != null)
            {
                StringBuilder postData  = new StringBuilder();
                
                /*postData.append(URLEncoder.encode("jugadas", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf("1"), "UTF-8"));*/
                
                postData.append(parameter + "=" + datosParaEnviar);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                
                //System.out.println("Sending: " + postData);
                
                
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);
                    
                //1.Traigo los datos del WS a un "Archivo":
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                //2.Leo el archivo y guardo el contenido en data:
                String linea;
                while(( linea = in.readLine()) != null)
                {
                    data += linea;
                } 
            }
        }
        catch(Exception e)
        {
            System.out.println( "Error: sendData(" + urlWS + ") ->" + e.toString() );
        }
        return data;
    }
    
    public static String sendFile(String urlWS , String parameter, File archivo)
    {
        URL url;
        String data = "";
        String response = "";
        
        try
        {
            url = new URL(urlWS);

            if(url != null)
            {
                String charset = "UTF-8";
                String requestURL = urlWS;

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
               // multipart.addFormField("param_name_1", "param_value");
                //multipart.addFormField("param_name_2", "param_value");
                //multipart.addFormField("param_name_3", "param_value");
                multipart.addFilePart(parameter, archivo);
                response = multipart.finish(); // response from server.

                /*
                StringBuilder postData  = new StringBuilder();
                
                /*postData.append(URLEncoder.encode("jugadas", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf("1"), "UTF-8"));*/
                
               /* postData.append(parameter + "=" + datosParaEnviar);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                
                //System.out.println("Sending: " + postData);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.addRequestProperty("Content-length", reqEntity.getContentLength()+"");
                conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

                OutputStream os = conn.getOutputStream();
                reqEntity.writeTo(conn.getOutputStream());
                os.close();
                conn.connect();
                
                
                //1.Traigo los datos del WS a un "Archivo":
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                //2.Leo el archivo y guardo el contenido en data:
                String linea;
                while(( linea = in.readLine()) != null)
                {
                    data += linea;
                } */
            }
        }
        catch(Exception e)
        {
            System.out.println( "Error: sendData(" + urlWS + ") ->" + e.toString() );
        }
        return response;
    }
    private static String getDataFromWS(String urlWS)
    {
        URL url;
        String data = "";
        try
        {
            url = new URL(urlWS);
            if(url != null)
            {
                //1.Traigo los datos del WS a un "Archivo":

                InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
                if(inputStreamReader != null)
                {
                    BufferedReader in = new BufferedReader(inputStreamReader);

                    //2.Leo el archivo y guardo el contenido en data:
                    String linea;
                    while(( linea = in.readLine()) != null)
                    {
                        data += linea;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("ERROR: Controller.getDataFromWS(" + urlWS +")");
        }
        return data;
    }
    public static Object dameObjetoJava(String url, Class claseAParsear)
    {
        String data = getDataFromWS(url);
        //System.out.println("dameObjetoJava: RECIBO ESTO -> " + url + "-> "+ data);

        Gson gson = new Gson();
        Object obj = null;
        if(!data.trim().equalsIgnoreCase(""))
        {
            obj = gson.fromJson(data, claseAParsear);
        }

        /* System.out.println("dameObjetoJava : parsie esto ->" + obj);*/

        return obj;
    }
    public static ArrayList dameArrayJava(String url, Class claseAParsear)
    {
        ArrayList arrSaliente = new ArrayList<>();

        String dataFromWS = getDataFromWS(url);
        //System.out.println("dataFromWS: " + dataFromWS);

        if(!dataFromWS.trim().equalsIgnoreCase(""))
        {
            Gson gson = new Gson();
            JsonArray entries = (JsonArray) new JsonParser().parse(dataFromWS);
            //System.out.println("entries: " + entries);

            for(int i = 0 ; i < entries.size() ; i++)
            {
                Object objGenerico = gson.fromJson( entries.get(i) , claseAParsear);
                arrSaliente.add(objGenerico);
                //System.out.println("objGenerico: " + objGenerico);
            }
        }


        return arrSaliente;
    }
    public static String dameArrayEnJSON(ArrayList<Object> arr)
    {
        Gson gson = new Gson();
        return gson.toJson(arr);
    }
    public static ArrayList solamenteConvertirDatosJSONaArrayJava(String datosJSON,Class claseAParsear)
    {
        ArrayList arrSaliente = new ArrayList<>();

        if(!datosJSON.trim().equalsIgnoreCase(""))
        {
            Gson gson = new Gson();
            JsonArray entries = (JsonArray) new JsonParser().parse(datosJSON);
            //System.out.println("entries: " + entries);

            for(int i = 0 ; i < entries.size() ; i++)
            {
                Object objGenerico = gson.fromJson( entries.get(i) , claseAParsear);
                arrSaliente.add(objGenerico);
                //System.out.println("objGenerico: " + objGenerico);
            }
        }

        return arrSaliente;
    }
    public static Object solamenteConvertirDatosJSONaObjetoJava(String datosJSON, Class claseAParsear)
    {
        Gson gson = new Gson();
        Object obj = null;
        if(!datosJSON.trim().equalsIgnoreCase(""))
        {
            obj = gson.fromJson(datosJSON, claseAParsear);
        }
        return obj;
    }
}