package io.invisible.symbols.manager.controller;

import io.invisible.symbols.manager.Configuration;
import io.invisible.symbols.manager.dao.BuildDaoImpl;
import io.invisible.symbols.manager.dao.TrashDaoImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ValeriiL
 */
public class IndexController {

    private static IndexController instance;

    private final BuildDaoImpl buildDao;
    private final TrashDaoImpl trashDao;

    private IndexController() {
        buildDao = BuildDaoImpl.getInstance();
        trashDao = TrashDaoImpl.getInstance();
    }

    public void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        try {
            defaultAction(request);
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
        }
    }

    public void defaultAction(HttpServletRequest request) {
        request.setAttribute("projects", buildDao.getProjects());
        request.setAttribute("trashQuota", Configuration.getTrashQuota());
        request.setAttribute("trashSize", trashDao.getTrashSize());
    }

    public static IndexController getInstance() {
        if (instance == null) {
            instance = new IndexController();
        }
        return instance;
    }
}
