
import com.slack.food.SlackBotApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackBotApplication.class)
public class NaverGeoTestController {


  private static String accessKey;
  private static String secretKey;
  private static CloseableHttpClient httpClient;

  @Before
  public void setup() {
    accessKey = "r05WGe1LhNWJOaEfjniC";
    secretKey = "RC7KYjsn8dZ10aZrpuINd1HQa1N4QMj7SUxahyYs";

    final int timeout = 5000;
    final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
    httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
  }


  @Test
  public void 런테스트() throws Exception {

    String accessKey = "r05WGe1LhNWJOaEfjniC";
    String secretKey = "RC7KYjsn8dZ10aZrpuINd1HQa1N4QMj7SUxahyYs";
    CloseableHttpClient httpClient;

    final int timeout = 5000;
    final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
    httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

    String ip = "110.70.47.172";
    final String requestMethod = "GET";
    final String hostName = "https://geolocation.apigw.gov-ntruss.com";
      final String requestUrl= "/geolocation/v2/geoLocation";

    final Map<String, List<String>> requestParameters = new HashMap<String, List<String>>();
    requestParameters.put("ip", Arrays.asList(ip));
    requestParameters.put("ext", Arrays.asList("t"));
    requestParameters.put("responseFormatType", Arrays.asList("json"));

    SortedMap<String, SortedSet<String>> parameters = convertTypeToSortedMap(requestParameters);

    String timestamp = generateTimestamp();
    System.out.println("timestamp: " + timestamp);

    String baseString = requestUrl + "?" + getRequestQueryString(parameters);
    System.out.println("baseString : " + baseString);

    String signature = makeSignature(requestMethod, baseString, timestamp, accessKey, secretKey);
    System.out.println("signature : " + signature);

    final String requestFullUrl = hostName + baseString;
    final HttpGet request = new HttpGet(requestFullUrl);
    request.setHeader("x-ncp-apigw-timestamp",timestamp);
    request.setHeader("x-ncp-iam-access-key",accessKey);
    request.setHeader("x-ncp-apigw-signature-v2",signature);

    final CloseableHttpResponse response;
    response = httpClient.execute(request);

    final String msg = getResponse(response);
    System.out.println(msg);
  }

  private String getResponse(final CloseableHttpResponse response) throws Exception {
    final StringBuffer buffer = new StringBuffer();
    final BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    String msg;

    try {
      while ((msg = reader.readLine()) != null) {
        buffer.append(msg);
      }
    } catch (final Exception e) {
      throw e;
    } finally {
      response.close();
    }
    return buffer.toString();
  }


  private static SortedMap<String, SortedSet<String>> convertTypeToSortedMap(final Map<String, List<String>> requestParameters) {
    final SortedMap<String, SortedSet<String>> significateParameters = new TreeMap<String, SortedSet<String>>();
    final Iterator<String> parameterNames = requestParameters.keySet().iterator();
    while (parameterNames.hasNext()) {
      final String parameterName = parameterNames.next();
      List<String> parameterValues = requestParameters.get(parameterName);
      if (parameterValues == null) {
        parameterValues = new ArrayList<String>();
      }

      for (String parameterValue : parameterValues) {
        if (parameterValue == null) {
          parameterValue = "";
        }

        SortedSet<String> significantValues = significateParameters.get(parameterName);
        if (significantValues == null) {
          significantValues = new TreeSet<String>();
          significateParameters.put(parameterName, significantValues);
        }
        significantValues.add(parameterValue);
      }

    }
    return significateParameters;
  }

  private static String generateTimestamp() {
    return Long.toString(System.currentTimeMillis());
  }

  private static String getRequestQueryString(final SortedMap<String, SortedSet<String>> significantParameters) {
    final StringBuilder queryString = new StringBuilder();
    final Iterator<Map.Entry<String, SortedSet<String>>> paramIt = significantParameters.entrySet().iterator();
    while (paramIt.hasNext()) {
      final Map.Entry<String, SortedSet<String>> sortedParameter = paramIt.next();
      final Iterator<String> valueIt = sortedParameter.getValue().iterator();
      while (valueIt.hasNext()) {
        final String parameterValue = valueIt.next();

        queryString.append(sortedParameter.getKey()).append('=').append(parameterValue);

        if (paramIt.hasNext() || valueIt.hasNext()) {
          queryString.append('&');
        }
      }
    }
    return queryString.toString();
  }


  public String makeSignature(final String method, final String baseString, final String timestamp, final String accessKey, final String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException{
    String space = " ";                       // one space
    String newLine = "\n";                    // new line

    String message = new StringBuilder()
        .append(method)
        .append(space)
        .append(baseString)
        .append(newLine)
        .append(timestamp)
        .append(newLine)
        .append(accessKey)
        .toString();

    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(signingKey);
    byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
    String encodeBase64String = Base64.encodeBase64String(rawHmac);
    return encodeBase64String;
  }

}
