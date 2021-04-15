package cc.leevi.webbase.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtils extends org.apache.commons.lang3.ArrayUtils{
    public static String httpURLPOSTCase(String methodUrl) {
        StringBuilder lastAnswer = new StringBuilder();
        try {
            URL url = new URL(methodUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null) {
                lastAnswer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastAnswer.toString();
    }
}
