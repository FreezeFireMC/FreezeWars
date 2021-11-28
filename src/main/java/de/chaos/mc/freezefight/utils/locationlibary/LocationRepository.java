package de.chaos.mc.freezefight.utils.locationlibary;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import de.chaos.mc.freezefight.utils.daos.DAOManager;
import de.chaos.mc.freezefight.utils.daos.LocationDAO;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationRepository implements LocationInterface {
    public JdbcPooledConnectionSource connectionSource;
    public DAOManager<LocationDAO, String> daoManager;

    public LocationRepository(JdbcPooledConnectionSource jdbcPooledConnectionSource) {
        this.connectionSource = jdbcPooledConnectionSource;
        this.daoManager = new DAOManager<LocationDAO, String>(LocationDAO.class, connectionSource);
    }


    @Override
    public List<String> getAllKeys(String Namespace) {
        List<String> list = new ArrayList<>();
        try {
            List<LocationDAO> locationList = daoManager.getDAO().queryForAll();
            for (LocationDAO locationDAO : locationList) {
                if (locationDAO.getMapName().equalsIgnoreCase(Namespace)) {
                    list.add(locationDAO.getName());
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> getAllMaps() {
        List<String> list = new ArrayList<>();
        try {
            List<LocationDAO> locationList = daoManager.getDAO().queryForAll();
            for (LocationDAO locationDAO : locationList) {
                list.add(locationDAO.getName());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }


    @Override
    public Location addLocation(String Namespace, String Locationname, Location location) {
        LocationDAO locationDAO = LocationDAO.builder()
                .Name(Locationname)
                .mapName(Namespace)
                .World(location.getWorld().getName())
                .x((long) location.getX())
                .y((long) location.getY())
                .z((long) location.getZ())
                .build();
        try {
            daoManager.getDAO().createOrUpdate(locationDAO);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return location;
    }

    @Override
    public Location getLocation(String LocationName) {
        LocationDAO locationDAO = null;
        try {
            locationDAO = daoManager.getDAO().queryForId(LocationName);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        Location location = new Location(Bukkit.getWorld(locationDAO.getWorld()), locationDAO.getX(), locationDAO.getY(), locationDAO.getZ());
        return location;
    }

    @Override
    public Location delLocation(String LocationName) {
        try {
            daoManager.getDAO().deleteById(LocationName);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}