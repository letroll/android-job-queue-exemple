package com.example.testpathlib;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Julien Quiévreux on 23/12/13.
 */
public class Utils
{

    /**
     * @param url  chemin de la source html
     * @param nvps NameValuePair
     * @return retourne une chaine de caractère retourné par l'url appelé, nécessite la permission
     * android:name="android.permission.INTERNET
     */
    public static String GetHTML(String url, List<NameValuePair> nvps)
            throws IOException, URISyntaxException
    {
        return GetHTML(url, nvps, "", 10000, 10000);
    }

    /**
     * @return html source as string
     */

    public static String GetHTML(String url, List<NameValuePair> nvps, String useragent,
                                 int timeoutConnection, int timeoutSocket)
            throws URISyntaxException, IOException
    {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        if (useragent != "")
        {
            httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, useragent);
        }
        HttpProtocolParams.setUserAgent(httpClient.getParams(), useragent);

        HttpResponse res;
        if (!url.startsWith("http://") || url.startsWith("ftp://"))
        {
            url = "http://" + url;
        }
        URI uri = new URI(url);
        if (nvps != null)
        {
            HttpPost methodpost = new HttpPost(uri);
            methodpost.addHeader("pragma", "no-cache");
            methodpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            res = httpClient.execute(methodpost);
        } else
        {
            HttpGet methodget = new HttpGet(uri);
            methodget.addHeader("pragma", "no-cache");
            res = httpClient.execute(methodget);
        }
        InputStream data = res.getEntity().getContent();
        return generateString(data);

    }

    /**
     * Génére une chaine de caractère à partir d'un flux entrant.
     *
     * @param stream le flux entrant
     * @return la chaine de caractère obtenu
     */
    public static String generateString(InputStream stream)
    {
        return generateStringBuilder(stream).toString();
    }

    /**
     * Génére une chaine de caractère à partir d'un flux entrant.
     *
     * @param stream le flux entrant
     * @return la chaine de caractère obtenu
     */
    public static StringBuilder generateStringBuilder(InputStream stream)
    {
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        try
        {
            String cur;
            while ((cur = buffer.readLine()) != null)
            {
                sb.append(cur).append("\n");
            }
            stream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb;
    }
}
