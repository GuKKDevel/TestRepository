import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DirDB {

    // ---Enums---------------------------------------------------
    enum Dbms {
        JAVADB, ACCESS95, HSQLDB
    }

    // ---Pseudo constants----------------------------------------
    static String FILESEP = System.getProperty("file.separator");

    // ---Static Variables----------------------------------------
    static Dbms db = Dbms.JAVADB;
    static Connection con;
    static Statement stmt;
    static Statement stmt1;
    static DatabaseMetaData dmd;
    static int nextdid = 1;
    static int nextfid = 1;

    // ---main-------------------------------------------------
    public static void main(String[] args) {
        if (args.length < 1)
        {
            System.out.println("usage: java DirDB [A|J|H] <command> [<options>]");
            System.out.println("");
            System.out.println("command      options");
            System.out.println("---------------------------------");
            System.out.println("POPULATE     <directory>");
            System.out.println("COUNT");
            System.out.println("FINDFILE     <name>");
            System.out.println("FINDDIR      <name>");
            System.out.println("BIGGESTFILES <howmany>");
            System.out.println("CLUSTERING   <clustersize>");
            System.exit(1);
        }
        if (args[0].equalsIgnoreCase("A"))
        {
            db = Dbms.ACCESS95;
        }
        else
            if (args[0].equalsIgnoreCase("H"))
            {
                db = Dbms.HSQLDB;
            }
        try
        {
            if (args[1].equalsIgnoreCase("populate"))
            {
                open();
                System.out.println("nach open");
                createTables();
                System.out.println("nach create");
                populate(args[2]);
                close();
            }
            else
                if (args[1].equalsIgnoreCase("count"))
                {
                    open();
                    countRecords();
                    close();
                }
                else
                    if (args[1].equalsIgnoreCase("findfile"))
                    {
                        open();
                        findFile(args[2]);
                        close();
                    }
                    else
                        if (args[1].equalsIgnoreCase("finddir"))
                        {
                            open();
                            findDir(args[2]);
                            close();
                        }
                        else
                            if (args[1].equalsIgnoreCase("biggestfiles"))
                            {
                                open();
                                biggestFiles(Integer.parseInt(args[2]));
                                close();
                            }
                            else
                                if (args[1].equalsIgnoreCase("clustering"))
                                {
                                    open();
                                    clustering(Integer.parseInt(args[2]));
                                    close();
                                }
        }
        catch (SQLException e)
        {
            while (e != null)
            {
                System.err.println(e.toString());
                System.err.println("SQL-State: " + e.getSQLState());
                System.err.println("ErrorCode: " + e.getErrorCode());
                e = e.getNextException();
            }
            System.exit(1);
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    private static void clustering(int parseInt) {
        // TODO Auto-generated method stub

    }

    private static void biggestFiles(int parseInt) {
        // TODO Auto-generated method stub

    }

    /**
     * Öffnet die Datenbank.
     */
    public static void open() throws Exception {
        // Treiber laden und Connection erzeugen
        if (db == Dbms.JAVADB)
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            con = DriverManager.getConnection("jdbc:derby:dirdb;create=true");
        }
        else
            if (db == Dbms.HSQLDB)
            {
                Class.forName("org.hsqldb.jdbcDriver");
                con = DriverManager.getConnection("jdbc:hsqldb:hsqldbtest", "SA", "");
            }
            else
            {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                con = DriverManager.getConnection("jdbc:odbc:DirDB");
            }
        // Metadaten ausgeben
        dmd = con.getMetaData();
        System.out.println("");
        System.out.println("Connection URL: " + dmd.getURL());
        System.out.println("Driver Name:    " + dmd.getDriverName());
        System.out.println("Driver Version: " + dmd.getDriverVersion());
        System.out.println("");
        // Statementobjekte erzeugen
        stmt = con.createStatement();
        stmt1 = con.createStatement();
    }

    /**
     * Schließt die Datenbank.
     */
    public static void close() throws SQLException {
        stmt.close();
        stmt1.close();
        con.close();
    }

    /**
     * Legt die Tabellen an.
     */
    public static void createTables() throws SQLException {
        // Anlegen der Tabelle dir
        try
        {
            stmt.executeUpdate("DROP TABLE dir");
        }
        catch (SQLException e)
        {
            // Nichts zu tun
        }
        stmt.executeUpdate("CREATE TABLE dir (" + "did       INT," + "dname     CHAR(100),"
                + "fatherdid INT," + "entries   INT)");
        stmt.executeUpdate("CREATE INDEX idir1 ON dir ( did )");
        stmt.executeUpdate("CREATE INDEX idir2 ON dir ( fatherdid )");
        // Anlegen der Tabelle file
        try
        {
            stmt.executeUpdate("DROP TABLE file");
        }
        catch (SQLException e)
        {
            // Nichts zu tun
        }
        stmt.executeUpdate("CREATE TABLE file (" + "fid       INT ," + "did       INT,"
                + "fname     CHAR(100)," + "fsize     INT," + "fdate     DATE,"
                + "ftime     CHAR(5))");
        stmt.executeUpdate("CREATE INDEX ifile1 ON file ( fid )");
    }

    /**
     * Durchläuft den Verzeichnisbaum rekursiv und schreibt Verzeichnis- und
     * Dateinamen in die Datenbank.
     */
    public static void populate(String dir) throws Exception {
        addDirectory(0, "", dir);
    }

    /**
     * Fügt das angegebene Verzeichnis und alle Unterverzeichnisse mit allen
     * darin enthaltenen Dateien zur Datenbank hinzu.
     */
    public static void addDirectory(int fatherdid, String parent, String name) throws Exception {
        String dirname = "";
        if (parent.length() > 0)
        {
            dirname = parent;
            if (!parent.endsWith(FILESEP))
            {
                dirname += FILESEP;
            }
        }
        dirname += name;
        System.out.println("processing " + dirname);
        File dir = new File(dirname);
        if (!dir.isDirectory()) { throw new Exception("not a directory: " + dirname); }
        // Verzeichnis anlegen
        int did = nextdid++;
        stmt.executeUpdate("INSERT INTO dir VALUES (" + did + "," + "\'" + name + "\'," + fatherdid
                + "," + "0)");
        // Verzeichniseinträge lesen
        File[] entries = dir.listFiles();
        // Verzeichnis durchlaufen
        for (int i = 0; i < entries.length; ++i)
        {
            if (entries[i].isDirectory())
            {
                addDirectory(did, dirname, entries[i].getName());
            }
            else
            {
                java.util.Date d = new java.util.Date(entries[i].lastModified());
                SimpleDateFormat sdf;
                // Datum
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(d);
                // Zeit
                sdf = new SimpleDateFormat("HH:mm");
                String time = sdf.format(d);
                // Satz anhängen
                stmt.executeUpdate("INSERT INTO file VALUES (" + (nextfid++) + "," + did + ","
                        + "\'" + entries[i].getName() + "\'," + entries[i].length() + "," + "{d \'"
                        + date + "\'}," + "\'" + time + "\')");
                System.out.println("  " + entries[i].getName());
            }
        }
        // Anzahl der Einträge aktualisieren
        stmt.executeUpdate("UPDATE dir SET entries = " + entries.length + "  WHERE did = " + did);
    }

    /**
     * Gibt die Anzahl der Dateien und Verzeichnisse aus.
     */
    public static void countRecords() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT count(*) FROM dir");
        if (!rs.next()) { throw new SQLException("SELECT COUNT(*): no result"); }
        System.out.println("Directories: " + rs.getInt(1));
        rs = stmt.executeQuery("SELECT count(*) FROM file");
        if (!rs.next()) { throw new SQLException("SELECT COUNT(*): no result"); }
        System.out.println("Files: " + rs.getInt(1));
        rs.close();
    }

    /**
     * Gibt eine Liste aller Files auf dem Bildschirm aus, die zu dem
     * angegebenen Dateinamen passen. Darin dürfen die üblichen SQL-Wildcards %
     * und _ enthalten sein.
     */
    public static void findFile(String name) throws SQLException {
        String query = "SELECT * FROM file " + "WHERE fname LIKE \'" + name + "\'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next())
        {
            String path = getDirPath(rs.getInt("did"));
            System.out.println(path + FILESEP + rs.getString("fname").trim());
        }
        rs.close();
    }

    /**
     * Liefert den Pfadnamen zu dem Verzeichnis mit dem angegebenen Schlüssel.
     */
    public static String getDirPath(int did) throws SQLException {
        String ret = "";
        while (true)
        {
            ResultSet rs = stmt1.executeQuery("SELECT * FROM dir WHERE did = " + did);
            if (!rs.next()) { throw new SQLException("no dir record found with did = " + did); }
            ret = rs.getString("dname").trim() + (ret.length() > 0 ? FILESEP + ret : "");
            if ((did = rs.getInt("fatherdid")) == 0)
            {
                break;
            }
        }
        return ret;
    }

    /**
     * Gibt eine Liste aller Verzeichnisse auf dem Bildschirm aus, die zu dem
     * angegebenen Verzeichnisnamen passen. Darin dürfen die üblichen
     * SQL-Wildcards % und _ enthalten sein.
     */
    public static void findDir(String name) throws SQLException {
        String query = "SELECT * FROM dir " + "WHERE dname LIKE \'" + name + "\'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next())
        {
            System.out.println(getDirPath(rs.getInt("did")) + " (" + rs.getInt("entries")
                    + " entries)");
        }
        rs.close();
    }
}