package Main;

import Models.User;

public class Context {

    private static User _currentuser;
    private static MyDatabase _database;

    private Context() {}
    public static User get_currentuser() {
        return _currentuser;
    }

    public static void set_currentuser(User _currentuser) {
        Context._currentuser = _currentuser;
    }

    public static MyDatabase get_database() {
        if(_database == null){
            _database = new MyDatabase();
            _database.initialize();
        }
        return _database;
    }

    public static void initialize() {
        _currentuser = null;
        initializeDatabase();
    }

    private static void initializeDatabase() {
        if(_database == null){
            _database = new MyDatabase();
            _database.initialize();
        }
    }

    public static void clearContext() {
        _currentuser = null;
        _database.disconnectToMongoDB();
        _database = null;
    }
}
