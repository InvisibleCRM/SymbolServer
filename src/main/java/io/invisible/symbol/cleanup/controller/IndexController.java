package io.invisible.symbol.cleanup.controller;

import io.invisible.symbol.cleanup.dao.BuildDaoImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ValeriiL
 */
public class IndexController {

    private static IndexController instance;

    private final BuildDaoImpl buildDao = BuildDaoImpl.getInstance();

    public void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        defaultAction(request);
    }

    public void defaultAction(HttpServletRequest request) {
        request.setAttribute("projects", buildDao.getProjects());
    }

    public static IndexController getInstance() {
        if (instance == null) {
            instance = new IndexController();
        }
        return instance;
    }
}
