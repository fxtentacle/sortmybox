package controllers;

import java.util.Iterator;
import java.util.List;

import models.User;

import org.mortbay.log.Log;

import play.modules.objectify.Datastore;
import play.mvc.Controller;
import play.mvc.With;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@With(Login.class)
public class Admin extends Controller {

    private static final int SEARCH_MAX_FETCH_SIZE = 20;
    
    public static void userSearch(String query) {
        checkAccess();

        User user = Login.getLoggedInUser();

        boolean ranSearch = false;
        List<User> results = Lists.newArrayList();

        if (query != null) {
            String normalized = query.toLowerCase().trim();
            Log.info("User query: " + query + " Normalized: " + normalized);    
            
            if (!normalized.isEmpty()) {
                ranSearch = true;
                
                Iterator<User> itr = Datastore
                    .query(User.class)
                    .filter("nameLower >=", normalized)
                    .filter("nameLower <=", normalized +"\ufffd")
                    .limit(SEARCH_MAX_FETCH_SIZE)
                    .fetch().iterator();
                while (itr.hasNext()) {
                    results.add(itr.next());
                }
            }
        }

        render(user, query, ranSearch, results);
    }

    private static void checkAccess() {
        User user = Login.getLoggedInUser();
        Preconditions.checkState(user.isAdmin(), "Logged in user is not an admin!");
    }

}