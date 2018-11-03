package com.huawei.security;

import com.huawei.RestHelper;
import com.huawei.graphbase.RestApi;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class GraphHttpClient {

    public final String csrfToken;
    public final CloseableHttpClient httpClient;
    public final HttpAuthInfo httpAuthInfo;

    private GraphHttpClient(final HttpAuthInfo httpAuthInfo,
                            final CloseableHttpClient httpClient, final String csrfToken) {
        this.httpAuthInfo = httpAuthInfo;
        this.csrfToken = csrfToken;
        this.httpClient = httpClient;
    }

    public void close() throws IOException {
        if (null != this.httpClient) {
            this.httpClient.close();
        }

    }

    public static GraphHttpClient newClient(final HttpAuthInfo httpAuthInfo) throws Exception {
        CloseableHttpClient httpClient = new DefaultHttpClient(createConnManager());

        // step1. cas login
        casLogin(httpAuthInfo, httpClient);

        // step2. base login
        baseLogin(httpAuthInfo, httpClient);

        // step3. fetch CSRF Token
        String token = fetchToken(httpAuthInfo, httpClient);
        if (null == token) {
            return null;
        }

        return new GraphHttpClient(httpAuthInfo, httpClient, token);
    }

    private static String fetchToken(HttpAuthInfo httpAuthInfo, CloseableHttpClient httpClient) throws Exception {
        HttpGet httpGet = new HttpGet(httpAuthInfo.getBaseUrl() + "/graph/user/me");
        CloseableHttpResponse csrfTokenRsp = null;
        String csrfToken = null;

        try {
            csrfTokenRsp = httpClient.execute(httpGet);
            RestHelper.checkHttpRsp(csrfTokenRsp);

            String tokenRspText = EntityUtils.toString(csrfTokenRsp.getEntity());
            JSONObject tokenRspJson = new JSONObject(tokenRspText);
            csrfToken = tokenRspJson.getString("csrfToken").toString().substring(0, tokenRspJson.getString("csrfToken").toString().length());

            if ((null == csrfToken) || (csrfToken.length() <= 0)) {
                System.out.println("TOKEN[4/4]: fetch token fail. " + httpGet.getRequestLine());
                throw new Exception("fetch token fail.");
            } else {
                System.out.println("TOKEN[4/4]: fetch token succeed. " + httpGet.getRequestLine() + " CSRFTOKEN: " + csrfToken + "\n");
            }
        } finally {
            if (null != csrfTokenRsp) {
                try {
                    csrfTokenRsp.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }

        return csrfToken;
    }

    private static boolean baseLogin(HttpAuthInfo httpAuthInfo, CloseableHttpClient httpclient) throws Exception {
        CloseableHttpResponse baseRsp = null;
        CloseableHttpResponse loginRsp = null;
        boolean isSuccessfully = false;

        try {
            baseRsp = httpclient.execute(new HttpGet(httpAuthInfo.getBaseUrl() + "/"));
            RestHelper.checkHttpRsp(baseRsp);

            HttpPost loginPost = new HttpPost(httpAuthInfo.getBaseUrl() + "/login");
            List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
            nvps1.add(new BasicNameValuePair("username", httpAuthInfo.getUsername()));
            loginPost.setEntity(new UrlEncodedFormEntity(nvps1, "UTF-8"));
            loginRsp = httpclient.execute(loginPost);
            RestHelper.checkHttpRsp(loginRsp);

            isSuccessfully = true;
            System.out.println("TOKEN[3/4]: base login succeed. " + loginPost.getRequestLine());
        } finally {
            try {
                if (null != baseRsp) {
                    baseRsp.close();
                }
                if (null != loginRsp) {
                    loginRsp.close();
                }
            } catch (IOException e) {
                // nothing to do
            }
        }
        return isSuccessfully;
    }

    private static boolean casLogin(HttpAuthInfo httpAuthInfo, CloseableHttpClient httpClient) throws Exception {
        CloseableHttpResponse casRsp = null;
        boolean isSuccessfully = false;

        try {
            HttpPost httpPost = new HttpPost(httpAuthInfo.getCasUrl());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("_eventId", "submit"));
            nvps.add(new BasicNameValuePair("submit", "Login"));
            nvps.add(new BasicNameValuePair("username", httpAuthInfo.getUsername()));
            nvps.add(new BasicNameValuePair("password", httpAuthInfo.getPassword()));
            nvps.add(new BasicNameValuePair("lt", getLT(httpAuthInfo, httpClient)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            casRsp = httpClient.execute(httpPost);
            RestHelper.checkHttpRsp(casRsp);

            isSuccessfully = checkLoginSuccessful(casRsp.getEntity().getContent());
            if (isSuccessfully) {
                System.out.println("TOKEN[2/4]: cas login succeed. POST " + httpAuthInfo.getCasUrl());
            } else {
                System.out.println("TOKEN[2/4]: cas login fail. please check username and password.");
                throw new Exception("cas login fail.");
            }
        } finally {
            if (null != casRsp) {
                try {
                    casRsp.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }

        return isSuccessfully;
    }

    private static ThreadSafeClientConnManager createConnManager() throws NoSuchAlgorithmException, KeyManagementException {
        // X509TrustManager
        final X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public final X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public final void checkClientTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {
                // to do nothing.
            }

            @Override
            public final void checkServerTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {
                // to do nothing.
            }

        };

        final javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{trustManager}, null);
        final ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
        connectionManager.setMaxTotal(100);
        connectionManager.getSchemeRegistry().register(new Scheme("https", 443, new SSLSocketFactory(
                context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
        return connectionManager;
    }

    private static String getLT(HttpAuthInfo httpAuthInfo, CloseableHttpClient httpClient) throws Exception {
        CloseableHttpResponse loginRsp = null;
        String LT = "";
        BufferedReader bufferedReader = null;

        try {
            loginRsp = httpClient.execute(new HttpGet(httpAuthInfo.getCasUrl()));
            RestHelper.checkHttpRsp(loginRsp);
            System.out.println("TOKEN[1/4]: open login page succeed. GET " + httpAuthInfo.getCasUrl());

            bufferedReader = new BufferedReader(new InputStreamReader(loginRsp.getEntity().getContent()));
            String lineContent = bufferedReader.readLine();
            while (lineContent != null) {
                if (lineContent.contains("name=\"lt\" value=")) {
                    LT = lineContent.trim().split("\"")[5];
                    break;
                }
                lineContent = bufferedReader.readLine();
            }
        } finally {
            if (null != loginRsp) {
                try {
                    loginRsp.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }

            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException ignore) {
                    // to do nothing.
                }
            }
        }
        return LT;
    }

    public static boolean checkLoginSuccessful(InputStream in) {
        final String LOGIN_SUCCESSFUL_KEYWORD = "Log In Successful";

        if (null == in) {
            return false;
        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String lineContent = bufferedReader.readLine();
            while (lineContent != null) {
                if (lineContent.contains(LOGIN_SUCCESSFUL_KEYWORD)) {
                    return true;
                }
                lineContent = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException ignore) {
                    // to do nothing.
                }
            }
        }
        return false;
    }
}
