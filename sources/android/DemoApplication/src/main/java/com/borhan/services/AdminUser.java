package com.borhan.services;

//<editor-fold defaultstate="collapsed" desc="comment">
import android.os.Handler;
import android.util.Log;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.BorhanConfiguration;
import com.borhan.client.services.BorhanAdminUserService;
//</editor-fold>

/**
 * Manage details for the administrative user
 *
 */
public class AdminUser {

    private static BorhanClient client;
    private static boolean userIsLogin;
    /**
     * Contains the session if the user has successfully logged
     */
    public static String ks;
    /**
     * 
     * api host
     */
    public static String host;
    
    public static String cdnHost;

    /**
     *
     */
    public static BorhanClient getClient() {
        return client;
    }

    /**
     */
    public static boolean userIsLogin() {
        return userIsLogin;
    }

    /**
     * Get an admin session using admin email and password (Used for login to
     * the BMC application)
     *
     * @param TAG constant in your class
     * @param email
     * @param password
     *
     * @throws BorhanApiException
     */
    public static void login(final String TAG, final String email, final String password, final LoginTaskListener loginTaskListener) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    // set a new configuration object
                    BorhanConfiguration config = new BorhanConfiguration();
                    config.setTimeout(10000);
                    config.setEndpoint(host);

                    client = new BorhanClient(config);

                    BorhanAdminUserService userService = new BorhanAdminUserService(client);
                    ks = userService.login(email, password);
                    Log.w(TAG, ks);
                    // set the borhan client to use the recieved ks as default for all future operations
                    client.setSessionId(ks);
                    userIsLogin = true;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            loginTaskListener.onLoginSuccess();
                        }
                    });
                } catch (final BorhanApiException e) {
                    e.printStackTrace();
                    Log.w(TAG, "Login error: " + e.getMessage() + " error code: " + e.code);
                    userIsLogin = false;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            loginTaskListener.onLoginError(e.getMessage());
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    public interface LoginTaskListener {

        void onLoginSuccess();

        void onLoginError(String errorMessage);
    }
}
