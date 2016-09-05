package msvision;

//// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import experiments.video.DetectedFaces;

public class JavaSample {
	private static Emotion strongestEmotion;

	public static void main(String[] args) {
		callMSAPINoFaceRect();
	}

	private static void callMSAPIWithFaceRect(DetectedFaces faces) {
		HttpClient httpclient = HttpClients.createDefault();
		try {
			URIBuilder builder = new URIBuilder(
					"https://api.projectoxford.ai/emotion/v1.0/recognize");
			builder.setParameter("visualFeatures", "Categories");
			builder.setParameter("details", "{string}");

			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Content-Type", "application/octet-stream");
			request.setHeader("Ocp-Apim-Subscription-Key",
					"6c252f89e302408c9a1d2dff47cc38f2");

			int[] detectedFace = faces.getFirstDetectedFace();
			String faceRect = constructFaceRectJSON(detectedFace);
			builder.addParameter("faceRectangles", faceRect);
			
			// Request body
//			StringEntity reqEntity = new StringEntity(
//					"{ \"url\": \"http://www.projectu.tv/wp-content/uploads/2015/06/shutterstock_81301759.jpg\" }");
			
//			StringEntity reqEntity = new StringEntity(
//					"{ \"url\": \"http://www.publicdomainpictures.net/pictures/20000/velka/sad-child-portrait.jpg\" }");

//			StringEntity reqEntity = new StringEntity(
//					"{ \"url\": \"https://az616578.vo.msecnd.net/files/2016/02/05/6359029962037484701891030667_Aston-Villa-v-Liverpool-at-Wembley-1.jpg\" }");

			StringEntity requestEntity = new StringEntity(faces.getImageByteArr());
			request.setEntity(requestEntity);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String jsonStr = EntityUtils.toString(entity);
				System.out.println(jsonStr);
				strongestEmotion = getStrongestEmotion(jsonStr);
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private static String constructFaceRectJSON(int[] detectedFace) {
		String faceRectJSON = "{\"faceRectangle\": {" +
			      "\"left\": " + detectedFace[0] + "," +
			      "\"top\": " + detectedFace[1] + "," +
			      "\"width\": " + detectedFace[2] + "," +
			      "\"height\": " + detectedFace[3] + "}" + "}";
		return faceRectJSON;
	}

	private static void callMSAPINoFaceRect() {
		HttpClient httpclient = HttpClients.createDefault();

		try {
			URIBuilder builder = new URIBuilder(
					"https://api.projectoxford.ai/emotion/v1.0/recognize");

//			builder.setParameter("visualFeatures", "Categories");
//			builder.setParameter("details", "{string}");

			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key",
					"6c252f89e302408c9a1d2dff47cc38f2");

			// Request body
//			StringEntity reqEntity = new StringEntity(
//					"{ \"url\": \"http://www.projectu.tv/wp-content/uploads/2015/06/shutterstock_81301759.jpg\" }");
			
//			StringEntity reqEntity = new StringEntity(
//					"{ \"url\": \"http://www.publicdomainpictures.net/pictures/20000/velka/sad-child-portrait.jpg\" }");

			StringEntity reqEntity = new StringEntity(
					"{ \"url\": \"https://az616578.vo.msecnd.net/files/2016/02/05/6359029962037484701891030667_Aston-Villa-v-Liverpool-at-Wembley-1.jpg\" }");

			request.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String jsonStr = EntityUtils.toString(entity);
				System.out.println(jsonStr);
				strongestEmotion = getStrongestEmotion(jsonStr);
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static Emotion getStrongestEmotion(String jsonStr)
			throws JSONException, ParseException, IOException {
		Emotion transientEmotion = null;
		JSONArray jsonArr = new JSONArray(jsonStr);
		JSONObject jsonObj = (JSONObject) jsonArr.get(0);
		JSONObject emotionResultsArr = (JSONObject) jsonObj.get("scores");
		
		Iterator<String> keys = emotionResultsArr.keys();
		while (keys.hasNext()) {
			
			String emotionName = (String) keys.next();
			BigDecimal emotionScore = emotionResultsArr.getBigDecimal(emotionName);
			System.out.println("\nEmotion Name : " + emotionName);
			System.out.println("Emotion Score : " + emotionScore);
			
			if (transientEmotion == null) {
				transientEmotion = new Emotion(emotionName, emotionScore);
			} else {
				if(!transientEmotion.isStrongerThan(emotionScore)) {
					transientEmotion = new Emotion(emotionName, emotionScore); 
				}
			}
		}
		
		System.out.println("\n\nStrongest Emotion is  "+ transientEmotion);
		return transientEmotion;
	}
}