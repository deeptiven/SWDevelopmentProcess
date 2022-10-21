package edu.gatech.seclass.crypto6300;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;


public class Database {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqlDb;
    private SharedPreferences sharedPreferences;

    public Database(Context context) {
        databaseHelper = new DatabaseHelper(context);
        //databaseHelper.getWritableDatabase();
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHAREDPREFS, Context.MODE_PRIVATE);
    }

    public void loginAction(String username, String password) {
        sqlDb = databaseHelper.getReadableDatabase();
        SharedPreferences.Editor editor = sharedPreferences.edit();


        Cursor cursor = sqlDb.query(DatabaseHelper.USER_TABLE,
                new String[] {DatabaseHelper.USER_IS_ADMIN},
                DatabaseHelper.USER_NAME + "=? and " + DatabaseHelper.USER_PASSWORD + "=?",
                new String[] {username, password},
                null, null, null);


        if(cursor.getCount() == 0) {
            editor.putBoolean(LoginActivity.LOGINSUCCESS, false);
            editor.putBoolean(LoginActivity.ISADMIN, false);
            editor.putString(LoginActivity.LOGGEDINUSER, null);
            editor.apply();
            return;
        }
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(DatabaseHelper.USER_IS_ADMIN);

        System.out.println(cursor.getInt(index));
        System.out.println("Index is " + index);
        boolean is_admin = cursor.getInt(index) == 1;

        editor.putBoolean(LoginActivity.LOGINSUCCESS, true);
        editor.putBoolean(LoginActivity.ISADMIN, is_admin);
        editor.putString(LoginActivity.LOGGEDINUSER, username);
        editor.apply();

        cursor.close();
        sqlDb.close();
    }

    public boolean signUpAction(String username, String password, String email) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_NAME, username);
        contentValues.put(DatabaseHelper.USER_PASSWORD, password);
        contentValues.put(DatabaseHelper.USER_EMAIL, email);
        contentValues.put(DatabaseHelper.USER_IS_ADMIN, 0);

        sqlDb = databaseHelper.getWritableDatabase();

        long result_user_table = -1;
        long result_player_table = -1;

        try {
            result_user_table = sqlDb.insert(DatabaseHelper.USER_TABLE, null, contentValues);

            if (result_user_table == -1)  {
                return false;
            }

            Cursor cursor = sqlDb.query(DatabaseHelper.USER_TABLE,
                    new String[] {DatabaseHelper.USER_ID},
                    DatabaseHelper.USER_NAME + "=? and " + DatabaseHelper.USER_PASSWORD + "=?",
                    new String[] {username, password},
                    null, null, null);

            cursor.moveToFirst();

            int index = cursor.getColumnIndex(DatabaseHelper.USER_ID);
            int user_id = cursor.getInt(index);

            contentValues.clear();
            contentValues.put(DatabaseHelper.PLAYER_ID, user_id);

            result_player_table = sqlDb.insert(DatabaseHelper.PLAYER_TABLE, null, contentValues);

            cursor.close();

        } catch (Exception e)   {
            e.printStackTrace();
            return false;
        }

        sqlDb.close();

        return result_player_table != -1;
    }

    // return true is the cryptogram title does not exists currently
    public boolean uniqueCryptoTitle(String title){
        sqlDb = databaseHelper.getReadableDatabase();
        boolean uniqueFlag = false;

        Cursor result = sqlDb.rawQuery("SELECT cryptogram_title\n" +
                "FROM cryptogram_table\n" +
                "WHERE cryptogram_title = ?", new String[] {title});

        if (result.getCount() == 0) {
            uniqueFlag = true;
        }

        result.close();
        sqlDb.close();
        return uniqueFlag;
    }

    public Cursor playerScores()    {
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result =  sqlDb.rawQuery("SELECT u.user_name as Username, " +
                "count(DISTINCT pc.cryptogarm_id) as CRYPTOGRAMS_ATTEMPTED, " +
                "p.player_points as Points\n" +
                "FROM user_table u \n" +
                "JOIN player_table p ON u.user_id = p.player_id\n" +
                "LEFT OUTER JOIN player_cryptos_table pc ON p.player_id = pc.player_id\n" +
                "GROUP BY p.player_id\n" +
                "ORDER BY p.player_points DESC", new String[] {});

        return result;
    }

    public int playerPoints(int playerid) {
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result =  sqlDb.rawQuery("SELECT player_points \n" +
                "FROM player_table \n" +
                "WHERE player_id =? ", new String[] {String.valueOf(playerid)});

        result.moveToFirst();
        int player_points = result.getInt(result.getColumnIndex("player_points"));

        result.close();
        sqlDb.close();
        return player_points;
    }

    public Cursor cryptoStats()    {
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result =  sqlDb.rawQuery("SELECT c.cryptogram_title as Title, " +
                "u.user_name as Creator," +
                "count(DISTINCT pc.game_id) as Played_games, " +
                "CAST(sum(pc.is_crypto_solved) as float)/CAST(count(DISTINCT pc.game_id) as float) as Percent_win," +
                "CASE WHEN c.cryptogram_is_enabled = 1 THEN 'No' WHEN c.cryptogram_is_enabled = 0 THEN 'Yes' END as Disabled\n" +
                "FROM cryptogram_table c \n" +
                "JOIN user_table u ON c.cryptogram_created_by = u.user_id\n" +
                "LEFT OUTER JOIN player_cryptos_table pc ON c.cryptogarm_id = pc.cryptogarm_id\n" +
                "GROUP BY c.cryptogarm_id\n" +
                "ORDER BY c.cryptogram_created_on DESC, c.cryptogarm_id DESC", new String[] {});
        return result;
    }

    public int getUserid(String username){
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result = sqlDb.rawQuery("SELECT user_id \n" +
                "FROM user_table \n" +
                "WHERE user_name = ?", new String[] {username});

        result.moveToFirst();
        int userID = result.getInt(result.getColumnIndex("user_id"));

        result.close();
        sqlDb.close();
        return userID;
    }

    public int getCryptoID(String CryptoName){
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result = sqlDb.rawQuery("SELECT cryptogarm_id \n" +
                "FROM cryptogram_table \n" +
                "WHERE cryptogram_title = ?", new String[] {CryptoName});

        result.moveToFirst();
        int cryptoID = result.getInt(result.getColumnIndex("cryptogarm_id"));

        result.close();
        sqlDb.close();
        return cryptoID;
    }

    // return a random cryptogram that is enabled, not created by the playerid, and not attempted by the playerid
    public Cursor randomCrypto(String playerid) {
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result = sqlDb.rawQuery("SELECT cryptogram_title, \n" +
                "cryptogram_solution, cryptogram_hint, cryptogram_encoded_phrase\n" +
                "FROM cryptogram_table c\n" +
                "LEFT OUTER JOIN (SELECT cryptogarm_id FROM player_cryptos_table WHERE player_id =?) pc\n"+
                "ON c.cryptogarm_id = pc.cryptogarm_id \n" +
                "WHERE cryptogram_is_enabled = 1\n" +
                "AND cryptogram_created_by != ? \n" +
                "AND pc.cryptogarm_id IS NULL\n" +
                "ORDER BY RANDOM()\n" +
                "LIMIT 1", new String[] {playerid, playerid});

      return result;
    }

    public boolean disableCryptogram(String cryptoTitle, int points)   {
        sqlDb = databaseHelper.getReadableDatabase();

        try {
            // Get player_id, points and cryptogram_id
            Cursor result = sqlDb.rawQuery("SELECT pt.player_id as Player, pt.player_points as Points\n" +
                            "FROM player_table pt\n" +
                            "JOIN cryptogram_table ct ON pt.player_id = ct.cryptogram_created_by\n" +
                            "WHERE ct.cryptogram_title =? and ct.cryptogram_is_enabled =?",
                    new String[] {cryptoTitle,"1"});



            if (result.getCount() == 0) {
                return false;
            }

            result.moveToFirst();
            long playerId = result.getLong(result.getColumnIndex("Player"));
            int playerPoints = result.getInt(result.getColumnIndex("Points"));

            result.close();

            if (playerPoints <= points) {
                playerPoints = 0;
            } else  {
                playerPoints -= points;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.PLAYER_POINTS, playerPoints);

            sqlDb.close();

            sqlDb = databaseHelper.getWritableDatabase();

            System.out.println("Update player's points - Penalize -- Double check");
            int updateResult = sqlDb.update(DatabaseHelper.PLAYER_TABLE, contentValues,
                    DatabaseHelper.PLAYER_ID + "=?",
                    new String[] {Long.toString(playerId)});
            System.out.println("updateResult1 : "+updateResult);
            if (updateResult == 0)  {
                return false;
            }

            // Disable Cryptogram
            contentValues.clear();
            contentValues.put(DatabaseHelper.CRYPTOGRAM_IS_ENABLED, 0);
            updateResult = sqlDb.update(DatabaseHelper.CRYPTOGRAM_TABLE, contentValues,
                    DatabaseHelper.CRYPTOGRAM_TITLE +"=?",
                    new String[] {cryptoTitle});
            System.out.println("updateResult2 : "+updateResult);

            if (updateResult == 0) {
                return false;
            }

            sqlDb.close();

        } catch (Exception e)   {
            //ToDo: Handle exception.
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public boolean createCryptogramAction(String cryptoTitle, String cryptosolution, String cryptoHint, String encodePhrase, int isEnabled, int createrId, String createDate) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CRYPTOGRAM_TITLE, cryptoTitle);
        contentValues.put(DatabaseHelper.CRYPTOGRAM_SOLUTION, cryptosolution);
        contentValues.put(DatabaseHelper.CRYPTOGRAM_CREATED_BY, createrId);
        contentValues.put(DatabaseHelper.CRYPTOGRAM_HINT, cryptoHint);
        contentValues.put(DatabaseHelper.CRYPTOGRAM_CREATED_ON, createDate);
        contentValues.put(DatabaseHelper.CRYPTOGRAM_IS_ENABLED, isEnabled);
        contentValues.put(DatabaseHelper.CRYPTOGRAM_ENCODED_PHRASE, encodePhrase);

        sqlDb = databaseHelper.getWritableDatabase();

        long result_crypto_table = sqlDb.insert(DatabaseHelper.CRYPTOGRAM_TABLE, null, contentValues);

        System.out.println("insert new cryptogram result : "+result_crypto_table);

        contentValues.clear();
        sqlDb.close();

        return (result_crypto_table != -1);

    }

    public boolean adjustPlayerPoints(String username, String increaseOrDecrease, int points) {
        sqlDb = databaseHelper.getWritableDatabase();

        Cursor result = sqlDb.rawQuery("SELECT p.player_id as Player, p.player_points as Points\n" +
                "FROM player_table p\n" +
                "JOIN user_table u ON p.player_id = u.user_id\n" +
                "WHERE u.user_name =? ", new String[] {username});

        if (result.getCount() == 0) {
            return false;
        }

        result.moveToFirst();
        long playerId = result.getLong(result.getColumnIndex("Player"));
        int playerPoints = result.getInt(result.getColumnIndex("Points"));

        if(increaseOrDecrease.equalsIgnoreCase("increase")) {
            playerPoints = playerPoints + points;
        } else if(increaseOrDecrease.equalsIgnoreCase("decrease")) {
            playerPoints = playerPoints - points;
            if(playerPoints < 0) {
                playerPoints = 0;
            }
        }

        result.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PLAYER_POINTS, playerPoints);
        int updateResult = sqlDb.update(DatabaseHelper.PLAYER_TABLE, contentValues,
                DatabaseHelper.PLAYER_ID + "=?",
                new String[] {Long.toString(playerId)});

        sqlDb.close();

        return (updateResult != -1);

    }

    // Update player_cryptos_table when the player make an attempt on a cryptogram
    // Execute this function every time the player make an attempt
    public boolean playCryptoAction(int playerid, String cryptogramID, int isSolved) {
        sqlDb = databaseHelper.getWritableDatabase();

        if (!(isSolved ==0 || isSolved == 1)){
            System.out.println("isSolved must be 0 or 1 ");
            return false;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.IS_CRYPTO_SOLVED, isSolved);
            contentValues.put(DatabaseHelper.CRYPTOGRAM_ID, cryptogramID);
            contentValues.put(DatabaseHelper.PLAYER_ID, playerid);

            long result_player_crypto_table = sqlDb.insert(DatabaseHelper.PLAYER_CRYPTOS_TABLE, null, contentValues);

            System.out.println("insert new player_crypto result : "+result_player_crypto_table);

            contentValues.clear();
            sqlDb.close();

            return (result_player_crypto_table != -1);
        }
    }

    public Cursor getCryptoDetails(String cryptoTitle) {
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result = sqlDb.rawQuery("SELECT c.cryptogram_encoded_phrase as Encrypted_Phrase," +
                        "c.cryptogram_solution as Solution_Phrase,\n" +
                        "c.cryptogram_hint as Hint\n" +
                        "FROM cryptogram_table c\n" +
                        "WHERE c.cryptogram_title =?",
                new String[]{cryptoTitle});

        result.moveToFirst();

        return result;

    }

    public boolean isCryptoDisabled(String cryptoTitle) {
        sqlDb = databaseHelper.getReadableDatabase();

        Cursor result = sqlDb.rawQuery("SELECT c.cryptogram_is_enabled as isEnabled\n" +
                        "FROM cryptogram_table c\n" +
                        "WHERE c.cryptogram_title =?",
                new String[]{cryptoTitle});

        result.moveToFirst();
        int res = result.getInt(result.getColumnIndex("isEnabled"));
        result.close();
        sqlDb.close();
        if(res == 0)  return true;
        else return false;
    }

}
