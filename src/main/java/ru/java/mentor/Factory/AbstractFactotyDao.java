package ru.java.mentor.Factory;

import ru.java.mentor.util.ExceptionFromReadMethod;
import ru.java.mentor.util.ReaderProperty;

public class AbstractFactotyDao {
    private static DAO dao;

    public static DAO getDAO() {
        try {
            if (ReaderProperty.readPropery("MethodToConnectDB").equalsIgnoreCase("Hibernate")) {
                dao = new HibernateFacory();


            } else {
                dao = new JDBCFactory();
            }
        } catch (ExceptionFromReadMethod e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return dao;
    }
}