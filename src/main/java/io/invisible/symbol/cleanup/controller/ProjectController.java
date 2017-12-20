package io.invisible.symbol.cleanup.controller;

import io.invisible.symbol.cleanup.dao.BuildDaoImpl;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ValeriiL
 */
public class ProjectController {

    private static ProjectController instance;

    private final BuildDaoImpl buildDao;

    private ProjectController() {
        buildDao = BuildDaoImpl.getInstance();
    }

    public void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getParameter("delete") != null) {
                deleteAction(request);
            } else {
                defaultAction(request);
            }
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
        }
    }

    public void deleteAction(HttpServletRequest request) {
        String projectName = request.getParameter("name");
        List<String> idsToDelete = new ArrayList<>();

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            if (parameterName.startsWith("build_")) {
                idsToDelete.add(request.getParameter(parameterName));
            }
        }

        buildDao.deleteBuilds(projectName, idsToDelete);

        defaultAction(request);
    }

    public void defaultAction(HttpServletRequest request) {
        String projectName = request.getParameter("name");

        request.setAttribute("builds", buildDao.getBuilds(projectName));
    }

    public static ProjectController getInstance() {
        if (instance == null) {
            instance = new ProjectController();
        }
        return instance;
    }
}
