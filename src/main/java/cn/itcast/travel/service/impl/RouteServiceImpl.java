package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import javax.sound.sampled.LineUnavailableException;
import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * Give three params and return PageBean
     * @param cid
     * @param pageSize
     * @param currentPage
     * @return
     */
    @Override
    public PageBean<Route> routePageQuery(int cid, int pageSize, int currentPage,String rname) {
        //find all counts in dao
        int totalCount = routeDao.findAllCount(cid,rname);
        //find all infos in dao
        List<Route> list= routeDao.findCurrentRouteList(currentPage,pageSize,cid,rname);

        //set properties
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setList(list);
        pb.setTotalCount(totalCount);
        pb.setTotalPages((totalCount%pageSize==0)?(totalCount/pageSize):((totalCount/pageSize) +1));

        return pb;
    }

    @Override
    public Route findOne(int rid) {
        //find the basic info in RouteDao
        Route route = routeDao.findOneByRid(rid);

        //find the seller info in SellerDao
        Seller seller = sellerDao.findSellerBySid(route.getSid());
        route.setSeller(seller);

        //find img info info in RouteImgDao
        List<RouteImg> listRouteImg = routeImgDao.findRouteImgByRid(rid);
        route.setRouteImgList(listRouteImg);

        //获取收藏次数
        int favoriteCount = favoriteDao.findFavoriteCountByRid(rid);
        route.setCount(favoriteCount);

        return route;
    }

}
