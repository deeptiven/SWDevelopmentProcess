package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CryptogramGame.db";

    public static final String USER_TABLE = "user_table";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_IS_ADMIN = "user_is_admin";

    public static final String PLAYER_TABLE = "player_table";
    public static final String PLAYER_ID = "player_id";
    public static final String PLAYER_POINTS = "player_points";

    public static final String CRYPTOGRAM_TABLE = "cryptogram_table";
    public static final String CRYPTOGRAM_ID = "cryptogarm_id";
    public static final String CRYPTOGRAM_TITLE = "cryptogram_title";
    public static final String CRYPTOGRAM_SOLUTION = "cryptogram_solution";
    public static final String CRYPTOGRAM_CREATED_BY = "cryptogram_created_by";
    public static final String CRYPTOGRAM_HINT = "cryptogram_hint";
    public static final String CRYPTOGRAM_CREATED_ON = "cryptogram_created_on";
    public static final String CRYPTOGRAM_IS_ENABLED = "cryptogram_is_enabled";
    public static final String CRYPTOGRAM_ENCODED_PHRASE = "cryptogram_encoded_phrase";

    public static final String PLAYER_CRYPTOS_TABLE = "player_cryptos_table";
    public static final String IS_CRYPTO_SOLVED = "is_crypto_solved";
    public static final String GAME_ID = "game_id";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*Query to create table for user. User Id is the primary key and it is autogenerated.*/
        db.execSQL("CREATE TABLE " + USER_TABLE +
                "("+USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+USER_NAME+" TEXT UNIQUE, " +
                ""+USER_PASSWORD+" TEXT, " +
                ""+USER_EMAIL+" TEXT, " +
                ""+USER_IS_ADMIN+" INTEGER)");  // This is boolean 1 - admin, 0 - player


        /*Query to insert admin values into the user_table.*/
        db.execSQL("INSERT INTO " + USER_TABLE + " VALUES (0, 'admin', 'admin', 'admin@sdp.com', 1);");

        /* Query to pre-opulate test data */
        db.execSQL("INSERT INTO " + USER_TABLE + " VALUES (1, 'deepti', 'deepti' , 'deepti@gmail.com', 0);");
        db.execSQL("INSERT INTO " + USER_TABLE + " VALUES (2, 'ajay', 'ajay', 'ajay@gmail.com', 0);");
        db.execSQL("INSERT INTO " + USER_TABLE + " VALUES (3, 'david', 'david' , 'david@gmail.com', 0);");

        /*Query to create player_table. It contains the player_id which refers to user_id of the user_table and the player points. */
        db.execSQL("CREATE TABLE " + PLAYER_TABLE +
                "("+PLAYER_POINTS+" INTEGER DEFAULT 20," +
                ""+PLAYER_ID+" INTEGER PRIMARY KEY, FOREIGN KEY("+PLAYER_ID+") " +
                "REFERENCES "+USER_TABLE+"("+USER_ID+"))");

        /* Queries to pre-populate test data */
        db.execSQL("INSERT INTO " + PLAYER_TABLE + " VALUES (20, 1);");
        db.execSQL("INSERT INTO " + PLAYER_TABLE + " VALUES (60, 2);");
        db.execSQL("INSERT INTO " + PLAYER_TABLE + " VALUES (40, 3);");

        /*Query to create the cryptogram_table. It contains autogenerated cryptogram_id which is the primary key.
        The cryptogram_created_by column contains the player_id of the user who created the cryptogram and hence it
        has reference to the player_id column of the player_table. */
        db.execSQL("CREATE TABLE " + CRYPTOGRAM_TABLE +
                "("+CRYPTOGRAM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+CRYPTOGRAM_TITLE+" TEXT UNIQUE, " +
                ""+CRYPTOGRAM_SOLUTION+" TEXT, " +
                ""+CRYPTOGRAM_CREATED_BY+" INTEGER, " +
                ""+CRYPTOGRAM_HINT+" TEXT, " +
                ""+CRYPTOGRAM_CREATED_ON+" TEXT, " +
                ""+CRYPTOGRAM_IS_ENABLED+" INTEGER, " +
                ""+CRYPTOGRAM_ENCODED_PHRASE+" TEXT, " +
                "FOREIGN KEY("+CRYPTOGRAM_CREATED_BY+") REFERENCES "+PLAYER_TABLE+"("+PLAYER_ID+"))");

        /* Query to pre-opulate test data */
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (1, 'firstCrypto', 'abc1', 1, 'hint1', '04-07-2019', 1, 'def1');");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (2, 'secondCrypto', 'abc2', 2, 'hint2', '04-07-2019', 0, 'def2')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (3, 'thirdCrypto', 'abc3', 1, 'hint3', '04-07-2019', 1,'def3')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (4, 'fourthCrypto', 'abc4', 2, 'hint4', '07-04-2019', 1, 'def4')");

        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (5, 'one', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (6, 'two', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (7, 'three', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (8, 'four', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (9, 'five', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (10, 'six', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (11, 'seven', 'abc', 1, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (12, 'eight', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (13, 'nine', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");
        db.execSQL("INSERT INTO " + CRYPTOGRAM_TABLE + " VALUES (14, 'ten', 'abc', 2, 'abc', '07-04-2019', 1, 'def')");




        /*Query to create player_cryptos_table. This contains the player_id and cryptogram_id .
         * game_id is the record for a player finishing a cryptogram game with is_crypto_solved
         * containing a 0 or 1 value to indicate if it was solved successfully. Thus, we can query
         * all attempts by a player on a cryptogram, all cryptograms solved successfully by a player,
         * number of times a cryptogram has been solved successfully by a player, win % etc.
         *
         *  ------------------------------------------------------------
         * |   game_id  | player_id | cryptogram_id | is_crypto_solved |
         *  ------------------------------------------------------------
         * |     1      |     5     |     19        |       0          |
         * -------------------------------------------------------------
         * |     2      |     5     |     10        |       0          |
         * -------------------------------------------------------------
         * |     3      |     5     |     19        |       1          |
         * -------------------------------------------------------------
         * |     4      |     8     |      4        |       1          |
         * -------------------------------------------------------------
         * |     5      |     7     |     19        |       1          |
         * -------------------------------------------------------------
         *
         * Example to calculate win % for cryptogram 19 : We first get the no. of times cryptogram
         * was played using count aggregation on cryptogram_id and then we get anther count aggregation
         * based on the value of is_crypto_solved for the cryptogram_id 19.
         * In the above example, it is 2/3.
         *
         */
        db.execSQL("CREATE TABLE " + PLAYER_CRYPTOS_TABLE +
                "("+IS_CRYPTO_SOLVED+" INTEGER," +  // This is boolean 1 - solved, 0 - attempted.
                ""+GAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+CRYPTOGRAM_ID+" INTEGER," +
                ""+PLAYER_ID+" INTEGER, " +
                "FOREIGN KEY("+CRYPTOGRAM_ID+") REFERENCES "+CRYPTOGRAM_TABLE+"("+CRYPTOGRAM_ID+")," +
                "FOREIGN KEY("+PLAYER_ID+") REFERENCES "+PLAYER_TABLE+"("+PLAYER_ID+"))");

        /* Queries to pre-populate test data */
        db.execSQL("INSERT INTO " + PLAYER_CRYPTOS_TABLE + " VALUES (1, 1, 1, 2)");
        db.execSQL("INSERT INTO " + PLAYER_CRYPTOS_TABLE + " VALUES (0, 2, 2, 2)");
        db.execSQL("INSERT INTO " + PLAYER_CRYPTOS_TABLE + " VALUES (0, 3, 2, 2)");
        db.execSQL("INSERT INTO " + PLAYER_CRYPTOS_TABLE + " VALUES (1, 4, 3, 2)");
        db.execSQL("INSERT INTO " + PLAYER_CRYPTOS_TABLE + " VALUES (1, 5, 1, 3)");
        db.execSQL("INSERT INTO " + PLAYER_CRYPTOS_TABLE + " VALUES (0, 6, 2, 3)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_CRYPTOS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CRYPTOGRAM_TABLE);
        onCreate(db);
    }

    public void disableCryptogram(SQLiteDatabase sqlDb, Integer points, String cryptoTitle){
        /*
        Cursor cryptogram = sqlDb.query(DatabaseHelper.CRYPTOGRAM_TABLE, new String[] {DatabaseHelper.CRYPTOGRAM_TITLE, DatabaseHelper.CRYPTOGRAM_CREATED_BY}, DatabaseHelper.CRYPTOGRAM_TITLE +"=?",new String[] {cryptoTitle}, null, null, null);
        cryptogram.moveToFirst();
        int title = cryptogram.getColumnIndex(DatabaseHelper.CRYPTOGRAM_TITLE);
        int username = cryptogram.getColumnIndex(DatabaseHelper.CRYPTOGRAM_CREATED_BY);
        */
    }
}
