package com.tspolice.htplive.network;

import android.content.Context;
import android.os.StrictMode;

import com.tspolice.htplive.R;
import com.tspolice.htplive.utils.UiHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DistanceCalc {

    private static DistanceCalc mDistanceCalc;
    private Context mContext;
    private UiHelper mUiHelper;

    private DistanceCalc(Context context) {
        this.mContext = context;
        this.mUiHelper = new UiHelper(context);
    }

    public static synchronized DistanceCalc getInstance(Context context) {
        if (mDistanceCalc == null) {
            mDistanceCalc = new DistanceCalc(context);
        }
        return mDistanceCalc;
    }

    public String getDistance(double latitude, double longitude, double dest_latitude, double dest_longitude) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String distanceInKm = "";
        String tag[] = {"text"};
        HttpResponse response;
        try {
            mUiHelper.showProgressDialog(mContext.getResources().getString(R.string.please_wait), false);
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(URLs.getGoogleDirectionsApi(latitude, longitude, dest_latitude, dest_longitude));
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(is);
            if (document != null) {
                NodeList nodeList;
                ArrayList<String> arrayList = new ArrayList<>();
                for (String s : tag) {
                    nodeList = document.getElementsByTagName(s);
                    if (nodeList.getLength() > 0) {
                        Node node = nodeList.item(nodeList.getLength() - 1);
                        arrayList.add(node.getTextContent());
                    } else {
                        arrayList.add("7.8 km");
                    }
                }
                distanceInKm = String.format("%s", arrayList.get(0));
                mUiHelper.dismissProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distanceInKm;
    }
}
