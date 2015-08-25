package csd.jt.capsmobile;

/**
 * Created by John on 18/7/2015.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class ServiceHandler {

    static String response = null;

    public ServiceHandler() {

    }

    /**
     * Making service call
     *
     * @url - url to make request
     */
    public String makeServiceCall(String url) {
        return this.makeServiceCall(url, null);
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @params - http request params
     */
    public String makeServiceCall(String url,
                                  HashMap<String, String> params) {
        try {


            String data = null;
            boolean first = true;
            if (params.size() > 0) {
                for (String key : params.keySet()) {
                    if (first) {
                        data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
                        first = false;
                    }
                    else
                        data += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
                }
            }


            URL link = new URL(url);
            URLConnection conn = link.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            response = sb.toString();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
}
